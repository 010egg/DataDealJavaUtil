// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import org.postgresql.core.Field;

class CacheMetadataField
{
    private String colName;
    private String tabName;
    private String schemaName;
    private int nullable;
    private boolean auto;
    
    protected CacheMetadataField(final Field f) {
        this.colName = f.getColumnName();
        this.tabName = f.getTableName();
        this.schemaName = f.getSchemaName();
        this.nullable = f.getNullable();
        this.auto = f.getAutoIncrement();
    }
    
    protected void get(final Field f) {
        f.setColumnName(this.colName);
        f.setTableName(this.tabName);
        f.setSchemaName(this.schemaName);
        f.setNullable(this.nullable);
        f.setAutoIncrement(this.auto);
    }
}
