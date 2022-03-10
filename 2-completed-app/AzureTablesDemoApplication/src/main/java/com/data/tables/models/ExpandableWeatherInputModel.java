package com.data.tables.models;

public class ExpandableWeatherInputModel {

    private String StationName;

    private String ObservationDate;

    private String ObservationTime;

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getObservationDate() {
        return ObservationDate;
    }

    public void setObservationDate(String observationDate) {
        ObservationDate = observationDate;
    }

    public String getObservationTime() {
        return ObservationTime;
    }

    public void setObservationTime(String observationTime) {
        ObservationTime = observationTime;
    }
}
