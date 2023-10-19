// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import java.util.TimerTask;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import org.postgresql.PGNotification;
import java.io.IOException;
import org.postgresql.core.Encoding;
import java.util.NoSuchElementException;
import java.util.Locale;
import org.postgresql.core.ResultHandler;
import org.postgresql.core.ParameterList;
import java.util.Enumeration;
import org.postgresql.util.PGInterval;
import org.postgresql.util.PGmoney;
import org.postgresql.geometric.PGpolygon;
import org.postgresql.geometric.PGpoint;
import org.postgresql.geometric.PGpath;
import org.postgresql.geometric.PGlseg;
import org.postgresql.geometric.PGline;
import org.postgresql.geometric.PGcircle;
import org.postgresql.geometric.PGbox;
import org.postgresql.util.PGBinaryObject;
import org.postgresql.util.PGobject;
import org.postgresql.core.BaseStatement;
import java.sql.ResultSet;
import org.postgresql.core.QueryExecutor;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import org.postgresql.core.Oid;
import java.util.StringTokenizer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.Collection;
import java.util.HashSet;
import org.postgresql.core.Utils;
import org.postgresql.core.ConnectionFactory;
import org.postgresql.PGProperty;
import org.postgresql.Driver;
import java.util.Properties;
import org.postgresql.util.HostSpec;
import java.sql.SQLException;
import org.postgresql.copy.CopyManager;
import java.sql.DatabaseMetaData;
import org.postgresql.largeobject.LargeObjectManager;
import org.postgresql.fastpath.Fastpath;
import java.util.Map;
import java.util.Timer;
import java.util.Set;
import java.sql.SQLWarning;
import org.postgresql.core.TypeInfo;
import org.postgresql.core.Query;
import org.postgresql.core.ProtocolConnection;
import org.postgresql.core.Logger;
import org.postgresql.core.BaseConnection;

public abstract class AbstractJdbc2Connection implements BaseConnection
{
    private static int nextConnectionID;
    private final Logger logger;
    private final String creatingURL;
    private Throwable openStackTrace;
    private final ProtocolConnection protoConnection;
    private final int compatibleInt;
    private final Query commitQuery;
    private final Query rollbackQuery;
    private TypeInfo _typeCache;
    private boolean disableColumnSanitiser;
    protected int prepareThreshold;
    protected boolean forcebinary;
    private boolean autoCommit;
    private boolean readOnly;
    private final boolean bindStringAsVarchar;
    private SQLWarning firstWarning;
    private Set<Integer> useBinarySendForOids;
    private Set<Integer> useBinaryReceiveForOids;
    private volatile Timer cancelTimer;
    private final TimestampUtils timestampUtils;
    protected Map typemap;
    private Fastpath fastpath;
    private LargeObjectManager largeobject;
    protected DatabaseMetaData metadata;
    private CopyManager copyManager;
    
    @Override
    public abstract DatabaseMetaData getMetaData() throws SQLException;
    
