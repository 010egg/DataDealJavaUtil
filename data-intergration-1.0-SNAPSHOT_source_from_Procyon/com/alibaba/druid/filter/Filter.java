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
import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.util.Properties;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import java.sql.Wrapper;

public interface Filter extends Wrapper
{
    void init(final DataSourceProxy p0);
    
    void destroy();
    
    void configFromProperties(final Properties p0);
    
    boolean isWrapperFor(final Class<?> p0);
    
     <T> T unwrap(final Class<T> p0);
    
    ConnectionProxy connection_connect(final FilterChain p0, final Properties p1) throws SQLException;
    
    StatementProxy connection_createStatement(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    String connection_nativeSQL(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    void connection_setAutoCommit(final FilterChain p0, final ConnectionProxy p1, final boolean p2) throws SQLException;
    
    boolean connection_getAutoCommit(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_commit(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_rollback(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_close(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    boolean connection_isClosed(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    DatabaseMetaData connection_getMetaData(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_setReadOnly(final FilterChain p0, final ConnectionProxy p1, final boolean p2) throws SQLException;
    
    boolean connection_isReadOnly(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_setCatalog(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    String connection_getCatalog(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_setTransactionIsolation(final FilterChain p0, final ConnectionProxy p1, final int p2) throws SQLException;
    
    int connection_getTransactionIsolation(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    SQLWarning connection_getWarnings(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_clearWarnings(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    StatementProxy connection_createStatement(final FilterChain p0, final ConnectionProxy p1, final int p2, final int p3) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2, final int p3, final int p4) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final FilterChain p0, final ConnectionProxy p1, final String p2, final int p3, final int p4) throws SQLException;
    
    Map<String, Class<?>> connection_getTypeMap(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_setTypeMap(final FilterChain p0, final ConnectionProxy p1, final Map<String, Class<?>> p2) throws SQLException;
    
    void connection_setHoldability(final FilterChain p0, final ConnectionProxy p1, final int p2) throws SQLException;
    
    int connection_getHoldability(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    Savepoint connection_setSavepoint(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    Savepoint connection_setSavepoint(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    void connection_rollback(final FilterChain p0, final ConnectionProxy p1, final Savepoint p2) throws SQLException;
    
    void connection_releaseSavepoint(final FilterChain p0, final ConnectionProxy p1, final Savepoint p2) throws SQLException;
    
    StatementProxy connection_createStatement(final FilterChain p0, final ConnectionProxy p1, final int p2, final int p3, final int p4) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2, final int p3, final int p4, final int p5) throws SQLException;
    
    CallableStatementProxy connection_prepareCall(final FilterChain p0, final ConnectionProxy p1, final String p2, final int p3, final int p4, final int p5) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2, final int p3) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2, final int[] p3) throws SQLException;
    
    PreparedStatementProxy connection_prepareStatement(final FilterChain p0, final ConnectionProxy p1, final String p2, final String[] p3) throws SQLException;
    
    Clob connection_createClob(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    Blob connection_createBlob(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    NClob connection_createNClob(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    SQLXML connection_createSQLXML(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    boolean connection_isValid(final FilterChain p0, final ConnectionProxy p1, final int p2) throws SQLException;
    
    void connection_setClientInfo(final FilterChain p0, final ConnectionProxy p1, final String p2, final String p3) throws SQLClientInfoException;
    
    void connection_setClientInfo(final FilterChain p0, final ConnectionProxy p1, final Properties p2) throws SQLClientInfoException;
    
    String connection_getClientInfo(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    Properties connection_getClientInfo(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    Array connection_createArrayOf(final FilterChain p0, final ConnectionProxy p1, final String p2, final Object[] p3) throws SQLException;
    
    Struct connection_createStruct(final FilterChain p0, final ConnectionProxy p1, final String p2, final Object[] p3) throws SQLException;
    
    String connection_getSchema(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    void connection_setSchema(final FilterChain p0, final ConnectionProxy p1, final String p2) throws SQLException;
    
    void connection_abort(final FilterChain p0, final ConnectionProxy p1, final Executor p2) throws SQLException;
    
    void connection_setNetworkTimeout(final FilterChain p0, final ConnectionProxy p1, final Executor p2, final int p3) throws SQLException;
    
    int connection_getNetworkTimeout(final FilterChain p0, final ConnectionProxy p1) throws SQLException;
    
    boolean resultSet_next(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_close(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_wasNull(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    String resultSet_getString(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    boolean resultSet_getBoolean(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    byte resultSet_getByte(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    short resultSet_getShort(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    int resultSet_getInt(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    long resultSet_getLong(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    float resultSet_getFloat(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    double resultSet_getDouble(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final FilterChain p0, final ResultSetProxy p1, final int p2, final int p3) throws SQLException;
    
    byte[] resultSet_getBytes(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Date resultSet_getDate(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Time resultSet_getTime(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    InputStream resultSet_getAsciiStream(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    InputStream resultSet_getUnicodeStream(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    InputStream resultSet_getBinaryStream(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    String resultSet_getString(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    boolean resultSet_getBoolean(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    byte resultSet_getByte(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    short resultSet_getShort(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    int resultSet_getInt(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    long resultSet_getLong(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    float resultSet_getFloat(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    double resultSet_getDouble(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final FilterChain p0, final ResultSetProxy p1, final String p2, final int p3) throws SQLException;
    
    byte[] resultSet_getBytes(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Date resultSet_getDate(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Time resultSet_getTime(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    InputStream resultSet_getAsciiStream(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    InputStream resultSet_getUnicodeStream(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    InputStream resultSet_getBinaryStream(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    SQLWarning resultSet_getWarnings(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_clearWarnings(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    String resultSet_getCursorName(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    ResultSetMetaData resultSet_getMetaData(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    Object resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
     <T> T resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final int p2, final Class<T> p3) throws SQLException;
    
    Object resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
     <T> T resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final String p2, final Class<T> p3) throws SQLException;
    
    int resultSet_findColumn(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Reader resultSet_getCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Reader resultSet_getCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    BigDecimal resultSet_getBigDecimal(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    boolean resultSet_isBeforeFirst(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_isAfterLast(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_isFirst(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_isLast(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_beforeFirst(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_afterLast(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_first(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_last(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    int resultSet_getRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_absolute(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    boolean resultSet_relative(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    boolean resultSet_previous(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_setFetchDirection(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    int resultSet_getFetchDirection(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_setFetchSize(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    int resultSet_getFetchSize(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    int resultSet_getType(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    int resultSet_getConcurrency(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_rowUpdated(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_rowInserted(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_rowDeleted(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_updateNull(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    void resultSet_updateBoolean(final FilterChain p0, final ResultSetProxy p1, final int p2, final boolean p3) throws SQLException;
    
    void resultSet_updateByte(final FilterChain p0, final ResultSetProxy p1, final int p2, final byte p3) throws SQLException;
    
    void resultSet_updateShort(final FilterChain p0, final ResultSetProxy p1, final int p2, final short p3) throws SQLException;
    
    void resultSet_updateInt(final FilterChain p0, final ResultSetProxy p1, final int p2, final int p3) throws SQLException;
    
    void resultSet_updateLong(final FilterChain p0, final ResultSetProxy p1, final int p2, final long p3) throws SQLException;
    
    void resultSet_updateFloat(final FilterChain p0, final ResultSetProxy p1, final int p2, final float p3) throws SQLException;
    
    void resultSet_updateDouble(final FilterChain p0, final ResultSetProxy p1, final int p2, final double p3) throws SQLException;
    
    void resultSet_updateBigDecimal(final FilterChain p0, final ResultSetProxy p1, final int p2, final BigDecimal p3) throws SQLException;
    
    void resultSet_updateString(final FilterChain p0, final ResultSetProxy p1, final int p2, final String p3) throws SQLException;
    
    void resultSet_updateBytes(final FilterChain p0, final ResultSetProxy p1, final int p2, final byte[] p3) throws SQLException;
    
    void resultSet_updateDate(final FilterChain p0, final ResultSetProxy p1, final int p2, final Date p3) throws SQLException;
    
    void resultSet_updateTime(final FilterChain p0, final ResultSetProxy p1, final int p2, final Time p3) throws SQLException;
    
    void resultSet_updateTimestamp(final FilterChain p0, final ResultSetProxy p1, final int p2, final Timestamp p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3, final int p4) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3, final int p4) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3, final int p4) throws SQLException;
    
    void resultSet_updateObject(final FilterChain p0, final ResultSetProxy p1, final int p2, final Object p3, final int p4) throws SQLException;
    
    void resultSet_updateObject(final FilterChain p0, final ResultSetProxy p1, final int p2, final Object p3) throws SQLException;
    
    void resultSet_updateNull(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    void resultSet_updateBoolean(final FilterChain p0, final ResultSetProxy p1, final String p2, final boolean p3) throws SQLException;
    
    void resultSet_updateByte(final FilterChain p0, final ResultSetProxy p1, final String p2, final byte p3) throws SQLException;
    
    void resultSet_updateShort(final FilterChain p0, final ResultSetProxy p1, final String p2, final short p3) throws SQLException;
    
    void resultSet_updateInt(final FilterChain p0, final ResultSetProxy p1, final String p2, final int p3) throws SQLException;
    
    void resultSet_updateLong(final FilterChain p0, final ResultSetProxy p1, final String p2, final long p3) throws SQLException;
    
    void resultSet_updateFloat(final FilterChain p0, final ResultSetProxy p1, final String p2, final float p3) throws SQLException;
    
    void resultSet_updateDouble(final FilterChain p0, final ResultSetProxy p1, final String p2, final double p3) throws SQLException;
    
    void resultSet_updateBigDecimal(final FilterChain p0, final ResultSetProxy p1, final String p2, final BigDecimal p3) throws SQLException;
    
    void resultSet_updateString(final FilterChain p0, final ResultSetProxy p1, final String p2, final String p3) throws SQLException;
    
    void resultSet_updateBytes(final FilterChain p0, final ResultSetProxy p1, final String p2, final byte[] p3) throws SQLException;
    
    void resultSet_updateDate(final FilterChain p0, final ResultSetProxy p1, final String p2, final Date p3) throws SQLException;
    
    void resultSet_updateTime(final FilterChain p0, final ResultSetProxy p1, final String p2, final Time p3) throws SQLException;
    
    void resultSet_updateTimestamp(final FilterChain p0, final ResultSetProxy p1, final String p2, final Timestamp p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3, final int p4) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3, final int p4) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3, final int p4) throws SQLException;
    
    void resultSet_updateObject(final FilterChain p0, final ResultSetProxy p1, final String p2, final Object p3, final int p4) throws SQLException;
    
    void resultSet_updateObject(final FilterChain p0, final ResultSetProxy p1, final String p2, final Object p3) throws SQLException;
    
    void resultSet_insertRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_updateRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_deleteRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_refreshRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_cancelRowUpdates(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_moveToInsertRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_moveToCurrentRow(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    Statement resultSet_getStatement(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    Object resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final int p2, final Map<String, Class<?>> p3) throws SQLException;
    
    Ref resultSet_getRef(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Blob resultSet_getBlob(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Clob resultSet_getClob(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Array resultSet_getArray(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Object resultSet_getObject(final FilterChain p0, final ResultSetProxy p1, final String p2, final Map<String, Class<?>> p3) throws SQLException;
    
    Ref resultSet_getRef(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Blob resultSet_getBlob(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Clob resultSet_getClob(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Array resultSet_getArray(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Date resultSet_getDate(final FilterChain p0, final ResultSetProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    Date resultSet_getDate(final FilterChain p0, final ResultSetProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    Time resultSet_getTime(final FilterChain p0, final ResultSetProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    Time resultSet_getTime(final FilterChain p0, final ResultSetProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final FilterChain p0, final ResultSetProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    Timestamp resultSet_getTimestamp(final FilterChain p0, final ResultSetProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    URL resultSet_getURL(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    URL resultSet_getURL(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    void resultSet_updateRef(final FilterChain p0, final ResultSetProxy p1, final int p2, final Ref p3) throws SQLException;
    
    void resultSet_updateRef(final FilterChain p0, final ResultSetProxy p1, final String p2, final Ref p3) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Blob p3) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Blob p3) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Clob p3) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Clob p3) throws SQLException;
    
    void resultSet_updateArray(final FilterChain p0, final ResultSetProxy p1, final int p2, final Array p3) throws SQLException;
    
    void resultSet_updateArray(final FilterChain p0, final ResultSetProxy p1, final String p2, final Array p3) throws SQLException;
    
    RowId resultSet_getRowId(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    RowId resultSet_getRowId(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    void resultSet_updateRowId(final FilterChain p0, final ResultSetProxy p1, final int p2, final RowId p3) throws SQLException;
    
    void resultSet_updateRowId(final FilterChain p0, final ResultSetProxy p1, final String p2, final RowId p3) throws SQLException;
    
    int resultSet_getHoldability(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    boolean resultSet_isClosed(final FilterChain p0, final ResultSetProxy p1) throws SQLException;
    
    void resultSet_updateNString(final FilterChain p0, final ResultSetProxy p1, final int p2, final String p3) throws SQLException;
    
    void resultSet_updateNString(final FilterChain p0, final ResultSetProxy p1, final String p2, final String p3) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final NClob p3) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final NClob p3) throws SQLException;
    
    NClob resultSet_getNClob(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    NClob resultSet_getNClob(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    SQLXML resultSet_getSQLXML(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    SQLXML resultSet_getSQLXML(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    void resultSet_updateSQLXML(final FilterChain p0, final ResultSetProxy p1, final int p2, final SQLXML p3) throws SQLException;
    
    void resultSet_updateSQLXML(final FilterChain p0, final ResultSetProxy p1, final String p2, final SQLXML p3) throws SQLException;
    
    String resultSet_getNString(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    String resultSet_getNString(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    Reader resultSet_getNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2) throws SQLException;
    
    Reader resultSet_getNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2) throws SQLException;
    
    void resultSet_updateNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void resultSet_updateNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void resultSet_updateNCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void resultSet_updateAsciiStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateBinaryStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateCharacterStream(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateBlob(final FilterChain p0, final ResultSetProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void resultSet_updateClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void resultSet_updateNClob(final FilterChain p0, final ResultSetProxy p1, final String p2, final Reader p3) throws SQLException;
    
    ResultSetProxy statement_executeQuery(final FilterChain p0, final StatementProxy p1, final String p2) throws SQLException;
    
    int statement_executeUpdate(final FilterChain p0, final StatementProxy p1, final String p2) throws SQLException;
    
    void statement_close(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int statement_getMaxFieldSize(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setMaxFieldSize(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    int statement_getMaxRows(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setMaxRows(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    void statement_setEscapeProcessing(final FilterChain p0, final StatementProxy p1, final boolean p2) throws SQLException;
    
    int statement_getQueryTimeout(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setQueryTimeout(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    void statement_cancel(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    SQLWarning statement_getWarnings(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_clearWarnings(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setCursorName(final FilterChain p0, final StatementProxy p1, final String p2) throws SQLException;
    
    boolean statement_execute(final FilterChain p0, final StatementProxy p1, final String p2) throws SQLException;
    
    ResultSetProxy statement_getResultSet(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int statement_getUpdateCount(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    boolean statement_getMoreResults(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setFetchDirection(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    int statement_getFetchDirection(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setFetchSize(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    int statement_getFetchSize(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int statement_getResultSetConcurrency(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int statement_getResultSetType(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_addBatch(final FilterChain p0, final StatementProxy p1, final String p2) throws SQLException;
    
    void statement_clearBatch(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int[] statement_executeBatch(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    Connection statement_getConnection(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    boolean statement_getMoreResults(final FilterChain p0, final StatementProxy p1, final int p2) throws SQLException;
    
    ResultSetProxy statement_getGeneratedKeys(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    int statement_executeUpdate(final FilterChain p0, final StatementProxy p1, final String p2, final int p3) throws SQLException;
    
    int statement_executeUpdate(final FilterChain p0, final StatementProxy p1, final String p2, final int[] p3) throws SQLException;
    
    int statement_executeUpdate(final FilterChain p0, final StatementProxy p1, final String p2, final String[] p3) throws SQLException;
    
    boolean statement_execute(final FilterChain p0, final StatementProxy p1, final String p2, final int p3) throws SQLException;
    
    boolean statement_execute(final FilterChain p0, final StatementProxy p1, final String p2, final int[] p3) throws SQLException;
    
    boolean statement_execute(final FilterChain p0, final StatementProxy p1, final String p2, final String[] p3) throws SQLException;
    
    int statement_getResultSetHoldability(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    boolean statement_isClosed(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    void statement_setPoolable(final FilterChain p0, final StatementProxy p1, final boolean p2) throws SQLException;
    
    boolean statement_isPoolable(final FilterChain p0, final StatementProxy p1) throws SQLException;
    
    ResultSetProxy preparedStatement_executeQuery(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    int preparedStatement_executeUpdate(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_setNull(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final int p3) throws SQLException;
    
    void preparedStatement_setBoolean(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final boolean p3) throws SQLException;
    
    void preparedStatement_setByte(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final byte p3) throws SQLException;
    
    void preparedStatement_setShort(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final short p3) throws SQLException;
    
    void preparedStatement_setInt(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final int p3) throws SQLException;
    
    void preparedStatement_setLong(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final long p3) throws SQLException;
    
    void preparedStatement_setFloat(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final float p3) throws SQLException;
    
    void preparedStatement_setDouble(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final double p3) throws SQLException;
    
    void preparedStatement_setBigDecimal(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final BigDecimal p3) throws SQLException;
    
    void preparedStatement_setString(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final String p3) throws SQLException;
    
    void preparedStatement_setBytes(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final byte[] p3) throws SQLException;
    
    void preparedStatement_setDate(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Date p3) throws SQLException;
    
    void preparedStatement_setTime(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Time p3) throws SQLException;
    
    void preparedStatement_setTimestamp(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Timestamp p3) throws SQLException;
    
    void preparedStatement_setAsciiStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final int p4) throws SQLException;
    
    void preparedStatement_setUnicodeStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final int p4) throws SQLException;
    
    void preparedStatement_setBinaryStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final int p4) throws SQLException;
    
    void preparedStatement_clearParameters(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_setObject(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Object p3, final int p4) throws SQLException;
    
    void preparedStatement_setObject(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Object p3) throws SQLException;
    
    boolean preparedStatement_execute(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_addBatch(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_setCharacterStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3, final int p4) throws SQLException;
    
    void preparedStatement_setRef(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Ref p3) throws SQLException;
    
    void preparedStatement_setBlob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Blob p3) throws SQLException;
    
    void preparedStatement_setClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Clob p3) throws SQLException;
    
    void preparedStatement_setArray(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Array p3) throws SQLException;
    
    ResultSetMetaData preparedStatement_getMetaData(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_setDate(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Date p3, final Calendar p4) throws SQLException;
    
    void preparedStatement_setTime(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Time p3, final Calendar p4) throws SQLException;
    
    void preparedStatement_setTimestamp(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Timestamp p3, final Calendar p4) throws SQLException;
    
    void preparedStatement_setNull(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final int p3, final String p4) throws SQLException;
    
    void preparedStatement_setURL(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final URL p3) throws SQLException;
    
    ParameterMetaData preparedStatement_getParameterMetaData(final FilterChain p0, final PreparedStatementProxy p1) throws SQLException;
    
    void preparedStatement_setRowId(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final RowId p3) throws SQLException;
    
    void preparedStatement_setNString(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final String p3) throws SQLException;
    
    void preparedStatement_setNCharacterStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void preparedStatement_setNClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final NClob p3) throws SQLException;
    
    void preparedStatement_setClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void preparedStatement_setBlob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void preparedStatement_setNClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void preparedStatement_setSQLXML(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final SQLXML p3) throws SQLException;
    
    void preparedStatement_setObject(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Object p3, final int p4, final int p5) throws SQLException;
    
    void preparedStatement_setAsciiStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void preparedStatement_setBinaryStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3, final long p4) throws SQLException;
    
    void preparedStatement_setCharacterStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3, final long p4) throws SQLException;
    
    void preparedStatement_setAsciiStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void preparedStatement_setBinaryStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void preparedStatement_setCharacterStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void preparedStatement_setNCharacterStream(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void preparedStatement_setClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void preparedStatement_setBlob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final InputStream p3) throws SQLException;
    
    void preparedStatement_setNClob(final FilterChain p0, final PreparedStatementProxy p1, final int p2, final Reader p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final int p2, final int p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final int p2, final int p3, final int p4) throws SQLException;
    
    boolean callableStatement_wasNull(final FilterChain p0, final CallableStatementProxy p1) throws SQLException;
    
    String callableStatement_getString(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    boolean callableStatement_getBoolean(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    byte callableStatement_getByte(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    short callableStatement_getShort(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    int callableStatement_getInt(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    long callableStatement_getLong(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    float callableStatement_getFloat(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    double callableStatement_getDouble(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final FilterChain p0, final CallableStatementProxy p1, final int p2, final int p3) throws SQLException;
    
    byte[] callableStatement_getBytes(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Date callableStatement_getDate(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Time callableStatement_getTime(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Object callableStatement_getObject(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Object callableStatement_getObject(final FilterChain p0, final CallableStatementProxy p1, final int p2, final Map<String, Class<?>> p3) throws SQLException;
    
    Ref callableStatement_getRef(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Blob callableStatement_getBlob(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Clob callableStatement_getClob(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Array callableStatement_getArray(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Date callableStatement_getDate(final FilterChain p0, final CallableStatementProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    Time callableStatement_getTime(final FilterChain p0, final CallableStatementProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final FilterChain p0, final CallableStatementProxy p1, final int p2, final Calendar p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final int p2, final int p3, final String p4) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3, final int p4) throws SQLException;
    
    void callableStatement_registerOutParameter(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3, final String p4) throws SQLException;
    
    URL callableStatement_getURL(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    void callableStatement_setURL(final FilterChain p0, final CallableStatementProxy p1, final String p2, final URL p3) throws SQLException;
    
    void callableStatement_setNull(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3) throws SQLException;
    
    void callableStatement_setBoolean(final FilterChain p0, final CallableStatementProxy p1, final String p2, final boolean p3) throws SQLException;
    
    void callableStatement_setByte(final FilterChain p0, final CallableStatementProxy p1, final String p2, final byte p3) throws SQLException;
    
    void callableStatement_setShort(final FilterChain p0, final CallableStatementProxy p1, final String p2, final short p3) throws SQLException;
    
    void callableStatement_setInt(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3) throws SQLException;
    
    void callableStatement_setLong(final FilterChain p0, final CallableStatementProxy p1, final String p2, final long p3) throws SQLException;
    
    void callableStatement_setFloat(final FilterChain p0, final CallableStatementProxy p1, final String p2, final float p3) throws SQLException;
    
    void callableStatement_setDouble(final FilterChain p0, final CallableStatementProxy p1, final String p2, final double p3) throws SQLException;
    
    void callableStatement_setBigDecimal(final FilterChain p0, final CallableStatementProxy p1, final String p2, final BigDecimal p3) throws SQLException;
    
    void callableStatement_setString(final FilterChain p0, final CallableStatementProxy p1, final String p2, final String p3) throws SQLException;
    
    void callableStatement_setBytes(final FilterChain p0, final CallableStatementProxy p1, final String p2, final byte[] p3) throws SQLException;
    
    void callableStatement_setDate(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Date p3) throws SQLException;
    
    void callableStatement_setTime(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Time p3) throws SQLException;
    
    void callableStatement_setTimestamp(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Timestamp p3) throws SQLException;
    
    void callableStatement_setAsciiStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3, final int p4) throws SQLException;
    
    void callableStatement_setBinaryStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3, final int p4) throws SQLException;
    
    void callableStatement_setObject(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Object p3, final int p4, final int p5) throws SQLException;
    
    void callableStatement_setObject(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Object p3, final int p4) throws SQLException;
    
    void callableStatement_setObject(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Object p3) throws SQLException;
    
    void callableStatement_setCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3, final int p4) throws SQLException;
    
    void callableStatement_setDate(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Date p3, final Calendar p4) throws SQLException;
    
    void callableStatement_setTime(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Time p3, final Calendar p4) throws SQLException;
    
    void callableStatement_setTimestamp(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Timestamp p3, final Calendar p4) throws SQLException;
    
    void callableStatement_setNull(final FilterChain p0, final CallableStatementProxy p1, final String p2, final int p3, final String p4) throws SQLException;
    
    String callableStatement_getString(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    boolean callableStatement_getBoolean(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    byte callableStatement_getByte(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    short callableStatement_getShort(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    int callableStatement_getInt(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    long callableStatement_getLong(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    float callableStatement_getFloat(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    double callableStatement_getDouble(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    byte[] callableStatement_getBytes(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Date callableStatement_getDate(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Time callableStatement_getTime(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Object callableStatement_getObject(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    BigDecimal callableStatement_getBigDecimal(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Object callableStatement_getObject(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Map<String, Class<?>> p3) throws SQLException;
    
    Ref callableStatement_getRef(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Blob callableStatement_getBlob(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Clob callableStatement_getClob(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Array callableStatement_getArray(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Date callableStatement_getDate(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    Time callableStatement_getTime(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    Timestamp callableStatement_getTimestamp(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Calendar p3) throws SQLException;
    
    URL callableStatement_getURL(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    RowId callableStatement_getRowId(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    RowId callableStatement_getRowId(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    void callableStatement_setRowId(final FilterChain p0, final CallableStatementProxy p1, final String p2, final RowId p3) throws SQLException;
    
    void callableStatement_setNString(final FilterChain p0, final CallableStatementProxy p1, final String p2, final String p3) throws SQLException;
    
    void callableStatement_setNCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void callableStatement_setNClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final NClob p3) throws SQLException;
    
    void callableStatement_setClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void callableStatement_setBlob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void callableStatement_setNClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    NClob callableStatement_getNClob(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    NClob callableStatement_getNClob(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    void callableStatement_setSQLXML(final FilterChain p0, final CallableStatementProxy p1, final String p2, final SQLXML p3) throws SQLException;
    
    SQLXML callableStatement_getSQLXML(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    SQLXML callableStatement_getSQLXML(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    String callableStatement_getNString(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    String callableStatement_getNString(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Reader callableStatement_getNCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Reader callableStatement_getNCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    Reader callableStatement_getCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final int p2) throws SQLException;
    
    Reader callableStatement_getCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2) throws SQLException;
    
    void callableStatement_setBlob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Blob p3) throws SQLException;
    
    void callableStatement_setClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Clob p3) throws SQLException;
    
    void callableStatement_setAsciiStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void callableStatement_setBinaryStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3, final long p4) throws SQLException;
    
    void callableStatement_setCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3, final long p4) throws SQLException;
    
    void callableStatement_setAsciiStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void callableStatement_setBinaryStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void callableStatement_setCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void callableStatement_setNCharacterStream(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void callableStatement_setClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3) throws SQLException;
    
    void callableStatement_setBlob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final InputStream p3) throws SQLException;
    
    void callableStatement_setNClob(final FilterChain p0, final CallableStatementProxy p1, final String p2, final Reader p3) throws SQLException;
    
     <T> T unwrap(final FilterChain p0, final Wrapper p1, final Class<T> p2) throws SQLException;
    
    boolean isWrapperFor(final FilterChain p0, final Wrapper p1, final Class<?> p2) throws SQLException;
    
    void clob_free(final FilterChain p0, final ClobProxy p1) throws SQLException;
    
    InputStream clob_getAsciiStream(final FilterChain p0, final ClobProxy p1) throws SQLException;
    
    Reader clob_getCharacterStream(final FilterChain p0, final ClobProxy p1) throws SQLException;
    
    Reader clob_getCharacterStream(final FilterChain p0, final ClobProxy p1, final long p2, final long p3) throws SQLException;
    
    String clob_getSubString(final FilterChain p0, final ClobProxy p1, final long p2, final int p3) throws SQLException;
    
    long clob_length(final FilterChain p0, final ClobProxy p1) throws SQLException;
    
    long clob_position(final FilterChain p0, final ClobProxy p1, final String p2, final long p3) throws SQLException;
    
    long clob_position(final FilterChain p0, final ClobProxy p1, final Clob p2, final long p3) throws SQLException;
    
    OutputStream clob_setAsciiStream(final FilterChain p0, final ClobProxy p1, final long p2) throws SQLException;
    
    Writer clob_setCharacterStream(final FilterChain p0, final ClobProxy p1, final long p2) throws SQLException;
    
    int clob_setString(final FilterChain p0, final ClobProxy p1, final long p2, final String p3) throws SQLException;
    
    int clob_setString(final FilterChain p0, final ClobProxy p1, final long p2, final String p3, final int p4, final int p5) throws SQLException;
    
    void clob_truncate(final FilterChain p0, final ClobProxy p1, final long p2) throws SQLException;
    
    void dataSource_releaseConnection(final FilterChain p0, final DruidPooledConnection p1) throws SQLException;
    
    DruidPooledConnection dataSource_getConnection(final FilterChain p0, final DruidDataSource p1, final long p2) throws SQLException;
    
    int resultSetMetaData_getColumnCount(final FilterChain p0, final ResultSetMetaDataProxy p1) throws SQLException;
    
    boolean resultSetMetaData_isAutoIncrement(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isCaseSensitive(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isSearchable(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isCurrency(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    int resultSetMetaData_isNullable(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isSigned(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    int resultSetMetaData_getColumnDisplaySize(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getColumnLabel(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getColumnName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getSchemaName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    int resultSetMetaData_getPrecision(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    int resultSetMetaData_getScale(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getTableName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getCatalogName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    int resultSetMetaData_getColumnType(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getColumnTypeName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isReadOnly(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isWritable(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    boolean resultSetMetaData_isDefinitelyWritable(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
    
    String resultSetMetaData_getColumnClassName(final FilterChain p0, final ResultSetMetaDataProxy p1, final int p2) throws SQLException;
}
