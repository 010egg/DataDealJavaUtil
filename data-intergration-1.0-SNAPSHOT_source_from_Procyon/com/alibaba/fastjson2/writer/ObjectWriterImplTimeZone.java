// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.TimeZone;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplTimeZone extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplTimeZone INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeString(((TimeZone)object).getID());
    }
    
    static {
        INSTANCE = new ObjectWriterImplTimeZone();
    }
}
