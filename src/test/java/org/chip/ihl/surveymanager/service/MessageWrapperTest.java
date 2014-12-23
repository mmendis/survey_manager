package org.chip.ihl.surveymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.chip.ihl.surveymanager.config.WrapperConfiguration;
import org.chip.ihl.surveymanager.config.test.TestConfig;
import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;
import org.chip.ihl.surveymanager.redcap.RedcapData;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Tests interaction with messaging queue
 * 'live' tests assume configuration points to a valid MQ host location.  Failing tests can be caused to improper configuration
 * or a problem with external MQ
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 *
 * TODO flawed test dependent on what's on cue - should fix
 */
@Test
@ContextConfiguration(classes = TestConfig.class)
public class MessageWrapperTest extends AbstractTestNGSpringContextTests {
    private Properties properties;
    private MessageService messageService;
    private WrapperConfiguration wrapperConfiguration;

    private static final int MESSAGE_RECEIVE_TIMEOUT_MS = 5000;

    @BeforeTest
    public void setup() throws Exception {
        // pull Queue properties
        properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = RedcapWrapperTest.class.getClassLoader().getResourceAsStream("test.properties");
            if (inputStream == null) {
                throw new IOException("Can't load test properties file");
            }
            properties.load(inputStream);
            wrapperConfiguration = new WrapperConfiguration();

            wrapperConfiguration.setMessagingUrl(properties.getProperty("wrapper.messaging.broker.url"));
            wrapperConfiguration.setMessagingSendTimeout(properties.getProperty("wrapper.messaging.broker.send.timeout"));
            wrapperConfiguration.setMessagingQueue(properties.getProperty("wrapper.messaging.broker.mailbox-destination"));
            wrapperConfiguration.setMessagingUsername(properties.getProperty("wrapper.messaging.broker.username"));
            wrapperConfiguration.setMessagingPassword(properties.getProperty("wrapper.messaging.broker.password"));

            Reporter.log(String.format("Configuration components:\nBroker URL: %s; Broker Queue: %s; Broker User: %s; Broker Password: %s; Broker Send Timeout: %s\n",
                    wrapperConfiguration.getMessagingUrl(),
                    wrapperConfiguration.getMessagingQueue(),
                    wrapperConfiguration.getMessagingUsername(),
                    wrapperConfiguration.getMessagingPassword(),
                    wrapperConfiguration.getMessagingSendTimeout()));


        } catch (IOException ie) {
            throw new RuntimeException("Can't load test properties file", ie);
        }
    }

    /**
     * Message that tests the sending and receiving of messages
     * (yes, this is actually an integration test)
     */
    @Test(groups = "live")
    public void testMessageExchange() {
        messageService = messageService();

        // clear the queue
        messageService.clearQueue();
        // test the exchange
        ArrayList<EAVSurveyRecord> recordsToTest = new ArrayList<>();
        recordsToTest.addAll(Arrays.asList(RedcapData.sampleRedcapRecords()));
        messageService.send(recordsToTest);
        ArrayList<EAVSurveyRecord> consumedRecords = messageService.receive();
        Assert.assertNotNull(consumedRecords);

        // perform some check that the lists are essentially equal
        Assert.assertTrue(consumedRecords.size() == recordsToTest.size());
        for (int i = 0; i < consumedRecords.size(); i++) {
            Assert.assertTrue(consumedRecords.get(i).isSame(recordsToTest.get(i)));
        }
    }

    // HELPERS
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(wrapperConfiguration.getMessagingUrl());
        factory.setUserName(wrapperConfiguration.getMessagingUsername());
        factory.setPassword(wrapperConfiguration.getMessagingPassword());
        factory.setSendTimeout(Integer.parseInt(wrapperConfiguration.getMessagingSendTimeout()));
        return factory;
    }

    public ActiveMQQueue surveyQueue() {
        return new ActiveMQQueue(wrapperConfiguration.getMessagingQueue());
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        // don't wait for messages
        template.setReceiveTimeout(MESSAGE_RECEIVE_TIMEOUT_MS);
        return template;
    }
    public MessageProducerBean producerBean() {
        return new MessageProducerBean(jmsTemplate(), surveyQueue(), objectMapper());
    }
    public MessageConsumerBean consumerBean() {
        return new MessageConsumerBean(jmsTemplate(), surveyQueue(), objectMapper());
    }

    public MessageService messageService() {
        return new MessageWrapper(producerBean(), consumerBean());
    }

}