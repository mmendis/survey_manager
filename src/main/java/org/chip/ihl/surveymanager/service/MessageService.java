package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;

import java.util.ArrayList;

/**
 * Wrapper to the message broker
 * Created by sboykin on 11/25/2014.
 */
public interface MessageService {
    public void send(ArrayList<RedcapSurveyRecord> records);
}
