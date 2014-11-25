package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REDCap survey record returned from remote server
 * Created by sboykin on 11/22/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedcapSurveyRecord {
    protected String recordId;
    protected String eventName;

    public RedcapSurveyRecord() {
    }

    public RedcapSurveyRecord(String recordId, String eventName) {
        this.recordId = recordId;
        this.eventName = eventName;
    }

    @JsonProperty("record")
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("redcap_event_name")
    public String getEventName() {
        return eventName;
    }
}
