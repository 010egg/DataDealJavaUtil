// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.RowId;
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
import java.sql.SQLFeatureNotSupportedException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.sql.ResultSet;

public abstract class ResultSetBase implements ResultSet
{
    protected boolean closed;
    protected boolean wasNull;
    private SQLWarning warning;
    private String cursorName;
    private int fetchSize;
    private int fetchDirection;
    protected Statement statement;
    protected ResultSetMetaData metaData;
    
    public ResultSetBase(final Statement statement) {
        this.closed = false;
        this.wasNull = false;
        this.fetchSize = 0;
        this.fetchDirection = 0;
        this.statement = statement;
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }
    
    @Override
    public void updateNString(final int columnIndex, final String x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNString(final String columnLabel, final String x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final NClob x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final NClob x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        return (NClob)this.getObject(columnIndex);
    }
    
    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        return (NClob)this.getObject(columnLabel);
    }
    
    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        return (SQLXML)this.getObject(columnIndex);
    }
    
    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        return (SQLXML)this.getObject(columnLabel);
    }
    
    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public String getNString(final int columnIndex) throws SQLException {
        return (String)this.getObject(columnIndex);
    }
    
    @Override
    public String getNString(final String columnLabel) throws SQLException {
        return (String)this.getObject(columnLabel);
    }
    
    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        return (Reader)this.getObject(columnIndex);
    }
    
    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        return (Reader)this.getObject(columnLabel);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader x, final long length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == null) {
            return null;
        }
        if (iface.isInstance(this)) {
            return (T)this;
        }
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface != null && iface.isInstance(this);
    }
    
    @Override
    public void close() throws SQLException {
        this.closed = true;
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.wasNull;
    }
    
    public Object getObjectInternal(final int columnIndex) throws SQLException {
        if (this.getMetaData() != null) {
            final String columnName = this.getMetaData().getColumnName(columnIndex);
            return this.getObject(columnName);
        }
        return null;
    }
    
    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        final Object obj = this.getObjectInternal(columnIndex);
        this.wasNull = (obj == null);
        return obj;
    }
    
    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        return this.getObject(this.findColumn(columnLabel));
    }
    
    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        return Integer.parseInt(columnLabel);
    }
    
    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        return (Reader)this.getObject(columnIndex);
    }
    
    @Override
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        return (Reader)this.getObject(columnLabel);
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return (BigDecimal)this.getObject(columnIndex);
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel));
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        this.warning = null;
    }
    
    public void setWarning(final SQLWarning warning) {
        this.warning = warning;
    }
    
    @Override
    public String getCursorName() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.cursorName;
    }
    
    public void setCursorName(final String cursorName) {
        this.cursorName = cursorName;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.warning;
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        this.fetchDirection = direction;
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.fetchDirection;
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        this.fetchSize = rows;
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return this.fetchSize;
    }
    
    @Override
    public boolean rowUpdated() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean rowInserted() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean rowDeleted() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        this.updateObject(columnIndex, null);
    }
    
    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        this.updateObject(columnLabel, null);
    }
    
    @Override
    public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateByte(final String columnLabel, final byte x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateShort(final String columnLabel, final short x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateInt(final String columnLabel, final int x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateLong(final String columnLabel, final long x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateFloat(final String columnLabel, final float x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateDouble(final String columnLabel, final double x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateString(final String columnLabel, final String x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateDate(final String columnLabel, final Date x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateTime(final String columnLabel, final Time x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException {
        this.updateObject(columnLabel, reader);
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x) throws SQLException {
        this.updateObject(this.findColumn(columnLabel), x);
    }
    
    @Override
    public void insertRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void updateRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void deleteRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void refreshRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void cancelRowUpdates() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void moveToInsertRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public void moveToCurrentRow() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
    }
    
    @Override
    public Statement getStatement() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
        return this.statement;
    }
    
    @Override
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        return this.getObject(columnIndex);
    }
    
    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        return (Ref)this.getObject(columnIndex);
    }
    
    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        return (Blob)this.getObject(columnIndex);
    }
    
    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        return (Clob)this.getObject(columnIndex);
    }
    
    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        return (Array)this.getObject(columnIndex);
    }
    
    @Override
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        return this.getObject(columnLabel);
    }
    
    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        return (Ref)this.getObject(columnLabel);
    }
    
    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        return (Blob)this.getObject(columnLabel);
    }
    
    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        return (Clob)this.getObject(columnLabel);
    }
    
    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        return (Array)this.getObject(columnLabel);
    }
    
    @Override
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        return (Date)this.getObject(columnIndex);
    }
    
    @Override
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        return (Date)this.getObject(columnLabel);
    }
    
    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        return (Time)this.getObject(columnIndex);
    }
    
    @Override
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        return (Time)this.getObject(columnLabel);
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        return (Timestamp)this.getObject(columnIndex);
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        return (Timestamp)this.getObject(columnLabel);
    }
    
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        return (URL)this.getObject(columnIndex);
    }
    
    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        return (URL)this.getObject(columnLabel);
    }
    
    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        return (RowId)this.getObject(columnIndex);
    }
    
    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        return (RowId)this.getObject(columnLabel);
    }
    
    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        this.updateObject(columnIndex, x);
    }
    
    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        this.updateObject(columnLabel, x);
    }
    
    @Override
    public int getHoldability() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
        return 0;
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
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        return (InputStream)this.getObject(columnIndex);
    }
    
    @Override
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        return (InputStream)this.getObject(columnIndex);
    }
    
    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        return (InputStream)this.getObject(columnIndex);
    }
    
    @Override
    public String getString(final String columnLabel) throws SQLException {
        return this.getString(this.findColumn(columnLabel));
    }
    
    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        return this.getBoolean(this.findColumn(columnLabel));
    }
    
    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        return this.getByte(this.findColumn(columnLabel));
    }
    
    @Override
    public short getShort(final String columnLabel) throws SQLException {
        return this.getShort(this.findColumn(columnLabel));
    }
    
    @Override
    public int getInt(final String columnLabel) throws SQLException {
        return this.getInt(this.findColumn(columnLabel));
    }
    
    @Override
    public long getLong(final String columnLabel) throws SQLException {
        return this.getLong(this.findColumn(columnLabel));
    }
    
    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        return this.getFloat(this.findColumn(columnLabel));
    }
    
    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        return this.getDouble(this.findColumn(columnLabel));
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel), scale);
    }
    
    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        return this.getBytes(this.findColumn(columnLabel));
    }
    
    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        return this.getDate(this.findColumn(columnLabel));
    }
    
    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        return this.getTime(this.findColumn(columnLabel));
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        return this.getTimestamp(this.findColumn(columnLabel));
    }
    
    @Override
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        return this.getAsciiStream(this.findColumn(columnLabel));
    }
    
    @Override
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        return this.getUnicodeStream(this.findColumn(columnLabel));
    }
    
    @Override
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        return this.getBinaryStream(this.findColumn(columnLabel));
    }
    
    @Override
    public boolean isBeforeFirst() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean isAfterLast() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean isFirst() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean isLast() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public void beforeFirst() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
    }
    
    @Override
    public void afterLast() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
    }
    
    @Override
    public boolean first() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean last() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public int getRow() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return 0;
    }
    
    @Override
    public boolean absolute(final int row) throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public boolean relative(final int rows) throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return false;
    }
    
    @Override
    public int getType() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return 0;
    }
    
    @Override
    public int getConcurrency() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        return 0;
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
        return this.metaData;
    }
}
