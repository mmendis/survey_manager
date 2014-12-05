package org.chip.ihl.surveymanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.chip.ihl.surveymanager.jms.MessageConsumerBean;
import org.chip.ihl.surveymanager.jms.MessageProducerBean;
import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.MessageWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;

/**
 * Messaging queue bean configuration
 * Created by sboykin on 12/2/2014.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class MessagingConfig {
    @Resource
    Environment environment;

    private static final String PROPERTY_BROKER_URL = "wrapper.messaging.broker.url";
    private static final String PROPERTY_BROKER_USER ="wrapper.messaging.broker.username" ;
    private static final String PROPERTY_BROKER_PASSWORD = "wrapper.messaging.broker.password";
    private static final String PROPERTY_BROKER_QUEUE = "wrapper.messaging.broker.mailbox-destination";
    private static final String PROPERTY_BROKER_SEND_TIMEOUT = "wrapper.messaging.broker.send.timeout";


    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(environment.getRequiredProperty(PROPERTY_BROKER_URL));
        factory.setUserName(environment.getRequiredProperty(PROPERTY_BROKER_USER));
        factory.setPassword(environment.getRequiredProperty(PROPERTY_BROKER_PASSWORD));
        factory.setSendTimeout(Integer.parseInt(environment.getRequiredProperty(PROPERTY_BROKER_SEND_TIMEOUT)));
        return factory;
    }

    @Bean
    public ActiveMQQueue surveyQueue() {
        return new ActiveMQQueue(environment.getRequiredProperty(PROPERTY_BROKER_QUEUE));
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

}
