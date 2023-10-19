// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc4;

import java.sql.SQLFeatureNotSupportedException;
import org.postgresql.Driver;
import java.util.logging.Logger;
import javax.sql.StatementEventListener;
import java.sql.Connection;
import org.postgresql.ds.jdbc23.AbstractJdbc23PooledConnection;

public abstract class AbstractJdbc4PooledConnection extends AbstractJdbc23PooledConnection
{
    public AbstractJdbc4PooledConnection(final Connection con, final boolean autoCommit, final boolean isXA) {
        super(con, autoCommit, isXA);
    }
    
    public void removeStatementEventListener(final StatementEventListener listener) {
    }
    
    public void addStatementEventListener(final StatementEventListener listener) {
    }
    
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw Driver.notImplemented(this.getClass(), "getParentLogger()");
    }
}
