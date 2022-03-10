package com.data.tables.controller;

import com.data.tables.common.Constants;
import com.data.tables.entities.ExpandableWeatherObject;
import com.data.tables.entities.UpdateWeatherObject;
import com.data.tables.models.FilterResultsInputModel;
import com.data.tables.models.SampleDataInputModel;
import com.data.tables.models.WeatherDataModel;
import com.data.tables.models.WeatherInputModel;
import com.data.tables.service.ITablesService;
import com.data.tables.untils.WeatherDataUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TablesServiceController  {

    @Autowired
    private ITablesService tablesService;

    private static final String LAYOUT_PAGE = "layouts/_layout";

    private static final String PRIVACY_PAGE = "privacy";

    private static final String INDEX_PAGE = "index";

    private static final String INDEX_PAGE_PART = "index::result-table";

    private static final String FILTER_PAGE = "filter-results";

    private static final String FILTER_PAGE_PART = "filter-results::result-table";

    private static final String ENTITY_TEMPLATE = "entity-update-template";

    private static final String HOME_PAGE_TITLE = "Home page";

    private static final String PRIVACY_PAGE_TITLE = "Privacy Policy";

    private static final String ATTRIBUTE_NAME_TITLE = "title";

    @GetMapping("/")
    public ModelAndView initLayout() {
        ModelAndView modelAndView = new ModelAndView(LAYOUT_PAGE);
        modelAndView.addObject(ATTRIBUTE_NAME_TITLE, HOME_PAGE_TITLE);
        return modelAndView;
    }

    @GetMapping("/initAllRows")
    public ModelAndView initAllRows() {
        ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
        return modelAndView;
    }

    @GetMapping("/getAllRows")
    public String getAllRows() {
        List<WeatherDataModel> entitiesList = tablesService.getAllRows();
        return new JSONObject(new HashMap<String, Object>(){{
            put("entitiesList", entitiesList);
            put("listOfKeys", WeatherDataUtils.getListOfKeys(entitiesList));
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @GetMapping("/initFilteredRows")
    public ModelAndView initFilteredRows() {
        ModelAndView modelAndView = new ModelAndView(FILTER_PAGE);
        modelAndView.addObject(ATTRIBUTE_NAME_TITLE, HOME_PAGE_TITLE);
        return modelAndView;
    }

    @PostMapping("/getFilteredRows")
    public String getFilteredRows(@RequestBody FilterResultsInputModel model) {
        List<String> filters = new ArrayList<String>();

        if (!StringUtils.isEmptyOrWhitespace(model.getPartitionKey())) {
            filters.add(String.format("PartitionKey eq '%s'", model.getPartitionKey()));
        }
        if (!StringUtils.isEmptyOrWhitespace(model.getRowKeyDateStart())
                && !StringUtils.isEmptyOrWhitespace(model.getRowKeyTimeStart())) {
            filters.add(String.format("RowKey ge '%s %s'", model.getRowKeyDateStart(), model.getRowKeyTimeStart()));
        }
        if (!StringUtils.isEmptyOrWhitespace(model.getRowKeyDateEnd())
                && !StringUtils.isEmptyOrWhitespace(model.getRowKeyTimeEnd())) {
            filters.add(String.format("RowKey le '%s %s'", model.getRowKeyDateEnd(), model.getRowKeyTimeEnd()));
        }
        if (model.getMinTemperature() != null) {
            filters.add(String.format("Temperature ge %f", model.getMinTemperature()));
        }
        if (model.getMaxTemperature() != null) {
            filters.add(String.format("Temperature le %f", model.getMaxTemperature()));
        }
        if (model.getMinPrecipitation() != null) {
            filters.add(String.format("Precipitation ge %f", model.getMinPrecipitation()));
        }
        if (model.getMaxPrecipitation() != null) {
            filters.add(String.format("Precipitation le %f", model.getMaxPrecipitation()));
        }

        String filter = String.join(" and ", filters);
        List<WeatherDataModel> entitiesList = tablesService.getEntitiesByFilter(filter);
        return new JSONObject(new HashMap<String, Object>(){{
            put("entitiesList", entitiesList);
            put("listOfKeys", WeatherDataUtils.getListOfKeys(entitiesList));
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/removeEntity")
    public String removeEntity(@RequestBody WeatherInputModel weatherInputModel) {
        tablesService.removeEntity(weatherInputModel);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping(value = "/insertTableEntity")
    public String insertTableEntity(@RequestBody WeatherInputModel weatherInputModel) {
        tablesService.insertTableEntity(weatherInputModel);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/upsertTableEntity")
    public String upsertTableEntity(@RequestBody WeatherInputModel weatherInputModel) {
        tablesService.upsertTableEntity(weatherInputModel);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/updateEntity")
    public String updateEntity(@RequestBody UpdateWeatherObject updateWeatherObject) {
        tablesService.updateEntity(updateWeatherObject);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/insertExpandableData")
    public String insertExpandableData(@RequestBody ExpandableWeatherObject expandableWeatherObject) {
        tablesService.insertExpandableData(expandableWeatherObject);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/upsertExpandableData")
    public String upsertExpandableData(@RequestBody ExpandableWeatherObject expandableWeatherObject) {
        tablesService.upsertExpandableData(expandableWeatherObject);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @PostMapping("/insertSampleData")
    public String insertSampleData(@RequestBody SampleDataInputModel sampleDataInputModel) {
        tablesService.insertSampleData(sampleDataInputModel);
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.SUCCESS_CODE);
            put("msg", Constants.SUCCESS_MSG);
        }}).toString();
    }

    @GetMapping("/showPrivacy")
    public ModelAndView showPrivacy() {
        ModelAndView modelAndView = new ModelAndView(PRIVACY_PAGE);
        modelAndView.addObject(ATTRIBUTE_NAME_TITLE, PRIVACY_PAGE_TITLE);
        return modelAndView;
    }

}
