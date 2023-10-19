// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Function;

final class FieldWriterStringFunc<T> extends FieldWriter<T>
{
    final Function<T, String> function;
    final boolean symbol;
    final boolean trim;
    final boolean raw;
    
    FieldWriterStringFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, String> function) {
        super(fieldName, ordinal, features, format, label, String.class, String.class, field, method);
        this.function = function;
        this.symbol = "symbol".equals(format);
        this.trim = "trim".equals(format);
        this.raw = ((features & 0x4000000000000L) != 0x0L);
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        String value;
        try {
            value = this.function.apply(object);
        }
        catch (RuntimeException error) {
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
        this.writeFieldName(jsonWriter);
        if (value == null && (this.features & (JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) != 0x0L) {
            jsonWriter.writeString("");
            return true;
        }
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
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        String value = this.function.apply(object);
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
    public Function getFunction() {
        return this.function;
    }
}
