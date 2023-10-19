// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db.dialect;

public class OracleDialect implements SQLDialect
{
    public static final String SELECT_CURRVAL = "SELECT logging_event_id_seq.currval from dual";
    
    @Override
    public String getSelectInsertId() {
        return "SELECT logging_event_id_seq.currval from dual";
    }
}
