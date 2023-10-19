// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplCharacter extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplCharacter INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final char ch = (char)object;
        jsonWriter.writeChar(ch);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final char ch = (char)object;
        jsonWriter.writeChar(ch);
    }
    
    static {
        INSTANCE = new ObjectWriterImplCharacter();
    }
}
