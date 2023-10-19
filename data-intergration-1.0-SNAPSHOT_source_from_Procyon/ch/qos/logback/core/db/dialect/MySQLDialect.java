// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db.dialect;

public class MySQLDialect implements SQLDialect
{
    public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    
    @Override
    public String getSelectInsertId() {
        return "SELECT LAST_INSERT_ID()";
    }
}
