    var windDirectionArray = ["CALM","N","NE","NW","E","W","SW","SE","S","VARIABLE"];
    var tableEntityBaseItem1 = ["temperature","humidity","barometer","windSpeed","precipitation"];
    var tableEntityBaseItem2 = ["Temperature","Humidity","Barometer","WindSpeed","Precipitation"];

    function initAllRows() {
        $('#main-context').load('./initAllRows');
        $(document).attr('title', "Home page - Azure Tables Demo Application");
    };

    function initFilter() {
        $('#main-context').load('./initFilteredRows');
        $(document).attr('title', "Home page - Azure Tables Demo Application");
    };

    function findByFilter() {
        var validated = true;

        $("label[id$='Filter-error']").each(function(index, item) {
            item.remove();
        });

        $("input[id$='Filter']").each(function(index, item){
            if (item.id != 'partitionKeyFilter' && item.id != 'rowKeyDateStartFilter'
                && item.id != 'rowKeyDateEndFilter' && itemId != 'rowKeyTimeStartFilter'
                && item.id != 'rowKeyTimeEndFilter') {
                var itemId = "#" + item.id;
                if ($(itemId).val() != "" ) {
                    var isNumeric = $.isNumeric($(itemId).val());
                    if (isNumeric == false) {
                        validated = false;
                        $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                    }
                }
            }
        });

        if (!validated) {
            return false;
        }

        var data = "{";
        if ($('#partitionKeyFilter').val() != "") {
            data = data + "\"partitionKey\":\"" + $('#partitionKeyFilter').val()  + "\",";
        } else {
            data = data + "\"partitionKey\":" + null  + ",";
        }

        if ($('#rowKeyDateStartFilter').val() != "") {
            data = data + "\"rowKeyDateStart\":\"" + $('#rowKeyDateStartFilter').val()  + "\",";
        } else {
            data = data + "\"rowKeyDateStart\":" + null  + ",";
        }

        if ($('#rowKeyTimeStartFilter').val() != "") {
            data = data + "\"rowKeyTimeStart\":\"" + $('#rowKeyTimeStartFilter').val()  + "\",";
        } else {
            data = data + "\"rowKeyTimeStart\":" + null  + ",";
        }

        if ($('#rowKeyDateEndFilter').val() != "") {
            data = data + "\"rowKeyDateEnd\":\"" + $('#rowKeyDateEndFilter').val()  + "\",";
        } else {
            data = data + "\"rowKeyDateEnd\":" + null  + ",";
        }


        if ($('#rowKeyTimeEndFilter').val() != "") {
            data = data + "\"rowKeyTimeEnd\":\"" + $('#rowKeyTimeEndFilter').val()  + "\",";
        } else {
            data = data + "\"rowKeyTimeEnd\":" + null  + ",";
        }

        if ($('#minTemperatureFilter').val() != "") {
            data = data + "\"minTemperature\":" + $('#minTemperatureFilter').val() + ",";
        } else {
            data = data + "\"minTemperature\":" + null  + ",";
        }

        if ($('#maxTemperatureFilter').val() != "") {
            data = data + "\"maxTemperature\":" + $('#maxTemperatureFilter').val() + ",";
        } else {
            data = data + "\"maxTemperature\":" + null  + ",";
        }

        if ($('#minPrecipitationFilter').val() != "") {
            data = data + "\"minPrecipitation\":" + $('#minPrecipitationFilter').val() + ",";
        } else {
            data = data + "\"minPrecipitation\":" + null  + ",";
        }

        if ($('#maxPrecipitationFilter').val() != "") {
            data = data + "\"maxPrecipitation\":" + $('#maxPrecipitationFilter').val() + ",";
        } else {
            data = data + "\"maxPrecipitation\":" + null  + ",";
        }

        data = data.substr(0, data.length -1) + "}";

        $.ajax({
            url: "./getFilteredRows",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success: function(data){
                if (data.code == 0) {
                    $('filterErrorArea').remove();
                    var html = "<thead><tr><th>Station Name</th><th>Observation Date</th>";
                    $.each(data.listOfKeys, function(index, key) {
                        html = html + "<th>" + key + "</th>"
                    });
                    html = html + "</tr></thead><tbody>";
                    $.each(data.entitiesList, function(index, item){
                        html = html + "<tr><td>" + item.stationName + "</td>" +
                            "<td>" + item.observationDate + "</td>";
                        $.each(data.listOfKeys, function(index, key) {
                            html = html + "<td>" + item.propertyMap[key] + "</td>"
                        });
                    });
                    html = html + "</tr></tbody>";
                    $('#result-table').html(html).show();
                } else {
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"filterErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#filterArea').before(errorHtml);
                }
            }
        });

    };

    function getAllRows() {
        $('indexErrorArea').remove();
        $.ajax({
            url: "./getAllRows",
            type: "get",
            dataType:"json",
            success: function(data) {
                if (data.code == 0) {
                    var html = "<thead><tr><th>Station Name</th><th>Observation Date</th>";
                    $.each(data.listOfKeys, function(keyIndex, key) {
                        html = html + "<th>" + key + "</th>"
                    });
                    html = html + "<th class=\"text-end\">Action</th></tr></thead>";
                    html = html + "<tbody>";
                    $.each(data.entitiesList, function(entityIndex, item){
                        html = html + "<tr><td>" + item.stationName + "</td><td>" + item.observationDate + "</td>";
                        $.each(data.listOfKeys, function(keyIndex, key) {
                            html = html + "<td>" + item.propertyMap[key] + "</td>"
                        });
                        html = html + "<td class=\"text-end\">";
                        html = html + "<button id=\"updateEntityButton_" + entityIndex + "\" class=\"btn btn-primary btn-sm entity-update\" data-bs-toggle=\"modal\" data-bs-target=\"#updateEntityModel\">Update</button>";
                        html = html + "<button id=\"removeEntityButton_" + entityIndex + "\" class=\"btn btn-danger btn-sm\">Delete</button>";
                        html = html + "</tr>";
                    });
                    html = html + "</tbody>"
                    $('#result-table').html(html).show();
                    $("button[id^='updateEntityButton_']").each(function(index, item){
                        var buttonId = "#" + item.id;
                        $(buttonId).on('click', function(){
                            showUpdateEntityModel(data.entitiesList[index], data.listOfKeys);
                        });
                    });
                    $("button[id^='removeEntityButton_']").each(function(index, item){
                        var buttonId = "#" + item.id;
                        $(buttonId).on('click', function(){
                            removeEntity(data.entitiesList[index]);
                        });
                    });
                } else {
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"indexErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#buttonArea').before(errorHtml);
                }
            }
        });
    };

    function insertTableEntity() {
        var validated = true;

        $("label[id$='TableEntity-error']").each(function(index, item) {
            item.remove();
        });

        $("input[id$='TableEntity']").each(function(index, item){
            var itemId = "#" + item.id;
            if (item.id == 'stationNameTableEntity'
                || item.id =='observationDateTableEntity'
                || item.id == 'observationTimeTableEntity') {
                if ($(itemId).val() == "") {
                    validated = false;
                    $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">This field is required.</label>")
                }
            } else {
                if ($(itemId).val() != "" ) {
                    var isNumeric = $.isNumeric($(itemId).val());
                    if (isNumeric == false) {
                        validated = false;
                        $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                    }
                }
            }
        });

        if (!validated) {
            return false;
        }

        var data = "{" +
            "\"stationName\":\"" + $('#stationNameTableEntity').val() + "\"," +
            "\"observationDate\":\"" + $('#observationDateTableEntity').val() + "\"," +
            "\"observationTime\":\"" + $('#observationTimeTableEntity').val() + "\"," +
            "\"temperature\":" + $('#temperatureTableEntity').val() + "," +
            "\"humidity\":" + $('#humidityTableEntity').val() + "," +
            "\"barometer\":" + $('#barometerTableEntity').val() + "," +
            "\"windDirection\":\"" + $('#windDirectionTableEntity').val() + "\"," +
            "\"windSpeed\":" + $('#windSpeedTableEntity').val() + "," +
            "\"precipitation\":" + $('#precipitationTableEntity').val() +
            "}"
        $.ajax({
            url: "./insertTableEntity",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeTableEntity').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"usingTableEntityErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#tableEntityModalBody').before(errorHtml);
                }
            }
        });
    };

    function upsertTableEntity() {
        var validated = true;

        $("input[id$='TableEntity']").each(function(index, item){
            var itemId = "#" + item.id;
            if (item.id == 'stationNameTableEntity'
                || item.id =='observationDateTableEntity'
                || item.id == 'observationTimeTableEntity') {
                if ($(itemId).val() == "") {
                    validated = false;
                    $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">This field is required.</label>")
                }
            } else {
                if ($(itemId).val() != "" ) {
                    var isNumeric = $.isNumeric($(itemId).val());
                    if (isNumeric == false) {
                        validated = false;
                        $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                    }
                }
            }
        });

        if (!validated) {
            return false;
        }

        var data = "{" +
            "\"stationName\":\"" + $('#stationNameTableEntity').val() + "\"," +
            "\"observationDate\":\"" + $('#observationDateTableEntity').val() + "\"," +
            "\"observationTime\":\"" + $('#observationTimeTableEntity').val() + "\"," +
            "\"temperature\":" + $('#temperatureTableEntity').val() + "," +
            "\"humidity\":" + $('#humidityTableEntity').val() + "," +
            "\"barometer\":" + $('#barometerTableEntity').val() + "," +
            "\"windDirection\":\"" + $('#windDirectionTableEntity').val() + "\"," +
            "\"windSpeed\":" + $('#windSpeedTableEntity').val() + "," +
            "\"precipitation\":" + $('#precipitationTableEntity').val() +
            "}"
        $.ajax({
            url: "./upsertTableEntity",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeTableEntity').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"col-sm-12\">" + "<label class=\"error\">" + data.msg + "</label>" + "</div>";
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"usingTableEntityErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#tableEntityModalBody').before(errorHtml);
                }
            }
        });
    };

    function insertExpandableData() {
        var validated = true;

        $("label[id$='Expandable-error']").each(function(index, item) {
            item.remove();
        });

        $("input[id$='Expandable']").each(function(index, item){
            var itemId = "#" + item.id;
            if (item.id == 'stationNameExpandable'
                || item.id == 'observationDateExpandable'
                || item.id == 'observationTimeExpandable') {
                if ($(itemId).val() == "") {
                    validated = false;
                    $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">This field is required.</label>")
                }
            } else {
                $.each(tableEntityBaseItem1, function(baseItemIndex, baseItem){
                    var baseItemId = baseItem + "Expandable";
                    if (item.id == baseItemId) {
                        if ($(itemId).val() != "" ) {
                            var isNumeric = $.isNumeric($(itemId).val());
                            if (isNumeric == false) {
                                validated = false;
                                $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                            }
                        }
                    }
                });
            }
        });

        if (!validated) {
            return false;
        }

        var data = "{" +
            "\"stationName\":\"" + $('#stationNameExpandable').val() + "\"," +
            "\"observationDate\":\"" + $('#observationDateExpandable').val() + " " + $('#observationTimeExpandable').val() + "\"," +
            "\"propertyMap\":" + "{"

        $("input[id$='Expandable']").each(function(index, item){
            if (item.id != 'stationNameExpandable'
                && item.id != 'observationDateExpandable'
                && item.id != 'observationTimeExpandable') {
                data = data + "\"" + item.name + "\":\"" + item.value + "\","
            }
        });
        $("select[id$='Expandable']").each(function(index, item){
            data = data + "\"" + item.name + "\":\"" + item.value + "\""
        });
        data = data + "}" + "}";
        $.ajax({
            url: "./insertExpandableData",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeExpandable').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"col-sm-12\">" + "<label class=\"error\">" + data.msg + "</label>" + "</div>";
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"usingExpandedDataErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#expandedDataModalBody').before(errorHtml);
                }
            }
        });
    };

    function upsertExpandableData() {
        var validated = true;

        $("label[id$='Expandable-error']").each(function(index, item) {
            item.remove();
        });

        $("input[id$='Expandable']").each(function(index, item){
            var itemId = "#" + item.id;
            if (item.id == 'stationNameExpandable'
                || item.id == 'observationDateExpandable'
                || item.id == 'observationTimeExpandable') {
                if ($(itemId).val() == "") {
                    validated = false;
                    $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">This field is required.</label>")
                }
            } else {
                $.each(tableEntityBaseItem1, function(baseItemIndex, baseItem){
                    var baseItemId = baseItem + "Expandable";
                    if (item.id == baseItemId) {
                        if ($(itemId).val() != "" ) {
                            var isNumeric = $.isNumeric($(itemId).val());
                            if (isNumeric == false) {
                                validated = false;
                                $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                            }
                        }
                    }
                });
            }
        });

        if (!validated) {
            return false;
        }

        var data = "{" +
            "\"stationName\":\"" + $('#stationNameExpandable').val() + "\"," +
            "\"observationDate\":\"" + $('#observationDateExpandable').val() + " " + $('#observationTimeExpandable').val() + "\"," +
            "\"propertyMap\":" + "{"

        $("input[id$='Expandable']").each(function(index, item){
            if (item.id != 'stationNameExpandable'
                && item.id != 'observationDateExpandable'
                && item.id != 'observationTimeExpandable') {
                data = data + "\"" + item.name + "\":\"" + item.value + "\","
            }
        });
        $("select[id$='Expandable']").each(function(index, item){
            data = data + "\"" + item.name + "\":\"" + item.value + "\""
        });
        data = data + "}" + "}";
        $.ajax({
            url: "./upsertExpandableData",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeExpandable').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"col-sm-12\">" + "<label class=\"error\">" + data.msg + "</label>" + "</div>";
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"usingExpandedDataErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#expandedDataModalBody').before(errorHtml);
                }
            }
        });
    };

    function insertSampleData() {
        var data = "{" +
             "\"unit\":\"" + $('#bulkDataUnits').val() + "\"," +
             "\"city\":\"" + $('#sampleDataCity').val() + "\""
         + "}"
        $.ajax({
            url: "./insertSampleData",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeSample').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"col-sm-12\">" + "<label class=\"error\">" + data.msg + "</label>" + "</div>";
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"sampleDataErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#sampleDataModalBody').before(errorHtml);
                }
            }
        });
    };

    function removeEntity(tableEntity) {
        $('indexErrorArea').remove();
        var data = "{" +
             "\"stationName\":\"" + tableEntity.stationName + "\"," +
             "\"observationDate\":\"" + tableEntity.observationDate + "\""
         + "}";
        $.ajax({
            url: "./removeEntity",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"indexErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#buttonArea').before(errorHtml);
                }
            }
        });
    };

    function showPrivacy() {
        $('#main-context').load('./showPrivacy');
        $(document).attr('title', "Privacy Policy - Azure Tables Demo Application");
    };

    function showUpdateEntityModel(entity, listOfKeys) {
        var html = "<div class=\"row g-3 mb-2\">" +
            "     <div class=\"col-sm-4\">" +
            "           <label for=\"PartitionKeyUpdate\" class=\"col-form-label\">Partition Key</label>" +
            "     </div>" +
            "     <div class=\"col-sm-8\">" +
            "           <input type=\"text\" id=\"PartitionKeyUpdate\" name=\"stationName\" class=\"form-control\" value=\"" + entity.stationName  + "\" readonly=\"readonly\">" +
            "     </div>" +
            "</div>" +
            "<div class=\"row g-3 mb-2\">" +
            "   <div class=\"col-sm-4\">" +
            "       <label for=\"RowKeyUpdate\" class=\"col-form-label\">Row Key</label>" +
            "   </div>" +
            "   <div class=\"col-sm-8\">" +
            "       <input type=\"text\" id=\"RowKeyUpdate\" name=\"observationDate\" class=\"form-control\" value=\"" + entity.observationDate + "\" readonly=\"readonly\">" +
            "   </div>" +
            "</div>" +
            "<input type=\"hidden\" name=\"etag\" value=" + entity.eTag + " />" +
            "<hr />";
        $.each(listOfKeys, function(index, key) {
            selectedValue = entity.propertyMap[key];
            html = html +
                    "<div class=\"row g-3 mb-2\">" +
                    "   <div class=\"col-sm-4\">" +
                    "       <label for=\"" + key + "Update\" class=\"col-form-label\">" + key + "</label>" +
                    "   </div>";
            if (key == 'WindDirection') {
                var isMatch = false;
                html = html +
                    "   <div class=\"col-sm-8\">" +
                    "       <select id=\"" + key + "Update\" name=\"" + key + "\" class=\"form-control\">";
                $.each(windDirectionArray, function(index, windDirectionArray) {
                    if (!isMatch && windDirectionArray == entity.propertyMap[key]) {
                        html = html + "<option selected>" + windDirectionArray + "</option>"
                        isMatch = true;
                    } else {
                        html = html + "<option>" + windDirectionArray + "</option>"
                    }
                });
                html = html + "</select></div>";
            } else {
                html = html +
                    "   <div class=\"col-sm-8\">" +
                    "       <input type=\"text\" id=\"" + key + "Update\" name=\"" + key + "\" class=\"form-control\" value=\"" + entity.propertyMap[key] + "\">" +
                    "   </div>";
            }
            html = html + "</div>";
        });
        html = html + "</div>";
        $('#updateEntityModalBody').html(html);
    };

    function updateTableEntity() {
        var validated = true;
        $("input[id$='Update']").each(function(index, item){
            $.each(tableEntityBaseItem2, function(baseItemIndex, baseItem){
                var baseItemId = baseItem + "Update";
                if (item.id == baseItemId) {
                    var itemId = "#" + item.id;
                    if ($(itemId).val() != "" ) {
                        var isNumeric = $.isNumeric($(itemId).val());
                        if (isNumeric == false) {
                            validated = false;
                            $(itemId).after("<label id=\"" + item.id + "-error\" class=\"error\" for=\"" + item.id + "\">Please enter a valid number.</label>")
                        }
                    }
                }
            });
        });

        if (!validated) {
            return false;
        }

        var data = "{" +
            "\"stationName\":\"" + $('#PartitionKeyUpdate').val() + "\"," +
            "\"observationDate\":\"" + $('#RowKeyUpdate').val() + "\"," +
            "\"propertyMap\":" + "{"
            $("input[id$='Update']").each(function(index, item){
                if (item.id != 'PartitionKeyUpdate'
                    && item.id != 'RowKeyUpdate') {
                    data = data + "\"" + item.name + "\":\"" + item.value + "\","
                }
            });
            $("select[id$='Update']").each(function(index, item){
                data = data + "\"" + item.name + "\":\"" + item.value + "\""
            });
            data = data + "}" + "}";
        $.ajax({
            url: "./updateEntity",
            type:"post",
            data: data,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            success:function(data){
                if (data.code == 0){
                    $('#closeUpdateEntity').click();
                    getAllRows();
                } else {
                    var errorHtml = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" id=\"updateEntityErrorArea\">" +
                        data.msg + "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button></div>"
                    $('#updateEntityModalBody').before(errorHtml);
                }
            }
        });
    };

    function addCustomField() {
        var fieldValue = $('#customFieldName').val();
        var convertedFieldValue = fieldValue.replace(/\s+/g,"");
        var html = "<div id=\"" + convertedFieldValue + "CustomExpandableDiv\" class=\"row g-3 mb-2\">" +
            "<div class=\"col-sm-4\">" +
                "<label for=\"" + convertedFieldValue + "Expandable\" class=\"col-form-label\">" + fieldValue + "</label>" +
            "</div>" +
            "<div class=\"col-sm-7\">" +
            "   <input type=\"text\" id=\"" + convertedFieldValue + "Expandable\" name=\"" + convertedFieldValue + "\" class=\"form-control\" placeholder=\"" + fieldValue + " value\">" +
            "</div>" +
            "<div class =\"col-sm-1 text-end\">" +
            "   <button id=\"" + convertedFieldValue + "ExpandableRemove\" class=\"btn btn-danger\">X</button>"
            "</div>" +
            "</div>";
        $('#addCustomFieldDiv').before(html);
        $("#" + convertedFieldValue + "ExpandableRemove").on('click', function() {
            $("#" + convertedFieldValue + "CustomExpandableDiv").remove();
        });
        $('#customFieldName').val("");
    };

    function removeErrorLabelAndResetValue(modelType) {
        $("div[id$='ErrorArea']").each(function(index, item){
            if (item.id != 'indexErrorArea' && item.id != 'filterErrorArea') {
                item.remove();
            }
        });
        if (modelType == 'UsingTableEntity') {
            $("label[id$='TableEntity-error']").each(function(index, item){
                item.remove();
            });
            $("input[id$='TableEntity']").each(function(index, item){
                item.value = ""
            });
            $("select[id$='TableEntity']").each(function(index, item){
                item.options[0].selected;
            });
        } else if (modelType == 'UsingExpandedData') {
            $("label[id$='Expandable-error']").each(function(index, item){
                item.remove();
            });
            $("input[id$='Expandable']").each(function(index, item){
                item.value = ""
            });
            $("select[id$='Expandable']").each(function(index, item){
                item.options[0].selected;
            });
            $("div[id$='CustomExpandableDiv']").each(function(index, item){
                item.remove();
            });
        } else if (modelType == 'SampleData') {
            $("select[id$='sampleDataCity']").each(function(index, item){
                item.options[0].selected;
            });
        } else if (modelType == 'UpdateEntity') {
            $("label[id$='Update-error']").each(function(index, item){
                item.remove();
            });
        }
    };

    $(function(){
        $('#applicationLink').on('click', function(){initAllRows();});
        $('#homeLink').on('click', function(){initAllRows();});
        $('#filterLink').on('click', function(){initFilter();});
        $('#privacyLink').attr('href', 'javascript:showPrivacy()');
        initAllRows();
    });