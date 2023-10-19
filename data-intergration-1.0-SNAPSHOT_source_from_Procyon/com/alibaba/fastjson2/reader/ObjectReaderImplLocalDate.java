// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.time.LocalDate;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

class ObjectReaderImplLocalDate extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplLocalDate INSTANCE;
    
    public ObjectReaderImplLocalDate(final String format, final Locale locale) {
        super(format, locale);
    }
    
    public static ObjectReaderImplLocalDate of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplLocalDate.INSTANCE;
        }
        return new ObjectReaderImplLocalDate(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return LocalDate.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readLocalDate();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (this.format == null || this.yyyyMMddhhmmss19 || this.formatISO8601 || jsonReader.isNumber()) {
            return jsonReader.readLocalDate();
        }
        final String str = jsonReader.readString();
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        if (this.formatMillis || this.formatUnixTime) {
            long millis = Long.parseLong(str);
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            return LocalDateTime.ofInstant(instant, context.getZoneId()).toLocalDate();
        }
        final DateTimeFormatter formatter = this.getDateFormatter(context.getLocale());
        if (!this.formatHasHour) {
            return LocalDate.parse(str, formatter);
        }
        if (!this.formatHasDay) {
            return LocalDate.of(1970, 1, 1);
        }
        return LocalDateTime.parse(str, formatter).toLocalDate();
    }
    
    static {
        INSTANCE = new ObjectReaderImplLocalDate(null, null);
    }
}
