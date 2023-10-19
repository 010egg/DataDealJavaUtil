// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowViewsStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    protected SQLName database;
    protected SQLExpr like;
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.database = x;
    }
    
    public SQLExpr getLike() {
        return this.like;
    }
    
    public void setLike(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.like = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.like);
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.database == expr) {
            this.setDatabase((SQLName)target);
            return true;
        }
        if (this.like == expr) {
            this.setLike(target);
            return true;
        }
        return false;
    }
}
