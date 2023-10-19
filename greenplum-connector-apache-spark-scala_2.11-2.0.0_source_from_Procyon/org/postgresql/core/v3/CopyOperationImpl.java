// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.copy.CopyOperation;

public abstract class CopyOperationImpl implements CopyOperation
{
    QueryExecutorImpl queryExecutor;
    int rowFormat;
    int[] fieldFormats;
    long handledRowCount;
    
    public CopyOperationImpl() {
        this.handledRowCount = -1L;
    }
    
    void init(final QueryExecutorImpl q, final int fmt, final int[] fmts) {
        this.queryExecutor = q;
        this.rowFormat = fmt;
        this.fieldFormats = fmts;
    }
    
    @Override
    public void cancelCopy() throws SQLException {
        this.queryExecutor.cancelCopy(this);
    }
    
    @Override
    public int getFieldCount() {
        return this.fieldFormats.length;
    }
    
    @Override
    public int getFieldFormat(final int field) {
        return this.fieldFormats[field];
    }
    
    @Override
    public int getFormat() {
        return this.rowFormat;
    }
    
    @Override
    public boolean isActive() {
        synchronized (this.queryExecutor) {
            return this.queryExecutor.hasLock(this);
        }
    }
    
    public void handleCommandStatus(final String status) throws PSQLException {
        if (status.startsWith("COPY")) {
            final int i = status.lastIndexOf(32);
            this.handledRowCount = ((i > 3) ? Long.parseLong(status.substring(i + 1)) : -1L);
            return;
        }
        throw new PSQLException(GT.tr("CommandComplete expected COPY but got: " + status, new Object[0]), PSQLState.COMMUNICATION_ERROR);
    }
    
    protected abstract void handleCopydata(final byte[] p0) throws PSQLException;
    
    @Override
    public long getHandledRowCount() {
        return this.handledRowCount;
    }
}
