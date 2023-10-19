// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

final class FieldWriterListMethod<T> extends FieldWriterList<T>
{
    FieldWriterListMethod(final String fieldName, final Type itemType, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Type fieldType, final Class fieldClass) {
        super(fieldName, itemType, ordinal, features, format, label, fieldType, fieldClass, field, method);
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
    public boolean write(final JSONWriter jsonWriter, final T object) {
        List value;
        try {
            value = (List)this.getFieldValue(object);
        }
        catch (JSONException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if (value == null) {
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) != 0x0L) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeArrayNull();
                return true;
            }
            return false;
        }
        else {
            if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L && value.isEmpty()) {
                return false;
            }
            final String refPath = jsonWriter.setPath(this, value);
            if (refPath != null) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeReference(refPath);
                jsonWriter.popPath(value);
                return true;
            }
            if (this.itemType == String.class) {
                this.writeListStr(jsonWriter, true, value);
            }
            else {
                this.writeList(jsonWriter, value);
            }
            jsonWriter.popPath(value);
            return true;
        }
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final List value = (List)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        this.writeListValue(jsonWriter, value);
    }
}
