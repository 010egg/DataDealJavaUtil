// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v2;

import org.postgresql.copy.CopyOperation;
import org.postgresql.PGNotification;
import org.postgresql.core.Notification;
import java.util.ArrayList;
import java.io.Writer;
import org.postgresql.Driver;
import java.io.IOException;
import java.sql.SQLWarning;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import java.sql.SQLException;
import org.postgresql.core.ResultHandler;
import org.postgresql.core.ParameterList;
import org.postgresql.core.ProtocolConnection;
import org.postgresql.core.Query;
import org.postgresql.core.Logger;
import org.postgresql.core.PGStream;
import org.postgresql.core.QueryExecutor;

public class QueryExecutorImpl implements QueryExecutor
{
    private final ProtocolConnectionImpl protoConnection;
    private final PGStream pgStream;
    private final Logger logger;
    
    public QueryExecutorImpl(final ProtocolConnectionImpl protoConnection, final PGStream pgStream, final Logger logger) {
        this.protoConnection = protoConnection;
        this.pgStream = pgStream;
        this.logger = logger;
    }
    
    @Override
    public Query createSimpleQuery(final String sql) {
        return new V2Query(sql, false, this.protoConnection);
    }
    
    @Override
    public Query createParameterizedQuery(final String sql) {
        return new V2Query(sql, true, this.protoConnection);
    }
    
    @Override
    public ParameterList createFastpathParameters(final int count) {
        return new FastpathParameterList(count);
    }
    
