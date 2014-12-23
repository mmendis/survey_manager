package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;

import java.util.ArrayList;

/**
 * Wrapper to the message broker
 * Created by sboykin on 11/25/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */
public interface MessageService {
    public static final String MESSAGE_LABEL = "message";

    public void send(ArrayList<EAVSurveyRecord> records);
    public ArrayList<EAVSurveyRecord> receive();
    public void clearQueue();
}
