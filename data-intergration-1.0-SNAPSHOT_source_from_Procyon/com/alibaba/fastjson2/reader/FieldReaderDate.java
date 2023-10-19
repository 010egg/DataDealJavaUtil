// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.ZoneOffset;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.Instant;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.function.BiConsumer;

final class FieldReaderDate<T> extends FieldReaderDateTimeCodec<T>
{
    final BiConsumer<T, Date> function;
    
    public FieldReaderDate(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Field field, final Method method, final BiConsumer<T, Date> function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, ObjectReaderImplDate.of(format, locale));
        this.function = function;
    }
    
    @Override
    protected void acceptNull(final T object) {
        this.accept(object, (Date)null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        Date date;
        try {
            date = this.dateReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
        }
        catch (Exception e) {
            if ((this.features & JSONReader.Feature.NullOnError.mask) == 0x0L) {
                throw e;
            }
            date = null;
        }
        this.accept(object, date);
    }
    
    @Override
    protected void accept(final T object, final Date value) {
        if (this.function != null) {
            this.function.accept(object, value);
            return;
        }
        if (object == null) {
            throw new JSONException("set " + this.fieldName + " error, object is null");
        }
        if (this.method != null) {
            try {
                this.method.invoke(object, value);
            }
            catch (Exception e) {
                throw new JSONException("set " + this.fieldName + " error", e);
            }
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
    
    @Override
    protected void accept(final T object, final Instant instant) {
        final Date date = Date.from(instant);
        this.accept(object, date);
    }
    
    @Override
    public void accept(final T object, final long value) {
        this.accept(object, new Date(value));
    }
    
    @Override
    protected void accept(final T object, final ZonedDateTime zdt) {
        final long epochMilli = zdt.toInstant().toEpochMilli();
        final Date value = new Date(epochMilli);
        this.accept(object, value);
    }
    
    @Override
    protected Object apply(final LocalDateTime ldt) {
        final ZoneOffset offset = DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(ldt);
        final Instant instant = ldt.toInstant(offset);
        return Date.from(instant);
    }
    
    @Override
    protected void accept(final T object, final LocalDateTime ldt) {
        final ZoneOffset offset = DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(ldt);
        final Instant instant = ldt.toInstant(offset);
        final Date value = Date.from(instant);
        this.accept(object, value);
    }
    
    @Override
    protected Object apply(final Date value) {
        return value;
    }
    
    @Override
    protected Object apply(final Instant instant) {
        return Date.from(instant);
    }
    
    @Override
    protected Object apply(final ZonedDateTime zdt) {
        final Instant instant = zdt.toInstant();
        return Date.from(instant);
    }
    
    @Override
    protected Object apply(final long millis) {
        return new Date(millis);
    }
}
