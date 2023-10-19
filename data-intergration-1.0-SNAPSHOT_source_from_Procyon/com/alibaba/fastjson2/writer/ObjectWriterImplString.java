// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplString extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplString INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeString((String)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeString((String)object);
    }
    
    static {
        INSTANCE = new ObjectWriterImplString();
    }
}
