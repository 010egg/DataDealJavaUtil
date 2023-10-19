// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;

public final class SQLLimit extends SQLObjectImpl implements SQLReplaceable
{
    private SQLExpr rowCount;
    private SQLExpr offset;
    private List<SQLExpr> by;
    
    public SQLLimit() {
    }
    
    public SQLLimit(final int rowCount) {
        this.setRowCount(new SQLIntegerExpr(rowCount));
    }
    
    public SQLLimit(final SQLExpr rowCount) {
        this.setRowCount(rowCount);
    }
    
    public SQLLimit(final SQLExpr offset, final SQLExpr rowCount) {
        this.setOffset(offset);
        this.setRowCount(rowCount);
    }
    
    public SQLExpr getRowCount() {
        return this.rowCount;
    }
    
    public void setRowCount(final SQLExpr rowCount) {
        if (rowCount != null) {
            rowCount.setParent(this);
        }
        this.rowCount = rowCount;
    }
    
    public void setRowCount(final int rowCount) {
        this.setRowCount(new SQLIntegerExpr(rowCount));
    }
    
    public SQLExpr getOffset() {
        return this.offset;
    }
    
    public void setOffset(final int offset) {
        this.setOffset(new SQLIntegerExpr(offset));
    }
    
    public void setOffset(final SQLExpr offset) {
        if (offset != null) {
            offset.setParent(this);
        }
        this.offset = offset;
    }
    
    public void merge(final SQLLimit other) {
        if (other == null) {
            return;
        }
        if (other.offset != null && this.offset == null) {
            this.offset = other.offset.clone();
        }
        if (other.rowCount != null && this.rowCount == null) {
            this.rowCount = other.rowCount.clone();
        }
        if (other.by != null) {
            for (final SQLExpr item : other.by) {
                this.addBy(item.clone());
            }
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.offset != null) {
                this.offset.accept(visitor);
            }
            if (this.rowCount != null) {
                this.rowCount.accept(visitor);
            }
            if (this.by != null) {
                for (final SQLExpr item : this.by) {
                    item.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLLimit clone() {
        final SQLLimit x = new SQLLimit();
        if (this.offset != null) {
            x.setOffset(this.offset.clone());
        }
        if (this.rowCount != null) {
            x.setRowCount(this.rowCount.clone());
        }
        if (this.by != null) {
            for (final SQLExpr item : this.by) {
                x.addBy(item);
            }
        }
        if (this.attributes != null) {
            x.attributes = (Map<String, Object>)((HashMap)this.attributes).clone();
        }
        return x;
    }
    
    public void addBy(final SQLExpr item) {
        if (item == null) {
            return;
        }
        if (this.by == null) {
            this.by = new ArrayList<SQLExpr>(1);
        }
        this.by.add(item);
        item.setParent(this);
    }
    
    public List<SQLExpr> getBy() {
        return this.by;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.rowCount == expr) {
            this.setRowCount(target);
            return true;
        }
        if (this.offset == expr) {
            this.setOffset(target);
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
        final SQLLimit limit = (SQLLimit)o;
        if (this.rowCount != null) {
            if (this.rowCount.equals(limit.rowCount)) {
                return (this.offset != null) ? this.offset.equals(limit.offset) : (limit.offset == null);
            }
        }
        else if (limit.rowCount == null) {
            return (this.offset != null) ? this.offset.equals(limit.offset) : (limit.offset == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.rowCount != null) ? this.rowCount.hashCode() : 0;
        result = 31 * result + ((this.offset != null) ? this.offset.hashCode() : 0);
        return result;
    }
}
