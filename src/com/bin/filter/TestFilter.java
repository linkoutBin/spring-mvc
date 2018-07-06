package com.bin.filter;

import javax.servlet.*;
import java.io.IOException;

public class TestFilter implements Filter {

    public TestFilter() {
        System.out.println("############filter-constructor######################");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("##################filter-init################");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        System.out.println("###################filter-dofilter######################");
        //过滤器链需要继续传递，来进行后续处理
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("######################filter-destroy######################");
    }
}
