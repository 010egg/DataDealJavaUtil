// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db;

import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.SQLException;
import ch.qos.logback.core.db.dialect.DBUtil;
import ch.qos.logback.core.db.dialect.SQLDialectCode;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class ConnectionSourceBase extends ContextAwareBase implements ConnectionSource
{
    private boolean started;
    private String user;
    private String password;
    private SQLDialectCode dialectCode;
    private boolean supportsGetGeneratedKeys;
    private boolean supportsBatchUpdates;
    
    public ConnectionSourceBase() {
        this.user = null;
        this.password = null;
        this.dialectCode = SQLDialectCode.UNKNOWN_DIALECT;
        this.supportsGetGeneratedKeys = false;
        this.supportsBatchUpdates = false;
    }
    
    public void discoverConnectionProperties() {
        Connection connection = null;
        try {
            connection = this.getConnection();
            if (connection == null) {
                this.addWarn("Could not get a connection");
                return;
            }
            final DatabaseMetaData meta = connection.getMetaData();
            final DBUtil util = new DBUtil();
            util.setContext(this.getContext());
            this.supportsGetGeneratedKeys = util.supportsGetGeneratedKeys(meta);
            this.supportsBatchUpdates = util.supportsBatchUpdates(meta);
            this.dialectCode = DBUtil.discoverSQLDialect(meta);
            this.addInfo("Driver name=" + meta.getDriverName());
            this.addInfo("Driver version=" + meta.getDriverVersion());
            this.addInfo("supportsGetGeneratedKeys=" + this.supportsGetGeneratedKeys);
        }
        catch (SQLException se) {
            this.addWarn("Could not discover the dialect to use.", se);
        }
        finally {
            DBHelper.closeConnection(connection);
        }
    }
    
    @Override
    public final boolean supportsGetGeneratedKeys() {
        return this.supportsGetGeneratedKeys;
    }
    
    @Override
    public final SQLDialectCode getSQLDialectCode() {
        return this.dialectCode;
    }
    
    public final String getPassword() {
        return this.password;
    }
    
    public final void setPassword(final String password) {
        this.password = password;
    }
    
    public final String getUser() {
        return this.user;
    }
    
    public final void setUser(final String username) {
        this.user = username;
    }
    
    @Override
    public final boolean supportsBatchUpdates() {
        return this.supportsBatchUpdates;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
}
