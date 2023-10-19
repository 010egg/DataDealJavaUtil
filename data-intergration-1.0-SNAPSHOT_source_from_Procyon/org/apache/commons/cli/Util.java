// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

final class Util
{
    static String stripLeadingHyphens(final String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("--")) {
            return str.substring(2, str.length());
        }
        if (str.startsWith("-")) {
            return str.substring(1, str.length());
        }
        return str;
    }
    
    static String stripLeadingAndTrailingQuotes(String str) {
        final int length = str.length();
        if (length > 1 && str.startsWith("\"") && str.endsWith("\"") && str.substring(1, length - 1).indexOf(34) == -1) {
            str = str.substring(1, length - 1);
        }
        return str;
    }
}
