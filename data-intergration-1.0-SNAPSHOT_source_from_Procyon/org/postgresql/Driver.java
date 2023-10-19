// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql;

import java.sql.SQLFeatureNotSupportedException;
import java.sql.DriverManager;
import org.postgresql.util.HostSpec;
import java.sql.DriverPropertyInfo;
import org.postgresql.jdbc4.Jdbc4Connection;
import java.sql.SQLException;
import java.security.AccessControlException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.Connection;
import java.io.InputStream;
import java.util.Enumeration;
import java.net.URL;
import java.util.ArrayList;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;
import org.postgresql.util.SharedTimer;
import org.postgresql.core.Logger;

public class Driver implements java.sql.Driver
{
    public static final int DEBUG = 2;
    public static final int INFO = 1;
    public static final int OFF = 0;
    private static Driver registeredDriver;
    private static final Logger logger;
    private static boolean logLevelSet;
    private static SharedTimer sharedTimer;
    private Properties defaultProperties;
    public static final int MAJORVERSION = 9;
    public static final int MINORVERSION = 4;
    private static String[] protocols;
    
    private synchronized Properties getDefaultProperties() throws IOException {
        if (this.defaultProperties != null) {
            return this.defaultProperties;
        }
        try {
            this.defaultProperties = AccessController.doPrivileged((PrivilegedExceptionAction<Properties>)new PrivilegedExceptionAction() {
                @Override
                public Object run() throws IOException {
                    return Driver.this.loadDefaultProperties();
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw (IOException)e.getException();
        }
        synchronized (Driver.class) {
            if (!Driver.logLevelSet) {
                final String driverLogLevel = PGProperty.LOG_LEVEL.get(this.defaultProperties);
                if (driverLogLevel != null) {
                    try {
                        setLogLevel(Integer.parseInt(driverLogLevel));
                    }
                    catch (Exception ex) {}
                }
            }
        }
        return this.defaultProperties;
    }
    
    private Properties loadDefaultProperties() throws IOException {
        final Properties merged = new Properties();
        try {
            PGProperty.USER.set(merged, System.getProperty("user.name"));
        }
        catch (SecurityException ex) {}
        ClassLoader cl = this.getClass().getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        if (cl == null) {
            Driver.logger.debug("Can't find a classloader for the Driver; not loading driver configuration");
            return merged;
        }
        Driver.logger.debug("Loading driver configuration via classloader " + cl);
        final ArrayList urls = new ArrayList();
        final Enumeration urlEnum = cl.getResources("org/postgresql/driverconfig.properties");
        while (urlEnum.hasMoreElements()) {
            urls.add(urlEnum.nextElement());
        }
        for (int i = urls.size() - 1; i >= 0; --i) {
            final URL url = urls.get(i);
            Driver.logger.debug("Loading driver configuration from: " + url);
            final InputStream is = url.openStream();
            merged.load(is);
            is.close();
        }
        return merged;
    }
    
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        if (!url.startsWith("jdbc:postgresql:")) {
            return null;
        }
        Properties defaults;
        try {
            defaults = this.getDefaultProperties();
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Error loading default settings from driverconfig.properties"), PSQLState.UNEXPECTED_ERROR, ioe);
        }
        Properties props = new Properties(defaults);
        if (info != null) {
            final Enumeration e = info.propertyNames();
            while (e.hasMoreElements()) {
                final String propName = e.nextElement();
                final String propValue = info.getProperty(propName);
                if (propValue == null) {
                    throw new PSQLException(GT.tr("Properties for the driver contains a non-string value for the key ") + propName, PSQLState.UNEXPECTED_ERROR);
                }
                props.setProperty(propName, propValue);
            }
        }
        if ((props = parseURL(url, props)) == null) {
            Driver.logger.debug("Error in url: " + url);
            return null;
        }
        try {
            Driver.logger.debug("Connecting with URL: " + url);
            final long timeout = timeout(props);
            if (timeout <= 0L) {
                return makeConnection(url, props);
            }
            final ConnectThread ct = new ConnectThread(url, props);
            final Thread thread = new Thread(ct, "PostgreSQL JDBC driver connection thread");
            thread.setDaemon(true);
            thread.start();
            return ct.getResult(timeout);
        }
        catch (PSQLException ex1) {
            Driver.logger.debug("Connection error:", ex1);
            throw ex1;
        }
        catch (AccessControlException ace) {
            throw new PSQLException(GT.tr("Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to."), PSQLState.UNEXPECTED_ERROR, ace);
        }
        catch (Exception ex2) {
            Driver.logger.debug("Unexpected connection error:", ex2);
            throw new PSQLException(GT.tr("Something unusual has occurred to cause the driver to fail. Please report this exception."), PSQLState.UNEXPECTED_ERROR, ex2);
        }
    }
    
    private static Connection makeConnection(final String url, final Properties props) throws SQLException {
        return new Jdbc4Connection(hostSpecs(props), user(props), database(props), props, url);
    }
    
    @Override
    public boolean acceptsURL(final String url) {
        return parseURL(url, null) != null;
    }
    
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) {
        Properties copy = new Properties(info);
        final Properties parse = parseURL(url, copy);
        if (parse != null) {
            copy = parse;
        }
        final PGProperty[] knownProperties = PGProperty.values();
        final DriverPropertyInfo[] props = new DriverPropertyInfo[knownProperties.length];
        for (int i = 0; i < props.length; ++i) {
            props[i] = knownProperties[i].toDriverPropertyInfo(copy);
        }
        return props;
    }
    
    @Override
    public int getMajorVersion() {
        return 9;
    }
    
    @Override
    public int getMinorVersion() {
        return 4;
    }
    
    public static String getVersion() {
        return "PostgreSQL 9.4 JDBC4.1 (build 1201)";
    }
    
    @Override
    public boolean jdbcCompliant() {
        return false;
    }
    
    public static Properties parseURL(final String url, final Properties defaults) {
        final Properties urlProps = new Properties(defaults);
        String l_urlServer = url;
        String l_urlArgs = "";
        final int l_qPos = url.indexOf(63);
        if (l_qPos != -1) {
            l_urlServer = url.substring(0, l_qPos);
            l_urlArgs = url.substring(l_qPos + 1);
        }
        if (!l_urlServer.startsWith("jdbc:postgresql:")) {
            return null;
        }
        l_urlServer = l_urlServer.substring("jdbc:postgresql:".length());
        if (l_urlServer.startsWith("//")) {
            l_urlServer = l_urlServer.substring(2);
            final int slash = l_urlServer.indexOf(47);
            if (slash == -1) {
                return null;
            }
            urlProps.setProperty("PGDBNAME", l_urlServer.substring(slash + 1));
            final String[] addresses = l_urlServer.substring(0, slash).split(",");
            final StringBuilder hosts = new StringBuilder();
            final StringBuilder ports = new StringBuilder();
            for (int addr = 0; addr < addresses.length; ++addr) {
                final String address = addresses[addr];
                final int portIdx = address.lastIndexOf(58);
                if (portIdx != -1 && address.lastIndexOf(93) < portIdx) {
                    final String portStr = address.substring(portIdx + 1);
                    try {
                        Integer.parseInt(portStr);
                    }
                    catch (NumberFormatException ex) {
                        return null;
                    }
                    ports.append(portStr);
                    hosts.append(address.subSequence(0, portIdx));
                }
                else {
                    ports.append("5432");
                    hosts.append(address);
                }
                ports.append(',');
                hosts.append(',');
            }
            ports.setLength(ports.length() - 1);
            hosts.setLength(hosts.length() - 1);
            urlProps.setProperty("PGPORT", ports.toString());
            urlProps.setProperty("PGHOST", hosts.toString());
        }
        else {
            urlProps.setProperty("PGPORT", "5432");
            urlProps.setProperty("PGHOST", "localhost");
            urlProps.setProperty("PGDBNAME", l_urlServer);
        }
        final String[] args = l_urlArgs.split("&");
        for (int i = 0; i < args.length; ++i) {
            final String token = args[i];
            if (token.length() != 0) {
                final int l_pos = token.indexOf(61);
                if (l_pos == -1) {
                    urlProps.setProperty(token, "");
                }
                else {
                    urlProps.setProperty(token.substring(0, l_pos), token.substring(l_pos + 1));
                }
            }
        }
        return urlProps;
    }
    
    private static HostSpec[] hostSpecs(final Properties props) {
        final String[] hosts = props.getProperty("PGHOST").split(",");
        final String[] ports = props.getProperty("PGPORT").split(",");
        final HostSpec[] hostSpecs = new HostSpec[hosts.length];
        for (int i = 0; i < hostSpecs.length; ++i) {
            hostSpecs[i] = new HostSpec(hosts[i], Integer.parseInt(ports[i]));
        }
        return hostSpecs;
    }
    
    private static String user(final Properties props) {
        return props.getProperty("user", "");
    }
    
    private static String database(final Properties props) {
        return props.getProperty("PGDBNAME", "");
    }
    
    private static long timeout(final Properties props) {
        final String timeout = PGProperty.LOGIN_TIMEOUT.get(props);
        if (timeout != null) {
            try {
                return (long)(Float.parseFloat(timeout) * 1000.0f);
            }
            catch (NumberFormatException e) {
                Driver.logger.debug("Couldn't parse loginTimeout value: " + timeout);
            }
        }
        return DriverManager.getLoginTimeout() * 1000L;
    }
    
    public static SQLFeatureNotSupportedException notImplemented(final Class callClass, final String functionName) {
        return new SQLFeatureNotSupportedException(GT.tr("Method {0} is not yet implemented.", callClass.getName() + "." + functionName), PSQLState.NOT_IMPLEMENTED.getState());
    }
    
    public static void setLogLevel(final int logLevel) {
        synchronized (Driver.class) {
            Driver.logger.setLogLevel(logLevel);
            Driver.logLevelSet = true;
        }
    }
    
    public static int getLogLevel() {
        synchronized (Driver.class) {
            return Driver.logger.getLogLevel();
        }
    }
    
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw notImplemented(this.getClass(), "getParentLogger()");
    }
    
