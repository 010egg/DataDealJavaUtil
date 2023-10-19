// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Locale;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplLocale extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplLocale INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeString(((Locale)object).toString());
    }
    
    static {
        INSTANCE = new ObjectWriterImplLocale();
    }
}
