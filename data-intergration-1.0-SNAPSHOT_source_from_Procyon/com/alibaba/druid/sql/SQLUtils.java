// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql;

import java.text.SimpleDateFormat;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import java.util.TimeZone;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.ast.statement.SQLDumpStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.util.PGUtils;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.util.OracleUtils;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleToMySqlOutputVisitor;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.util.StringUtils;
import java.util.ArrayList;
import com.alibaba.druid.sql.dialect.clickhouse.visitor.ClickSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.antspark.visitor.AntsparkSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.h2.visitor.H2SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.clickhouse.visitor.ClickhouseOutputVisitor;
import com.alibaba.druid.sql.dialect.antspark.visitor.AntsparkOutputVisitor;
import com.alibaba.druid.sql.dialect.blink.vsitor.BlinkOutputVisitor;
import com.alibaba.druid.sql.dialect.ads.visitor.AdsOutputVisitor;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveOutputVisitor;
import com.alibaba.druid.sql.dialect.h2.visitor.H2OutputVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsOutputVisitor;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2OutputVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import java.util.Map;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import java.nio.charset.Charset;

public class SQLUtils
{
    public static final Charset UTF8;
    private static final SQLParserFeature[] FORMAT_DEFAULT_FEATURES;
    public static FormatOption DEFAULT_FORMAT_OPTION;
    public static FormatOption DEFAULT_LCASE_FORMAT_OPTION;
    private static final Log LOG;
    
    public static String toSQLString(final SQLObject sqlObject, final String dbType) {
        return toSQLString(sqlObject, DbType.valueOf(dbType));
    }
    
    public static String toSQLString(final SQLObject sqlObject, final DbType dbType) {
        return toSQLString(sqlObject, dbType, null, (VisitorFeature[])null);
    }
    
    public static String toSQLString(final SQLObject sqlObject, final DbType dbType, final FormatOption option) {
        return toSQLString(sqlObject, dbType, option, (VisitorFeature[])null);
    }
    
    public static String toSQLString(final SQLObject sqlObject, final DbType dbType, FormatOption option, final VisitorFeature... features) {
        final StringBuilder out = new StringBuilder();
        final SQLASTOutputVisitor visitor = createOutputVisitor(out, dbType);
        if (option == null) {
            option = SQLUtils.DEFAULT_FORMAT_OPTION;
        }
        visitor.setUppCase(option.isUppCase());
        visitor.setPrettyFormat(option.isPrettyFormat());
        visitor.setParameterized(option.isParameterized());
        int featuresValue = option.features;
        if (features != null) {
            for (final VisitorFeature feature : features) {
                visitor.config(feature, true);
                featuresValue |= feature.mask;
            }
        }
        visitor.setFeatures(featuresValue);
        sqlObject.accept(visitor);
        final String sql = out.toString();
        return sql;
    }
    
    public static String toSQLString(final SQLObject obj) {
        if (obj instanceof SQLStatement) {
            final SQLStatement stmt = (SQLStatement)obj;
            return toSQLString(stmt, stmt.getDbType());
        }
        if (obj instanceof MySqlObject) {
            return toMySqlString(obj);
        }
        final StringBuilder out = new StringBuilder();
        obj.accept(new SQLASTOutputVisitor(out));
        final String sql = out.toString();
        return sql;
    }
    
    public static String toOdpsString(final SQLObject sqlObject) {
        return toOdpsString(sqlObject, null);
    }
    
    public static String toHiveString(final SQLObject sqlObject) {
        return toSQLString(sqlObject, DbType.odps);
    }
    
