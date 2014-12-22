package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 *
 * TODO setup message sends to be asynchronous (to allow for longer timeouts)
 */
public class MessageProducerBean {
    private final Logger logger = LoggerFactory.getLogger(MessageProducerBean.class);
    private JmsTemplate jmsTemplate;
    private Destination destination;
    private ObjectMapper objectMapper;

    public MessageProducerBean(JmsTemplate jmsTemplate, Destination destination, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.objectMapper = objectMapper;
    }

    /**
     * sends out a message (in JSON format)
     * @param surveyMessage the message to send
     */
    public void sendMessage(final SurveyMessage surveyMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    //ObjectMessage message = session.createObjectMessage();
                    String textAsJson = "";
                    TextMessage textMessage = session.createTextMessage();
                    if (surveyMessage != null && surveyMessage.getRecords() != null) {
                        //message.setObject(surveyMessage.getRecords());
                        textAsJson = objectMapper.writeValueAsString(surveyMessage.getRecords());
                        textMessage.setText(textAsJson);
                    }
                    logger.info("Sending the following text: " + textAsJson);
                    //return message;
                    return textMessage;
                } catch (JsonProcessingException e) {
                    logger.error("Unable to convert message to json format", e);
                    throw new JMSException("Unable to convert message to json format");
                }
            }
        });
    }
}
