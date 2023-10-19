// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds;

import javax.sql.ConnectionEvent;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.PooledConnection;
import org.postgresql.ds.jdbc4.AbstractJdbc4PooledConnection;

public class PGPooledConnection extends AbstractJdbc4PooledConnection implements PooledConnection
{
    public PGPooledConnection(final Connection con, final boolean autoCommit, final boolean isXA) {
        super(con, autoCommit, isXA);
    }
    
    public PGPooledConnection(final Connection con, final boolean autoCommit) {
        this(con, autoCommit, false);
    }
    
    @Override
    protected ConnectionEvent createConnectionEvent(final SQLException sqle) {
        return new ConnectionEvent(this, sqle);
    }
}
