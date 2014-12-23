package org.chip.ihl.surveymanager.config.test;

import org.chip.ihl.surveymanager.service.MessageService;
import org.chip.ihl.surveymanager.service.RedcapService;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Spring bean configuration for test cases
 * Created by sboykin on 11/21/2014.
 *
 * © 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */
@Configuration
public class TestConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
        PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new ClassPathResource("test.properties"));
        return props;
    }

    @Bean
    public RedcapService redcapServiceMock() {
        return Mockito.mock(RedcapService.class);
    }

    @Bean
    public MessageService messageServiceMock() { return Mockito.mock(MessageService.class);}
}
