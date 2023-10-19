// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.util.TimerTask;
import org.postgresql.jdbc2.TimestampUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import org.postgresql.PGConnection;

public interface BaseConnection extends PGConnection, Connection
{
    void cancelQuery() throws SQLException;
    
    ResultSet execSQLQuery(final String p0) throws SQLException;
    
    ResultSet execSQLQuery(final String p0, final int p1, final int p2) throws SQLException;
    
    void execSQLUpdate(final String p0) throws SQLException;
    
    QueryExecutor getQueryExecutor();
    
    Object getObject(final String p0, final String p1, final byte[] p2) throws SQLException;
    
    Encoding getEncoding() throws SQLException;
    
    TypeInfo getTypeInfo();
    
    @Deprecated
    boolean haveMinimumCompatibleVersion(final String p0);
    
    boolean haveMinimumCompatibleVersion(final int p0);
    
    @Deprecated
    boolean haveMinimumServerVersion(final String p0);
    
    boolean haveMinimumServerVersion(final int p0);
    
    byte[] encodeString(final String p0) throws SQLException;
    
    String escapeString(final String p0) throws SQLException;
    
    boolean getStandardConformingStrings();
    
    TimestampUtils getTimestampUtils();
    
    Logger getLogger();
    
    boolean getStringVarcharFlag();
    
    int getTransactionState();
    
    boolean binaryTransferSend(final int p0);
    
    boolean isColumnSanitiserDisabled();
    
    void addTimerTask(final TimerTask p0, final long p1);
    
    void purgeTimerTasks();
}
