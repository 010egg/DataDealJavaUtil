// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import java.util.Iterator;
import java.util.List;
import ch.qos.logback.core.CoreConstants;

public class CallerData
{
    public static final String NA = "?";
    private static final String LOG4J_CATEGORY = "org.apache.log4j.Category";
    private static final String SLF4J_BOUNDARY = "org.slf4j.Logger";
    public static final int LINE_NA = -1;
    public static final String CALLER_DATA_NA;
    public static final StackTraceElement[] EMPTY_CALLER_DATA_ARRAY;
    
    static {
        CALLER_DATA_NA = "?#?:?" + CoreConstants.LINE_SEPARATOR;
        EMPTY_CALLER_DATA_ARRAY = new StackTraceElement[0];
    }
    
    public static StackTraceElement[] extract(final Throwable t, final String fqnOfInvokingClass, final int maxDepth, final List<String> frameworkPackageList) {
        if (t == null) {
            return null;
        }
        final StackTraceElement[] steArray = t.getStackTrace();
        int found = -1;
        for (int i = 0; i < steArray.length; ++i) {
            if (isInFrameworkSpace(steArray[i].getClassName(), fqnOfInvokingClass, frameworkPackageList)) {
                found = i + 1;
            }
            else if (found != -1) {
                break;
            }
        }
        if (found == -1) {
            return CallerData.EMPTY_CALLER_DATA_ARRAY;
        }
        final int availableDepth = steArray.length - found;
        final int desiredDepth = (maxDepth < availableDepth) ? maxDepth : availableDepth;
        final StackTraceElement[] callerDataArray = new StackTraceElement[desiredDepth];
        for (int j = 0; j < desiredDepth; ++j) {
            callerDataArray[j] = steArray[found + j];
        }
        return callerDataArray;
    }
    
    static boolean isInFrameworkSpace(final String currentClass, final String fqnOfInvokingClass, final List<String> frameworkPackageList) {
        return currentClass.equals(fqnOfInvokingClass) || currentClass.equals("org.apache.log4j.Category") || currentClass.startsWith("org.slf4j.Logger") || isInFrameworkSpaceList(currentClass, frameworkPackageList);
    }
    
    private static boolean isInFrameworkSpaceList(final String currentClass, final List<String> frameworkPackageList) {
        if (frameworkPackageList == null) {
            return false;
        }
        for (final String s : frameworkPackageList) {
            if (currentClass.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
    
    public static StackTraceElement naInstance() {
        return new StackTraceElement("?", "?", "?", -1);
    }
}
