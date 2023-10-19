// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.SQLException;

public class PSQLException extends SQLException
{
    private ServerErrorMessage _serverError;
    
    public PSQLException(final String msg, final PSQLState state, final Throwable cause) {
        super(msg, (state == null) ? null : state.getState());
        this.initCause(cause);
    }
    
    public PSQLException(final String msg, final PSQLState state) {
        this(msg, state, null);
    }
    
    public PSQLException(final ServerErrorMessage serverError) {
        this(serverError.toString(), new PSQLState(serverError.getSQLState()));
        this._serverError = serverError;
    }
    
    public ServerErrorMessage getServerErrorMessage() {
        return this._serverError;
    }
}
