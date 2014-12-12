package org.chip.ihl.surveymanager.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * Initialize/load Spring Security filter chain
 * Created by sboykin on 12/10/2014.
 */
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        super.afterSpringSecurityFilterChain(servletContext);
    }
}