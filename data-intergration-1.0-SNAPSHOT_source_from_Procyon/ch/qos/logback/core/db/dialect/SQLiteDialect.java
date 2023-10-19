// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db.dialect;

public class SQLiteDialect implements SQLDialect
{
    public static final String SELECT_CURRVAL = "SELECT last_insert_rowid();";
    
    @Override
    public String getSelectInsertId() {
        return "SELECT last_insert_rowid();";
    }
}
