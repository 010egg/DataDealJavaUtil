// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.encoding;

import com.alibaba.druid.proxy.jdbc.ClobProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import java.io.UnsupportedEncodingException;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.io.StringReader;
import com.alibaba.druid.util.Utils;
import java.io.Reader;
import java.util.Map;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.util.Properties;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterAdapter;

public class EncodingConvertFilter extends FilterAdapter
{
    public static final String ATTR_CHARSET_PARAMETER = "ali.charset.param";
    public static final String ATTR_CHARSET_CONVERTER = "ali.charset.converter";
    private String clientEncoding;
    private String serverEncoding;
    
    @Override
    public ConnectionProxy connection_connect(final FilterChain chain, final Properties info) throws SQLException {
        final ConnectionProxy conn = chain.connection_connect(info);
        final CharsetParameter param = new CharsetParameter();
        param.setClientEncoding(info.getProperty("clientEncoding"));
        param.setServerEncoding(info.getProperty("serverEncoding"));
        if (param.getClientEncoding() == null || "".equalsIgnoreCase(param.getClientEncoding())) {
            param.setClientEncoding(this.clientEncoding);
        }
        if (param.getServerEncoding() == null || "".equalsIgnoreCase(param.getServerEncoding())) {
            param.setServerEncoding(this.serverEncoding);
        }
        conn.putAttribute("ali.charset.param", param);
        conn.putAttribute("ali.charset.converter", new CharsetConvert(param.getClientEncoding(), param.getServerEncoding()));
        return conn;
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final String value = super.resultSet_getString(chain, result, columnIndex);
        return this.decode(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final String value = super.resultSet_getString(chain, result, columnLabel);
        return this.decode(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case -1:
            case 1:
            case 12:
            case 2005: {
                value = super.resultSet_getString(chain, result, columnIndex);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnIndex);
                break;
            }
        }
        return this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Class<T> type) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case -1:
            case 1:
            case 12:
            case 2005: {
                value = super.resultSet_getString(chain, result, columnIndex);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnIndex, type);
                break;
            }
        }
        return (T)this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case -1:
            case 1:
            case 12:
            case 2005: {
                value = super.resultSet_getString(chain, result, columnIndex);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnIndex, map);
                break;
            }
        }
        return this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnIndex = rawResultSet.findColumn(columnLabel);
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case -1:
            case 1:
            case 12:
            case 2005: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnLabel);
                break;
            }
        }
        return this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Class<T> type) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnIndex = rawResultSet.findColumn(columnLabel);
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case -1:
            case 1:
            case 12:
            case 2005: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnLabel, type);
                break;
            }
        }
        return (T)this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        final ResultSet rawResultSet = result.getResultSetRaw();
        final ResultSetMetaData metadata = rawResultSet.getMetaData();
        final int columnIndex = rawResultSet.findColumn(columnLabel);
        final int columnType = metadata.getColumnType(columnIndex);
        Object value = null;
        switch (columnType) {
            case 1: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            case 2005: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            case -1: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            case 12: {
                value = super.resultSet_getString(chain, result, columnLabel);
                break;
            }
            default: {
                value = super.resultSet_getObject(chain, result, columnLabel, map);
                break;
            }
        }
        return this.decodeObject(result.getStatementProxy().getConnectionProxy(), value);
    }
    
    public Object decodeObject(final ConnectionProxy connection, final Object object) throws SQLException {
        if (object instanceof String) {
            return this.decode(connection, (String)object);
        }
        if (object instanceof Reader) {
            final Reader reader = (Reader)object;
            final String text = Utils.read(reader);
            return new StringReader(this.decode(connection, text));
        }
        return object;
    }
    
    public Object decodeObject(final CallableStatementProxy stmt, final Object object) throws SQLException {
        if (object instanceof String) {
            return this.decode(stmt.getConnectionProxy(), (String)object);
        }
        if (object instanceof Reader) {
            final Reader reader = (Reader)object;
            final String text = Utils.read(reader);
            return new StringReader(this.decode(stmt.getConnectionProxy(), text));
        }
        return object;
    }
    
    public String encode(final ConnectionProxy connection, final String s) throws SQLException {
        try {
            final CharsetConvert charsetConvert = (CharsetConvert)connection.getAttribute("ali.charset.converter");
            return charsetConvert.encode(s);
        }
        catch (UnsupportedEncodingException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
    
    public String decode(final ConnectionProxy connection, final String s) throws SQLException {
        try {
            final CharsetConvert charsetConvert = (CharsetConvert)connection.getAttribute("ali.charset.converter");
            return charsetConvert.decode(s);
        }
        catch (UnsupportedEncodingException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql));
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int autoGeneratedKeys) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql), autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql), resultSetType, resultSetConcurrency);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql), resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final int[] columnIndexes) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql), columnIndexes);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, final String sql, final String[] columnNames) throws SQLException {
        return super.connection_prepareStatement(chain, connection, this.encode(connection, sql), columnNames);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        return super.connection_prepareCall(chain, connection, this.encode(connection, sql));
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return super.connection_prepareCall(chain, connection, this.encode(connection, sql), resultSetType, resultSetConcurrency);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return super.connection_prepareCall(chain, connection, this.encode(connection, sql), resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public String connection_nativeSQL(final FilterChain chain, final ConnectionProxy connection, final String sql) throws SQLException {
        final String encodedSql = this.encode(connection, sql);
        return super.connection_nativeSQL(chain, connection, encodedSql);
    }
    
    @Override
    public void statement_addBatch(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        super.statement_addBatch(chain, statement, this.encode(statement.getConnectionProxy(), sql));
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return super.statement_execute(chain, statement, this.encode(statement.getConnectionProxy(), sql));
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        return super.statement_execute(chain, statement, this.encode(statement.getConnectionProxy(), sql), autoGeneratedKeys);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        return super.statement_execute(chain, statement, this.encode(statement.getConnectionProxy(), sql), columnIndexes);
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        return super.statement_execute(chain, statement, this.encode(statement.getConnectionProxy(), sql), columnNames);
    }
    
    @Override
    public ResultSetProxy statement_executeQuery(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return super.statement_executeQuery(chain, statement, this.encode(statement.getConnectionProxy(), sql));
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql) throws SQLException {
        return super.statement_executeUpdate(chain, statement, this.encode(statement.getConnectionProxy(), sql));
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        return super.statement_executeUpdate(chain, statement, this.encode(statement.getConnectionProxy(), sql), autoGeneratedKeys);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        return super.statement_executeUpdate(chain, statement, this.encode(statement.getConnectionProxy(), sql), columnIndexes);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        return super.statement_executeUpdate(chain, statement, this.encode(statement.getConnectionProxy(), sql), columnNames);
    }
    
    @Override
    public void preparedStatement_setString(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final String x) throws SQLException {
        super.preparedStatement_setString(chain, statement, parameterIndex, this.encode(statement.getConnectionProxy(), x));
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        final String text = Utils.read(reader);
        final String encodedText = this.encode(statement.getConnectionProxy(), text);
        super.preparedStatement_setCharacterStream(chain, statement, parameterIndex, new StringReader(encodedText));
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final int length) throws SQLException {
        final String text = Utils.read(reader, length);
        final String encodedText = this.encode(statement.getConnectionProxy(), text);
        super.preparedStatement_setCharacterStream(chain, statement, parameterIndex, new StringReader(encodedText), encodedText.length());
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        final String text = Utils.read(reader, (int)length);
        final String encodedText = this.encode(statement.getConnectionProxy(), text);
        super.preparedStatement_setCharacterStream(chain, statement, parameterIndex, new StringReader(encodedText), encodedText.length());
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.preparedStatement_setObject(chain, statement, parameterIndex, encodedText);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.preparedStatement_setObject(chain, statement, parameterIndex, new StringReader(encodedText2));
        }
        else {
            super.preparedStatement_setObject(chain, statement, parameterIndex, x);
        }
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.preparedStatement_setObject(chain, statement, parameterIndex, encodedText, targetSqlType);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.preparedStatement_setObject(chain, statement, parameterIndex, new StringReader(encodedText2), targetSqlType);
        }
        else {
            super.preparedStatement_setObject(chain, statement, parameterIndex, x, targetSqlType);
        }
    }
    
    @Override
    public void preparedStatement_setObject(final FilterChain chain, final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.preparedStatement_setObject(chain, statement, parameterIndex, encodedText, targetSqlType, scaleOrLength);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.preparedStatement_setObject(chain, statement, parameterIndex, new StringReader(encodedText2), targetSqlType, scaleOrLength);
        }
        else {
            super.preparedStatement_setObject(chain, statement, parameterIndex, x, targetSqlType, scaleOrLength);
        }
    }
    
    @Override
    public long clob_position(final FilterChain chain, final ClobProxy wrapper, final String searchstr, final long start) throws SQLException {
        return chain.clob_position(wrapper, this.encode(wrapper.getConnectionWrapper(), searchstr), start);
    }
    
    @Override
    public String clob_getSubString(final FilterChain chain, final ClobProxy wrapper, final long pos, final int length) throws SQLException {
        final String text = super.clob_getSubString(chain, wrapper, pos, length);
        return this.decode(wrapper.getConnectionWrapper(), text);
    }
    
    @Override
    public Reader clob_getCharacterStream(final FilterChain chain, final ClobProxy wrapper) throws SQLException {
        final Reader reader = super.clob_getCharacterStream(chain, wrapper);
        final String text = Utils.read(reader);
        return new StringReader(this.decode(wrapper.getConnectionWrapper(), text));
    }
    
    @Override
    public Reader clob_getCharacterStream(final FilterChain chain, final ClobProxy wrapper, final long pos, final long length) throws SQLException {
        final Reader reader = super.clob_getCharacterStream(chain, wrapper, pos, length);
        final String text = Utils.read(reader);
        return new StringReader(this.decode(wrapper.getConnectionWrapper(), text));
    }
    
    @Override
    public int clob_setString(final FilterChain chain, final ClobProxy wrapper, final long pos, final String str) throws SQLException {
        return chain.clob_setString(wrapper, pos, this.encode(wrapper.getConnectionWrapper(), str));
    }
    
    @Override
    public int clob_setString(final FilterChain chain, final ClobProxy wrapper, final long pos, final String str, final int offset, final int len) throws SQLException {
        return chain.clob_setString(wrapper, pos, this.encode(wrapper.getConnectionWrapper(), str), offset, len);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        final String text = Utils.read(reader);
        final Reader encodeReader = new StringReader(this.encode(statement.getConnectionProxy(), text));
        super.callableStatement_setCharacterStream(chain, statement, parameterName, encodeReader);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final int length) throws SQLException {
        final String text = Utils.read(reader, length);
        final String encodeText = this.encode(statement.getConnectionProxy(), text);
        final Reader encodeReader = new StringReader(encodeText);
        super.callableStatement_setCharacterStream(chain, statement, parameterName, encodeReader, encodeText.length());
    }
    
    @Override
    public void callableStatement_setCharacterStream(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        final String text = Utils.read(reader, (int)length);
        final String encodeText = this.encode(statement.getConnectionProxy(), text);
        final Reader encodeReader = new StringReader(encodeText);
        super.callableStatement_setCharacterStream(chain, statement, parameterName, encodeReader, (long)encodeText.length());
    }
    
    @Override
    public void callableStatement_setString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final String x) throws SQLException {
        super.callableStatement_setString(chain, statement, parameterName, this.encode(statement.getConnectionProxy(), x));
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.callableStatement_setObject(chain, statement, parameterName, encodedText);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.callableStatement_setObject(chain, statement, parameterName, new StringReader(encodedText2));
        }
        else {
            super.callableStatement_setObject(chain, statement, parameterName, x);
        }
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.callableStatement_setObject(chain, statement, parameterName, encodedText, targetSqlType);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.callableStatement_setObject(chain, statement, parameterName, new StringReader(encodedText2), targetSqlType);
        }
        else {
            super.callableStatement_setObject(chain, statement, parameterName, x, targetSqlType);
        }
    }
    
    @Override
    public void callableStatement_setObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        if (x instanceof String) {
            final String encodedText = this.encode(statement.getConnectionProxy(), (String)x);
            super.callableStatement_setObject(chain, statement, parameterName, encodedText, targetSqlType, scale);
        }
        else if (x instanceof Reader) {
            final String text = Utils.read((Reader)x);
            final String encodedText2 = this.encode(statement.getConnectionProxy(), text);
            super.callableStatement_setObject(chain, statement, parameterName, new StringReader(encodedText2), targetSqlType, scale);
        }
        else {
            super.callableStatement_setObject(chain, statement, parameterName, x, targetSqlType, scale);
        }
    }
    
    @Override
    public String callableStatement_getString(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final String value = super.callableStatement_getString(chain, statement, parameterIndex);
        return this.decode(statement.getConnectionProxy(), value);
    }
    
    @Override
    public String callableStatement_getString(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final String value = super.callableStatement_getString(chain, statement, parameterName);
        return this.decode(statement.getConnectionProxy(), value);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final Object value = chain.callableStatement_getObject(statement, parameterIndex);
        return this.decodeObject(statement, value);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        final Object value = chain.callableStatement_getObject(statement, parameterIndex, map);
        return this.decodeObject(statement, value);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final Object value = chain.callableStatement_getObject(statement, parameterName);
        return this.decodeObject(statement, value);
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        final Object value = chain.callableStatement_getObject(statement, parameterName, map);
        return this.decodeObject(statement, value);
    }
}
