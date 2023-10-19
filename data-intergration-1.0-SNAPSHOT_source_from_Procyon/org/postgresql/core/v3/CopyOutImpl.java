// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import java.sql.SQLException;
import org.postgresql.copy.CopyOut;

public class CopyOutImpl extends CopyOperationImpl implements CopyOut
{
    private byte[] currentDataRow;
    
    @Override
    public byte[] readFromCopy() throws SQLException {
        this.currentDataRow = null;
        this.queryExecutor.readFromCopy(this);
        return this.currentDataRow;
    }
    
    void handleCopydata(final byte[] data) {
        this.currentDataRow = data;
    }
}
