// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplAtomicInteger extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplAtomicInteger INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    final Class defineClass;
    
    public ObjectWriterImplAtomicInteger(final Class defineClass) {
        this.defineClass = defineClass;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final AtomicInteger atomic = (AtomicInteger)object;
        if (jsonWriter.isWriteTypeInfo(atomic, fieldType)) {
            final long JSONB_TYPE_HASH = 7576651708426282938L;
            jsonWriter.writeTypeName(ObjectWriterImplAtomicInteger.JSONB_TYPE_NAME_BYTES, 7576651708426282938L);
        }
        jsonWriter.writeInt32(atomic.intValue());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final AtomicInteger atomic = (AtomicInteger)object;
        jsonWriter.writeInt32(atomic.intValue());
    }
    
    static {
        INSTANCE = new ObjectWriterImplAtomicInteger(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("AtomicInteger");
    }
}
