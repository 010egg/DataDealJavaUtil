// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLSomeExpr extends SQLExprImpl
{
    public SQLSelect subQuery;
    
    public SQLSomeExpr() {
    }
    
    public SQLSomeExpr(final SQLSelect select) {
        this.setSubQuery(select);
    }
    
    @Override
    public SQLSomeExpr clone() {
        final SQLSomeExpr x = new SQLSomeExpr();
        if (this.subQuery != null) {
            x.setSubQuery(this.subQuery.clone());
        }
        return x;
    }
    
    public SQLSelect getSubQuery() {
        return this.subQuery;
    }
    
    public void setSubQuery(final SQLSelect subQuery) {
        if (subQuery != null) {
            subQuery.setParent(this);
        }
        this.subQuery = subQuery;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.subQuery != null) {
            this.subQuery.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.subQuery == null) ? 0 : this.subQuery.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLSomeExpr other = (SQLSomeExpr)obj;
        if (this.subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        }
        else if (!this.subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.subQuery == null) {
            return null;
        }
        final SQLSelectQueryBlock queryBlock = this.subQuery.getFirstQueryBlock();
        if (queryBlock == null) {
            return null;
        }
        final List<SQLSelectItem> selectList = queryBlock.getSelectList();
        if (selectList.size() == 1) {
            return selectList.get(0).computeDataType();
        }
        return null;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.subQuery);
    }
}
