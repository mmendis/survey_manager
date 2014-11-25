package org.chip.ihl.surveymanager.rest.controller.exception;

/**
 * 'Unacceptable Exception' for controller (meaning bad data passed into service
 * Created by sboykin on 11/25/2014.
 */
public class UnacceptableException extends RuntimeException {
    public UnacceptableException(String error) {
        super(error);
    }
}
