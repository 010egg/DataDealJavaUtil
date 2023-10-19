// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.sql.SQLWarning;
import org.postgresql.core.Utils;
import java.sql.SQLClientInfoException;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.sql.Statement;
import org.postgresql.jdbc2.AbstractJdbc2Array;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.Array;
import java.sql.Struct;
import org.postgresql.core.BaseConnection;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import org.postgresql.Driver;
import java.sql.Clob;
import java.sql.SQLException;
import org.postgresql.core.TypeInfo;
import org.postgresql.PGProperty;
import org.postgresql.util.HostSpec;
import java.util.Properties;
import java.util.regex.Pattern;
import java.sql.SQLPermission;
import org.postgresql.jdbc3g.AbstractJdbc3gConnection;

abstract class AbstractJdbc4Connection extends AbstractJdbc3gConnection
{
    private static final SQLPermission SQL_PERMISSION_ABORT;
    private static final Pattern PATTERN_GET_SCHEMA;
    private final Properties _clientInfo;
    
    public AbstractJdbc4Connection(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final String url) throws SQLException {
        super(hostSpecs, user, database, info, url);
        final TypeInfo types = this.getTypeInfo();
        if (this.haveMinimumServerVersion("8.3")) {
            types.addCoreType("xml", 142, 2009, "java.sql.SQLXML", 143);
        }
        this._clientInfo = new Properties();
        if (this.haveMinimumServerVersion("9.0")) {
            String appName = PGProperty.APPLICATION_NAME.get(info);
            if (appName == null) {
                appName = "";
            }
            this._clientInfo.put("ApplicationName", appName);
        }
    }
    
