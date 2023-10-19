// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.modules;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;

public interface ObjectWriterAnnotationProcessor
{
    default void getBeanInfo(final BeanInfo beanInfo, final Class objectClass) {
    }
    
    default void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectType, final Field field) {
    }
    
    default void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectType, final Method method) {
    }
}
