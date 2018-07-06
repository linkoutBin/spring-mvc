package com.bin.servlet;

import javax.servlet.*;
import java.io.IOException;

public class TestServlet implements Servlet {

    public TestServlet() {
        System.out.println("########################servlet-constructor###################");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("################servlet-init####################");
    }

    @Override
    public ServletConfig getServletConfig() {
        System.out.println("#################servlet-config##################");
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("################servlet-service##################");
    }

    @Override
    public String getServletInfo() {
        System.out.println("##############servlet-info#################");
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("##############servlet-destroy################");
    }
}
