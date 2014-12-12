package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.config.WrapperConfiguration;
import org.chip.ihl.surveymanager.config.test.TestConfig;
import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.chip.ihl.surveymanager.redcap.RedcapData.sampleRedcapRecords;

/**
 * Tests REDCap wrapper functionality
 * Assumes configuration a) points to a valid REDCap server, b) has a valid, active API token and c) points to a project that has records in it (this part needs improvement)
 */
@Test
@ContextConfiguration(classes = TestConfig.class)
public class RedcapWrapperTest extends AbstractTestNGSpringContextTests {
    private final Logger logger = LoggerFactory.getLogger(RedcapWrapperTest.class);

    private Properties properties;
    private final String EAV_RECORD_TYPE = "eav";
    private final String TEST_EVENT_NAME = "event_1_arm_1";
    private final String TEST_SURVEY_FORM = "pah_inbound_ivr_adult_survey";
    private final String TEST_RECORD_ID = "1";

    private String testBaseUrl;
    private String testApiToken;


    private RedcapWrapper validWrapper;

    @Mock
    private RestTemplate mockRestTemplate;
    @Qualifier("propertyConfigurer")

    @BeforeTest
    public void setup() throws Exception {
        // setup mocks
        MockitoAnnotations.initMocks(this);


        // pull REDCap properties
        properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = RedcapWrapperTest.class.getClassLoader().getResourceAsStream("test.properties");
            if (inputStream == null) {
                throw new IOException("Can't load test properties file");
            }
            properties.load(inputStream);
            testBaseUrl = properties.getProperty("wrapper.redcap.test.baseUrl");
            testApiToken = properties.getProperty("wrapper.redcap.test.api.token");

            Reporter.log(String.format("Configuration components:\nREDCap Base URL: %s; Token: %s\n",
                    testBaseUrl, testApiToken));

            validWrapper = getWrapperFromParams(testApiToken, null);
        } catch (IOException ie) {
            throw new RuntimeException("Can't load test properties file", ie);
        }
    }
    // pullRecordRequest tests
    @Test
    public void whenValidParametersPullRecordRequestReturnsOk() throws Exception {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingRecordTypePullRecordRequestThrowsException() {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, null, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidRecordTypePullRecordRequestThrowsException() {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, "myinvalidrecordtype", TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
    }

    /**
     * Should return OK
     */
    @Test
    public void whenMissingRecordIdPullRecordRequestReturnsOk() {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, null, TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test
    /**
     * TODO This test is flawed because the test doesn't account for the case when there are no records in the project - fix
     */
    public void whenRecordIdNotFoundPullRecordRequestReturnsEmptyOk() throws Exception {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, "idNotInSystem", TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        Assert.assertTrue(result.getRecords().isEmpty());
    }

    /**
     * when valid record, records returned should only be for that ID
     */
//    @Test
//    public void whenEventFoundPullRecordRequestReturnsThoseRecordIDs() {
//        RedcapWrapper wrapper = getValidWrapper();
//        RedcapResult result = wrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
//        Assert.assertEquals(HttpStatus.OK, result.getStatus());
//        List<RedcapSurveyRecord> records = result.getRecords();
//        if (records != null) {
//            for (RedcapSurveyRecord surveyRecord : records) {
//                Assert.assertEquals(TEST_RECORD_ID, surveyRecord.getRecordId());
//            }
//        }
//    }
//
    /**
     * Missing event means all events should be returned
     */
    @Test
    public void whenMissingEventPullRecordRequestReturnsOk() throws Exception {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, null);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    /**
     * When event not found, should return no records
     */
    @Test
    public void whenEventNotFoundPullRecordRequestReturnsEmptyOk() {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, "anEventThatWontBeFound");
        Assert.assertTrue(result.getRecords().isEmpty());
    }

    /**
     * when valid event, records returned should only be for that event
     */
    @Test
    public void whenEventFoundPullRecordRequestReturnsThoseEventRecords() {
        RedcapResult result = validWrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        List<RedcapSurveyRecord> records = result.getRecords();
        if (records != null) {
            for (RedcapSurveyRecord surveyRecord : records) {
                Assert.assertEquals(TEST_EVENT_NAME, surveyRecord.getEventName());
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingTokenPullRecordRequestThrowsException() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams(null, null);
        RedcapResult result = wrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
    }

    @Test
    public void whenInvalidTokenPullRecordRequestReturns403() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams("anInvalidToken", null);
        RedcapResult result = wrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.FORBIDDEN, result.getStatus());
    }

    @Test
    public void whenInvalidRedcapApiUrlPullRecordRequestReturnsException() {
        RedcapWrapper wrapper = getWrapperFromParams(testApiToken, null);
        // Test bad API uri
        RedcapResult result = wrapper.pullRecordRequest("aBadRedcapURIHere", EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
        Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenPrivateFormRequestedPullRecordRequestReturnsException() {
        String surveyForm = "aPrivateForm";
        List<String> privateForms = new ArrayList<>(1);
        privateForms.add(surveyForm);
        RedcapWrapper wrapper = getWrapperFromParams(testApiToken, privateForms);
        RedcapResult result = wrapper.pullRecordRequest(testBaseUrl, EAV_RECORD_TYPE, TEST_RECORD_ID, surveyForm, TEST_EVENT_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingRedcapApiUrlPullRecordRequestReturns404() {
        RedcapWrapper wrapper = getWrapperFromParams(testApiToken, null);
        // Test bad API uri
        RedcapResult result = wrapper.pullRecordRequest(null, EAV_RECORD_TYPE, TEST_RECORD_ID, TEST_SURVEY_FORM, TEST_EVENT_NAME);
    }

    // TODO There should also be test coverage to check the survey form, but this doesn't work as expected in REDCap (v. 6.2.0)
    // TODO Should have a check that pull request on specific form only returns fields from that form,
    // TODO but it appears REDCap returns all forms if specified form not found (should verify))


    // HELPERS
    private RedcapWrapper getWrapperFromParams(String apiToken, List<String> privateForms) {
        WrapperConfiguration wc = new WrapperConfiguration();
        wc.setRedcapApiToken(apiToken);
        if (privateForms != null)
        wc.getRedcapPrivateForms().addAll(privateForms);
        return new RedcapWrapper(wc);
    }

    private HttpEntity<RedcapSurveyRecord> setupTestHttpEntity() {
        List<MediaType> acceptTypes = new ArrayList<>(1);
        acceptTypes.add(MediaType.APPLICATION_JSON);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(acceptTypes);

        return new HttpEntity<>(headers);
    }

    private ResponseEntity<RedcapSurveyRecord[]> goodResponse() {
        return new ResponseEntity<>(sampleRedcapRecords(), HttpStatus.OK);
    }

    private ResponseEntity<RedcapSurveyRecord[]> unAuthorizedResponse() {
        return new ResponseEntity<>(sampleRedcapRecords(), HttpStatus.UNAUTHORIZED);
    }

}