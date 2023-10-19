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
import java.sql.ParameterMetaData;
import java.sql.Connection;
import java.sql.RowId;
import java.net.URL;
import java.util.Calendar;
import java.sql.Ref;
import java.sql.Statement;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import java.util.concurrent.Executor;
import java.sql.Struct;
import java.sql.Array;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Savepoint;
import java.util.Map;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.Wrapper;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;

public interface FilterChain
{
    DataSourceProxy getDataSource();
    
    int getFilterSize();
    
    int getPos();
    
    FilterChain cloneChain();
    
     <T> T unwrap(final Wrapper p0, final Class<T> p1) throws SQLException;
    
    boolean isWrapperFor(final Wrapper p0, final Class<?> p1) throws SQLException;
    
    ConnectionProxy connection_connect(final Properties p0) throws SQLException;
    
    StatementProxy connection_createStatement(final ConnectionProxy p0) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final ConnectionProxy p0, final String p1) throws SQLException;
    
    String connection_nativeSQL(final ConnectionProxy p0, final String p1) throws SQLException;
    
    void connection_setAutoCommit(final ConnectionProxy p0, final boolean p1) throws SQLException;
    
    boolean connection_getAutoCommit(final ConnectionProxy p0) throws SQLException;
    
    void connection_commit(final ConnectionProxy p0) throws SQLException;
    
    void connection_rollback(final ConnectionProxy p0) throws SQLException;
    
    void connection_close(final ConnectionProxy p0) throws SQLException;
    
    boolean connection_isClosed(final ConnectionProxy p0) throws SQLException;
    
    DatabaseMetaData connection_getMetaData(final ConnectionProxy p0) throws SQLException;
    
    void connection_setReadOnly(final ConnectionProxy p0, final boolean p1) throws SQLException;
    
    boolean connection_isReadOnly(final ConnectionProxy p0) throws SQLException;
    
    void connection_setCatalog(final ConnectionProxy p0, final String p1) throws SQLException;
    
    String connection_getCatalog(final ConnectionProxy p0) throws SQLException;
    
    void connection_setTransactionIsolation(final ConnectionProxy p0, final int p1) throws SQLException;
    
    int connection_getTransactionIsolation(final ConnectionProxy p0) throws SQLException;
    
    SQLWarning connection_getWarnings(final ConnectionProxy p0) throws SQLException;
    
    void connection_clearWarnings(final ConnectionProxy p0) throws SQLException;
    
    StatementProxy connection_createStatement(final ConnectionProxy p0, final int p1, final int p2) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1, final int p2, final int p3) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final ConnectionProxy p0, final String p1, final int p2, final int p3) throws SQLException;
    
    Map<String, Class<?>> connection_getTypeMap(final ConnectionProxy p0) throws SQLException;
    
    void connection_setTypeMap(final ConnectionProxy p0, final Map<String, Class<?>> p1) throws SQLException;
    
    void connection_setHoldability(final ConnectionProxy p0, final int p1) throws SQLException;
    
    int connection_getHoldability(final ConnectionProxy p0) throws SQLException;
    
    Savepoint connection_setSavepoint(final ConnectionProxy p0) throws SQLException;
    
    Savepoint connection_setSavepoint(final ConnectionProxy p0, final String p1) throws SQLException;
    
    void connection_rollback(final ConnectionProxy p0, final Savepoint p1) throws SQLException;
    
    void connection_releaseSavepoint(final ConnectionProxy p0, final Savepoint p1) throws SQLException;
    
    StatementProxy connection_createStatement(final ConnectionProxy p0, final int p1, final int p2, final int p3) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1, final int p2, final int p3, final int p4) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final ConnectionProxy p0, final String p1, final int p2, final int p3, final int p4) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1, final int p2) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1, final int[] p2) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final ConnectionProxy p0, final String p1, final String[] p2) throws SQLException;
    
    Clob connection_createClob(final ConnectionProxy p0) throws SQLException;
    
    Blob connection_createBlob(final ConnectionProxy p0) throws SQLException;
    
    NClob connection_createNClob(final ConnectionProxy p0) throws SQLException;
    
    SQLXML connection_createSQLXML(final ConnectionProxy p0) throws SQLException;
    
    boolean connection_isValid(final ConnectionProxy p0, final int p1) throws SQLException;
    
    void connection_setClientInfo(final ConnectionProxy p0, final String p1, final String p2) throws SQLClientInfoException;
    
    void connection_setClientInfo(final ConnectionProxy p0, final Properties p1) throws SQLClientInfoException;
    
    String connection_getClientInfo(final ConnectionProxy p0, final String p1) throws SQLException;
    
    Properties connection_getClientInfo(final ConnectionProxy p0) throws SQLException;
    
    Array connection_createArrayOf(final ConnectionProxy p0, final String p1, final Object[] p2) throws SQLException;
    
    Struct connection_createStruct(final ConnectionProxy p0, final String p1, final Object[] p2) throws SQLException;
    
    String connection_getSchema(final ConnectionProxy p0) throws SQLException;
    
    void connection_setSchema(final ConnectionProxy p0, final String p1) throws SQLException;
    
    void connection_abort(final ConnectionProxy p0, final Executor p1) throws SQLException;
    
    void connection_setNetworkTimeout(final ConnectionProxy p0, final Executor p1, final int p2) throws SQLException;
    
    int connection_getNetworkTimeout(final ConnectionProxy p0) throws SQLException;
    
    boolean resultSet_next(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_close(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_wasNull(final ResultSetProxy p0) throws SQLException;
    
    String resultSet_getString(final ResultSetProxy p0, final int p1) throws SQLException;
    
    boolean resultSet_getBoolean(final ResultSetProxy p0, final int p1) throws SQLException;
    
    byte resultSet_getByte(final ResultSetProxy p0, final int p1) throws SQLException;
    
    short resultSet_getShort(final ResultSetProxy p0, final int p1) throws SQLException;
    
    int resultSet_getInt(final ResultSetProxy p0, final int p1) throws SQLException;
    
    long resultSet_getLong(final ResultSetProxy p0, final int p1) throws SQLException;
    
    float resultSet_getFloat(final ResultSetProxy p0, final int p1) throws SQLException;
    
    double resultSet_getDouble(final ResultSetProxy p0, final int p1) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final ResultSetProxy p0, final int p1, final int p2) throws SQLException;
    
    byte[] resultSet_getBytes(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Date resultSet_getDate(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Time resultSet_getTime(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final ResultSetProxy p0, final int p1) throws SQLException;
    
    InputStream resultSet_getAsciiStream(final ResultSetProxy p0, final int p1) throws SQLException;
    
    InputStream resultSet_getUnicodeStream(final ResultSetProxy p0, final int p1) throws SQLException;
    
    InputStream resultSet_getBinaryStream(final ResultSetProxy p0, final int p1) throws SQLException;
    
    String resultSet_getString(final ResultSetProxy p0, final String p1) throws SQLException;
    
    boolean resultSet_getBoolean(final ResultSetProxy p0, final String p1) throws SQLException;
    
    byte resultSet_getByte(final ResultSetProxy p0, final String p1) throws SQLException;
    
    short resultSet_getShort(final ResultSetProxy p0, final String p1) throws SQLException;
    
    int resultSet_getInt(final ResultSetProxy p0, final String p1) throws SQLException;
    
    long resultSet_getLong(final ResultSetProxy p0, final String p1) throws SQLException;
    
    float resultSet_getFloat(final ResultSetProxy p0, final String p1) throws SQLException;
    
    double resultSet_getDouble(final ResultSetProxy p0, final String p1) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final ResultSetProxy p0, final String p1, final int p2) throws SQLException;
    
    byte[] resultSet_getBytes(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Date resultSet_getDate(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Time resultSet_getTime(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final ResultSetProxy p0, final String p1) throws SQLException;
    
    InputStream resultSet_getAsciiStream(final ResultSetProxy p0, final String p1) throws SQLException;
    
    InputStream resultSet_getUnicodeStream(final ResultSetProxy p0, final String p1) throws SQLException;
    
    InputStream resultSet_getBinaryStream(final ResultSetProxy p0, final String p1) throws SQLException;
    
    SQLWarning resultSet_getWarnings(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_clearWarnings(final ResultSetProxy p0) throws SQLException;
    
    String resultSet_getCursorName(final ResultSetProxy p0) throws SQLException;
    
    ResultSetMetaData resultSet_getMetaData(final ResultSetProxy p0) throws SQLException;
    
    Object resultSet_getObject(final ResultSetProxy p0, final int p1) throws SQLException;
    
     <T> T resultSet_getObject(final ResultSetProxy p0, final int p1, final Class<T> p2) throws SQLException;
    
    Object resultSet_getObject(final ResultSetProxy p0, final String p1) throws SQLException;
    
     <T> T resultSet_getObject(final ResultSetProxy p0, final String p1, final Class<T> p2) throws SQLException;
    
    int resultSet_findColumn(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Reader resultSet_getCharacterStream(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Reader resultSet_getCharacterStream(final ResultSetProxy p0, final String p1) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final ResultSetProxy p0, final int p1) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final ResultSetProxy p0, final String p1) throws SQLException;
    
    boolean resultSet_isBeforeFirst(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_isAfterLast(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_isFirst(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_isLast(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_beforeFirst(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_afterLast(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_first(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_last(final ResultSetProxy p0) throws SQLException;
    
    int resultSet_getRow(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_absolute(final ResultSetProxy p0, final int p1) throws SQLException;
    
    boolean resultSet_relative(final ResultSetProxy p0, final int p1) throws SQLException;
    
    boolean resultSet_previous(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_setFetchDirection(final ResultSetProxy p0, final int p1) throws SQLException;
    
    int resultSet_getFetchDirection(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_setFetchSize(final ResultSetProxy p0, final int p1) throws SQLException;
    
    int resultSet_getFetchSize(final ResultSetProxy p0) throws SQLException;
    
    int resultSet_getType(final ResultSetProxy p0) throws SQLException;
    
    int resultSet_getConcurrency(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_rowUpdated(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_rowInserted(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_rowDeleted(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_updateNull(final ResultSetProxy p0, final int p1) throws SQLException;
    
    void resultSet_updateBoolean(final ResultSetProxy p0, final int p1, final boolean p2) throws SQLException;
    
    void resultSet_updateByte(final ResultSetProxy p0, final int p1, final byte p2) throws SQLException;
    
    void resultSet_updateShort(final ResultSetProxy p0, final int p1, final short p2) throws SQLException;
    
    void resultSet_updateInt(final ResultSetProxy p0, final int p1, final int p2) throws SQLException;
    
    void resultSet_updateLong(final ResultSetProxy p0, final int p1, final long p2) throws SQLException;
    
    void resultSet_updateFloat(final ResultSetProxy p0, final int p1, final float p2) throws SQLException;
    
    void resultSet_updateDouble(final ResultSetProxy p0, final int p1, final double p2) throws SQLException;
    
    void resultSet_updateBigDecimal(final ResultSetProxy p0, final int p1, final BigDecimal p2) throws SQLException;
    
    void resultSet_updateString(final ResultSetProxy p0, final int p1, final String p2) throws SQLException;
    
    void resultSet_updateBytes(final ResultSetProxy p0, final int p1, final byte[] p2) throws SQLException;
    
    void resultSet_updateDate(final ResultSetProxy p0, final int p1, final Date p2) throws SQLException;
    
    void resultSet_updateTime(final ResultSetProxy p0, final int p1, final Time p2) throws SQLException;
    
    void resultSet_updateTimestamp(final ResultSetProxy p0, final int p1, final Timestamp p2) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final int p1, final InputStream p2, final int p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final int p1, final InputStream p2, final int p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final int p1, final Reader p2, final int p3) throws SQLException;
    
    void resultSet_updateObject(final ResultSetProxy p0, final int p1, final Object p2, final int p3) throws SQLException;
    
    void resultSet_updateObject(final ResultSetProxy p0, final int p1, final Object p2) throws SQLException;
    
    void resultSet_updateNull(final ResultSetProxy p0, final String p1) throws SQLException;
    
    void resultSet_updateBoolean(final ResultSetProxy p0, final String p1, final boolean p2) throws SQLException;
    
    void resultSet_updateByte(final ResultSetProxy p0, final String p1, final byte p2) throws SQLException;
    
    void resultSet_updateShort(final ResultSetProxy p0, final String p1, final short p2) throws SQLException;
    
    void resultSet_updateInt(final ResultSetProxy p0, final String p1, final int p2) throws SQLException;
    
    void resultSet_updateLong(final ResultSetProxy p0, final String p1, final long p2) throws SQLException;
    
    void resultSet_updateFloat(final ResultSetProxy p0, final String p1, final float p2) throws SQLException;
    
    void resultSet_updateDouble(final ResultSetProxy p0, final String p1, final double p2) throws SQLException;
    
    void resultSet_updateBigDecimal(final ResultSetProxy p0, final String p1, final BigDecimal p2) throws SQLException;
    
    void resultSet_updateString(final ResultSetProxy p0, final String p1, final String p2) throws SQLException;
    
    void resultSet_updateBytes(final ResultSetProxy p0, final String p1, final byte[] p2) throws SQLException;
    
    void resultSet_updateDate(final ResultSetProxy p0, final String p1, final Date p2) throws SQLException;
    
    void resultSet_updateTime(final ResultSetProxy p0, final String p1, final Time p2) throws SQLException;
    
    void resultSet_updateTimestamp(final ResultSetProxy p0, final String p1, final Timestamp p2) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final String p1, final InputStream p2, final int p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final String p1, final InputStream p2, final int p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final String p1, final Reader p2, final int p3) throws SQLException;
    
    void resultSet_updateObject(final ResultSetProxy p0, final String p1, final Object p2, final int p3) throws SQLException;
    
    void resultSet_updateObject(final ResultSetProxy p0, final String p1, final Object p2) throws SQLException;
    
    void resultSet_insertRow(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_updateRow(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_deleteRow(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_refreshRow(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_cancelRowUpdates(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_moveToInsertRow(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_moveToCurrentRow(final ResultSetProxy p0) throws SQLException;
    
    Statement resultSet_getStatement(final ResultSetProxy p0) throws SQLException;
    
    Object resultSet_getObject(final ResultSetProxy p0, final int p1, final Map<String, Class<?>> p2) throws SQLException;
    
    Ref resultSet_getRef(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Blob resultSet_getBlob(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Clob resultSet_getClob(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Array resultSet_getArray(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Object resultSet_getObject(final ResultSetProxy p0, final String p1, final Map<String, Class<?>> p2) throws SQLException;
    
    Ref resultSet_getRef(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Blob resultSet_getBlob(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Clob resultSet_getClob(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Array resultSet_getArray(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Date resultSet_getDate(final ResultSetProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    Date resultSet_getDate(final ResultSetProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    Time resultSet_getTime(final ResultSetProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    Time resultSet_getTime(final ResultSetProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final ResultSetProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final ResultSetProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    URL resultSet_getURL(final ResultSetProxy p0, final int p1) throws SQLException;
    
    URL resultSet_getURL(final ResultSetProxy p0, final String p1) throws SQLException;
    
    void resultSet_updateRef(final ResultSetProxy p0, final int p1, final Ref p2) throws SQLException;
    
    void resultSet_updateRef(final ResultSetProxy p0, final String p1, final Ref p2) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final int p1, final Blob p2) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final String p1, final Blob p2) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final int p1, final Clob p2) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final String p1, final Clob p2) throws SQLException;
    
    void resultSet_updateArray(final ResultSetProxy p0, final int p1, final Array p2) throws SQLException;
    
    void resultSet_updateArray(final ResultSetProxy p0, final String p1, final Array p2) throws SQLException;
    
    RowId resultSet_getRowId(final ResultSetProxy p0, final int p1) throws SQLException;
    
    RowId resultSet_getRowId(final ResultSetProxy p0, final String p1) throws SQLException;
    
    void resultSet_updateRowId(final ResultSetProxy p0, final int p1, final RowId p2) throws SQLException;
    
    void resultSet_updateRowId(final ResultSetProxy p0, final String p1, final RowId p2) throws SQLException;
    
    int resultSet_getHoldability(final ResultSetProxy p0) throws SQLException;
    
    boolean resultSet_isClosed(final ResultSetProxy p0) throws SQLException;
    
    void resultSet_updateNString(final ResultSetProxy p0, final int p1, final String p2) throws SQLException;
    
    void resultSet_updateNString(final ResultSetProxy p0, final String p1, final String p2) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final int p1, final NClob p2) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final String p1, final NClob p2) throws SQLException;
    
    NClob resultSet_getNClob(final ResultSetProxy p0, final int p1) throws SQLException;
    
    NClob resultSet_getNClob(final ResultSetProxy p0, final String p1) throws SQLException;
    
    SQLXML resultSet_getSQLXML(final ResultSetProxy p0, final int p1) throws SQLException;
    
    SQLXML resultSet_getSQLXML(final ResultSetProxy p0, final String p1) throws SQLException;
    
    void resultSet_updateSQLXML(final ResultSetProxy p0, final int p1, final SQLXML p2) throws SQLException;
    
    void resultSet_updateSQLXML(final ResultSetProxy p0, final String p1, final SQLXML p2) throws SQLException;
    
    String resultSet_getNString(final ResultSetProxy p0, final int p1) throws SQLException;
    
    String resultSet_getNString(final ResultSetProxy p0, final String p1) throws SQLException;
    
    Reader resultSet_getNCharacterStream(final ResultSetProxy p0, final int p1) throws SQLException;
    
    Reader resultSet_getNCharacterStream(final ResultSetProxy p0, final String p1) throws SQLException;
    
    void resultSet_updateNCharacterStream(final ResultSetProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateNCharacterStream(final ResultSetProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void resultSet_updateNCharacterStream(final ResultSetProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void resultSet_updateNCharacterStream(final ResultSetProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void resultSet_updateAsciiStream(final ResultSetProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateBinaryStream(final ResultSetProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateCharacterStream(final ResultSetProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateBlob(final ResultSetProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void resultSet_updateClob(final ResultSetProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void resultSet_updateNClob(final ResultSetProxy p0, final String p1, final Reader p2) throws SQLException;
    
    ResultSetProxy statement_executeQuery(final StatementProxy p0, final String p1) throws SQLException;
    
    int statement_executeUpdate(final StatementProxy p0, final String p1) throws SQLException;
    
    void statement_close(final StatementProxy p0) throws SQLException;
    
    int statement_getMaxFieldSize(final StatementProxy p0) throws SQLException;
    
    void statement_setMaxFieldSize(final StatementProxy p0, final int p1) throws SQLException;
    
    int statement_getMaxRows(final StatementProxy p0) throws SQLException;
    
    void statement_setMaxRows(final StatementProxy p0, final int p1) throws SQLException;
    
    void statement_setEscapeProcessing(final StatementProxy p0, final boolean p1) throws SQLException;
    
    int statement_getQueryTimeout(final StatementProxy p0) throws SQLException;
    
    void statement_setQueryTimeout(final StatementProxy p0, final int p1) throws SQLException;
    
    void statement_cancel(final StatementProxy p0) throws SQLException;
    
    SQLWarning statement_getWarnings(final StatementProxy p0) throws SQLException;
    
    void statement_clearWarnings(final StatementProxy p0) throws SQLException;
    
    void statement_setCursorName(final StatementProxy p0, final String p1) throws SQLException;
    
    boolean statement_execute(final StatementProxy p0, final String p1) throws SQLException;
    
    ResultSetProxy statement_getResultSet(final StatementProxy p0) throws SQLException;
    
    int statement_getUpdateCount(final StatementProxy p0) throws SQLException;
    
    boolean statement_getMoreResults(final StatementProxy p0) throws SQLException;
    
    void statement_setFetchDirection(final StatementProxy p0, final int p1) throws SQLException;
    
    int statement_getFetchDirection(final StatementProxy p0) throws SQLException;
    
    void statement_setFetchSize(final StatementProxy p0, final int p1) throws SQLException;
    
    int statement_getFetchSize(final StatementProxy p0) throws SQLException;
    
    int statement_getResultSetConcurrency(final StatementProxy p0) throws SQLException;
    
    int statement_getResultSetType(final StatementProxy p0) throws SQLException;
    
    void statement_addBatch(final StatementProxy p0, final String p1) throws SQLException;
    
    void statement_clearBatch(final StatementProxy p0) throws SQLException;
    
    int[] statement_executeBatch(final StatementProxy p0) throws SQLException;
    
    Connection statement_getConnection(final StatementProxy p0) throws SQLException;
    
    boolean statement_getMoreResults(final StatementProxy p0, final int p1) throws SQLException;
    
    ResultSetProxy statement_getGeneratedKeys(final StatementProxy p0) throws SQLException;
    
    int statement_executeUpdate(final StatementProxy p0, final String p1, final int p2) throws SQLException;
    
    int statement_executeUpdate(final StatementProxy p0, final String p1, final int[] p2) throws SQLException;
    
    int statement_executeUpdate(final StatementProxy p0, final String p1, final String[] p2) throws SQLException;
    
    boolean statement_execute(final StatementProxy p0, final String p1, final int p2) throws SQLException;
    
    boolean statement_execute(final StatementProxy p0, final String p1, final int[] p2) throws SQLException;
    
    boolean statement_execute(final StatementProxy p0, final String p1, final String[] p2) throws SQLException;
    
    int statement_getResultSetHoldability(final StatementProxy p0) throws SQLException;
    
    boolean statement_isClosed(final StatementProxy p0) throws SQLException;
    
    void statement_setPoolable(final StatementProxy p0, final boolean p1) throws SQLException;
    
    boolean statement_isPoolable(final StatementProxy p0) throws SQLException;
    
    ResultSetProxy preparedStatement_executeQuery(final PreparedStatementProxy p0) throws SQLException;
    
    int preparedStatement_executeUpdate(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_setNull(final PreparedStatementProxy p0, final int p1, final int p2) throws SQLException;
    
    void preparedStatement_setBoolean(final PreparedStatementProxy p0, final int p1, final boolean p2) throws SQLException;
    
    void preparedStatement_setByte(final PreparedStatementProxy p0, final int p1, final byte p2) throws SQLException;
    
    void preparedStatement_setShort(final PreparedStatementProxy p0, final int p1, final short p2) throws SQLException;
    
    void preparedStatement_setInt(final PreparedStatementProxy p0, final int p1, final int p2) throws SQLException;
    
    void preparedStatement_setLong(final PreparedStatementProxy p0, final int p1, final long p2) throws SQLException;
    
    void preparedStatement_setFloat(final PreparedStatementProxy p0, final int p1, final float p2) throws SQLException;
    
    void preparedStatement_setDouble(final PreparedStatementProxy p0, final int p1, final double p2) throws SQLException;
    
    void preparedStatement_setBigDecimal(final PreparedStatementProxy p0, final int p1, final BigDecimal p2) throws SQLException;
    
    void preparedStatement_setString(final PreparedStatementProxy p0, final int p1, final String p2) throws SQLException;
    
    void preparedStatement_setBytes(final PreparedStatementProxy p0, final int p1, final byte[] p2) throws SQLException;
    
    void preparedStatement_setDate(final PreparedStatementProxy p0, final int p1, final Date p2) throws SQLException;
    
    void preparedStatement_setTime(final PreparedStatementProxy p0, final int p1, final Time p2) throws SQLException;
    
    void preparedStatement_setTimestamp(final PreparedStatementProxy p0, final int p1, final Timestamp p2) throws SQLException;
    
    void preparedStatement_setAsciiStream(final PreparedStatementProxy p0, final int p1, final InputStream p2, final int p3) throws SQLException;
    
    void preparedStatement_setUnicodeStream(final PreparedStatementProxy p0, final int p1, final InputStream p2, final int p3) throws SQLException;
    
    void preparedStatement_setBinaryStream(final PreparedStatementProxy p0, final int p1, final InputStream p2, final int p3) throws SQLException;
    
    void preparedStatement_clearParameters(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_setObject(final PreparedStatementProxy p0, final int p1, final Object p2, final int p3) throws SQLException;
    
    void preparedStatement_setObject(final PreparedStatementProxy p0, final int p1, final Object p2) throws SQLException;
    
    boolean preparedStatement_execute(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_addBatch(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_setCharacterStream(final PreparedStatementProxy p0, final int p1, final Reader p2, final int p3) throws SQLException;
    
    void preparedStatement_setRef(final PreparedStatementProxy p0, final int p1, final Ref p2) throws SQLException;
    
    void preparedStatement_setBlob(final PreparedStatementProxy p0, final int p1, final Blob p2) throws SQLException;
    
    void preparedStatement_setClob(final PreparedStatementProxy p0, final int p1, final Clob p2) throws SQLException;
    
    void preparedStatement_setArray(final PreparedStatementProxy p0, final int p1, final Array p2) throws SQLException;
    
    ResultSetMetaData preparedStatement_getMetaData(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_setDate(final PreparedStatementProxy p0, final int p1, final Date p2, final Calendar p3) throws SQLException;
    
    void preparedStatement_setTime(final PreparedStatementProxy p0, final int p1, final Time p2, final Calendar p3) throws SQLException;
    
    void preparedStatement_setTimestamp(final PreparedStatementProxy p0, final int p1, final Timestamp p2, final Calendar p3) throws SQLException;
    
    void preparedStatement_setNull(final PreparedStatementProxy p0, final int p1, final int p2, final String p3) throws SQLException;
    
    void preparedStatement_setURL(final PreparedStatementProxy p0, final int p1, final URL p2) throws SQLException;
    
    ParameterMetaData preparedStatement_getParameterMetaData(final PreparedStatementProxy p0) throws SQLException;
    
    void preparedStatement_setRowId(final PreparedStatementProxy p0, final int p1, final RowId p2) throws SQLException;
    
    void preparedStatement_setNString(final PreparedStatementProxy p0, final int p1, final String p2) throws SQLException;
    
    void preparedStatement_setNCharacterStream(final PreparedStatementProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void preparedStatement_setNClob(final PreparedStatementProxy p0, final int p1, final NClob p2) throws SQLException;
    
    void preparedStatement_setClob(final PreparedStatementProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void preparedStatement_setBlob(final PreparedStatementProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void preparedStatement_setNClob(final PreparedStatementProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void preparedStatement_setSQLXML(final PreparedStatementProxy p0, final int p1, final SQLXML p2) throws SQLException;
    
    void preparedStatement_setObject(final PreparedStatementProxy p0, final int p1, final Object p2, final int p3, final int p4) throws SQLException;
    
    void preparedStatement_setAsciiStream(final PreparedStatementProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void preparedStatement_setBinaryStream(final PreparedStatementProxy p0, final int p1, final InputStream p2, final long p3) throws SQLException;
    
    void preparedStatement_setCharacterStream(final PreparedStatementProxy p0, final int p1, final Reader p2, final long p3) throws SQLException;
    
    void preparedStatement_setAsciiStream(final PreparedStatementProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void preparedStatement_setBinaryStream(final PreparedStatementProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void preparedStatement_setCharacterStream(final PreparedStatementProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void preparedStatement_setNCharacterStream(final PreparedStatementProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void preparedStatement_setClob(final PreparedStatementProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void preparedStatement_setBlob(final PreparedStatementProxy p0, final int p1, final InputStream p2) throws SQLException;
    
    void preparedStatement_setNClob(final PreparedStatementProxy p0, final int p1, final Reader p2) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final int p1, final int p2) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final int p1, final int p2, final int p3) throws SQLException;
    
    boolean callableStatement_wasNull(final CallableStatementProxy p0) throws SQLException;
    
    String callableStatement_getString(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    boolean callableStatement_getBoolean(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    byte callableStatement_getByte(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    short callableStatement_getShort(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    int callableStatement_getInt(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    long callableStatement_getLong(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    float callableStatement_getFloat(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    double callableStatement_getDouble(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy p0, final int p1, final int p2) throws SQLException;
    
    byte[] callableStatement_getBytes(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Date callableStatement_getDate(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Time callableStatement_getTime(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Object callableStatement_getObject(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Object callableStatement_getObject(final CallableStatementProxy p0, final int p1, final Map<String, Class<?>> p2) throws SQLException;
    
    Ref callableStatement_getRef(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Blob callableStatement_getBlob(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Clob callableStatement_getClob(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Array callableStatement_getArray(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Date callableStatement_getDate(final CallableStatementProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    Time callableStatement_getTime(final CallableStatementProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final CallableStatementProxy p0, final int p1, final Calendar p2) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final int p1, final int p2, final String p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final String p1, final int p2) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final String p1, final int p2, final int p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final CallableStatementProxy p0, final String p1, final int p2, final String p3) throws SQLException;
    
    URL callableStatement_getURL(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    void callableStatement_setURL(final CallableStatementProxy p0, final String p1, final URL p2) throws SQLException;
    
    void callableStatement_setNull(final CallableStatementProxy p0, final String p1, final int p2) throws SQLException;
    
    void callableStatement_setBoolean(final CallableStatementProxy p0, final String p1, final boolean p2) throws SQLException;
    
    void callableStatement_setByte(final CallableStatementProxy p0, final String p1, final byte p2) throws SQLException;
    
    void callableStatement_setShort(final CallableStatementProxy p0, final String p1, final short p2) throws SQLException;
    
    void callableStatement_setInt(final CallableStatementProxy p0, final String p1, final int p2) throws SQLException;
    
    void callableStatement_setLong(final CallableStatementProxy p0, final String p1, final long p2) throws SQLException;
    
    void callableStatement_setFloat(final CallableStatementProxy p0, final String p1, final float p2) throws SQLException;
    
    void callableStatement_setDouble(final CallableStatementProxy p0, final String p1, final double p2) throws SQLException;
    
    void callableStatement_setBigDecimal(final CallableStatementProxy p0, final String p1, final BigDecimal p2) throws SQLException;
    
    void callableStatement_setString(final CallableStatementProxy p0, final String p1, final String p2) throws SQLException;
    
    void callableStatement_setBytes(final CallableStatementProxy p0, final String p1, final byte[] p2) throws SQLException;
    
    void callableStatement_setDate(final CallableStatementProxy p0, final String p1, final Date p2) throws SQLException;
    
    void callableStatement_setTime(final CallableStatementProxy p0, final String p1, final Time p2) throws SQLException;
    
    void callableStatement_setTimestamp(final CallableStatementProxy p0, final String p1, final Timestamp p2) throws SQLException;
    
    void callableStatement_setAsciiStream(final CallableStatementProxy p0, final String p1, final InputStream p2, final int p3) throws SQLException;
    
    void callableStatement_setBinaryStream(final CallableStatementProxy p0, final String p1, final InputStream p2, final int p3) throws SQLException;
    
    void callableStatement_setObject(final CallableStatementProxy p0, final String p1, final Object p2, final int p3, final int p4) throws SQLException;
    
    void callableStatement_setObject(final CallableStatementProxy p0, final String p1, final Object p2, final int p3) throws SQLException;
    
    void callableStatement_setObject(final CallableStatementProxy p0, final String p1, final Object p2) throws SQLException;
    
    void callableStatement_setCharacterStream(final CallableStatementProxy p0, final String p1, final Reader p2, final int p3) throws SQLException;
    
    void callableStatement_setDate(final CallableStatementProxy p0, final String p1, final Date p2, final Calendar p3) throws SQLException;
    
    void callableStatement_setTime(final CallableStatementProxy p0, final String p1, final Time p2, final Calendar p3) throws SQLException;
    
    void callableStatement_setTimestamp(final CallableStatementProxy p0, final String p1, final Timestamp p2, final Calendar p3) throws SQLException;
    
    void callableStatement_setNull(final CallableStatementProxy p0, final String p1, final int p2, final String p3) throws SQLException;
    
    String callableStatement_getString(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    boolean callableStatement_getBoolean(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    byte callableStatement_getByte(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    short callableStatement_getShort(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    int callableStatement_getInt(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    long callableStatement_getLong(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    float callableStatement_getFloat(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    double callableStatement_getDouble(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    byte[] callableStatement_getBytes(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Date callableStatement_getDate(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Time callableStatement_getTime(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Object callableStatement_getObject(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Object callableStatement_getObject(final CallableStatementProxy p0, final String p1, final Map<String, Class<?>> p2) throws SQLException;
    
    Ref callableStatement_getRef(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Blob callableStatement_getBlob(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Clob callableStatement_getClob(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Array callableStatement_getArray(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Date callableStatement_getDate(final CallableStatementProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    Time callableStatement_getTime(final CallableStatementProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final CallableStatementProxy p0, final String p1, final Calendar p2) throws SQLException;
    
    URL callableStatement_getURL(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    RowId callableStatement_getRowId(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    RowId callableStatement_getRowId(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    void callableStatement_setRowId(final CallableStatementProxy p0, final String p1, final RowId p2) throws SQLException;
    
    void callableStatement_setNString(final CallableStatementProxy p0, final String p1, final String p2) throws SQLException;
    
    void callableStatement_setNCharacterStream(final CallableStatementProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void callableStatement_setNClob(final CallableStatementProxy p0, final String p1, final NClob p2) throws SQLException;
    
    void callableStatement_setClob(final CallableStatementProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void callableStatement_setBlob(final CallableStatementProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void callableStatement_setNClob(final CallableStatementProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    NClob callableStatement_getNClob(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    NClob callableStatement_getNClob(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    void callableStatement_setSQLXML(final CallableStatementProxy p0, final String p1, final SQLXML p2) throws SQLException;
    
    SQLXML callableStatement_getSQLXML(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    SQLXML callableStatement_getSQLXML(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    String callableStatement_getNString(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    String callableStatement_getNString(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Reader callableStatement_getNCharacterStream(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Reader callableStatement_getNCharacterStream(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    Reader callableStatement_getCharacterStream(final CallableStatementProxy p0, final int p1) throws SQLException;
    
    Reader callableStatement_getCharacterStream(final CallableStatementProxy p0, final String p1) throws SQLException;
    
    void callableStatement_setBlob(final CallableStatementProxy p0, final String p1, final Blob p2) throws SQLException;
    
    void callableStatement_setClob(final CallableStatementProxy p0, final String p1, final Clob p2) throws SQLException;
    
    void callableStatement_setAsciiStream(final CallableStatementProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void callableStatement_setBinaryStream(final CallableStatementProxy p0, final String p1, final InputStream p2, final long p3) throws SQLException;
    
    void callableStatement_setCharacterStream(final CallableStatementProxy p0, final String p1, final Reader p2, final long p3) throws SQLException;
    
    void callableStatement_setAsciiStream(final CallableStatementProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void callableStatement_setBinaryStream(final CallableStatementProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void callableStatement_setCharacterStream(final CallableStatementProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void callableStatement_setNCharacterStream(final CallableStatementProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void callableStatement_setClob(final CallableStatementProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void callableStatement_setBlob(final CallableStatementProxy p0, final String p1, final InputStream p2) throws SQLException;
    
    void callableStatement_setNClob(final CallableStatementProxy p0, final String p1, final Reader p2) throws SQLException;
    
    void clob_free(final ClobProxy p0) throws SQLException;
    
    InputStream clob_getAsciiStream(final ClobProxy p0) throws SQLException;
    
    Reader clob_getCharacterStream(final ClobProxy p0) throws SQLException;
    
    Reader clob_getCharacterStream(final ClobProxy p0, final long p1, final long p2) throws SQLException;
    
    String clob_getSubString(final ClobProxy p0, final long p1, final int p2) throws SQLException;
    
    long clob_length(final ClobProxy p0) throws SQLException;
    
    long clob_position(final ClobProxy p0, final String p1, final long p2) throws SQLException;
    
    long clob_position(final ClobProxy p0, final Clob p1, final long p2) throws SQLException;
    
    OutputStream clob_setAsciiStream(final ClobProxy p0, final long p1) throws SQLException;
    
    Writer clob_setCharacterStream(final ClobProxy p0, final long p1) throws SQLException;
    
    int clob_setString(final ClobProxy p0, final long p1, final String p2) throws SQLException;
    
    int clob_setString(final ClobProxy p0, final long p1, final String p2, final int p3, final int p4) throws SQLException;
    
    void clob_truncate(final ClobProxy p0, final long p1) throws SQLException;
    
    void dataSource_recycle(final DruidPooledConnection p0) throws SQLException;
    
    DruidPooledConnection dataSource_connect(final DruidDataSource p0, final long p1) throws SQLException;
    
    int resultSetMetaData_getColumnCount(final ResultSetMetaDataProxy p0) throws SQLException;
    
    boolean resultSetMetaData_isAutoIncrement(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isCaseSensitive(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isSearchable(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isCurrency(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    int resultSetMetaData_isNullable(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isSigned(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    int resultSetMetaData_getColumnDisplaySize(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getColumnLabel(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getColumnName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getSchemaName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    int resultSetMetaData_getPrecision(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    int resultSetMetaData_getScale(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getTableName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getCatalogName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    int resultSetMetaData_getColumnType(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getColumnTypeName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isReadOnly(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isWritable(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    boolean resultSetMetaData_isDefinitelyWritable(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
    
    String resultSetMetaData_getColumnClassName(final ResultSetMetaDataProxy p0, final int p1) throws SQLException;
}
