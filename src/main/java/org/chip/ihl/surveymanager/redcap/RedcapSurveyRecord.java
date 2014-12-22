package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * REDCap survey record returned from remote server
 * Created by sboykin on 11/22/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 *
 *
 * TODO awful hack in here to duplicate the subject ID field as the unique record ID for serialization - look into
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@class")
//@JsonSubTypes({@JsonSubTypes.Type(value = EAVSurveyRecord.class, name = "EAVSurveyRecord")})
public class RedcapSurveyRecord implements Comparable<RedcapSurveyRecord>, Serializable {
    protected String recordId;
    protected String eventName;
    protected String subjectId;  // unfortunately duplicate of recordId because of object mapping quirk
//    protected String surveyForm;

    public RedcapSurveyRecord() {
    }

    public RedcapSurveyRecord(String recordId, String eventName) {
        this.recordId = recordId;
        this.subjectId = recordId;
        this.eventName = eventName;
    }

    @JsonProperty("subject_id")
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    //    @JsonProperty("survey_id")
//    public String getSurveyForm() {
//        return surveyForm;
//    }

    @JsonProperty("record")
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
        this.subjectId = recordId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("event_id")
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
