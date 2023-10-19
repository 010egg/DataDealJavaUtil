// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.Statement;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.util.Map;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;

public class CallableStatementProxyImpl extends PreparedStatementProxyImpl implements CallableStatementProxy
{
    private final CallableStatement statement;
    
    public CallableStatementProxyImpl(final ConnectionProxy connection, final CallableStatement statement, final String sql, final long id) {
        super(connection, statement, sql, id);
        this.statement = statement;
    }
    
    @Override
    public CallableStatement getRawObject() {
        return this.statement;
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterIndex, sqlType);
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterIndex, sqlType, scale);
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        return this.createChain().callableStatement_wasNull(this);
    }
    
    @Override
    public String getString(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getString(this, parameterIndex);
    }
    
    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getBoolean(this, parameterIndex);
    }
    
    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getByte(this, parameterIndex);
    }
    
    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getShort(this, parameterIndex);
    }
    
    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getInt(this, parameterIndex);
    }
    
    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getLong(this, parameterIndex);
    }
    
    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getFloat(this, parameterIndex);
    }
    
    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getDouble(this, parameterIndex);
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        return this.createChain().callableStatement_getBigDecimal(this, parameterIndex, scale);
    }
    
    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getBytes(this, parameterIndex);
    }
    
    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getDate(this, parameterIndex);
    }
    
    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getTime(this, parameterIndex);
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getTimestamp(this, parameterIndex);
    }
    
    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getObject(this, parameterIndex);
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getBigDecimal(this, parameterIndex);
    }
    
    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        return this.createChain().callableStatement_getObject(this, parameterIndex, map);
    }
    
    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getRef(this, parameterIndex);
    }
    
    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getBlob(this, parameterIndex);
    }
    
    @Override
    public Clob getClob(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getClob(this, parameterIndex);
    }
    
    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getArray(this, parameterIndex);
    }
    
    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getDate(this, parameterIndex, cal);
    }
    
    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getTime(this, parameterIndex, cal);
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getTimestamp(this, parameterIndex, cal);
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterName, sqlType);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterName, sqlType, scale);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        this.createChain().callableStatement_registerOutParameter(this, parameterName, sqlType, typeName);
    }
    
    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getURL(this, parameterIndex);
    }
    
    @Override
    public void setURL(final String parameterName, final URL val) throws SQLException {
        this.createChain().callableStatement_setURL(this, parameterName, val);
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {
        this.createChain().callableStatement_setNull(this, parameterName, sqlType);
    }
    
    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {
        this.createChain().callableStatement_setBoolean(this, parameterName, x);
    }
    
    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {
        this.createChain().callableStatement_setByte(this, parameterName, x);
    }
    
    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {
        this.createChain().callableStatement_setShort(this, parameterName, x);
    }
    
    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {
        this.createChain().callableStatement_setInt(this, parameterName, x);
    }
    
    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {
        this.createChain().callableStatement_setLong(this, parameterName, x);
    }
    
    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {
        this.createChain().callableStatement_setFloat(this, parameterName, x);
    }
    
    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {
        this.createChain().callableStatement_setDouble(this, parameterName, x);
    }
    
    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
        this.createChain().callableStatement_setBigDecimal(this, parameterName, x);
    }
    
    @Override
    public void setString(final String parameterName, final String x) throws SQLException {
        this.createChain().callableStatement_setString(this, parameterName, x);
    }
    
    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {
        this.createChain().callableStatement_setBytes(this, parameterName, x);
    }
    
    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {
        this.createChain().callableStatement_setDate(this, parameterName, x);
    }
    
    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {
        this.createChain().callableStatement_setTime(this, parameterName, x);
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
        this.createChain().callableStatement_setTimestamp(this, parameterName, x);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        this.createChain().callableStatement_setAsciiStream(this, parameterName, x, length);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        this.createChain().callableStatement_setBinaryStream(this, parameterName, x, length);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        this.createChain().callableStatement_setObject(this, parameterName, x, targetSqlType, scale);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        this.createChain().callableStatement_setObject(this, parameterName, x, targetSqlType);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        this.createChain().callableStatement_setObject(this, parameterName, x);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
        this.createChain().callableStatement_setCharacterStream(this, parameterName, reader, length);
    }
    
    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        this.createChain().callableStatement_setDate(this, parameterName, x, cal);
    }
    
    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        this.createChain().callableStatement_setTime(this, parameterName, x, cal);
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        this.createChain().callableStatement_setTimestamp(this, parameterName, x, cal);
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        this.createChain().callableStatement_setNull(this, parameterName, sqlType, typeName);
    }
    
    @Override
    public String getString(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getString(this, parameterName);
    }
    
    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getBoolean(this, parameterName);
    }
    
    @Override
    public byte getByte(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getByte(this, parameterName);
    }
    
    @Override
    public short getShort(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getShort(this, parameterName);
    }
    
    @Override
    public int getInt(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getInt(this, parameterName);
    }
    
    @Override
    public long getLong(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getLong(this, parameterName);
    }
    
    @Override
    public float getFloat(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getFloat(this, parameterName);
    }
    
    @Override
    public double getDouble(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getDouble(this, parameterName);
    }
    
    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getBytes(this, parameterName);
    }
    
    @Override
    public Date getDate(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getDate(this, parameterName);
    }
    
    @Override
    public Time getTime(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getTime(this, parameterName);
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getTimestamp(this, parameterName);
    }
    
    @Override
    public Object getObject(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getObject(this, parameterName);
    }
    
    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getBigDecimal(this, parameterName);
    }
    
    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        return this.createChain().callableStatement_getObject(this, parameterName, map);
    }
    
    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getRef(this, parameterName);
    }
    
    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getBlob(this, parameterName);
    }
    
    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getClob(this, parameterName);
    }
    
    @Override
    public Array getArray(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getArray(this, parameterName);
    }
    
    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getDate(this, parameterName, cal);
    }
    
    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getTime(this, parameterName, cal);
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        return this.createChain().callableStatement_getTimestamp(this, parameterName, cal);
    }
    
    @Override
    public URL getURL(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getURL(this, parameterName);
    }
    
    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getRowId(this, parameterIndex);
    }
    
    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getRowId(this, parameterName);
    }
    
    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        this.createChain().callableStatement_setRowId(this, parameterName, x);
    }
    
    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {
        this.createChain().callableStatement_setNString(this, parameterName, value);
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        this.createChain().callableStatement_setNCharacterStream(this, parameterName, value, length);
    }
    
    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        this.createChain().callableStatement_setNClob(this, parameterName, value);
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        this.createChain().callableStatement_setClob(this, parameterName, reader, length);
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        this.createChain().callableStatement_setBlob(this, parameterName, inputStream, length);
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        this.createChain().callableStatement_setNClob(this, parameterName, reader, length);
    }
    
    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getNClob(this, parameterIndex);
    }
    
    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getNClob(this, parameterName);
    }
    
    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        this.createChain().callableStatement_setSQLXML(this, parameterName, xmlObject);
    }
    
    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getSQLXML(this, parameterIndex);
    }
    
    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getSQLXML(this, parameterName);
    }
    
    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getNString(this, parameterIndex);
    }
    
    @Override
    public String getNString(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getNString(this, parameterName);
    }
    
    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getNCharacterStream(this, parameterIndex);
    }
    
    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getNCharacterStream(this, parameterName);
    }
    
    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        return this.createChain().callableStatement_getCharacterStream(this, parameterIndex);
    }
    
    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        return this.createChain().callableStatement_getCharacterStream(this, parameterName);
    }
    
    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        this.createChain().callableStatement_setBlob(this, parameterName, x);
    }
    
    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        this.createChain().callableStatement_setClob(this, parameterName, x);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        this.createChain().callableStatement_setAsciiStream(this, parameterName, x, length);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        this.createChain().callableStatement_setBinaryStream(this, parameterName, x, length);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
        this.createChain().callableStatement_setCharacterStream(this, parameterName, reader, length);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
        this.createChain().callableStatement_setAsciiStream(this, parameterName, x);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
        this.createChain().callableStatement_setBinaryStream(this, parameterName, x);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
        this.createChain().callableStatement_setCharacterStream(this, parameterName, reader);
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        this.createChain().callableStatement_setNCharacterStream(this, parameterName, value);
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        this.createChain().callableStatement_setClob(this, parameterName, reader);
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        this.createChain().callableStatement_setBlob(this, parameterName, inputStream);
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        this.createChain().callableStatement_setNClob(this, parameterName, reader);
    }
    
    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        return this.statement.getObject(parameterIndex, type);
    }
    
    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        return this.statement.getObject(parameterName, type);
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == PreparedStatement.class || iface == CallableStatement.class) {
            return (T)this.statement;
        }
        return super.unwrap(iface);
    }
}
