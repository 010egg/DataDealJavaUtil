// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

public class SizeAndTimeBasedRollingPolicy<E> extends TimeBasedRollingPolicy<E>
{
    String maxFileSizeAsString;
    
    @Override
    public void start() {
        final SizeAndTimeBasedFNATP<E> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<E>();
        if (this.maxFileSizeAsString == null) {
            this.addError("MaxFileSize property must be set");
            return;
        }
        this.addInfo("Achive files will be limied to [" + this.maxFileSizeAsString + "] each.");
        sizeAndTimeBasedFNATP.setMaxFileSize(this.maxFileSizeAsString);
        this.timeBasedFileNamingAndTriggeringPolicy = sizeAndTimeBasedFNATP;
        super.start();
    }
    
    public void setMaxFileSize(final String maxFileSize) {
        this.maxFileSizeAsString = maxFileSize;
    }
    
    @Override
    public String toString() {
        return "c.q.l.core.rolling.SizeAndTimeBasedRollingPolicy@" + this.hashCode();
    }
}
