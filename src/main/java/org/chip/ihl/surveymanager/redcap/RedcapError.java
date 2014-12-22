package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sboykin on 11/21/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedcapError {
    private String error;

    public RedcapError() {
    }

    public RedcapError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
