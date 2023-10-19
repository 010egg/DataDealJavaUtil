// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import java.util.function.BiConsumer;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Collection;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderListField<T> extends FieldReaderList<T, Object>
{
    final long fieldOffset;
    
    FieldReaderListField(final String fieldName, final Type fieldType, final Class fieldClass, final Type itemType, final Class itemClass, final int ordinal, final long features, final String format, final Locale locale, final Collection defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldClass, itemType, itemClass, ordinal, features, format, locale, defaultValue, schema, null, field, null);
        this.fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
    }
    
    @Override
    public void accept(final Object object, final Object value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        JDKUtils.UNSAFE.putObject(object, this.fieldOffset, value);
    }
}
