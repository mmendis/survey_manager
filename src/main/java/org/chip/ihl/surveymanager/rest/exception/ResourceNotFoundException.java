package org.chip.ihl.surveymanager.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 'Resource not found' custom exception
 * Created by sboykin on 11/25/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String error) {
        super(error);
    }
}
