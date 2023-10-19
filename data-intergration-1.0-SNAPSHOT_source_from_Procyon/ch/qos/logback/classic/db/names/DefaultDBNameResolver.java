// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db.names;

public class DefaultDBNameResolver implements DBNameResolver
{
    @Override
    public <N extends Enum<?>> String getTableName(final N tableName) {
        return tableName.toString().toLowerCase();
    }
    
    @Override
    public <N extends Enum<?>> String getColumnName(final N columnName) {
        return columnName.toString().toLowerCase();
    }
}