    protected AbstractJdbc2Connection(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final String url) throws SQLException {
        this.disableColumnSanitiser = false;
        this.forcebinary = false;
        this.autoCommit = true;
        this.readOnly = false;
        this.firstWarning = null;
        this.cancelTimer = null;
        this.fastpath = null;
        this.largeobject = null;
        this.copyManager = null;
        this.creatingURL = url;
        int logLevel = Driver.getLogLevel();
        final Integer connectionLogLevel = PGProperty.LOG_LEVEL.getInteger(info);
        if (connectionLogLevel != null) {
            logLevel = connectionLogLevel;
        }
        synchronized (AbstractJdbc2Connection.class) {
            (this.logger = new Logger(AbstractJdbc2Connection.nextConnectionID++)).setLogLevel(logLevel);
        }
        if (logLevel > 0) {
            this.enableDriverManagerLogging();
        }
        this.prepareThreshold = PGProperty.PREPARE_THRESHOLD.getInt(info);
        if (this.prepareThreshold == -1) {
            this.forcebinary = true;
        }
        final boolean binaryTransfer = PGProperty.BINARY_TRANSFER.getBoolean(info);
        if (this.logger.logInfo()) {
            this.logger.info(Driver.getVersion());
        }
        this.protoConnection = ConnectionFactory.openConnection(hostSpecs, user, database, info, this.logger);
        int compat = Utils.parseServerVersionStr(PGProperty.COMPATIBLE.get(info));
        if (compat == 0) {
            compat = 90400;
        }
        this.compatibleInt = compat;
        if (PGProperty.READ_ONLY.getBoolean(info)) {
            this.setReadOnly(true);
        }
        final Set<Integer> binaryOids = new HashSet<Integer>();
        if (binaryTransfer && this.protoConnection.getProtocolVersion() >= 3) {
            binaryOids.add(17);
            binaryOids.add(21);
            binaryOids.add(23);
            binaryOids.add(20);
            binaryOids.add(700);
            binaryOids.add(701);
            binaryOids.add(1083);
            binaryOids.add(1082);
            binaryOids.add(1266);
            binaryOids.add(1114);
            binaryOids.add(1184);
            binaryOids.add(1005);
            binaryOids.add(1007);
            binaryOids.add(1016);
            binaryOids.add(1021);
            binaryOids.add(1022);
            binaryOids.add(1022);
            binaryOids.add(1015);
            binaryOids.add(1009);
            binaryOids.add(600);
            binaryOids.add(603);
            binaryOids.add(2950);
        }
        if (!this.haveMinimumCompatibleVersion("8.0")) {
            binaryOids.remove(1083);
            binaryOids.remove(1266);
            binaryOids.remove(1114);
            binaryOids.remove(1184);
        }
        if (!this.haveMinimumCompatibleVersion("8.3")) {
            binaryOids.remove(1005);
            binaryOids.remove(1007);
            binaryOids.remove(1016);
            binaryOids.remove(1021);
            binaryOids.remove(1022);
            binaryOids.remove(1022);
            binaryOids.remove(1015);
            binaryOids.remove(1009);
        }
        binaryOids.addAll(this.getOidSet(PGProperty.BINARY_TRANSFER_ENABLE.get(info)));
        binaryOids.removeAll(this.getOidSet(PGProperty.BINARY_TRANSFER_DISABLE.get(info)));
        (this.useBinarySendForOids = new HashSet<Integer>()).addAll(binaryOids);
        (this.useBinaryReceiveForOids = new HashSet<Integer>()).addAll(binaryOids);
        this.useBinarySendForOids.remove(1082);
        this.protoConnection.setBinaryReceiveOids(this.useBinaryReceiveForOids);
        if (this.logger.logDebug()) {
            this.logger.debug("    compatible = " + this.compatibleInt);
            this.logger.debug("    loglevel = " + logLevel);
            this.logger.debug("    prepare threshold = " + this.prepareThreshold);
            this.logger.debug("    types using binary send = " + this.oidsToString(this.useBinarySendForOids));
            this.logger.debug("    types using binary receive = " + this.oidsToString(this.useBinaryReceiveForOids));
            this.logger.debug("    integer date/time = " + this.protoConnection.getIntegerDateTimes());
        }
        final String stringType = PGProperty.STRING_TYPE.get(info);
        if (stringType != null) {
            if (stringType.equalsIgnoreCase("unspecified")) {
                this.bindStringAsVarchar = false;
            }
            else {
                if (!stringType.equalsIgnoreCase("varchar")) {
                    throw new PSQLException(GT.tr("Unsupported value for stringtype parameter: {0}", stringType), PSQLState.INVALID_PARAMETER_VALUE);
                }
                this.bindStringAsVarchar = true;
            }
        }
        else {
            this.bindStringAsVarchar = this.haveMinimumCompatibleVersion("8.0");
        }
        this.timestampUtils = new TimestampUtils(this.haveMinimumServerVersion("7.4"), this.haveMinimumServerVersion("8.2"), !this.protoConnection.getIntegerDateTimes());
        this.commitQuery = this.getQueryExecutor().createSimpleQuery("COMMIT");
        this.rollbackQuery = this.getQueryExecutor().createSimpleQuery("ROLLBACK");
        final int unknownLength = PGProperty.UNKNOWN_LENGTH.getInt(info);
        this._typeCache = this.createTypeInfo(this, unknownLength);
        this.initObjectTypes(info);
        if (PGProperty.LOG_UNCLOSED_CONNECTIONS.getBoolean(info)) {
            this.openStackTrace = new Throwable("Connection was created at this point:");
            this.enableDriverManagerLogging();
        }
        this.disableColumnSanitiser = PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(info);
    }
    