    @Override
    public Clob createClob() throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "createClob()");
    }
    
    @Override
    public Blob createBlob() throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "createBlob()");
    }
    
    @Override
    public NClob createNClob() throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "createNClob()");
    }
    
    @Override
    public SQLXML createSQLXML() throws SQLException {
        this.checkClosed();
        return new Jdbc4SQLXML(this);
    }
    
    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "createStruct(String, Object[])");
    }
    
    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        this.checkClosed();
        final int oid = this.getTypeInfo().getPGArrayType(typeName);
        if (oid == 0) {
            throw new PSQLException(GT.tr("Unable to find server array type for provided name {0}.", typeName), PSQLState.INVALID_NAME);
        }
        final char delim = this.getTypeInfo().getArrayDelimiter(oid);
        final StringBuilder sb = new StringBuilder();
        appendArray(sb, elements, delim);
        return new Jdbc4Array(this, oid, sb.toString());
    }
    
    private static void appendArray(final StringBuilder sb, final Object elements, final char delim) {
        sb.append('{');
        for (int nElements = java.lang.reflect.Array.getLength(elements), i = 0; i < nElements; ++i) {
            if (i > 0) {
                sb.append(delim);
            }
            final Object o = java.lang.reflect.Array.get(elements, i);
            if (o == null) {
                sb.append("NULL");
            }
            else if (o.getClass().isArray()) {
                appendArray(sb, o, delim);
            }
            else {
                final String s = o.toString();
                AbstractJdbc2Array.escapeArrayElement(sb, s);
            }
        }
        sb.append('}');
    }
    
    @Override
    public boolean isValid(final int timeout) throws SQLException {
        if (this.isClosed()) {
            return false;
        }
        if (timeout < 0) {
            throw new PSQLException(GT.tr("Invalid timeout ({0}<0).", timeout), PSQLState.INVALID_PARAMETER_VALUE);
        }
        boolean valid = false;
        Statement stmt = null;
        try {
            if (!this.isClosed()) {
                stmt = this.createStatement();
                stmt.setQueryTimeout(timeout);
                stmt.executeUpdate("");
                valid = true;
            }
        }
        catch (SQLException e) {
            this.getLogger().log(GT.tr("Validating connection."), e);
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (Exception ex) {}
            }
        }
        return valid;
    }
    
    @Override
    public void setClientInfo(final String name, String value) throws SQLClientInfoException {
        try {
            this.checkClosed();
        }
        catch (SQLException cause) {
            final Map<String, ClientInfoStatus> failures = new HashMap<String, ClientInfoStatus>();
            failures.put(name, ClientInfoStatus.REASON_UNKNOWN);
            throw new SQLClientInfoException(GT.tr("This connection has been closed."), failures, cause);
        }
        if (this.haveMinimumServerVersion("9.0") && "ApplicationName".equals(name)) {
            if (value == null) {
                value = "";
            }
            try {
                final StringBuilder sql = new StringBuilder("SET application_name = '");
                Utils.escapeLiteral(sql, value, this.getStandardConformingStrings());
                sql.append("'");
                this.execSQLUpdate(sql.toString());
            }
            catch (SQLException sqle) {
                final Map<String, ClientInfoStatus> failures = new HashMap<String, ClientInfoStatus>();
                failures.put(name, ClientInfoStatus.REASON_UNKNOWN);
                throw new SQLClientInfoException(GT.tr("Failed to set ClientInfo property: {0}", "ApplicationName"), sqle.getSQLState(), failures, sqle);
            }
            this._clientInfo.put(name, value);
            return;
        }
        this.addWarning(new SQLWarning(GT.tr("ClientInfo property not supported."), PSQLState.NOT_IMPLEMENTED.getState()));
    }
    
    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
        try {
            this.checkClosed();
        }
        catch (SQLException cause) {
            final Map<String, ClientInfoStatus> failures = new HashMap<String, ClientInfoStatus>();
            for (final Map.Entry<Object, Object> e : properties.entrySet()) {
                failures.put(e.getKey(), ClientInfoStatus.REASON_UNKNOWN);
            }
            throw new SQLClientInfoException(GT.tr("This connection has been closed."), failures, cause);
        }
        final Map<String, ClientInfoStatus> failures2 = new HashMap<String, ClientInfoStatus>();
        for (final String name : new String[] { "ApplicationName" }) {
            try {
                this.setClientInfo(name, properties.getProperty(name, null));
            }
            catch (SQLClientInfoException e2) {
                failures2.putAll(e2.getFailedProperties());
            }
        }
        if (!failures2.isEmpty()) {
            throw new SQLClientInfoException(GT.tr("One ore more ClientInfo failed."), PSQLState.NOT_IMPLEMENTED.getState(), failures2);
        }
    }
    
    @Override
    public String getClientInfo(final String name) throws SQLException {
        this.checkClosed();
        return this._clientInfo.getProperty(name);
    }
    
    @Override
    public Properties getClientInfo() throws SQLException {
        this.checkClosed();
        return this._clientInfo;
    }
    
    public <T> T createQueryObject(final Class<T> ifc) throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "createQueryObject(Class<T>)");
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        this.checkClosed();
        return iface.isAssignableFrom(this.getClass());
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        this.checkClosed();
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }
    
    @Override
    public String getSchema() throws SQLException {
        this.checkClosed();
        final Statement stmt = this.createStatement();
        String searchPath;
        try {
            final ResultSet rs = stmt.executeQuery("SHOW search_path");
            try {
                if (!rs.next()) {
                    return null;
                }
                searchPath = rs.getString(1);
            }
            finally {
                rs.close();
            }
        }
        finally {
            stmt.close();
        }
        if (searchPath.startsWith("\"")) {
            final Matcher matcher = AbstractJdbc4Connection.PATTERN_GET_SCHEMA.matcher(searchPath);
            matcher.find();
            return matcher.group(1).replaceAll("\"\"", "\"");
        }
        final int commaIndex = searchPath.indexOf(44);
        if (commaIndex == -1) {
            return searchPath;
        }
        return searchPath.substring(0, commaIndex);
    }
    
    @Override
    public void abort(final Executor executor) throws SQLException {
        if (this.isClosed()) {
            return;
        }
        AbstractJdbc4Connection.SQL_PERMISSION_ABORT.checkGuard(this);
        final AbortCommand command = new AbortCommand();
        if (executor != null) {
            executor.execute(command);
        }
        else {
            command.run();
        }
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setNetworkTimeout(Executor, int)");
    }
    
    @Override
    public int getNetworkTimeout() throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getNetworkTimeout()");
    }
    
    static {
        SQL_PERMISSION_ABORT = new SQLPermission("callAbort");
        PATTERN_GET_SCHEMA = Pattern.compile("^\\\"(.*)\\\"(?!\\\")");
    }
    
    public class AbortCommand implements Runnable
    {
        @Override
        public void run() {
            AbstractJdbc2Connection.this.abort();
        }
    }
}
