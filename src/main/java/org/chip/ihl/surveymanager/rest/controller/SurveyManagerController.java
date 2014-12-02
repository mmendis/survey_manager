package org.chip.ihl.surveymanager.rest.controller;

import org.apache.log4j.Logger;
import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;
import org.chip.ihl.surveymanager.rest.controller.exception.ResourceNotFoundException;
import org.chip.ihl.surveymanager.rest.controller.exception.UnacceptableException;
import org.chip.ihl.surveymanager.rest.controller.exception.UnauthorizedException;
import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Web service endpoints for for REDCap interaction
 * Created by sboykin on 11/23/2014.
 */
@RestController
public class SurveyManagerController {
    private static final String EAV_RECORD_TYPE = "eav";
    private final Logger logger = Logger.getLogger(SurveyManagerController.class);

    @Autowired
    protected RedcapService redcapService;


    @Autowired
    protected MessageService messageService;


    /**
     * Retrieves REDCap records for a particular subject and pushes to message queue
     * @param recordId  The survey subject ID (relative to project)
     * @param recordType    The record format type e.g. 'eav' (default) or 'flat'
     //* @param projectToken  The API token for the project to pull from
     * @param eventName    (optional) The event name to pull from, if longitudnal study
     * @param surveyForm    (optional) The survey form to pull form
     */
    @RequestMapping(value = "/trigger/pull", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public void pullRedcapRecords(
            @RequestParam(value="record", required = false) String recordId,
            @RequestParam(value = "recordType", defaultValue = EAV_RECORD_TYPE) String recordType,
            //@RequestParam(value = "token", required = true) String projectToken,
            @RequestParam(value = "redcap_event_name", required = false) String eventName,
            @RequestParam(value = "instrument", required = false) String surveyForm) {

        RedcapResult redcapResult = redcapService.pullRecordRequest(recordType, recordId, surveyForm, eventName);
        switch (redcapResult.getStatus()) {
            case OK:
                ArrayList<RedcapSurveyRecord> records = redcapResult.getRecords();
                if (records == null || records.isEmpty()) {
                    throw new ResourceNotFoundException("REDCap returned no records to push.");
                }
                else {
                    messageService.send(records);
                    logger.info("Successfully pulled REDCap records and pushed to message queue.");
                }
                break;
            case NOT_FOUND:
                throw new ResourceNotFoundException("Not found");
            case UNAUTHORIZED:
                throw new UnauthorizedException(redcapResult.getRedcapError().getError());
            case FORBIDDEN:
                throw new UnauthorizedException(redcapResult.getRedcapError().getError());
            case NOT_ACCEPTABLE:
                throw new UnacceptableException(redcapResult.getRedcapError().getError());
            default:
                throw new RuntimeException(redcapResult.getRedcapError().getError());
        }
    }


    /***** Exception Handlers ****/
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Missing or invalid arguments")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badArguments(Exception ex) {
        logger.error("Error in client request", ex);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Records not found")
    @ExceptionHandler(ResourceNotFoundException.class)
    public void notFound(Exception ex) {
        logger.warn("Request not found", ex);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
    @ExceptionHandler(UnauthorizedException.class)
    public void unAuthorized(Exception ex) {
        logger.error("Request not authorized", ex);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Data not acceptable")
    @ExceptionHandler(UnacceptableException.class)
    public void unacceptable(Exception ex) {
        logger.error("Request data not acceptable", ex);
    }
}
