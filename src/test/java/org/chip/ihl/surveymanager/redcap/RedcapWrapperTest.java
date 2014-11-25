package org.chip.ihl.surveymanager.redcap;

import org.chip.ihl.surveymanager.service.RedcapWrapper;
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
        //HttpEntity<EAVSurveyRecord> testEntity = setupTestHttpEntity();
       // EAVSurveyRecord[] goodResponseArray = goodResponse().getBody();
        // when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(EAVSurveyRecord[].class))).thenReturn(goodResponse());
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        //Assert.assertEquals(goodResponseArray.length, result.getRecords().size());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenMissingRecordTypePullRecordRequestThrowsException() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, null, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidRecordTypePullRecordRequestThrowsException() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, "myinvalidrecordtype", DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    /**
     * Should return OK
     */
    @Test
    public void whenMissingRecordIdPullRecordRequestReturnsOk() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, null, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test
    public void whenRecordIdNotFoundPullRecordRequestReturnsEmptyOk() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, "idNotInSystem", DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    /**
     * when valid record, records returned should only be for that ID
     */
    @Test
    public void whenEventFoundPullRecordRequestReturnsThoseRecordIDs() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
        List<RedcapSurveyRecord> records = result.getRecords();
        if (records != null) {
            for (RedcapSurveyRecord surveyRecord : records) {
                Assert.assertEquals(DEFAULT_RECORD_ID, surveyRecord.getRecordId());
            }
        }
    }

//    @Test
//    public void whenMissingSurveyFormPullRecordRequestReturnsOk() throws Exception {
//        RedcapWrapper wrapper = getWrapperFromParams();
//        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, null, DEFAULT_EVENT_NAME);
//        Assert.assertEquals(HttpStatus.OK, result.getStatus());
//    }
//
    // NOTE This test does not appear to do what it suggests in REDCap....
    // TODO Check REDCap documentation, groups to see if this is so and adjust; commenting out for now
//    @Test
//    public void whenSurveyFormNotFoundPullRecordRequestReturnsEmptyOk() {
//        RedcapWrapper wrapper = getWrapperFromParams();
//        RedcapResult result = wrapper.pullRecordRequest(EAV_RECORD_TYPE, DEFAULT_RECORD_ID, "aFormThatWontBeFound", DEFAULT_EVENT_NAME);
//        Assert.assertEquals(HttpStatus.OK, result.getStatus());
//        Assert.assertEquals(true, result.getRecords().isEmpty());
//    }

    /**
     * Missing event means all events should be returned
     */
    @Test
    public void whenMissingEventPullRecordRequestReturnsOk() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, null);
        Assert.assertEquals(HttpStatus.OK, result.getStatus());
    }

    /**
     * When event not found, should return no records
     */
    @Test
    public void whenEventNotFoundPullRecordRequestReturns404OrEmptyOk() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, "anEventThatWontBeFound");
        Assert.assertEquals(true, (HttpStatus.NOT_FOUND == result.getStatus()) || result.getRecords().isEmpty());
    }

    /**
     * when valid event, records returned should only be for that event
     */
    @Test
    public void whenEventFoundPullRecordRequestReturnsThoseEventRecords() {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest(apiToken, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
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
        RedcapResult result = wrapper.pullRecordRequest(null, EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
    }

    @Test
    public void whenInvalidTokenPullRecordRequestReturns403() throws Exception {
        RedcapWrapper wrapper = getWrapperFromParams();
        RedcapResult result = wrapper.pullRecordRequest("myInvalidToken", EAV_RECORD_TYPE, DEFAULT_RECORD_ID, DEFAULT_SURVEY_FORM, DEFAULT_EVENT_NAME);
        Assert.assertEquals(HttpStatus.FORBIDDEN, result.getStatus());
    }

    // TODO There should also be test coverage to check the survey form, but this doesn't work as expected in REDCap (v. 6.2.0)
    // TODO Should have a check that pull request on specific form only returns fields from that form, but that would b


    // HELPERS
    private RedcapWrapper getWrapperFromParams() {
        return new RedcapWrapper(host, protocol, apiUri, port);
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

    private static RedcapSurveyRecord[] sampleRedcapRecords() {
        RedcapSurveyRecord[] records = new RedcapSurveyRecord[3];
        records[0] = new EAVSurveyRecord("record_1", "my_field_name_1", "my_event_name_1", "my_value_1");
        records[1] = new EAVSurveyRecord("record_2", "my_field_name_2", "my_event_name_2", "my_value_2");
        records[2] = new EAVSurveyRecord("record_3", "my_field_name_3", "my_event_name_3", "my_value_3");

        return records;
    }
}