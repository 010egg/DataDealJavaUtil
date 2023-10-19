// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplAtomicReference extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplAtomicReference INSTANCE;
    final Class defineClass;
    
    public ObjectWriterImplAtomicReference(final Class defineClass) {
        this.defineClass = defineClass;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final AtomicReference atomic = (AtomicReference)object;
        final Object ref = atomic.get();
        if (ref == null) {
            jsonWriter.writeNull();
        }
        jsonWriter.writeAny(ref);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final AtomicReference atomic = (AtomicReference)object;
        final Object ref = atomic.get();
        if (ref == null) {
            jsonWriter.writeNull();
        }
        jsonWriter.writeAny(ref);
    }
    
    static {
        INSTANCE = new ObjectWriterImplAtomicReference(null);
    }
}
