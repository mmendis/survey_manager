package org.chip.ihl.surveymanager.rest.exception;

/**
 * Web endpoint not reachable
 * Created by sboykin on 12/5/2014.
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
