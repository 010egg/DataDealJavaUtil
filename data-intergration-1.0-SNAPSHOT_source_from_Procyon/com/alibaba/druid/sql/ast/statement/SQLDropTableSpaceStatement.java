// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropTableSpaceStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private SQLName name;
    private boolean ifExists;
    private SQLExpr engine;
    
    public SQLDropTableSpaceStatement() {
    }
    
    public SQLDropTableSpaceStatement(final DbType dbType) {
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
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public String getTableSpaceName() {
        if (this.name == null) {
            return null;
        }
        if (this.name instanceof SQLName) {
            return this.name.getSimpleName();
        }
        return null;
    }
    
    public SQLExpr getEngine() {
        return this.engine;
    }
    
    public void setEngine(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.engine = x;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
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
