// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc2.AbstractJdbc2Array;

public abstract class AbstractJdbc4Array extends AbstractJdbc2Array
{
    public AbstractJdbc4Array(final BaseConnection connection, final int oid, final byte[] fieldBytes) throws SQLException {
        super(connection, oid, fieldBytes);
    }
    
    public AbstractJdbc4Array(final BaseConnection connection, final int oid, final String fieldString) throws SQLException {
        super(connection, oid, fieldString);
    }
    
    public void free() throws SQLException {
        this.connection = null;
        this.fieldString = null;
        this.fieldBytes = null;
        this.arrayList = null;
    }
}
