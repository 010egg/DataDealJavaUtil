// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.sql.Statement;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.SQLException;
import java.sql.Connection;
import java.io.Serializable;
import com.alibaba.druid.pool.ValidConnectionChecker;
import com.alibaba.druid.pool.ValidConnectionCheckerAdapter;

public class MSSQLValidConnectionChecker extends ValidConnectionCheckerAdapter implements ValidConnectionChecker, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean isValidConnection(final Connection c, final String validateQuery, final int validationQueryTimeout) throws Exception {
        if (c.isClosed()) {
            return false;
        }
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            if (validationQueryTimeout > 0) {
                stmt.setQueryTimeout(validationQueryTimeout);
            }
            stmt.execute(validateQuery);
            return true;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            JdbcUtils.close(stmt);
        }
    }
}
