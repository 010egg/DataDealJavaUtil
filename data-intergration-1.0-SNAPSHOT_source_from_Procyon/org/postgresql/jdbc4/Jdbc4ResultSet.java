// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.RowId;
import java.sql.NClob;
import java.sql.SQLXML;
import java.io.Reader;
import java.io.InputStream;
import java.util.Map;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Query;
import java.sql.ResultSet;

public class Jdbc4ResultSet extends AbstractJdbc4ResultSet implements ResultSet
{
    Jdbc4ResultSet(final Query originalQuery, final BaseStatement statement, final Field[] fields, final List tuples, final ResultCursor cursor, final int maxRows, final int maxFieldSize, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(originalQuery, statement, fields, tuples, cursor, maxRows, maxFieldSize, rsType, rsConcurrency, rsHoldability);
    }
    
    @Override
    protected ResultSetMetaData createMetaData() throws SQLException {
        return new Jdbc4ResultSetMetaData(this.connection, this.fields);
    }
    
    @Override
    protected Clob makeClob(final long oid) throws SQLException {
        return new Jdbc4Clob(this.connection, oid);
    }
    
    @Override
    protected Blob makeBlob(final long oid) throws SQLException {
        return new Jdbc4Blob(this.connection, oid);
    }
    
    @Override
    protected Array makeArray(final int oid, final byte[] value) throws SQLException {
        return new Jdbc4Array(this.connection, oid, value);
    }
    
    @Override
    protected Array makeArray(final int oid, final String value) throws SQLException {
        return new Jdbc4Array(this.connection, oid, value);
    }
    
    @Override
    public Object getObject(final String s, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(s, map);
    }
    
    @Override
    public Object getObject(final int i, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(i, map);
    }
}
