package org.chip.ihl.surveymanager.rest.exception;

/**
 * Web endpoint not reachable
 * Created by sboykin on 12/5/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */
public class UnreachableException extends RuntimeException {
    public UnreachableException(String error) {
        super(error);
    }

    public UnreachableException(Exception e) {
        super(e);
    }
}
