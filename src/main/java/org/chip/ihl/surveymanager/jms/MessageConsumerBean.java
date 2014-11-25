package org.chip.ihl.surveymanager.jms;

import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

import javax.jms.*;
import java.util.ArrayList;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 */
public class MessageConsumerBean {
    private JmsTemplate jmsTemplate;
    private Destination destination;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public SurveyMessage receiveMessage() {
        ObjectMessage message = (ObjectMessage) jmsTemplate.receive(destination);
        try {
            SurveyMessage surveyMessage = new SurveyMessage();
            surveyMessage.setRecords((ArrayList<RedcapSurveyRecord>) message.getObject());
            return surveyMessage;
        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }
    }
}
