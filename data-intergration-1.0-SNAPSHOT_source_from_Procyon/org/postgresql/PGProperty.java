// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql;

import java.sql.DriverPropertyInfo;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.Properties;

public enum PGProperty
{
    PG_DBNAME("PGDBNAME", (String)null, "Database name to connect to (may be specified directly in the JDBC URL)", true), 
    PG_HOST("PGHOST", (String)null, "Hostname of the PostgreSQL server (may be specified directly in the JDBC URL)", false), 
    PG_PORT("PGPORT", (String)null, "Port of the PostgreSQL server (may be specified directly in the JDBC URL)"), 
    USER("user", (String)null, "Username to connect to the database as.", true), 
    PASSWORD("password", (String)null, "Password to use when authenticating.", false), 
    PROTOCOL_VERSION("protocolVersion", (String)null, "Force use of a particular protocol version when connecting, if set, disables protocol version fallback.", false, new String[] { "2", "3" }), 
    LOG_LEVEL("loglevel", "0", "The log level", false, new String[] { "0", "1", "2" }), 
    PREPARE_THRESHOLD("prepareThreshold", "5", "Statement prepare threshold. A value of {@code -1} stands for forceBinary"), 
    BINARY_TRANSFER("binaryTransfer", "true", "Use binary format for sending and receiving data if possible"), 
    COMPATIBLE("compatible", "9.4", "Force compatibility of some features with an older version of the driver"), 
    READ_ONLY("readOnly", "false", "Puts this connection in read-only mode"), 
    BINARY_TRANSFER_ENABLE("binaryTransferEnable", "", "Comma separated list of types to enable binary transfer. Either OID numbers or names"), 
    BINARY_TRANSFER_DISABLE("binaryTransferDisable", "", "Comma separated list of types to disable binary transfer. Either OID numbers or names. Overrides values in the driver default set and values set with binaryTransferEnable."), 
    STRING_TYPE("stringtype", (String)null, "The type to bind String parameters as (usually 'varchar', 'unspecified' allows implicit casting to other types)", false, new String[] { "unspecified", "varchar" }), 
    UNKNOWN_LENGTH("unknownLength", Integer.toString(Integer.MAX_VALUE), "Specifies the length to return for types of unknown length"), 
    LOG_UNCLOSED_CONNECTIONS("logUnclosedConnections", "false", "When connections that are not explicitly closed are garbage collected, log the stacktrace from the opening of the connection to trace the leak source"), 
    DISABLE_COLUMN_SANITISER("disableColumnSanitiser", "false", "Enable optimization that disables column name sanitiser"), 
    SSL("ssl", (String)null, "Control use of SSL (any non-null value causes SSL to be required)"), 
    SSL_MODE("sslmode", (String)null, "Parameter governing the use of SSL"), 
    SSL_FACTORY("sslfactory", (String)null, "Provide a SSLSocketFactory class when using SSL."), 
    SSL_FACTORY_ARG("sslfactoryarg", (String)null, "Argument forwarded to constructor of SSLSocketFactory class."), 
    SSL_HOSTNAME_VERIFIER("sslhostnameverifier", (String)null, "A class, implementing javax.net.ssl.HostnameVerifier that can verify the server"), 
    SSL_CERT("sslcert", (String)null, "The location of the client's SSL certificate"), 
    SSL_KEY("sslkey", (String)null, "The location of the client's PKCS#8 SSL key"), 
    SSL_ROOT_CERT("sslrootcert", (String)null, "The location of the root certificate for authenticating the server."), 
    SSL_PASSWORD("sslpassword", (String)null, "The password for the client's ssl key (ignored if sslpasswordcallback is set)"), 
    SSL_PASSWORD_CALLBACK("sslpasswordcallback", (String)null, "A class, implementing javax.security.auth.callback.CallbackHandler that can handle PassworCallback for the ssl password."), 
    TCP_KEEP_ALIVE("tcpKeepAlive", "false", "Enable or disable TCP keep-alive. The default is {@code false}."), 
    LOGIN_TIMEOUT("loginTimeout", "0", "Specify how long to wait for establishment of a database connection."), 
    CONNECT_TIMEOUT("connectTimeout", "0", "The timeout value used for socket connect operations."), 
    SOCKET_TIMEOUT("socketTimeout", "0", "The timeout value used for socket read operations."), 
    RECEIVE_BUFFER_SIZE("receiveBufferSize", "-1", "Socket read buffer size"), 
    SEND_BUFFER_SIZE("sendBufferSize", "-1", "Socket write buffer size"), 
    ASSUME_MIN_SERVER_VERSION("assumeMinServerVersion", (String)null, "Assume the server is at least that version"), 
    APPLICATION_NAME("ApplicationName", (String)null, "name of the application (backend >= 9.0)"), 
    JAAS_APPLICATION_NAME("jaasApplicationName", (String)null, "Specifies the name of the JAAS system or application login configuration."), 
    KERBEROS_SERVER_NAME("kerberosServerName", (String)null, "The Kerberos service name to use when authenticating with GSSAPI."), 
    USE_SPNEGO("useSpnego", "false", "Use SPNEGO in SSPI authentication requests"), 
    GSS_LIB("gsslib", "auto", "Force SSSPI or GSSAPI", false, new String[] { "auto", "sspi", "gssapi" }), 
    SSPI_SERVICE_CLASS("sspiServiceClass", "POSTGRES", "The Windows SSPI service class for SPN"), 
    CHARSET("charSet", (String)null, "The character set to use for data sent to the database or received from the database (for backend <= 7.2)"), 
    ALLOW_ENCODING_CHANGES("allowEncodingChanges", "false", "Allow for changes in client_encoding"), 
    CURRENT_SCHEMA("currentSchema", (String)null, "Specify the schema to be set in the search-path"), 
    TARGET_SERVER_TYPE("targetServerType", "any", "Specifies what kind of server to connect", false, new String[] { "any", "master", "slave", "preferSlave" }), 
    LOAD_BALANCE_HOSTS("loadBalanceHosts", "false", "If disabled hosts are connected in the given order. If enabled hosts are chosen randomly from the set of suitable candidates"), 
    HOST_RECHECK_SECONDS("hostRecheckSeconds", "10", "Specifies period (seconds) after host statuses are checked again in case they have changed");
    
