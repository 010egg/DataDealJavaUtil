// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.Field;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc2.AbstractJdbc2ResultSetMetaData;

abstract class AbstractJdbc4ResultSetMetaData extends AbstractJdbc2ResultSetMetaData
{
    public AbstractJdbc4ResultSetMetaData(final BaseConnection connection, final Field[] fields) {
        super(connection, fields);
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
