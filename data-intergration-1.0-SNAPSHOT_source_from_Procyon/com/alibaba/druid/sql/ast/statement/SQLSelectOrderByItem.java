// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.List;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public final class SQLSelectOrderByItem extends SQLObjectImpl implements SQLReplaceable
{
    protected SQLExpr expr;
    protected String collate;
    protected SQLOrderingSpecification type;
    protected NullsOrderType nullsOrderType;
    protected transient SQLSelectItem resolvedSelectItem;
    
    public SQLSelectOrderByItem() {
    }
    
    public SQLSelectOrderByItem(final SQLExpr expr) {
        this.setExpr(expr);
    }
    
    public SQLSelectOrderByItem(final SQLExpr expr, final SQLOrderingSpecification type) {
        this.setExpr(expr);
        this.type = type;
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
    
    public String getCollate() {
        return this.collate;
    }
    
    public void setCollate(final String collate) {
        this.collate = collate;
    }
    
    public SQLOrderingSpecification getType() {
        return this.type;
    }
    
    public void setType(final SQLOrderingSpecification type) {
        this.type = type;
    }
    
    public NullsOrderType getNullsOrderType() {
        return this.nullsOrderType;
    }
    
    public void setNullsOrderType(final NullsOrderType nullsOrderType) {
        this.nullsOrderType = nullsOrderType;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this) && this.expr != null) {
            this.expr.accept(v);
        }
        v.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.collate == null) ? 0 : this.collate.hashCode());
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        final SQLSelectOrderByItem other = (SQLSelectOrderByItem)obj;
        if (this.collate == null) {
            if (other.collate != null) {
                return false;
            }
        }
        else if (!this.collate.equals(other.collate)) {
            return false;
        }
        if (this.expr == null) {
            if (other.expr != null) {
                return false;
            }
        }
        else if (!this.expr.equals(other.expr)) {
            return false;
        }
        return this.type == other.type;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            if (target instanceof SQLIntegerExpr && this.parent instanceof SQLOrderBy) {
                ((SQLOrderBy)this.parent).getItems().remove(this);
            }
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    public SQLSelectOrderByItem clone() {
        final SQLSelectOrderByItem x = new SQLSelectOrderByItem();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.collate = this.collate;
        x.type = this.type;
        x.nullsOrderType = this.nullsOrderType;
        return x;
    }
    
    public SQLSelectItem getResolvedSelectItem() {
        return this.resolvedSelectItem;
    }
    
    public void setResolvedSelectItem(final SQLSelectItem resolvedSelectItem) {
        this.resolvedSelectItem = resolvedSelectItem;
    }
    
    public boolean isClusterBy() {
        if (this.parent instanceof SQLCreateTableStatement) {
            final List<SQLSelectOrderByItem> clusteredBy = ((SQLCreateTableStatement)this.parent).getClusteredBy();
            return clusteredBy.indexOf(this) != -1;
        }
        if (this.parent instanceof SQLSelectQueryBlock) {
            final List<SQLSelectOrderByItem> clusterBy = ((SQLSelectQueryBlock)this.parent).getClusterByDirect();
            return clusterBy != null && clusterBy.indexOf(this) != -1;
        }
        return false;
    }
    
    public boolean isSortBy() {
        if (this.parent instanceof SQLCreateTableStatement) {
            final List<SQLSelectOrderByItem> sortedBy = ((SQLCreateTableStatement)this.parent).getSortedBy();
            return sortedBy.indexOf(this) != -1;
        }
        if (this.parent instanceof SQLSelectQueryBlock) {
            final List<SQLSelectOrderByItem> sortedBy = ((SQLSelectQueryBlock)this.parent).getSortByDirect();
            return sortedBy != null && sortedBy.indexOf(this) != -1;
        }
        return false;
    }
    
    public boolean isDistributeBy() {
        if (this.parent instanceof SQLSelectQueryBlock) {
            final List<SQLSelectOrderByItem> distributeBy = ((SQLSelectQueryBlock)this.parent).getDistributeBy();
            return distributeBy.indexOf(this) != -1;
        }
        return false;
    }
    
    public enum NullsOrderType
    {
        NullsFirst, 
        NullsLast;
        
        public String toFormalString() {
            if (NullsOrderType.NullsFirst.equals(this)) {
                return "NULLS FIRST";
            }
            if (NullsOrderType.NullsLast.equals(this)) {
                return "NULLS LAST";
            }
            throw new IllegalArgumentException();
        }
    }
}
