package org.chip.ihl.surveymanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.rest.validator.ConfigValidator;
import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.MessageWrapper;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.chip.ihl.surveymanager.service.RedcapWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;

/**
 * Main application context class
 * Created by sboykin on 11/21/2014.
 */
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Resource
    Environment environment;

    @Bean
    public WrapperConfiguration wrapperConfiguration() {
        WrapperConfiguration configuration = new WrapperConfiguration();
        configuration.setRedcapApiToken(environment.getRequiredProperty(ManagerPropertyHeaders.REDCAP_API_TOKEN.getHeader()));

        return configuration;
    }
    @Bean
    public RedcapService redcapService() {
        WrapperConfiguration wc = wrapperConfiguration();
        return new RedcapWrapper(wc.getRedcapApiToken());
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        WrapperConfiguration wc = wrapperConfiguration();
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(environment.getRequiredProperty(ManagerPropertyHeaders.MESSAGE_BROKER_URL.getHeader()));
        factory.setUserName(environment.getRequiredProperty(ManagerPropertyHeaders.MESSAGE_BROKER_USERNAME.getHeader()));
        factory.setPassword(environment.getRequiredProperty(ManagerPropertyHeaders.MESSAGE_BROKER_PASSWORD.getHeader()));
        factory.setSendTimeout(Integer.parseInt(environment.getRequiredProperty(ManagerPropertyHeaders.MESSAGE_BROKER_SEND_TIMEOUT.getHeader())));
        return factory;
    }

    @Bean
    public ActiveMQQueue surveyQueue() {
        return new ActiveMQQueue(environment.getRequiredProperty(ManagerPropertyHeaders.MESSAGE_BROKER_RESULT_QUEUE.getHeader()));
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
        return new MessageProducerBean(jmsTemplate(), surveyQueue());
    }
    @Bean
    public MessageConsumerBean consumerBean() {
        return new MessageConsumerBean(jmsTemplate(), surveyQueue());
    }

    @Bean
    public MessageService messageService() {
        return new MessageWrapper(producerBean(), consumerBean());
    }

    @Bean
    public ConfigValidator configValidator() {
        return new ConfigValidator();
    }

}
