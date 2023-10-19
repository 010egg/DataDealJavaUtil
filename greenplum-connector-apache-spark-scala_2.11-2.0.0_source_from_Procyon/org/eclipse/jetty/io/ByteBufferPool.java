// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import java.nio.ByteBuffer;

public interface ByteBufferPool
{
    ByteBuffer acquire(final int p0, final boolean p1);
    
    void release(final ByteBuffer p0);
}
