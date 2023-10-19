// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

class ObjectWriterImplBoolean extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplBoolean INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeBooleanNull();
            return;
        }
        jsonWriter.writeBool((boolean)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeBooleanNull();
            return;
        }
        jsonWriter.writeBool((boolean)object);
    }
    
    static {
        INSTANCE = new ObjectWriterImplBoolean();
    }
}
