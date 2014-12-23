package org.chip.ihl.surveymanager.jms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.ArrayList;

/**
 * Bean to send messages to queue
 * Created by sboykin on 11/25/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */
public class MessageConsumerBean {
    private final Logger logger = LoggerFactory.getLogger(MessageConsumerBean.class);
    private JmsTemplate jmsTemplate;
    private Destination destination;
    private ObjectMapper objectMapper;

    public MessageConsumerBean() {
    }

    public MessageConsumerBean(JmsTemplate jmsTemplate, Destination destination, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.objectMapper = objectMapper;
    }

    public SurveyMessage receiveMessage() {
//        ObjectMessage message = (ObjectMessage) jmsTemplate.receive(destination);
        SurveyMessage surveyMessage = null;
        logger.debug("Receive timeout: " + jmsTemplate.getReceiveTimeout());
        TextMessage message = (TextMessage) jmsTemplate.receive(destination);
        try {
//            surveyMessage.getRecords().addAll((ArrayList<RedcapSurveyRecord>) message.getObject());
            if (message != null && message.getText() != null && !message.getText().isEmpty()) {
                ArrayList<? extends EAVSurveyRecord> records = objectMapper.readValue(message.getText(), new TypeReference<ArrayList<EAVSurveyRecord>>() {
                });
                surveyMessage = new SurveyMessage();
                surveyMessage.getRecords().addAll(records);
            }
            return surveyMessage;
        } catch (JMSException jmse) {
            throw new RuntimeException("Problem getting message from queue", jmse);
        } catch (Exception e) {
            throw new RuntimeException("Problem getting message from queue", e);
        }
    }
}
