package org.chip.ihl.surveymanager.rest.exception;

/**
 * 'Unauthorized request' exception
 * Created by sboykin on 11/25/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String error) {
        super(error);
    }
}
