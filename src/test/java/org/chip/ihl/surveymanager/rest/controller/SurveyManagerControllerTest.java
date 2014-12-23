/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.rest.controller;

import org.chip.ihl.surveymanager.config.test.TestConfig;
import org.chip.ihl.surveymanager.config.test.TestRestServiceWebConfig;
import org.chip.ihl.surveymanager.redcap.EAVSurveyRecord;
import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for SurveyManagerController.java
 *
 */
@Test
@ContextConfiguration(classes = {TestConfig.class, TestRestServiceWebConfig.class})
@WebAppConfiguration
public class SurveyManagerControllerTest extends AbstractTestNGSpringContextTests {
    private final Logger logger = LoggerFactory.getLogger(SurveyManagerControllerTest.class);
    protected MockMvc mockMvc;

    @Inject
    protected WebApplicationContext webApplicationContext;

    @Inject
    protected RedcapService redcapServiceMock;

    @Inject
    protected MessageService messageServiceMock;

    @BeforeTest
    public void setup() {
    }

    private final String REDCAP_PULL_URI = "/trigger/pull";
    private static final String EAV_RECORD_TYPE = "eav";
    private static final String EAV_RECORD_ID_1 = "rec_1";
    private static final String RECORD_NOT_IN_SYSTEM = "aRecordNotInSystem";
    private static final String INVALID_RECORD_TYPE = "anInvalidRecordType";
    private static final String EAV_FIELD_NAME = "sample_fieldname";
    private static final String EAV_FIELD_VALUE_1 = "sample_fieldvalue_1";
    private static final String EAV_EVENT_NAME_1 = "event_name_1";
    private static final String EAV_SURVEY_FORM_1 = "sample_survey_form_1";
    private static final String TEST_REDCAP_URL = "http://testredcap.dummy.org";


    protected RedcapResult getTestRedcapRecordsForSingleId() {
//        List<? extends EAVSurveyRecord> records = new ArrayList<>(1);
        List<EAVSurveyRecord> records = new ArrayList<>(1);
        records.add(new EAVSurveyRecord(EAV_RECORD_ID_1, EAV_FIELD_NAME, EAV_EVENT_NAME_1, EAV_FIELD_VALUE_1));
        return new RedcapResult(HttpStatus.OK, records, null);
    }

    protected RedcapResult getTestRedcapRecordsForNotFound() {
        return new RedcapResult(HttpStatus.NOT_FOUND);
    }

    protected RedcapResult getTestRedcapRecordsForEmptyRecords() {
        return new RedcapResult(HttpStatus.OK);
    }

    protected RedcapResult getTestRedcapRecordsForUnauthorizedToken() {
        return new RedcapResult(HttpStatus.UNAUTHORIZED);
    }
    protected RedcapResult getTestRedcapRecordsForForbiddenToken() {
        return new RedcapResult(HttpStatus.FORBIDDEN);
    }

