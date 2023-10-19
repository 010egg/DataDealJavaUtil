// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat.config;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DruidStatNamespaceHandler extends NamespaceHandlerSupport
{
    public void init() {
        this.registerBeanDefinitionParser("annotation-driven", (BeanDefinitionParser)new DruidStatBeanDefinitionParser());
    }
}
