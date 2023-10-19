// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.net.URL;
import java.sql.SQLXML;
import java.sql.RowId;
import java.sql.Ref;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.NClob;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Clob;
import java.io.Reader;
import java.sql.Blob;
import java.math.BigDecimal;
import java.io.InputStream;
import java.sql.Array;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.Statement;
import java.util.Map;
import java.sql.PreparedStatement;

public class PreparedStatementProxyImpl extends StatementProxyImpl implements PreparedStatementProxy
{
    private PreparedStatement statement;
    protected final String sql;
    private JdbcParameter[] parameters;
    private int parametersSize;
    private Map<Integer, JdbcParameter> paramMap;
    
    public PreparedStatementProxyImpl(final ConnectionProxy connection, final PreparedStatement statement, final String sql, final long id) {
        super(connection, statement, id);
        this.statement = statement;
        this.sql = sql;
        char quote = '\0';
        int paramCount = 0;
        for (int i = 0; i < sql.length(); ++i) {
            final char ch = sql.charAt(i);
            if (ch == '\'') {
                if (quote == '\0') {
                    quote = ch;
                }
                else if (quote == '\'') {
                    quote = '\0';
                }
            }
            else if (ch == '\"') {
                if (quote == '\0') {
                    quote = ch;
                }
                else if (quote == '\"') {
                    quote = '\0';
                }
            }
            if (quote == '\0' && ch == '?') {
                ++paramCount;
            }
        }
        this.parameters = new JdbcParameter[paramCount];
    }
    
    @Override
    public Map<Integer, JdbcParameter> getParameters() {
        if (this.paramMap == null) {
            this.paramMap = new HashMap<Integer, JdbcParameter>(this.parametersSize);
            for (int i = 0; i < this.parametersSize; ++i) {
                this.paramMap.put(i, this.parameters[i]);
            }
        }
        return this.paramMap;
    }
    
    protected void setStatement(final PreparedStatement statement) {
        super.statement = statement;
        this.statement = statement;
    }
    
