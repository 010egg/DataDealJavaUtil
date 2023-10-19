// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.FnvHash;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.repository.SchemaObject;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLExprTableSource extends SQLTableSourceImpl implements SQLReplaceable
{
    protected SQLExpr expr;
    protected List<SQLName> partitions;
    protected SQLTableSampling sampling;
    protected SchemaObject schemaObject;
    protected List<SQLName> columns;
    
    public SQLExprTableSource() {
    }
    
    public SQLExprTableSource(final String tableName) {
        this(SQLUtils.toSQLExpr(tableName), null);
    }
    
    public SQLExprTableSource(final SQLExpr expr) {
        this(expr, null);
    }
    
    public SQLExprTableSource(final SQLExpr expr, final String alias) {
        this.setExpr(expr);
        this.setAlias(alias);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.expr = x;
    }
    
    public void setExpr(final String name) {
        this.setExpr(new SQLIdentifierExpr(name));
    }
    
    public SQLTableSampling getSampling() {
        return this.sampling;
    }
    
    public void setSampling(final SQLTableSampling x) {
        if (x != null) {
            x.setParent(this);
        }
        this.sampling = x;
    }
    
    public SQLName getName() {
        if (this.expr instanceof SQLName) {
            return (SQLName)this.expr;
        }
        return null;
    }
    
    public String getTableName(final boolean normalize) {
        final String tableName = this.getTableName();
        if (normalize) {
            return SQLUtils.normalize(tableName);
        }
        return tableName;
    }
    
    public String getTableName() {
        if (this.expr == null) {
            return null;
        }
        if (this.expr instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)this.expr).getName();
        }
        if (this.expr instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)this.expr).getSimpleName();
        }
        return null;
    }
    
    public String getSchema() {
        if (this.expr == null) {
            return null;
        }
        if (!(this.expr instanceof SQLPropertyExpr)) {
            return null;
        }
        final SQLExpr owner = ((SQLPropertyExpr)this.expr).getOwner();
        if (owner instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)owner).getName();
        }
        if (owner instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)owner).getSimpleName();
        }
        if (owner instanceof SQLAllColumnExpr) {
            return "*";
        }
        return null;
    }
    
    public String getCatalog() {
        if (this.expr instanceof SQLPropertyExpr) {
            final SQLExpr owner = ((SQLPropertyExpr)this.expr).getOwner();
            if (owner instanceof SQLPropertyExpr) {
                final SQLExpr catalogExpr = ((SQLPropertyExpr)owner).getOwner();
                if (catalogExpr instanceof SQLIdentifierExpr) {
                    return ((SQLIdentifierExpr)catalogExpr).getName();
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean setCatalog(final String catalog) {
        if (this.expr instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.expr;
            final SQLExpr owner = propertyExpr.getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                if (catalog == null) {
                    return false;
                }
                propertyExpr.setOwner(new SQLPropertyExpr(catalog, ((SQLIdentifierExpr)owner).getName()));
                return true;
            }
            else if (owner instanceof SQLPropertyExpr) {
                final SQLPropertyExpr propertyOwner = (SQLPropertyExpr)owner;
                final SQLExpr propertyOwnerOwner = propertyOwner.getOwner();
                if (propertyOwnerOwner instanceof SQLIdentifierExpr) {
                    if (catalog == null) {
                        propertyExpr.setOwner(((SQLIdentifierExpr)propertyOwnerOwner).getName());
                    }
                    else {
                        propertyOwner.setOwner(new SQLIdentifierExpr(catalog));
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setCatalog(final String catalog, final String schema) {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog is null.");
        }
        if (schema == null) {
            throw new IllegalArgumentException("schema is null.");
        }
        this.setSchema(schema);
        this.setCatalog(catalog);
    }
    
    public void setSchema(final String schema) {
        if (this.expr instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.expr;
            if (StringUtils.isEmpty(schema)) {
                this.setExpr(new SQLIdentifierExpr(propertyExpr.getName()));
            }
            else {
                propertyExpr.setOwner(schema);
            }
        }
        else {
            if (StringUtils.isEmpty(schema)) {
                return;
            }
            final String ident = ((SQLIdentifierExpr)this.expr).getName();
            this.setExpr(new SQLPropertyExpr(schema, ident));
        }
    }
    
    public void setSimpleName(final String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("schema is empty.");
        }
        if (this.expr == null) {
            this.expr = new SQLIdentifierExpr(name);
        }
        else if (this.expr instanceof SQLPropertyExpr) {
            ((SQLPropertyExpr)this.expr).setName(name);
        }
        else {
            this.expr = new SQLIdentifierExpr(name);
        }
    }
    
    public List<SQLName> getPartitions() {
        if (this.partitions == null) {
            this.partitions = new ArrayList<SQLName>(2);
        }
        return this.partitions;
    }
    
    public int getPartitionSize() {
        if (this.partitions == null) {
            return 0;
        }
        return this.partitions.size();
    }
    
    public void addPartition(final SQLName partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        if (this.partitions == null) {
            this.partitions = new ArrayList<SQLName>(2);
        }
        this.partitions.add(partition);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.sampling != null) {
                this.sampling.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public String computeAlias() {
        String alias = this.getAlias();
        if (alias == null && this.expr instanceof SQLName) {
            alias = ((SQLName)this.expr).getSimpleName();
        }
        return SQLUtils.normalize(alias);
    }
    
    @Override
    public SQLExprTableSource clone() {
        final SQLExprTableSource x = new SQLExprTableSource();
        this.cloneTo(x);
        return x;
    }
    
    public void cloneTo(final SQLExprTableSource x) {
        x.alias = this.alias;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.partitions != null) {
            for (final SQLName p : this.partitions) {
                final SQLName p2 = p.clone();
                x.addPartition(p2);
            }
        }
        if (this.schemaObject != null) {
            x.setSchemaObject(this.schemaObject);
        }
        if (this.columns != null) {
            x.columns = new ArrayList<SQLName>(this.columns.size());
            for (final SQLName column : this.columns) {
                final SQLName clonedColumn = column.clone();
                clonedColumn.setParent(x);
                x.columns.add(clonedColumn);
            }
        }
    }
    
    public List<SQLName> getColumns() {
        if (this.columns == null) {
            this.columns = new ArrayList<SQLName>(2);
        }
        return this.columns;
    }
    
    public List<SQLName> getColumnsDirect() {
        return this.columns;
    }
    
    public SchemaObject getSchemaObject() {
        return this.schemaObject;
    }
    
    public void setSchemaObject(final SchemaObject schemaObject) {
        this.schemaObject = schemaObject;
    }
    
    @Override
    public boolean containsAlias(final String alias) {
        final long hashCode64 = FnvHash.hashCode64(alias);
        return this.containsAlias(hashCode64);
    }
    
    public boolean containsAlias(final long aliasHash) {
        if (this.aliasHashCode64() == aliasHash) {
            return true;
        }
        if (this.expr instanceof SQLPropertyExpr) {
            final long exprNameHash = ((SQLPropertyExpr)this.expr).hashCode64();
            if (exprNameHash == aliasHash) {
                return true;
            }
        }
        if (this.expr instanceof SQLName) {
            final long exprNameHash = ((SQLName)this.expr).nameHashCode64();
            return exprNameHash == aliasHash;
        }
        return false;
    }
    
    @Override
    public SQLColumnDefinition findColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columnName);
        return this.findColumn(hash);
    }
    
    @Override
    public SQLColumnDefinition findColumn(final long columnNameHash) {
        final SQLObject object = this.resolveColum(columnNameHash);
        if (object instanceof SQLColumnDefinition) {
            return (SQLColumnDefinition)object;
        }
        return null;
    }
    
    @Override
    public SQLObject resolveColum(final long columnNameHash) {
        if (this.schemaObject != null) {
            final SQLStatement stmt = this.schemaObject.getStatement();
            if (stmt instanceof SQLCreateTableStatement) {
                final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)stmt;
                return createTableStmt.findColumn(columnNameHash);
            }
        }
        SQLObject resolvedOwnerObject = null;
        if (this.expr instanceof SQLIdentifierExpr) {
            resolvedOwnerObject = ((SQLIdentifierExpr)this.expr).getResolvedOwnerObject();
        }
        if (resolvedOwnerObject == null) {
            return resolvedOwnerObject;
        }
        if (resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry) {
            final SQLSelect subQuery = ((SQLWithSubqueryClause.Entry)resolvedOwnerObject).getSubQuery();
            if (subQuery == null) {
                return null;
            }
            final SQLSelectQueryBlock firstQueryBlock = subQuery.getFirstQueryBlock();
            if (firstQueryBlock == null) {
                return null;
            }
            final SQLSelectItem selectItem = firstQueryBlock.findSelectItem(columnNameHash);
            if (selectItem != null) {
                return selectItem;
            }
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columnName);
        return this.findTableSourceWithColumn(hash, columnName, 0);
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnName_hash, final String name, final int option) {
        if (this.schemaObject != null) {
            final SQLStatement stmt = this.schemaObject.getStatement();
            if (stmt instanceof SQLCreateTableStatement) {
                final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)stmt;
                if (createTableStmt.findColumn(columnName_hash) != null) {
                    return this;
                }
            }
        }
        if (this.expr instanceof SQLIdentifierExpr) {
            final SQLTableSource tableSource = ((SQLIdentifierExpr)this.expr).getResolvedTableSource();
            if (tableSource != null) {
                return this;
            }
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSource(final long alias_hash) {
        if (alias_hash == 0L) {
            return null;
        }
        if (this.aliasHashCode64() == alias_hash) {
            return this;
        }
        if (this.expr instanceof SQLName) {
            final long exprNameHash = ((SQLName)this.expr).nameHashCode64();
            if (exprNameHash == alias_hash) {
                return this;
            }
        }
        if (this.expr instanceof SQLPropertyExpr) {
            final long hash = ((SQLPropertyExpr)this.expr).hashCode64();
            if (hash == alias_hash) {
                return this;
            }
        }
        return null;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.expr) {
            this.setExpr(target);
            return true;
        }
        if (this.partitions != null) {
            for (int i = 0; i < this.partitions.size(); ++i) {
                if (this.partitions.get(i) == expr) {
                    target.setParent(this);
                    this.partitions.set(i, (SQLName)target);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public long aliasHashCode64() {
        if (this.alias != null) {
            if (this.aliasHashCode64 == 0L) {
                this.aliasHashCode64 = FnvHash.hashCode64(this.alias);
            }
            return this.aliasHashCode64;
        }
        if (this.expr instanceof SQLName) {
            return ((SQLName)this.expr).nameHashCode64();
        }
        return 0L;
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
        final SQLExprTableSource that = (SQLExprTableSource)o;
        Label_0072: {
            if (this.expr != null) {
                if (this.expr.equals(that.expr)) {
                    break Label_0072;
                }
            }
            else if (that.expr == null) {
                break Label_0072;
            }
            return false;
        }
        Label_0107: {
            if (this.partitions != null) {
                if (this.partitions.equals(that.partitions)) {
                    break Label_0107;
                }
            }
            else if (that.partitions == null) {
                break Label_0107;
            }
            return false;
        }
        Label_0140: {
            if (this.sampling != null) {
                if (this.sampling.equals(that.sampling)) {
                    break Label_0140;
                }
            }
            else if (that.sampling == null) {
                break Label_0140;
            }
            return false;
        }
        if (this.schemaObject != null) {
            if (this.schemaObject.equals(that.schemaObject)) {
                return (this.columns != null) ? this.columns.equals(that.columns) : (that.columns == null);
            }
        }
        else if (that.schemaObject == null) {
            return (this.columns != null) ? this.columns.equals(that.columns) : (that.columns == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.expr != null) ? this.expr.hashCode() : 0);
        result = 31 * result + ((this.partitions != null) ? this.partitions.hashCode() : 0);
        result = 31 * result + ((this.sampling != null) ? this.sampling.hashCode() : 0);
        result = 31 * result + ((this.schemaObject != null) ? this.schemaObject.hashCode() : 0);
        result = 31 * result + ((this.columns != null) ? this.columns.hashCode() : 0);
        return result;
    }
}
