// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.copy;

import org.postgresql.util.ByteStreamWriter;
import java.io.IOException;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.PGConnection;
import java.io.OutputStream;

public class PGCopyOutputStream extends OutputStream implements CopyIn
{
    private CopyIn op;
    private final byte[] copyBuffer;
    private final byte[] singleByteBuffer;
    private int at;
    
    public PGCopyOutputStream(final PGConnection connection, final String sql) throws SQLException {
        this(connection, sql, 65536);
    }
    
    public PGCopyOutputStream(final PGConnection connection, final String sql, final int bufferSize) throws SQLException {
        this(connection.getCopyAPI().copyIn(sql), bufferSize);
    }
    
    public PGCopyOutputStream(final CopyIn op) {
        this(op, 65536);
    }
    
    public PGCopyOutputStream(final CopyIn op, final int bufferSize) {
        this.singleByteBuffer = new byte[1];
        this.at = 0;
        this.op = op;
        this.copyBuffer = new byte[bufferSize];
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.checkClosed();
        if (b < 0 || b > 255) {
            throw new IOException(GT.tr("Cannot write to copy a byte of value {0}", b));
        }
        this.singleByteBuffer[0] = (byte)b;
        this.write(this.singleByteBuffer, 0, 1);
    }
    
    @Override
    public void write(final byte[] buf) throws IOException {
        this.write(buf, 0, buf.length);
    }
    
    @Override
    public void write(final byte[] buf, final int off, final int siz) throws IOException {
        this.checkClosed();
        try {
            this.writeToCopy(buf, off, siz);
        }
        catch (SQLException se) {
            final IOException ioe = new IOException("Write to copy failed.");
            ioe.initCause(se);
            throw ioe;
        }
    }
    
    private void checkClosed() throws IOException {
        if (this.op == null) {
            throw new IOException(GT.tr("This copy stream is closed.", new Object[0]));
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.op == null) {
            return;
        }
        if (this.op.isActive()) {
            try {
                this.endCopy();
            }
            catch (SQLException se) {
                final IOException ioe = new IOException("Ending write to copy failed.");
                ioe.initCause(se);
                throw ioe;
            }
        }
        this.op = null;
    }
    
    @Override
    public void flush() throws IOException {
        this.checkClosed();
        try {
            this.op.writeToCopy(this.copyBuffer, 0, this.at);
            this.at = 0;
            this.op.flushCopy();
        }
        catch (SQLException e) {
            final IOException ioe = new IOException("Unable to flush stream");
            ioe.initCause(e);
            throw ioe;
        }
    }
    
    @Override
    public void writeToCopy(final byte[] buf, final int off, final int siz) throws SQLException {
        if (this.at > 0 && siz > this.copyBuffer.length - this.at) {
            this.op.writeToCopy(this.copyBuffer, 0, this.at);
            this.at = 0;
        }
        if (siz > this.copyBuffer.length) {
            this.op.writeToCopy(buf, off, siz);
        }
        else {
            System.arraycopy(buf, off, this.copyBuffer, this.at, siz);
            this.at += siz;
        }
    }
    
    @Override
    public void writeToCopy(final ByteStreamWriter from) throws SQLException {
        this.op.writeToCopy(from);
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
    public void cancelCopy() throws SQLException {
        this.op.cancelCopy();
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
    public void flushCopy() throws SQLException {
        this.op.flushCopy();
    }
    
    @Override
    public long endCopy() throws SQLException {
        if (this.at > 0) {
            this.op.writeToCopy(this.copyBuffer, 0, this.at);
        }
        this.op.endCopy();
        return this.getHandledRowCount();
    }
    
    @Override
    public long getHandledRowCount() {
        return this.op.getHandledRowCount();
    }
}
