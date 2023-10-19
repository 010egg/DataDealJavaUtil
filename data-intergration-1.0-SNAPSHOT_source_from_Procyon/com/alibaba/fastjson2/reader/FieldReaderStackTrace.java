// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Iterator;
import java.util.Collection;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

public class FieldReaderStackTrace extends FieldReaderObject
{
    public FieldReaderStackTrace(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field, final BiConsumer function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, function);
    }
    
    @Override
    public void accept(final Object object, Object value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (value == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
        }
        if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            int nullCount = 0;
            for (final Object item : collection) {
                if (item == null) {
                    ++nullCount;
                }
            }
            if (nullCount == collection.size()) {
                value = new StackTraceElement[0];
            }
            else {
                final StackTraceElement[] array = new StackTraceElement[collection.size()];
                collection.toArray(array);
                value = array;
            }
        }
        this.function.accept(object, value);
    }
}
