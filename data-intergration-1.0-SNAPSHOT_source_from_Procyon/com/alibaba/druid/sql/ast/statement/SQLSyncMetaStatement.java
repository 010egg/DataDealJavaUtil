// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLSyncMetaStatement extends SQLStatementImpl
{
    private Boolean restrict;
    private Boolean ignore;
    private SQLName from;
    private SQLExpr like;
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.from);
            this.acceptChild(v, this.like);
        }
        v.endVisit(this);
    }
    
    public Boolean getRestrict() {
        return this.restrict;
    }
    
    public void setRestrict(final Boolean restrict) {
        this.restrict = restrict;
    }
    
    public Boolean getIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final Boolean ignore) {
        this.ignore = ignore;
    }
    
    public SQLName getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLName from) {
        this.from = from;
    }
    
    public SQLExpr getLike() {
        return this.like;
    }
    
    public void setLike(final SQLExpr like) {
        this.like = like;
    }
}
