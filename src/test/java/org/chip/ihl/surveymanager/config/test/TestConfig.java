package org.chip.ihl.surveymanager.config.test;

import org.chip.ihl.surveymanager.service.RedcapService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Test bean configuration
 * Created by sboykin on 11/21/2014.
 */
@Configuration
public class TestConfig {
    @Bean
    public RedcapService redcapServiceMock() {
        return Mockito.mock(RedcapService.class);
    }
}
