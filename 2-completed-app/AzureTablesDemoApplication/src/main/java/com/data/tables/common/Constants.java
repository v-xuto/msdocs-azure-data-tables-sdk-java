package com.data.tables.common;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final Integer FAILED_CODE = -1;

    public static final Integer SUCCESS_CODE = 0;

    public static final String SUCCESS_MSG = "success!";

    public static final String BLANK_STRING = "";

    public static final List<String> EXCLUDE_TABLE_ENTITY_KEYS = new ArrayList<String>(){{
        add("PartitionKey");
        add("RowKey");
        add("odata.etag");
        add("Timestamp");}};

    public static final List<String> DEFAULT_LIST_OF_KEYS = new ArrayList<String>() {{
        add("Temperature");
        add("Humidity");
        add("Barometer");
        add("WindDirection");
        add("WindSpeed");
        add("Precipitation");
    }};
}
