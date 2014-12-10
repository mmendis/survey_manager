package org.chip.ihl.surveymanager.rest.controller;

import org.apache.commons.configuration.Configuration;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

/**
 * Controller for loading and saving wrapper properties
 * Created by sboykin on 12/9/2014.
 */
@Controller
@RequestMapping("/admin/config")
public class ManagerPropertiesController {
    private final Logger logger = LoggerFactory.getLogger(ManagerPropertiesController.class);

    @Autowired
    @Qualifier("configValidator")
    ConfigValidator configValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(configValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getWrapperConfig(@ModelAttribute("wrapperConfig") WrapperConfiguration wc) {
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration("application.properties");
//            modelMap.addAttribute("apiToken", configuration.getString(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));
            wc.setRedcapApiToken(configuration.getString(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));
            wc.setMessagingUrl(configuration.getString(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader()));
            wc.setMessagingUsername(configuration.getString(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader()));
            wc.setMessagingPassword(configuration.getString(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader(), wc.getMessagingPassword()));
            wc.setMessagingQueue(configuration.getString(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader(), wc.getMessagingQueue()));
            wc.setMessagingSendTimeout(configuration.getString(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader(), wc.getMessagingSendTimeout()));
            return "managerProperties";
        } catch (ConfigurationException e) {
            logger.error("Cannot access application properties file.  Check server and web application configuration.");
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
//    public String saveWrapperConfig(@ModelAttribute("wrapperConfig") WrapperConfiguration wc, @ModelAttribute("message") String message) {
    public String saveWrapperConfig(Model model, @Validated @ModelAttribute("wrapperConfig") WrapperConfiguration wc, BindingResult result) {
        String message = "Successfully changed configuration.  The application server must be restarted for the changes to take effect.";
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration("application.properties");
            // first save Old properties to backup file just in case...
            configuration.save("application.properties.BACKUP");

            // make sure validation passed
            //model.addAttribute("wrapperConfig", wc);
            if (result.hasErrors()) {
                message = "Errors found in configuration input.  Please check input values below";
            } else {
                // update values and save new configuration
                configuration.setProperty(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader(), wc.getRedcapApiToken());
                configuration.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader(), wc.getMessagingUrl());
                configuration.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader(), wc.getMessagingUsername());
                configuration.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader(), wc.getMessagingPassword());
                configuration.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader(), wc.getMessagingQueue());
                configuration.setProperty(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader(), wc.getMessagingSendTimeout());
                configuration.save();
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
