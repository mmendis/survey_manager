package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * REDCap survey record returned from remote server
 * Created by sboykin on 11/22/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedcapSurveyRecord implements Comparable<RedcapSurveyRecord>, Serializable {
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

    @Override
    public int compareTo(RedcapSurveyRecord o) {
        int result;
        result = nullSafeStringComparator(this.recordId, o.recordId);
        if (result != 0) {
            return result;
        }
        return nullSafeStringComparator(this.eventName, o.eventName);
    }

    public boolean isSame(Object obj) {
        return (compareTo((RedcapSurveyRecord) obj) == 0);
    }

    protected static int nullSafeStringComparator(final String one, final String two) {
        if (one == null ^ two == null) {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null) {
            return 0;
        }

        return one.compareToIgnoreCase(two);
    }

}
