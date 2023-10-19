// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlShowDatabaseStatusStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLName name;
    private SQLOrderBy orderBy;
    private SQLExpr where;
    private SQLLimit limit;
    private boolean full;
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit x) {
        if (x != null) {
            x.setParent(this);
        }
        this.limit = x;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.orderBy = x;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.where = x;
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
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
}
