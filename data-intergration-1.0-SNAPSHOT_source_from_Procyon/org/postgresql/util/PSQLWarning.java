// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.SQLWarning;

public class PSQLWarning extends SQLWarning
{
    private ServerErrorMessage serverError;
    
    public PSQLWarning(final ServerErrorMessage err) {
        this.serverError = err;
    }
    
    @Override
    public String toString() {
        return this.serverError.toString();
    }
    
    @Override
    public String getSQLState() {
        return this.serverError.getSQLState();
    }
    
    @Override
    public String getMessage() {
        return this.serverError.getMessage();
    }
    
    public ServerErrorMessage getServerErrorMessage() {
        return this.serverError;
    }
}
