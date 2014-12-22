package org.chip.ihl.surveymanager.rest.exception;

/**
 * Web endpoint not reachable
 * Created by sboykin on 12/5/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
public class UnreachableException extends RuntimeException {
    public UnreachableException(String error) {
        super(error);
    }

    public UnreachableException(Exception e) {
        super(e);
    }
}
