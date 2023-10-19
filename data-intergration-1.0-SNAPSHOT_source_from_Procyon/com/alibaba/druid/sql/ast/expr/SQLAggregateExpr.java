// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLKeep;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import java.io.Serializable;

public class SQLAggregateExpr extends SQLMethodInvokeExpr implements Serializable, SQLReplaceable
{
    private static final long serialVersionUID = 1L;
    protected SQLAggregateOption option;
    protected SQLKeep keep;
    protected SQLExpr filter;
    protected SQLOver over;
    protected SQLName overRef;
    protected SQLOrderBy orderBy;
    protected boolean withinGroup;
    protected Boolean ignoreNulls;
    
    public SQLAggregateExpr(final String methodName) {
        this.withinGroup = false;
        this.ignoreNulls = false;
        this.methodName = methodName;
    }
    
    public SQLAggregateExpr(final String methodName, final SQLAggregateOption option) {
        this.withinGroup = false;
        this.ignoreNulls = false;
        this.methodName = methodName;
        this.option = option;
    }
    
    public SQLAggregateExpr(final String methodName, final SQLAggregateOption option, final SQLExpr... arguments) {
        this.withinGroup = false;
        this.ignoreNulls = false;
        this.methodName = methodName;
        this.option = option;
        if (arguments != null) {
            for (final SQLExpr argument : arguments) {
                if (argument != null) {
                    this.addArgument(argument);
                }
            }
        }
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }
    
    public SQLAggregateOption getOption() {
        return this.option;
    }
    
    public void setOption(final SQLAggregateOption option) {
        this.option = option;
    }
    
    public boolean isDistinct() {
        return this.option == SQLAggregateOption.DISTINCT;
    }
    
    public SQLOver getOver() {
        return this.over;
    }
    
    public void setOver(final SQLOver x) {
        if (x != null) {
            x.setParent(this);
        }
        this.over = x;
    }
    
    public SQLName getOverRef() {
        return this.overRef;
    }
    
    public void setOverRef(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.overRef = x;
    }
    
    public SQLKeep getKeep() {
        return this.keep;
    }
    
    public void setKeep(final SQLKeep keep) {
        if (keep != null) {
            keep.setParent(this);
        }
        this.keep = keep;
    }
    
    public boolean isWithinGroup() {
        return this.withinGroup;
    }
    
    public void setWithinGroup(final boolean withinGroup) {
        this.withinGroup = withinGroup;
    }
    
    @Deprecated
    public SQLOrderBy getWithinGroup() {
        return this.orderBy;
    }
    
    public boolean isIgnoreNulls() {
        return this.ignoreNulls != null && this.ignoreNulls;
    }
    
    public Boolean getIgnoreNulls() {
        return this.ignoreNulls;
    }
    
    public void setIgnoreNulls(final boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.owner != null) {
                this.owner.accept(visitor);
            }
            for (final SQLExpr arg : this.arguments) {
                if (arg != null) {
                    arg.accept(visitor);
                }
            }
            if (this.keep != null) {
                this.keep.accept(visitor);
            }
            if (this.filter != null) {
                this.filter.accept(visitor);
            }
            if (this.over != null) {
                this.over.accept(visitor);
            }
            if (this.overRef != null) {
                this.overRef.accept(visitor);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.arguments);
        if (this.keep != null) {
            children.add(this.keep);
        }
        if (this.over != null) {
            children.add(this.over);
        }
        if (this.orderBy != null) {
            children.add(this.orderBy);
        }
        return children;
    }
    
    public SQLExpr getFilter() {
        return this.filter;
    }
    
    public void setFilter(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.filter = x;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final SQLAggregateExpr that = (SQLAggregateExpr)o;
        if (this.option != that.option) {
            return false;
        }
        Label_0085: {
            if (this.keep != null) {
                if (this.keep.equals(that.keep)) {
                    break Label_0085;
                }
            }
            else if (that.keep == null) {
                break Label_0085;
            }
            return false;
        }
        Label_0118: {
            if (this.filter != null) {
                if (this.filter.equals(that.filter)) {
                    break Label_0118;
                }
            }
            else if (that.filter == null) {
                break Label_0118;
            }
            return false;
        }
        Label_0151: {
            if (this.over != null) {
                if (this.over.equals(that.over)) {
                    break Label_0151;
                }
            }
            else if (that.over == null) {
                break Label_0151;
            }
            return false;
        }
        Label_0184: {
            if (this.overRef != null) {
                if (this.overRef.equals(that.overRef)) {
                    break Label_0184;
                }
            }
            else if (that.overRef == null) {
                break Label_0184;
            }
            return false;
        }
        if (this.orderBy != null) {
            if (this.orderBy.equals(that.orderBy)) {
                return (this.ignoreNulls != null) ? this.ignoreNulls.equals(that.ignoreNulls) : (that.ignoreNulls == null);
            }
        }
        else if (that.orderBy == null) {
            return (this.ignoreNulls != null) ? this.ignoreNulls.equals(that.ignoreNulls) : (that.ignoreNulls == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.option != null) ? this.option.hashCode() : 0);
        result = 31 * result + ((this.keep != null) ? this.keep.hashCode() : 0);
        result = 31 * result + ((this.filter != null) ? this.filter.hashCode() : 0);
        result = 31 * result + ((this.over != null) ? this.over.hashCode() : 0);
        result = 31 * result + ((this.overRef != null) ? this.overRef.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + ((this.ignoreNulls != null) ? this.ignoreNulls.hashCode() : 0);
        return result;
    }
    
    @Override
    public SQLAggregateExpr clone() {
        final SQLAggregateExpr x = new SQLAggregateExpr(this.methodName);
        x.option = this.option;
        for (final SQLExpr arg : this.arguments) {
            x.addArgument(arg.clone());
        }
        if (this.keep != null) {
            x.setKeep(this.keep.clone());
        }
        if (this.over != null) {
            x.setOver(this.over.clone());
        }
        if (this.overRef != null) {
            x.setOverRef(this.overRef.clone());
        }
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        x.ignoreNulls = this.ignoreNulls;
        if (this.attributes != null) {
            for (final Map.Entry<String, Object> entry : this.attributes.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof SQLObject) {
                    value = ((SQLObject)value).clone();
                }
                x.putAttribute(key, value);
            }
        }
        return x;
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.resolvedReturnDataType != null) {
            return this.resolvedReturnDataType;
        }
        final long hash = this.methodNameHashCode64();
        if (hash == FnvHash.Constants.COUNT || hash == FnvHash.Constants.ROW_NUMBER) {
            return SQLIntegerExpr.DATA_TYPE;
        }
        if (this.arguments.size() > 0) {
            final SQLDataType dataType = this.arguments.get(0).computeDataType();
            if (dataType != null && dataType.nameHashCode64() != FnvHash.Constants.BOOLEAN) {
                return dataType;
            }
        }
        if (hash == FnvHash.Constants.SUM) {
            return SQLNumberExpr.DATA_TYPE_DOUBLE;
        }
        if (hash == FnvHash.Constants.WM_CONCAT || hash == FnvHash.Constants.GROUP_CONCAT) {
            return SQLCharExpr.DATA_TYPE;
        }
        return null;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (target == null) {
            return false;
        }
        for (int i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == expr) {
                this.arguments.set(i, target);
                target.setParent(this);
                return true;
            }
        }
        if (this.overRef == expr) {
            this.setOverRef((SQLName)target);
            return true;
        }
        if (this.filter != null) {
            (this.filter = target).setParent(this);
        }
        return false;
    }
}
