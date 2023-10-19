// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLIdentifierExpr extends SQLExprImpl implements SQLName, Comparable<SQLIdentifierExpr>
{
    protected String name;
    private long hashCode64;
    private SQLObject resolvedColumn;
    private SQLObject resolvedOwnerObject;
    
    public SQLIdentifierExpr() {
    }
    
    public SQLIdentifierExpr(final String name) {
        this.name = name;
    }
    
    public SQLIdentifierExpr(final String name, final long hash_lower) {
        this.name = name;
        this.hashCode64 = hash_lower;
    }
    
    @Override
    public String getSimpleName() {
        return this.name;
    }
    
    public String getLowerName() {
        if (this.name == null) {
            return null;
        }
        return this.name.toLowerCase();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
        this.hashCode64 = 0L;
        if (this.parent instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.parent;
            propertyExpr.computeHashCode64();
        }
    }
    
    @Override
    public long nameHashCode64() {
        return this.hashCode64();
    }
    
    @Override
    public long hashCode64() {
        if (this.hashCode64 == 0L && this.name != null) {
            this.hashCode64 = FnvHash.hashCode64(this.name);
        }
        return this.hashCode64;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append(this.name);
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final long value = this.hashCode64();
        return (int)(value ^ value >>> 32);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SQLIdentifierExpr)) {
            return false;
        }
        final SQLIdentifierExpr other = (SQLIdentifierExpr)obj;
        return this.hashCode64() == other.hashCode64();
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public SQLIdentifierExpr clone() {
        final SQLIdentifierExpr x = new SQLIdentifierExpr(this.name, this.hashCode64);
        x.resolvedColumn = this.resolvedColumn;
        x.resolvedOwnerObject = this.resolvedOwnerObject;
        if (this.hint != null) {
            x.hint = this.hint.clone();
        }
        return x;
    }
    
    public SQLIdentifierExpr simplify() {
        final String normalized = SQLUtils.normalize(this.name);
        if (normalized != this.name) {
            return new SQLIdentifierExpr(normalized, this.hashCode64);
        }
        return this;
    }
    
    public String normalizedName() {
        return SQLUtils.normalize(this.name);
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
    
    public SQLSelectItem getResolvedSelectItem() {
        if (this.resolvedColumn instanceof SQLSelectItem) {
            return (SQLSelectItem)this.resolvedColumn;
        }
        return null;
    }
    
    public SQLObject getResolvedColumnObject() {
        return this.resolvedColumn;
    }
    
    public void setResolvedColumn(final SQLColumnDefinition resolvedColumn) {
        this.resolvedColumn = resolvedColumn;
    }
    
    public void setResolvedColumn(final SQLSelectItem selectItem) {
        this.resolvedColumn = selectItem;
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
    
    public SQLObject getResolvedOwnerObject() {
        return this.resolvedOwnerObject;
    }
    
    public void setResolvedOwnerObject(final SQLObject resolvedOwnerObject) {
        this.resolvedOwnerObject = resolvedOwnerObject;
    }
    
    public SQLParameter getResolvedParameter() {
        if (this.resolvedColumn instanceof SQLParameter) {
            return (SQLParameter)this.resolvedColumn;
        }
        return null;
    }
    
    public void setResolvedParameter(final SQLParameter resolvedParameter) {
        this.resolvedColumn = resolvedParameter;
    }
    
    public SQLDeclareItem getResolvedDeclareItem() {
        if (this.resolvedColumn instanceof SQLDeclareItem) {
            return (SQLDeclareItem)this.resolvedColumn;
        }
        return null;
    }
    
    public void setResolvedDeclareItem(final SQLDeclareItem resolvedDeclareItem) {
        this.resolvedColumn = resolvedDeclareItem;
    }
    
    @Override
    public SQLDataType computeDataType() {
        final SQLColumnDefinition resolvedColumn = this.getResolvedColumn();
        if (resolvedColumn != null) {
            return resolvedColumn.getDataType();
        }
        if (this.resolvedColumn instanceof SQLSelectItem) {
            return ((SQLSelectItem)this.resolvedColumn).computeDataType();
        }
        SQLSelectQueryBlock queryBlock = null;
        if (this.resolvedOwnerObject instanceof SQLSubqueryTableSource) {
            final SQLSelect select = ((SQLSubqueryTableSource)this.resolvedOwnerObject).getSelect();
            queryBlock = select.getFirstQueryBlock();
        }
        else if (this.resolvedOwnerObject instanceof SQLUnionQueryTableSource) {
            final SQLUnionQuery union = ((SQLUnionQueryTableSource)this.resolvedOwnerObject).getUnion();
            queryBlock = union.getFirstQueryBlock();
        }
        else if (this.resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry) {
            queryBlock = ((SQLWithSubqueryClause.Entry)this.resolvedOwnerObject).getSubQuery().getFirstQueryBlock();
        }
        if (queryBlock != null) {
            final SQLSelectItem selectItem = queryBlock.findSelectItem(this.nameHashCode64());
            if (selectItem != null) {
                return selectItem.computeDataType();
            }
        }
        return null;
    }
    
    public boolean nameEquals(final String name) {
        return SQLUtils.nameEquals(this.name, name);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    public static boolean matchIgnoreCase(final SQLExpr expr, final String name) {
        if (!(expr instanceof SQLIdentifierExpr)) {
            return false;
        }
        final SQLIdentifierExpr ident = (SQLIdentifierExpr)expr;
        return ident.getName().equalsIgnoreCase(name);
    }
    
    @Override
    public int compareTo(final SQLIdentifierExpr o) {
        return this.normalizedName().compareTo(o.normalizedName());
    }
}
