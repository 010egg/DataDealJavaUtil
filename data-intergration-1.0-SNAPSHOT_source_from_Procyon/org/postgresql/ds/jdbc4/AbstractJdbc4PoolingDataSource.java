// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc4;

import java.sql.SQLFeatureNotSupportedException;
import org.postgresql.Driver;
import java.util.logging.Logger;
import java.sql.SQLException;
import org.postgresql.ds.jdbc23.AbstractJdbc23PoolingDataSource;

public abstract class AbstractJdbc4PoolingDataSource extends AbstractJdbc23PoolingDataSource
{
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }
    
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }
    
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw Driver.notImplemented(this.getClass(), "getParentLogger()");
    }
}
