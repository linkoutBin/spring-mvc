package com.bin.jndi.impl;

import com.bin.jndi.JndiResource;

public class JndiResourceImpl implements JndiResource {
    private String userName;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