    public static String toOdpsString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.odps, option);
    }
    
    public static String toAntsparkString(final SQLObject sqlObject) {
        return toAntsparkString(sqlObject, null);
    }
    
    public static String toAntsparkString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.antspark, option);
    }
    
    public static String toMySqlString(final SQLObject sqlObject) {
        return toMySqlString(sqlObject, (FormatOption)null);
    }
    
    public static String toMySqlStringIfNotNull(final SQLObject sqlObject, final String defaultStr) {
        if (sqlObject == null) {
            return defaultStr;
        }
        return toMySqlString(sqlObject, (FormatOption)null);
    }
    
    public static String toMySqlString(final SQLObject sqlObject, final VisitorFeature... features) {
        return toMySqlString(sqlObject, new FormatOption(features));
    }
    
    public static String toNormalizeMysqlString(final SQLObject sqlObject) {
        if (sqlObject != null) {
            return normalize(toSQLString(sqlObject, DbType.mysql));
        }
        return null;
    }
    
    public static String toMySqlString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.mysql, option);
    }
    
    public static SQLExpr toMySqlExpr(final String sql) {
        return toSQLExpr(sql, DbType.mysql);
    }
    
    public static String formatMySql(final String sql) {
        return format(sql, DbType.mysql);
    }
    
    public static String formatMySql(final String sql, final FormatOption option) {
        return format(sql, DbType.mysql, option);
    }
    
    public static String formatOracle(final String sql) {
        return format(sql, DbType.oracle);
    }
    
    public static String formatOracle(final String sql, final FormatOption option) {
        return format(sql, DbType.oracle, option);
    }
    
    public static String formatOdps(final String sql) {
        return format(sql, DbType.odps);
    }
    
    public static String formatPresto(final String sql) {
        return formatPresto(sql, null);
    }
    
    public static String formatPresto(final String sql, final FormatOption option) {
        final SQLParserFeature[] features = { SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.KeepNameQuotes };
        return format(sql, DbType.mysql, null, option, features);
    }
    
    public static String formatHive(final String sql) {
        return format(sql, DbType.hive);
    }
    
    public static String formatOdps(final String sql, final FormatOption option) {
        return format(sql, DbType.odps, option);
    }
    
    public static String formatHive(final String sql, final FormatOption option) {
        return format(sql, DbType.hive, option);
    }
    
    public static String formatSQLServer(final String sql) {
        return format(sql, DbType.sqlserver);
    }
    
    public static String toOracleString(final SQLObject sqlObject) {
        return toOracleString(sqlObject, null);
    }
    
    public static String toOracleString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.oracle, option);
    }
    
    public static String toPGString(final SQLObject sqlObject) {
        return toPGString(sqlObject, null);
    }
    
    public static String toPGString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.postgresql, option);
    }
    
    public static String toDB2String(final SQLObject sqlObject) {
        return toDB2String(sqlObject, null);
    }
    
    public static String toDB2String(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.db2, option);
    }
    
    public static String toSQLServerString(final SQLObject sqlObject) {
        return toSQLServerString(sqlObject, null);
    }
    
    public static String toSQLServerString(final SQLObject sqlObject, final FormatOption option) {
        return toSQLString(sqlObject, DbType.sqlserver, option);
    }
    
    public static String formatPGSql(final String sql, final FormatOption option) {
        return format(sql, DbType.postgresql, option);
    }
    
    public static SQLExpr toSQLExpr(final String sql, final DbType dbType) {
        final SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType, new SQLParserFeature[0]);
        final SQLExpr expr = parser.expr();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return expr;
    }
    
    public static SQLSelectOrderByItem toOrderByItem(final String sql, final DbType dbType) {
        final SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType, new SQLParserFeature[0]);
        final SQLSelectOrderByItem orderByItem = parser.parseSelectOrderByItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return orderByItem;
    }
    
    public static SQLUpdateSetItem toUpdateSetItem(final String sql, final DbType dbType) {
        final SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType, new SQLParserFeature[0]);
        final SQLUpdateSetItem updateSetItem = parser.parseUpdateSetItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return updateSetItem;
    }
    
    public static SQLSelectItem toSelectItem(final String sql, final DbType dbType) {
        final SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType, new SQLParserFeature[0]);
        final SQLSelectItem selectItem = parser.parseSelectItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return selectItem;
    }
    
    public static List<SQLStatement> toStatementList(final String sql, final DbType dbType) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        return parser.parseStatementList();
    }
    
    public static SQLExpr toSQLExpr(final String sql) {
        return toSQLExpr(sql, null);
    }
    
    public static String format(final String sql, final String dbType) {
        return format(sql, DbType.of(dbType));
    }
    
    public static String format(final String sql, final DbType dbType) {
        return format(sql, dbType, null, null);
    }
    
    public static String format(final String sql, final DbType dbType, final FormatOption option) {
        return format(sql, dbType, null, option);
    }
    
    public static String format(final String sql, final DbType dbType, final List<Object> parameters) {
        return format(sql, dbType, parameters, null);
    }
    
    public static String format(final String sql, final DbType dbType, final List<Object> parameters, final FormatOption option) {
        return format(sql, dbType, parameters, option, SQLUtils.FORMAT_DEFAULT_FEATURES);
    }
    
    public static String format(final String sql, final DbType dbType, final List<Object> parameters, final FormatOption option, final SQLParserFeature[] features) {
        try {
            final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);
            final List<SQLStatement> statementList = parser.parseStatementList();
            return toSQLString(statementList, dbType, parameters, option);
        }
        catch (ParserException ex) {
            SQLUtils.LOG.warn("rowFormat error", ex);
            return sql;
        }
    }
    
    public static String toSQLString(final List<SQLStatement> statementList, final DbType dbType) {
        return toSQLString(statementList, dbType, (List<Object>)null);
    }
    
    public static String toSQLString(final List<SQLStatement> statementList, final DbType dbType, final FormatOption option) {
        return toSQLString(statementList, dbType, null, option);
    }
    
    public static String toSQLString(final List<SQLStatement> statementList, final DbType dbType, final List<Object> parameters) {
        return toSQLString(statementList, dbType, parameters, null, null);
    }
    
    public static String toSQLString(final List<SQLStatement> statementList, final DbType dbType, final List<Object> parameters, final FormatOption option) {
        return toSQLString(statementList, dbType, parameters, option, null);
    }
    
    public static String toSQLString(final List<SQLStatement> statementList, final DbType dbType, final List<Object> parameters, FormatOption option, final Map<String, String> tableMapping) {
        final StringBuilder out = new StringBuilder();
        final SQLASTOutputVisitor visitor = createFormatOutputVisitor(out, statementList, dbType);
        if (parameters != null) {
            visitor.setInputParameters(parameters);
        }
        if (option == null) {
            option = SQLUtils.DEFAULT_FORMAT_OPTION;
        }
        visitor.setFeatures(option.features);
        if (tableMapping != null) {
            visitor.setTableMapping(tableMapping);
        }
        final boolean printStmtSeperator = DbType.sqlserver != dbType && DbType.oracle != dbType;
        for (int i = 0, size = statementList.size(); i < size; ++i) {
            final SQLStatement stmt = statementList.get(i);
            if (i > 0) {
                final SQLStatement preStmt = statementList.get(i - 1);
                if (printStmtSeperator && !preStmt.isAfterSemi()) {
                    visitor.print(";");
                }
                final List<String> comments = preStmt.getAfterCommentsDirect();
                if (comments != null) {
                    for (int j = 0; j < comments.size(); ++j) {
                        final String comment = comments.get(j);
                        if (j != 0) {
                            visitor.println();
                        }
                        visitor.printComment(comment);
                    }
                }
                if (printStmtSeperator) {
                    visitor.println();
                }
                if (!(stmt instanceof SQLSetStatement)) {
                    visitor.println();
                }
            }
            stmt.accept(visitor);
            if (i == size - 1) {
                final List<String> comments2 = stmt.getAfterCommentsDirect();
                if (comments2 != null) {
                    for (int k = 0; k < comments2.size(); ++k) {
                        final String comment2 = comments2.get(k);
                        if (k != 0) {
                            visitor.println();
                        }
                        visitor.printComment(comment2);
                    }
                }
            }
        }
        return out.toString();
    }
    
    public static SQLASTOutputVisitor createOutputVisitor(final Appendable out, final DbType dbType) {
        return createFormatOutputVisitor(out, null, dbType);
    }
    
    public static SQLASTOutputVisitor createFormatOutputVisitor(final Appendable out, final List<SQLStatement> statementList, DbType dbType) {
        if (dbType == null) {
            if (statementList != null && statementList.size() > 0) {
                dbType = statementList.get(0).getDbType();
            }
            if (dbType == null) {
                dbType = DbType.other;
            }
        }
        switch (dbType) {
            case oracle:
            case oceanbase_oracle: {
                if (statementList == null || statementList.size() == 1) {
                    return new OracleOutputVisitor(out, false);
                }
                return new OracleOutputVisitor(out, true);
            }
            case mysql:
            case mariadb: {
                return new MySqlOutputVisitor(out);
            }
            case postgresql: {
                return new PGOutputVisitor(out);
            }
            case sqlserver:
            case jtds: {
                return new SQLServerOutputVisitor(out);
            }
            case db2: {
                return new DB2OutputVisitor(out);
            }
            case odps: {
                return new OdpsOutputVisitor(out);
            }
            case h2: {
                return new H2OutputVisitor(out);
            }
            case hive: {
                return new HiveOutputVisitor(out);
            }
            case ads: {
                return new AdsOutputVisitor(out);
            }
            case blink: {
                return new BlinkOutputVisitor(out);
            }
            case antspark: {
                return new AntsparkOutputVisitor(out);
            }
            case clickhouse: {
                return new ClickhouseOutputVisitor(out);
            }
            default: {
                return new SQLASTOutputVisitor(out, dbType);
            }
        }
    }
    
    @Deprecated
    public static SchemaStatVisitor createSchemaStatVisitor(final List<SQLStatement> statementList, final DbType dbType) {
        return createSchemaStatVisitor(dbType);
    }
    
    public static SchemaStatVisitor createSchemaStatVisitor(final DbType dbType) {
        return createSchemaStatVisitor((SchemaRepository)null, dbType);
    }
    
    public static SchemaStatVisitor createSchemaStatVisitor(final SchemaRepository repository) {
        return createSchemaStatVisitor(repository, repository.getDbType());
    }
    
    public static SchemaStatVisitor createSchemaStatVisitor(SchemaRepository repository, final DbType dbType) {
        if (repository == null) {
            repository = new SchemaRepository(dbType);
        }
        if (dbType == null) {
            return new SchemaStatVisitor(repository);
        }
        switch (dbType) {
            case oracle: {
                return new OracleSchemaStatVisitor(repository);
            }
            case mysql:
            case mariadb:
            case elastic_search: {
                return new MySqlSchemaStatVisitor(repository);
            }
            case postgresql: {
                return new PGSchemaStatVisitor(repository);
            }
            case sqlserver:
            case jtds: {
                return new SQLServerSchemaStatVisitor(repository);
            }
            case db2: {
                return new DB2SchemaStatVisitor(repository);
            }
            case odps: {
                return new OdpsSchemaStatVisitor(repository);
            }
            case h2: {
                return new H2SchemaStatVisitor(repository);
            }
            case hive: {
                return new HiveSchemaStatVisitor(repository);
            }
            case antspark: {
                return new AntsparkSchemaStatVisitor(repository);
            }
            case clickhouse: {
                return new ClickSchemaStatVisitor(repository);
            }
            default: {
                return new SchemaStatVisitor(repository);
            }
        }
    }
    
    public static List<SQLStatement> parseStatements(final String sql, final String dbType, final SQLParserFeature... features) {
        return parseStatements(sql, DbType.of(dbType), features);
    }
    
    public static List<SQLStatement> parseStatements(final String sql, final DbType dbType, final SQLParserFeature... features) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);
        final List<SQLStatement> stmtList = new ArrayList<SQLStatement>();
        parser.parseStatementList(stmtList, -1, null);
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("syntax error : " + sql);
        }
        return stmtList;
    }
    
    public static List<SQLStatement> parseStatements(final String sql, final DbType dbType, final boolean keepComments) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, keepComments);
        final List<SQLStatement> stmtList = parser.parseStatementList();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("syntax error. " + sql);
        }
        return stmtList;
    }
    
    public static List<SQLStatement> parseStatements(final String sql, final String dbType) {
        return parseStatements(sql, dbType, new SQLParserFeature[0]);
    }
    
    public static List<SQLStatement> parseStatements(final String sql, final DbType dbType) {
        return parseStatements(sql, dbType, new SQLParserFeature[0]);
    }
    
    public static SQLStatement parseSingleStatement(final String sql, final DbType dbType, final boolean keepComments) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, keepComments);
        final List<SQLStatement> stmtList = parser.parseStatementList();
        if (stmtList.size() > 1) {
            throw new ParserException("multi-statement be found.");
        }
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("syntax error. " + sql);
        }
        return stmtList.get(0);
    }
    
    public static SQLStatement parseSingleStatement(final String sql, final String dbType, final SQLParserFeature... features) {
        return parseSingleStatement(sql, DbType.of(dbType), features);
    }
    
    public static SQLStatement parseSingleStatement(final String sql, final DbType dbType, final SQLParserFeature... features) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);
        final List<SQLStatement> stmtList = parser.parseStatementList();
        if (stmtList.size() > 1) {
            throw new ParserException("multi-statement be found.");
        }
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("syntax error. " + sql);
        }
        return stmtList.get(0);
    }
    
    public static SQLStatement parseSingleMysqlStatement(final String sql) {
        return parseSingleStatement(sql, DbType.mysql, false);
    }
    
    public static String buildToDate(final String columnName, final String tableAlias, String pattern, DbType dbType) {
        final StringBuilder sql = new StringBuilder();
        if (StringUtils.isEmpty(columnName)) {
            return "";
        }
        if (dbType == null) {
            dbType = DbType.mysql;
        }
        String formatMethod = "";
        if (DbType.mysql == dbType) {
            formatMethod = "STR_TO_DATE";
            if (StringUtils.isEmpty(pattern)) {
                pattern = "%Y-%m-%d %H:%i:%s";
            }
        }
        else {
            if (DbType.oracle != dbType) {
                return "";
            }
            formatMethod = "TO_DATE";
            if (StringUtils.isEmpty(pattern)) {
                pattern = "yyyy-mm-dd hh24:mi:ss";
            }
        }
        sql.append(formatMethod).append("(");
        if (!StringUtils.isEmpty(tableAlias)) {
            sql.append(tableAlias).append(".");
        }
        sql.append(columnName).append(",");
        sql.append("'");
        sql.append(pattern);
        sql.append("')");
        return sql.toString();
    }
    
    public static List<SQLExpr> split(final SQLBinaryOpExpr x) {
        return SQLBinaryOpExpr.split(x);
    }
    
    public static String translateOracleToMySql(final String sql) {
        final List<SQLStatement> stmtList = toStatementList(sql, DbType.oracle);
        final StringBuilder out = new StringBuilder();
        final OracleToMySqlOutputVisitor visitor = new OracleToMySqlOutputVisitor(out, false);
        for (int i = 0; i < stmtList.size(); ++i) {
            stmtList.get(i).accept(visitor);
        }
        final String mysqlSql = out.toString();
        return mysqlSql;
    }
    
    public static String addCondition(final String sql, final String condition, final DbType dbType) {
        final String result = addCondition(sql, condition, SQLBinaryOperator.BooleanAnd, false, dbType);
        return result;
    }
    
    public static String addCondition(final String sql, final String condition, SQLBinaryOperator op, final boolean left, final DbType dbType) {
        if (sql == null) {
            throw new IllegalArgumentException("sql is null");
        }
        if (condition == null) {
            return sql;
        }
        if (op == null) {
            op = SQLBinaryOperator.BooleanAnd;
        }
        if (op != SQLBinaryOperator.BooleanAnd && op != SQLBinaryOperator.BooleanOr) {
            throw new IllegalArgumentException("add condition not support : " + op);
        }
        final List<SQLStatement> stmtList = parseStatements(sql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }
        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }
        final SQLStatement stmt = stmtList.get(0);
        final SQLExpr conditionExpr = toSQLExpr(condition, dbType);
        addCondition(stmt, op, conditionExpr, left);
        return toSQLString(stmt, dbType);
    }
    
    public static void addCondition(final SQLStatement stmt, final SQLBinaryOperator op, final SQLExpr condition, final boolean left) {
        if (stmt instanceof SQLSelectStatement) {
            final SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
            if (query instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                final SQLExpr newCondition = buildCondition(op, condition, left, queryBlock.getWhere());
                queryBlock.setWhere(newCondition);
                return;
            }
            throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
        }
        else {
            if (stmt instanceof SQLDeleteStatement) {
                final SQLDeleteStatement delete = (SQLDeleteStatement)stmt;
                final SQLExpr newCondition2 = buildCondition(op, condition, left, delete.getWhere());
                delete.setWhere(newCondition2);
                return;
            }
            if (stmt instanceof SQLUpdateStatement) {
                final SQLUpdateStatement update = (SQLUpdateStatement)stmt;
                final SQLExpr newCondition2 = buildCondition(op, condition, left, update.getWhere());
                update.setWhere(newCondition2);
                return;
            }
            throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
        }
    }
    
    public static SQLExpr buildCondition(final SQLBinaryOperator op, final SQLExpr condition, final boolean left, final SQLExpr where) {
        if (where == null) {
            return condition;
        }
        SQLBinaryOpExpr newCondition;
        if (left) {
            newCondition = new SQLBinaryOpExpr(condition, op, where);
        }
        else {
            newCondition = new SQLBinaryOpExpr(where, op, condition);
        }
        return newCondition;
    }
    
    public static String addSelectItem(final String selectSql, final String expr, final String alias, final DbType dbType) {
        return addSelectItem(selectSql, expr, alias, false, dbType);
    }
    
    public static String addSelectItem(final String selectSql, final String expr, final String alias, final boolean first, final DbType dbType) {
        final List<SQLStatement> stmtList = parseStatements(selectSql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + selectSql);
        }
        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + selectSql);
        }
        final SQLStatement stmt = stmtList.get(0);
        final SQLExpr columnExpr = toSQLExpr(expr, dbType);
        addSelectItem(stmt, columnExpr, alias, first);
        return toSQLString(stmt, dbType);
    }
    
    public static void addSelectItem(final SQLStatement stmt, final SQLExpr expr, final String alias, final boolean first) {
        if (expr == null) {
            return;
        }
        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("add selectItem not support " + stmt.getClass().getName());
        }
        final SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            addSelectItem(queryBlock, expr, alias, first);
            return;
        }
        throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
    }
    
    public static void addSelectItem(final SQLSelectQueryBlock queryBlock, final SQLExpr expr, final String alias, final boolean first) {
        final SQLSelectItem selectItem = new SQLSelectItem(expr, alias);
        queryBlock.getSelectList().add(selectItem);
        selectItem.setParent(selectItem);
    }
    
    public static String refactor(final String sql, final DbType dbType, final Map<String, String> tableMapping) {
        final List<SQLStatement> stmtList = parseStatements(sql, dbType);
        return toSQLString(stmtList, dbType, null, null, tableMapping);
    }
    
    public static long hash(final String sql, final DbType dbType) {
        final Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
        final StringBuilder buf = new StringBuilder(sql.length());
        while (true) {
            lexer.nextToken();
            final Token token = lexer.token();
            if (token == Token.EOF) {
                return buf.hashCode();
            }
            if (token == Token.ERROR) {
                return Utils.fnv_64(sql);
            }
            if (buf.length() != 0) {}
        }
    }
    
    public static SQLExpr not(final SQLExpr expr) {
        if (expr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)expr;
            final SQLBinaryOperator op = binaryOpExpr.getOperator();
            SQLBinaryOperator notOp = null;
            switch (op) {
                case Equality: {
                    notOp = SQLBinaryOperator.LessThanOrGreater;
                    break;
                }
                case LessThanOrEqualOrGreaterThan: {
                    notOp = SQLBinaryOperator.Equality;
                    break;
                }
                case LessThan: {
                    notOp = SQLBinaryOperator.GreaterThanOrEqual;
                    break;
                }
                case LessThanOrEqual: {
                    notOp = SQLBinaryOperator.GreaterThan;
                    break;
                }
                case GreaterThan: {
                    notOp = SQLBinaryOperator.LessThanOrEqual;
                    break;
                }
                case GreaterThanOrEqual: {
                    notOp = SQLBinaryOperator.LessThan;
                    break;
                }
                case Is: {
                    notOp = SQLBinaryOperator.IsNot;
                    break;
                }
                case IsNot: {
                    notOp = SQLBinaryOperator.Is;
                    break;
                }
            }
            if (notOp != null) {
                return new SQLBinaryOpExpr(binaryOpExpr.getLeft(), notOp, binaryOpExpr.getRight());
            }
        }
        if (expr instanceof SQLInListExpr) {
            final SQLInListExpr inListExpr = (SQLInListExpr)expr;
            final SQLInListExpr newInListExpr = new SQLInListExpr(inListExpr);
            newInListExpr.getTargetList().addAll(inListExpr.getTargetList());
            newInListExpr.setNot(!inListExpr.isNot());
            return newInListExpr;
        }
        return new SQLUnaryExpr(SQLUnaryOperator.Not, expr);
    }
    
    public static String normalize(final String name) {
        return normalize(name, null);
    }
    
    public static String normalize(final String name, final boolean isTrimmed) {
        return _normalize(name, null, false, isTrimmed);
    }
    
    public static String normalize(final String name, final DbType dbType) {
        return _normalize(name, dbType, false);
    }
    
    private static String _normalize(final String name, final DbType dbType, final boolean isForced) {
        return _normalize(name, dbType, isForced, true);
    }
    
    private static String _normalize(final String name, final DbType dbType, final boolean isForced, final boolean isTrimmed) {
        if (name == null) {
            return null;
        }
        if (name.length() > 2) {
            final char c0 = name.charAt(0);
            final char x0 = name.charAt(name.length() - 1);
            if ((c0 == '\"' && x0 == '\"') || (c0 == '`' && x0 == '`') || (c0 == '\'' && x0 == '\'')) {
                String normalizeName = name.substring(1, name.length() - 1);
                if (isTrimmed) {
                    normalizeName = normalizeName.trim();
                }
                final int dotIndex = normalizeName.indexOf(46);
                if (dotIndex > 0 && c0 == '`') {
                    normalizeName = normalizeName.replaceAll("`\\.`", ".");
                }
                if (!isForced) {
                    if (DbType.oracle == dbType) {
                        if (OracleUtils.isKeyword(normalizeName)) {
                            return name;
                        }
                    }
                    else if (DbType.mysql == dbType) {
                        if (MySqlUtils.isKeyword(normalizeName)) {
                            return name;
                        }
                    }
                    else if ((DbType.postgresql == dbType || DbType.db2 == dbType) && PGUtils.isKeyword(normalizeName)) {
                        return name;
                    }
                }
                return normalizeName;
            }
        }
        return name;
    }
    
    public static String forcedNormalize(final String name, final DbType dbType) {
        return _normalize(name, dbType, true);
    }
    
    public static boolean nameEquals(final SQLName a, final SQLName b) {
        return a == b || (a != null && b != null && a.nameHashCode64() == b.nameHashCode64());
    }
    
    public static boolean nameEquals(final String a, final String b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.equalsIgnoreCase(b)) {
            return true;
        }
        final String normalize_a = normalize(a);
        final String normalize_b = normalize(b);
        return normalize_a.equalsIgnoreCase(normalize_b);
    }
    
    public static boolean isValue(final SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        }
        if (expr instanceof SQLVariantRefExpr) {
            return true;
        }
        if (expr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)expr;
            final SQLBinaryOperator op = binaryOpExpr.getOperator();
            if (op == SQLBinaryOperator.Add || op == SQLBinaryOperator.Subtract || op == SQLBinaryOperator.Multiply) {
                return isValue(binaryOpExpr.getLeft()) && isValue(binaryOpExpr.getRight());
            }
        }
        return false;
    }
    
    public static boolean replaceInParent(final SQLExpr expr, final SQLExpr target) {
        if (expr == null) {
            return false;
        }
        final SQLObject parent = expr.getParent();
        return parent instanceof SQLReplaceable && ((SQLReplaceable)parent).replace(expr, target);
    }
    
    public static boolean replaceInParent(final SQLTableSource cmp, final SQLTableSource dest) {
        if (cmp == null) {
            return false;
        }
        final SQLObject parent = cmp.getParent();
        if (parent instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
            if (queryBlock.getFrom() == cmp) {
                queryBlock.setFrom(dest);
                return true;
            }
        }
        if (parent instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)parent;
            return join.replace(cmp, dest);
        }
        return false;
    }
    
    public static boolean replaceInParent(final SQLSelectQuery cmp, final SQLSelectQuery dest) {
        if (cmp == null) {
            return false;
        }
        final SQLObject parent = cmp.getParent();
        if (parent == null) {
            return false;
        }
        if (parent instanceof SQLUnionQuery) {
            return ((SQLUnionQuery)parent).replace(cmp, dest);
        }
        return parent instanceof SQLSelect && ((SQLSelect)parent).replace(cmp, dest);
    }
    
    public static String desensitizeTable(String tableName) {
        if (tableName == null) {
            return null;
        }
        tableName = normalize(tableName);
        final long hash = FnvHash.hashCode64(tableName);
        return Utils.hex_t(hash);
    }
    
    public static String sort(final String sql, final DbType dbType) {
        final List stmtList = parseStatements(sql, DbType.oracle);
        SQLCreateTableStatement.sort(stmtList);
        return toSQLString(stmtList, dbType);
    }
    
    public static Object[] clearLimit(final String query, final DbType dbType) {
        final List stmtList = parseStatements(query, dbType);
        SQLLimit limit = null;
        final SQLStatement statement = stmtList.get(0);
        if (statement instanceof SQLSelectStatement) {
            final SQLSelectStatement selectStatement = (SQLSelectStatement)statement;
            if (selectStatement.getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                limit = clearLimit(selectStatement.getSelect().getQueryBlock());
            }
        }
        if (statement instanceof SQLDumpStatement) {
            final SQLDumpStatement dumpStatement = (SQLDumpStatement)statement;
            if (dumpStatement.getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                limit = clearLimit(dumpStatement.getSelect().getQueryBlock());
            }
        }
        if (statement instanceof MySqlSelectIntoStatement) {
            final MySqlSelectIntoStatement sqlSelectIntoStatement = (MySqlSelectIntoStatement)statement;
            limit = clearLimit(sqlSelectIntoStatement.getSelect().getQueryBlock());
        }
        if (statement instanceof MySqlInsertStatement) {
            final MySqlInsertStatement insertStatement = (MySqlInsertStatement)statement;
            limit = clearLimit(insertStatement.getQuery().getQueryBlock());
        }
        final String sql = toSQLString(stmtList, dbType);
        return new Object[] { sql, limit };
    }
    
    private static SQLLimit clearLimit(final SQLSelectQueryBlock queryBlock) {
        if (queryBlock == null) {
            return null;
        }
        final SQLLimit limit = queryBlock.getLimit();
        queryBlock.setLimit(null);
        return limit;
    }
    
    public static SQLLimit getLimit(final SQLStatement statement, final DbType dbType) {
        if (statement instanceof SQLSelectStatement) {
            final SQLSelectQueryBlock queryBlock = ((SQLSelectStatement)statement).getSelect().getQueryBlock();
            return (queryBlock == null) ? null : queryBlock.getLimit();
        }
        if (statement instanceof SQLDumpStatement) {
            final SQLSelectQueryBlock queryBlock = ((SQLDumpStatement)statement).getSelect().getQueryBlock();
            return (queryBlock == null) ? null : queryBlock.getLimit();
        }
        if (statement instanceof MySqlSelectIntoStatement) {
            final SQLSelectQueryBlock queryBlock = ((MySqlSelectIntoStatement)statement).getSelect().getQueryBlock();
            return (queryBlock == null) ? null : queryBlock.getLimit();
        }
        if (!(statement instanceof MySqlInsertStatement)) {
            return null;
        }
        final SQLSelect select = ((MySqlInsertStatement)statement).getQuery();
        if (select == null) {
            return null;
        }
        if (select.getQuery() instanceof SQLUnionQuery) {
            return ((SQLUnionQuery)select.getQuery()).getLimit();
        }
        return select.getQueryBlock().getLimit();
    }
    
    public static SQLLimit getLimit(final String query, final DbType dbType) {
        final List stmtList = parseStatements(query, dbType);
        final SQLStatement statement = stmtList.get(0);
        return getLimit(statement, dbType);
    }
    
    public static String convertTimeZone(final String sql, final TimeZone from, final TimeZone to) {
        final SQLStatement statement = parseSingleMysqlStatement(sql);
        statement.accept(new TimeZoneVisitor(from, to));
        return statement.toString();
    }
    
    public static SQLStatement convertTimeZone(final SQLStatement stmt, final TimeZone from, final TimeZone to) {
        stmt.accept(new TimeZoneVisitor(from, to));
        return stmt;
    }
    
    public static List<SQLInsertStatement> splitInsertValues(final DbType dbType, final String insertSql, final int size) {
        final SQLStatement statement = parseStatements(insertSql, dbType, false).get(0);
        if (!(statement instanceof SQLInsertStatement)) {
            throw new IllegalArgumentException("The SQL must be insert statement.");
        }
        final List<SQLInsertStatement> insertLists = new ArrayList<SQLInsertStatement>();
        final SQLInsertStatement insertStatement = (SQLInsertStatement)statement;
        final List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        final int totalSize = valuesList.size();
        if (totalSize <= size) {
            insertLists.add(insertStatement);
        }
        else {
            final SQLInsertStatement insertTemplate = new SQLInsertStatement();
            insertStatement.cloneTo(insertTemplate);
            insertTemplate.getValuesList().clear();
            int batchCount = 0;
            if (totalSize % size == 0) {
                batchCount = totalSize / size;
            }
            else {
                batchCount = totalSize / size + 1;
            }
            for (int i = 0; i < batchCount; ++i) {
                final SQLInsertStatement subInsertStatement = new SQLInsertStatement();
                insertTemplate.cloneTo(subInsertStatement);
                final int fromIndex = i * size;
                final int toIndex = (fromIndex + size > totalSize) ? totalSize : (fromIndex + size);
                final List<SQLInsertStatement.ValuesClause> subValuesList = valuesList.subList(fromIndex, toIndex);
                subInsertStatement.getValuesList().addAll(subValuesList);
                insertLists.add(subInsertStatement);
            }
        }
        return insertLists;
    }
    
    static {
        UTF8 = Charset.forName("UTF-8");
        FORMAT_DEFAULT_FEATURES = new SQLParserFeature[] { SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup };
        SQLUtils.DEFAULT_FORMAT_OPTION = new FormatOption(true, true);
        SQLUtils.DEFAULT_LCASE_FORMAT_OPTION = new FormatOption(false, true);
        LOG = LogFactory.getLog(SQLUtils.class);
    }
    
    public static class FormatOption
    {
        private int features;
        
        public FormatOption() {
            this.features = VisitorFeature.of(VisitorFeature.OutputUCase, VisitorFeature.OutputPrettyFormat);
        }
        
        public FormatOption(final VisitorFeature... features) {
            this.features = VisitorFeature.of(VisitorFeature.OutputUCase, VisitorFeature.OutputPrettyFormat);
            this.features = VisitorFeature.of(features);
        }
        
        public FormatOption(final boolean ucase) {
            this(ucase, true);
        }
        
        public FormatOption(final boolean ucase, final boolean prettyFormat) {
            this(ucase, prettyFormat, false);
        }
        
        public FormatOption(final boolean ucase, final boolean prettyFormat, final boolean parameterized) {
            this.features = VisitorFeature.of(VisitorFeature.OutputUCase, VisitorFeature.OutputPrettyFormat);
            this.features = VisitorFeature.config(this.features, VisitorFeature.OutputUCase, ucase);
            this.features = VisitorFeature.config(this.features, VisitorFeature.OutputPrettyFormat, prettyFormat);
            this.features = VisitorFeature.config(this.features, VisitorFeature.OutputParameterized, parameterized);
        }
        
        public boolean isDesensitize() {
            return this.isEnabled(VisitorFeature.OutputDesensitize);
        }
        
        public void setDesensitize(final boolean val) {
            this.config(VisitorFeature.OutputDesensitize, val);
        }
        
        public boolean isUppCase() {
            return this.isEnabled(VisitorFeature.OutputUCase);
        }
        
        public void setUppCase(final boolean val) {
            this.config(VisitorFeature.OutputUCase, val);
        }
        
        public boolean isPrettyFormat() {
            return this.isEnabled(VisitorFeature.OutputPrettyFormat);
        }
        
        public void setPrettyFormat(final boolean prettyFormat) {
            this.config(VisitorFeature.OutputPrettyFormat, prettyFormat);
        }
        
        public boolean isParameterized() {
            return this.isEnabled(VisitorFeature.OutputParameterized);
        }
        
        public void setParameterized(final boolean parameterized) {
            this.config(VisitorFeature.OutputParameterized, parameterized);
        }
        
        public void config(final VisitorFeature feature, final boolean state) {
            this.features = VisitorFeature.config(this.features, feature, state);
        }
        
        public final boolean isEnabled(final VisitorFeature feature) {
            return VisitorFeature.isEnabled(this.features, feature);
        }
    }
    
    static class TimeZoneVisitor extends SQLASTVisitorAdapter
    {
        private TimeZone from;
        private TimeZone to;
        
        public TimeZoneVisitor(final TimeZone from, final TimeZone to) {
            this.from = from;
            this.to = to;
        }
        
        @Override
        public boolean visit(final SQLTimestampExpr x) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String newTime = format.format(x.getDate(this.from));
            x.setLiteral(newTime);
            return true;
        }
    }
}
