package org.chip.ihl.surveymanager.service;

//import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.redcap.RedcapError;
import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Manages web API calls to the REDCap server
 * Created by sboykin on 11/19/2014.
 */
public class RedcapWrapper implements RedcapService {
    private final Logger logger = LoggerFactory.getLogger(RedcapWrapper.class);

    private String hostname;
    private String protocol;
    private String uri;
    private String port;
    private String apiToken;

    // hard-coding some parameters
    private final String REDCAP_DEFAULT_RECORD_FORMAT = "json";
    private final String REDCAP_PARAM_VALUE_RECORD_EAV_VALUE = "eav";
    private final String REDCAP_PARAM_VALUE_RECORD_FLAT_VALUE = "flat";
    private static final String REDCAP_PARAM_VALUE_RECORD_LABEL = "record";

    // some injections
   // @Autowired
    private ObjectMapper objectMapper;

    public RedcapWrapper() {
        objectMapper = new ObjectMapper();
    }

    public RedcapWrapper(String hostname, String protocol, String uri, String port, String apiToken) {
        this();
        this.hostname = hostname;
        this.protocol = protocol;
        this.uri = uri;
        this.port = port;
        this.apiToken = apiToken;
    }

    /**
     * Makes client call to REDCap instance
     * @param recordType - the type of REDCap record to return (e.g. 'eav', 'flat')
     * @param recordId - The ID of the subject (relative to the survey - empty means return all records)
     * @param surveyForm - The survey form (empty means return all forms)
     * @param eventName - The event name (optional, for longitudnal studies only - empty means return all events)
     * @return a response object containing status code and (optionally) survey records
     *
     *
     */
    @Override
    public RedcapResult pullRecordRequest(String recordType, String recordId, String surveyForm, String eventName) {
        if (recordType == null || recordType.isEmpty()) {
            throw new IllegalArgumentException("Record type cannot be null");
        }
        if (!recordType.equalsIgnoreCase(REDCAP_PARAM_VALUE_RECORD_EAV_VALUE) && !recordType.equalsIgnoreCase(REDCAP_PARAM_VALUE_RECORD_FLAT_VALUE)) {
            throw new IllegalArgumentException(String.format("Record type must be one of the following: {%s, %s}", REDCAP_PARAM_VALUE_RECORD_EAV_VALUE, REDCAP_PARAM_VALUE_RECORD_FLAT_VALUE));
        }
        if (apiToken == null || apiToken.isEmpty()) {
            throw new IllegalArgumentException(String.format("REDCap API token must not be null"));
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                logger.error("Redcap Server error: {} {}", response.getStatusCode(), response.getStatusText());
            }
        });

        // prepare callout request
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", apiToken);
        params.add("content", REDCAP_PARAM_VALUE_RECORD_LABEL);
        params.add("format", REDCAP_DEFAULT_RECORD_FORMAT);
        params.add("type", recordType);
        params.add("exportSurveyFields", "true");
        params.add("rawOrLabel", "raw");
        params.add("exportCheckboxLabel", "true");
        if (surveyForm != null && !surveyForm.isEmpty()) {
            params.add("forms", surveyForm);
        }
        if (recordId != null && !recordId.isEmpty()) {
            params.add("records", recordId);
        }
        if (eventName != null && !eventName.isEmpty()) {
            params.add("events", eventName);
        }
        HttpHeaders headers = new HttpHeaders();
       // headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       // headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        logger.debug("request parameters: {}", params.toString());
        RedcapResult redcapResult = new RedcapResult();

        // make request (POST)
        try {
            String redcapUrl = buildBaseUrl(protocol, hostname, port, uri);
            ResponseEntity<String> responseEntity = restTemplate.exchange(redcapUrl, HttpMethod.POST, requestEntity, String.class);
            String responseStr = responseEntity.getBody();

            logger.info("Response body: " + responseStr);
            redcapResult.setStatus(responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                RedcapSurveyRecord[] records = objectMapper.readValue(responseStr, RedcapSurveyRecord[].class);
                if (records != null) {
                    redcapResult.getRecords().addAll(Arrays.asList(records));
                }
            } else {
                //RedcapError redcapError = objectMapper.readValue(responseStr, RedcapError.class);
                redcapResult.setRedcapError(new RedcapError(responseStr));
            }
        } catch (IOException ie) {
            logger.error("Unexpected IO exception", ie);
            if (redcapResult.getStatus() == null) { redcapResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR); }
            redcapResult.setRedcapError(new RedcapError(ie.getMessage()));
        } catch (Exception e) {
            if (redcapResult.getStatus() == null) { redcapResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR); }
            logger.error("Unexpected exception", e);
            redcapResult.setRedcapError(new RedcapError(e.getMessage()));
        }
        return redcapResult;
    }

    @Override
    public RedcapResult pushRecordsRequest(String recordType, List<RedcapSurveyRecord> importRecords, String overwriteBehavior, String returnContent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // HELPERS
    private String buildBaseUrl(String protocol, String host, String port, String uri) {
        return String.format("%s://%s:%s/%s", protocol, host, port, uri);
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
