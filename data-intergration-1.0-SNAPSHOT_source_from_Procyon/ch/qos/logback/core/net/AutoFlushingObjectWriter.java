// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AutoFlushingObjectWriter implements ObjectWriter
{
    private final ObjectOutputStream objectOutputStream;
    private final int resetFrequency;
    private int writeCounter;
    
    public AutoFlushingObjectWriter(final ObjectOutputStream objectOutputStream, final int resetFrequency) {
        this.writeCounter = 0;
        this.objectOutputStream = objectOutputStream;
        this.resetFrequency = resetFrequency;
    }
    
    @Override
    public void write(final Object object) throws IOException {
        this.objectOutputStream.writeObject(object);
        this.objectOutputStream.flush();
        this.preventMemoryLeak();
    }
    
    private void preventMemoryLeak() throws IOException {
        if (++this.writeCounter >= this.resetFrequency) {
            this.objectOutputStream.reset();
            this.writeCounter = 0;
        }
    }
}
