package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.jms.SurveyMessage;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Implementation of MessageService
 * Created by sboykin on 11/25/2014.
 */
public class MessageWrapper implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageWrapper.class);

    private MessageProducerBean producerBean;
    private MessageConsumerBean consumerBean;

    public MessageWrapper(MessageProducerBean producerBean, MessageConsumerBean consumerBean) {
        this.producerBean = producerBean;
        this.consumerBean = consumerBean;
    }

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
