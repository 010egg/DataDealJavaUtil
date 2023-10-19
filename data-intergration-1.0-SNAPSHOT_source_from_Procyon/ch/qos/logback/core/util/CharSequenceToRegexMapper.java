// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.text.DateFormatSymbols;

class CharSequenceToRegexMapper
{
    DateFormatSymbols symbols;
    
    CharSequenceToRegexMapper() {
        this.symbols = DateFormatSymbols.getInstance();
    }
    
    String toRegex(final CharSequenceState css) {
        final int occurrences = css.occurrences;
        final char c = css.c;
        switch (css.c) {
            case 'G':
            case 'z': {
                return ".*";
            }
            case 'M': {
                if (occurrences <= 2) {
                    return this.number(occurrences);
                }
                if (occurrences == 3) {
                    return this.getRegexForShortMonths();
                }
                return this.getRegexForLongMonths();
            }
            case 'D':
            case 'F':
            case 'H':
            case 'K':
            case 'S':
            case 'W':
            case 'd':
            case 'h':
            case 'k':
            case 'm':
            case 's':
            case 'w':
            case 'y': {
                return this.number(occurrences);
            }
            case 'E': {
                if (occurrences >= 4) {
                    return this.getRegexForLongDaysOfTheWeek();
                }
                return this.getRegexForShortDaysOfTheWeek();
            }
            case 'a': {
                return this.getRegexForAmPms();
            }
            case 'Z': {
                return "(\\+|-)\\d{4}";
            }
            case '.': {
                return "\\.";
            }
            case '\\': {
                throw new IllegalStateException("Forward slashes are not allowed");
            }
            case '\'': {
                if (occurrences == 1) {
                    return "";
                }
                throw new IllegalStateException("Too many single quotes");
            }
            default: {
                if (occurrences == 1) {
                    return "" + c;
                }
                return c + "{" + occurrences + "}";
            }
        }
    }
    
    private String number(final int occurrences) {
        return "\\d{" + occurrences + "}";
    }
    
    private String getRegexForAmPms() {
        return this.symbolArrayToRegex(this.symbols.getAmPmStrings());
    }
    
    private String getRegexForLongDaysOfTheWeek() {
        return this.symbolArrayToRegex(this.symbols.getWeekdays());
    }
    
    private String getRegexForShortDaysOfTheWeek() {
        return this.symbolArrayToRegex(this.symbols.getShortWeekdays());
    }
    
    private String getRegexForLongMonths() {
        return this.symbolArrayToRegex(this.symbols.getMonths());
    }
    
    String getRegexForShortMonths() {
        return this.symbolArrayToRegex(this.symbols.getShortMonths());
    }
    
    private String symbolArrayToRegex(final String[] symbolArray) {
        final int[] minMax = findMinMaxLengthsInSymbols(symbolArray);
        return ".{" + minMax[0] + "," + minMax[1] + "}";
    }
    
    static int[] findMinMaxLengthsInSymbols(final String[] symbols) {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (final String symbol : symbols) {
            final int len = symbol.length();
            if (len != 0) {
                min = Math.min(min, len);
                max = Math.max(max, len);
            }
        }
        return new int[] { min, max };
    }
}
