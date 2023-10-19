// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowSessionStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    protected SQLExpr like;
    
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
        if (visitor.visit(this) && this.like != null) {
            this.like.accept(visitor);
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.like == expr) {
            this.setLike(target);
            return true;
        }
        return false;
    }
}
