package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/application-config.xml")
public class RedcapWrapperTest extends AbstractTestNGSpringContextTests {
    private final Logger logger = LoggerFactory.getLogger(RedcapWrapperTest.class);

    private Properties properties;
    private final String EAV_RECORD_TYPE = "eav";
    private final String DEFAULT_EVENT_NAME = "event_1_arm_1";
    private final String DEFAULT_SURVEY_FORM = "pah_inbound_ivr_adult_survey";
    private final String DEFAULT_RECORD_ID = "1";

    private String host;
    private String port;
    private String protocol;
    private String apiUri;
    private String apiToken;

    @Mock
    private RestTemplate mockRestTemplate;

    @BeforeTest
    public void setup() throws Exception {
        // setup mocks
        MockitoAnnotations.initMocks(this);


        // pull REDCap properties
        properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = RedcapWrapperTest.class.getClassLoader().getResourceAsStream("application.properties");
            if (inputStream == null) {
                throw new IOException("Can't load test properties file");
            }
            properties.load(inputStream);
            host = properties.getProperty("wrapper.redcap.host");
            port = properties.getProperty("wrapper.redcap.port");
            protocol = properties.getProperty("wrapper.redcap.protocol");
            apiUri = properties.getProperty("wrapper.redcap.api.uri");
            apiToken = properties.getProperty("wrapper.redcap.api.token");

            Reporter.log(String.format("Configuration components:\nHost: %s; Port: %s, Protocol: %s, URI: %s, Token: %s\n",
                    host, port, protocol, apiUri, apiToken));
        } catch (IOException ie) {
            throw new RuntimeException("Can't load test properties file", ie);
        }
    }
    // pullRecordRequest tests
    @Test
    public void whenValidParametersPullRecordRequestReturnsOk() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingRecordTypePullRecordRequestThrowsException() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(null, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidRecordTypePullRecordRequestThrowsException() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest("myinvalidrecordtype", DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    /**
     * Should return OK
     */
    @Test
    public void whenMissingRecordIdPullRecordRequestReturnsOk() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, null, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test
    /**
     * TODO This test is flawed because the test doesn't account for the case when there are no records in the project - fix
     */
    public void whenRecordIdNotFoundPullRecordRequestReturnsEmptyOk() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, "idNotInSystem", DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        Assert.assertTrue(result.getRecords().isEmpty());
    }

    /**
     * when valid record, records returned should only be for that ID
     */
    @Test
    public void whenEventFoundPullRecordRequestReturnsThoseRecordIDs() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        List<RedcapSurveyRecord> records = result.getRecords();
        if (records != null) {
            for (RedcapSurveyRecord surveyRecord : records) {
                Assert.assertEquals(DEFAULT_RECORD_ID, surveyRecord.getRecordId());
            }
        }
    }

    /**
     * Missing event means all events should be returned
     */
    @Test
    public void whenMissingEventPullRecordRequestReturnsOk() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, null);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    /**
     * When event not found, should return no records
     */
    @Test
    public void whenEventNotFoundPullRecordRequestReturns404OrEmptyOk() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, "anEventThatWontBeFound");
        Assert.assertEquals(true, (HttpStatus.NOT_FOUND == result.getStatus()) || result.getRecords().isEmpty());
    }

    /**
     * when valid event, records returned should only be for that event
     */
    @Test
    public void whenEventFoundPullRecordRequestReturnsThoseEventRecords() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        List<RedcapSurveyRecord> records = result.getRecords();
        if (records != null) {
            for (RedcapSurveyRecord surveyRecord : records) {
                Assert.assertEquals(DEFAULT_EVENT_NAME, surveyRecord.getEventName());
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingTokenPullRecordRequestThrowsException() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        wrapper.setApiToken(null);
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    @Test
    public void whenInvalidTokenPullRecordRequestReturns403() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        wrapper.setApiToken("anInvalidToken");
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.FORBIDDEN, result.getStatus());
    }

    @Test
    public void whenInvalidRedcapHostnamePullRecordRequestReturns500() {
        RedcapWrapper wrapper = getWrapperFromParams();
        // Test bad hostname
        wrapper.setHostname("aBadHostXYZ");
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatus());
    }

    @Test
    public void whenInvalidRedcapPortPullRecordRequestReturns500() {
        RedcapWrapper wrapper = getWrapperFromParams();
        // Test bad port
        wrapper.setPort("3");
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatus());
    }

    @Test
    public void whenInvalidRedcapApiUriPullRecordRequestReturns404() {
        RedcapWrapper wrapper = getWrapperFromParams();
        // Test bad API uri
        wrapper.setUri("aBadRedcapURIHere");
        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    }

    // TODO There should also be test coverage to check the survey form, but this doesn't work as expected in REDCap (v. 6.2.0)
    // TODO Should have a check that pull request on specific form only returns fields from that form,
    // TODO but it appears REDCap returns all forms if specified form not found (should verify))


    // HELPERS
    private RedcapWrapper getWrapperFromParams() {
        return new RedcapWrapper(host, protocol, apiUri, port, apiToken);
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