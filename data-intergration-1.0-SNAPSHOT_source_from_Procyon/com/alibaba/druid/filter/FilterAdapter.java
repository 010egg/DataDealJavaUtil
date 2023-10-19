// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter;

import com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxy;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import java.io.Writer;
import java.io.OutputStream;
import com.alibaba.druid.proxy.jdbc.ClobProxy;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import java.sql.Wrapper;
import java.util.concurrent.Executor;
import java.sql.SQLClientInfoException;
import java.sql.Savepoint;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
import java.sql.Struct;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.SQLXML;
import java.sql.RowId;
import java.sql.Ref;
import java.util.Map;
import java.sql.NClob;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Clob;
import java.io.Reader;
import java.sql.Blob;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Array;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.util.Properties;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import javax.management.NotificationBroadcasterSupport;

public abstract class FilterAdapter extends NotificationBroadcasterSupport implements Filter
{
    @Override
    public void init(final DataSourceProxy dataSource) {
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return iface == this.getClass();
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) {
        if (iface == this.getClass()) {
            return (T)this;
        }
        return null;
    }
    
    @Override
    public Array callableStatement_getArray(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getArray(statement, parameterIndex);
    }
    
    @Override
    public Array callableStatement_getArray(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getArray(statement, parameterName);
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getBigDecimal(statement, parameterIndex);
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final int scale) throws SQLException {
        return chain.callableStatement_getBigDecimal(statement, parameterIndex, scale);
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getBigDecimal(statement, parameterName);
    }
    
    @Override
    public Blob callableStatement_getBlob(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getBlob(statement, parameterIndex);
    }
    
