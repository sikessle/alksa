var EAWA = (function () {
    'use strict';

    function EAWA(dataUrl, settingsUrl) {
    	this.dataUrl = dataUrl;
        this.settingsUrl = settingsUrl;
    	this.requestTarget = $('#request textarea');
    	this.resultTable = $('#result table');
		this.attributeCheckboxes = $('#columns input[type="checkbox"]');
		this.filterCheckboxes = $('#filter input[type="checkbox"]');
		this.filterFreetextWrapper = $('#filter .input-group');
        this.queriesLearnedTarget = $('#queries-learned .log');
        this.queriesProductiveTarget = $('#queries-productive .log');
        this.statusBox = $('#status');
		this.sendButton = $('#send');
        this.resetButton = $('#reset');
        this.learningCheckbox = $('#toggle-learn');
        this.alksaCheckbox = $('#toggle-alksa');

		this.attributes = [];
		this.filterAttrs = [];
		this.filterFreetext = [];

        if (typeof String.prototype.startsWith != 'function') {
            String.prototype.startsWith = function (str){
                return this.slice(0, str.length) == str;
            };
        }
    }

    EAWA.prototype.run = function () {
        this.setupListener();
    };

    EAWA.prototype.setupListener = function () {
        var self = this;
        this.attributeCheckboxes.change(this.attributeCheckboxChanged.bind(this));
        this.filterCheckboxes.change(this.filterCheckboxChanged.bind(this));
        this.filterFreetextWrapper.keyup(this.filterFreetextWrapperChanged.bind(this));
        this.sendButton.click(this.send.bind(this));

        this.learningCheckbox.change(function () {
            self.putOrDeleteByCheckbox("learning", this);
        });
        this.alksaCheckbox.change(function () {
            self.putOrDeleteByCheckbox("alksa", this);
        });
        this.resetButton.click(function () {
            $.get(self.settingsUrl + "reset", null, function () {
                document.location.reload();
            });
        });
        
        this.updatePayload();
    };

    EAWA.prototype.putOrDeleteByCheckbox = function (path, checkbox) {
        var httpType = checkbox.checked ? 'PUT' : 'DELETE';
        $.ajax({
            url: this.settingsUrl + path,
            type: httpType
        });             
    };

    EAWA.prototype.send = function () {
    	$.ajax({
    		url: this.dataUrl, 
    		method: 'post',
    		data: this.requestTarget.val(), 
    		success: this.handleResponse.bind(this), 
    		dataType: 'json',
    		contentType: 'application/json'
    	});
    };

    EAWA.prototype.handleResponse = function (data) {

        this.fillQueryLogBox(this.queriesLearnedTarget, data.learnedQueries);
        this.fillQueryLogBox(this.queriesProductiveTarget, data.productiveQueries);

        if (data.accepted) {
            this.statusBox.html("ALKSA: QUERY ACCEPTED");
            this.fillResultTable(data.resultHead, data.resultBody);
        } else {
            this.statusBox.html("ALKSA: QUERY NOT ACCEPTED");
        }
    };

    EAWA.prototype.fillResultTable = function (resultHead, resultBody) {
        var head = this.resultTable.find("thead tr");
        var body  = this.resultTable.find("tbody");
        var tr;

        head.html("");
        body.html("");

        head.append("<th>" + resultHead.join("</th><th>") + "</th>");

        resultBody.forEach(function (row) {
            body.append("<tr><td>"+row.join("</td><td>")+"</td></tr>");
        });
    };

    EAWA.prototype.fillQueryLogBox = function (target, queries) {
        target.html("");

        target.append(queries.map(function (q) {
            if (q.startsWith("ACCEPTED"))
                return '<span class="accepted">' + q.replace(/ACCEPTED:\s*/g, "") + '</span>';
            if (q.startsWith("REJECTED"))
                return '<span class="rejected">' + q.replace(/REJECTED:\s*/g, "") + '</span>';
            if (q.startsWith("ERROR"))
                return '<span class="error">' + q.replace(/ERROR:\s*/g, "") + '</span>';
            return "unknown query: " + q;
        }).join("<br>"));
    };

    EAWA.prototype.filterFreetextWrapperChanged = function () {
    	var freetext = [];

    	this.filterFreetextWrapper.each(function (i, el) {
    		var textfield = $(el).find("input");
    		var column = $(el).find(".input-group-addon").html();

    		if (textfield.val() != "") {
    			freetext.push(column + "'" + textfield.val() + "'");
    		}
    	});

    	this.filterFreetext = freetext;

        this.updatePayload();
    };

    EAWA.prototype.attributeCheckboxChanged = function () {
    	var columns = [];

    	this.collectCheckedCheckboxes(this.attributeCheckboxes, columns);
    	this.attributes = columns;

        this.updatePayload();
    };

    EAWA.prototype.filterCheckboxChanged = function () {
    	var filterConditions = [];

    	this.collectCheckedCheckboxes(this.filterCheckboxes, filterConditions);
    	this.filterAttrs = filterConditions;

        this.updatePayload();
    };

    EAWA.prototype.collectCheckedCheckboxes = function (checkboxes, targetArray) {
    	checkboxes.each(function (i, el) {
    		if (el.checked) {
    			targetArray.push(el.value);
    		}
    	});
    };

    EAWA.prototype.updatePayload = function () {
		var payload = {
            columns: "",
            where: ""
        };

		payload.columns = this.attributes.join(", ");
		payload.where = this.filterAttrs.concat(this.filterFreetext).join(" AND ");

        this.requestTarget.html(JSON.stringify(payload, null, "\t"));
    };

    return EAWA;

}());

$(document).ready(function () {
	var eawa = new EAWA("/countries", "/settings/");
	eawa.run();
}); 
