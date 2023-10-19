// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.time.temporal.TemporalAccessor;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import java.text.ParseException;
import com.alibaba.fastjson2.JSONException;
import java.text.SimpleDateFormat;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Date;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

public class ObjectReaderImplDate extends DateTimeCodec implements ObjectReader
{
    public static final ObjectReaderImplDate INSTANCE;
    
    public static ObjectReaderImplDate of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectReaderImplDate.INSTANCE;
        }
        return new ObjectReaderImplDate(format, locale);
    }
    
    public ObjectReaderImplDate(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public Class getObjectClass() {
        return Date.class;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            return new Date(millis);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        return this.readDate(jsonReader);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.isInt()) {
            long millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
            return new Date(millis);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        return this.readDate(jsonReader);
    }
    
    private Object readDate(final JSONReader jsonReader) {
        if (this.useSimpleFormatter) {
            final String str = jsonReader.readString();
            try {
                return new SimpleDateFormat(this.format).parse(str);
            }
            catch (ParseException e) {
                throw new JSONException(jsonReader.info("parse error : " + str), e);
            }
        }
        if (jsonReader.nextIfNullOrEmptyString()) {
            return null;
        }
        long millis;
        if ((this.formatUnixTime || this.formatMillis) && jsonReader.isString()) {
            millis = jsonReader.readInt64Value();
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
        }
        else if (this.format != null) {
            ZonedDateTime zdt;
            if (this.yyyyMMddhhmmss19) {
                if (jsonReader.isSupportSmartMatch()) {
                    millis = jsonReader.readMillisFromString();
                }
                else {
                    millis = jsonReader.readMillis19();
                }
                if (millis != 0L || !jsonReader.wasNull()) {
                    return new Date(millis);
                }
                zdt = jsonReader.readZonedDateTime();
            }
            else {
                final DateTimeFormatter formatter = this.getDateFormatter(jsonReader.getLocale());
                if (formatter != null) {
                    final String str2 = jsonReader.readString();
                    if (str2.isEmpty() || "null".equals(str2)) {
                        return null;
                    }
                    LocalDateTime ldt;
                    if (!this.formatHasHour) {
                        if (!this.formatHasDay) {
                            final TemporalAccessor parsed = formatter.parse(str2);
                            final int year = parsed.get(ChronoField.YEAR);
                            final int month = parsed.get(ChronoField.MONTH_OF_YEAR);
                            final int dayOfYear = 1;
                            ldt = LocalDateTime.of(LocalDate.of(year, month, dayOfYear), LocalTime.MIN);
                        }
                        else if (str2.length() == 19 && jsonReader.isEnabled(JSONReader.Feature.SupportSmartMatch)) {
                            ldt = DateUtils.parseLocalDateTime(str2, 0, str2.length());
                        }
                        else {
                            if (this.format.indexOf(45) != -1 && str2.indexOf(45) == -1 && TypeUtils.isInteger(str2)) {
                                millis = Long.parseLong(str2);
                                return new Date(millis);
                            }
                            final LocalDate localDate = LocalDate.parse(str2, formatter);
                            ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                        }
                    }
                    else if (str2.length() == 19 && (this.yyyyMMddhhmm16 || jsonReader.isEnabled(JSONReader.Feature.SupportSmartMatch))) {
                        final int length = this.yyyyMMddhhmm16 ? 16 : 19;
                        ldt = DateUtils.parseLocalDateTime(str2, 0, length);
                    }
                    else {
                        ldt = LocalDateTime.parse(str2, formatter);
                    }
                    zdt = ldt.atZone(jsonReader.getContext().getZoneId());
                }
                else {
                    zdt = jsonReader.readZonedDateTime();
                }
            }
            if (zdt == null) {
                return null;
            }
            final long seconds = zdt.toEpochSecond();
            final int nanos = zdt.toLocalTime().getNano();
            if (seconds < 0L && nanos > 0) {
                millis = (seconds + 1L) * 1000L;
                final long adjustment = nanos / 1000000 - 1000;
                millis += adjustment;
            }
            else {
                millis = seconds * 1000L;
                millis += nanos / 1000000;
            }
        }
        else {
            if (jsonReader.isTypeRedirect() && jsonReader.nextIfMatchIdent('\"', 'v', 'a', 'l', '\"')) {
                jsonReader.nextIfMatch(':');
                millis = jsonReader.readInt64Value();
                jsonReader.nextIfObjectEnd();
                jsonReader.setTypeRedirect(false);
            }
            else {
                millis = jsonReader.readMillisFromString();
            }
            if (millis == 0L && jsonReader.wasNull()) {
                return null;
            }
            if (this.formatUnixTime) {
                millis *= 1000L;
            }
        }
        return new Date(millis);
    }
    
    static {
        INSTANCE = new ObjectReaderImplDate(null, null);
    }
}
