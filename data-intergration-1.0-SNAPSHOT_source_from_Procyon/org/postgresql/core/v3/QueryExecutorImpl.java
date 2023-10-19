// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.util.PSQLWarning;
import org.postgresql.util.ServerErrorMessage;
import org.postgresql.PGNotification;
import org.postgresql.core.Notification;
import java.lang.ref.PhantomReference;
import org.postgresql.core.Utils;
import org.postgresql.copy.CopyOperation;
import java.sql.SQLWarning;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import java.io.IOException;
import org.postgresql.core.PGBindException;
import java.sql.SQLException;
import org.postgresql.core.ResultHandler;
import org.postgresql.core.ParameterList;
import org.postgresql.core.Parser;
import org.postgresql.core.Query;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.PGProperty;
import java.util.Properties;
import org.postgresql.core.Logger;
import org.postgresql.core.PGStream;
import java.util.ArrayList;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import org.postgresql.core.QueryExecutor;

public class QueryExecutorImpl implements QueryExecutor
{
    private Object lockedFor;
    private static final int MAX_BUFFERED_RECV_BYTES = 64000;
    private static final int NODATA_QUERY_RESPONSE_SIZE_BYTES = 250;
    private final HashMap parsedQueryMap;
    private final ReferenceQueue parsedQueryCleanupQueue;
    private final HashMap openPortalMap;
    private final ReferenceQueue openPortalCleanupQueue;
    private final ArrayList pendingParseQueue;
    private final ArrayList pendingBindQueue;
    private final ArrayList pendingExecuteQueue;
    private final ArrayList pendingDescribeStatementQueue;
    private final ArrayList pendingDescribePortalQueue;
    private long nextUniqueID;
    private final ProtocolConnectionImpl protoConnection;
    private final PGStream pgStream;
    private final Logger logger;
    private final boolean allowEncodingChanges;
    private int estimatedReceiveBufferBytes;
    private final SimpleQuery beginTransactionQuery;
    private final SimpleQuery EMPTY_QUERY;
    
    public QueryExecutorImpl(final ProtocolConnectionImpl protoConnection, final PGStream pgStream, final Properties info, final Logger logger) {
        this.lockedFor = null;
        this.parsedQueryMap = new HashMap();
        this.parsedQueryCleanupQueue = new ReferenceQueue();
        this.openPortalMap = new HashMap();
        this.openPortalCleanupQueue = new ReferenceQueue();
        this.pendingParseQueue = new ArrayList();
        this.pendingBindQueue = new ArrayList();
        this.pendingExecuteQueue = new ArrayList();
        this.pendingDescribeStatementQueue = new ArrayList();
        this.pendingDescribePortalQueue = new ArrayList();
        this.nextUniqueID = 1L;
        this.estimatedReceiveBufferBytes = 0;
        this.beginTransactionQuery = new SimpleQuery(new String[] { "BEGIN" }, null);
        this.EMPTY_QUERY = new SimpleQuery(new String[] { "" }, null);
        this.protoConnection = protoConnection;
        this.pgStream = pgStream;
        this.logger = logger;
        this.allowEncodingChanges = PGProperty.ALLOW_ENCODING_CHANGES.getBoolean(info);
    }
    
