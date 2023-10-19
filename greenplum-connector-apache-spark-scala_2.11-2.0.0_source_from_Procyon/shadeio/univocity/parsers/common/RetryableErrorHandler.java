// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public abstract class RetryableErrorHandler<T extends Context> implements ProcessorErrorHandler<T>
{
    private Object defaultValue;
    private boolean skipRecord;
    
    public RetryableErrorHandler() {
        this.skipRecord = true;
    }
    
    public final void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
        this.keepRecord();
    }
    
    public final void keepRecord() {
        this.skipRecord = false;
    }
    
    public final Object getDefaultValue() {
        return this.defaultValue;
    }
    
    final void prepareToRun() {
        this.skipRecord = true;
        this.defaultValue = null;
    }
    
    public final boolean isRecordSkipped() {
        return this.skipRecord;
    }
}
