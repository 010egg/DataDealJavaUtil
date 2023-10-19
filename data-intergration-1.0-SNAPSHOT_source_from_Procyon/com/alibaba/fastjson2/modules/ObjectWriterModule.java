// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.modules;

import com.alibaba.fastjson2.writer.FieldWriter;
import java.util.List;
import com.alibaba.fastjson2.writer.ObjectWriterCreator;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;

public interface ObjectWriterModule
{
    default void init(final ObjectWriterProvider provider) {
    }
    
    default ObjectWriter getObjectWriter(final Type objectType, final Class objectClass) {
        return null;
    }
    
    default boolean createFieldWriters(final ObjectWriterCreator creator, final Class objectType, final List<FieldWriter> fieldWriters) {
        return false;
    }
    
    default ObjectWriterAnnotationProcessor getAnnotationProcessor() {
        return null;
    }
    
    default ObjectWriterProvider getProvider() {
        return null;
    }
}
