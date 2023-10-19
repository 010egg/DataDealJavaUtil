// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db;

import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.classic.spi.IThrowableProxy;
import java.util.Iterator;
import java.util.Set;
import java.sql.Statement;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.Map;
import java.sql.Connection;
import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
import java.sql.PreparedStatement;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.db.names.DBNameResolver;
import java.lang.reflect.Method;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;

public class DBAppender extends DBAppenderBase<ILoggingEvent>
{
    protected String insertPropertiesSQL;
    protected String insertExceptionSQL;
    protected String insertSQL;
    protected static final Method GET_GENERATED_KEYS_METHOD;
    private DBNameResolver dbNameResolver;
    static final int TIMESTMP_INDEX = 1;
    static final int FORMATTED_MESSAGE_INDEX = 2;
    static final int LOGGER_NAME_INDEX = 3;
    static final int LEVEL_STRING_INDEX = 4;
    static final int THREAD_NAME_INDEX = 5;
    static final int REFERENCE_FLAG_INDEX = 6;
    static final int ARG0_INDEX = 7;
    static final int ARG1_INDEX = 8;
    static final int ARG2_INDEX = 9;
    static final int ARG3_INDEX = 10;
    static final int CALLER_FILENAME_INDEX = 11;
    static final int CALLER_CLASS_INDEX = 12;
    static final int CALLER_METHOD_INDEX = 13;
    static final int CALLER_LINE_INDEX = 14;
    static final int EVENT_ID_INDEX = 15;
    static final StackTraceElement EMPTY_CALLER_DATA;
    
    static {
        EMPTY_CALLER_DATA = CallerData.naInstance();
        Method getGeneratedKeysMethod;
        try {
            getGeneratedKeysMethod = PreparedStatement.class.getMethod("getGeneratedKeys", (Class<?>[])null);
        }
        catch (Exception ex) {
            getGeneratedKeysMethod = null;
        }
        GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;
    }
    
    public void setDbNameResolver(final DBNameResolver dbNameResolver) {
        this.dbNameResolver = dbNameResolver;
    }
    
    @Override
    public void start() {
        if (this.dbNameResolver == null) {
            this.dbNameResolver = new DefaultDBNameResolver();
        }
        this.insertExceptionSQL = SQLBuilder.buildInsertExceptionSQL(this.dbNameResolver);
        this.insertPropertiesSQL = SQLBuilder.buildInsertPropertiesSQL(this.dbNameResolver);
        this.insertSQL = SQLBuilder.buildInsertSQL(this.dbNameResolver);
        super.start();
    }
    
