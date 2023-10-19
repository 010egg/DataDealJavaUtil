// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

public class Field
{
    public static final int TEXT_FORMAT = 0;
    public static final int BINARY_FORMAT = 1;
    private final int length;
    private final int oid;
    private final int mod;
    private final String columnLabel;
    private String columnName;
    private int format;
    private final int tableOid;
    private final int positionInTable;
    private String tableName;
    private String schemaName;
    private int nullable;
    private boolean autoIncrement;
    
    public Field(final String name, final int oid, final int length, final int mod) {
        this(name, name, oid, length, mod, 0, 0);
    }
    
    public Field(final String name, final int oid) {
        this(name, oid, 0, -1);
    }
    
    public Field(final String columnLabel, final String columnName, final int oid, final int length, final int mod, final int tableOid, final int positionInTable) {
        this.format = 0;
        this.tableName = "";
        this.schemaName = "";
        this.nullable = 2;
        this.autoIncrement = false;
        this.columnLabel = columnLabel;
        this.columnName = columnName;
        this.oid = oid;
        this.length = length;
        this.mod = mod;
        this.tableOid = tableOid;
        this.positionInTable = positionInTable;
    }
    
    public int getOID() {
        return this.oid;
    }
    
    public int getMod() {
        return this.mod;
    }
    
    public String getColumnLabel() {
        return this.columnLabel;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public int getFormat() {
        return this.format;
    }
    
    public void setFormat(final int format) {
        this.format = format;
    }
    
    public int getTableOid() {
        return this.tableOid;
    }
    
    public int getPositionInTable() {
        return this.positionInTable;
    }
    
    public void setNullable(final int nullable) {
        this.nullable = nullable;
    }
    
    public int getNullable() {
        return this.nullable;
    }
    
    public void setAutoIncrement(final boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
    
    public boolean getAutoIncrement() {
        return this.autoIncrement;
    }
    
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public void setSchemaName(final String schemaName) {
        this.schemaName = schemaName;
    }
    
    public String getSchemaName() {
        return this.schemaName;
    }
    
    @Override
    public String toString() {
        return "Field(" + ((this.columnName != null) ? this.columnName : "") + "," + Oid.toString(this.oid) + "," + this.length + "," + ((this.format == 0) ? 'T' : 'B') + ")";
    }
}
