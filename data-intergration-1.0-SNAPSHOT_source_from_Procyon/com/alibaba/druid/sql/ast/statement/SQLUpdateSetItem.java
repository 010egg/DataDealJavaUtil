// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLUpdateSetItem extends SQLObjectImpl implements SQLReplaceable
{
    private SQLExpr column;
    private SQLExpr value;
    
    public SQLExpr getColumn() {
        return this.column;
    }
    
    public void cloneTo(final SQLUpdateSetItem x) {
        if (this.column != null) {
            (x.column = this.column.clone()).setParent(x);
        }
        if (this.value != null) {
            (x.value = this.value.clone()).setParent(x);
        }
    }
    
    @Override
    public SQLUpdateSetItem clone() {
        final SQLUpdateSetItem x = new SQLUpdateSetItem();
        this.cloneTo(x);
        return x;
    }
    
    public void setColumn(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.column = x;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            this.column.output(buf);
            buf.append(" = ");
            this.value.output(buf);
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.column != null) {
                this.column.accept(visitor);
            }
            if (this.value != null) {
                this.value.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean columnMatch(final String column) {
        if (this.column instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)this.column).nameEquals(column);
        }
        if (this.column instanceof SQLPropertyExpr) {
            ((SQLPropertyExpr)this.column).nameEquals(column);
        }
        return false;
    }
    
    public boolean columnMatch(final long columnHash) {
        return this.column instanceof SQLName && ((SQLName)this.column).nameHashCode64() == columnHash;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.column) {
            this.setColumn(target);
            return true;
        }
        if (expr == this.value) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLUpdateSetItem that = (SQLUpdateSetItem)o;
        if (this.column != null) {
            if (this.column.equals(that.column)) {
                return (this.value != null) ? this.value.equals(that.value) : (that.value == null);
            }
        }
        else if (that.column == null) {
            return (this.value != null) ? this.value.equals(that.value) : (that.value == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.column != null) ? this.column.hashCode() : 0;
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
}
