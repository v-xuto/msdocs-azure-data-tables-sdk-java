package com.data.tables.models;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class WeatherDataModel {

    public WeatherDataModel() {}

    public WeatherDataModel(String stationName, String observationDate, OffsetDateTime timestamp, String etag) {
        this.stationName = stationName;
        this.observationDate = observationDate;
        this.timestamp = timestamp;
        this.etag = etag;
    }

    private String stationName;

    private String observationDate;

    private OffsetDateTime timestamp;

    private String etag;

    private Map<String, Object> propertyMap = new HashMap<String, Object>();

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(String observationDate) {
        this.observationDate = observationDate;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
