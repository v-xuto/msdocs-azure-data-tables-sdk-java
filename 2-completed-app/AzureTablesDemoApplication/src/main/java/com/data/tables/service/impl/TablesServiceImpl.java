package com.data.tables.service.impl;

import com.azure.data.tables.TableAsyncClient;
import com.azure.data.tables.models.*;
import com.data.tables.data.SampleWeatherData;
import com.data.tables.entities.ExpandableWeatherObject;
import com.data.tables.entities.UpdateWeatherObject;
import com.data.tables.models.SampleDataInputModel;
import com.data.tables.models.WeatherDataModel;
import com.data.tables.models.WeatherInputModel;
import com.data.tables.service.ITablesService;
import com.data.tables.untils.WeatherDataUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TablesServiceImpl implements ITablesService {
    
    /**
     * Get all weather data entities
     * @return List<WeatherDataModel>
     */
    @Override
    public List<WeatherDataModel> getAllRows() {
        return Collections.unmodifiableList(
                WeatherDataUtils.filledValue(WeatherDataUtils.createAsyncClient().listEntities()
                        .map(entity -> {
                            return WeatherDataUtils.tableEntityToWeatherDataModel(entity);
                        })
                        .collectList()
                        .block()));
    }

    /**
     * Get weather data entities by filters
     * @param filter
     * @return List<WeatherDataModel>
     */
    @Override
    public List<WeatherDataModel> getEntitiesByFilter(String filter) {
        return Collections.unmodifiableList(
                WeatherDataUtils.filledValue(WeatherDataUtils.createAsyncClient().listEntities(new ListEntitiesOptions().setFilter(filter))
                        .map(entity -> {
                            return WeatherDataUtils.tableEntityToWeatherDataModel(entity);
                        }).collectList().block()));
    }

    /**
     * Delete weather data by table key
     * @param model
     */
    @Override
    public void removeEntity(WeatherInputModel model) {
        TableAsyncClient client = WeatherDataUtils.createAsyncClient();
        TableEntity entity = client.getEntity(model.getStationName(),
                WeatherDataUtils.formatRowKey(model.getObservationDate(),
                        model.getObservationTime())).block();
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.DELETE, entity);
        client.submitTransaction(Arrays.asList(action)).block();
    }

    /**
     * Insert a new weather data
     * @param model
     */
    @Override
    public void insertTableEntity(WeatherInputModel model) {
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.CREATE, 
                WeatherDataUtils.createTableEntity(model));
        WeatherDataUtils.createAsyncClient().submitTransaction(Arrays.asList(action)).block();
    }

    @Override
    public void upsertTableEntity(WeatherInputModel model) {
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
                WeatherDataUtils.createTableEntity(model));
        WeatherDataUtils.createAsyncClient().submitTransaction(Arrays.asList(action)).block();
    }

    /**
     * Update the weather data
     * @param model
     */
    @Override
    public void updateEntity(UpdateWeatherObject model) {
        TableEntity tableEntity = WeatherDataUtils.createAsyncClient().getEntity(model.getStationName(),
                model.getObservationDate()).block();
        Map<String, Object> propertiesMap = model.getPropertyMap();
        propertiesMap.keySet().forEach(key -> tableEntity.getProperties().put(key, propertiesMap.get(key)));
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.UPDATE_REPLACE, tableEntity);
        WeatherDataUtils.createAsyncClient().submitTransaction(Arrays.asList(action)).block();
    }

    /**
     * Insert a new expandable weather data
     * @param model
     */
    @Override
    public void insertExpandableData(ExpandableWeatherObject model){
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.CREATE,
                WeatherDataUtils.createTableEntity(model));
        WeatherDataUtils.createAsyncClient().submitTransaction(Arrays.asList(action)).block();
    }

    /**
     * Update the expandable weather data
     * @param model
     */
    @Override
    public void upsertExpandableData(ExpandableWeatherObject model){
        TableTransactionAction action = new TableTransactionAction(TableTransactionActionType.UPSERT_REPLACE,
                WeatherDataUtils.createTableEntity(model));
        WeatherDataUtils.createAsyncClient().submitTransaction(Arrays.asList(action)).block();
    }

    /**
     * Insert or update weather data
     * @param model
     */
    @Override
    public void insertSampleData(SampleDataInputModel model) {
        List<WeatherInputModel> sampleList = SampleWeatherData.getSampleData(model.getUnit(), model.getCity());
        List<TableTransactionAction> actions = new ArrayList<TableTransactionAction>();
        sampleList.parallelStream().forEach(sampleData ->
            actions.add(new TableTransactionAction(TableTransactionActionType.UPSERT_REPLACE,
                    WeatherDataUtils.createTableEntity(sampleData))));
        WeatherDataUtils.createAsyncClient().submitTransaction(actions).block();
    }

}