    public static SharedTimer getSharedTimer() {
        return Driver.sharedTimer;
    }
    
    public static void register() throws SQLException {
        if (isRegistered()) {
            throw new IllegalStateException("Driver is already registered. It can only be registered once.");
        }
        final Driver registeredDriver = new Driver();
        DriverManager.registerDriver(registeredDriver);
        Driver.registeredDriver = registeredDriver;
    }
    
    public static void deregister() throws SQLException {
        if (!isRegistered()) {
            throw new IllegalStateException("Driver is not registered (or it has not been registered using Driver.register() method)");
        }
        DriverManager.deregisterDriver(Driver.registeredDriver);
        Driver.registeredDriver = null;
    }
    
    public static boolean isRegistered() {
        return Driver.registeredDriver != null;
    }
    
    static {
        logger = new Logger();
        Driver.logLevelSet = false;
        Driver.sharedTimer = new SharedTimer(Driver.logger);
        try {
            register();
        }
        catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
        Driver.protocols = new String[] { "jdbc", "postgresql" };
    }
    
    private static class ConnectThread implements Runnable
    {
        private final String url;
        private final Properties props;
        private Connection result;
        private Throwable resultException;
        private boolean abandoned;
        
        ConnectThread(final String url, final Properties props) {
            this.url = url;
            this.props = props;
        }
        