    @Override
    public Blob callableStatement_getBlob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getBlob(statement, parameterName);
    }
    
    @Override
    public boolean callableStatement_getBoolean(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getBoolean(statement, parameterIndex);
    }
    
    @Override
    public boolean callableStatement_getBoolean(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getBoolean(statement, parameterName);
    }
    
    @Override
    public byte callableStatement_getByte(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getByte(statement, parameterIndex);
    }
    
    @Override
    public byte callableStatement_getByte(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getByte(statement, parameterName);
    }
    
    @Override
    public byte[] callableStatement_getBytes(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getBytes(statement, parameterIndex);
    }
    
    @Override
    public byte[] callableStatement_getBytes(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getBytes(statement, parameterName);
    }
    
    @Override
    public Reader callableStatement_getCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getCharacterStream(statement, parameterIndex);
    }
    
    @Override
    public Reader callableStatement_getCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getCharacterStream(statement, parameterName);
    }
    
    @Override
    public Clob callableStatement_getClob(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getClob(statement, parameterIndex);
    }
    
    @Override
    public Clob callableStatement_getClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getClob(statement, parameterName);
    }
    
    @Override
    public Date callableStatement_getDate(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getDate(statement, parameterIndex);
    }
    
    @Override
    public Date callableStatement_getDate(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        return chain.callableStatement_getDate(statement, parameterIndex, cal);
    }
    
    @Override
    public Date callableStatement_getDate(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getDate(statement, parameterName);
    }
    
    @Override
    public Date callableStatement_getDate(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        return chain.callableStatement_getDate(statement, parameterName, cal);
    }
    
    @Override
    public double callableStatement_getDouble(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getDouble(statement, parameterIndex);
    }
    
    @Override
    public double callableStatement_getDouble(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getDouble(statement, parameterName);
    }
    
    @Override
    public float callableStatement_getFloat(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getFloat(statement, parameterIndex);
    }
    
    @Override
    public float callableStatement_getFloat(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getFloat(statement, parameterName);
    }
    
    @Override
    public int callableStatement_getInt(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getInt(statement, parameterIndex);
    }
    
    @Override
    public int callableStatement_getInt(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getInt(statement, parameterName);
    }
    
    @Override
    public long callableStatement_getLong(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getLong(statement, parameterIndex);
    }
    
    @Override
    public long callableStatement_getLong(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getLong(statement, parameterName);
    }
    
    @Override
    public Reader callableStatement_getNCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getNCharacterStream(statement, parameterIndex);
    }
    
    @Override
    public Reader callableStatement_getNCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getNCharacterStream(statement, parameterName);
    }
    
    @Override
    public NClob callableStatement_getNClob(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getNClob(statement, parameterIndex);
    }
    
    @Override
    public NClob callableStatement_getNClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getNClob(statement, parameterName);
    }
    
    @Override
    public String callableStatement_getNString(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getNString(statement, parameterIndex);
    }
    
    @Override
    public String callableStatement_getNString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getNString(statement, parameterName);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getObject(statement, parameterIndex);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        return chain.callableStatement_getObject(statement, parameterIndex, map);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getObject(statement, parameterName);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        return chain.callableStatement_getObject(statement, parameterName, map);
    }
    
    @Override
    public Ref callableStatement_getRef(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getRef(statement, parameterIndex);
    }
    
    @Override
    public Ref callableStatement_getRef(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getRef(statement, parameterName);
    }
    
    @Override
    public RowId callableStatement_getRowId(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getRowId(statement, parameterIndex);
    }
    
    @Override
    public RowId callableStatement_getRowId(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getRowId(statement, parameterName);
    }
    
    @Override
    public short callableStatement_getShort(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getShort(statement, parameterIndex);
    }
    
    @Override
    public short callableStatement_getShort(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getShort(statement, parameterName);
    }
    
    @Override
    public SQLXML callableStatement_getSQLXML(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getSQLXML(statement, parameterIndex);
    }
    
    @Override
    public SQLXML callableStatement_getSQLXML(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getSQLXML(statement, parameterName);
    }
    
    @Override
    public String callableStatement_getString(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getString(statement, parameterIndex);
    }
    
    @Override
    public String callableStatement_getString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getString(statement, parameterName);
    }
    
    @Override
    public Time callableStatement_getTime(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getTime(statement, parameterIndex);
    }
    
    @Override
    public Time callableStatement_getTime(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        return chain.callableStatement_getTime(statement, parameterIndex, cal);
    }
    
    @Override
    public Time callableStatement_getTime(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getTime(statement, parameterName);
    }
    
    @Override
    public Time callableStatement_getTime(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        return chain.callableStatement_getTime(statement, parameterName, cal);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getTimestamp(statement, parameterIndex);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        return chain.callableStatement_getTimestamp(statement, parameterIndex, cal);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getTimestamp(statement, parameterName);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        return chain.callableStatement_getTimestamp(statement, parameterName, cal);
    }
    
    @Override
    public URL callableStatement_getURL(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        return chain.callableStatement_getURL(statement, parameterIndex);
    }
    
    @Override
    public URL callableStatement_getURL(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        return chain.callableStatement_getURL(statement, parameterName);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final int sqlType) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterIndex, sqlType);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterIndex, sqlType, scale);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int sqlType) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterName, sqlType);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int sqlType, final int scale) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterName, sqlType, scale);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int sqlType, final String typeName) throws SQLException {
        chain.callableStatement_registerOutParameter(statement, parameterName, sqlType, typeName);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x) throws SQLException {
        chain.callableStatement_setAsciiStream(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x, final int length) throws SQLException {
        chain.callableStatement_setAsciiStream(statement, parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x, final long length) throws SQLException {
        chain.callableStatement_setAsciiStream(statement, parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setBigDecimal(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final BigDecimal x) throws SQLException {
        chain.callableStatement_setBigDecimal(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x) throws SQLException {
        chain.callableStatement_setBinaryStream(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x, final int length) throws SQLException {
        chain.callableStatement_setBinaryStream(statement, parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream x, final long length) throws SQLException {
        chain.callableStatement_setBinaryStream(statement, parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setBlob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Blob x) throws SQLException {
        chain.callableStatement_setBlob(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setBlob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream inputStream) throws SQLException {
        chain.callableStatement_setBlob(statement, parameterName, inputStream);
    }
    
    @Override
    public void callableStatement_setBlob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        chain.callableStatement_setBlob(statement, parameterName, inputStream, length);
    }
    
    @Override
    public void callableStatement_setBoolean(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final boolean x) throws SQLException {
        chain.callableStatement_setBoolean(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setByte(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final byte x) throws SQLException {
        chain.callableStatement_setByte(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setBytes(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final byte[] x) throws SQLException {
        chain.callableStatement_setBytes(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        chain.callableStatement_setCharacterStream(statement, parameterName, reader);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final int length) throws SQLException {
        chain.callableStatement_setCharacterStream(statement, parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        chain.callableStatement_setCharacterStream(statement, parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Clob x) throws SQLException {
        chain.callableStatement_setClob(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        chain.callableStatement_setClob(statement, parameterName, reader);
    }
    
    @Override
    public void callableStatement_setClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        chain.callableStatement_setClob(statement, parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setDate(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Date x) throws SQLException {
        chain.callableStatement_setDate(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setDate(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Date x, final Calendar cal) throws SQLException {
        chain.callableStatement_setDate(statement, parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setDouble(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final double x) throws SQLException {
        chain.callableStatement_setDouble(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setFloat(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final float x) throws SQLException {
        chain.callableStatement_setFloat(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setInt(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int x) throws SQLException {
        chain.callableStatement_setInt(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setLong(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final long x) throws SQLException {
        chain.callableStatement_setLong(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setNCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader value) throws SQLException {
        chain.callableStatement_setNCharacterStream(statement, parameterName, value);
    }
    
    @Override
    public void callableStatement_setNCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader value, final long length) throws SQLException {
        chain.callableStatement_setNCharacterStream(statement, parameterName, value, length);
    }
    
    @Override
    public void callableStatement_setNClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final NClob value) throws SQLException {
        chain.callableStatement_setNClob(statement, parameterName, value);
    }
    
    @Override
    public void callableStatement_setNClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        chain.callableStatement_setNClob(statement, parameterName, reader);
    }
    
    @Override
    public void callableStatement_setNClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        chain.callableStatement_setNClob(statement, parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setNString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final String value) throws SQLException {
        chain.callableStatement_setNString(statement, parameterName, value);
    }
    
    @Override
    public void callableStatement_setNull(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int sqlType) throws SQLException {
        chain.callableStatement_setNull(statement, parameterName, sqlType);
    }
    
    @Override
    public void callableStatement_setNull(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final int sqlType, final String typeName) throws SQLException {
        chain.callableStatement_setNull(statement, parameterName, sqlType, typeName);
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x) throws SQLException {
        chain.callableStatement_setObject(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        chain.callableStatement_setObject(statement, parameterName, x, targetSqlType);
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        chain.callableStatement_setObject(statement, parameterName, x, targetSqlType, scale);
    }
    
    @Override
    public void callableStatement_setRowId(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final RowId x) throws SQLException {
        chain.callableStatement_setRowId(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setShort(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final short x) throws SQLException {
        chain.callableStatement_setShort(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setSQLXML(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final SQLXML xmlObject) throws SQLException {
        chain.callableStatement_setSQLXML(statement, parameterName, xmlObject);
    }
    
    @Override
    public void callableStatement_setString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final String x) throws SQLException {
        chain.callableStatement_setString(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setTime(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Time x) throws SQLException {
        chain.callableStatement_setTime(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setTime(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Time x, final Calendar cal) throws SQLException {
        chain.callableStatement_setTime(statement, parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setTimestamp(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Timestamp x) throws SQLException {
        chain.callableStatement_setTimestamp(statement, parameterName, x);
    }
    
    @Override
    public void callableStatement_setTimestamp(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        chain.callableStatement_setTimestamp(statement, parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setURL(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final URL val) throws SQLException {
        chain.callableStatement_setURL(statement, parameterName, val);
    }
    
    @Override
    public boolean callableStatement_wasNull(final FilterChain chain, final CallableStatementProxy statement) throws SQLException {
        return chain.callableStatement_wasNull(statement);
    }
    
    @Override
    public void connection_clearWarnings(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_clearWarnings(connection);
    }
    
    @Override
    public void connection_close(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_close(connection);
    }
    
    @Override
    public void connection_commit(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_commit(connection);
    }
    
    @Override
    public ConnectionProxy connection_connect(final FilterChain chain, final Properties info) throws SQLException {
        return chain.connection_connect(info);
    }
    
    @Override
    public Array connection_createArrayOf(final FilterChain chain, final ConnectionProxy connection, final String typeName, final Object[] elements) throws SQLException {
        return chain.connection_createArrayOf(connection, typeName, elements);
    }
    
    @Override
    public Blob connection_createBlob(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_createBlob(connection);
    }
    
    @Override
    public Clob connection_createClob(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_createClob(connection);
    }
    
    @Override
    public NClob connection_createNClob(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_createNClob(connection);
    }
    
    @Override
    public SQLXML connection_createSQLXML(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_createSQLXML(connection);
    }
    
    @Override
    public StatementProxy connection_createStatement(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_createStatement(connection);
    }
    
    @Override
    public StatementProxy connection_createStatement(final FilterChain chain, final ConnectionProxy connection, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return chain.connection_createStatement(connection, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public StatementProxy connection_createStatement(final FilterChain chain, final ConnectionProxy connection, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return chain.connection_createStatement(connection, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public Struct connection_createStruct(final FilterChain chain, final ConnectionProxy connection, final String typeName, final Object[] attributes) throws SQLException {
        return chain.connection_createStruct(connection, typeName, attributes);
    }
    
    @Override
    public boolean connection_getAutoCommit(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getAutoCommit(connection);
    }
    
    @Override
    public String connection_getCatalog(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getCatalog(connection);
    }
    
    @Override
    public Properties connection_getClientInfo(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getClientInfo(connection);
    }
    
    @Override
    public String connection_getClientInfo(final FilterChain chain, final ConnectionProxy connection, final String name) throws SQLException {
        return chain.connection_getClientInfo(connection, name);
    }
    
    @Override
    public int connection_getHoldability(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getHoldability(connection);
    }
    
    @Override
    public DatabaseMetaData connection_getMetaData(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getMetaData(connection);
    }
    
    @Override
    public int connection_getTransactionIsolation(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getTransactionIsolation(connection);
    }
    
    @Override
    public Map<String, Class<?>> connection_getTypeMap(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getTypeMap(connection);
    }
    
    @Override
    public SQLWarning connection_getWarnings(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getWarnings(connection);
    }
    
    @Override
    public boolean connection_isClosed(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_isClosed(connection);
    }
    
    @Override
    public boolean connection_isReadOnly(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_isReadOnly(connection);
    }
    
    @Override
    public boolean connection_isValid(final FilterChain chain, final ConnectionProxy connection, final int timeout) throws SQLException {
        return chain.connection_isValid(connection, timeout);
    }
    
    @Override
    public String connection_nativeSQL(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        return chain.connection_nativeSQL(connection, sql);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        return chain.connection_prepareCall(connection, sql);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return chain.connection_prepareCall(connection, sql, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return chain.connection_prepareCall(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        return chain.connection_prepareStatement(connection, sql);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int autoGeneratedKeys) throws SQLException {
        return chain.connection_prepareStatement(connection, sql, autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return chain.connection_prepareStatement(connection, sql, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return chain.connection_prepareStatement(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int[] columnIndexes) throws SQLException {
        return chain.connection_prepareStatement(connection, sql, columnIndexes);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final String[] columnNames) throws SQLException {
        return chain.connection_prepareStatement(connection, sql, columnNames);
    }
    
    @Override
    public void connection_releaseSavepoint(final FilterChain chain, final ConnectionProxy connection, final Savepoint savepoint) throws SQLException {
        chain.connection_releaseSavepoint(connection, savepoint);
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_rollback(connection);
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection, final Savepoint savepoint) throws SQLException {
        chain.connection_rollback(connection, savepoint);
    }
    
    @Override
    public void connection_setAutoCommit(final FilterChain chain, final ConnectionProxy connection, final boolean autoCommit) throws SQLException {
        chain.connection_setAutoCommit(connection, autoCommit);
    }
    
    @Override
    public void connection_setCatalog(final FilterChain chain, final ConnectionProxy connection, final String catalog) throws SQLException {
        chain.connection_setCatalog(connection, catalog);
    }
    
    @Override
    public void connection_setClientInfo(final FilterChain chain, final ConnectionProxy connection, final Properties properties) throws SQLClientInfoException {
        chain.connection_setClientInfo(connection, properties);
    }
    
    @Override
    public void connection_setClientInfo(final FilterChain chain, final ConnectionProxy connection, final String name, final String value) throws SQLClientInfoException {
        chain.connection_setClientInfo(connection, name, value);
    }
    
    @Override
    public void connection_setHoldability(final FilterChain chain, final ConnectionProxy connection, final int holdability) throws SQLException {
        chain.connection_setHoldability(connection, holdability);
    }
    
    @Override
    public void connection_setReadOnly(final FilterChain chain, final ConnectionProxy connection, final boolean readOnly) throws SQLException {
        chain.connection_setReadOnly(connection, readOnly);
    }
    
    @Override
    public Savepoint connection_setSavepoint(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_setSavepoint(connection);
    }
    
    @Override
    public Savepoint connection_setSavepoint(final FilterChain chain, final ConnectionProxy connection, final String name) throws SQLException {
        return chain.connection_setSavepoint(connection, name);
    }
    
    @Override
    public void connection_setTransactionIsolation(final FilterChain chain, final ConnectionProxy connection, final int level) throws SQLException {
        chain.connection_setTransactionIsolation(connection, level);
    }
    
    @Override
    public void connection_setTypeMap(final FilterChain chain, final ConnectionProxy connection, final Map<String, Class<?>> map) throws SQLException {
        chain.connection_setTypeMap(connection, map);
    }
    
    @Override
    public String connection_getSchema(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getSchema(connection);
    }
    
    @Override
    public void connection_setSchema(final FilterChain chain, final ConnectionProxy connection, final String schema) throws SQLException {
        chain.connection_setSchema(connection, schema);
    }
    
    @Override
    public void connection_abort(final FilterChain chain, final ConnectionProxy connection, final Executor executor) throws SQLException {
        chain.connection_abort(connection, executor);
    }
    
    @Override
    public void connection_setNetworkTimeout(final FilterChain chain, final ConnectionProxy connection, final Executor executor, final int milliseconds) throws SQLException {
        chain.connection_setNetworkTimeout(connection, executor, milliseconds);
    }
    
    @Override
    public int connection_getNetworkTimeout(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        return chain.connection_getNetworkTimeout(connection);
    }
    
    @Override
    public boolean isWrapperFor(final FilterChain chain, final Wrapper wrapper, final Class<?> iface) throws SQLException {
        return chain.isWrapperFor(wrapper, iface);
    }
    
    @Override
    public void preparedStatement_addBatch(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        chain.preparedStatement_addBatch(statement);
    }
    
    @Override
    public void preparedStatement_clearParameters(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        chain.preparedStatement_clearParameters(statement);
    }
    
    @Override
    public boolean preparedStatement_execute(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        return chain.preparedStatement_execute(statement);
    }
    
    @Override
    public ResultSetProxy preparedStatement_executeQuery(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        return chain.preparedStatement_executeQuery(statement);
    }
    
    @Override
    public int preparedStatement_executeUpdate(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        return chain.preparedStatement_executeUpdate(statement);
    }
    
    @Override
    public ResultSetMetaData preparedStatement_getMetaData(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        return chain.preparedStatement_getMetaData(statement);
    }
    
    @Override
    public ParameterMetaData preparedStatement_getParameterMetaData(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        return chain.preparedStatement_getParameterMetaData(statement);
    }
    
    @Override
    public void preparedStatement_setArray(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Array x) throws SQLException {
        chain.preparedStatement_setArray(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x) throws SQLException {
        chain.preparedStatement_setAsciiStream(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        chain.preparedStatement_setAsciiStream(statement, parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final long length) throws SQLException {
        chain.preparedStatement_setAsciiStream(statement, parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setBigDecimal(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final BigDecimal x) throws SQLException {
        chain.preparedStatement_setBigDecimal(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x) throws SQLException {
        chain.preparedStatement_setBinaryStream(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        chain.preparedStatement_setBinaryStream(statement, parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final long length) throws SQLException {
        chain.preparedStatement_setBinaryStream(statement, parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setBlob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Blob x) throws SQLException {
        chain.preparedStatement_setBlob(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBlob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream inputStream) throws SQLException {
        chain.preparedStatement_setBlob(statement, parameterIndex, inputStream);
    }
    
    @Override
    public void preparedStatement_setBlob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        chain.preparedStatement_setBlob(statement, parameterIndex, inputStream, length);
    }
    
    @Override
    public void preparedStatement_setBoolean(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final boolean x) throws SQLException {
        chain.preparedStatement_setBoolean(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setByte(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final byte x) throws SQLException {
        chain.preparedStatement_setByte(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBytes(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final byte[] x) throws SQLException {
        chain.preparedStatement_setBytes(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        chain.preparedStatement_setCharacterStream(statement, parameterIndex, reader);
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final int length) throws SQLException {
        chain.preparedStatement_setCharacterStream(statement, parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        chain.preparedStatement_setCharacterStream(statement, parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Clob x) throws SQLException {
        chain.preparedStatement_setClob(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        chain.preparedStatement_setClob(statement, parameterIndex, reader);
    }
    
    @Override
    public void preparedStatement_setClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        chain.preparedStatement_setClob(statement, parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setDate(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Date x) throws SQLException {
        chain.preparedStatement_setDate(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setDate(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        chain.preparedStatement_setDate(statement, parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setDouble(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final double x) throws SQLException {
        chain.preparedStatement_setDouble(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setFloat(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final float x) throws SQLException {
        chain.preparedStatement_setFloat(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setInt(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final int x) throws SQLException {
        chain.preparedStatement_setInt(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setLong(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final long x) throws SQLException {
        chain.preparedStatement_setLong(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setNCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader value) throws SQLException {
        chain.preparedStatement_setNCharacterStream(statement, parameterIndex, value);
    }
    
    @Override
    public void preparedStatement_setNCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader value, final long length) throws SQLException {
        chain.preparedStatement_setNCharacterStream(statement, parameterIndex, value, length);
    }
    
    @Override
    public void preparedStatement_setNClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final NClob value) throws SQLException {
        chain.preparedStatement_setNClob(statement, parameterIndex, value);
    }
    
    @Override
    public void preparedStatement_setNClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        chain.preparedStatement_setNClob(statement, parameterIndex, reader);
    }
    
    @Override
    public void preparedStatement_setNClob(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        chain.preparedStatement_setNClob(statement, parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setNString(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final String value) throws SQLException {
        chain.preparedStatement_setNString(statement, parameterIndex, value);
    }
    
    @Override
    public void preparedStatement_setNull(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final int sqlType) throws SQLException {
        chain.preparedStatement_setNull(statement, parameterIndex, sqlType);
    }
    
    @Override
    public void preparedStatement_setNull(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        chain.preparedStatement_setNull(statement, parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x) throws SQLException {
        chain.preparedStatement_setObject(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        chain.preparedStatement_setObject(statement, parameterIndex, x, targetSqlType);
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        chain.preparedStatement_setObject(statement, parameterIndex, x, targetSqlType, scaleOrLength);
    }
    
    @Override
    public void preparedStatement_setRef(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Ref x) throws SQLException {
        chain.preparedStatement_setRef(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setRowId(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final RowId x) throws SQLException {
        chain.preparedStatement_setRowId(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setShort(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final short x) throws SQLException {
        chain.preparedStatement_setShort(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setSQLXML(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        chain.preparedStatement_setSQLXML(statement, parameterIndex, xmlObject);
    }
    
    @Override
    public void preparedStatement_setString(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final String x) throws SQLException {
        chain.preparedStatement_setString(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setTime(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Time x) throws SQLException {
        chain.preparedStatement_setTime(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setTime(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        chain.preparedStatement_setTime(statement, parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setTimestamp(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Timestamp x) throws SQLException {
        chain.preparedStatement_setTimestamp(statement, parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setTimestamp(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        chain.preparedStatement_setTimestamp(statement, parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setUnicodeStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        chain.preparedStatement_setUnicodeStream(statement, parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setURL(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final URL x) throws SQLException {
        chain.preparedStatement_setURL(statement, parameterIndex, x);
    }
    
    @Override
    public boolean resultSet_absolute(final FilterChain chain, final ResultSetProxy result, final int row) throws SQLException {
        return chain.resultSet_absolute(result, row);
    }
    
    @Override
    public void resultSet_afterLast(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_afterLast(resultSet);
    }
    
    @Override
    public void resultSet_beforeFirst(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_beforeFirst(resultSet);
    }
    
    @Override
    public void resultSet_cancelRowUpdates(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_cancelRowUpdates(resultSet);
    }
    
    @Override
    public void resultSet_clearWarnings(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_clearWarnings(resultSet);
    }
    
    @Override
    public void resultSet_close(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_close(resultSet);
    }
    
    @Override
    public void resultSet_deleteRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_deleteRow(resultSet);
    }
    
    @Override
    public int resultSet_findColumn(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_findColumn(result, columnLabel);
    }
    
    @Override
    public boolean resultSet_first(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_first(resultSet);
    }
    
    @Override
    public Array resultSet_getArray(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getArray(result, columnIndex);
    }
    
    @Override
    public Array resultSet_getArray(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getArray(result, columnLabel);
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getAsciiStream(result, columnIndex);
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getAsciiStream(result, columnLabel);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getBigDecimal(result, columnIndex);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final int scale) throws SQLException {
        return chain.resultSet_getBigDecimal(result, columnIndex, scale);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getBigDecimal(result, columnLabel);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final int scale) throws SQLException {
        return chain.resultSet_getBigDecimal(result, columnLabel, scale);
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getBinaryStream(result, columnIndex);
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getBinaryStream(result, columnLabel);
    }
    
    @Override
    public Blob resultSet_getBlob(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getBlob(result, columnIndex);
    }
    
    @Override
    public Blob resultSet_getBlob(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getBlob(result, columnLabel);
    }
    
    @Override
    public boolean resultSet_getBoolean(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getBoolean(result, columnIndex);
    }
    
    @Override
    public boolean resultSet_getBoolean(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getBoolean(result, columnLabel);
    }
    
    @Override
    public byte resultSet_getByte(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getByte(result, columnIndex);
    }
    
    @Override
    public byte resultSet_getByte(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getByte(result, columnLabel);
    }
    
    @Override
    public byte[] resultSet_getBytes(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getBytes(result, columnIndex);
    }
    
    @Override
    public byte[] resultSet_getBytes(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getBytes(result, columnLabel);
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getCharacterStream(result, columnIndex);
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getCharacterStream(result, columnLabel);
    }
    
    @Override
    public Clob resultSet_getClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getClob(result, columnIndex);
    }
    
    @Override
    public Clob resultSet_getClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getClob(result, columnLabel);
    }
    
    @Override
    public int resultSet_getConcurrency(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getConcurrency(resultSet);
    }
    
    @Override
    public String resultSet_getCursorName(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getCursorName(resultSet);
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getDate(result, columnIndex);
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getDate(result, columnIndex, cal);
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getDate(result, columnLabel);
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Calendar cal) throws SQLException {
        return chain.resultSet_getDate(result, columnLabel, cal);
    }
    
    @Override
    public double resultSet_getDouble(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getDouble(result, columnIndex);
    }
    
    @Override
    public double resultSet_getDouble(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getDouble(result, columnLabel);
    }
    
    @Override
    public int resultSet_getFetchDirection(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getFetchDirection(resultSet);
    }
    
    @Override
    public int resultSet_getFetchSize(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getFetchSize(resultSet);
    }
    
    @Override
    public float resultSet_getFloat(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getFloat(result, columnIndex);
    }
    
    @Override
    public float resultSet_getFloat(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getFloat(result, columnLabel);
    }
    
    @Override
    public int resultSet_getHoldability(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getHoldability(resultSet);
    }
    
    @Override
    public int resultSet_getInt(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getInt(result, columnIndex);
    }
    
    @Override
    public int resultSet_getInt(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getInt(result, columnLabel);
    }
    
    @Override
    public long resultSet_getLong(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getLong(result, columnIndex);
    }
    
    @Override
    public long resultSet_getLong(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getLong(result, columnLabel);
    }
    
    @Override
    public ResultSetMetaData resultSet_getMetaData(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getMetaData(resultSet);
    }
    
    @Override
    public Reader resultSet_getNCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getNCharacterStream(result, columnIndex);
    }
    
    @Override
    public Reader resultSet_getNCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getNCharacterStream(result, columnLabel);
    }
    
    @Override
    public NClob resultSet_getNClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getNClob(result, columnIndex);
    }
    
    @Override
    public NClob resultSet_getNClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getNClob(result, columnLabel);
    }
    
    @Override
    public String resultSet_getNString(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getNString(result, columnIndex);
    }
    
    @Override
    public String resultSet_getNString(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getNString(result, columnLabel);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getObject(result, columnIndex);
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Class<T> type) throws SQLException {
        return chain.resultSet_getObject(result, columnIndex, type);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        return chain.resultSet_getObject(result, columnIndex, map);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getObject(result, columnLabel);
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Class<T> type) throws SQLException {
        return chain.resultSet_getObject(result, columnLabel, type);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        return chain.resultSet_getObject(result, columnLabel, map);
    }
    
    @Override
    public Ref resultSet_getRef(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getRef(result, columnIndex);
    }
    
    @Override
    public Ref resultSet_getRef(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getRef(result, columnLabel);
    }
    
    @Override
    public int resultSet_getRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getRow(resultSet);
    }
    
    @Override
    public RowId resultSet_getRowId(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getRowId(result, columnIndex);
    }
    
    @Override
    public RowId resultSet_getRowId(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getRowId(result, columnLabel);
    }
    
    @Override
    public short resultSet_getShort(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getShort(result, columnIndex);
    }
    
    @Override
    public short resultSet_getShort(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getShort(result, columnLabel);
    }
    
    @Override
    public SQLXML resultSet_getSQLXML(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getSQLXML(result, columnIndex);
    }
    
    @Override
    public SQLXML resultSet_getSQLXML(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getSQLXML(result, columnLabel);
    }
    
    @Override
    public Statement resultSet_getStatement(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getStatement(resultSet);
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getString(result, columnIndex);
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getString(result, columnLabel);
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getTime(result, columnIndex);
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getTime(result, columnIndex, cal);
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getTime(result, columnLabel);
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Calendar cal) throws SQLException {
        return chain.resultSet_getTime(result, columnLabel, cal);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getTimestamp(result, columnIndex);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getTimestamp(result, columnIndex, cal);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getTimestamp(result, columnLabel);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Calendar cal) throws SQLException {
        return chain.resultSet_getTimestamp(result, columnLabel, cal);
    }
    
    @Override
    public int resultSet_getType(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getType(resultSet);
    }
    
    @Override
    public InputStream resultSet_getUnicodeStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getUnicodeStream(result, columnIndex);
    }
    
    @Override
    public InputStream resultSet_getUnicodeStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getUnicodeStream(result, columnLabel);
    }
    
    @Override
    public URL resultSet_getURL(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        return chain.resultSet_getURL(result, columnIndex);
    }
    
    @Override
    public URL resultSet_getURL(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        return chain.resultSet_getURL(result, columnLabel);
    }
    
    @Override
    public SQLWarning resultSet_getWarnings(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_getWarnings(resultSet);
    }
    
    @Override
    public void resultSet_insertRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_insertRow(resultSet);
    }
    
    @Override
    public boolean resultSet_isAfterLast(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_isAfterLast(resultSet);
    }
    
    @Override
    public boolean resultSet_isBeforeFirst(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_isBeforeFirst(resultSet);
    }
    
    @Override
    public boolean resultSet_isClosed(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_isClosed(resultSet);
    }
    
    @Override
    public boolean resultSet_isFirst(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_isFirst(resultSet);
    }
    
    @Override
    public boolean resultSet_isLast(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_isLast(resultSet);
    }
    
    @Override
    public boolean resultSet_last(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_last(resultSet);
    }
    
    @Override
    public void resultSet_moveToCurrentRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_moveToCurrentRow(resultSet);
    }
    
    @Override
    public void resultSet_moveToInsertRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_moveToInsertRow(resultSet);
    }
    
    @Override
    public boolean resultSet_next(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_next(resultSet);
    }
    
    @Override
    public boolean resultSet_previous(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_previous(resultSet);
    }
    
    @Override
    public void resultSet_refreshRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_refreshRow(resultSet);
    }
    
    @Override
    public boolean resultSet_relative(final FilterChain chain, final ResultSetProxy result, final int rows) throws SQLException {
        return chain.resultSet_relative(result, rows);
    }
    
    @Override
    public boolean resultSet_rowDeleted(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_rowDeleted(resultSet);
    }
    
    @Override
    public boolean resultSet_rowInserted(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_rowInserted(resultSet);
    }
    
    @Override
    public boolean resultSet_rowUpdated(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_rowUpdated(resultSet);
    }
    
    @Override
    public void resultSet_setFetchDirection(final FilterChain chain, final ResultSetProxy result, final int direction) throws SQLException {
        chain.resultSet_setFetchDirection(result, direction);
    }
    
    @Override
    public void resultSet_setFetchSize(final FilterChain chain, final ResultSetProxy result, final int rows) throws SQLException {
        chain.resultSet_setFetchSize(result, rows);
    }
    
    @Override
    public void resultSet_updateArray(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Array x) throws SQLException {
        chain.resultSet_updateArray(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateArray(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Array x) throws SQLException {
        chain.resultSet_updateArray(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateAsciiStream(result, columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateBigDecimal(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final BigDecimal x) throws SQLException {
        chain.resultSet_updateBigDecimal(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBigDecimal(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final BigDecimal x) throws SQLException {
        chain.resultSet_updateBigDecimal(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateBinaryStream(result, columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream inputStream) throws SQLException {
        chain.resultSet_updateBlob(result, columnIndex, inputStream);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        chain.resultSet_updateBlob(result, columnIndex, inputStream, length);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Blob x) throws SQLException {
        chain.resultSet_updateBlob(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream inputStream) throws SQLException {
        chain.resultSet_updateBlob(result, columnLabel, inputStream);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
        chain.resultSet_updateBlob(result, columnLabel, inputStream, length);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Blob x) throws SQLException {
        chain.resultSet_updateBlob(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBoolean(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final boolean x) throws SQLException {
        chain.resultSet_updateBoolean(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBoolean(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final boolean x) throws SQLException {
        chain.resultSet_updateBoolean(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateByte(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final byte x) throws SQLException {
        chain.resultSet_updateByte(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateByte(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final byte x) throws SQLException {
        chain.resultSet_updateByte(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBytes(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final byte[] x) throws SQLException {
        chain.resultSet_updateBytes(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBytes(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final byte[] x) throws SQLException {
        chain.resultSet_updateBytes(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader x) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader x, final int length) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader x, final long length) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader, final int length) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateCharacterStream(result, columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Clob x) throws SQLException {
        chain.resultSet_updateClob(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader reader) throws SQLException {
        chain.resultSet_updateClob(result, columnIndex, reader);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateClob(result, columnIndex, reader, length);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Clob x) throws SQLException {
        chain.resultSet_updateClob(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader) throws SQLException {
        chain.resultSet_updateClob(result, columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateClob(result, columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateDate(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Date x) throws SQLException {
        chain.resultSet_updateDate(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateDate(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Date x) throws SQLException {
        chain.resultSet_updateDate(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateDouble(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final double x) throws SQLException {
        chain.resultSet_updateDouble(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateDouble(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final double x) throws SQLException {
        chain.resultSet_updateDouble(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateFloat(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final float x) throws SQLException {
        chain.resultSet_updateFloat(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateFloat(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final float x) throws SQLException {
        chain.resultSet_updateFloat(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateInt(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final int x) throws SQLException {
        chain.resultSet_updateInt(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateInt(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final int x) throws SQLException {
        chain.resultSet_updateInt(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateLong(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final long x) throws SQLException {
        chain.resultSet_updateLong(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateLong(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final long x) throws SQLException {
        chain.resultSet_updateLong(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader x) throws SQLException {
        chain.resultSet_updateNCharacterStream(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader x, final long length) throws SQLException {
        chain.resultSet_updateNCharacterStream(result, columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader) throws SQLException {
        chain.resultSet_updateNCharacterStream(result, columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateNCharacterStream(result, columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final NClob nClob) throws SQLException {
        chain.resultSet_updateNClob(result, columnIndex, nClob);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader reader) throws SQLException {
        chain.resultSet_updateNClob(result, columnIndex, reader);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateNClob(result, columnIndex, reader, length);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final NClob nClob) throws SQLException {
        chain.resultSet_updateNClob(result, columnLabel, nClob);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader) throws SQLException {
        chain.resultSet_updateNClob(result, columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateNClob(result, columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateNString(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final String nString) throws SQLException {
        chain.resultSet_updateNString(result, columnIndex, nString);
    }
    
    @Override
    public void resultSet_updateNString(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final String nString) throws SQLException {
        chain.resultSet_updateNString(result, columnLabel, nString);
    }
    
    @Override
    public void resultSet_updateNull(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        chain.resultSet_updateNull(result, columnIndex);
    }
    
    @Override
    public void resultSet_updateNull(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        chain.resultSet_updateNull(result, columnLabel);
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Object x) throws SQLException {
        chain.resultSet_updateObject(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        chain.resultSet_updateObject(result, columnIndex, x, scaleOrLength);
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Object x) throws SQLException {
        chain.resultSet_updateObject(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        chain.resultSet_updateObject(result, columnLabel, x, scaleOrLength);
    }
    
    @Override
    public void resultSet_updateRef(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Ref x) throws SQLException {
        chain.resultSet_updateRef(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateRef(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Ref x) throws SQLException {
        chain.resultSet_updateRef(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateRow(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_updateRow(resultSet);
    }
    
    @Override
    public void resultSet_updateRowId(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final RowId x) throws SQLException {
        chain.resultSet_updateRowId(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateRowId(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final RowId x) throws SQLException {
        chain.resultSet_updateRowId(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateShort(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final short x) throws SQLException {
        chain.resultSet_updateShort(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateShort(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final short x) throws SQLException {
        chain.resultSet_updateShort(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateSQLXML(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final SQLXML xmlObject) throws SQLException {
        chain.resultSet_updateSQLXML(result, columnIndex, xmlObject);
    }
    
    @Override
    public void resultSet_updateSQLXML(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final SQLXML xmlObject) throws SQLException {
        chain.resultSet_updateSQLXML(result, columnLabel, xmlObject);
    }
    
    @Override
    public void resultSet_updateString(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final String x) throws SQLException {
        chain.resultSet_updateString(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateString(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final String x) throws SQLException {
        chain.resultSet_updateString(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateTime(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Time x) throws SQLException {
        chain.resultSet_updateTime(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateTime(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Time x) throws SQLException {
        chain.resultSet_updateTime(result, columnLabel, x);
    }
    
    @Override
    public void resultSet_updateTimestamp(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Timestamp x) throws SQLException {
        chain.resultSet_updateTimestamp(result, columnIndex, x);
    }
    
    @Override
    public void resultSet_updateTimestamp(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Timestamp x) throws SQLException {
        chain.resultSet_updateTimestamp(result, columnLabel, x);
    }
    
    @Override
    public boolean resultSet_wasNull(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        return chain.resultSet_wasNull(resultSet);
    }
    
    @Override
    public void statement_addBatch(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        chain.statement_addBatch(statement, sql);
    }
    
    @Override
    public void statement_cancel(final FilterChain chain, final StatementProxy statement) throws SQLException {
        chain.statement_cancel(statement);
    }
    
    @Override
    public void statement_clearBatch(final FilterChain chain, final StatementProxy statement) throws SQLException {
        chain.statement_clearBatch(statement);
    }
    
    @Override
    public void statement_close(final FilterChain chain, final StatementProxy statement) throws SQLException {
        chain.statement_close(statement);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return chain.statement_execute(statement, sql);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        return chain.statement_execute(statement, sql, autoGeneratedKeys);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        return chain.statement_execute(statement, sql, columnIndexes);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        return chain.statement_execute(statement, sql, columnNames);
    }
    
    @Override
    public int[] statement_executeBatch(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_executeBatch(statement);
    }
    
    @Override
    public ResultSetProxy statement_executeQuery(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return chain.statement_executeQuery(statement, sql);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return chain.statement_executeUpdate(statement, sql);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        return chain.statement_executeUpdate(statement, sql, autoGeneratedKeys);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        return chain.statement_executeUpdate(statement, sql, columnIndexes);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        return chain.statement_executeUpdate(statement, sql, columnNames);
    }
    
    @Override
    public Connection statement_getConnection(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getConnection(statement);
    }
    
    @Override
    public int statement_getFetchDirection(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getFetchDirection(statement);
    }
    
    @Override
    public int statement_getFetchSize(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getFetchSize(statement);
    }
    
    @Override
    public ResultSetProxy statement_getGeneratedKeys(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getGeneratedKeys(statement);
    }
    
    @Override
    public int statement_getMaxFieldSize(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getMaxFieldSize(statement);
    }
    
    @Override
    public int statement_getMaxRows(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getMaxRows(statement);
    }
    
    @Override
    public boolean statement_getMoreResults(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getMoreResults(statement);
    }
    
    @Override
    public boolean statement_getMoreResults(final FilterChain chain, final StatementProxy statement, final int current) throws SQLException {
        return chain.statement_getMoreResults(statement, current);
    }
    
    @Override
    public int statement_getQueryTimeout(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getQueryTimeout(statement);
    }
    
    @Override
    public void statement_setQueryTimeout(final FilterChain chain, final StatementProxy statement, final int seconds) throws SQLException {
        chain.statement_setQueryTimeout(statement, seconds);
    }
    
    @Override
    public ResultSetProxy statement_getResultSet(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getResultSet(statement);
    }
    
    @Override
    public int statement_getResultSetConcurrency(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getResultSetConcurrency(statement);
    }
    
    @Override
    public int statement_getResultSetHoldability(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getResultSetHoldability(statement);
    }
    
    @Override
    public int statement_getResultSetType(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getResultSetType(statement);
    }
    
    @Override
    public int statement_getUpdateCount(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getUpdateCount(statement);
    }
    
    @Override
    public SQLWarning statement_getWarnings(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_getWarnings(statement);
    }
    
    @Override
    public void statement_clearWarnings(final FilterChain chain, final StatementProxy statement) throws SQLException {
        chain.statement_clearWarnings(statement);
    }
    
    @Override
    public boolean statement_isClosed(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_isClosed(statement);
    }
    
    @Override
    public boolean statement_isPoolable(final FilterChain chain, final StatementProxy statement) throws SQLException {
        return chain.statement_isPoolable(statement);
    }
    
    @Override
    public void statement_setCursorName(final FilterChain chain, final StatementProxy statement, final String name) throws SQLException {
        chain.statement_setCursorName(statement, name);
    }
    
    @Override
    public void statement_setEscapeProcessing(final FilterChain chain, final StatementProxy statement, final boolean enable) throws SQLException {
        chain.statement_setEscapeProcessing(statement, enable);
    }
    
    @Override
    public void statement_setFetchDirection(final FilterChain chain, final StatementProxy statement, final int direction) throws SQLException {
        chain.statement_setFetchDirection(statement, direction);
    }
    
    @Override
    public void statement_setFetchSize(final FilterChain chain, final StatementProxy statement, final int rows) throws SQLException {
        chain.statement_setFetchSize(statement, rows);
    }
    
    @Override
    public void statement_setMaxFieldSize(final FilterChain chain, final StatementProxy statement, final int max) throws SQLException {
        chain.statement_setMaxFieldSize(statement, max);
    }
    
    @Override
    public void statement_setMaxRows(final FilterChain chain, final StatementProxy statement, final int max) throws SQLException {
        chain.statement_setMaxRows(statement, max);
    }
    
    @Override
    public void statement_setPoolable(final FilterChain chain, final StatementProxy statement, final boolean poolable) throws SQLException {
        chain.statement_setPoolable(statement, poolable);
    }
    
    @Override
    public <T> T unwrap(final FilterChain chain, final Wrapper wrapper, final Class<T> iface) throws SQLException {
        return chain.unwrap(wrapper, iface);
    }
    
    @Override
    public long clob_length(final FilterChain chain, final ClobProxy wrapper) throws SQLException {
        return chain.clob_length(wrapper);
    }
    
    @Override
    public String clob_getSubString(final FilterChain chain, final ClobProxy wrapper, final long pos, final int length) throws SQLException {
        return chain.clob_getSubString(wrapper, pos, length);
    }
    
    @Override
    public Reader clob_getCharacterStream(final FilterChain chain, final ClobProxy wrapper) throws SQLException {
        return chain.clob_getCharacterStream(wrapper);
    }
    
    @Override
    public InputStream clob_getAsciiStream(final FilterChain chain, final ClobProxy wrapper) throws SQLException {
        return chain.clob_getAsciiStream(wrapper);
    }
    
    @Override
    public long clob_position(final FilterChain chain, final ClobProxy wrapper, final String searchstr, final long start) throws SQLException {
        return chain.clob_position(wrapper, searchstr, start);
    }
    
    @Override
    public long clob_position(final FilterChain chain, final ClobProxy wrapper, final Clob searchstr, final long start) throws SQLException {
        return chain.clob_position(wrapper, searchstr, start);
    }
    
    @Override
    public int clob_setString(final FilterChain chain, final ClobProxy wrapper, final long pos, final String str) throws SQLException {
        return chain.clob_setString(wrapper, pos, str);
    }
    
    @Override
    public int clob_setString(final FilterChain chain, final ClobProxy wrapper, final long pos, final String str, final int offset, final int len) throws SQLException {
        return chain.clob_setString(wrapper, pos, str, offset, len);
    }
    
    @Override
    public OutputStream clob_setAsciiStream(final FilterChain chain, final ClobProxy wrapper, final long pos) throws SQLException {
        return chain.clob_setAsciiStream(wrapper, pos);
    }
    
    @Override
    public Writer clob_setCharacterStream(final FilterChain chain, final ClobProxy wrapper, final long pos) throws SQLException {
        return chain.clob_setCharacterStream(wrapper, pos);
    }
    
    @Override
    public void clob_truncate(final FilterChain chain, final ClobProxy wrapper, final long len) throws SQLException {
        chain.clob_truncate(wrapper, len);
    }
    
    @Override
    public void clob_free(final FilterChain chain, final ClobProxy wrapper) throws SQLException {
        chain.clob_free(wrapper);
    }
    
    @Override
    public Reader clob_getCharacterStream(final FilterChain chain, final ClobProxy wrapper, final long pos, final long length) throws SQLException {
        return chain.clob_getCharacterStream(wrapper, pos, length);
    }
    
    @Override
    public void dataSource_releaseConnection(final FilterChain chain, final DruidPooledConnection connection) throws SQLException {
        chain.dataSource_recycle(connection);
    }
    
    @Override
    public DruidPooledConnection dataSource_getConnection(final FilterChain chain, final DruidDataSource dataSource, final long maxWaitMillis) throws SQLException {
        return chain.dataSource_connect(dataSource, maxWaitMillis);
    }
    
    @Override
    public int resultSetMetaData_getColumnCount(final FilterChain chain, final ResultSetMetaDataProxy metaData) throws SQLException {
        return chain.resultSetMetaData_getColumnCount(metaData);
    }
    
    @Override
    public boolean resultSetMetaData_isAutoIncrement(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isAutoIncrement(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isCaseSensitive(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isCaseSensitive(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isSearchable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isSearchable(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isCurrency(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isCurrency(metaData, column);
    }
    
    @Override
    public int resultSetMetaData_isNullable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isNullable(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isSigned(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isSigned(metaData, column);
    }
    
    @Override
    public int resultSetMetaData_getColumnDisplaySize(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnDisplaySize(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getColumnLabel(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnLabel(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getColumnName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnName(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getSchemaName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getSchemaName(metaData, column);
    }
    
    @Override
    public int resultSetMetaData_getPrecision(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getPrecision(metaData, column);
    }
    
    @Override
    public int resultSetMetaData_getScale(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getScale(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getTableName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getTableName(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getCatalogName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getCatalogName(metaData, column);
    }
    
    @Override
    public int resultSetMetaData_getColumnType(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnType(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getColumnTypeName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnTypeName(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isReadOnly(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isReadOnly(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isWritable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isWritable(metaData, column);
    }
    
    @Override
    public boolean resultSetMetaData_isDefinitelyWritable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isDefinitelyWritable(metaData, column);
    }
    
    @Override
    public String resultSetMetaData_getColumnClassName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnClassName(metaData, column);
    }
}
