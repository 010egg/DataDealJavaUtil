// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.time.ZoneOffset;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplDate extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplDate INSTANCE;
    static final char[] PREFIX_CHARS;
    static final byte[] PREFIX_BYTES;
    static final char[] PREFIX_CHARS_SQL;
    static final byte[] PREFIX_BYTES_SQL;
    
    public ObjectWriterImplDate(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeMillis(((Date)object).getTime());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final Date date = (Date)object;
        final long millis = date.getTime();
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            char end = ')';
            if (jsonWriter.utf16) {
                char[] prefix;
                if ("java.sql.Date".equals(date.getClass().getName())) {
                    prefix = ObjectWriterImplDate.PREFIX_CHARS_SQL;
                    end = '}';
                }
                else {
                    prefix = ObjectWriterImplDate.PREFIX_CHARS;
                }
                jsonWriter.writeRaw(prefix, 0, prefix.length);
            }
            else {
                byte[] prefix2;
                if ("java.sql.Date".equals(date.getClass().getName())) {
                    prefix2 = ObjectWriterImplDate.PREFIX_BYTES_SQL;
                    end = '}';
                }
                else {
                    prefix2 = ObjectWriterImplDate.PREFIX_BYTES;
                }
                jsonWriter.writeRaw(prefix2);
            }
            jsonWriter.writeInt64(millis);
            jsonWriter.writeRaw(end);
            return;
        }
        if (this.formatMillis || (this.format == null && ctx.isDateFormatMillis())) {
            jsonWriter.writeInt64(millis);
            return;
        }
        if (this.formatUnixTime || (this.format == null && ctx.isDateFormatUnixTime())) {
            jsonWriter.writeInt64(millis / 1000L);
            return;
        }
        final ZoneId zoneId = ctx.getZoneId();
        int offsetSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(Math.floorDiv(millis, 1000L));
        }
        else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
            offsetSeconds = 0;
        }
        else {
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
            offsetSeconds = zdt.getOffset().getTotalSeconds();
        }
        final boolean formatISO8601 = this.formatISO8601 || ctx.isDateFormatISO8601();
        String dateFormat;
        if (formatISO8601) {
            dateFormat = null;
        }
        else {
            dateFormat = this.format;
            if (dateFormat == null) {
                dateFormat = ctx.getDateFormat();
            }
        }
        if (dateFormat == null) {
            final int SECONDS_PER_DAY = 86400;
            final long epochSecond = Math.floorDiv(millis, 1000L);
            int offsetTotalSeconds;
            if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
                offsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(epochSecond);
            }
            else {
                final Instant instant = Instant.ofEpochMilli(millis);
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
                final int mos = (int)Math.floorMod(millis, 1000L);
                if (mos == 0 && !formatISO8601) {
                    if (hour == 0 && minute == 0 && second == 0 && "java.sql.Date".equals(date.getClass().getName())) {
                        jsonWriter.writeDateYYYMMDD10(year, month, dayOfMonth);
                    }
                    else {
                        jsonWriter.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
                    }
                }
                else {
                    jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, mos, offsetSeconds, formatISO8601);
                }
                return;
            }
        }
        DateTimeFormatter formatter;
        if (this.format != null) {
            formatter = this.getDateFormatter();
        }
        else {
            formatter = ctx.getDateFormatter();
        }
        final ZonedDateTime zdt2 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
        final String str = formatter.format(zdt2);
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterImplDate(null, null);
        PREFIX_CHARS = "new Date(".toCharArray();
        PREFIX_BYTES = "new Date(".getBytes(StandardCharsets.UTF_8);
        PREFIX_CHARS_SQL = "{\"@type\":\"java.sql.Date\",\"val\":".toCharArray();
        PREFIX_BYTES_SQL = "{\"@type\":\"java.sql.Date\",\"val\":".getBytes(StandardCharsets.UTF_8);
    }
}
