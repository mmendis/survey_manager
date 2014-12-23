/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.rest.exception;

/**
 * 'Unauthorized request' exception
 * Created by sboykin on 11/25/2014.
 *
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String error) {
        super(error);
    }
}
