// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

final class FieldWriterListField<T> extends FieldWriterList<T>
{
    FieldWriterListField(final String fieldName, final Type itemType, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field) {
        super(fieldName, itemType, ordinal, features, format, label, fieldType, fieldClass, field, null);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final List value = (List)this.getFieldValue(object);
        final JSONWriter.Context context = jsonWriter.context;
        if (value == null) {
            final long features = this.features | context.getFeatures();
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) != 0x0L) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeArrayNull();
                return true;
            }
            return false;
        }
        else {
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
        final boolean refDetect = jsonWriter.isRefDetect();
        if (refDetect) {
            final String refPath = jsonWriter.setPath(this.fieldName, value);
            if (refPath != null) {
                jsonWriter.writeReference(refPath);
                jsonWriter.popPath(value);
                return;
            }
        }
        this.writeListValue(jsonWriter, value);
        if (refDetect) {
            jsonWriter.popPath(value);
        }
    }
}
