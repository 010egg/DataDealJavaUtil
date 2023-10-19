// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc23;

import java.util.HashMap;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.StringRefAddr;
import javax.naming.Reference;
import org.postgresql.ds.PGPooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.Driver;
import org.postgresql.ds.PGPoolingDataSource;
import javax.sql.PooledConnection;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import org.postgresql.ds.PGConnectionPoolDataSource;
import java.util.Stack;
import java.util.Map;
import org.postgresql.ds.common.BaseDataSource;

public abstract class AbstractJdbc23PoolingDataSource extends BaseDataSource
{
    protected static Map dataSources;
    protected String dataSourceName;
    private int initialConnections;
    private int maxConnections;
    private boolean initialized;
    private Stack available;
    private Stack used;
    private Object lock;
    private PGConnectionPoolDataSource source;
    private ConnectionEventListener connectionEventListener;
    
    public AbstractJdbc23PoolingDataSource() {
        this.initialConnections = 0;
        this.maxConnections = 0;
        this.initialized = false;
        this.available = new Stack();
        this.used = new Stack();
        this.lock = new Object();
        this.connectionEventListener = new ConnectionEventListener() {
            @Override
            public void connectionClosed(final ConnectionEvent event) {
                ((PooledConnection)event.getSource()).removeConnectionEventListener(this);
                synchronized (AbstractJdbc23PoolingDataSource.this.lock) {
                    if (AbstractJdbc23PoolingDataSource.this.available == null) {
                        return;
                    }
                    final boolean removed = AbstractJdbc23PoolingDataSource.this.used.remove(event.getSource());
                    if (removed) {
                        AbstractJdbc23PoolingDataSource.this.available.push(event.getSource());
                        AbstractJdbc23PoolingDataSource.this.lock.notify();
                    }
                }
            }
            
            @Override
            public void connectionErrorOccurred(final ConnectionEvent event) {
                ((PooledConnection)event.getSource()).removeConnectionEventListener(this);
                synchronized (AbstractJdbc23PoolingDataSource.this.lock) {
                    if (AbstractJdbc23PoolingDataSource.this.available == null) {
                        return;
                    }
                    AbstractJdbc23PoolingDataSource.this.used.remove(event.getSource());
                    AbstractJdbc23PoolingDataSource.this.lock.notify();
                }
            }
        };
    }
    
    public static PGPoolingDataSource getDataSource(final String name) {
        return AbstractJdbc23PoolingDataSource.dataSources.get(name);
    }
    
    @Override
    public String getDescription() {
        return "Pooling DataSource '" + this.dataSourceName + " from " + Driver.getVersion();
    }
    
    @Override
    public void setServerName(final String serverName) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        super.setServerName(serverName);
    }
    
    @Override
    public void setDatabaseName(final String databaseName) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        super.setDatabaseName(databaseName);
    }
    
    @Override
    public void setUser(final String user) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        super.setUser(user);
    }
    
    @Override
    public void setPassword(final String password) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        super.setPassword(password);
    }
    
    @Override
    public void setPortNumber(final int portNumber) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        super.setPortNumber(portNumber);
    }
    
    public int getInitialConnections() {
        return this.initialConnections;
    }
    
    public void setInitialConnections(final int initialConnections) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        this.initialConnections = initialConnections;
    }
    
    public int getMaxConnections() {
        return this.maxConnections;
    }
    
    public void setMaxConnections(final int maxConnections) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        this.maxConnections = maxConnections;
    }
    
    public String getDataSourceName() {
        return this.dataSourceName;
    }
    
    public void setDataSourceName(final String dataSourceName) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot set Data Source properties after DataSource has been used");
        }
        if (this.dataSourceName != null && dataSourceName != null && dataSourceName.equals(this.dataSourceName)) {
            return;
        }
        synchronized (AbstractJdbc23PoolingDataSource.dataSources) {
            if (getDataSource(dataSourceName) != null) {
                throw new IllegalArgumentException("DataSource with name '" + dataSourceName + "' already exists!");
            }
            if (this.dataSourceName != null) {
                AbstractJdbc23PoolingDataSource.dataSources.remove(this.dataSourceName);
            }
            this.addDataSource(this.dataSourceName = dataSourceName);
        }
    }
    
    public void initialize() throws SQLException {
        synchronized (this.lock) {
            this.source = this.createConnectionPool();
            try {
                this.source.initializeFrom(this);
            }
            catch (Exception e) {
                throw new PSQLException(GT.tr("Failed to setup DataSource."), PSQLState.UNEXPECTED_ERROR, e);
            }
            while (this.available.size() < this.initialConnections) {
                this.available.push(this.source.getPooledConnection());
            }
            this.initialized = true;
        }
    }
    
    protected boolean isInitialized() {
        return this.initialized;
    }
    
    protected PGConnectionPoolDataSource createConnectionPool() {
        return new PGConnectionPoolDataSource();
    }
    
    @Override
    public Connection getConnection(final String user, final String password) throws SQLException {
        if (user == null || (user.equals(this.getUser()) && ((password == null && this.getPassword() == null) || (password != null && password.equals(this.getPassword()))))) {
            return this.getConnection();
        }
        if (!this.initialized) {
            this.initialize();
        }
        return super.getConnection(user, password);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        if (!this.initialized) {
            this.initialize();
        }
        return this.getPooledConnection();
    }
    
    public void close() {
        synchronized (this.lock) {
            while (this.available.size() > 0) {
                final PGPooledConnection pci = this.available.pop();
                try {
                    pci.close();
                }
                catch (SQLException ex) {}
            }
            this.available = null;
            while (this.used.size() > 0) {
                final PGPooledConnection pci = this.used.pop();
                pci.removeConnectionEventListener(this.connectionEventListener);
                try {
                    pci.close();
                }
                catch (SQLException ex2) {}
            }
            this.used = null;
        }
        this.removeStoredDataSource();
    }
    
    protected void removeStoredDataSource() {
        synchronized (AbstractJdbc23PoolingDataSource.dataSources) {
            AbstractJdbc23PoolingDataSource.dataSources.remove(this.dataSourceName);
        }
    }
    
    protected abstract void addDataSource(final String p0);
    
    private Connection getPooledConnection() throws SQLException {
        PooledConnection pc = null;
        Label_0135: {
            synchronized (this.lock) {
                if (this.available == null) {
                    throw new PSQLException(GT.tr("DataSource has been closed."), PSQLState.CONNECTION_DOES_NOT_EXIST);
                }
                while (this.available.size() <= 0) {
                    if (this.maxConnections == 0 || this.used.size() < this.maxConnections) {
                        pc = this.source.getPooledConnection();
                        this.used.push(pc);
                        break Label_0135;
                    }
                    try {
                        this.lock.wait(1000L);
                    }
                    catch (InterruptedException e) {}
                }
                pc = this.available.pop();
                this.used.push(pc);
            }
        }
        pc.addConnectionEventListener(this.connectionEventListener);
        return pc.getConnection();
    }
    
    @Override
    public Reference getReference() throws NamingException {
        final Reference ref = super.getReference();
        ref.add(new StringRefAddr("dataSourceName", this.dataSourceName));
        if (this.initialConnections > 0) {
            ref.add(new StringRefAddr("initialConnections", Integer.toString(this.initialConnections)));
        }
        if (this.maxConnections > 0) {
            ref.add(new StringRefAddr("maxConnections", Integer.toString(this.maxConnections)));
        }
        return ref;
    }
    
    static {
        AbstractJdbc23PoolingDataSource.dataSources = new HashMap();
    }
}
