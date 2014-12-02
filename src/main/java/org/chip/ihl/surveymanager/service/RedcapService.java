package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;

import java.util.List;

/**
 * external REDCap service interface
 * Created by sboykin on 11/21/2014.
 */
public interface RedcapService {
    RedcapResult pullRecordRequest(String recordType, String recordId, String surveyForm, String eventName);
    RedcapResult pushRecordsRequest(String recordType, List<RedcapSurveyRecord> importRecords, String overwriteBehavior, String returnContent);
}
