// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class NonClosableInputStream extends FilterInputStream
{
    NonClosableInputStream(final InputStream is) {
        super(is);
    }
    
    @Override
    public void close() {
    }
    
    public void realClose() throws IOException {
        super.close();
    }
}
