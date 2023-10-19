// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

abstract class FieldWriterBoolVal extends FieldWriterBoolean
{
    FieldWriterBoolVal(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        boolean value;
        try {
            value = (boolean)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (!value) {
            final long features = this.features | jsonWriter.getFeatures();
            if (this.defaultValue == null && (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0x0L) {
                return false;
            }
        }
        this.writeBool(jsonWriter, value);
        return true;
    }
}
