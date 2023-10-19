// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

final class FieldWriterListStrFunc<T> extends FieldWriter<T>
{
    final Function<T, List> function;
    
    FieldWriterListStrFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, List> function, final Type fieldType, final Class fieldClass) {
        super(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        List list;
        try {
            list = this.function.apply(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if (list == null) {
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) != 0x0L) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeArrayNull();
                return true;
            }
            return false;
        }
        else {
            if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L && list.isEmpty()) {
                return false;
            }
            this.writeFieldName(jsonWriter);
            if (jsonWriter.jsonb) {
                final int size = list.size();
                jsonWriter.startArray(size);
                for (int i = 0; i < size; ++i) {
                    final String item = list.get(i);
                    if (item == null) {
                        jsonWriter.writeNull();
                    }
                    else {
                        jsonWriter.writeString(item);
                    }
                }
                return true;
            }
            jsonWriter.startArray();
            for (int j = 0; j < list.size(); ++j) {
                if (j != 0) {
                    jsonWriter.writeComma();
                }
                final String item2 = list.get(j);
                if (item2 == null) {
                    jsonWriter.writeNull();
                }
                else {
                    jsonWriter.writeString(item2);
                }
            }
            jsonWriter.endArray();
            return true;
        }
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final List list = this.function.apply(object);
        if (list == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.jsonb) {
            final int size = list.size();
            jsonWriter.startArray(size);
            for (int i = 0; i < size; ++i) {
                final String item = list.get(i);
                if (item == null) {
                    jsonWriter.writeNull();
                }
                else {
                    jsonWriter.writeString(item);
                }
            }
            return;
        }
        jsonWriter.startArray();
        for (int j = 0; j < list.size(); ++j) {
            if (j != 0) {
                jsonWriter.writeComma();
            }
            final String item2 = list.get(j);
            if (item2 == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeString(item2);
            }
        }
        jsonWriter.endArray();
    }
}
