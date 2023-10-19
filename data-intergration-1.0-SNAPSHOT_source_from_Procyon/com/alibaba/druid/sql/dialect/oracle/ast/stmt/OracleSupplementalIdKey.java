// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleSupplementalIdKey extends OracleSQLObjectImpl implements SQLTableElement
{
    private boolean all;
    private boolean primaryKey;
    private boolean unique;
    private boolean uniqueIndex;
    private boolean foreignKey;
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isAll() {
        return this.all;
    }
    
    public void setAll(final boolean all) {
        this.all = all;
    }
    
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }
    
    public void setPrimaryKey(final boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public boolean isUnique() {
        return this.unique;
    }
    
    public void setUnique(final boolean unique) {
        this.unique = unique;
    }
    
    public boolean isForeignKey() {
        return this.foreignKey;
    }
    
    public void setForeignKey(final boolean foreignKey) {
        this.foreignKey = foreignKey;
    }
    
    public boolean isUniqueIndex() {
        return this.uniqueIndex;
    }
    
    public void setUniqueIndex(final boolean uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }
    
    @Override
    public OracleSupplementalIdKey clone() {
        final OracleSupplementalIdKey x = new OracleSupplementalIdKey();
        x.all = this.all;
        x.primaryKey = this.primaryKey;
        x.unique = this.unique;
        x.uniqueIndex = this.uniqueIndex;
        x.foreignKey = this.foreignKey;
        return x;
    }
}
