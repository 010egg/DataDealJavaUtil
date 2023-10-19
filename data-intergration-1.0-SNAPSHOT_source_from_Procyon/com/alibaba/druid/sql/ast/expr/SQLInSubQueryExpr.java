// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLInSubQueryExpr extends SQLExprImpl implements Serializable, SQLReplaceable
{
    private static final long serialVersionUID = 1L;
    private boolean not;
    private SQLExpr expr;
    public SQLSelect subQuery;
    public SQLCommentHint hint;
    private boolean global;
    
    public SQLInSubQueryExpr() {
        this.not = false;
    }
    
    public SQLInSubQueryExpr(final SQLSelect select) {
        this.not = false;
        this.setSubQuery(select);
    }
    
    public SQLInSubQueryExpr(final SQLExpr expr, final SQLSelectQueryBlock queryBlock) {
        this.not = false;
        this.setExpr(expr);
        this.setSubQuery(new SQLSelect(queryBlock));
    }
    
    @Override
    public SQLInSubQueryExpr clone() {
        final SQLInSubQueryExpr x = new SQLInSubQueryExpr();
        x.not = this.not;
        x.global = this.global;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.subQuery != null) {
            x.setSubQuery(this.subQuery.clone());
        }
        return x;
    }
    
    public boolean isNot() {
        return this.not;
    }
    
    public void setNot(final boolean not) {
        this.not = not;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    public SQLSelect getSubQuery() {
        return this.subQuery;
    }
    
    public void setSubQuery(final SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.subQuery = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.subQuery != null) {
                this.subQuery.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.expr, this.subQuery);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + (this.not ? 1231 : 1237);
        result = 31 * result + (this.global ? 1231 : 1237);
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
        final SQLInSubQueryExpr other = (SQLInSubQueryExpr)obj;
        if (this.expr == null) {
            if (other.expr != null) {
                return false;
            }
        }
        else if (!this.expr.equals(other.expr)) {
            return false;
        }
        if (this.not != other.not) {
            return false;
        }
        if (this.global != other.global) {
            return false;
        }
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
        return SQLBooleanExpr.DATA_TYPE;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    public SQLCommentHint getHint() {
        return this.hint;
    }
    
    @Override
    public void setHint(final SQLCommentHint hint) {
        this.hint = hint;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
}
