// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class LayoutBase<E> extends ContextAwareBase implements Layout<E>
{
    protected boolean started;
    String fileHeader;
    String fileFooter;
    String presentationHeader;
    String presentationFooter;
    
    @Override
    public void setContext(final Context context) {
        this.context = context;
    }
    
    @Override
    public Context getContext() {
        return this.context;
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    @Override
    public String getFileHeader() {
        return this.fileHeader;
    }
    
    @Override
    public String getPresentationHeader() {
        return this.presentationHeader;
    }
    
    @Override
    public String getPresentationFooter() {
        return this.presentationFooter;
    }
    
    @Override
    public String getFileFooter() {
        return this.fileFooter;
    }
    
    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    public void setFileHeader(final String header) {
        this.fileHeader = header;
    }
    
    public void setFileFooter(final String footer) {
        this.fileFooter = footer;
    }
    
    public void setPresentationHeader(final String header) {
        this.presentationHeader = header;
    }
    
    public void setPresentationFooter(final String footer) {
        this.presentationFooter = footer;
    }
}