    @Override
    public synchronized byte[] fastpathCall(final int fnid, final ParameterList parameters, final boolean suppressBegin) throws SQLException {
        if (this.protoConnection.getTransactionState() == 0 && !suppressBegin) {
            if (this.logger.logDebug()) {
                this.logger.debug("Issuing BEGIN before fastpath call.");
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
                final V2Query query = (V2Query)this.createSimpleQuery("");
                final SimpleParameterList params = (SimpleParameterList)query.createParameterList();
                this.sendQuery(query, params, "BEGIN");
                this.processResults(query, handler, 0, 0);
            }
            catch (IOException ioe) {
                throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, ioe);
            }
        }
        try {
            this.sendFastpathCall(fnid, (FastpathParameterList)parameters);
            return this.receiveFastpathResult();
        }
        catch (IOException ioe2) {
            throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, ioe2);
        }
    }
    
    private void sendFastpathCall(final int fnid, final FastpathParameterList params) throws IOException {
        final int count = params.getParameterCount();
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> FastpathCall(fnid=" + fnid + ",paramCount=" + count + ")");
        }
        this.pgStream.SendChar(70);
        this.pgStream.SendChar(0);
        this.pgStream.SendInteger4(fnid);
        this.pgStream.SendInteger4(count);
        for (int i = 1; i <= count; ++i) {
            params.writeV2FastpathValue(i, this.pgStream);
        }
        this.pgStream.flush();
    }
    
    @Override
    public synchronized void processNotifies() throws SQLException {
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
                        throw this.receiveErrorMessage();
                    }
                    case 78: {
                        this.protoConnection.addWarning(this.receiveNotification());
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
        SQLException error = null;
        boolean endQuery = false;
        byte[] result = null;
        while (!endQuery) {
            int c = this.pgStream.ReceiveChar();
            switch (c) {
                case 65: {
                    this.receiveAsyncNotify();
                    continue;
                }
                case 69: {
                    final SQLException newError = this.receiveErrorMessage();
                    if (error == null) {
                        error = newError;
                        continue;
                    }
                    error.setNextException(newError);
                    continue;
                }
                case 78: {
                    this.protoConnection.addWarning(this.receiveNotification());
                    continue;
                }
                case 86: {
                    c = this.pgStream.ReceiveChar();
                    if (c == 71) {
                        if (this.logger.logDebug()) {
                            this.logger.debug(" <=BE FastpathResult");
                        }
                        final int len = this.pgStream.ReceiveInteger4();
                        result = this.pgStream.Receive(len);
                        c = this.pgStream.ReceiveChar();
                    }
                    else if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE FastpathVoidResult");
                    }
                    if (c != 48) {
                        throw new PSQLException(GT.tr("Unknown Response Type {0}.", new Character((char)c)), PSQLState.CONNECTION_FAILURE);
                    }
                    continue;
                }
                case 90: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ReadyForQuery");
                    }
                    endQuery = true;
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
        return result;
    }
    
    @Override
    public synchronized void execute(final Query query, final ParameterList parameters, final ResultHandler handler, final int maxRows, final int fetchSize, final int flags) throws SQLException {
        this.execute((V2Query)query, (SimpleParameterList)parameters, handler, maxRows, flags);
    }
    
    @Override
    public synchronized void execute(final Query[] queries, final ParameterList[] parameters, ResultHandler handler, final int maxRows, final int fetchSize, final int flags) throws SQLException {
        final ResultHandler delegateHandler = handler;
        handler = new ResultHandler() {
            @Override
            public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
                delegateHandler.handleResultRows(fromQuery, fields, tuples, cursor);
            }
            
            @Override
            public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
                delegateHandler.handleCommandStatus(status, updateCount, insertOID);
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
            }
        };
        for (int i = 0; i < queries.length; ++i) {
            this.execute((V2Query)queries[i], (SimpleParameterList)parameters[i], handler, maxRows, flags);
        }
        delegateHandler.handleCompletion();
    }
    
    @Override
    public void fetch(final ResultCursor cursor, final ResultHandler handler, final int rows) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "fetch(ResultCursor,ResultHandler,int)");
    }
    
    private void execute(final V2Query query, SimpleParameterList parameters, ResultHandler handler, final int maxRows, final int flags) throws SQLException {
        if ((flags & 0x20) != 0x0) {
            return;
        }
        if (parameters == null) {
            parameters = (SimpleParameterList)query.createParameterList();
        }
        parameters.checkAllParametersSet();
        String queryPrefix = null;
        if (this.protoConnection.getTransactionState() == 0 && (flags & 0x10) == 0x0) {
            queryPrefix = "BEGIN;";
            final ResultHandler delegateHandler = handler;
            handler = new ResultHandler() {
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
                        if (!status.equals("BEGIN")) {
                            this.handleError(new PSQLException(GT.tr("Expected command status BEGIN, got {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                        }
                        this.sawBegin = true;
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
        try {
            this.sendQuery(query, parameters, queryPrefix);
            this.processResults(query, handler, maxRows, flags);
        }
        catch (IOException e) {
            this.protoConnection.close();
            handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, e));
        }
        handler.handleCompletion();
    }
    
    protected void sendQuery(final V2Query query, final SimpleParameterList params, final String queryPrefix) throws IOException {
        if (this.logger.logDebug()) {
            this.logger.debug(" FE=> Query(\"" + ((queryPrefix == null) ? "" : queryPrefix) + query.toString(params) + "\")");
        }
        this.pgStream.SendChar(81);
        final Writer encodingWriter = this.pgStream.getEncodingWriter();
        if (queryPrefix != null) {
            encodingWriter.write(queryPrefix);
        }
        final String[] fragments = query.getFragments();
        for (int i = 0; i < fragments.length; ++i) {
            encodingWriter.write(fragments[i]);
            if (i < params.getParameterCount()) {
                params.writeV2Value(i + 1, encodingWriter);
            }
        }
        encodingWriter.write(0);
        this.pgStream.flush();
    }
    
    protected void processResults(final Query originalQuery, final ResultHandler handler, final int maxRows, final int flags) throws IOException {
        final boolean bothRowsAndStatus = (flags & 0x40) != 0x0;
        Field[] fields = null;
        List tuples = null;
        boolean endQuery = false;
        while (!endQuery) {
            int c = this.pgStream.ReceiveChar();
            switch (c) {
                case 65: {
                    this.receiveAsyncNotify();
                    continue;
                }
                case 66: {
                    if (fields == null) {
                        throw new IOException("Data transfer before field metadata");
                    }
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE BinaryRow");
                    }
                    Object tuple = null;
                    try {
                        tuple = this.pgStream.ReceiveTupleV2(fields.length, true);
                    }
                    catch (OutOfMemoryError oome) {
                        if (maxRows == 0 || tuples.size() < maxRows) {
                            handler.handleError(new PSQLException(GT.tr("Ran out of memory retrieving query results."), PSQLState.OUT_OF_MEMORY, oome));
                        }
                    }
                    for (int i = 0; i < fields.length; ++i) {
                        fields[i].setFormat(1);
                    }
                    if (maxRows != 0 && tuples.size() >= maxRows) {
                        continue;
                    }
                    tuples.add(tuple);
                    continue;
                }
                case 67: {
                    final String status = this.pgStream.ReceiveString();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE CommandStatus(" + status + ")");
                    }
                    if (fields == null) {
                        this.interpretCommandStatus(status, handler);
                        continue;
                    }
                    handler.handleResultRows(originalQuery, fields, tuples, null);
                    fields = null;
                    if (bothRowsAndStatus) {
                        this.interpretCommandStatus(status, handler);
                        continue;
                    }
                    continue;
                }
                case 68: {
                    if (fields == null) {
                        throw new IOException("Data transfer before field metadata");
                    }
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE DataRow");
                    }
                    Object tuple2 = null;
                    try {
                        tuple2 = this.pgStream.ReceiveTupleV2(fields.length, false);
                    }
                    catch (OutOfMemoryError oome2) {
                        if (maxRows == 0 || tuples.size() < maxRows) {
                            handler.handleError(new PSQLException(GT.tr("Ran out of memory retrieving query results."), PSQLState.OUT_OF_MEMORY, oome2));
                        }
                    }
                    if (maxRows != 0 && tuples.size() >= maxRows) {
                        continue;
                    }
                    tuples.add(tuple2);
                    continue;
                }
                case 69: {
                    handler.handleError(this.receiveErrorMessage());
                    continue;
                }
                case 73: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE EmptyQuery");
                    }
                    c = this.pgStream.ReceiveChar();
                    if (c != 0) {
                        throw new IOException("Expected \\0 after EmptyQuery, got: " + c);
                    }
                    continue;
                }
                case 78: {
                    handler.handleWarning(this.receiveNotification());
                    continue;
                }
                case 80: {
                    final String portalName = this.pgStream.ReceiveString();
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE PortalName(" + portalName + ")");
                        continue;
                    }
                    continue;
                }
                case 84: {
                    fields = this.receiveFields();
                    tuples = new ArrayList();
                    continue;
                }
                case 90: {
                    if (this.logger.logDebug()) {
                        this.logger.debug(" <=BE ReadyForQuery");
                    }
                    endQuery = true;
                    continue;
                }
                default: {
                    throw new IOException("Unexpected packet type: " + c);
                }
            }
        }
    }
    
    private Field[] receiveFields() throws IOException {
        final int size = this.pgStream.ReceiveInteger2();
        final Field[] fields = new Field[size];
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE RowDescription(" + fields.length + ")");
        }
        for (int i = 0; i < fields.length; ++i) {
            final String columnLabel = this.pgStream.ReceiveString();
            final int typeOid = this.pgStream.ReceiveInteger4();
            final int typeLength = this.pgStream.ReceiveInteger2();
            final int typeModifier = this.pgStream.ReceiveInteger4();
            fields[i] = new Field(columnLabel, columnLabel, typeOid, typeLength, typeModifier, 0, 0);
        }
        return fields;
    }
    
    private void receiveAsyncNotify() throws IOException {
        final int pid = this.pgStream.ReceiveInteger4();
        final String msg = this.pgStream.ReceiveString();
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE AsyncNotify(pid=" + pid + ",msg=" + msg + ")");
        }
        this.protoConnection.addNotification(new Notification(msg, pid));
    }
    
    private SQLException receiveErrorMessage() throws IOException {
        final String errorMsg = this.pgStream.ReceiveString().trim();
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE ErrorResponse(" + errorMsg + ")");
        }
        return new PSQLException(errorMsg, PSQLState.UNKNOWN_STATE);
    }
    
    private SQLWarning receiveNotification() throws IOException {
        String warnMsg = this.pgStream.ReceiveString();
        final int severityMark = warnMsg.indexOf(":");
        warnMsg = warnMsg.substring(severityMark + 1).trim();
        if (this.logger.logDebug()) {
            this.logger.debug(" <=BE NoticeResponse(" + warnMsg + ")");
        }
        return new SQLWarning(warnMsg);
    }
    
    private void interpretCommandStatus(final String status, final ResultHandler handler) throws IOException {
        int update_count = 0;
        long insert_oid = 0L;
        Label_0186: {
            if (status.equals("BEGIN")) {
                this.protoConnection.setTransactionState(1);
            }
            else if (status.equals("COMMIT") || status.equals("ROLLBACK")) {
                this.protoConnection.setTransactionState(0);
            }
            else {
                if (!status.startsWith("INSERT") && !status.startsWith("UPDATE") && !status.startsWith("DELETE")) {
                    if (!status.startsWith("MOVE")) {
                        break Label_0186;
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
        }
        handler.handleCommandStatus(status, update_count, insert_oid);
    }
    
    @Override
    public CopyOperation startCopy(final String sql, final boolean suppressBegin) throws SQLException {
        throw new PSQLException(GT.tr("Copy not implemented for protocol version 2"), PSQLState.NOT_IMPLEMENTED);
    }
}
