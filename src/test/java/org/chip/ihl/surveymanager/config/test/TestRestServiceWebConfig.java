package org.chip.ihl.surveymanager.config.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC configuration for test environment
 * Created by sboykin on 11/24/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.chip.ihl.surveymanager.rest.controller")
public class TestRestServiceWebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
