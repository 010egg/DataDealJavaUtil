// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import org.postgresql.jdbc2.ResultWrapper;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.NClob;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.io.InputStream;
import java.io.Reader;
import org.postgresql.Driver;
import java.sql.RowId;
import java.sql.SQLXML;
import java.sql.SQLException;
import org.postgresql.jdbc3.AbstractJdbc3Connection;
import org.postgresql.jdbc3g.AbstractJdbc3gStatement;

abstract class AbstractJdbc4Statement extends AbstractJdbc3gStatement
{
    private boolean poolable;
    private boolean closeOnCompletion;
    
    AbstractJdbc4Statement(final Jdbc4Connection c, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(c, rsType, rsConcurrency, rsHoldability);
        this.closeOnCompletion = false;
        this.poolable = true;
    }
    
    public AbstractJdbc4Statement(final Jdbc4Connection connection, final String sql, final boolean isCallable, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, sql, isCallable, rsType, rsConcurrency, rsHoldability);
        this.closeOnCompletion = false;
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        if (x instanceof SQLXML) {
            this.setSQLXML(parameterIndex, (SQLXML)x);
        }
        else {
            super.setObject(parameterIndex, x);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(parameterIndex, targetSqlType);
            return;
        }
        switch (targetSqlType) {
            case 2009: {
                if (x instanceof SQLXML) {
                    this.setSQLXML(parameterIndex, (SQLXML)x);
                    break;
                }
                this.setSQLXML(parameterIndex, new Jdbc4SQLXML(this.connection, x.toString()));
                break;
            }
            default: {
                super.setObject(parameterIndex, x, targetSqlType, scale);
                break;
            }
        }
    }
    
    @Override
    public void setNull(int parameterIndex, final int targetSqlType) throws SQLException {
        this.checkClosed();
        switch (targetSqlType) {
            case 2009: {
                final int oid = 142;
                if (this.adjustIndex) {
                    --parameterIndex;
                }
                this.preparedParameters.setNull(parameterIndex, oid);
            }
            default: {
                super.setNull(parameterIndex, targetSqlType);
            }
        }
    }
    
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setRowId(int, RowId)");
    }
    
    public void setNString(final int parameterIndex, final String value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNString(int, String)");
    }
    
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(int, Reader, long)");
    }
    
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(int, Reader)");
    }
    
    public void setCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(int, Reader, long)");
    }
    
    public void setCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(int, Reader)");
    }
    
    public void setBinaryStream(final int parameterIndex, final InputStream value, final long length) throws SQLException {
        if (length > 2147483647L) {
            throw new PSQLException(GT.tr("Object is too large to send over the protocol."), PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE);
        }
        this.preparedParameters.setBytea(parameterIndex, value, (int)length);
    }
    
    public void setBinaryStream(final int parameterIndex, final InputStream value) throws SQLException {
        this.preparedParameters.setBytea(parameterIndex, value);
    }
    
    public void setAsciiStream(final int parameterIndex, final InputStream value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(int, InputStream, long)");
    }
    
    public void setAsciiStream(final int parameterIndex, final InputStream value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(int, InputStream)");
    }
    
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(int, NClob)");
    }
    
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(int, Reader, long)");
    }
    
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(int, Reader)");
    }
    
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        this.checkClosed();
        if (inputStream == null) {
            this.setNull(parameterIndex, 2004);
            return;
        }
        if (length < 0L) {
            throw new PSQLException(GT.tr("Invalid stream length {0}.", new Long(length)), PSQLState.INVALID_PARAMETER_VALUE);
        }
        final long oid = this.createBlob(parameterIndex, inputStream, length);
        this.setLong(parameterIndex, oid);
    }
    
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        this.checkClosed();
        if (inputStream == null) {
            this.setNull(parameterIndex, 2004);
            return;
        }
        final long oid = this.createBlob(parameterIndex, inputStream, -1L);
        this.setLong(parameterIndex, oid);
    }
    
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(int, Reader, long)");
    }
    
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(int, Reader)");
    }
    
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        this.checkClosed();
        if (xmlObject == null || xmlObject.getString() == null) {
            this.setNull(parameterIndex, 2009);
        }
        else {
            this.setString(parameterIndex, xmlObject.getString(), 142);
        }
    }
    
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        this.checkClosed();
        this.poolable = poolable;
    }
    
    @Override
    public boolean isPoolable() throws SQLException {
        this.checkClosed();
        return this.poolable;
    }
    
    public RowId getRowId(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowId(int)");
    }
    
    public RowId getRowId(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowId(String)");
    }
    
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setRowId(String, RowId)");
    }
    
    public void setNString(final String parameterName, final String value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNString(String, String)");
    }
    
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader, long)");
    }
    
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader)");
    }
    
    public void setCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader, long)");
    }
    
    public void setCharacterStream(final String parameterName, final Reader value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader)");
    }
    
    public void setBinaryStream(final String parameterName, final InputStream value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream, long)");
    }
    
    public void setBinaryStream(final String parameterName, final InputStream value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream)");
    }
    
    public void setAsciiStream(final String parameterName, final InputStream value, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream, long)");
    }
    
    public void setAsciiStream(final String parameterName, final InputStream value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream)");
    }
    
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, NClob)");
    }
    
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Reader, long)");
    }
    
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Reader)");
    }
    
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream, long)");
    }
    
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream)");
    }
    
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader, long)");
    }
    
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader)");
    }
    
    public NClob getNClob(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNClob(int)");
    }
    
    public NClob getNClob(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNClob(String)");
    }
    
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setSQLXML(String, SQLXML)");
    }
    
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2009, "SQLXML");
        return (SQLXML)this.callResult[parameterIndex - 1];
    }
    
    public SQLXML getSQLXML(final String parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getSQLXML(String)");
    }
    
    public String getNString(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNString(int)");
    }
    
    public String getNString(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNString(String)");
    }
    
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
    }
    
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(String)");
    }
    
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getCharacterStream(int)");
    }
    
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getCharacterStream(String)");
    }
    
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setBlob(String, Blob)");
    }
    
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setClob(String, Clob)");
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
    
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw Driver.notImplemented(this.getClass(), "getParentLogger()");
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        this.closeOnCompletion = true;
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.closeOnCompletion;
    }
    
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(int, Class<T>)");
    }
    
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getObject(String, Class<T>)");
    }
    
    protected void checkCompletion() throws SQLException {
        if (!this.closeOnCompletion) {
            return;
        }
        for (ResultWrapper result = this.firstUnclosedResult; result != null; result = result.getNext()) {
            if (result.getResultSet() != null && !result.getResultSet().isClosed()) {
                return;
            }
        }
        this.closeOnCompletion = false;
        try {
            this.close();
        }
        finally {
            this.closeOnCompletion = true;
        }
    }
}
