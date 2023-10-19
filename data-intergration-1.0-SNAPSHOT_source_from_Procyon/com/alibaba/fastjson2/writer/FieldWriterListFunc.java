// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

final class FieldWriterListFunc<T> extends FieldWriterList<T>
{
    final Function<T, List> function;
    
    FieldWriterListFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Type itemType, final Field field, final Method method, final Function<T, List> function, final Type fieldType, final Class fieldClass) {
        super(fieldName, itemType, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        List value;
        try {
            value = this.function.apply(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) == 0x0L) {
                return false;
            }
            this.writeFieldName(jsonWriter);
            jsonWriter.writeArrayNull();
            return true;
        }
        else {
            if ((this.features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L && value.isEmpty()) {
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
        final List list = this.function.apply(object);
        if (list == null) {
            jsonWriter.writeNull();
            return;
        }
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        if (jsonWriter.jsonb) {
            final int size = list.size();
            jsonWriter.startArray(size);
            for (int i = 0; i < size; ++i) {
                final Object item = list.get(i);
                if (item == null) {
                    jsonWriter.writeNull();
                }
                else {
                    final Class<?> itemClass = item.getClass();
                    ObjectWriter itemObjectWriter;
                    if (itemClass == previousClass) {
                        itemObjectWriter = previousObjectWriter;
                    }
                    else {
                        itemObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                        previousClass = itemClass;
                        previousObjectWriter = itemObjectWriter;
                    }
                    itemObjectWriter.write(jsonWriter, item);
                }
            }
            return;
        }
        jsonWriter.startArray();
        for (int j = 0; j < list.size(); ++j) {
            if (j != 0) {
                jsonWriter.writeComma();
            }
            final Object item2 = list.get(j);
            if (item2 == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass2 = item2.getClass();
                ObjectWriter itemObjectWriter2;
                if (itemClass2 == previousClass) {
                    itemObjectWriter2 = previousObjectWriter;
                }
                else {
                    itemObjectWriter2 = this.getItemWriter(jsonWriter, itemClass2);
                    previousClass = itemClass2;
                    previousObjectWriter = itemObjectWriter2;
                }
                itemObjectWriter2.write(jsonWriter, item2);
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
