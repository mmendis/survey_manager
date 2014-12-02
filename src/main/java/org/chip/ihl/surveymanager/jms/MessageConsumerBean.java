package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.ArrayList;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 */
public class MessageConsumerBean {
    private JmsTemplate jmsTemplate;
    private Destination destination;

    public MessageConsumerBean() {
    }

    public MessageConsumerBean(JmsTemplate jmsTemplate, Destination destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public SurveyMessage receiveMessage() {
        ObjectMessage message = (ObjectMessage) jmsTemplate.receive(destination);
        SurveyMessage surveyMessage = new SurveyMessage();
        try {
            surveyMessage.getRecords().addAll((ArrayList<RedcapSurveyRecord>) message.getObject());
            return surveyMessage;
        } catch (JMSException e2) {
            throw new RuntimeException("Problem getting message from queue", e2);
        }
    }
}
