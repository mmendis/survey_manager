package org.chip.ihl.surveymanager.jms;

import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;

import java.util.ArrayList;

/**
 * Container for the payload that will be sent to message queue
 * Created by sboykin on 11/25/2014.
 */
public class SurveyMessage {
    private String mailId;
    private ArrayList<EAVSurveyRecord> records;

    public SurveyMessage() {
        this.records = new ArrayList<>();
    }

    public SurveyMessage(ArrayList<EAVSurveyRecord> records) {
        this.records = records;
    }

    public ArrayList<EAVSurveyRecord> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<EAVSurveyRecord> records) {
        this.records = records;
    }

    public SurveyMessage(String mailId, ArrayList<EAVSurveyRecord> object) {
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
