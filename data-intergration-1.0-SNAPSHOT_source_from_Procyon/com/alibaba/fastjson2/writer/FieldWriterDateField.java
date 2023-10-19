// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.lang.reflect.Field;

final class FieldWriterDateField<T> extends FieldWriterDate<T>
{
    FieldWriterDateField(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(fieldName, ordinal, features, format, label, Date.class, Date.class, field, null);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Date value = (Date)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        this.writeDate(jsonWriter, false, value.getTime());
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final Date date = (Date)this.getFieldValue(object);
        if (date != null) {
            this.writeDate(jsonWriter, date.getTime());
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeNull();
            return true;
        }
        return false;
    }
}
