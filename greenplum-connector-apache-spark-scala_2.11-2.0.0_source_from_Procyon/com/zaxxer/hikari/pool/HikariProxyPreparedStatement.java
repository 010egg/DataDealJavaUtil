// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.SQLType;
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
import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Wrapper;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class HikariProxyPreparedStatement extends ProxyPreparedStatement implements Statement, PreparedStatement, Wrapper, AutoCloseable
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
            return ((PreparedStatement)super.delegate).getMaxFieldSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxFieldSize(final int maxFieldSize) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setMaxFieldSize(maxFieldSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getMaxRows() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxRows(final int maxRows) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setMaxRows(maxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setEscapeProcessing(final boolean escapeProcessing) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setEscapeProcessing(escapeProcessing);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getQueryTimeout() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getQueryTimeout();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setQueryTimeout(final int queryTimeout) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setQueryTimeout(queryTimeout);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void cancel() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).cancel();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).clearWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCursorName(final String cursorName) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCursorName(cursorName);
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
            return ((PreparedStatement)super.delegate).getUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getMoreResults() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getMoreResults();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchDirection(final int fetchDirection) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setFetchDirection(fetchDirection);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getFetchDirection();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchSize(final int fetchSize) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setFetchSize(fetchSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getFetchSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetConcurrency() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getResultSetConcurrency();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetType() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getResultSetType();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void addBatch(final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).addBatch(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearBatch() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).clearBatch();
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
            return ((PreparedStatement)super.delegate).getMoreResults(n);
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
            return ((PreparedStatement)super.delegate).getResultSetHoldability();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).isClosed();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setPoolable(poolable);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).isPoolable();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).closeOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).isCloseOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeUpdateCount() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getLargeUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLargeMaxRows(final long largeMaxRows) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setLargeMaxRows(largeMaxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeMaxRows() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getLargeMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long[] executeLargeBatch() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql) throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate(sql, columnNames);
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
            ((PreparedStatement)super.delegate).setNull(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBoolean(final int n, final boolean b) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBoolean(n, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setByte(final int n, final byte b) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setByte(n, b);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setShort(final int n, final short n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setShort(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setInt(final int n, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setInt(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLong(final int n, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setLong(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFloat(final int n, final float n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setFloat(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDouble(final int n, final double n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDouble(n, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBigDecimal(final int n, final BigDecimal bigDecimal) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBigDecimal(n, bigDecimal);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setString(final int n, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setString(n, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBytes(final int n, final byte[] array) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBytes(n, array);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final int n, final Date date) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDate(n, date);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final int n, final Time time) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTime(n, time);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final int n, final Timestamp timestamp) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTimestamp(n, timestamp);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setUnicodeStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setUnicodeStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearParameters() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).clearParameters();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o);
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
            ((PreparedStatement)super.delegate).addBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setRef(final int n, final Ref ref) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setRef(n, ref);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final Blob blob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, blob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Clob clob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, clob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setArray(final int n, final Array array) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setArray(n, array);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getMetaData();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setDate(final int n, final Date date, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDate(n, date, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTime(final int n, final Time time, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTime(n, time, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setTimestamp(final int n, final Timestamp timestamp, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTimestamp(n, timestamp, calendar);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNull(final int n, final int n2, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNull(n, n2, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setURL(final int n, final URL url) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setURL(n, url);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getParameterMetaData();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setRowId(final int n, final RowId rowId) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setRowId(n, rowId);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNString(final int n, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNString(n, s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final NClob nClob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, nClob);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setSQLXML(final int n, final SQLXML sqlxml) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setSQLXML(n, sqlxml);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int n, final Object o, final int n2, final int n3) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, n2, n3);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setAsciiStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBinaryStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNCharacterStream(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setClob(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setBlob(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, inputStream);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setNClob(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, reader);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final SQLType targetSqlType, final int scaleOrLength) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final SQLType targetSqlType) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(parameterIndex, x, targetSqlType);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).isWrapperFor(clazz);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    HikariProxyPreparedStatement(final ProxyConnection connection, final PreparedStatement statement) {
        super(connection, statement);
    }
}
