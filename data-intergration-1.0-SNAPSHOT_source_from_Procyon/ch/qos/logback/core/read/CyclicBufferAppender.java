// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.read;

import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.AppenderBase;

public class CyclicBufferAppender<E> extends AppenderBase<E>
{
    CyclicBuffer<E> cb;
    int maxSize;
    
    public CyclicBufferAppender() {
        this.maxSize = 512;
    }
    
    @Override
    public void start() {
        this.cb = new CyclicBuffer<E>(this.maxSize);
        super.start();
    }
    
    @Override
    public void stop() {
        this.cb = null;
        super.stop();
    }
    
    @Override
    protected void append(final E eventObject) {
        if (!this.isStarted()) {
            return;
        }
        this.cb.add(eventObject);
    }
    
    public int getLength() {
        if (this.isStarted()) {
            return this.cb.length();
        }
        return 0;
    }
    
    public E get(final int i) {
        if (this.isStarted()) {
            return this.cb.get(i);
        }
        return null;
    }
    
    public void reset() {
        this.cb.clear();
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }
}
