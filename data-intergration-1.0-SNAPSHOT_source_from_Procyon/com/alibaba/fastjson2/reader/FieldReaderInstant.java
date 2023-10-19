// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.ZoneOffset;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.function.BiConsumer;

public final class FieldReaderInstant<T> extends FieldReaderDateTimeCodec<T>
{
    final BiConsumer<T, Instant> function;
    
    FieldReaderInstant(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Field field, final Method method, final BiConsumer<T, Instant> function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, ObjectReaderImplInstant.of(format, locale));
        this.function = function;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Instant date = this.dateReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
        this.accept(object, date);
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final Instant instant = jsonReader.readInstant();
        this.accept(object, instant);
    }
    
    @Override
    protected void accept(final T object, final LocalDateTime ldt) {
        final ZoneOffset offset = DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(ldt);
        final Instant instant = ldt.toInstant(offset);
        this.accept(object, instant);
    }
    
    @Override
    protected void accept(final T object, final Date value) {
        this.accept(object, value.toInstant());
    }
    
    @Override
    protected void accept(final T object, final ZonedDateTime zdt) {
        this.accept(object, zdt.toInstant());
    }
    
    @Override
    protected Object apply(final Date value) {
        return value.toInstant();
    }
    
    @Override
    protected Object apply(final Instant value) {
        return value;
    }
    
    @Override
    protected Object apply(final ZonedDateTime zdt) {
        return zdt.toInstant();
    }
    
    @Override
    protected Object apply(final LocalDateTime ldt) {
        final ZoneOffset offset = DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(ldt);
        return ldt.toInstant(offset);
    }
    
    @Override
    protected Object apply(final long millis) {
        return Instant.ofEpochMilli(millis);
    }
    
    @Override
    protected void acceptNull(final T object) {
        this.accept(object, (Instant)null);
    }
    
    @Override
    public void accept(final T object, final long milli) {
        this.accept(object, Instant.ofEpochMilli(milli));
    }
    
    @Override
    protected void accept(final T object, final Instant instant) {
        if (this.schema != null) {
            this.schema.assertValidate(instant);
        }
        if (object == null) {
            throw new JSONException("set " + this.fieldName + " error, object is null");
        }
        if (instant == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
        }
        if (this.function != null) {
            this.function.accept(object, instant);
            return;
        }
        if (this.method != null) {
            try {
                this.method.invoke(object, instant);
            }
            catch (Exception e) {
                throw new JSONException("set " + this.fieldName + " error", e);
            }
            return;
        }
        if (this.fieldOffset != -1L) {
            JDKUtils.UNSAFE.putObject(object, this.fieldOffset, instant);
            return;
        }
        try {
            this.field.set(object, instant);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
