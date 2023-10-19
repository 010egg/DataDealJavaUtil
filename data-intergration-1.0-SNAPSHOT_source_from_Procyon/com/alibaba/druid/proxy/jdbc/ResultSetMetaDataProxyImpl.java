// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import com.alibaba.druid.filter.FilterChain;
import java.sql.SQLException;
import java.sql.Wrapper;
import com.alibaba.druid.filter.FilterChainImpl;
import java.sql.ResultSetMetaData;

public class ResultSetMetaDataProxyImpl extends WrapperProxyImpl implements ResultSetMetaDataProxy
{
    private final ResultSetMetaData metaData;
    private final ResultSetProxy resultSet;
    private FilterChainImpl filterChain;
    
    public ResultSetMetaDataProxyImpl(final ResultSetMetaData metaData, final long id, final ResultSetProxy resultSet) {
        super(metaData, id);
        this.filterChain = null;
        this.metaData = metaData;
        this.resultSet = resultSet;
    }
    
    @Override
    public int getColumnCount() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_getColumnCount(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isAutoIncrement(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isAutoIncrement(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isCaseSensitive(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isCaseSensitive(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isSearchable(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isSearchable(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isCurrency(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isCurrency(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int isNullable(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_isNullable(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isSigned(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isSigned(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getColumnDisplaySize(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_getColumnDisplaySize(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getColumnLabel(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getColumnLabel(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getColumnName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getColumnName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getSchemaName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getSchemaName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getPrecision(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_getPrecision(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getScale(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_getScale(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getTableName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getTableName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getCatalogName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getCatalogName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getColumnType(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.resultSetMetaData_getColumnType(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getColumnTypeName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getColumnTypeName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isReadOnly(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isReadOnly(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isWritable(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isWritable(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isDefinitelyWritable(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.resultSetMetaData_isDefinitelyWritable(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getColumnClassName(final int column) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.resultSetMetaData_getColumnClassName(this, column);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public FilterChainImpl createChain() {
        FilterChainImpl chain = this.filterChain;
        if (chain == null) {
            chain = new FilterChainImpl(this.resultSet.getStatementProxy().getConnectionProxy().getDirectDataSource());
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
    public ResultSetProxy getResultSetProxy() {
        return this.resultSet;
    }
    
    @Override
    public ResultSetMetaData getResultSetMetaDataRaw() {
        return this.metaData;
    }
}
