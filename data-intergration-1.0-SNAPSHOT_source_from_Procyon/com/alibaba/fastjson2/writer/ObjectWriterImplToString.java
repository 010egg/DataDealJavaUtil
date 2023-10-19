// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

public class ObjectWriterImplToString extends ObjectWriterPrimitiveImpl
{
    public static final ObjectWriterImplToString INSTANCE;
    public static final ObjectWriterImplToString DIRECT;
    private final boolean direct;
    
    public ObjectWriterImplToString() {
        this(false);
    }
    
    public ObjectWriterImplToString(final boolean direct) {
        this.direct = direct;
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final String str = object.toString();
        if (this.direct) {
            jsonWriter.writeRaw(str);
        }
        else {
            jsonWriter.writeString(str);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplToString(false);
        DIRECT = new ObjectWriterImplToString(true);
    }
}
