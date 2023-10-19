// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import org.apache.commons.logging.LogFactory;
import org.springframework.util.PatternMatchUtils;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClient;
import java.util.Iterator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.aop.TargetSource;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;

public class SpringIbatisBeanTypeAutoProxyCreator extends AbstractAutoProxyCreator implements SpringIbatisBeanTypeAutoProxyCreatorMBean
{
    private static final Log LOG;
    private static final long serialVersionUID = -9094985530794052264L;
    private List<String> beanNames;
    private final List<String> proxyBeanNames;
    
    public SpringIbatisBeanTypeAutoProxyCreator() {
        this.beanNames = new ArrayList<String>();
        this.proxyBeanNames = new ArrayList<String>();
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
                return SpringIbatisBeanTypeAutoProxyCreator.PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
            }
        }
        return SpringIbatisBeanTypeAutoProxyCreator.DO_NOT_PROXY;
    }
    
    protected Object createProxy(final Class beanClass, final String beanName, final Object[] specificInterceptors, final TargetSource targetSource) {
        try {
            final Object target = targetSource.getTarget();
            if (target instanceof SqlMapClientWrapper) {
                this.proxyBeanNames.add(beanName);
                return target;
            }
            if (target instanceof SqlMapClient) {
                this.proxyBeanNames.add(beanName);
                return new SqlMapClientWrapper((ExtendedSqlMapClient)target);
            }
            return super.createProxy(beanClass, beanName, specificInterceptors, targetSource);
        }
        catch (Throwable ex) {
            SpringIbatisBeanTypeAutoProxyCreator.LOG.error(ex.getMessage(), ex);
            return super.createProxy(beanClass, beanName, specificInterceptors, targetSource);
        }
    }
    
    protected boolean isMatch(final String beanName, final String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }
    
    public List<String> getBeanNames() {
        return this.beanNames;
    }
    
    public List<String> getProxyBeanNames() {
        return this.proxyBeanNames;
    }
    
    static {
        LOG = LogFactory.getLog(SpringIbatisBeanTypeAutoProxyCreator.class);
    }
}
