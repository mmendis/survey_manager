package org.chip.ihl.surveymanager.rest.controller.exception;

/**
 * 'Unauthorized request' exception
 * Created by sboykin on 11/25/2014.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String error) {
        super(error);
    }
}