    private String _name;
    private String _defaultValue;
    private boolean _required;
    private String _description;
    private String[] _choices;
    
    private PGProperty(final String name, final String defaultValue, final String description) {
        this(name, defaultValue, description, false);
    }
    
    private PGProperty(final String name, final String defaultValue, final String description, final boolean required) {
        this(name, defaultValue, description, required, (String[])null);
    }
    
    private PGProperty(final String name, final String defaultValue, final String description, final boolean required, final String[] choices) {
        this._name = name;
        this._defaultValue = defaultValue;
        this._required = required;
        this._description = description;
        this._choices = choices;
    }
    
    public String getName() {
        return this._name;
    }
    
    public String getDefaultValue() {
        return this._defaultValue;
    }
    
    public String[] getChoices() {
        return this._choices;
    }
    
    public String get(final Properties properties) {
        return properties.getProperty(this._name, this._defaultValue);
    }
    
    public void set(final Properties properties, final String value) {
        if (value == null) {
            properties.remove(this._name);
        }
        else {
            properties.setProperty(this._name, value);
        }
    }
    
    public boolean getBoolean(final Properties properties) {
        return Boolean.valueOf(this.get(properties));
    }
    
    public int getIntNoCheck(final Properties properties) {
        final String value = this.get(properties);
        return Integer.parseInt(value);
    }
    
    public int getInt(final Properties properties) throws PSQLException {
        final String value = this.get(properties);
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException nfe) {
            throw new PSQLException(GT.tr("{0} parameter value must be an integer but was: {1}", new Object[] { this.getName(), value }), PSQLState.INVALID_PARAMETER_VALUE, nfe);
        }
    }
    
    public Integer getInteger(final Properties properties) throws PSQLException {
        final String value = this.get(properties);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException nfe) {
            throw new PSQLException(GT.tr("{0} parameter value must be an integer but was: {1}", new Object[] { this.getName(), value }), PSQLState.INVALID_PARAMETER_VALUE, nfe);
        }
    }
    
    public void set(final Properties properties, final boolean value) {
        properties.setProperty(this._name, Boolean.toString(value));
    }
    
    public void set(final Properties properties, final int value) {
        properties.setProperty(this._name, Integer.toString(value));
    }
    
    public boolean isPresent(final Properties properties) {
        return this.get(properties) != null;
    }
    
    public DriverPropertyInfo toDriverPropertyInfo(final Properties properties) {
        final DriverPropertyInfo propertyInfo = new DriverPropertyInfo(this._name, this.get(properties));
        propertyInfo.required = this._required;
        propertyInfo.description = this._description;
        propertyInfo.choices = this._choices;
        return propertyInfo;
    }
    
    public static PGProperty forName(final String name) {
        for (final PGProperty property : values()) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }
}
