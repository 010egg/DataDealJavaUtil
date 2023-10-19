// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import org.postgresql.largeobject.LargeObject;
import java.io.InputStream;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc3.AbstractJdbc3Blob;

public abstract class AbstractJdbc4Blob extends AbstractJdbc3Blob
{
    public AbstractJdbc4Blob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
    
    public synchronized InputStream getBinaryStream(final long pos, final long length) throws SQLException {
        this.checkFreed();
        final LargeObject subLO = this.getLo(false).copy();
        this.addSubLO(subLO);
        if (pos > 2147483647L) {
            subLO.seek64(pos - 1L, 0);
        }
        else {
            subLO.seek((int)pos - 1, 0);
        }
        return subLO.getInputStream(length);
    }
}
