package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RedcapSurveyRecord in EAV format
 * Created by sboykin on 11/19/2014.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class EAVSurveyRecord extends RedcapSurveyRecord {
    private String fieldName;
    private String value;

    public EAVSurveyRecord(String recordId, String fieldName, String eventName, String value) {
        super(recordId, eventName);
        this.fieldName = fieldName;
        this.value = value;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("field_name")
    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

}
