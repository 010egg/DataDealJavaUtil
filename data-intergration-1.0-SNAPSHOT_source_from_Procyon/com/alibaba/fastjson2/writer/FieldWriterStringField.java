// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.Field;

final class FieldWriterStringField<T> extends FieldWriter<T>
{
    FieldWriterStringField(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(fieldName, ordinal, features, format, label, String.class, String.class, field, null);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        String value = (String)this.getFieldValue(object);
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) == 0x0L || (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0x0L) {
                return false;
            }
            if ((features & (JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) != 0x0L) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeString("");
                return true;
            }
        }
        if (this.trim && value != null) {
            value = value.trim();
        }
        this.writeFieldName(jsonWriter);
        if (this.symbol && jsonWriter.jsonb) {
            jsonWriter.writeSymbol(value);
        }
        else if (this.raw) {
            jsonWriter.writeRaw(value);
        }
        else {
            jsonWriter.writeString(value);
        }
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        String value = (String)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        if (this.trim) {
            value = value.trim();
        }
        if (this.raw) {
            jsonWriter.writeRaw(value);
        }
        else {
            jsonWriter.writeString(value);
        }
    }
}
