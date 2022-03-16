package com.data.tables.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableTransactionAction;
import com.azure.data.tables.models.TableTransactionActionType;
import com.data.tables.data.SampleWeatherData;
import com.data.tables.entities.ExpandableWeatherObject;
import com.data.tables.entities.UpdateWeatherObject;
import com.data.tables.models.FilterResultsInputModel;
import com.data.tables.models.SampleDataInputModel;
import com.data.tables.models.WeatherDataModel;
import com.data.tables.models.WeatherInputModel;
import com.data.tables.service.ITablesService;
import com.data.tables.untils.WeatherDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;


@Service
public class TablesServiceImpl implements ITablesService {

    @Autowired
    private TableClient tableClient;
    /**
     * Get all weather data entities
     * @return List<WeatherDataModel>
     */
    @Override
    public List<WeatherDataModel> getAllRows() {
        List<WeatherDataModel> modelList = new ArrayList<>();
        tableClient.listEntities().stream().forEach(entity -> modelList.add(WeatherDataUtils.mapTableEntityToWeatherDataModel(entity)));
        return Collections.unmodifiableList(WeatherDataUtils.filledValue(modelList));
    }

    /**
     * Get weather data entities by filters
     * @param model
     * @return List<WeatherDataModel>
     */
    @Override
    public List<WeatherDataModel> getFilteredRows(FilterResultsInputModel model) {

        List<String> filters = new ArrayList<>();

        if (!StringUtils.isEmptyOrWhitespace(model.getPartitionKey())) {
            filters.add(String.format("PartitionKey eq '%s'", model.getPartitionKey()));
        }
        if (!StringUtils.isEmptyOrWhitespace(model.getRowKeyDateStart())
                && !StringUtils.isEmptyOrWhitespace(model.getRowKeyTimeStart())) {
            filters.add(String.format("RowKey ge '%s %s'", model.getRowKeyDateStart(), model.getRowKeyTimeStart()));
        }
        if (!StringUtils.isEmptyOrWhitespace(model.getRowKeyDateEnd())
                && !StringUtils.isEmptyOrWhitespace(model.getRowKeyTimeEnd())) {
            filters.add(String.format("RowKey le '%s %s'", model.getRowKeyDateEnd(), model.getRowKeyTimeEnd()));
        }
        if (model.getMinTemperature() != null) {
            filters.add(String.format("Temperature ge %f", model.getMinTemperature()));
        }
        if (model.getMaxTemperature() != null) {
            filters.add(String.format("Temperature le %f", model.getMaxTemperature()));
        }
        if (model.getMinPrecipitation() != null) {
            filters.add(String.format("Precipitation ge %f", model.getMinPrecipitation()));
        }
        if (model.getMaxPrecipitation() != null) {
            filters.add(String.format("Precipitation le %f", model.getMaxPrecipitation()));
        }

        List<WeatherDataModel> modelList = new ArrayList<>();
        tableClient.listEntities(new ListEntitiesOptions().setFilter(String.join(" and ", filters)), null, null)
                .stream().forEach(entity -> modelList.add(WeatherDataUtils.mapTableEntityToWeatherDataModel(entity)));
        return Collections.unmodifiableList(WeatherDataUtils.filledValue(modelList));
    }

    /**
     * Delete weather data by table key
     * @param model
     */
    @Override
    public void removeEntity(WeatherInputModel model) {
        tableClient.deleteEntity(model.getStationName(),
                WeatherDataUtils.formatRowKey(model.getObservationDate(), model.getObservationTime()));
    }

    /**
     * Insert a new weather data
     * @param model
     */
    @Override
    public void insertTableEntity(WeatherInputModel model) {
        tableClient.createEntity(WeatherDataUtils.createTableEntity(model));
    }

    @Override
    public void upsertTableEntity(WeatherInputModel model) {
        tableClient.upsertEntity(WeatherDataUtils.createTableEntity(model));
    }

    /**
     * Update the weather data
     * @param model
     */
    @Override
    public void updateEntity(UpdateWeatherObject model) {
        TableEntity tableEntity = tableClient.getEntity(model.getStationName(), model.getObservationDate());
        Map<String, Object> propertiesMap = model.getPropertyMap();
        propertiesMap.keySet().forEach(key -> tableEntity.getProperties().put(key, propertiesMap.get(key)));
        tableClient.updateEntity(tableEntity);
    }

    /**
     * Insert a new expandable weather data
     * @param model
     */
    @Override
    public void insertExpandableData(ExpandableWeatherObject model){
        tableClient.createEntity(WeatherDataUtils.createTableEntity(model));
    }

    /**
     * Update the expandable weather data
     * @param model
     */
    @Override
    public void upsertExpandableData(ExpandableWeatherObject model){
        tableClient.upsertEntity(WeatherDataUtils.createTableEntity(model));
    }

    /**
     * Insert or update weather data
     * @param model
     */
    @Override
    public void insertSampleData(SampleDataInputModel model) {
        List<WeatherInputModel> sampleList = SampleWeatherData.getSampleData(model.getUnit(), model.getCity());
        List<TableTransactionAction> actions = new ArrayList<>();
        sampleList.parallelStream().forEach(sampleData ->
            actions.add(new TableTransactionAction(TableTransactionActionType.UPSERT_REPLACE,
                    WeatherDataUtils.createTableEntity(sampleData))));
        tableClient.submitTransaction(actions);
    }

}
