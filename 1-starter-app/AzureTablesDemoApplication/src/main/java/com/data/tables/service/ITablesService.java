package com.data.tables.service;

import com.data.tables.entities.ExpandableWeatherObject;
import com.data.tables.entities.UpdateWeatherObject;
import com.data.tables.models.SampleDataInputModel;
import com.data.tables.models.WeatherDataModel;
import com.data.tables.models.WeatherInputModel;
import java.util.List;

public interface ITablesService {

    List<WeatherDataModel> getAllRows();

    List<WeatherDataModel> getEntitiesByFilter(String filter);

    void removeEntity(WeatherInputModel model);

    void insertTableEntity(WeatherInputModel model);

    void upsertTableEntity(WeatherInputModel model);

    void updateEntity(UpdateWeatherObject model);

    void insertExpandableData(ExpandableWeatherObject model);

    void upsertExpandableData(ExpandableWeatherObject model);

    void insertSampleData(SampleDataInputModel model);

}
