// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterStringMethod<T> extends FieldWriter<T>
{
    FieldWriterStringMethod(final String fieldName, final int ordinal, final String format, final String label, final long features, final Field field, final Method method) {
        super(fieldName, ordinal, features, format, label, String.class, String.class, field, method);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        try {
            return this.method.invoke(object, new Object[0]);
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        String value = (String)this.getFieldValue(object);
        if (this.trim && value != null) {
            value = value.trim();
        }
        if (this.symbol && jsonWriter.jsonb) {
            jsonWriter.writeSymbol(value);
        }
        else if (this.raw) {
            jsonWriter.writeRaw(value);
        }
        else {
            jsonWriter.writeString(value);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        String value;
        try {
            value = (String)this.getFieldValue(object);
        }
        catch (JSONException error) {
            if ((jsonWriter.getFeatures(this.features) | JSONWriter.Feature.IgnoreNonFieldGetter.mask) != 0x0L) {
                return false;
            }
            throw error;
        }
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) == 0x0L) {
                return false;
            }
        }
        if (this.trim && value != null) {
            value = value.trim();
        }
        this.writeString(jsonWriter, value);
        return true;
    }
}
