package org.chip.ihl.surveymanager.redcap;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores results from remote REDCap survey call
 * Created by sboykin on 11/20/2014.
 */
public class RedcapResult {
    private HttpStatus status;
    private ArrayList<RedcapSurveyRecord> records;
    private RedcapError redcapError;

    public RedcapResult() {
        this.records = new ArrayList<>();
        this.redcapError = new RedcapError();
    }
    public RedcapResult(HttpStatus status, List<RedcapSurveyRecord> records, RedcapError error) {
        this();
        this.status = status;
        this.records.addAll(records);
        this.redcapError = error;
    }

    public RedcapResult(HttpStatus status) {
        this();
        this.status = status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ArrayList<RedcapSurveyRecord> getRecords() {
        return records;
    }

    public RedcapError getRedcapError() {
        return redcapError;
    }

    public void setRedcapError(RedcapError error) {
        this.redcapError = error;
    }
}
