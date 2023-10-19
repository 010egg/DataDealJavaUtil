// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropFunctionStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private SQLName name;
    private boolean ifExists;
    private boolean temporary;
    
    public SQLDropFunctionStatement() {
    }
    
    public SQLDropFunctionStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public boolean isTemporary() {
        return this.temporary;
    }
    
    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.name == expr) {
            this.setName((SQLName)target);
            return true;
        }
        return false;
    }
}
