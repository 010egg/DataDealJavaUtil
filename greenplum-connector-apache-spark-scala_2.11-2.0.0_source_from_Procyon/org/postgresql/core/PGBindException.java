// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.io.IOException;

public class PGBindException extends IOException
{
    private final IOException ioe;
    
    public PGBindException(final IOException ioe) {
        this.ioe = ioe;
    }
    
    public IOException getIOException() {
        return this.ioe;
    }
}
