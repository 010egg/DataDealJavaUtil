// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.encoder;

import java.io.IOException;
import java.io.OutputStream;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class EncoderBase<E> extends ContextAwareBase implements Encoder<E>
{
    protected boolean started;
    protected OutputStream outputStream;
    
    @Override
    public void init(final OutputStream os) throws IOException {
        this.outputStream = os;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
}
