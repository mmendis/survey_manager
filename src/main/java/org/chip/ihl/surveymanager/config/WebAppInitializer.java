package org.chip.ihl.surveymanager.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Spring Web app initialization for deployment configuration
 * Created by sboykin on 11/24/2014.
 */
public class WebAppInitializer implements WebApplicationInitializer {

    public static final String CONFIG_LOCATION = "org.chip.ihl.surveymanager.config";
    public static final String MAPPING_URL = "/";
    public static final String MAPPING_JSP_URL = "/WEB-INF/views/";
    private static final String XML_CONTEXT_CONFIG_LOCATION = "/WEB-INF/spring/jms-context.xml";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // create root app context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class, MessagingConfig.class);

        // manage root context lifecycle
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // create dispatcher servlet's spring context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(MvcConfig.class);

        // Register, map dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

//        WebApplicationContext context = getContext();
//        servletContext.addListener(new ContextLoaderListener(context));
//
//        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
//        dispatcher.setInitParameter("contextConfigLocation", XML_CONTEXT_CONFIG_LOCATION);
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping(MAPPING_URL);
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.register(AppConfig.class);
        return context;
    }

}
