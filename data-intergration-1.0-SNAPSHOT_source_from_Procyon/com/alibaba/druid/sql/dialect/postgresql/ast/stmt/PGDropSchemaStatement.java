// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class PGDropSchemaStatement extends SQLStatementImpl implements PGSQLStatement, SQLDropStatement
{
    private SQLIdentifierExpr schemaName;
    private boolean ifExists;
    private boolean cascade;
    private boolean restrict;
    
    public SQLIdentifierExpr getSchemaName() {
        return this.schemaName;
    }
    
    public void setSchemaName(final SQLIdentifierExpr schemaName) {
        this.schemaName = schemaName;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
    
    public boolean isRestrict() {
        return this.restrict;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.schemaName);
        }
        visitor.endVisit(this);
    }
}
