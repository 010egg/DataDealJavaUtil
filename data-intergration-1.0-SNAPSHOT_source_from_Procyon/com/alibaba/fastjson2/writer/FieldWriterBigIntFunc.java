// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.function.Function;

final class FieldWriterBigIntFunc<T> extends FieldWriter<T>
{
    final Function<T, BigInteger> function;
    
    FieldWriterBigIntFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, BigInteger> function) {
        super(fieldName, ordinal, features, format, label, BigInteger.class, BigInteger.class, null, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final BigInteger value = (BigInteger)this.getFieldValue(object);
        jsonWriter.writeBigInt(value, this.features);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T o) {
        final BigInteger value = this.function.apply(o);
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                return false;
            }
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeBigInt(value, this.features);
        return true;
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
