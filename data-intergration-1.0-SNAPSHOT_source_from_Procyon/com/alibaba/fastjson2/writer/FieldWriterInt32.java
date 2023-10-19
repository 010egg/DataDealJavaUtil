// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

abstract class FieldWriterInt32<T> extends FieldWriter<T>
{
    final boolean toString;
    
    protected FieldWriterInt32(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.toString = ((features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0x0L || "string".equals(format));
    }
    
    @Override
    public final void writeInt32(final JSONWriter jsonWriter, final int value) {
        if (this.toString) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeString(Integer.toString(value));
            return;
        }
        this.writeFieldName(jsonWriter);
        if (this.format != null) {
            jsonWriter.writeInt32(value, this.format);
        }
        else {
            jsonWriter.writeInt32(value);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Integer value;
        try {
            value = (Integer)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value != null) {
            this.writeInt32(jsonWriter, value);
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
        final Integer value = (Integer)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeInt32(value);
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        if (valueClass == this.fieldClass) {
            return ObjectWriterImplInt32.INSTANCE;
        }
        return jsonWriter.getObjectWriter(valueClass);
    }
}
