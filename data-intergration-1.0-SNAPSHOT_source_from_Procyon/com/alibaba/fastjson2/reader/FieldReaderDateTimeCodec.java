// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.Date;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

abstract class FieldReaderDateTimeCodec<T> extends FieldReader<T>
{
    final ObjectReader dateReader;
    final boolean formatUnixTime;
    final boolean formatMillis;
    
    public FieldReaderDateTimeCodec(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field, final ObjectReader dateReader) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field);
        this.dateReader = dateReader;
        boolean formatUnixTime = false;
        boolean formatMillis = false;
        final boolean hasDay = false;
        final boolean hasHour = false;
        if (format != null) {
            switch (format) {
                case "unixtime": {
                    formatUnixTime = true;
                    break;
                }
                case "millis": {
                    formatMillis = true;
                    break;
                }
            }
        }
        this.formatUnixTime = formatUnixTime;
        this.formatMillis = formatMillis;
    }
    
    @Override
    public final Object readFieldValue(final JSONReader jsonReader) {
        return this.dateReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
    }
    
    @Override
    public final ObjectReader getObjectReader(final JSONReader jsonReader) {
        return this.dateReader;
    }
    
    @Override
    public final ObjectReader getObjectReader(final JSONReader.Context context) {
        return this.dateReader;
    }
    
    protected abstract void accept(final T p0, final Date p1);
    
    protected abstract void acceptNull(final T p0);
    
    protected abstract void accept(final T p0, final Instant p1);
    
    protected abstract void accept(final T p0, final LocalDateTime p1);
    
    protected abstract void accept(final T p0, final ZonedDateTime p1);
    
    protected abstract Object apply(final Date p0);
    
    protected abstract Object apply(final Instant p0);
    
    protected abstract Object apply(final ZonedDateTime p0);
    
    protected abstract Object apply(final LocalDateTime p0);
    
    protected abstract Object apply(final long p0);
    
    @Override
    public void accept(final T object, Object value) {
        if (value == null) {
            this.acceptNull(object);
            return;
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equals(str)) {
                this.acceptNull(object);
                return;
            }
            if ((this.format == null || this.formatUnixTime || this.formatMillis) && IOUtils.isNumber(str)) {
                long millis = Long.parseLong(str);
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                this.accept(object, millis);
                return;
            }
            value = DateUtils.parseDate(str, this.format, DateUtils.DEFAULT_ZONE_ID);
        }
        if (value instanceof Date) {
            this.accept(object, (Date)value);
        }
        else if (value instanceof Instant) {
            this.accept(object, (Instant)value);
        }
        else if (value instanceof Long) {
            this.accept(object, (long)value);
        }
        else if (value instanceof LocalDateTime) {
            this.accept(object, (LocalDateTime)value);
        }
        else {
            if (!(value instanceof ZonedDateTime)) {
                throw new JSONException("not support value " + value.getClass());
            }
            this.accept(object, (ZonedDateTime)value);
        }
    }
    
    @Override
    public boolean supportAcceptType(final Class valueClass) {
        return valueClass == Date.class || valueClass == String.class;
    }
}
