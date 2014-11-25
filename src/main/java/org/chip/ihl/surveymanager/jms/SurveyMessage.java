package org.chip.ihl.surveymanager.jms;

import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the payload that will be sent to message queue
 * Created by sboykin on 11/25/2014.
 */
public class SurveyMessage {
    private String mailId;
    private ArrayList<RedcapSurveyRecord> records;

    public SurveyMessage() {
    }

    public SurveyMessage(ArrayList<RedcapSurveyRecord> records) {
        this.records = records;
    }

    public ArrayList<RedcapSurveyRecord> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<RedcapSurveyRecord> records) {
        this.records = records;
    }

    public SurveyMessage(String mailId, ArrayList<RedcapSurveyRecord> object) {
        this.mailId = mailId;
        this.records = object;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

}
