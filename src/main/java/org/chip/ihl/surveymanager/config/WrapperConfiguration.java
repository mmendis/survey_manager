package org.chip.ihl.surveymanager.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sboykin on 12/9/2014.
 */
public class WrapperConfiguration {
    private String redcapApiToken;
    private List<String> redcapPrivateForms;
    private String messagingUrl;
    private String messagingUsername;
    private String messagingPassword;
    private String messagingQueue;
    private String messagingSendTimeout;

    public WrapperConfiguration() {
        redcapPrivateForms = new ArrayList<>();
    }

    public void setRedcapPrivateForms(List<Object> list) {
        getRedcapPrivateForms().clear();
        if (list != null) {
            for (Object form : list) {
                getRedcapPrivateForms().add((String) form);
            }
        }

    }

    public List<String> getRedcapPrivateForms() {
        return redcapPrivateForms;
    }

    public String getMessagingUrl() {
        return messagingUrl;
    }

    public void setMessagingUrl(String messagingUrl) {
        this.messagingUrl = messagingUrl;
    }

    public String getMessagingUsername() {
        return messagingUsername;
    }

    public void setMessagingUsername(String messagingUsername) {
        this.messagingUsername = messagingUsername;
    }

    public String getMessagingPassword() {
        return messagingPassword;
    }

    public void setMessagingPassword(String messagingPassword) {
        this.messagingPassword = messagingPassword;
    }

    public String getMessagingQueue() {
        return messagingQueue;
    }

    public void setMessagingQueue(String messagingQueue) {
        this.messagingQueue = messagingQueue;
    }

    public String getMessagingSendTimeout() {
        return messagingSendTimeout;
    }

    public void setMessagingSendTimeout(String messagingSendTimeout) {
        this.messagingSendTimeout = messagingSendTimeout;
    }

    public String getRedcapApiToken() {
        return redcapApiToken;
    }

    public void setRedcapApiToken(String redcapApiToken) {
        this.redcapApiToken = redcapApiToken;
    }

}
