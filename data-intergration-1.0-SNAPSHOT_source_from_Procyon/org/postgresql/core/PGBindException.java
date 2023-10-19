// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.io.IOException;

public class PGBindException extends IOException
{
    private IOException _ioe;
    
    public PGBindException(final IOException ioe) {
        this._ioe = ioe;
    }
    
    public IOException getIOException() {
        return this._ioe;
    }
}
