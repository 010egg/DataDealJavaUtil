// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLType;
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
import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Wrapper;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Statement;

public class HikariProxyCallableStatement extends ProxyCallableStatement implements Statement, CallableStatement, PreparedStatement, Wrapper, AutoCloseable
{
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        try {
            return super.executeQuery(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        try {
            return super.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getMaxFieldSize() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getMaxFieldSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxFieldSize(final int maxFieldSize) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setMaxFieldSize(maxFieldSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getMaxRows() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxRows(final int maxRows) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setMaxRows(maxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setEscapeProcessing(final boolean escapeProcessing) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setEscapeProcessing(escapeProcessing);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getQueryTimeout() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getQueryTimeout();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setQueryTimeout(final int queryTimeout) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setQueryTimeout(queryTimeout);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void cancel() throws SQLException {
        try {
            ((CallableStatement)super.delegate).cancel();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        try {
            ((CallableStatement)super.delegate).clearWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCursorName(final String cursorName) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCursorName(cursorName);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql) throws SQLException {
        try {
            return super.execute(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        try {
            return super.getResultSet();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getUpdateCount() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getMoreResults() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getMoreResults();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchDirection(final int fetchDirection) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setFetchDirection(fetchDirection);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFetchDirection();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchSize(final int fetchSize) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setFetchSize(fetchSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFetchSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetConcurrency() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getResultSetConcurrency();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetType() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getResultSetType();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void addBatch(final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).addBatch(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearBatch() throws SQLException {
        try {
            ((CallableStatement)super.delegate).clearBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return super.executeBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        try {
            return super.getConnection();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getMoreResults(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getMoreResults(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return super.getGeneratedKeys();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.executeUpdate(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.executeUpdate(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.execute(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.execute(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.execute(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetHoldability() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getResultSetHoldability();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).isClosed();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setPoolable(poolable);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).isPoolable();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        try {
            ((CallableStatement)super.delegate).closeOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).isCloseOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeUpdateCount() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLargeUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLargeMaxRows(final long largeMaxRows) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setLargeMaxRows(largeMaxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeMaxRows() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLargeMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long[] executeLargeBatch() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeUpdate(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeUpdate(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeUpdate(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int n, final int n2, final int n3) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2, n3);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).wasNull();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public String getString(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getString(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getBoolean(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBoolean(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public byte getByte(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getByte(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public short getShort(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getShort(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getInt(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getInt(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLong(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLong(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public float getFloat(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFloat(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public double getDouble(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDouble(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final int n, final int n2) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public byte[] getBytes(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBytes(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Date getDate(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Time getTime(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final int n, final Map map) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n, map);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Ref getRef(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRef(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Blob getBlob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBlob(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Clob getClob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getClob(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Array getArray(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getArray(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Date getDate(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(n, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Time getTime(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(n, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(n, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int n, final int n2, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String s, final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String s, final int n, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n, s2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public URL getURL(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getURL(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setURL(final String s, final URL url) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setURL(s, url);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNull(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBoolean(final String s, final boolean b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBoolean(s, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setByte(final String s, final byte b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setByte(s, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setShort(final String s, final short n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setShort(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setInt(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setInt(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLong(final String s, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setLong(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFloat(final String s, final float n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setFloat(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDouble(final String s, final double n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDouble(s, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBigDecimal(final String s, final BigDecimal bigDecimal) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBigDecimal(s, bigDecimal);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setString(final String s, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setString(s, s2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBytes(final String s, final byte[] array) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBytes(s, array);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final String s, final Date date) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(s, date);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final String s, final Time time) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(s, time);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final String s, final Timestamp timestamp) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(s, timestamp);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final String s, final Object o, final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final String s, final Object o, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final String s, final Object o) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final String s, final Reader reader, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final String s, final Date date, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(s, date, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final String s, final Time time, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(s, time, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final String s, final Timestamp timestamp, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(s, timestamp, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNull(final String s, final int n, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(s, n, s2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public String getString(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getString(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getBoolean(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBoolean(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public byte getByte(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getByte(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public short getShort(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getShort(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getInt(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getInt(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLong(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLong(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public float getFloat(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFloat(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public double getDouble(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDouble(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public byte[] getBytes(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBytes(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Date getDate(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Time getTime(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public BigDecimal getBigDecimal(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final String s, final Map map) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s, map);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Ref getRef(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRef(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Blob getBlob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBlob(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Clob getClob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getClob(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Array getArray(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getArray(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Date getDate(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(s, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Time getTime(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(s, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Timestamp getTimestamp(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(s, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public URL getURL(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getURL(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public RowId getRowId(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRowId(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public RowId getRowId(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRowId(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setRowId(final String s, final RowId rowId) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setRowId(s, rowId);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNString(final String s, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNString(s, s2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(s, reader, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final String s, final NClob nClob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, nClob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, reader, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, inputStream, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, reader, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public NClob getNClob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNClob(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public NClob getNClob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNClob(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setSQLXML(final String s, final SQLXML sqlxml) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setSQLXML(s, sqlxml);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getSQLXML(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public SQLXML getSQLXML(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getSQLXML(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public String getNString(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNString(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public String getNString(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNString(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNCharacterStream(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Reader getNCharacterStream(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNCharacterStream(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Reader getCharacterStream(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getCharacterStream(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Reader getCharacterStream(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getCharacterStream(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final String s, final Blob blob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, blob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final String s, final Clob clob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, clob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader, n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(s, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final int n, final Class clazz) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n, (Class<Object>)clazz);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Object getObject(final String s, final Class clazz) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s, (Class<Object>)clazz);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final SQLType targetSqlType, final int scaleOrLength) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(parameterName, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final String parameterName, final Object x, final SQLType targetSqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(parameterName, x, targetSqlType);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterIndex, sqlType);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final int scale) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterIndex, sqlType, scale);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final int parameterIndex, final SQLType sqlType, final String typeName) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterIndex, sqlType, typeName);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterName, sqlType);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final int scale) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterName, sqlType, scale);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void registerOutParameter(final String parameterName, final SQLType sqlType, final String typeName) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(parameterName, sqlType, typeName);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            return super.executeQuery();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        try {
            return super.executeUpdate();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNull(final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBoolean(final int n, final boolean b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBoolean(n, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setByte(final int n, final byte b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setByte(n, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setShort(final int n, final short n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setShort(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setInt(final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setInt(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLong(final int n, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setLong(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFloat(final int n, final float n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setFloat(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDouble(final int n, final double n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDouble(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBigDecimal(final int n, final BigDecimal bigDecimal) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBigDecimal(n, bigDecimal);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setString(final int n, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setString(n, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBytes(final int n, final byte[] array) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBytes(n, array);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final int n, final Date date) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(n, date);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final int n, final Time time) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(n, time);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final int n, final Timestamp timestamp) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(n, timestamp);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setUnicodeStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setUnicodeStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearParameters() throws SQLException {
        try {
            ((CallableStatement)super.delegate).clearParameters();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(n, o, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(n, o);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute() throws SQLException {
        try {
            return super.execute();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void addBatch() throws SQLException {
        try {
            ((CallableStatement)super.delegate).addBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setRef(final int n, final Ref ref) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setRef(n, ref);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final Blob blob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(n, blob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Clob clob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(n, clob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setArray(final int n, final Array array) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setArray(n, array);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getMetaData();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final int n, final Date date, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(n, date, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final int n, final Time time, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(n, time, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final int n, final Timestamp timestamp, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(n, timestamp, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNull(final int n, final int n2, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(n, n2, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setURL(final int n, final URL url) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setURL(n, url);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getParameterMetaData();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setRowId(final int n, final RowId rowId) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setRowId(n, rowId);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNString(final int n, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNString(n, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final NClob nClob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(n, nClob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setSQLXML(final int n, final SQLXML sqlxml) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setSQLXML(n, sqlxml);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o, final int n2, final int n3) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(n, o, n2, n3);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final SQLType targetSqlType, final int scaleOrLength) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final SQLType targetSqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).executeLargeUpdate();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).isWrapperFor(clazz);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    protected HikariProxyCallableStatement(final ProxyConnection connection, final CallableStatement statement) {
        super(connection, statement);
    }
}
