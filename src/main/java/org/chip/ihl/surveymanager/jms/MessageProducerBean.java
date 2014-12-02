package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 */
public class MessageProducerBean {
    private final Logger logger = LoggerFactory.getLogger(MessageProducerBean.class);
    private JmsTemplate jmsTemplate;
    private Destination destination;

    public MessageProducerBean(JmsTemplate jmsTemplate, Destination destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void sendMessage(final SurveyMessage surveyMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                    if (surveyMessage != null && surveyMessage.getRecords() != null) {
                        message.setObject(surveyMessage.getRecords());
                    }
                    return message;
            }
        });
    }
}
