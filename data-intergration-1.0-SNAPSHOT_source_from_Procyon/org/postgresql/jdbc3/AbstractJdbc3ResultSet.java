// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import org.postgresql.Driver;
import java.net.URL;
import java.sql.SQLException;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Query;
import org.postgresql.jdbc2.AbstractJdbc2ResultSet;

public abstract class AbstractJdbc3ResultSet extends AbstractJdbc2ResultSet
{
    public AbstractJdbc3ResultSet(final Query originalQuery, final BaseStatement statement, final Field[] fields, final List tuples, final ResultCursor cursor, final int maxRows, final int maxFieldSize, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(originalQuery, statement, fields, tuples, cursor, maxRows, maxFieldSize, rsType, rsConcurrency);
    }
    
    @Override
    protected Object internalGetObject(final int columnIndex, final Field field) throws SQLException {
        switch (this.getSQLType(columnIndex)) {
            case 16: {
                return new Boolean(this.getBoolean(columnIndex));
            }
            default: {
                return super.internalGetObject(columnIndex, field);
            }
        }
    }
    
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getURL(int)");
    }
    
    @Override
    public URL getURL(final String columnName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getURL(String)");
    }
    
    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRef(int,Ref)");
    }
    
    @Override
    public void updateRef(final String columnName, final Ref x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRef(String,Ref)");
    }
    
    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int,Blob)");
    }
    
    @Override
    public void updateBlob(final String columnName, final Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(String,Blob)");
    }
    
    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int,Clob)");
    }
    
    @Override
    public void updateClob(final String columnName, final Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(String,Clob)");
    }
    
    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateArray(final String columnName, final Array x) throws SQLException {
        this.updateArray(this.findColumn(columnName), x);
    }
}
