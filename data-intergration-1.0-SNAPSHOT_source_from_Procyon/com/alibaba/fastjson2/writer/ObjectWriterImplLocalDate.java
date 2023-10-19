// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplLocalDate extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplLocalDate INSTANCE;
    
    private ObjectWriterImplLocalDate(final String format, final Locale locale) {
        super(format, locale);
    }
    
    public static ObjectWriterImplLocalDate of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectWriterImplLocalDate.INSTANCE;
        }
        return new ObjectWriterImplLocalDate(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeLocalDate((LocalDate)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final LocalDate date = (LocalDate)object;
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            final LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            final long millis = dateTime.atZone(ctx.getZoneId()).toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            final LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            final long millis = dateTime.atZone(ctx.getZoneId()).toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis);
            return;
        }
        if (this.yyyyMMdd8) {
            jsonWriter.writeDateYYYMMDD8(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            return;
        }
        if (this.yyyyMMdd10) {
            jsonWriter.writeDateYYYMMDD10(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            return;
        }
        if (this.yyyyMMddhhmmss19) {
            jsonWriter.writeDateTime19(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0, 0);
            return;
        }
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            jsonWriter.writeDateYYYMMDD10(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            return;
        }
        String str;
        if (this.formatHasHour || ctx.isDateFormatHasHour()) {
            str = formatter.format(LocalDateTime.of(date, LocalTime.MIN));
        }
        else {
            str = formatter.format(date);
        }
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplLocalDate(null, null);
    }
}
