// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.lang.reflect.Field;

final class FieldWriterBigIntField<T> extends FieldWriter<T>
{
    FieldWriterBigIntField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, BigInteger.class, BigInteger.class, field, null);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final BigInteger value = (BigInteger)this.getFieldValue(object);
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
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final BigInteger value = (BigInteger)this.getFieldValue(object);
        jsonWriter.writeBigInt(value, this.features);
    }
}
