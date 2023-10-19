// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.ZonedDateTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import java.util.function.Function;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplZonedDateTime extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplZonedDateTime INSTANCE;
    private final Function function;
    
    public ObjectWriterImplZonedDateTime(final String format, final Locale locale) {
        this(format, locale, null);
    }
    
    public ObjectWriterImplZonedDateTime(final String format, final Locale locale, final Function function) {
        super(format, locale);
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeZonedDateTime((ZonedDateTime)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        ZonedDateTime zdt;
        if (this.function != null) {
            zdt = this.function.apply(object);
        }
        else {
            zdt = (ZonedDateTime)object;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            final long millis = zdt.toInstant().toEpochMilli();
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            jsonWriter.writeInt64(zdt.toInstant().toEpochMilli());
            return;
        }
        final int year = zdt.getYear();
        if (year >= 0 && year <= 9999) {
            if (this.formatISO8601 || ctx.isDateFormatISO8601()) {
                jsonWriter.writeDateTimeISO8601(year, zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond(), zdt.getNano() / 1000000, zdt.getOffset().getTotalSeconds(), true);
                return;
            }
            if (this.yyyyMMddhhmmss19) {
                jsonWriter.writeDateTime19(year, zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond());
                return;
            }
            if (this.yyyyMMddhhmmss14) {
                jsonWriter.writeDateTime14(year, zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond());
                return;
            }
        }
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            jsonWriter.writeZonedDateTime(zdt);
            return;
        }
        final String str = formatter.format(zdt);
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplZonedDateTime(null, null);
    }
}
