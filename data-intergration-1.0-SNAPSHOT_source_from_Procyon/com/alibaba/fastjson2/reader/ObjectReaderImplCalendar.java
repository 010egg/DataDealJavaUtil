// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Calendar;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectReaderImplCalendar extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplCalendar INSTANCE;
    
    public static ObjectReaderImplCalendar of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplCalendar.INSTANCE;
        }
        return new ObjectReaderImplCalendar(format, locale);
    }
    
    public ObjectReaderImplCalendar(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return Calendar.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar;
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        long millis = jsonReader.readMillisFromString();
        if (this.formatUnixTime) {
            millis *= 1000L;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.isString()) {
            if (this.format != null) {
                final DateTimeFormatter formatter = this.getDateFormatter();
                if (formatter != null) {
                    final String str = jsonReader.readString();
                    if (str.isEmpty()) {
                        return null;
                    }
                    final LocalDateTime ldt = LocalDateTime.parse(str, formatter);
                    final ZonedDateTime zdt = ZonedDateTime.of(ldt, jsonReader.getContext().getZoneId());
                    final long millis = zdt.toInstant().toEpochMilli();
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(millis);
                    return calendar;
                }
            }
            long millis2 = jsonReader.readMillisFromString();
            if (millis2 == 0L && jsonReader.wasNull()) {
                return null;
            }
            if (this.formatUnixTime) {
                millis2 *= 1000L;
            }
            final Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(millis2);
            return calendar2;
        }
        else {
            if (jsonReader.readIfNull()) {
                return null;
            }
            long millis2 = jsonReader.readInt64Value();
            if (this.formatUnixTime || jsonReader.getContext().isFormatUnixTime()) {
                millis2 *= 1000L;
            }
            final Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(millis2);
            return calendar2;
        }
    }
    
    static {
        INSTANCE = new ObjectReaderImplCalendar(null, null);
    }
}
