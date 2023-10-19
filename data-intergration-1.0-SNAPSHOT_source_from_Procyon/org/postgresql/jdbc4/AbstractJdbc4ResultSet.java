// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLXML;
import java.io.InputStream;
import java.io.Reader;
import java.sql.NClob;
import org.postgresql.Driver;
import java.sql.RowId;
import java.sql.SQLException;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Query;
import org.postgresql.jdbc3g.AbstractJdbc3gResultSet;

abstract class AbstractJdbc4ResultSet extends AbstractJdbc3gResultSet
{
    AbstractJdbc4ResultSet(final Query originalQuery, final BaseStatement statement, final Field[] fields, final List tuples, final ResultCursor cursor, final int maxRows, final int maxFieldSize, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(originalQuery, statement, fields, tuples, cursor, maxRows, maxFieldSize, rsType, rsConcurrency, rsHoldability);
    }
    
    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowId(int)");
    }
    
    @Override
    public RowId getRowId(final String columnName) throws SQLException {
        return this.getRowId(this.findColumn(columnName));
    }
    
    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRowId(int, RowId)");
    }
    
    @Override
    public void updateRowId(final String columnName, final RowId x) throws SQLException {
        this.updateRowId(this.findColumn(columnName), x);
    }
    
    @Override
    public int getHoldability() throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getHoldability()");
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.rows == null;
    }
    
    @Override
    public void close() throws SQLException {
        try {
            super.close();
        }
        finally {
            ((AbstractJdbc4Statement)this.statement).checkCompletion();
        }
    }
    
    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNString(int, String)");
    }
    
    @Override
    public void updateNString(final String columnName, final String nString) throws SQLException {
        this.updateNString(this.findColumn(columnName), nString);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, NClob)");
    }
    
    @Override
    public void updateNClob(final String columnName, final NClob nClob) throws SQLException {
        this.updateNClob(this.findColumn(columnName), nClob);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader)");
    }
    
    @Override
    public void updateNClob(final String columnName, final Reader reader) throws SQLException {
        this.updateNClob(this.findColumn(columnName), reader);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader, long)");
    }
    
    @Override
    public void updateNClob(final String columnName, final Reader reader, final long length) throws SQLException {
        this.updateNClob(this.findColumn(columnName), reader, length);
    }
    
    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNClob(int)");
    }
    
    @Override
    public NClob getNClob(final String columnName) throws SQLException {
        return this.getNClob(this.findColumn(columnName));
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream, long)");
    }
    
    @Override
    public void updateBlob(final String columnName, final InputStream inputStream, final long length) throws SQLException {
        this.updateBlob(this.findColumn(columnName), inputStream, length);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream)");
    }
    
    @Override
    public void updateBlob(final String columnName, final InputStream inputStream) throws SQLException {
        this.updateBlob(this.findColumn(columnName), inputStream);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader, long)");
    }
    
    @Override
    public void updateClob(final String columnName, final Reader reader, final long length) throws SQLException {
        this.updateClob(this.findColumn(columnName), reader, length);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader)");
    }
    
    @Override
    public void updateClob(final String columnName, final Reader reader) throws SQLException {
        this.updateClob(this.findColumn(columnName), reader);
    }
    
    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        final String data = this.getString(columnIndex);
        if (data == null) {
            return null;
        }
        return new Jdbc4SQLXML(this.connection, data);
    }
    
    @Override
    public SQLXML getSQLXML(final String columnName) throws SQLException {
        return this.getSQLXML(this.findColumn(columnName));
    }
    
    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
        this.updateValue(columnIndex, xmlObject);
    }
    
    @Override
    public void updateSQLXML(final String columnName, final SQLXML xmlObject) throws SQLException {
        this.updateSQLXML(this.findColumn(columnName), xmlObject);
    }
    
    @Override
    public String getNString(final int columnIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNString(int)");
    }
    
    @Override
    public String getNString(final String columnName) throws SQLException {
        return this.getNString(this.findColumn(columnName));
    }
    
    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
    }
    
    @Override
    public Reader getNCharacterStream(final String columnName) throws SQLException {
        return this.getNCharacterStream(this.findColumn(columnName));
    }
    
    public void updateNCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, int)");
    }
    
    public void updateNCharacterStream(final String columnName, final Reader x, final int length) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x, length);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader)");
    }
    
    @Override
    public void updateNCharacterStream(final String columnName, final Reader x) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, long)");
    }
    
    @Override
    public void updateNCharacterStream(final String columnName, final Reader x, final long length) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x, length);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateCharaceterStream(int, Reader, long)");
    }
    
    @Override
    public void updateCharacterStream(final String columnName, final Reader reader, final long length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader, length);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateCharaceterStream(int, Reader)");
    }
    
    @Override
    public void updateCharacterStream(final String columnName, final Reader reader) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream, long)");
    }
    
    @Override
    public void updateBinaryStream(final String columnName, final InputStream inputStream, final long length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), inputStream, length);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream)");
    }
    
    @Override
    public void updateBinaryStream(final String columnName, final InputStream inputStream) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), inputStream);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream, long)");
    }
    
    @Override
    public void updateAsciiStream(final String columnName, final InputStream inputStream, final long length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), inputStream, length);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream)");
    }
    
    @Override
    public void updateAsciiStream(final String columnName, final InputStream inputStream) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), inputStream);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }
    
    @Override
    protected Object internalGetObject(final int columnIndex, final Field field) throws SQLException {
        switch (this.getSQLType(columnIndex)) {
            case 2009: {
                return this.getSQLXML(columnIndex);
            }
            default: {
                return super.internalGetObject(columnIndex, field);
            }
        }
    }
    
    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(int, Class<T>)");
    }
    
    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        return this.getObject(this.findColumn(columnLabel), type);
    }
}
