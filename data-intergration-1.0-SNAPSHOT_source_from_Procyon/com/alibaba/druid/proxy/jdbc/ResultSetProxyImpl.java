// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import com.alibaba.druid.filter.FilterChain;
import java.sql.SQLWarning;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Statement;
import java.sql.SQLXML;
import java.sql.RowId;
import java.sql.Ref;
import java.sql.NClob;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Clob;
import java.io.Reader;
import java.sql.Blob;
import java.math.BigDecimal;
import java.io.InputStream;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Wrapper;
import com.alibaba.druid.filter.FilterChainImpl;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.stat.JdbcSqlStat;
import java.sql.ResultSet;

public class ResultSetProxyImpl extends WrapperProxyImpl implements ResultSetProxy
{
    private final ResultSet resultSet;
    private final StatementProxy statement;
    private final String sql;
    protected int cursorIndex;
    protected int fetchRowCount;
    protected long constructNano;
    protected final JdbcSqlStat sqlStat;
    private int closeCount;
    private long readStringLength;
    private long readBytesLength;
    private int openInputStreamCount;
    private int openReaderCount;
    private Map<Integer, Integer> logicColumnMap;
    private Map<Integer, Integer> physicalColumnMap;
    private List<Integer> hiddenColumns;
    private FilterChainImpl filterChain;
    
    public ResultSetProxyImpl(final StatementProxy statement, final ResultSet resultSet, final long id, final String sql) {
        super(resultSet, id);
        this.cursorIndex = 0;
        this.fetchRowCount = 0;
        this.closeCount = 0;
        this.readStringLength = 0L;
        this.readBytesLength = 0L;
        this.openInputStreamCount = 0;
        this.openReaderCount = 0;
        this.logicColumnMap = null;
        this.physicalColumnMap = null;
        this.hiddenColumns = null;
        this.filterChain = null;
        this.statement = statement;
        this.resultSet = resultSet;
        this.sql = sql;
        this.sqlStat = this.statement.getSqlStat();
    }
    
    @Override
    public long getConstructNano() {
        return this.constructNano;
    }
    
    @Override
    public void setConstructNano(final long constructNano) {
        this.constructNano = constructNano;
    }
    
    @Override
    public void setConstructNano() {
        if (this.constructNano <= 0L) {
            this.constructNano = System.nanoTime();
        }
    }
    
    @Override
    public int getCursorIndex() {
        return this.cursorIndex;
    }
    
    @Override
    public int getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    @Override
    public String getSql() {
        return this.sql;
    }
    
    @Override
    public JdbcSqlStat getSqlStat() {
        return this.sqlStat;
    }
    
    @Override
    public ResultSet getResultSetRaw() {
        return this.resultSet;
    }
    
    @Override
    public StatementProxy getStatementProxy() {
        return this.statement;
    }
    
    @Override
    public FilterChainImpl createChain() {
        FilterChainImpl chain = this.filterChain;
        if (chain == null) {
            chain = new FilterChainImpl(this.statement.getConnectionProxy().getDirectDataSource());
        }
        else {
            this.filterChain = null;
        }
        return chain;
    }
    
    public void recycleFilterChain(final FilterChainImpl chain) {
        chain.reset();
        this.filterChain = chain;
    }
    
