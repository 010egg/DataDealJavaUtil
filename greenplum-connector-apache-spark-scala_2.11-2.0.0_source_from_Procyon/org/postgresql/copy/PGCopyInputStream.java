// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.copy;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import java.io.IOException;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.PGConnection;
import java.io.InputStream;

public class PGCopyInputStream extends InputStream implements CopyOut
{
    private CopyOut op;
    private byte[] buf;
    private int at;
    private int len;
    
    public PGCopyInputStream(final PGConnection connection, final String sql) throws SQLException {
        this(connection.getCopyAPI().copyOut(sql));
    }
    
    public PGCopyInputStream(final CopyOut op) {
        this.op = op;
    }
    
    private boolean gotBuf() throws IOException {
        if (this.at < this.len) {
            return this.buf != null;
        }
        try {
            this.buf = this.op.readFromCopy();
        }
        catch (SQLException sqle) {
            throw new IOException(GT.tr("Copying from database failed: {0}", sqle));
        }
        if (this.buf == null) {
            this.at = -1;
            return false;
        }
        this.at = 0;
        this.len = this.buf.length;
        return true;
    }
    
    private void checkClosed() throws IOException {
        if (this.op == null) {
            throw new IOException(GT.tr("This copy stream is closed.", new Object[0]));
        }
    }
    
    @Override
    public int available() throws IOException {
        this.checkClosed();
        return (this.buf != null) ? (this.len - this.at) : 0;
    }
    
    @Override
    public int read() throws IOException {
        this.checkClosed();
        return this.gotBuf() ? (this.buf[this.at++] & 0xFF) : -1;
    }
    
    @Override
    public int read(final byte[] buf) throws IOException {
        return this.read(buf, 0, buf.length);
    }
    
    @Override
    public int read(final byte[] buf, final int off, final int siz) throws IOException {
        this.checkClosed();
        int got = 0;
        boolean didReadSomething = false;
        while (got < siz && (didReadSomething = this.gotBuf())) {
            buf[off + got++] = this.buf[this.at++];
        }
        return (got == 0 && !didReadSomething) ? -1 : got;
    }
    
    @Override
    public byte[] readFromCopy() throws SQLException {
        byte[] result = this.buf;
        try {
            if (this.gotBuf()) {
                if (this.at > 0 || this.len < this.buf.length) {
                    final byte[] ba = new byte[this.len - this.at];
                    for (int i = this.at; i < this.len; ++i) {
                        ba[i - this.at] = this.buf[i];
                    }
                    result = ba;
                }
                this.at = this.len;
            }
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Read from copy failed.", new Object[0]), PSQLState.CONNECTION_FAILURE);
        }
        return result;
    }
    
    @Override
    public byte[] readFromCopy(final boolean block) throws SQLException {
        return this.readFromCopy();
    }
    
    @Override
    public void close() throws IOException {
        if (this.op == null) {
            return;
        }
        if (this.op.isActive()) {
            try {
                this.op.cancelCopy();
            }
            catch (SQLException se) {
                final IOException ioe = new IOException("Failed to close copy reader.");
                ioe.initCause(se);
                throw ioe;
            }
        }
        this.op = null;
    }
    
    @Override
    public void cancelCopy() throws SQLException {
        this.op.cancelCopy();
    }
    
    @Override
    public int getFormat() {
        return this.op.getFormat();
    }
    
    @Override
    public int getFieldFormat(final int field) {
        return this.op.getFieldFormat(field);
    }
    
    @Override
    public int getFieldCount() {
        return this.op.getFieldCount();
    }
    
    @Override
    public boolean isActive() {
        return this.op != null && this.op.isActive();
    }
    
    @Override
    public long getHandledRowCount() {
        return this.op.getHandledRowCount();
    }
}
