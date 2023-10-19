// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.util.Collection;
import java.util.Collections;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;
import java.util.Iterator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.aop.TargetSource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;

public class BeanTypeAutoProxyCreator extends AbstractAutoProxyCreator implements InitializingBean, ApplicationContextAware
{
    private static final long serialVersionUID = -9094985530794052264L;
    private Class<?> targetBeanType;
    private ApplicationContext context;
    private List<String> beanNames;
    
    public BeanTypeAutoProxyCreator() {
        this.beanNames = new ArrayList<String>();
    }
    
    public void setTargetBeanType(final Class<?> targetClass) {
        this.targetBeanType = targetClass;
    }
    
    public void setApplicationContext(final ApplicationContext context) {
        this.context = context;
    }
    
    protected Object[] getAdvicesAndAdvisorsForBean(final Class beanClass, final String beanName, final TargetSource targetSource) {
        for (String mappedName : this.beanNames) {
            if (FactoryBean.class.isAssignableFrom(beanClass)) {
                if (!mappedName.startsWith("&")) {
                    continue;
                }
                mappedName = mappedName.substring("&".length());
            }
            if (this.isMatch(beanName, mappedName)) {
                return BeanTypeAutoProxyCreator.PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
            }
        }
        return BeanTypeAutoProxyCreator.DO_NOT_PROXY;
    }
    
    protected boolean isMatch(final String beanName, final String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull((Object)this.targetBeanType, "targetType cannot be null");
        final String[] beanNames = this.context.getBeanNamesForType((Class)this.targetBeanType);
        Collections.addAll(this.beanNames, beanNames);
    }
}
