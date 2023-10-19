// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplAtomicLong extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplAtomicLong INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    final Class defineClass;
    
    public ObjectWriterImplAtomicLong(final Class defineClass) {
        this.defineClass = defineClass;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final AtomicLong atomic = (AtomicLong)object;
        if (jsonWriter.isWriteTypeInfo(atomic, fieldType)) {
            final long JSONB_TYPE_HASH = -1591858996898070466L;
            jsonWriter.writeTypeName(ObjectWriterImplAtomicLong.JSONB_TYPE_NAME_BYTES, -1591858996898070466L);
        }
        final long longValue = atomic.longValue();
        jsonWriter.writeInt64(longValue);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeInt64(((Number)object).longValue());
    }
    
    static {
        INSTANCE = new ObjectWriterImplAtomicLong(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("AtomicLong");
    }
}
