package com.data.tables.models;

import java.io.Serializable;

public class FilterResultsInputModel implements Serializable {

    private String partitionKey;

    private String rowKeyDateStart;

    private String rowKeyTimeStart;

    private String rowKeyDateEnd;

    private String rowKeyTimeEnd;

    private Double minTemperature;

    private Double maxTemperature;

    private Double minPrecipitation;

    private Double maxPrecipitation;

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public String getRowKeyDateStart() {
        return rowKeyDateStart;
    }

    public void setRowKeyDateStart(String rowKeyDateStart) {
        this.rowKeyDateStart = rowKeyDateStart;
    }

    public String getRowKeyTimeStart() {
        return rowKeyTimeStart;
    }

    public void setRowKeyTimeStart(String rowKeyTimeStart) {
        this.rowKeyTimeStart = rowKeyTimeStart;
    }

    public String getRowKeyDateEnd() {
        return rowKeyDateEnd;
    }

    public void setRowKeyDateEnd(String rowKeyDateEnd) {
        this.rowKeyDateEnd = rowKeyDateEnd;
    }

    public String getRowKeyTimeEnd() {
        return rowKeyTimeEnd;
    }

    public void setRowKeyTimeEnd(String rowKeyTimeEnd) {
        this.rowKeyTimeEnd = rowKeyTimeEnd;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getMinPrecipitation() {
        return minPrecipitation;
    }

    public void setMinPrecipitation(Double minPrecipitation) {
        this.minPrecipitation = minPrecipitation;
    }

    public Double getMaxPrecipitation() {
        return maxPrecipitation;
    }

    public void setMaxPrecipitation(Double maxPrecipitation) {
        this.maxPrecipitation = maxPrecipitation;
    }
}
