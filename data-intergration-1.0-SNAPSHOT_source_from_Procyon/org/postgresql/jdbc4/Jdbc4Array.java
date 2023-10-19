// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import org.postgresql.jdbc2.ArrayAssistant;
import org.postgresql.jdbc2.ArrayAssistantRegistry;
import org.postgresql.jdbc4.array.UUIDArrayAssistant;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import java.sql.Array;

public class Jdbc4Array extends AbstractJdbc4Array implements Array
{
    public Jdbc4Array(final BaseConnection conn, final int oid, final String fieldString) throws SQLException {
        super(conn, oid, fieldString);
    }
    
    public Jdbc4Array(final BaseConnection conn, final int oid, final byte[] fieldBytes) throws SQLException {
        super(conn, oid, fieldBytes);
    }
    
    @Override
    public Object getArray(final Map<String, Class<?>> map) throws SQLException {
        return this.getArrayImpl(map);
    }
    
    @Override
    public Object getArray(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return this.getArrayImpl(index, count, map);
    }
    
    @Override
    public ResultSet getResultSet(final Map<String, Class<?>> map) throws SQLException {
        return this.getResultSetImpl(map);
    }
    
    @Override
    public ResultSet getResultSet(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return this.getResultSetImpl(index, count, map);
    }
    
    static {
        ArrayAssistantRegistry.register(2950, new UUIDArrayAssistant());
        ArrayAssistantRegistry.register(2951, new UUIDArrayAssistant());
    }
}
