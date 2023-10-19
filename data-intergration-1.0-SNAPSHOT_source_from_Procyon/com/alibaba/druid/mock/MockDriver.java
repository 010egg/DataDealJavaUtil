// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import com.alibaba.druid.mock.handler.MySqlMockExecuteHandlerImpl;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverPropertyInfo;
import java.util.Properties;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import com.alibaba.druid.mock.handler.MockExecuteHandler;
import com.alibaba.druid.support.logging.Log;
import java.sql.Driver;

public class MockDriver implements Driver, MockDriverMBean
{
    private static Log LOG;
    public static final MockExecuteHandler DEFAULT_HANDLER;
    private String prefix;
    private String mockPrefix;
    private MockExecuteHandler executeHandler;
    public static final MockDriver instance;
    private final AtomicLong connectCount;
    private final AtomicLong connectionCloseCount;
    private final AtomicLong connectionIdSeed;
    private final List<MockConnection> connections;
    private long idleTimeCount;
    private boolean logExecuteQueryEnable;
    private static final String MBEAN_NAME = "com.alibaba.druid:type=MockDriver";
    
    public MockDriver() {
        this.prefix = "jdbc:fake:";
        this.mockPrefix = "jdbc:mock:";
        this.executeHandler = MockDriver.DEFAULT_HANDLER;
        this.connectCount = new AtomicLong();
        this.connectionCloseCount = new AtomicLong();
        this.connectionIdSeed = new AtomicLong(1000L);
        this.connections = new CopyOnWriteArrayList<MockConnection>();
        this.idleTimeCount = 180000L;
        this.logExecuteQueryEnable = true;
    }
    
    @Override
    public boolean isLogExecuteQueryEnable() {
        return this.logExecuteQueryEnable;
    }
    
    private static Log getLog() {
        if (MockDriver.LOG == null) {
            MockDriver.LOG = LogFactory.getLog(MockDriver.class);
        }
        return MockDriver.LOG;
    }
    
    @Override
    public void setLogExecuteQueryEnable(final boolean logExecuteQueryEnable) {
        this.logExecuteQueryEnable = logExecuteQueryEnable;
    }
    
    @Override
    public long getIdleTimeCount() {
        return this.idleTimeCount;
    }
    
    @Override
    public void setIdleTimeCount(final long idleTimeCount) {
        this.idleTimeCount = idleTimeCount;
    }
    
    public long generateConnectionId() {
        return this.connectionIdSeed.incrementAndGet();
    }
    
    @Override
    public void closeAllConnections() throws SQLException {
        for (int i = 0, size = this.connections.size(); i < size; ++i) {
            final Connection conn = this.connections.get(size - i - 1);
            conn.close();
        }
    }
    
    @Override
    public int getConnectionsSize() {
        return this.connections.size();
    }
    
    public List<MockConnection> getConnections() {
        return this.connections;
    }
    
    protected void incrementConnectionCloseCount() {
        this.connectionCloseCount.incrementAndGet();
    }
    
    @Override
    public long getConnectionCloseCount() {
        return this.connectionCloseCount.get();
    }
    
    protected void afterConnectionClose(final MockConnection conn) {
        this.connectionCloseCount.incrementAndGet();
        this.connections.remove(conn);
        if (getLog().isDebugEnabled()) {
            getLog().debug("conn-" + conn.getId() + " close");
        }
    }
    
    public static boolean registerDriver(final Driver driver) {
        try {
            DriverManager.registerDriver(driver);
            try {
                final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
                final ObjectName objectName = new ObjectName("com.alibaba.druid:type=MockDriver");
                if (!mbeanServer.isRegistered(objectName)) {
                    mbeanServer.registerMBean(MockDriver.instance, objectName);
                }
            }
            catch (Exception ex) {
                getLog().warn("register druid-driver mbean error", ex);
            }
            return true;
        }
        catch (Exception e) {
            getLog().error("registerDriver error", e);
            return false;
        }
    }
    
    public MockExecuteHandler getExecuteHandler() {
        return this.executeHandler;
    }
    
