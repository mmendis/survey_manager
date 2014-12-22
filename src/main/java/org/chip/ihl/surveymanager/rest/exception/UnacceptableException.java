package org.chip.ihl.surveymanager.rest.exception;

/**
 * 'Unacceptable Exception' for controller (meaning bad data passed into service
 * Created by sboykin on 11/25/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
public class UnacceptableException extends RuntimeException {
    public UnacceptableException(String error) {
        super(error);
    }
}
