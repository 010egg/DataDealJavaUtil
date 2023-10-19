// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import org.postgresql.util.ByteConverter;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Calendar;

public class TimestampUtils
{
    private static final int ONEDAY = 86400000;
    private StringBuffer sbuf;
    private Calendar defaultCal;
    private final TimeZone defaultTz;
    private Calendar calCache;
    private int calCacheZone;
    private final boolean min74;
    private final boolean min82;
    private final boolean usesDouble;
    
    TimestampUtils(final boolean min74, final boolean min82, final boolean usesDouble) {
        this.sbuf = new StringBuffer();
        this.defaultCal = new GregorianCalendar();
        this.defaultTz = this.defaultCal.getTimeZone();
        this.min74 = min74;
        this.min82 = min82;
        this.usesDouble = usesDouble;
    }
    
    private Calendar getCalendar(final int sign, final int hr, final int min, final int sec) {
        final int rawOffset = sign * (((hr * 60 + min) * 60 + sec) * 1000);
        if (this.calCache != null && this.calCacheZone == rawOffset) {
            return this.calCache;
        }
        final StringBuffer zoneID = new StringBuffer("GMT");
        zoneID.append((sign < 0) ? '-' : '+');
        if (hr < 10) {
            zoneID.append('0');
        }
        zoneID.append(hr);
        if (min < 10) {
            zoneID.append('0');
        }
        zoneID.append(min);
        if (sec < 10) {
            zoneID.append('0');
        }
        zoneID.append(sec);
        final TimeZone syntheticTZ = new SimpleTimeZone(rawOffset, zoneID.toString());
        this.calCache = new GregorianCalendar(syntheticTZ);
        this.calCacheZone = rawOffset;
        return this.calCache;
    }
    
