// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.sql.ResultSet;
import java.sql.Statement;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.pool.DruidPooledConnection;
import java.sql.Connection;
import java.util.Properties;
import java.io.Serializable;
import com.alibaba.druid.pool.ValidConnectionChecker;
import com.alibaba.druid.pool.ValidConnectionCheckerAdapter;

public class OracleValidConnectionChecker extends ValidConnectionCheckerAdapter implements ValidConnectionChecker, Serializable
{
    private static final long serialVersionUID = -2227528634302168877L;
    private int timeout;
    private String defaultValidateQuery;
    
    public OracleValidConnectionChecker() {
        this.timeout = 1;
        this.defaultValidateQuery = "SELECT 'x' FROM DUAL";
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        final String property = properties.getProperty("druid.oracle.pingTimeout");
        if (property != null && property.length() > 0) {
            final int value = Integer.parseInt(property);
            this.setTimeout(value);
        }
    }
    
    public void setTimeout(final int seconds) {
        this.timeout = seconds;
    }
    
    @Override
    public boolean isValidConnection(Connection conn, String validateQuery, final int validationQueryTimeout) throws Exception {
        if (validateQuery == null || validateQuery.isEmpty()) {
            validateQuery = this.defaultValidateQuery;
        }
        if (conn.isClosed()) {
            return false;
        }
        if (conn instanceof DruidPooledConnection) {
            conn = ((DruidPooledConnection)conn).getConnection();
        }
        if (conn instanceof ConnectionProxy) {
            conn = ((ConnectionProxy)conn).getRawObject();
        }
        if (validateQuery == null || validateQuery.isEmpty()) {
            return true;
        }
        final int queryTimeout = (validationQueryTimeout <= 0) ? this.timeout : validationQueryTimeout;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            stmt.setQueryTimeout(queryTimeout);
            rs = stmt.executeQuery(validateQuery);
            return true;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
    }
}
