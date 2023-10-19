// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat.config;

import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

public class DruidStatBeanDefinitionParser implements BeanDefinitionParser
{
    public static final String STAT_ANNOTATION_PROCESSOR_BEAN_NAME = "com.alibaba.druid.support.spring.stat.annotation.internalStatAnnotationBeanPostProcessor";
    public static final String STAT_ANNOTATION_PROCESSOR_BEAN_CLASS = "com.alibaba.druid.support.spring.stat.annotation.StatAnnotationBeanPostProcessor";
    public static final String STAT_ANNOTATION_ADVICE_BEAN_NAME = "druid-stat-interceptor";
    public static final String STAT_ANNOTATION_ADVICE_BEAN_CLASS = "com.alibaba.druid.support.spring.stat.DruidStatInterceptor";
    
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        final Object source = parserContext.extractSource((Object)element);
        final CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        parserContext.pushContainingComponent(compDefinition);
        final BeanDefinitionRegistry registry = parserContext.getRegistry();
        if (registry.containsBeanDefinition("com.alibaba.druid.support.spring.stat.annotation.internalStatAnnotationBeanPostProcessor")) {
            parserContext.getReaderContext().error("Only one DruidStatBeanDefinitionParser may exist within the context.", source);
        }
        else {
            final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("com.alibaba.druid.support.spring.stat.annotation.StatAnnotationBeanPostProcessor");
            builder.getRawBeanDefinition().setSource(source);
            registerComponent(parserContext, builder, "com.alibaba.druid.support.spring.stat.annotation.internalStatAnnotationBeanPostProcessor");
        }
        if (!registry.containsBeanDefinition("druid-stat-interceptor")) {
            final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition("com.alibaba.druid.support.spring.stat.DruidStatInterceptor");
            builder.getRawBeanDefinition().setSource(source);
            registerComponent(parserContext, builder, "druid-stat-interceptor");
        }
        parserContext.popAndRegisterContainingComponent();
        return null;
    }
    
    private static void registerComponent(final ParserContext parserContext, final BeanDefinitionBuilder builder, final String beanName) {
        builder.setRole(2);
        parserContext.getRegistry().registerBeanDefinition(beanName, (BeanDefinition)builder.getBeanDefinition());
        final BeanDefinitionHolder holder = new BeanDefinitionHolder((BeanDefinition)builder.getBeanDefinition(), beanName);
        parserContext.registerComponent((ComponentDefinition)new BeanComponentDefinition(holder));
    }
}
