package org.chip.ihl.surveymanager.service;

import junit.framework.Assert;
import org.chip.ihl.surveymanager.config.MessagingConfig;
import org.chip.ihl.surveymanager.redcap.RedcapData;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests interaction with messaging queue
 * Assumes configuration points to a valid MQ host location.  Failing tests can be caused to improper configuration
 * or a problem with external MQ
 */
@Test
@ContextConfiguration(classes = MessagingConfig.class)
public class MessageWrapperTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private MessageService messageService;


    /**
     * Message that tests the sending and receiving of messages
     * (yes, this is actually an integration test)
     */
    @Test
    public void testMessageExchange() {
        ArrayList<RedcapSurveyRecord> recordsToTest = new ArrayList<>();
        recordsToTest.addAll(Arrays.asList(RedcapData.sampleRedcapRecords()));
        messageService.send(recordsToTest);
        ArrayList<RedcapSurveyRecord> consumedRecords = messageService.receive();
        Assert.assertNotNull(consumedRecords);

        // perform some check that the lists are essentially equal
        Assert.assertTrue(consumedRecords.size() == recordsToTest.size());
        for (int i = 0; i < consumedRecords.size(); i++) {
            Assert.assertTrue(consumedRecords.get(i).isSame(recordsToTest.get(i)));
        }
    }
}