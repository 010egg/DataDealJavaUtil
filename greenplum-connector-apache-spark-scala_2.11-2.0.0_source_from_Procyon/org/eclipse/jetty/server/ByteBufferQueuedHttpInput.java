// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.nio.ByteBuffer;

public class ByteBufferQueuedHttpInput extends QueuedHttpInput<ByteBuffer>
{
    @Override
    protected int remaining(final ByteBuffer item) {
        return item.remaining();
    }
    
    @Override
    protected int get(final ByteBuffer item, final byte[] buffer, final int offset, final int length) {
        final int l = Math.min(item.remaining(), length);
        item.get(buffer, offset, l);
        return l;
    }
    
    @Override
    protected void consume(final ByteBuffer item, final int length) {
        item.position(item.position() + length);
    }
    
    @Override
    protected void onContentConsumed(final ByteBuffer item) {
    }
}