    @Override
    protected void subAppend(final ILoggingEvent event, final Connection connection, final PreparedStatement insertStatement) throws Throwable {
        this.bindLoggingEventWithInsertStatement(insertStatement, event);
        this.bindLoggingEventArgumentsWithPreparedStatement(insertStatement, event.getArgumentArray());
        this.bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());
        final int updateCount = insertStatement.executeUpdate();
        if (updateCount != 1) {
            this.addWarn("Failed to insert loggingEvent");
        }
    }
    
    @Override
    protected void secondarySubAppend(final ILoggingEvent event, final Connection connection, final long eventId) throws Throwable {
        final Map<String, String> mergedMap = this.mergePropertyMaps(event);
        this.insertProperties(mergedMap, connection, eventId);
        if (event.getThrowableProxy() != null) {
            this.insertThrowable(event.getThrowableProxy(), connection, eventId);
        }
    }
    
    void bindLoggingEventWithInsertStatement(final PreparedStatement stmt, final ILoggingEvent event) throws SQLException {
        stmt.setLong(1, event.getTimeStamp());
        stmt.setString(2, event.getFormattedMessage());
        stmt.setString(3, event.getLoggerName());
        stmt.setString(4, event.getLevel().toString());
        stmt.setString(5, event.getThreadName());
        stmt.setShort(6, DBHelper.computeReferenceMask(event));
    }
    
    void bindLoggingEventArgumentsWithPreparedStatement(final PreparedStatement stmt, final Object[] argArray) throws SQLException {
        final int arrayLen = (argArray != null) ? argArray.length : 0;
        for (int i = 0; i < arrayLen && i < 4; ++i) {
            stmt.setString(7 + i, this.asStringTruncatedTo254(argArray[i]));
        }
        if (arrayLen < 4) {
            for (int i = arrayLen; i < 4; ++i) {
                stmt.setString(7 + i, null);
            }
        }
    }
    
    String asStringTruncatedTo254(final Object o) {
        String s = null;
        if (o != null) {
            s = o.toString();
        }
        if (s == null) {
            return null;
        }
        if (s.length() <= 254) {
            return s;
        }
        return s.substring(0, 254);
    }
    
    void bindCallerDataWithPreparedStatement(final PreparedStatement stmt, final StackTraceElement[] callerDataArray) throws SQLException {
        final StackTraceElement caller = this.extractFirstCaller(callerDataArray);
        stmt.setString(11, caller.getFileName());
        stmt.setString(12, caller.getClassName());
        stmt.setString(13, caller.getMethodName());
        stmt.setString(14, Integer.toString(caller.getLineNumber()));
    }
    
    private StackTraceElement extractFirstCaller(final StackTraceElement[] callerDataArray) {
        StackTraceElement caller = DBAppender.EMPTY_CALLER_DATA;
        if (this.hasAtLeastOneNonNullElement(callerDataArray)) {
            caller = callerDataArray[0];
        }
        return caller;
    }
    
    private boolean hasAtLeastOneNonNullElement(final StackTraceElement[] callerDataArray) {
        return callerDataArray != null && callerDataArray.length > 0 && callerDataArray[0] != null;
    }
    
    Map<String, String> mergePropertyMaps(final ILoggingEvent event) {
        final Map<String, String> mergedMap = new HashMap<String, String>();
        final Map<String, String> loggerContextMap = event.getLoggerContextVO().getPropertyMap();
        final Map<String, String> mdcMap = event.getMDCPropertyMap();
        if (loggerContextMap != null) {
            mergedMap.putAll(loggerContextMap);
        }
        if (mdcMap != null) {
            mergedMap.putAll(mdcMap);
        }
        return mergedMap;
    }
    
    @Override
    protected Method getGeneratedKeysMethod() {
        return DBAppender.GET_GENERATED_KEYS_METHOD;
    }
    
    @Override
    protected String getInsertSQL() {
        return this.insertSQL;
    }
    
    protected void insertProperties(final Map<String, String> mergedMap, final Connection connection, final long eventId) throws SQLException {
        final Set<String> propertiesKeys = mergedMap.keySet();
        if (propertiesKeys.size() > 0) {
            PreparedStatement insertPropertiesStatement = null;
            try {
                insertPropertiesStatement = connection.prepareStatement(this.insertPropertiesSQL);
                for (final String key : propertiesKeys) {
                    final String value = mergedMap.get(key);
                    insertPropertiesStatement.setLong(1, eventId);
                    insertPropertiesStatement.setString(2, key);
                    insertPropertiesStatement.setString(3, value);
                    if (this.cnxSupportsBatchUpdates) {
                        insertPropertiesStatement.addBatch();
                    }
                    else {
                        insertPropertiesStatement.execute();
                    }
                }
                if (this.cnxSupportsBatchUpdates) {
                    insertPropertiesStatement.executeBatch();
                }
            }
            finally {
                ch.qos.logback.core.db.DBHelper.closeStatement(insertPropertiesStatement);
            }
            ch.qos.logback.core.db.DBHelper.closeStatement(insertPropertiesStatement);
        }
    }
    
    void updateExceptionStatement(final PreparedStatement exceptionStatement, final String txt, final short i, final long eventId) throws SQLException {
        exceptionStatement.setLong(1, eventId);
        exceptionStatement.setShort(2, i);
        exceptionStatement.setString(3, txt);
        if (this.cnxSupportsBatchUpdates) {
            exceptionStatement.addBatch();
        }
        else {
            exceptionStatement.execute();
        }
    }
    
    short buildExceptionStatement(final IThrowableProxy tp, short baseIndex, final PreparedStatement insertExceptionStatement, final long eventId) throws SQLException {
        final StringBuilder buf = new StringBuilder();
        ThrowableProxyUtil.subjoinFirstLine(buf, tp);
        final String string = buf.toString();
        final short j = baseIndex;
        baseIndex = (short)(j + 1);
        this.updateExceptionStatement(insertExceptionStatement, string, j, eventId);
        final int commonFrames = tp.getCommonFrames();
        final StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        for (int i = 0; i < stepArray.length - commonFrames; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append('\t');
            ThrowableProxyUtil.subjoinSTEP(sb, stepArray[i]);
            final String string2 = sb.toString();
            final short k = baseIndex;
            baseIndex = (short)(k + 1);
            this.updateExceptionStatement(insertExceptionStatement, string2, k, eventId);
        }
        if (commonFrames > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append('\t').append("... ").append(commonFrames).append(" common frames omitted");
            final String string3 = sb2.toString();
            final short l = baseIndex;
            baseIndex = (short)(l + 1);
            this.updateExceptionStatement(insertExceptionStatement, string3, l, eventId);
        }
        return baseIndex;
    }
    
    protected void insertThrowable(IThrowableProxy tp, final Connection connection, final long eventId) throws SQLException {
        PreparedStatement exceptionStatement = null;
        try {
            exceptionStatement = connection.prepareStatement(this.insertExceptionSQL);
            short baseIndex = 0;
            while (tp != null) {
                baseIndex = this.buildExceptionStatement(tp, baseIndex, exceptionStatement, eventId);
                tp = tp.getCause();
            }
            if (this.cnxSupportsBatchUpdates) {
                exceptionStatement.executeBatch();
            }
        }
        finally {
            ch.qos.logback.core.db.DBHelper.closeStatement(exceptionStatement);
        }
        ch.qos.logback.core.db.DBHelper.closeStatement(exceptionStatement);
    }
}
