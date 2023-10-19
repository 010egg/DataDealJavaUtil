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

class FieldReaderInt32ValueField<T> extends FieldReaderObjectField<T>
{
    final long fieldOffset;
    
    FieldReaderInt32ValueField(final String fieldName, final Class fieldType, final int ordinal, final String format, final Integer defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, 0L, format, defaultValue, schema, field);
        this.fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final int fieldInt = jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldInt);
        }
        JDKUtils.UNSAFE.putInt(object, this.fieldOffset, fieldInt);
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final int fieldInt = jsonReader.readInt32Value();
        this.accept(object, fieldInt);
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(int)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(int)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final int intValue = TypeUtils.toIntValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(intValue);
        }
        JDKUtils.UNSAFE.putInt(object, this.fieldOffset, intValue);
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        final int intValue = (int)value;
        JDKUtils.UNSAFE.putInt(object, this.fieldOffset, intValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt32Value();
    }
}
