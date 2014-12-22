package org.chip.ihl.surveymanager.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Spring Web app initialization for deployment configuration
 * Created by sboykin on 11/24/2014.
 *
 * Copyright 2014, Boston Children's Hospital (http://chip.org).
 */
//public class WebAppInitializer implements WebApplicationInitializer {
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SecurityConfig.class, AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }
    
//
//    public static final String CONFIG_LOCATION = "org.chip.ihl.surveymanager.config";
//    public static final String MAPPING_URL = "/";
//    public static final String MAPPING_JSP_URL = "/WEB-INF/views/";
//    private static final String XML_CONTEXT_CONFIG_LOCATION = "/WEB-INF/spring/jms-context.xml";
//
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        // create root app context
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfig.class, MessagingConfig.class);
//
//        // manage root context lifecycle
//        servletContext.addListener(new ContextLoaderListener(rootContext));
//
//        // create dispatcher servlet's spring context
//        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
//        dispatcherContext.register(MvcConfig.class);
//
//        // Register, map dispatcher servlet
//        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//
////        WebApplicationContext context = getContext();
////        servletContext.addListener(new ContextLoaderListener(context));
////
////        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
////        dispatcher.setInitParameter("contextConfigLocation", XML_CONTEXT_CONFIG_LOCATION);
////        dispatcher.setLoadOnStartup(1);
////        dispatcher.addMapping(MAPPING_URL);
//    }
//
//    private AnnotationConfigWebApplicationContext getContext() {
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation(CONFIG_LOCATION);
//        context.register(AppConfig.class);
//        return context;
//    }

}
