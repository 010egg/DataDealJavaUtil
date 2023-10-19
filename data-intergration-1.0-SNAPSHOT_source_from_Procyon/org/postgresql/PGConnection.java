// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql;

import org.postgresql.fastpath.Fastpath;
import org.postgresql.largeobject.LargeObjectManager;
import org.postgresql.copy.CopyManager;
import java.sql.SQLException;

public interface PGConnection
{
    PGNotification[] getNotifications() throws SQLException;
    
    CopyManager getCopyAPI() throws SQLException;
    
    LargeObjectManager getLargeObjectAPI() throws SQLException;
    
    Fastpath getFastpathAPI() throws SQLException;
    
    @Deprecated
    void addDataType(final String p0, final String p1);
    
    void addDataType(final String p0, final Class p1) throws SQLException;
    
    void setPrepareThreshold(final int p0);
    
    int getPrepareThreshold();
    
    int getBackendPID();
}
