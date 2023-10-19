// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.modules;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;

public interface ObjectReaderAnnotationProcessor
{
    default void getBeanInfo(final BeanInfo beanInfo, final Class<?> objectClass) {
    }
    
    default void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Field field) {
    }
    
    default void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Constructor constructor, final int paramIndex, final Parameter parameter) {
    }
    
    default void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method, final int paramIndex, final Parameter parameter) {
    }
    
    default void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method) {
    }
}
