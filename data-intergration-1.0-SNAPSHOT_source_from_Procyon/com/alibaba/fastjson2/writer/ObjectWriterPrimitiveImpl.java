// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.function.Function;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

public abstract class ObjectWriterPrimitiveImpl<T> implements ObjectWriter<T>
{
    @Override
    public void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        this.writeJSONB(jsonWriter, object, null, null, 0L);
    }
    
    @Override
    public void writeArrayMapping(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        this.write(jsonWriter, object, null, null, 0L);
    }
    
    public Function getFunction() {
        return null;
    }
}
