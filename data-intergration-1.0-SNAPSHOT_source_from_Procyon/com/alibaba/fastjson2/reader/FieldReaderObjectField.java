// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import java.util.Date;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Map;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.JDKUtils;
import java.util.function.BiConsumer;
import java.lang.reflect.Method;
import java.util.Locale;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

class FieldReaderObjectField<T> extends FieldReaderObject<T>
{
    FieldReaderObjectField(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Object defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, (fieldType == null) ? field.getType() : fieldType, fieldClass, ordinal, features, format, null, defaultValue, schema, null, field, null);
    }
    
    @Override
    public void accept(final T object, final boolean value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Boolean.TYPE) {
            JDKUtils.UNSAFE.putBoolean(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setBoolean(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final byte value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Byte.TYPE) {
            JDKUtils.UNSAFE.putByte(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setByte(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final short value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Short.TYPE) {
            JDKUtils.UNSAFE.putShort(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setShort(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Integer.TYPE) {
            JDKUtils.UNSAFE.putInt(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setInt(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Long.TYPE) {
            JDKUtils.UNSAFE.putLong(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setLong(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final float value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Float.TYPE) {
            JDKUtils.UNSAFE.putFloat(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setFloat(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final double value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Double.TYPE) {
            JDKUtils.UNSAFE.putDouble(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setDouble(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final char value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L && this.fieldClass == Character.TYPE) {
            JDKUtils.UNSAFE.putChar(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setChar(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, Object value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (value == null) {
            if ((this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
                return;
            }
        }
        else {
            if (this.fieldClass.isPrimitive()) {
                this.acceptPrimitive(object, value);
                return;
            }
            if (this.fieldType != this.fieldClass && Map.class.isAssignableFrom(this.fieldClass) && value instanceof Map && this.fieldClass != Map.class) {
                final ObjectReader objectReader = this.getObjectReader(JSONFactory.createReadContext());
                value = objectReader.createInstance((Map)value, new JSONReader.Feature[0]);
            }
            else if (!this.fieldClass.isInstance(value)) {
                if (value instanceof String) {
                    final String str = (String)value;
                    if (this.fieldClass == LocalDate.class) {
                        if (this.format != null) {
                            value = LocalDate.parse(str, DateTimeFormatter.ofPattern(this.format));
                        }
                        else {
                            value = DateUtils.parseLocalDate(str);
                        }
                    }
                    else if (this.fieldClass == Date.class) {
                        if (this.format != null) {
                            value = DateUtils.parseDate(str, this.format, DateUtils.DEFAULT_ZONE_ID);
                        }
                        else {
                            value = DateUtils.parseDate(str);
                        }
                    }
                }
                if (!this.fieldClass.isInstance(value)) {
                    value = TypeUtils.cast(value, this.fieldType);
                }
            }
        }
        if (this.fieldOffset != -1L) {
            JDKUtils.UNSAFE.putObject(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.set(object, value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    final void acceptPrimitive(final T object, final Object value) {
        if (this.fieldClass == Integer.TYPE) {
            if (value instanceof Number) {
                final int intValue = ((Number)value).intValue();
                this.accept(object, intValue);
                return;
            }
        }
        else if (this.fieldClass == Long.TYPE) {
            if (value instanceof Number) {
                final long longValue = ((Number)value).longValue();
                this.accept(object, longValue);
                return;
            }
        }
        else if (this.fieldClass == Float.TYPE) {
            if (value instanceof Number) {
                final float floatValue = ((Number)value).floatValue();
                this.accept(object, floatValue);
                return;
            }
        }
        else if (this.fieldClass == Double.TYPE) {
            if (value instanceof Number) {
                final double doubleValue = ((Number)value).doubleValue();
                this.accept(object, doubleValue);
                return;
            }
        }
        else if (this.fieldClass == Short.TYPE) {
            if (value instanceof Number) {
                final short shortValue = ((Number)value).shortValue();
                this.accept(object, shortValue);
                return;
            }
        }
        else if (this.fieldClass == Byte.TYPE) {
            if (value instanceof Number) {
                final byte byteValue = ((Number)value).byteValue();
                this.accept(object, byteValue);
                return;
            }
        }
        else if (this.fieldClass == Character.TYPE) {
            if (value instanceof Character) {
                final char charValue = (char)value;
                this.accept(object, charValue);
                return;
            }
        }
        else if (this.fieldClass == Boolean.TYPE && value instanceof Boolean) {
            final boolean booleanValue = (boolean)value;
            this.accept(object, booleanValue);
            return;
        }
        throw new JSONException("set " + this.fieldName + " error, type not support " + value.getClass());
    }
}
