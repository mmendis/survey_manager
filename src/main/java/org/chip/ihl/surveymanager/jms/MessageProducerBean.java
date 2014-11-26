package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.service.MessageService;
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
    private ObjectMapper objectMapper;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendMessage(final SurveyMessage surveyMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
//                try {
                    if (surveyMessage != null && surveyMessage.getRecords() != null) {
                        message.setObject(surveyMessage.getRecords());
                        //String text = objectMapper.writeValueAsString(surveyMessage.getRecords());
                        //message.setString(MessageService.MESSAGE_LABEL, text);
                    }
                    return message;
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException("Cannot serialize survey message", e);
//                }
            }
        });
    }
}
