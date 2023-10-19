// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.ZonedDateTime;
import java.time.DateTimeException;
import java.time.Instant;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Locale;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

abstract class FieldWriterDate<T> extends FieldWriter<T>
{
    protected DateTimeFormatter formatter;
    final boolean formatMillis;
    final boolean formatISO8601;
    final boolean formatyyyyMMdd8;
    final boolean formatyyyyMMddhhmmss14;
    final boolean formatyyyyMMddhhmmss19;
    final boolean formatUnixTime;
    protected ObjectWriter dateWriter;
    
    protected FieldWriterDate(final String fieldName, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method);
        boolean formatMillis = false;
        boolean formatISO8601 = false;
        boolean formatUnixTime = false;
        boolean formatyyyyMMdd8 = false;
        boolean formatyyyyMMddhhmmss14 = false;
        boolean formatyyyyMMddhhmmss15 = false;
        if (format != null) {
            switch (format) {
                case "millis": {
                    formatMillis = true;
                    break;
                }
                case "iso8601": {
                    formatISO8601 = true;
                    break;
                }
                case "unixtime": {
                    formatUnixTime = true;
                    break;
                }
                case "yyyy-MM-dd HH:mm:ss": {
                    formatyyyyMMddhhmmss15 = true;
                    break;
                }
                case "yyyyMMdd": {
                    formatyyyyMMdd8 = true;
                    break;
                }
                case "yyyyMMddHHmmss": {
                    formatyyyyMMddhhmmss14 = true;
                    break;
                }
            }
        }
        this.formatMillis = formatMillis;
        this.formatISO8601 = formatISO8601;
        this.formatUnixTime = formatUnixTime;
        this.formatyyyyMMdd8 = formatyyyyMMdd8;
        this.formatyyyyMMddhhmmss14 = formatyyyyMMddhhmmss14;
        this.formatyyyyMMddhhmmss19 = formatyyyyMMddhhmmss15;
    }
    
    @Override
    public boolean isDateFormatMillis() {
        return this.formatMillis;
    }
    
    @Override
    public boolean isDateFormatISO8601() {
        return this.formatISO8601;
    }
    
    public DateTimeFormatter getFormatter() {
        if (this.formatter == null && this.format != null && !this.formatMillis && !this.formatISO8601 && !this.formatUnixTime) {
            this.formatter = DateTimeFormatter.ofPattern(this.format);
        }
        return this.formatter;
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        if (valueClass == this.fieldClass) {
            final ObjectWriterProvider provider = jsonWriter.context.provider;
            if (this.dateWriter == null) {
                if ((provider.userDefineMask & 0x10L) != 0x0L) {
                    this.dateWriter = provider.getObjectWriter(valueClass, valueClass, false);
                }
                else {
                    if (this.format == null) {
                        return this.dateWriter = ObjectWriterImplDate.INSTANCE;
                    }
                    return this.dateWriter = new ObjectWriterImplDate(this.format, null);
                }
            }
            return this.dateWriter;
        }
        return jsonWriter.getObjectWriter(valueClass);
    }
    
    @Override
    public void writeDate(final JSONWriter jsonWriter, final long timeMillis) {
        if (jsonWriter.jsonb) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeMillis(timeMillis);
            return;
        }
        final int SECONDS_PER_DAY = 86400;
        final JSONWriter.Context ctx = jsonWriter.context;
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeInt64(timeMillis / 1000L);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeInt64(timeMillis);
            return;
        }
        final ZoneId zoneId = ctx.getZoneId();
        final String dateFormat = (this.format != null) ? this.format : ctx.getDateFormat();
        final boolean formatyyyyMMddhhmmss19 = this.formatyyyyMMddhhmmss19 || (ctx.isFormatyyyyMMddhhmmss19() && this.format == null);
        if (dateFormat == null || this.formatyyyyMMddhhmmss14 || formatyyyyMMddhhmmss19) {
            final long epochSecond = Math.floorDiv(timeMillis, 1000L);
            int offsetTotalSeconds;
            if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
                offsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(epochSecond);
            }
            else {
                final Instant instant = Instant.ofEpochMilli(timeMillis);
                offsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
            }
            final long localSecond = epochSecond + offsetTotalSeconds;
            final long localEpochDay = Math.floorDiv(localSecond, 86400L);
            final int secsOfDay = (int)Math.floorMod(localSecond, 86400L);
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long zeroDay = localEpochDay + 719528L;
            zeroDay -= 60L;
            long adjust = 0L;
            if (zeroDay < 0L) {
                final long adjustCycles = (zeroDay + 1L) / 146097L - 1L;
                adjust = adjustCycles * 400L;
                zeroDay += -adjustCycles * 146097L;
            }
            long yearEst = (400L * zeroDay + 591L) / 146097L;
            long doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
            if (doyEst < 0L) {
                --yearEst;
                doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
            }
            yearEst += adjust;
            final int marchDoy0 = (int)doyEst;
            final int marchMonth0 = (marchDoy0 * 5 + 2) / 153;
            final int month = (marchMonth0 + 2) % 12 + 1;
            final int dayOfMonth = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1;
            yearEst += marchMonth0 / 10;
            if (yearEst < -999999999L || yearEst > 999999999L) {
                throw new DateTimeException("Invalid year " + yearEst);
            }
            final int year = (int)yearEst;
            final int MINUTES_PER_HOUR = 60;
            final int SECONDS_PER_MINUTE = 60;
            final int SECONDS_PER_HOUR = 3600;
            long secondOfDay = secsOfDay;
            if (secondOfDay < 0L || secondOfDay > 86399L) {
                throw new DateTimeException("Invalid secondOfDay " + secondOfDay);
            }
            final int hours = (int)(secondOfDay / 3600L);
            secondOfDay -= hours * 3600;
            final int minutes = (int)(secondOfDay / 60L);
            secondOfDay -= minutes * 60;
            final int hour = hours;
            final int minute = minutes;
            final int second = (int)secondOfDay;
            if (year >= 0 && year <= 9999) {
                if (this.formatyyyyMMddhhmmss14) {
                    this.writeFieldName(jsonWriter);
                    jsonWriter.writeDateTime14(year, month, dayOfMonth, hour, minute, second);
                    return;
                }
                if (formatyyyyMMddhhmmss19) {
                    this.writeFieldName(jsonWriter);
                    jsonWriter.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
                    return;
                }
                final int millis = (int)Math.floorMod(timeMillis, 1000L);
                if (millis != 0) {
                    final Instant instant2 = Instant.ofEpochMilli(timeMillis);
                    final int offsetSeconds = ctx.getZoneId().getRules().getOffset(instant2).getTotalSeconds();
                    this.writeFieldName(jsonWriter);
                    jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, millis, offsetSeconds, false);
                    return;
                }
                this.writeFieldName(jsonWriter);
                jsonWriter.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
                return;
            }
        }
        this.writeFieldName(jsonWriter);
        final ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), zoneId);
        if (this.formatISO8601 || (ctx.isDateFormatISO8601() && this.format == null)) {
            final int year2 = zdt.getYear();
            if (year2 >= 0 && year2 <= 9999) {
                final int month2 = zdt.getMonthValue();
                final int dayOfMonth2 = zdt.getDayOfMonth();
                final int hour2 = zdt.getHour();
                final int minute2 = zdt.getMinute();
                final int second2 = zdt.getSecond();
                final int millis2 = zdt.getNano() / 1000000;
                final int offsetSeconds2 = zdt.getOffset().getTotalSeconds();
                jsonWriter.writeDateTimeISO8601(year2, month2, dayOfMonth2, hour2, minute2, second2, millis2, offsetSeconds2, true);
                return;
            }
        }
        if (this.formatyyyyMMdd8) {
            final int year2 = zdt.getYear();
            if (year2 >= 0 && year2 <= 9999) {
                final int month2 = zdt.getMonthValue();
                final int dayOfMonth2 = zdt.getDayOfMonth();
                jsonWriter.writeDateYYYMMDD8(year2, month2, dayOfMonth2);
                return;
            }
        }
        DateTimeFormatter formatter = this.getFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter != null) {
            jsonWriter.writeString(formatter.format(zdt));
        }
        else {
            jsonWriter.writeZonedDateTime(zdt);
        }
    }
}
