// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.client.event.RowHandler;
import java.util.Map;
import com.ibatis.common.util.PaginatedList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.lang.reflect.Method;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;

public class SqlMapClientImplWrapper extends SqlMapClientImpl
{
    private SqlMapClientImpl raw;
    private static Method getLocalSqlMapSessionMethod;
    private ConcurrentMap<String, IbatisStatementInfo> statementInfoMap;
    
    public SqlMapClientImplWrapper(final SqlMapClientImpl raw) {
        super(raw.getDelegate());
        this.statementInfoMap = new ConcurrentHashMap<String, IbatisStatementInfo>(16, 0.75f, 1);
        this.raw = raw;
    }
    
    public void setLocal(final String id, final SqlMapExecutor executor) {
        IbatisStatementInfo stmtInfo = this.statementInfoMap.get(id);
        if (stmtInfo != null) {
            JdbcSqlStat.setContextSqlName(stmtInfo.getId());
            JdbcSqlStat.setContextSqlFile(stmtInfo.getResource());
            return;
        }
        Object statement = null;
        if (executor instanceof SqlMapSessionImpl) {
            statement = ((SqlMapSessionImpl)executor).getMappedStatement(id);
        }
        if (executor instanceof SqlMapClientImpl) {
            statement = ((SqlMapClientImpl)executor).getMappedStatement(id);
        }
        if (statement == null) {
            return;
        }
        final String stmtId = IbatisUtils.getId(statement);
        final String stmtResource = IbatisUtils.getResource(statement);
        stmtInfo = new IbatisStatementInfo(stmtId, stmtResource);
        this.statementInfoMap.putIfAbsent(id, stmtInfo);
        JdbcSqlStat.setContextSqlName(stmtId);
        JdbcSqlStat.setContextSqlFile(stmtResource);
    }
    
    protected SqlMapSessionWrapper getLocalSqlMapSessionWrapper() {
        try {
            if (SqlMapClientImplWrapper.getLocalSqlMapSessionMethod == null) {
                (SqlMapClientImplWrapper.getLocalSqlMapSessionMethod = this.raw.getClass().getDeclaredMethod("getLocalSqlMapSession", (Class<?>[])new Class[0])).setAccessible(true);
            }
            final SqlMapSessionImpl sessionImpl = (SqlMapSessionImpl)SqlMapClientImplWrapper.getLocalSqlMapSessionMethod.invoke(this.raw, new Object[0]);
            IbatisUtils.set(sessionImpl, this);
            return new SqlMapSessionWrapper((ExtendedSqlMapClient)this.raw, (SqlMapSession)sessionImpl);
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    public SqlMapSession openSession(final Connection conn) {
        final SqlMapSession session = this.raw.openSession(conn);
        IbatisUtils.setClientImpl((SqlMapExecutor)session, this);
        return (SqlMapSession)new SqlMapSessionWrapper((ExtendedSqlMapClient)this.raw, session);
    }
    
    public SqlMapSession getSession() {
        final SqlMapSession session = this.raw.getSession();
        IbatisUtils.setClientImpl((SqlMapExecutor)session, this);
        return (SqlMapSession)new SqlMapSessionWrapper((ExtendedSqlMapClient)this.raw, session);
    }
    
    public Object insert(final String id, final Object param) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().insert(id, param);
    }
    
    public Object insert(final String id) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().insert(id);
    }
    
    public int update(final String id, final Object param) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().update(id, param);
    }
    
    public int update(final String id) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().update(id);
    }
    
    public int delete(final String id, final Object param) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().delete(id, param);
    }
    
    public int delete(final String id) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().delete(id);
    }
    
    public Object queryForObject(final String id, final Object paramObject) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForObject(id, paramObject);
    }
    
    public Object queryForObject(final String id) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForObject(id);
    }
    
    public Object queryForObject(final String id, final Object paramObject, final Object resultObject) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForObject(id, paramObject, resultObject);
    }
    
    public List queryForList(final String id, final Object paramObject) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForList(id, paramObject);
    }
    
    public List queryForList(final String id) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForList(id);
    }
    
    public List queryForList(final String id, final Object paramObject, final int skip, final int max) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForList(id, paramObject, skip, max);
    }
    
    public List queryForList(final String id, final int skip, final int max) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForList(id, skip, max);
    }
    
    @Deprecated
    public PaginatedList queryForPaginatedList(final String id, final Object paramObject, final int pageSize) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForPaginatedList(id, paramObject, pageSize);
    }
    
    @Deprecated
    public PaginatedList queryForPaginatedList(final String id, final int pageSize) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForPaginatedList(id, pageSize);
    }
    
    public Map queryForMap(final String id, final Object paramObject, final String keyProp) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForMap(id, paramObject, keyProp);
    }
    
    public Map queryForMap(final String id, final Object paramObject, final String keyProp, final String valueProp) throws SQLException {
        return this.getLocalSqlMapSessionWrapper().queryForMap(id, paramObject, keyProp, valueProp);
    }
    
    public void queryWithRowHandler(final String id, final Object paramObject, final RowHandler rowHandler) throws SQLException {
        this.getLocalSqlMapSessionWrapper().queryWithRowHandler(id, paramObject, rowHandler);
    }
    
    public void queryWithRowHandler(final String id, final RowHandler rowHandler) throws SQLException {
        this.getLocalSqlMapSessionWrapper().queryWithRowHandler(id, rowHandler);
    }
    
    public void startTransaction() throws SQLException {
        this.getLocalSqlMapSessionWrapper().startTransaction();
    }
    
    public void startTransaction(final int transactionIsolation) throws SQLException {
        this.getLocalSqlMapSessionWrapper().startTransaction(transactionIsolation);
    }
    
    public void commitTransaction() throws SQLException {
        this.getLocalSqlMapSessionWrapper().commitTransaction();
    }
    
    public void endTransaction() throws SQLException {
        try {
            this.getLocalSqlMapSessionWrapper().endTransaction();
        }
        finally {
            this.getLocalSqlMapSessionWrapper().close();
        }
    }
    
    public void startBatch() throws SQLException {
        this.getLocalSqlMapSessionWrapper().startBatch();
    }
    
    public int executeBatch() throws SQLException {
        return this.getLocalSqlMapSessionWrapper().executeBatch();
    }
    
    public List executeBatchDetailed() throws SQLException, BatchException {
        return this.getLocalSqlMapSessionWrapper().executeBatchDetailed();
    }
    
    public void setUserConnection(final Connection connection) throws SQLException {
        try {
            this.getLocalSqlMapSessionWrapper().setUserConnection(connection);
        }
        finally {
            if (connection == null) {
                this.getLocalSqlMapSessionWrapper().close();
            }
        }
    }
    
    static {
        SqlMapClientImplWrapper.getLocalSqlMapSessionMethod = null;
    }
    
    public static class IbatisStatementInfo
    {
        private final String id;
        private final String resource;
        
        public IbatisStatementInfo(final String id, final String resource) {
            this.id = id;
            this.resource = resource;
        }
        
        public String getId() {
            return this.id;
        }
        
        public String getResource() {
            return this.resource;
        }
    }
}
