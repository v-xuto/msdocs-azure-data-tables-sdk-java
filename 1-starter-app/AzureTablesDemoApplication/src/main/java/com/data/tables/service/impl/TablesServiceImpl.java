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
        return null;
    }

    /**
     * Get weather data entities by filters
     * @param filter
     * @return List<WeatherDataModel>
     */
    @Override
    public List<WeatherDataModel> getEntitiesByFilter(String filter) {
        return null;
    }

    /**
     * Delete weather data by table key
     * @param model
     */
    @Override
    public void removeEntity(WeatherInputModel model) {
        return null;
    }

    /**
     * Insert a new weather data
     * @param model
     */
    @Override
    public void insertTableEntity(WeatherInputModel model) {
        return null;
    }

    @Override
    public void upsertTableEntity(WeatherInputModel model) {
        return null;
    }

    /**
     * Update the weather data
     * @param model
     */
    @Override
    public void updateEntity(UpdateWeatherObject model) {
        return null;
    }

    /**
     * Insert a new expandable weather data
     * @param model
     */
    @Override
    public void insertExpandableData(ExpandableWeatherObject model){
        return null;
    }

    /**
     * Update the expandable weather data
     * @param model
     */
    @Override
    public void upsertExpandableData(ExpandableWeatherObject model){
        return null;
    }

    /**
     * Insert or update weather data
     * @param model
     */
    @Override
    public void insertSampleData(SampleDataInputModel model) {
        return null;
    }

}