    private Set<Integer> getOidSet(final String oidList) throws PSQLException {
        final Set oids = new HashSet();
        final StringTokenizer tokenizer = new StringTokenizer(oidList, ",");
        while (tokenizer.hasMoreTokens()) {
            final String oid = tokenizer.nextToken();
            oids.add(Oid.valueOf(oid));
        }
        return (Set<Integer>)oids;
    }
    
    private String oidsToString(final Set<Integer> oids) {
        final StringBuilder sb = new StringBuilder();
        for (final Integer oid : oids) {
            sb.append(Oid.toString(oid));
            sb.append(',');
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        else {
            sb.append(" <none>");
        }
        return sb.toString();
    }
    
    @Override
    public TimestampUtils getTimestampUtils() {
        return this.timestampUtils;
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        return this.createStatement(1003, 1007);
    }
    
    @Override
    public abstract Statement createStatement(final int p0, final int p1) throws SQLException;
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return this.prepareStatement(sql, 1003, 1007);
    }
    
    @Override
    public abstract PreparedStatement prepareStatement(final String p0, final int p1, final int p2) throws SQLException;
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return this.prepareCall(sql, 1003, 1007);
    }
    
    @Override
    public abstract CallableStatement prepareCall(final String p0, final int p1, final int p2) throws SQLException;
    
    @Override
    public Map getTypeMap() throws SQLException {
        this.checkClosed();
        return this.typemap;
    }
    
    @Override
    public QueryExecutor getQueryExecutor() {
        return this.protoConnection.getQueryExecutor();
    }
    
    public void addWarning(final SQLWarning warn) {
        if (this.firstWarning != null) {
            this.firstWarning.setNextWarning(warn);
        }
        else {
            this.firstWarning = warn;
        }
    }
    
    @Override
    public ResultSet execSQLQuery(final String s) throws SQLException {
        return this.execSQLQuery(s, 1003, 1007);
    }
    
    @Override
    public ResultSet execSQLQuery(final String s, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        BaseStatement stat;
        boolean hasResultSet;
        for (stat = (BaseStatement)this.createStatement(resultSetType, resultSetConcurrency), hasResultSet = stat.executeWithFlags(s, 16); !hasResultSet && stat.getUpdateCount() != -1; hasResultSet = stat.getMoreResults()) {}
        if (!hasResultSet) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        final SQLWarning warnings = stat.getWarnings();
        if (warnings != null) {
            this.addWarning(warnings);
        }
        return stat.getResultSet();
    }
    
    @Override
    public void execSQLUpdate(final String s) throws SQLException {
        final BaseStatement stmt = (BaseStatement)this.createStatement();
        if (stmt.executeWithFlags(s, 22)) {
            throw new PSQLException(GT.tr("A result was returned when none was expected."), PSQLState.TOO_MANY_RESULTS);
        }
        final SQLWarning warnings = stmt.getWarnings();
        if (warnings != null) {
            this.addWarning(warnings);
        }
        stmt.close();
    }
    
    public void setCursorName(final String cursor) throws SQLException {
        this.checkClosed();
    }
    
    public String getCursorName() throws SQLException {
        this.checkClosed();
        return null;
    }
    
    public String getURL() throws SQLException {
        return this.creatingURL;
    }
    
    public String getUserName() throws SQLException {
        return this.protoConnection.getUser();
    }
    
    @Override
    public Fastpath getFastpathAPI() throws SQLException {
        this.checkClosed();
        if (this.fastpath == null) {
            this.fastpath = new Fastpath(this);
        }
        return this.fastpath;
    }
    
    @Override
    public LargeObjectManager getLargeObjectAPI() throws SQLException {
        this.checkClosed();
        if (this.largeobject == null) {
            this.largeobject = new LargeObjectManager(this);
        }
        return this.largeobject;
    }
    
    @Override
    public Object getObject(final String type, final String value, final byte[] byteValue) throws SQLException {
        if (this.typemap != null) {
            final Class c = this.typemap.get(type);
            if (c != null) {
                throw new PSQLException(GT.tr("Custom type maps are not supported."), PSQLState.NOT_IMPLEMENTED);
            }
        }
        PGobject obj = null;
        if (this.logger.logDebug()) {
            this.logger.debug("Constructing object from type=" + type + " value=<" + value + ">");
        }
        try {
            final Class klass = this._typeCache.getPGobject(type);
            if (klass != null) {
                obj = klass.newInstance();
                obj.setType(type);
                if (byteValue != null && obj instanceof PGBinaryObject) {
                    final PGBinaryObject binObj = (PGBinaryObject)obj;
                    binObj.setByteValue(byteValue, 0);
                }
                else {
                    obj.setValue(value);
                }
            }
            else {
                obj = new PGobject();
                obj.setType(type);
                obj.setValue(value);
            }
            return obj;
        }
        catch (SQLException sx) {
            throw sx;
        }
        catch (Exception ex) {
            throw new PSQLException(GT.tr("Failed to create object for: {0}.", type), PSQLState.CONNECTION_FAILURE, ex);
        }
    }
    
    protected TypeInfo createTypeInfo(final BaseConnection conn, final int unknownLength) {
        return new TypeInfoCache(conn, unknownLength);
    }
    
    @Override
    public TypeInfo getTypeInfo() {
        return this._typeCache;
    }
    
    @Override
    public void addDataType(final String type, final String name) {
        try {
            this.addDataType(type, Class.forName(name));
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot register new type: " + e);
        }
    }
    
    @Override
    public void addDataType(final String type, final Class klass) throws SQLException {
        this.checkClosed();
        this._typeCache.addDataType(type, klass);
    }
    
    private void initObjectTypes(final Properties info) throws SQLException {
        this.addDataType("box", PGbox.class);
        this.addDataType("circle", PGcircle.class);
        this.addDataType("line", PGline.class);
        this.addDataType("lseg", PGlseg.class);
        this.addDataType("path", PGpath.class);
        this.addDataType("point", PGpoint.class);
        this.addDataType("polygon", PGpolygon.class);
        this.addDataType("money", PGmoney.class);
        this.addDataType("interval", PGInterval.class);
        final Enumeration e = info.propertyNames();
        while (e.hasMoreElements()) {
            final String propertyName = e.nextElement();
            if (propertyName.startsWith("datatype.")) {
                final String typeName = propertyName.substring(9);
                final String className = info.getProperty(propertyName);
                Class klass;
                try {
                    klass = Class.forName(className);
                }
                catch (ClassNotFoundException cnfe) {
                    throw new PSQLException(GT.tr("Unable to load the class {0} responsible for the datatype {1}", new Object[] { className, typeName }), PSQLState.SYSTEM_ERROR, cnfe);
                }
                this.addDataType(typeName, klass);
            }
        }
    }
    
    @Override
    public void close() {
        this.releaseTimer();
        this.protoConnection.close();
        this.openStackTrace = null;
    }
    
    @Override
    public String nativeSQL(final String sql) throws SQLException {
        this.checkClosed();
        final StringBuilder buf = new StringBuilder(sql.length());
        AbstractJdbc2Statement.parseSql(sql, 0, buf, false, this.getStandardConformingStrings());
        return buf.toString();
    }
    
    @Override
    public synchronized SQLWarning getWarnings() throws SQLException {
        this.checkClosed();
        final SQLWarning newWarnings = this.protoConnection.getWarnings();
        if (this.firstWarning == null) {
            this.firstWarning = newWarnings;
        }
        else {
            this.firstWarning.setNextWarning(newWarnings);
        }
        return this.firstWarning;
    }
    
    @Override
    public synchronized void clearWarnings() throws SQLException {
        this.checkClosed();
        this.protoConnection.getWarnings();
        this.firstWarning = null;
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.checkClosed();
        if (this.protoConnection.getTransactionState() != 0) {
            throw new PSQLException(GT.tr("Cannot change transaction read-only property in the middle of a transaction."), PSQLState.ACTIVE_SQL_TRANSACTION);
        }
        if (this.haveMinimumServerVersion("7.4") && readOnly != this.readOnly) {
            final String readOnlySql = "SET SESSION CHARACTERISTICS AS TRANSACTION " + (readOnly ? "READ ONLY" : "READ WRITE");
            this.execSQLUpdate(readOnlySql);
        }
        this.readOnly = readOnly;
    }
    
    @Override
    public boolean isReadOnly() throws SQLException {
        this.checkClosed();
        return this.readOnly;
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.checkClosed();
        if (this.autoCommit == autoCommit) {
            return;
        }
        if (!this.autoCommit) {
            this.commit();
        }
        this.autoCommit = autoCommit;
    }
    
    @Override
    public boolean getAutoCommit() throws SQLException {
        this.checkClosed();
        return this.autoCommit;
    }
    
    private void executeTransactionCommand(final Query query) throws SQLException {
        int flags = 22;
        if (this.prepareThreshold == 0) {
            flags |= 0x1;
        }
        this.getQueryExecutor().execute(query, null, new TransactionCommandHandler(), 0, 0, flags);
    }
    
    @Override
    public void commit() throws SQLException {
        this.checkClosed();
        if (this.autoCommit) {
            throw new PSQLException(GT.tr("Cannot commit when autoCommit is enabled."), PSQLState.NO_ACTIVE_SQL_TRANSACTION);
        }
        if (this.protoConnection.getTransactionState() != 0) {
            this.executeTransactionCommand(this.commitQuery);
        }
    }
    
    protected void checkClosed() throws SQLException {
        if (this.isClosed()) {
            throw new PSQLException(GT.tr("This connection has been closed."), PSQLState.CONNECTION_DOES_NOT_EXIST);
        }
    }
    
    @Override
    public void rollback() throws SQLException {
        this.checkClosed();
        if (this.autoCommit) {
            throw new PSQLException(GT.tr("Cannot rollback when autoCommit is enabled."), PSQLState.NO_ACTIVE_SQL_TRANSACTION);
        }
        if (this.protoConnection.getTransactionState() != 0) {
            this.executeTransactionCommand(this.rollbackQuery);
        }
    }
    
    @Override
    public int getTransactionState() {
        return this.protoConnection.getTransactionState();
    }
    
    @Override
    public int getTransactionIsolation() throws SQLException {
        this.checkClosed();
        String level = null;
        if (this.haveMinimumServerVersion("7.3")) {
            final ResultSet rs = this.execSQLQuery("SHOW TRANSACTION ISOLATION LEVEL");
            if (rs.next()) {
                level = rs.getString(1);
            }
            rs.close();
        }
        else {
            final SQLWarning saveWarnings = this.getWarnings();
            this.clearWarnings();
            this.execSQLUpdate("SHOW TRANSACTION ISOLATION LEVEL");
            final SQLWarning warning = this.getWarnings();
            if (warning != null) {
                level = warning.getMessage();
            }
            this.clearWarnings();
            if (saveWarnings != null) {
                this.addWarning(saveWarnings);
            }
        }
        if (level == null) {
            return 2;
        }
        level = level.toUpperCase(Locale.US);
        if (level.indexOf("READ COMMITTED") != -1) {
            return 2;
        }
        if (level.indexOf("READ UNCOMMITTED") != -1) {
            return 1;
        }
        if (level.indexOf("REPEATABLE READ") != -1) {
            return 4;
        }
        if (level.indexOf("SERIALIZABLE") != -1) {
            return 8;
        }
        return 2;
    }
    
    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.checkClosed();
        if (this.protoConnection.getTransactionState() != 0) {
            throw new PSQLException(GT.tr("Cannot change transaction isolation level in the middle of a transaction."), PSQLState.ACTIVE_SQL_TRANSACTION);
        }
        final String isolationLevelName = this.getIsolationLevelName(level);
        if (isolationLevelName == null) {
            throw new PSQLException(GT.tr("Transaction isolation level {0} not supported.", new Integer(level)), PSQLState.NOT_IMPLEMENTED);
        }
        final String isolationLevelSQL = "SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL " + isolationLevelName;
        this.execSQLUpdate(isolationLevelSQL);
    }
    
    protected String getIsolationLevelName(final int level) {
        final boolean pg80 = this.haveMinimumServerVersion("8.0");
        if (level == 2) {
            return "READ COMMITTED";
        }
        if (level == 8) {
            return "SERIALIZABLE";
        }
        if (pg80 && level == 1) {
            return "READ UNCOMMITTED";
        }
        if (pg80 && level == 4) {
            return "REPEATABLE READ";
        }
        return null;
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.checkClosed();
    }
    
    @Override
    public String getCatalog() throws SQLException {
        this.checkClosed();
        return this.protoConnection.getDatabase();
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.openStackTrace != null) {
                this.logger.log(GT.tr("Finalizing a Connection that was never closed:"), this.openStackTrace);
            }
            this.close();
        }
        finally {
            super.finalize();
        }
    }
    
    public String getDBVersionNumber() {
        return this.protoConnection.getServerVersion();
    }
    
    private static int integerPart(final String dirtyString) {
        int start;
        for (start = 0; start < dirtyString.length() && !Character.isDigit(dirtyString.charAt(start)); ++start) {}
        int end;
        for (end = start; end < dirtyString.length() && Character.isDigit(dirtyString.charAt(end)); ++end) {}
        if (start == end) {
            return 0;
        }
        return Integer.parseInt(dirtyString.substring(start, end));
    }
    
    public int getServerMajorVersion() {
        try {
            final StringTokenizer versionTokens = new StringTokenizer(this.protoConnection.getServerVersion(), ".");
            return integerPart(versionTokens.nextToken());
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }
    
    public int getServerMinorVersion() {
        try {
            final StringTokenizer versionTokens = new StringTokenizer(this.protoConnection.getServerVersion(), ".");
            versionTokens.nextToken();
            return integerPart(versionTokens.nextToken());
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }
    
    @Override
    public boolean haveMinimumServerVersion(final String ver) {
        final int requiredver = Utils.parseServerVersionStr(ver);
        if (requiredver == 0) {
            return this.protoConnection.getServerVersion().compareTo(ver) >= 0;
        }
        return this.haveMinimumServerVersion(requiredver);
    }
    
    @Override
    public boolean haveMinimumServerVersion(final int ver) {
        return this.protoConnection.getServerVersionNum() >= ver;
    }
    
    @Override
    public boolean haveMinimumCompatibleVersion(final int ver) {
        return this.compatibleInt >= ver;
    }
    
    @Override
    public boolean haveMinimumCompatibleVersion(final String ver) {
        return this.haveMinimumCompatibleVersion(Utils.parseServerVersionStr(ver));
    }
    
    @Override
    public Encoding getEncoding() {
        return this.protoConnection.getEncoding();
    }
    
    @Override
    public byte[] encodeString(final String str) throws SQLException {
        try {
            return this.getEncoding().encode(str);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Unable to translate data into the desired encoding."), PSQLState.DATA_ERROR, ioe);
        }
    }
    
    @Override
    public String escapeString(final String str) throws SQLException {
        return Utils.escapeLiteral(null, str, this.protoConnection.getStandardConformingStrings()).toString();
    }
    
    @Override
    public boolean getStandardConformingStrings() {
        return this.protoConnection.getStandardConformingStrings();
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.protoConnection.isClosed();
    }
    
    @Override
    public void cancelQuery() throws SQLException {
        this.checkClosed();
        this.protoConnection.sendQueryCancel();
    }
    
    @Override
    public PGNotification[] getNotifications() throws SQLException {
        this.checkClosed();
        this.getQueryExecutor().processNotifies();
        final PGNotification[] notifications = this.protoConnection.getNotifications();
        return (PGNotification[])((notifications.length == 0) ? null : notifications);
    }
    
    @Override
    public int getPrepareThreshold() {
        return this.prepareThreshold;
    }
    
    @Override
    public void setPrepareThreshold(final int newThreshold) {
        this.prepareThreshold = newThreshold;
    }
    
    public boolean getForceBinary() {
        return this.forcebinary;
    }
    
    public void setForceBinary(final boolean newValue) {
        this.forcebinary = newValue;
    }
    
    public void setTypeMapImpl(final Map map) throws SQLException {
        this.typemap = map;
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    protected void enableDriverManagerLogging() {
        if (DriverManager.getLogWriter() == null) {
            DriverManager.setLogWriter(new PrintWriter(System.out, true));
        }
    }
    
    public int getProtocolVersion() {
        return this.protoConnection.getProtocolVersion();
    }
    
    @Override
    public boolean getStringVarcharFlag() {
        return this.bindStringAsVarchar;
    }
    
    @Override
    public CopyManager getCopyAPI() throws SQLException {
        this.checkClosed();
        if (this.copyManager == null) {
            this.copyManager = new CopyManager(this);
        }
        return this.copyManager;
    }
    
    @Override
    public boolean binaryTransferSend(final int oid) {
        return this.useBinarySendForOids.contains(oid);
    }
    
    @Override
    public int getBackendPID() {
        return this.protoConnection.getBackendPID();
    }
    
    @Override
    public boolean isColumnSanitiserDisabled() {
        return this.disableColumnSanitiser;
    }
    
    public void setDisableColumnSanitiser(final boolean disableColumnSanitiser) {
        this.disableColumnSanitiser = disableColumnSanitiser;
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        this.checkClosed();
        final Statement stmt = this.createStatement();
        try {
            if (schema != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("SET SESSION search_path TO '");
                Utils.escapeLiteral(sb, schema, this.protoConnection.getStandardConformingStrings());
                sb.append("'");
                stmt.executeUpdate(sb.toString());
            }
            else {
                stmt.executeUpdate("SET SESSION search_path TO DEFAULT");
            }
        }
        finally {
            stmt.close();
        }
    }
    
    protected void abort() {
        this.protoConnection.abort();
    }
    
    private synchronized Timer getTimer() {
        if (this.cancelTimer == null) {
            this.cancelTimer = Driver.getSharedTimer().getTimer();
        }
        return this.cancelTimer;
    }
    
    private synchronized void releaseTimer() {
        if (this.cancelTimer != null) {
            this.cancelTimer = null;
            Driver.getSharedTimer().releaseTimer();
        }
    }
    
    @Override
    public void addTimerTask(final TimerTask timerTask, final long milliSeconds) {
        final Timer timer = this.getTimer();
        timer.schedule(timerTask, milliSeconds);
    }
    
    @Override
    public void purgeTimerTasks() {
        final Timer timer = this.cancelTimer;
        if (timer != null) {
            timer.purge();
        }
    }
    
    static {
        AbstractJdbc2Connection.nextConnectionID = 1;
    }
    
    private class TransactionCommandHandler implements ResultHandler
    {
        private SQLException error;
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            AbstractJdbc2Connection.this.addWarning(warning);
        }
        
        @Override
        public void handleError(final SQLException newError) {
            if (this.error == null) {
                this.error = newError;
            }
            else {
                this.error.setNextException(newError);
            }
        }
        
        @Override
        public void handleCompletion() throws SQLException {
            if (this.error != null) {
                throw this.error;
            }
        }
    }
}