    private ParsedTimestamp loadCalendar(final Calendar defaultTz, final String str, final String type) throws SQLException {
        final char[] s = str.toCharArray();
        final int slen = s.length;
        final ParsedTimestamp result = new ParsedTimestamp();
        try {
            int start = skipWhitespace(s, 0);
            int end = firstNonDigit(s, start);
            if (charAt(s, end) == '-') {
                result.hasDate = true;
                result.year = number(s, start, end);
                start = end + 1;
                end = firstNonDigit(s, start);
                result.month = number(s, start, end);
                final char sep = charAt(s, end);
                if (sep != '-') {
                    throw new NumberFormatException("Expected date to be dash-separated, got '" + sep + "'");
                }
                start = end + 1;
                end = firstNonDigit(s, start);
                result.day = number(s, start, end);
                start = skipWhitespace(s, end);
            }
            if (Character.isDigit(charAt(s, start))) {
                result.hasTime = true;
                end = firstNonDigit(s, start);
                result.hour = number(s, start, end);
                char sep = charAt(s, end);
                if (sep != ':') {
                    throw new NumberFormatException("Expected time to be colon-separated, got '" + sep + "'");
                }
                start = end + 1;
                end = firstNonDigit(s, start);
                result.minute = number(s, start, end);
                sep = charAt(s, end);
                if (sep != ':') {
                    throw new NumberFormatException("Expected time to be colon-separated, got '" + sep + "'");
                }
                start = end + 1;
                end = firstNonDigit(s, start);
                result.second = number(s, start, end);
                start = end;
                if (charAt(s, start) == '.') {
                    end = firstNonDigit(s, start + 1);
                    int num = number(s, start + 1, end);
                    for (int numlength = end - (start + 1); numlength < 9; ++numlength) {
                        num *= 10;
                    }
                    result.nanos = num;
                    start = end;
                }
                start = skipWhitespace(s, start);
            }
            char sep = charAt(s, start);
            if (sep == '-' || sep == '+') {
                final int tzsign = (sep == '-') ? -1 : 1;
                end = firstNonDigit(s, start + 1);
                final int tzhr = number(s, start + 1, end);
                start = end;
                sep = charAt(s, start);
                int tzmin;
                if (sep == ':') {
                    end = firstNonDigit(s, start + 1);
                    tzmin = number(s, start + 1, end);
                    start = end;
                }
                else {
                    tzmin = 0;
                }
                int tzsec = 0;
                if (this.min82) {
                    sep = charAt(s, start);
                    if (sep == ':') {
                        end = firstNonDigit(s, start + 1);
                        tzsec = number(s, start + 1, end);
                        start = end;
                    }
                }
                result.tz = this.getCalendar(tzsign, tzhr, tzmin, tzsec);
                start = skipWhitespace(s, start);
            }
            if (result.hasDate && start < slen) {
                final String eraString = new String(s, start, slen - start);
                if (eraString.startsWith("AD")) {
                    result.era = 1;
                    start += 2;
                }
                else if (eraString.startsWith("BC")) {
                    result.era = 0;
                    start += 2;
                }
            }
            if (start < slen) {
                throw new NumberFormatException("Trailing junk on timestamp: '" + new String(s, start, slen - start) + "'");
            }
            if (!result.hasTime && !result.hasDate) {
                throw new NumberFormatException("Timestamp has neither date nor time");
            }
        }
        catch (NumberFormatException nfe) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { type, str }), PSQLState.BAD_DATETIME_FORMAT, nfe);
        }
        return result;
    }
    
    private static void showParse(final String type, final String what, final Calendar cal, final Date result, final Calendar resultCal) {
    }
    
    private static void showString(final String type, final Calendar cal, final Date value, final String result) {
    }
    
    public synchronized Calendar toCalendar(Calendar cal, final String s) throws SQLException {
        if (s == null) {
            return null;
        }
        if (cal == null) {
            cal = this.defaultCal;
        }
        final int slen = s.length();
        if (slen == 8 && s.equals("infinity")) {
            cal.setTime(new Timestamp(9223372036825200000L));
            return cal;
        }
        if (slen == 9 && s.equals("-infinity")) {
            cal.setTime(new Timestamp(-9223372036832400000L));
            return cal;
        }
        final ParsedTimestamp ts = this.loadCalendar(cal, s, "timestamp");
        final Calendar useCal = (ts.tz == null) ? cal : ts.tz;
        useCal.set(0, ts.era);
        useCal.set(1, ts.year);
        useCal.set(2, ts.month - 1);
        useCal.set(5, ts.day);
        useCal.set(11, ts.hour);
        useCal.set(12, ts.minute);
        useCal.set(13, ts.second);
        useCal.set(14, 0);
        showParse("calendar", s, cal, null, useCal);
        return useCal;
    }
    
    public synchronized Timestamp toTimestamp(Calendar cal, final String s) throws SQLException {
        if (s == null) {
            return null;
        }
        final int slen = s.length();
        if (slen == 8 && s.equals("infinity")) {
            return new Timestamp(9223372036825200000L);
        }
        if (slen == 9 && s.equals("-infinity")) {
            return new Timestamp(-9223372036832400000L);
        }
        if (cal == null) {
            cal = this.defaultCal;
        }
        final ParsedTimestamp ts = this.loadCalendar(cal, s, "timestamp");
        final Calendar useCal = (ts.tz == null) ? cal : ts.tz;
        useCal.set(0, ts.era);
        useCal.set(1, ts.year);
        useCal.set(2, ts.month - 1);
        useCal.set(5, ts.day);
        useCal.set(11, ts.hour);
        useCal.set(12, ts.minute);
        useCal.set(13, ts.second);
        useCal.set(14, 0);
        final Timestamp result = new Timestamp(useCal.getTime().getTime());
        result.setNanos(ts.nanos);
        showParse("timestamp", s, cal, result, useCal);
        return result;
    }
    
    public synchronized Time toTime(Calendar cal, final String s) throws SQLException {
        if (s == null) {
            return null;
        }
        final int slen = s.length();
        if ((slen == 8 && s.equals("infinity")) || (slen == 9 && s.equals("-infinity"))) {
            throw new PSQLException(GT.tr("Infinite value found for timestamp/date. This cannot be represented as time."), PSQLState.DATETIME_OVERFLOW);
        }
        if (cal == null) {
            cal = this.defaultCal;
        }
        final ParsedTimestamp ts = this.loadCalendar(cal, s, "time");
        Calendar useCal = (ts.tz == null) ? cal : ts.tz;
        useCal.set(11, ts.hour);
        useCal.set(12, ts.minute);
        useCal.set(13, ts.second);
        useCal.set(14, (ts.nanos + 500000) / 1000000);
        if (ts.hasDate) {
            useCal.set(0, ts.era);
            useCal.set(1, ts.year);
            useCal.set(2, ts.month - 1);
            useCal.set(5, ts.day);
            cal.setTime(new java.sql.Date(useCal.getTime().getTime()));
            useCal = cal;
        }
        useCal.set(0, 1);
        useCal.set(1, 1970);
        useCal.set(2, 0);
        useCal.set(5, 1);
        final Time result = new Time(useCal.getTime().getTime());
        showParse("time", s, cal, result, useCal);
        return result;
    }
    
    public synchronized java.sql.Date toDate(Calendar cal, final String s) throws SQLException {
        if (s == null) {
            return null;
        }
        final int slen = s.length();
        if (slen == 8 && s.equals("infinity")) {
            return new java.sql.Date(9223372036825200000L);
        }
        if (slen == 9 && s.equals("-infinity")) {
            return new java.sql.Date(-9223372036832400000L);
        }
        if (cal == null) {
            cal = this.defaultCal;
        }
        final ParsedTimestamp ts = this.loadCalendar(cal, s, "date");
        Calendar useCal = (ts.tz == null) ? cal : ts.tz;
        useCal.set(0, ts.era);
        useCal.set(1, ts.year);
        useCal.set(2, ts.month - 1);
        useCal.set(5, ts.day);
        if (ts.hasTime) {
            useCal.set(11, ts.hour);
            useCal.set(12, ts.minute);
            useCal.set(13, ts.second);
            useCal.set(14, (ts.nanos + 500000) / 1000000);
            cal.setTime(new java.sql.Date(useCal.getTime().getTime()));
            useCal = cal;
        }
        useCal.set(11, 0);
        useCal.set(12, 0);
        useCal.set(13, 0);
        useCal.set(14, 0);
        final java.sql.Date result = new java.sql.Date(useCal.getTime().getTime());
        showParse("date", s, cal, result, useCal);
        return result;
    }
    
    public synchronized String toString(Calendar cal, final Timestamp x) {
        if (cal == null) {
            cal = this.defaultCal;
        }
        cal.setTime(x);
        this.sbuf.setLength(0);
        if (x.getTime() == 9223372036825200000L) {
            this.sbuf.append("infinity");
        }
        else if (x.getTime() == -9223372036832400000L) {
            this.sbuf.append("-infinity");
        }
        else {
            appendDate(this.sbuf, cal);
            this.sbuf.append(' ');
            appendTime(this.sbuf, cal, x.getNanos());
            this.appendTimeZone(this.sbuf, cal);
            appendEra(this.sbuf, cal);
        }
        showString("timestamp", cal, x, this.sbuf.toString());
        return this.sbuf.toString();
    }
    
    public synchronized String toString(Calendar cal, final java.sql.Date x) {
        if (cal == null) {
            cal = this.defaultCal;
        }
        cal.setTime(x);
        this.sbuf.setLength(0);
        if (x.getTime() == 9223372036825200000L) {
            this.sbuf.append("infinity");
        }
        else if (x.getTime() == -9223372036832400000L) {
            this.sbuf.append("-infinity");
        }
        else {
            appendDate(this.sbuf, cal);
            appendEra(this.sbuf, cal);
            this.appendTimeZone(this.sbuf, cal);
        }
        showString("date", cal, x, this.sbuf.toString());
        return this.sbuf.toString();
    }
    
    public synchronized String toString(Calendar cal, final Time x) {
        if (cal == null) {
            cal = this.defaultCal;
        }
        cal.setTime(x);
        this.sbuf.setLength(0);
        appendTime(this.sbuf, cal, cal.get(14) * 1000000);
        if (this.min74) {
            this.appendTimeZone(this.sbuf, cal);
        }
        showString("time", cal, x, this.sbuf.toString());
        return this.sbuf.toString();
    }
    
    private static void appendDate(final StringBuffer sb, final Calendar cal) {
        final int l_year = cal.get(1);
        for (int l_yearlen = String.valueOf(l_year).length(), i = 4; i > l_yearlen; --i) {
            sb.append("0");
        }
        sb.append(l_year);
        sb.append('-');
        final int l_month = cal.get(2) + 1;
        if (l_month < 10) {
            sb.append('0');
        }
        sb.append(l_month);
        sb.append('-');
        final int l_day = cal.get(5);
        if (l_day < 10) {
            sb.append('0');
        }
        sb.append(l_day);
    }
    
    private static void appendTime(final StringBuffer sb, final Calendar cal, final int nanos) {
        final int hours = cal.get(11);
        if (hours < 10) {
            sb.append('0');
        }
        sb.append(hours);
        sb.append(':');
        final int minutes = cal.get(12);
        if (minutes < 10) {
            sb.append('0');
        }
        sb.append(minutes);
        sb.append(':');
        final int seconds = cal.get(13);
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(seconds);
        final char[] decimalStr = { '0', '0', '0', '0', '0', '0', '0', '0', '0' };
        final char[] nanoStr = Integer.toString(nanos).toCharArray();
        System.arraycopy(nanoStr, 0, decimalStr, decimalStr.length - nanoStr.length, nanoStr.length);
        sb.append('.');
        sb.append(decimalStr, 0, 6);
    }
    
    private void appendTimeZone(final StringBuffer sb, final Calendar cal) {
        final int offset = (cal.get(15) + cal.get(16)) / 1000;
        final int absoff = Math.abs(offset);
        final int hours = absoff / 60 / 60;
        final int mins = (absoff - hours * 60 * 60) / 60;
        final int secs = absoff - hours * 60 * 60 - mins * 60;
        sb.append((offset >= 0) ? " +" : " -");
        if (hours < 10) {
            sb.append('0');
        }
        sb.append(hours);
        sb.append(':');
        if (mins < 10) {
            sb.append('0');
        }
        sb.append(mins);
        if (this.min82) {
            sb.append(':');
            if (secs < 10) {
                sb.append('0');
            }
            sb.append(secs);
        }
    }
    
    private static void appendEra(final StringBuffer sb, final Calendar cal) {
        if (cal.get(0) == 0) {
            sb.append(" BC");
        }
    }
    
    private static int skipWhitespace(final char[] s, final int start) {
        final int slen = s.length;
        for (int i = start; i < slen; ++i) {
            if (!Character.isSpace(s[i])) {
                return i;
            }
        }
        return slen;
    }
    
    private static int firstNonDigit(final char[] s, final int start) {
        final int slen = s.length;
        for (int i = start; i < slen; ++i) {
            if (!Character.isDigit(s[i])) {
                return i;
            }
        }
        return slen;
    }
    
    private static int number(final char[] s, final int start, final int end) {
        if (start >= end) {
            throw new NumberFormatException();
        }
        int n = 0;
        for (int i = start; i < end; ++i) {
            n = 10 * n + (s[i] - '0');
        }
        return n;
    }
    
    private static char charAt(final char[] s, final int pos) {
        if (pos >= 0 && pos < s.length) {
            return s[pos];
        }
        return '\0';
    }
    
    public java.sql.Date toDateBin(TimeZone tz, final byte[] bytes) throws PSQLException {
        if (bytes.length != 4) {
            throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "date"), PSQLState.BAD_DATETIME_FORMAT);
        }
        final int days = ByteConverter.int4(bytes, 0);
        if (tz == null) {
            tz = this.defaultTz;
        }
        final long secs = toJavaSecs(days * 86400L);
        long millis = secs * 1000L;
        int offset = tz.getOffset(millis);
        if (millis <= -185543533774800000L) {
            millis = -9223372036832400000L;
            offset = 0;
        }
        else if (millis >= 185543533774800000L) {
            millis = 9223372036825200000L;
            offset = 0;
        }
        return new java.sql.Date(millis - offset);
    }
    
    public Time toTimeBin(TimeZone tz, final byte[] bytes) throws PSQLException {
        if (bytes.length != 8 && bytes.length != 12) {
            throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "time"), PSQLState.BAD_DATETIME_FORMAT);
        }
        long millis;
        if (this.usesDouble) {
            final double time = ByteConverter.float8(bytes, 0);
            millis = (long)(time * 1000.0);
        }
        else {
            final long time2 = ByteConverter.int8(bytes, 0);
            millis = time2 / 1000L;
        }
        int timeOffset;
        if (bytes.length == 12) {
            timeOffset = ByteConverter.int4(bytes, 8);
            timeOffset *= -1000;
        }
        else {
            if (tz == null) {
                tz = this.defaultTz;
            }
            timeOffset = tz.getOffset(millis);
        }
        millis -= timeOffset;
        return new Time(millis);
    }
    
    public Timestamp toTimestampBin(TimeZone tz, final byte[] bytes, final boolean timestamptz) throws PSQLException {
        if (bytes.length != 8) {
            throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "timestamp"), PSQLState.BAD_DATETIME_FORMAT);
        }
        long secs;
        int nanos;
        if (this.usesDouble) {
            final double time = ByteConverter.float8(bytes, 0);
            if (time == Double.POSITIVE_INFINITY) {
                return new Timestamp(9223372036825200000L);
            }
            if (time == Double.NEGATIVE_INFINITY) {
                return new Timestamp(-9223372036832400000L);
            }
            secs = (long)time;
            nanos = (int)((time - secs) * 1000000.0);
        }
        else {
            final long time2 = ByteConverter.int8(bytes, 0);
            if (time2 == Long.MAX_VALUE) {
                return new Timestamp(9223372036825200000L);
            }
            if (time2 == Long.MIN_VALUE) {
                return new Timestamp(-9223372036832400000L);
            }
            secs = time2 / 1000000L;
            nanos = (int)(time2 - secs * 1000000L);
        }
        if (nanos < 0) {
            --secs;
            nanos += 1000000;
        }
        nanos *= 1000;
        secs = toJavaSecs(secs);
        long millis = secs * 1000L;
        if (!timestamptz) {
            if (tz == null) {
                tz = this.defaultTz;
            }
            millis -= tz.getOffset(millis);
        }
        final Timestamp ts = new Timestamp(millis);
        ts.setNanos(nanos);
        return ts;
    }
    
    public java.sql.Date convertToDate(final Timestamp timestamp, TimeZone tz) {
        long millis = timestamp.getTime();
        if (millis <= -9223372036832400000L || millis >= 9223372036825200000L) {
            return new java.sql.Date(millis);
        }
        if (tz == null) {
            tz = this.defaultTz;
        }
        final int offset = tz.getOffset(millis) + tz.getDSTSavings();
        final long timePart = millis % 86400000L;
        if (timePart + offset >= 86400000L) {
            millis += 86400000L;
        }
        millis -= timePart;
        millis -= offset;
        return new java.sql.Date(millis);
    }
    
    public Time convertToTime(final Timestamp timestamp, TimeZone tz) {
        long millis = timestamp.getTime();
        if (tz == null) {
            tz = this.defaultTz;
        }
        final int offset = tz.getOffset(millis);
        final long low = -tz.getOffset(millis);
        final long high = low + 86400000L;
        if (millis < low) {
            do {
                millis += 86400000L;
            } while (millis < low);
        }
        else if (millis >= high) {
            do {
                millis -= 86400000L;
            } while (millis > high);
        }
        return new Time(millis);
    }
    
    public String timeToString(final Date time) {
        final long millis = time.getTime();
        if (millis <= -9223372036832400000L) {
            return "-infinity";
        }
        if (millis >= 9223372036825200000L) {
            return "infinity";
        }
        return time.toString();
    }
    
    private static long toJavaSecs(long secs) {
        secs += 946684800L;
        if (secs < -12219292800L) {
            secs += 864000L;
            if (secs < -14825808000L) {
                int extraLeaps = (int)((secs + 14825808000L) / 3155760000L);
                extraLeaps = --extraLeaps - extraLeaps / 4;
                secs += extraLeaps * 86400L;
            }
        }
        return secs;
    }
    
    private static long toPgSecs(long secs) {
        secs -= 946684800L;
        if (secs < -13165977600L) {
            secs -= 864000L;
            if (secs < -15773356800L) {
                int years = (int)((secs + 15773356800L) / -3155823050L);
                years = ++years - years / 4;
                secs += years * 86400;
            }
        }
        return secs;
    }
    
    public void toBinDate(TimeZone tz, final byte[] bytes, final java.sql.Date value) throws PSQLException {
        long millis = value.getTime();
        if (tz == null) {
            tz = this.defaultTz;
        }
        millis += tz.getOffset(millis);
        final long secs = toPgSecs(millis / 1000L);
        ByteConverter.int4(bytes, 0, (int)(secs / 86400L));
    }
    
    private static class ParsedTimestamp
    {
        boolean hasDate;
        int era;
        int year;
        int month;
        boolean hasTime;
        int day;
        int hour;
        int minute;
        int second;
        int nanos;
        Calendar tz;
        
        private ParsedTimestamp() {
            this.hasDate = false;
            this.era = 1;
            this.year = 1970;
            this.month = 1;
            this.hasTime = false;
            this.day = 1;
            this.hour = 0;
            this.minute = 0;
            this.second = 0;
            this.nanos = 0;
            this.tz = null;
        }
    }
}
