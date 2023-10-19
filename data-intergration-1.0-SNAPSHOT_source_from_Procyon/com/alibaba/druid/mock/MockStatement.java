// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import com.alibaba.druid.util.jdbc.StatementBase;

public class MockStatement extends StatementBase implements MockStatementBase, Statement
{
    public static final String ERROR_SQL = "THROW ERROR";
    protected MockConnection mockConnection;
    
    public MockStatement(final Connection connection) {
        super(connection);
        if (connection instanceof MockConnection) {
            this.mockConnection = (MockConnection)connection;
        }
    }
    
    @Override
    protected void checkOpen() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        if (this.mockConnection != null) {
            this.mockConnection.checkState();
        }
    }
    
    @Override
    public MockConnection getConnection() {
        return this.mockConnection;
    }
    
    public void setFakeConnection(final MockConnection fakeConnection) {
        this.setConnection(this.mockConnection = fakeConnection);
    }
    
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        this.checkOpen();
        if (this.mockConnection != null && this.mockConnection.getDriver() != null) {
            return this.mockConnection.getDriver().executeQuery(this, sql);
        }
        return new MockResultSet(this);
    }
    
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        this.checkOpen();
        if (this.mockConnection != null) {
            this.mockConnection.handleSleep();
        }
        return 0;
    }
    
    @Override
    public boolean execute(final String sql) throws SQLException {
        this.checkOpen();
        if ("THROW ERROR".equals(sql)) {
            throw new SQLException();
        }
        if (this.mockConnection != null) {
            this.mockConnection.setLastSql(sql);
            this.mockConnection.handleSleep();
        }
        return false;
    }
}
