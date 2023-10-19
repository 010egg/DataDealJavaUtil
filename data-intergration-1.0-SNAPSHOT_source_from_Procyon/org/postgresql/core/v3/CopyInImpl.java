// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import java.sql.SQLException;
import org.postgresql.copy.CopyIn;

public class CopyInImpl extends CopyOperationImpl implements CopyIn
{
    @Override
    public void writeToCopy(final byte[] data, final int off, final int siz) throws SQLException {
        this.queryExecutor.writeToCopy(this, data, off, siz);
    }
    
    @Override
    public void flushCopy() throws SQLException {
        this.queryExecutor.flushCopy(this);
    }
    
    @Override
    public long endCopy() throws SQLException {
        return this.queryExecutor.endCopy(this);
    }
}
