// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.filter.FilterEventAdapter;

public class RandomDataSourceValidateFilter extends FilterEventAdapter
{
    @Override
    protected void statementExecuteUpdateAfter(final StatementProxy statement, final String sql, final int updateCount) {
        this.recordTime(statement);
    }
    
    @Override
    protected void statementExecuteQueryAfter(final StatementProxy statement, final String sql, final ResultSetProxy resultSet) {
        this.recordTime(statement);
    }
    
    @Override
    protected void statementExecuteAfter(final StatementProxy statement, final String sql, final boolean result) {
        this.recordTime(statement);
    }
    
    @Override
    protected void statementExecuteBatchAfter(final StatementProxy statement, final int[] result) {
        this.recordTime(statement);
    }
    
    private void recordTime(final StatementProxy statement) {
        final ConnectionProxy conn = statement.getConnectionProxy();
        if (conn != null) {
            final DataSourceProxy dataSource = conn.getDirectDataSource();
            RandomDataSourceValidateThread.logSuccessTime(dataSource);
        }
    }
}
