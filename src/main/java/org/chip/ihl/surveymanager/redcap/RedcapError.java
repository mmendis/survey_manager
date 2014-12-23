package org.chip.ihl.surveymanager.redcap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sboykin on 11/21/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
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
