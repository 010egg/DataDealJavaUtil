// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc2.AbstractJdbc2Blob;

public abstract class AbstractJdbc3Blob extends AbstractJdbc2Blob
{
    public AbstractJdbc3Blob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
    
    public synchronized int setBytes(final long pos, final byte[] bytes) throws SQLException {
        return this.setBytes(pos, bytes, 0, bytes.length);
    }
    
    public synchronized int setBytes(final long pos, final byte[] bytes, final int offset, final int len) throws SQLException {
        this.assertPosition(pos);
        this.getLo(true).seek((int)(pos - 1L));
        this.getLo(true).write(bytes, offset, len);
        return len;
    }
}
