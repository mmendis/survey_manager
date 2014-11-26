package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.chip.ihl.surveymanager.service.MessageService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.JmsException;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 */
public class MessageConsumerBean {
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

    public SurveyMessage receiveMessage() {
        ObjectMessage message = (ObjectMessage) jmsTemplate.receive(destination);
        SurveyMessage surveyMessage = new SurveyMessage();
        try {
           // String text = message.getString(MessageService.MESSAGE_LABEL);
           // RedcapSurveyRecord[] records = objectMapper.readValue(text, RedcapSurveyRecord[].class);
           // surveyMessage.getRecords().addAll(Arrays.asList(records));
            surveyMessage.getRecords().addAll((ArrayList<RedcapSurveyRecord>) message.getObject());
            return surveyMessage;
//        } catch (JsonMappingException e) {
//            throw new RuntimeException("Cannot deserialize incoming message", e);
//        } catch (JsonParseException e1) {
//            throw new RuntimeException("Cannot deserialize incoming message", e1);
        } catch (JMSException e2) {
            throw new RuntimeException("Problem getting message from queue", e2);
//        } catch (IOException e3) {
//            throw new RuntimeException(e3);
        }
    }
}
