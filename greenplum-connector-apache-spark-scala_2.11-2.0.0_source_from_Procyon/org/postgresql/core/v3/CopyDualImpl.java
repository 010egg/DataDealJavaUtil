// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ByteStreamWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import org.postgresql.copy.CopyDual;

public class CopyDualImpl extends CopyOperationImpl implements CopyDual
{
    private Queue<byte[]> received;
    
    public CopyDualImpl() {
        this.received = new LinkedList<byte[]>();
    }
    
    @Override
    public void writeToCopy(final byte[] data, final int off, final int siz) throws SQLException {
        this.queryExecutor.writeToCopy(this, data, off, siz);
    }
    
    @Override
    public void writeToCopy(final ByteStreamWriter from) throws SQLException {
        this.queryExecutor.writeToCopy(this, from);
    }
    
    @Override
    public void flushCopy() throws SQLException {
        this.queryExecutor.flushCopy(this);
    }
    
    @Override
    public long endCopy() throws SQLException {
        return this.queryExecutor.endCopy(this);
    }
    
    @Override
    public byte[] readFromCopy() throws SQLException {
        return this.readFromCopy(true);
    }
    
    @Override
    public byte[] readFromCopy(final boolean block) throws SQLException {
        if (this.received.isEmpty()) {
            this.queryExecutor.readFromCopy(this, block);
        }
        return this.received.poll();
    }
    
    @Override
    public void handleCommandStatus(final String status) throws PSQLException {
    }
    
    @Override
    protected void handleCopydata(final byte[] data) {
        this.received.add(data);
    }
}
