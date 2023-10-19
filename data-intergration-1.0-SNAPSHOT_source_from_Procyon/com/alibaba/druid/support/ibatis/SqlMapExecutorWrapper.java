// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import com.ibatis.sqlmap.engine.execution.BatchException;
import java.util.Map;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.event.RowHandler;
import java.util.List;
import java.sql.SQLException;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public class SqlMapExecutorWrapper implements SqlMapExecutor
{
    private SqlMapExecutor executor;
    protected final SqlMapClientImpl clientImpl;
    protected final SqlMapClientImplWrapper clientImplWrapper;
    
    public SqlMapExecutorWrapper(final ExtendedSqlMapClient client, final SqlMapExecutor executor) {
        this.executor = executor;
        this.clientImpl = ((client.getClass() == SqlMapClientImpl.class) ? ((SqlMapClientImpl)client) : null);
        this.clientImplWrapper = ((this.clientImpl != null) ? new SqlMapClientImplWrapper(this.clientImpl) : null);
    }
    
    public Object insert(final String id, final Object parameterObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.insert(id, parameterObject);
    }
    
    public Object insert(final String id) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.insert(id);
    }
    
    public int update(final String id, final Object parameterObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.update(id, parameterObject);
    }
    
    public int update(final String id) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.update(id);
    }
    
    public int delete(final String id, final Object parameterObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.delete(id, parameterObject);
    }
    
    public int delete(final String id) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.delete(id);
    }
    
    public Object queryForObject(final String id, final Object parameterObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForObject(id, parameterObject);
    }
    
    public Object queryForObject(final String id) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForObject(id);
    }
    
    public Object queryForObject(final String id, final Object parameterObject, final Object resultObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForObject(id, parameterObject, resultObject);
    }
    
    public List queryForList(final String id, final Object parameterObject) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForList(id, parameterObject);
    }
    
    public List queryForList(final String id) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForList(id);
    }
    
    public List queryForList(final String id, final Object parameterObject, final int skip, final int max) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForList(id, parameterObject, skip, max);
    }
    
    public List queryForList(final String id, final int skip, final int max) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForList(id, skip, max);
    }
    
    public void queryWithRowHandler(final String id, final Object parameterObject, final RowHandler rowHandler) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        this.executor.queryWithRowHandler(id, parameterObject, rowHandler);
    }
    
    public void queryWithRowHandler(final String id, final RowHandler rowHandler) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        this.executor.queryWithRowHandler(id, rowHandler);
    }
    
    public PaginatedList queryForPaginatedList(final String id, final Object parameterObject, final int pageSize) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForPaginatedList(id, parameterObject, pageSize);
    }
    
    public PaginatedList queryForPaginatedList(final String id, final int pageSize) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForPaginatedList(id, pageSize);
    }
    
    public Map queryForMap(final String id, final Object parameterObject, final String keyProp) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForMap(id, parameterObject, keyProp);
    }
    
    public Map queryForMap(final String id, final Object parameterObject, final String keyProp, final String valueProp) throws SQLException {
        this.clientImplWrapper.setLocal(id, this.executor);
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.queryForMap(id, parameterObject, keyProp, valueProp);
    }
    
    public void startBatch() throws SQLException {
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        this.executor.startBatch();
    }
    
    public int executeBatch() throws SQLException {
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.executeBatch();
    }
    
    public List executeBatchDetailed() throws SQLException, BatchException {
        IbatisUtils.setClientImpl(this.executor, this.clientImplWrapper);
        return this.executor.executeBatchDetailed();
    }
}
