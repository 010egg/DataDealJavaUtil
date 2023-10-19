// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLOver extends SQLObjectImpl implements SQLReplaceable
{
    protected final List<SQLExpr> partitionBy;
    protected SQLOrderBy orderBy;
    protected SQLOrderBy distributeBy;
    protected SQLOrderBy sortBy;
    protected SQLOrderBy clusterBy;
    protected SQLName of;
    protected WindowingType windowingType;
    protected boolean windowingPreceding;
    protected boolean windowingFollowing;
    protected SQLExpr windowingBetweenBegin;
    protected WindowingBound windowingBetweenBeginBound;
    protected SQLExpr windowingBetweenEnd;
    protected WindowingBound windowingBetweenEndBound;
    protected boolean excludeCurrentRow;
    
    public SQLOver() {
        this.partitionBy = new ArrayList<SQLExpr>();
    }
    
    public SQLOver(final SQLOrderBy orderBy) {
        this.partitionBy = new ArrayList<SQLExpr>();
        this.setOrderBy(orderBy);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.partitionBy != null) {
                for (final SQLExpr item : this.partitionBy) {
                    if (item != null) {
                        item.accept(visitor);
                    }
                }
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
            if (this.distributeBy != null) {
                this.distributeBy.accept(visitor);
            }
            if (this.sortBy != null) {
                this.sortBy.accept(visitor);
            }
            if (this.clusterBy != null) {
                this.clusterBy.accept(visitor);
            }
            if (this.of != null) {
                this.of.accept(visitor);
            }
        }
        visitor.endVisit(this);
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
    
    public SQLOrderBy getClusterBy() {
        return this.clusterBy;
    }
    
    public void setClusterBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.clusterBy = x;
    }
    
    public SQLOrderBy getDistributeBy() {
        return this.distributeBy;
    }
    
    public void setDistributeBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.distributeBy = x;
    }
    
    public SQLOrderBy getSortBy() {
        return this.sortBy;
    }
    
    public void setSortBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.sortBy = x;
    }
    
    public SQLName getOf() {
        return this.of;
    }
    
    public void setOf(final SQLName of) {
        if (of != null) {
            of.setParent(this);
        }
        this.of = of;
    }
    
    public List<SQLExpr> getPartitionBy() {
        return this.partitionBy;
    }
    
    public WindowingType getWindowingType() {
        return this.windowingType;
    }
    
    public void setWindowingType(final WindowingType windowingType) {
        this.windowingType = windowingType;
    }
    
    public boolean isWindowingPreceding() {
        return this.windowingPreceding;
    }
    
    public void setWindowingPreceding(final boolean windowingPreceding) {
        this.windowingPreceding = windowingPreceding;
    }
    
    public SQLExpr getWindowingBetweenBegin() {
        return this.windowingBetweenBegin;
    }
    
    public void setWindowingBetweenBegin(final SQLExpr windowingBetweenBegin) {
        this.windowingBetweenBegin = windowingBetweenBegin;
    }
    
    public SQLExpr getWindowingBetweenEnd() {
        return this.windowingBetweenEnd;
    }
    
    public void setWindowingBetweenEnd(final SQLExpr windowingBetweenEnd) {
        this.windowingBetweenEnd = windowingBetweenEnd;
    }
    
    public boolean isWindowingBetweenEndPreceding() {
        return this.windowingBetweenEndBound == WindowingBound.PRECEDING;
    }
    
    public boolean isWindowingBetweenEndFollowing() {
        return this.windowingBetweenEndBound == WindowingBound.FOLLOWING;
    }
    
    public WindowingBound getWindowingBetweenBeginBound() {
        return this.windowingBetweenBeginBound;
    }
    
    public void setWindowingBetweenBeginBound(final WindowingBound windowingBetweenBeginBound) {
        this.windowingBetweenBeginBound = windowingBetweenBeginBound;
    }
    
    public WindowingBound getWindowingBetweenEndBound() {
        return this.windowingBetweenEndBound;
    }
    
    public void setWindowingBetweenEndBound(final WindowingBound windowingBetweenEndBound) {
        this.windowingBetweenEndBound = windowingBetweenEndBound;
    }
    
    public boolean isExcludeCurrentRow() {
        return this.excludeCurrentRow;
    }
    
    public void setExcludeCurrentRow(final boolean excludeCurrentRow) {
        this.excludeCurrentRow = excludeCurrentRow;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLOver sqlOver = (SQLOver)o;
        if (this.windowingPreceding != sqlOver.windowingPreceding) {
            return false;
        }
        if (this.windowingFollowing != sqlOver.windowingFollowing) {
            return false;
        }
        if (!this.partitionBy.equals(sqlOver.partitionBy)) {
            return false;
        }
        Label_0106: {
            if (this.orderBy != null) {
                if (this.orderBy.equals(sqlOver.orderBy)) {
                    break Label_0106;
                }
            }
            else if (sqlOver.orderBy == null) {
                break Label_0106;
            }
            return false;
        }
        Label_0139: {
            if (this.of != null) {
                if (this.of.equals(sqlOver.of)) {
                    break Label_0139;
                }
            }
            else if (sqlOver.of == null) {
                break Label_0139;
            }
            return false;
        }
        if (this.windowingType != sqlOver.windowingType) {
            return false;
        }
        Label_0185: {
            if (this.windowingBetweenBegin != null) {
                if (this.windowingBetweenBegin.equals(sqlOver.windowingBetweenBegin)) {
                    break Label_0185;
                }
            }
            else if (sqlOver.windowingBetweenBegin == null) {
                break Label_0185;
            }
            return false;
        }
        if (this.windowingBetweenBeginBound != sqlOver.windowingBetweenBeginBound) {
            return false;
        }
        if (this.windowingBetweenEnd != null) {
            if (this.windowingBetweenEnd.equals(sqlOver.windowingBetweenEnd)) {
                return this.windowingBetweenEndBound == sqlOver.windowingBetweenEndBound;
            }
        }
        else if (sqlOver.windowingBetweenEnd == null) {
            return this.windowingBetweenEndBound == sqlOver.windowingBetweenEndBound;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.partitionBy != null) ? this.partitionBy.hashCode() : 0;
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + ((this.of != null) ? this.of.hashCode() : 0);
        result = 31 * result + ((this.windowingType != null) ? this.windowingType.hashCode() : 0);
        result = 31 * result + (this.windowingPreceding ? 1 : 0);
        result = 31 * result + (this.windowingFollowing ? 1 : 0);
        result = 31 * result + ((this.windowingBetweenBegin != null) ? this.windowingBetweenBegin.hashCode() : 0);
        result = 31 * result + ((this.windowingBetweenBeginBound != null) ? this.windowingBetweenBeginBound.hashCode() : 0);
        result = 31 * result + ((this.windowingBetweenEnd != null) ? this.windowingBetweenEnd.hashCode() : 0);
        result = 31 * result + ((this.windowingBetweenEndBound != null) ? this.windowingBetweenEndBound.hashCode() : 0);
        return result;
    }
    
    public void cloneTo(final SQLOver x) {
        for (final SQLExpr item : this.partitionBy) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.partitionBy.add(item2);
        }
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        if (this.of != null) {
            x.setOf(this.of.clone());
        }
        x.windowingType = this.windowingType;
        x.windowingPreceding = this.windowingPreceding;
        x.windowingFollowing = this.windowingFollowing;
        if (this.windowingBetweenBegin != null) {
            x.setWindowingBetweenBegin(this.windowingBetweenBegin.clone());
        }
        x.windowingBetweenBeginBound = this.windowingBetweenBeginBound;
        x.windowingBetweenEndBound = this.windowingBetweenEndBound;
        if (this.windowingBetweenEnd != null) {
            x.setWindowingBetweenEnd(this.windowingBetweenEnd.clone());
        }
    }
    
    @Override
    public SQLOver clone() {
        final SQLOver x = new SQLOver();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.windowingBetweenBegin == expr) {
            this.setWindowingBetweenBegin(target);
            return true;
        }
        if (this.windowingBetweenEnd == expr) {
            this.setWindowingBetweenEnd(target);
            return true;
        }
        for (int i = 0; i < this.partitionBy.size(); ++i) {
            if (this.partitionBy.get(i) == expr) {
                this.partitionBy.set(i, target);
                target.setParent(this);
            }
        }
        return false;
    }
    
    public enum WindowingType
    {
        ROWS("ROWS"), 
        RANGE("RANGE");
        
        public final String name;
        public final String name_lower;
        
        private WindowingType(final String name) {
            this.name = name;
            this.name_lower = name.toLowerCase();
        }
    }
    
    public enum WindowingBound
    {
        UNBOUNDED_PRECEDING("UNBOUNDED PRECEDING"), 
        PRECEDING("PRECEDING"), 
        CURRENT_ROW("CURRENT ROW"), 
        FOLLOWING("FOLLOWING"), 
        UNBOUNDED_FOLLOWING("UNBOUNDED FOLLOWING");
        
        public final String name;
        public final String name_lower;
        
        private WindowingBound(final String name) {
            this.name = name;
            this.name_lower = name.toLowerCase();
        }
    }
}