    @Override
    public boolean absolute(final int row) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_absolute(this, row);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public void afterLast() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_afterLast(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void beforeFirst() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_beforeFirst(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void cancelRowUpdates() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_cancelRowUpdates(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_clearWarnings(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void close() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_close(this);
        ++this.closeCount;
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void deleteRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_deleteRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_findColumn(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean first() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_first(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Array value = chain.resultSet_getArray(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Array value = chain.resultSet_getArray(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getAsciiStream(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getAsciiStream(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final BigDecimal value = chain.resultSet_getBigDecimal(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final BigDecimal value = chain.resultSet_getBigDecimal(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final BigDecimal value = chain.resultSet_getBigDecimal(this, columnIndex, scale);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final BigDecimal value = chain.resultSet_getBigDecimal(this, columnLabel, scale);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getBinaryStream(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getBinaryStream(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Blob value = chain.resultSet_getBlob(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Blob value = chain.resultSet_getBlob(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_getBoolean(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_getBoolean(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final byte value = chain.resultSet_getByte(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final byte value = chain.resultSet_getByte(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final byte[] value = chain.resultSet_getBytes(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final byte[] value = chain.resultSet_getBytes(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Reader value = chain.resultSet_getCharacterStream(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Reader value = chain.resultSet_getCharacterStream(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Clob value = chain.resultSet_getClob(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Clob value = chain.resultSet_getClob(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getConcurrency() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getConcurrency(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getCursorName() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSet_getCursorName(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Date value = chain.resultSet_getDate(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Date value = chain.resultSet_getDate(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Date value = chain.resultSet_getDate(this, columnIndex, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Date value = chain.resultSet_getDate(this, columnLabel, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final double value = chain.resultSet_getDouble(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final double value = chain.resultSet_getDouble(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getFetchDirection(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getFetchSize(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final float value = chain.resultSet_getFloat(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final float value = chain.resultSet_getFloat(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getHoldability() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getHoldability(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getInt(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getInt(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getInt(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getInt(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public long getLong(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final long value = chain.resultSet_getLong(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public long getLong(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final long value = chain.resultSet_getLong(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final ResultSetMetaData value = chain.resultSet_getMetaData(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Reader value = chain.resultSet_getNCharacterStream(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Reader value = chain.resultSet_getNCharacterStream(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final NClob value = chain.resultSet_getNClob(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final NClob value = chain.resultSet_getNClob(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getNString(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSet_getNString(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getNString(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSet_getNString(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Object value = chain.resultSet_getObject(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Object value = chain.resultSet_getObject(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Object value = chain.resultSet_getObject(this, columnIndex, map);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Object value = chain.resultSet_getObject(this, columnLabel, map);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Ref value = chain.resultSet_getRef(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Ref value = chain.resultSet_getRef(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getRow(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final RowId value = chain.resultSet_getRowId(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final RowId value = chain.resultSet_getRowId(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final SQLXML value = chain.resultSet_getSQLXML(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final SQLXML value = chain.resultSet_getSQLXML(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public short getShort(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final short value = chain.resultSet_getShort(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public short getShort(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final short value = chain.resultSet_getShort(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Statement getStatement() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Statement stmt = chain.resultSet_getStatement(this);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public String getString(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSet_getString(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getString(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSet_getString(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Time value = chain.resultSet_getTime(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Time value = chain.resultSet_getTime(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Time value = chain.resultSet_getTime(this, columnIndex, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Time value = chain.resultSet_getTime(this, columnLabel, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Timestamp value = chain.resultSet_getTimestamp(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Timestamp value = chain.resultSet_getTimestamp(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Timestamp value = chain.resultSet_getTimestamp(this, columnIndex, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Timestamp value = chain.resultSet_getTimestamp(this, columnLabel, cal);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getType() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSet_getType(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final URL value = chain.resultSet_getURL(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final URL value = chain.resultSet_getURL(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getUnicodeStream(this, columnIndex);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final InputStream value = chain.resultSet_getUnicodeStream(this, columnLabel);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final SQLWarning value = chain.resultSet_getWarnings(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public void insertRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_insertRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public boolean isAfterLast() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_isAfterLast(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isBeforeFirst() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_isBeforeFirst(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_isClosed(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isFirst() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_isFirst(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isLast() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_isLast(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean last() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_last(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public void moveToCurrentRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_moveToCurrentRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void moveToInsertRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_moveToInsertRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public boolean next() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean moreRows = chain.resultSet_next(this);
        if (moreRows) {
            ++this.cursorIndex;
            if (this.cursorIndex > this.fetchRowCount) {
                this.fetchRowCount = this.cursorIndex;
            }
        }
        this.recycleFilterChain(chain);
        return moreRows;
    }
    
    @Override
    public boolean previous() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean moreRows = chain.resultSet_previous(this);
        if (moreRows) {
            --this.cursorIndex;
        }
        this.recycleFilterChain(chain);
        return moreRows;
    }
    
    @Override
    public void refreshRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_refreshRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public boolean relative(final int rows) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_relative(this, rows);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean rowDeleted() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_rowDeleted(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean rowInserted() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_rowInserted(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean rowUpdated() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSet_rowUpdated(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_setFetchDirection(this, direction);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_setFetchSize(this, rows);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateArray(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateArray(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateAsciiStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBigDecimal(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBigDecimal(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBinaryStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final int columnIndex, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBlob(final String columnLabel, final InputStream x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBlob(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBoolean(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBoolean(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateByte(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateByte(final String columnLabel, final byte x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateByte(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBytes(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateBytes(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader x, final int length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateCharacterStream(final String columnLabel, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateCharacterStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateClob(final String columnLabel, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateClob(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateDate(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateDate(final String columnLabel, final Date x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateDate(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateDouble(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateDouble(final String columnLabel, final double x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateDouble(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateFloat(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateFloat(final String columnLabel, final float x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateFloat(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateInt(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateInt(final String columnLabel, final int x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateInt(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateLong(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateLong(final String columnLabel, final long x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateLong(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNCharacterStream(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNCharacterStream(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNCharacterStream(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNCharacterStream(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final NClob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final NClob x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnIndex, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNClob(final String columnLabel, final Reader x, final long length) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNClob(this, columnLabel, x, length);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNString(final int columnIndex, final String x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNString(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNString(final String columnLabel, final String x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNString(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNull(this, columnIndex);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateNull(this, columnLabel);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateObject(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateObject(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateObject(this, columnIndex, x, scaleOrLength);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateObject(this, columnLabel, x, scaleOrLength);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateRef(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateRef(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateRow() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateRow(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateRowId(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateRowId(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateSQLXML(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateSQLXML(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateShort(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateShort(final String columnLabel, final short x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateShort(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateString(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateString(final String columnLabel, final String x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateString(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateTime(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateTime(final String columnLabel, final Time x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateTime(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateTimestamp(this, columnIndex, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.resultSet_updateTimestamp(this, columnLabel, x);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public boolean wasNull() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean result = chain.resultSet_wasNull(this);
        this.recycleFilterChain(chain);
        return result;
    }
    
    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final T value = chain.resultSet_getObject(this, columnIndex, type);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final T value = chain.resultSet_getObject(this, columnLabel, type);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getCloseCount() {
        return this.closeCount;
    }
    
    @Override
    public void addReadStringLength(final int length) {
        this.readStringLength += length;
    }
    
    @Override
    public long getReadStringLength() {
        return this.readStringLength;
    }
    
    @Override
    public void addReadBytesLength(final int length) {
        this.readBytesLength += length;
    }
    
    @Override
    public long getReadBytesLength() {
        return this.readBytesLength;
    }
    
    @Override
    public void incrementOpenInputStreamCount() {
        ++this.openInputStreamCount;
    }
    
    @Override
    public int getOpenInputStreamCount() {
        return this.openInputStreamCount;
    }
    
    @Override
    public void incrementOpenReaderCount() {
        ++this.openReaderCount;
    }
    
    @Override
    public int getOpenReaderCount() {
        return this.openReaderCount;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == ResultSetProxy.class || iface == ResultSetProxyImpl.class) {
            return (T)this;
        }
        return super.unwrap(iface);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface == ResultSetProxy.class || iface == ResultSetProxyImpl.class || super.isWrapperFor(iface);
    }
    
    @Override
    public int getPhysicalColumn(final int logicColumn) {
        if (this.logicColumnMap == null) {
            return logicColumn;
        }
        return this.logicColumnMap.get(logicColumn);
    }
    
    @Override
    public int getLogicColumn(final int physicalColumn) {
        if (this.physicalColumnMap == null) {
            return physicalColumn;
        }
        return this.physicalColumnMap.get(physicalColumn);
    }
    
    @Override
    public int getHiddenColumnCount() {
        if (this.hiddenColumns == null) {
            return 0;
        }
        return this.hiddenColumns.size();
    }
    
    @Override
    public List<Integer> getHiddenColumns() {
        return this.hiddenColumns;
    }
    
    @Override
    public void setLogicColumnMap(final Map<Integer, Integer> logicColumnMap) {
        this.logicColumnMap = logicColumnMap;
    }
    
    @Override
    public void setPhysicalColumnMap(final Map<Integer, Integer> physicalColumnMap) {
        this.physicalColumnMap = physicalColumnMap;
    }
    
    @Override
    public void setHiddenColumns(final List<Integer> hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }
}
