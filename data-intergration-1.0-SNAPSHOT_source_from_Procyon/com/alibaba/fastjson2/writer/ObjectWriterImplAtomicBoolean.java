// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplAtomicBoolean extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplAtomicBoolean INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeBooleanNull();
            return;
        }
        jsonWriter.writeBool(((AtomicBoolean)object).get());
    }
    
    static {
        INSTANCE = new ObjectWriterImplAtomicBoolean();
    }
}
