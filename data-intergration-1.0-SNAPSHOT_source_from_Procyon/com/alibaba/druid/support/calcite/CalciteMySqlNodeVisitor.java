// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.calcite;

import java.util.HashMap;
import org.apache.calcite.sql.SqlExplainFormat;
import org.apache.calcite.sql.SqlExplain;
import org.apache.calcite.sql.SqlExplainLevel;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.ast.expr.SQLDefaultExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnnestTableSource;
import com.alibaba.druid.sql.ast.statement.SQLValuesQuery;
import com.alibaba.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.alibaba.druid.sql.ast.expr.SQLExtractExpr;
import com.alibaba.druid.sql.ast.expr.SQLNotExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import org.apache.calcite.sql.SqlIntervalLiteral;
import org.apache.calcite.sql.SqlIntervalQualifier;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.TDDLHint;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import org.apache.calcite.sql.SqlDynamicParam;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import org.apache.calcite.avatica.util.TimeUnit;
import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.fun.SqlTrimFunction;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import org.apache.calcite.sql.SqlWindow;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlReturnTypeInference;
import org.apache.calcite.sql.SqlUnresolvedFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import org.apache.calcite.util.TimeString;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import org.apache.calcite.util.DateString;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import org.apache.calcite.util.TimestampString;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import org.apache.calcite.sql.parser.SqlParserUtil;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import org.apache.calcite.sql.fun.SqlQuantifyOperator;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import java.util.Arrays;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLSomeExpr;
import com.alibaba.druid.sql.ast.expr.SQLAnyExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import org.apache.calcite.sql.SqlWithItem;
import org.apache.calcite.sql.SqlWith;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import org.apache.calcite.sql.fun.SqlCase;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import org.apache.calcite.sql.fun.SqlCastFunction;
import org.apache.calcite.sql.SqlTypeNameSpec;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlBasicTypeNameSpec;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import org.apache.calcite.sql.SqlInternalOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.calcite.sql.SqlBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLName;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.JoinType;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.JoinConditionType;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import org.apache.calcite.sql.SqlOrderBy;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import org.apache.calcite.sql.SqlDelete;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import java.util.Iterator;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlUpdate;
import org.apache.calcite.sql.SqlIdentifier;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import org.apache.calcite.sql.SqlInsert;
import java.util.List;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParserPos;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import java.util.Map;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

public class CalciteMySqlNodeVisitor extends MySqlASTVisitorAdapter
{
    static Map<Long, SqlOperator> operators;
    private SqlNode sqlNode;
    private static Map<Long, SqlTypeName> nameHashCode64SqlTypeNameMapping;
    static long JSON_VALUE;
    static long JSON_OBJECT;
    static long JSON_ARRAY;
    static long JSON_SCALAR;
    
    static SqlOperator func(final long hash) {
        return CalciteMySqlNodeVisitor.operators.get(hash);
    }
    
    public SqlNode getSqlNode() {
        return this.sqlNode;
    }
    
