// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.lang.reflect.Field;

final class FieldWriterBigDecimalField<T> extends FieldWriter<T>
{
    FieldWriterBigDecimalField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, BigDecimal.class, BigDecimal.class, field, null);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final BigDecimal value = (BigDecimal)this.getFieldValue(object);
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                return false;
            }
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeDecimal(value, this.features, this.decimalFormat);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final BigDecimal value = (BigDecimal)this.getFieldValue(object);
        jsonWriter.writeDecimal(value, this.features, this.decimalFormat);
    }
}
