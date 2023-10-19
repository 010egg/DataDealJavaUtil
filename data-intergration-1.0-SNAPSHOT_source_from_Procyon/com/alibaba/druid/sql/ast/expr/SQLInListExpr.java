// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLInListExpr extends SQLExprImpl implements SQLReplaceable, Serializable
{
    private static final long serialVersionUID = 1L;
    private boolean not;
    private SQLExpr expr;
    private List<SQLExpr> targetList;
    protected SQLCommentHint hint;
    
    public SQLInListExpr() {
        this.not = false;
        this.targetList = new ArrayList<SQLExpr>();
    }
    
    public SQLInListExpr(final SQLExpr expr) {
        this.not = false;
        this.targetList = new ArrayList<SQLExpr>();
        this.setExpr(expr);
    }
    
    public SQLInListExpr(final String expr, final String... values) {
        this.not = false;
        this.targetList = new ArrayList<SQLExpr>();
        this.setExpr(SQLUtils.toSQLExpr(expr));
        for (final String value : values) {
            this.targetList.add(new SQLCharExpr(value));
        }
    }
    
    public SQLInListExpr(final SQLExpr expr, final boolean not) {
        this.not = false;
        this.targetList = new ArrayList<SQLExpr>();
        this.setExpr(expr);
        this.not = not;
    }
    
    @Override
    public SQLInListExpr clone() {
        final SQLInListExpr x = new SQLInListExpr();
        x.not = this.not;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        for (final SQLExpr e : this.targetList) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.targetList.add(e2);
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
    
    public void addTarget(final SQLExpr x) {
        x.setParent(this);
        this.targetList.add(x);
    }
    
    public void addTarget(final int index, final SQLExpr x) {
        x.setParent(this);
        this.targetList.add(index, x);
    }
    
    public List<SQLExpr> getTargetList() {
        return this.targetList;
    }
    
    public boolean sortTargetList() {
        if (this.targetList.size() < 2) {
            return true;
        }
        final Class<?> firstClass = this.targetList.get(0).getClass();
        for (int i = 1; i < this.targetList.size(); ++i) {
            if (this.targetList.get(i).getClass() != firstClass) {
                return false;
            }
        }
        if (!Comparable.class.isAssignableFrom(firstClass)) {
            return false;
        }
        Collections.sort(this.targetList);
        return true;
    }
    
    public void setTargetList(final List<SQLExpr> targetList) {
        this.targetList = targetList;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.targetList != null) {
                for (final SQLExpr item : this.targetList) {
                    if (item != null) {
                        item.accept(visitor);
                    }
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.expr != null) {
            children.add(this.expr);
        }
        children.addAll(this.targetList);
        return children;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + (this.not ? 1231 : 1237);
        result = 31 * result + ((this.targetList == null) ? 0 : this.targetList.hashCode());
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
        final SQLInListExpr other = (SQLInListExpr)obj;
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
        if (this.targetList == null) {
            if (other.targetList != null) {
                return false;
            }
        }
        else if (!this.targetList.equals(other.targetList)) {
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
        for (int i = this.targetList.size() - 1; i >= 0; --i) {
            if (this.targetList.get(i) == expr) {
                if (target == null) {
                    this.targetList.remove(i);
                }
                else {
                    this.targetList.set(i, target);
                    target.setParent(this);
                }
                return true;
            }
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
}
