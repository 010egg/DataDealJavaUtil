// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc3.AbstractJdbc3ParameterMetaData;

public abstract class AbstractJdbc4ParameterMetaData extends AbstractJdbc3ParameterMetaData
{
    public AbstractJdbc4ParameterMetaData(final BaseConnection connection, final int[] oids) {
        super(connection, oids);
    }
    
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }
    
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }
}
