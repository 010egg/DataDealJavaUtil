// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsReadStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitorAdapter;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlCodeStat extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName) {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName, final boolean throwError) {
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        try {
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, DbType.odps, SQLParserFeature.EnableMultiUnion, SQLParserFeature.EnableSQLBinaryOpExprGroup);
            final CodeStatVisitor v = new CodeStatVisitor();
            for (final SQLStatement stmt : stmtList) {
                stmt.accept(v);
            }
            return v.toString();
        }
        catch (Exception ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return null;
        }
    }
    
    public static class SqlStat
    {
        @JSONField(ordinal = 0)
        public int statementCount;
        @JSONField(ordinal = 1)
        public int cte;
        @JSONField(ordinal = 2)
        public int union;
        @JSONField(ordinal = 3)
        public int select;
        @JSONField(ordinal = 4)
        public int groupBy;
        @JSONField(ordinal = 5)
        public int orderBy;
        @JSONField(ordinal = 6)
        public int from;
        @JSONField(ordinal = 7)
        public int join;
        @JSONField(ordinal = 8)
        public int over;
        @JSONField(ordinal = 9)
        public int subQuery;
        @JSONField(ordinal = 10)
        public int lateralView;
        @JSONField(ordinal = 40)
        public int insert;
        @JSONField(ordinal = 41)
        public int insertInto;
        @JSONField(ordinal = 42)
        public int insertOverwrite;
        @JSONField(ordinal = 43)
        public int insertSelect;
        @JSONField(ordinal = 44)
        public int insertMulti;
        @JSONField(ordinal = 51)
        public int update;
        @JSONField(ordinal = 52)
        public int delete;
        @JSONField(ordinal = 60)
        public int create;
        @JSONField(ordinal = 61)
        public int createTable;
        @JSONField(ordinal = 62)
        public int createView;
        @JSONField(ordinal = 70)
        public int drop;
        @JSONField(ordinal = 71)
        public int dropTable;
        @JSONField(ordinal = 72)
        public int dropView;
        @JSONField(ordinal = 80)
        public int set;
        @JSONField(ordinal = 90)
        public int alter;
        @JSONField(ordinal = 100)
        public int read;
        @JSONField(ordinal = 200)
        public int condition;
        @JSONField(ordinal = 201)
        public int joinCondition;
        @JSONField(ordinal = 202)
        public int valueCondition;
        @JSONField(ordinal = 203)
        public int otherCondition;
        @JSONField(ordinal = 204)
        public int limit;
        @JSONField(ordinal = 300)
        public int aggregate;
        @JSONField(ordinal = 201)
        public int functionCall;
        @JSONField(ordinal = 202)
        public int having;
    }
    
    static class CodeStatVisitor extends OdpsASTVisitorAdapter
    {
        SqlStat stat;
        
        CodeStatVisitor() {
            this.stat = new SqlStat();
        }
        
        @Override
        public void preVisit(final SQLObject x) {
            if (x instanceof SQLStatement) {
                final SqlStat stat = this.stat;
                ++stat.statementCount;
                if (x instanceof SQLInsertStatement) {
                    final SqlStat stat2 = this.stat;
                    ++stat2.insert;
                    final SQLInsertStatement insert = (SQLInsertStatement)x;
                    if (insert.getQuery() != null) {
                        final SqlStat stat3 = this.stat;
                        ++stat3.insertSelect;
                    }
                    if (insert.isOverwrite()) {
                        final SqlStat stat4 = this.stat;
                        ++stat4.insertOverwrite;
                    }
                    else {
                        final SqlStat stat5 = this.stat;
                        ++stat5.insertInto;
                    }
                }
                else if (x instanceof HiveMultiInsertStatement) {
                    final SqlStat stat6 = this.stat;
                    ++stat6.insert;
                    final SqlStat stat7 = this.stat;
                    ++stat7.insertMulti;
                    final SqlStat stat8 = this.stat;
                    ++stat8.insertSelect;
                    for (final HiveInsert item : ((HiveMultiInsertStatement)x).getItems()) {
                        if (item.isOverwrite()) {
                            final SqlStat stat9 = this.stat;
                            ++stat9.insertOverwrite;
                        }
                        else {
                            final SqlStat stat10 = this.stat;
                            ++stat10.insertInto;
                        }
                    }
                }
                else if (x instanceof SQLDropStatement) {
                    final SqlStat stat11 = this.stat;
                    ++stat11.drop;
                    if (x instanceof SQLDropTableStatement) {
                        final SqlStat stat12 = this.stat;
                        ++stat12.dropTable;
                    }
                    else if (x instanceof SQLDropViewStatement) {
                        final SqlStat stat13 = this.stat;
                        ++stat13.dropView;
                    }
                }
                else if (x instanceof SQLCreateStatement) {
                    final SqlStat stat14 = this.stat;
                    ++stat14.create;
                    if (x instanceof SQLCreateTableStatement) {
                        final SqlStat stat15 = this.stat;
                        ++stat15.createTable;
                    }
                    else if (x instanceof SQLCreateViewStatement) {
                        final SqlStat stat16 = this.stat;
                        ++stat16.createView;
                    }
                }
                else if (x instanceof SQLDeleteStatement) {
                    final SqlStat stat17 = this.stat;
                    ++stat17.delete;
                }
                else if (x instanceof SQLUpdateStatement) {
                    final SqlStat stat18 = this.stat;
                    ++stat18.update;
                }
                else if (x instanceof SQLSetStatement) {
                    final SqlStat stat19 = this.stat;
                    ++stat19.set;
                }
                else if (x instanceof SQLAlterStatement) {
                    final SqlStat stat20 = this.stat;
                    ++stat20.alter;
                }
                else if (x instanceof OdpsReadStatement) {
                    final SqlStat stat21 = this.stat;
                    ++stat21.read;
                }
            }
        }
        
        @Override
        public boolean visit(final SQLUnionQuery x) {
            final SqlStat stat = this.stat;
            ++stat.union;
            return true;
        }
        
        @Override
        public boolean visit(final SQLLateralViewTableSource x) {
            final SqlStat stat = this.stat;
            ++stat.lateralView;
            return true;
        }
        
        @Override
        public boolean visit(final OdpsSelectQueryBlock x) {
            return this.visit((SQLSelectQueryBlock)x);
        }
        
        @Override
        public boolean visit(final SQLSelectQueryBlock x) {
            final SqlStat stat = this.stat;
            ++stat.select;
            return true;
        }
        
        @Override
        public boolean visit(final SQLSelectGroupByClause x) {
            final SqlStat stat = this.stat;
            ++stat.groupBy;
            if (x.getHaving() != null) {
                final SqlStat stat2 = this.stat;
                ++stat2.having;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLLimit x) {
            final SqlStat stat = this.stat;
            ++stat.limit;
            return false;
        }
        
        @Override
        public boolean visit(final SQLWithSubqueryClause.Entry x) {
            final SqlStat stat = this.stat;
            ++stat.cte;
            return true;
        }
        
        @Override
        public boolean visit(final SQLOrderBy x) {
            final SqlStat stat = this.stat;
            ++stat.orderBy;
            return true;
        }
        
        @Override
        public boolean visit(final SQLExprTableSource x) {
            final SqlStat stat = this.stat;
            ++stat.from;
            return true;
        }
        
        @Override
        public boolean visit(final SQLSubqueryTableSource x) {
            final SqlStat stat = this.stat;
            ++stat.from;
            final SqlStat stat2 = this.stat;
            ++stat2.subQuery;
            return true;
        }
        
        @Override
        public boolean visit(final SQLJoinTableSource x) {
            if (!(x.getParent() instanceof SQLJoinTableSource)) {
                final SqlStat stat = this.stat;
                ++stat.from;
            }
            final SqlStat stat2 = this.stat;
            ++stat2.join;
            return true;
        }
        
        @Override
        public boolean visit(final SQLMethodInvokeExpr x) {
            final SqlStat stat = this.stat;
            ++stat.functionCall;
            return true;
        }
        
        @Override
        public boolean visit(final SQLAggregateExpr x) {
            final SqlStat stat = this.stat;
            ++stat.aggregate;
            return true;
        }
        
        @Override
        public boolean visit(final SQLOver x) {
            final SqlStat stat = this.stat;
            ++stat.over;
            return true;
        }
        
        @Override
        public boolean visit(final SQLCastExpr x) {
            final SqlStat stat = this.stat;
            ++stat.functionCall;
            return true;
        }
        
        @Override
        public boolean visit(final SQLInListExpr x) {
            final SqlStat stat = this.stat;
            ++stat.condition;
            return true;
        }
        
        @Override
        public boolean visit(final SQLBinaryOpExpr x) {
            if (x.getOperator() != null && x.getOperator().isRelational()) {
                final SqlStat stat = this.stat;
                ++stat.condition;
                final SQLExpr left = x.getLeft();
                final SQLExpr right = x.getRight();
                if (left instanceof SQLName && right instanceof SQLName) {
                    final SqlStat stat2 = this.stat;
                    ++stat2.joinCondition;
                }
                else if ((left instanceof SQLName || right instanceof SQLName) && (left instanceof SQLLiteralExpr || right instanceof SQLLiteralExpr)) {
                    final SqlStat stat3 = this.stat;
                    ++stat3.valueCondition;
                }
                else {
                    final SqlStat stat4 = this.stat;
                    ++stat4.otherCondition;
                }
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLCaseExpr x) {
            final SQLExpr value = x.getValueExpr();
            if (value != null) {
                final SqlStat stat = this.stat;
                stat.condition += x.getItems().size();
            }
            if (x.getElseExpr() != null) {
                final SqlStat stat2 = this.stat;
                ++stat2.condition;
            }
            return true;
        }
        
        @Override
        public boolean visit(final SQLExistsExpr x) {
            final SqlStat stat = this.stat;
            ++stat.condition;
            return true;
        }
        
        @Override
        public String toString() {
            return JSON.toJSONString(this.stat, SerializerFeature.PrettyFormat, SerializerFeature.NotWriteDefaultValue);
        }
        
        public Map toMap() {
            return (Map)JSON.toJSON(this.stat);
        }
    }
}
