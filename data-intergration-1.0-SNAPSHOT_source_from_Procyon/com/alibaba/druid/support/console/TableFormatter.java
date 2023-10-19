// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.console;

import java.util.Iterator;
import java.util.List;

public class TableFormatter
{
    public static String format(final List<String[]> rows) {
        final String[] titleRow = rows.get(0);
        final int[] maxLens = new int[titleRow.length];
        for (final String[] row : rows) {
            for (int j = 0; j < row.length; ++j) {
                final int len = displayLen(row[j]);
                if (len > maxLens[j]) {
                    maxLens[j] = len;
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(makeSplitLine(maxLens));
        for (int i = 0; i < rows.size(); ++i) {
            final String[] row2 = rows.get(i);
            sb.append("|");
            for (int k = 0; k < row2.length; ++k) {
                sb.append(padStr(row2[k], maxLens[k]));
                sb.append(" |");
            }
            sb.append("\n");
            if (i == 0) {
                sb.append(makeSplitLine(maxLens));
            }
        }
        sb.append(makeSplitLine(maxLens));
        return sb.toString();
    }
    
    public static String makeSplitLine(final int[] maxLens) {
        final StringBuilder sb = new StringBuilder("+");
        for (final int len : maxLens) {
            for (int i = 0; i < len; ++i) {
                sb.append("-");
            }
            sb.append("-+");
        }
        sb.append("\n");
        return sb.toString();
    }
    
    public static int displayLen(final String value) {
        return value.length();
    }
    
    public static String padStr(final String old, final int length) {
        if (old == null) {
            return "";
        }
        final int vLen = displayLen(old);
        if (vLen > length) {
            return old;
        }
        final StringBuilder sb = new StringBuilder(old);
        for (int i = 0; i < length - vLen; ++i) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
