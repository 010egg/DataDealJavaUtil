// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.sql.SQLWarning;
import java.sql.SQLException;
import java.util.List;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;

public class SetupQueryRunner
{
    public static byte[][] run(final ProtocolConnection protoConnection, final String queryString, final boolean wantResults) throws SQLException {
        final QueryExecutor executor = protoConnection.getQueryExecutor();
        final Query query = executor.createSimpleQuery(queryString);
        final SimpleResultHandler handler = new SimpleResultHandler(protoConnection);
        int flags = 17;
        if (!wantResults) {
            flags |= 0x6;
        }
        try {
            executor.execute(query, null, handler, 0, 0, flags);
        }
        finally {
            query.close();
        }
        if (!wantResults) {
            return null;
        }
        final List tuples = handler.getResults();
        if (tuples == null || tuples.size() != 1) {
            throw new PSQLException(GT.tr("An unexpected result was returned by a query."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
        }
        return tuples.get(0);
    }
    
    private static class SimpleResultHandler implements ResultHandler
    {
        private SQLException error;
        private List tuples;
        private final ProtocolConnection protoConnection;
        
        SimpleResultHandler(final ProtocolConnection protoConnection) {
            this.protoConnection = protoConnection;
        }
        
        List getResults() {
            return this.tuples;
        }
        
        @Override
        public void handleResultRows(final Query fromQuery, final Field[] fields, final List tuples, final ResultCursor cursor) {
            this.tuples = tuples;
        }
        
        @Override
        public void handleCommandStatus(final String status, final int updateCount, final long insertOID) {
        }
        
        @Override
        public void handleWarning(final SQLWarning warning) {
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
