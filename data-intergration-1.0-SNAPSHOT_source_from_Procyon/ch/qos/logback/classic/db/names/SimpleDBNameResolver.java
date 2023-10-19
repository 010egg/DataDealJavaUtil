// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.db.names;

public class SimpleDBNameResolver implements DBNameResolver
{
    private String tableNamePrefix;
    private String tableNameSuffix;
    private String columnNamePrefix;
    private String columnNameSuffix;
    
    public SimpleDBNameResolver() {
        this.tableNamePrefix = "";
        this.tableNameSuffix = "";
        this.columnNamePrefix = "";
        this.columnNameSuffix = "";
    }
    
    @Override
    public <N extends Enum<?>> String getTableName(final N tableName) {
        return String.valueOf(this.tableNamePrefix) + tableName.name().toLowerCase() + this.tableNameSuffix;
    }
    
    @Override
    public <N extends Enum<?>> String getColumnName(final N columnName) {
        return String.valueOf(this.columnNamePrefix) + columnName.name().toLowerCase() + this.columnNameSuffix;
    }
    
    public void setTableNamePrefix(final String tableNamePrefix) {
        this.tableNamePrefix = ((tableNamePrefix != null) ? tableNamePrefix : "");
    }
    
    public void setTableNameSuffix(final String tableNameSuffix) {
        this.tableNameSuffix = ((tableNameSuffix != null) ? tableNameSuffix : "");
    }
    
    public void setColumnNamePrefix(final String columnNamePrefix) {
        this.columnNamePrefix = ((columnNamePrefix != null) ? columnNamePrefix : "");
    }
    
    public void setColumnNameSuffix(final String columnNameSuffix) {
        this.columnNameSuffix = ((columnNameSuffix != null) ? columnNameSuffix : "");
    }
}
