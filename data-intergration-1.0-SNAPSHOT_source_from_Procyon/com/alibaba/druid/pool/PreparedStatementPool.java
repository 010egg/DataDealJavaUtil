// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.LinkedHashMap;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.util.Map;
import java.sql.PreparedStatement;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import java.sql.SQLException;
import com.alibaba.druid.util.OracleUtils;
import com.alibaba.druid.support.logging.Log;

public class PreparedStatementPool
{
    private static final Log LOG;
    private final LRUCache map;
    private final DruidAbstractDataSource dataSource;
    
    public PreparedStatementPool(final DruidConnectionHolder holder) {
        this.dataSource = holder.getDataSource();
        int initCapacity = holder.getDataSource().getMaxPoolPreparedStatementPerConnectionSize();
        if (initCapacity <= 0) {
            initCapacity = 16;
        }
        this.map = new LRUCache(initCapacity);
    }
    
    public PreparedStatementHolder get(final DruidPooledPreparedStatement.PreparedStatementKey key) throws SQLException {
        final PreparedStatementHolder holder = ((LinkedHashMap<K, PreparedStatementHolder>)this.map).get(key);
        if (holder != null) {
            if (holder.isInUse() && !this.dataSource.isSharePreparedStatements()) {
                return null;
            }
            holder.incrementHitCount();
            this.dataSource.incrementCachedPreparedStatementHitCount();
            if (holder.isEnterOracleImplicitCache()) {
                OracleUtils.exitImplicitCacheToActive(holder.statement);
            }
        }
        else {
            this.dataSource.incrementCachedPreparedStatementMissCount();
        }
        return holder;
    }
    
    public void remove(final PreparedStatementHolder stmtHolder) throws SQLException {
        if (stmtHolder == null) {
            return;
        }
        this.map.remove(stmtHolder.key);
        this.closeRemovedStatement(stmtHolder);
    }
    
    public void put(final PreparedStatementHolder stmtHolder) throws SQLException {
        final PreparedStatement stmt = stmtHolder.statement;
        if (stmt == null) {
            return;
        }
        if (this.dataSource.isOracle() && this.dataSource.isUseOracleImplicitCache()) {
            OracleUtils.enterImplicitCache(stmt);
            stmtHolder.setEnterOracleImplicitCache(true);
        }
        else {
            stmtHolder.setEnterOracleImplicitCache(false);
        }
        final PreparedStatementHolder oldStmtHolder = this.map.put(stmtHolder.key, stmtHolder);
        if (oldStmtHolder == stmtHolder) {
            return;
        }
        if (oldStmtHolder != null) {
            oldStmtHolder.setPooling(false);
            this.closeRemovedStatement(oldStmtHolder);
        }
        else if (stmtHolder.getHitCount() == 0) {
            this.dataSource.incrementCachedPreparedStatementCount();
        }
        stmtHolder.setPooling(true);
        if (PreparedStatementPool.LOG.isDebugEnabled()) {
            String message = null;
            if (stmtHolder.statement instanceof PreparedStatementProxy) {
                final PreparedStatementProxy stmtProxy = (PreparedStatementProxy)stmtHolder.statement;
                if (stmtProxy instanceof CallableStatementProxy) {
                    message = "{conn-" + stmtProxy.getConnectionProxy().getId() + ", cstmt-" + stmtProxy.getId() + "} enter cache";
                }
                else {
                    message = "{conn-" + stmtProxy.getConnectionProxy().getId() + ", pstmt-" + stmtProxy.getId() + "} enter cache";
                }
            }
            else {
                message = "stmt enter cache";
            }
            PreparedStatementPool.LOG.debug(message);
        }
    }
    
    public void clear() {
        final Iterator<Map.Entry<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder>> iter = this.map.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder> entry = iter.next();
            this.closeRemovedStatement(entry.getValue());
            iter.remove();
        }
    }
    
    public void closeRemovedStatement(final PreparedStatementHolder holder) {
        if (PreparedStatementPool.LOG.isDebugEnabled()) {
            String message = null;
            if (holder.statement instanceof PreparedStatementProxy) {
                final PreparedStatementProxy stmtProxy = (PreparedStatementProxy)holder.statement;
                if (stmtProxy instanceof CallableStatementProxy) {
                    message = "{conn-" + stmtProxy.getConnectionProxy().getId() + ", cstmt-" + stmtProxy.getId() + "} exit cache";
                }
                else {
                    message = "{conn-" + stmtProxy.getConnectionProxy().getId() + ", pstmt-" + stmtProxy.getId() + "} exit cache";
                }
            }
            else {
                message = "stmt exit cache";
            }
            PreparedStatementPool.LOG.debug(message);
        }
        holder.setPooling(false);
        if (holder.isInUse()) {
            return;
        }
        if (holder.isEnterOracleImplicitCache()) {
            try {
                OracleUtils.exitImplicitCacheToClose(holder.statement);
            }
            catch (Exception ex) {
                PreparedStatementPool.LOG.error("exitImplicitCacheToClose error", ex);
            }
        }
        this.dataSource.closePreapredStatement(holder);
    }
    
    public Map<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder> getMap() {
        return this.map;
    }
    
    public int size() {
        return this.map.size();
    }
    
    static {
        LOG = LogFactory.getLog(PreparedStatementPool.class);
    }
    
    public enum MethodType
    {
        M1, 
        M2, 
        M3, 
        M4, 
        M5, 
        M6, 
        Precall_1, 
        Precall_2, 
        Precall_3;
    }
    
    public class LRUCache extends LinkedHashMap<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder>
    {
        private static final long serialVersionUID = 1L;
        
        public LRUCache(final int maxSize) {
            super(maxSize, 0.75f, true);
        }
        
        @Override
        protected boolean removeEldestEntry(final Map.Entry<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder> eldest) {
            final boolean remove = this.size() > PreparedStatementPool.this.dataSource.getMaxPoolPreparedStatementPerConnectionSize();
            if (remove) {
                PreparedStatementPool.this.closeRemovedStatement(eldest.getValue());
            }
            return remove;
        }
    }
}
