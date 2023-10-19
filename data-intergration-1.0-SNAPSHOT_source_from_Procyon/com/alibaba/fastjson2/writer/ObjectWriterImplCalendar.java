// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.util.Calendar;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplCalendar extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplCalendar INSTANCE;
    
    public ObjectWriterImplCalendar(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final long millis = ((Calendar)object).getTimeInMillis();
        jsonWriter.writeMillis(millis);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final Calendar date = (Calendar)object;
        final long millis = date.getTimeInMillis();
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        if (this.format == null && ctx.isDateFormatMillis()) {
            jsonWriter.writeInt64(millis);
            return;
        }
        final ZoneId zoneId = ctx.getZoneId();
        final Instant instant = Instant.ofEpochMilli(millis);
        final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
        final int offsetSeconds = zdt.getOffset().getTotalSeconds();
        final int year = zdt.getYear();
        if (year >= 0 && year <= 9999) {
            if (this.format == null && ctx.isDateFormatISO8601()) {
                final int month = zdt.getMonthValue();
                final int dayOfMonth = zdt.getDayOfMonth();
                final int hour = zdt.getHour();
                final int minute = zdt.getMinute();
                final int second = zdt.getSecond();
                final int nano = zdt.getNano() / 1000000;
                jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, nano, offsetSeconds, true);
                return;
            }
            final String dateFormat = (this.format == null) ? ctx.getDateFormat() : this.format;
            if (dateFormat == null) {
                final int month2 = zdt.getMonthValue();
                final int dayOfMonth2 = zdt.getDayOfMonth();
                final int hour2 = zdt.getHour();
                final int minute2 = zdt.getMinute();
                final int second2 = zdt.getSecond();
                final int nano2 = zdt.getNano();
                if (nano2 == 0) {
                    jsonWriter.writeDateTime19(year, month2, dayOfMonth2, hour2, minute2, second2);
                }
                else {
                    jsonWriter.writeDateTimeISO8601(year, month2, dayOfMonth2, hour2, minute2, second2, nano2 / 1000000, offsetSeconds, false);
                }
                return;
            }
        }
        DateTimeFormatter dateFormatter;
        if (this.format != null) {
            dateFormatter = this.getDateFormatter();
        }
        else {
            dateFormatter = ctx.getDateFormatter();
        }
        if (dateFormatter == null) {
            jsonWriter.writeZonedDateTime(zdt);
            return;
        }
        final String str = dateFormatter.format(zdt);
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplCalendar(null, null);
    }
}
