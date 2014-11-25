package org.chip.ihl.surveymanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.chip.ihl.surveymanager.service.RedcapWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

/**
 * Application context class
 * Created by sboykin on 11/21/2014.
 */
@Configuration
@ComponentScan(basePackages = {"org.chip.ihl.surveymanager"})
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class AppConfig {
    private static final String PROPERTY_REDCAP_HOST = "wrapper.redcap.host";
    private static final String PROPERTY_REDCAP_PROTOCOL = "wrapper.redcap.protocol";
    private static final String PROPERTY_REDCAP_PORT = "80";
    private static final String PROPERTY_REDCAP_API_URI = "wrapper.redcap.api.uri";

    @Resource
    Environment environment;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RedcapService redcapService() {
        return new RedcapWrapper(
                environment.getRequiredProperty(PROPERTY_REDCAP_HOST),
                environment.getRequiredProperty(PROPERTY_REDCAP_PROTOCOL),
                environment.getRequiredProperty(PROPERTY_REDCAP_API_URI),
                environment.getRequiredProperty(PROPERTY_REDCAP_PORT)
        );
    }
}
