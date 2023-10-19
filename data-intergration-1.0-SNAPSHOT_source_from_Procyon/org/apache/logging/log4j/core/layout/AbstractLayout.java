// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.status.StatusLogger;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.LogEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import java.io.Serializable;

public abstract class AbstractLayout<T extends Serializable> implements Layout<T>
{
    protected static final Logger LOGGER;
    protected final Configuration configuration;
    protected long eventCount;
    protected final byte[] footer;
    protected final byte[] header;
    
    @Deprecated
    public AbstractLayout(final byte[] header, final byte[] footer) {
        this(null, header, footer);
    }
    
    public AbstractLayout(final Configuration configuration, final byte[] header, final byte[] footer) {
        this.configuration = configuration;
        this.header = header;
        this.footer = footer;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    @Override
    public byte[] getFooter() {
        return this.footer;
    }
    
    @Override
    public byte[] getHeader() {
        return this.header;
    }
    
    protected void markEvent() {
        ++this.eventCount;
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        final byte[] data = this.toByteArray(event);
        writeTo(data, 0, data.length, destination);
    }
    
    public static void writeTo(final byte[] data, int offset, int length, final ByteBufferDestination destination) {
        int chunk = 0;
        ByteBuffer buffer = destination.getByteBuffer();
        do {
            if (length > buffer.remaining()) {
                buffer = destination.drain(buffer);
            }
            chunk = Math.min(length, buffer.remaining());
            buffer.put(data, offset, chunk);
            offset += chunk;
            length -= chunk;
        } while (length > 0);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
