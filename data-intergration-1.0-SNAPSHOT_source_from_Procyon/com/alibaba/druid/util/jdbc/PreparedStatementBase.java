// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.ParameterMetaData;
import java.net.URL;
import java.util.Calendar;
import java.sql.ResultSetMetaData;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.io.Reader;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import com.alibaba.druid.mock.MockResultSetMetaData;
import com.alibaba.druid.mock.MockParameterMetaData;
import java.util.List;
import java.sql.PreparedStatement;

public abstract class PreparedStatementBase extends StatementBase implements PreparedStatement
{
    private List<Object> parameters;
    private MockParameterMetaData metadata;
    private MockResultSetMetaData resultSetMetaData;
    
    public PreparedStatementBase(final Connection connection) {
        super(connection);
        this.parameters = new ArrayList<Object>();
        this.metadata = new MockParameterMetaData();
        this.resultSetMetaData = new MockResultSetMetaData();
    }
    
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        this.parameters.add(parameterIndex - 1, null);
    }
    
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        for (int i = this.parameters.size(); i < parameterIndex; ++i) {
            this.parameters.add(null);
        }
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void clearParameters() throws SQLException {
        this.checkOpen();
        this.parameters.clear();
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void addBatch() throws SQLException {
        this.checkOpen();
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
    
    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkOpen();
        return this.resultSetMetaData;
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.parameters.add(parameterIndex - 1, null);
    }
    
    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        this.checkOpen();
        return this.metadata;
    }
    
    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setNString(final int parameterIndex, final String value) throws SQLException {
        this.parameters.add(parameterIndex - 1, value);
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, value);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        this.parameters.add(parameterIndex - 1, value);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader value, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, value);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, inputStream);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
    
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        this.parameters.add(parameterIndex - 1, xmlObject);
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.parameters.add(parameterIndex - 1, x);
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        this.parameters.add(parameterIndex - 1, value);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        this.parameters.add(parameterIndex - 1, inputStream);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.parameters.add(parameterIndex - 1, reader);
    }
}
