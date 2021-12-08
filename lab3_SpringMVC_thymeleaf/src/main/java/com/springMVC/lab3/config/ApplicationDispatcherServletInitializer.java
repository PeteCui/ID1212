package com.springMVC.lab3.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//Java based configuration
//create a dispatcher servlet
public class ApplicationDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }


    //look inside the config and tell the dispatcher what are the beans it needs to create
    @Override
    protected Class<?>[] getServletConfigClasses() {
        Class[] configFiles = {MyAppConfig.class};
        return configFiles;
    }

    @Override
    protected String[] getServletMappings() {
        //handle every url
        String [] mappings = {"/"};
        return mappings;
    }
}
