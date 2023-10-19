// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.Map;
import java.time.Instant;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

public final class ObjectReaderImplInstant extends DateTimeCodec implements ObjectReader
{
    public static final ObjectReaderImplInstant INSTANCE;
    
    public static ObjectReaderImplInstant of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplInstant.INSTANCE;
        }
        return new ObjectReaderImplInstant(format, locale);
    }
    
    ObjectReaderImplInstant(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return Instant.class;
    }
    
    @Override
    public Object createInstance(final Map map, final long features) {
        final Number nano = map.get("nano");
        final Number epochSecond = map.get("epochSecond");
        if (nano != null && epochSecond != null) {
            return Instant.ofEpochSecond(epochSecond.longValue(), nano.longValue());
        }
        if (epochSecond != null) {
            return Instant.ofEpochSecond(epochSecond.longValue());
        }
        final Number epochMilli = map.get("epochMilli");
        if (epochMilli != null) {
            return Instant.ofEpochMilli(epochMilli.longValue());
        }
        throw new JSONException("can not create instant.");
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readInstant();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (jsonReader.isInt() && context.getDateFormat() == null) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            return Instant.ofEpochMilli(millis);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (this.format == null || this.yyyyMMddhhmmss19 || this.formatISO8601 || jsonReader.isObject()) {
            return jsonReader.readInstant();
        }
        final String str = jsonReader.readString();
        if (str.isEmpty()) {
            return null;
        }
        if (this.formatMillis || this.formatUnixTime) {
            long millis2 = Long.parseLong(str);
            if (this.formatUnixTime) {
                millis2 *= 1000L;
            }
            return Instant.ofEpochMilli(millis2);
        }
        final DateTimeFormatter formatter = this.getDateFormatter(jsonReader.getLocale());
        if (!this.formatHasHour) {
            return ZonedDateTime.of(LocalDate.parse(str, formatter), LocalTime.MIN, context.getZoneId()).toInstant();
        }
        if (!this.formatHasDay) {
            return ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(str, formatter), context.getZoneId()).toInstant();
        }
        final LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
        return ZonedDateTime.of(localDateTime, context.getZoneId()).toInstant();
    }
    
    static {
        INSTANCE = new ObjectReaderImplInstant(null, null);
    }
}
