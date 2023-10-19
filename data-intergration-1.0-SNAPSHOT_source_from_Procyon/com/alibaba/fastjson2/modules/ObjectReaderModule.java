// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.modules;

import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;

public interface ObjectReaderModule
{
    default void init(final ObjectReaderProvider provider) {
    }
    
    default ObjectReaderProvider getProvider() {
        return null;
    }
    
    default ObjectReaderAnnotationProcessor getAnnotationProcessor() {
        return null;
    }
    
    default void getBeanInfo(final BeanInfo beanInfo, final Class<?> objectClass) {
        final ObjectReaderAnnotationProcessor annotationProcessor = this.getAnnotationProcessor();
        if (annotationProcessor != null) {
            annotationProcessor.getBeanInfo(beanInfo, objectClass);
        }
    }
    
    default void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Field field) {
        final ObjectReaderAnnotationProcessor annotationProcessor = this.getAnnotationProcessor();
        if (annotationProcessor != null) {
            annotationProcessor.getFieldInfo(fieldInfo, objectClass, field);
        }
    }
    
    default ObjectReader getObjectReader(final ObjectReaderProvider provider, final Type type) {
        return this.getObjectReader(type);
    }
    
    default ObjectReader getObjectReader(final Type type) {
        return null;
    }
}
