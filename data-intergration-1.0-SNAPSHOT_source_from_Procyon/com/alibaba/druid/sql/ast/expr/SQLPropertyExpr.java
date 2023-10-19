// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExprComparor;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLPropertyExpr extends SQLExprImpl implements SQLName, SQLReplaceable, Comparable<SQLPropertyExpr>
{
    private SQLExpr owner;
    private String name;
    protected long nameHashCod64;
    protected long hashCode64;
    protected SQLObject resolvedColumn;
    protected SQLObject resolvedOwnerObject;
    
    public SQLPropertyExpr(final String owner2, final String owner, final String name) {
        this(new SQLPropertyExpr(owner2, owner), name);
    }
    
    public SQLPropertyExpr(final String owner, final String name) {
        this(new SQLIdentifierExpr(owner), name);
    }
    
    public SQLPropertyExpr(final SQLExpr owner, final String name) {
        this.setOwner(owner);
        this.name = name;
    }
    
    public SQLPropertyExpr(final SQLExpr owner, final String name, final long nameHashCod64) {
        this.setOwner(owner);
        this.name = name;
        this.nameHashCod64 = nameHashCod64;
    }
    
    public SQLPropertyExpr() {
    }
    
    @Override
    public String getSimpleName() {
        return this.name;
    }
    
    public SQLExpr getOwner() {
        return this.owner;
    }
    
    @Deprecated
    public String getOwnernName() {
        if (this.owner instanceof SQLName) {
            return ((SQLName)this.owner).toString();
        }
        return null;
    }
    
    public String getOwnerName() {
        if (this.owner instanceof SQLName) {
            return ((SQLName)this.owner).toString();
        }
        return null;
    }
    
    public void setOwner(final SQLExpr owner) {
        if (owner != null) {
            owner.setParent(this);
        }
        if (this.parent instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.parent;
            propertyExpr.computeHashCode64();
        }
        this.owner = owner;
        this.hashCode64 = 0L;
    }
    
    protected void computeHashCode64() {
        long hash;
        if (this.owner instanceof SQLName) {
            hash = ((SQLName)this.owner).hashCode64();
            hash ^= 0x2EL;
            hash *= 1099511628211L;
        }
        else if (this.owner == null) {
            hash = -3750763034362895579L;
        }
        else {
            hash = FnvHash.fnv1a_64_lower(this.owner.toString());
            hash ^= 0x2EL;
            hash *= 1099511628211L;
        }
        hash = FnvHash.hashCode64(hash, this.name);
        this.hashCode64 = hash;
    }
    
    public void setOwner(final String owner) {
        if (owner == null) {
            this.owner = null;
            return;
        }
        if (owner.indexOf(46) != -1) {
            final SQLExpr ownerExpr = SQLUtils.toSQLExpr(owner);
            this.setOwner(ownerExpr);
        }
        else {
            this.setOwner(new SQLIdentifierExpr(owner));
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
        this.hashCode64 = 0L;
        this.nameHashCod64 = 0L;
        if (this.parent instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.parent;
            propertyExpr.computeHashCode64();
        }
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            this.owner.output(buf);
            buf.append(".");
            buf.append(this.name);
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.owner != null) {
            this.owner.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return Collections.singletonList(this.owner);
    }
    
    @Override
    public int hashCode() {
        final long hash = this.hashCode64();
        return (int)(hash ^ hash >>> 32);
    }
    
    @Override
    public long hashCode64() {
        if (this.hashCode64 == 0L) {
            this.computeHashCode64();
        }
        return this.hashCode64;
    }
    
    public boolean equals(final SQLIdentifierExpr other) {
        return other != null && this.nameHashCode64() == other.nameHashCode64() && this.resolvedOwnerObject != null && this.resolvedOwnerObject == other.getResolvedOwnerObject() && this.resolvedColumn != null && this.resolvedColumn == other.getResolvedColumn();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SQLPropertyExpr)) {
            return false;
        }
        final SQLPropertyExpr other = (SQLPropertyExpr)obj;
        if (this.nameHashCode64() != other.nameHashCode64()) {
            return false;
        }
        if (this.owner == null) {
            if (other.owner != null) {
                return false;
            }
        }
        else if (!this.owner.equals(other.owner)) {
            return false;
        }
        return true;
    }
    
    @Override
    public SQLPropertyExpr clone() {
        SQLExpr owner_x = null;
        if (this.owner != null) {
            owner_x = this.owner.clone();
        }
        final SQLPropertyExpr x = new SQLPropertyExpr(owner_x, this.name, this.nameHashCod64);
        x.hashCode64 = this.hashCode64;
        x.resolvedColumn = this.resolvedColumn;
        x.resolvedOwnerObject = this.resolvedOwnerObject;
        return x;
    }
    
    public boolean matchOwner(final String alias) {
        return this.owner instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)this.owner).getName().equalsIgnoreCase(alias);
    }
    
    @Override
    public long nameHashCode64() {
        if (this.nameHashCod64 == 0L && this.name != null) {
            this.nameHashCod64 = FnvHash.hashCode64(this.name);
        }
        return this.nameHashCod64;
    }
    
    public String normalizedName() {
        String ownerName;
        if (this.owner instanceof SQLIdentifierExpr) {
            ownerName = ((SQLIdentifierExpr)this.owner).normalizedName();
        }
        else if (this.owner instanceof SQLPropertyExpr) {
            ownerName = ((SQLPropertyExpr)this.owner).normalizedName();
        }
        else {
            ownerName = this.owner.toString();
        }
        return ownerName + '.' + SQLUtils.normalize(this.name);
    }
    
    @Override
    public SQLColumnDefinition getResolvedColumn() {
        if (this.resolvedColumn instanceof SQLColumnDefinition) {
            return (SQLColumnDefinition)this.resolvedColumn;
        }
        if (this.resolvedColumn instanceof SQLSelectItem) {
            final SQLSelectItem selectItem = (SQLSelectItem)this.resolvedColumn;
            final SQLExpr expr = selectItem.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                return ((SQLIdentifierExpr)expr).getResolvedColumn();
            }
            if (expr instanceof SQLPropertyExpr) {
                return ((SQLPropertyExpr)expr).getResolvedColumn();
            }
        }
        return null;
    }
    
    public void setResolvedColumn(final SQLColumnDefinition resolvedColumn) {
        this.resolvedColumn = resolvedColumn;
    }
    
    public void setResolvedColumn(final SQLSelectItem resolvedColumn) {
        this.resolvedColumn = resolvedColumn;
    }
    
    public SQLTableSource getResolvedTableSource() {
        if (this.resolvedOwnerObject instanceof SQLTableSource) {
            return (SQLTableSource)this.resolvedOwnerObject;
        }
        return null;
    }
    
    public void setResolvedTableSource(final SQLTableSource resolvedTableSource) {
        this.resolvedOwnerObject = resolvedTableSource;
    }
    
    public void setResolvedProcedure(final SQLCreateProcedureStatement stmt) {
        this.resolvedOwnerObject = stmt;
    }
    
    public void setResolvedOwnerObject(final SQLObject resolvedOwnerObject) {
        this.resolvedOwnerObject = resolvedOwnerObject;
    }
    
    public SQLCreateProcedureStatement getResolvedProcudure() {
        if (this.resolvedOwnerObject instanceof SQLCreateProcedureStatement) {
            return (SQLCreateProcedureStatement)this.resolvedOwnerObject;
        }
        return null;
    }
    
    public SQLObject getResolvedOwnerObject() {
        return this.resolvedOwnerObject;
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.resolvedColumn instanceof SQLColumnDefinition && this.resolvedColumn != null) {
            return ((SQLColumnDefinition)this.resolvedColumn).getDataType();
        }
        if (this.resolvedColumn instanceof SQLSelectItem && this.resolvedColumn != null) {
            return ((SQLSelectItem)this.resolvedColumn).computeDataType();
        }
        if (this.resolvedOwnerObject == null) {
            return null;
        }
        if (this.resolvedOwnerObject instanceof SQLSubqueryTableSource) {
            final SQLSelect select = ((SQLSubqueryTableSource)this.resolvedOwnerObject).getSelect();
            final SQLSelectQueryBlock queryBlock = select.getFirstQueryBlock();
            if (queryBlock == null) {
                return null;
            }
            final SQLSelectItem selectItem = queryBlock.findSelectItem(this.nameHashCode64());
            if (selectItem != null) {
                return selectItem.computeDataType();
            }
        }
        else if (this.resolvedOwnerObject instanceof SQLUnionQueryTableSource) {
            final SQLSelectQueryBlock queryBlock2 = ((SQLUnionQueryTableSource)this.resolvedOwnerObject).getUnion().getFirstQueryBlock();
            if (queryBlock2 == null) {
                return null;
            }
            final SQLSelectItem selectItem2 = queryBlock2.findSelectItem(this.nameHashCode64());
            if (selectItem2 != null) {
                return selectItem2.computeDataType();
            }
        }
        else if (this.resolvedOwnerObject instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)this.resolvedOwnerObject).getExpr();
            if (expr != null) {}
        }
        return null;
    }
    
    public boolean nameEquals(final String name) {
        return SQLUtils.nameEquals(this.name, name);
    }
    
    public SQLPropertyExpr simplify() {
        final String normalizedName = SQLUtils.normalize(this.name);
        SQLExpr normalizedOwner = this.owner;
        if (normalizedOwner instanceof SQLIdentifierExpr) {
            normalizedOwner = ((SQLIdentifierExpr)normalizedOwner).simplify();
        }
        if (normalizedName != this.name || normalizedOwner != this.owner) {
            return new SQLPropertyExpr(normalizedOwner, normalizedName, this.hashCode64);
        }
        return this;
    }
    
    @Override
    public String toString() {
        if (this.owner == null) {
            return this.name;
        }
        return this.owner.toString() + '.' + this.name;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.owner) {
            this.setOwner(target);
            return true;
        }
        return false;
    }
    
    @Override
    public int compareTo(final SQLPropertyExpr o) {
        final int r = SQLExprComparor.compareTo(this.owner, o.owner);
        if (r != 0) {
            return r;
        }
        return this.name.compareTo(o.name);
    }
}
