package com.bin.jndi.factory;

import com.bin.jndi.impl.JndiResourceImpl;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Enumeration;
import java.util.Hashtable;

public class MyBeanFactory implements ObjectFactory {

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        JndiResourceImpl jndiResource = new JndiResourceImpl();
        Reference reference = (Reference) obj;
        Enumeration adds = reference.getAll();
        while (adds.hasMoreElements()) {
            RefAddr refAddr = (RefAddr) adds.nextElement();
            String userName = refAddr.getType();
            String value = (String) refAddr.getContent();
            if(userName.equals("userName")){
                jndiResource.setUserName(value);
            }
        }
        return jndiResource;
    }
}
