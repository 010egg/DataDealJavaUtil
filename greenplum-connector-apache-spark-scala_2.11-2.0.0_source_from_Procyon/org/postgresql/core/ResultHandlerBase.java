// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.util.List;
import java.sql.SQLWarning;
import java.sql.SQLException;

public class ResultHandlerBase implements ResultHandler
{
    private SQLException firstException;
    private SQLException lastException;
    private SQLWarning firstWarning;
    private SQLWarning lastWarning;
    
    @Override
    public void handleResultRows(final Query fromQuery, final Field[] fields, final List<Tuple> tuples, final ResultCursor cursor) {
    }
    
    @Override
    public void handleCommandStatus(final String status, final long updateCount, final long insertOID) {
    }
    
    @Override
    public void secureProgress() {
    }
    
    @Override
    public void handleWarning(final SQLWarning warning) {
        if (this.firstWarning == null) {
            this.lastWarning = warning;
            this.firstWarning = warning;
            return;
        }
        this.lastWarning.setNextException(warning);
        this.lastWarning = warning;
    }
    
    @Override
    public void handleError(final SQLException error) {
        if (this.firstException == null) {
            this.lastException = error;
            this.firstException = error;
            return;
        }
        this.lastException.setNextException(error);
        this.lastException = error;
    }
    
    @Override
    public void handleCompletion() throws SQLException {
        if (this.firstException != null) {
            throw this.firstException;
        }
    }
    
    @Override
    public SQLException getException() {
        return this.firstException;
    }
    
    @Override
    public SQLWarning getWarning() {
        return this.firstWarning;
    }
}