    @Override
    public boolean visit(final SQLInsertStatement x) {
        final SqlNodeList keywords = new SqlNodeList((Collection)new ArrayList(), SqlParserPos.ZERO);
        final SQLExprTableSource tableSource = x.getTableSource();
        final SqlNode targetTable = this.convertToSqlNode(tableSource.getExpr());
        final SQLSelect query = x.getQuery();
        SqlNode source;
        if (query != null) {
            query.accept(this);
            source = this.sqlNode;
        }
        else {
            final List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
            final SqlNode[] rows = new SqlNode[valuesList.size()];
            for (int j = 0; j < valuesList.size(); ++j) {
                final List<SQLExpr> values = valuesList.get(j).getValues();
                final SqlNode[] valueNodes = new SqlNode[values.size()];
                for (int i = 0; i < values.size(); ++i) {
                    final SqlNode valueNode = this.convertToSqlNode(values.get(i));
                    valueNodes[i] = valueNode;
                }
                final SqlBasicCall row = new SqlBasicCall((SqlOperator)SqlStdOperatorTable.ROW, valueNodes, SqlParserPos.ZERO);
                rows[j] = (SqlNode)row;
            }
            source = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.VALUES, rows, SqlParserPos.ZERO);
        }
        final SqlNodeList columnList = (x.getColumns().size() > 0) ? this.convertToSqlNodeList(x.getColumns()) : null;
        this.sqlNode = (SqlNode)new SqlInsert(SqlParserPos.ZERO, keywords, targetTable, source, columnList);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlInsertStatement x) {
        return this.visit((SQLInsertStatement)x);
    }
    
    private boolean visit(List<SQLInsertStatement.ValuesClause> valuesList) {
        boolean isBatch = false;
        final List<SQLInsertStatement.ValuesClause> newValuesList = convertToSingleValuesIfNeed(valuesList);
        if (newValuesList.size() < valuesList.size()) {
            isBatch = true;
            valuesList = newValuesList;
        }
        final SqlNode[] rows = new SqlNode[valuesList.size()];
        for (int j = 0; j < valuesList.size(); ++j) {
            final List<SQLExpr> values = valuesList.get(j).getValues();
            final SqlNode[] valueNodes = new SqlNode[values.size()];
            for (int i = 0; i < values.size(); ++i) {
                final SqlNode valueNode = this.convertToSqlNode(values.get(i));
                valueNodes[i] = valueNode;
            }
            final SqlBasicCall row = new SqlBasicCall((SqlOperator)SqlStdOperatorTable.ROW, valueNodes, SqlParserPos.ZERO);
            rows[j] = (SqlNode)row;
        }
        this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.VALUES, rows, SqlParserPos.ZERO);
        return isBatch;
    }
    
    @Override
    public boolean visit(final MySqlUpdateStatement x) {
        if (x.getTableSource().getClass() != SQLExprTableSource.class) {
            throw new UnsupportedOperationException("Support single table only for SqlUpdate statement of calcite.");
        }
        final SQLExprTableSource tableSource = (SQLExprTableSource)x.getTableSource();
        final SqlNode targetTable = this.convertToSqlNode(tableSource.getExpr());
        final List<SqlNode> columns = new ArrayList<SqlNode>();
        final List<SqlNode> values = new ArrayList<SqlNode>();
        for (final SQLUpdateSetItem item : x.getItems()) {
            columns.add(this.convertToSqlNode(item.getColumn()));
            values.add(this.convertToSqlNode(item.getValue()));
        }
        final SqlNodeList targetColumnList = new SqlNodeList((Collection)columns, SqlParserPos.ZERO);
        final SqlNodeList sourceExpressList = new SqlNodeList((Collection)values, SqlParserPos.ZERO);
        final SqlNode condition = this.convertToSqlNode(x.getWhere());
        SqlIdentifier alias = null;
        if (x.getTableSource().getAlias() != null) {
            alias = new SqlIdentifier(tableSource.getAlias(), SqlParserPos.ZERO);
        }
        this.sqlNode = (SqlNode)new SqlUpdate(SqlParserPos.ZERO, targetTable, targetColumnList, sourceExpressList, condition, (SqlSelect)null, alias);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeleteStatement x) {
        final SQLExprTableSource tableSource = (SQLExprTableSource)x.getTableSource();
        final SqlNode targetTable = this.convertToSqlNode(tableSource.getExpr());
        final SqlNode condition = this.convertToSqlNode(x.getWhere());
        SqlIdentifier alias = null;
        if (x.getTableSource().getAlias() != null) {
            alias = new SqlIdentifier(tableSource.getAlias(), SqlParserPos.ZERO);
        }
        this.sqlNode = (SqlNode)new SqlDelete(SqlParserPos.ZERO, targetTable, condition, (SqlSelect)null, alias);
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQuery x) {
        SqlNode[] nodes;
        if (x.getRelations().size() > 2) {
            nodes = new SqlNode[x.getRelations().size()];
            for (int i = 0; i < x.getRelations().size(); ++i) {
                nodes[i] = this.convertToSqlNode(x.getRelations().get(i));
            }
        }
        else {
            final SqlNode left = this.convertToSqlNode(x.getLeft());
            final SqlNode right = this.convertToSqlNode(x.getRight());
            nodes = new SqlNode[] { left, right };
        }
        SqlNodeList orderBySqlNode = null;
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            orderBySqlNode = this.convertOrderby(orderBy);
        }
        SqlNode offset = null;
        SqlNode fetch = null;
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            offset = this.convertToSqlNode(limit.getOffset());
            fetch = this.convertToSqlNode(limit.getRowCount());
        }
        final SQLUnionOperator operator = x.getOperator();
        SqlNode union = null;
        switch (operator) {
            case UNION_ALL: {
                union = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.UNION_ALL, nodes, SqlParserPos.ZERO);
                break;
            }
            case UNION:
            case DISTINCT: {
                union = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.UNION, nodes, SqlParserPos.ZERO);
                break;
            }
            case INTERSECT: {
                union = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.INTERSECT, nodes, SqlParserPos.ZERO);
                break;
            }
            case EXCEPT: {
                union = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.EXCEPT, nodes, SqlParserPos.ZERO);
                break;
            }
            default: {
                throw new FastsqlException("unsupported join type: " + operator);
            }
        }
        if (null == orderBy && null == offset && null == fetch) {
            this.sqlNode = union;
        }
        else {
            if (orderBySqlNode == null) {
                orderBySqlNode = SqlNodeList.EMPTY;
            }
            this.sqlNode = (SqlNode)new SqlOrderBy(SqlParserPos.ZERO, union, orderBySqlNode, offset, fetch);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSelectQueryBlock x) {
        return this.visit((SQLSelectQueryBlock)x);
    }
    
    public boolean visit(final SQLTableSource x) {
        final Class<?> clazz = x.getClass();
        if (clazz == SQLJoinTableSource.class) {
            this.visit((SQLJoinTableSource)x);
        }
        else if (clazz == SQLExprTableSource.class) {
            this.visit((SQLExprTableSource)x);
        }
        else if (clazz == SQLSubqueryTableSource.class) {
            this.visit((SQLSubqueryTableSource)x);
        }
        else {
            x.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        final SQLExpr expr = x.getExpr();
        SqlIdentifier table;
        if (expr instanceof SQLIdentifierExpr) {
            table = this.buildIdentifier((SQLIdentifierExpr)expr);
        }
        else {
            if (!(expr instanceof SQLPropertyExpr)) {
                throw new FastsqlException("not support : " + expr);
            }
            table = this.buildIdentifier((SQLPropertyExpr)expr);
        }
        if (x.getAlias() != null) {
            final SqlIdentifier alias = new SqlIdentifier(x.computeAlias(), SqlParserPos.ZERO);
            final SqlBasicCall as = new SqlBasicCall((SqlOperator)SqlStdOperatorTable.AS, new SqlNode[] { (SqlNode)table, (SqlNode)alias }, SqlParserPos.ZERO);
            this.sqlNode = (SqlNode)as;
        }
        else {
            this.sqlNode = (SqlNode)table;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource x) {
        final SQLJoinTableSource.JoinType joinType = x.getJoinType();
        final SqlNode left = this.convertToSqlNode(x.getLeft());
        final SqlNode right = this.convertToSqlNode(x.getRight());
        SqlNode condition = this.convertToSqlNode(x.getCondition());
        SqlLiteral conditionType = (condition == null) ? JoinConditionType.NONE.symbol(SqlParserPos.ZERO) : JoinConditionType.ON.symbol(SqlParserPos.ZERO);
        if (condition == null && !x.getUsing().isEmpty()) {
            final List<SQLExpr> using = x.getUsing();
            conditionType = JoinConditionType.USING.symbol(SqlParserPos.ZERO);
            condition = (SqlNode)this.convertToSqlNodeList(x.getUsing());
        }
        switch (joinType) {
            case COMMA: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.COMMA.symbol(SqlParserPos.ZERO), right, JoinConditionType.NONE.symbol(SqlParserPos.ZERO), (SqlNode)null);
                break;
            }
            case JOIN:
            case INNER_JOIN: {
                if (condition == null) {
                    this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.COMMA.symbol(SqlParserPos.ZERO), right, conditionType, (SqlNode)null);
                    break;
                }
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.INNER.symbol(SqlParserPos.ZERO), right, conditionType, condition);
                break;
            }
            case LEFT_OUTER_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.LEFT.symbol(SqlParserPos.ZERO), right, conditionType, condition);
                break;
            }
            case RIGHT_OUTER_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.RIGHT.symbol(SqlParserPos.ZERO), right, conditionType, condition);
                break;
            }
            case NATURAL_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(true, SqlParserPos.ZERO), JoinType.COMMA.symbol(SqlParserPos.ZERO), right, JoinConditionType.NONE.symbol(SqlParserPos.ZERO), (SqlNode)null);
                break;
            }
            case CROSS_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.CROSS.symbol(SqlParserPos.ZERO), right, JoinConditionType.NONE.symbol(SqlParserPos.ZERO), (SqlNode)null);
                break;
            }
            case NATURAL_CROSS_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(true, SqlParserPos.ZERO), JoinType.CROSS.symbol(SqlParserPos.ZERO), right, JoinConditionType.NONE.symbol(SqlParserPos.ZERO), (SqlNode)null);
                break;
            }
            case FULL_OUTER_JOIN: {
                this.sqlNode = (SqlNode)new SqlJoin(SqlParserPos.ZERO, left, SqlLiteral.createBoolean(false, SqlParserPos.ZERO), JoinType.FULL.symbol(SqlParserPos.ZERO), right, (condition == null) ? JoinConditionType.NONE.symbol(SqlParserPos.ZERO) : conditionType, condition);
                break;
            }
            default: {
                throw new UnsupportedOperationException("unsupported : " + joinType);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        this.sqlNode = this.convertToSqlNode(x.getSelect());
        final String alias = x.getAlias();
        if (alias != null) {
            final SqlIdentifier aliasIdentifier = new SqlIdentifier(alias, SqlParserPos.ZERO);
            final List<SQLName> columns = x.getColumns();
            SqlNode[] operands;
            if (columns.size() == 0) {
                operands = new SqlNode[] { this.sqlNode, (SqlNode)aliasIdentifier };
            }
            else {
                operands = new SqlNode[columns.size() + 2];
                operands[0] = this.sqlNode;
                operands[1] = (SqlNode)aliasIdentifier;
                for (int i = 0; i < columns.size(); ++i) {
                    final SQLName column = columns.get(i);
                    operands[i + 2] = (SqlNode)new SqlIdentifier(SQLUtils.normalize(column.getSimpleName()), SqlParserPos.ZERO);
                }
            }
            this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.AS, operands, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQueryTableSource x) {
        x.getUnion().accept(this);
        final String alias = x.getAlias();
        if (alias != null) {
            final SqlIdentifier aliasIdentifier = new SqlIdentifier(alias, SqlParserPos.ZERO);
            this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.AS, new SqlNode[] { this.sqlNode, (SqlNode)aliasIdentifier }, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLInSubQueryExpr x) {
        final SqlNode left = this.convertToSqlNode(x.getExpr());
        SqlBinaryOperator subOperator = SqlStdOperatorTable.IN;
        if (x.isNot()) {
            subOperator = SqlStdOperatorTable.NOT_IN;
        }
        final SqlNode right = this.convertToSqlNode(x.subQuery);
        this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)subOperator, new SqlNode[] { left, right }, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectQueryBlock x) {
        SqlNodeList keywordList = null;
        final List<SqlNode> keywordNodes = new ArrayList<SqlNode>(5);
        final int option = x.getDistionOption();
        if (option != 0) {
            if (option == 2 || option == 4) {
                keywordNodes.add((SqlNode)SqlSelectKeyword.DISTINCT.symbol(SqlParserPos.ZERO));
            }
            else if (option == 1) {
                keywordNodes.add((SqlNode)SqlSelectKeyword.ALL.symbol(SqlParserPos.ZERO));
            }
            keywordList = new SqlNodeList((Collection)keywordNodes, SqlParserPos.ZERO);
        }
        final List<SqlNode> columnNodes = new ArrayList<SqlNode>(x.getSelectList().size());
        for (final SQLSelectItem selectItem : x.getSelectList()) {
            final SqlNode column = this.convertToSqlNode(selectItem);
            columnNodes.add(column);
        }
        final SqlNodeList selectList = new SqlNodeList((Collection)columnNodes, SqlParserPos.ZERO);
        SqlNode from = null;
        final SQLTableSource tableSource = x.getFrom();
        if (tableSource != null) {
            from = this.convertToSqlNode(tableSource);
        }
        final SqlNode where = this.convertToSqlNode(x.getWhere());
        SqlNodeList orderBySqlNode = null;
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            orderBySqlNode = this.convertOrderby(orderBy);
        }
        SqlNodeList groupBySqlNode = null;
        SqlNode having = null;
        final SQLSelectGroupByClause groupBys = x.getGroupBy();
        if (groupBys != null) {
            if (groupBys.getHaving() != null) {
                having = this.convertToSqlNode(groupBys.getHaving());
            }
            if (groupBys.getItems().size() > 0) {
                final List<SqlNode> groupByNodes = new ArrayList<SqlNode>(groupBys.getItems().size());
                for (final SQLExpr groupBy : groupBys.getItems()) {
                    final SqlNode groupByNode = this.convertToSqlNode(groupBy);
                    groupByNodes.add(groupByNode);
                }
                groupBySqlNode = new SqlNodeList((Collection)groupByNodes, SqlParserPos.ZERO);
            }
            SqlInternalOperator op = null;
            if (groupBys.isWithRollUp()) {
                op = SqlStdOperatorTable.ROLLUP;
            }
            else if (groupBys.isWithCube()) {
                op = SqlStdOperatorTable.CUBE;
            }
            if (op != null) {
                final List<SqlNode> rollupNodes = new ArrayList<SqlNode>(1);
                boolean isRow = false;
                for (final SqlNode node : groupBySqlNode.getList()) {
                    if (node instanceof SqlBasicCall && ((SqlBasicCall)node).getOperator() == SqlStdOperatorTable.ROW) {
                        isRow = true;
                        break;
                    }
                }
                if (isRow) {
                    rollupNodes.add((SqlNode)op.createCall(SqlParserPos.ZERO, groupBySqlNode.toArray()));
                    groupBySqlNode = new SqlNodeList((Collection)rollupNodes, SqlParserPos.ZERO);
                }
                else {
                    rollupNodes.add((SqlNode)op.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)groupBySqlNode }));
                    groupBySqlNode = new SqlNodeList((Collection)rollupNodes, SqlParserPos.ZERO);
                }
            }
        }
        SqlNode offset = null;
        SqlNode fetch = null;
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            offset = this.convertToSqlNode(limit.getOffset());
            fetch = this.convertToSqlNode(limit.getRowCount());
        }
        final SqlNodeList hints = this.convertHints(x.getHints());
        if (orderBy != null && x.getParent() instanceof SQLUnionQuery) {
            this.sqlNode = (SqlNode)new TDDLSqlSelect(SqlParserPos.ZERO, keywordList, selectList, from, where, groupBySqlNode, having, null, null, offset, fetch, hints, null);
            this.sqlNode = (SqlNode)new SqlOrderBy(SqlParserPos.ZERO, this.sqlNode, orderBySqlNode, (SqlNode)null, fetch);
        }
        else {
            if (orderBySqlNode == null) {
                orderBySqlNode = SqlNodeList.EMPTY;
            }
            if (hints == null || SqlNodeList.isEmptyList((SqlNode)hints)) {
                this.sqlNode = (SqlNode)new SqlSelect(SqlParserPos.ZERO, keywordList, selectList, from, where, groupBySqlNode, having, (SqlNodeList)null, SqlNodeList.EMPTY, (SqlNode)null, (SqlNode)null, (SqlNodeList)null);
                if (!SqlNodeList.isEmptyList((SqlNode)orderBySqlNode) || offset != null || fetch != null) {
                    this.sqlNode = (SqlNode)new SqlOrderBy(SqlParserPos.ZERO, this.sqlNode, orderBySqlNode, offset, fetch);
                }
            }
            else {
                this.sqlNode = (SqlNode)new TDDLSqlSelect(SqlParserPos.ZERO, keywordList, selectList, from, where, groupBySqlNode, having, null, orderBySqlNode, offset, fetch, hints, null);
            }
        }
        return false;
    }
    
    private SqlTypeName toSqlTypeName(final SQLDataType dataType) {
        final long nameHashCode64 = dataType.nameHashCode64();
        final SqlTypeName sqlTypeName = CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.get(nameHashCode64);
        if (sqlTypeName != null) {
            return sqlTypeName;
        }
        throw new FastsqlException("TODO");
    }
    
    @Override
    public boolean visit(final SQLCastExpr x) {
        final SqlLiteral functionQualifier = null;
        final SqlNode sqlNode = this.convertToSqlNode(x.getExpr());
        final SQLDataType dataType = x.getDataType();
        String typeName = dataType.getName().toUpperCase();
        if (dataType.nameHashCode64() == FnvHash.Constants.INT) {
            typeName = "INTEGER";
        }
        else if (dataType.nameHashCode64() == FnvHash.Constants.NUMERIC) {
            typeName = "DECIMAL";
        }
        final SqlIdentifier dataTypeNode = (SqlIdentifier)this.convertToSqlNode(new SQLIdentifierExpr(typeName));
        int scale = -1;
        int precision = -1;
        final List<SQLExpr> arguments = dataType.getArguments();
        if (arguments != null && !arguments.isEmpty()) {
            scale = arguments.get(0).getNumber().intValue();
            if (arguments.size() > 1) {
                precision = arguments.get(1).getNumber().intValue();
            }
        }
        final SqlDataTypeSpec sqlDataTypeSpec = new SqlDataTypeSpec((SqlTypeNameSpec)new SqlBasicTypeNameSpec(this.toSqlTypeName(dataType), scale, precision, SqlParserPos.ZERO), SqlParserPos.ZERO);
        final SqlOperator sqlOperator = (SqlOperator)new SqlCastFunction();
        this.sqlNode = (SqlNode)new CalciteSqlBasicCall(sqlOperator, new SqlNode[] { sqlNode, (SqlNode)sqlDataTypeSpec }, SqlParserPos.ZERO, false, functionQualifier);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCaseExpr x) {
        final SQLExpr valueExpr = x.getValueExpr();
        SqlNode nodeValue = null;
        final SqlNodeList nodeWhen = new SqlNodeList(SqlParserPos.ZERO);
        final SqlNodeList nodeThen = new SqlNodeList(SqlParserPos.ZERO);
        if (valueExpr != null) {
            nodeValue = this.convertToSqlNode(valueExpr);
        }
        final List items = x.getItems();
        for (int elExpr = 0, size = items.size(); elExpr < size; ++elExpr) {
            this.visit(items.get(elExpr));
            if (this.sqlNode != null && this.sqlNode instanceof SqlNodeList) {
                final SqlNodeList nodeListTemp = (SqlNodeList)this.sqlNode;
                nodeWhen.add(nodeListTemp.get(0));
                nodeThen.add(nodeListTemp.get(1));
            }
        }
        final SQLExpr elseExpr = x.getElseExpr();
        final SqlNode nodeElse = this.convertToSqlNode(elseExpr);
        final SqlNodeList sqlNodeList = new SqlNodeList(SqlParserPos.ZERO);
        sqlNodeList.add(nodeValue);
        sqlNodeList.add((SqlNode)nodeWhen);
        sqlNodeList.add((SqlNode)nodeThen);
        sqlNodeList.add(nodeElse);
        this.sqlNode = (SqlNode)SqlCase.createSwitched(SqlParserPos.ZERO, nodeValue, nodeWhen, nodeThen, nodeElse);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCaseExpr.Item x) {
        final SQLExpr conditionExpr = x.getConditionExpr();
        final SqlNode sqlNode1 = this.convertToSqlNode(conditionExpr);
        final SQLExpr valueExpr = x.getValueExpr();
        final SqlNode sqlNode2 = this.convertToSqlNode(valueExpr);
        final SqlNodeList sqlNodeList = new SqlNodeList(SqlParserPos.ZERO);
        sqlNodeList.add(sqlNode1);
        sqlNodeList.add(sqlNode2);
        this.sqlNode = (SqlNode)sqlNodeList;
        return false;
    }
    
    @Override
    public boolean visit(final SQLListExpr x) {
        final List<SQLExpr> items = x.getItems();
        final List<SqlNode> objects = new ArrayList<SqlNode>();
        for (int i = 0; i < items.size(); ++i) {
            final SQLExpr sqlExpr = items.get(i);
            final SqlNode sqlNode = this.convertToSqlNode(sqlExpr);
            objects.add(sqlNode);
        }
        this.sqlNode = (SqlNode)SqlStdOperatorTable.ROW.createCall(SqlParserPos.ZERO, (List)objects);
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        final SQLWithSubqueryClause with = x.getWithSubQuery();
        if (with != null) {
            final SqlNodeList withList = new SqlNodeList(SqlParserPos.ZERO);
            final List<SQLWithSubqueryClause.Entry> entries = with.getEntries();
            for (final SQLWithSubqueryClause.Entry entry : entries) {
                this.visit(entry);
                withList.add(this.sqlNode);
            }
            final SqlNode query = this.convertToSqlNode(x.getQuery());
            if (query instanceof SqlOrderBy) {
                final SqlOrderBy orderBy = (SqlOrderBy)query;
                final SqlWith w = new SqlWith(SqlParserPos.ZERO, withList, orderBy.query);
                this.sqlNode = (SqlNode)new SqlOrderBy(SqlParserPos.ZERO, (SqlNode)w, orderBy.orderList, orderBy.offset, orderBy.fetch);
            }
            else {
                this.sqlNode = (SqlNode)new SqlWith(SqlParserPos.ZERO, withList, query);
            }
            if (query instanceof SqlSelect) {
                final SqlSelect select = (SqlSelect)query;
                final SqlNode fetch = select.getFetch();
                final SqlNodeList orderList = select.getOrderList();
                if (fetch != null || (orderList != null && orderList.size() > 0)) {
                    SqlNodeList orderByList = null;
                    if (orderList != null) {
                        orderByList = new SqlNodeList((Collection)orderList.getList(), SqlParserPos.ZERO);
                        orderList.getList().clear();
                    }
                    else {
                        orderByList = SqlNodeList.EMPTY;
                    }
                    this.sqlNode = (SqlNode)new SqlOrderBy(SqlParserPos.ZERO, this.sqlNode, orderByList, (SqlNode)null, fetch);
                    if (fetch != null) {
                        select.setFetch((SqlNode)null);
                    }
                }
            }
        }
        else {
            this.sqlNode = this.convertToSqlNode(x.getQuery());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause.Entry x) {
        SqlNodeList columnList = null;
        final List<SQLName> columns = x.getColumns();
        if (columns.size() > 0) {
            columnList = new SqlNodeList(SqlParserPos.ZERO);
            for (final SQLName column : columns) {
                columnList.add((SqlNode)new SqlIdentifier(column.getSimpleName(), SqlParserPos.ZERO));
            }
        }
        final SqlNode query = this.convertToSqlNode(x.getSubQuery());
        final SqlIdentifier name = new SqlIdentifier(x.getAlias(), SqlParserPos.ZERO);
        this.sqlNode = (SqlNode)new SqlWithItem(SqlParserPos.ZERO, name, columnList, query);
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectStatement x) {
        final SqlNode sqlNode = this.convertToSqlNode(x.getSelect());
        if (sqlNode instanceof TDDLSqlSelect) {
            final TDDLSqlSelect select = (TDDLSqlSelect)sqlNode;
            final SqlNodeList headHints = this.convertHints(x.getHeadHintsDirect());
            select.setHeadHints(headHints);
            this.sqlNode = (SqlNode)select;
        }
        else {
            this.sqlNode = sqlNode;
        }
        return false;
    }
    
    protected void visit(final SQLSelectQuery x) {
        final Class<?> clazz = x.getClass();
        if (clazz == MySqlSelectQueryBlock.class) {
            this.visit((MySqlSelectQueryBlock)x);
        }
        else if (clazz == SQLUnionQuery.class) {
            this.visit((SQLUnionQuery)x);
        }
        else {
            x.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLAllExpr x) {
        this.sqlNode = this.convertToSqlNode(x.getSubQuery());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAnyExpr x) {
        this.sqlNode = this.convertToSqlNode(x.getSubQuery());
        return false;
    }
    
    private boolean isSqlAllExpr(final SQLExpr x) {
        return x.getClass() == SQLAllExpr.class;
    }
    
    private boolean isAnyOrSomeExpr(final SQLExpr x) {
        return x.getClass() == SQLAnyExpr.class || x.getClass() == SQLSomeExpr.class;
    }
    
    @Override
    public boolean visit(final SQLSelectItem x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIdentifierExpr) {
            this.visit((SQLIdentifierExpr)expr);
        }
        else if (expr instanceof SQLPropertyExpr) {
            this.visit((SQLPropertyExpr)expr);
        }
        else if (expr instanceof SQLAggregateExpr) {
            this.visit((SQLAggregateExpr)expr);
        }
        else {
            expr.accept(this);
        }
        final String alias = x.getAlias();
        if (alias != null && alias.length() > 0) {
            final String alias2 = x.getAlias2();
            this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.AS, new SqlNode[] { this.sqlNode, (SqlNode)new SqlIdentifier(SQLUtils.normalize(alias2, DbType.mysql), SqlParserPos.ZERO) }, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        if (x.getName().equalsIgnoreCase("unknown")) {
            this.sqlNode = (SqlNode)SqlLiteral.createUnknown(SqlParserPos.ZERO);
            return false;
        }
        this.sqlNode = (SqlNode)this.buildIdentifier(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        this.sqlNode = (SqlNode)this.buildIdentifier(x);
        return false;
    }
    
    SqlIdentifier buildIdentifier(final SQLIdentifierExpr x) {
        return new SqlIdentifier(SQLUtils.normalize(x.getName()), SqlParserPos.ZERO);
    }
    
    SqlIdentifier buildIdentifier(final SQLPropertyExpr x) {
        String name = SQLUtils.normalize(x.getName());
        if ("*".equals(name)) {
            name = "";
        }
        final SQLExpr owner = x.getOwner();
        List<String> names;
        if (owner instanceof SQLIdentifierExpr) {
            names = Arrays.asList(((SQLIdentifierExpr)owner).normalizedName(), name);
        }
        else {
            if (!(owner instanceof SQLPropertyExpr)) {
                throw new FastsqlException("not support : " + owner);
            }
            names = new ArrayList<String>();
            this.buildIdentifier((SQLPropertyExpr)owner, names);
            names.add(name);
        }
        return new SqlIdentifier((List)names, SqlParserPos.ZERO);
    }
    
    void buildIdentifier(final SQLPropertyExpr x, final List<String> names) {
        final String name = SQLUtils.normalize(x.getName());
        final SQLExpr owner = x.getOwner();
        if (owner instanceof SQLIdentifierExpr) {
            names.add(((SQLIdentifierExpr)owner).normalizedName());
        }
        else {
            if (!(owner instanceof SQLPropertyExpr)) {
                throw new FastsqlException("not support : " + owner);
            }
            this.buildIdentifier((SQLPropertyExpr)owner, names);
        }
        names.add(name);
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExprGroup x) {
        SqlOperator operator = null;
        switch (x.getOperator()) {
            case BooleanAnd: {
                operator = (SqlOperator)SqlStdOperatorTable.AND;
                break;
            }
            case BooleanOr: {
                operator = (SqlOperator)SqlStdOperatorTable.OR;
                break;
            }
        }
        final List<SQLExpr> items = x.getItems();
        SqlNode group = null;
        for (int i = 0; i < items.size(); ++i) {
            final SQLExpr item = items.get(i);
            final SqlNode calciteNode = this.convertToSqlNode(item);
            if (group == null) {
                group = calciteNode;
            }
            else {
                group = (SqlNode)new SqlBasicCall(operator, new SqlNode[] { group, calciteNode }, SqlParserPos.ZERO);
            }
        }
        this.sqlNode = group;
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        SqlOperator operator = null;
        SqlQuantifyOperator someOrAllOperator = null;
        final SqlNode left = this.convertToSqlNode(x.getLeft());
        final SQLExpr rightExpr = x.getRight();
        final SqlNode right = this.convertToSqlNode(rightExpr);
        switch (x.getOperator()) {
            case Equality: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_EQ;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_EQ;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.EQUALS;
                break;
            }
            case GreaterThan: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_GT;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_GT;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.GREATER_THAN;
                break;
            }
            case GreaterThanOrEqual: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_GE;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_GE;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.GREATER_THAN_OR_EQUAL;
                break;
            }
            case LessThan: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_LT;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_LT;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.LESS_THAN;
                break;
            }
            case LessThanOrEqual: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_LE;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_LE;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.LESS_THAN_OR_EQUAL;
                break;
            }
            case NotEqual:
            case LessThanOrGreater: {
                if (this.isSqlAllExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.ALL_NE;
                    break;
                }
                if (this.isAnyOrSomeExpr(rightExpr)) {
                    someOrAllOperator = SqlStdOperatorTable.SOME_NE;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.NOT_EQUALS;
                break;
            }
            case Add: {
                operator = (SqlOperator)SqlStdOperatorTable.PLUS;
                break;
            }
            case Subtract: {
                operator = (SqlOperator)SqlStdOperatorTable.MINUS;
                break;
            }
            case Multiply: {
                operator = (SqlOperator)SqlStdOperatorTable.MULTIPLY;
                break;
            }
            case Divide: {
                operator = (SqlOperator)SqlStdOperatorTable.DIVIDE;
                break;
            }
            case Modulus: {
                operator = (SqlOperator)SqlStdOperatorTable.MOD;
                break;
            }
            case Like: {
                operator = (SqlOperator)SqlStdOperatorTable.LIKE;
                break;
            }
            case NotLike: {
                operator = (SqlOperator)SqlStdOperatorTable.NOT_LIKE;
                break;
            }
            case BooleanAnd: {
                operator = (SqlOperator)SqlStdOperatorTable.AND;
                break;
            }
            case BooleanOr: {
                operator = (SqlOperator)SqlStdOperatorTable.OR;
                break;
            }
            case Concat: {
                operator = (SqlOperator)SqlStdOperatorTable.CONCAT;
                break;
            }
            case Is: {
                if (rightExpr instanceof SQLNullExpr) {
                    operator = (SqlOperator)SqlStdOperatorTable.IS_NULL;
                    break;
                }
                if (rightExpr instanceof SQLIdentifierExpr) {
                    final long hashCode64 = ((SQLIdentifierExpr)rightExpr).nameHashCode64();
                    if (hashCode64 == FnvHash.Constants.JSON || hashCode64 == CalciteMySqlNodeVisitor.JSON_VALUE) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_JSON_VALUE;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_OBJECT) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_JSON_OBJECT;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_ARRAY) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_JSON_ARRAY;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_SCALAR) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_JSON_SCALAR;
                    }
                    else if (hashCode64 == FnvHash.Constants.UNKNOWN) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_UNKNOWN;
                    }
                    break;
                }
                if (!(rightExpr instanceof SQLBooleanExpr)) {
                    break;
                }
                if (((SQLBooleanExpr)rightExpr).getValue()) {
                    operator = (SqlOperator)SqlStdOperatorTable.IS_TRUE;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.IS_FALSE;
                break;
            }
            case IsNot: {
                if (rightExpr instanceof SQLNullExpr) {
                    operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_NULL;
                    break;
                }
                if (rightExpr instanceof SQLIdentifierExpr) {
                    final long hashCode64 = ((SQLIdentifierExpr)rightExpr).nameHashCode64();
                    if (hashCode64 == FnvHash.Constants.JSON || hashCode64 == CalciteMySqlNodeVisitor.JSON_VALUE) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_JSON_VALUE;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_OBJECT) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_JSON_OBJECT;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_ARRAY) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_JSON_ARRAY;
                    }
                    else if (hashCode64 == CalciteMySqlNodeVisitor.JSON_SCALAR) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_JSON_SCALAR;
                    }
                    else if (hashCode64 == FnvHash.Constants.UNKNOWN) {
                        operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_UNKNOWN;
                    }
                    break;
                }
                if (!(rightExpr instanceof SQLBooleanExpr)) {
                    break;
                }
                if (((SQLBooleanExpr)rightExpr).getValue()) {
                    operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_TRUE;
                    break;
                }
                operator = (SqlOperator)SqlStdOperatorTable.IS_NOT_FALSE;
                break;
            }
            case Escape: {
                final SqlBasicCall like = (SqlBasicCall)left;
                this.sqlNode = (SqlNode)new SqlBasicCall(like.getOperator(), new SqlNode[] { like.operands[0], like.operands[1], right }, SqlParserPos.ZERO);
                return false;
            }
            default: {
                throw new FastsqlException("not support " + x.getOperator());
            }
        }
        if (someOrAllOperator != null) {
            this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)someOrAllOperator, new SqlNode[] { left, right }, SqlParserPos.ZERO);
        }
        else if (operator == SqlStdOperatorTable.IS_NULL || operator == SqlStdOperatorTable.IS_NOT_NULL || operator == SqlStdOperatorTable.IS_TRUE || operator == SqlStdOperatorTable.IS_NOT_TRUE) {
            this.sqlNode = (SqlNode)new SqlBasicCall(operator, new SqlNode[] { left }, SqlParserPos.ZERO);
        }
        else {
            this.sqlNode = (SqlNode)new SqlBasicCall(operator, new SqlNode[] { left, right }, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBetweenExpr x) {
        final SQLExpr testExpr = x.getTestExpr();
        SqlOperator sqlOperator = (SqlOperator)SqlStdOperatorTable.BETWEEN;
        if (x.isNot()) {
            sqlOperator = (SqlOperator)SqlStdOperatorTable.NOT_BETWEEN;
        }
        final SqlNode sqlNode = this.convertToSqlNode(testExpr);
        final SqlNode sqlNodeBegin = this.convertToSqlNode(x.getBeginExpr());
        final SqlNode sqlNodeEnd = this.convertToSqlNode(x.getEndExpr());
        final ArrayList<SqlNode> sqlNodes = new ArrayList<SqlNode>(3);
        sqlNodes.add(sqlNode);
        sqlNodes.add(sqlNodeBegin);
        sqlNodes.add(sqlNodeEnd);
        this.sqlNode = (SqlNode)new SqlBasicCall(sqlOperator, SqlParserUtil.toNodeArray((List)sqlNodes), SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLExistsExpr x) {
        final SqlOperator sqlOperator = (SqlOperator)SqlStdOperatorTable.EXISTS;
        SqlNode sqlNode = (SqlNode)sqlOperator.createCall(SqlParserPos.ZERO, new SqlNode[] { this.convertToSqlNode(x.getSubQuery()) });
        if (x.isNot()) {
            sqlNode = (SqlNode)SqlStdOperatorTable.NOT.createCall(SqlParserPos.ZERO, new SqlNode[] { sqlNode });
        }
        this.sqlNode = sqlNode;
        return false;
    }
    
    @Override
    public boolean visit(final SQLAllColumnExpr x) {
        this.sqlNode = (SqlNode)new SqlIdentifier((List)Arrays.asList(""), SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x) {
        String text = x.getText();
        text = text.replaceAll("\\\\", "\\\\\\\\");
        this.sqlNode = (SqlNode)SqlLiteral.createCharString(text, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLNCharExpr x) {
        String text = x.getText();
        text = text.replaceAll("\\\\", "\\\\\\\\");
        this.sqlNode = (SqlNode)SqlLiteral.createCharString(text, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLNullExpr x) {
        this.sqlNode = (SqlNode)SqlLiteral.createNull(SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLIntegerExpr x) {
        this.sqlNode = (SqlNode)SqlLiteral.createExactNumeric(x.getNumber().toString(), SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLBooleanExpr x) {
        this.sqlNode = (SqlNode)SqlLiteral.createBoolean(x.getBooleanValue(), SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLNumberExpr x) {
        final String str = x.toString();
        if (str.indexOf(69) > 0 || str.indexOf(101) > 0) {
            this.sqlNode = (SqlNode)SqlLiteral.createApproxNumeric(str, SqlParserPos.ZERO);
        }
        else {
            this.sqlNode = (SqlNode)SqlLiteral.createExactNumeric(str, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTimestampExpr x) {
        String literal = x.getLiteral();
        int precision = 0;
        if (literal.endsWith("00")) {
            final char c3 = literal.charAt(literal.length() - 3);
            if (c3 >= '0' && c3 <= '9') {
                literal = literal.substring(0, literal.length() - 2);
                precision = 3;
            }
        }
        final TimestampString ts = new TimestampString(literal);
        this.sqlNode = (SqlNode)SqlLiteral.createTimestamp(ts, precision, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDateExpr x) {
        final String literal = x.getLiteral();
        final DateString ds = new DateString(literal);
        this.sqlNode = (SqlNode)SqlLiteral.createDate(ds, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLTimeExpr x) {
        final String literal = ((SQLCharExpr)x.getLiteral()).getText();
        final TimeString ds = new TimeString(literal);
        this.sqlNode = (SqlNode)SqlLiteral.createTime(ds, 0, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCurrentTimeExpr x) {
        this.sqlNode = (SqlNode)new SqlIdentifier(x.getType().name, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAggregateExpr x) {
        final String methodName = x.getMethodName();
        final long hashCode64 = x.methodNameHashCode64();
        SqlOperator functionOperator = func(hashCode64);
        if (functionOperator == null) {
            functionOperator = (SqlOperator)new SqlUnresolvedFunction(new SqlIdentifier(methodName, SqlParserPos.ZERO), (SqlReturnTypeInference)null, (SqlOperandTypeInference)null, (SqlOperandTypeChecker)null, (List)null, SqlFunctionCategory.USER_DEFINED_FUNCTION);
        }
        SqlLiteral functionQualifier = null;
        if (x.getOption() == SQLAggregateOption.DISTINCT) {
            functionQualifier = SqlSelectKeyword.DISTINCT.symbol(SqlParserPos.ZERO);
        }
        final List<SQLExpr> arguments = x.getArguments();
        final List<SqlNode> argNodes = new ArrayList<SqlNode>(arguments.size());
        for (int i = 0, size = arguments.size(); i < size; ++i) {
            argNodes.add(this.convertToSqlNode(arguments.get(i)));
        }
        this.sqlNode = (SqlNode)functionOperator.createCall(functionQualifier, SqlParserPos.ZERO, SqlParserUtil.toNodeArray((List)argNodes));
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            final SqlNodeList orderByItems = this.convertOrderby(orderBy);
            this.sqlNode = (SqlNode)SqlStdOperatorTable.WITHIN_GROUP.createCall(SqlParserPos.ZERO, new SqlNode[] { this.sqlNode, (SqlNode)orderByItems });
        }
        final SQLOver over = x.getOver();
        if (over != null) {
            final SqlNode aggNode = this.sqlNode;
            final SQLOver.WindowingBound windowingBetweenBeginBound = over.getWindowingBetweenBeginBound();
            final SQLOver.WindowingBound windowingBetweenEndBound = over.getWindowingBetweenEndBound();
            final boolean isRow = over.getWindowingType() != SQLOver.WindowingType.RANGE;
            SqlNode lowerBound;
            if (over.getWindowingBetweenBegin() != null) {
                over.getWindowingBetweenBegin().accept(this);
                lowerBound = SqlWindow.createPreceding(this.sqlNode, SqlParserPos.ZERO);
            }
            else {
                lowerBound = createSymbol(windowingBetweenBeginBound);
            }
            final SqlNode upperBound = createSymbol(windowingBetweenEndBound);
            final SqlWindow window = new SqlWindow(SqlParserPos.ZERO, (SqlIdentifier)null, (SqlIdentifier)null, this.convertToSqlNodeList(over.getPartitionBy()), this.convertOrderby(over.getOrderBy()), SqlLiteral.createBoolean(isRow, SqlParserPos.ZERO), lowerBound, upperBound, (SqlLiteral)null);
            this.sqlNode = (SqlNode)SqlStdOperatorTable.OVER.createCall(SqlParserPos.ZERO, new SqlNode[] { aggNode, (SqlNode)window });
        }
        final SQLExpr filter = x.getFilter();
        if (filter != null) {
            final SqlNode aggNode2 = this.sqlNode;
            filter.accept(this);
            this.sqlNode = (SqlNode)SqlStdOperatorTable.FILTER.createCall(SqlParserPos.ZERO, new SqlNode[] { aggNode2, this.sqlNode });
        }
        return false;
    }
    
    protected static SqlNode createSymbol(final SQLOver.WindowingBound bound) {
        if (bound == null) {
            return null;
        }
        switch (bound) {
            case CURRENT_ROW: {
                return SqlWindow.createCurrentRow(SqlParserPos.ZERO);
            }
            case UNBOUNDED_FOLLOWING: {
                return SqlWindow.createUnboundedFollowing(SqlParserPos.ZERO);
            }
            case UNBOUNDED_PRECEDING: {
                return SqlWindow.createUnboundedPreceding(SqlParserPos.ZERO);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        final List<SqlNode> argNodes = new ArrayList<SqlNode>(arguments.size());
        final long nameHashCode64 = x.methodNameHashCode64();
        SqlOperator functionOperator = func(nameHashCode64);
        final String methodName = x.getMethodName();
        if (functionOperator == null) {
            if (nameHashCode64 == FnvHash.Constants.TRIM) {
                functionOperator = (SqlOperator)SqlStdOperatorTable.TRIM;
                if (arguments.size() == 1) {
                    final SqlNode sqlNode = this.convertToSqlNode(arguments.get(0));
                    this.sqlNode = (SqlNode)new CalciteSqlBasicCall(functionOperator, new SqlNode[] { (SqlNode)SqlLiteral.createSymbol((Enum)SqlTrimFunction.Flag.BOTH, SqlParserPos.ZERO), (SqlNode)SqlCharStringLiteral.createCharString(" ", SqlParserPos.ZERO), sqlNode }, SqlParserPos.ZERO, false, null);
                    return false;
                }
            }
            else {
                functionOperator = (SqlOperator)new SqlUnresolvedFunction(new SqlIdentifier(methodName, SqlParserPos.ZERO), (SqlReturnTypeInference)null, (SqlOperandTypeInference)null, (SqlOperandTypeChecker)null, (List)null, SqlFunctionCategory.USER_DEFINED_FUNCTION);
            }
        }
        final SqlLiteral functionQualifier = null;
        for (final SQLExpr exp : arguments) {
            argNodes.add(this.convertToSqlNode(exp));
        }
        if ((nameHashCode64 == FnvHash.Constants.TIMESTAMPDIFF || nameHashCode64 == FnvHash.Constants.TIMESTAMPADD) && argNodes.size() > 0 && argNodes.get(0) instanceof SqlIdentifier) {
            final SqlIdentifier arg0 = (SqlIdentifier)argNodes.get(0);
            final TimeUnit timeUnit = TimeUnit.valueOf(arg0.toString().toUpperCase());
            argNodes.set(0, (SqlNode)SqlLiteral.createSymbol((Enum)timeUnit, SqlParserPos.ZERO));
        }
        this.sqlNode = (SqlNode)new CalciteSqlBasicCall(functionOperator, SqlParserUtil.toNodeArray((List)argNodes), SqlParserPos.ZERO, false, functionQualifier);
        return false;
    }
    
    @Override
    public boolean visit(final SQLInListExpr x) {
        final SqlNodeList sqlNodes = this.convertToSqlNodeList(x.getTargetList());
        final SqlOperator sqlOperator = (SqlOperator)(x.isNot() ? SqlStdOperatorTable.NOT_IN : SqlStdOperatorTable.IN);
        this.sqlNode = (SqlNode)new SqlBasicCall(sqlOperator, new SqlNode[] { this.convertToSqlNode(x.getExpr()), (SqlNode)sqlNodes }, SqlParserPos.ZERO);
        return false;
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        if ("?".equals(x.getName())) {
            this.sqlNode = (SqlNode)new SqlDynamicParam(x.getIndex(), SqlParserPos.ZERO);
            return false;
        }
        System.out.println("end");
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnaryExpr x) {
        final SQLUnaryOperator operator = x.getOperator();
        switch (operator) {
            case NOT: {
                this.sqlNode = (SqlNode)SqlStdOperatorTable.NOT.createCall(SqlParserPos.ZERO, new SqlNode[] { this.convertToSqlNode(x.getExpr()) });
                break;
            }
            case Negative: {
                this.sqlNode = (SqlNode)SqlStdOperatorTable.UNARY_MINUS.createCall(SqlParserPos.ZERO, new SqlNode[] { this.convertToSqlNode(x.getExpr()) });
                break;
            }
            default: {
                super.visit(x);
                break;
            }
        }
        return false;
    }
    
    protected SqlNodeList convertToSqlNodeList(final SQLExpr expr) {
        if (expr instanceof SQLListExpr) {
            return this.convertToSqlNodeList(((SQLListExpr)expr).getItems());
        }
        final List<SqlNode> nodes = new ArrayList<SqlNode>(1);
        return new SqlNodeList((Collection)nodes, SqlParserPos.ZERO);
    }
    
    protected SqlNodeList convertToSqlNodeList(final List<? extends SQLExpr> exprList) {
        final int size = exprList.size();
        final List<SqlNode> nodes = new ArrayList<SqlNode>(size);
        for (int i = 0; i < size; ++i) {
            final SQLExpr expr = (SQLExpr)exprList.get(i);
            SqlNode node;
            if (expr instanceof SQLListExpr) {
                node = (SqlNode)this.convertToSqlNodeList(((SQLListExpr)expr).getItems());
            }
            else {
                node = this.convertToSqlNode(expr);
            }
            nodes.add(node);
        }
        return new SqlNodeList((Collection)nodes, SqlParserPos.ZERO);
    }
    
    protected SqlNode convertToSqlNode(final SQLObject ast) {
        if (ast == null) {
            return null;
        }
        final CalciteMySqlNodeVisitor visitor = new CalciteMySqlNodeVisitor();
        ast.accept(visitor);
        return visitor.getSqlNode();
    }
    
    private SqlNodeList convertOrderby(final SQLOrderBy orderBy) {
        if (orderBy == null) {
            return new SqlNodeList((Collection)new ArrayList(), SqlParserPos.ZERO);
        }
        final List<SQLSelectOrderByItem> items = orderBy.getItems();
        final List<SqlNode> orderByNodes = new ArrayList<SqlNode>(items.size());
        for (final SQLSelectOrderByItem item : items) {
            SqlNode node = this.convertToSqlNode(item.getExpr());
            if (item.getType() == SQLOrderingSpecification.DESC) {
                node = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.DESC, new SqlNode[] { node }, SqlParserPos.ZERO);
            }
            final SQLSelectOrderByItem.NullsOrderType nullsOrderType = item.getNullsOrderType();
            if (nullsOrderType != null) {
                switch (nullsOrderType) {
                    case NullsFirst: {
                        node = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.NULLS_FIRST, new SqlNode[] { node }, SqlParserPos.ZERO);
                        break;
                    }
                    case NullsLast: {
                        node = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.NULLS_LAST, new SqlNode[] { node }, SqlParserPos.ZERO);
                        break;
                    }
                }
            }
            orderByNodes.add(node);
        }
        return new SqlNodeList((Collection)orderByNodes, SqlParserPos.ZERO);
    }
    
    private SqlNodeList convertHints(final List<SQLCommentHint> hints) {
        if (hints == null) {
            return null;
        }
        final List<SqlNode> nodes = new ArrayList<SqlNode>(hints.size());
        for (final SQLCommentHint hint : hints) {
            if (hint instanceof TDDLHint) {
                nodes.add((SqlNode)this.convertTDDLHint((TDDLHint)hint));
            }
        }
        return new SqlNodeList((Collection)nodes, SqlParserPos.ZERO);
    }
    
    private SqlNodeList convertTDDLHint(final TDDLHint hint) {
        final List<TDDLHint.Function> functions = hint.getFunctions();
        final List<SqlNode> funNodes = new ArrayList<SqlNode>(functions.size());
        for (final TDDLHint.Function function : functions) {
            final String functionName = function.getName();
            final List<TDDLHint.Argument> arguments = function.getArguments();
            final SqlNode[] argNodes = new SqlNode[arguments.size()];
            for (int i = 0; i < arguments.size(); ++i) {
                final TDDLHint.Argument argument = arguments.get(i);
                final SqlNode argName = this.convertToSqlNode(argument.getName());
                final SqlNode argValue = this.convertToSqlNode(argument.getValue());
                final List<SqlNode> arg = new ArrayList<SqlNode>();
                if (argName != null) {
                    arg.add(argName);
                }
                if (argValue != null) {
                    arg.add(argValue);
                }
                SqlNode argNode = null;
                if (arg.size() == 2) {
                    argNode = (SqlNode)SqlStdOperatorTable.EQUALS.createCall(SqlParserPos.ZERO, (List)arg);
                }
                else if (arg.size() == 1) {
                    argNode = argName;
                }
                argNodes[i] = argNode;
            }
            final SqlNode funNode = (SqlNode)new SqlBasicCall((SqlOperator)new SqlUnresolvedFunction(new SqlIdentifier(functionName, SqlParserPos.ZERO), (SqlReturnTypeInference)null, (SqlOperandTypeInference)null, (SqlOperandTypeChecker)null, (List)null, SqlFunctionCategory.USER_DEFINED_FUNCTION), argNodes, SqlParserPos.ZERO);
            funNodes.add(funNode);
        }
        return new SqlNodeList((Collection)funNodes, SqlParserPos.ZERO);
    }
    
    public static List<SQLInsertStatement.ValuesClause> convertToSingleValuesIfNeed(final List<SQLInsertStatement.ValuesClause> valuesClauseList) {
        if (valuesClauseList.size() <= 1) {
            return valuesClauseList;
        }
        for (final SQLInsertStatement.ValuesClause clause : valuesClauseList) {
            for (final SQLExpr expr : clause.getValues()) {
                if (expr instanceof SQLVariantRefExpr && ((SQLVariantRefExpr)expr).getName().equals("?")) {
                    continue;
                }
                return valuesClauseList;
            }
        }
        return Arrays.asList(valuesClauseList.get(0));
    }
    
    @Override
    public boolean visit(final SQLIntervalExpr x) {
        final TimeUnit[] timeUnits = getTimeUnit(x.getUnit());
        final List<SqlNode> convertedArgs = new ArrayList<SqlNode>(2);
        final SqlIntervalQualifier unitNode = new SqlIntervalQualifier(timeUnits[0], timeUnits[1], SqlParserPos.ZERO);
        final SqlLiteral valueNode = (SqlLiteral)this.convertToSqlNode(x.getValue());
        this.sqlNode = (SqlNode)SqlIntervalLiteral.createInterval(1, valueNode.toValue(), unitNode, SqlParserPos.ZERO);
        return false;
    }
    
    public static TimeUnit[] getTimeUnit(final SQLIntervalUnit unit) {
        final TimeUnit[] timeUnits = new TimeUnit[2];
        switch (unit) {
            case SECOND: {
                timeUnits[0] = TimeUnit.SECOND;
                timeUnits[1] = TimeUnit.SECOND;
                break;
            }
            case MINUTE: {
                timeUnits[0] = TimeUnit.MINUTE;
                timeUnits[1] = TimeUnit.MINUTE;
                break;
            }
            case HOUR: {
                timeUnits[0] = TimeUnit.HOUR;
                timeUnits[1] = TimeUnit.HOUR;
                break;
            }
            case DAY: {
                timeUnits[0] = TimeUnit.DAY;
                timeUnits[1] = TimeUnit.DAY;
                break;
            }
            case WEEK: {
                timeUnits[0] = TimeUnit.WEEK;
                timeUnits[1] = TimeUnit.WEEK;
                break;
            }
            case MONTH: {
                timeUnits[0] = TimeUnit.MONTH;
                timeUnits[1] = TimeUnit.MONTH;
                break;
            }
            case QUARTER: {
                timeUnits[0] = TimeUnit.QUARTER;
                timeUnits[1] = TimeUnit.QUARTER;
                break;
            }
            case YEAR: {
                timeUnits[0] = TimeUnit.YEAR;
                timeUnits[1] = TimeUnit.YEAR;
                break;
            }
            case MINUTE_SECOND: {
                timeUnits[0] = TimeUnit.MINUTE;
                timeUnits[1] = TimeUnit.SECOND;
                break;
            }
            case HOUR_SECOND: {
                timeUnits[0] = TimeUnit.HOUR;
                timeUnits[1] = TimeUnit.SECOND;
                break;
            }
            case HOUR_MINUTE: {
                timeUnits[0] = TimeUnit.HOUR;
                timeUnits[1] = TimeUnit.MINUTE;
                break;
            }
            case DAY_SECOND: {
                timeUnits[0] = TimeUnit.DAY;
                timeUnits[1] = TimeUnit.SECOND;
                break;
            }
            case DAY_MINUTE: {
                timeUnits[0] = TimeUnit.DAY;
                timeUnits[1] = TimeUnit.MINUTE;
                break;
            }
            case DAY_HOUR: {
                timeUnits[0] = TimeUnit.DAY;
                timeUnits[1] = TimeUnit.HOUR;
                break;
            }
            case YEAR_MONTH: {
                timeUnits[0] = TimeUnit.YEAR;
                timeUnits[1] = TimeUnit.MONTH;
                break;
            }
            default: {
                throw new ParserException("Unsupported time unit");
            }
        }
        return timeUnits;
    }
    
    @Override
    public boolean visit(final SQLNotExpr x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIdentifierExpr) {
            final long hashCode64 = ((SQLIdentifierExpr)expr).nameHashCode64();
            if (hashCode64 == FnvHash.Constants.UNKNOWN) {
                this.sqlNode = (SqlNode)SqlStdOperatorTable.NOT.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)SqlLiteral.createUnknown(SqlParserPos.ZERO) });
                return false;
            }
        }
        expr.accept(this);
        this.sqlNode = (SqlNode)SqlStdOperatorTable.NOT.createCall(SqlParserPos.ZERO, new SqlNode[] { this.sqlNode });
        return false;
    }
    
    @Override
    public boolean visit(final SQLExtractExpr x) {
        x.getValue().accept(this);
        final TimeUnit[] timeUnits = getTimeUnit(x.getUnit());
        this.sqlNode = (SqlNode)SqlStdOperatorTable.EXTRACT.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)new SqlIntervalQualifier(timeUnits[0], timeUnits[1], SqlParserPos.ZERO), this.sqlNode });
        return false;
    }
    
    @Override
    public boolean visit(final SQLGroupingSetExpr x) {
        this.sqlNode = (SqlNode)SqlStdOperatorTable.GROUPING_SETS.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)this.convertToSqlNodeList(x.getParameters()) });
        return false;
    }
    
    @Override
    public boolean visit(final SQLValuesQuery x) {
        final List<SqlNode> valuesNodes = new ArrayList<SqlNode>();
        for (final SQLExpr value : x.getValues()) {
            valuesNodes.add((SqlNode)SqlStdOperatorTable.ROW.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)this.convertToSqlNodeList(value) }));
        }
        this.sqlNode = (SqlNode)SqlStdOperatorTable.VALUES.createCall(SqlParserPos.ZERO, (List)valuesNodes);
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnnestTableSource x) {
        this.sqlNode = (SqlNode)SqlStdOperatorTable.UNNEST.createCall(SqlParserPos.ZERO, new SqlNode[] { (SqlNode)this.convertToSqlNodeList(x.getItems()) });
        final String alias = x.getAlias();
        if (alias != null) {
            this.sqlNode = (SqlNode)new SqlBasicCall((SqlOperator)SqlStdOperatorTable.AS, new SqlNode[] { this.sqlNode, (SqlNode)new SqlIdentifier(alias, SqlParserPos.ZERO) }, SqlParserPos.ZERO);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDefaultExpr x) {
        this.sqlNode = (SqlNode)SqlStdOperatorTable.DEFAULT.createCall(SqlParserPos.ZERO, new SqlNode[0]);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExplainStatement x) {
        x.getStatement().accept(this);
        final SqlNode explicandum = this.sqlNode;
        this.sqlNode = (SqlNode)new SqlExplain(SqlParserPos.ZERO, explicandum, SqlLiteral.createSymbol((Enum)SqlExplainLevel.EXPPLAN_ATTRIBUTES, SqlParserPos.ZERO), SqlLiteral.createSymbol((Enum)SqlExplain.Depth.PHYSICAL, SqlParserPos.ZERO), SqlLiteral.createSymbol((Enum)SqlExplainFormat.TEXT, SqlParserPos.ZERO), 0);
        return false;
    }
    
    static {
        CalciteMySqlNodeVisitor.operators = new HashMap<Long, SqlOperator>();
        final List<SqlOperator> list = (List<SqlOperator>)SqlStdOperatorTable.instance().getOperatorList();
        for (final SqlOperator op : list) {
            final long h = FnvHash.hashCode64(op.getName());
            if (h == FnvHash.Constants.TRIM) {
                continue;
            }
            CalciteMySqlNodeVisitor.operators.put(h, op);
        }
        CalciteMySqlNodeVisitor.operators.put(FnvHash.Constants.CEILING, (SqlOperator)SqlStdOperatorTable.CEIL);
        (CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping = new HashMap<Long, SqlTypeName>()).put(FnvHash.Constants.BIT, SqlTypeName.BOOLEAN);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.BOOLEAN, SqlTypeName.BOOLEAN);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.TINYINT, SqlTypeName.TINYINT);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.SMALLINT, SqlTypeName.SMALLINT);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.INT, SqlTypeName.INTEGER);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.INTEGER, SqlTypeName.INTEGER);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.BIGINT, SqlTypeName.BIGINT);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.DECIMAL, SqlTypeName.DECIMAL);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.FLOAT, SqlTypeName.FLOAT);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.REAL, SqlTypeName.REAL);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.DOUBLE, SqlTypeName.DOUBLE);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.DATE, SqlTypeName.DATE);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.TIME, SqlTypeName.TIME);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.TIMESTAMP, SqlTypeName.TIMESTAMP);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.CHAR, SqlTypeName.CHAR);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.VARCHAR, SqlTypeName.VARCHAR);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.BINARY, SqlTypeName.BINARY);
        CalciteMySqlNodeVisitor.nameHashCode64SqlTypeNameMapping.put(FnvHash.Constants.VARBINARY, SqlTypeName.VARBINARY);
        CalciteMySqlNodeVisitor.JSON_VALUE = FnvHash.fnv1a_64_lower("JSON VALUE");
        CalciteMySqlNodeVisitor.JSON_OBJECT = FnvHash.fnv1a_64_lower("JSON OBJECT");
        CalciteMySqlNodeVisitor.JSON_ARRAY = FnvHash.fnv1a_64_lower("JSON ARRAY");
        CalciteMySqlNodeVisitor.JSON_SCALAR = FnvHash.fnv1a_64_lower("JSON SCALAR");
    }
}
