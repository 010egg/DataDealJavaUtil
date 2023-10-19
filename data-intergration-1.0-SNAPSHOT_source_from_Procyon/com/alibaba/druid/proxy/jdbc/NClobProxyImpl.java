// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.filter.FilterChain;
import java.sql.Clob;
import java.sql.NClob;

public class NClobProxyImpl extends ClobProxyImpl implements NClobProxy
{
    private final NClob nclob;
    
    public NClobProxyImpl(final DataSourceProxy dataSource, final ConnectionProxy connection, final NClob clob) {
        super(dataSource, connection, clob);
        this.nclob = clob;
    }
    
    @Override
    public FilterChain createChain() {
        return new FilterChainImpl(this.dataSource);
    }
    
    @Override
    public NClob getRawNClob() {
        return this.nclob;
    }
}
