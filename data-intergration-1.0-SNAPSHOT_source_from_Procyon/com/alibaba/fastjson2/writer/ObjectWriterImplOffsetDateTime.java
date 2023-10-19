// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.OffsetDateTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplOffsetDateTime extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplOffsetDateTime INSTANCE;
    
    private ObjectWriterImplOffsetDateTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    public static ObjectWriterImplOffsetDateTime of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectWriterImplOffsetDateTime.INSTANCE;
        }
        return new ObjectWriterImplOffsetDateTime(format, locale);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final OffsetDateTime odt = (OffsetDateTime)object;
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            final long millis = odt.toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            final long millis = odt.toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis);
            return;
        }
        final int year = odt.getYear();
        if (year >= 0 && year <= 9999) {
            if (this.formatISO8601 || ctx.isDateFormatISO8601()) {
                jsonWriter.writeDateTimeISO8601(year, odt.getMonthValue(), odt.getDayOfMonth(), odt.getHour(), odt.getMinute(), odt.getSecond(), odt.getNano() / 1000000, odt.getOffset().getTotalSeconds(), true);
                return;
            }
            if (this.yyyyMMddhhmmss19) {
                jsonWriter.writeDateTime19(year, odt.getMonthValue(), odt.getDayOfMonth(), odt.getHour(), odt.getMinute(), odt.getSecond());
                return;
            }
            if (this.yyyyMMddhhmmss14) {
                jsonWriter.writeDateTime14(year, odt.getMonthValue(), odt.getDayOfMonth(), odt.getHour(), odt.getMinute(), odt.getSecond());
                return;
            }
        }
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            jsonWriter.writeOffsetDateTime(odt);
            return;
        }
        final String str = formatter.format(odt);
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplOffsetDateTime(null, null);
    }
}
