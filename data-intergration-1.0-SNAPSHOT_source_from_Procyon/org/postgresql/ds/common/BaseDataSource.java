// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.common;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.StringRefAddr;
import javax.naming.Reference;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.Driver;
import org.postgresql.PGProperty;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;
import java.io.PrintWriter;
import javax.naming.Referenceable;

public abstract class BaseDataSource implements Referenceable
{
    private transient PrintWriter logger;
    private String serverName;
    private String databaseName;
    private String user;
    private String password;
    private int portNumber;
    private Properties properties;
    
    public BaseDataSource() {
        this.serverName = "localhost";
        this.databaseName = "";
        this.portNumber = 0;
        this.properties = new Properties();
    }
    
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.user, this.password);
    }
    
    public Connection getConnection(final String user, final String password) throws SQLException {
        try {
            final Connection con = DriverManager.getConnection(this.getUrl(), user, password);
            if (this.logger != null) {
                this.logger.println("Created a non-pooled connection for " + user + " at " + this.getUrl());
            }
            return con;
        }
        catch (SQLException e) {
            if (this.logger != null) {
                this.logger.println("Failed to create a non-pooled connection for " + user + " at " + this.getUrl() + ": " + e);
            }
            throw e;
        }
    }
    
    public PrintWriter getLogWriter() {
        return this.logger;
    }
    
    public void setLogWriter(final PrintWriter printWriter) {
        this.logger = printWriter;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public void setServerName(final String serverName) {
        if (serverName == null || serverName.equals("")) {
            this.serverName = "localhost";
        }
        else {
            this.serverName = serverName;
        }
    }
    
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }
    
    public abstract String getDescription();
    
    public String getUser() {
        return this.user;
    }
    
    public void setUser(final String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public int getPortNumber() {
        return this.portNumber;
    }
    
    public void setPortNumber(final int portNumber) {
        this.portNumber = portNumber;
    }
    
    public String getCompatible() {
        return PGProperty.COMPATIBLE.get(this.properties);
    }
    
    public void setCompatible(final String compatible) {
        PGProperty.COMPATIBLE.set(this.properties, compatible);
    }
    
    public int getLoginTimeout() {
        return PGProperty.LOGIN_TIMEOUT.getIntNoCheck(this.properties);
    }
    
    public void setLoginTimeout(final int loginTimeout) {
        PGProperty.LOGIN_TIMEOUT.set(this.properties, loginTimeout);
    }
    
    public int getConnectTimeout() {
        return PGProperty.CONNECT_TIMEOUT.getIntNoCheck(this.properties);
    }
    
    public void setConnectTimeout(final int connectTimeout) {
        PGProperty.CONNECT_TIMEOUT.set(this.properties, connectTimeout);
    }
    
    public int getLogLevel() {
        return PGProperty.LOG_LEVEL.getIntNoCheck(this.properties);
    }
    
    public void setLogLevel(final int logLevel) {
        PGProperty.LOG_LEVEL.set(this.properties, logLevel);
    }
    
    public int getProtocolVersion() {
        if (!PGProperty.PROTOCOL_VERSION.isPresent(this.properties)) {
            return 0;
        }
        return PGProperty.PROTOCOL_VERSION.getIntNoCheck(this.properties);
    }
    
    public void setProtocolVersion(final int protocolVersion) {
        if (protocolVersion == 0) {
            PGProperty.PROTOCOL_VERSION.set(this.properties, null);
        }
        else {
            PGProperty.PROTOCOL_VERSION.set(this.properties, protocolVersion);
        }
    }
    
    public int getReceiveBufferSize() {
        return PGProperty.RECEIVE_BUFFER_SIZE.getIntNoCheck(this.properties);
    }
    
    public void setReceiveBufferSize(final int nbytes) {
        PGProperty.RECEIVE_BUFFER_SIZE.set(this.properties, nbytes);
    }
    
    public int getSendBufferSize() {
        return PGProperty.SEND_BUFFER_SIZE.getIntNoCheck(this.properties);
    }
    
    public void setSendBufferSize(final int nbytes) {
        PGProperty.SEND_BUFFER_SIZE.set(this.properties, nbytes);
    }
    
    public void setPrepareThreshold(final int count) {
        PGProperty.PREPARE_THRESHOLD.set(this.properties, count);
    }
    
    public int getPrepareThreshold() {
        return PGProperty.PREPARE_THRESHOLD.getIntNoCheck(this.properties);
    }
    
    public void setUnknownLength(final int unknownLength) {
        PGProperty.UNKNOWN_LENGTH.set(this.properties, unknownLength);
    }
    
    public int getUnknownLength() {
        return PGProperty.UNKNOWN_LENGTH.getIntNoCheck(this.properties);
    }
    
    public void setSocketTimeout(final int seconds) {
        PGProperty.SOCKET_TIMEOUT.set(this.properties, seconds);
    }
    
    public int getSocketTimeout() {
        return PGProperty.SOCKET_TIMEOUT.getIntNoCheck(this.properties);
    }
    
    public void setSsl(final boolean enabled) {
        if (enabled) {
            PGProperty.SSL.set(this.properties, true);
        }
        else {
            PGProperty.SSL.set(this.properties, null);
        }
    }
    
    public boolean getSsl() {
        return PGProperty.SSL.isPresent(this.properties);
    }
    
    public void setSslfactory(final String classname) {
        PGProperty.SSL_FACTORY.set(this.properties, classname);
    }
    
    public String getSslfactory() {
        return PGProperty.SSL_FACTORY.get(this.properties);
    }
    
    public String getSslMode() {
        return PGProperty.SSL_MODE.get(this.properties);
    }
    
    public void setSslMode(final String mode) {
        PGProperty.SSL_MODE.set(this.properties, mode);
    }
    
    public String getSslFactoryArg() {
        return PGProperty.SSL_FACTORY_ARG.get(this.properties);
    }
    
    public void setSslFactoryArg(final String arg) {
        PGProperty.SSL_FACTORY_ARG.set(this.properties, arg);
    }
    
    public String getSslHostnameVerifier() {
        return PGProperty.SSL_HOSTNAME_VERIFIER.get(this.properties);
    }
    
    public void setSslHostnameVerifier(final String className) {
        PGProperty.SSL_HOSTNAME_VERIFIER.set(this.properties, className);
    }
    
    public String getSslCert() {
        return PGProperty.SSL_CERT.get(this.properties);
    }
    
    public void setSslCert(final String file) {
        PGProperty.SSL_CERT.set(this.properties, file);
    }
    
    public String getSslKey() {
        return PGProperty.SSL_KEY.get(this.properties);
    }
    
    public void setSslKey(final String file) {
        PGProperty.SSL_KEY.set(this.properties, file);
    }
    
    public String getSslRootCert() {
        return PGProperty.SSL_ROOT_CERT.get(this.properties);
    }
    
    public void setSslRootCert(final String file) {
        PGProperty.SSL_ROOT_CERT.set(this.properties, file);
    }
    
    public String getSslPassword() {
        return PGProperty.SSL_PASSWORD.get(this.properties);
    }
    
    public void setSslPassword(final String password) {
        PGProperty.SSL_PASSWORD.set(this.properties, password);
    }
    
    public String getSslPasswordCallback() {
        return PGProperty.SSL_PASSWORD_CALLBACK.get(this.properties);
    }
    
    public void setSslPasswordCallback(final String className) {
        PGProperty.SSL_PASSWORD_CALLBACK.set(this.properties, className);
    }
    
    public void setApplicationName(final String applicationName) {
        PGProperty.APPLICATION_NAME.set(this.properties, applicationName);
    }
    
    public String getApplicationName() {
        return PGProperty.APPLICATION_NAME.get(this.properties);
    }
    
    public void setTargetServerType(final String targetServerType) {
        PGProperty.TARGET_SERVER_TYPE.set(this.properties, targetServerType);
    }
    
    public String getTargetServerType() {
        return PGProperty.TARGET_SERVER_TYPE.get(this.properties);
    }
    
    public void setLoadBalanceHosts(final boolean loadBalanceHosts) {
        PGProperty.LOAD_BALANCE_HOSTS.set(this.properties, loadBalanceHosts);
    }
    
    public boolean getLoadBalanceHosts() {
        return PGProperty.LOAD_BALANCE_HOSTS.isPresent(this.properties);
    }
    
    public void setHostRecheckSeconds(final int hostRecheckSeconds) {
        PGProperty.HOST_RECHECK_SECONDS.set(this.properties, hostRecheckSeconds);
    }
    
    public int getHostRecheckSeconds() {
        return PGProperty.HOST_RECHECK_SECONDS.getIntNoCheck(this.properties);
    }
    
    public void setTcpKeepAlive(final boolean enabled) {
        PGProperty.TCP_KEEP_ALIVE.set(this.properties, enabled);
    }
    
    public boolean getTcpKeepAlive() {
        return PGProperty.TCP_KEEP_ALIVE.getBoolean(this.properties);
    }
    
    public void setBinaryTransfer(final boolean enabled) {
        PGProperty.BINARY_TRANSFER.set(this.properties, enabled);
    }
    
    public boolean getBinaryTransfer() {
        return PGProperty.BINARY_TRANSFER.getBoolean(this.properties);
    }
    
    public void setBinaryTransferEnable(final String oidList) {
        PGProperty.BINARY_TRANSFER_ENABLE.set(this.properties, oidList);
    }
    
    public String getBinaryTransferEnable() {
        return PGProperty.BINARY_TRANSFER_ENABLE.get(this.properties);
    }
    
    public void setBinaryTransferDisable(final String oidList) {
        PGProperty.BINARY_TRANSFER_DISABLE.set(this.properties, oidList);
    }
    
    public String getBinaryTransferDisable() {
        return PGProperty.BINARY_TRANSFER_DISABLE.get(this.properties);
    }
    
    public String getStringType() {
        return PGProperty.STRING_TYPE.get(this.properties);
    }
    
    public void setStringType(final String stringType) {
        PGProperty.STRING_TYPE.set(this.properties, stringType);
    }
    
    public boolean isColumnSanitiserDisabled() {
        return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(this.properties);
    }
    
    public boolean getDisableColumnSanitiser() {
        return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(this.properties);
    }
    
    public void setDisableColumnSanitiser(final boolean disableColumnSanitiser) {
        PGProperty.DISABLE_COLUMN_SANITISER.set(this.properties, disableColumnSanitiser);
    }
    
    public String getCurrentSchema() {
        return PGProperty.CURRENT_SCHEMA.get(this.properties);
    }
    
    public void setCurrentSchema(final String currentSchema) {
        PGProperty.CURRENT_SCHEMA.set(this.properties, currentSchema);
    }
    
    public boolean getReadOnly() {
        return PGProperty.READ_ONLY.getBoolean(this.properties);
    }
    
    public void setReadOnly(final boolean readOnly) {
        PGProperty.READ_ONLY.set(this.properties, readOnly);
    }
    
    public boolean getLogUnclosedConnections() {
        return PGProperty.LOG_UNCLOSED_CONNECTIONS.getBoolean(this.properties);
    }
    
    public void setLogUnclosedConnections(final boolean enabled) {
        PGProperty.LOG_UNCLOSED_CONNECTIONS.set(this.properties, enabled);
    }
    
    public String getAssumeMinServerVersion() {
        return PGProperty.ASSUME_MIN_SERVER_VERSION.get(this.properties);
    }
    
    public void setAssumeMinServerVersion(final String minVersion) {
        PGProperty.ASSUME_MIN_SERVER_VERSION.set(this.properties, minVersion);
    }
    
    public String getJaasApplicationName() {
        return PGProperty.JAAS_APPLICATION_NAME.get(this.properties);
    }
    
    public void setJaasApplicationName(final String name) {
        PGProperty.JAAS_APPLICATION_NAME.set(this.properties, name);
    }
    
    public String getKerberosServerName() {
        return PGProperty.KERBEROS_SERVER_NAME.get(this.properties);
    }
    
    public void setKerberosServerName(final String serverName) {
        PGProperty.KERBEROS_SERVER_NAME.set(this.properties, serverName);
    }
    
    public boolean getUseSpNego() {
        return PGProperty.USE_SPNEGO.getBoolean(this.properties);
    }
    
    public void setUseSpNego(final boolean use) {
        PGProperty.USE_SPNEGO.set(this.properties, use);
    }
    
    public String getGssLib() {
        return PGProperty.GSS_LIB.get(this.properties);
    }
    
    public void setGssLib(final String lib) {
        PGProperty.GSS_LIB.set(this.properties, lib);
    }
    
    public String getSspiServiceClass() {
        return PGProperty.SSPI_SERVICE_CLASS.get(this.properties);
    }
    
    public void setSspiServiceClass(final String serviceClass) {
        PGProperty.SSPI_SERVICE_CLASS.set(this.properties, serviceClass);
    }
    
    public String getCharset() {
        return PGProperty.CHARSET.get(this.properties);
    }
    
    public void setCharset(final String charset) {
        PGProperty.CHARSET.set(this.properties, charset);
    }
    
    public boolean getAllowEncodingChanges() {
        return PGProperty.ALLOW_ENCODING_CHANGES.getBoolean(this.properties);
    }
    
    public void setAllowEncodingChanges(final boolean allow) {
        PGProperty.ALLOW_ENCODING_CHANGES.set(this.properties, allow);
    }
    
    public String getUrl() {
        final StringBuilder url = new StringBuilder(100);
        url.append("jdbc:postgresql://");
        url.append(this.serverName);
        if (this.portNumber != 0) {
            url.append(":").append(this.portNumber);
        }
        url.append("/").append(this.databaseName);
        final StringBuilder query = new StringBuilder(100);
        for (final PGProperty property : PGProperty.values()) {
            if (property.isPresent(this.properties)) {
                if (query.length() != 0) {
                    query.append("&");
                }
                query.append(property.getName());
                query.append("=");
                query.append(property.get(this.properties));
            }
        }
        if (query.length() > 0) {
            url.append("?");
            url.append((CharSequence)query);
        }
        return url.toString();
    }
    
    public void setUrl(final String url) {
        final Properties p = Driver.parseURL(url, null);
        for (final PGProperty property : PGProperty.values()) {
            this.setProperty(property, property.get(p));
        }
    }
    
    public String getProperty(final String name) throws SQLException {
        final PGProperty pgProperty = PGProperty.forName(name);
        if (pgProperty != null) {
            return this.getProperty(pgProperty);
        }
        throw new PSQLException(GT.tr("Unsupported property name: {0}", name), PSQLState.INVALID_PARAMETER_VALUE);
    }
    
    public void setProperty(final String name, final String value) throws SQLException {
        final PGProperty pgProperty = PGProperty.forName(name);
        if (pgProperty != null) {
            this.setProperty(pgProperty, value);
            return;
        }
        throw new PSQLException(GT.tr("Unsupported property name: {0}", name), PSQLState.INVALID_PARAMETER_VALUE);
    }
    
    public String getProperty(final PGProperty property) {
        return property.get(this.properties);
    }
    
    public void setProperty(final PGProperty property, final String value) {
        if (value == null) {
            return;
        }
        switch (property) {
            case PG_HOST: {
                this.serverName = value;
                break;
            }
            case PG_PORT: {
                try {
                    this.portNumber = Integer.parseInt(value);
                }
                catch (NumberFormatException e) {
                    this.portNumber = 0;
                }
                break;
            }
            case PG_DBNAME: {
                this.databaseName = value;
                break;
            }
            case USER: {
                this.user = value;
                break;
            }
            case PASSWORD: {
                this.password = value;
                break;
            }
            default: {
                this.properties.setProperty(property.getName(), value);
                break;
            }
        }
    }
    
    protected Reference createReference() {
        return new Reference(this.getClass().getName(), PGObjectFactory.class.getName(), null);
    }
    
    @Override
    public Reference getReference() throws NamingException {
        final Reference ref = this.createReference();
        ref.add(new StringRefAddr("serverName", this.serverName));
        if (this.portNumber != 0) {
            ref.add(new StringRefAddr("portNumber", Integer.toString(this.portNumber)));
        }
        ref.add(new StringRefAddr("databaseName", this.databaseName));
        if (this.user != null) {
            ref.add(new StringRefAddr("user", this.user));
        }
        if (this.password != null) {
            ref.add(new StringRefAddr("password", this.password));
        }
        for (final PGProperty property : PGProperty.values()) {
            if (property.isPresent(this.properties)) {
                ref.add(new StringRefAddr(property.getName(), property.get(this.properties)));
            }
        }
        return ref;
    }
    
    public void setFromReference(final Reference ref) {
        this.databaseName = this.getReferenceProperty(ref, "databaseName");
        final String port = this.getReferenceProperty(ref, "portNumber");
        if (port != null) {
            this.portNumber = Integer.parseInt(port);
        }
        this.serverName = this.getReferenceProperty(ref, "serverName");
        this.user = this.getReferenceProperty(ref, "user");
        this.password = this.getReferenceProperty(ref, "password");
        for (final PGProperty property : PGProperty.values()) {
            property.set(this.properties, this.getReferenceProperty(ref, property.getName()));
        }
    }
    
    private String getReferenceProperty(final Reference ref, final String propertyName) {
        final RefAddr addr = ref.get(propertyName);
        if (addr == null) {
            return null;
        }
        return (String)addr.getContent();
    }
    
    protected void writeBaseObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(this.serverName);
        out.writeObject(this.databaseName);
        out.writeObject(this.user);
        out.writeObject(this.password);
        out.writeInt(this.portNumber);
        out.writeObject(this.properties);
    }
    
    protected void readBaseObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.serverName = (String)in.readObject();
        this.databaseName = (String)in.readObject();
        this.user = (String)in.readObject();
        this.password = (String)in.readObject();
        this.portNumber = in.readInt();
        this.properties = (Properties)in.readObject();
    }
    
    public void initializeFrom(final BaseDataSource source) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        source.writeBaseObject(oos);
        oos.close();
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(bais);
        this.readBaseObject(ois);
    }
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL DataSource unable to load PostgreSQL JDBC Driver");
        }
    }
}
