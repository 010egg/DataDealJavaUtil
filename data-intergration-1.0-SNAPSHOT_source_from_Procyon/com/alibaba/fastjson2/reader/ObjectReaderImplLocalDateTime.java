// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.time.LocalDateTime;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

class ObjectReaderImplLocalDateTime extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplLocalDateTime INSTANCE;
    
    public ObjectReaderImplLocalDateTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return LocalDateTime.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readLocalDateTime();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (jsonReader.isInt()) {
            final DateTimeFormatter formatter = this.getDateFormatter();
            if (formatter != null) {
                final String str = jsonReader.readString();
                return LocalDateTime.parse(str, formatter);
            }
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZoneId zoneId = context.getZoneId();
            return LocalDateTime.ofInstant(instant, zoneId);
        }
        else {
            if (jsonReader.readIfNull()) {
                return null;
            }
            if (this.format == null || this.yyyyMMddhhmmss19 || this.formatISO8601) {
                return jsonReader.readLocalDateTime();
            }
            final String str2 = jsonReader.readString();
            if (str2.isEmpty()) {
                return null;
            }
            if (this.formatMillis || this.formatUnixTime) {
                long millis = Long.parseLong(str2);
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                final Instant instant = Instant.ofEpochMilli(millis);
                return LocalDateTime.ofInstant(instant, context.getZoneId());
            }
            final DateTimeFormatter formatter2 = this.getDateFormatter(context.getLocale());
            if (!this.formatHasHour) {
                return LocalDateTime.of(LocalDate.parse(str2, formatter2), LocalTime.MIN);
            }
            if (!this.formatHasDay) {
                return LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(str2, formatter2));
            }
            return LocalDateTime.parse(str2, formatter2);
        }
    }
    
    static {
        INSTANCE = new ObjectReaderImplLocalDateTime(null, null);
    }
}
