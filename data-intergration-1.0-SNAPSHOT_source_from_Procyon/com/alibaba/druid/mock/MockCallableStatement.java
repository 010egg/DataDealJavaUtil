// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.SQLFeatureNotSupportedException;
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
import java.util.ArrayList;
import java.util.List;
import java.sql.CallableStatement;

public class MockCallableStatement extends MockPreparedStatement implements CallableStatement
{
    private final List<Object> outParameters;
    private boolean wasNull;
    
    public MockCallableStatement(final MockConnection conn, final String sql) {
        super(conn, sql);
        this.outParameters = new ArrayList<Object>();
        this.wasNull = false;
    }
    
    public List<Object> getOutParameters() {
        return this.outParameters;
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
        if (parameterIndex < 1) {
            throw new SQLException();
        }
        if (this.outParameters.size() >= parameterIndex - 1) {
            this.outParameters.add(null);
            return;
        }
        throw new SQLException();
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        this.registerOutParameter(parameterIndex, sqlType);
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.wasNull;
    }
    
    @Override
    public String getString(final int columnIndex) throws SQLException {
        return (String)this.getObject(columnIndex);
    }
    
    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        final Object obj = this.getObject(columnIndex);
        return obj != null && (boolean)obj;
    }
    
    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0;
        }
        return number.byteValue();
    }
    
    @Override
    public short getShort(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0;
        }
        return number.shortValue();
    }
    
    @Override
    public int getInt(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0;
        }
        return number.intValue();
    }
    
    @Override
    public long getLong(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0L;
        }
        return number.longValue();
    }
    
    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0.0f;
        }
        return number.floatValue();
    }
    
    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        final Number number = (Number)this.getObject(columnIndex);
        if (number == null) {
            return 0.0;
        }
        return number.doubleValue();
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        return (BigDecimal)this.getObject(columnIndex);
    }
    
    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        return (byte[])this.getObject(columnIndex);
    }
    
    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        return (Date)this.getObject(columnIndex);
    }
    
    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return (Time)this.getObject(columnIndex);
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return (Timestamp)this.getObject(columnIndex);
    }
    
    @Override
    public String getString(final String columnLabel) throws SQLException {
        return this.getString(Integer.parseInt(columnLabel));
    }
    
    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        return this.getBoolean(Integer.parseInt(columnLabel));
    }
    
    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        return this.getByte(Integer.parseInt(columnLabel));
    }
    
    @Override
    public short getShort(final String columnLabel) throws SQLException {
        return this.getShort(Integer.parseInt(columnLabel));
    }
    
    @Override
    public int getInt(final String columnLabel) throws SQLException {
        return this.getInt(Integer.parseInt(columnLabel));
    }
    
    @Override
    public long getLong(final String columnLabel) throws SQLException {
        return this.getLong(Integer.parseInt(columnLabel));
    }
    
    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        return this.getFloat(Integer.parseInt(columnLabel));
    }
    
    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        return this.getDouble(Integer.parseInt(columnLabel));
    }
    
    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        return this.getBytes(Integer.parseInt(columnLabel));
    }
    
    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        return this.getDate(Integer.parseInt(columnLabel));
    }
    
    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        return this.getTime(Integer.parseInt(columnLabel));
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        return this.getTimestamp(Integer.parseInt(columnLabel));
    }
    
    @Override
    public Object getObject(final int parameterIndex) throws SQLException {
        final Object obj = this.outParameters.get(parameterIndex - 1);
        this.wasNull = (obj == null);
        return obj;
    }
    
    @Override
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        return (BigDecimal)this.getObject(parameterIndex);
    }
    
    @Override
    public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        return this.getObject(parameterIndex);
    }
    
    @Override
    public Ref getRef(final int parameterIndex) throws SQLException {
        return (Ref)this.getObject(parameterIndex);
    }
    
    @Override
    public Blob getBlob(final int parameterIndex) throws SQLException {
        return (Blob)this.getObject(parameterIndex);
    }
    
    @Override
    public Clob getClob(final int parameterIndex) throws SQLException {
        return (Clob)this.getObject(parameterIndex);
    }
    
    @Override
    public Array getArray(final int parameterIndex) throws SQLException {
        return (Array)this.getObject(parameterIndex);
    }
    
    @Override
    public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
        return (Date)this.getObject(parameterIndex);
    }
    
    @Override
    public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
        return (Time)this.getObject(parameterIndex);
    }
    
    @Override
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
        return (Timestamp)this.getObject(parameterIndex);
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.registerOutParameter(parameterIndex, sqlType);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
        this.registerOutParameter(Integer.parseInt(parameterName), sqlType);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
        this.registerOutParameter(parameterName, sqlType);
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        this.registerOutParameter(parameterName, sqlType);
    }
    
    @Override
    public URL getURL(final int parameterIndex) throws SQLException {
        return (URL)this.getObject(parameterIndex);
    }
    
    @Override
    public void setURL(final String parameterName, final URL x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType) throws SQLException {
        this.setObject(parameterName, null);
    }
    
    @Override
    public void setBoolean(final String parameterName, final boolean x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setByte(final String parameterName, final byte x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setShort(final String parameterName, final short x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setInt(final String parameterName, final int x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setLong(final String parameterName, final long x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setFloat(final String parameterName, final float x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setDouble(final String parameterName, final double x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setString(final String parameterName, final String x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBytes(final String parameterName, final byte[] x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setDate(final String parameterName, final Date x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setTime(final String parameterName, final Time x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setObject(final String parameterName, final Object x) throws SQLException {
        this.setObject(Integer.parseInt(parameterName), x);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader x, final int length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
        this.setObject(parameterName, null);
    }
    
    @Override
    public Object getObject(final String parameterName) throws SQLException {
        return this.getObject(Integer.parseInt(parameterName));
    }
    
    @Override
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        return this.getBigDecimal(Integer.parseInt(parameterName));
    }
    
    @Override
    public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        return this.getObject(parameterName);
    }
    
    @Override
    public Ref getRef(final String parameterName) throws SQLException {
        return (Ref)this.getObject(parameterName);
    }
    
    @Override
    public Blob getBlob(final String parameterName) throws SQLException {
        return (Blob)this.getObject(parameterName);
    }
    
    @Override
    public Clob getClob(final String parameterName) throws SQLException {
        return (Clob)this.getObject(parameterName);
    }
    
    @Override
    public Array getArray(final String parameterName) throws SQLException {
        return (Array)this.getObject(parameterName);
    }
    
    @Override
    public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
        return (Date)this.getObject(parameterName);
    }
    
    @Override
    public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
        return (Time)this.getObject(parameterName);
    }
    
    @Override
    public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
        return (Timestamp)this.getObject(parameterName);
    }
    
    @Override
    public URL getURL(final String parameterName) throws SQLException {
        return (URL)this.getObject(parameterName);
    }
    
    @Override
    public RowId getRowId(final int parameterIndex) throws SQLException {
        return (RowId)this.getObject(parameterIndex);
    }
    
    @Override
    public RowId getRowId(final String parameterName) throws SQLException {
        return (RowId)this.getObject(parameterName);
    }
    
    @Override
    public void setRowId(final String parameterName, final RowId x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNString(final String parameterName, final String x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNClob(final String parameterName, final NClob x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setClob(final String parameterName, final Reader x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public NClob getNClob(final int parameterIndex) throws SQLException {
        return (NClob)this.getObject(parameterIndex);
    }
    
    @Override
    public NClob getNClob(final String parameterName) throws SQLException {
        return (NClob)this.getObject(parameterName);
    }
    
    @Override
    public void setSQLXML(final String parameterName, final SQLXML x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
        return (SQLXML)this.getObject(parameterIndex);
    }
    
    @Override
    public SQLXML getSQLXML(final String parameterName) throws SQLException {
        return (SQLXML)this.getObject(parameterName);
    }
    
    @Override
    public String getNString(final int parameterIndex) throws SQLException {
        return (String)this.getObject(parameterIndex);
    }
    
    @Override
    public String getNString(final String parameterName) throws SQLException {
        return (String)this.getObject(parameterName);
    }
    
    @Override
    public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
        return (Reader)this.getObject(parameterIndex);
    }
    
    @Override
    public Reader getNCharacterStream(final String parameterName) throws SQLException {
        return (Reader)this.getObject(parameterName);
    }
    
    @Override
    public Reader getCharacterStream(final int parameterIndex) throws SQLException {
        return (Reader)this.getObject(parameterIndex);
    }
    
    @Override
    public Reader getCharacterStream(final String parameterName) throws SQLException {
        return (Reader)this.getObject(parameterName);
    }
    
    @Override
    public void setBlob(final String parameterName, final Blob x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setClob(final String parameterName, final Clob x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader x, final long length) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setCharacterStream(final String parameterName, final Reader x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNCharacterStream(final String parameterName, final Reader x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setClob(final String parameterName, final Reader x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setBlob(final String parameterName, final InputStream x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public void setNClob(final String parameterName, final Reader x) throws SQLException {
        this.setObject(parameterName, x);
    }
    
    @Override
    public <T> T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public <T> T getObject(final String parameterName, final Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
