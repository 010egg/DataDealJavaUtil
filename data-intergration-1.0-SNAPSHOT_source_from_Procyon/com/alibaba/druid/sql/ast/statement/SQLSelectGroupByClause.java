// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLSelectGroupByClause extends SQLObjectImpl implements SQLReplaceable
{
    private final List<SQLExpr> items;
    private SQLExpr having;
    private boolean withRollUp;
    private boolean withCube;
    private boolean distinct;
    private boolean paren;
    
    public SQLSelectGroupByClause() {
        this.items = new ArrayList<SQLExpr>();
        this.withRollUp = false;
        this.withCube = false;
        this.distinct = false;
        this.paren = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.items.size(); ++i) {
                final SQLExpr item = this.items.get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
            if (this.having != null) {
                this.having.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isDistinct() {
        return this.distinct;
    }
    
    public void setDistinct(final boolean distinct) {
        this.distinct = distinct;
    }
    
    public boolean isWithRollUp() {
        return this.withRollUp;
    }
    
    public void setWithRollUp(final boolean withRollUp) {
        this.withRollUp = withRollUp;
    }
    
    public boolean isWithCube() {
        return this.withCube;
    }
    
    public void setWithCube(final boolean withCube) {
        this.withCube = withCube;
    }
    
    public SQLExpr getHaving() {
        return this.having;
    }
    
    public void setHaving(final SQLExpr having) {
        if (having != null) {
            having.setParent(this);
        }
        this.having = having;
    }
    
    public void addHaving(final SQLExpr condition) {
        if (condition == null) {
            return;
        }
        if (this.having == null) {
            this.having = condition;
        }
        else {
            this.having = SQLBinaryOpExpr.and(this.having, condition);
        }
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public boolean containsItem(final SQLExpr item) {
        return this.items.contains(item);
    }
    
    public void addItem(final SQLExpr sqlExpr) {
        if (sqlExpr != null) {
            sqlExpr.setParent(this);
            this.items.add(sqlExpr);
        }
    }
    
    public void addItem(final int index, final SQLExpr sqlExpr) {
        if (sqlExpr != null) {
            sqlExpr.setParent(this);
            this.items.add(index, sqlExpr);
        }
    }
    
    @Override
    public SQLSelectGroupByClause clone() {
        final SQLSelectGroupByClause x = new SQLSelectGroupByClause();
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        if (this.having != null) {
            x.setHaving(this.having.clone());
        }
        x.withRollUp = this.withRollUp;
        x.withCube = this.withCube;
        x.distinct = this.distinct;
        x.paren = this.paren;
        if (this.hint != null) {
            x.setHint(this.hint.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.having) {
            this.setHaving(target);
            return true;
        }
        for (int i = this.items.size() - 1; i >= 0; --i) {
            if (this.items.get(i) == expr) {
                if (target instanceof SQLIntegerExpr) {
                    this.items.remove(i);
                }
                else {
                    this.items.set(i, target);
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
    
    public boolean isParen() {
        return this.paren;
    }
    
    public void setParen(final boolean paren) {
        this.paren = paren;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSelectGroupByClause that = (SQLSelectGroupByClause)o;
        if (this.withRollUp != that.withRollUp) {
            return false;
        }
        if (this.withCube != that.withCube) {
            return false;
        }
        if (this.distinct != that.distinct) {
            return false;
        }
        if (this.paren != that.paren) {
            return false;
        }
        Label_0116: {
            if (this.items != null) {
                if (this.items.equals(that.items)) {
                    break Label_0116;
                }
            }
            else if (that.items == null) {
                break Label_0116;
            }
            return false;
        }
        if (this.having != null) {
            if (this.having.equals(that.having)) {
                return (this.hint != null) ? this.hint.equals(that.hint) : (that.hint == null);
            }
        }
        else if (that.having == null) {
            return (this.hint != null) ? this.hint.equals(that.hint) : (that.hint == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.items != null) ? this.items.hashCode() : 0;
        result = 31 * result + ((this.having != null) ? this.having.hashCode() : 0);
        result = 31 * result + (this.withRollUp ? 1 : 0);
        result = 31 * result + (this.withCube ? 1 : 0);
        result = 31 * result + (this.distinct ? 1 : 0);
        result = 31 * result + (this.paren ? 1 : 0);
        result = 31 * result + ((this.hint != null) ? this.hint.hashCode() : 0);
        return result;
    }
}
