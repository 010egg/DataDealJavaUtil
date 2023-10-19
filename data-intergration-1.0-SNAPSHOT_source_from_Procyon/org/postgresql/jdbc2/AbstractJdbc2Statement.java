// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.BatchUpdateException;
import java.util.TimeZone;
import org.postgresql.Driver;
import java.sql.Ref;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import org.postgresql.util.HStoreConverter;
import java.util.Map;
import org.postgresql.util.PGBinaryObject;
import org.postgresql.util.PGobject;
import org.postgresql.core.types.PGUnknown;
import org.postgresql.core.types.PGBoolean;
import org.postgresql.core.types.PGNumber;
import org.postgresql.core.types.PGBigDecimal;
import org.postgresql.core.types.PGFloat;
import org.postgresql.core.types.PGDouble;
import org.postgresql.core.types.PGLong;
import org.postgresql.core.types.PGInteger;
import org.postgresql.core.types.PGShort;
import org.postgresql.core.types.PGByte;
import org.postgresql.core.types.PGType;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import java.math.BigDecimal;
import org.postgresql.util.ByteConverter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.postgresql.core.ResultHandler;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import org.postgresql.core.ResultCursor;
import java.sql.ResultSet;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.ParameterList;
import org.postgresql.core.Query;
import java.sql.SQLWarning;
import org.postgresql.core.BaseConnection;
import java.util.TimerTask;
import java.util.ArrayList;
import org.postgresql.core.BaseStatement;

public abstract class AbstractJdbc2Statement implements BaseStatement
{
    private boolean forceBinaryTransfers;
    protected ArrayList batchStatements;
    protected ArrayList batchParameters;
    protected final int resultsettype;
    protected final int concurrency;
    protected int fetchdirection;
    private volatile TimerTask cancelTimerTask;
    protected boolean wantsGeneratedKeysOnce;
    public boolean wantsGeneratedKeysAlways;
    protected BaseConnection connection;
    protected SQLWarning warnings;
    protected SQLWarning lastWarning;
    protected int maxrows;
    protected int fetchSize;
    protected int timeout;
    protected boolean replaceProcessingEnabled;
    protected ResultWrapper result;
    protected ResultWrapper firstUnclosedResult;
    protected ResultWrapper generatedKeys;
    protected boolean adjustIndex;
    protected boolean outParmBeforeFunc;
    private static final short IN_SQLCODE = 0;
    private static final short IN_STRING = 1;
    private static final short IN_IDENTIFIER = 6;
    private static final short BACKSLASH = 2;
    private static final short ESC_TIMEDATE = 3;
    private static final short ESC_FUNCTION = 4;
    private static final short ESC_OUTERJOIN = 5;
    private static final short ESC_ESCAPECHAR = 7;
    protected final Query preparedQuery;
    protected final ParameterList preparedParameters;
    protected Query lastSimpleQuery;
    protected int m_prepareThreshold;
    protected int m_useCount;
    private boolean isFunction;
    private int[] functionReturnType;
    private int[] testReturn;
    private boolean returnTypeSet;
    protected Object[] callResult;
    protected int maxfieldSize;
    protected boolean isClosed;
    private int lastIndex;
    
    @Override
    public ResultSet createDriverResultSet(final Field[] fields, final List tuples) throws SQLException {
        return this.createResultSet(null, fields, tuples, null);
    }
    
    public AbstractJdbc2Statement(final AbstractJdbc2Connection c, final int rsType, final int rsConcurrency) throws SQLException {
        this.forceBinaryTransfers = Boolean.getBoolean("org.postgresql.forceBinary");
        this.batchStatements = null;
        this.batchParameters = null;
        this.fetchdirection = 1000;
        this.cancelTimerTask = null;
        this.wantsGeneratedKeysOnce = false;
        this.wantsGeneratedKeysAlways = false;
        this.warnings = null;
        this.lastWarning = null;
        this.maxrows = 0;
        this.fetchSize = 0;
        this.timeout = 0;
        this.replaceProcessingEnabled = true;
        this.result = null;
        this.firstUnclosedResult = null;
        this.generatedKeys = null;
        this.adjustIndex = false;
        this.outParmBeforeFunc = false;
        this.m_useCount = 0;
        this.maxfieldSize = 0;
        this.isClosed = false;
        this.lastIndex = 0;
        this.connection = c;
        this.preparedQuery = null;
        this.preparedParameters = null;
        this.lastSimpleQuery = null;
        this.forceBinaryTransfers |= c.getForceBinary();
        this.resultsettype = rsType;
        this.concurrency = rsConcurrency;
    }
    
    public AbstractJdbc2Statement(final AbstractJdbc2Connection connection, final String sql, final boolean isCallable, final int rsType, final int rsConcurrency) throws SQLException {
        this.forceBinaryTransfers = Boolean.getBoolean("org.postgresql.forceBinary");
        this.batchStatements = null;
        this.batchParameters = null;
        this.fetchdirection = 1000;
        this.cancelTimerTask = null;
        this.wantsGeneratedKeysOnce = false;
        this.wantsGeneratedKeysAlways = false;
        this.warnings = null;
        this.lastWarning = null;
        this.maxrows = 0;
        this.fetchSize = 0;
        this.timeout = 0;
        this.replaceProcessingEnabled = true;
        this.result = null;
        this.firstUnclosedResult = null;
        this.generatedKeys = null;
        this.adjustIndex = false;
        this.outParmBeforeFunc = false;
        this.m_useCount = 0;
        this.maxfieldSize = 0;
        this.isClosed = false;
        this.lastIndex = 0;
        this.connection = connection;
        this.lastSimpleQuery = null;
        String parsed_sql = this.replaceProcessing(sql);
        if (isCallable) {
            parsed_sql = this.modifyJdbcCall(parsed_sql);
        }
        this.preparedQuery = connection.getQueryExecutor().createParameterizedQuery(parsed_sql);
        this.preparedParameters = this.preparedQuery.createParameterList();
        final int inParamCount = this.preparedParameters.getInParameterCount() + 1;
        this.testReturn = new int[inParamCount];
        this.functionReturnType = new int[inParamCount];
        this.forceBinaryTransfers |= connection.getForceBinary();
        this.resultsettype = rsType;
        this.concurrency = rsConcurrency;
    }
    
    @Override
    public abstract ResultSet createResultSet(final Query p0, final Field[] p1, final List p2, final ResultCursor p3) throws SQLException;
    
    public BaseConnection getPGConnection() {
        return this.connection;
    }
    
    public String getFetchingCursorName() {
        return null;
    }
    
    @Override
    public int getFetchSize() {
        return this.fetchSize;
    }
    
    protected boolean wantsScrollableResultSet() {
        return this.resultsettype != 1003;
    }
    
    protected boolean wantsHoldableResultSet() {
        return false;
    }
    
