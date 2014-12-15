package org.chip.ihl.surveymanager.redcap;

/**
 * Common static methods to create test data
 * Created by sboykin on 11/25/2014.
 */
public class RedcapData {
    public static EAVSurveyRecord[] sampleRedcapRecords() {
        EAVSurveyRecord[] records = new EAVSurveyRecord[3];
        records[0] = new EAVSurveyRecord("record_1", "my_field_name_1", "my_event_name_1", "my_value_1");
        records[1] = new EAVSurveyRecord("record_2", "my_field_name_2", "my_event_name_2", "my_value_2");
        records[2] = new EAVSurveyRecord("record_3", "my_field_name_3", "my_event_name_3", "my_value_3");

        return records;
    }

}
