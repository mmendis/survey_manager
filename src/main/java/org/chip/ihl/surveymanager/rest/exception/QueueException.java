/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.rest.exception;

/**
 * Web endpoint not reachable
 * Created by sboykin on 12/5/2014.
 *
 */
public class QueueException extends RuntimeException {
    public QueueException(String error) {
        super(error);
    }

    public QueueException(Exception e) {
        super(e);
    }

    public QueueException(String s, Exception me) {
        super(s, me);
    }
}
