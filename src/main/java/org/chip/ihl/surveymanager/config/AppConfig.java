/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.rest.validator.ConfigValidator;
import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.MessageWrapper;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.chip.ihl.surveymanager.service.RedcapWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;

/**
 * Main application context class
 * Created by sboykin on 11/21/2014.
 *
 */
public class AppConfig {
    private static final String DEFAULT_PROPERTIES_FILE = "surveymanager.default.properties";
    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public ReloadingStrategy reloadingStrategy() {
        FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
        strategy.setRefreshDelay(1000);
        return strategy;
    }

    @Bean
    public PropertiesConfiguration applicationProperties() {
        try {
            JndiTemplate jndiTemplate = new JndiTemplate();
            String propertiesFile = jndiTemplate.lookup("java:comp/env/myscilhsConfigFile", String.class);
            // if fresh deployment, use default configuration
            if (propertiesFile == null) {
                throw new RuntimeException("Could not find mySCILHS properties file location from JNDI");
            } else {
//                PropertiesConfiguration pc = new PropertiesConfiguration(propertiesFile);
                PropertiesConfiguration pc = retrievePropertiesConfiguration(propertiesFile);
                pc.setReloadingStrategy(reloadingStrategy());
                return pc;
            }
        } catch (ConfigurationException ce) {
            logger.error("Cannot access application properties file.  Check server and web application configuration.", ce);
            throw new RuntimeException(ce);
        } catch (NamingException ne) {
            logger.error("Problem doing JNDI lookup of myscilhs configuration path", ne);
            throw new RuntimeException(ne);
        }
    }

    @Bean
    public WrapperConfiguration wrapperConfiguration() {
        PropertiesConfiguration pc = applicationProperties();
        WrapperConfiguration wc = new WrapperConfiguration();
        wc.setRedcapApiToken(pc.getString(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));
        wc.setRedcapPrivateForms(pc.getList(ManagerPropertyHeaders.REDCAP_PRIVATE_FORMS.getHeader()));
        wc.setMessagingUrl(pc.getString(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader()));
        wc.setMessagingSendTimeout(pc.getString(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader()));
        wc.setMessagingQueue(pc.getString(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader()));
        wc.setMessagingUsername(pc.getString(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader()));
        wc.setMessagingPassword(pc.getString(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader()));

        return wc;
    }
    @Bean
    public RedcapService redcapService() {
        return new RedcapWrapper(wrapperConfiguration());
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        WrapperConfiguration wc = wrapperConfiguration();
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(wc.getMessagingUrl());
        factory.setUserName(wc.getMessagingUsername());
        factory.setPassword(wc.getMessagingPassword());
        factory.setSendTimeout(Integer.parseInt(wc.getMessagingSendTimeout()));
        return factory;
    }

    @Bean
    public ActiveMQQueue surveyQueue() {
        WrapperConfiguration wc = wrapperConfiguration();
        return new ActiveMQQueue(wc.getMessagingQueue());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }
    @Bean
    public MessageProducerBean producerBean() {
        return new MessageProducerBean(jmsTemplate(), surveyQueue(), objectMapper());
    }
    @Bean
    public MessageConsumerBean consumerBean() {
        return new MessageConsumerBean(jmsTemplate(), surveyQueue(), objectMapper());
    }

    @Bean
    public MessageService messageService() {
        return new MessageWrapper(producerBean(), consumerBean());
    }

    @Bean
    public ConfigValidator configValidator() {
        return new ConfigValidator();
    }

    /**
     * pointing to properties file with form validation messages
     * @return
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("validation");
        return source;
    }

    // HELPERS
    /**
     * method to retrieve properties (if no property file in active location, load/store from defaults
     * @param propertiesFile
     * @return
     */
    private PropertiesConfiguration retrievePropertiesConfiguration(String propertiesFile) throws ConfigurationException {
        if (propertiesFile == null || propertiesFile.isEmpty()) {
            throw new RuntimeException("Properties file location is missing");
        }
        PropertiesConfiguration configToUse = null;
        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(propertiesFile);
            configToUse = pc;
        } catch (ConfigurationException e) {
            logger.warn("Active properties file not found in configuration location.  Loading from defaults.");
            try { // copy from defaults and save to active location
                configToUse = new PropertiesConfiguration(DEFAULT_PROPERTIES_FILE);
                configToUse.setFileName(propertiesFile);
                configToUse.save();
            } catch (ConfigurationException ce2) {
                logger.error(String.format("Problem loading default properties file (%s) into active location (%s)", DEFAULT_PROPERTIES_FILE, propertiesFile));
                throw ce2;
            }
        }
        return configToUse;
    }
}
