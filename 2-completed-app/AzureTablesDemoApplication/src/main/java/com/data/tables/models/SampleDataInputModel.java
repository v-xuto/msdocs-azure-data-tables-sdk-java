package com.data.tables.models;

import java.io.Serializable;

public class SampleDataInputModel implements Serializable {

    private String unit;

    private String city;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
