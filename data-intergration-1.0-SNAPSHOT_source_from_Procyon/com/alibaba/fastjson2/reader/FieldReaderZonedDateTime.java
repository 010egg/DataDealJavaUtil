// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.time.LocalDateTime;
import java.time.Instant;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.function.BiConsumer;

public class FieldReaderZonedDateTime<T> extends FieldReaderDateTimeCodec<T>
{
    final BiConsumer<T, ZonedDateTime> function;
    
    FieldReaderZonedDateTime(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Field field, final Method method, final BiConsumer<T, ZonedDateTime> function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, ObjectReaderImplZonedDateTime.of(format, locale));
        this.function = function;
    }
    
    @Override
    public final void readFieldValue(final JSONReader jsonReader, final T object) {
        final ZonedDateTime date = this.dateReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
        this.accept(object, date);
    }
    
    @Override
    public final void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final ZonedDateTime date = jsonReader.readZonedDateTime();
        this.accept(object, date);
    }
    
    @Override
    protected void accept(final T object, final Date value) {
        final Instant instant = value.toInstant();
        final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
        this.accept(object, zdt);
    }
    
    @Override
    protected void accept(final T object, final Instant instant) {
        final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
        this.accept(object, zdt);
    }
    
    @Override
    protected void accept(final T object, final LocalDateTime ldt) {
        final ZonedDateTime zdt = ZonedDateTime.of(ldt, DateUtils.DEFAULT_ZONE_ID);
        this.accept(object, zdt);
    }
    
    @Override
    protected Object apply(final Date value) {
        final Instant instant = value.toInstant();
        return ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
    }
    
    @Override
    protected Object apply(final Instant value) {
        return ZonedDateTime.ofInstant(value, DateUtils.DEFAULT_ZONE_ID);
    }
    
    @Override
    protected Object apply(final ZonedDateTime zdt) {
        return zdt;
    }
    
    @Override
    protected Object apply(final long millis) {
        final Instant instant = Instant.ofEpochMilli(millis);
        return ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
    }
    
    @Override
    protected Object apply(final LocalDateTime ldt) {
        return ldt.atZone(DateUtils.DEFAULT_ZONE_ID);
    }
    
    @Override
    protected void acceptNull(final T object) {
        this.accept(object, (ZonedDateTime)null);
    }
    
    @Override
    public void accept(final T object, final long milli) {
        final Instant instant = Instant.ofEpochMilli(milli);
        final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
        this.accept(object, zdt);
    }
    
    @Override
    protected void accept(final T object, final ZonedDateTime zdt) {
        if (this.schema != null) {
            this.schema.assertValidate(zdt);
        }
        if (zdt == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
        }
        if (object == null) {
            throw new JSONException("set " + this.fieldName + " error, object is null");
        }
        if (this.function != null) {
            this.function.accept(object, zdt);
            return;
        }
        if (this.method != null) {
            try {
                this.method.invoke(object, zdt);
            }
            catch (Exception e) {
                throw new JSONException("set " + this.fieldName + " error", e);
            }
            return;
        }
        if (this.fieldOffset != -1L) {
            JDKUtils.UNSAFE.putObject(object, this.fieldOffset, zdt);
            return;
        }
        try {
            this.field.set(object, zdt);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
