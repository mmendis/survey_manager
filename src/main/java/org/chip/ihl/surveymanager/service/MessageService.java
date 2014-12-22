package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;

import java.util.ArrayList;

/**
 * Wrapper to the message broker
 * Created by sboykin on 11/25/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
public interface MessageService {
    public static final String MESSAGE_LABEL = "message";

    public void send(ArrayList<EAVSurveyRecord> records);
    public ArrayList<EAVSurveyRecord> receive();
    public void clearQueue();
}
