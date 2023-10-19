// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository;

import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.util.PGUtils;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGFunctionTableSource;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.db2.ast.DB2Object;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

class SchemaResolveVisitorFactory
{
    static void resolve(final SchemaResolveVisitor visitor, final SQLCreateTableStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLExprTableSource table = x.getTableSource();
        ctx.setTableSource(table);
        table.accept(visitor);
        final List<SQLTableElement> elements = x.getTableElementList();
        for (int i = 0; i < elements.size(); ++i) {
            final SQLTableElement e = elements.get(i);
            if (e instanceof SQLColumnDefinition) {
                final SQLColumnDefinition columnn = (SQLColumnDefinition)e;
                final SQLName columnnName = columnn.getName();
                if (columnnName instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)columnnName;
                    identifierExpr.setResolvedTableSource(table);
                    identifierExpr.setResolvedColumn(columnn);
                }
            }
            else if (e instanceof SQLUniqueConstraint) {
                final List<SQLSelectOrderByItem> columns = ((SQLUniqueConstraint)e).getColumns();
                for (final SQLSelectOrderByItem orderByItem : columns) {
                    final SQLExpr orderByItemExpr = orderByItem.getExpr();
                    if (orderByItemExpr instanceof SQLIdentifierExpr) {
                        final SQLIdentifierExpr identifierExpr2 = (SQLIdentifierExpr)orderByItemExpr;
                        identifierExpr2.setResolvedTableSource(table);
                        final SQLColumnDefinition column = x.findColumn(identifierExpr2.nameHashCode64());
                        if (column == null) {
                            continue;
                        }
                        identifierExpr2.setResolvedColumn(column);
                    }
                }
            }
            else {
                e.accept(visitor);
            }
        }
        final SQLSelect select = x.getSelect();
        if (select != null) {
            visitor.visit(select);
        }
        final SchemaRepository repository = visitor.getRepository();
        if (repository != null) {
            repository.acceptCreateTable(x);
        }
        visitor.popContext();
        final SQLExprTableSource like = x.getLike();
        if (like != null) {
            like.accept(visitor);
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLUpdateStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            with.accept(visitor);
        }
        final SQLTableSource table = x.getTableSource();
        final SQLTableSource from = x.getFrom();
        ctx.setTableSource(table);
        ctx.setFrom(from);
        table.accept(visitor);
        if (from != null) {
            from.accept(visitor);
        }
        final List<SQLUpdateSetItem> items = x.getItems();
        for (final SQLUpdateSetItem item : items) {
            final SQLExpr column = item.getColumn();
            if (column instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)column;
                identifierExpr.setResolvedTableSource(table);
                visitor.visit(identifierExpr);
            }
            else if (column instanceof SQLListExpr) {
                final SQLListExpr columnGroup = (SQLListExpr)column;
                for (final SQLExpr columnGroupItem : columnGroup.getItems()) {
                    if (columnGroupItem instanceof SQLIdentifierExpr) {
                        final SQLIdentifierExpr identifierExpr2 = (SQLIdentifierExpr)columnGroupItem;
                        identifierExpr2.setResolvedTableSource(table);
                        visitor.visit(identifierExpr2);
                    }
                    else {
                        columnGroupItem.accept(visitor);
                    }
                }
            }
            else {
                column.accept(visitor);
            }
            final SQLExpr value = item.getValue();
            if (value != null) {
                value.accept(visitor);
            }
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            where.accept(visitor);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            orderBy.accept(visitor);
        }
        for (final SQLExpr sqlExpr : x.getReturning()) {
            sqlExpr.accept(visitor);
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLDeleteStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            visitor.visit(with);
        }
        SQLTableSource table = x.getTableSource();
        SQLTableSource from = x.getFrom();
        if (from == null) {
            from = x.getUsing();
        }
        if (table == null && from != null) {
            table = from;
            from = null;
        }
        if (from != null) {
            ctx.setFrom(from);
            from.accept(visitor);
        }
        if (table != null) {
            if (from != null && table instanceof SQLExprTableSource) {
                final SQLExpr tableExpr = ((SQLExprTableSource)table).getExpr();
                if (tableExpr instanceof SQLPropertyExpr && ((SQLPropertyExpr)tableExpr).getName().equals("*")) {
                    final String alias = ((SQLPropertyExpr)tableExpr).getOwnernName();
                    final SQLTableSource refTableSource = from.findTableSource(alias);
                    if (refTableSource != null) {
                        ((SQLPropertyExpr)tableExpr).setResolvedTableSource(refTableSource);
                    }
                }
            }
            table.accept(visitor);
            ctx.setTableSource(table);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            where.accept(visitor);
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLInsertStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            visitor.visit(with);
        }
        final SQLTableSource table = x.getTableSource();
        ctx.setTableSource(table);
        if (table != null) {
            table.accept(visitor);
        }
        for (final SQLExpr column : x.getColumns()) {
            column.accept(visitor);
        }
        if (x instanceof HiveInsertStatement) {
            for (final SQLAssignItem item : ((HiveInsertStatement)x).getPartitions()) {
                item.accept(visitor);
            }
        }
        for (final SQLInsertStatement.ValuesClause valuesClause : x.getValuesList()) {
            valuesClause.accept(visitor);
        }
        final SQLSelect query = x.getQuery();
        if (query != null) {
            visitor.visit(query);
        }
        visitor.popContext();
    }
    
    static void resolveIdent(final SchemaResolveVisitor visitor, final SQLIdentifierExpr x) {
        final SchemaResolveVisitor.Context ctx = visitor.getContext();
        if (ctx == null) {
            return;
        }
        final String ident = x.getName();
        final long hash = x.nameHashCode64();
        SQLTableSource tableSource = null;
        if ((hash == FnvHash.Constants.LEVEL || hash == FnvHash.Constants.CONNECT_BY_ISCYCLE) && ctx.object instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)ctx.object;
            if (queryBlock.getStartWith() != null || queryBlock.getConnectBy() != null) {
                return;
            }
        }
        final SQLTableSource ctxTable = ctx.getTableSource();
        if (ctxTable instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)ctxTable;
            tableSource = join.findTableSourceWithColumn(hash, ident, visitor.getOptions());
            if (tableSource == null) {
                final SQLTableSource left = join.getLeft();
                final SQLTableSource right = join.getRight();
                if (left instanceof SQLSubqueryTableSource && right instanceof SQLExprTableSource) {
                    final SQLSelect leftSelect = ((SQLSubqueryTableSource)left).getSelect();
                    if (leftSelect.getQuery() instanceof SQLSelectQueryBlock) {
                        final boolean hasAllColumn = ((SQLSelectQueryBlock)leftSelect.getQuery()).selectItemHasAllColumn();
                        if (!hasAllColumn) {
                            tableSource = right;
                        }
                    }
                }
                else if (right instanceof SQLSubqueryTableSource && left instanceof SQLExprTableSource) {
                    final SQLSelect rightSelect = ((SQLSubqueryTableSource)right).getSelect();
                    if (rightSelect.getQuery() instanceof SQLSelectQueryBlock) {
                        final boolean hasAllColumn = ((SQLSelectQueryBlock)rightSelect.getQuery()).selectItemHasAllColumn();
                        if (!hasAllColumn) {
                            tableSource = left;
                        }
                    }
                }
                else if (left instanceof SQLExprTableSource && right instanceof SQLExprTableSource) {
                    final SQLExprTableSource leftExprTableSource = (SQLExprTableSource)left;
                    final SQLExprTableSource rightExprTableSource = (SQLExprTableSource)right;
                    if (leftExprTableSource.getSchemaObject() != null && rightExprTableSource.getSchemaObject() == null) {
                        tableSource = rightExprTableSource;
                    }
                    else if (rightExprTableSource.getSchemaObject() != null && leftExprTableSource.getSchemaObject() == null) {
                        tableSource = leftExprTableSource;
                    }
                }
            }
        }
        else if (ctxTable instanceof SQLSubqueryTableSource) {
            tableSource = ctxTable.findTableSourceWithColumn(hash, ident, visitor.getOptions());
        }
        else if (ctxTable instanceof SQLLateralViewTableSource) {
            tableSource = ctxTable.findTableSourceWithColumn(hash, ident, visitor.getOptions());
            if (tableSource == null) {
                tableSource = ((SQLLateralViewTableSource)ctxTable).getTableSource();
            }
        }
        else {
            for (SchemaResolveVisitor.Context parentCtx = ctx; parentCtx != null; parentCtx = parentCtx.parent) {
                final SQLDeclareItem declareItem = parentCtx.findDeclare(hash);
                if (declareItem != null) {
                    x.setResolvedDeclareItem(declareItem);
                    return;
                }
                if (parentCtx.object instanceof SQLBlockStatement) {
                    final SQLBlockStatement block = (SQLBlockStatement)parentCtx.object;
                    final SQLParameter parameter = block.findParameter(hash);
                    if (parameter != null) {
                        x.setResolvedParameter(parameter);
                        return;
                    }
                }
                else if (parentCtx.object instanceof SQLCreateProcedureStatement) {
                    final SQLCreateProcedureStatement createProc = (SQLCreateProcedureStatement)parentCtx.object;
                    final SQLParameter parameter = createProc.findParameter(hash);
                    if (parameter != null) {
                        x.setResolvedParameter(parameter);
                        return;
                    }
                }
            }
            tableSource = ctxTable;
            if (tableSource instanceof SQLExprTableSource) {
                final SchemaObject table = ((SQLExprTableSource)tableSource).getSchemaObject();
                if (table != null && table.findColumn(hash) == null) {
                    SQLCreateTableStatement createStmt = null;
                    final SQLStatement smt = table.getStatement();
                    if (smt instanceof SQLCreateTableStatement) {
                        createStmt = (SQLCreateTableStatement)smt;
                    }
                    if (createStmt != null && createStmt.getTableElementList().size() > 0) {
                        tableSource = null;
                    }
                }
            }
        }
        if (tableSource instanceof SQLExprTableSource) {
            SQLExpr expr = ((SQLExprTableSource)tableSource).getExpr();
            if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)expr;
                if (func.methodNameHashCode64() == FnvHash.Constants.ANN) {
                    expr = func.getArguments().get(0);
                }
            }
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final long identHash = identExpr.nameHashCode64();
                tableSource = unwrapAlias(ctx, tableSource, identHash);
            }
        }
        if (tableSource != null) {
            x.setResolvedTableSource(tableSource);
            final SQLColumnDefinition column = tableSource.findColumn(hash);
            if (column != null) {
                x.setResolvedColumn(column);
            }
            if (ctxTable instanceof SQLJoinTableSource) {
                final String alias = tableSource.computeAlias();
                if (alias == null || tableSource instanceof SQLWithSubqueryClause.Entry) {
                    return;
                }
                if (visitor.isEnabled(SchemaResolveVisitor.Option.ResolveIdentifierAlias)) {
                    final SQLPropertyExpr propertyExpr = new SQLPropertyExpr(new SQLIdentifierExpr(alias), ident, hash);
                    propertyExpr.setResolvedColumn(x.getResolvedColumn());
                    propertyExpr.setResolvedTableSource(x.getResolvedTableSource());
                    SQLUtils.replaceInParent(x, propertyExpr);
                }
            }
        }
        if (x.getResolvedColumn() == null && x.getResolvedTableSource() == null) {
            for (SchemaResolveVisitor.Context parentCtx = ctx; parentCtx != null; parentCtx = parentCtx.parent) {
                final SQLDeclareItem declareItem = parentCtx.findDeclare(hash);
                if (declareItem != null) {
                    x.setResolvedDeclareItem(declareItem);
                    return;
                }
                if (parentCtx.object instanceof SQLBlockStatement) {
                    final SQLBlockStatement block = (SQLBlockStatement)parentCtx.object;
                    final SQLParameter parameter = block.findParameter(hash);
                    if (parameter != null) {
                        x.setResolvedParameter(parameter);
                        return;
                    }
                }
                else if (parentCtx.object instanceof SQLCreateProcedureStatement) {
                    final SQLCreateProcedureStatement createProc = (SQLCreateProcedureStatement)parentCtx.object;
                    final SQLParameter parameter = createProc.findParameter(hash);
                    if (parameter != null) {
                        x.setResolvedParameter(parameter);
                        return;
                    }
                }
            }
        }
        if (x.getResolvedColumnObject() == null && ctx.object instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock2 = (SQLSelectQueryBlock)ctx.object;
            boolean having = false;
            SQLObject current = x;
            SQLObject parent = x.getParent();
            while (parent != null) {
                if (parent instanceof SQLSelectGroupByClause && parent.getParent() == queryBlock2) {
                    final SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause)parent;
                    if (current == groupBy.getHaving()) {
                        having = true;
                        break;
                    }
                    break;
                }
                else {
                    current = parent;
                    parent = parent.getParent();
                }
            }
            if (having) {
                final SQLSelectItem selectItem = queryBlock2.findSelectItem(x.hashCode64());
                if (selectItem != null) {
                    x.setResolvedColumn(selectItem);
                }
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLPropertyExpr x) {
        final SchemaResolveVisitor.Context ctx = visitor.getContext();
        if (ctx == null) {
            return;
        }
        long owner_hash = 0L;
        final SQLExpr ownerObj = x.getOwner();
        if (ownerObj instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr owner = (SQLIdentifierExpr)ownerObj;
            owner_hash = owner.nameHashCode64();
        }
        else if (ownerObj instanceof SQLPropertyExpr) {
            owner_hash = ((SQLPropertyExpr)ownerObj).hashCode64();
        }
        SQLTableSource tableSource = null;
        final SQLTableSource ctxTable = ctx.getTableSource();
        if (ctxTable != null) {
            tableSource = ctxTable.findTableSource(owner_hash);
        }
        if (tableSource == null) {
            final SQLTableSource ctxFrom = ctx.getFrom();
            if (ctxFrom != null) {
                tableSource = ctxFrom.findTableSource(owner_hash);
            }
        }
        if (tableSource == null) {
            for (SchemaResolveVisitor.Context parentCtx = ctx; parentCtx != null; parentCtx = parentCtx.parent) {
                final SQLTableSource parentCtxTable = parentCtx.getTableSource();
                if (parentCtxTable != null) {
                    tableSource = parentCtxTable.findTableSource(owner_hash);
                    if (tableSource == null) {
                        final SQLTableSource ctxFrom2 = parentCtx.getFrom();
                        if (ctxFrom2 != null) {
                            tableSource = ctxFrom2.findTableSource(owner_hash);
                        }
                    }
                    if (tableSource != null) {
                        break;
                    }
                }
                else {
                    if (parentCtx.object instanceof SQLBlockStatement) {
                        final SQLBlockStatement block = (SQLBlockStatement)parentCtx.object;
                        final SQLParameter parameter = block.findParameter(owner_hash);
                        if (parameter != null) {
                            x.setResolvedOwnerObject(parameter);
                            return;
                        }
                    }
                    else if (parentCtx.object instanceof SQLMergeStatement) {
                        final SQLMergeStatement mergeStatement = (SQLMergeStatement)parentCtx.object;
                        final SQLTableSource into = mergeStatement.getInto();
                        if (into instanceof SQLSubqueryTableSource && into.aliasHashCode64() == owner_hash) {
                            x.setResolvedOwnerObject(into);
                        }
                    }
                    final SQLDeclareItem declareItem = parentCtx.findDeclare(owner_hash);
                    if (declareItem != null) {
                        final SQLObject resolvedObject = declareItem.getResolvedObject();
                        if (resolvedObject instanceof SQLCreateProcedureStatement || resolvedObject instanceof SQLCreateFunctionStatement || resolvedObject instanceof SQLTableSource) {
                            x.setResolvedOwnerObject(resolvedObject);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (tableSource != null) {
            x.setResolvedTableSource(tableSource);
            final SQLObject column = tableSource.resolveColum(x.nameHashCode64());
            if (column instanceof SQLColumnDefinition) {
                x.setResolvedColumn((SQLColumnDefinition)column);
            }
            else if (column instanceof SQLSelectItem) {
                x.setResolvedColumn((SQLSelectItem)column);
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLBinaryOpExpr x) {
        final SQLBinaryOperator op = x.getOperator();
        final SQLExpr left = x.getLeft();
        if ((op == SQLBinaryOperator.BooleanAnd || op == SQLBinaryOperator.BooleanOr) && left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)left).getOperator() == op) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x, op);
            for (int i = 0; i < groupList.size(); ++i) {
                final SQLExpr item = groupList.get(i);
                item.accept(visitor);
            }
            return;
        }
        if (left != null) {
            if (left instanceof SQLBinaryOpExpr) {
                resolve(visitor, (SQLBinaryOpExpr)left);
            }
            else {
                left.accept(visitor);
            }
        }
        final SQLExpr right = x.getRight();
        if (right != null) {
            right.accept(visitor);
        }
    }
    
    static SQLTableSource unwrapAlias(final SchemaResolveVisitor.Context ctx, final SQLTableSource tableSource, final long identHash) {
        if (ctx == null) {
            return tableSource;
        }
        if (ctx.object instanceof SQLDeleteStatement && (ctx.getTableSource() == null || tableSource == ctx.getTableSource()) && ctx.getFrom() != null) {
            final SQLTableSource found = ctx.getFrom().findTableSource(identHash);
            if (found != null) {
                return found;
            }
        }
        for (SchemaResolveVisitor.Context parentCtx = ctx; parentCtx != null; parentCtx = parentCtx.parent) {
            SQLWithSubqueryClause with = null;
            if (parentCtx.object instanceof SQLSelect) {
                final SQLSelect select = (SQLSelect)parentCtx.object;
                with = select.getWithSubQuery();
            }
            else if (parentCtx.object instanceof SQLDeleteStatement) {
                final SQLDeleteStatement delete = (SQLDeleteStatement)parentCtx.object;
                with = delete.getWith();
            }
            else if (parentCtx.object instanceof SQLInsertStatement) {
                final SQLInsertStatement insertStmt = (SQLInsertStatement)parentCtx.object;
                with = insertStmt.getWith();
            }
            else if (parentCtx.object instanceof SQLUpdateStatement) {
                final SQLUpdateStatement updateStmt = (SQLUpdateStatement)parentCtx.object;
                with = updateStmt.getWith();
            }
            if (with != null) {
                final SQLWithSubqueryClause.Entry entry = with.findEntry(identHash);
                if (entry != null) {
                    return entry;
                }
            }
        }
        return tableSource;
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLSelectQueryBlock x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        if (ctx != null && ctx.level >= 32) {
            return;
        }
        SQLTableSource from = x.getFrom();
        if (from != null) {
            ctx.setTableSource(from);
            final Class fromClass = from.getClass();
            if (fromClass == SQLExprTableSource.class) {
                visitor.visit((SQLExprTableSource)from);
            }
            else {
                from.accept(visitor);
            }
        }
        else if (x.getParent() != null && x.getParent().getParent() instanceof HiveInsert && x.getParent().getParent().getParent() instanceof HiveMultiInsertStatement) {
            final HiveMultiInsertStatement insert = (HiveMultiInsertStatement)x.getParent().getParent().getParent();
            if (insert.getFrom() instanceof SQLExprTableSource) {
                from = insert.getFrom();
                ctx.setTableSource(from);
            }
        }
        final List<SQLSelectItem> selectList = x.getSelectList();
        final List<SQLSelectItem> columns = new ArrayList<SQLSelectItem>();
        for (int i = selectList.size() - 1; i >= 0; --i) {
            final SQLSelectItem selectItem = selectList.get(i);
            final SQLExpr expr = selectItem.getExpr();
            if (expr instanceof SQLAllColumnExpr) {
                final SQLAllColumnExpr allColumnExpr = (SQLAllColumnExpr)expr;
                allColumnExpr.setResolvedTableSource(from);
                visitor.visit(allColumnExpr);
                if (visitor.isEnabled(SchemaResolveVisitor.Option.ResolveAllColumn)) {
                    extractColumns(visitor, from, null, columns);
                }
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                visitor.visit(propertyExpr);
                final String ownerName = propertyExpr.getOwnernName();
                if (propertyExpr.getName().equals("*") && visitor.isEnabled(SchemaResolveVisitor.Option.ResolveAllColumn)) {
                    final SQLTableSource tableSource = x.findTableSource(ownerName);
                    extractColumns(visitor, tableSource, ownerName, columns);
                }
                SQLColumnDefinition column = propertyExpr.getResolvedColumn();
                if (column != null) {
                    continue;
                }
                final SQLTableSource tableSource2 = x.findTableSource(propertyExpr.getOwnernName());
                if (tableSource2 != null) {
                    column = tableSource2.findColumn(propertyExpr.nameHashCode64());
                    if (column != null) {
                        propertyExpr.setResolvedColumn(column);
                    }
                }
            }
            else if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                visitor.visit(identExpr);
                final long name_hash = identExpr.nameHashCode64();
                SQLColumnDefinition column2 = identExpr.getResolvedColumn();
                if (column2 != null) {
                    continue;
                }
                if (from == null) {
                    continue;
                }
                column2 = from.findColumn(name_hash);
                if (column2 != null) {
                    identExpr.setResolvedColumn(column2);
                }
            }
            else {
                expr.accept(visitor);
            }
            if (columns.size() > 0) {
                for (final SQLSelectItem column3 : columns) {
                    column3.setParent(x);
                    column3.getExpr().accept(visitor);
                }
                selectList.remove(i);
                selectList.addAll(i, columns);
                columns.clear();
            }
        }
        final SQLExprTableSource into = x.getInto();
        if (into != null) {
            visitor.visit(into);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            if (where instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)where;
                resolveExpr(visitor, binaryOpExpr.getLeft());
                resolveExpr(visitor, binaryOpExpr.getRight());
            }
            else if (where instanceof SQLBinaryOpExprGroup) {
                final SQLBinaryOpExprGroup binaryOpExprGroup = (SQLBinaryOpExprGroup)where;
                for (final SQLExpr item : binaryOpExprGroup.getItems()) {
                    if (item instanceof SQLBinaryOpExpr) {
                        final SQLBinaryOpExpr binaryOpExpr2 = (SQLBinaryOpExpr)item;
                        resolveExpr(visitor, binaryOpExpr2.getLeft());
                        resolveExpr(visitor, binaryOpExpr2.getRight());
                    }
                    else {
                        item.accept(visitor);
                    }
                }
            }
            else {
                where.accept(visitor);
            }
        }
        final SQLExpr startWith = x.getStartWith();
        if (startWith != null) {
            startWith.accept(visitor);
        }
        final SQLExpr connectBy = x.getConnectBy();
        if (connectBy != null) {
            connectBy.accept(visitor);
        }
        final SQLSelectGroupByClause groupBy = x.getGroupBy();
        if (groupBy != null) {
            groupBy.accept(visitor);
        }
        final List<SQLWindow> windows = x.getWindows();
        if (windows != null) {
            for (final SQLWindow window : windows) {
                window.accept(visitor);
            }
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            for (final SQLSelectOrderByItem orderByItem : orderBy.getItems()) {
                final SQLExpr orderByItemExpr = orderByItem.getExpr();
                if (orderByItemExpr instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr orderByItemIdentExpr = (SQLIdentifierExpr)orderByItemExpr;
                    final long hash = orderByItemIdentExpr.nameHashCode64();
                    final SQLSelectItem selectItem2 = x.findSelectItem(hash);
                    if (selectItem2 != null) {
                        orderByItem.setResolvedSelectItem(selectItem2);
                        final SQLExpr selectItemExpr = selectItem2.getExpr();
                        if (selectItemExpr instanceof SQLIdentifierExpr) {
                            orderByItemIdentExpr.setResolvedTableSource(((SQLIdentifierExpr)selectItemExpr).getResolvedTableSource());
                            orderByItemIdentExpr.setResolvedColumn(((SQLIdentifierExpr)selectItemExpr).getResolvedColumn());
                            continue;
                        }
                        if (selectItemExpr instanceof SQLPropertyExpr) {
                            orderByItemIdentExpr.setResolvedTableSource(((SQLPropertyExpr)selectItemExpr).getResolvedTableSource());
                            orderByItemIdentExpr.setResolvedColumn(((SQLPropertyExpr)selectItemExpr).getResolvedColumn());
                            continue;
                        }
                        continue;
                    }
                }
                orderByItemExpr.accept(visitor);
            }
        }
        final int forUpdateOfSize = x.getForUpdateOfSize();
        if (forUpdateOfSize > 0) {
            for (final SQLExpr sqlExpr : x.getForUpdateOf()) {
                sqlExpr.accept(visitor);
            }
        }
        final List<SQLSelectOrderByItem> distributeBy = x.getDistributeBy();
        if (distributeBy != null) {
            for (final SQLSelectOrderByItem item2 : distributeBy) {
                item2.accept(visitor);
            }
        }
        final List<SQLSelectOrderByItem> sortBy = x.getSortBy();
        if (sortBy != null) {
            for (final SQLSelectOrderByItem item3 : sortBy) {
                item3.accept(visitor);
            }
        }
        visitor.popContext();
    }
    
    static void extractColumns(final SchemaResolveVisitor visitor, final SQLTableSource from, final String ownerName, final List<SQLSelectItem> columns) {
        if (from instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)from).getExpr();
            final SchemaRepository repository = visitor.getRepository();
            if (repository == null) {
                return;
            }
            final String alias = from.getAlias();
            final SchemaObject table = repository.findTable((SQLExprTableSource)from);
            if (table != null) {
                final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)table.getStatement();
                for (final SQLTableElement e : createTableStmt.getTableElementList()) {
                    if (e instanceof SQLColumnDefinition) {
                        final SQLColumnDefinition column = (SQLColumnDefinition)e;
                        if (alias != null) {
                            final SQLPropertyExpr name = new SQLPropertyExpr(alias, column.getName().getSimpleName());
                            name.setResolvedColumn(column);
                            columns.add(new SQLSelectItem(name));
                        }
                        else if (ownerName != null) {
                            final SQLPropertyExpr name = new SQLPropertyExpr(ownerName, column.getName().getSimpleName());
                            name.setResolvedColumn(column);
                            columns.add(new SQLSelectItem(name));
                        }
                        else if (from.getParent() instanceof SQLJoinTableSource && from instanceof SQLExprTableSource && expr instanceof SQLName) {
                            final String tableName = expr.toString();
                            final SQLPropertyExpr name2 = new SQLPropertyExpr(tableName, column.getName().getSimpleName());
                            name2.setResolvedColumn(column);
                            columns.add(new SQLSelectItem(name2));
                        }
                        else {
                            final SQLIdentifierExpr name3 = (SQLIdentifierExpr)column.getName().clone();
                            name3.setResolvedColumn(column);
                            columns.add(new SQLSelectItem(name3));
                        }
                    }
                }
                return;
            }
            if (expr instanceof SQLIdentifierExpr) {
                final SQLTableSource resolvedTableSource = ((SQLIdentifierExpr)expr).getResolvedTableSource();
                if (resolvedTableSource instanceof SQLWithSubqueryClause.Entry) {
                    final SQLWithSubqueryClause.Entry entry = (SQLWithSubqueryClause.Entry)resolvedTableSource;
                    final SQLSelect select = ((SQLWithSubqueryClause.Entry)resolvedTableSource).getSubQuery();
                    final SQLSelectQueryBlock firstQueryBlock = select.getFirstQueryBlock();
                    if (firstQueryBlock != null) {
                        for (final SQLSelectItem item : firstQueryBlock.getSelectList()) {
                            final String itemAlias = item.computeAlias();
                            if (itemAlias != null) {
                                final SQLIdentifierExpr columnExpr = new SQLIdentifierExpr(itemAlias);
                                columnExpr.setResolvedColumn(item);
                                columns.add(new SQLSelectItem(columnExpr));
                            }
                        }
                    }
                }
            }
        }
        else if (from instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)from;
            extractColumns(visitor, join.getLeft(), ownerName, columns);
            extractColumns(visitor, join.getRight(), ownerName, columns);
        }
        else if (from instanceof SQLSubqueryTableSource) {
            final SQLSelectQueryBlock subQuery = ((SQLSubqueryTableSource)from).getSelect().getQueryBlock();
            if (subQuery == null) {
                return;
            }
            final List<SQLSelectItem> subSelectList = subQuery.getSelectList();
            for (final SQLSelectItem subSelectItem : subSelectList) {
                if (subSelectItem.getAlias() != null) {
                    continue;
                }
                if (!(subSelectItem.getExpr() instanceof SQLName)) {
                    return;
                }
            }
            for (final SQLSelectItem subSelectItem : subSelectList) {
                final String alias2 = subSelectItem.computeAlias();
                columns.add(new SQLSelectItem(new SQLIdentifierExpr(alias2)));
            }
        }
        else if (from instanceof SQLUnionQueryTableSource) {
            final SQLSelectQueryBlock firstQueryBlock2 = ((SQLUnionQueryTableSource)from).getUnion().getFirstQueryBlock();
            if (firstQueryBlock2 == null) {
                return;
            }
            final List<SQLSelectItem> subSelectList = firstQueryBlock2.getSelectList();
            for (final SQLSelectItem subSelectItem : subSelectList) {
                if (subSelectItem.getAlias() != null) {
                    continue;
                }
                if (!(subSelectItem.getExpr() instanceof SQLName)) {
                    return;
                }
            }
            for (final SQLSelectItem subSelectItem : subSelectList) {
                final String alias2 = subSelectItem.computeAlias();
                columns.add(new SQLSelectItem(new SQLIdentifierExpr(alias2)));
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLAllColumnExpr x) {
        SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource == null) {
            SQLSelectQueryBlock queryBlock = null;
            for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLTableSource) {
                    return;
                }
                if (parent instanceof SQLSelectQueryBlock) {
                    queryBlock = (SQLSelectQueryBlock)parent;
                    break;
                }
            }
            if (queryBlock == null) {
                return;
            }
            final SQLTableSource from = queryBlock.getFrom();
            if (from == null || from instanceof SQLJoinTableSource) {
                return;
            }
            x.setResolvedTableSource(from);
            tableSource = from;
        }
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)tableSource).getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final SQLTableSource resolvedTableSource = ((SQLIdentifierExpr)expr).getResolvedTableSource();
                if (resolvedTableSource != null) {
                    x.setResolvedTableSource(resolvedTableSource);
                }
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor v, final SQLMethodInvokeExpr x) {
        final SQLExpr owner = x.getOwner();
        if (owner != null) {
            resolveExpr(v, owner);
        }
        for (final SQLExpr arg : x.getArguments()) {
            resolveExpr(v, arg);
        }
        final SQLExpr from = x.getFrom();
        if (from != null) {
            resolveExpr(v, from);
        }
        final SQLExpr using = x.getUsing();
        if (using != null) {
            resolveExpr(v, using);
        }
        final SQLExpr _for = x.getFor();
        if (_for != null) {
            resolveExpr(v, _for);
        }
        final long nameHash = x.methodNameHashCode64();
        final SchemaRepository repository = v.getRepository();
        if (repository != null) {
            final SQLDataType dataType = repository.findFuntionReturnType(nameHash);
            if (dataType != null) {
                x.setResolvedReturnDataType(dataType);
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLSelect x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLWithSubqueryClause with = x.getWithSubQuery();
        if (with != null) {
            visitor.visit(with);
        }
        final SQLSelectQuery query = x.getQuery();
        if (query != null) {
            if (query instanceof SQLSelectQueryBlock) {
                visitor.visit((SQLSelectQueryBlock)query);
            }
            else {
                query.accept(visitor);
            }
        }
        final SQLSelectQueryBlock queryBlock = x.getFirstQueryBlock();
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            for (final SQLSelectOrderByItem orderByItem : orderBy.getItems()) {
                final SQLExpr orderByItemExpr = orderByItem.getExpr();
                if (orderByItemExpr instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr orderByItemIdentExpr = (SQLIdentifierExpr)orderByItemExpr;
                    final long hash = orderByItemIdentExpr.nameHashCode64();
                    SQLSelectItem selectItem = null;
                    if (queryBlock != null) {
                        selectItem = queryBlock.findSelectItem(hash);
                    }
                    if (selectItem != null) {
                        orderByItem.setResolvedSelectItem(selectItem);
                        final SQLExpr selectItemExpr = selectItem.getExpr();
                        if (selectItemExpr instanceof SQLIdentifierExpr) {
                            orderByItemIdentExpr.setResolvedTableSource(((SQLIdentifierExpr)selectItemExpr).getResolvedTableSource());
                            orderByItemIdentExpr.setResolvedColumn(((SQLIdentifierExpr)selectItemExpr).getResolvedColumn());
                            continue;
                        }
                        if (selectItemExpr instanceof SQLPropertyExpr) {
                            orderByItemIdentExpr.setResolvedTableSource(((SQLPropertyExpr)selectItemExpr).getResolvedTableSource());
                            orderByItemIdentExpr.setResolvedColumn(((SQLPropertyExpr)selectItemExpr).getResolvedColumn());
                            continue;
                        }
                        continue;
                    }
                }
                orderByItemExpr.accept(visitor);
            }
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLWithSubqueryClause x) {
        final List<SQLWithSubqueryClause.Entry> entries = x.getEntries();
        final SchemaResolveVisitor.Context context = visitor.getContext();
        for (final SQLWithSubqueryClause.Entry entry : entries) {
            final SQLSelect query = entry.getSubQuery();
            if (query != null) {
                visitor.visit(query);
                final long alias_hash = entry.aliasHashCode64();
                if (context == null || alias_hash == 0L) {
                    continue;
                }
                context.addTableSource(alias_hash, entry);
            }
            else {
                entry.getReturningStatement().accept(visitor);
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLExprTableSource x) {
        SQLExpr expr = x.getExpr();
        SQLExpr annFeature = null;
        if (expr instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)expr;
            if (func.methodNameHashCode64() == FnvHash.Constants.ANN) {
                expr = func.getArguments().get(0);
                annFeature = func.getArguments().get(1);
                if (annFeature instanceof SQLIdentifierExpr) {
                    ((SQLIdentifierExpr)annFeature).setResolvedTableSource(x);
                }
                else if (annFeature instanceof SQLPropertyExpr) {
                    ((SQLPropertyExpr)annFeature).setResolvedTableSource(x);
                }
            }
        }
        if (expr instanceof SQLName) {
            if (x.getSchemaObject() != null) {
                return;
            }
            SQLIdentifierExpr identifierExpr = null;
            if (expr instanceof SQLIdentifierExpr) {
                identifierExpr = (SQLIdentifierExpr)expr;
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLExpr owner = ((SQLPropertyExpr)expr).getOwner();
                if (owner instanceof SQLIdentifierExpr) {
                    identifierExpr = (SQLIdentifierExpr)owner;
                }
            }
            if (identifierExpr != null) {
                checkParameter(visitor, identifierExpr);
                SQLTableSource tableSource = unwrapAlias(visitor.getContext(), null, identifierExpr.nameHashCode64());
                if (tableSource == null && x.getParent() instanceof HiveMultiInsertStatement) {
                    final SQLWithSubqueryClause with = ((HiveMultiInsertStatement)x.getParent()).getWith();
                    if (with != null) {
                        final SQLWithSubqueryClause.Entry entry = (SQLWithSubqueryClause.Entry)(tableSource = with.findEntry(identifierExpr.nameHashCode64()));
                    }
                }
                if (tableSource != null) {
                    identifierExpr.setResolvedTableSource(tableSource);
                    return;
                }
            }
            final SchemaRepository repository = visitor.getRepository();
            if (repository != null) {
                final SchemaObject table = repository.findTable((SQLName)expr);
                if (table != null) {
                    x.setSchemaObject(table);
                    if (annFeature != null && annFeature instanceof SQLIdentifierExpr) {
                        final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)annFeature;
                        final SQLColumnDefinition column = table.findColumn(identExpr.nameHashCode64());
                        if (column != null) {
                            identExpr.setResolvedColumn(column);
                        }
                    }
                    return;
                }
                final SchemaObject view = repository.findView((SQLName)expr);
                if (view != null) {
                    x.setSchemaObject(view);
                }
            }
        }
        else {
            if (expr instanceof SQLMethodInvokeExpr) {
                visitor.visit((SQLMethodInvokeExpr)expr);
                return;
            }
            if (expr instanceof SQLQueryExpr) {
                final SQLSelect select = ((SQLQueryExpr)expr).getSubQuery();
                visitor.visit(select);
                final SQLSelectQueryBlock queryBlock = select.getQueryBlock();
                if (queryBlock != null && annFeature instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr identExpr2 = (SQLIdentifierExpr)annFeature;
                    final SQLObject columnDef = queryBlock.resolveColum(identExpr2.nameHashCode64());
                    if (columnDef instanceof SQLColumnDefinition) {
                        identExpr2.setResolvedColumn((SQLColumnDefinition)columnDef);
                    }
                    else if (columnDef instanceof SQLSelectItem) {
                        identExpr2.setResolvedColumn((SQLSelectItem)columnDef);
                    }
                }
                return;
            }
            expr.accept(visitor);
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLAlterTableStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLTableSource tableSource = x.getTableSource();
        ctx.setTableSource(tableSource);
        for (final SQLAlterTableItem item : x.getItems()) {
            item.accept(visitor);
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLMergeStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLTableSource into = x.getInto();
        if (into instanceof SQLExprTableSource) {
            ctx.setTableSource(into);
        }
        else {
            into.accept(visitor);
        }
        final SQLTableSource using = x.getUsing();
        if (using != null) {
            using.accept(visitor);
            ctx.setFrom(using);
        }
        final SQLExpr on = x.getOn();
        if (on != null) {
            on.accept(visitor);
        }
        final SQLMergeStatement.MergeUpdateClause updateClause = x.getUpdateClause();
        if (updateClause != null) {
            for (final SQLUpdateSetItem item : updateClause.getItems()) {
                final SQLExpr column = item.getColumn();
                if (column instanceof SQLIdentifierExpr) {
                    ((SQLIdentifierExpr)column).setResolvedTableSource(into);
                }
                else if (column instanceof SQLPropertyExpr) {
                    ((SQLPropertyExpr)column).setResolvedTableSource(into);
                }
                else {
                    column.accept(visitor);
                }
                final SQLExpr value = item.getValue();
                if (value != null) {
                    value.accept(visitor);
                }
            }
            final SQLExpr where = updateClause.getWhere();
            if (where != null) {
                where.accept(visitor);
            }
            final SQLExpr deleteWhere = updateClause.getDeleteWhere();
            if (deleteWhere != null) {
                deleteWhere.accept(visitor);
            }
        }
        final SQLMergeStatement.MergeInsertClause insertClause = x.getInsertClause();
        if (insertClause != null) {
            for (final SQLExpr column : insertClause.getColumns()) {
                if (column instanceof SQLIdentifierExpr) {
                    ((SQLIdentifierExpr)column).setResolvedTableSource(into);
                }
                else if (column instanceof SQLPropertyExpr) {
                    ((SQLPropertyExpr)column).setResolvedTableSource(into);
                }
                column.accept(visitor);
            }
            for (final SQLExpr value2 : insertClause.getValues()) {
                value2.accept(visitor);
            }
            final SQLExpr where2 = insertClause.getWhere();
            if (where2 != null) {
                where2.accept(visitor);
            }
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLCreateFunctionStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLDeclareItem declareItem = new SQLDeclareItem(x.getName().clone(), null);
        declareItem.setResolvedObject(x);
        final SchemaResolveVisitor.Context parentCtx = visitor.getContext();
        if (parentCtx != null) {
            parentCtx.declare(declareItem);
        }
        else {
            ctx.declare(declareItem);
        }
        for (final SQLParameter parameter : x.getParameters()) {
            parameter.accept(visitor);
        }
        final SQLStatement block = x.getBlock();
        if (block != null) {
            block.accept(visitor);
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLCreateProcedureStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLDeclareItem declareItem = new SQLDeclareItem(x.getName().clone(), null);
        declareItem.setResolvedObject(x);
        final SchemaResolveVisitor.Context parentCtx = visitor.getContext();
        if (parentCtx != null) {
            parentCtx.declare(declareItem);
        }
        else {
            ctx.declare(declareItem);
        }
        for (final SQLParameter parameter : x.getParameters()) {
            parameter.accept(visitor);
        }
        final SQLStatement block = x.getBlock();
        if (block != null) {
            block.accept(visitor);
        }
        visitor.popContext();
    }
    
    static boolean resolve(final SchemaResolveVisitor visitor, final SQLIfStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLExpr condition = x.getCondition();
        if (condition != null) {
            condition.accept(visitor);
        }
        for (final SQLStatement stmt : x.getStatements()) {
            stmt.accept(visitor);
        }
        for (final SQLIfStatement.ElseIf elseIf : x.getElseIfList()) {
            elseIf.accept(visitor);
        }
        final SQLIfStatement.Else e = x.getElseItem();
        if (e != null) {
            e.accept(visitor);
        }
        visitor.popContext();
        return false;
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLBlockStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        for (final SQLParameter parameter : x.getParameters()) {
            visitor.visit(parameter);
        }
        for (final SQLStatement stmt : x.getStatementList()) {
            stmt.accept(visitor);
        }
        final SQLStatement exception = x.getException();
        if (exception != null) {
            exception.accept(visitor);
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLParameter x) {
        final SQLName name = x.getName();
        if (name instanceof SQLIdentifierExpr) {
            ((SQLIdentifierExpr)name).setResolvedParameter(x);
        }
        final SQLExpr expr = x.getDefaultValue();
        SchemaResolveVisitor.Context ctx = null;
        if (expr != null) {
            if (expr instanceof SQLQueryExpr) {
                ctx = visitor.createContext(x);
                final SQLSubqueryTableSource tableSource = new SQLSubqueryTableSource(((SQLQueryExpr)expr).getSubQuery());
                tableSource.setParent(x);
                tableSource.setAlias(x.getName().getSimpleName());
                ctx.setTableSource(tableSource);
            }
            expr.accept(visitor);
        }
        if (ctx != null) {
            visitor.popContext();
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLDeclareItem x) {
        final SchemaResolveVisitor.Context ctx = visitor.getContext();
        if (ctx != null) {
            ctx.declare(x);
        }
        final SQLName name = x.getName();
        if (name instanceof SQLIdentifierExpr) {
            ((SQLIdentifierExpr)name).setResolvedDeclareItem(x);
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLOver x) {
        final SQLName of = x.getOf();
        final SQLOrderBy orderBy = x.getOrderBy();
        final List<SQLExpr> partitionBy = x.getPartitionBy();
        if (of == null && orderBy != null) {
            orderBy.accept(visitor);
        }
        if (partitionBy != null) {
            for (final SQLExpr expr : partitionBy) {
                expr.accept(visitor);
            }
        }
    }
    
    private static boolean checkParameter(final SchemaResolveVisitor visitor, final SQLIdentifierExpr x) {
        if (x.getResolvedParameter() != null) {
            return true;
        }
        final SchemaResolveVisitor.Context ctx = visitor.getContext();
        if (ctx == null) {
            return false;
        }
        final long hash = x.hashCode64();
        for (SchemaResolveVisitor.Context parentCtx = ctx; parentCtx != null; parentCtx = parentCtx.parent) {
            if (parentCtx.object instanceof SQLBlockStatement) {
                final SQLBlockStatement block = (SQLBlockStatement)parentCtx.object;
                final SQLParameter parameter = block.findParameter(hash);
                if (parameter != null) {
                    x.setResolvedParameter(parameter);
                    return true;
                }
            }
            if (parentCtx.object instanceof SQLCreateProcedureStatement) {
                final SQLCreateProcedureStatement createProc = (SQLCreateProcedureStatement)parentCtx.object;
                final SQLParameter parameter = createProc.findParameter(hash);
                if (parameter != null) {
                    x.setResolvedParameter(parameter);
                    return true;
                }
            }
            if (parentCtx.object instanceof SQLSelect) {
                final SQLSelect select = (SQLSelect)parentCtx.object;
                final SQLWithSubqueryClause with = select.getWithSubQuery();
                if (with != null) {
                    final SQLWithSubqueryClause.Entry entry = with.findEntry(hash);
                    if (entry != null) {
                        x.setResolvedTableSource(entry);
                        return true;
                    }
                }
                final SchemaRepository repo = visitor.getRepository();
                if (repo != null) {
                    final SchemaObject view = repo.findView(x);
                    if (view != null && view.getStatement() instanceof SQLCreateViewStatement) {
                        x.setResolvedOwnerObject(view.getStatement());
                    }
                }
            }
            final SQLDeclareItem declareItem = parentCtx.findDeclare(hash);
            if (declareItem != null) {
                x.setResolvedDeclareItem(declareItem);
                break;
            }
        }
        return false;
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLReplaceStatement x) {
        final SchemaResolveVisitor.Context ctx = visitor.createContext(x);
        final SQLExprTableSource tableSource = x.getTableSource();
        ctx.setTableSource(tableSource);
        visitor.visit(tableSource);
        for (final SQLExpr column : x.getColumns()) {
            column.accept(visitor);
        }
        final SQLQueryExpr queryExpr = x.getQuery();
        if (queryExpr != null) {
            visitor.visit(queryExpr.getSubQuery());
        }
        visitor.popContext();
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLFetchStatement x) {
        resolveExpr(visitor, x.getCursorName());
        for (final SQLExpr expr : x.getInto()) {
            resolveExpr(visitor, expr);
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLForeignKeyConstraint x) {
        final SchemaRepository repository = visitor.getRepository();
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLCreateTableStatement) {
            final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)parent;
            final SQLTableSource table = createTableStmt.getTableSource();
            for (final SQLName item : x.getReferencingColumns()) {
                final SQLIdentifierExpr columnName = (SQLIdentifierExpr)item;
                columnName.setResolvedTableSource(table);
                final SQLColumnDefinition column = createTableStmt.findColumn(columnName.nameHashCode64());
                if (column != null) {
                    columnName.setResolvedColumn(column);
                }
            }
        }
        else if (parent instanceof SQLAlterTableAddConstraint) {
            final SQLAlterTableStatement stmt = (SQLAlterTableStatement)parent.getParent();
            final SQLTableSource table = stmt.getTableSource();
            for (final SQLName item : x.getReferencingColumns()) {
                final SQLIdentifierExpr columnName = (SQLIdentifierExpr)item;
                columnName.setResolvedTableSource(table);
            }
        }
        if (repository == null) {
            return;
        }
        final SQLExprTableSource table2 = x.getReferencedTable();
        for (final SQLName item2 : x.getReferencedColumns()) {
            final SQLIdentifierExpr columnName2 = (SQLIdentifierExpr)item2;
            columnName2.setResolvedTableSource(table2);
        }
        final SQLName tableName = table2.getName();
        final SchemaObject tableObject = repository.findTable(tableName);
        if (tableObject == null) {
            return;
        }
        final SQLStatement tableStmt = tableObject.getStatement();
        if (tableStmt instanceof SQLCreateTableStatement) {
            final SQLCreateTableStatement refCreateTableStmt = (SQLCreateTableStatement)tableStmt;
            for (final SQLName item3 : x.getReferencedColumns()) {
                final SQLIdentifierExpr columnName3 = (SQLIdentifierExpr)item3;
                final SQLColumnDefinition column2 = refCreateTableStmt.findColumn(columnName3.nameHashCode64());
                if (column2 != null) {
                    columnName3.setResolvedColumn(column2);
                }
            }
        }
    }
    
    static void resolve(final SchemaResolveVisitor visitor, final SQLCreateViewStatement x) {
        x.getSubQuery().accept(visitor);
    }
    
    static void resolveExpr(final SchemaResolveVisitor visitor, final SQLExpr x) {
        if (x == null) {
            return;
        }
        final Class<?> clazz = x.getClass();
        if (clazz == SQLIdentifierExpr.class) {
            visitor.visit((SQLIdentifierExpr)x);
            return;
        }
        if (clazz == SQLIntegerExpr.class || clazz == SQLCharExpr.class) {
            return;
        }
        x.accept(visitor);
    }
    
    static void resolveUnion(final SchemaResolveVisitor visitor, final SQLUnionQuery x) {
        final SQLUnionOperator operator = x.getOperator();
        final List<SQLSelectQuery> relations = x.getRelations();
        if (relations.size() > 2) {
            for (final SQLSelectQuery relation : relations) {
                relation.accept(visitor);
            }
            return;
        }
        final SQLSelectQuery left = x.getLeft();
        final SQLSelectQuery right = x.getRight();
        final boolean bracket = x.isParenthesized() && !(x.getParent() instanceof SQLUnionQueryTableSource);
        if (!bracket && left instanceof SQLUnionQuery && ((SQLUnionQuery)left).getOperator() == operator && !right.isParenthesized() && x.getOrderBy() == null) {
            SQLUnionQuery leftUnion = (SQLUnionQuery)left;
            final List<SQLSelectQuery> rights = new ArrayList<SQLSelectQuery>();
            rights.add(right);
            if (leftUnion.getRelations().size() > 2) {
                rights.addAll(leftUnion.getRelations());
            }
            else {
                SQLSelectQuery leftLeft;
                SQLSelectQuery leftRight;
                while (true) {
                    leftLeft = leftUnion.getLeft();
                    leftRight = leftUnion.getRight();
                    if (leftUnion.isParenthesized() || leftUnion.getOrderBy() != null || leftLeft.isParenthesized() || leftRight.isParenthesized() || !(leftLeft instanceof SQLUnionQuery) || ((SQLUnionQuery)leftLeft).getOperator() != operator) {
                        break;
                    }
                    rights.add(leftRight);
                    leftUnion = (SQLUnionQuery)leftLeft;
                }
                rights.add(leftRight);
                rights.add(leftLeft);
            }
            for (int i = rights.size() - 1; i >= 0; --i) {
                final SQLSelectQuery item = rights.get(i);
                item.accept(visitor);
            }
            return;
        }
        if (left != null) {
            left.accept(visitor);
        }
        if (right != null) {
            right.accept(visitor);
        }
    }
    
    static class MySqlResolveVisitor extends MySqlASTVisitorAdapter implements SchemaResolveVisitor
    {
        private SchemaRepository repository;
        private int options;
        private Context context;
        
        public MySqlResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final MySqlRepeatStatement x) {
            return true;
        }
        
        @Override
        public boolean visit(final MySqlDeclareStatement x) {
            for (final SQLDeclareItem declareItem : x.getVarList()) {
                this.visit(declareItem);
            }
            return false;
        }
        
        @Override
        public boolean visit(final MySqlCursorDeclareStatement x) {
            return true;
        }
        
        @Override
        public boolean visit(final MysqlForeignKey x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final MySqlSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final MySqlCreateTableStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            final SQLExprTableSource like = x.getLike();
            if (like != null) {
                like.accept(this);
            }
            return false;
        }
        
        @Override
        public boolean visit(final MySqlUpdateStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final MySqlDeleteStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final MySqlInsertStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class DB2ResolveVisitor extends DB2ASTVisitorAdapter implements SchemaResolveVisitor
    {
        private SchemaRepository repository;
        private int options;
        private Context context;
        
        public DB2ResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final DB2SelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            final long hash64 = x.hashCode64();
            if (hash64 == FnvHash.Constants.CURRENT_DATE || hash64 == DB2Object.Constants.CURRENT_TIME) {
                return false;
            }
            SchemaResolveVisitorFactory.resolveIdent(this, x);
            return true;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class OracleResolveVisitor extends OracleASTVisitorAdapter implements SchemaResolveVisitor
    {
        private SchemaRepository repository;
        private int options;
        private Context context;
        
        public OracleResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final OracleCreatePackageStatement x) {
            final Context ctx = this.createContext(x);
            for (final SQLStatement stmt : x.getStatements()) {
                stmt.accept(this);
            }
            this.popContext();
            return false;
        }
        
        @Override
        public boolean visit(final OracleForStatement x) {
            final Context ctx = this.createContext(x);
            final SQLName index = x.getIndex();
            final SQLExpr range = x.getRange();
            if (index != null) {
                final SQLDeclareItem declareItem = new SQLDeclareItem(index, null);
                declareItem.setParent(x);
                if (index instanceof SQLIdentifierExpr) {
                    ((SQLIdentifierExpr)index).setResolvedDeclareItem(declareItem);
                }
                declareItem.setResolvedObject(range);
                ctx.declare(declareItem);
                if (range instanceof SQLQueryExpr) {
                    final SQLSelect select = ((SQLQueryExpr)range).getSubQuery();
                    final SQLSubqueryTableSource tableSource = new SQLSubqueryTableSource(select);
                    declareItem.setResolvedObject(tableSource);
                }
                index.accept(this);
            }
            if (range != null) {
                range.accept(this);
            }
            for (final SQLStatement stmt : x.getStatements()) {
                stmt.accept(this);
            }
            this.popContext();
            return false;
        }
        
        @Override
        public boolean visit(final OracleForeignKey x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleSelectTableReference x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            if (x.nameHashCode64() == FnvHash.Constants.ROWNUM) {
                return false;
            }
            SchemaResolveVisitorFactory.resolveIdent(this, x);
            return true;
        }
        
        @Override
        public boolean visit(final OracleCreateTableStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleUpdateStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleDeleteStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final OracleMultiInsertStatement x) {
            final Context ctx = this.createContext(x);
            final SQLSelect select = x.getSubQuery();
            this.visit(select);
            final OracleSelectSubqueryTableSource tableSource = new OracleSelectSubqueryTableSource(select);
            tableSource.setParent(x);
            ctx.setTableSource(tableSource);
            for (final OracleMultiInsertStatement.Entry entry : x.getEntries()) {
                entry.accept(this);
            }
            this.popContext();
            return false;
        }
        
        @Override
        public boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
            for (final SQLExpr column : x.getColumns()) {
                if (column instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr identColumn = (SQLIdentifierExpr)column;
                    identColumn.setResolvedTableSource(x.getTableSource());
                }
            }
            return true;
        }
        
        @Override
        public boolean visit(final OracleInsertStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class OdpsResolveVisitor extends OdpsASTVisitorAdapter implements SchemaResolveVisitor
    {
        private int options;
        private SchemaRepository repository;
        private Context context;
        
        public OdpsResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final OdpsSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final OdpsCreateTableStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final HiveInsert x) {
            final Context ctx = this.createContext(x);
            final SQLExprTableSource tableSource = x.getTableSource();
            if (tableSource != null) {
                ctx.setTableSource(x.getTableSource());
                this.visit(tableSource);
            }
            final List<SQLAssignItem> partitions = x.getPartitions();
            if (partitions != null) {
                for (final SQLAssignItem item : partitions) {
                    item.accept(this);
                }
            }
            final SQLSelect select = x.getQuery();
            if (select != null) {
                this.visit(select);
            }
            this.popContext();
            return false;
        }
        
        @Override
        public boolean visit(final HiveInsertStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class HiveResolveVisitor extends HiveASTVisitorAdapter implements SchemaResolveVisitor
    {
        private int options;
        private SchemaRepository repository;
        private Context context;
        
        public HiveResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        public boolean visit(final OdpsSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final HiveCreateTableStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final HiveInsert x) {
            final Context ctx = this.createContext(x);
            final SQLExprTableSource tableSource = x.getTableSource();
            if (tableSource != null) {
                ctx.setTableSource(x.getTableSource());
                this.visit(tableSource);
            }
            final List<SQLAssignItem> partitions = x.getPartitions();
            if (partitions != null) {
                for (final SQLAssignItem item : partitions) {
                    item.accept(this);
                }
            }
            final SQLSelect select = x.getQuery();
            if (select != null) {
                this.visit(select);
            }
            this.popContext();
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class PGResolveVisitor extends PGASTVisitorAdapter implements SchemaResolveVisitor
    {
        private int options;
        private SchemaRepository repository;
        private Context context;
        
        public PGResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final PGSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final PGFunctionTableSource x) {
            for (final SQLParameter parameter : x.getParameters()) {
                final SQLName name = parameter.getName();
                if (name instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr identName = (SQLIdentifierExpr)name;
                    identName.setResolvedTableSource(x);
                }
            }
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            if (PGUtils.isPseudoColumn(x.nameHashCode64())) {
                return false;
            }
            SchemaResolveVisitorFactory.resolveIdent(this, x);
            return true;
        }
        
        @Override
        public boolean visit(final PGUpdateStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final PGDeleteStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final PGSelectStatement x) {
            this.createContext(x);
            this.visit(x.getSelect());
            this.popContext();
            return false;
        }
        
        @Override
        public boolean visit(final PGInsertStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class SQLServerResolveVisitor extends SQLServerASTVisitorAdapter implements SchemaResolveVisitor
    {
        private int options;
        private SchemaRepository repository;
        private Context context;
        
        public SQLServerResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final SQLServerSelectQueryBlock x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLServerUpdateStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean visit(final SQLServerInsertStatement x) {
            SchemaResolveVisitorFactory.resolve(this, x);
            return false;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
    
    static class SQLResolveVisitor extends SQLASTVisitorAdapter implements SchemaResolveVisitor
    {
        private int options;
        private SchemaRepository repository;
        private Context context;
        
        public SQLResolveVisitor(final SchemaRepository repository, final int options) {
            this.repository = repository;
            this.options = options;
        }
        
        @Override
        public boolean visit(final SQLSelectItem x) {
            final SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SchemaResolveVisitorFactory.resolveIdent(this, (SQLIdentifierExpr)expr);
                return false;
            }
            if (expr instanceof SQLPropertyExpr) {
                SchemaResolveVisitorFactory.resolve(this, (SQLPropertyExpr)expr);
                return false;
            }
            return true;
        }
        
        @Override
        public boolean isEnabled(final Option option) {
            return (this.options & option.mask) != 0x0;
        }
        
        @Override
        public int getOptions() {
            return this.options;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Context createContext(final SQLObject object) {
            return this.context = new Context(object, this.context);
        }
        
        @Override
        public void popContext() {
            if (this.context != null) {
                this.context = this.context.parent;
            }
        }
        
        @Override
        public SchemaRepository getRepository() {
            return this.repository;
        }
    }
}
