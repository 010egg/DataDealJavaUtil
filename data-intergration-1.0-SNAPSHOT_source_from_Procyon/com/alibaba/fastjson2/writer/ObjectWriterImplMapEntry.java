// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Map;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplMapEntry extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplMapEntry INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final Map.Entry entry = (Map.Entry)object;
        if (entry == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.startArray(2);
        jsonWriter.writeAny(entry.getKey());
        jsonWriter.writeAny(entry.getValue());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final Map.Entry entry = (Map.Entry)object;
        if (entry == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.startObject();
        jsonWriter.writeAny(entry.getKey());
        jsonWriter.writeColon();
        jsonWriter.writeAny(entry.getValue());
        jsonWriter.endObject();
    }
    
    static {
        INSTANCE = new ObjectWriterImplMapEntry();
    }
}
