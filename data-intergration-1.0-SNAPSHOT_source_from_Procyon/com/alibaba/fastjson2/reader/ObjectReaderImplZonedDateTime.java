// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.function.Function;
import com.alibaba.fastjson2.codec.DateTimeCodec;

class ObjectReaderImplZonedDateTime extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplZonedDateTime INSTANCE;
    private Function builder;
    
    public static ObjectReaderImplZonedDateTime of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplZonedDateTime.INSTANCE;
        }
        return new ObjectReaderImplZonedDateTime(format, locale);
    }
    
    public ObjectReaderImplZonedDateTime(final Function builder) {
        super(null, null);
        this.builder = builder;
    }
    
    public ObjectReaderImplZonedDateTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return ZonedDateTime.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readZonedDateTime();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        ZonedDateTime zdt;
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            zdt = ZonedDateTime.ofInstant(instant, context.getZoneId());
        }
        else if (jsonReader.readIfNull()) {
            zdt = null;
        }
        else if (this.format == null || this.yyyyMMddhhmmss19 || this.formatISO8601) {
            zdt = jsonReader.readZonedDateTime();
        }
        else {
            final String str = jsonReader.readString();
            if (this.formatMillis || this.formatUnixTime) {
                long millis2 = Long.parseLong(str);
                if (this.formatUnixTime) {
                    millis2 *= 1000L;
                }
                final Instant instant2 = Instant.ofEpochMilli(millis2);
                zdt = ZonedDateTime.ofInstant(instant2, context.getZoneId());
            }
            else {
                final DateTimeFormatter formatter = this.getDateFormatter(jsonReader.getLocale());
                if (!this.formatHasHour) {
                    zdt = ZonedDateTime.of(LocalDate.parse(str, formatter), LocalTime.MIN, context.getZoneId());
                }
                else if (!this.formatHasDay) {
                    zdt = ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(str, formatter), context.getZoneId());
                }
                else {
                    final LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
                    zdt = ZonedDateTime.of(localDateTime, context.getZoneId());
                }
            }
        }
        if (this.builder != null && zdt != null) {
            return this.builder.apply(zdt);
        }
        return zdt;
    }
    
    static {
        INSTANCE = new ObjectReaderImplZonedDateTime(null, null);
    }
}
