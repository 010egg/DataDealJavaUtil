// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import org.postgresql.Driver;
import java.io.Reader;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc3.AbstractJdbc3Clob;

public abstract class AbstractJdbc4Clob extends AbstractJdbc3Clob
{
    public AbstractJdbc4Clob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
    
    public synchronized Reader getCharacterStream(final long pos, final long length) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "getCharacterStream(long, long)");
    }
}
