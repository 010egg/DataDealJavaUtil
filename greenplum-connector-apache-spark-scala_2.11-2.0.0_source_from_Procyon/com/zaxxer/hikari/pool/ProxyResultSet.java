// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public abstract class ProxyResultSet implements ResultSet
{
    protected final ProxyConnection connection;
    protected final ProxyStatement statement;
    final ResultSet delegate;
    
    protected ProxyResultSet(final ProxyConnection connection, final ProxyStatement statement, final ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.delegate = resultSet;
    }
    
    final SQLException checkException(final SQLException e) {
        return this.connection.checkException(e);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + this.delegate;
    }
    
    @Override
    public final Statement getStatement() throws SQLException {
        return this.statement;
    }
    
    @Override
    public void updateRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.updateRow();
    }
    
    @Override
    public void insertRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.insertRow();
    }
    
    @Override
    public void deleteRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.deleteRow();
    }
    
    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this.delegate)) {
            return (T)this.delegate;
        }
        if (this.delegate != null) {
            return this.delegate.unwrap(iface);
        }
        throw new SQLException("Wrapped ResultSet is not an instance of " + iface);
    }
}
