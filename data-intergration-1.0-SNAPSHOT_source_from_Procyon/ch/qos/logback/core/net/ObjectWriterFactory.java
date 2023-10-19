// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectWriterFactory
{
    public AutoFlushingObjectWriter newAutoFlushingObjectWriter(final OutputStream outputStream) throws IOException {
        return new AutoFlushingObjectWriter(new ObjectOutputStream(outputStream), 70);
    }
}
