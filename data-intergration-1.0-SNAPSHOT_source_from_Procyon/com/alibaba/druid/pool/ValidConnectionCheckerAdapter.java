// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Properties;
import java.sql.ResultSet;
import java.sql.Statement;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.Connection;

public class ValidConnectionCheckerAdapter implements ValidConnectionChecker
{
    @Override
    public boolean isValidConnection(final Connection conn, final String query, final int validationQueryTimeout) throws Exception {
        if (query == null || query.length() == 0) {
            return true;
        }
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (validationQueryTimeout > 0) {
                stmt.setQueryTimeout(validationQueryTimeout);
            }
            rs = stmt.executeQuery(query);
            return true;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
}
