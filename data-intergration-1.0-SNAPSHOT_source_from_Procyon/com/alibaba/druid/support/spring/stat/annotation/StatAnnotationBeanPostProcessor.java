// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat.annotation;

import org.springframework.aop.Advisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import javax.annotation.Resource;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;

public class StatAnnotationBeanPostProcessor extends AbstractAdvisingBeanPostProcessor implements BeanFactoryAware
{
    @Resource(name = "druid-stat-interceptor")
    private DruidStatInterceptor druidStatInterceptor;
    
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.setBeforeExistingAdvisors(true);
        final StatAnnotationAdvisor advisor = new StatAnnotationAdvisor(this.druidStatInterceptor);
        advisor.setBeanFactory(beanFactory);
        this.advisor = (Advisor)advisor;
    }
}
