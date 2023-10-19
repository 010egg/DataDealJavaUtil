// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.SQLException;

public class PSQLException extends SQLException
{
    private ServerErrorMessage serverError;
    
    public PSQLException(final String msg, final PSQLState state, final Throwable cause) {
        super(msg, (state == null) ? null : state.getState(), cause);
    }
    
    public PSQLException(final String msg, final PSQLState state) {
        super(msg, (state == null) ? null : state.getState());
    }
    
    public PSQLException(final ServerErrorMessage serverError) {
        this(serverError, true);
    }
    
    public PSQLException(final ServerErrorMessage serverError, final boolean detail) {
        super(detail ? serverError.toString() : serverError.getNonSensitiveErrorMessage(), serverError.getSQLState());
        this.serverError = serverError;
    }
    
    public ServerErrorMessage getServerErrorMessage() {
        return this.serverError;
    }
}
