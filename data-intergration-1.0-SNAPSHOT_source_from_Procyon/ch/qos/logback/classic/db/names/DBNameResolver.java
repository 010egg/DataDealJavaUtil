// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db.names;

public interface DBNameResolver
{
     <N extends Enum<?>> String getTableName(final N p0);
    
     <N extends Enum<?>> String getColumnName(final N p0);
}
