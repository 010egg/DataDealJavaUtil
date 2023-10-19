// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public abstract class ProxyPreparedStatement extends ProxyStatement implements PreparedStatement
{
    ProxyPreparedStatement(final ProxyConnection connection, final PreparedStatement statement) {
        super(connection, statement);
    }
    
    @Override
    public boolean execute() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).execute();
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        this.connection.markCommitStateDirty();
        final ResultSet resultSet = ((PreparedStatement)this.delegate).executeQuery();
        return ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).executeUpdate();
    }
    
    @Override
    public long executeLargeUpdate() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).executeLargeUpdate();
    }
}
