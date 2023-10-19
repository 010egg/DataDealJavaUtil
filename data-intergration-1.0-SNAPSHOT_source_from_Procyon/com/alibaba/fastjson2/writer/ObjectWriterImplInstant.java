// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.ZonedDateTime;
import java.time.DateTimeException;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplInstant extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplInstant INSTANCE;
    
    public ObjectWriterImplInstant(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        jsonWriter.writeInstant((Instant)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context context = jsonWriter.context;
        final String dateFormat = (this.format != null) ? this.format : context.getDateFormat();
        final Instant instant = (Instant)object;
        if (dateFormat == null) {
            jsonWriter.writeInstant(instant);
            return;
        }
        final boolean yyyyMMddhhmmss19 = this.yyyyMMddhhmmss19 || (context.isFormatyyyyMMddhhmmss19() && this.format == null);
        if (this.yyyyMMddhhmmss14 || yyyyMMddhhmmss19 || this.yyyyMMdd8 || this.yyyyMMdd10) {
            final int SECONDS_PER_DAY = 86400;
            final ZoneId zoneId = context.getZoneId();
            final long epochSecond = instant.getEpochSecond();
            int offsetTotalSeconds;
            if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
                offsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(epochSecond);
            }
            else {
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
            if (yyyyMMddhhmmss19) {
                jsonWriter.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
                return;
            }
            if (this.yyyyMMddhhmmss14) {
                jsonWriter.writeDateTime14(year, month, dayOfMonth, hour, minute, second);
                return;
            }
            if (this.yyyyMMdd10) {
                jsonWriter.writeDateYYYMMDD10(year, month, dayOfMonth);
                return;
            }
            jsonWriter.writeDateYYYMMDD8(year, month, dayOfMonth);
        }
        else {
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, context.getZoneId());
            if (this.formatUnixTime || (this.format == null && context.isDateFormatUnixTime())) {
                final long millis = zdt.toInstant().toEpochMilli();
                jsonWriter.writeInt64(millis / 1000L);
                return;
            }
            if (this.formatMillis || (this.format == null && context.isDateFormatMillis())) {
                jsonWriter.writeInt64(zdt.toInstant().toEpochMilli());
                return;
            }
            final int year2 = zdt.getYear();
            if (year2 >= 0 && year2 <= 9999 && (this.formatISO8601 || (this.format == null && context.isDateFormatISO8601()))) {
                jsonWriter.writeDateTimeISO8601(year2, zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond(), zdt.getNano() / 1000000, zdt.getOffset().getTotalSeconds(), true);
                return;
            }
            DateTimeFormatter formatter = this.getDateFormatter();
            if (formatter == null) {
                formatter = context.getDateFormatter();
            }
            if (formatter == null) {
                jsonWriter.writeZonedDateTime(zdt);
            }
            else {
                final String str = formatter.format(zdt);
                jsonWriter.writeString(str);
            }
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInstant(null, null);
    }
}
