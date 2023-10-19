// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import java.util.ArrayList;
import java.util.List;

public class LoggerNameUtil
{
    public static int getFirstSeparatorIndexOf(final String name) {
        return getSeparatorIndexOf(name, 0);
    }
    
    public static int getSeparatorIndexOf(final String name, final int fromIndex) {
        final int dotIndex = name.indexOf(46, fromIndex);
        final int dollarIndex = name.indexOf(36, fromIndex);
        if (dotIndex == -1 && dollarIndex == -1) {
            return -1;
        }
        if (dotIndex == -1) {
            return dollarIndex;
        }
        if (dollarIndex == -1) {
            return dotIndex;
        }
        return (dotIndex < dollarIndex) ? dotIndex : dollarIndex;
    }
    
    public static List<String> computeNameParts(final String loggerName) {
        final List<String> partList = new ArrayList<String>();
        int fromIndex = 0;
        while (true) {
            final int index = getSeparatorIndexOf(loggerName, fromIndex);
            if (index == -1) {
                break;
            }
            partList.add(loggerName.substring(fromIndex, index));
            fromIndex = index + 1;
        }
        partList.add(loggerName.substring(fromIndex));
        return partList;
    }
}
