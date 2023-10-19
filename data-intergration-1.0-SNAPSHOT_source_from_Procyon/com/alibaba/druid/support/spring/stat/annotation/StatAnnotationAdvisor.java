// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat.annotation;

import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.Pointcut;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class StatAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware
{
    private Advice advice;
    private Pointcut pointcut;
    private DruidStatInterceptor druidStatInterceptor;
    
    public StatAnnotationAdvisor(final DruidStatInterceptor druidStatInterceptor) {
        this.druidStatInterceptor = druidStatInterceptor;
        this.advice = this.buildAdvice();
        this.pointcut = this.buildPointcut();
    }
    
    public Pointcut getPointcut() {
        return this.pointcut;
    }
    
    public Advice getAdvice() {
        return this.advice;
    }
    
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware)this.advice).setBeanFactory(beanFactory);
        }
    }
    
    protected Advice buildAdvice() {
        return (Advice)this.druidStatInterceptor;
    }
    
    protected Pointcut buildPointcut() {
        final Pointcut cpc = (Pointcut)new AnnotationMatchingPointcut((Class)Stat.class, true);
        final Pointcut mpc = (Pointcut)AnnotationMatchingPointcut.forMethodAnnotation((Class)Stat.class);
        final ComposablePointcut result = new ComposablePointcut(cpc).union(mpc);
        return (Pointcut)result;
    }
}
