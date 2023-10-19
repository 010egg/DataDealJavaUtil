// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.time.OffsetDateTime;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectReaderImplOffsetDateTime extends DateTimeCodec implements ObjectReader
{
    static final ObjectReaderImplOffsetDateTime INSTANCE;
    
    public static ObjectReaderImplOffsetDateTime of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplOffsetDateTime.INSTANCE;
        }
        return new ObjectReaderImplOffsetDateTime(format, locale);
    }
    
    public ObjectReaderImplOffsetDateTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return OffsetDateTime.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZoneId zoneId = context.getZoneId();
            final LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);
            return OffsetDateTime.of(ldt, zoneId.getRules().getOffset(instant));
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (this.format == null || this.yyyyMMddhhmmss19 || this.formatISO8601) {
            return jsonReader.readOffsetDateTime();
        }
        final String str = jsonReader.readString();
        final ZoneId zoneId2 = context.getZoneId();
        if (this.formatMillis || this.formatUnixTime) {
            long millis2 = Long.parseLong(str);
            if (this.formatUnixTime) {
                millis2 *= 1000L;
            }
            final Instant instant2 = Instant.ofEpochMilli(millis2);
            final LocalDateTime ldt2 = LocalDateTime.ofInstant(instant2, zoneId2);
            return OffsetDateTime.of(ldt2, zoneId2.getRules().getOffset(instant2));
        }
        final DateTimeFormatter formatter = this.getDateFormatter(jsonReader.getLocale());
        if (!this.formatHasHour) {
            final LocalDateTime ldt3 = LocalDateTime.of(LocalDate.parse(str, formatter), LocalTime.MIN);
            return OffsetDateTime.of(ldt3, zoneId2.getRules().getOffset(ldt3));
        }
        if (!this.formatHasDay) {
            return ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(str, formatter), zoneId2);
        }
        final LocalDateTime ldt3 = LocalDateTime.parse(str, formatter);
        return OffsetDateTime.of(ldt3, zoneId2.getRules().getOffset(ldt3));
    }
    
    static {
        INSTANCE = new ObjectReaderImplOffsetDateTime(null, null);
    }
}
