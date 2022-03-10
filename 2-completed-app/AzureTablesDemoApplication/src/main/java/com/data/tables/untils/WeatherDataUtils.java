package com.data.tables.untils;

import com.azure.core.credential.AzureNamedKeyCredential;
import com.azure.data.tables.TableAsyncClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.data.tables.common.Constants;
import com.data.tables.entities.ExpandableWeatherObject;
import com.data.tables.models.WeatherDataModel;
import com.data.tables.models.WeatherInputModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WeatherDataUtils {


    private static String URI;

    private static String KEY;

    private static String ACCOUNT;

    private static String TABLE_NAME;

    @Value("${azure.tables.uri}")
    public void setURIStatic(String uri) {
        WeatherDataUtils.URI = uri;
    }

    @Value("${azure.tables.key}")
    public void setKeyStatic(String key) {
        WeatherDataUtils.KEY = key;
    }

    @Value("${azure.tables.account}")
    public void setAccountStatic(String account) {
        WeatherDataUtils.ACCOUNT = account;
    }


    @Value("${azure.tables.tableName}")
    public void setTableNameStatic(String tableName) {
        WeatherDataUtils.TABLE_NAME = tableName;
    }

    /**
     * Get a table async client
     *
     * @return TableAsyncClient
     */
    public static TableAsyncClient createAsyncClient() {
        return new TableClientBuilder()
                .endpoint(URI)
                .credential(new AzureNamedKeyCredential(ACCOUNT, KEY))
                .tableName(TABLE_NAME)
                .buildAsyncClient();
    }

    /**
     * Format row key of table entity
     *
     * @param observationDate
     * @param observationTime
     * @return String
     */
    public static String formatRowKey(String observationDate, String observationTime) {
        if (StringUtils.isEmptyOrWhitespace(observationTime == null ? null : observationTime.trim())) {
            return String.format("%s", observationDate);
        }
        return String.format("%s %s", observationDate, observationTime);
    }

    /**
     * Create table entity from input data of weather data
     *
     * @param model
     * @return TableEntity
     */
    public static TableEntity createTableEntity(ExpandableWeatherObject model) {
        TableEntity tableEntity = new TableEntity(model.getStationName(),
                formatRowKey(model.getObservationDate(), null));
        rearrangeEntityProperties(tableEntity.getProperties(), model.getPropertyMap());
        return tableEntity;
    }

    /**
     * Create table entity from input data of weather data
     *
     * @param model
     * @return TableEntity
     */
    public static TableEntity createTableEntity(WeatherInputModel model) {
        TableEntity tableEntity = new TableEntity(model.getStationName(),
                formatRowKey(model.getObservationDate(), model.getObservationTime()));
        Map<String, Object> properties = tableEntity.getProperties();
        properties.put("Temperature", model.getTemperature());
        properties.put("Humidity", model.getHumidity());
        properties.put("Barometer", model.getBarometer());
        properties.put("WindDirection", model.getWindDirection());
        properties.put("WindSpeed", model.getWindSpeed());
        properties.put("Precipitation", model.getPrecipitation());
        return tableEntity;
    }

    /**
     * Convert table entity to java bean of weather data
     *
     * @param entity
     * @return WeatherDataModel
     */
    public static WeatherDataModel tableEntityToWeatherDataModel(TableEntity entity) {
        WeatherDataModel observation = new WeatherDataModel(
                entity.getPartitionKey(), entity.getRowKey(),
                entity.getTimestamp(), entity.getETag());
        rearrangeEntityProperties(observation.getPropertyMap(), entity.getProperties());
        return observation;
    }

    /**
     * get entity keys list
     *
     * @param weatherDataModelList
     * @return List<String>
     */
    public static List<String> getListOfKeys(List<WeatherDataModel> weatherDataModelList) {
        List<String> listOfKeys = new ArrayList<>() {{
            addAll(Constants.DEFAULT_LIST_OF_KEYS);
        }};
        weatherDataModelList.forEach(model -> model.getPropertyMap().keySet().forEach(key -> {
            if (listOfKeys.parallelStream().noneMatch(storiedKey -> storiedKey.equals(key))
                    && Constants.EXCLUDE_TABLE_ENTITY_KEYS.parallelStream().noneMatch(storiedKey -> storiedKey.equals(key))) {
                listOfKeys.add(key);
            }
        }));
        return listOfKeys;
    }

    /**
     * filled value of list item
     *
     * @param weatherDataModelList
     * @return List<WeatherDataModel>
     */
    public static List<WeatherDataModel> filledValue(List<WeatherDataModel> weatherDataModelList) {
        List<String> listOfKeys = getListOfKeys(weatherDataModelList);
        weatherDataModelList.parallelStream().forEach(weatherDataModel ->
                listOfKeys.parallelStream().forEach(storedKey -> {
                    if (!weatherDataModel.getPropertyMap().containsKey(storedKey)) {
                        weatherDataModel.getPropertyMap().put(storedKey, Constants.BLANK_STRING);
                    }
                }));
        return weatherDataModelList;
    }

    /**
     * Rearrange and set properties of table entity
     *
     * @param target
     * @param source
     */
    private static void rearrangeEntityProperties(Map<String, Object> target, Map<String, Object> source) {
        Constants.DEFAULT_LIST_OF_KEYS.forEach(key -> {
            if (source.containsKey(key)) {
                target.put(key, source.get(key));
            }
        });
        source.keySet().forEach(key -> {
            if (Constants.DEFAULT_LIST_OF_KEYS.parallelStream().noneMatch(defaultKey -> defaultKey.equals(key))
                    && Constants.EXCLUDE_TABLE_ENTITY_KEYS.parallelStream().noneMatch(defaultKey -> defaultKey.equals(key))) {
                target.put(key, source.get(key));
            }
        });
    }

}