    private void resetMocks() {
        Mockito.reset(redcapServiceMock, messageServiceMock);
    }

//    @Test
//    public void pullRedcapRecords_shouldReturnRecordsForSingleRecordId() throws Exception {
//        resetMocks();
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        RedcapResult redcapResult = getTestRedcapRecordsForSingleId();
//        when(redcapServiceMock.pullRecordRequest(anyString(), eq(EAV_RECORD_ID_1), anyString(), eq(EAV_EVENT_NAME_1))).thenReturn(redcapResult);
//
//        mockMvc.perform(post(REDCAP_PULL_URI)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("record", EAV_RECORD_ID_1)
//                        .param("recordType", EAV_RECORD_TYPE)
//                       // .param("token", "anyToken")
//                        .param("redcap_event_name", EAV_EVENT_NAME_1)
//                        .param("instrument", EAV_SURVEY_FORM_1)
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].record", is(EAV_RECORD_ID_1)))
//                .andExpect(jsonPath("$[0].redcap_event_name", is(EAV_EVENT_NAME_1)))
//                .andExpect(jsonPath("$[0].field_name", is(EAV_FIELD_NAME)))
//                .andExpect(jsonPath("$[0].value", is(EAV_FIELD_VALUE_1
////                        .andExpect(jsonPath("$[*].fieldValue", containsInAnyOrder(EAV_FIELD_VALUE_1
//                )));
//
//    }

    @Test
    public void pullRedcapRecords_shouldReturnOkForSingleRecordId() throws Exception {
        resetMocks();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        RedcapResult redcapResult = getTestRedcapRecordsForSingleId();
        when(redcapServiceMock.pullRecordRequest(anyString(), anyString(), eq(EAV_RECORD_ID_1), anyString(), eq(EAV_EVENT_NAME_1))).thenReturn(redcapResult);

        mockMvc.perform(post(REDCAP_PULL_URI)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("record", EAV_RECORD_ID_1)
                        .param("recordType", EAV_RECORD_TYPE)
                                // .param("token", "anyToken")
                        .param("redcap_event_name", EAV_EVENT_NAME_1)
                        .param("redcap_url", TEST_REDCAP_URL)
                        .param("instrument", EAV_SURVEY_FORM_1)
        )
                .andExpect(status().isOk());

    }

    @Test
    public void pullRedcapRecords_shouldReturnNotFoundWhenRedcapResultNotFound() throws Exception {
        resetMocks();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        RedcapResult redcapResult = getTestRedcapRecordsForNotFound();
        when(redcapServiceMock.pullRecordRequest(anyString(), anyString(), eq(EAV_RECORD_ID_1), anyString(), eq(EAV_EVENT_NAME_1))).thenReturn(redcapResult);

        mockMvc.perform(post(REDCAP_PULL_URI)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("record", EAV_RECORD_ID_1)
                        .param("recordType", EAV_RECORD_TYPE)
//                        .param("token", "anyToken")
                        .param("redcap_event_name", EAV_EVENT_NAME_1)
                        .param("redcap_url", TEST_REDCAP_URL)
                        .param("instrument", EAV_SURVEY_FORM_1)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void pullRedcapRecords_shouldReturnOkWhenRedcapResultRecordsEmpty() throws Exception {
        resetMocks();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        RedcapResult redcapResult = getTestRedcapRecordsForEmptyRecords();
        when(redcapServiceMock.pullRecordRequest(anyString(), anyString(), eq(RECORD_NOT_IN_SYSTEM), anyString(), anyString())).thenReturn(redcapResult);

        mockMvc.perform(post(REDCAP_PULL_URI)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("record", RECORD_NOT_IN_SYSTEM)
                        .param("recordType", EAV_RECORD_TYPE)
//                        .param("token", "anyToken")
                        .param("redcap_event_name", EAV_EVENT_NAME_1)
                        .param("redcap_url", TEST_REDCAP_URL)
                        .param("instrument", EAV_SURVEY_FORM_1)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void pullRedcapRecords_shouldReturnBadRequestWhenInvalidRecordType() throws Exception {
        resetMocks();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        when(redcapServiceMock.pullRecordRequest(anyString(), eq(INVALID_RECORD_TYPE), eq(EAV_RECORD_ID_1), anyString(), eq(EAV_EVENT_NAME_1))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post(REDCAP_PULL_URI)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("record", EAV_RECORD_ID_1)
                        .param("recordType", INVALID_RECORD_TYPE)
                        //.param("token", "anyToken")
                        .param("redcap_url", TEST_REDCAP_URL)
                        .param("redcap_event_name", EAV_EVENT_NAME_1)
                        .param("instrument", EAV_SURVEY_FORM_1)
        )
                .andExpect(status().isBadRequest());
    }

//    @Test
//    public void pullRedcapRecords_shouldReturnUnauthorized() throws Exception {
//        resetMocks();
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//        RedcapResult redcapResult = getTestRedcapRecordsForUnauthorizedToken();
//        when(redcapServiceMock.pullRecordRequest(anyString(), eq(EAV_RECORD_ID_1), anyString(), eq(EAV_EVENT_NAME_1))).thenReturn(getTestRedcapRecordsForUnauthorizedToken());
//
//        mockMvc.perform(post(REDCAP_PULL_URI)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("record", EAV_RECORD_ID_1)
//                        .param("recordType", EAV_RECORD_TYPE)
//                       // .param("token", "anyToken")
//                        .param("redcap_event_name", EAV_EVENT_NAME_1)
//                        .param("instrument", EAV_SURVEY_FORM_1)
//        )
//                .andExpect(status().isUnauthorized());
//    }
}