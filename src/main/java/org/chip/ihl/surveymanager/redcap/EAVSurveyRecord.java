package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RedcapSurveyRecord in EAV format
 * Created by sboykin on 11/19/2014.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class EAVSurveyRecord extends RedcapSurveyRecord {
    protected String fieldName;
    protected String value;

    public EAVSurveyRecord() {
        super();
    }

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

    @Override
    public int compareTo(RedcapSurveyRecord o) {
        int result = super.compareTo(o);
        if (result != 0) {
            return result;
        }
        EAVSurveyRecord esr = (EAVSurveyRecord) o;
        result = nullSafeStringComparator(this.fieldName, esr.fieldName);
        if (result != 0) {
            return result;
        }
        return nullSafeStringComparator(this.value, esr.value);
    }

    @Override
    public boolean isSame(Object obj) {
        return (super.isSame(obj) && compareTo((RedcapSurveyRecord) obj) == 0);
    }
}
