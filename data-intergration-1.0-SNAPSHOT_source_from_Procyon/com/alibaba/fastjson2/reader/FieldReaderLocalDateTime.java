// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.util.Date;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import com.alibaba.fastjson2.JSONReader;
import java.time.Instant;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.function.BiConsumer;

public final class FieldReaderLocalDateTime<T> extends FieldReaderDateTimeCodec<T>
{
    final BiConsumer<T, ZonedDateTime> function;
    
    FieldReaderLocalDateTime(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Field field, final Method method, final BiConsumer<T, ZonedDateTime> function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, (format != null) ? new ObjectReaderImplLocalDateTime(format, locale) : ObjectReaderImplLocalDateTime.INSTANCE);
        this.function = function;
    }
    
    @Override
    public boolean supportAcceptType(final Class valueClass) {
        return this.fieldClass == Instant.class || this.fieldClass == Long.class;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final Object object) {
        final LocalDateTime date = this.dateReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
        this.accept(object, date);
    }
    
    @Override
    public void accept(final Object object, final long value) {
        final Instant instant = Instant.ofEpochMilli(value);
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        final LocalDateTime ldt = zdt.toLocalDateTime();
        this.accept(object, ldt);
    }
    
    @Override
    protected void accept(final Object object, final Date value) {
        final Instant instant = value.toInstant();
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        final LocalDateTime ldt = zdt.toLocalDateTime();
        this.accept(object, ldt);
    }
    
    @Override
    protected void acceptNull(final Object object) {
        this.accept(object, (LocalDateTime)null);
    }
    
    @Override
    protected void accept(final Object object, final Instant instant) {
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        final LocalDateTime ldt = zdt.toLocalDateTime();
        this.accept(object, ldt);
    }
    
    @Override
    protected void accept(final Object object, final ZonedDateTime zdt) {
        final LocalDateTime ldt = zdt.toLocalDateTime();
        this.accept(object, ldt);
    }
    
    @Override
    protected Object apply(final Date value) {
        final Instant instant = value.toInstant();
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        return zdt.toLocalDateTime();
    }
    
    @Override
    protected Object apply(final Instant instant) {
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        return zdt.toLocalDateTime();
    }
    
    @Override
    protected Object apply(final ZonedDateTime zdt) {
        return zdt.toLocalDateTime();
    }
    
    @Override
    protected Object apply(final LocalDateTime ldt) {
        return ldt;
    }
    
    @Override
    protected Object apply(final long millis) {
        final Instant instant = Instant.ofEpochMilli(millis);
        final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
        return zdt.toLocalDateTime();
    }
    
    public void accept(final Object object, final LocalDateTime value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (object == null) {
            throw new JSONException("set " + this.fieldName + " error, object is null");
        }
        if (value == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
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
}
