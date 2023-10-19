// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONException;
import java.time.temporal.TemporalAccessor;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.TimeZone;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReaderImplDate;
import com.alibaba.fastjson2.JSONReader;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.zone.ZoneRules;
import java.time.ZoneId;

public class DateUtils
{
    public static final ZoneId DEFAULT_ZONE_ID;
    public static final String SHANGHAI_ZONE_ID_NAME = "Asia/Shanghai";
    public static final ZoneId SHANGHAI_ZONE_ID;
    public static final ZoneRules SHANGHAI_ZONE_RULES;
    public static final String OFFSET_8_ZONE_ID_NAME = "+08:00";
    public static final ZoneId OFFSET_8_ZONE_ID;
    public static final LocalDate LOCAL_DATE_19700101;
    static DateTimeFormatter DATE_TIME_FORMATTER_34;
    static DateTimeFormatter DATE_TIME_FORMATTER_COOKIE;
    static DateTimeFormatter DATE_TIME_FORMATTER_COOKIE_LOCAL;
    static DateTimeFormatter DATE_TIME_FORMATTER_RFC_2822;
    static final int LOCAL_EPOCH_DAY;
    
    public static Date parseDateYMDHMS19(final String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        final long millis = parseMillisYMDHMS19(str, DateUtils.DEFAULT_ZONE_ID);
        return new Date(millis);
    }
    
    public static Date parseDate(final String str, final String format) {
        return parseDate(str, format, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static Date parseDate(final String str, final String format, ZoneId zoneId) {
        if (str == null || str.isEmpty() || "null".equals(str)) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            final long millis = parseMillis(str, zoneId);
            if (millis == 0L) {
                return null;
            }
            return new Date(millis);
        }
        else {
            switch (format) {
                case "yyyy-MM-dd'T'HH:mm:ss": {
                    final long millis2 = parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
                    return new Date(millis2);
                }
                case "yyyy-MM-dd HH:mm:ss": {
                    final long millis2 = parseMillisYMDHMS19(str, zoneId);
                    return new Date(millis2);
                }
                case "yyyy/MM/dd HH:mm:ss": {
                    final long millis2 = parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH);
                    return new Date(millis2);
                }
                case "dd.MM.yyyy HH:mm:ss": {
                    final long millis2 = parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT);
                    return new Date(millis2);
                }
                case "yyyy-MM-dd": {
                    final long millis2 = parseMillis10(str, zoneId, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
                    return new Date(millis2);
                }
                case "yyyy/MM/dd": {
                    final long millis2 = parseMillis10(str, zoneId, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
                    return new Date(millis2);
                }
                case "yyyyMMdd": {
                    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                    final LocalDate ldt = LocalDate.parse(str, formatter);
                    final long millis3 = millis(zoneId, ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), 0, 0, 0, 0);
                    return new Date(millis3);
                }
                case "yyyyMMddHHmmssSSSZ": {
                    final long millis2 = parseMillis(str, DateUtils.DEFAULT_ZONE_ID);
                    return new Date(millis2);
                }
                case "iso8601": {
                    return parseDate(str);
                }
                default: {
                    if (zoneId == null) {
                        zoneId = DateUtils.DEFAULT_ZONE_ID;
                    }
                    final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(format);
                    final LocalDateTime ldt2 = LocalDateTime.parse(str, formatter2);
                    final long millis2 = millis(ldt2, zoneId);
                    return new Date(millis2);
                }
            }
        }
    }
    
    public static Date parseDate(final String str) {
        final long millis = parseMillis(str, DateUtils.DEFAULT_ZONE_ID);
        if (millis == 0L) {
            return null;
        }
        return new Date(millis);
    }
    
    public static Date parseDate(final String str, final ZoneId zoneId) {
        final long millis = parseMillis(str, zoneId);
        if (millis == 0L) {
            return null;
        }
        return new Date(millis);
    }
    
