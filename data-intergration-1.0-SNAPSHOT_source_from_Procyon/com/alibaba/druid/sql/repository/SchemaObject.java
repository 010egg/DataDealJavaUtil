// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository;

import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLStatement;

public class SchemaObject
{
    private final Schema schema;
    private final String name;
    private final long hashCode64;
    private final SchemaObjectType type;
    private SQLStatement statement;
    private long rowCount;
    
    protected SchemaObject(final Schema schema, final String name, final SchemaObjectType type) {
        this(schema, name, type, null);
    }
    
    protected SchemaObject(final Schema schema, final String name, final SchemaObjectType type, final SQLStatement statement) {
        this.rowCount = -1L;
        this.schema = schema;
        this.name = name;
        this.type = type;
        this.statement = statement;
        this.hashCode64 = FnvHash.hashCode64(name);
    }
    
    public SchemaObject clone() {
        final SchemaObject x = new SchemaObject(this.schema, this.name, this.type);
        if (this.statement != null) {
            x.statement = this.statement.clone();
        }
        x.rowCount = this.rowCount;
        return x;
    }
    
    public long nameHashCode64() {
        return this.hashCode64;
    }
    
    public SQLStatement getStatement() {
        return this.statement;
    }
    
    public SQLColumnDefinition findColumn(final String columName) {
        final long hash = FnvHash.hashCode64(columName);
        return this.findColumn(hash);
    }
    
    public SQLColumnDefinition findColumn(final long columNameHash) {
        if (this.statement == null) {
            return null;
        }
        if (this.statement instanceof SQLCreateTableStatement) {
            return ((SQLCreateTableStatement)this.statement).findColumn(columNameHash);
        }
        return null;
    }
    
    public boolean matchIndex(final String columnName) {
        if (this.statement == null) {
            return false;
        }
        if (this.statement instanceof SQLCreateTableStatement) {
            final SQLTableElement index = ((SQLCreateTableStatement)this.statement).findIndex(columnName);
            return index != null;
        }
        return false;
    }
    
    public boolean matchKey(final String columnName) {
        if (this.statement == null) {
            return false;
        }
        if (this.statement instanceof SQLCreateTableStatement) {
            final SQLTableElement index = ((SQLCreateTableStatement)this.statement).findIndex(columnName);
            return index instanceof SQLUniqueConstraint;
        }
        return false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SchemaObjectType getType() {
        return this.type;
    }
    
    public long getRowCount() {
        return this.rowCount;
    }
    
    public Schema getSchema() {
        return this.schema;
    }
    
    public enum Type
    {
        Sequence, 
        Table, 
        View, 
        Index, 
        Function;
    }
}
