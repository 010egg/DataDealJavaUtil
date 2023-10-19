// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.io.File;
import ch.qos.logback.core.util.DefaultInvocationGate;
import ch.qos.logback.core.util.InvocationGate;
import ch.qos.logback.core.util.FileSize;

public class SizeBasedTriggeringPolicy<E> extends TriggeringPolicyBase<E>
{
    public static final String SEE_SIZE_FORMAT = "http://logback.qos.ch/codes.html#sbtp_size_format";
    public static final long DEFAULT_MAX_FILE_SIZE = 10485760L;
    String maxFileSizeAsString;
    FileSize maxFileSize;
    InvocationGate invocationGate;
    
    public SizeBasedTriggeringPolicy() {
        this.maxFileSizeAsString = Long.toString(10485760L);
        this.invocationGate = new DefaultInvocationGate();
    }
    
    public SizeBasedTriggeringPolicy(final String maxFileSize) {
        this.maxFileSizeAsString = Long.toString(10485760L);
        this.invocationGate = new DefaultInvocationGate();
        this.setMaxFileSize(maxFileSize);
    }
    
    @Override
    public boolean isTriggeringEvent(final File activeFile, final E event) {
        final long now = System.currentTimeMillis();
        return !this.invocationGate.isTooSoon(now) && activeFile.length() >= this.maxFileSize.getSize();
    }
    
    public String getMaxFileSize() {
        return this.maxFileSizeAsString;
    }
    
    public void setMaxFileSize(final String maxFileSize) {
        this.maxFileSizeAsString = maxFileSize;
        this.maxFileSize = FileSize.valueOf(maxFileSize);
    }
    
    long toFileSize(final String value) {
        if (value == null) {
            return 10485760L;
        }
        String s = value.trim().toUpperCase();
        long multiplier = 1L;
        int index;
        if ((index = s.indexOf("KB")) != -1) {
            multiplier = 1024L;
            s = s.substring(0, index);
        }
        else if ((index = s.indexOf("MB")) != -1) {
            multiplier = 1048576L;
            s = s.substring(0, index);
        }
        else if ((index = s.indexOf("GB")) != -1) {
            multiplier = 1073741824L;
            s = s.substring(0, index);
        }
        if (s != null) {
            try {
                return Long.valueOf(s) * multiplier;
            }
            catch (NumberFormatException e) {
                this.addError("[" + s + "] is not in proper int format. Please refer to " + "http://logback.qos.ch/codes.html#sbtp_size_format");
                this.addError("[" + value + "] not in expected format.", e);
            }
        }
        return 10485760L;
    }
}
