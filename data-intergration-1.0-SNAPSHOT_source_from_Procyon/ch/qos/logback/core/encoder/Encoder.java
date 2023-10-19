// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.encoder;

import java.io.IOException;
import java.io.OutputStream;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAware;

public interface Encoder<E> extends ContextAware, LifeCycle
{
    void init(final OutputStream p0) throws IOException;
    
    void doEncode(final E p0) throws IOException;
    
    void close() throws IOException;
}
