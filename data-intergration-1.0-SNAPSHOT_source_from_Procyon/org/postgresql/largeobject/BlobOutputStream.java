// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.largeobject;

import java.sql.SQLException;
import java.io.IOException;
import java.io.OutputStream;

public class BlobOutputStream extends OutputStream
{
    private LargeObject lo;
    private byte[] buf;
    private int bsize;
    private int bpos;
    
    public BlobOutputStream(final LargeObject lo) {
        this(lo, 1024);
    }
    
    public BlobOutputStream(final LargeObject lo, final int bsize) {
        this.lo = lo;
        this.bsize = bsize;
        this.buf = new byte[bsize];
        this.bpos = 0;
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.checkClosed();
        try {
            if (this.bpos >= this.bsize) {
                this.lo.write(this.buf);
                this.bpos = 0;
            }
            this.buf[this.bpos++] = (byte)b;
        }
        catch (SQLException se) {
            throw new IOException(se.toString());
        }
    }
    
    @Override
    public void write(final byte[] buf, final int off, final int len) throws IOException {
        this.checkClosed();
        try {
            if (this.bpos > 0) {
                this.flush();
            }
            if (off == 0 && len == buf.length) {
                this.lo.write(buf);
            }
            else {
                this.lo.write(buf, off, len);
            }
        }
        catch (SQLException se) {
            throw new IOException(se.toString());
        }
    }
    
    @Override
    public void flush() throws IOException {
        this.checkClosed();
        try {
            if (this.bpos > 0) {
                this.lo.write(this.buf, 0, this.bpos);
            }
            this.bpos = 0;
        }
        catch (SQLException se) {
            throw new IOException(se.toString());
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.lo != null) {
            try {
                this.flush();
                this.lo.close();
                this.lo = null;
            }
            catch (SQLException se) {
                throw new IOException(se.toString());
            }
        }
    }
    
    private void checkClosed() throws IOException {
        if (this.lo == null) {
            throw new IOException("BlobOutputStream is closed");
        }
    }
}
