// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Schema
{
    private String catalog;
    private String name;
    protected final Map<Long, SchemaObject> objects;
    protected final Map<Long, SchemaObject> functions;
    private SchemaRepository repository;
    
    protected Schema(final SchemaRepository repository) {
        this(repository, null);
    }
    
    protected Schema(final SchemaRepository repository, final String name) {
        this.objects = new ConcurrentHashMap<Long, SchemaObject>(16, 0.75f, 1);
        this.functions = new ConcurrentHashMap<Long, SchemaObject>(16, 0.75f, 1);
        this.repository = repository;
        this.setName(name);
    }
    
    protected Schema(final SchemaRepository repository, final String catalog, final String name) {
        this.objects = new ConcurrentHashMap<Long, SchemaObject>(16, 0.75f, 1);
        this.functions = new ConcurrentHashMap<Long, SchemaObject>(16, 0.75f, 1);
        this.repository = repository;
        this.catalog = catalog;
        this.name = name;
    }
    
    public SchemaRepository getRepository() {
        return this.repository;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSimpleName() {
        if (this.name == null) {
            return null;
        }
        final int p = this.name.indexOf(46);
        if (p == -1) {
            return this.name;
        }
        final SQLExpr expr = SQLUtils.toSQLExpr(this.name, this.repository.dbType);
        if (expr instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)expr).getSimpleName();
        }
        return this.name.substring(p + 1);
    }
    
    public void setName(final String name) {
        this.name = name;
        if (name != null && name.indexOf(46) != -1) {
            final SQLExpr expr = SQLUtils.toSQLExpr(name, this.repository.dbType);
            if (expr instanceof SQLPropertyExpr) {
                this.catalog = ((SQLPropertyExpr)expr).getOwnernName();
            }
        }
    }
    
    public SchemaObject findTable(final String tableName) {
        final long hashCode64 = FnvHash.hashCode64(tableName);
        return this.findTable(hashCode64);
    }
    
    public SchemaObject findTable(final long nameHashCode64) {
        final SchemaObject object = this.objects.get(nameHashCode64);
        if (object != null && object.getType() == SchemaObjectType.Table) {
            return object;
        }
        return null;
    }
    
    public SchemaObject findView(final String viewName) {
        final long hashCode64 = FnvHash.hashCode64(viewName);
        return this.findView(hashCode64);
    }
    
    public SchemaObject findView(final long nameHashCode64) {
        final SchemaObject object = this.objects.get(nameHashCode64);
        if (object != null && object.getType() == SchemaObjectType.View) {
            return object;
        }
        return null;
    }
    
    public SchemaObject findTableOrView(final String tableName) {
        final long hashCode64 = FnvHash.hashCode64(tableName);
        return this.findTableOrView(hashCode64);
    }
    
    public SchemaObject findTableOrView(final long hashCode64) {
        final SchemaObject object = this.objects.get(hashCode64);
        if (object == null) {
            return null;
        }
        final SchemaObjectType type = object.getType();
        if (type == SchemaObjectType.Table || type == SchemaObjectType.View) {
            return object;
        }
        return null;
    }
    
    public SchemaObject findFunction(String functionName) {
        functionName = SQLUtils.normalize(functionName);
        final String lowerName = functionName.toLowerCase();
        return this.functions.get(lowerName);
    }
    
    public boolean isSequence(final String name) {
        final long nameHashCode64 = FnvHash.hashCode64(name);
        final SchemaObject object = this.objects.get(nameHashCode64);
        return object != null && object.getType() == SchemaObjectType.Sequence;
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final String alias) {
        if (tableSource instanceof SQLExprTableSource) {
            if (StringUtils.equalsIgnoreCase(alias, tableSource.computeAlias())) {
                final SQLExprTableSource exprTableSource = (SQLExprTableSource)tableSource;
                SchemaObject tableObject = exprTableSource.getSchemaObject();
                if (tableObject != null) {
                    return tableObject;
                }
                final SQLExpr expr = exprTableSource.getExpr();
                if (expr instanceof SQLIdentifierExpr) {
                    final long tableNameHashCode64 = ((SQLIdentifierExpr)expr).nameHashCode64();
                    tableObject = this.findTable(tableNameHashCode64);
                    if (tableObject != null) {
                        exprTableSource.setSchemaObject(tableObject);
                    }
                    return tableObject;
                }
                if (expr instanceof SQLPropertyExpr) {
                    final long tableNameHashCode64 = ((SQLPropertyExpr)expr).nameHashCode64();
                    tableObject = this.findTable(tableNameHashCode64);
                    if (tableObject != null) {
                        exprTableSource.setSchemaObject(tableObject);
                    }
                    return tableObject;
                }
            }
            return null;
        }
        if (!(tableSource instanceof SQLJoinTableSource)) {
            return null;
        }
        final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
        final SQLTableSource left = join.getLeft();
        SchemaObject tableObject2 = this.findTable(left, alias);
        if (tableObject2 != null) {
            return tableObject2;
        }
        final SQLTableSource right = join.getRight();
        tableObject2 = this.findTable(right, alias);
        return tableObject2;
    }
    
    public SQLColumnDefinition findColumn(final SQLTableSource tableSource, final SQLSelectItem selectItem) {
        if (selectItem == null) {
            return null;
        }
        return this.findColumn(tableSource, selectItem.getExpr());
    }
    
    public SQLColumnDefinition findColumn(final SQLTableSource tableSource, SQLExpr expr) {
        final SchemaObject object = this.findTable(tableSource, expr);
        if (object != null) {
            if (expr instanceof SQLAggregateExpr) {
                final SQLAggregateExpr aggregateExpr = (SQLAggregateExpr)expr;
                final String function = aggregateExpr.getMethodName();
                if ("min".equalsIgnoreCase(function) || "max".equalsIgnoreCase(function)) {
                    final SQLExpr arg = expr = aggregateExpr.getArguments().get(0);
                }
            }
            if (expr instanceof SQLName) {
                return object.findColumn(((SQLName)expr).getSimpleName());
            }
        }
        return null;
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final SQLSelectItem selectItem) {
        if (selectItem == null) {
            return null;
        }
        return this.findTable(tableSource, selectItem.getExpr());
    }
    
    public SchemaObject findTable(final SQLTableSource tableSource, final SQLExpr expr) {
        if (expr instanceof SQLAggregateExpr) {
            final SQLAggregateExpr aggregateExpr = (SQLAggregateExpr)expr;
            final String function = aggregateExpr.getMethodName();
            if ("min".equalsIgnoreCase(function) || "max".equalsIgnoreCase(function)) {
                final SQLExpr arg = aggregateExpr.getArguments().get(0);
                return this.findTable(tableSource, arg);
            }
        }
        if (expr instanceof SQLPropertyExpr) {
            final String ownerName = ((SQLPropertyExpr)expr).getOwnernName();
            return this.findTable(tableSource, ownerName);
        }
        if (!(expr instanceof SQLAllColumnExpr) && !(expr instanceof SQLIdentifierExpr)) {
            return null;
        }
        if (tableSource instanceof SQLExprTableSource) {
            return this.findTable(tableSource, tableSource.computeAlias());
        }
        if (tableSource instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            SchemaObject table = this.findTable(join.getLeft(), expr);
            if (table == null) {
                table = this.findTable(join.getRight(), expr);
            }
            return table;
        }
        return null;
    }
    
    public Map<String, SchemaObject> getTables(final SQLTableSource x) {
        final Map<String, SchemaObject> tables = new LinkedHashMap<String, SchemaObject>();
        this.computeTables(x, tables);
        return tables;
    }
    
    protected void computeTables(final SQLTableSource x, final Map<String, SchemaObject> tables) {
        if (x == null) {
            return;
        }
        if (x instanceof SQLExprTableSource) {
            final SQLExprTableSource exprTableSource = (SQLExprTableSource)x;
            final SQLExpr expr = exprTableSource.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final long tableNameHashCode64 = ((SQLIdentifierExpr)expr).nameHashCode64();
                final String tableName = ((SQLIdentifierExpr)expr).getName();
                SchemaObject table = exprTableSource.getSchemaObject();
                if (table == null) {
                    table = this.findTable(tableNameHashCode64);
                    if (table != null) {
                        exprTableSource.setSchemaObject(table);
                    }
                }
                if (table != null) {
                    tables.put(tableName, table);
                    final String alias = x.getAlias();
                    if (alias != null && !alias.equalsIgnoreCase(tableName)) {
                        tables.put(alias, table);
                    }
                }
            }
            return;
        }
        if (x instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)x;
            this.computeTables(join.getLeft(), tables);
            this.computeTables(join.getRight(), tables);
        }
    }
    
    public int getTableCount() {
        int count = 0;
        for (final SchemaObject object : this.objects.values()) {
            if (object.getType() == SchemaObjectType.Table) {
                ++count;
            }
        }
        return count;
    }
    
    public Collection<SchemaObject> getObjects() {
        return this.objects.values();
    }
    
    public boolean removeObject(final Long nameHashCode64) {
        return this.objects.remove(nameHashCode64) != null;
    }
    
    public int getViewCount() {
        int count = 0;
        for (final SchemaObject object : this.objects.values()) {
            if (object.getType() == SchemaObjectType.View) {
                ++count;
            }
        }
        return count;
    }
    
    public List<String> showTables() {
        final List<String> tables = new ArrayList<String>(this.objects.size());
        for (final SchemaObject object : this.objects.values()) {
            if (object.getType() == SchemaObjectType.Table) {
                tables.add(object.getName());
            }
        }
        Collections.sort(tables);
        return tables;
    }
    
    public String getCatalog() {
        return this.catalog;
    }
}
