// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter;

public class DruidNativeJdbcExtractor extends NativeJdbcExtractorAdapter
{
    protected Connection doGetNativeConnection(final Connection con) throws SQLException {
        return con.unwrap(Connection.class);
    }
    
    public Statement getNativeStatement(final Statement stmt) throws SQLException {
        return stmt.unwrap(Statement.class);
    }
    
    public PreparedStatement getNativePreparedStatement(final PreparedStatement ps) throws SQLException {
        return ps.unwrap(PreparedStatement.class);
    }
    
    public CallableStatement getNativeCallableStatement(final CallableStatement cs) throws SQLException {
        return cs.unwrap(CallableStatement.class);
    }
    
    public ResultSet getNativeResultSet(final ResultSet rs) throws SQLException {
        return rs.unwrap(ResultSet.class);
    }
}
