// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db;

import ch.qos.logback.core.db.dialect.SQLDialectCode;
import java.sql.SQLException;
import java.sql.Connection;
import ch.qos.logback.core.spi.LifeCycle;

public interface ConnectionSource extends LifeCycle
{
    Connection getConnection() throws SQLException;
    
    SQLDialectCode getSQLDialectCode();
    
    boolean supportsGetGeneratedKeys();
    
    boolean supportsBatchUpdates();
}
