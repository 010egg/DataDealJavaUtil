// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class PGAlterSchemaStatement extends SQLStatementImpl implements PGSQLStatement, SQLAlterStatement
{
    private SQLIdentifierExpr schemaName;
    private SQLIdentifierExpr newName;
    private SQLIdentifierExpr newOwner;
    
    public SQLIdentifierExpr getSchemaName() {
        return this.schemaName;
    }
    
    public void setSchemaName(final SQLIdentifierExpr schemaName) {
        this.schemaName = schemaName;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    public SQLIdentifierExpr getNewName() {
        return this.newName;
    }
    
    public void setNewName(final SQLIdentifierExpr newName) {
        this.newName = newName;
    }
    
    public SQLIdentifierExpr getNewOwner() {
        return this.newOwner;
    }
    
    public void setNewOwner(final SQLIdentifierExpr newOwner) {
        this.newOwner = newOwner;
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.schemaName);
            this.acceptChild(visitor, this.newName);
            this.acceptChild(visitor, this.newOwner);
        }
        visitor.endVisit(this);
    }
}
