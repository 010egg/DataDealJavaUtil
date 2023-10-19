// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Calendar;
import java.util.TimeZone;
import com.alibaba.druid.support.logging.Log;

public class StringUtils
{
    private static final Log LOG;
    
    private StringUtils() {
    }
    
    public static Integer subStringToInteger(final String src, final String start, final String to) {
        return stringToInteger(subString(src, start, to));
    }
    
    public static String subString(final String src, final String start, final String to) {
        return subString(src, start, to, false);
    }
    
    public static String subString(final String src, final String start, final String to, final boolean toLast) {
        int indexFrom = (start == null) ? 0 : src.indexOf(start);
        int indexTo;
        if (to == null) {
            indexTo = src.length();
        }
        else {
            indexTo = (toLast ? src.lastIndexOf(to) : src.indexOf(to));
        }
        if (indexFrom < 0 || indexTo < 0 || indexFrom > indexTo) {
            return null;
        }
        if (null != start) {
            indexFrom += start.length();
        }
        return src.substring(indexFrom, indexTo);
    }
    
    public static Integer stringToInteger(String in) {
        if (in == null) {
            return null;
        }
        in = in.trim();
        if (in.length() == 0) {
            return null;
        }
        try {
            return Integer.parseInt(in);
        }
        catch (NumberFormatException e) {
            StringUtils.LOG.warn("stringToInteger fail,string=" + in, e);
            return null;
        }
    }
    
    public static boolean equals(final String a, final String b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }
    
    public static boolean equalsIgnoreCase(final String a, final String b) {
        if (a == null) {
            return b == null;
        }
        return a.equalsIgnoreCase(b);
    }
    
    public static boolean isEmpty(final CharSequence value) {
        return value == null || value.length() == 0;
    }
    
    public static int lowerHashCode(final String text) {
        if (text == null) {
            return 0;
        }
        int h = 0;
        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            h = 31 * h + ch;
        }
        return h;
    }
    
    public static boolean isNumber(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int sz = str.length();
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        final int start = (str.charAt(0) == '-') ? 1 : 0;
        if (sz > start + 1 && str.charAt(start) == '0' && str.charAt(start + 1) == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false;
            }
            while (i < str.length()) {
                final char ch = str.charAt(i);
                if ((ch < '0' || ch > '9') && (ch < 'a' || ch > 'f') && (ch < 'A' || ch > 'F')) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        else {
            --sz;
            int i;
            for (i = start; i < sz || (i < sz + 1 && allowSigns && !foundDigit); ++i) {
                final char ch = str.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    foundDigit = true;
                    allowSigns = false;
                }
                else if (ch == '.') {
                    if (hasDecPoint || hasExp) {
                        return false;
                    }
                    hasDecPoint = true;
                }
                else if (ch == 'e' || ch == 'E') {
                    if (hasExp) {
                        return false;
                    }
                    if (!foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                }
                else {
                    if (ch != '+' && ch != '-') {
                        return false;
                    }
                    if (!allowSigns) {
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false;
                }
            }
            if (i >= str.length()) {
                return !allowSigns && foundDigit;
            }
            final char ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                return true;
            }
            if (ch == 'e' || ch == 'E') {
                return false;
            }
            if (!allowSigns && (ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F')) {
                return foundDigit;
            }
            return (ch == 'l' || ch == 'L') && foundDigit && !hasExp;
        }
    }
    
    public static boolean isNumber(final char[] chars) {
        if (chars == null || chars.length == 0) {
            return false;
        }
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        final int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false;
            }
            while (i < chars.length) {
                final char ch = chars[i];
                if ((ch < '0' || ch > '9') && (ch < 'a' || ch > 'f') && (ch < 'A' || ch > 'F')) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        else {
            --sz;
            int i;
            for (i = start; i < sz || (i < sz + 1 && allowSigns && !foundDigit); ++i) {
                final char ch = chars[i];
                if (ch >= '0' && ch <= '9') {
                    foundDigit = true;
                    allowSigns = false;
                }
                else if (ch == '.') {
                    if (hasDecPoint || hasExp) {
                        return false;
                    }
                    hasDecPoint = true;
                }
                else if (ch == 'e' || ch == 'E') {
                    if (hasExp) {
                        return false;
                    }
                    if (!foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                }
                else {
                    if (ch != '+' && ch != '-') {
                        return false;
                    }
                    if (!allowSigns) {
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false;
                }
            }
            if (i >= chars.length) {
                return !allowSigns && foundDigit;
            }
            final char ch = chars[i];
            if (ch >= '0' && ch <= '9') {
                return true;
            }
            if (ch == 'e' || ch == 'E') {
                return false;
            }
            if (!allowSigns && (ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F')) {
                return foundDigit;
            }
            if (ch == 'l' || ch == 'L') {
                return foundDigit && !hasExp;
            }
            return ch == '.';
        }
    }
    
    public static String formatDateTime19(final long millis, final TimeZone timeZone) {
        final Calendar cale = (timeZone == null) ? Calendar.getInstance() : Calendar.getInstance(timeZone);
        cale.setTimeInMillis(millis);
        final int year = cale.get(1);
        final int month = cale.get(2) + 1;
        final int dayOfMonth = cale.get(5);
        final int hour = cale.get(11);
        final int minute = cale.get(12);
        final int second = cale.get(13);
        final char[] chars = { (char)(year / 1000 + 48), (char)(year / 100 % 10 + 48), (char)(year / 10 % 10 + 48), (char)(year % 10 + 48), '-', (char)(month / 10 + 48), (char)(month % 10 + 48), '-', (char)(dayOfMonth / 10 + 48), (char)(dayOfMonth % 10 + 48), ' ', (char)(hour / 10 + 48), (char)(hour % 10 + 48), ':', (char)(minute / 10 + 48), (char)(minute % 10 + 48), ':', (char)(second / 10 + 48), (char)(second % 10 + 48) };
        return new String(chars);
    }
    
    public static String removeNameQuotes(final String s) {
        if (s == null || s.length() <= 1) {
            return null;
        }
        final int len = s.length();
        final char c0 = s.charAt(0);
        final char last = s.charAt(len - 1);
        if (c0 == last && (c0 == '`' || c0 == '\'' || c0 == '\"')) {
            return s.substring(1, len - 1);
        }
        return s;
    }
    
    static {
        LOG = LogFactory.getLog(StringUtils.class);
    }
}
