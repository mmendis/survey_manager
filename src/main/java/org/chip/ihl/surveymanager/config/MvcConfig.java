/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC configuration
 * Created by sboykin on 12/9/2014.
 */
@Configuration
@ComponentScan(basePackages = {"org.chip.ihl.surveymanager.rest.controller"})
@EnableWebMvc
@Import(SecurityConfig.class)
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        return bean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/login_fail").setViewName("login_failed");
    }
}
