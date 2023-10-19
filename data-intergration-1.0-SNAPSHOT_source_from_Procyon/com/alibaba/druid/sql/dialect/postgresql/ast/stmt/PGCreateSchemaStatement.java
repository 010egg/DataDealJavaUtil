// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class PGCreateSchemaStatement extends SQLStatementImpl implements PGSQLStatement, SQLCreateStatement
{
    private SQLIdentifierExpr schemaName;
    private SQLIdentifierExpr userName;
    private boolean ifNotExists;
    private boolean authorization;
    
    public SQLIdentifierExpr getSchemaName() {
        return this.schemaName;
    }
    
    public void setSchemaName(final SQLIdentifierExpr schemaName) {
        this.schemaName = schemaName;
    }
    
    public SQLIdentifierExpr getUserName() {
        return this.userName;
    }
    
    public void setUserName(final SQLIdentifierExpr userName) {
        this.userName = userName;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public boolean isAuthorization() {
        return this.authorization;
    }
    
    public void setAuthorization(final boolean authorization) {
        this.authorization = authorization;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.schemaName);
            this.acceptChild(visitor, this.userName);
        }
        visitor.endVisit(this);
    }
}
