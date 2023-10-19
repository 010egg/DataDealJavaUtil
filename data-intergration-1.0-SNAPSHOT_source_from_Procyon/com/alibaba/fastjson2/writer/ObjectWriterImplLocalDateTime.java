// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplLocalDateTime extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplLocalDateTime INSTANCE;
    
    public ObjectWriterImplLocalDateTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeLocalDateTime((LocalDateTime)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final LocalDateTime ldt = (LocalDateTime)object;
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            final long millis = ldt.atZone(ctx.getZoneId()).toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            final long millis = ldt.atZone(ctx.getZoneId()).toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis);
            return;
        }
        final int year = ldt.getYear();
        if (year >= 0 && year <= 9999) {
            if (this.formatISO8601 || (this.format == null && ctx.isDateFormatISO8601())) {
                final int month = ldt.getMonthValue();
                final int dayOfMonth = ldt.getDayOfMonth();
                final int hour = ldt.getHour();
                final int minute = ldt.getMinute();
                final int second = ldt.getSecond();
                final int nano = ldt.getNano() / 1000000;
                final int offsetSeconds = ctx.getZoneId().getRules().getOffset(ldt).getTotalSeconds();
                jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, nano, offsetSeconds, true);
                return;
            }
            if (this.yyyyMMddhhmmss19) {
                jsonWriter.writeDateTime19(year, ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
                return;
            }
            if (this.yyyyMMddhhmmss14) {
                jsonWriter.writeDateTime14(year, ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
                return;
            }
            if (this.yyyyMMdd8) {
                jsonWriter.writeDateYYYMMDD8(year, ldt.getMonthValue(), ldt.getDayOfMonth());
                return;
            }
            if (this.yyyyMMdd10) {
                jsonWriter.writeDateYYYMMDD10(year, ldt.getMonthValue(), ldt.getDayOfMonth());
                return;
            }
        }
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            jsonWriter.writeLocalDateTime(ldt);
            return;
        }
        final String str = formatter.format(ldt);
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplLocalDateTime(null, null);
    }
}
