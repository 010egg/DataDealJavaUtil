// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.util.Set;
import java.sql.SQLWarning;
import java.sql.SQLException;
import org.postgresql.PGNotification;
import org.postgresql.util.HostSpec;

public interface ProtocolConnection
{
    public static final int TRANSACTION_IDLE = 0;
    public static final int TRANSACTION_OPEN = 1;
    public static final int TRANSACTION_FAILED = 2;
    
    HostSpec getHostSpec();
    
    String getUser();
    
    String getDatabase();
    
    String getServerVersion();
    
    int getServerVersionNum();
    
    Encoding getEncoding();
    
    boolean getStandardConformingStrings();
    
    int getTransactionState();
    
    PGNotification[] getNotifications() throws SQLException;
    
    SQLWarning getWarnings();
    
    QueryExecutor getQueryExecutor();
    
    void sendQueryCancel() throws SQLException;
    
    void close();
    
    boolean isClosed();
    
    int getProtocolVersion();
    
    void setBinaryReceiveOids(final Set<Integer> p0);
    
    boolean getIntegerDateTimes();
    
    int getBackendPID();
    
    void abort();
}
