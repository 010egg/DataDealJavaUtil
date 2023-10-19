// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor.transform;

import com.alibaba.druid.DbType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.SQLUtils;
import java.util.ArrayList;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;

public class FromSubqueryResolver extends OracleASTVisitorAdapter
{
    private final List<SQLStatement> targetList;
    private final String viewName;
    private final Map<String, String> mappings;
    private int viewNameSeed;
    
    public FromSubqueryResolver(final List<SQLStatement> targetList, final String viewName) {
        this.mappings = new LinkedHashMap<String, String>();
        this.viewNameSeed = 1;
        this.targetList = targetList;
        this.viewName = viewName;
    }
    
    @Override
    public boolean visit(final OracleSelectSubqueryTableSource x) {
        return this.visit((SQLSubqueryTableSource)x);
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        final String subViewName = this.generateSubViewName();
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
            queryBlock.setFrom(subViewName, x.getAlias());
        }
        else if (parent instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)parent;
            if (join.getLeft() == x) {
                join.setLeft(subViewName, x.getAlias());
            }
            else if (join.getRight() == x) {
                join.setRight(subViewName, x.getAlias());
            }
        }
        final SQLCreateViewStatement stmt = new SQLCreateViewStatement();
        stmt.setName(this.generateSubViewName());
        final SQLSelect select = x.getSelect();
        stmt.setSubQuery(select);
        this.targetList.add(0, stmt);
        stmt.accept(new FromSubqueryResolver(this.targetList, this.viewName));
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            final String ident = identifierExpr.getName();
            final String mappingIdent = this.mappings.get(ident);
            if (mappingIdent != null) {
                x.setExpr(new SQLIdentifierExpr(mappingIdent));
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectTableReference x) {
        return this.visit((SQLExprTableSource)x);
    }
    
    private String generateSubViewName() {
        return this.viewName + "_" + this.targetList.size();
    }
    
    public static List<SQLStatement> resolve(final SQLCreateViewStatement stmt) {
        final List<SQLStatement> targetList = new ArrayList<SQLStatement>();
        targetList.add(stmt);
        final String viewName = SQLUtils.normalize(stmt.getName().getSimpleName());
        final FromSubqueryResolver visitor = new FromSubqueryResolver(targetList, viewName);
        final SQLWithSubqueryClause withSubqueryClause = stmt.getSubQuery().getWithSubQuery();
        if (withSubqueryClause != null) {
            stmt.getSubQuery().setWithSubQuery(null);
            for (final SQLWithSubqueryClause.Entry entry : withSubqueryClause.getEntries()) {
                final String entryName = entry.getAlias();
                final SQLCreateViewStatement entryStmt = new SQLCreateViewStatement();
                entryStmt.setOrReplace(true);
                entryStmt.setDbType(stmt.getDbType());
                final String entryViewName = visitor.generateSubViewName();
                entryStmt.setName(entryViewName);
                entryStmt.setSubQuery(entry.getSubQuery());
                visitor.targetList.add(0, entryStmt);
                visitor.mappings.put(entryName, entryViewName);
                entryStmt.accept(visitor);
            }
        }
        stmt.accept(visitor);
        final DbType dbType = stmt.getDbType();
        for (int i = 0; i < targetList.size() - 1; ++i) {
            final SQLCreateViewStatement targetStmt = targetList.get(i);
            targetStmt.setOrReplace(true);
            targetStmt.setDbType(dbType);
            targetStmt.setAfterSemi(true);
        }
        return targetList;
    }
}
