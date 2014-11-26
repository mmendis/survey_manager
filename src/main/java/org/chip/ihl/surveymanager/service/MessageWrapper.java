package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
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

    @Autowired
    private MessageConsumerBean consumerBean;

    @Override
    public void send(ArrayList<RedcapSurveyRecord> records) {
        producerBean.sendMessage(new SurveyMessage(records));
    }

    @Override
    public ArrayList<RedcapSurveyRecord> receive() {
        SurveyMessage message = consumerBean.receiveMessage();
        if (message != null) {
            return message.getRecords();
        } else {
            return null;
        }
    }
}
