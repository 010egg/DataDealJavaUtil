// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

abstract class FieldWriterInt16<T> extends FieldWriter<T>
{
    FieldWriterInt16(final String name, final int ordinal, final long features, final String format, final String label, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldClass, fieldClass, field, method);
    }
    
    protected final void writeInt16(final JSONWriter jsonWriter, final short value) {
        final boolean writeNonStringValueAsString = (jsonWriter.getFeatures() & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0x0L;
        if (writeNonStringValueAsString) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeString(Short.toString(value));
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeInt16(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Short value;
        try {
            value = (Short)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value != null) {
            this.writeInt16(jsonWriter, value);
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
            return false;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeNumberNull();
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Short value = (Short)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeInt32(value);
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        return ObjectWriterImplInt16.INSTANCE;
    }
}
