// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.net.URL;
import java.util.Calendar;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.util.Map;
import java.sql.Statement;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.sql.ResultSet;

public final class DruidPooledResultSet extends PoolableWrapper implements ResultSet
{
    private final ResultSet rs;
    private final DruidPooledStatement stmt;
    protected boolean closed;
    protected int cursorIndex;
    protected int fetchRowCount;
    
    public DruidPooledResultSet(final DruidPooledStatement stmt, final ResultSet rs) {
        super(rs);
        this.closed = false;
        this.cursorIndex = 0;
        this.fetchRowCount = 0;
        this.stmt = stmt;
        this.rs = rs;
    }
    
    protected SQLException checkException(final Throwable error) throws SQLException {
        return this.stmt.checkException(error);
    }
    
    public DruidPooledStatement getPoolableStatement() {
        return this.stmt;
    }
    
    public ResultSet getRawResultSet() {
        return this.rs;
    }
    
    @Override
    public boolean next() throws SQLException {
        try {
            final boolean moreRows = this.rs.next();
            if (moreRows) {
                ++this.cursorIndex;
                if (this.cursorIndex > this.fetchRowCount) {
                    this.fetchRowCount = this.cursorIndex;
                }
            }
            return moreRows;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void close() throws SQLException {
        try {
            this.closed = true;
            this.rs.close();
            this.stmt.recordFetchRowCount(this.fetchRowCount);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    public int getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        try {
            return this.rs.wasNull();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getString(final int columnIndex) throws SQLException {
        try {
            return this.rs.getString(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        try {
            return this.rs.getBoolean(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        try {
            return this.rs.getByte(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public short getShort(final int columnIndex) throws SQLException {
        try {
            return this.rs.getShort(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getInt(final int columnIndex) throws SQLException {
        try {
            return this.rs.getInt(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public long getLong(final int columnIndex) throws SQLException {
        try {
            return this.rs.getLong(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        try {
            return this.rs.getFloat(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        try {
            return this.rs.getDouble(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        try {
            return this.rs.getBigDecimal(columnIndex, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        try {
            return this.rs.getBytes(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        try {
            return this.rs.getDate(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        try {
            return this.rs.getTime(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        try {
            return this.rs.getTimestamp(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        try {
            return this.rs.getAsciiStream(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        try {
            return this.rs.getUnicodeStream(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        try {
            return this.rs.getBinaryStream(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getString(final String columnLabel) throws SQLException {
        try {
            return this.rs.getString(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        try {
            return this.rs.getBoolean(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        try {
            return this.rs.getByte(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public short getShort(final String columnLabel) throws SQLException {
        try {
            return this.rs.getShort(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getInt(final String columnLabel) throws SQLException {
        try {
            return this.rs.getInt(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public long getLong(final String columnLabel) throws SQLException {
        try {
            return this.rs.getLong(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        try {
            return this.rs.getFloat(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        try {
            return this.rs.getDouble(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        try {
            return this.rs.getBigDecimal(columnLabel, scale);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        try {
            return this.rs.getBytes(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        try {
            return this.rs.getDate(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        try {
            return this.rs.getTime(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        try {
            return this.rs.getTimestamp(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        try {
            return this.rs.getAsciiStream(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        try {
            return this.rs.getUnicodeStream(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        try {
            return this.rs.getBinaryStream(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return this.rs.getWarnings();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        try {
            this.rs.clearWarnings();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getCursorName() throws SQLException {
        try {
            return this.rs.getCursorName();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return this.rs.getMetaData();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        try {
            return this.rs.getObject(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        try {
            return this.rs.getObject(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        try {
            return this.rs.findColumn(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        try {
            return this.rs.getCharacterStream(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        try {
            return this.rs.getCharacterStream(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        try {
            return this.rs.getBigDecimal(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        try {
            return this.rs.getBigDecimal(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isBeforeFirst() throws SQLException {
        try {
            return this.rs.isBeforeFirst();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isAfterLast() throws SQLException {
        try {
            return this.rs.isAfterLast();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isFirst() throws SQLException {
        try {
            return this.rs.isFirst();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isLast() throws SQLException {
        try {
            return this.rs.isLast();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void beforeFirst() throws SQLException {
        try {
            this.rs.beforeFirst();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void afterLast() throws SQLException {
        try {
            this.rs.afterLast();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean first() throws SQLException {
        try {
            return this.rs.first();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean last() throws SQLException {
        try {
            return this.rs.last();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getRow() throws SQLException {
        try {
            return this.rs.getRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean absolute(final int row) throws SQLException {
        try {
            return this.rs.absolute(row);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean relative(final int rows) throws SQLException {
        try {
            return this.rs.relative(rows);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean previous() throws SQLException {
        try {
            final boolean moreRows = this.rs.previous();
            if (moreRows) {
                --this.cursorIndex;
            }
            return moreRows;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        try {
            this.rs.setFetchDirection(direction);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return this.rs.getFetchDirection();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        try {
            this.rs.setFetchSize(rows);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        try {
            return this.rs.getFetchSize();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getType() throws SQLException {
        try {
            return this.rs.getType();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getConcurrency() throws SQLException {
        try {
            return this.rs.getConcurrency();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean rowUpdated() throws SQLException {
        try {
            return this.rs.rowUpdated();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean rowInserted() throws SQLException {
        try {
            return this.rs.rowInserted();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean rowDeleted() throws SQLException {
        try {
            return this.rs.rowDeleted();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        try {
            this.rs.updateNull(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        try {
            this.rs.updateBoolean(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        try {
            this.rs.updateByte(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        try {
            this.rs.updateShort(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        try {
            this.rs.updateInt(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        try {
            this.rs.updateLong(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        try {
            this.rs.updateFloat(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        try {
            this.rs.updateDouble(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        try {
            this.rs.updateBigDecimal(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        try {
            this.rs.updateString(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        try {
            this.rs.updateBytes(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        try {
            this.rs.updateDate(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        try {
            this.rs.updateTime(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        try {
            this.rs.updateTimestamp(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        try {
            this.rs.updateObject(columnIndex, x, scaleOrLength);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        try {
            this.rs.updateObject(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        try {
            this.rs.updateNull(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        try {
            this.rs.updateBoolean(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateByte(final String columnLabel, final byte x) throws SQLException {
        try {
            this.rs.updateByte(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateShort(final String columnLabel, final short x) throws SQLException {
        try {
            this.rs.updateShort(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateInt(final String columnLabel, final int x) throws SQLException {
        try {
            this.rs.updateInt(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateLong(final String columnLabel, final long x) throws SQLException {
        try {
            this.rs.updateLong(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateFloat(final String columnLabel, final float x) throws SQLException {
        try {
            this.rs.updateFloat(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateDouble(final String columnLabel, final double x) throws SQLException {
        try {
            this.rs.updateDouble(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        try {
            this.rs.updateBigDecimal(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateString(final String columnLabel, final String x) throws SQLException {
        try {
            this.rs.updateString(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        try {
            this.rs.updateBytes(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateDate(final String columnLabel, final Date x) throws SQLException {
        try {
            this.rs.updateDate(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateTime(final String columnLabel, final Time x) throws SQLException {
        try {
            this.rs.updateTime(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        try {
            this.rs.updateTimestamp(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnLabel, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnLabel, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnLabel, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        try {
            this.rs.updateObject(columnLabel, x, scaleOrLength);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x) throws SQLException {
        try {
            this.rs.updateObject(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void insertRow() throws SQLException {
        try {
            this.rs.insertRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateRow() throws SQLException {
        try {
            this.rs.updateRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void deleteRow() throws SQLException {
        try {
            this.rs.deleteRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void refreshRow() throws SQLException {
        try {
            this.rs.refreshRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void cancelRowUpdates() throws SQLException {
        try {
            this.rs.cancelRowUpdates();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void moveToInsertRow() throws SQLException {
        try {
            this.rs.moveToInsertRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void moveToCurrentRow() throws SQLException {
        try {
            this.rs.moveToCurrentRow();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Statement getStatement() {
        return this.stmt;
    }
    
    @Override
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        try {
            return this.rs.getObject(columnIndex, map);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        try {
            return this.rs.getRef(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        try {
            return this.rs.getBlob(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        try {
            return this.rs.getClob(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        try {
            return this.rs.getArray(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        try {
            return this.rs.getObject(columnLabel, map);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        try {
            return this.rs.getRef(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        try {
            return this.rs.getBlob(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        try {
            return this.rs.getClob(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        try {
            return this.rs.getArray(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        try {
            return this.rs.getDate(columnIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        try {
            return this.rs.getDate(columnLabel, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        try {
            return this.rs.getTime(columnIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        try {
            return this.rs.getTime(columnLabel, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        try {
            return this.rs.getTimestamp(columnIndex, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        try {
            return this.rs.getTimestamp(columnLabel, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        try {
            return this.rs.getURL(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        try {
            return this.rs.getURL(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        try {
            this.rs.updateRef(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException {
        try {
            this.rs.updateRef(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        try {
            this.rs.updateBlob(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        try {
            this.rs.updateBlob(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        try {
            this.rs.updateClob(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException {
        try {
            this.rs.updateClob(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        try {
            this.rs.updateArray(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException {
        try {
            this.rs.updateArray(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        try {
            return this.rs.getRowId(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        try {
            return this.rs.getRowId(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        try {
            this.rs.updateRowId(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        try {
            this.rs.updateRowId(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int getHoldability() throws SQLException {
        try {
            return this.rs.getHoldability();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }
    
    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException {
        try {
            this.rs.updateNString(columnIndex, nString);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNString(final String columnLabel, final String nString) throws SQLException {
        try {
            this.rs.updateNString(columnLabel, nString);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        try {
            this.rs.updateNClob(columnIndex, nClob);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
        try {
            this.rs.updateNClob(columnLabel, nClob);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        try {
            return this.rs.getNClob(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        try {
            return this.rs.getNClob(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        try {
            return this.rs.getSQLXML(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        try {
            return this.rs.getSQLXML(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
        try {
            this.rs.updateSQLXML(columnIndex, xmlObject);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
        try {
            this.rs.updateSQLXML(columnLabel, xmlObject);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getNString(final int columnIndex) throws SQLException {
        try {
            return this.rs.getNString(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public String getNString(final String columnLabel) throws SQLException {
        try {
            return this.rs.getNString(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        try {
            return this.rs.getNCharacterStream(columnIndex);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        try {
            return this.rs.getNCharacterStream(columnLabel);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        try {
            this.rs.updateNCharacterStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateNCharacterStream(columnLabel, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnLabel, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnLabel, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnLabel, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        try {
            this.rs.updateBlob(columnIndex, inputStream, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
        try {
            this.rs.updateBlob(columnLabel, inputStream, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateClob(columnIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateClob(columnLabel, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateNClob(columnIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        try {
            this.rs.updateNClob(columnLabel, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        try {
            this.rs.updateNCharacterStream(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        try {
            this.rs.updateNCharacterStream(columnLabel, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        try {
            this.rs.updateAsciiStream(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        try {
            this.rs.updateBinaryStream(columnLabel, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        try {
            this.rs.updateCharacterStream(columnLabel, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
        try {
            this.rs.updateBlob(columnIndex, inputStream);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
        try {
            this.rs.updateBlob(columnLabel, inputStream);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
        try {
            this.rs.updateClob(columnIndex, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
        try {
            this.rs.updateClob(columnLabel, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
        try {
            this.rs.updateNClob(columnIndex, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
        try {
            this.rs.updateNClob(columnLabel, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        try {
            return this.rs.getObject(columnIndex, type);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        try {
            return this.rs.getObject(columnLabel, type);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
}
