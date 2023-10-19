// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;

class FieldReaderInt64ValueField<T> extends FieldReaderObjectField<T>
{
    final long fieldOffset;
    
    FieldReaderInt64ValueField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Long defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
        this.fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final long fieldLong = jsonReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldLong);
        }
        JDKUtils.UNSAFE.putLong(object, this.fieldOffset, fieldLong);
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        this.readFieldValue(jsonReader, object);
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(long)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(long)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final long longValue = TypeUtils.toLongValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        JDKUtils.UNSAFE.putLong(object, this.fieldOffset, longValue);
    }
}
