// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.time.LocalTime;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

class ObjectReaderImplLocalTime extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplLocalTime INSTANCE;
    
    public ObjectReaderImplLocalTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return LocalTime.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readLocalTime();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZoneId zoneId = context.getZoneId();
            return LocalDateTime.ofInstant(instant, zoneId).toLocalTime();
        }
        if (this.format == null || jsonReader.isNumber()) {
            return jsonReader.readLocalTime();
        }
        if (this.yyyyMMddhhmmss19 || this.formatISO8601) {
            return jsonReader.readLocalDateTime().toLocalTime();
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
            final Instant instant2 = Instant.ofEpochMilli(millis2);
            return LocalDateTime.ofInstant(instant2, context.getZoneId()).toLocalTime();
        }
        final DateTimeFormatter formatter = this.getDateFormatter(context.getLocale());
        if (this.formatHasDay) {
            return LocalDateTime.parse(str, formatter).toLocalTime();
        }
        return LocalTime.parse(str, formatter);
    }
    
    static {
        INSTANCE = new ObjectReaderImplLocalTime(null, null);
    }
}
