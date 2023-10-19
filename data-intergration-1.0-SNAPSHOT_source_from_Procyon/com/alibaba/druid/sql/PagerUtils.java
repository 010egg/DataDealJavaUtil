// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import java.util.Iterator;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.DbType;

public class PagerUtils
{
    public static String count(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
        return count(selectStmt.getSelect(), dbType);
    }
    
    public static String limit(final String sql, final DbType dbType, final int offset, final int count) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
        return limit(selectStmt.getSelect(), dbType, offset, count);
    }
    
    public static String limit(final String sql, final DbType dbType, final int offset, final int count, final boolean check) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }
        final SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
        limit(selectStmt.getSelect(), dbType, offset, count, check);
        return selectStmt.toString();
    }
    
    public static String limit(final SQLSelect select, final DbType dbType, final int offset, final int count) {
        limit(select, dbType, offset, count, false);
        return SQLUtils.toSQLString(select, dbType);
    }
    
    public static boolean limit(final SQLSelect select, final DbType dbType, final int offset, final int count, final boolean check) {
        final SQLSelectQuery query = select.getQuery();
        switch (dbType) {
            case oracle: {
                return limitOracle(select, dbType, offset, count, check);
            }
            case db2: {
                return limitDB2(select, dbType, offset, count, check);
            }
            case sqlserver:
            case jtds: {
                return limitSQLServer(select, dbType, offset, count, check);
            }
            default: {
                if (query instanceof SQLSelectQueryBlock) {
                    return limitQueryBlock(select, dbType, offset, count, check);
                }
                if (query instanceof SQLUnionQuery) {
                    return limitUnion((SQLUnionQuery)query, dbType, offset, count, check);
                }
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private static boolean limitUnion(final SQLUnionQuery queryBlock, final DbType dbType, final int offset, final int count, final boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                final int rowCount = ((SQLNumericLiteralExpr)limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            }
            else if (check && limit.getRowCount() instanceof SQLVariantRefExpr) {
                return false;
            }
            limit.setRowCount(new SQLIntegerExpr(count));
        }
        if (limit == null) {
            limit = new SQLLimit();
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            limit.setRowCount(new SQLIntegerExpr(count));
            queryBlock.setLimit(limit);
        }
        return true;
    }
    
    private static boolean limitQueryBlock(final SQLSelect select, DbType dbType, final int offset, final int count, final boolean check) {
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)select.getQuery();
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case mysql:
            case mariadb:
            case h2:
            case ads: {
                return limitMySqlQueryBlock(queryBlock, dbType, offset, count, check);
            }
            case postgresql:
            case hive:
            case odps: {
                return limitSQLQueryBlock(queryBlock, dbType, offset, count, check);
            }
            case oracle:
            case oceanbase_oracle: {
                return limitOracle(select, dbType, offset, count, check);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private static boolean limitSQLQueryBlock(final SQLSelectQueryBlock queryBlock, final DbType dbType, final int offset, final int count, final boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                final int rowCount = ((SQLNumericLiteralExpr)limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            }
            limit.setRowCount(new SQLIntegerExpr(count));
        }
        limit = new SQLLimit();
        if (offset > 0) {
            limit.setOffset(new SQLIntegerExpr(offset));
        }
        limit.setRowCount(new SQLIntegerExpr(count));
        queryBlock.setLimit(limit);
        return true;
    }
    
    private static boolean limitDB2(final SQLSelect select, final DbType dbType, final int offset, final int count, final boolean check) {
        final SQLSelectQuery query = select.getQuery();
        final SQLBinaryOpExpr gt = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), DbType.db2);
        final SQLBinaryOpExpr lteq = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), DbType.db2);
        final SQLBinaryOpExpr pageCondition = new SQLBinaryOpExpr(gt, SQLBinaryOperator.BooleanAnd, lteq, DbType.db2);
        if (query instanceof SQLSelectQueryBlock) {
            final DB2SelectQueryBlock queryBlock = (DB2SelectQueryBlock)query;
            if (offset <= 0) {
                final SQLExpr first = queryBlock.getFirst();
                if (check && first != null && first instanceof SQLNumericLiteralExpr) {
                    final int rowCount = ((SQLNumericLiteralExpr)first).getNumber().intValue();
                    if (rowCount < count) {
                        return false;
                    }
                }
                queryBlock.setFirst(new SQLIntegerExpr(count));
                return true;
            }
            final SQLAggregateExpr aggregateExpr = new SQLAggregateExpr("ROW_NUMBER");
            SQLOrderBy orderBy = select.getOrderBy();
            if (orderBy == null && select.getQuery() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock selectQueryBlcok = (SQLSelectQueryBlock)select.getQuery();
                orderBy = selectQueryBlcok.getOrderBy();
                selectQueryBlcok.setOrderBy(null);
            }
            else {
                select.setOrderBy(null);
            }
            aggregateExpr.setOver(new SQLOver(orderBy));
            queryBlock.getSelectList().add(new SQLSelectItem(aggregateExpr, "ROWNUM"));
            final DB2SelectQueryBlock countQueryBlock = new DB2SelectQueryBlock();
            countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
            countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
            countQueryBlock.setWhere(pageCondition);
            select.setQuery(countQueryBlock);
            return true;
        }
        else {
            final DB2SelectQueryBlock countQueryBlock2 = new DB2SelectQueryBlock();
            countQueryBlock2.getSelectList().add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")));
            final SQLAggregateExpr aggregateExpr = new SQLAggregateExpr("ROW_NUMBER");
            final SQLOrderBy orderBy = select.getOrderBy();
            aggregateExpr.setOver(new SQLOver(orderBy));
            select.setOrderBy(null);
            countQueryBlock2.getSelectList().add(new SQLSelectItem(aggregateExpr, "ROWNUM"));
            countQueryBlock2.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
            if (offset <= 0) {
                select.setQuery(countQueryBlock2);
                return true;
            }
            final DB2SelectQueryBlock offsetQueryBlock = new DB2SelectQueryBlock();
            offsetQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
            offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock2), "XXX"));
            offsetQueryBlock.setWhere(pageCondition);
            select.setQuery(offsetQueryBlock);
            return true;
        }
    }
    
    private static boolean limitSQLServer(final SQLSelect select, final DbType dbType, final int offset, final int count, final boolean check) {
        final SQLSelectQuery query = select.getQuery();
        final SQLBinaryOpExpr gt = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), DbType.sqlserver);
        final SQLBinaryOpExpr lteq = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), DbType.sqlserver);
        final SQLBinaryOpExpr pageCondition = new SQLBinaryOpExpr(gt, SQLBinaryOperator.BooleanAnd, lteq, DbType.sqlserver);
        if (query instanceof SQLSelectQueryBlock) {
            final SQLServerSelectQueryBlock queryBlock = (SQLServerSelectQueryBlock)query;
            if (offset <= 0) {
                final SQLServerTop top = queryBlock.getTop();
                if (check && top != null && !top.isPercent() && top.getExpr() instanceof SQLNumericLiteralExpr) {
                    final int rowCount = ((SQLNumericLiteralExpr)top.getExpr()).getNumber().intValue();
                    if (rowCount <= count) {
                        return false;
                    }
                }
                queryBlock.setTop(new SQLServerTop(new SQLNumberExpr(count)));
                return true;
            }
            final SQLAggregateExpr aggregateExpr = new SQLAggregateExpr("ROW_NUMBER");
            final SQLOrderBy orderBy = select.getOrderBy();
            if (orderBy != null) {
                aggregateExpr.setOver(new SQLOver(orderBy));
                select.setOrderBy(null);
            }
            else if (queryBlock.getOrderBy() != null) {
                aggregateExpr.setOver(new SQLOver(queryBlock.getOrderBy()));
                queryBlock.setOrderBy(null);
            }
            queryBlock.getSelectList().add(new SQLSelectItem(aggregateExpr, "ROWNUM"));
            final SQLServerSelectQueryBlock countQueryBlock = new SQLServerSelectQueryBlock();
            countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
            countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
            countQueryBlock.setWhere(pageCondition);
            select.setQuery(countQueryBlock);
            return true;
        }
        else {
            final SQLServerSelectQueryBlock countQueryBlock2 = new SQLServerSelectQueryBlock();
            countQueryBlock2.getSelectList().add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")));
            countQueryBlock2.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
            if (offset <= 0) {
                countQueryBlock2.setTop(new SQLServerTop(new SQLNumberExpr(count)));
                select.setQuery(countQueryBlock2);
                return true;
            }
            final SQLAggregateExpr aggregateExpr = new SQLAggregateExpr("ROW_NUMBER");
            final SQLOrderBy orderBy = select.getOrderBy();
            aggregateExpr.setOver(new SQLOver(orderBy));
            select.setOrderBy(null);
            countQueryBlock2.getSelectList().add(new SQLSelectItem(aggregateExpr, "ROWNUM"));
            final SQLServerSelectQueryBlock offsetQueryBlock = new SQLServerSelectQueryBlock();
            offsetQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
            offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock2), "XXX"));
            offsetQueryBlock.setWhere(pageCondition);
            select.setQuery(offsetQueryBlock);
            return true;
        }
    }
    
    private static boolean limitOracle(final SQLSelect select, final DbType dbType, final int offset, final int count, final boolean check) {
        final SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            final OracleSelectQueryBlock queryBlock = (OracleSelectQueryBlock)query;
            SQLOrderBy orderBy = select.getOrderBy();
            if (orderBy == null && queryBlock.getOrderBy() != null) {
                orderBy = queryBlock.getOrderBy();
            }
            if (queryBlock.getGroupBy() == null && orderBy == null && offset <= 0) {
                final SQLExpr where = queryBlock.getWhere();
                if (check && where instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOpExpr binaryOpWhere = (SQLBinaryOpExpr)where;
                    if (binaryOpWhere.getOperator() == SQLBinaryOperator.LessThanOrEqual) {
                        final SQLExpr left = binaryOpWhere.getLeft();
                        final SQLExpr right = binaryOpWhere.getRight();
                        if (left instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)left).getName().equalsIgnoreCase("ROWNUM") && right instanceof SQLNumericLiteralExpr) {
                            final int rowCount = ((SQLNumericLiteralExpr)right).getNumber().intValue();
                            if (rowCount <= count) {
                                return false;
                            }
                        }
                    }
                }
                final SQLExpr condition = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count), DbType.oracle);
                if (queryBlock.getWhere() == null) {
                    queryBlock.setWhere(condition);
                }
                else {
                    queryBlock.setWhere(new SQLBinaryOpExpr(queryBlock.getWhere(), SQLBinaryOperator.BooleanAnd, condition, DbType.oracle));
                }
                return true;
            }
        }
        final OracleSelectQueryBlock countQueryBlock = new OracleSelectQueryBlock();
        countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")));
        countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLIdentifierExpr("ROWNUM"), "RN"));
        countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
        countQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), DbType.oracle));
        select.setOrderBy(null);
        if (offset <= 0) {
            select.setQuery(countQueryBlock);
            return true;
        }
        final OracleSelectQueryBlock offsetQueryBlock = new OracleSelectQueryBlock();
        offsetQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
        offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock), "XXX"));
        offsetQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("RN"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), DbType.oracle));
        select.setQuery(offsetQueryBlock);
        return true;
    }
    
    private static boolean limitMySqlQueryBlock(final SQLSelectQueryBlock queryBlock, final DbType dbType, final int offset, final int count, final boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                final int rowCount = ((SQLNumericLiteralExpr)limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            }
            else if (check && limit.getRowCount() instanceof SQLVariantRefExpr) {
                return false;
            }
            limit.setRowCount(new SQLIntegerExpr(count));
        }
        if (limit == null) {
            limit = new SQLLimit();
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            limit.setRowCount(new SQLIntegerExpr(count));
            queryBlock.setLimit(limit);
        }
        return true;
    }
    
    private static String count(final SQLSelect select, final DbType dbType) {
        if (select.getOrderBy() != null) {
            select.setOrderBy(null);
        }
        final SQLSelectQuery query = select.getQuery();
        clearOrderBy(query);
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectItem countItem = createCountItem(dbType);
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            final List<SQLSelectItem> selectList = queryBlock.getSelectList();
            if (queryBlock.getGroupBy() != null && queryBlock.getGroupBy().getItems().size() > 0) {
                if (queryBlock.getSelectList().size() == 1 && queryBlock.getSelectList().get(0).getExpr() instanceof SQLAllColumnExpr) {
                    queryBlock.getSelectList().clear();
                    queryBlock.getSelectList().add(new SQLSelectItem(new SQLIntegerExpr(1)));
                }
                return createCountUseSubQuery(select, dbType);
            }
            final int option = queryBlock.getDistionOption();
            if (option == 2 && selectList.size() >= 1) {
                final SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT", SQLAggregateOption.DISTINCT);
                for (int i = 0; i < selectList.size(); ++i) {
                    countExpr.addArgument(selectList.get(i).getExpr());
                }
                selectList.clear();
                queryBlock.setDistionOption(0);
                queryBlock.addSelectItem(countExpr);
            }
            else {
                selectList.clear();
                selectList.add(countItem);
            }
            return SQLUtils.toSQLString(select, dbType);
        }
        else {
            if (query instanceof SQLUnionQuery) {
                return createCountUseSubQuery(select, dbType);
            }
            throw new IllegalStateException();
        }
    }
    
    private static String createCountUseSubQuery(final SQLSelect select, final DbType dbType) {
        final SQLSelectQueryBlock countSelectQuery = createQueryBlock(dbType);
        final SQLSelectItem countItem = createCountItem(dbType);
        countSelectQuery.getSelectList().add(countItem);
        final SQLSubqueryTableSource fromSubquery = new SQLSubqueryTableSource(select);
        fromSubquery.setAlias("ALIAS_COUNT");
        countSelectQuery.setFrom(fromSubquery);
        final SQLSelect countSelect = new SQLSelect(countSelectQuery);
        final SQLSelectStatement countStmt = new SQLSelectStatement(countSelect, dbType);
        return SQLUtils.toSQLString(countStmt, dbType);
    }
    
    private static SQLSelectQueryBlock createQueryBlock(DbType dbType) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case mysql:
            case mariadb:
            case ads: {
                return new MySqlSelectQueryBlock();
            }
            case oracle: {
                return new OracleSelectQueryBlock();
            }
            case postgresql: {
                return new PGSelectQueryBlock();
            }
            case sqlserver:
            case jtds: {
                return new SQLServerSelectQueryBlock();
            }
            case db2: {
                return new DB2SelectQueryBlock();
            }
            case odps: {
                return new OdpsSelectQueryBlock();
            }
            default: {
                return new SQLSelectQueryBlock(dbType);
            }
        }
    }
    
    private static SQLSelectItem createCountItem(final DbType dbType) {
        final SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT");
        countExpr.addArgument(new SQLAllColumnExpr());
        final SQLSelectItem countItem = new SQLSelectItem(countExpr);
        return countItem;
    }
    
    private static void clearOrderBy(final SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock.getOrderBy() != null) {
                queryBlock.setOrderBy(null);
            }
            return;
        }
        if (query instanceof SQLUnionQuery) {
            final SQLUnionQuery union = (SQLUnionQuery)query;
            if (union.getOrderBy() != null) {
                union.setOrderBy(null);
            }
            clearOrderBy(union.getLeft());
            clearOrderBy(union.getRight());
        }
    }
    
    public static int getLimit(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            return -1;
        }
        final SQLStatement stmt = stmtList.get(0);
        if (stmt instanceof SQLSelectStatement) {
            final SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
            final SQLSelectQuery query = selectStmt.getSelect().getQuery();
            if (query instanceof SQLSelectQueryBlock) {
                if (query instanceof MySqlSelectQueryBlock) {
                    final SQLLimit limit = ((MySqlSelectQueryBlock)query).getLimit();
                    if (limit == null) {
                        return -1;
                    }
                    final SQLExpr rowCountExpr = limit.getRowCount();
                    if (rowCountExpr instanceof SQLNumericLiteralExpr) {
                        final int rowCount = ((SQLNumericLiteralExpr)rowCountExpr).getNumber().intValue();
                        return rowCount;
                    }
                    return Integer.MAX_VALUE;
                }
                else {
                    if (!(query instanceof OdpsSelectQueryBlock)) {
                        return -1;
                    }
                    final SQLLimit limit = ((OdpsSelectQueryBlock)query).getLimit();
                    final SQLExpr rowCountExpr = (limit != null) ? limit.getRowCount() : null;
                    if (rowCountExpr instanceof SQLNumericLiteralExpr) {
                        final int rowCount = ((SQLNumericLiteralExpr)rowCountExpr).getNumber().intValue();
                        return rowCount;
                    }
                    return Integer.MAX_VALUE;
                }
            }
        }
        return -1;
    }
    
    public static boolean hasUnorderedLimit(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (DbType.mysql == dbType) {
            final MySqlUnorderedLimitDetectVisitor visitor = new MySqlUnorderedLimitDetectVisitor();
            for (final SQLStatement stmt : stmtList) {
                stmt.accept(visitor);
            }
            return visitor.unorderedLimitCount > 0;
        }
        if (DbType.oracle == dbType) {
            final OracleUnorderedLimitDetectVisitor visitor2 = new OracleUnorderedLimitDetectVisitor();
            for (final SQLStatement stmt : stmtList) {
                stmt.accept(visitor2);
            }
            return visitor2.unorderedLimitCount > 0;
        }
        throw new FastsqlException("not supported. dbType : " + dbType);
    }
    
    private static class MySqlUnorderedLimitDetectVisitor extends MySqlASTVisitorAdapter
    {
        public int unorderedLimitCount;
        
        @Override
        public boolean visit(final MySqlSelectQueryBlock x) {
            final SQLOrderBy orderBy = x.getOrderBy();
            final SQLLimit limit = x.getLimit();
            if (limit != null && (orderBy == null || orderBy.getItems().size() == 0)) {
                boolean subQueryHasOrderBy = false;
                final SQLTableSource from = x.getFrom();
                if (from instanceof SQLSubqueryTableSource) {
                    final SQLSubqueryTableSource subqueryTabSrc = (SQLSubqueryTableSource)from;
                    final SQLSelect select = subqueryTabSrc.getSelect();
                    if (select.getQuery() instanceof SQLSelectQueryBlock) {
                        final SQLSelectQueryBlock subquery = (SQLSelectQueryBlock)select.getQuery();
                        if (subquery.getOrderBy() != null && subquery.getOrderBy().getItems().size() > 0) {
                            subQueryHasOrderBy = true;
                        }
                    }
                }
                if (!subQueryHasOrderBy) {
                    ++this.unorderedLimitCount;
                }
            }
            return true;
        }
    }
    
    private static class OracleUnorderedLimitDetectVisitor extends OracleASTVisitorAdapter
    {
        public int unorderedLimitCount;
        
        @Override
        public boolean visit(final SQLBinaryOpExpr x) {
            final SQLExpr left = x.getLeft();
            final SQLExpr right = x.getRight();
            boolean rownum = false;
            if (left instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)left).getName().equalsIgnoreCase("ROWNUM") && right instanceof SQLLiteralExpr) {
                rownum = true;
            }
            else if (right instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)right).getName().equalsIgnoreCase("ROWNUM") && left instanceof SQLLiteralExpr) {
                rownum = true;
            }
            OracleSelectQueryBlock selectQuery = null;
            if (rownum) {
                SQLObject parent = x.getParent();
                while (parent != null) {
                    if (parent instanceof SQLSelectQuery) {
                        if (parent instanceof OracleSelectQueryBlock) {
                            final OracleSelectQueryBlock queryBlock = (OracleSelectQueryBlock)parent;
                            final SQLTableSource from = queryBlock.getFrom();
                            if (from instanceof SQLExprTableSource) {
                                selectQuery = queryBlock;
                            }
                            else if (from instanceof SQLSubqueryTableSource) {
                                final SQLSelect subSelect = ((SQLSubqueryTableSource)from).getSelect();
                                if (subSelect.getQuery() instanceof OracleSelectQueryBlock) {
                                    selectQuery = (OracleSelectQueryBlock)subSelect.getQuery();
                                }
                            }
                            break;
                        }
                        break;
                    }
                    else {
                        parent = parent.getParent();
                    }
                }
            }
            if (selectQuery != null) {
                SQLOrderBy orderBy = selectQuery.getOrderBy();
                final SQLObject parent2 = selectQuery.getParent();
                if (orderBy == null && parent2 instanceof SQLSelect) {
                    final SQLSelect select = (SQLSelect)parent2;
                    orderBy = select.getOrderBy();
                }
                if (orderBy == null || orderBy.getItems().size() == 0) {
                    ++this.unorderedLimitCount;
                }
            }
            return true;
        }
        
        @Override
        public boolean visit(final OracleSelectQueryBlock queryBlock) {
            final boolean isExprTableSrc = queryBlock.getFrom() instanceof SQLExprTableSource;
            if (!isExprTableSrc) {
                return true;
            }
            boolean rownum = false;
            for (final SQLSelectItem item : queryBlock.getSelectList()) {
                final SQLExpr itemExpr = item.getExpr();
                if (itemExpr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)itemExpr).getName().equalsIgnoreCase("ROWNUM")) {
                    rownum = true;
                    break;
                }
            }
            if (!rownum) {
                return true;
            }
            final SQLObject parent = queryBlock.getParent();
            if (!(parent instanceof SQLSelect)) {
                return true;
            }
            final SQLSelect select = (SQLSelect)parent;
            if (select.getOrderBy() == null || select.getOrderBy().getItems().size() == 0) {
                ++this.unorderedLimitCount;
            }
            return false;
        }
    }
}
