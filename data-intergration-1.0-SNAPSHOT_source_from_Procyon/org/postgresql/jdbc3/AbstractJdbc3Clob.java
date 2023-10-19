// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import java.io.Writer;
import java.io.OutputStream;
import org.postgresql.Driver;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc2.AbstractJdbc2Clob;

public abstract class AbstractJdbc3Clob extends AbstractJdbc2Clob
{
    public AbstractJdbc3Clob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
    
    public synchronized int setString(final long pos, final String str) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "setString(long,str)");
    }
    
    public synchronized int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "setString(long,String,int,int)");
    }
    
    public synchronized OutputStream setAsciiStream(final long pos) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(long)");
    }
    
    public synchronized Writer setCharacterStream(final long pos) throws SQLException {
        this.checkFreed();
        throw Driver.notImplemented(this.getClass(), "setCharacteStream(long)");
    }
}
