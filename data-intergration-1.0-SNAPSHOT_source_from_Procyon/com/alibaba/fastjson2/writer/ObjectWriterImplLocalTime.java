// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplLocalTime extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplLocalTime INSTANCE;
    
    public ObjectWriterImplLocalTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeLocalTime((LocalTime)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final LocalTime time = (LocalTime)object;
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            final LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(1970, 1, 1), time);
            final Instant instant = dateTime.atZone(ctx.getZoneId()).toInstant();
            final long millis = instant.toEpochMilli();
            jsonWriter.writeInt64(millis);
            return;
        }
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            final LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(1970, 1, 1), time);
            final Instant instant = dateTime.atZone(ctx.getZoneId()).toInstant();
            final int seconds = (int)(instant.toEpochMilli() / 1000L);
            jsonWriter.writeInt32(seconds);
            return;
        }
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            final int hour = time.getHour();
            final int minute = time.getMinute();
            final int second = time.getSecond();
            final int nano = time.getNano();
            if (nano == 0) {
                jsonWriter.writeTimeHHMMSS8(hour, minute, second);
            }
            else {
                jsonWriter.writeLocalTime(time);
            }
            return;
        }
        String str;
        if (this.formatHasDay || ctx.isDateFormatHasDay()) {
            str = formatter.format(LocalDateTime.of(LocalDate.of(1970, 1, 1), time));
        }
        else {
            str = formatter.format(time);
        }
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplLocalTime(null, null);
    }
}
