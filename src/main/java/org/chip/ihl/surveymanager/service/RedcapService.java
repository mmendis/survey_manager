package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.redcap.RedcapResult;
import org.chip.ihl.surveymanager.redcap.RedcapSurveyRecord;

import java.util.List;

/**
 * external REDCap service interface
 * Created by sboykin on 11/21/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */
public interface RedcapService {
    RedcapResult pullRecordRequest(String redcapBaseUrl, String recordType, String recordId, String surveyForm, String eventName);
    RedcapResult pushRecordsRequest(String recordType, List<RedcapSurveyRecord> importRecords, String overwriteBehavior, String returnContent);
}
