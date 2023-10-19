// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;

public class MysqlForeignKey extends SQLForeignKeyImpl
{
    private SQLName indexName;
    private boolean hasConstraint;
    private Match referenceMatch;
    protected Option onUpdate;
    protected Option onDelete;
    
    public MysqlForeignKey() {
        this.dbType = DbType.mysql;
    }
    
    public SQLName getIndexName() {
        return this.indexName;
    }
    
    public void setIndexName(final SQLName indexName) {
        this.indexName = indexName;
    }
    
    public boolean isHasConstraint() {
        return this.hasConstraint;
    }
    
    public void setHasConstraint(final boolean hasConstraint) {
        this.hasConstraint = hasConstraint;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
    }
    
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getReferencedTableName());
            this.acceptChild(visitor, this.getReferencingColumns());
            this.acceptChild(visitor, this.getReferencedColumns());
            this.acceptChild(visitor, this.indexName);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MysqlForeignKey clone() {
        final MysqlForeignKey x = new MysqlForeignKey();
        this.cloneTo(x);
        if (this.indexName != null) {
            x.setIndexName(this.indexName.clone());
        }
        x.referenceMatch = this.referenceMatch;
        x.onUpdate = this.onUpdate;
        x.onDelete = this.onDelete;
        return x;
    }
    
    public Match getReferenceMatch() {
        return this.referenceMatch;
    }
    
    public void setReferenceMatch(final Match referenceMatch) {
        this.referenceMatch = referenceMatch;
    }
    
    public Option getOnUpdate() {
        return this.onUpdate;
    }
    
    public void setOnUpdate(final Option onUpdate) {
        this.onUpdate = onUpdate;
    }
    
    public Option getOnDelete() {
        return this.onDelete;
    }
    
    public void setOnDelete(final Option onDelete) {
        this.onDelete = onDelete;
    }
}
