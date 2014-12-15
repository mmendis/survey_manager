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
 */
//@PropertySource("classpath:application.properties")
//@PropertySource("file://WEB-INF/classes/application.properties")
public class AppConfig {
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
            if (propertiesFile == null) {
                throw new RuntimeException("Could not find mySCILHS properties file string from JNDI");
            } else {
                //String propertiesFile = String.format("%s/application.properties", propertiesPath);
//            PropertiesConfiguration pc = new PropertiesConfiguration("/WEB-INF/classes/application.properties");
                PropertiesConfiguration pc = new PropertiesConfiguration(propertiesFile);
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

}
