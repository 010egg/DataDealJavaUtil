// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db.dialect;

public class MsSQLDialect implements SQLDialect
{
    public static final String SELECT_CURRVAL = "SELECT @@identity id";
    
    @Override
    public String getSelectInsertId() {
        return "SELECT @@identity id";
    }
}
