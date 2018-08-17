package com.bin.bean;

import com.bin.jmx.beans.HelloMBean;

public class Hello implements HelloMBean {
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
