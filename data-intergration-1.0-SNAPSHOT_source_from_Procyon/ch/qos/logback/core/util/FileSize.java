// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSize
{
    private static final String LENGTH_PART = "([0-9]+)";
    private static final int DOUBLE_GROUP = 1;
    private static final String UNIT_PART = "(|kb|mb|gb)s?";
    private static final int UNIT_GROUP = 2;
    private static final Pattern FILE_SIZE_PATTERN;
    public static final long KB_COEFFICIENT = 1024L;
    public static final long MB_COEFFICIENT = 1048576L;
    public static final long GB_COEFFICIENT = 1073741824L;
    final long size;
    
    public FileSize(final long size) {
        this.size = size;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public static FileSize valueOf(final String fileSizeStr) {
        final Matcher matcher = FileSize.FILE_SIZE_PATTERN.matcher(fileSizeStr);
        if (matcher.matches()) {
            final String lenStr = matcher.group(1);
            final String unitStr = matcher.group(2);
            final long lenValue = Long.valueOf(lenStr);
            long coefficient;
            if (unitStr.equalsIgnoreCase("")) {
                coefficient = 1L;
            }
            else if (unitStr.equalsIgnoreCase("kb")) {
                coefficient = 1024L;
            }
            else if (unitStr.equalsIgnoreCase("mb")) {
                coefficient = 1048576L;
            }
            else {
                if (!unitStr.equalsIgnoreCase("gb")) {
                    throw new IllegalStateException("Unexpected " + unitStr);
                }
                coefficient = 1073741824L;
            }
            return new FileSize(lenValue * coefficient);
        }
        throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
    }
    
    @Override
    public String toString() {
        final long inKB = this.size / 1024L;
        if (inKB == 0L) {
            return this.size + " Bytes";
        }
        final long inMB = this.size / 1048576L;
        if (inMB == 0L) {
            return inKB + " KB";
        }
        return inMB + " MB";
    }
    
    static {
        FILE_SIZE_PATTERN = Pattern.compile("([0-9]+)\\s*(|kb|mb|gb)s?", 2);
    }
}
