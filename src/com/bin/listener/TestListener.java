package com.bin.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class TestListener implements ServletContextListener {

    public TestListener() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        URL[] urls = new URL[2];
        try{
            urls[0] = URI.create("http://www.baidu.com").toURL();
        }catch (Exception e){
            e.printStackTrace();
        }
        try (URLClassLoader urlClassLoader = new URLClassLoader(urls)) {
            System.out.println("URLClassLoader:" + urlClassLoader.getURLs().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("######################testListener constructor########################");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("##################" + servletContextEvent.getServletContext().getInitParameter("contextConfigLocation"));
        System.out.println("################testListener contextInitialized###############");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("################testListener contextDestroyed###############");
    }

}
