package com.bin.controller;

import com.bin.bean.MyBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xingshulin
 * @Date: 2018/12/20 上午11:43
 * @Description: TODO
 * @Version: 1.0
 **/
public class HelloController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<MyBean> beanList = new ArrayList<>();
        beanList.add(new MyBean("name1"));
        beanList.add(new MyBean("name2"));
        return new ModelAndView("test", "beans", beanList);
    }
}
