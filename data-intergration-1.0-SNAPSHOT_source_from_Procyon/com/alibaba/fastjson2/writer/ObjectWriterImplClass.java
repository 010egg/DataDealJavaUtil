// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplClass extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplClass INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            jsonWriter.writeTypeName("java.lang.Class");
        }
        final Class clazz = (Class)object;
        jsonWriter.writeString(clazz.getName());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Class clazz = (Class)object;
        jsonWriter.writeString(clazz.getName());
    }
    
    static {
        INSTANCE = new ObjectWriterImplClass();
    }
}
