// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import org.springframework.beans.factory.FactoryBean;

public class JdbcStatManagerFactoryBean implements FactoryBean<JdbcStatManager>
{
    public JdbcStatManager getObject() throws Exception {
        return JdbcStatManager.getInstance();
    }
    
    public Class<?> getObjectType() {
        return JdbcStatManager.class;
    }
    
    public boolean isSingleton() {
        return true;
    }
}
