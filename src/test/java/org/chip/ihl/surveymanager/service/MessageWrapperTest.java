package org.chip.ihl.surveymanager.service;

import junit.framework.Assert;
import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.jms.SurveyMessage;
import org.chip.ihl.surveymanager.redcap.RedcapData;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

@Test
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/jms-context.xml")
public class MessageWrapperTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MessageProducerBean producerBean;
    @Autowired
    private MessageConsumerBean consumerBean;


    /**
     * Message that tests the sending and receiving of messages
     * (yes, this is actually an integration test)
     */
    @Test
    public void testMessageExchange() {
        ArrayList<RedcapSurveyRecord> recordsToTest = new ArrayList<>();
        recordsToTest.addAll(Arrays.asList(RedcapData.sampleRedcapRecords()));
        producerBean.sendMessage(new SurveyMessage(recordsToTest));
        SurveyMessage consumedMessage = consumerBean.receiveMessage();
        Assert.assertNotNull(consumedMessage);
        ArrayList<RedcapSurveyRecord> consumedRecords = consumedMessage.getRecords();
        // perform some check that the lists are essentially equal
        int count = 0;
        Assert.assertTrue(consumedRecords.size() == recordsToTest.size());
        for (int i = 0; i < consumedRecords.size(); i++) {
            Assert.assertTrue(consumedRecords.get(i).isSame(recordsToTest.get(i)));
            count++;
        }
    }
}