// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.sql.PreparedStatement;
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
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class DruidPooledCallableStatement extends DruidPooledPreparedStatement implements CallableStatement
{
    private CallableStatement stmt;
    
    public DruidPooledCallableStatement(final DruidPooledConnection conn, final PreparedStatementHolder holder) throws SQLException {
        super(conn, holder);
        this.stmt = (CallableStatement)holder.statement;
    }
    
    public CallableStatement getCallableStatementRaw() {
        return this.stmt;
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterIndex, sqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        try {
            return this.stmt.wasNull();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getString(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getString(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getBoolean(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte getByte(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getByte(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public short getShort(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getShort(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getInt(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getInt(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public long getLong(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getLong(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public float getFloat(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getFloat(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public double getDouble(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getDouble(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        try {
            return this.stmt.getBigDecimal(parameterIndex, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getBytes(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getDate(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getTime(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getTimestamp(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        try {
            final Object obj = this.stmt.getObject(parameterIndex);
            return this.wrapObject(obj);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    private Object wrapObject(Object obj) {
        if (obj instanceof ResultSet) {
            final ResultSet rs = (ResultSet)obj;
            final DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            this.addResultSetTrace(poolableResultSet);
            obj = poolableResultSet;
        }
        return obj;
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getBigDecimal(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        try {
            final Object obj = this.stmt.getObject(parameterIndex, map);
            return this.wrapObject(obj);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getRef(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getBlob(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Clob getClob(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getClob(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getArray(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getDate(parameterIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getTime(parameterIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getTimestamp(parameterIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterName, sqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterName, sqlType, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        try {
            this.stmt.registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getURL(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setURL(final String parameterName, final URL val) throws SQLException {
        try {
            this.stmt.setURL(parameterName, val);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {
        try {
            this.stmt.setNull(parameterName, sqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {
        try {
            this.stmt.setBoolean(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {
        try {
            this.stmt.setByte(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {
        try {
            this.stmt.setShort(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {
        try {
            this.stmt.setInt(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {
        try {
            this.stmt.setLong(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {
        try {
            this.stmt.setFloat(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {
        try {
            this.stmt.setDouble(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
        try {
            this.stmt.setBigDecimal(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setString(final String parameterName, final String x) throws SQLException {
        try {
            this.stmt.setString(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {
        try {
            this.stmt.setBytes(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {
        try {
            this.stmt.setDate(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {
        try {
            this.stmt.setTime(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
        try {
            this.stmt.setTimestamp(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        try {
            this.stmt.setAsciiStream(parameterName, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        try {
            this.stmt.setBinaryStream(parameterName, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        try {
            this.stmt.setObject(parameterName, x, targetSqlType, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        try {
            this.stmt.setObject(parameterName, x, targetSqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        try {
            this.stmt.setObject(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
        try {
            this.stmt.setCharacterStream(parameterName, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        try {
            this.stmt.setDate(parameterName, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        try {
            this.stmt.setTime(parameterName, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        try {
            this.stmt.setTimestamp(parameterName, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        try {
            this.stmt.setNull(parameterName, sqlType, typeName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getString(final String parameterName) throws SQLException {
        try {
            return this.stmt.getString(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean getBoolean(final String parameterName) throws SQLException {
        try {
            return this.stmt.getBoolean(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte getByte(final String parameterName) throws SQLException {
        try {
            return this.stmt.getByte(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public short getShort(final String parameterName) throws SQLException {
        try {
            return this.stmt.getShort(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getInt(final String parameterName) throws SQLException {
        try {
            return this.stmt.getInt(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public long getLong(final String parameterName) throws SQLException {
        try {
            return this.stmt.getLong(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public float getFloat(final String parameterName) throws SQLException {
        try {
            return this.stmt.getFloat(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public double getDouble(final String parameterName) throws SQLException {
        try {
            return this.stmt.getDouble(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte[] getBytes(final String parameterName) throws SQLException {
        try {
            return this.stmt.getBytes(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final String parameterName) throws SQLException {
        try {
            return this.stmt.getDate(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final String parameterName) throws SQLException {
        try {
            return this.stmt.getTime(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        try {
            return this.stmt.getTimestamp(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final String parameterName) throws SQLException {
        try {
            final Object obj = this.stmt.getObject(parameterName);
            return this.wrapObject(obj);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        try {
            return this.stmt.getBigDecimal(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        try {
            final Object obj = this.stmt.getObject(parameterName, map);
            return this.wrapObject(obj);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        try {
            return this.stmt.getRef(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        try {
            return this.stmt.getBlob(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        try {
            return this.stmt.getClob(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Array getArray(final String parameterName) throws SQLException {
        try {
            return this.stmt.getArray(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getDate(parameterName, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getTime(parameterName, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        try {
            return this.stmt.getTimestamp(parameterName, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public URL getURL(final String parameterName) throws SQLException {
        try {
            return this.stmt.getURL(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getRowId(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        try {
            return this.stmt.getRowId(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        try {
            this.stmt.setRowId(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNString(final String parameterName, final String value) throws SQLException {
        try {
            this.stmt.setNString(parameterName, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
        try {
            this.stmt.setNCharacterStream(parameterName, value, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final String parameterName, final NClob value) throws SQLException {
        try {
            this.stmt.setNClob(parameterName, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        try {
            this.stmt.setClob(parameterName, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        try {
            this.stmt.setBlob(parameterName, inputStream, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
        try {
            this.stmt.setNClob(parameterName, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getNClob(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        try {
            return this.stmt.getNClob(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
        try {
            this.stmt.setSQLXML(parameterName, xmlObject);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getSQLXML(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        try {
            return this.stmt.getSQLXML(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getNString(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getNString(final String parameterName) throws SQLException {
        try {
            return this.stmt.getNString(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getNCharacterStream(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        try {
            return this.stmt.getNCharacterStream(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        try {
            return this.stmt.getCharacterStream(parameterIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        try {
            return this.stmt.getCharacterStream(parameterName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        try {
            this.stmt.setBlob(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        try {
            this.stmt.setClob(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        try {
            this.stmt.setAsciiStream(parameterName, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        try {
            this.stmt.setBinaryStream(parameterName, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
        try {
            this.stmt.setCharacterStream(parameterName, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
        try {
            this.stmt.setAsciiStream(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
        try {
            this.stmt.setBinaryStream(parameterName, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
        try {
            this.stmt.setCharacterStream(parameterName, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
        try {
            this.stmt.setNCharacterStream(parameterName, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final String parameterName, final Reader reader) throws SQLException {
        try {
            this.stmt.setClob(parameterName, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
        try {
            this.stmt.setBlob(parameterName, inputStream);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader reader) throws SQLException {
        try {
            this.stmt.setNClob(parameterName, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        return this.stmt.getObject(parameterIndex, type);
    }
    
    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        return this.stmt.getObject(parameterName, type);
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface != CallableStatement.class && iface != PreparedStatement.class) {
            return super.unwrap(iface);
        }
        if (this.stmt instanceof CallableStatementProxy) {
            return this.stmt.unwrap(iface);
        }
        return (T)this.stmt;
    }
}
