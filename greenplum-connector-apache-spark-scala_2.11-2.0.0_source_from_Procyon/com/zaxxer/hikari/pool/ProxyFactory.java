// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Statement;
import com.zaxxer.hikari.util.FastList;
import java.sql.Connection;

public final class ProxyFactory
{
    private ProxyFactory() {
    }
    
    static ProxyConnection getProxyConnection(final PoolEntry poolEntry, final Connection connection, final FastList<Statement> list, final ProxyLeakTask proxyLeakTask, final long n, final boolean b, final boolean b2) {
        return new HikariProxyConnection(poolEntry, connection, list, proxyLeakTask, n, b, b2);
    }
    
    static Statement getProxyStatement(final ProxyConnection proxyConnection, final Statement statement) {
        return new HikariProxyStatement(proxyConnection, statement);
    }
    
    static CallableStatement getProxyCallableStatement(final ProxyConnection proxyConnection, final CallableStatement callableStatement) {
        return new HikariProxyCallableStatement(proxyConnection, callableStatement);
    }
    
    static PreparedStatement getProxyPreparedStatement(final ProxyConnection proxyConnection, final PreparedStatement preparedStatement) {
        return new HikariProxyPreparedStatement(proxyConnection, preparedStatement);
    }
    
    static ResultSet getProxyResultSet(final ProxyConnection proxyConnection, final ProxyStatement proxyStatement, final ResultSet set) {
        return new HikariProxyResultSet(proxyConnection, proxyStatement, set);
    }
}
