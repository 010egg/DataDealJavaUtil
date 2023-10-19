// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.alibaba.druid.util.jdbc.PreparedStatementBase;

public class MockPreparedStatement extends PreparedStatementBase implements MockStatementBase, PreparedStatement
{
    private final String sql;
    
    public MockPreparedStatement(final MockConnection conn, final String sql) {
        super(conn);
        this.sql = sql;
    }
    
    public String getSql() {
        return this.sql;
    }
    
    @Override
    public MockConnection getConnection() throws SQLException {
        return (MockConnection)super.getConnection();
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        this.checkOpen();
        final MockConnection conn = this.getConnection();
        if (conn != null && conn.getDriver() != null) {
            return conn.getDriver().executeQuery(this, this.sql);
        }
        if (conn != null) {
            conn.handleSleep();
            return conn.getDriver().createMockResultSet(this);
        }
        return new MockResultSet(this);
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        this.checkOpen();
        if (this.getConnection() != null) {
            this.getConnection().handleSleep();
        }
        return 0;
    }
    
    @Override
    public boolean execute() throws SQLException {
        this.checkOpen();
        if (this.getConnection() != null) {
            this.getConnection().handleSleep();
        }
        return false;
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        this.checkOpen();
        if (this.resultSet == null) {
            this.resultSet = this.getConnection().getDriver().createResultSet(this);
        }
        return this.resultSet;
    }
}
