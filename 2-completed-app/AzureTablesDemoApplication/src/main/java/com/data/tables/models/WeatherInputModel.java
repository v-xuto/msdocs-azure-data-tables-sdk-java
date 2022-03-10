package com.data.tables.models;

import java.io.Serializable;

public class WeatherInputModel implements Serializable {

    private String StationName;

    private String ObservationDate;

    private String ObservationTime;

    private Double Temperature;

    private Double Humidity;

    private Double Barometer;

    private String WindDirection;

    private Double WindSpeed;

    private Double Precipitation;

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

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double temperature) {
        Temperature = temperature;
    }

    public Double getHumidity() {
        return Humidity;
    }

    public void setHumidity(Double humidity) {
        Humidity = humidity;
    }

    public Double getBarometer() {
        return Barometer;
    }

    public void setBarometer(Double barometer) {
        Barometer = barometer;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public Double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        WindSpeed = windSpeed;
    }

    public Double getPrecipitation() {
        return Precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        Precipitation = precipitation;
    }
}
