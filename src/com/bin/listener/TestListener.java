package com.bin.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestListener implements ServletContextListener{

    public TestListener() {
        System.out.println("######################3testListener constructor########################");
    }
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("##################"+servletContextEvent.getServletContext().getInitParameter("contextConfigLocation"));
        System.out.println("################contextInitialized###############");
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("################contextDestroyed###############");
    }

}
