// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;

public class MySqlShowRuleStatusStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private boolean lite;
    private SQLOrderBy orderBy;
    private SQLExpr where;
    private SQLLimit limit;
    private boolean full;
    private boolean version;
    
    public MySqlShowRuleStatusStatement() {
        this.lite = true;
        this.full = false;
        this.version = false;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        this.limit = limit;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    public boolean isLite() {
        return this.lite;
    }
    
    public void setLite(final boolean lite) {
        this.lite = lite;
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
    
    public boolean isVersion() {
        return this.version;
    }
    
    public void setVersion(final boolean version) {
        this.version = version;
    }
}