        @Override
        public void run() {
            Connection conn;
            Throwable error;
            try {
                conn = makeConnection(this.url, this.props);
                error = null;
            }
            catch (Throwable t) {
                conn = null;
                error = t;
            }
            synchronized (this) {
                if (this.abandoned) {
                    if (conn != null) {
                        try {
                            conn.close();
                        }
                        catch (SQLException e) {}
                    }
                }
                else {
                    this.result = conn;
                    this.resultException = error;
                    this.notify();
                }
            }
        }
        
        public Connection getResult(final long timeout) throws SQLException {
            final long expiry = System.currentTimeMillis() + timeout;
            synchronized (this) {
                while (this.result == null) {
                    if (this.resultException != null) {
                        if (this.resultException instanceof SQLException) {
                            this.resultException.fillInStackTrace();
                            throw (SQLException)this.resultException;
                        }
                        throw new PSQLException(GT.tr("Something unusual has occurred to cause the driver to fail. Please report this exception."), PSQLState.UNEXPECTED_ERROR, this.resultException);
                    }
                    else {
                        final long delay = expiry - System.currentTimeMillis();
                        if (delay <= 0L) {
                            this.abandoned = true;
                            throw new PSQLException(GT.tr("Connection attempt timed out."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
                        }
                        try {
                            this.wait(delay);
                        }
                        catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            this.abandoned = true;
                            throw new RuntimeException(GT.tr("Interrupted while attempting to connect."));
                        }
                    }
                }
                return this.result;
            }
        }
    }
}
