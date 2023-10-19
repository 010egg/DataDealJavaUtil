// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.largeobject;

import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;

public class BlobInputStream extends InputStream
{
    private LargeObject lo;
    private long apos;
    private byte[] buffer;
    private int bpos;
    private int bsize;
    private int mpos;
    private long limit;
    
    public BlobInputStream(final LargeObject lo) {
        this(lo, 1024);
    }
    
    public BlobInputStream(final LargeObject lo, final int bsize) {
        this(lo, 1024, -1L);
    }
    
    public BlobInputStream(final LargeObject lo, final int bsize, final long limit) {
        this.mpos = 0;
        this.limit = -1L;
        this.lo = lo;
        this.buffer = null;
        this.bpos = 0;
        this.apos = 0L;
        this.bsize = bsize;
        this.limit = limit;
    }
    
    @Override
    public int read() throws IOException {
        this.checkClosed();
        try {
            if (this.limit > 0L && this.apos >= this.limit) {
                return -1;
            }
            if (this.buffer == null || this.bpos >= this.buffer.length) {
                this.buffer = this.lo.read(this.bsize);
                this.bpos = 0;
            }
            if (this.bpos >= this.buffer.length) {
                return -1;
            }
            int ret = this.buffer[this.bpos] & 0x7F;
            if ((this.buffer[this.bpos] & 0x80) == 0x80) {
                ret |= 0x80;
            }
            ++this.bpos;
            ++this.apos;
            return ret;
        }
        catch (SQLException se) {
            throw new IOException(se.toString());
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.lo != null) {
            try {
                this.lo.close();
                this.lo = null;
            }
            catch (SQLException se) {
                throw new IOException(se.toString());
            }
        }
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        try {
            this.mpos = this.lo.tell();
        }
        catch (SQLException ex) {}
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.checkClosed();
        try {
            this.lo.seek(this.mpos);
        }
        catch (SQLException se) {
            throw new IOException(se.toString());
        }
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    private void checkClosed() throws IOException {
        if (this.lo == null) {
            throw new IOException("BlobOutputStream is closed");
        }
    }
}
