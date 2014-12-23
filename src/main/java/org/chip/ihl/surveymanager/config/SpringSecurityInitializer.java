/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * Initialize/load Spring Security filter chain
 * Created by sboykin on 12/10/2014.
 *
 */
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        super.afterSpringSecurityFilterChain(servletContext);
    }
}
