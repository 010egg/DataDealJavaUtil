// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.ResultSet;
import java.sql.Statement;
import com.alibaba.druid.util.JdbcUtils;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.pool.DruidPooledConnection;
import java.sql.Connection;
import java.util.Properties;
import com.alibaba.druid.util.Utils;
import java.lang.reflect.Method;
import com.alibaba.druid.support.logging.Log;
import java.io.Serializable;
import com.alibaba.druid.pool.ValidConnectionChecker;
import com.alibaba.druid.pool.ValidConnectionCheckerAdapter;

public class MySqlValidConnectionChecker extends ValidConnectionCheckerAdapter implements ValidConnectionChecker, Serializable
{
    public static final int DEFAULT_VALIDATION_QUERY_TIMEOUT = 1;
    public static final String DEFAULT_VALIDATION_QUERY = "SELECT 1";
    private static final long serialVersionUID = 1L;
    private static final Log LOG;
    private Class<?> clazz;
    private Method ping;
    private boolean usePingMethod;
    
    public MySqlValidConnectionChecker() {
        this.usePingMethod = false;
        try {
            this.clazz = Utils.loadClass("com.mysql.jdbc.MySQLConnection");
            if (this.clazz == null) {
                this.clazz = Utils.loadClass("com.mysql.cj.jdbc.ConnectionImpl");
            }
            if (this.clazz != null) {
                this.ping = this.clazz.getMethod("pingInternal", Boolean.TYPE, Integer.TYPE);
            }
            if (this.ping != null) {
                this.usePingMethod = true;
            }
        }
        catch (Exception e) {
            MySqlValidConnectionChecker.LOG.warn("Cannot resolve com.mysql.jdbc.Connection.ping method.  Will use 'SELECT 1' instead.", e);
        }
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        final String property = properties.getProperty("druid.mysql.usePingMethod");
        if ("true".equals(property)) {
            this.setUsePingMethod(true);
        }
        else if ("false".equals(property)) {
            this.setUsePingMethod(false);
        }
    }
    
    public boolean isUsePingMethod() {
        return this.usePingMethod;
    }
    
    public void setUsePingMethod(final boolean usePingMethod) {
        this.usePingMethod = usePingMethod;
    }
    
    @Override
    public boolean isValidConnection(Connection conn, final String validateQuery, int validationQueryTimeout) throws Exception {
        if (conn.isClosed()) {
            return false;
        }
        if (this.usePingMethod) {
            if (conn instanceof DruidPooledConnection) {
                conn = ((DruidPooledConnection)conn).getConnection();
            }
            if (conn instanceof ConnectionProxy) {
                conn = ((ConnectionProxy)conn).getRawObject();
            }
            if (this.clazz.isAssignableFrom(conn.getClass())) {
                if (validationQueryTimeout <= 0) {
                    validationQueryTimeout = 1;
                }
                try {
                    this.ping.invoke(conn, true, validationQueryTimeout * 1000);
                }
                catch (InvocationTargetException e) {
                    final Throwable cause = e.getCause();
                    if (cause instanceof SQLException) {
                        throw (SQLException)cause;
                    }
                    throw e;
                }
                return true;
            }
        }
        String query = validateQuery;
        if (validateQuery == null || validateQuery.isEmpty()) {
            query = "SELECT 1";
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
    
    static {
        LOG = LogFactory.getLog(MySqlValidConnectionChecker.class);
    }
}
