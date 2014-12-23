/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.rest.controller;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.chip.ihl.surveymanager.config.ManagerPropertyHeaders;
import org.chip.ihl.surveymanager.config.WrapperConfiguration;
import org.chip.ihl.surveymanager.rest.validator.ConfigValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for loading and saving wrapper properties
 * Created by sboykin on 12/9/2014.
 *
 */
@Controller
@RequestMapping("/admin/config")
public class ManagerPropertiesController {
    private static final String MESSAGE_SUCCESS_HEAD_TAG = "<span class=\"passMessage\">";
    private static final String MESSAGE_FAIL_HEAD_TAG = "<span class=\"failMessage\">";
    private static final String MESSAGE_FOOTER_TAG = "</span>";
    private final Logger logger = LoggerFactory.getLogger(ManagerPropertiesController.class);

    @Autowired
    @Qualifier("configValidator")
    ConfigValidator configValidator;

    @Autowired
    PropertiesConfiguration appProperties;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(configValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getWrapperConfig(@ModelAttribute("wrapperConfig") WrapperConfiguration wc) {
            wc.setRedcapApiToken(appProperties.getString(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));
            wc.setRedcapPrivateForms(appProperties.getList(ManagerPropertyHeaders.REDCAP_PRIVATE_FORMS.getHeader()));
            wc.setRedcapApiToken(appProperties.getString(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));
            wc.setMessagingUrl(appProperties.getString(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader()));
            wc.setMessagingUsername(appProperties.getString(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader()));
            wc.setMessagingPassword(appProperties.getString(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader(), wc.getMessagingPassword()));
            wc.setMessagingQueue(appProperties.getString(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader(), wc.getMessagingQueue()));
            wc.setMessagingSendTimeout(appProperties.getString(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader(), wc.getMessagingSendTimeout()));
            return "managerProperties";
    }

    @RequestMapping(method = RequestMethod.POST)
//    public String saveWrapperConfig(@ModelAttribute("wrapperConfig") WrapperConfiguration wc, @ModelAttribute("message") String message) {
    public String saveWrapperConfig(Model model, @Validated @ModelAttribute("wrapperConfig") WrapperConfiguration wc, BindingResult result) {
        String message = String.format("%s Successfully changed configuration.  The application must be reloaded (or the application server restarted) for the changes to take effect. %s", MESSAGE_SUCCESS_HEAD_TAG, MESSAGE_FOOTER_TAG);
        try {
            //PropertiesConfiguration configuration = new PropertiesConfiguration("surveymanager.default.properties");
            // first save Old properties to backup file just in case...
            appProperties.save("surveymanager.default.properties.BACKUP");

            // make sure validation passed
            //model.addAttribute("wrapperConfig", wc);
            if (result.hasErrors()) {
                message = String.format("%s Errors found in configuration input.  Please check input values below %s", MESSAGE_FAIL_HEAD_TAG, MESSAGE_FOOTER_TAG);
            } else {
                // update values and save new configuration
                appProperties.setProperty(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader(), wc.getRedcapApiToken());
                appProperties.setProperty(ManagerPropertyHeaders.REDCAP_PRIVATE_FORMS.getHeader(), wc.getRedcapPrivateForms());
                appProperties.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader(), wc.getMessagingUrl());
                appProperties.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader(), wc.getMessagingUsername());
                appProperties.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader(), wc.getMessagingPassword());
                appProperties.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader(), wc.getMessagingQueue());
                appProperties.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader(), wc.getMessagingSendTimeout());
                appProperties.save();
                logger.info("Successfully saved wrapper configuration values: " + wc.toString());
            }
            model.addAttribute("message", message);
            return "managerProperties";
        } catch (ConfigurationException e) {
            logger.error("Cannot access application properties file.  Check server and web application configuration.");
            message = "Error saving wrapper configuration.";
            throw new RuntimeException(e);
        }
    }


}
