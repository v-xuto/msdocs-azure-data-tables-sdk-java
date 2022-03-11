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

    @GetMapping("/")
    public ModelAndView initLayout() {
        return null;
    }

    @GetMapping("/initAllRows")
    public ModelAndView initAllRows() {
        return null;
    }

    @GetMapping("/getAllRows")
    public String getAllRows() {
        return null;
    }

    @GetMapping("/initFilteredRows")
    public ModelAndView initFilteredRows() {
        return null;
    }

    @PostMapping("/getFilteredRows")
    public String getFilteredRows(@RequestBody FilterResultsInputModel model) {
        return null;
    }

    @PostMapping("/removeEntity")
    public String removeEntity(@RequestBody WeatherInputModel weatherInputModel) {
        return null;
    }

    @PostMapping(value = "/insertTableEntity")
    public String insertTableEntity(@RequestBody WeatherInputModel weatherInputModel) {
        return null;
    }

    @PostMapping("/upsertTableEntity")
    public String upsertTableEntity(@RequestBody WeatherInputModel weatherInputModel) {
        return null;
    }

    @PostMapping("/updateEntity")
    public String updateEntity(@RequestBody UpdateWeatherObject updateWeatherObject) {
        return null;
    }

    @PostMapping("/insertExpandableData")
    public String insertExpandableData(@RequestBody ExpandableWeatherObject expandableWeatherObject) {
        return null;
    }

    @PostMapping("/upsertExpandableData")
    public String upsertExpandableData(@RequestBody ExpandableWeatherObject expandableWeatherObject) {
        return null;
    }

    @PostMapping("/insertSampleData")
    public String insertSampleData(@RequestBody SampleDataInputModel sampleDataInputModel) {
        return null;
    }

    @GetMapping("/showPrivacy")
    public ModelAndView showPrivacy() {
        return null;
    }

}