    public void setParameter(final int jdbcIndex, final JdbcParameter parameter) {
        final int index = jdbcIndex - 1;
        if (jdbcIndex > this.parametersSize) {
            this.parametersSize = jdbcIndex;
        }
        if (this.parametersSize >= this.parameters.length) {
            final int oldCapacity = this.parameters.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity <= 4) {
                newCapacity = 4;
            }
            this.parameters = Arrays.copyOf(this.parameters, newCapacity);
        }
        this.parameters[index] = parameter;
        if (this.paramMap != null) {
            this.paramMap = null;
        }
    }
    
    @Override
    public int getParametersSize() {
        return this.parametersSize;
    }
    
    @Override
    public JdbcParameter getParameter(final int i) {
        if (i > this.parametersSize) {
            return null;
        }
        return this.parameters[i];
    }
    
    @Override
    public String getSql() {
        return this.sql;
    }
    
    @Override
    public PreparedStatement getRawObject() {
        return this.statement;
    }
    
    @Override
    public void addBatch() throws SQLException {
        this.createChain().preparedStatement_addBatch(this);
    }
    
    @Override
    public void clearParameters() throws SQLException {
        this.createChain().preparedStatement_clearParameters(this);
    }
    
    @Override
    public String getBatchSql() {
        return this.sql;
    }
    
    @Override
    public boolean execute() throws SQLException {
        this.updateCount = null;
        this.lastExecuteSql = this.sql;
        this.lastExecuteType = StatementExecuteType.Execute;
        this.lastExecuteStartNano = -1L;
        this.lastExecuteTimeNano = -1L;
        return this.firstResultSet = this.createChain().preparedStatement_execute(this);
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        this.firstResultSet = true;
        this.updateCount = null;
        this.lastExecuteSql = this.sql;
        this.lastExecuteType = StatementExecuteType.ExecuteQuery;
        this.lastExecuteStartNano = -1L;
        this.lastExecuteTimeNano = -1L;
        return this.createChain().preparedStatement_executeQuery(this);
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        this.firstResultSet = false;
        this.updateCount = null;
        this.lastExecuteSql = this.sql;
        this.lastExecuteType = StatementExecuteType.ExecuteUpdate;
        this.lastExecuteStartNano = -1L;
        this.lastExecuteTimeNano = -1L;
        this.updateCount = this.createChain().preparedStatement_executeUpdate(this);
        return this.updateCount;
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.createChain().preparedStatement_getMetaData(this);
    }
    
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.createChain().preparedStatement_getParameterMetaData(this);
    }
    
    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2003, x));
        this.createChain().preparedStatement_setArray(this, parameterIndex, x);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10002, x));
        this.createChain().preparedStatement_setAsciiStream(this, parameterIndex, x);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10002, x, length));
        this.createChain().preparedStatement_setAsciiStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10002, x, length));
        this.createChain().preparedStatement_setAsciiStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x));
        this.createChain().preparedStatement_setBigDecimal(this, parameterIndex, x);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10001, x));
        this.createChain().preparedStatement_setBinaryStream(this, parameterIndex, x);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10001, x, length));
        this.createChain().preparedStatement_setBinaryStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10001, x, length));
        this.createChain().preparedStatement_setBinaryStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2004, x));
        this.createChain().preparedStatement_setBlob(this, parameterIndex, x);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2004, x));
        this.createChain().preparedStatement_setBlob(this, parameterIndex, x);
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2004, x, length));
        this.createChain().preparedStatement_setBlob(this, parameterIndex, x, length);
    }
    
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(16, x));
        this.createChain().preparedStatement_setBoolean(this, parameterIndex, x);
    }
    
    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(-6, x));
        this.createChain().preparedStatement_setByte(this, parameterIndex, x);
    }
    
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10007, x));
        this.createChain().preparedStatement_setBytes(this, parameterIndex, x);
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10003, x));
        this.createChain().preparedStatement_setCharacterStream(this, parameterIndex, x);
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader x, final int length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10003, x, length));
        this.createChain().preparedStatement_setCharacterStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10003, x, length));
        this.createChain().preparedStatement_setCharacterStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2005, x));
        this.createChain().preparedStatement_setClob(this, parameterIndex, x);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2005, x));
        this.createChain().preparedStatement_setClob(this, parameterIndex, x);
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2005, x, length));
        this.createChain().preparedStatement_setClob(this, parameterIndex, x, length);
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x));
        this.createChain().preparedStatement_setDate(this, parameterIndex, x);
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(91, x, cal));
        this.createChain().preparedStatement_setDate(this, parameterIndex, x, cal);
    }
    
    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(8, x));
        this.createChain().preparedStatement_setDouble(this, parameterIndex, x);
    }
    
    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(6, x));
        this.createChain().preparedStatement_setFloat(this, parameterIndex, x);
    }
    
    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        this.setParameter(parameterIndex, this.createParemeter(x));
        this.createChain().preparedStatement_setInt(this, parameterIndex, x);
    }
    
    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x));
        this.createChain().preparedStatement_setLong(this, parameterIndex, x);
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10004, x));
        this.createChain().preparedStatement_setNCharacterStream(this, parameterIndex, x);
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10004, x, length));
        this.createChain().preparedStatement_setNCharacterStream(this, parameterIndex, x, length);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final NClob x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2011, x));
        this.createChain().preparedStatement_setNClob(this, parameterIndex, x);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2011, x));
        this.createChain().preparedStatement_setNClob(this, parameterIndex, x);
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader x, final long length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2011, x, length));
        this.createChain().preparedStatement_setNClob(this, parameterIndex, x, length);
    }
    
    @Override
    public void setNString(final int parameterIndex, final String x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(-9, x));
        this.createChain().preparedStatement_setNString(this, parameterIndex, x);
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        this.setParameter(parameterIndex, this.createParameterNull(sqlType));
        this.createChain().preparedStatement_setNull(this, parameterIndex, sqlType);
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.setParameter(parameterIndex, this.createParameterNull(sqlType));
        this.createChain().preparedStatement_setNull(this, parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        this.setObjectParameter(parameterIndex, x);
        this.createChain().preparedStatement_setObject(this, parameterIndex, x);
    }
    
    private void setObjectParameter(final int parameterIndex, final Object x) {
        if (x == null) {
            this.setParameter(parameterIndex, this.createParameterNull(1111));
            return;
        }
        final Class<?> clazz = x.getClass();
        if (clazz == Byte.class) {
            this.setParameter(parameterIndex, this.createParameter(-6, x));
            return;
        }
        if (clazz == Short.class) {
            this.setParameter(parameterIndex, this.createParameter(5, x));
            return;
        }
        if (clazz == Integer.class) {
            this.setParameter(parameterIndex, this.createParemeter((int)x));
            return;
        }
        if (clazz == Long.class) {
            this.setParameter(parameterIndex, this.createParameter((long)x));
            return;
        }
        if (clazz == String.class) {
            this.setParameter(parameterIndex, this.createParameter((String)x));
            return;
        }
        if (clazz == BigDecimal.class) {
            this.setParameter(parameterIndex, this.createParameter((BigDecimal)x));
            return;
        }
        if (clazz == Float.class) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(6, x));
            return;
        }
        if (clazz == Double.class) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(8, x));
            return;
        }
        if (clazz == Date.class || clazz == java.util.Date.class) {
            this.setParameter(parameterIndex, this.createParameter((java.util.Date)x));
            return;
        }
        if (clazz == Timestamp.class) {
            this.setParameter(parameterIndex, this.createParameter((Timestamp)x));
            return;
        }
        if (clazz == Time.class) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(92, x));
            return;
        }
        if (clazz == Boolean.class) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(16, x));
            return;
        }
        if (clazz == byte[].class) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(10007, x));
            return;
        }
        if (x instanceof InputStream) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(10001, x));
            return;
        }
        if (x instanceof Reader) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(10003, x));
            return;
        }
        if (x instanceof Clob) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(2005, x));
            return;
        }
        if (x instanceof NClob) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(2011, x));
            return;
        }
        if (x instanceof Blob) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(2004, x));
            return;
        }
        final String className = x.getClass().getName();
        if (className.equals("java.time.LocalTime")) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(92, x));
            return;
        }
        if (className.equals("java.time.LocalDate")) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(91, x));
            return;
        }
        if (className.equals("java.time.LocalDateTime")) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(93, x));
            return;
        }
        if (className.equals("java.time.ZonedDateTime")) {
            this.setParameter(parameterIndex, new JdbcParameterImpl(93, x));
            return;
        }
        this.setParameter(parameterIndex, this.createParameter(1111, null));
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(targetSqlType, x));
        this.createChain().preparedStatement_setObject(this, parameterIndex, x, targetSqlType);
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x, targetSqlType, scaleOrLength));
        this.createChain().preparedStatement_setObject(this, parameterIndex, x, targetSqlType, scaleOrLength);
    }
    
    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2006, x));
        this.createChain().preparedStatement_setRef(this, parameterIndex, x);
    }
    
    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(-8, x));
        this.createChain().preparedStatement_setRowId(this, parameterIndex, x);
    }
    
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(2009, x));
        this.createChain().preparedStatement_setSQLXML(this, parameterIndex, x);
    }
    
    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(5, x));
        this.createChain().preparedStatement_setShort(this, parameterIndex, x);
    }
    
    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x));
        this.createChain().preparedStatement_setString(this, parameterIndex, x);
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(92, x));
        this.createChain().preparedStatement_setTime(this, parameterIndex, x);
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(92, x, cal));
        this.createChain().preparedStatement_setTime(this, parameterIndex, x, cal);
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(x));
        this.createChain().preparedStatement_setTimestamp(this, parameterIndex, x);
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(93, x));
        this.createChain().preparedStatement_setTimestamp(this, parameterIndex, x, cal);
    }
    
    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10005, x));
        this.createChain().preparedStatement_setURL(this, parameterIndex, x);
    }
    
    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.setParameter(parameterIndex, this.createParameter(10006, x, length));
        this.createChain().preparedStatement_setUnicodeStream(this, parameterIndex, x, length);
    }
    
    @Override
    public String getLastExecuteSql() {
        return this.sql;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == PreparedStatementProxy.class) {
            return (T)this;
        }
        return super.unwrap(iface);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface == PreparedStatementProxy.class || super.isWrapperFor(iface);
    }
    
    private JdbcParameter createParemeter(final int x) {
        return JdbcParameterInt.valueOf(x);
    }
    
    private JdbcParameter createParameter(final long x) {
        return JdbcParameterLong.valueOf(x);
    }
    
    private JdbcParameter createParameterNull(final int sqlType) {
        return JdbcParameterNull.valueOf(sqlType);
    }
    
    private JdbcParameter createParameter(final java.util.Date x) {
        if (x == null) {
            return JdbcParameterNull.DATE;
        }
        return new JdbcParameterDate(x);
    }
    
    private JdbcParameter createParameter(final BigDecimal x) {
        if (x == null) {
            return JdbcParameterNull.DECIMAL;
        }
        return JdbcParameterDecimal.valueOf(x);
    }
    
    private JdbcParameter createParameter(final String x) {
        if (x == null) {
            return JdbcParameterNull.VARCHAR;
        }
        if (x.length() == 0) {
            return JdbcParameterString.empty;
        }
        return new JdbcParameterString(x);
    }
    
    private JdbcParameter createParameter(final Timestamp x) {
        if (x == null) {
            return JdbcParameterNull.TIMESTAMP;
        }
        return new JdbcParameterTimestamp(x);
    }
    
    private JdbcParameter createParameter(final Object x, final int sqlType, final int scaleOrLength) {
        if (x == null) {
            return JdbcParameterNull.valueOf(sqlType);
        }
        return new JdbcParameterImpl(sqlType, x, -1L, null, scaleOrLength);
    }
    
    private JdbcParameter createParameter(final int sqlType, final Object value, final long length) {
        if (value == null) {
            return JdbcParameterNull.valueOf(sqlType);
        }
        return new JdbcParameterImpl(sqlType, value, length);
    }
    
    private JdbcParameter createParameter(final int sqlType, final Object value) {
        if (value == null) {
            return JdbcParameterNull.valueOf(sqlType);
        }
        return new JdbcParameterImpl(sqlType, value);
    }
    
    public JdbcParameter createParameter(final int sqlType, final Object value, final Calendar calendar) {
        if (value == null) {
            return JdbcParameterNull.valueOf(sqlType);
        }
        return new JdbcParameterImpl(sqlType, value, calendar);
    }
}
