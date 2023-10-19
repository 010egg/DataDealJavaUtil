// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

abstract class FieldWriterInt64<T> extends FieldWriter<T>
{
    final boolean browserCompatible;
    
    FieldWriterInt64(final String name, final int ordinal, final long features, final String format, final String label, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldClass, fieldClass, field, method);
        this.browserCompatible = ((features & JSONWriter.Feature.BrowserCompatible.mask) != 0x0L);
    }
    
    @Override
    public final void writeInt64(final JSONWriter jsonWriter, final long value) {
        final long features = jsonWriter.getFeatures() | this.features;
        boolean writeAsString = (features & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask)) != 0x0L;
        this.writeFieldName(jsonWriter);
        if (!writeAsString) {
            writeAsString = (this.browserCompatible && !jsonWriter.jsonb && (value > 9007199254740991L || value < -9007199254740991L));
        }
        if (writeAsString) {
            jsonWriter.writeString(Long.toString(value));
        }
        else {
            jsonWriter.writeInt64(value);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Long value;
        try {
            value = (Long)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value != null) {
            this.writeInt64(jsonWriter, value);
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask)) == 0x0L) {
            return false;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeNumberNull();
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Long value = (Long)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeInt64(value);
    }
}
