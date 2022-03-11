package com.data.tables.entities;

import java.util.*;

public class ExpandableWeatherObject {

    private String stationName;

    private String observationDate;

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

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public boolean containsProperty(String key) {
        return this.propertyMap.containsKey(key);
    }

    public Object getPropertyValue(String key) {
        return containsProperty(key) ? this.propertyMap.get(key) : null;
    }

    public void putProperty(String key, Object value) {
        this.propertyMap.put(key, value);
    }

    public List<String> getPropertyKeys() {
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
        Iterator<String> iterators = this.propertyMap.keySet().iterator();
        while (iterators.hasNext()) {
            list.add(iterators.next());
        }
        return Collections.unmodifiableList(list);
    }

    public Integer getPropertyCount() {
        return this.propertyMap.size();
    }
}