    public static long parseMillis(final String str) {
        return parseMillis(str, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static long parseMillis(final String str, final ZoneId zoneId) {
        if (str == null) {
            return 0L;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            return parseMillis(bytes, 0, bytes.length, StandardCharsets.ISO_8859_1, zoneId);
        }
        final char[] chars = JDKUtils.getCharArray(str);
        return parseMillis(chars, 0, chars.length, zoneId);
    }
    
    public static LocalDateTime parseLocalDateTime(final String str) {
        if (str == null) {
            return null;
        }
        return parseLocalDateTime(str, 0, str.length());
    }
    
    public static LocalDateTime parseLocalDateTime(final String str, final int off, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        LocalDateTime ldt;
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            ldt = parseLocalDateTime(bytes, off, len);
        }
        else if (JDKUtils.JVM_VERSION == 8 && !JDKUtils.FIELD_STRING_VALUE_ERROR) {
            final char[] chars = JDKUtils.getCharArray(str);
            ldt = parseLocalDateTime(chars, off, len);
        }
        else {
            final char[] chars = new char[len];
            str.getChars(off, off + len, chars, 0);
            ldt = parseLocalDateTime(chars, off, len);
        }
        if (ldt != null) {
            return ldt;
        }
        switch (str) {
            case "":
            case "null":
            case "00000000":
            case "000000000000":
            case "0000\u5e7400\u670800\u65e5":
            case "0000-0-00":
            case "0000-00-0":
            case "0000-00-00": {
                return null;
            }
            default: {
                throw new DateTimeParseException(str, str, off);
            }
        }
    }
    
    public static LocalDateTime parseLocalDateTime(final char[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        switch (len) {
            case 4: {
                if (str[off] == 'n' && str[off + 1] == 'u' && str[off + 2] == 'l' && str[off + 3] == 'l') {
                    return null;
                }
                final String input = new String(str, off, len);
                throw new DateTimeParseException("illegal input " + input, input, 0);
            }
            case 8: {
                if (str[2] == ':' && str[5] == ':') {
                    final LocalTime localTime = parseLocalTime8(str, off);
                    return LocalDateTime.of(DateUtils.LOCAL_DATE_19700101, localTime);
                }
                final LocalDate localDate = parseLocalDate8(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 9: {
                final LocalDate localDate = parseLocalDate9(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 10: {
                final LocalDate localDate = parseLocalDate10(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 11: {
                final LocalDate localDate = parseLocalDate11(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 12: {
                return parseLocalDateTime12(str, off);
            }
            case 14: {
                return parseLocalDateTime14(str, off);
            }
            case 16: {
                return parseLocalDateTime16(str, off);
            }
            case 17: {
                return parseLocalDateTime17(str, off);
            }
            case 18: {
                return parseLocalDateTime18(str, off);
            }
            case 19: {
                return parseLocalDateTime19(str, off);
            }
            case 20: {
                return parseLocalDateTime20(str, off);
            }
            default: {
                return parseLocalDateTimeX(str, off, len);
            }
        }
    }
    
    public static LocalTime parseLocalTime5(final byte[] bytes, final int off) {
        if (off + 5 > bytes.length) {
            return null;
        }
        final byte c0 = bytes[off];
        final byte c2 = bytes[off + 1];
        final byte c3 = bytes[off + 2];
        final byte c4 = bytes[off + 3];
        final byte c5 = bytes[off + 4];
        if (c3 != 58) {
            return null;
        }
        final byte h0 = c0;
        final byte h2 = c2;
        final byte i0 = c4;
        final byte i2 = c5;
        if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
            return null;
        }
        final int hour = (h0 - 48) * 10 + (h2 - 48);
        if (i0 >= 48 && i0 <= 57 && i2 >= 48 && i2 <= 57) {
            final int minute = (i0 - 48) * 10 + (i2 - 48);
            return LocalTime.of(hour, minute);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime5(final char[] chars, final int off) {
        if (off + 5 > chars.length) {
            return null;
        }
        final char c0 = chars[off];
        final char c2 = chars[off + 1];
        final char c3 = chars[off + 2];
        final char c4 = chars[off + 3];
        final char c5 = chars[off + 4];
        if (c3 != ':') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c2;
        final char i0 = c4;
        final char i2 = c5;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 >= '0' && i0 <= '9' && i2 >= '0' && i2 <= '9') {
            final int minute = (i0 - '0') * 10 + (i2 - '0');
            return LocalTime.of(hour, minute);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime8(final byte[] bytes, final int off) {
        if (off + 8 > bytes.length) {
            return null;
        }
        final char c0 = (char)bytes[off];
        final char c2 = (char)bytes[off + 1];
        final char c3 = (char)bytes[off + 2];
        final char c4 = (char)bytes[off + 3];
        final char c5 = (char)bytes[off + 4];
        final char c6 = (char)bytes[off + 5];
        final char c7 = (char)bytes[off + 6];
        final char c8 = (char)bytes[off + 7];
        return parseLocalTime(c0, c2, c3, c4, c5, c6, c7, c8);
    }
    
    public static LocalTime parseLocalTime8(final char[] bytes, final int off) {
        if (off + 8 > bytes.length) {
            return null;
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        return parseLocalTime(c0, c2, c3, c4, c5, c6, c7, c8);
    }
    
    public static LocalTime parseLocalTime(final char c0, final char c1, final char c2, final char c3, final char c4, final char c5, final char c6, final char c7) {
        if (c2 != ':' || c5 != ':') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c1;
        final char i0 = c3;
        final char i2 = c4;
        final char s0 = c6;
        final char s2 = c7;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalTime.of(hour, minute, second);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime10(final byte[] bytes, final int off) {
        if (off + 10 > bytes.length) {
            return null;
        }
        final byte c0 = bytes[off];
        final byte c2 = bytes[off + 1];
        final byte c3 = bytes[off + 2];
        final byte c4 = bytes[off + 3];
        final byte c5 = bytes[off + 4];
        final byte c6 = bytes[off + 5];
        final byte c7 = bytes[off + 6];
        final byte c8 = bytes[off + 7];
        final byte c9 = bytes[off + 8];
        final byte c10 = bytes[off + 9];
        if (c3 != 58 || c6 != 58 || c9 != 46) {
            return null;
        }
        final byte h0 = c0;
        final byte h2 = c2;
        final byte i0 = c4;
        final byte i2 = c5;
        final byte s0 = c7;
        final byte s2 = c8;
        final byte m0 = c10;
        if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
            return null;
        }
        final int hour = (h0 - 48) * 10 + (h2 - 48);
        if (i0 < 48 || i0 > 57 || i2 < 48 || i2 > 57) {
            return null;
        }
        final int minute = (i0 - 48) * 10 + (i2 - 48);
        if (s0 < 48 || s0 > 57 || s2 < 48 || s2 > 57) {
            return null;
        }
        final int second = (s0 - 48) * 10 + (s2 - 48);
        if (m0 >= 48 && m0 <= 57) {
            int millis = (m0 - 48) * 100;
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime10(final char[] bytes, final int off) {
        if (off + 10 > bytes.length) {
            return null;
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        final char c9 = bytes[off + 8];
        final char c10 = bytes[off + 9];
        if (c3 != ':' || c6 != ':' || c9 != '.') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c2;
        final char i0 = c4;
        final char i2 = c5;
        final char s0 = c7;
        final char s2 = c8;
        final char m0 = c10;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s2 - '0');
        if (m0 >= '0' && m0 <= '9') {
            int millis = (m0 - '0') * 100;
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime11(final byte[] bytes, final int off) {
        if (off + 11 > bytes.length) {
            return null;
        }
        final byte c0 = bytes[off];
        final byte c2 = bytes[off + 1];
        final byte c3 = bytes[off + 2];
        final byte c4 = bytes[off + 3];
        final byte c5 = bytes[off + 4];
        final byte c6 = bytes[off + 5];
        final byte c7 = bytes[off + 6];
        final byte c8 = bytes[off + 7];
        final byte c9 = bytes[off + 8];
        final byte c10 = bytes[off + 9];
        final byte c11 = bytes[off + 10];
        if (c3 != 58 || c6 != 58 || c9 != 46) {
            return null;
        }
        final byte h0 = c0;
        final byte h2 = c2;
        final byte i0 = c4;
        final byte i2 = c5;
        final byte s0 = c7;
        final byte s2 = c8;
        final byte m0 = c10;
        final byte m2 = c11;
        if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
            return null;
        }
        final int hour = (h0 - 48) * 10 + (h2 - 48);
        if (i0 < 48 || i0 > 57 || i2 < 48 || i2 > 57) {
            return null;
        }
        final int minute = (i0 - 48) * 10 + (i2 - 48);
        if (s0 < 48 || s0 > 57 || s2 < 48 || s2 > 57) {
            return null;
        }
        final int second = (s0 - 48) * 10 + (s2 - 48);
        if (m0 >= 48 && m0 <= 57 && m2 >= 48 && m2 <= 57) {
            int millis = (m0 - 48) * 100 + (m2 - 48) * 10;
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime11(final char[] bytes, final int off) {
        if (off + 11 > bytes.length) {
            return null;
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        final char c9 = bytes[off + 8];
        final char c10 = bytes[off + 9];
        final char c11 = bytes[off + 10];
        if (c3 != ':' || c6 != ':' || c9 != '.') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c2;
        final char i0 = c4;
        final char i2 = c5;
        final char s0 = c7;
        final char s2 = c8;
        final char m0 = c10;
        final char m2 = c11;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s2 - '0');
        if (m0 >= '0' && m0 <= '9' && m2 >= '0' && m2 <= '9') {
            int millis = (m0 - '0') * 100 + (m2 - '0') * 10;
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime12(final byte[] bytes, final int off) {
        if (off + 12 > bytes.length) {
            return null;
        }
        final byte c0 = bytes[off];
        final byte c2 = bytes[off + 1];
        final byte c3 = bytes[off + 2];
        final byte c4 = bytes[off + 3];
        final byte c5 = bytes[off + 4];
        final byte c6 = bytes[off + 5];
        final byte c7 = bytes[off + 6];
        final byte c8 = bytes[off + 7];
        final byte c9 = bytes[off + 8];
        final byte c10 = bytes[off + 9];
        final byte c11 = bytes[off + 10];
        final byte c12 = bytes[off + 11];
        if (c3 != 58 || c6 != 58 || c9 != 46) {
            return null;
        }
        final byte h0 = c0;
        final byte h2 = c2;
        final byte i0 = c4;
        final byte i2 = c5;
        final byte s0 = c7;
        final byte s2 = c8;
        final byte m0 = c10;
        final byte m2 = c11;
        final byte m3 = c12;
        if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
            return null;
        }
        final int hour = (h0 - 48) * 10 + (h2 - 48);
        if (i0 < 48 || i0 > 57 || i2 < 48 || i2 > 57) {
            return null;
        }
        final int minute = (i0 - 48) * 10 + (i2 - 48);
        if (s0 < 48 || s0 > 57 || s2 < 48 || s2 > 57) {
            return null;
        }
        final int second = (s0 - 48) * 10 + (s2 - 48);
        if (m0 >= 48 && m0 <= 57 && m2 >= 48 && m2 <= 57 && m3 >= 48 && m3 <= 57) {
            int millis = (m0 - 48) * 100 + (m2 - 48) * 10 + (m3 - 48);
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime12(final char[] bytes, final int off) {
        if (off + 12 > bytes.length) {
            return null;
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        final char c9 = bytes[off + 8];
        final char c10 = bytes[off + 9];
        final char c11 = bytes[off + 10];
        final char c12 = bytes[off + 11];
        if (c3 != ':' || c6 != ':' || c9 != '.') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c2;
        final char i0 = c4;
        final char i2 = c5;
        final char s0 = c7;
        final char s2 = c8;
        final char m0 = c10;
        final char m2 = c11;
        final char m3 = c12;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s2 - '0');
        if (m0 >= '0' && m0 <= '9' && m2 >= '0' && m2 <= '9' && m3 >= '0' && m3 <= '9') {
            int millis = (m0 - '0') * 100 + (m2 - '0') * 10 + (m3 - '0');
            millis *= 1000000;
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime18(final byte[] bytes, final int off) {
        if (off + 18 > bytes.length) {
            return null;
        }
        final byte c0 = bytes[off];
        final byte c2 = bytes[off + 1];
        final byte c3 = bytes[off + 2];
        final byte c4 = bytes[off + 3];
        final byte c5 = bytes[off + 4];
        final byte c6 = bytes[off + 5];
        final byte c7 = bytes[off + 6];
        final byte c8 = bytes[off + 7];
        final byte c9 = bytes[off + 8];
        final byte c10 = bytes[off + 9];
        final byte c11 = bytes[off + 10];
        final byte c12 = bytes[off + 11];
        final byte c13 = bytes[off + 12];
        final byte c14 = bytes[off + 13];
        final byte c15 = bytes[off + 14];
        final byte c16 = bytes[off + 15];
        final byte c17 = bytes[off + 16];
        final byte c18 = bytes[off + 17];
        if (c3 != 58 || c6 != 58 || c9 != 46) {
            return null;
        }
        final byte h0 = c0;
        final byte h2 = c2;
        final byte i0 = c4;
        final byte i2 = c5;
        final byte s0 = c7;
        final byte s2 = c8;
        final byte m0 = c10;
        final byte m2 = c11;
        final byte m3 = c12;
        final byte m4 = c13;
        final byte m5 = c14;
        final byte m6 = c15;
        final byte m7 = c16;
        final byte m8 = c17;
        final byte m9 = c18;
        if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
            return null;
        }
        final int hour = (h0 - 48) * 10 + (h2 - 48);
        if (i0 < 48 || i0 > 57 || i2 < 48 || i2 > 57) {
            return null;
        }
        final int minute = (i0 - 48) * 10 + (i2 - 48);
        if (s0 < 48 || s0 > 57 || s2 < 48 || s2 > 57) {
            return null;
        }
        final int second = (s0 - 48) * 10 + (s2 - 48);
        if (m0 >= 48 && m0 <= 57 && m2 >= 48 && m2 <= 57 && m3 >= 48 && m3 <= 57 && m4 >= 48 && m4 <= 57 && m5 >= 48 && m5 <= 57 && m6 >= 48 && m6 <= 57 && m7 >= 48 && m7 <= 57 && m8 >= 48 && m8 <= 57 && m9 >= 48 && m9 <= 57) {
            final int millis = (m0 - 48) * 100000000 + (m2 - 48) * 10000000 + (m3 - 48) * 1000000 + (m4 - 48) * 100000 + (m5 - 48) * 10000 + (m6 - 48) * 1000 + (m7 - 48) * 100 + (m8 - 48) * 10 + (m9 - 48);
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalTime parseLocalTime18(final char[] bytes, final int off) {
        if (off + 18 > bytes.length) {
            return null;
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        final char c9 = bytes[off + 8];
        final char c10 = bytes[off + 9];
        final char c11 = bytes[off + 10];
        final char c12 = bytes[off + 11];
        final char c13 = bytes[off + 12];
        final char c14 = bytes[off + 13];
        final char c15 = bytes[off + 14];
        final char c16 = bytes[off + 15];
        final char c17 = bytes[off + 16];
        final char c18 = bytes[off + 17];
        if (c3 != ':' || c6 != ':' || c9 != '.') {
            return null;
        }
        final char h0 = c0;
        final char h2 = c2;
        final char i0 = c4;
        final char i2 = c5;
        final char s0 = c7;
        final char s2 = c8;
        final char m0 = c10;
        final char m2 = c11;
        final char m3 = c12;
        final char m4 = c13;
        final char m5 = c14;
        final char m6 = c15;
        final char m7 = c16;
        final char m8 = c17;
        final char m9 = c18;
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s2 - '0');
        if (m0 >= '0' && m0 <= '9' && m2 >= '0' && m2 <= '9' && m3 >= '0' && m3 <= '9' && m4 >= '0' && m4 <= '9' && m5 >= '0' && m5 <= '9' && m6 >= '0' && m6 <= '9' && m7 >= '0' && m7 <= '9' && m8 >= '0' && m8 <= '9' && m9 >= '0' && m9 <= '9') {
            final int millis = (m0 - '0') * 100000000 + (m2 - '0') * 10000000 + (m3 - '0') * 1000000 + (m4 - '0') * 100000 + (m5 - '0') * 10000 + (m6 - '0') * 1000 + (m7 - '0') * 100 + (m8 - '0') * 10 + (m9 - '0');
            return LocalTime.of(hour, minute, second, millis);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime(final byte[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        switch (len) {
            case 4: {
                if (str[off] == 110 && str[off + 1] == 117 && str[off + 2] == 108 && str[off + 3] == 108) {
                    return null;
                }
                final String input = new String(str, off, len);
                throw new DateTimeParseException("illegal input " + input, input, 0);
            }
            case 8: {
                final LocalDate localDate = parseLocalDate8(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 9: {
                final LocalDate localDate = parseLocalDate9(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 10: {
                final LocalDate localDate = parseLocalDate10(str, off);
                if (localDate == null) {
                    return null;
                }
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
            case 11: {
                return LocalDateTime.of(parseLocalDate11(str, off), LocalTime.MIN);
            }
            case 12: {
                return parseLocalDateTime12(str, off);
            }
            case 14: {
                return parseLocalDateTime14(str, off);
            }
            case 16: {
                return parseLocalDateTime16(str, off);
            }
            case 17: {
                return parseLocalDateTime17(str, off);
            }
            case 18: {
                return parseLocalDateTime18(str, off);
            }
            case 19: {
                return parseLocalDateTime19(str, off);
            }
            case 20: {
                return parseLocalDateTime20(str, off);
            }
            default: {
                return parseLocalDateTimeX(str, off, len);
            }
        }
    }
    
    public static LocalDate parseLocalDate(final String str) {
        if (str == null) {
            return null;
        }
        LocalDate localDate;
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            localDate = parseLocalDate(bytes, 0, bytes.length);
        }
        else {
            final char[] chars = JDKUtils.getCharArray(str);
            localDate = parseLocalDate(chars, 0, chars.length);
        }
        if (localDate != null) {
            return localDate;
        }
        switch (str) {
            case "":
            case "null":
            case "00000000":
            case "0000\u5e7400\u670800\u65e5":
            case "0000-0-00":
            case "0000-00-00": {
                return null;
            }
            default: {
                throw new DateTimeParseException(str, str, 0);
            }
        }
    }
    
    public static LocalDate parseLocalDate(final byte[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        if (off + len > str.length) {
            final String input = new String(str, off, len);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        switch (len) {
            case 8: {
                return parseLocalDate8(str, off);
            }
            case 9: {
                return parseLocalDate9(str, off);
            }
            case 10: {
                return parseLocalDate10(str, off);
            }
            case 11: {
                return parseLocalDate11(str, off);
            }
            default: {
                if (len == 4 && str[off] == 110 && str[off + 1] == 117 && str[off + 2] == 108 && str[off + 3] == 108) {
                    return null;
                }
                final String input = new String(str, off, len);
                throw new DateTimeParseException("illegal input " + input, input, 0);
            }
        }
    }
    
    public static LocalDate parseLocalDate(final char[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        if (off + len > str.length) {
            final String input = new String(str, off, len);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        switch (len) {
            case 8: {
                return parseLocalDate8(str, off);
            }
            case 9: {
                return parseLocalDate9(str, off);
            }
            case 10: {
                return parseLocalDate10(str, off);
            }
            case 11: {
                return parseLocalDate11(str, off);
            }
            default: {
                if (len == 4 && str[off] == 'n' && str[off + 1] == 'u' && str[off + 2] == 'l' && str[off + 3] == 'l') {
                    return null;
                }
                final String input = new String(str, off, len);
                throw new DateTimeParseException("illegal input " + input, input, 0);
            }
        }
    }
    
    public static long parseMillis(final byte[] bytes, final int off, final int len) {
        return parseMillis(bytes, off, len, StandardCharsets.UTF_8, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static long parseMillis(final byte[] bytes, final int off, final int len, final Charset charset) {
        return parseMillis(bytes, off, len, charset, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static long parseMillis(final byte[] chars, final int off, final int len, final Charset charset, ZoneId zoneId) {
        if (chars == null || len == 0) {
            return 0L;
        }
        if (len == 4 && chars[off] == 110 && chars[off + 1] == 117 && chars[off + 2] == 108 && chars[off + 3] == 108) {
            return 0L;
        }
        final char c0 = (char)chars[off];
        long millis;
        if (c0 == '\"' && chars[len - 1] == 34) {
            final JSONReader jsonReader = JSONReader.of(chars, off, len, charset);
            try {
                final Date date = (Date)ObjectReaderImplDate.INSTANCE.readObject(jsonReader, null, null, 0L);
                millis = date.getTime();
                if (jsonReader != null) {
                    jsonReader.close();
                }
            }
            catch (Throwable t) {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        else if (len == 19) {
            millis = parseMillis19(chars, off, zoneId);
        }
        else {
            final char c2;
            if (len > 19 || (len == 16 && ((c2 = (char)chars[off + 10]) == '+' || c2 == '-'))) {
                final ZonedDateTime zdt = parseZonedDateTime(chars, off, len, zoneId);
                if (zdt == null) {
                    final String input = new String(chars, off, len - off);
                    throw new DateTimeParseException("illegal input " + input, input, 0);
                }
                millis = zdt.toInstant().toEpochMilli();
            }
            else if ((c0 == '-' || (c0 >= '0' && c0 <= '9')) && IOUtils.isNumber(chars, off, len)) {
                millis = TypeUtils.parseLong(chars, off, len);
                if (len == 8 && millis >= 19700101L && millis <= 21000101L) {
                    final int year = (int)millis / 10000;
                    final int month = (int)millis % 10000 / 100;
                    final int dom = (int)millis % 100;
                    if (month >= 1 && month <= 12) {
                        int max = 31;
                        switch (month) {
                            case 2: {
                                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                                max = (leapYear ? 29 : 28);
                                break;
                            }
                            case 4:
                            case 6:
                            case 9:
                            case 11: {
                                max = 30;
                                break;
                            }
                        }
                        if (dom <= max) {
                            final LocalDateTime ldt = LocalDateTime.of(year, month, dom, 0, 0, 0);
                            final ZonedDateTime zdt2 = ZonedDateTime.ofLocal(ldt, zoneId, null);
                            final long seconds = zdt2.toEpochSecond();
                            millis = seconds * 1000L;
                        }
                    }
                }
            }
            else {
                final char last = (char)chars[len - 1];
                if (last == 'Z') {
                    zoneId = ZoneOffset.UTC;
                }
                LocalDateTime ldt2 = parseLocalDateTime(chars, off, len);
                if (ldt2 == null && chars[off] == 48 && chars[off + 1] == 48 && chars[off + 2] == 48 && chars[off + 3] == 48 && chars[off + 4] == 45 && chars[off + 5] == 48 && chars[off + 6] == 48 && chars[off + 7] == 45 && chars[off + 8] == 48 && chars[off + 9] == 48) {
                    ldt2 = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                }
                final ZonedDateTime zdt3 = ZonedDateTime.ofLocal(ldt2, zoneId, null);
                final long seconds2 = zdt3.toEpochSecond();
                final int nanos = ldt2.getNano();
                if (seconds2 < 0L && nanos > 0) {
                    millis = (seconds2 + 1L) * 1000L + nanos / 1000000 - 1000L;
                }
                else {
                    millis = seconds2 * 1000L + nanos / 1000000;
                }
            }
        }
        return millis;
    }
    
    public static long parseMillis(final char[] bytes, final int off, final int len) {
        return parseMillis(bytes, off, len, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static long parseMillis(final char[] chars, final int off, int len, ZoneId zoneId) {
        if (chars == null || len == 0) {
            return 0L;
        }
        if (len == 4 && chars[off] == 'n' && chars[off + 1] == 'u' && chars[off + 2] == 'l' && chars[off + 3] == 'l') {
            return 0L;
        }
        final char c0 = chars[off];
        long millis;
        if (c0 == '\"' && chars[len - 1] == '\"') {
            final JSONReader jsonReader = JSONReader.of(chars, off, len);
            try {
                final Date date = (Date)ObjectReaderImplDate.INSTANCE.readObject(jsonReader, null, null, 0L);
                millis = date.getTime();
                if (jsonReader != null) {
                    jsonReader.close();
                }
            }
            catch (Throwable t) {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        else if (len == 19) {
            millis = parseMillis19(chars, off, zoneId);
        }
        else {
            final char c2;
            if (len > 19 || (len == 16 && ((c2 = chars[off + 10]) == '+' || c2 == '-'))) {
                final ZonedDateTime zdt = parseZonedDateTime(chars, off, len, zoneId);
                if (zdt == null) {
                    final String input = new String(chars, off, len - off);
                    throw new DateTimeParseException("illegal input " + input, input, 0);
                }
                millis = zdt.toInstant().toEpochMilli();
            }
            else if ((c0 == '-' || (c0 >= '0' && c0 <= '9')) && IOUtils.isNumber(chars, off, len)) {
                millis = TypeUtils.parseLong(chars, off, len);
                if (len == 8 && millis >= 19700101L && millis <= 21000101L) {
                    final int year = (int)millis / 10000;
                    final int month = (int)millis % 10000 / 100;
                    final int dom = (int)millis % 100;
                    if (month >= 1 && month <= 12) {
                        int max = 31;
                        switch (month) {
                            case 2: {
                                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                                max = (leapYear ? 29 : 28);
                                break;
                            }
                            case 4:
                            case 6:
                            case 9:
                            case 11: {
                                max = 30;
                                break;
                            }
                        }
                        if (dom <= max) {
                            final LocalDateTime ldt = LocalDateTime.of(year, month, dom, 0, 0, 0);
                            final ZonedDateTime zdt2 = ZonedDateTime.ofLocal(ldt, zoneId, null);
                            final long seconds = zdt2.toEpochSecond();
                            millis = seconds * 1000L;
                        }
                    }
                }
            }
            else {
                final char last = chars[len - 1];
                if (last == 'Z') {
                    --len;
                    zoneId = ZoneOffset.UTC;
                }
                LocalDateTime ldt2 = parseLocalDateTime(chars, off, len);
                if (ldt2 == null && chars[off] == '0' && chars[off + 1] == '0' && chars[off + 2] == '0' && chars[off + 3] == '0' && chars[off + 4] == '-' && chars[off + 5] == '0' && chars[off + 6] == '0' && chars[off + 7] == '-' && chars[off + 8] == '0' && chars[off + 9] == '0') {
                    ldt2 = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                }
                if (ldt2 == null) {
                    final String input2 = new String(chars, off, len - off);
                    throw new DateTimeParseException("illegal input " + input2, input2, 0);
                }
                final ZonedDateTime zdt3 = ZonedDateTime.ofLocal(ldt2, zoneId, null);
                final long seconds2 = zdt3.toEpochSecond();
                final int nanos = ldt2.getNano();
                if (seconds2 < 0L && nanos > 0) {
                    millis = (seconds2 + 1L) * 1000L + nanos / 1000000 - 1000L;
                }
                else {
                    millis = seconds2 * 1000L + nanos / 1000000;
                }
            }
        }
        return millis;
    }
    
    public static LocalDate parseLocalDate8(final byte[] str, final int off) {
        if (off + 8 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c7 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = '0';
            d2 = c8;
        }
        else if (c2 == '/' && c4 == '/') {
            m0 = '0';
            m2 = c0;
            d0 = '0';
            d2 = c3;
            y0 = c5;
            y2 = c6;
            y3 = c7;
            y4 = c8;
        }
        else if (c2 == '-' && c6 == '-') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = '2';
            y2 = '0';
            y3 = c7;
            y4 = c8;
        }
        else {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate8(final char[] str, final int off) {
        if (off + 8 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c7 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = '0';
            d2 = c8;
        }
        else if (c2 == '/' && c4 == '/') {
            m0 = '0';
            m2 = c0;
            d0 = '0';
            d2 = c3;
            y0 = c5;
            y2 = c6;
            y3 = c7;
            y4 = c8;
        }
        else if (c2 == '-' && c6 == '-') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = '2';
            y2 = '0';
            y3 = c7;
            y4 = c8;
        }
        else {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate9(final byte[] str, final int off) {
        if (off + 9 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c8 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else if (c5 == '-' && c7 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c5 == '/' && c8 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else if (c5 == '/' && c7 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c2 == '.' && c5 == '.') {
            d0 = '0';
            d2 = c0;
            m0 = c3;
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c3 == '.' && c5 == '.') {
            d0 = c0;
            d2 = c2;
            m0 = '0';
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c2 == '-' && c5 == '-') {
            d0 = '0';
            d2 = c0;
            m0 = c3;
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c3 == '-' && c5 == '-') {
            d0 = c0;
            d2 = c2;
            m0 = '0';
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c3 == '-' && c7 == '-') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = '2';
            y2 = '0';
            y3 = c8;
            y4 = c9;
        }
        else if (c2 == '/' && c5 == '/') {
            m0 = '0';
            m2 = c0;
            d0 = c3;
            d2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else {
            if (c3 != '/' || c5 != '/') {
                return null;
            }
            m0 = c0;
            m2 = c2;
            d0 = '0';
            d2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate9(final char[] str, final int off) {
        if (off + 9 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c8 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else if (c5 == '-' && c7 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c5 == '/' && c8 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else if (c5 == '/' && c7 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c2 == '.' && c5 == '.') {
            d0 = '0';
            d2 = c0;
            m0 = c3;
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c3 == '.' && c5 == '.') {
            d0 = c0;
            d2 = c2;
            m0 = '0';
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c2 == '-' && c5 == '-') {
            d0 = '0';
            d2 = c0;
            m0 = c3;
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c3 == '-' && c5 == '-') {
            d0 = c0;
            d2 = c2;
            m0 = '0';
            m2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else if (c5 == '\u5e74' && c7 == '\u6708' && c9 == '\u65e5') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = '0';
            d2 = c8;
        }
        else if (c5 == '\ub144' && c7 == '\uc6d4' && c9 == '\uc77c') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = '0';
            d2 = c8;
        }
        else if (c3 == '-' && c7 == '-') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = '2';
            y2 = '0';
            y3 = c8;
            y4 = c9;
        }
        else if (c2 == '/' && c5 == '/') {
            m0 = '0';
            m2 = c0;
            d0 = c3;
            d2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        else {
            if (c3 != '/' || c5 != '/') {
                return null;
            }
            m0 = c0;
            m2 = c2;
            d0 = '0';
            d2 = c4;
            y0 = c6;
            y2 = c7;
            y3 = c8;
            y4 = c9;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate10(final byte[] str, final int off) {
        if (off + 10 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c8 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c5 == '/' && c8 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c3 == '.' && c6 == '.') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else if (c3 == '-' && c6 == '-') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else if (c3 == '/' && c6 == '/') {
            m0 = c0;
            m2 = c2;
            d0 = c4;
            d2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else {
            if (c2 != ' ' || c6 != ' ') {
                return null;
            }
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = '0';
            d2 = c0;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate10(final char[] str, final int off) {
        if (off + 10 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c8 == '-') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c5 == '/' && c8 == '/') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c3 == '.' && c6 == '.') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else if (c3 == '-' && c6 == '-') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else if (c3 == '/' && c6 == '/') {
            m0 = c0;
            m2 = c2;
            d0 = c4;
            d2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
        }
        else if (c5 == '\u5e74' && c7 == '\u6708' && c10 == '\u65e5') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c5 == '\ub144' && c7 == '\uc6d4' && c10 == '\uc77c') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
        }
        else if (c5 == '\u5e74' && c8 == '\u6708' && c10 == '\u65e5') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else if (c5 == '\ub144' && c8 == '\uc6d4' && c10 == '\uc77c') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
        }
        else {
            if (c2 != ' ' || c6 != ' ') {
                return null;
            }
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = '0';
            d2 = c0;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate11(final char[] str, final int off) {
        if (off + 11 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '\u5e74' && c8 == '\u6708' && c11 == '\u65e5') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c5 == '-' && c8 == '-' && c11 == 'Z') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else if (c5 == '\ub144' && c8 == '\uc6d4' && c11 == '\uc77c') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else {
            if (c3 != ' ' || c7 != ' ') {
                return null;
            }
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = c0;
            d2 = c2;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDate parseLocalDate11(final byte[] str, final int off) {
        if (off + 11 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        if (c5 == '-' && c8 == '-' && c11 == 'Z') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
        }
        else {
            if (c3 != ' ' || c7 != ' ') {
                return null;
            }
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = c0;
            d2 = c2;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (year == 0 && month2 == 0 && dom == 0) {
            return null;
        }
        return LocalDate.of(year, month2, dom);
    }
    
    public static LocalDateTime parseLocalDateTime12(final char[] str, final int off) {
        if (off + 12 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char y0 = str[off];
        final char y2 = str[off + 1];
        final char y3 = str[off + 2];
        final char y4 = str[off + 3];
        final char m0 = str[off + 4];
        final char m2 = str[off + 5];
        final char d0 = str[off + 6];
        final char d2 = str[off + 7];
        final char h0 = str[off + 8];
        final char h2 = str[off + 9];
        final char i0 = str[off + 10];
        final char i2 = str[off + 11];
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input2 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input3 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input3, input3, 0);
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String input4 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String input5 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input5, input5, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String input6 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input6, input6, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (year == 0 && month == 0 && dom == 0 && hour == 0 && minute == 0) {
            return null;
        }
        return LocalDateTime.of(year, month, dom, hour, minute, 0);
    }
    
    public static LocalDateTime parseLocalDateTime12(final byte[] str, final int off) {
        if (off + 12 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char y0 = (char)str[off];
        final char y2 = (char)str[off + 1];
        final char y3 = (char)str[off + 2];
        final char y4 = (char)str[off + 3];
        final char m0 = (char)str[off + 4];
        final char m2 = (char)str[off + 5];
        final char d0 = (char)str[off + 6];
        final char d2 = (char)str[off + 7];
        final char h0 = (char)str[off + 8];
        final char h2 = (char)str[off + 9];
        final char i0 = (char)str[off + 10];
        final char i2 = (char)str[off + 11];
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input2 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input3 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input3, input3, 0);
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String input4 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String input5 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input5, input5, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String input6 = new String(str, off, off + 12);
            throw new DateTimeParseException("illegal input " + input6, input6, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (year == 0 && month == 0 && dom == 0 && hour == 0 && minute == 0) {
            return null;
        }
        return LocalDateTime.of(year, month, dom, hour, minute, 0);
    }
    
    public static LocalDateTime parseLocalDateTime14(final char[] str, final int off) {
        if (off + 14 > str.length) {
            return null;
        }
        final char y0 = str[off];
        final char y2 = str[off + 1];
        final char y3 = str[off + 2];
        final char y4 = str[off + 3];
        final char m0 = str[off + 4];
        final char m2 = str[off + 5];
        final char d0 = str[off + 6];
        final char d2 = str[off + 7];
        final char h0 = str[off + 8];
        final char h2 = str[off + 9];
        final char i0 = str[off + 10];
        final char i2 = str[off + 11];
        final char s0 = str[off + 12];
        final char s2 = str[off + 13];
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month, dom, hour, minute, second);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime14(final byte[] str, final int off) {
        if (off + 14 > str.length) {
            return null;
        }
        final char y0 = (char)str[off];
        final char y2 = (char)str[off + 1];
        final char y3 = (char)str[off + 2];
        final char y4 = (char)str[off + 3];
        final char m0 = (char)str[off + 4];
        final char m2 = (char)str[off + 5];
        final char d0 = (char)str[off + 6];
        final char d2 = (char)str[off + 7];
        final char h0 = (char)str[off + 8];
        final char h2 = (char)str[off + 9];
        final char i0 = (char)str[off + 10];
        final char i2 = (char)str[off + 11];
        final char s0 = (char)str[off + 12];
        final char s2 = (char)str[off + 13];
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month, dom, hour, minute, second);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime16(final char[] str, final int off) {
        if (off + 16 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        char s0 = '0';
        char s2 = '0';
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        if (c5 == '-' && c8 == '-' && (c11 == 'T' || c11 == ' ') && c14 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
        }
        else if (c9 == 'T' && c16 == 'Z') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
            h0 = c10;
            h2 = c11;
            i0 = c12;
            i2 = c13;
            s0 = c14;
            s2 = c15;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == 'T' || c11 == ' ') && c13 == ':' && c15 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = '0';
            h2 = c12;
            i0 = '0';
            i2 = c14;
            s2 = c16;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':') {
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = '0';
            d2 = c0;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
        }
        else {
            if (c2 != ' ' || c6 != ' ' || c11 != ' ' || c13 != ':' || c15 != ':') {
                return null;
            }
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = '0';
            h2 = c12;
            i0 = '0';
            i2 = c14;
            s2 = c16;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime16(final byte[] str, final int off) {
        if (off + 16 > str.length) {
            return null;
        }
        final byte c0 = str[off];
        final byte c2 = str[off + 1];
        final byte c3 = str[off + 2];
        final byte c4 = str[off + 3];
        final byte c5 = str[off + 4];
        final byte c6 = str[off + 5];
        final byte c7 = str[off + 6];
        final byte c8 = str[off + 7];
        final byte c9 = str[off + 8];
        final byte c10 = str[off + 9];
        final byte c11 = str[off + 10];
        final byte c12 = str[off + 11];
        final byte c13 = str[off + 12];
        final byte c14 = str[off + 13];
        final byte c15 = str[off + 14];
        final byte c16 = str[off + 15];
        char s0 = '0';
        char s2 = '0';
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        if (c5 == 45 && c8 == 45 && (c11 == 84 || c11 == 32) && c14 == 58) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c6;
            m2 = (char)c7;
            d0 = (char)c9;
            d2 = (char)c10;
            h0 = (char)c12;
            h2 = (char)c13;
            i0 = (char)c15;
            i2 = (char)c16;
        }
        else if (c9 == 84 && c16 == 90) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c5;
            m2 = (char)c6;
            d0 = (char)c7;
            d2 = (char)c8;
            h0 = (char)c10;
            h2 = (char)c11;
            i0 = (char)c12;
            i2 = (char)c13;
            s0 = (char)c14;
            s2 = (char)c15;
        }
        else if (c5 == -27 && c6 == -71 && c7 == -76 && c9 == -26 && c10 == -100 && c11 == -120 && c14 == -26 && c15 == -105 && c16 == -91) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = '0';
            m2 = (char)c8;
            d0 = (char)c12;
            d2 = (char)c13;
            h0 = '0';
            h2 = '0';
            i0 = '0';
            i2 = '0';
        }
        else if (c5 == -27 && c6 == -71 && c7 == -76 && c10 == -26 && c11 == -100 && c12 == -120 && c14 == -26 && c15 == -105 && c16 == -91) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c8;
            m2 = (char)c9;
            d0 = '0';
            d2 = (char)c13;
            h0 = '0';
            h2 = '0';
            i0 = '0';
            i2 = '0';
        }
        else if (c5 == 45 && c8 == 45 && (c11 == 84 || c11 == 32) && c13 == 58 && c15 == 58) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c6;
            m2 = (char)c7;
            d0 = (char)c9;
            d2 = (char)c10;
            h0 = '0';
            h2 = (char)c12;
            i0 = '0';
            i2 = (char)c14;
            s2 = (char)c16;
        }
        else if (c2 == 32 && c6 == 32 && c11 == 32 && c14 == 58) {
            y0 = (char)c7;
            y2 = (char)c8;
            y3 = (char)c9;
            y4 = (char)c10;
            final int month = month((char)c3, (char)c4, (char)c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = '0';
            d2 = (char)c0;
            h0 = (char)c12;
            h2 = (char)c13;
            i0 = (char)c15;
            i2 = (char)c16;
        }
        else {
            if (c2 != 32 || c6 != 32 || c11 != 32 || c13 != 58 || c15 != 58) {
                return null;
            }
            d0 = '0';
            d2 = (char)c0;
            final int month = month((char)c3, (char)c4, (char)c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = (char)c7;
            y2 = (char)c8;
            y3 = (char)c9;
            y4 = (char)c10;
            h0 = '0';
            h2 = (char)c12;
            i0 = '0';
            i2 = (char)c14;
            s2 = (char)c16;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime17(final char[] str, final int off) {
        if (off + 17 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        int nanoOfSecond = 0;
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == 'T' || c11 == ' ') && c14 == ':' && c17 == 'Z') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = '0';
        }
        else if (c5 == '-' && c7 == '-' && (c9 == ' ' || c9 == 'T') && c12 == ':' && c15 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = '0';
            d2 = c8;
            h0 = c10;
            h2 = c11;
            i0 = c13;
            i2 = c14;
            s0 = c16;
            s2 = c17;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = c0;
            d2 = c2;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = '0';
            s2 = '0';
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c13 == ':' && c15 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = '0';
            h2 = c12;
            i0 = '0';
            i2 = c14;
            s0 = c16;
            s2 = c17;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c13 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = '0';
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = '0';
            s2 = c17;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = '0';
            s2 = c17;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c16 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                return null;
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = '0';
            s2 = c17;
        }
        else {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
            h0 = c9;
            h2 = c10;
            i0 = c11;
            i2 = c12;
            s0 = c13;
            s2 = c14;
            if (c15 < '0' || c15 > '9' || c16 < '0' || c16 > '9' || c17 < '0' || c17 > '9') {
                return null;
            }
            nanoOfSecond = ((c15 - '0') * 100 + (c16 - '0') * 10 + (c17 - '0')) * 1000000;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            return null;
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second, nanoOfSecond);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTime17(final byte[] str, final int off) {
        if (off + 17 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final byte c0 = str[off];
        final byte c2 = str[off + 1];
        final byte c3 = str[off + 2];
        final byte c4 = str[off + 3];
        final byte c5 = str[off + 4];
        final byte c6 = str[off + 5];
        final byte c7 = str[off + 6];
        final byte c8 = str[off + 7];
        final byte c9 = str[off + 8];
        final byte c10 = str[off + 9];
        final byte c11 = str[off + 10];
        final byte c12 = str[off + 11];
        final byte c13 = str[off + 12];
        final byte c14 = str[off + 13];
        final byte c15 = str[off + 14];
        final byte c16 = str[off + 15];
        final byte c17 = str[off + 16];
        int nanoOfSecond = 0;
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == 45 && c8 == 45 && (c11 == 84 || c11 == 32) && c14 == 58 && c17 == 90) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c6;
            m2 = (char)c7;
            d0 = (char)c9;
            d2 = (char)c10;
            h0 = (char)c12;
            h2 = (char)c13;
            i0 = (char)c15;
            i2 = (char)c16;
            s0 = '0';
            s2 = '0';
        }
        else if (c5 == 45 && c7 == 45 && (c9 == 32 || c9 == 84) && c12 == 58 && c15 == 58) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = '0';
            m2 = (char)c6;
            d0 = '0';
            d2 = (char)c8;
            h0 = (char)c10;
            h2 = (char)c11;
            i0 = (char)c13;
            i2 = (char)c14;
            s0 = (char)c16;
            s2 = (char)c17;
        }
        else if (c5 == -27 && c6 == -71 && c7 == -76 && c10 == -26 && c11 == -100 && c12 == -120 && c15 == -26 && c16 == -105 && c17 == -91) {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c8;
            m2 = (char)c9;
            d0 = (char)c13;
            d2 = (char)c14;
            h0 = '0';
            h2 = '0';
            i0 = '0';
            i2 = '0';
            s0 = '0';
            s2 = '0';
        }
        else if (c3 == 32 && c7 == 32 && c12 == 32 && c15 == 58) {
            y0 = (char)c8;
            y2 = (char)c9;
            y3 = (char)c10;
            y4 = (char)c11;
            final int month = month((char)c4, (char)c5, (char)c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 17);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            d0 = (char)c0;
            d2 = (char)c2;
            h0 = (char)c13;
            h2 = (char)c14;
            i0 = (char)c16;
            i2 = (char)c17;
            s0 = '0';
            s2 = '0';
        }
        else if (c2 == 32 && c6 == 32 && c11 == 32 && c13 == 58 && c15 == 58) {
            d0 = '0';
            d2 = (char)c0;
            final int month = month((char)c3, (char)c4, (char)c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 17);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = (char)c7;
            y2 = (char)c8;
            y3 = (char)c9;
            y4 = (char)c10;
            h0 = '0';
            h2 = (char)c12;
            i0 = '0';
            i2 = (char)c14;
            s0 = (char)c16;
            s2 = (char)c17;
        }
        else if (c2 == 32 && c6 == 32 && c11 == 32 && c13 == 58 && c16 == 58) {
            d0 = '0';
            d2 = (char)c0;
            final int month = month((char)c3, (char)c4, (char)c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 17);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = (char)c7;
            y2 = (char)c8;
            y3 = (char)c9;
            y4 = (char)c10;
            h0 = '0';
            h2 = (char)c12;
            i0 = (char)c14;
            i2 = (char)c15;
            s0 = '0';
            s2 = (char)c17;
        }
        else if (c2 == 32 && c6 == 32 && c11 == 32 && c14 == 58 && c16 == 58) {
            d0 = '0';
            d2 = (char)c0;
            final int month = month((char)c3, (char)c4, (char)c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 17);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = (char)c7;
            y2 = (char)c8;
            y3 = (char)c9;
            y4 = (char)c10;
            h0 = (char)c12;
            h2 = (char)c13;
            i0 = '0';
            i2 = (char)c15;
            s0 = '0';
            s2 = (char)c17;
        }
        else if (c3 == 32 && c7 == 32 && c12 == 32 && c14 == 58 && c16 == 58) {
            d0 = (char)c0;
            d2 = (char)c2;
            final int month = month((char)c4, (char)c5, (char)c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 17);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = (char)c8;
            y2 = (char)c9;
            y3 = (char)c10;
            y4 = (char)c11;
            h0 = '0';
            h2 = (char)c13;
            i0 = '0';
            i2 = (char)c15;
            s0 = '0';
            s2 = (char)c17;
        }
        else {
            y0 = (char)c0;
            y2 = (char)c2;
            y3 = (char)c3;
            y4 = (char)c4;
            m0 = (char)c5;
            m2 = (char)c6;
            d0 = (char)c7;
            d2 = (char)c8;
            h0 = (char)c9;
            h2 = (char)c10;
            i0 = (char)c11;
            i2 = (char)c12;
            s0 = (char)c13;
            s2 = (char)c14;
            if (c15 < 48 || c15 > 57 || c16 < 48 || c16 > 57 || c17 < 48 || c17 > 57) {
                return null;
            }
            nanoOfSecond = ((c15 - 48) * 100 + (c16 - 48) * 10 + (c17 - 48)) * 1000000;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input2 = new String(str, off, 17);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input3 = new String(str, off, 17);
            throw new DateTimeParseException("illegal input " + input3, input3, 0);
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String input4 = new String(str, off, 17);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String input5 = new String(str, off, 17);
            throw new DateTimeParseException("illegal input " + input5, input5, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String input6 = new String(str, off, 17);
            throw new DateTimeParseException("illegal input " + input6, input6, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second, nanoOfSecond);
        }
        final String input7 = new String(str, off, 17);
        throw new DateTimeParseException("illegal input " + input7, input7, 0);
    }
    
    public static LocalDateTime parseLocalDateTime18(final char[] str, final int off) {
        if (off + 18 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c7 == '-' && (c10 == ' ' || c10 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
            h0 = c11;
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c10 == ' ' || c10 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
            h0 = c11;
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = '0';
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c13 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = '0';
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = '0';
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else {
            if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c14 != ':' || c16 != ':') {
                final String input3 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input3, input3, 0);
            }
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input2 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input4 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String input5 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input5, input5, 0);
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String input6 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input6, input6, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String input7 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input7, input7, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second);
        }
        final String input8 = new String(str, off, 18);
        throw new DateTimeParseException("illegal input " + input8, input8, 0);
    }
    
    public static LocalDateTime parseLocalDateTime18(final byte[] str, final int off) {
        if (off + 18 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c7 == '-' && (c10 == ' ' || c10 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = '0';
            m2 = c6;
            d0 = c8;
            d2 = c9;
            h0 = c11;
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c10 == ' ' || c10 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = '0';
            d2 = c9;
            h0 = c11;
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c13 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = '0';
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c16 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c13 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = '0';
            h2 = c12;
            i0 = c14;
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c16 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = '0';
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = c18;
        }
        else {
            if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c14 != ':' || c16 != ':') {
                final String input3 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input3, input3, 0);
            }
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String input2 = new String(str, off, 18);
                throw new DateTimeParseException("illegal input " + input2, input2, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = '0';
            i2 = c15;
            s0 = c17;
            s2 = c18;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input2 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input4 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int month2 = (m0 - '0') * 10 + (m2 - '0');
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String input5 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input5, input5, 0);
        }
        final int dom = (d0 - '0') * 10 + (d2 - '0');
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String input6 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input6, input6, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String input7 = new String(str, off, 18);
            throw new DateTimeParseException("illegal input " + input7, input7, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            return LocalDateTime.of(year, month2, dom, hour, minute, second);
        }
        final String input8 = new String(str, off, 18);
        throw new DateTimeParseException("illegal input " + input8, input8, 0);
    }
    
    public static LocalDateTime parseLocalDateTime19(final char[] str, final int off) {
        if (off + 19 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c5 == '/' && c8 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '/' && c6 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else {
            if (c2 != ' ' || c6 != ' ' || c11 != ' ' || c14 != ':' || c17 != ':') {
                return null;
            }
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            final int month = month(c3, c4, c5);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c0;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        return localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2);
    }
    
    public static LocalDateTime parseLocalDateTime19(final String str, final int off) {
        if (off + 19 > str.length()) {
            return null;
        }
        LocalDateTime ldt;
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            ldt = parseLocalDateTime19(bytes, off);
        }
        else if (JDKUtils.JVM_VERSION == 8 && !JDKUtils.FIELD_STRING_VALUE_ERROR) {
            final char[] chars = JDKUtils.getCharArray(str);
            ldt = parseLocalDateTime19(chars, off);
        }
        else {
            final char[] chars = new char[19];
            str.getChars(off, off + 19, chars, 0);
            ldt = parseLocalDateTime19(chars, off);
        }
        return ldt;
    }
    
    public static LocalDateTime parseLocalDateTime19(final byte[] str, final int off) {
        if (off + 19 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c5 == '/' && c8 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '/' && c6 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else {
            if (c2 != ' ' || c6 != ' ' || c11 != ' ' || c14 != ':' || c17 != ':') {
                return null;
            }
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            final int month = month(c3, c4, c5);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c0;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        return localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2);
    }
    
    public static LocalDateTime parseLocalDateTime20(final char[] str, final int off) {
        if (off + 19 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = str[off + 19];
        if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c15 != ':' || c18 != ':') {
            return null;
        }
        final int month = month(c4, c5, c6);
        char m0;
        char m2;
        if (month > 0) {
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
        }
        else {
            m0 = '0';
            m2 = '0';
        }
        return localDateTime(c8, c9, c10, c11, m0, m2, c0, c2, c13, c14, c16, c17, c19, c20);
    }
    
    public static LocalDateTime parseLocalDateTime20(final byte[] str, final int off) {
        if (off + 19 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (char)str[off + 19];
        if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c15 != ':' || c18 != ':') {
            return null;
        }
        final int month = month(c4, c5, c6);
        char m0;
        char m2;
        if (month > 0) {
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
        }
        else {
            m0 = '0';
            m2 = '0';
        }
        return localDateTime(c8, c9, c10, c11, m0, m2, c0, c2, c13, c14, c16, c17, c19, c20);
    }
    
    public static LocalDateTime parseLocalDateTime26(final byte[] str, final int off) {
        if (off + 26 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (char)str[off + 19];
        final char c21 = (char)str[off + 20];
        final char c22 = (char)str[off + 21];
        final char c23 = (char)str[off + 22];
        final char c24 = (char)str[off + 23];
        final char c25 = (char)str[off + 24];
        final char c26 = (char)str[off + 25];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, '0', '0', '0');
    }
    
    public static LocalDateTime parseLocalDateTime26(final char[] str, final int off) {
        if (off + 26 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = str[off + 19];
        final char c21 = str[off + 20];
        final char c22 = str[off + 21];
        final char c23 = str[off + 22];
        final char c24 = str[off + 23];
        final char c25 = str[off + 24];
        final char c26 = str[off + 25];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, '0', '0', '0');
    }
    
    public static LocalDateTime parseLocalDateTime27(final byte[] str, final int off) {
        if (off + 27 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (char)str[off + 19];
        final char c21 = (char)str[off + 20];
        final char c22 = (char)str[off + 21];
        final char c23 = (char)str[off + 22];
        final char c24 = (char)str[off + 23];
        final char c25 = (char)str[off + 24];
        final char c26 = (char)str[off + 25];
        final char c27 = (char)str[off + 26];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, '0', '0');
    }
    
    public static LocalDateTime parseLocalDateTime27(final char[] str, final int off) {
        if (off + 27 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = str[off + 19];
        final char c21 = str[off + 20];
        final char c22 = str[off + 21];
        final char c23 = str[off + 22];
        final char c24 = str[off + 23];
        final char c25 = str[off + 24];
        final char c26 = str[off + 25];
        final char c27 = str[off + 26];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, '0', '0');
    }
    
    public static LocalDateTime parseLocalDateTime28(final char[] str, final int off) {
        if (off + 28 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = str[off + 19];
        final char c21 = str[off + 20];
        final char c22 = str[off + 21];
        final char c23 = str[off + 22];
        final char c24 = str[off + 23];
        final char c25 = str[off + 24];
        final char c26 = str[off + 25];
        final char c27 = str[off + 26];
        final char c28 = str[off + 27];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, c28, '0');
    }
    
    public static LocalDateTime parseLocalDateTime28(final byte[] str, final int off) {
        if (off + 28 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (char)str[off + 19];
        final char c21 = (char)str[off + 20];
        final char c22 = (char)str[off + 21];
        final char c23 = (char)str[off + 22];
        final char c24 = (char)str[off + 23];
        final char c25 = (char)str[off + 24];
        final char c26 = (char)str[off + 25];
        final char c27 = (char)str[off + 26];
        final char c28 = (char)str[off + 27];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, c28, '0');
    }
    
    public static LocalDateTime parseLocalDateTime29(final byte[] str, final int off) {
        if (off + 29 > str.length) {
            return null;
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (char)str[off + 19];
        final char c21 = (char)str[off + 20];
        final char c22 = (char)str[off + 21];
        final char c23 = (char)str[off + 22];
        final char c24 = (char)str[off + 23];
        final char c25 = (char)str[off + 24];
        final char c26 = (char)str[off + 25];
        final char c27 = (char)str[off + 26];
        final char c28 = (char)str[off + 27];
        final char c29 = (char)str[off + 28];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, c28, c29);
    }
    
    public static LocalDateTime parseLocalDateTime29(final char[] str, final int off) {
        if (off + 29 > str.length) {
            return null;
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = str[off + 19];
        final char c21 = str[off + 20];
        final char c22 = str[off + 21];
        final char c23 = str[off + 22];
        final char c24 = str[off + 23];
        final char c25 = str[off + 24];
        final char c26 = str[off + 25];
        final char c27 = str[off + 26];
        final char c28 = str[off + 27];
        final char c29 = str[off + 28];
        if (c5 != '-' || c8 != '-' || (c11 != ' ' && c11 != 'T') || c14 != ':' || c17 != ':' || c20 != '.') {
            return null;
        }
        return localDateTime(c0, c2, c3, c4, c6, c7, c9, c10, c12, c13, c15, c16, c18, c19, c21, c22, c23, c24, c25, c26, c27, c28, c29);
    }
    
    public static LocalDateTime parseLocalDateTimeX(final char[] str, final int offset, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        if (len < 21 || len > 29) {
            return null;
        }
        final char c0 = str[offset];
        final char c2 = str[offset + 1];
        final char c3 = str[offset + 2];
        final char c4 = str[offset + 3];
        final char c5 = str[offset + 4];
        final char c6 = str[offset + 5];
        final char c7 = str[offset + 6];
        final char c8 = str[offset + 7];
        final char c9 = str[offset + 8];
        final char c10 = str[offset + 9];
        final char c11 = str[offset + 10];
        final char c12 = str[offset + 11];
        final char c13 = str[offset + 12];
        final char c14 = str[offset + 13];
        final char c15 = str[offset + 14];
        final char c16 = str[offset + 15];
        final char c17 = str[offset + 16];
        final char c18 = str[offset + 17];
        final char c19 = str[offset + 18];
        final char c20 = str[offset + 19];
        char c21 = '0';
        char c22 = '0';
        char c23 = '0';
        char c24 = '0';
        char c25 = '0';
        char c26 = '0';
        char c27 = '0';
        char c28 = '0';
        char c29 = '\0';
        switch (len) {
            case 21: {
                c29 = str[offset + 20];
                break;
            }
            case 22: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                break;
            }
            case 23: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                break;
            }
            case 24: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                break;
            }
            case 25: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                c24 = str[offset + 24];
                break;
            }
            case 26: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                c24 = str[offset + 24];
                c25 = str[offset + 25];
                break;
            }
            case 27: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                c24 = str[offset + 24];
                c25 = str[offset + 25];
                c26 = str[offset + 26];
                break;
            }
            case 28: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                c24 = str[offset + 24];
                c25 = str[offset + 25];
                c26 = str[offset + 26];
                c27 = str[offset + 27];
                break;
            }
            default: {
                c29 = str[offset + 20];
                c21 = str[offset + 21];
                c22 = str[offset + 22];
                c23 = str[offset + 23];
                c24 = str[offset + 24];
                c25 = str[offset + 25];
                c26 = str[offset + 26];
                c27 = str[offset + 27];
                c28 = str[offset + 28];
                break;
            }
        }
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.') {
            final char y0 = c0;
            final char y2 = c2;
            final char y3 = c3;
            final char y4 = c4;
            final char m0 = c6;
            final char m2 = c7;
            final char d0 = c9;
            final char d2 = c10;
            final char h0 = c12;
            final char h2 = c13;
            final char i0 = c15;
            final char i2 = c16;
            final char s0 = c18;
            final char s2 = c19;
            final char S0 = c29;
            final char S2 = c21;
            final char S3 = c22;
            final char S4 = c23;
            final char S5 = c24;
            final char S6 = c25;
            final char S7 = c26;
            final char S8 = c27;
            final char S9 = c28;
            return localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2, S0, S2, S3, S4, S5, S6, S7, S8, S9);
        }
        return null;
    }
    
    public static LocalDateTime parseLocalDateTimeX(final byte[] str, final int offset, final int len) {
        if (str == null || len == 0) {
            return null;
        }
        if (len < 21 || len > 29) {
            return null;
        }
        final char c0 = (char)str[offset];
        final char c2 = (char)str[offset + 1];
        final char c3 = (char)str[offset + 2];
        final char c4 = (char)str[offset + 3];
        final char c5 = (char)str[offset + 4];
        final char c6 = (char)str[offset + 5];
        final char c7 = (char)str[offset + 6];
        final char c8 = (char)str[offset + 7];
        final char c9 = (char)str[offset + 8];
        final char c10 = (char)str[offset + 9];
        final char c11 = (char)str[offset + 10];
        final char c12 = (char)str[offset + 11];
        final char c13 = (char)str[offset + 12];
        final char c14 = (char)str[offset + 13];
        final char c15 = (char)str[offset + 14];
        final char c16 = (char)str[offset + 15];
        final char c17 = (char)str[offset + 16];
        final char c18 = (char)str[offset + 17];
        final char c19 = (char)str[offset + 18];
        final char c20 = (char)str[offset + 19];
        char c21 = '0';
        char c22 = '0';
        char c23 = '0';
        char c24 = '0';
        char c25 = '0';
        char c26 = '0';
        char c27 = '0';
        char c28 = '0';
        char c29 = '\0';
        switch (len) {
            case 21: {
                c29 = (char)str[offset + 20];
                break;
            }
            case 22: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                break;
            }
            case 23: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                break;
            }
            case 24: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                break;
            }
            case 25: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                c24 = (char)str[offset + 24];
                break;
            }
            case 26: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                c24 = (char)str[offset + 24];
                c25 = (char)str[offset + 25];
                break;
            }
            case 27: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                c24 = (char)str[offset + 24];
                c25 = (char)str[offset + 25];
                c26 = (char)str[offset + 26];
                break;
            }
            case 28: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                c24 = (char)str[offset + 24];
                c25 = (char)str[offset + 25];
                c26 = (char)str[offset + 26];
                c27 = (char)str[offset + 27];
                break;
            }
            default: {
                c29 = (char)str[offset + 20];
                c21 = (char)str[offset + 21];
                c22 = (char)str[offset + 22];
                c23 = (char)str[offset + 23];
                c24 = (char)str[offset + 24];
                c25 = (char)str[offset + 25];
                c26 = (char)str[offset + 26];
                c27 = (char)str[offset + 27];
                c28 = (char)str[offset + 28];
                break;
            }
        }
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.') {
            final char y0 = c0;
            final char y2 = c2;
            final char y3 = c3;
            final char y4 = c4;
            final char m0 = c6;
            final char m2 = c7;
            final char d0 = c9;
            final char d2 = c10;
            final char h0 = c12;
            final char h2 = c13;
            final char i0 = c15;
            final char i2 = c16;
            final char s0 = c18;
            final char s2 = c19;
            final char S0 = c29;
            final char S2 = c21;
            final char S3 = c22;
            final char S4 = c23;
            final char S5 = c24;
            final char S6 = c25;
            final char S7 = c26;
            final char S8 = c27;
            final char S9 = c28;
            return localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2, S0, S2, S3, S4, S5, S6, S7, S8, S9);
        }
        return null;
    }
    
    static ZonedDateTime parseZonedDateTime16(final char[] str, final int off, final ZoneId defaultZonedId) {
        if (off + 16 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 13];
        if (c5 != '-' || c8 != '-' || (c11 != '+' && c11 != '-') || c12 != ':') {
            final String input2 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final char y0 = c0;
        final char y2 = c2;
        final char y3 = c3;
        final char y4 = c4;
        final char m0 = c6;
        final char m2 = c7;
        final char d0 = c9;
        final char d2 = c10;
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input3 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input3, input3, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input4 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 >= '0' && d0 <= '9' && d2 >= '0' && d2 <= '9') {
            final int dom = (d0 - '0') * 10 + (d2 - '0');
            final String zoneIdStr = new String(str, off + 10, 6);
            final ZoneId zoneId = getZoneId(zoneIdStr, defaultZonedId);
            final LocalDateTime ldt = LocalDateTime.of(LocalDate.of(year, month, dom), LocalTime.MIN);
            return ZonedDateTime.of(ldt, zoneId);
        }
        final String input5 = new String(str, off, 16);
        throw new DateTimeParseException("illegal input " + input5, input5, 0);
    }
    
    static ZonedDateTime parseZonedDateTime16(final byte[] str, final int off, final ZoneId defaultZonedId) {
        if (off + 16 > str.length) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 13];
        if (c5 != '-' || c8 != '-' || (c11 != '+' && c11 != '-') || c12 != ':') {
            final String input2 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input2, input2, 0);
        }
        final char y0 = c0;
        final char y2 = c2;
        final char y3 = c3;
        final char y4 = c4;
        final char m0 = c6;
        final char m2 = c7;
        final char d0 = c9;
        final char d2 = c10;
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String input3 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input3, input3, 0);
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String input4 = new String(str, off, 16);
            throw new DateTimeParseException("illegal input " + input4, input4, 0);
        }
        final int month = (m0 - '0') * 10 + (m2 - '0');
        if (d0 >= '0' && d0 <= '9' && d2 >= '0' && d2 <= '9') {
            final int dom = (d0 - '0') * 10 + (d2 - '0');
            final String zoneIdStr = new String(str, off + 10, 6);
            final ZoneId zoneId = getZoneId(zoneIdStr, defaultZonedId);
            final LocalDateTime ldt = LocalDateTime.of(LocalDate.of(year, month, dom), LocalTime.MIN);
            return ZonedDateTime.of(ldt, zoneId);
        }
        final String input5 = new String(str, off, 16);
        throw new DateTimeParseException("illegal input " + input5, input5, 0);
    }
    
    public static ZonedDateTime parseZonedDateTime(final String str) {
        return parseZonedDateTime(str, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static ZonedDateTime parseZonedDateTime(final String str, final ZoneId defaultZoneId) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return null;
        }
        ZonedDateTime zdt;
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            zdt = parseZonedDateTime(bytes, 0, bytes.length, defaultZoneId);
        }
        else {
            final char[] chars = JDKUtils.getCharArray(str);
            zdt = parseZonedDateTime(chars, 0, chars.length, defaultZoneId);
        }
        if (zdt != null) {
            return zdt;
        }
        switch (str) {
            case "null":
            case "0":
            case "0000-00-00": {
                return null;
            }
            default: {
                throw new DateTimeParseException(str, str, 0);
            }
        }
    }
    
    public static ZonedDateTime parseZonedDateTime(final byte[] str, final int off, final int len) {
        return parseZonedDateTime(str, off, len, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static ZonedDateTime parseZonedDateTime(final char[] str, final int off, final int len) {
        return parseZonedDateTime(str, off, len, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static ZonedDateTime parseZonedDateTime(final byte[] str, final int off, final int len, final ZoneId defaultZoneId) {
        if (str == null) {
            return null;
        }
        if (len == 0) {
            return null;
        }
        if (len == 16) {
            return parseZonedDateTime16(str, off, defaultZoneId);
        }
        if (len < 19) {
            return null;
        }
        String zoneIdStr = null;
        final char c0 = (char)str[off];
        final char c2 = (char)str[off + 1];
        final char c3 = (char)str[off + 2];
        final char c4 = (char)str[off + 3];
        final char c5 = (char)str[off + 4];
        final char c6 = (char)str[off + 5];
        final char c7 = (char)str[off + 6];
        final char c8 = (char)str[off + 7];
        final char c9 = (char)str[off + 8];
        final char c10 = (char)str[off + 9];
        final char c11 = (char)str[off + 10];
        final char c12 = (char)str[off + 11];
        final char c13 = (char)str[off + 12];
        final char c14 = (char)str[off + 13];
        final char c15 = (char)str[off + 14];
        final char c16 = (char)str[off + 15];
        final char c17 = (char)str[off + 16];
        final char c18 = (char)str[off + 17];
        final char c19 = (char)str[off + 18];
        final char c20 = (len == 19) ? ' ' : ((char)str[off + 19]);
        char c21 = '0';
        char c22 = '0';
        char c23 = '0';
        char c24 = '0';
        char c25 = '0';
        char c26 = '0';
        char c27 = '0';
        char c28 = '0';
        char c29 = '\0';
        char c30 = '\0';
        switch (len) {
            case 19:
            case 20: {
                c30 = '\0';
                break;
            }
            case 21: {
                c30 = (char)str[off + 20];
                break;
            }
            case 22: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                break;
            }
            case 23: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                break;
            }
            case 24: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                break;
            }
            case 25: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                break;
            }
            case 26: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                c25 = (char)str[off + 25];
                break;
            }
            case 27: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                c25 = (char)str[off + 25];
                c26 = (char)str[off + 26];
                break;
            }
            case 28: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                c25 = (char)str[off + 25];
                c26 = (char)str[off + 26];
                c27 = (char)str[off + 27];
                break;
            }
            case 29: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                c25 = (char)str[off + 25];
                c26 = (char)str[off + 26];
                c27 = (char)str[off + 27];
                c28 = (char)str[off + 28];
                break;
            }
            default: {
                c30 = (char)str[off + 20];
                c21 = (char)str[off + 21];
                c22 = (char)str[off + 22];
                c23 = (char)str[off + 23];
                c24 = (char)str[off + 24];
                c25 = (char)str[off + 25];
                c26 = (char)str[off + 26];
                c27 = (char)str[off + 27];
                c28 = (char)str[off + 28];
                c29 = (char)str[off + 29];
                break;
            }
        }
        boolean isTimeZone = false;
        boolean pm = false;
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        char S0;
        char S2;
        char S3;
        char S4;
        char S5;
        char S6;
        char S7;
        char S8;
        char S9;
        int zoneIdBegin;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && (c20 == '[' || c20 == 'Z' || c20 == '+' || c20 == '-' || c20 == ' ')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 19;
        }
        else if (c5 == '-' && c8 == '-' && c11 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':' && len == 20) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 20;
        }
        else if (len == 20 && c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c4, c5, c6);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c0;
            d2 = c2;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 20;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 21 || c21 == '[' || c21 == '+' || c21 == '-' || c21 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 21;
            isTimeZone = (c21 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 22 || c22 == '[' || c22 == '+' || c22 == '-' || c22 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
            isTimeZone = (c22 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == 'Z' && c18 == '[' && c21 == ']' && len == 22) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = '0';
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            isTimeZone = true;
            zoneIdBegin = 17;
        }
        else if (len == 22 && c4 == ' ' && c6 == ',' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && (c30 == 'A' || c30 == 'P') && c21 == 'M') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c5;
            h0 = '0';
            h2 = c13;
            pm = (c30 == 'P');
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
        }
        else if (len == 22 && c3 == '/' && c6 == '/' && c11 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && (c30 == 'A' || c30 == 'P') && c21 == 'M') {
            m0 = c0;
            m2 = c2;
            d0 = c4;
            d2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            pm = (c30 == 'P');
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
        }
        else if (len == 23 && c4 == ' ' && c6 == ',' && c7 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':' && c30 == ' ' && (c21 == 'A' || c21 == 'P') && c22 == 'M') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c5;
            h0 = c13;
            h2 = c14;
            pm = (c21 == 'P');
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
        }
        else if (len == 23 && c4 == ' ' && c7 == ',' && c8 == ' ' && c13 == ' ' && c15 == ':' && c18 == ':' && c30 == ' ' && (c21 == 'A' || c21 == 'P') && c22 == 'M') {
            y0 = c9;
            y2 = c10;
            y3 = c11;
            y4 = c12;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c5;
            d2 = c6;
            h0 = '0';
            h2 = c14;
            pm = (c21 == 'P');
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
        }
        else if (len == 24 && c4 == ' ' && c7 == ',' && c8 == ' ' && c13 == ' ' && c16 == ':' && c19 == ':' && c21 == ' ' && (c22 == 'A' || c22 == 'P') && c23 == 'M') {
            y0 = c9;
            y2 = c10;
            y3 = c11;
            y4 = c12;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c5;
            d2 = c6;
            h0 = c14;
            h2 = c15;
            pm = (c22 == 'P');
            i0 = c17;
            i2 = c18;
            s0 = c20;
            s2 = c30;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 23 || c23 == '[' || c23 == '|' || c23 == '+' || c23 == '-' || c23 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
            isTimeZone = (c23 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 24 || c24 == '[' || c24 == '|' || c24 == '+' || c24 == '-' || c24 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
            isTimeZone = (c24 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 25 || c25 == '[' || c25 == '|' || c25 == '+' || c25 == '-' || c25 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 25;
            isTimeZone = (c25 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 26 || c26 == '[' || c26 == '|' || c26 == '+' || c26 == '-' || c26 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 26;
            isTimeZone = (c26 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 27 || c27 == '[' || c27 == '|' || c27 == '+' || c27 == '-' || c27 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            if (c23 == ' ') {
                S4 = '0';
                S5 = '0';
                S6 = '0';
                S7 = '0';
                S8 = '0';
                S9 = '0';
                zoneIdBegin = 23;
            }
            else {
                S4 = c23;
                S5 = c24;
                S6 = c25;
                S7 = c26;
                S8 = '0';
                S9 = '0';
                zoneIdBegin = 27;
                isTimeZone = (c27 == '|');
            }
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 28 || c28 == '[' || c28 == '|' || c28 == '+' || c28 == '-' || c28 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = c26;
            S8 = c27;
            S9 = '0';
            zoneIdBegin = 28;
            isTimeZone = (c28 == '|');
        }
        else if (len == 28 && c4 == ' ' && c8 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && c23 == ' ') {
            final int month = month(c5, c6, c7);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            y0 = c24;
            y2 = c25;
            y3 = c26;
            y4 = c27;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 19;
            zoneIdStr = new String(str, off + 20, 3);
        }
        else if (len == 28 && c4 == ',' && c5 == ' ' && c7 == ' ' && c11 == ' ' && c16 == ' ' && c19 == ':' && c21 == ':' && c24 == ' ') {
            y0 = c12;
            y2 = c13;
            y3 = c14;
            y4 = c15;
            final int month = month(c8, c9, c10);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c6;
            h0 = c17;
            h2 = c18;
            i0 = c20;
            i2 = c30;
            s0 = c22;
            s2 = c23;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
            isTimeZone = true;
        }
        else if (len == 29 && c4 == ',' && c5 == ' ' && c8 == ' ' && c12 == ' ' && c17 == ' ' && c20 == ':' && c22 == ':' && c25 == ' ') {
            y0 = c13;
            y2 = c14;
            y3 = c15;
            y4 = c16;
            final int month = month(c9, c10, c11);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c6;
            d2 = c7;
            h0 = c18;
            h2 = c19;
            i0 = c30;
            i2 = c21;
            s0 = c23;
            s2 = c24;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 25;
            isTimeZone = true;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 29 || c29 == '[' || c29 == '|' || c29 == '+' || c29 == '-' || c29 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = c26;
            S8 = c27;
            S9 = c28;
            zoneIdBegin = 29;
            isTimeZone = (c29 == '|');
        }
        else if (len == 22 && (c18 == '+' || c18 == '-')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
            h0 = c9;
            h2 = c10;
            i0 = c11;
            i2 = c12;
            s0 = c13;
            s2 = c14;
            S0 = c15;
            S2 = c16;
            S3 = c17;
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 17;
        }
        else {
            if ((len == 32 && c7 == ',' && c8 == ' ' && c11 == '-' && c15 == '-' && c20 == ' ' && c22 == ':' && c25 == ':' && str[off + 28] == 32) || (len == 33 && c8 == ',' && c9 == ' ' && c12 == '-' && c16 == '-' && c30 == ' ' && c23 == ':' && c26 == ':' && str[off + 29] == 32) || (len == 34 && c9 == ',' && c10 == ' ' && c13 == '-' && c17 == '-' && c21 == ' ' && c24 == ':' && c27 == ':' && str[off + 30] == 32) || (len == 35 && c10 == ',' && c11 == ' ' && c14 == '-' && c18 == '-' && c22 == ' ' && c25 == ':' && c28 == ':' && str[off + 31] == 32)) {
                return parseZonedDateTimeCookie(new String(str, off, len));
            }
            if (len == 34) {
                DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_34;
                if (formatter == null) {
                    formatter = (DateUtils.DATE_TIME_FORMATTER_34 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss O yyyy"));
                }
                return ZonedDateTime.parse(new String(str, off, len), formatter);
            }
            if (len == 31 && str[off + 3] == 44) {
                DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_RFC_2822;
                if (formatter == null) {
                    formatter = (DateUtils.DATE_TIME_FORMATTER_RFC_2822 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z"));
                }
                return ZonedDateTime.parse(new String(str, off, len), formatter);
            }
            return null;
        }
        if (pm) {
            final int hourValue = hourAfterNoon(h0, h2);
            h0 = (char)(hourValue >> 16);
            h2 = (char)(short)hourValue;
        }
        final LocalDateTime ldt = localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2, S0, S2, S3, S4, S5, S6, S7, S8, S9);
        if (ldt == null) {
            return null;
        }
        ZoneId zoneId = null;
        if (isTimeZone) {
            final String s3;
            final String tzStr = s3 = new String(str, zoneIdBegin, len - zoneIdBegin);
            switch (s3) {
                case "UTC":
                case "[UTC]": {
                    zoneId = ZoneOffset.UTC;
                    break;
                }
                default: {
                    final TimeZone timeZone = TimeZone.getTimeZone(tzStr);
                    zoneId = timeZone.toZoneId();
                    break;
                }
            }
        }
        else if (zoneIdBegin == len) {
            zoneId = defaultZoneId;
        }
        else {
            final char first = (char)str[off + zoneIdBegin];
            if (first == 'Z') {
                zoneId = ZoneOffset.UTC;
            }
            else {
                if (zoneIdStr == null) {
                    if (first == '+' || first == '-') {
                        zoneIdStr = new String(str, off + zoneIdBegin, len - zoneIdBegin);
                    }
                    else if (first == ' ') {
                        zoneIdStr = new String(str, off + zoneIdBegin + 1, len - zoneIdBegin - 1);
                    }
                    else if (zoneIdBegin < len) {
                        zoneIdStr = new String(str, off + zoneIdBegin + 1, len - zoneIdBegin - 2);
                    }
                }
                zoneId = getZoneId(zoneIdStr, defaultZoneId);
            }
        }
        if (zoneId == null) {
            zoneId = defaultZoneId;
        }
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        return ZonedDateTime.ofLocal(ldt, zoneId, null);
    }
    
    public static ZonedDateTime parseZonedDateTime(final char[] str, final int off, final int len, final ZoneId defaultZoneId) {
        if (str == null) {
            return null;
        }
        if (len == 0) {
            return null;
        }
        if (len == 16) {
            return parseZonedDateTime16(str, off, defaultZoneId);
        }
        if (len < 19) {
            final String input = new String(str, off, str.length - off);
            throw new DateTimeParseException("illegal input " + input, input, 0);
        }
        String zoneIdStr = null;
        final char c0 = str[off];
        final char c2 = str[off + 1];
        final char c3 = str[off + 2];
        final char c4 = str[off + 3];
        final char c5 = str[off + 4];
        final char c6 = str[off + 5];
        final char c7 = str[off + 6];
        final char c8 = str[off + 7];
        final char c9 = str[off + 8];
        final char c10 = str[off + 9];
        final char c11 = str[off + 10];
        final char c12 = str[off + 11];
        final char c13 = str[off + 12];
        final char c14 = str[off + 13];
        final char c15 = str[off + 14];
        final char c16 = str[off + 15];
        final char c17 = str[off + 16];
        final char c18 = str[off + 17];
        final char c19 = str[off + 18];
        final char c20 = (len == 19) ? ' ' : str[off + 19];
        char c21 = '0';
        char c22 = '0';
        char c23 = '0';
        char c24 = '0';
        char c25 = '0';
        char c26 = '0';
        char c27 = '0';
        char c28 = '0';
        char c29 = '\0';
        char c30 = '\0';
        switch (len) {
            case 19:
            case 20: {
                c30 = '\0';
                break;
            }
            case 21: {
                c30 = str[off + 20];
                break;
            }
            case 22: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                break;
            }
            case 23: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                break;
            }
            case 24: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                break;
            }
            case 25: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                break;
            }
            case 26: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                c25 = str[off + 25];
                break;
            }
            case 27: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                c25 = str[off + 25];
                c26 = str[off + 26];
                break;
            }
            case 28: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                c25 = str[off + 25];
                c26 = str[off + 26];
                c27 = str[off + 27];
                break;
            }
            case 29: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                c25 = str[off + 25];
                c26 = str[off + 26];
                c27 = str[off + 27];
                c28 = str[off + 28];
                break;
            }
            default: {
                c30 = str[off + 20];
                c21 = str[off + 21];
                c22 = str[off + 22];
                c23 = str[off + 23];
                c24 = str[off + 24];
                c25 = str[off + 25];
                c26 = str[off + 26];
                c27 = str[off + 27];
                c28 = str[off + 28];
                c29 = str[off + 29];
                break;
            }
        }
        boolean isTimeZone = false;
        boolean pm = false;
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        char S0;
        char S2;
        char S3;
        char S4;
        char S5;
        char S6;
        char S7;
        char S8;
        char S9;
        int zoneIdBegin;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && (c20 == '[' || c20 == 'Z' || c20 == '+' || c20 == '-' || c20 == ' ')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 19;
        }
        else if (c5 == '-' && c8 == '-' && c11 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':' && len == 20) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 20;
        }
        else if (len == 20 && c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c4, c5, c6);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c0;
            d2 = c2;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 20;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 21 || c21 == '[' || c21 == '+' || c21 == '-' || c21 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 21;
            isTimeZone = (c21 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 22 || c22 == '[' || c22 == '+' || c22 == '-' || c22 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
            isTimeZone = (c22 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == 'Z' && c18 == '[' && c21 == ']' && len == 22) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = '0';
            s2 = '0';
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            isTimeZone = true;
            zoneIdBegin = 17;
        }
        else if (len == 22 && c4 == ' ' && c6 == ',' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && (c30 == 'A' || c30 == 'P') && c21 == 'M') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c5;
            h0 = '0';
            h2 = c13;
            pm = (c30 == 'P');
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
        }
        else if (len == 22 && c3 == '/' && c6 == '/' && c11 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && (c30 == 'A' || c30 == 'P') && c21 == 'M') {
            m0 = c0;
            m2 = c2;
            d0 = c4;
            d2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            pm = (c30 == 'P');
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 22;
        }
        else if (len == 23 && c4 == ' ' && c6 == ',' && c7 == ' ' && c12 == ' ' && c15 == ':' && c18 == ':' && c30 == ' ' && (c21 == 'A' || c21 == 'P') && c22 == 'M') {
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c5;
            h0 = c13;
            h2 = c14;
            pm = (c21 == 'P');
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
        }
        else if (len == 23 && c4 == ' ' && c7 == ',' && c8 == ' ' && c13 == ' ' && c15 == ':' && c18 == ':' && c30 == ' ' && (c21 == 'A' || c21 == 'P') && c22 == 'M') {
            y0 = c9;
            y2 = c10;
            y3 = c11;
            y4 = c12;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c5;
            d2 = c6;
            h0 = '0';
            h2 = c14;
            pm = (c21 == 'P');
            i0 = c16;
            i2 = c17;
            s0 = c19;
            s2 = c20;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
        }
        else if (len == 24 && c4 == ' ' && c7 == ',' && c8 == ' ' && c13 == ' ' && c16 == ':' && c19 == ':' && c21 == ' ' && (c22 == 'A' || c22 == 'P') && c23 == 'M') {
            y0 = c9;
            y2 = c10;
            y3 = c11;
            y4 = c12;
            final int month = month(c0, c2, c3);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c5;
            d2 = c6;
            h0 = c14;
            h2 = c15;
            pm = (c22 == 'P');
            i0 = c17;
            i2 = c18;
            s0 = c20;
            s2 = c30;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 23 || c23 == '[' || c23 == '|' || c23 == '+' || c23 == '-' || c23 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 23;
            isTimeZone = (c23 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 24 || c24 == '[' || c24 == '|' || c24 == '+' || c24 == '-' || c24 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
            isTimeZone = (c24 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 25 || c25 == '[' || c25 == '|' || c25 == '+' || c25 == '-' || c25 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 25;
            isTimeZone = (c25 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 26 || c26 == '[' || c26 == '|' || c26 == '+' || c26 == '-' || c26 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 26;
            isTimeZone = (c26 == '|');
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 27 || c27 == '[' || c27 == '|' || c27 == '+' || c27 == '-' || c27 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            if (c23 == ' ') {
                S4 = '0';
                S5 = '0';
                S6 = '0';
                S7 = '0';
                S8 = '0';
                S9 = '0';
                zoneIdBegin = 23;
            }
            else {
                S4 = c23;
                S5 = c24;
                S6 = c25;
                S7 = c26;
                S8 = '0';
                S9 = '0';
                zoneIdBegin = 27;
                isTimeZone = (c27 == '|');
            }
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 28 || c28 == '[' || c28 == '|' || c28 == '+' || c28 == '-' || c28 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = c26;
            S8 = c27;
            S9 = '0';
            zoneIdBegin = 28;
            isTimeZone = (c28 == '|');
        }
        else if (len == 28 && c4 == ' ' && c8 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':' && c20 == ' ' && c23 == ' ') {
            final int month = month(c5, c6, c7);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            y0 = c24;
            y2 = c25;
            y3 = c26;
            y4 = c27;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 19;
            zoneIdStr = new String(str, off + 20, 3);
        }
        else if (len == 28 && c4 == ',' && c5 == ' ' && c7 == ' ' && c11 == ' ' && c16 == ' ' && c19 == ':' && c21 == ':' && c24 == ' ') {
            y0 = c12;
            y2 = c13;
            y3 = c14;
            y4 = c15;
            final int month = month(c8, c9, c10);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = '0';
            d2 = c6;
            h0 = c17;
            h2 = c18;
            i0 = c20;
            i2 = c30;
            s0 = c22;
            s2 = c23;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 24;
            isTimeZone = true;
        }
        else if (len == 29 && c4 == ',' && c5 == ' ' && c8 == ' ' && c12 == ' ' && c17 == ' ' && c20 == ':' && c22 == ':' && c25 == ' ') {
            y0 = c13;
            y2 = c14;
            y3 = c15;
            y4 = c16;
            final int month = month(c9, c10, c11);
            if (month > 0) {
                m0 = (char)(48 + month / 10);
                m2 = (char)(48 + month % 10);
            }
            else {
                m0 = '0';
                m2 = '0';
            }
            d0 = c6;
            d2 = c7;
            h0 = c18;
            h2 = c19;
            i0 = c30;
            i2 = c21;
            s0 = c23;
            s2 = c24;
            S0 = '0';
            S2 = '0';
            S3 = '0';
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 25;
            isTimeZone = true;
        }
        else if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':' && c20 == '.' && (len == 29 || c29 == '[' || c29 == '|' || c29 == '+' || c29 == '-' || c29 == 'Z')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
            S0 = c30;
            S2 = c21;
            S3 = c22;
            S4 = c23;
            S5 = c24;
            S6 = c25;
            S7 = c26;
            S8 = c27;
            S9 = c28;
            zoneIdBegin = 29;
            isTimeZone = (c29 == '|');
        }
        else if (len == 22 && (c18 == '+' || c18 == '-')) {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c5;
            m2 = c6;
            d0 = c7;
            d2 = c8;
            h0 = c9;
            h2 = c10;
            i0 = c11;
            i2 = c12;
            s0 = c13;
            s2 = c14;
            S0 = c15;
            S2 = c16;
            S3 = c17;
            S4 = '0';
            S5 = '0';
            S6 = '0';
            S7 = '0';
            S8 = '0';
            S9 = '0';
            zoneIdBegin = 17;
        }
        else {
            if ((len == 32 && c7 == ',' && c8 == ' ' && c11 == '-' && c15 == '-' && c20 == ' ' && c22 == ':' && c25 == ':' && str[off + 28] == ' ') || (len == 33 && c8 == ',' && c9 == ' ' && c12 == '-' && c16 == '-' && c30 == ' ' && c23 == ':' && c26 == ':' && str[off + 29] == ' ') || (len == 34 && c9 == ',' && c10 == ' ' && c13 == '-' && c17 == '-' && c21 == ' ' && c24 == ':' && c27 == ':' && str[off + 30] == ' ') || (len == 35 && c10 == ',' && c11 == ' ' && c14 == '-' && c18 == '-' && c22 == ' ' && c25 == ':' && c28 == ':' && str[off + 31] == ' ')) {
                return parseZonedDateTimeCookie(new String(str, off, len));
            }
            if (len == 34) {
                DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_34;
                if (formatter == null) {
                    formatter = (DateUtils.DATE_TIME_FORMATTER_34 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss O yyyy"));
                }
                return ZonedDateTime.parse(new String(str, off, len), formatter);
            }
            if (len == 31 && str[off + 3] == ',') {
                DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_RFC_2822;
                if (formatter == null) {
                    formatter = (DateUtils.DATE_TIME_FORMATTER_RFC_2822 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z"));
                }
                return ZonedDateTime.parse(new String(str, off, len), formatter);
            }
            return null;
        }
        if (pm) {
            final int hourValue = hourAfterNoon(h0, h2);
            h0 = (char)(hourValue >> 16);
            h2 = (char)(short)hourValue;
        }
        final LocalDateTime ldt = localDateTime(y0, y2, y3, y4, m0, m2, d0, d2, h0, h2, i0, i2, s0, s2, S0, S2, S3, S4, S5, S6, S7, S8, S9);
        if (ldt == null) {
            return null;
        }
        ZoneId zoneId = null;
        if (isTimeZone) {
            final String s3;
            final String tzStr = s3 = new String(str, zoneIdBegin, len - zoneIdBegin);
            switch (s3) {
                case "UTC":
                case "[UTC]": {
                    zoneId = ZoneOffset.UTC;
                    break;
                }
                default: {
                    final TimeZone timeZone = TimeZone.getTimeZone(tzStr);
                    zoneId = timeZone.toZoneId();
                    break;
                }
            }
        }
        else if (zoneIdBegin == len) {
            zoneId = defaultZoneId;
        }
        else {
            final char first = str[off + zoneIdBegin];
            if (first == 'Z') {
                zoneId = ZoneOffset.UTC;
            }
            else {
                if (zoneIdStr == null) {
                    if (first == '+' || first == '-') {
                        zoneIdStr = new String(str, off + zoneIdBegin, len - zoneIdBegin);
                    }
                    else if (first == ' ') {
                        zoneIdStr = new String(str, off + zoneIdBegin + 1, len - zoneIdBegin - 1);
                    }
                    else if (zoneIdBegin < len) {
                        zoneIdStr = new String(str, off + zoneIdBegin + 1, len - zoneIdBegin - 2);
                    }
                }
                zoneId = getZoneId(zoneIdStr, defaultZoneId);
            }
        }
        if (zoneId == null) {
            zoneId = defaultZoneId;
        }
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        return ZonedDateTime.ofLocal(ldt, zoneId, null);
    }
    
    static ZonedDateTime parseZonedDateTimeCookie(final String str) {
        if (str.endsWith(" CST")) {
            DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_COOKIE_LOCAL;
            if (formatter == null) {
                formatter = (DateUtils.DATE_TIME_FORMATTER_COOKIE_LOCAL = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss"));
            }
            final String strLocalDateTime = str.substring(0, str.length() - 4);
            final LocalDateTime ldt = LocalDateTime.parse(strLocalDateTime, formatter);
            return ZonedDateTime.of(ldt, DateUtils.SHANGHAI_ZONE_ID);
        }
        DateTimeFormatter formatter = DateUtils.DATE_TIME_FORMATTER_COOKIE;
        if (formatter == null) {
            formatter = (DateUtils.DATE_TIME_FORMATTER_COOKIE = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz"));
        }
        return ZonedDateTime.parse(str, formatter);
    }
    
    public static ZoneId getZoneId(final String zoneIdStr, final ZoneId defaultZoneId) {
        if (zoneIdStr == null) {
            return (defaultZoneId != null) ? defaultZoneId : DateUtils.DEFAULT_ZONE_ID;
        }
        ZoneId zoneId = null;
        switch (zoneIdStr) {
            case "000": {
                zoneId = ZoneOffset.UTC;
                break;
            }
            case "+08:00": {
                zoneId = DateUtils.OFFSET_8_ZONE_ID;
                break;
            }
            case "CST": {
                zoneId = DateUtils.SHANGHAI_ZONE_ID;
                break;
            }
            default: {
                final char c0;
                if (zoneIdStr.length() > 0 && ((c0 = zoneIdStr.charAt(0)) == '+' || c0 == '-') && zoneIdStr.charAt(zoneIdStr.length() - 1) != ']') {
                    zoneId = ZoneOffset.of(zoneIdStr);
                    break;
                }
                final int p0;
                final int p2;
                if ((p0 = zoneIdStr.indexOf(91)) > 0 && (p2 = zoneIdStr.indexOf(93, p0)) > 0) {
                    final String str = zoneIdStr.substring(p0 + 1, p2);
                    zoneId = ZoneId.of(str);
                    break;
                }
                zoneId = ZoneId.of(zoneIdStr);
                break;
            }
        }
        return zoneId;
    }
    
    public static long parseMillisYMDHMS19(final String str, ZoneId zoneId) {
        if (str == null) {
            return 0L;
        }
        char c0;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        char c17;
        char c18;
        char c19;
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            if (chars.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = chars[0];
            c2 = chars[1];
            c3 = chars[2];
            c4 = chars[3];
            c5 = chars[4];
            c6 = chars[5];
            c7 = chars[6];
            c8 = chars[7];
            c9 = chars[8];
            c10 = chars[9];
            c11 = chars[10];
            c12 = chars[11];
            c13 = chars[12];
            c14 = chars[13];
            c15 = chars[14];
            c16 = chars[15];
            c17 = chars[16];
            c18 = chars[17];
            c19 = chars[18];
        }
        else if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0 && JDKUtils.STRING_VALUE != null) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            if (bytes.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = (char)bytes[0];
            c2 = (char)bytes[1];
            c3 = (char)bytes[2];
            c4 = (char)bytes[3];
            c5 = (char)bytes[4];
            c6 = (char)bytes[5];
            c7 = (char)bytes[6];
            c8 = (char)bytes[7];
            c9 = (char)bytes[8];
            c10 = (char)bytes[9];
            c11 = (char)bytes[10];
            c12 = (char)bytes[11];
            c13 = (char)bytes[12];
            c14 = (char)bytes[13];
            c15 = (char)bytes[14];
            c16 = (char)bytes[15];
            c17 = (char)bytes[16];
            c18 = (char)bytes[17];
            c19 = (char)bytes[18];
        }
        else {
            if (str.length() != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = str.charAt(0);
            c2 = str.charAt(1);
            c3 = str.charAt(2);
            c4 = str.charAt(3);
            c5 = str.charAt(4);
            c6 = str.charAt(5);
            c7 = str.charAt(6);
            c8 = str.charAt(7);
            c9 = str.charAt(8);
            c10 = str.charAt(9);
            c11 = str.charAt(10);
            c12 = str.charAt(11);
            c13 = str.charAt(12);
            c14 = str.charAt(13);
            c15 = str.charAt(14);
            c16 = str.charAt(15);
            c17 = str.charAt(16);
            c18 = str.charAt(17);
            c19 = str.charAt(18);
        }
        if (c5 != '-' || c8 != '-' || c11 != ' ' || c14 != ':' || c17 != ':') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        final char y0 = c0;
        final char y2 = c2;
        final char y3 = c3;
        final char y4 = c4;
        final char m0 = c6;
        final char m2 = c7;
        final char d0 = c9;
        final char d2 = c10;
        final char h0 = c12;
        final char h2 = c13;
        final char i0 = c15;
        final char i2 = c16;
        final char s0 = c18;
        final char s2 = c19;
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int month = (m0 - '0') * 10 + (m2 - '0');
        if ((month == 0 && year != 0) || month > 12) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (year == 0 && month == 0 && dom == 0) {
                year = 1970;
                month = 1;
                dom = 1;
            }
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month - 362) / 12 + (dom - 1);
            if (month > 2) {
                --total;
                final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                if (!leapYear2) {
                    --total;
                }
            }
            final long epochDay = total - 719528L;
            final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
            if (zoneId == null) {
                zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
            final long SECONDS_1991_09_15_02 = 684900000L;
            int zoneOffsetTotalSeconds;
            if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
                zoneOffsetTotalSeconds = 28800;
            }
            else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
                zoneOffsetTotalSeconds = 0;
            }
            else {
                final LocalDate localDate = LocalDate.of(year, month, dom);
                final LocalTime localTime = LocalTime.of(hour, minute, second, 0);
                final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
                zoneOffsetTotalSeconds = offset.getTotalSeconds();
            }
            return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        }
        throw new DateTimeParseException("illegal input", str, 0);
    }
    
    static long parseMillis19(final String str, ZoneId zoneId, final DateTimeFormatPattern pattern) {
        if (str == null || "null".equals(str)) {
            return 0L;
        }
        if (pattern.length != 19) {
            throw new UnsupportedOperationException();
        }
        char c0;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        char c17;
        char c18;
        char c19;
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            if (chars.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = chars[0];
            c2 = chars[1];
            c3 = chars[2];
            c4 = chars[3];
            c5 = chars[4];
            c6 = chars[5];
            c7 = chars[6];
            c8 = chars[7];
            c9 = chars[8];
            c10 = chars[9];
            c11 = chars[10];
            c12 = chars[11];
            c13 = chars[12];
            c14 = chars[13];
            c15 = chars[14];
            c16 = chars[15];
            c17 = chars[16];
            c18 = chars[17];
            c19 = chars[18];
        }
        else if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            if (bytes.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = (char)bytes[0];
            c2 = (char)bytes[1];
            c3 = (char)bytes[2];
            c4 = (char)bytes[3];
            c5 = (char)bytes[4];
            c6 = (char)bytes[5];
            c7 = (char)bytes[6];
            c8 = (char)bytes[7];
            c9 = (char)bytes[8];
            c10 = (char)bytes[9];
            c11 = (char)bytes[10];
            c12 = (char)bytes[11];
            c13 = (char)bytes[12];
            c14 = (char)bytes[13];
            c15 = (char)bytes[14];
            c16 = (char)bytes[15];
            c17 = (char)bytes[16];
            c18 = (char)bytes[17];
            c19 = (char)bytes[18];
        }
        else {
            if (str.length() != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = str.charAt(0);
            c2 = str.charAt(1);
            c3 = str.charAt(2);
            c4 = str.charAt(3);
            c5 = str.charAt(4);
            c6 = str.charAt(5);
            c7 = str.charAt(6);
            c8 = str.charAt(7);
            c9 = str.charAt(8);
            c10 = str.charAt(9);
            c11 = str.charAt(10);
            c12 = str.charAt(11);
            c13 = str.charAt(12);
            c14 = str.charAt(13);
            c15 = str.charAt(14);
            c16 = str.charAt(15);
            c17 = str.charAt(16);
            c18 = str.charAt(17);
            c19 = str.charAt(18);
        }
        char y0 = '\0';
        char y2 = '\0';
        char y3 = '\0';
        char y4 = '\0';
        char m0 = '\0';
        char m2 = '\0';
        char d0 = '\0';
        char d2 = '\0';
        char h0 = '\0';
        char h2 = '\0';
        char i0 = '\0';
        char i2 = '\0';
        char s0 = '\0';
        char s2 = '\0';
        switch (pattern) {
            case DATE_TIME_FORMAT_19_DASH: {
                if (c5 != '-' || c8 != '-' || c11 != ' ' || c14 != ':' || c17 != ':') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                y0 = c0;
                y2 = c2;
                y3 = c3;
                y4 = c4;
                m0 = c6;
                m2 = c7;
                d0 = c9;
                d2 = c10;
                h0 = c12;
                h2 = c13;
                i0 = c15;
                i2 = c16;
                s0 = c18;
                s2 = c19;
                break;
            }
            case DATE_TIME_FORMAT_19_DASH_T: {
                if (c5 != '-' || c8 != '-' || c11 != 'T' || c14 != ':' || c17 != ':') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                y0 = c0;
                y2 = c2;
                y3 = c3;
                y4 = c4;
                m0 = c6;
                m2 = c7;
                d0 = c9;
                d2 = c10;
                h0 = c12;
                h2 = c13;
                i0 = c15;
                i2 = c16;
                s0 = c18;
                s2 = c19;
                break;
            }
            case DATE_TIME_FORMAT_19_SLASH: {
                if (c5 != '/' || c8 != '/' || c11 != ' ' || c14 != ':' || c17 != ':') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                y0 = c0;
                y2 = c2;
                y3 = c3;
                y4 = c4;
                m0 = c6;
                m2 = c7;
                d0 = c9;
                d2 = c10;
                h0 = c12;
                h2 = c13;
                i0 = c15;
                i2 = c16;
                s0 = c18;
                s2 = c19;
                break;
            }
            case DATE_TIME_FORMAT_19_DOT: {
                if (c3 != '.' || c6 != '.' || c11 != ' ' || c14 != ':' || c17 != ':') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                d0 = c0;
                d2 = c2;
                m0 = c4;
                m2 = c5;
                y0 = c7;
                y2 = c8;
                y3 = c9;
                y4 = c10;
                h0 = c12;
                h2 = c13;
                i0 = c15;
                i2 = c16;
                s0 = c18;
                s2 = c19;
                break;
            }
            default: {
                throw new DateTimeParseException("illegal input", str, 0);
            }
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int month = (m0 - '0') * 10 + (m2 - '0');
        if ((month == 0 && year != 0) || month > 12) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (year == 0 && month == 0 && dom == 0) {
                year = 1970;
                month = 1;
                dom = 1;
            }
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month - 362) / 12 + (dom - 1);
            if (month > 2) {
                --total;
                final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                if (!leapYear2) {
                    --total;
                }
            }
            final long epochDay = total - 719528L;
            final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
            if (zoneId == null) {
                zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
            final long SECONDS_1991_09_15_02 = 684900000L;
            int zoneOffsetTotalSeconds;
            if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
                zoneOffsetTotalSeconds = 28800;
            }
            else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
                zoneOffsetTotalSeconds = 0;
            }
            else {
                final LocalDate localDate = LocalDate.of(year, month, dom);
                final LocalTime localTime = LocalTime.of(hour, minute, second, 0);
                final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
                zoneOffsetTotalSeconds = offset.getTotalSeconds();
            }
            return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        }
        throw new DateTimeParseException("illegal input", str, 0);
    }
    
    static long parseMillis10(final String str, final ZoneId zoneId, final DateTimeFormatPattern pattern) {
        if (str == null || "null".equals(str)) {
            return 0L;
        }
        if (pattern.length != 10) {
            throw new UnsupportedOperationException();
        }
        char c0;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            if (chars.length != 10) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = chars[0];
            c2 = chars[1];
            c3 = chars[2];
            c4 = chars[3];
            c5 = chars[4];
            c6 = chars[5];
            c7 = chars[6];
            c8 = chars[7];
            c9 = chars[8];
            c10 = chars[9];
        }
        else if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
            if (bytes.length != 10) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = (char)bytes[0];
            c2 = (char)bytes[1];
            c3 = (char)bytes[2];
            c4 = (char)bytes[3];
            c5 = (char)bytes[4];
            c6 = (char)bytes[5];
            c7 = (char)bytes[6];
            c8 = (char)bytes[7];
            c9 = (char)bytes[8];
            c10 = (char)bytes[9];
        }
        else {
            if (str.length() != 10) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = str.charAt(0);
            c2 = str.charAt(1);
            c3 = str.charAt(2);
            c4 = str.charAt(3);
            c5 = str.charAt(4);
            c6 = str.charAt(5);
            c7 = str.charAt(6);
            c8 = str.charAt(7);
            c9 = str.charAt(8);
            c10 = str.charAt(9);
        }
        char y0 = '\0';
        char y2 = '\0';
        char y3 = '\0';
        char y4 = '\0';
        char m0 = '\0';
        char m2 = '\0';
        char d0 = '\0';
        char d2 = '\0';
        switch (pattern) {
            case DATE_FORMAT_10_DASH: {
                if (c5 != '-' || c8 != '-') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                y0 = c0;
                y2 = c2;
                y3 = c3;
                y4 = c4;
                m0 = c6;
                m2 = c7;
                d0 = c9;
                d2 = c10;
                break;
            }
            case DATE_FORMAT_10_SLASH: {
                if (c5 != '/' || c8 != '/') {
                    throw new DateTimeParseException("illegal input", str, 0);
                }
                y0 = c0;
                y2 = c2;
                y3 = c3;
                y4 = c4;
                m0 = c6;
                m2 = c7;
                d0 = c9;
                d2 = c10;
                break;
            }
            default: {
                throw new DateTimeParseException("illegal input", str, 0);
            }
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int month = (m0 - '0') * 10 + (m2 - '0');
        if ((month == 0 && year != 0) || month > 12) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (year == 0 && month == 0 && dom == 0) {
            year = 1970;
            month = 1;
            dom = 1;
        }
        final int DAYS_PER_CYCLE = 146097;
        final long DAYS_0000_TO_1970 = 719528L;
        long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month - 362) / 12 + (dom - 1);
        if (month > 2) {
            --total;
            final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
            if (!leapYear2) {
                --total;
            }
        }
        final long epochDay = total - 719528L;
        final long utcSeconds = epochDay * 86400L;
        final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
        final long SECONDS_1991_09_15_02 = 684900000L;
        int zoneOffsetTotalSeconds;
        if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
            zoneOffsetTotalSeconds = 0;
        }
        else {
            final LocalDate localDate = LocalDate.of(year, month, dom);
            final LocalDateTime ldt = LocalDateTime.of(localDate, LocalTime.MIN);
            final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
            zoneOffsetTotalSeconds = offset.getTotalSeconds();
        }
        return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
    }
    
    public static long parseMillis19(final String str, ZoneId zoneId) {
        if (str == null) {
            throw new NullPointerException();
        }
        char c0;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        char c17;
        char c18;
        char c19;
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            if (chars.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = chars[0];
            c2 = chars[1];
            c3 = chars[2];
            c4 = chars[3];
            c5 = chars[4];
            c6 = chars[5];
            c7 = chars[6];
            c8 = chars[7];
            c9 = chars[8];
            c10 = chars[9];
            c11 = chars[10];
            c12 = chars[11];
            c13 = chars[12];
            c14 = chars[13];
            c15 = chars[14];
            c16 = chars[15];
            c17 = chars[16];
            c18 = chars[17];
            c19 = chars[18];
        }
        else if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            final byte[] chars2 = JDKUtils.STRING_VALUE.apply(str);
            if (chars2.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = (char)chars2[0];
            c2 = (char)chars2[1];
            c3 = (char)chars2[2];
            c4 = (char)chars2[3];
            c5 = (char)chars2[4];
            c6 = (char)chars2[5];
            c7 = (char)chars2[6];
            c8 = (char)chars2[7];
            c9 = (char)chars2[8];
            c10 = (char)chars2[9];
            c11 = (char)chars2[10];
            c12 = (char)chars2[11];
            c13 = (char)chars2[12];
            c14 = (char)chars2[13];
            c15 = (char)chars2[14];
            c16 = (char)chars2[15];
            c17 = (char)chars2[16];
            c18 = (char)chars2[17];
            c19 = (char)chars2[18];
        }
        else {
            if (str.length() != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            c0 = str.charAt(0);
            c2 = str.charAt(1);
            c3 = str.charAt(2);
            c4 = str.charAt(3);
            c5 = str.charAt(4);
            c6 = str.charAt(5);
            c7 = str.charAt(6);
            c8 = str.charAt(7);
            c9 = str.charAt(8);
            c10 = str.charAt(9);
            c11 = str.charAt(10);
            c12 = str.charAt(11);
            c13 = str.charAt(12);
            c14 = str.charAt(13);
            c15 = str.charAt(14);
            c16 = str.charAt(15);
            c17 = str.charAt(16);
            c18 = str.charAt(17);
            c19 = str.charAt(18);
        }
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c5 == '/' && c8 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '/' && c6 == '/' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '.' && c6 == '.' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = '0';
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else {
            if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c15 != ':' || c18 != ':') {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = '0';
            s2 = c19;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        int month2 = (m0 - '0') * 10 + (m2 - '0');
        if ((month2 == 0 && year != 0) || month2 > 12) {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month2) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (year == 0 && month2 == 0 && dom == 0) {
                year = 1970;
                month2 = 1;
                dom = 1;
            }
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month2 - 362) / 12 + (dom - 1);
            if (month2 > 2) {
                --total;
                final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                if (!leapYear2) {
                    --total;
                }
            }
            final long epochDay = total - 719528L;
            final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
            if (zoneId == null) {
                zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
            final long SECONDS_1991_09_15_02 = 684900000L;
            int zoneOffsetTotalSeconds;
            if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
                zoneOffsetTotalSeconds = 28800;
            }
            else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
                zoneOffsetTotalSeconds = 0;
            }
            else {
                final LocalDate localDate = LocalDate.of(year, month2, dom);
                final LocalTime localTime = LocalTime.of(hour, minute, second, 0);
                final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
                zoneOffsetTotalSeconds = offset.getTotalSeconds();
            }
            return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        }
        throw new DateTimeParseException("illegal input " + str, str, 0);
    }
    
    public static long parseMillis19(final byte[] bytes, final int off, ZoneId zoneId) {
        if (bytes == null) {
            throw new NullPointerException();
        }
        final char c0 = (char)bytes[off];
        final char c2 = (char)bytes[off + 1];
        final char c3 = (char)bytes[off + 2];
        final char c4 = (char)bytes[off + 3];
        final char c5 = (char)bytes[off + 4];
        final char c6 = (char)bytes[off + 5];
        final char c7 = (char)bytes[off + 6];
        final char c8 = (char)bytes[off + 7];
        final char c9 = (char)bytes[off + 8];
        final char c10 = (char)bytes[off + 9];
        final char c11 = (char)bytes[off + 10];
        final char c12 = (char)bytes[off + 11];
        final char c13 = (char)bytes[off + 12];
        final char c14 = (char)bytes[off + 13];
        final char c15 = (char)bytes[off + 14];
        final char c16 = (char)bytes[off + 15];
        final char c17 = (char)bytes[off + 16];
        final char c18 = (char)bytes[off + 17];
        final char c19 = (char)bytes[off + 18];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c5 == '/' && c8 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '/' && c6 == '/' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '.' && c6 == '.' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = '0';
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else {
            if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c15 != ':' || c18 != ':') {
                final String str2 = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str2, str2, 0);
            }
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = '0';
            s2 = c19;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String str = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String str3 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str3, str3, 0);
        }
        int month2 = (m0 - '0') * 10 + (m2 - '0');
        if ((month2 == 0 && year != 0) || month2 > 12) {
            final String str3 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str3, str3, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String str4 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str4, str4, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month2) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            final String str5 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str5, str5, 0);
        }
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String str5 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str5, str5, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String str6 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str6, str6, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (year == 0 && month2 == 0 && dom == 0) {
                year = 1970;
                month2 = 1;
                dom = 1;
            }
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month2 - 362) / 12 + (dom - 1);
            if (month2 > 2) {
                --total;
                final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                if (!leapYear2) {
                    --total;
                }
            }
            final long epochDay = total - 719528L;
            final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
            if (zoneId == null) {
                zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
            final long SECONDS_1991_09_15_02 = 684900000L;
            int zoneOffsetTotalSeconds;
            if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
                zoneOffsetTotalSeconds = 28800;
            }
            else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
                zoneOffsetTotalSeconds = 0;
            }
            else {
                final LocalDate localDate = LocalDate.of(year, month2, dom);
                final LocalTime localTime = LocalTime.of(hour, minute, second, 0);
                final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
                zoneOffsetTotalSeconds = offset.getTotalSeconds();
            }
            return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        }
        final String str7 = new String(bytes, off, 19);
        throw new DateTimeParseException("illegal input " + str7, str7, 0);
    }
    
    public static long parseMillis19(final char[] bytes, final int off, ZoneId zoneId) {
        if (bytes == null) {
            throw new NullPointerException();
        }
        final char c0 = bytes[off];
        final char c2 = bytes[off + 1];
        final char c3 = bytes[off + 2];
        final char c4 = bytes[off + 3];
        final char c5 = bytes[off + 4];
        final char c6 = bytes[off + 5];
        final char c7 = bytes[off + 6];
        final char c8 = bytes[off + 7];
        final char c9 = bytes[off + 8];
        final char c10 = bytes[off + 9];
        final char c11 = bytes[off + 10];
        final char c12 = bytes[off + 11];
        final char c13 = bytes[off + 12];
        final char c14 = bytes[off + 13];
        final char c15 = bytes[off + 14];
        final char c16 = bytes[off + 15];
        final char c17 = bytes[off + 16];
        final char c18 = bytes[off + 17];
        final char c19 = bytes[off + 18];
        char y0;
        char y2;
        char y3;
        char y4;
        char m0;
        char m2;
        char d0;
        char d2;
        char h0;
        char h2;
        char i0;
        char i2;
        char s0;
        char s2;
        if (c5 == '-' && c8 == '-' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c5 == '/' && c8 == '/' && (c11 == ' ' || c11 == 'T') && c14 == ':' && c17 == ':') {
            y0 = c0;
            y2 = c2;
            y3 = c3;
            y4 = c4;
            m0 = c6;
            m2 = c7;
            d0 = c9;
            d2 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '/' && c6 == '/' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == '.' && c6 == '.' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            m0 = c4;
            m2 = c5;
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c2 == ' ' && c6 == ' ' && c11 == ' ' && c14 == ':' && c17 == ':') {
            d0 = '0';
            d2 = c0;
            final int month = month(c3, c4, c5);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c7;
            y2 = c8;
            y3 = c9;
            y4 = c10;
            h0 = c12;
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c14 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = '0';
            h2 = c13;
            i0 = c15;
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else if (c3 == ' ' && c7 == ' ' && c12 == ' ' && c15 == ':' && c17 == ':') {
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = '0';
            i2 = c16;
            s0 = c18;
            s2 = c19;
        }
        else {
            if (c3 != ' ' || c7 != ' ' || c12 != ' ' || c15 != ':' || c18 != ':') {
                final String str2 = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str2, str2, 0);
            }
            d0 = c0;
            d2 = c2;
            final int month = month(c4, c5, c6);
            if (month <= 0) {
                final String str = new String(bytes, off, 19);
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            m0 = (char)(48 + month / 10);
            m2 = (char)(48 + month % 10);
            y0 = c8;
            y2 = c9;
            y3 = c10;
            y4 = c11;
            h0 = c13;
            h2 = c14;
            i0 = c16;
            i2 = c17;
            s0 = '0';
            s2 = c19;
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            final String str = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str, str, 0);
        }
        int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
            final String str3 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str3, str3, 0);
        }
        int month2 = (m0 - '0') * 10 + (m2 - '0');
        if ((month2 == 0 && year != 0) || month2 > 12) {
            final String str3 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str3, str3, 0);
        }
        if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
            final String str4 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str4, str4, 0);
        }
        int dom = (d0 - '0') * 10 + (d2 - '0');
        int max = 31;
        switch (month2) {
            case 2: {
                final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                max = (leapYear ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                max = 30;
                break;
            }
        }
        if ((dom == 0 && year != 0) || dom > max) {
            final String str5 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str5, str5, 0);
        }
        if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
            final String str5 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str5, str5, 0);
        }
        final int hour = (h0 - '0') * 10 + (h2 - '0');
        if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
            final String str6 = new String(bytes, off, 19);
            throw new DateTimeParseException("illegal input " + str6, str6, 0);
        }
        final int minute = (i0 - '0') * 10 + (i2 - '0');
        if (s0 >= '0' && s0 <= '9' && s2 >= '0' && s2 <= '9') {
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (year == 0 && month2 == 0 && dom == 0) {
                year = 1970;
                month2 = 1;
                dom = 1;
            }
            final int DAYS_PER_CYCLE = 146097;
            final long DAYS_0000_TO_1970 = 719528L;
            long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month2 - 362) / 12 + (dom - 1);
            if (month2 > 2) {
                --total;
                final boolean leapYear2 = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
                if (!leapYear2) {
                    --total;
                }
            }
            final long epochDay = total - 719528L;
            final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
            if (zoneId == null) {
                zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
            final long SECONDS_1991_09_15_02 = 684900000L;
            int zoneOffsetTotalSeconds;
            if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
                zoneOffsetTotalSeconds = 28800;
            }
            else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
                zoneOffsetTotalSeconds = 0;
            }
            else {
                final LocalDate localDate = LocalDate.of(year, month2, dom);
                final LocalTime localTime = LocalTime.of(hour, minute, second, 0);
                final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
                zoneOffsetTotalSeconds = offset.getTotalSeconds();
            }
            return (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        }
        final String str7 = new String(bytes, off, 19);
        throw new DateTimeParseException("illegal input " + str7, str7, 0);
    }
    
    public static LocalDateTime localDateTime(final char y0, final char y1, final char y2, final char y3, final char m0, final char m1, final char d0, final char d1, final char h0, final char h1, final char i0, final char i1, final char s0, final char s1) {
        if (y0 < '0' || y0 > '9' || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y1 - '0') * 100 + (y2 - '0') * 10 + (y3 - '0');
        if (m0 < '0' || m0 > '9' || m1 < '0' || m1 > '9') {
            return null;
        }
        final int month = (m0 - '0') * 10 + (m1 - '0');
        if (d0 < '0' || d0 > '9' || d1 < '0' || d1 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d1 - '0');
        if (h0 < '0' || h0 > '9' || h1 < '0' || h1 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h1 - '0');
        if (i0 < '0' || i0 > '9' || i1 < '0' || i1 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i1 - '0');
        if (s0 < '0' || s0 > '9' || s1 < '0' || s1 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s1 - '0');
        if (year == 0 && month == 0 && dom == 0 && hour == 0 && minute == 0 && second == 0) {
            return null;
        }
        if (hour > 24 || minute > 60 || second > 60) {
            return null;
        }
        return LocalDateTime.of(year, month, dom, hour, minute, second, 0);
    }
    
    public static LocalDateTime localDateTime(final char y0, final char y1, final char y2, final char y3, final char m0, final char m1, final char d0, final char d1, final char h0, final char h1, final char i0, final char i1, final char s0, final char s1, final char S0, final char S1, final char S2, final char S3, final char S4, final char S5, final char S6, final char S7, final char S8) {
        if (y0 < '0' || y0 > '9' || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y1 - '0') * 100 + (y2 - '0') * 10 + (y3 - '0');
        if (m0 < '0' || m0 > '9' || m1 < '0' || m1 > '9') {
            return null;
        }
        final int month = (m0 - '0') * 10 + (m1 - '0');
        if (d0 < '0' || d0 > '9' || d1 < '0' || d1 > '9') {
            return null;
        }
        final int dom = (d0 - '0') * 10 + (d1 - '0');
        if (h0 < '0' || h0 > '9' || h1 < '0' || h1 > '9') {
            return null;
        }
        final int hour = (h0 - '0') * 10 + (h1 - '0');
        if (i0 < '0' || i0 > '9' || i1 < '0' || i1 > '9') {
            return null;
        }
        final int minute = (i0 - '0') * 10 + (i1 - '0');
        if (s0 < '0' || s0 > '9' || s1 < '0' || s1 > '9') {
            return null;
        }
        final int second = (s0 - '0') * 10 + (s1 - '0');
        if (S0 >= '0' && S0 <= '9' && S1 >= '0' && S1 <= '9' && S2 >= '0' && S2 <= '9' && S3 >= '0' && S3 <= '9' && S4 >= '0' && S4 <= '9' && S5 >= '0' && S5 <= '9' && S6 >= '0' && S6 <= '9' && S7 >= '0' && S7 <= '9' && S8 >= '0' && S8 <= '9') {
            final int nanos = (S0 - '0') * 100000000 + (S1 - '0') * 10000000 + (S2 - '0') * 1000000 + (S3 - '0') * 100000 + (S4 - '0') * 10000 + (S5 - '0') * 1000 + (S6 - '0') * 100 + (S7 - '0') * 10 + (S8 - '0');
            return LocalDateTime.of(year, month, dom, hour, minute, second, nanos);
        }
        return null;
    }
    
    public static long millis(final LocalDateTime ldt) {
        return millis(null, ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), ldt.getNano());
    }
    
    public static long millis(final LocalDateTime ldt, final ZoneId zoneId) {
        return millis(zoneId, ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), ldt.getNano());
    }
    
    public static long millis(ZoneId zoneId, final int year, final int month, final int dom, final int hour, final int minute, final int second, final int nanoOfSecond) {
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        final int DAYS_PER_CYCLE = 146097;
        final long DAYS_0000_TO_1970 = 719528L;
        long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month - 362) / 12 + (dom - 1);
        if (month > 2) {
            --total;
            final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
            if (!leapYear) {
                --total;
            }
        }
        final long epochDay = total - 719528L;
        final long utcSeconds = epochDay * 86400L + hour * 3600 + minute * 60 + second;
        final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
        final long SECONDS_1991_09_15_02 = 684900000L;
        int zoneOffsetTotalSeconds;
        if (shanghai && utcSeconds >= SECONDS_1991_09_15_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (zoneId == ZoneOffset.UTC || "UTC".equals(zoneId.getId())) {
            zoneOffsetTotalSeconds = 0;
        }
        else {
            final LocalDate localDate = LocalDate.of(year, month, dom);
            final LocalTime localTime = LocalTime.of(hour, minute, second, nanoOfSecond);
            final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
            final ZoneOffset offset = zoneId.getRules().getOffset(ldt);
            zoneOffsetTotalSeconds = offset.getTotalSeconds();
        }
        long millis = (utcSeconds - zoneOffsetTotalSeconds) * 1000L;
        if (nanoOfSecond != 0) {
            millis += nanoOfSecond / 1000000;
        }
        return millis;
    }
    
    public static long utcSeconds(final int year, final int month, final int dom, final int hour, final int minute, final int second) {
        final int DAYS_PER_CYCLE = 146097;
        final long DAYS_0000_TO_1970 = 719528L;
        long total = 365 * year + ((year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400) + (367 * month - 362) / 12 + (dom - 1);
        if (month > 2) {
            --total;
            final boolean leapYear = (year & 0x3) == 0x0 && (year % 100 != 0 || year % 400 == 0);
            if (!leapYear) {
                --total;
            }
        }
        final long epochDay = total - 719528L;
        return epochDay * 86400L + hour * 3600 + minute * 60 + second;
    }
    
    public static String formatYMDHMS19(final Date date) {
        return formatYMDHMS19(date, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static String formatYMDHMS19(final Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        final long timeMillis = date.getTime();
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        final long SECONDS_1991_09_15_02 = 684900000L;
        final boolean shanghai = zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES;
        int offsetTotalSeconds;
        if (shanghai && epochSecond > 684900000L) {
            offsetTotalSeconds = 28800;
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
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        final int h0 = hour / 10 + 48;
        final int h2 = hour % 10 + 48;
        final int i0 = minute / 10 + 48;
        final int i2 = minute % 10 + 48;
        final int s0 = second / 10 + 48;
        final int s2 = second % 10 + 48;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = { (byte)y0, (byte)y2, (byte)y3, (byte)y4, 45, (byte)m0, (byte)m2, 45, (byte)d0, (byte)d2, 32, (byte)h0, (byte)h2, 58, (byte)i0, (byte)i2, 58, (byte)s0, (byte)s2 };
            return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        final char[] chars = { (char)y0, (char)y2, (char)y3, (char)y4, '-', (char)m0, (char)m2, '-', (char)d0, (char)d2, ' ', (char)h0, (char)h2, ':', (char)i0, (char)i2, ':', (char)s0, (char)s2 };
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
        }
        return new String(chars);
    }
    
    public static String formatYMD8(final Date date) {
        if (date == null) {
            return null;
        }
        return formatYMD8(date.getTime(), DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static String formatYMD8(final long timeMillis, ZoneId zoneId) {
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(epochSecond);
        }
        else {
            final Instant instant = Instant.ofEpochMilli(timeMillis);
            offsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
        }
        final long localSecond = epochSecond + offsetTotalSeconds;
        final long localEpochDay = Math.floorDiv(localSecond, 86400L);
        final int off = (int)(localEpochDay - DateUtils.LOCAL_EPOCH_DAY + 128L);
        final String[] cache = CacheDate8.CACHE;
        if (off >= 0 && off < cache.length) {
            final String str = cache[off];
            if (str != null) {
                return str;
            }
        }
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
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        String str2;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = { (byte)y0, (byte)y2, (byte)y3, (byte)y4, (byte)m0, (byte)m2, (byte)d0, (byte)d2 };
            str2 = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            final char[] chars = { (char)y0, (char)y2, (char)y3, (char)y4, (char)m0, (char)m2, (char)d0, (char)d2 };
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str2 = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
            }
            else {
                str2 = new String(chars);
            }
        }
        if (off >= 0 && off < cache.length) {
            cache[off] = str2;
        }
        return str2;
    }
    
    public static String formatYMD10(final LocalDate date) {
        if (date == null) {
            return null;
        }
        final int year = date.getYear();
        final int month = date.getMonthValue();
        final int dayOfMonth = date.getDayOfMonth();
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        final char separator = '-';
        String str;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = { (byte)y0, (byte)y2, (byte)y3, (byte)y4, 45, (byte)m0, (byte)m2, 45, (byte)d0, (byte)d2 };
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            final char[] chars = { (char)y0, (char)y2, (char)y3, (char)y4, '-', (char)m0, (char)m2, '-', (char)d0, (char)d2 };
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
            }
            else {
                str = new String(chars);
            }
        }
        return str;
    }
    
    public static String formatYMD10(final Date date) {
        if (date == null) {
            return null;
        }
        return formatYMD10(date.getTime(), DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static String formatYMD8(final LocalDate date) {
        if (date == null) {
            return null;
        }
        final int year = date.getYear();
        final int month = date.getMonthValue();
        final int dayOfMonth = date.getDayOfMonth();
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        String str;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = { (byte)y0, (byte)y2, (byte)y3, (byte)y4, (byte)m0, (byte)m2, (byte)d0, (byte)d2 };
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            final char[] chars = { (char)y0, (char)y2, (char)y3, (char)y4, (char)m0, (char)m2, (char)d0, (char)d2 };
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
            }
            else {
                str = new String(chars);
            }
        }
        return str;
    }
    
    public static String formatYMD10(final long timeMillis, ZoneId zoneId) {
        if (zoneId == null) {
            zoneId = DateUtils.DEFAULT_ZONE_ID;
        }
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(epochSecond);
        }
        else {
            final Instant instant = Instant.ofEpochMilli(timeMillis);
            offsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
        }
        final long localSecond = epochSecond + offsetTotalSeconds;
        final long localEpochDay = Math.floorDiv(localSecond, 86400L);
        final int off = (int)(localEpochDay - DateUtils.LOCAL_EPOCH_DAY + 128L);
        final String[] cache = CacheDate10.CACHE;
        if (off >= 0 && off < cache.length) {
            final String str = cache[off];
            if (str != null) {
                return str;
            }
        }
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
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        final char separator = '-';
        String str2;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = { (byte)y0, (byte)y2, (byte)y3, (byte)y4, 45, (byte)m0, (byte)m2, 45, (byte)d0, (byte)d2 };
            str2 = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            final char[] chars = { (char)y0, (char)y2, (char)y3, (char)y4, '-', (char)m0, (char)m2, '-', (char)d0, (char)d2 };
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str2 = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
            }
            else {
                str2 = new String(chars);
            }
        }
        if (off >= 0 && off < cache.length) {
            cache[off] = str2;
        }
        return str2;
    }
    
    public static String format(final Date date, final String format) {
        if (date == null) {
            return null;
        }
        if (format == null) {
            return format(date);
        }
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss": {
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            }
            case "yyyy-MM-ddTHH:mm:ss":
            case "yyyy-MM-dd'T'HH:mm:ss": {
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            }
            case "dd.MM.yyyy HH:mm:ss": {
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT);
            }
            case "yyyyMMdd": {
                return formatYMD8(date.getTime(), DateUtils.DEFAULT_ZONE_ID);
            }
            case "yyyy-MM-dd": {
                return formatYMD10(date.getTime(), DateUtils.DEFAULT_ZONE_ID);
            }
            case "yyyy/MM/dd": {
                return format(date.getTime(), DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            }
            case "dd.MM.yyyy": {
                return format(date.getTime(), DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            }
            default: {
                final long epochMilli = date.getTime();
                final Instant instant = Instant.ofEpochMilli(epochMilli);
                final ZonedDateTime zdt = instant.atZone(DateUtils.DEFAULT_ZONE_ID);
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return formatter.format(zdt);
            }
        }
    }
    
    public static String formatYMDHMS19(final ZonedDateTime zdt) {
        if (zdt == null) {
            return null;
        }
        final int year = zdt.getYear();
        final int month = zdt.getMonthValue();
        final int dayOfMonth = zdt.getDayOfMonth();
        final int hour = zdt.getHour();
        final int minute = zdt.getMinute();
        final int second = zdt.getSecond();
        return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    public static String format(final ZonedDateTime zdt, final String format) {
        if (zdt == null) {
            return null;
        }
        final int year = zdt.getYear();
        final int month = zdt.getMonthValue();
        final int dayOfMonth = zdt.getDayOfMonth();
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss": {
                final int hour = zdt.getHour();
                final int minute = zdt.getMinute();
                final int second = zdt.getSecond();
                return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            }
            case "yyyy-MM-ddTHH:mm:ss":
            case "yyyy-MM-dd'T'HH:mm:ss": {
                final int hour = zdt.getHour();
                final int minute = zdt.getMinute();
                final int second = zdt.getSecond();
                return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            }
            case "yyyy-MM-dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
            }
            case "yyyy/MM/dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            }
            case "dd.MM.yyyy": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            }
            default: {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return formatter.format(zdt);
            }
        }
    }
    
    public static String formatYMDHMS19(final LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        final int year = ldt.getYear();
        final int month = ldt.getMonthValue();
        final int dayOfMonth = ldt.getDayOfMonth();
        final int hour = ldt.getHour();
        final int minute = ldt.getMinute();
        final int second = ldt.getSecond();
        return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    public static String format(final LocalDateTime ldt, final String format) {
        if (ldt == null) {
            return null;
        }
        final int year = ldt.getYear();
        final int month = ldt.getMonthValue();
        final int dayOfMonth = ldt.getDayOfMonth();
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss": {
                final int hour = ldt.getHour();
                final int minute = ldt.getMinute();
                final int second = ldt.getSecond();
                return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            }
            case "yyyy-MM-ddTHH:mm:ss":
            case "yyyy-MM-dd'T'HH:mm:ss": {
                final int hour = ldt.getHour();
                final int minute = ldt.getMinute();
                final int second = ldt.getSecond();
                return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            }
            case "yyyy-MM-dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
            }
            case "yyyy/MM/dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            }
            case "dd.MM.yyyy": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            }
            default: {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return formatter.format(ldt);
            }
        }
    }
    
    public static String formatYMDHMS19(final LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        final int year = localDate.getYear();
        final int month = localDate.getMonthValue();
        final int dayOfMonth = localDate.getDayOfMonth();
        return format(year, month, dayOfMonth, 0, 0, 0, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    public static String format(final LocalDate localDate, final String format) {
        if (localDate == null) {
            return null;
        }
        final int year = localDate.getYear();
        final int month = localDate.getMonthValue();
        final int dayOfMonth = localDate.getDayOfMonth();
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss": {
                return format(year, month, dayOfMonth, 0, 0, 0, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            }
            case "yyyy-MM-ddTHH:mm:ss":
            case "yyyy-MM-dd'T'HH:mm:ss": {
                return format(year, month, dayOfMonth, 0, 0, 0, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            }
            case "yyyy-MM-dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
            }
            case "yyyy/MM/dd": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            }
            case "dd.MM.yyyy": {
                return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            }
            default: {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return formatter.format(localDate);
            }
        }
    }
    
    public static String format(final int year, final int month, final int dayOfMonth) {
        return format(year, month, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
    }
    
    public static String format(final int year, final int month, final int dayOfMonth, final DateTimeFormatPattern pattern) {
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = new byte[10];
            if (pattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
                bytes[0] = (byte)d0;
                bytes[1] = (byte)d2;
                bytes[2] = 46;
                bytes[3] = (byte)m0;
                bytes[4] = (byte)m2;
                bytes[5] = 46;
                bytes[6] = (byte)y0;
                bytes[7] = (byte)y2;
                bytes[8] = (byte)y3;
                bytes[9] = (byte)y4;
            }
            else {
                final byte separator = (byte)((pattern == DateTimeFormatPattern.DATE_FORMAT_10_DASH) ? 45 : 47);
                bytes[0] = (byte)y0;
                bytes[1] = (byte)y2;
                bytes[2] = (byte)y3;
                bytes[3] = (byte)y4;
                bytes[4] = separator;
                bytes[5] = (byte)m0;
                bytes[6] = (byte)m2;
                bytes[7] = separator;
                bytes[8] = (byte)d0;
                bytes[9] = (byte)d2;
            }
            return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        final char[] chars = new char[10];
        if (pattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
            chars[0] = (char)d0;
            chars[1] = (char)d2;
            chars[2] = '.';
            chars[3] = (char)m0;
            chars[4] = (char)m2;
            chars[5] = '.';
            chars[6] = (char)y0;
            chars[7] = (char)y2;
            chars[8] = (char)y3;
            chars[9] = (char)y4;
        }
        else {
            final char separator2 = (pattern == DateTimeFormatPattern.DATE_FORMAT_10_DASH) ? '-' : '/';
            chars[0] = (char)y0;
            chars[1] = (char)y2;
            chars[2] = (char)y3;
            chars[3] = (char)y4;
            chars[4] = separator2;
            chars[5] = (char)m0;
            chars[6] = (char)m2;
            chars[7] = separator2;
            chars[8] = (char)d0;
            chars[9] = (char)d2;
        }
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
        }
        return new String(chars);
    }
    
    public static String format(final long timeMillis) {
        return format(timeMillis, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    public static String format(final Date date) {
        if (date == null) {
            return null;
        }
        return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    public static String format(final long timeMillis, final DateTimeFormatPattern pattern) {
        final ZoneId zoneId = DateUtils.DEFAULT_ZONE_ID;
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(epochSecond);
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
        if (pattern == DateTimeFormatPattern.DATE_FORMAT_10_DASH || pattern == DateTimeFormatPattern.DATE_FORMAT_10_SLASH || pattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
            return format(year, month, dayOfMonth, pattern);
        }
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
        return format(year, month, dayOfMonth, hour, minute, second, pattern);
    }
    
    public static String format(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        return format(year, month, dayOfMonth, hour, minute, second, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }
    
    static String format(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second, final DateTimeFormatPattern pattern) {
        final int y0 = year / 1000 + 48;
        final int y2 = year / 100 % 10 + 48;
        final int y3 = year / 10 % 10 + 48;
        final int y4 = year % 10 + 48;
        final int m0 = month / 10 + 48;
        final int m2 = month % 10 + 48;
        final int d0 = dayOfMonth / 10 + 48;
        final int d2 = dayOfMonth % 10 + 48;
        final int h0 = hour / 10 + 48;
        final int h2 = hour % 10 + 48;
        final int i0 = minute / 10 + 48;
        final int i2 = minute % 10 + 48;
        final int s0 = second / 10 + 48;
        final int s2 = second % 10 + 48;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            final byte[] bytes = new byte[19];
            if (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT) {
                bytes[0] = (byte)d0;
                bytes[1] = (byte)d2;
                bytes[2] = 46;
                bytes[3] = (byte)m0;
                bytes[4] = (byte)m2;
                bytes[5] = 46;
                bytes[6] = (byte)y0;
                bytes[7] = (byte)y2;
                bytes[8] = (byte)y3;
                bytes[9] = (byte)y4;
                bytes[10] = 32;
            }
            else {
                final char separator = (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH) ? ' ' : 'T';
                final char dateSeparator = (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH) ? '/' : '-';
                bytes[0] = (byte)y0;
                bytes[1] = (byte)y2;
                bytes[2] = (byte)y3;
                bytes[3] = (byte)y4;
                bytes[4] = (byte)dateSeparator;
                bytes[5] = (byte)m0;
                bytes[6] = (byte)m2;
                bytes[7] = (byte)dateSeparator;
                bytes[8] = (byte)d0;
                bytes[9] = (byte)d2;
                bytes[10] = (byte)separator;
            }
            bytes[11] = (byte)h0;
            bytes[12] = (byte)h2;
            bytes[13] = 58;
            bytes[14] = (byte)i0;
            bytes[15] = (byte)i2;
            bytes[16] = 58;
            bytes[17] = (byte)s0;
            bytes[18] = (byte)s2;
            return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        final char[] chars = new char[19];
        if (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT) {
            chars[0] = (char)d0;
            chars[1] = (char)d2;
            chars[2] = '.';
            chars[3] = (char)m0;
            chars[4] = (char)m2;
            chars[5] = '.';
            chars[6] = (char)y0;
            chars[7] = (char)y2;
            chars[8] = (char)y3;
            chars[9] = (char)y4;
            chars[10] = ' ';
        }
        else {
            final char separator = (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH) ? ' ' : 'T';
            final char dateSeparator = (pattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH) ? '/' : '-';
            chars[0] = (char)y0;
            chars[1] = (char)y2;
            chars[2] = (char)y3;
            chars[3] = (char)y4;
            chars[4] = dateSeparator;
            chars[5] = (char)m0;
            chars[6] = (char)m2;
            chars[7] = dateSeparator;
            chars[8] = (char)d0;
            chars[9] = (char)d2;
            chars[10] = separator;
        }
        chars[11] = (char)h0;
        chars[12] = (char)h2;
        chars[13] = ':';
        chars[14] = (char)i0;
        chars[15] = (char)i2;
        chars[16] = ':';
        chars[17] = (char)s0;
        chars[18] = (char)s2;
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
        }
        return new String(chars);
    }
    
    public static String toString(final Date date) {
        return toString(date.getTime(), false, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static String toString(final long timeMillis, final boolean timeZone, final ZoneId zoneId) {
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(epochSecond);
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
        final int millis = (int)Math.floorMod(timeMillis, 1000L);
        int millislen;
        if (millis == 0) {
            millislen = 0;
        }
        else if (millis < 10) {
            millislen = 4;
        }
        else if (millis % 100 == 0) {
            millislen = 2;
        }
        else if (millis % 10 == 0) {
            millislen = 3;
        }
        else {
            millislen = 4;
        }
        int zonelen;
        if (timeZone) {
            zonelen = ((offsetTotalSeconds == 0) ? 1 : 6);
        }
        else {
            zonelen = 0;
        }
        final int len = 19 + millislen + zonelen;
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            final char[] chars = new char[len];
            IOUtils.writeLocalDate(chars, 0, year, month, dayOfMonth);
            chars[10] = ' ';
            IOUtils.writeLocalTime(chars, 11, hour, minute, second);
            if (millislen > 0) {
                chars[19] = '.';
                for (int i = 20; i < len; ++i) {
                    chars[i] = '0';
                }
                if (millis < 10) {
                    IOUtils.getChars(millis, 19 + millislen, chars);
                }
                else if (millis % 100 == 0) {
                    IOUtils.getChars(millis / 100, 19 + millislen, chars);
                }
                else if (millis % 10 == 0) {
                    IOUtils.getChars(millis / 10, 19 + millislen, chars);
                }
                else {
                    IOUtils.getChars(millis, 19 + millislen, chars);
                }
            }
            if (timeZone) {
                final int timeZoneOffset = offsetTotalSeconds / 3600;
                if (offsetTotalSeconds == 0) {
                    chars[19 + millislen] = 'Z';
                }
                else {
                    final int offsetAbs = Math.abs(timeZoneOffset);
                    if (timeZoneOffset >= 0) {
                        chars[19 + millislen] = '+';
                    }
                    else {
                        chars[19 + millislen] = '-';
                    }
                    chars[19 + millislen + 1] = '0';
                    IOUtils.getChars(offsetAbs, 19 + millislen + 3, chars);
                    chars[19 + millislen + 3] = ':';
                    chars[19 + millislen + 4] = '0';
                    int offsetMinutes = (offsetTotalSeconds - timeZoneOffset * 3600) / 60;
                    if (offsetMinutes < 0) {
                        offsetMinutes = -offsetMinutes;
                    }
                    IOUtils.getChars(offsetMinutes, 19 + millislen + zonelen, chars);
                }
            }
            return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
        }
        final byte[] bytes = new byte[len];
        IOUtils.writeLocalDate(bytes, 0, year, month, dayOfMonth);
        bytes[10] = 32;
        IOUtils.writeLocalTime(bytes, 11, hour, minute, second);
        if (millislen > 0) {
            bytes[19] = 46;
            for (int i = 20; i < len; ++i) {
                bytes[i] = 48;
            }
            if (millis < 10) {
                IOUtils.getChars(millis, 19 + millislen, bytes);
            }
            else if (millis % 100 == 0) {
                IOUtils.getChars(millis / 100, 19 + millislen, bytes);
            }
            else if (millis % 10 == 0) {
                IOUtils.getChars(millis / 10, 19 + millislen, bytes);
            }
            else {
                IOUtils.getChars(millis, 19 + millislen, bytes);
            }
        }
        if (timeZone) {
            final int timeZoneOffset = offsetTotalSeconds / 3600;
            if (offsetTotalSeconds == 0) {
                bytes[19 + millislen] = 90;
            }
            else {
                final int offsetAbs = Math.abs(timeZoneOffset);
                if (timeZoneOffset >= 0) {
                    bytes[19 + millislen] = 43;
                }
                else {
                    bytes[19 + millislen] = 45;
                }
                bytes[19 + millislen + 1] = 48;
                IOUtils.getChars(offsetAbs, 19 + millislen + 3, bytes);
                bytes[19 + millislen + 3] = 58;
                bytes[19 + millislen + 4] = 48;
                int offsetMinutes = (offsetTotalSeconds - timeZoneOffset * 3600) / 60;
                if (offsetMinutes < 0) {
                    offsetMinutes = -offsetMinutes;
                }
                IOUtils.getChars(offsetMinutes, 19 + millislen + zonelen, bytes);
            }
        }
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        return new String(bytes, 0, bytes.length, StandardCharsets.ISO_8859_1);
    }
    
    public static int month(final char c0, final char c1, final char c2) {
        switch (c0) {
            case 'J': {
                if (c1 == 'a' && c2 == 'n') {
                    return 1;
                }
                if (c1 != 'u') {
                    break;
                }
                if (c2 == 'n') {
                    return 6;
                }
                if (c2 == 'l') {
                    return 7;
                }
                break;
            }
            case 'F': {
                if (c1 == 'e' && c2 == 'b') {
                    return 2;
                }
                break;
            }
            case 'M': {
                if (c1 != 'a') {
                    break;
                }
                if (c2 == 'r') {
                    return 3;
                }
                if (c2 == 'y') {
                    return 5;
                }
                break;
            }
            case 'A': {
                if (c1 == 'p' && c2 == 'r') {
                    return 4;
                }
                if (c1 == 'u' && c2 == 'g') {
                    return 8;
                }
                break;
            }
            case 'S': {
                if (c1 == 'e' && c2 == 'p') {
                    return 9;
                }
                break;
            }
            case 'O': {
                if (c1 == 'c' && c2 == 't') {
                    return 10;
                }
                break;
            }
            case 'N': {
                if (c1 == 'o' && c2 == 'v') {
                    return 11;
                }
                break;
            }
            case 'D': {
                if (c1 == 'e' && c2 == 'c') {
                    return 12;
                }
                break;
            }
        }
        return 0;
    }
    
    public static int hourAfterNoon(char h0, char h1) {
        if (h0 == '0') {
            switch (h1) {
                case '0': {
                    h0 = '1';
                    h1 = '2';
                    break;
                }
                case '1': {
                    h0 = '1';
                    h1 = '3';
                    break;
                }
                case '2': {
                    h0 = '1';
                    h1 = '4';
                    break;
                }
                case '3': {
                    h0 = '1';
                    h1 = '5';
                    break;
                }
                case '4': {
                    h0 = '1';
                    h1 = '6';
                    break;
                }
                case '5': {
                    h0 = '1';
                    h1 = '7';
                    break;
                }
                case '6': {
                    h0 = '1';
                    h1 = '8';
                    break;
                }
                case '7': {
                    h0 = '1';
                    h1 = '9';
                    break;
                }
                case '8': {
                    h0 = '2';
                    h1 = '0';
                    break;
                }
                case '9': {
                    h0 = '2';
                    h1 = '1';
                    break;
                }
            }
        }
        else if (h0 == '1') {
            switch (h1) {
                case '0': {
                    h0 = '2';
                    h1 = '2';
                    break;
                }
                case '1': {
                    h0 = '2';
                    h1 = '3';
                    break;
                }
                case '2': {
                    h0 = '2';
                    h1 = '4';
                    break;
                }
            }
        }
        return h0 << 16 | h1;
    }
    
    public static int getShanghaiZoneOffsetTotalSeconds(final long seconds) {
        final long SECONDS_1991_09_15_02 = 684900000L;
        final long SECONDS_1991_04_14_03 = 671598000L;
        final long SECONDS_1990_09_16_02 = 653450400L;
        final long SECONDS_1990_04_15_03 = 640148400L;
        final long SECONDS_1989_09_17_02 = 622000800L;
        final long SECONDS_1989_04_16_03 = 608698800L;
        final long SECONDS_1988_09_11_02 = 589946400L;
        final long SECONDS_1988_04_17_03 = 577249200L;
        final long SECONDS_1987_09_13_02 = 558496800L;
        final long SECONDS_1987_04_12_03 = 545194800L;
        final long SECONDS_1986_09_14_02 = 527047200L;
        final long SECONDS_1986_05_04_03 = 515559600L;
        final long SECONDS_1949_05_28_00 = -649987200L;
        final long SECONDS_1949_05_01_01 = -652316400L;
        final long SECONDS_1948_10_01_00 = -670636800L;
        final long SECONDS_1948_05_01_01 = -683852400L;
        final long SECONDS_1947_11_01_00 = -699580800L;
        final long SECONDS_1947_04_15_01 = -716857200L;
        final long SECONDS_1946_10_01_00 = -733795200L;
        final long SECONDS_1946_05_15_01 = -745801200L;
        final long SECONDS_1945_09_02_00 = -767836800L;
        final long SECONDS_1942_01_31_01 = -881017200L;
        final long SECONDS_1941_11_02_00 = -888796800L;
        final long SECONDS_1941_03_15_01 = -908838000L;
        final long SECONDS_1940_10_13_00 = -922060800L;
        final long SECONDS_1940_06_01_01 = -933634800L;
        final long SECONDS_1919_10_01_00 = -1585872000L;
        final long SECONDS_1919_04_13_01 = -1600642800L;
        final long SECONDS_1901_01_01_00 = -2177452800L;
        final int OFFSET_0900_TOTAL_SECONDS = 32400;
        final int OFFSET_0800_TOTAL_SECONDS = 28800;
        final int OFFSET_0543_TOTAL_SECONDS = 29143;
        int zoneOffsetTotalSeconds;
        if (seconds >= SECONDS_1991_09_15_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1991_04_14_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1990_09_16_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1990_04_15_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1989_09_17_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1989_04_16_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1988_09_11_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1988_04_17_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1987_09_13_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1987_04_12_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1986_09_14_02) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1986_05_04_03) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1949_05_28_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1949_05_01_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1948_10_01_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1948_05_01_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1947_11_01_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1947_04_15_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1946_10_01_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1946_05_15_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1945_09_02_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1942_01_31_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1941_11_02_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1941_03_15_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1940_10_13_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1940_06_01_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1919_10_01_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else if (seconds >= SECONDS_1919_04_13_01) {
            zoneOffsetTotalSeconds = 32400;
        }
        else if (seconds >= SECONDS_1901_01_01_00) {
            zoneOffsetTotalSeconds = 28800;
        }
        else {
            zoneOffsetTotalSeconds = 29143;
        }
        return zoneOffsetTotalSeconds;
    }
    
    public static boolean isLocalDate(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.length() == 10 && str.charAt(4) == '-' && str.charAt(7) == '-') {
            final char y0 = str.charAt(0);
            final char y2 = str.charAt(1);
            final char y3 = str.charAt(2);
            final char y4 = str.charAt(3);
            final char m0 = str.charAt(5);
            final char m2 = str.charAt(6);
            final char d0 = str.charAt(8);
            final char d2 = str.charAt(9);
            final int yyyy = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
            final int mm = (m0 - '0') * 10 + (m2 - '0');
            final int dd = (d0 - '0') * 10 + (d2 - '0');
            if (mm > 12) {
                return false;
            }
            if (dd > 28) {
                int dom = 31;
                switch (mm) {
                    case 2: {
                        final boolean isLeapYear = (yyyy & 0x3) == 0x0 && (yyyy % 100 != 0 || yyyy % 400 == 0);
                        dom = (isLeapYear ? 29 : 28);
                        break;
                    }
                    case 4:
                    case 6:
                    case 9:
                    case 11: {
                        dom = 30;
                        break;
                    }
                }
                return dd <= dom;
            }
            return true;
        }
        else {
            if (str.length() < 9 || str.length() > 40) {
                return false;
            }
            try {
                return parseLocalDate(str) != null;
            }
            catch (DateTimeException | JSONException ex2) {
                final RuntimeException ex;
                final RuntimeException ignored = ex;
                return false;
            }
        }
    }
    
    public static boolean isDate(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        final char c10;
        if (str.length() == 19 && str.charAt(4) == '-' && str.charAt(7) == '-' && ((c10 = str.charAt(10)) == ' ' || c10 == 'T') && str.charAt(13) == ':' && str.charAt(16) == ':') {
            final char y0 = str.charAt(0);
            final char y2 = str.charAt(1);
            final char y3 = str.charAt(2);
            final char y4 = str.charAt(3);
            final char m0 = str.charAt(5);
            final char m2 = str.charAt(6);
            final char d0 = str.charAt(8);
            final char d2 = str.charAt(9);
            final char h0 = str.charAt(11);
            final char h2 = str.charAt(12);
            final char i0 = str.charAt(14);
            final char i2 = str.charAt(15);
            final char s0 = str.charAt(17);
            final char s2 = str.charAt(18);
            if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9' || m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9' || d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9' || h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9' || i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9' || s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
                return false;
            }
            final int yyyy = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
            final int mm = (m0 - '0') * 10 + (m2 - '0');
            final int dd = (d0 - '0') * 10 + (d2 - '0');
            final int hh = (h0 - '0') * 10 + (h2 - '0');
            final int ii = (i0 - '0') * 10 + (i2 - '0');
            final int ss = (s0 - '0') * 10 + (s2 - '0');
            if (mm > 12) {
                return false;
            }
            if (dd > 28) {
                int dom = 31;
                switch (mm) {
                    case 2: {
                        final boolean isLeapYear = (yyyy & 0x3) == 0x0 && (yyyy % 100 != 0 || yyyy % 400 == 0);
                        dom = (isLeapYear ? 29 : 28);
                        break;
                    }
                    case 4:
                    case 6:
                    case 9:
                    case 11: {
                        dom = 30;
                        break;
                    }
                }
                if (dd > dom) {
                    return false;
                }
            }
            return hh <= 24 && ii <= 60 && ss <= 61;
        }
        else {
            try {
                return parseMillis(str, DateUtils.DEFAULT_ZONE_ID) != 0L;
            }
            catch (DateTimeException | JSONException ex2) {
                final RuntimeException ex;
                final RuntimeException ignored = ex;
                return false;
            }
        }
    }
    
    public static boolean isLocalTime(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char h0;
        char h2;
        char m0;
        char m2;
        char s0;
        char s2;
        if (str.length() == 8 && str.charAt(2) == ':' && str.charAt(5) == ':') {
            h0 = str.charAt(0);
            h2 = str.charAt(1);
            m0 = str.charAt(3);
            m2 = str.charAt(4);
            s0 = str.charAt(6);
            s2 = str.charAt(7);
        }
        else {
            try {
                LocalTime.parse(str);
                return true;
            }
            catch (DateTimeParseException ignored) {
                return false;
            }
        }
        if (h0 < '0' || h0 > '2' || h2 < '0' || h2 > '9' || m0 < '0' || m0 > '6' || m2 < '0' || m2 > '9' || s0 < '0' || s0 > '6' || s2 < '0' || s2 > '9') {
            return false;
        }
        final int hh = (h0 - '0') * 10 + (h2 - '0');
        if (hh > 24) {
            return false;
        }
        final int mm = (m0 - '0') * 10 + (m2 - '0');
        if (mm > 60) {
            return false;
        }
        final int ss = (s0 - '0') * 10 + (s2 - '0');
        return ss <= 61;
    }
    
    public static int readNanos(final char[] chars, final int len, final int offset) {
        switch (len) {
            case 1: {
                return 100000000 * (chars[offset] - '0');
            }
            case 2: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0');
            }
            case 3: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0');
            }
            case 4: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0');
            }
            case 5: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0') + 10000 * (chars[offset + 4] - '0');
            }
            case 6: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0') + 10000 * (chars[offset + 4] - '0') + 1000 * (chars[offset + 5] - '0');
            }
            case 7: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0') + 10000 * (chars[offset + 4] - '0') + 1000 * (chars[offset + 5] - '0') + 100 * (chars[offset + 6] - '0');
            }
            case 8: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0') + 10000 * (chars[offset + 4] - '0') + 1000 * (chars[offset + 5] - '0') + 100 * (chars[offset + 6] - '0') + 10 * (chars[offset + 7] - '0');
            }
            default: {
                return 100000000 * (chars[offset] - '0') + 10000000 * (chars[offset + 1] - '0') + 1000000 * (chars[offset + 2] - '0') + 100000 * (chars[offset + 3] - '0') + 10000 * (chars[offset + 4] - '0') + 1000 * (chars[offset + 5] - '0') + 100 * (chars[offset + 6] - '0') + 10 * (chars[offset + 7] - '0') + chars[offset + 8] - 48;
            }
        }
    }
    
    public static int readNanos(final byte[] bytes, final int len, final int offset) {
        switch (len) {
            case 1: {
                return 100000000 * (bytes[offset] - 48);
            }
            case 2: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48);
            }
            case 3: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48);
            }
            case 4: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48);
            }
            case 5: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48) + 10000 * (bytes[offset + 4] - 48);
            }
            case 6: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48) + 10000 * (bytes[offset + 4] - 48) + 1000 * (bytes[offset + 5] - 48);
            }
            case 7: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48) + 10000 * (bytes[offset + 4] - 48) + 1000 * (bytes[offset + 5] - 48) + 100 * (bytes[offset + 6] - 48);
            }
            case 8: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48) + 10000 * (bytes[offset + 4] - 48) + 1000 * (bytes[offset + 5] - 48) + 100 * (bytes[offset + 6] - 48) + 10 * (bytes[offset + 7] - 48);
            }
            default: {
                return 100000000 * (bytes[offset] - 48) + 10000000 * (bytes[offset + 1] - 48) + 1000000 * (bytes[offset + 2] - 48) + 100000 * (bytes[offset + 3] - 48) + 10000 * (bytes[offset + 4] - 48) + 1000 * (bytes[offset + 5] - 48) + 100 * (bytes[offset + 6] - 48) + 10 * (bytes[offset + 7] - 48) + bytes[offset + 8] - 48;
            }
        }
    }
    
    static {
        DEFAULT_ZONE_ID = ZoneId.systemDefault();
        SHANGHAI_ZONE_ID = ("Asia/Shanghai".equals(DateUtils.DEFAULT_ZONE_ID.getId()) ? DateUtils.DEFAULT_ZONE_ID : ZoneId.of("Asia/Shanghai"));
        SHANGHAI_ZONE_RULES = DateUtils.SHANGHAI_ZONE_ID.getRules();
        OFFSET_8_ZONE_ID = ZoneId.of("+08:00");
        LOCAL_DATE_19700101 = LocalDate.of(1970, 1, 1);
        final long timeMillis = System.currentTimeMillis();
        final ZoneId zoneId = DateUtils.DEFAULT_ZONE_ID;
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(timeMillis, 1000L);
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(epochSecond);
        }
        else {
            final Instant instant = Instant.ofEpochMilli(timeMillis);
            offsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
        }
        final long localSecond = epochSecond + offsetTotalSeconds;
        LOCAL_EPOCH_DAY = (int)Math.floorDiv(localSecond, 86400L);
    }
    
    static class CacheDate8
    {
        static final String[] CACHE;
        
        static {
            CACHE = new String[1024];
        }
    }
    
    static class CacheDate10
    {
        static final String[] CACHE;
        
        static {
            CACHE = new String[1024];
        }
    }
    
    public enum DateTimeFormatPattern
    {
        DATE_FORMAT_10_DASH("yyyy-MM-dd", 10), 
        DATE_FORMAT_10_SLASH("yyyy/MM/dd", 10), 
        DATE_FORMAT_10_DOT("dd.MM.yyyy", 10), 
        DATE_TIME_FORMAT_19_DASH("yyyy-MM-dd HH:mm:ss", 19), 
        DATE_TIME_FORMAT_19_DASH_T("yyyy-MM-dd'T'HH:mm:ss", 19), 
        DATE_TIME_FORMAT_19_SLASH("yyyy/MM/dd HH:mm:ss", 19), 
        DATE_TIME_FORMAT_19_DOT("dd.MM.yyyy HH:mm:ss", 19);
        
        public final String pattern;
        public final int length;
        
        private DateTimeFormatPattern(final String pattern, final int length) {
            this.pattern = pattern;
            this.length = length;
        }
        
        private static /* synthetic */ DateTimeFormatPattern[] $values() {
            return new DateTimeFormatPattern[] { DateTimeFormatPattern.DATE_FORMAT_10_DASH, DateTimeFormatPattern.DATE_FORMAT_10_SLASH, DateTimeFormatPattern.DATE_FORMAT_10_DOT, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T, DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
