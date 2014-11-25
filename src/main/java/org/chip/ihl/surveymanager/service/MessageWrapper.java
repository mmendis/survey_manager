package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.jms.SurveyMessage;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Implementation of MessageService
 * Created by sboykin on 11/25/2014.
 */
public class MessageWrapper implements MessageService {
    @Autowired
    private MessageProducerBean producerBean;

    @Override
    public void send(ArrayList<RedcapSurveyRecord> records) {
        producerBean.sendMessage(new SurveyMessage(records));
    }
}
