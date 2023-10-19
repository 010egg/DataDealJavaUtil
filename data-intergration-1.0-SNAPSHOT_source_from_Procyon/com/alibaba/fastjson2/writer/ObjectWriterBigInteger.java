// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.math.BigInteger;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterBigInteger implements ObjectWriter
{
    static final ObjectWriterBigInteger INSTANCE;
    final long features;
    
    public ObjectWriterBigInteger(final long features) {
        this.features = features;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeBigInt((BigInteger)object, features);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeBigInt((BigInteger)object, features);
    }
    
    static {
        INSTANCE = new ObjectWriterBigInteger(0L);
    }
}
