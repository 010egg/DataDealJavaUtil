// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock.handler;

import com.alibaba.druid.mock.MockPreparedStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import java.sql.Timestamp;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.mock.MockResultSetMetaData;
import com.alibaba.druid.util.jdbc.ResultSetMetaDataBase;
import java.sql.Statement;
import com.alibaba.druid.mock.MockResultSet;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.sql.SQLException;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import java.sql.ResultSet;
import com.alibaba.druid.mock.MockStatementBase;

public class MySqlMockExecuteHandlerImpl implements MockExecuteHandler
{
    @Override
    public ResultSet executeQuery(final MockStatementBase statement, final String sql) throws SQLException {
        final SQLStatementParser parser = new MySqlStatementParser(sql);
        final List<SQLStatement> stmtList = parser.parseStatementList();
        if (stmtList.size() > 1) {
            throw new SQLException("not support multi-statment. " + sql);
        }
        if (stmtList.size() == 0) {
            throw new SQLException("executeQueryError : " + sql);
        }
        final SQLStatement stmt = stmtList.get(0);
        if (stmt instanceof CobarShowStatus) {
            return this.showStatus(statement);
        }
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new SQLException("executeQueryError : " + sql);
        }
        final SQLSelect select = ((SQLSelectStatement)stmt).getSelect();
        final SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            return this.executeQuery(statement, (SQLSelectQueryBlock)query);
        }
        throw new SQLException("TODO");
    }
    
    public ResultSet executeQuery(final MockStatementBase statement, final SQLSelectQueryBlock query) throws SQLException {
        final SQLTableSource from = query.getFrom();
        if (from instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)from).getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final String ident = ((SQLIdentifierExpr)expr).getName();
                if ("dual".equalsIgnoreCase(ident)) {
                    return this.executeQueryFromDual(statement, query);
                }
            }
            throw new SQLException("TODO : " + query);
        }
        if (from == null) {
            return this.executeQueryFromDual(statement, query);
        }
        throw new SQLException("TODO");
    }
    
    public ResultSet showStatus(final MockStatementBase statement) throws SQLException {
        final MockResultSet rs = new MockResultSet(statement);
        final MockResultSetMetaData metaData = rs.getMockMetaData();
        final Object[] row = { "on" };
        final ResultSetMetaDataBase.ColumnMetaData column = new ResultSetMetaDataBase.ColumnMetaData();
        column.setColumnType(-9);
        metaData.getColumns().add(column);
        rs.getRows().add(row);
        return rs;
    }
    
    public ResultSet executeQueryFromDual(final MockStatementBase statement, final SQLSelectQueryBlock query) throws SQLException {
        final MockResultSet rs = statement.getConnection().getDriver().createMockResultSet(statement);
        final MockResultSetMetaData metaData = rs.getMockMetaData();
        final Object[] row = new Object[query.getSelectList().size()];
        for (int i = 0, size = query.getSelectList().size(); i < size; ++i) {
            final ResultSetMetaDataBase.ColumnMetaData column = new ResultSetMetaDataBase.ColumnMetaData();
            final SQLSelectItem item = query.getSelectList().get(i);
            final SQLExpr expr = item.getExpr();
            if (expr instanceof SQLIntegerExpr) {
                row[i] = ((SQLNumericLiteralExpr)expr).getNumber();
                column.setColumnType(4);
            }
            else if (expr instanceof SQLNumberExpr) {
                row[i] = ((SQLNumericLiteralExpr)expr).getNumber();
                column.setColumnType(3);
            }
            else if (expr instanceof SQLCharExpr) {
                row[i] = ((SQLCharExpr)expr).getText();
                column.setColumnType(12);
            }
            else if (expr instanceof SQLNCharExpr) {
                row[i] = ((SQLNCharExpr)expr).getText();
                column.setColumnType(-9);
            }
            else if (expr instanceof SQLBooleanExpr) {
                row[i] = ((SQLBooleanExpr)expr).getBooleanValue();
                column.setColumnType(-9);
            }
            else if (expr instanceof SQLNullExpr) {
                row[i] = null;
            }
            else if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                if (!"NOW".equalsIgnoreCase(methodInvokeExpr.getMethodName())) {
                    throw new SQLException("TODO");
                }
                row[i] = new Timestamp(System.currentTimeMillis());
            }
            else if (expr instanceof SQLVariantRefExpr) {
                final SQLVariantRefExpr varExpr = (SQLVariantRefExpr)expr;
                final int varIndex = varExpr.getIndex();
                if (statement instanceof MockPreparedStatement) {
                    final MockPreparedStatement mockPstmt = (MockPreparedStatement)statement;
                    row[i] = mockPstmt.getParameters().get(varIndex);
                }
                else {
                    row[i] = null;
                }
            }
            else {
                row[i] = null;
            }
            metaData.getColumns().add(column);
        }
        rs.getRows().add(row);
        return rs;
    }
}