    private void lock(final Object obtainer) throws PSQLException {
        if (this.lockedFor == obtainer) {
            throw new PSQLException(GT.tr("Tried to obtain lock while already holding it"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        this.waitOnLock();
        this.lockedFor = obtainer;
    }
    
    private void unlock(final Object holder) throws PSQLException {
        if (this.lockedFor != holder) {
            throw new PSQLException(GT.tr("Tried to break lock on database connection"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        this.lockedFor = null;
        this.notify();
    }
    
    private void waitOnLock() throws PSQLException {
        while (this.lockedFor != null) {
            try {
                this.wait();
                continue;
            }
            catch (InterruptedException ie) {
                throw new PSQLException(GT.tr("Interrupted while waiting to obtain lock on database connection"), PSQLState.OBJECT_NOT_IN_STATE, ie);
            }
            break;
        }
    }
    
    boolean hasLock(final Object holder) {
        return this.lockedFor == holder;
    }
    
    @Override
    public Query createSimpleQuery(final String sql) {
        return this.parseQuery(sql, false);
    }
    
    @Override
    public Query createParameterizedQuery(final String sql) {
        return this.parseQuery(sql, true);
    }
    
    private Query parseQuery(final String query, final boolean withParameters) {
        final ArrayList statementList = new ArrayList();
        final ArrayList fragmentList = new ArrayList(15);
        int fragmentStart = 0;
        int inParen = 0;
        final boolean standardConformingStrings = this.protoConnection.getStandardConformingStrings();
        final char[] aChars = query.toCharArray();
        for (int i = 0; i < aChars.length; ++i) {
            switch (aChars[i]) {
                case '\'': {
                    i = Parser.parseSingleQuotes(aChars, i, standardConformingStrings);
                    break;
                }
                case '\"': {
                    i = Parser.parseDoubleQuotes(aChars, i);
                    break;
                }
                case '-': {
                    i = Parser.parseLineComment(aChars, i);
                    break;
                }
                case '/': {
                    i = Parser.parseBlockComment(aChars, i);
                    break;
                }
                case '$': {
                    i = Parser.parseDollarQuotes(aChars, i);
                    break;
                }
                case '(': {
                    ++inParen;
                    break;
                }
                case ')': {
                    --inParen;
                    break;
                }
                case '?': {
                    if (!withParameters) {
                        break;
                    }
                    if (i + 1 < aChars.length && aChars[i + 1] == '?') {
                        ++i;
                        break;
                    }
                    fragmentList.add(query.substring(fragmentStart, i));
                    fragmentStart = i + 1;
                    break;
                }
                case ';': {
                    if (inParen == 0) {
                        fragmentList.add(query.substring(fragmentStart, i));
                        fragmentStart = i + 1;
                        if (fragmentList.size() > 1 || fragmentList.get(0).trim().length() > 0) {
                            statementList.add(fragmentList.toArray(new String[fragmentList.size()]));
                        }
                        fragmentList.clear();
                        break;
                    }
                    break;
                }
            }
        }
        fragmentList.add(query.substring(fragmentStart));
        if (fragmentList.size() > 1 || fragmentList.get(0).trim().length() > 0) {
            statementList.add(fragmentList.toArray(new String[fragmentList.size()]));
        }
        if (statementList.isEmpty()) {
            return this.EMPTY_QUERY;
        }
        if (statementList.size() == 1) {
            return new SimpleQuery(statementList.get(0), this.protoConnection);
        }
        final SimpleQuery[] subqueries = new SimpleQuery[statementList.size()];
        final int[] offsets = new int[statementList.size()];
        int offset = 0;
        for (int j = 0; j < statementList.size(); ++j) {
            final String[] fragments = statementList.get(j);
            offsets[j] = offset;
            subqueries[j] = new SimpleQuery(fragments, this.protoConnection);
            offset += fragments.length - 1;
        }
        return new CompositeQuery(subqueries, offsets);
    }
    
    @Override
    public synchronized void execute(final Query query, ParameterList parameters, ResultHandler handler, final int maxRows, final int fetchSize, final int flags) throws SQLException {
        this.waitOnLock();
        if (this.logger.logDebug()) {
            this.logger.debug("simple execute, handler=" + handler + ", maxRows=" + maxRows + ", fetchSize=" + fetchSize + ", flags=" + flags);
        }
        if (parameters == null) {
            parameters = SimpleQuery.NO_PARAMETERS;
        }
        final boolean describeOnly = (0x20 & flags) != 0x0;
        ((V3ParameterList)parameters).convertFunctionOutParameters();
        while (true) {
            if (!describeOnly) {
                ((V3ParameterList)parameters).checkAllParametersSet();
                try {
                    try {
                        handler = this.sendQueryPreamble(handler, flags);
                        final ErrorTrackingResultHandler trackingHandler = new ErrorTrackingResultHandler(handler);
                        this.sendQuery((V3Query)query, (V3ParameterList)parameters, maxRows, fetchSize, flags, trackingHandler);
                        this.sendSync();
                        this.processResults(handler, flags);
                        this.estimatedReceiveBufferBytes = 0;
                    }
                    catch (PGBindException se) {
                        this.sendSync();
                        this.processResults(handler, flags);
                        this.estimatedReceiveBufferBytes = 0;
                        handler.handleError(new PSQLException(GT.tr("Unable to bind parameter values for statement."), PSQLState.INVALID_PARAMETER_VALUE, se.getIOException()));
                    }
                }
                catch (IOException e) {
                    this.protoConnection.close();
                    handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, e));
                }
                handler.handleCompletion();
                return;
            }
            continue;
        }
    }
    
    @Override
    public synchronized void execute(final Query[] queries, final ParameterList[] parameterLists, ResultHandler handler, final int maxRows, final int fetchSize, final int flags) throws SQLException {
        this.waitOnLock();
        if (this.logger.logDebug()) {
            this.logger.debug("batch execute " + queries.length + " queries, handler=" + handler + ", maxRows=" + maxRows + ", fetchSize=" + fetchSize + ", flags=" + flags);
        }
        final boolean describeOnly = (0x20 & flags) != 0x0;
        if (!describeOnly) {
            for (int i = 0; i < parameterLists.length; ++i) {
                if (parameterLists[i] != null) {
                    ((V3ParameterList)parameterLists[i]).checkAllParametersSet();
                }
            }
        }
        try {
            handler = this.sendQueryPreamble(handler, flags);
            final ErrorTrackingResultHandler trackingHandler = new ErrorTrackingResultHandler(handler);
            this.estimatedReceiveBufferBytes = 0;
            for (int j = 0; j < queries.length; ++j) {
                final V3Query query = (V3Query)queries[j];
                V3ParameterList parameters = (V3ParameterList)parameterLists[j];
                if (parameters == null) {
                    parameters = SimpleQuery.NO_PARAMETERS;
                }
                this.sendQuery(query, parameters, maxRows, fetchSize, flags, trackingHandler);
                if (trackingHandler.hasErrors()) {
                    break;
                }
            }
            if (!trackingHandler.hasErrors()) {
                this.sendSync();
                this.processResults(handler, flags);
                this.estimatedReceiveBufferBytes = 0;
            }
        }
        catch (IOException e) {
            this.protoConnection.close();
            handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, e));
        }
        handler.handleCompletion();
    }
    
    private ResultHandler sendQueryPreamble(final ResultHandler delegateHandler, final int flags) throws IOException {
        this.processDeadParsedQueries();
        this.processDeadPortals();
        if ((flags & 0x10) != 0x0 || this.protoConnection.getTransactionState() != 0) {
            return delegateHandler;
        }
        int beginFlags = 2;
        if ((flags & 0x1) != 0x0) {
            beginFlags |= 0x1;
        }
        this.sendOneQuery(this.beginTransactionQuery, SimpleQuery.NO_PARAMETERS, 0, 0, beginFlags);
        return new ResultHandler() {
            private boolean sawBegin = false;
            
            @Override
            public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
                if (this.sawBegin) {
                    delegateHandler.handleResultRows(fromQuery, fields, tuples, cursor);
                }
            }
            
            @Override
            public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
                if (!this.sawBegin) {
                    this.sawBegin = true;
                    if (!status.equals("BEGIN")) {
                        this.handleError(new PSQLException(GT.tr("Expected command status BEGIN, got {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                    }
                }
                else {
                    delegateHandler.handleCommandStatus(status, updateCount, insertOID);
                }
            }
            
            @Override
            public void handleWarning(final SQLWarning warning) {
                delegateHandler.handleWarning(warning);
            }
            
            @Override
            public void handleError(final SQLException error) {
                delegateHandler.handleError(error);
            }
            
            @Override
            public void handleCompletion() throws SQLException {
                delegateHandler.handleCompletion();
            }
        };
    }
    
    @Override
    public synchronized byte[] fastpathCall(final int fnid, final ParameterList parameters, final boolean suppressBegin) throws SQLException {
        this.waitOnLock();
        if (!suppressBegin) {
            this.doSubprotocolBegin();
        }
        try {
            this.sendFastpathCall(fnid, (SimpleParameterList)parameters);
            return this.receiveFastpathResult();
        }
        catch (IOException ioe) {
            this.protoConnection.close();
            throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    public void doSubprotocolBegin() throws SQLException {
        if (this.protoConnection.getTransactionState() == 0) {
            if (this.logger.logDebug()) {
                this.logger.debug("Issuing BEGIN before fastpath or copy call.");
            }
            final ResultHandler handler = new ResultHandler() {
                private boolean sawBegin = false;
                private SQLException sqle = null;
                
                @Override
                public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
                }
                
                @Override
                public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
                    if (!this.sawBegin) {
                        if (!status.equals("BEGIN")) {
                            this.handleError(new PSQLException(GT.tr("Expected command status BEGIN, got {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                        }
                        this.sawBegin = true;
                    }
                    else {
                        this.handleError(new PSQLException(GT.tr("Unexpected command status: {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                    }
                }
                
                @Override
                public void handleWarning(final SQLWarning warning) {
                    this.handleError(warning);
                }
                
                @Override
                public void handleError(final SQLException error) {
                    if (this.sqle == null) {
                        this.sqle = error;
                    }
                    else {
                        this.sqle.setNextException(error);
                    }
                }
                
                @Override
                public void handleCompletion() throws SQLException {
                    if (this.sqle != null) {
                        throw this.sqle;
                    }
                }
            };
            try {
                this.sendOneQuery(this.beginTransactionQuery, SimpleQuery.NO_PARAMETERS, 0, 0, 2);
                this.sendSync();
                this.processResults(handler, 0);
                this.estimatedReceiveBufferBytes = 0;
            }
            catch (IOException ioe) {
                throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, ioe);
            }
        }
    }
    
    @Override
    public ParameterList createFastpathParameters(final int count) {
        return new SimpleParameterList(count, this.protoConnection);
    }
    
    private void sendFastpathCall(final int fnid, final SimpleParameterList params) throws SQLException, IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> FunctionCall(" + fnid + ", " + params.getParameterCount() + " params)");
        }
        final int paramCount = params.getParameterCount();
        int encodedSize = 0;
        for (int i = 1; i <= paramCount; ++i) {
            if (params.isNull(i)) {
                encodedSize += 4;
            }
            else {
                encodedSize += 4 + params.getV3Length(i);
            }
        }
        this.pgStream.SendChar(70);
        this.pgStream.SendInteger4(10 + 2 * paramCount + 2 + encodedSize + 2);
        this.pgStream.SendInteger4(fnid);
        this.pgStream.SendInteger2(paramCount);
        for (int i = 1; i <= paramCount; ++i) {
            this.pgStream.SendInteger2(params.isBinary(i) ? 1 : 0);
        }
        this.pgStream.SendInteger2(paramCount);
        for (int i = 1; i <= paramCount; ++i) {
            if (params.isNull(i)) {
                this.pgStream.SendInteger4(-1);
            }
            else {
                this.pgStream.SendInteger4(params.getV3Length(i));
                params.writeV3Value(i, this.pgStream);
            }
        }
        this.pgStream.SendInteger2(1);
        this.pgStream.flush();
    }
    
    @Override
    public synchronized void processNotifies() throws SQLException {
        this.waitOnLock();
        if (this.protoConnection.getTransactionState() != 0) {
            return;
        }
        try {
            while (this.pgStream.hasMessagePending()) {
                final int c = this.pgStream.ReceiveChar();
                switch (c) {
                    case 65: {
                        this.receiveAsyncNotify();
                        continue;
                    }
                    case 69: {
                        throw this.receiveErrorResponse();
                    }
                    case 78: {
                        final SQLWarning warning = this.receiveNoticeResponse();
                        this.protoConnection.addWarning(warning);
                        continue;
                    }
                    default: {
                        throw new PSQLException(GT.tr("Unknown Response Type {0}.", new Character((char)c)), PSQLState.CONNECTION_FAILURE);
                    }
                }
            }
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    private byte[] receiveFastpathResult() throws IOException, SQLException {
        boolean endQuery = false;
        SQLException error = null;
        byte[] returnValue = null;
        while (!endQuery) {
            final int c = this.pgStream.ReceiveChar();
            switch (c) {
                case 65: {
                    this.receiveAsyncNotify();
                    continue;
                }
                case 69: {
                    final SQLException newError = this.receiveErrorResponse();
                    if (error == null) {
                        error = newError;
                        continue;
                    }
                    error.setNextException(newError);
                    continue;
                }
                case 78: {
                    final SQLWarning warning = this.receiveNoticeResponse();
                    this.protoConnection.addWarning(warning);
                    continue;
                }
                case 90: {
                    this.receiveRFQ();
                    endQuery = true;
                    continue;
                }
                case 86: {
                    final int msgLen = this.pgStream.ReceiveInteger4();
                    final int valueLen = this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE FunctionCallResponse(" + valueLen + " bytes)");
                    }
                    if (valueLen != -1) {
                        final byte[] buf = new byte[valueLen];
                        this.pgStream.Receive(buf, 0, valueLen);
                        returnValue = buf;
                        continue;
                    }
                    continue;
                }
                default: {
                    throw new PSQLException(GT.tr("Unknown Response Type {0}.", new Character((char)c)), PSQLState.CONNECTION_FAILURE);
                }
            }
        }
        if (error != null) {
            throw error;
        }
        return returnValue;
    }
    
    @Override
    public synchronized CopyOperation startCopy(final String sql, final boolean suppressBegin) throws SQLException {
        this.waitOnLock();
        if (!suppressBegin) {
            this.doSubprotocolBegin();
        }
        final byte[] buf = Utils.encodeUTF8(sql);
        try {
            if (this.logger.logDebug()) {
                this.logger.debug(" FE=> Query(CopyStart)");
            }
            this.pgStream.SendChar(81);
            this.pgStream.SendInteger4(buf.length + 4 + 1);
            this.pgStream.Send(buf);
            this.pgStream.SendChar(0);
            this.pgStream.flush();
            return this.processCopyResults(null, true);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when starting copy"), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    private synchronized void initCopy(final CopyOperationImpl op) throws SQLException, IOException {
        this.pgStream.ReceiveInteger4();
        final int rowFormat = this.pgStream.ReceiveChar();
        final int numFields = this.pgStream.ReceiveInteger2();
        final int[] fieldFormats = new int[numFields];
        for (int i = 0; i < numFields; ++i) {
            fieldFormats[i] = this.pgStream.ReceiveInteger2();
        }
        this.lock(op);
        op.init(this, rowFormat, fieldFormats);
    }
    
    public void cancelCopy(final CopyOperationImpl op) throws SQLException {
        if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to cancel an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        SQLException error = null;
        int errors = 0;
        try {
            if (op instanceof CopyInImpl) {
                synchronized (this) {
                    if (this.logger.logDebug()) {
                        this.logger.debug("FE => CopyFail");
                    }
                    final byte[] msg = Utils.encodeUTF8("Copy cancel requested");
                    this.pgStream.SendChar(102);
                    this.pgStream.SendInteger4(5 + msg.length);
                    this.pgStream.Send(msg);
                    this.pgStream.SendChar(0);
                    this.pgStream.flush();
                    do {
                        try {
                            this.processCopyResults(op, true);
                        }
                        catch (SQLException se) {
                            ++errors;
                            if (error != null) {
                                SQLException e;
                                SQLException next;
                                for (e = se; (next = e.getNextException()) != null; e = next) {}
                                e.setNextException(error);
                            }
                            error = se;
                        }
                    } while (this.hasLock(op));
                }
            }
            else if (op instanceof CopyOutImpl) {
                this.protoConnection.sendQueryCancel();
            }
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when canceling copy operation"), PSQLState.CONNECTION_FAILURE, ioe);
        }
        if (op instanceof CopyInImpl) {
            if (errors < 1) {
                throw new PSQLException(GT.tr("Missing expected error response to copy cancel request"), PSQLState.COMMUNICATION_ERROR);
            }
            if (errors > 1) {
                throw new PSQLException(GT.tr("Got {0} error responses to single copy cancel request", String.valueOf(errors)), PSQLState.COMMUNICATION_ERROR, error);
            }
        }
    }
    
    public synchronized long endCopy(final CopyInImpl op) throws SQLException {
        if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to end inactive copy"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        try {
            if (this.logger.logDebug()) {
                this.logger.debug(" FE=> CopyDone");
            }
            this.pgStream.SendChar(99);
            this.pgStream.SendInteger4(4);
            this.pgStream.flush();
            this.processCopyResults(op, true);
            return op.getHandledRowCount();
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when ending copy"), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    public synchronized void writeToCopy(final CopyInImpl op, final byte[] data, final int off, final int siz) throws SQLException {
        if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to write to an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> CopyData(" + siz + ")");
        }
        try {
            this.pgStream.SendChar(100);
            this.pgStream.SendInteger4(siz + 4);
            this.pgStream.Send(data, off, siz);
            this.processCopyResults(op, false);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when writing to copy"), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    public synchronized void flushCopy(final CopyInImpl op) throws SQLException {
        if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to write to an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        try {
            this.pgStream.flush();
            this.processCopyResults(op, false);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when writing to copy"), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    synchronized void readFromCopy(final CopyOutImpl op) throws SQLException {
        if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to read from inactive copy"), PSQLState.OBJECT_NOT_IN_STATE);
        }
        try {
            this.processCopyResults(op, true);
        }
        catch (IOException ioe) {
            throw new PSQLException(GT.tr("Database connection failed when reading from copy"), PSQLState.CONNECTION_FAILURE, ioe);
        }
    }
    
    CopyOperationImpl processCopyResults(CopyOperationImpl op, boolean block) throws SQLException, IOException {
        boolean endReceiving = false;
        SQLException error = null;
        SQLException errors = null;
        while (!endReceiving && (block || this.pgStream.hasMessagePending())) {
            if (!block) {
                final int c = this.pgStream.PeekChar();
                if (c == 67) {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CommandStatus, Ignored until CopyDone");
                        break;
                    }
                    break;
                }
            }
            final int c = this.pgStream.ReceiveChar();
            switch (c) {
                case 65: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE Asynchronous Notification while copying");
                    }
                    this.receiveAsyncNotify();
                    break;
                }
                case 78: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE Notification while copying");
                    }
                    this.protoConnection.addWarning(this.receiveNoticeResponse());
                    break;
                }
                case 67: {
                    final String status = this.receiveCommandStatus();
                    try {
                        if (op == null) {
                            throw new PSQLException(GT.tr("Received CommandComplete ''{0}'' without an active copy operation", status), PSQLState.OBJECT_NOT_IN_STATE);
                        }
                        op.handleCommandStatus(status);
                    }
                    catch (SQLException se) {
                        error = se;
                    }
                    block = true;
                    break;
                }
                case 69: {
                    error = this.receiveErrorResponse();
                    block = true;
                    break;
                }
                case 71: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyInResponse");
                    }
                    if (op != null) {
                        error = new PSQLException(GT.tr("Got CopyInResponse from server during an active {0}", op.getClass().getName()), PSQLState.OBJECT_NOT_IN_STATE);
                    }
                    op = new CopyInImpl();
                    this.initCopy(op);
                    endReceiving = true;
                    break;
                }
                case 72: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyOutResponse");
                    }
                    if (op != null) {
                        error = new PSQLException(GT.tr("Got CopyOutResponse from server during an active {0}", op.getClass().getName()), PSQLState.OBJECT_NOT_IN_STATE);
                    }
                    op = new CopyOutImpl();
                    this.initCopy(op);
                    endReceiving = true;
                    break;
                }
                case 100: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyData");
                    }
                    final int len = this.pgStream.ReceiveInteger4() - 4;
                    final byte[] buf = this.pgStream.Receive(len);
                    if (op == null) {
                        error = new PSQLException(GT.tr("Got CopyData without an active copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
                    }
                    else if (!(op instanceof CopyOutImpl)) {
                        error = new PSQLException(GT.tr("Unexpected copydata from server for {0}", op.getClass().getName()), PSQLState.COMMUNICATION_ERROR);
                    }
                    else {
                        ((CopyOutImpl)op).handleCopydata(buf);
                    }
                    endReceiving = true;
                    break;
                }
                case 99: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyDone");
                    }
                    final int len = this.pgStream.ReceiveInteger4() - 4;
                    if (len > 0) {
                        this.pgStream.Receive(len);
                    }
                    if (!(op instanceof CopyOutImpl)) {
                        error = new PSQLException("Got CopyDone while not copying from server", PSQLState.OBJECT_NOT_IN_STATE);
                    }
                    block = true;
                    break;
                }
                case 83: {
                    final int l_len = this.pgStream.ReceiveInteger4();
                    final String name = this.pgStream.ReceiveString();
                    final String value = this.pgStream.ReceiveString();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ParameterStatus(" + name + " = " + value + ")");
                    }
                    if (name.equals("client_encoding") && !value.equalsIgnoreCase("UTF8") && !this.allowEncodingChanges) {
                        this.protoConnection.close();
                        error = new PSQLException(GT.tr("The server''s client_encoding parameter was changed to {0}. The JDBC driver requires client_encoding to be UTF8 for correct operation.", value), PSQLState.CONNECTION_FAILURE);
                        endReceiving = true;
                    }
                    if (name.equals("DateStyle") && !value.startsWith("ISO,")) {
                        this.protoConnection.close();
                        error = new PSQLException(GT.tr("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", value), PSQLState.CONNECTION_FAILURE);
                        endReceiving = true;
                    }
                    if (name.equals("standard_conforming_strings")) {
                        if (value.equals("on")) {
                            this.protoConnection.setStandardConformingStrings(true);
                        }
                        else if (value.equals("off")) {
                            this.protoConnection.setStandardConformingStrings(false);
                        }
                        else {
                            this.protoConnection.close();
                            error = new PSQLException(GT.tr("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", value), PSQLState.CONNECTION_FAILURE);
                            endReceiving = true;
                        }
                    }
                    break;
                }
                case 90: {
                    this.receiveRFQ();
                    if (this.hasLock(op)) {
                        this.unlock(op);
                    }
                    op = null;
                    endReceiving = true;
                    break;
                }
                case 84: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE RowDescription (during copy ignored)");
                    }
                    this.skipMessage();
                    break;
                }
                case 68: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE DataRow (during copy ignored)");
                    }
                    this.skipMessage();
                    break;
                }
                default: {
                    throw new IOException(GT.tr("Unexpected packet type during copy: {0}", Integer.toString(c)));
                }
            }
            if (error != null) {
                if (errors != null) {
                    error.setNextException(errors);
                }
                errors = error;
                error = null;
            }
        }
        if (errors != null) {
            throw errors;
        }
        return op;
    }
    
    private void flushIfDeadlockRisk(final Query query, boolean disallowBatching, final ErrorTrackingResultHandler trackingHandler, final int flags) throws IOException {
        this.estimatedReceiveBufferBytes += 250;
        final SimpleQuery sq = (SimpleQuery)query;
        if (sq.isStatementDescribed()) {
            final int maxResultRowSize = sq.getMaxResultRowSize();
            if (maxResultRowSize >= 0) {
                this.estimatedReceiveBufferBytes += maxResultRowSize;
            }
            else {
                this.logger.debug("Couldn't estimate result size or result size unbounded, disabling batching for this query.");
                disallowBatching = true;
            }
        }
        if (disallowBatching || this.estimatedReceiveBufferBytes >= 64000) {
            this.logger.debug("Forcing Sync, receive buffer full or batching disallowed");
            this.sendSync();
            this.processResults(trackingHandler, flags);
            this.estimatedReceiveBufferBytes = 0;
        }
    }
    
    private void sendQuery(final V3Query query, final V3ParameterList parameters, final int maxRows, final int fetchSize, final int flags, final ErrorTrackingResultHandler trackingHandler) throws IOException, SQLException {
        final SimpleQuery[] subqueries = query.getSubqueries();
        final SimpleParameterList[] subparams = parameters.getSubparams();
        final boolean disallowBatching = (flags & 0x80) != 0x0;
        if (subqueries == null) {
            this.flushIfDeadlockRisk(query, disallowBatching, trackingHandler, flags);
            if (!trackingHandler.hasErrors()) {
                this.sendOneQuery((SimpleQuery)query, (SimpleParameterList)parameters, maxRows, fetchSize, flags);
            }
        }
        else {
            for (int i = 0; i < subqueries.length; ++i) {
                final SimpleQuery subquery = subqueries[i];
                this.flushIfDeadlockRisk(subquery, disallowBatching, trackingHandler, flags);
                if (trackingHandler.hasErrors()) {
                    break;
                }
                SimpleParameterList subparam = SimpleQuery.NO_PARAMETERS;
                if (subparams != null) {
                    subparam = subparams[i];
                }
                this.sendOneQuery(subquery, subparam, maxRows, fetchSize, flags);
            }
        }
    }
    
    private void sendSync() throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> Sync");
        }
        this.pgStream.SendChar(83);
        this.pgStream.SendInteger4(4);
        this.pgStream.flush();
    }
    
    private void sendParse(final SimpleQuery query, final SimpleParameterList params, final boolean oneShot) throws IOException {
        final int[] typeOIDs = params.getTypeOIDs();
        if (query.isPreparedFor(typeOIDs)) {
            return;
        }
        query.unprepare();
        this.processDeadParsedQueries();
        query.setFields(null);
        String statementName = null;
        if (!oneShot) {
            statementName = "S_" + this.nextUniqueID++;
            query.setStatementName(statementName);
            query.setStatementTypes(typeOIDs.clone());
        }
        final byte[] encodedStatementName = query.getEncodedStatementName();
        final String[] fragments = query.getFragments();
        if (this.logger.logDebug()) {
            final StringBuilder sbuf = new StringBuilder(" FE=> Parse(stmt=" + statementName + ",query=\"");
            for (int i = 0; i < fragments.length; ++i) {
                if (i > 0) {
                    sbuf.append("$").append(i);
                }
                sbuf.append(fragments[i]);
            }
            sbuf.append("\",oids={");
            for (int i = 1; i <= params.getParameterCount(); ++i) {
                if (i != 1) {
                    sbuf.append(",");
                }
                sbuf.append(params.getTypeOID(i));
            }
            sbuf.append("})");
            this.logger.debug(sbuf.toString());
        }
        final byte[][] parts = new byte[fragments.length * 2 - 1][];
        int j = 0;
        int encodedSize = 0;
        for (int k = 0; k < fragments.length; ++k) {
            if (k != 0) {
                parts[j] = Utils.encodeUTF8("$" + k);
                encodedSize += parts[j].length;
                ++j;
            }
            parts[j] = Utils.encodeUTF8(fragments[k]);
            encodedSize += parts[j].length;
            ++j;
        }
        encodedSize = 4 + ((encodedStatementName == null) ? 0 : encodedStatementName.length) + 1 + encodedSize + 1 + 2 + 4 * params.getParameterCount();
        this.pgStream.SendChar(80);
        this.pgStream.SendInteger4(encodedSize);
        if (encodedStatementName != null) {
            this.pgStream.Send(encodedStatementName);
        }
        this.pgStream.SendChar(0);
        for (int k = 0; k < parts.length; ++k) {
            this.pgStream.Send(parts[k]);
        }
        this.pgStream.SendChar(0);
        this.pgStream.SendInteger2(params.getParameterCount());
        for (int k = 1; k <= params.getParameterCount(); ++k) {
            this.pgStream.SendInteger4(params.getTypeOID(k));
        }
        this.pendingParseQueue.add(new Object[] { query, query.getStatementName() });
    }
    
    private void sendBind(final SimpleQuery query, final SimpleParameterList params, final Portal portal, final boolean noBinaryTransfer) throws IOException {
        final String statementName = query.getStatementName();
        final byte[] encodedStatementName = query.getEncodedStatementName();
        final byte[] encodedPortalName = (byte[])((portal == null) ? null : portal.getEncodedPortalName());
        if (this.logger.logDebug()) {
            final StringBuilder sbuf = new StringBuilder(" FE=> Bind(stmt=" + statementName + ",portal=" + portal);
            for (int i = 1; i <= params.getParameterCount(); ++i) {
                sbuf.append(",$").append(i).append("=<").append(params.toString(i)).append(">");
            }
            sbuf.append(")");
            this.logger.debug(sbuf.toString());
        }
        long encodedSize = 0L;
        for (int j = 1; j <= params.getParameterCount(); ++j) {
            if (params.isNull(j)) {
                encodedSize += 4L;
            }
            else {
                encodedSize += 4L + params.getV3Length(j);
            }
        }
        int numBinaryFields = 0;
        final Field[] fields = query.getFields();
        if (!noBinaryTransfer && fields != null) {
            for (int k = 0; k < fields.length; ++k) {
                if (this.useBinary(fields[k])) {
                    fields[k].setFormat(1);
                    numBinaryFields = fields.length;
                }
            }
        }
        encodedSize = 4 + ((encodedPortalName == null) ? 0 : encodedPortalName.length) + 1 + ((encodedStatementName == null) ? 0 : encodedStatementName.length) + 1 + 2 + params.getParameterCount() * 2 + 2 + encodedSize + 2L + numBinaryFields * 2;
        if (encodedSize > 1073741823L) {
            throw new PGBindException(new IOException(GT.tr("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", new Long(encodedSize))));
        }
        this.pgStream.SendChar(66);
        this.pgStream.SendInteger4((int)encodedSize);
        if (encodedPortalName != null) {
            this.pgStream.Send(encodedPortalName);
        }
        this.pgStream.SendChar(0);
        if (encodedStatementName != null) {
            this.pgStream.Send(encodedStatementName);
        }
        this.pgStream.SendChar(0);
        this.pgStream.SendInteger2(params.getParameterCount());
        for (int k = 1; k <= params.getParameterCount(); ++k) {
            this.pgStream.SendInteger2(params.isBinary(k) ? 1 : 0);
        }
        this.pgStream.SendInteger2(params.getParameterCount());
        PGBindException bindException = null;
        for (int l = 1; l <= params.getParameterCount(); ++l) {
            if (params.isNull(l)) {
                this.pgStream.SendInteger4(-1);
            }
            else {
                this.pgStream.SendInteger4(params.getV3Length(l));
                try {
                    params.writeV3Value(l, this.pgStream);
                }
                catch (PGBindException be) {
                    bindException = be;
                }
            }
        }
        this.pgStream.SendInteger2(numBinaryFields);
        for (int l = 0; l < numBinaryFields; ++l) {
            this.pgStream.SendInteger2(fields[l].getFormat());
        }
        this.pendingBindQueue.add(portal);
        if (bindException != null) {
            throw bindException;
        }
    }
    
    private boolean useBinary(final Field field) {
        final int oid = field.getOID();
        return this.protoConnection.useBinaryForReceive(oid);
    }
    
    private void sendDescribePortal(final SimpleQuery query, final Portal portal) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> Describe(portal=" + portal + ")");
        }
        final byte[] encodedPortalName = (byte[])((portal == null) ? null : portal.getEncodedPortalName());
        final int encodedSize = 5 + ((encodedPortalName == null) ? 0 : encodedPortalName.length) + 1;
        this.pgStream.SendChar(68);
        this.pgStream.SendInteger4(encodedSize);
        this.pgStream.SendChar(80);
        if (encodedPortalName != null) {
            this.pgStream.Send(encodedPortalName);
        }
        this.pgStream.SendChar(0);
        this.pendingDescribePortalQueue.add(query);
        query.setPortalDescribed(true);
    }
    
    private void sendDescribeStatement(final SimpleQuery query, final SimpleParameterList params, final boolean describeOnly) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> Describe(statement=" + query.getStatementName() + ")");
        }
        final byte[] encodedStatementName = query.getEncodedStatementName();
        final int encodedSize = 5 + ((encodedStatementName == null) ? 0 : encodedStatementName.length) + 1;
        this.pgStream.SendChar(68);
        this.pgStream.SendInteger4(encodedSize);
        this.pgStream.SendChar(83);
        if (encodedStatementName != null) {
            this.pgStream.Send(encodedStatementName);
        }
        this.pgStream.SendChar(0);
        this.pendingDescribeStatementQueue.add(new Object[] { query, params, new Boolean(describeOnly), query.getStatementName() });
        this.pendingDescribePortalQueue.add(query);
        query.setStatementDescribed(true);
        query.setPortalDescribed(true);
    }
    
    private void sendExecute(final SimpleQuery query, final Portal portal, final int limit) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> Execute(portal=" + portal + ",limit=" + limit + ")");
        }
        final byte[] encodedPortalName = (byte[])((portal == null) ? null : portal.getEncodedPortalName());
        final int encodedSize = (encodedPortalName == null) ? 0 : encodedPortalName.length;
        this.pgStream.SendChar(69);
        this.pgStream.SendInteger4(5 + encodedSize + 4);
        if (encodedPortalName != null) {
            this.pgStream.Send(encodedPortalName);
        }
        this.pgStream.SendChar(0);
        this.pgStream.SendInteger4(limit);
        this.pendingExecuteQueue.add(new Object[] { query, portal });
    }
    
    private void sendClosePortal(final String portalName) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> ClosePortal(" + portalName + ")");
        }
        final byte[] encodedPortalName = (byte[])((portalName == null) ? null : Utils.encodeUTF8(portalName));
        final int encodedSize = (encodedPortalName == null) ? 0 : encodedPortalName.length;
        this.pgStream.SendChar(67);
        this.pgStream.SendInteger4(6 + encodedSize);
        this.pgStream.SendChar(80);
        if (encodedPortalName != null) {
            this.pgStream.Send(encodedPortalName);
        }
        this.pgStream.SendChar(0);
    }
    
    private void sendCloseStatement(final String statementName) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> CloseStatement(" + statementName + ")");
        }
        final byte[] encodedStatementName = Utils.encodeUTF8(statementName);
        this.pgStream.SendChar(67);
        this.pgStream.SendInteger4(5 + encodedStatementName.length + 1);
        this.pgStream.SendChar(83);
        this.pgStream.Send(encodedStatementName);
        this.pgStream.SendChar(0);
    }
    
    private void sendOneQuery(final SimpleQuery query, final SimpleParameterList params, final int maxRows, final int fetchSize, final int flags) throws IOException {
        final boolean noResults = (flags & 0x4) != 0x0;
        final boolean noMeta = (flags & 0x2) != 0x0;
        final boolean describeOnly = (flags & 0x20) != 0x0;
        final boolean usePortal = (flags & 0x8) != 0x0 && !noResults && !noMeta && fetchSize > 0 && !describeOnly;
        final boolean oneShot = (flags & 0x1) != 0x0 && !usePortal;
        final boolean noBinaryTransfer = (flags & 0x100) != 0x0;
        int rows;
        if (noResults) {
            rows = 1;
        }
        else if (!usePortal) {
            rows = maxRows;
        }
        else if (maxRows != 0 && fetchSize > maxRows) {
            rows = maxRows;
        }
        else {
            rows = fetchSize;
        }
        this.sendParse(query, params, oneShot);
        final boolean queryHasUnknown = query.hasUnresolvedTypes();
        final boolean paramsHasUnknown = params.hasUnresolvedTypes();
        final boolean describeStatement = describeOnly || (!oneShot && paramsHasUnknown && queryHasUnknown && !query.isStatementDescribed());
        if (!describeStatement && paramsHasUnknown && !queryHasUnknown) {
            final int[] queryOIDs = query.getStatementTypes();
            final int[] paramOIDs = params.getTypeOIDs();
            for (int i = 0; i < paramOIDs.length; ++i) {
                if (paramOIDs[i] == 0) {
                    params.setResolvedType(i + 1, queryOIDs[i]);
                }
            }
        }
        if (describeStatement) {
            this.sendDescribeStatement(query, params, describeOnly);
            if (describeOnly) {
                return;
            }
        }
        Portal portal = null;
        if (usePortal) {
            final String portalName = "C_" + this.nextUniqueID++;
            portal = new Portal(query, portalName);
        }
        this.sendBind(query, params, portal, noBinaryTransfer);
        if (!noMeta && !describeStatement && query.getFields() == null) {
            this.sendDescribePortal(query, portal);
        }
        this.sendExecute(query, portal, rows);
    }
    
    private void registerParsedQuery(final SimpleQuery query, final String statementName) {
        if (statementName == null) {
            return;
        }
        final PhantomReference cleanupRef = new PhantomReference((T)query, this.parsedQueryCleanupQueue);
        this.parsedQueryMap.put(cleanupRef, statementName);
        query.setCleanupRef(cleanupRef);
    }
    
    private void processDeadParsedQueries() throws IOException {
        PhantomReference deadQuery;
        while ((deadQuery = (PhantomReference)this.parsedQueryCleanupQueue.poll()) != null) {
            final String statementName = this.parsedQueryMap.remove(deadQuery);
            this.sendCloseStatement(statementName);
            deadQuery.clear();
        }
    }
    
    private void registerOpenPortal(final Portal portal) {
        if (portal == null) {
            return;
        }
        final String portalName = portal.getPortalName();
        final PhantomReference cleanupRef = new PhantomReference((T)portal, this.openPortalCleanupQueue);
        this.openPortalMap.put(cleanupRef, portalName);
        portal.setCleanupRef(cleanupRef);
    }
    
    private void processDeadPortals() throws IOException {
        PhantomReference deadPortal;
        while ((deadPortal = (PhantomReference)this.openPortalCleanupQueue.poll()) != null) {
            final String portalName = this.openPortalMap.remove(deadPortal);
            this.sendClosePortal(portalName);
            deadPortal.clear();
        }
    }
    
    protected void processResults(final ResultHandler handler, final int flags) throws IOException {
        final boolean noResults = (flags & 0x4) != 0x0;
        final boolean bothRowsAndStatus = (flags & 0x40) != 0x0;
        List tuples = null;
        boolean endQuery = false;
        boolean doneAfterRowDescNoData = false;
        int parseIndex = 0;
        int describeIndex = 0;
        int describePortalIndex = 0;
        int bindIndex = 0;
        int executeIndex = 0;
        while (!endQuery) {
            final int c = this.pgStream.ReceiveChar();
            switch (c) {
                case 65: {
                    this.receiveAsyncNotify();
                    continue;
                }
                case 49: {
                    this.pgStream.ReceiveInteger4();
                    final Object[] parsedQueryAndStatement = this.pendingParseQueue.get(parseIndex++);
                    final SimpleQuery parsedQuery = (SimpleQuery)parsedQueryAndStatement[0];
                    final String parsedStatementName = (String)parsedQueryAndStatement[1];
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ParseComplete [" + parsedStatementName + "]");
                    }
                    this.registerParsedQuery(parsedQuery, parsedStatementName);
                    continue;
                }
                case 116: {
                    this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ParameterDescription");
                    }
                    final Object[] describeData = this.pendingDescribeStatementQueue.get(describeIndex);
                    final SimpleQuery query = (SimpleQuery)describeData[0];
                    final SimpleParameterList params = (SimpleParameterList)describeData[1];
                    final boolean describeOnly = (boolean)describeData[2];
                    final String origStatementName = (String)describeData[3];
                    for (int numParams = this.pgStream.ReceiveInteger2(), i = 1; i <= numParams; ++i) {
                        final int typeOid = this.pgStream.ReceiveInteger4();
                        params.setResolvedType(i, typeOid);
                    }
                    if ((origStatementName == null && query.getStatementName() == null) || (origStatementName != null && origStatementName.equals(query.getStatementName()))) {
                        query.setStatementTypes(params.getTypeOIDs().clone());
                    }
                    if (describeOnly) {
                        doneAfterRowDescNoData = true;
                    }
                    else {
                        ++describeIndex;
                    }
                    continue;
                }
                case 50: {
                    this.pgStream.ReceiveInteger4();
                    final Portal boundPortal = this.pendingBindQueue.get(bindIndex++);
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE BindComplete [" + boundPortal + "]");
                    }
                    this.registerOpenPortal(boundPortal);
                    continue;
                }
                case 51: {
                    this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CloseComplete");
                        continue;
                    }
                    continue;
                }
                case 110: {
                    this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE NoData");
                    }
                    ++describePortalIndex;
                    if (doneAfterRowDescNoData) {
                        final Object[] describeData2 = this.pendingDescribeStatementQueue.get(describeIndex++);
                        final SimpleQuery currentQuery = (SimpleQuery)describeData2[0];
                        final Field[] fields = currentQuery.getFields();
                        if (fields == null) {
                            continue;
                        }
                        tuples = new ArrayList();
                        handler.handleResultRows(currentQuery, fields, tuples, null);
                        tuples = null;
                        continue;
                    }
                    continue;
                }
                case 115: {
                    this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE PortalSuspended");
                    }
                    final Object[] executeData = this.pendingExecuteQueue.get(executeIndex++);
                    final SimpleQuery currentQuery = (SimpleQuery)executeData[0];
                    final Portal currentPortal = (Portal)executeData[1];
                    final Field[] fields2 = currentQuery.getFields();
                    if (fields2 != null && !noResults && tuples == null) {
                        tuples = new ArrayList();
                    }
                    handler.handleResultRows(currentQuery, fields2, tuples, currentPortal);
                    tuples = null;
                    continue;
                }
                case 67: {
                    final String status = this.receiveCommandStatus();
                    doneAfterRowDescNoData = false;
                    final Object[] executeData2 = this.pendingExecuteQueue.get(executeIndex++);
                    final SimpleQuery currentQuery2 = (SimpleQuery)executeData2[0];
                    final Portal currentPortal2 = (Portal)executeData2[1];
                    final Field[] fields3 = currentQuery2.getFields();
                    if (fields3 != null && !noResults && tuples == null) {
                        tuples = new ArrayList();
                    }
                    if (fields3 == null && tuples != null) {
                        throw new IllegalStateException("Received resultset tuples, but no field structure for them");
                    }
                    if (fields3 != null || tuples != null) {
                        handler.handleResultRows(currentQuery2, fields3, tuples, null);
                        tuples = null;
                        if (bothRowsAndStatus) {
                            this.interpretCommandStatus(status, handler);
                        }
                    }
                    else {
                        this.interpretCommandStatus(status, handler);
                    }
                    if (currentPortal2 == null) {
                        continue;
                    }
                    currentPortal2.close();
                    continue;
                }
                case 68: {
                    byte[][] tuple = null;
                    try {
                        tuple = this.pgStream.ReceiveTupleV3();
                    }
                    catch (OutOfMemoryError oome) {
                        if (!noResults) {
                            handler.handleError(new PSQLException(GT.tr("Ran out of memory retrieving query results."), PSQLState.OUT_OF_MEMORY, oome));
                        }
                    }
                    if (!noResults) {
                        if (tuples == null) {
                            tuples = new ArrayList();
                        }
                        tuples.add(tuple);
                    }
                    if (this.logger.logDebug()) {
                        int length;
                        if (tuple == null) {
                            length = -1;
                        }
                        else {
                            length = 0;
                            for (int j = 0; j < tuple.length; ++j) {
                                if (tuple[j] != null) {
                                    length += tuple[j].length;
                                }
                            }
                        }
                        this.logger.debug(" <=BE DataRow(len=" + length + ")");
                        continue;
                    }
                    continue;
                }
                case 69: {
                    final SQLException error = this.receiveErrorResponse();
                    handler.handleError(error);
                    continue;
                }
                case 73: {
                    this.pgStream.ReceiveInteger4();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE EmptyQuery");
                    }
                    final Object[] executeData3 = this.pendingExecuteQueue.get(executeIndex++);
                    final Query currentQuery3 = (Query)executeData3[0];
                    final Portal currentPortal3 = (Portal)executeData3[1];
                    handler.handleCommandStatus("EMPTY", 0, 0L);
                    if (currentPortal3 == null) {
                        continue;
                    }
                    currentPortal3.close();
                    continue;
                }
                case 78: {
                    final SQLWarning warning = this.receiveNoticeResponse();
                    handler.handleWarning(warning);
                    continue;
                }
                case 83: {
                    final int l_len = this.pgStream.ReceiveInteger4();
                    final String name = this.pgStream.ReceiveString();
                    final String value = this.pgStream.ReceiveString();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ParameterStatus(" + name + " = " + value + ")");
                    }
                    if (name.equals("client_encoding") && !value.equalsIgnoreCase("UTF8") && !this.allowEncodingChanges) {
                        this.protoConnection.close();
                        handler.handleError(new PSQLException(GT.tr("The server''s client_encoding parameter was changed to {0}. The JDBC driver requires client_encoding to be UTF8 for correct operation.", value), PSQLState.CONNECTION_FAILURE));
                        endQuery = true;
                    }
                    if (name.equals("DateStyle") && !value.startsWith("ISO,")) {
                        this.protoConnection.close();
                        handler.handleError(new PSQLException(GT.tr("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", value), PSQLState.CONNECTION_FAILURE));
                        endQuery = true;
                    }
                    if (!name.equals("standard_conforming_strings")) {
                        continue;
                    }
                    if (value.equals("on")) {
                        this.protoConnection.setStandardConformingStrings(true);
                    }
                    else if (value.equals("off")) {
                        this.protoConnection.setStandardConformingStrings(false);
                    }
                    else {
                        this.protoConnection.close();
                        handler.handleError(new PSQLException(GT.tr("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", value), PSQLState.CONNECTION_FAILURE));
                        endQuery = true;
                    }
                    continue;
                }
                case 84: {
                    final Field[] fields3 = this.receiveFields();
                    tuples = new ArrayList();
                    final SimpleQuery query2 = this.pendingDescribePortalQueue.get(describePortalIndex++);
                    query2.setFields(fields3);
                    if (doneAfterRowDescNoData) {
                        final Object[] describeData3 = this.pendingDescribeStatementQueue.get(describeIndex++);
                        final SimpleQuery currentQuery4 = (SimpleQuery)describeData3[0];
                        currentQuery4.setFields(fields3);
                        handler.handleResultRows(currentQuery4, fields3, tuples, null);
                        tuples = null;
                        continue;
                    }
                    continue;
                }
                case 90: {
                    this.receiveRFQ();
                    endQuery = true;
                    while (parseIndex < this.pendingParseQueue.size()) {
                        final Object[] failedQueryAndStatement = this.pendingParseQueue.get(parseIndex++);
                        final SimpleQuery failedQuery = (SimpleQuery)failedQueryAndStatement[0];
                        failedQuery.unprepare();
                    }
                    this.pendingParseQueue.clear();
                    this.pendingDescribeStatementQueue.clear();
                    this.pendingDescribePortalQueue.clear();
                    this.pendingBindQueue.clear();
                    this.pendingExecuteQueue.clear();
                    continue;
                }
                case 71: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyInResponse");
                        this.logger.debug(" FE=> CopyFail");
                    }
                    final byte[] buf = Utils.encodeUTF8("The JDBC driver currently does not support COPY operations.");
                    this.pgStream.SendChar(102);
                    this.pgStream.SendInteger4(buf.length + 4 + 1);
                    this.pgStream.Send(buf);
                    this.pgStream.SendChar(0);
                    this.pgStream.flush();
                    this.sendSync();
                    this.skipMessage();
                    continue;
                }
                case 72: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyOutResponse");
                    }
                    this.skipMessage();
                    handler.handleError(new PSQLException(GT.tr("The driver currently does not support COPY operations."), PSQLState.NOT_IMPLEMENTED));
                    continue;
                }
                case 99: {
                    this.skipMessage();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyDone");
                        continue;
                    }
                    continue;
                }
                case 100: {
                    this.skipMessage();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CopyData");
                        continue;
                    }
                    continue;
                }
                default: {
                    throw new IOException("Unexpected packet type: " + c);
                }
            }
        }
    }
    
    private void skipMessage() throws IOException {
        final int l_len = this.pgStream.ReceiveInteger4();
        this.pgStream.Skip(l_len - 4);
    }
    
    @Override
    public synchronized void fetch(final ResultCursor cursor, ResultHandler handler, final int fetchSize) throws SQLException {
        this.waitOnLock();
        final Portal portal = (Portal)cursor;
        final ResultHandler delegateHandler = handler;
        handler = new ResultHandler() {
            @Override
            public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
                delegateHandler.handleResultRows(fromQuery, fields, tuples, cursor);
            }
            
            @Override
            public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
                this.handleResultRows(portal.getQuery(), null, new ArrayList(), null);
            }
            
            @Override
            public void handleWarning(final SQLWarning warning) {
                delegateHandler.handleWarning(warning);
            }
            
            @Override
            public void handleError(final SQLException error) {
                delegateHandler.handleError(error);
            }
            
            @Override
            public void handleCompletion() throws SQLException {
                delegateHandler.handleCompletion();
            }
        };
        try {
            this.processDeadParsedQueries();
            this.processDeadPortals();
            this.sendExecute(portal.getQuery(), portal, fetchSize);
            this.sendSync();
            this.processResults(handler, 0);
            this.estimatedReceiveBufferBytes = 0;
        }
        catch (IOException e) {
            this.protoConnection.close();
            handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, e));
        }
        handler.handleCompletion();
    }
    
    private Field[] receiveFields() throws IOException {
        final int l_msgSize = this.pgStream.ReceiveInteger4();
        final int size = this.pgStream.ReceiveInteger2();
        final Field[] fields = new Field[size];
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE RowDescription(" + size + ")");
        }
        for (int i = 0; i < fields.length; ++i) {
            final String columnLabel = this.pgStream.ReceiveString();
            final int tableOid = this.pgStream.ReceiveInteger4();
            final short positionInTable = (short)this.pgStream.ReceiveInteger2();
            final int typeOid = this.pgStream.ReceiveInteger4();
            final int typeLength = this.pgStream.ReceiveInteger2();
            final int typeModifier = this.pgStream.ReceiveInteger4();
            final int formatType = this.pgStream.ReceiveInteger2();
            (fields[i] = new Field(columnLabel, "", typeOid, typeLength, typeModifier, tableOid, positionInTable)).setFormat(formatType);
            if (this.logger.logDebug()) {
                this.logger.debug("        " + fields[i]);
            }
        }
        return fields;
    }
    
    private void receiveAsyncNotify() throws IOException {
        final int msglen = this.pgStream.ReceiveInteger4();
        final int pid = this.pgStream.ReceiveInteger4();
        final String msg = this.pgStream.ReceiveString();
        final String param = this.pgStream.ReceiveString();
        this.protoConnection.addNotification(new Notification(msg, pid, param));
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE AsyncNotify(" + pid + "," + msg + "," + param + ")");
        }
    }
    
    private SQLException receiveErrorResponse() throws IOException {
        final int elen = this.pgStream.ReceiveInteger4();
        final String totalMessage = this.pgStream.ReceiveString(elen - 4);
        final ServerErrorMessage errorMsg = new ServerErrorMessage(totalMessage, this.logger.getLogLevel());
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE ErrorMessage(" + errorMsg.toString() + ")");
        }
        return new PSQLException(errorMsg);
    }
    
    private SQLWarning receiveNoticeResponse() throws IOException {
        final int nlen = this.pgStream.ReceiveInteger4();
        final ServerErrorMessage warnMsg = new ServerErrorMessage(this.pgStream.ReceiveString(nlen - 4), this.logger.getLogLevel());
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE NoticeResponse(" + warnMsg.toString() + ")");
        }
        return new PSQLWarning(warnMsg);
    }
    
    private String receiveCommandStatus() throws IOException {
        final int l_len = this.pgStream.ReceiveInteger4();
        final String status = this.pgStream.ReceiveString(l_len - 5);
        this.pgStream.Receive(1);
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE CommandStatus(" + status + ")");
        }
        return status;
    }
    
    private void interpretCommandStatus(final String status, final ResultHandler handler) {
        int update_count = 0;
        long insert_oid = 0L;
        Label_0143: {
            if (!status.startsWith("INSERT") && !status.startsWith("UPDATE") && !status.startsWith("DELETE")) {
                if (!status.startsWith("MOVE")) {
                    break Label_0143;
                }
            }
            try {
                final long updates = Long.parseLong(status.substring(1 + status.lastIndexOf(32)));
                if (updates > 2147483647L) {
                    update_count = -2;
                }
                else {
                    update_count = (int)updates;
                }
                if (status.startsWith("INSERT")) {
                    insert_oid = Long.parseLong(status.substring(1 + status.indexOf(32), status.lastIndexOf(32)));
                }
            }
            catch (NumberFormatException nfe) {
                handler.handleError(new PSQLException(GT.tr("Unable to interpret the update count in command completion tag: {0}.", status), PSQLState.CONNECTION_FAILURE));
                return;
            }
        }
        handler.handleCommandStatus(status, update_count, insert_oid);
    }
    
    private void receiveRFQ() throws IOException {
        if (this.pgStream.ReceiveInteger4() != 5) {
            throw new IOException("unexpected length of ReadyForQuery message");
        }
        final char tStatus = (char)this.pgStream.ReceiveChar();
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE ReadyForQuery(" + tStatus + ")");
        }
        switch (tStatus) {
            case 'I': {
                this.protoConnection.setTransactionState(0);
                break;
            }
            case 'T': {
                this.protoConnection.setTransactionState(1);
                break;
            }
            case 'E': {
                this.protoConnection.setTransactionState(2);
                break;
            }
            default: {
                throw new IOException("unexpected transaction state in ReadyForQuery message: " + (int)tStatus);
            }
        }
    }
    
    private static class ErrorTrackingResultHandler implements ResultHandler
    {
        private final ResultHandler delegateHandler;
        private boolean sawError;
        
        ErrorTrackingResultHandler(final ResultHandler delegateHandler) {
            this.sawError = false;
            this.delegateHandler = delegateHandler;
        }
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
            this.delegateHandler.handleResultRows(fromQuery, fields, tuples, cursor);
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
            this.delegateHandler.handleCommandStatus(status, updateCount, insertOID);
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
            this.delegateHandler.handleWarning(warning);
        }
        
        @Override
        public void handleError(final SQLException error) {
            this.sawError = true;
            this.delegateHandler.handleError(error);
        }
        
        @Override
        public void handleCompletion() throws SQLException {
            this.delegateHandler.handleCompletion();
        }
        
        boolean hasErrors() {
            return this.sawError;
        }
    }
}
