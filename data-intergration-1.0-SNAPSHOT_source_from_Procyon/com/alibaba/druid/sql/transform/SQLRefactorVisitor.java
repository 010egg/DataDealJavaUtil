// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.repository.SchemaObject;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.HashMap;
import com.alibaba.druid.DbType;
import java.util.Map;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class SQLRefactorVisitor extends SQLASTVisitorAdapter
{
    private int havingLevel;
    private int groupByLevel;
    private char quote;
    private Map<Long, TableMapping> tableMappings;
    
    public SQLRefactorVisitor(final DbType dbType) {
        this.havingLevel = 0;
        this.groupByLevel = 0;
        this.quote = '\"';
        this.tableMappings = new HashMap<Long, TableMapping>();
        this.dbType = dbType;
        switch (dbType) {
            case mysql:
            case mariadb:
            case ads: {
                this.quote = '`';
                break;
            }
        }
    }
    
    public void addMapping(final TableMapping mapping) {
        this.tableMappings.put(mapping.getSrcTableHash(), mapping);
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        final TableMapping mapping = this.findMapping(x);
        if (mapping == null) {
            return true;
        }
        final String destTable = mapping.getDestTable();
        x.setExpr(new SQLIdentifierExpr(this.quote(destTable)));
        return false;
    }
    
    private TableMapping findMapping(final SQLExprTableSource x) {
        final SchemaObject schemaObject = x.getSchemaObject();
        if (schemaObject == null) {
            return null;
        }
        final long nameHashCode = FnvHash.hashCode64(schemaObject.getName());
        return this.tableMappings.get(nameHashCode);
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        TableMapping mapping = null;
        if (this.groupByLevel > 0 || this.havingLevel > 0) {
            SQLSelectQueryBlock queryBlock = null;
            for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelectQueryBlock) {
                    queryBlock = (SQLSelectQueryBlock)parent;
                    break;
                }
            }
            boolean matchAlias = false;
            if (queryBlock != null) {
                for (final SQLSelectItem item : queryBlock.getSelectList()) {
                    if (item.alias_hash() == x.hashCode64()) {
                        matchAlias = true;
                        break;
                    }
                }
            }
            if (matchAlias) {
                SQLObject parent2 = x.getParent();
                if (parent2 instanceof SQLOrderBy || parent2 instanceof SQLSelectGroupByClause) {
                    return false;
                }
                if (this.havingLevel > 0) {
                    boolean agg = false;
                    while (parent2 != null) {
                        if (parent2 instanceof SQLSelectQueryBlock) {
                            break;
                        }
                        if (parent2 instanceof SQLAggregateExpr) {
                            agg = true;
                            break;
                        }
                        parent2 = parent2.getParent();
                    }
                    if (!agg) {
                        return false;
                    }
                }
            }
        }
        final SQLObject ownerObject = x.getResolvedOwnerObject();
        if (ownerObject instanceof SQLExprTableSource) {
            mapping = this.findMapping((SQLExprTableSource)ownerObject);
        }
        if (mapping == null) {
            return false;
        }
        final String srcName = x.getName();
        final String mappingColumn = mapping.getMappingColumn(srcName);
        if (mappingColumn != null) {
            x.setName(this.quote(mappingColumn));
        }
        final SQLObject parent3 = x.getParent();
        if (parent3 instanceof SQLSelectItem && ((SQLSelectItem)parent3).getAlias() == null) {
            ((SQLSelectItem)parent3).setAlias(srcName);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectGroupByClause x) {
        ++this.groupByLevel;
        for (final SQLExpr item : x.getItems()) {
            item.accept(this);
        }
        final SQLExpr having = x.getHaving();
        if (having != null) {
            ++this.havingLevel;
            having.accept(this);
            --this.havingLevel;
        }
        --this.groupByLevel;
        return false;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        TableMapping mapping = null;
        SchemaObject schemaObject = null;
        boolean aliasOwer = false;
        final SQLObject ownerObject = x.getResolvedOwnerObject();
        if (ownerObject instanceof SQLExprTableSource) {
            final SQLExprTableSource exprTableSource = (SQLExprTableSource)ownerObject;
            if (exprTableSource.getAlias() != null && x.getOwner() instanceof SQLIdentifierExpr && FnvHash.hashCode64(exprTableSource.getAlias()) == ((SQLIdentifierExpr)x.getOwner()).nameHashCode64()) {
                aliasOwer = true;
            }
            mapping = this.findMapping(exprTableSource);
            schemaObject = exprTableSource.getSchemaObject();
        }
        if (mapping == null) {
            return false;
        }
        final String srcName = x.getName();
        final String mappingColumn = mapping.getMappingColumn(srcName);
        if (mappingColumn != null) {
            x.setName(this.quote(mappingColumn));
        }
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLSelectItem && ((SQLSelectItem)parent).getAlias() == null) {
            ((SQLSelectItem)parent).setAlias(srcName);
        }
        if (x.getOwner() instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)x.getOwner()).nameHashCode64() == mapping.getSrcTableHash() && !aliasOwer) {
            x.setOwner(new SQLIdentifierExpr(this.quote(mapping.getDestTable())));
        }
        return false;
    }
    
    private String quote(final String name) {
        final char[] chars = new char[name.length() + 2];
        name.getChars(0, name.length(), chars, 1);
        chars[0] = '`';
        chars[chars.length - 1] = '`';
        return new String(chars);
    }
}
