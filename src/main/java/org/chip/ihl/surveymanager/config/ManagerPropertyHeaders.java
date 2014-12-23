/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.config;

/**
 * Created by sboykin on 12/9/2014.
 */
public enum ManagerPropertyHeaders {
    REDCAP_API_TOKEN ("wrapper.redcap.api.token"),
    REDCAP_PRIVATE_FORMS ("wrapper.redcap.private.forms"),
    MESSAGE_BROKER_URL ("wrapper.messaging.broker.url"),
    MESSAGE_BROKER_USERNAME ("wrapper.messaging.broker.username"),
    MESSAGE_BROKER_PASSWORD ("wrapper.messaging.broker.password"),
    MESSAGE_BROKER_RESULT_QUEUE ("wrapper.messaging.broker.mailbox-destination"),
    MESSAGE_BROKER_SEND_TIMEOUT ("wrapper.messaging.broker.send.timeout");

    private String header;

    ManagerPropertyHeaders(String s) {
        this.header = s;
    }

    public String getHeader() {
        return header;
    }
}
