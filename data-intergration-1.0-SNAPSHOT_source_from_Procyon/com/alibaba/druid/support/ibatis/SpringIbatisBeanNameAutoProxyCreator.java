// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import org.apache.commons.logging.LogFactory;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.aop.TargetSource;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;

public class SpringIbatisBeanNameAutoProxyCreator extends BeanNameAutoProxyCreator implements SpringIbatisBeanNameAutoProxyCreatorMBean
{
    private static final Log LOG;
    private List<String> proxyBeanNames;
    
    public SpringIbatisBeanNameAutoProxyCreator() {
        this.proxyBeanNames = new ArrayList<String>();
    }
    
    public List<String> getProxyBeanNames() {
        return this.proxyBeanNames;
    }
    
    public void setProxyBeanNames(final List<String> proxyBeanNames) {
        this.proxyBeanNames = proxyBeanNames;
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
            SpringIbatisBeanNameAutoProxyCreator.LOG.error(ex.getMessage(), ex);
            return super.createProxy(beanClass, beanName, specificInterceptors, targetSource);
        }
    }
    
    static {
        LOG = LogFactory.getLog(SpringIbatisBeanNameAutoProxyCreator.class);
    }
}