    @Override
    public ResultSet executeQuery(final String p_sql) throws SQLException {
        if (this.preparedQuery != null) {
            throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
        }
        if (this.forceBinaryTransfers) {
            this.clearWarnings();
            while (this.firstUnclosedResult != null) {
                if (this.firstUnclosedResult.getResultSet() != null) {
                    this.firstUnclosedResult.getResultSet().close();
                }
                this.firstUnclosedResult = this.firstUnclosedResult.getNext();
            }
            final PreparedStatement ps = this.connection.prepareStatement(p_sql, this.resultsettype, this.concurrency, this.getResultSetHoldability());
            ps.setMaxFieldSize(this.getMaxFieldSize());
            ps.setFetchSize(this.getFetchSize());
            ps.setFetchDirection(this.getFetchDirection());
            final AbstractJdbc2ResultSet rs = (AbstractJdbc2ResultSet)ps.executeQuery();
            rs.registerRealStatement(this);
            final ResultWrapper resultWrapper = new ResultWrapper(rs);
            this.firstUnclosedResult = resultWrapper;
            this.result = resultWrapper;
            return rs;
        }
        if (!this.executeWithFlags(p_sql, 0)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        if (this.result.getNext() != null) {
            throw new PSQLException(GT.tr("Multiple ResultSets were returned by the query."), PSQLState.TOO_MANY_RESULTS);
        }
        return this.result.getResultSet();
    }
    
    public ResultSet executeQuery() throws SQLException {
        if (!this.executeWithFlags(0)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
        }
        if (this.result.getNext() != null) {
            throw new PSQLException(GT.tr("Multiple ResultSets were returned by the query."), PSQLState.TOO_MANY_RESULTS);
        }
        return this.result.getResultSet();
    }
    
    @Override
    public int executeUpdate(final String p_sql) throws SQLException {
        if (this.preparedQuery != null) {
            throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
        }
        if (this.isFunction) {
            this.executeWithFlags(p_sql, 0);
            return 0;
        }
        this.executeWithFlags(p_sql, 4);
        for (ResultWrapper iter = this.result; iter != null; iter = iter.getNext()) {
            if (iter.getResultSet() != null) {
                throw new PSQLException(GT.tr("A result was returned when none was expected."), PSQLState.TOO_MANY_RESULTS);
            }
        }
        return this.getUpdateCount();
    }
    
    public int executeUpdate() throws SQLException {
        if (this.isFunction) {
            this.executeWithFlags(0);
            return 0;
        }
        this.executeWithFlags(4);
        for (ResultWrapper iter = this.result; iter != null; iter = iter.getNext()) {
            if (iter.getResultSet() != null) {
                throw new PSQLException(GT.tr("A result was returned when none was expected."), PSQLState.TOO_MANY_RESULTS);
            }
        }
        return this.getUpdateCount();
    }
    
    @Override
    public boolean execute(final String p_sql) throws SQLException {
        if (this.preparedQuery != null) {
            throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
        }
        return this.executeWithFlags(p_sql, 0);
    }
    
    @Override
    public boolean executeWithFlags(String p_sql, final int flags) throws SQLException {
        this.checkClosed();
        p_sql = this.replaceProcessing(p_sql);
        final Query simpleQuery = this.connection.getQueryExecutor().createSimpleQuery(p_sql);
        this.execute(simpleQuery, null, 0x1 | flags);
        this.lastSimpleQuery = simpleQuery;
        return this.result != null && this.result.getResultSet() != null;
    }
    
    public boolean execute() throws SQLException {
        return this.executeWithFlags(0);
    }
    
    @Override
    public boolean executeWithFlags(final int flags) throws SQLException {
        this.checkClosed();
        this.execute(this.preparedQuery, this.preparedParameters, flags);
        if (!this.isFunction || !this.returnTypeSet) {
            return this.result != null && this.result.getResultSet() != null;
        }
        if (this.result == null || this.result.getResultSet() == null) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned."), PSQLState.NO_DATA);
        }
        final ResultSet rs = this.result.getResultSet();
        if (!rs.next()) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned."), PSQLState.NO_DATA);
        }
        final int cols = rs.getMetaData().getColumnCount();
        final int outParameterCount = this.preparedParameters.getOutParameterCount();
        if (cols != outParameterCount) {
            throw new PSQLException(GT.tr("A CallableStatement was executed with an invalid number of parameters"), PSQLState.SYNTAX_ERROR);
        }
        this.lastIndex = 0;
        this.callResult = new Object[this.preparedParameters.getParameterCount() + 1];
        for (int i = 0, j = 0; i < cols; ++i, ++j) {
            while (j < this.functionReturnType.length && this.functionReturnType[j] == 0) {
                ++j;
            }
            this.callResult[j] = rs.getObject(i + 1);
            final int columnType = rs.getMetaData().getColumnType(i + 1);
            if (columnType != this.functionReturnType[j]) {
                if (columnType != 8 || this.functionReturnType[j] != 7) {
                    throw new PSQLException(GT.tr("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", new Object[] { new Integer(i + 1), "java.sql.Types=" + columnType, "java.sql.Types=" + this.functionReturnType[j] }), PSQLState.DATA_TYPE_MISMATCH);
                }
                if (this.callResult[j] != null) {
                    this.callResult[j] = new Float(((Double)this.callResult[j]).floatValue());
                }
            }
        }
        rs.close();
        this.result = null;
        return false;
    }
    
    protected void closeForNextExecution() throws SQLException {
        this.clearWarnings();
        while (this.firstUnclosedResult != null) {
            final ResultSet rs = this.firstUnclosedResult.getResultSet();
            if (rs != null) {
                rs.close();
            }
            this.firstUnclosedResult = this.firstUnclosedResult.getNext();
        }
        this.result = null;
        if (this.lastSimpleQuery != null) {
            this.lastSimpleQuery.close();
            this.lastSimpleQuery = null;
        }
        if (this.generatedKeys != null) {
            if (this.generatedKeys.getResultSet() != null) {
                this.generatedKeys.getResultSet().close();
            }
            this.generatedKeys = null;
        }
    }
    
    protected void execute(final Query queryToExecute, final ParameterList queryParameters, int flags) throws SQLException {
        this.closeForNextExecution();
        if (this.fetchSize > 0 && !this.wantsScrollableResultSet() && !this.connection.getAutoCommit() && !this.wantsHoldableResultSet()) {
            flags |= 0x8;
        }
        if (this.wantsGeneratedKeysOnce || this.wantsGeneratedKeysAlways) {
            flags |= 0x40;
            if ((flags & 0x4) != 0x0) {
                flags &= 0xFFFFFFFB;
            }
        }
        if (this.preparedQuery != null) {
            ++this.m_useCount;
            if ((this.m_prepareThreshold == 0 || this.m_useCount < this.m_prepareThreshold) && !this.forceBinaryTransfers) {
                flags |= 0x1;
            }
        }
        if (this.connection.getAutoCommit()) {
            flags |= 0x10;
        }
        if (this.concurrency != 1007) {
            flags |= 0x100;
        }
        if (queryToExecute.isEmpty()) {
            flags |= 0x10;
        }
        if (!queryToExecute.isStatementDescribed() && this.forceBinaryTransfers) {
            final int flags2 = flags | 0x20;
            final StatementResultHandler handler2 = new StatementResultHandler();
            this.connection.getQueryExecutor().execute(queryToExecute, queryParameters, handler2, 0, 0, flags2);
            final ResultWrapper result2 = handler2.getResults();
            if (result2 != null) {
                result2.getResultSet().close();
            }
        }
        final StatementResultHandler handler3 = new StatementResultHandler();
        this.result = null;
        try {
            this.startTimer();
            this.connection.getQueryExecutor().execute(queryToExecute, queryParameters, handler3, this.maxrows, this.fetchSize, flags);
        }
        finally {
            this.killTimerTask();
        }
        final ResultWrapper results = handler3.getResults();
        this.firstUnclosedResult = results;
        this.result = results;
        if (this.wantsGeneratedKeysOnce || this.wantsGeneratedKeysAlways) {
            this.generatedKeys = this.result;
            this.result = this.result.getNext();
            if (this.wantsGeneratedKeysOnce) {
                this.wantsGeneratedKeysOnce = false;
            }
        }
    }
    
    @Override
    public void setCursorName(final String name) throws SQLException {
        this.checkClosed();
    }
    
    @Override
    public int getUpdateCount() throws SQLException {
        this.checkClosed();
        if (this.result == null || this.result.getResultSet() != null) {
            return -1;
        }
        return this.result.getUpdateCount();
    }
    
    @Override
    public boolean getMoreResults() throws SQLException {
        if (this.result == null) {
            return false;
        }
        this.result = this.result.getNext();
        while (this.firstUnclosedResult != this.result) {
            if (this.firstUnclosedResult.getResultSet() != null) {
                this.firstUnclosedResult.getResultSet().close();
            }
            this.firstUnclosedResult = this.firstUnclosedResult.getNext();
        }
        return this.result != null && this.result.getResultSet() != null;
    }
    
    @Override
    public int getMaxRows() throws SQLException {
        this.checkClosed();
        return this.maxrows;
    }
    
    @Override
    public void setMaxRows(final int max) throws SQLException {
        this.checkClosed();
        if (max < 0) {
            throw new PSQLException(GT.tr("Maximum number of rows must be a value grater than or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.maxrows = max;
    }
    
    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        this.checkClosed();
        this.replaceProcessingEnabled = enable;
    }
    
    @Override
    public int getQueryTimeout() throws SQLException {
        this.checkClosed();
        return this.timeout;
    }
    
    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        this.checkClosed();
        if (seconds < 0) {
            throw new PSQLException(GT.tr("Query timeout must be a value greater than or equals to 0."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.timeout = seconds;
    }
    
    public void addWarning(final SQLWarning warn) {
        if (this.warnings == null) {
            this.warnings = warn;
            this.lastWarning = warn;
        }
        else {
            this.lastWarning.setNextWarning(warn);
            this.lastWarning = warn;
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.checkClosed();
        return this.warnings;
    }
    
    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.maxfieldSize;
    }
    
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        this.checkClosed();
        if (max < 0) {
            throw new PSQLException(GT.tr("The maximum field size must be a value greater than or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.maxfieldSize = max;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        this.warnings = null;
        this.lastWarning = null;
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        this.checkClosed();
        if (this.result == null) {
            return null;
        }
        return this.result.getResultSet();
    }
    
    @Override
    public void close() throws SQLException {
        if (this.isClosed) {
            return;
        }
        this.killTimerTask();
        this.closeForNextExecution();
        if (this.preparedQuery != null) {
            this.preparedQuery.close();
        }
        this.isClosed = true;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.close();
        }
        catch (SQLException e) {}
        finally {
            super.finalize();
        }
    }
    
    protected String replaceProcessing(final String p_sql) throws SQLException {
        if (this.replaceProcessingEnabled) {
            final int len = p_sql.length();
            final StringBuilder newsql = new StringBuilder(len);
            for (int i = 0; i < len; ++i) {
                i = parseSql(p_sql, i, newsql, false, this.connection.getStandardConformingStrings());
                if (i < len) {
                    newsql.append(p_sql.charAt(i));
                }
            }
            return newsql.toString();
        }
        return p_sql;
    }
    
    protected static int parseSql(final String p_sql, int i, final StringBuilder newsql, final boolean stopOnComma, final boolean stdStrings) throws SQLException {
        short state = 0;
        final int len = p_sql.length();
        int nestedParenthesis = 0;
        boolean endOfNested = false;
        --i;
        while (!endOfNested && ++i < len) {
            final char c = p_sql.charAt(i);
            switch (state) {
                case 0: {
                    if (c == '\'') {
                        state = 1;
                    }
                    else if (c == '\"') {
                        state = 6;
                    }
                    else if (c == '(') {
                        ++nestedParenthesis;
                    }
                    else if (c == ')') {
                        if (--nestedParenthesis < 0) {
                            endOfNested = true;
                            continue;
                        }
                    }
                    else {
                        if (stopOnComma && c == ',' && nestedParenthesis == 0) {
                            endOfNested = true;
                            continue;
                        }
                        if (c == '{' && i + 1 < len) {
                            final char next = p_sql.charAt(i + 1);
                            final char nextnext = (i + 2 < len) ? p_sql.charAt(i + 2) : '\0';
                            if (next == 'd' || next == 'D') {
                                state = 3;
                                ++i;
                                newsql.append("DATE ");
                                continue;
                            }
                            if (next == 't' || next == 'T') {
                                state = 3;
                                if (nextnext == 's' || nextnext == 'S') {
                                    i += 2;
                                    newsql.append("TIMESTAMP ");
                                    continue;
                                }
                                ++i;
                                newsql.append("TIME ");
                                continue;
                            }
                            else {
                                if (next == 'f' || next == 'F') {
                                    state = 4;
                                    i += ((nextnext == 'n' || nextnext == 'N') ? 2 : 1);
                                    continue;
                                }
                                if (next == 'o' || next == 'O') {
                                    state = 5;
                                    i += ((nextnext == 'j' || nextnext == 'J') ? 2 : 1);
                                    continue;
                                }
                                if (next == 'e' || next == 'E') {
                                    state = 7;
                                    continue;
                                }
                            }
                        }
                    }
                    newsql.append(c);
                    continue;
                }
                case 1: {
                    if (c == '\'') {
                        state = 0;
                    }
                    else if (c == '\\' && !stdStrings) {
                        state = 2;
                    }
                    newsql.append(c);
                    continue;
                }
                case 6: {
                    if (c == '\"') {
                        state = 0;
                    }
                    newsql.append(c);
                    continue;
                }
                case 2: {
                    state = 1;
                    newsql.append(c);
                    continue;
                }
                case 4: {
                    final int posArgs = p_sql.indexOf(40, i);
                    if (posArgs != -1) {
                        final String functionName = p_sql.substring(i, posArgs).trim();
                        i = posArgs + 1;
                        final StringBuilder args = new StringBuilder();
                        i = parseSql(p_sql, i, args, false, stdStrings);
                        newsql.append(escapeFunction(functionName, args.toString(), stdStrings));
                    }
                    ++i;
                    while (i < len && p_sql.charAt(i) != '}') {
                        newsql.append(p_sql.charAt(i++));
                    }
                    state = 0;
                    continue;
                }
                case 3:
                case 5:
                case 7: {
                    if (c == '}') {
                        state = 0;
                        continue;
                    }
                    newsql.append(c);
                    continue;
                }
            }
        }
        return i;
    }
    
    protected static String escapeFunction(final String functionName, final String args, final boolean stdStrings) throws SQLException {
        final int len = args.length();
        int i = 0;
        final ArrayList parsedArgs = new ArrayList();
        while (i < len) {
            final StringBuilder arg = new StringBuilder();
            final int lastPos = i;
            i = parseSql(args, i, arg, true, stdStrings);
            if (lastPos != i) {
                parsedArgs.add(arg);
            }
            ++i;
        }
        try {
            final Method escapeMethod = EscapedFunctions.getFunction(functionName);
            return (String)escapeMethod.invoke(null, parsedArgs);
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SQLException) {
                throw (SQLException)e.getTargetException();
            }
            throw new PSQLException(e.getTargetException().getMessage(), PSQLState.SYSTEM_ERROR);
        }
        catch (Exception e2) {
            final StringBuilder buf = new StringBuilder();
            buf.append(functionName).append('(');
            for (int iArg = 0; iArg < parsedArgs.size(); ++iArg) {
                buf.append(parsedArgs.get(iArg));
                if (iArg != parsedArgs.size() - 1) {
                    buf.append(',');
                }
            }
            buf.append(')');
            return buf.toString();
        }
    }
    
    public int getInsertedOID() throws SQLException {
        this.checkClosed();
        if (this.result == null) {
            return 0;
        }
        return (int)this.result.getInsertOID();
    }
    
    @Override
    public long getLastOID() throws SQLException {
        this.checkClosed();
        if (this.result == null) {
            return 0L;
        }
        return this.result.getInsertOID();
    }
    
    public void setNull(int parameterIndex, final int sqlType) throws SQLException {
        this.checkClosed();
        int oid = 0;
        switch (sqlType) {
            case 4: {
                oid = 23;
                break;
            }
            case -6:
            case 5: {
                oid = 21;
                break;
            }
            case -5: {
                oid = 20;
                break;
            }
            case 7: {
                oid = 700;
                break;
            }
            case 6:
            case 8: {
                oid = 701;
                break;
            }
            case 2:
            case 3: {
                oid = 1700;
                break;
            }
            case 1: {
                oid = 1042;
                break;
            }
            case -1:
            case 12: {
                oid = (this.connection.getStringVarcharFlag() ? 1043 : 0);
                break;
            }
            case 91: {
                oid = 1082;
                break;
            }
            case 92:
            case 93: {
                oid = 0;
                break;
            }
            case -7: {
                oid = 16;
                break;
            }
            case -4:
            case -3:
            case -2: {
                if (this.connection.haveMinimumCompatibleVersion("7.2")) {
                    oid = 17;
                    break;
                }
                oid = 26;
                break;
            }
            case 2004:
            case 2005: {
                oid = 26;
                break;
            }
            case 0:
            case 1111:
            case 2001:
            case 2002:
            case 2003: {
                oid = 0;
                break;
            }
            default: {
                throw new PSQLException(GT.tr("Unknown Types value."), PSQLState.INVALID_PARAMETER_TYPE);
            }
        }
        if (this.adjustIndex) {
            --parameterIndex;
        }
        this.preparedParameters.setNull(parameterIndex, oid);
    }
    
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        this.checkClosed();
        this.bindString(parameterIndex, x ? "1" : "0", 16);
    }
    
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        this.setShort(parameterIndex, x);
    }
    
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        this.checkClosed();
        if (this.connection.binaryTransferSend(21)) {
            final byte[] val = new byte[2];
            ByteConverter.int2(val, 0, x);
            this.bindBytes(parameterIndex, val, 21);
            return;
        }
        this.bindLiteral(parameterIndex, Integer.toString(x), 21);
    }
    
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        this.checkClosed();
        if (this.connection.binaryTransferSend(23)) {
            final byte[] val = new byte[4];
            ByteConverter.int4(val, 0, x);
            this.bindBytes(parameterIndex, val, 23);
            return;
        }
        this.bindLiteral(parameterIndex, Integer.toString(x), 23);
    }
    
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        this.checkClosed();
        if (this.connection.binaryTransferSend(20)) {
            final byte[] val = new byte[8];
            ByteConverter.int8(val, 0, x);
            this.bindBytes(parameterIndex, val, 20);
            return;
        }
        this.bindLiteral(parameterIndex, Long.toString(x), 20);
    }
    
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        this.checkClosed();
        if (this.connection.binaryTransferSend(700)) {
            final byte[] val = new byte[4];
            ByteConverter.float4(val, 0, x);
            this.bindBytes(parameterIndex, val, 700);
            return;
        }
        this.bindLiteral(parameterIndex, Float.toString(x), 701);
    }
    
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        this.checkClosed();
        if (this.connection.binaryTransferSend(701)) {
            final byte[] val = new byte[8];
            ByteConverter.float8(val, 0, x);
            this.bindBytes(parameterIndex, val, 701);
            return;
        }
        this.bindLiteral(parameterIndex, Double.toString(x), 701);
    }
    
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(parameterIndex, 3);
        }
        else {
            this.bindLiteral(parameterIndex, x.toString(), 1700);
        }
    }
    
    public void setString(final int parameterIndex, final String x) throws SQLException {
        this.checkClosed();
        this.setString(parameterIndex, x, this.getStringType());
    }
    
    private int getStringType() {
        return this.connection.getStringVarcharFlag() ? 1043 : 0;
    }
    
    protected void setString(int parameterIndex, final String x, final int oid) throws SQLException {
        this.checkClosed();
        if (x == null) {
            if (this.adjustIndex) {
                --parameterIndex;
            }
            this.preparedParameters.setNull(parameterIndex, oid);
        }
        else {
            this.bindString(parameterIndex, x, oid);
        }
    }
    
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        this.checkClosed();
        if (null == x) {
            this.setNull(parameterIndex, -3);
            return;
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            final byte[] copy = new byte[x.length];
            System.arraycopy(x, 0, copy, 0, x.length);
            this.preparedParameters.setBytea(parameterIndex, copy, 0, x.length);
        }
        else {
            final LargeObjectManager lom = this.connection.getLargeObjectAPI();
            final long oid = lom.createLO();
            final LargeObject lob = lom.open(oid);
            lob.write(x);
            lob.close();
            this.setLong(parameterIndex, oid);
        }
    }
    
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        this.setDate(parameterIndex, x, null);
    }
    
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        this.setTime(parameterIndex, x, null);
    }
    
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        this.setTimestamp(parameterIndex, x, null);
    }
    
    private void setCharacterStreamPost71(final int parameterIndex, final InputStream x, final int length, final String encoding) throws SQLException {
        if (x == null) {
            this.setNull(parameterIndex, 12);
            return;
        }
        if (length < 0) {
            throw new PSQLException(GT.tr("Invalid stream length {0}.", new Integer(length)), PSQLState.INVALID_PARAMETER_VALUE);
        }
        try {
            final InputStreamReader l_inStream = new InputStreamReader(x, encoding);
            final char[] l_chars = new char[length];
            int l_charsRead = 0;
            do {
                final int n = l_inStream.read(l_chars, l_charsRead, length - l_charsRead);
                if (n == -1) {
                    break;
                }
                l_charsRead += n;
            } while (l_charsRead != length);
            this.setString(parameterIndex, new String(l_chars, 0, l_charsRead), 1043);
        }
        catch (UnsupportedEncodingException l_uee) {
            throw new PSQLException(GT.tr("The JVM claims not to support the {0} encoding.", encoding), PSQLState.UNEXPECTED_ERROR, l_uee);
        }
        catch (IOException l_ioe) {
            throw new PSQLException(GT.tr("Provided InputStream failed."), PSQLState.UNEXPECTED_ERROR, l_ioe);
        }
    }
    
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkClosed();
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            this.setCharacterStreamPost71(parameterIndex, x, length, "ASCII");
        }
        else {
            this.setBinaryStream(parameterIndex, x, length);
        }
    }
    
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkClosed();
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            this.setCharacterStreamPost71(parameterIndex, x, length, "UTF-8");
        }
        else {
            this.setBinaryStream(parameterIndex, x, length);
        }
    }
    
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(parameterIndex, -3);
            return;
        }
        if (length < 0) {
            throw new PSQLException(GT.tr("Invalid stream length {0}.", new Integer(length)), PSQLState.INVALID_PARAMETER_VALUE);
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            this.preparedParameters.setBytea(parameterIndex, x, length);
        }
        else {
            final LargeObjectManager lom = this.connection.getLargeObjectAPI();
            final long oid = lom.createLO();
            final LargeObject lob = lom.open(oid);
            final OutputStream los = lob.getOutputStream();
            try {
                for (int c = x.read(), p = 0; c > -1 && p < length; c = x.read(), ++p) {
                    los.write(c);
                }
                los.close();
            }
            catch (IOException se) {
                throw new PSQLException(GT.tr("Provided InputStream failed."), PSQLState.UNEXPECTED_ERROR, se);
            }
            this.setLong(parameterIndex, oid);
        }
    }
    
    public void clearParameters() throws SQLException {
        this.preparedParameters.clear();
    }
    
    private PGType createInternalType(final Object x, final int targetType) throws PSQLException {
        if (x instanceof Byte) {
            return PGByte.castToServerType((Byte)x, targetType);
        }
        if (x instanceof Short) {
            return PGShort.castToServerType((Short)x, targetType);
        }
        if (x instanceof Integer) {
            return PGInteger.castToServerType((Integer)x, targetType);
        }
        if (x instanceof Long) {
            return PGLong.castToServerType((Long)x, targetType);
        }
        if (x instanceof Double) {
            return PGDouble.castToServerType((Double)x, targetType);
        }
        if (x instanceof Float) {
            return PGFloat.castToServerType((Float)x, targetType);
        }
        if (x instanceof BigDecimal) {
            return PGBigDecimal.castToServerType((BigDecimal)x, targetType);
        }
        if (x instanceof Number) {
            return PGNumber.castToServerType((Number)x, targetType);
        }
        if (x instanceof Boolean) {
            return PGBoolean.castToServerType((Boolean)x, targetType);
        }
        return new PGUnknown(x);
    }
    
    private void setPGobject(final int parameterIndex, final PGobject x) throws SQLException {
        final String typename = x.getType();
        final int oid = this.connection.getTypeInfo().getPGType(typename);
        if (oid == 0) {
            throw new PSQLException(GT.tr("Unknown type {0}.", typename), PSQLState.INVALID_PARAMETER_TYPE);
        }
        if (x instanceof PGBinaryObject && this.connection.binaryTransferSend(oid)) {
            final PGBinaryObject binObj = (PGBinaryObject)x;
            final byte[] data = new byte[binObj.lengthInBytes()];
            binObj.toBytes(data, 0);
            this.bindBytes(parameterIndex, data, oid);
        }
        else {
            this.setString(parameterIndex, x.getValue(), oid);
        }
    }
    
    private void setMap(final int parameterIndex, final Map x) throws SQLException {
        final int oid = this.connection.getTypeInfo().getPGType("hstore");
        if (oid == 0) {
            throw new PSQLException(GT.tr("No hstore extension installed."), PSQLState.INVALID_PARAMETER_TYPE);
        }
        if (this.connection.binaryTransferSend(oid)) {
            final byte[] data = HStoreConverter.toBytes(x, this.connection.getEncoding());
            this.bindBytes(parameterIndex, data, oid);
        }
        else {
            this.setString(parameterIndex, HStoreConverter.toString(x), oid);
        }
    }
    
    public void setObject(final int parameterIndex, final Object in, final int targetSqlType, final int scale) throws SQLException {
        this.checkClosed();
        if (in == null) {
            this.setNull(parameterIndex, targetSqlType);
            return;
        }
        final Object pgType = this.createInternalType(in, targetSqlType);
        switch (targetSqlType) {
            case 4: {
                this.bindLiteral(parameterIndex, pgType.toString(), 23);
                break;
            }
            case -6:
            case 5: {
                this.bindLiteral(parameterIndex, pgType.toString(), 21);
                break;
            }
            case -5: {
                this.bindLiteral(parameterIndex, pgType.toString(), 20);
                break;
            }
            case 7: {
                this.bindLiteral(parameterIndex, pgType.toString(), 700);
                break;
            }
            case 6:
            case 8: {
                this.bindLiteral(parameterIndex, pgType.toString(), 701);
                break;
            }
            case 2:
            case 3: {
                this.bindLiteral(parameterIndex, pgType.toString(), 1700);
                break;
            }
            case 1: {
                this.setString(parameterIndex, pgType.toString(), 1042);
                break;
            }
            case -1:
            case 12: {
                this.setString(parameterIndex, pgType.toString(), this.getStringType());
                break;
            }
            case 91: {
                if (in instanceof Date) {
                    this.setDate(parameterIndex, (Date)in);
                    break;
                }
                Date tmpd;
                if (in instanceof java.util.Date) {
                    tmpd = new Date(((java.util.Date)in).getTime());
                }
                else {
                    tmpd = this.connection.getTimestampUtils().toDate(null, in.toString());
                }
                this.setDate(parameterIndex, tmpd);
                break;
            }
            case 92: {
                if (in instanceof Time) {
                    this.setTime(parameterIndex, (Time)in);
                    break;
                }
                Time tmpt;
                if (in instanceof java.util.Date) {
                    tmpt = new Time(((java.util.Date)in).getTime());
                }
                else {
                    tmpt = this.connection.getTimestampUtils().toTime(null, in.toString());
                }
                this.setTime(parameterIndex, tmpt);
                break;
            }
            case 93: {
                if (in instanceof Timestamp) {
                    this.setTimestamp(parameterIndex, (Timestamp)in);
                    break;
                }
                Timestamp tmpts;
                if (in instanceof java.util.Date) {
                    tmpts = new Timestamp(((java.util.Date)in).getTime());
                }
                else {
                    tmpts = this.connection.getTimestampUtils().toTimestamp(null, in.toString());
                }
                this.setTimestamp(parameterIndex, tmpts);
                break;
            }
            case -7: {
                this.bindLiteral(parameterIndex, pgType.toString(), 16);
                break;
            }
            case -4:
            case -3:
            case -2: {
                this.setObject(parameterIndex, in);
                break;
            }
            case 2004: {
                if (in instanceof Blob) {
                    this.setBlob(parameterIndex, (Blob)in);
                    break;
                }
                if (in instanceof InputStream) {
                    final long oid = this.createBlob(parameterIndex, (InputStream)in, -1L);
                    this.setLong(parameterIndex, oid);
                    break;
                }
                throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", new Object[] { in.getClass().getName(), "Types.BLOB" }), PSQLState.INVALID_PARAMETER_TYPE);
            }
            case 2005: {
                if (in instanceof Clob) {
                    this.setClob(parameterIndex, (Clob)in);
                    break;
                }
                throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", new Object[] { in.getClass().getName(), "Types.CLOB" }), PSQLState.INVALID_PARAMETER_TYPE);
            }
            case 2003: {
                if (in instanceof Array) {
                    this.setArray(parameterIndex, (Array)in);
                    break;
                }
                throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", new Object[] { in.getClass().getName(), "Types.ARRAY" }), PSQLState.INVALID_PARAMETER_TYPE);
            }
            case 2001: {
                this.bindString(parameterIndex, in.toString(), 0);
                break;
            }
            case 1111: {
                if (in instanceof PGobject) {
                    this.setPGobject(parameterIndex, (PGobject)in);
                    break;
                }
                this.bindString(parameterIndex, in.toString(), 0);
                break;
            }
            default: {
                throw new PSQLException(GT.tr("Unsupported Types value: {0}", new Integer(targetSqlType)), PSQLState.INVALID_PARAMETER_TYPE);
            }
        }
    }
    
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        this.setObject(parameterIndex, x, targetSqlType, 0);
    }
    
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(parameterIndex, 1111);
        }
        else if (x instanceof String) {
            this.setString(parameterIndex, (String)x);
        }
        else if (x instanceof BigDecimal) {
            this.setBigDecimal(parameterIndex, (BigDecimal)x);
        }
        else if (x instanceof Short) {
            this.setShort(parameterIndex, (short)x);
        }
        else if (x instanceof Integer) {
            this.setInt(parameterIndex, (int)x);
        }
        else if (x instanceof Long) {
            this.setLong(parameterIndex, (long)x);
        }
        else if (x instanceof Float) {
            this.setFloat(parameterIndex, (float)x);
        }
        else if (x instanceof Double) {
            this.setDouble(parameterIndex, (double)x);
        }
        else if (x instanceof byte[]) {
            this.setBytes(parameterIndex, (byte[])x);
        }
        else if (x instanceof Date) {
            this.setDate(parameterIndex, (Date)x);
        }
        else if (x instanceof Time) {
            this.setTime(parameterIndex, (Time)x);
        }
        else if (x instanceof Timestamp) {
            this.setTimestamp(parameterIndex, (Timestamp)x);
        }
        else if (x instanceof Boolean) {
            this.setBoolean(parameterIndex, (boolean)x);
        }
        else if (x instanceof Byte) {
            this.setByte(parameterIndex, (byte)x);
        }
        else if (x instanceof Blob) {
            this.setBlob(parameterIndex, (Blob)x);
        }
        else if (x instanceof Clob) {
            this.setClob(parameterIndex, (Clob)x);
        }
        else if (x instanceof Array) {
            this.setArray(parameterIndex, (Array)x);
        }
        else if (x instanceof PGobject) {
            this.setPGobject(parameterIndex, (PGobject)x);
        }
        else if (x instanceof Character) {
            this.setString(parameterIndex, ((Character)x).toString());
        }
        else {
            if (!(x instanceof Map)) {
                throw new PSQLException(GT.tr("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", x.getClass().getName()), PSQLState.INVALID_PARAMETER_TYPE);
            }
            this.setMap(parameterIndex, (Map)x);
        }
    }
    
    public void registerOutParameter(final int parameterIndex, int sqlType, final boolean setPreparedParameters) throws SQLException {
        this.checkClosed();
        switch (sqlType) {
            case -6: {
                sqlType = 5;
                break;
            }
            case -1: {
                sqlType = 12;
                break;
            }
            case 3: {
                sqlType = 2;
                break;
            }
            case 6: {
                sqlType = 8;
                break;
            }
            case -4:
            case -3: {
                sqlType = -2;
                break;
            }
        }
        if (!this.isFunction) {
            throw new PSQLException(GT.tr("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one."), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
        }
        this.checkIndex(parameterIndex, false);
        if (setPreparedParameters) {
            this.preparedParameters.registerOutParameter(parameterIndex, sqlType);
        }
        this.functionReturnType[parameterIndex - 1] = sqlType;
        this.testReturn[parameterIndex - 1] = sqlType;
        if (this.functionReturnType[parameterIndex - 1] == 1 || this.functionReturnType[parameterIndex - 1] == -1) {
            this.testReturn[parameterIndex - 1] = 12;
        }
        else if (this.functionReturnType[parameterIndex - 1] == 6) {
            this.testReturn[parameterIndex - 1] = 7;
        }
        this.returnTypeSet = true;
    }
    
    public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale, final boolean setPreparedParameters) throws SQLException {
        this.registerOutParameter(parameterIndex, sqlType, setPreparedParameters);
    }
    
    public boolean wasNull() throws SQLException {
        if (this.lastIndex == 0) {
            throw new PSQLException(GT.tr("wasNull cannot be call before fetching a result."), PSQLState.OBJECT_NOT_IN_STATE);
        }
        return this.callResult[this.lastIndex - 1] == null;
    }
    
    public String getString(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 12, "String");
        return (String)this.callResult[parameterIndex - 1];
    }
    
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -7, "Boolean");
        return this.callResult[parameterIndex - 1] != null && (boolean)this.callResult[parameterIndex - 1];
    }
    
    public byte getByte(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 5, "Byte");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return ((Integer)this.callResult[parameterIndex - 1]).byteValue();
    }
    
    public short getShort(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 5, "Short");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return ((Integer)this.callResult[parameterIndex - 1]).shortValue();
    }
    
    public int getInt(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 4, "Int");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0;
        }
        return (int)this.callResult[parameterIndex - 1];
    }
    
    public long getLong(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -5, "Long");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0L;
        }
        return (long)this.callResult[parameterIndex - 1];
    }
    
    public float getFloat(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 7, "Float");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0.0f;
        }
        return (float)this.callResult[parameterIndex - 1];
    }
    
    public double getDouble(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 8, "Double");
        if (this.callResult[parameterIndex - 1] == null) {
            return 0.0;
        }
        return (double)this.callResult[parameterIndex - 1];
    }
    
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2, "BigDecimal");
        return (BigDecimal)this.callResult[parameterIndex - 1];
    }
    
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, -3, -2, "Bytes");
        return (byte[])this.callResult[parameterIndex - 1];
    }
    
    public Date getDate(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 91, "Date");
        return (Date)this.callResult[parameterIndex - 1];
    }
    
    public Time getTime(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 92, "Time");
        return (Time)this.callResult[parameterIndex - 1];
    }
    
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 93, "Timestamp");
        return (Timestamp)this.callResult[parameterIndex - 1];
    }
    
    public Object getObject(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex);
        return this.callResult[parameterIndex - 1];
    }
    
    @Override
    public String toString() {
        if (this.preparedQuery == null) {
            return super.toString();
        }
        return this.preparedQuery.toString(this.preparedParameters);
    }
    
    protected void bindLiteral(int paramIndex, final String s, final int oid) throws SQLException {
        if (this.adjustIndex) {
            --paramIndex;
        }
        this.preparedParameters.setLiteralParameter(paramIndex, s, oid);
    }
    
    protected void bindBytes(int paramIndex, final byte[] b, final int oid) throws SQLException {
        if (this.adjustIndex) {
            --paramIndex;
        }
        this.preparedParameters.setBinaryParameter(paramIndex, b, oid);
    }
    
    private void bindString(int paramIndex, final String s, final int oid) throws SQLException {
        if (this.adjustIndex) {
            --paramIndex;
        }
        this.preparedParameters.setStringParameter(paramIndex, s, oid);
    }
    
    private String modifyJdbcCall(final String p_sql) throws SQLException {
        this.checkClosed();
        this.isFunction = false;
        final boolean stdStrings = this.connection.getStandardConformingStrings();
        final int len = p_sql.length();
        int state = 1;
        boolean inQuotes = false;
        boolean inEscape = false;
        this.outParmBeforeFunc = false;
        int startIndex = -1;
        int endIndex = -1;
        boolean syntaxError = false;
        int i = 0;
        while (i < len && !syntaxError) {
            final char ch = p_sql.charAt(i);
            switch (state) {
                case 1: {
                    if (ch == '{') {
                        ++i;
                        ++state;
                        continue;
                    }
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    i = len;
                    continue;
                }
                case 2: {
                    if (ch == '?') {
                        final boolean b = true;
                        this.isFunction = b;
                        this.outParmBeforeFunc = b;
                        ++i;
                        ++state;
                        continue;
                    }
                    if (ch == 'c' || ch == 'C') {
                        state += 3;
                        continue;
                    }
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                case 3: {
                    if (ch == '=') {
                        ++i;
                        ++state;
                        continue;
                    }
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                case 4: {
                    if (ch == 'c' || ch == 'C') {
                        ++state;
                        continue;
                    }
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                case 5: {
                    if ((ch == 'c' || ch == 'C') && i + 4 <= len && p_sql.substring(i, i + 4).equalsIgnoreCase("call")) {
                        this.isFunction = true;
                        i += 4;
                        ++state;
                        continue;
                    }
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                case 6: {
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        ++state;
                        startIndex = i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                case 7: {
                    if (ch == '\'') {
                        inQuotes = !inQuotes;
                        ++i;
                        continue;
                    }
                    if (inQuotes && ch == '\\' && !stdStrings) {
                        i += 2;
                        continue;
                    }
                    if (!inQuotes && ch == '{') {
                        inEscape = !inEscape;
                        ++i;
                        continue;
                    }
                    if (!inQuotes && ch == '}') {
                        if (!inEscape) {
                            endIndex = i;
                            ++i;
                            ++state;
                            continue;
                        }
                        inEscape = false;
                        continue;
                    }
                    else {
                        if (!inQuotes && ch == ';') {
                            syntaxError = true;
                            continue;
                        }
                        ++i;
                        continue;
                    }
                    break;
                }
                case 8: {
                    if (Character.isWhitespace(ch)) {
                        ++i;
                        continue;
                    }
                    syntaxError = true;
                    continue;
                }
                default: {
                    throw new IllegalStateException("somehow got into bad state " + state);
                }
            }
        }
        if (i == len && !syntaxError) {
            if (state == 1) {
                return p_sql;
            }
            if (state != 8) {
                syntaxError = true;
            }
        }
        if (syntaxError) {
            throw new PSQLException(GT.tr("Malformed function or procedure escape syntax at offset {0}.", new Integer(i)), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
        }
        if (this.connection.haveMinimumServerVersion("8.1") && ((AbstractJdbc2Connection)this.connection).getProtocolVersion() == 3) {
            final String s = p_sql.substring(startIndex, endIndex);
            final StringBuilder sb = new StringBuilder(s);
            if (this.outParmBeforeFunc) {
                boolean needComma = false;
                final int opening = s.indexOf(40) + 1;
                for (int closing = s.indexOf(41), j = opening; j < closing; ++j) {
                    if (!Character.isWhitespace(sb.charAt(j))) {
                        needComma = true;
                        break;
                    }
                }
                if (needComma) {
                    sb.insert(opening, "?,");
                }
                else {
                    sb.insert(opening, "?");
                }
            }
            return "select * from " + sb.toString() + " as result";
        }
        return "select " + p_sql.substring(startIndex, endIndex) + " as result";
    }
    
    protected void checkIndex(final int parameterIndex, final int type1, final int type2, final String getName) throws SQLException {
        this.checkIndex(parameterIndex);
        if (type1 != this.testReturn[parameterIndex - 1] && type2 != this.testReturn[parameterIndex - 1]) {
            throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", new Object[] { "java.sql.Types=" + this.testReturn[parameterIndex - 1], getName, "java.sql.Types=" + type1 }), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
        }
    }
    
    protected void checkIndex(final int parameterIndex, final int type, final String getName) throws SQLException {
        this.checkIndex(parameterIndex);
        if (type != this.testReturn[parameterIndex - 1]) {
            throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", new Object[] { "java.sql.Types=" + this.testReturn[parameterIndex - 1], getName, "java.sql.Types=" + type }), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
        }
    }
    
    private void checkIndex(final int parameterIndex) throws SQLException {
        this.checkIndex(parameterIndex, true);
    }
    
    private void checkIndex(final int parameterIndex, final boolean fetchingData) throws SQLException {
        if (!this.isFunction) {
            throw new PSQLException(GT.tr("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made."), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
        }
        if (fetchingData) {
            if (!this.returnTypeSet) {
                throw new PSQLException(GT.tr("No function outputs were registered."), PSQLState.OBJECT_NOT_IN_STATE);
            }
            if (this.callResult == null) {
                throw new PSQLException(GT.tr("Results cannot be retrieved from a CallableStatement before it is executed."), PSQLState.NO_DATA);
            }
            this.lastIndex = parameterIndex;
        }
    }
    
    @Override
    public void setPrepareThreshold(int newThreshold) throws SQLException {
        this.checkClosed();
        if (newThreshold < 0) {
            this.forceBinaryTransfers = true;
            newThreshold = 1;
        }
        else {
            this.forceBinaryTransfers = false;
        }
        this.m_prepareThreshold = newThreshold;
    }
    
    @Override
    public int getPrepareThreshold() {
        return this.m_prepareThreshold;
    }
    
    @Override
    public void setUseServerPrepare(final boolean flag) throws SQLException {
        this.setPrepareThreshold(flag ? 1 : 0);
    }
    
    @Override
    public boolean isUseServerPrepare() {
        return this.preparedQuery != null && this.m_prepareThreshold != 0 && this.m_useCount + 1 >= this.m_prepareThreshold;
    }
    
    protected void checkClosed() throws SQLException {
        if (this.isClosed) {
            throw new PSQLException(GT.tr("This statement has been closed."), PSQLState.OBJECT_NOT_IN_STATE);
        }
    }
    
    @Override
    public void addBatch(String p_sql) throws SQLException {
        this.checkClosed();
        if (this.preparedQuery != null) {
            throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
        }
        if (this.batchStatements == null) {
            this.batchStatements = new ArrayList();
            this.batchParameters = new ArrayList();
        }
        p_sql = this.replaceProcessing(p_sql);
        this.batchStatements.add(this.connection.getQueryExecutor().createSimpleQuery(p_sql));
        this.batchParameters.add(null);
    }
    
    @Override
    public void clearBatch() throws SQLException {
        if (this.batchStatements != null) {
            this.batchStatements.clear();
            this.batchParameters.clear();
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        this.checkClosed();
        this.closeForNextExecution();
        if (this.batchStatements == null || this.batchStatements.isEmpty()) {
            return new int[0];
        }
        final int size = this.batchStatements.size();
        final int[] updateCounts = new int[size];
        final Query[] queries = this.batchStatements.toArray(new Query[this.batchStatements.size()]);
        final ParameterList[] parameterLists = this.batchParameters.toArray(new ParameterList[this.batchParameters.size()]);
        this.batchStatements.clear();
        this.batchParameters.clear();
        int flags = 0;
        boolean preDescribe = false;
        if (this.wantsGeneratedKeysAlways) {
            flags = 64;
        }
        else {
            flags = 4;
        }
        if (this.preparedQuery != null) {
            this.m_useCount += queries.length;
        }
        if (this.m_prepareThreshold == 0 || this.m_useCount < this.m_prepareThreshold) {
            flags |= 0x1;
        }
        else {
            preDescribe = (this.wantsGeneratedKeysAlways && !queries[0].isStatementDescribed());
        }
        if (this.connection.getAutoCommit()) {
            flags |= 0x10;
        }
        if (preDescribe || this.forceBinaryTransfers) {
            final int flags2 = flags | 0x20;
            final StatementResultHandler handler2 = new StatementResultHandler();
            this.connection.getQueryExecutor().execute(queries[0], parameterLists[0], handler2, 0, 0, flags2);
            final ResultWrapper result2 = handler2.getResults();
            if (result2 != null) {
                result2.getResultSet().close();
            }
        }
        this.result = null;
        ResultHandler handler3;
        if (this.isFunction) {
            handler3 = new CallableBatchResultHandler(queries, parameterLists, updateCounts);
        }
        else {
            handler3 = new BatchResultHandler(queries, parameterLists, updateCounts, this.wantsGeneratedKeysAlways);
        }
        try {
            this.startTimer();
            this.connection.getQueryExecutor().execute(queries, parameterLists, handler3, this.maxrows, this.fetchSize, flags);
        }
        finally {
            this.killTimerTask();
        }
        if (this.wantsGeneratedKeysAlways) {
            this.generatedKeys = new ResultWrapper(((BatchResultHandler)handler3).getGeneratedKeys());
        }
        return updateCounts;
    }
    
    @Override
    public void cancel() throws SQLException {
        this.connection.cancelQuery();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }
    
    @Override
    public int getFetchDirection() {
        return this.fetchdirection;
    }
    
    @Override
    public int getResultSetConcurrency() {
        return this.concurrency;
    }
    
    @Override
    public int getResultSetType() {
        return this.resultsettype;
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        switch (direction) {
            case 1000:
            case 1001:
            case 1002: {
                this.fetchdirection = direction;
            }
            default: {
                throw new PSQLException(GT.tr("Invalid fetch direction constant: {0}.", new Integer(direction)), PSQLState.INVALID_PARAMETER_VALUE);
            }
        }
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        this.checkClosed();
        if (rows < 0) {
            throw new PSQLException(GT.tr("Fetch size must be a value greater to or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
        }
        this.fetchSize = rows;
    }
    
    public void addBatch() throws SQLException {
        this.checkClosed();
        if (this.batchStatements == null) {
            this.batchStatements = new ArrayList();
            this.batchParameters = new ArrayList();
        }
        this.batchStatements.add(this.preparedQuery);
        this.batchParameters.add(this.preparedParameters.copy());
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkClosed();
        ResultSet rs = this.getResultSet();
        if (rs == null || ((AbstractJdbc2ResultSet)rs).isResultSetClosed()) {
            final int flags = 49;
            final StatementResultHandler handler = new StatementResultHandler();
            this.connection.getQueryExecutor().execute(this.preparedQuery, this.preparedParameters, handler, 0, 0, flags);
            final ResultWrapper wrapper = handler.getResults();
            if (wrapper != null) {
                rs = wrapper.getResultSet();
            }
        }
        if (rs != null) {
            return rs.getMetaData();
        }
        return null;
    }
    
    public void setArray(final int i, final Array x) throws SQLException {
        this.checkClosed();
        if (null == x) {
            this.setNull(i, 2003);
            return;
        }
        final String typename = "_" + x.getBaseTypeName();
        final int oid = this.connection.getTypeInfo().getPGType(typename);
        if (oid == 0) {
            throw new PSQLException(GT.tr("Unknown type {0}.", typename), PSQLState.INVALID_PARAMETER_TYPE);
        }
        if (x instanceof AbstractJdbc2Array) {
            final AbstractJdbc2Array arr = (AbstractJdbc2Array)x;
            if (arr.isBinary()) {
                this.bindBytes(i, arr.toBytes(), oid);
                return;
            }
        }
        this.setString(i, x.toString(), oid);
    }
    
    protected long createBlob(final int i, final InputStream inputStream, final long length) throws SQLException {
        final LargeObjectManager lom = this.connection.getLargeObjectAPI();
        final long oid = lom.createLO();
        final LargeObject lob = lom.open(oid);
        final OutputStream outputStream = lob.getOutputStream();
        final byte[] buf = new byte[4096];
        try {
            long remaining;
            if (length > 0L) {
                remaining = length;
            }
            else {
                remaining = Long.MAX_VALUE;
            }
            for (int numRead = inputStream.read(buf, 0, (length > 0L && remaining < buf.length) ? ((int)remaining) : buf.length); numRead != -1 && remaining > 0L; numRead = inputStream.read(buf, 0, (length > 0L && remaining < buf.length) ? ((int)remaining) : buf.length)) {
                remaining -= numRead;
                outputStream.write(buf, 0, numRead);
            }
        }
        catch (IOException se) {
            throw new PSQLException(GT.tr("Unexpected error writing large object to database."), PSQLState.UNEXPECTED_ERROR, se);
        }
        finally {
            try {
                outputStream.close();
            }
            catch (Exception ex) {}
        }
        return oid;
    }
    
    public void setBlob(final int i, final Blob x) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(i, 2004);
            return;
        }
        final InputStream inStream = x.getBinaryStream();
        try {
            final long oid = this.createBlob(i, inStream, x.length());
            this.setLong(i, oid);
        }
        finally {
            try {
                inStream.close();
            }
            catch (Exception ex) {}
        }
    }
    
    public void setCharacterStream(final int i, final Reader x, final int length) throws SQLException {
        this.checkClosed();
        if (x == null) {
            if (this.connection.haveMinimumServerVersion("7.2")) {
                this.setNull(i, 12);
            }
            else {
                this.setNull(i, 2005);
            }
            return;
        }
        if (length < 0) {
            throw new PSQLException(GT.tr("Invalid stream length {0}.", new Integer(length)), PSQLState.INVALID_PARAMETER_VALUE);
        }
        if (this.connection.haveMinimumCompatibleVersion("7.2")) {
            final char[] l_chars = new char[length];
            int l_charsRead = 0;
            try {
                do {
                    final int n = x.read(l_chars, l_charsRead, length - l_charsRead);
                    if (n == -1) {
                        break;
                    }
                    l_charsRead += n;
                } while (l_charsRead != length);
            }
            catch (IOException l_ioe) {
                throw new PSQLException(GT.tr("Provided Reader failed."), PSQLState.UNEXPECTED_ERROR, l_ioe);
            }
            this.setString(i, new String(l_chars, 0, l_charsRead));
        }
        else {
            final LargeObjectManager lom = this.connection.getLargeObjectAPI();
            final long oid = lom.createLO();
            final LargeObject lob = lom.open(oid);
            final OutputStream los = lob.getOutputStream();
            try {
                for (int c = x.read(), p = 0; c > -1 && p < length; c = x.read(), ++p) {
                    los.write(c);
                }
                los.close();
            }
            catch (IOException se) {
                throw new PSQLException(GT.tr("Unexpected error writing large object to database."), PSQLState.UNEXPECTED_ERROR, se);
            }
            this.setLong(i, oid);
        }
    }
    
    public void setClob(final int i, final Clob x) throws SQLException {
        this.checkClosed();
        if (x == null) {
            this.setNull(i, 2005);
            return;
        }
        final Reader l_inStream = x.getCharacterStream();
        final int l_length = (int)x.length();
        final LargeObjectManager lom = this.connection.getLargeObjectAPI();
        final long oid = lom.createLO();
        final LargeObject lob = lom.open(oid);
        final Charset connectionCharset = Charset.forName(this.connection.getEncoding().name());
        final OutputStream los = lob.getOutputStream();
        final Writer lw = new OutputStreamWriter(los, connectionCharset);
        try {
            for (int c = l_inStream.read(), p = 0; c > -1 && p < l_length; c = l_inStream.read(), ++p) {
                lw.write(c);
            }
            lw.close();
        }
        catch (IOException se) {
            throw new PSQLException(GT.tr("Unexpected error writing large object to database."), PSQLState.UNEXPECTED_ERROR, se);
        }
        this.setLong(i, oid);
    }
    
    public void setNull(final int i, final int t, final String s) throws SQLException {
        this.checkClosed();
        this.setNull(i, t);
    }
    
    public void setRef(final int i, final Ref x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "setRef(int,Ref)");
    }
    
    public void setDate(final int i, final Date d, Calendar cal) throws SQLException {
        this.checkClosed();
        if (d == null) {
            this.setNull(i, 91);
            return;
        }
        if (this.connection.binaryTransferSend(1082)) {
            final byte[] val = new byte[4];
            final TimeZone tz = (cal != null) ? cal.getTimeZone() : null;
            this.connection.getTimestampUtils().toBinDate(tz, val, d);
            this.preparedParameters.setBinaryParameter(i, val, 1082);
            return;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        this.bindString(i, this.connection.getTimestampUtils().toString(cal, d), 0);
    }
    
    public void setTime(final int i, final Time t, Calendar cal) throws SQLException {
        this.checkClosed();
        if (t == null) {
            this.setNull(i, 92);
            return;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        this.bindString(i, this.connection.getTimestampUtils().toString(cal, t), 0);
    }
    
    public void setTimestamp(final int i, final Timestamp t, Calendar cal) throws SQLException {
        this.checkClosed();
        if (t == null) {
            this.setNull(i, 93);
            return;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        this.bindString(i, this.connection.getTimestampUtils().toString(cal, t), 0);
    }
    
    public Array getArray(final int i) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 2003, "Array");
        return (Array)this.callResult[i - 1];
    }
    
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        this.checkClosed();
        this.checkIndex(parameterIndex, 2, "BigDecimal");
        return (BigDecimal)this.callResult[parameterIndex - 1];
    }
    
    public Blob getBlob(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getBlob(int)");
    }
    
    public Clob getClob(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getClob(int)");
    }
    
    public Object getObjectImpl(final int i, final Map map) throws SQLException {
        if (map == null || map.isEmpty()) {
            return this.getObject(i);
        }
        throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
    }
    
    public Ref getRef(final int i) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRef(int)");
    }
    
    public Date getDate(final int i, Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 91, "Date");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toDate(cal, value);
    }
    
    public Time getTime(final int i, Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 92, "Time");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toTime(cal, value);
    }
    
    public Timestamp getTimestamp(final int i, Calendar cal) throws SQLException {
        this.checkClosed();
        this.checkIndex(i, 93, "Timestamp");
        if (this.callResult[i - 1] == null) {
            return null;
        }
        if (cal != null) {
            cal = (Calendar)cal.clone();
        }
        final String value = this.callResult[i - 1].toString();
        return this.connection.getTimestampUtils().toTimestamp(cal, value);
    }
    
    public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "registerOutParameter(int,int,String)");
    }
    
    protected synchronized void startTimer() {
        if (this.timeout == 0) {
            return;
        }
        this.killTimerTask();
        this.cancelTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    AbstractJdbc2Statement.this.cancel();
                }
                catch (SQLException ex) {}
            }
        };
        this.connection.addTimerTask(this.cancelTimerTask, this.timeout * 1000);
    }
    
    private synchronized void killTimerTask() {
        if (this.cancelTimerTask != null) {
            this.cancelTimerTask.cancel();
            this.cancelTimerTask = null;
            this.connection.purgeTimerTasks();
        }
    }
    
    protected boolean getForceBinaryTransfer() {
        return this.forceBinaryTransfers;
    }
    
    public class StatementResultHandler implements ResultHandler
    {
        private SQLException error;
        private ResultWrapper results;
        
        ResultWrapper getResults() {
            return this.results;
        }
        
        private void append(final ResultWrapper newResult) {
            if (this.results == null) {
                this.results = newResult;
            }
            else {
                this.results.append(newResult);
            }
        }
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
            try {
                final ResultSet rs = AbstractJdbc2Statement.this.createResultSet(fromQuery, fields, tuples, cursor);
                this.append(new ResultWrapper(rs));
            }
            catch (SQLException e) {
                this.handleError(e);
            }
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
            this.append(new ResultWrapper(updateCount, insertOID));
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            AbstractJdbc2Statement.this.addWarning(warning);
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
    
    private class BatchResultHandler implements ResultHandler
    {
        private BatchUpdateException batchException;
        private int resultIndex;
        private final Query[] queries;
        private final ParameterList[] parameterLists;
        private final int[] updateCounts;
        private final boolean expectGeneratedKeys;
        private ResultSet generatedKeys;
        
        BatchResultHandler(final Query[] queries, final ParameterList[] parameterLists, final int[] updateCounts, final boolean expectGeneratedKeys) {
            this.batchException = null;
            this.resultIndex = 0;
            this.queries = queries;
            this.parameterLists = parameterLists;
            this.updateCounts = updateCounts;
            this.expectGeneratedKeys = expectGeneratedKeys;
        }
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
            if (!this.expectGeneratedKeys) {
                this.handleError(new PSQLException(GT.tr("A result was returned when none was expected."), PSQLState.TOO_MANY_RESULTS));
            }
            else if (this.generatedKeys == null) {
                try {
                    this.generatedKeys = AbstractJdbc2Statement.this.createResultSet(fromQuery, fields, tuples, cursor);
                }
                catch (SQLException e) {
                    this.handleError(e);
                }
            }
            else {
                ((AbstractJdbc2ResultSet)this.generatedKeys).addRows(tuples);
            }
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
            if (this.resultIndex >= this.updateCounts.length) {
                this.handleError(new PSQLException(GT.tr("Too many update results were returned."), PSQLState.TOO_MANY_RESULTS));
                return;
            }
            this.updateCounts[this.resultIndex++] = updateCount;
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            AbstractJdbc2Statement.this.addWarning(warning);
        }
        
        @Override
        public void handleError(final SQLException newError) {
            if (this.batchException == null) {
                int[] successCounts;
                if (this.resultIndex >= this.updateCounts.length) {
                    successCounts = this.updateCounts;
                }
                else {
                    successCounts = new int[this.resultIndex];
                    System.arraycopy(this.updateCounts, 0, successCounts, 0, this.resultIndex);
                }
                String queryString = "<unknown>";
                if (this.resultIndex < this.queries.length) {
                    queryString = this.queries[this.resultIndex].toString(this.parameterLists[this.resultIndex]);
                }
                this.batchException = new BatchUpdateException(GT.tr("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", new Object[] { new Integer(this.resultIndex), queryString }), newError.getSQLState(), successCounts);
            }
            this.batchException.setNextException(newError);
        }
        
        @Override
        public void handleCompletion() throws SQLException {
            if (this.batchException != null) {
                throw this.batchException;
            }
        }
        
        public ResultSet getGeneratedKeys() {
            return this.generatedKeys;
        }
    }
    
    private class CallableBatchResultHandler implements ResultHandler
    {
        private BatchUpdateException batchException;
        private int resultIndex;
        private final Query[] queries;
        private final ParameterList[] parameterLists;
        private final int[] updateCounts;
        
        CallableBatchResultHandler(final Query[] queries, final ParameterList[] parameterLists, final int[] updateCounts) {
            this.batchException = null;
            this.resultIndex = 0;
            this.queries = queries;
            this.parameterLists = parameterLists;
            this.updateCounts = updateCounts;
        }
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
            if (this.resultIndex >= this.updateCounts.length) {
                this.handleError(new PSQLException(GT.tr("Too many update results were returned."), PSQLState.TOO_MANY_RESULTS));
                return;
            }
            this.updateCounts[this.resultIndex++] = updateCount;
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            AbstractJdbc2Statement.this.addWarning(warning);
        }
        
        @Override
        public void handleError(final SQLException newError) {
            if (this.batchException == null) {
                int[] successCounts;
                if (this.resultIndex >= this.updateCounts.length) {
                    successCounts = this.updateCounts;
                }
                else {
                    successCounts = new int[this.resultIndex];
                    System.arraycopy(this.updateCounts, 0, successCounts, 0, this.resultIndex);
                }
                String queryString = "<unknown>";
                if (this.resultIndex < this.queries.length) {
                    queryString = this.queries[this.resultIndex].toString(this.parameterLists[this.resultIndex]);
                }
                this.batchException = new BatchUpdateException(GT.tr("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", new Object[] { new Integer(this.resultIndex), queryString }), newError.getSQLState(), successCounts);
            }
            this.batchException.setNextException(newError);
        }
        
        @Override
        public void handleCompletion() throws SQLException {
            if (this.batchException != null) {
                throw this.batchException;
            }
        }
    }
}
