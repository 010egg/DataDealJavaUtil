// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db.dialect;

public class PostgreSQLDialect implements SQLDialect
{
    public static final String SELECT_CURRVAL = "SELECT currval('logging_event_id_seq')";
    
    @Override
    public String getSelectInsertId() {
        return "SELECT currval('logging_event_id_seq')";
    }
}