    public void setExecuteHandler(final MockExecuteHandler executeHandler) {
        this.executeHandler = executeHandler;
    }
    
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            return null;
        }
        if (info != null) {
            final Object val = info.get("connectSleep");
            if (val != null) {
                final long millis = Long.parseLong(val.toString());
                try {
                    Thread.sleep(millis);
                }
                catch (InterruptedException ex) {}
            }
        }
        final MockConnection conn = this.createMockConnection(this, url, info);
        if (getLog().isDebugEnabled()) {
            getLog().debug("connect, url " + url + ", id " + conn.getId());
        }
        if (url == null) {
            this.connectCount.incrementAndGet();
            this.connections.add(conn);
            return conn;
        }
        if (url.startsWith(this.prefix)) {
            final String catalog = url.substring(this.prefix.length());
            conn.setCatalog(catalog);
            this.connectCount.incrementAndGet();
            this.connections.add(conn);
            return conn;
        }
        if (url.startsWith(this.mockPrefix)) {
            final String catalog = url.substring(this.mockPrefix.length());
            conn.setCatalog(catalog);
            this.connectCount.incrementAndGet();
            this.connections.add(conn);
            return conn;
        }
        return null;
    }
    
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        return url != null && (url.startsWith(this.prefix) || url.startsWith(this.mockPrefix));
    }
    
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
        return null;
    }
    
    @Override
    public int getMajorVersion() {
        return 0;
    }
    
    @Override
    public int getMinorVersion() {
        return 0;
    }
    
    @Override
    public boolean jdbcCompliant() {
        return true;
    }
    
    public MockResultSet createMockResultSet(final MockStatementBase stmt) {
        return new MockResultSet(stmt);
    }
    
    public ResultSet executeQuery(final MockStatementBase stmt, final String sql) throws SQLException {
        if (this.logExecuteQueryEnable && getLog().isDebugEnabled()) {
            getLog().debug("executeQuery " + sql);
        }
        final MockConnection conn = stmt.getConnection();
        final long idleTimeMillis = System.currentTimeMillis() - conn.getLastActiveTimeMillis();
        if (idleTimeMillis >= this.idleTimeCount) {
            throw new SQLException("connection is idle time count");
        }
        conn.setLastActiveTimeMillis(System.currentTimeMillis());
        this.handleSleep(conn);
        if ("SELECT value FROM _int_1000_".equalsIgnoreCase(sql)) {
            final MockResultSet rs = this.createMockResultSet(stmt);
            for (int i = 0; i < 1000; ++i) {
                rs.getRows().add(new Object[] { i });
            }
            return rs;
        }
        return this.executeHandler.executeQuery(stmt, sql);
    }
    
    public void handleSleep(final MockConnection conn) {
        if (conn != null) {
            conn.handleSleep();
        }
    }
    
    public ResultSet createResultSet(final MockPreparedStatement stmt) {
        final MockResultSet rs = new MockResultSet(stmt);
        final String sql = stmt.getSql();
        if ("SELECT 1".equalsIgnoreCase(sql)) {
            rs.getRows().add(new Object[] { 1 });
        }
        else if ("SELECT NOW()".equalsIgnoreCase(sql)) {
            rs.getRows().add(new Object[] { new Timestamp(System.currentTimeMillis()) });
        }
        else if ("SELECT ?".equalsIgnoreCase(sql)) {
            rs.getRows().add(new Object[] { stmt.getParameters().get(0) });
        }
        return rs;
    }
    
    protected Clob createClob(final MockConnection conn) throws SQLException {
        return new MockClob();
    }
    
    protected Blob createBlob(final MockConnection conn) throws SQLException {
        return new MockBlob();
    }
    
    protected NClob createNClob(final MockConnection conn) throws SQLException {
        return new MockNClob();
    }
    
    protected SQLXML createSQLXML(final MockConnection conn) throws SQLException {
        return new MockSQLXML();
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public MockConnection createMockConnection(final MockDriver driver, final String url, final Properties connectProperties) {
        return new MockConnection(this, url, connectProperties);
    }
    
    public MockPreparedStatement createMockPreparedStatement(final MockConnection conn, final String sql) {
        return new MockPreparedStatement(conn, sql);
    }
    
    public MockStatement createMockStatement(final MockConnection conn) {
        return new MockStatement(conn);
    }
    
    public MockCallableStatement createMockCallableStatement(final MockConnection conn, final String sql) {
        return new MockCallableStatement(conn, sql);
    }
    
    static {
        DEFAULT_HANDLER = new MySqlMockExecuteHandlerImpl();
        registerDriver(instance = new MockDriver());
    }
}
