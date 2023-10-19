// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLExprUtils;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import java.util.TimeZone;
import java.util.Map;
import com.alibaba.druid.sql.dialect.phoenix.visitor.PhoenixOutputVisitor;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2OutputVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.alibaba.druid.sql.dialect.h2.visitor.H2OutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTParameterizedVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlParameterizedVisitor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.util.FnvHash;
import java.util.Iterator;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.SQLParserFeature;

public class ParameterizedOutputVisitorUtils
{
    private static final SQLParserFeature[] defaultFeatures;
    private static final SQLParserFeature[] defaultFeatures2;
    private static final SQLParserFeature[] defaultFeatures_tddl;
    private static final SQLParserFeature[] defaultFeatures2_tddl;
    
    public static String parameterize(final String sql, final DbType dbType) {
        return parameterize(sql, dbType, (List<Object>)null, (VisitorFeature[])null);
    }
    
    public static String parameterize(final String sql, final DbType dbType, final VisitorFeature... features) {
        return parameterize(sql, dbType, (List<Object>)null, features);
    }
    
    public static String parameterize(final String sql, final DbType dbType, final SQLSelectListCache selectListCache) {
        return parameterize(sql, dbType, selectListCache, null, new VisitorFeature[0]);
    }
    
    public static String parameterize(final String sql, final DbType dbType, final List<Object> outParameters) {
        return parameterize(sql, dbType, null, outParameters, new VisitorFeature[0]);
    }
    
    public static String parameterize(final String sql, final DbType dbType, final List<Object> outParameters, final VisitorFeature... features) {
        return parameterize(sql, dbType, null, outParameters, features);
    }
    
    public static String parameterizeForTDDL(final String sql, final DbType dbType, final List<Object> outParameters, final VisitorFeature... features) {
        return parameterizeForTDDL(sql, dbType, null, outParameters, features);
    }
    
    private static void configVisitorFeatures(final ParameterizedVisitor visitor, final VisitorFeature... features) {
        if (features != null) {
            for (int i = 0; i < features.length; ++i) {
                visitor.config(features[i], true);
            }
        }
    }
    
    public static String parameterize(final String sql, final DbType dbType, final SQLSelectListCache selectListCache, final List<Object> outParameters, final VisitorFeature... visitorFeatures) {
        final SQLParserFeature[] features = (outParameters == null) ? ParameterizedOutputVisitorUtils.defaultFeatures2 : ParameterizedOutputVisitorUtils.defaultFeatures;
        return parameterize(sql, dbType, selectListCache, outParameters, features, visitorFeatures);
    }
    
    public static String parameterizeForTDDL(final String sql, final DbType dbType, final SQLSelectListCache selectListCache, final List<Object> outParameters, final VisitorFeature... visitorFeatures) {
        final SQLParserFeature[] features = (outParameters == null) ? ParameterizedOutputVisitorUtils.defaultFeatures2_tddl : ParameterizedOutputVisitorUtils.defaultFeatures_tddl;
        return parameterize(sql, dbType, selectListCache, outParameters, features, visitorFeatures);
    }
    
    public static String parameterize(final String sql, final DbType dbType, final SQLSelectListCache selectListCache, final List<Object> outParameters, final SQLParserFeature[] features, final VisitorFeature... visitorFeatures) {
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);
        if (selectListCache != null) {
            parser.setSelectListCache(selectListCache);
        }
        final List<SQLStatement> statementList = parser.parseStatementList();
        if (statementList.size() == 0) {
            return sql;
        }
        final StringBuilder out = new StringBuilder(sql.length());
        final ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        if (outParameters != null) {
            visitor.setOutputParameters(outParameters);
        }
        configVisitorFeatures(visitor, visitorFeatures);
        for (int i = 0; i < statementList.size(); ++i) {
            final SQLStatement stmt = statementList.get(i);
            if (i > 0) {
                final SQLStatement preStmt = statementList.get(i - 1);
                if (preStmt.getClass() == stmt.getClass()) {
                    final StringBuilder buf = new StringBuilder();
                    final ParameterizedVisitor v1 = createParameterizedOutputVisitor(buf, dbType);
                    preStmt.accept(v1);
                    if (out.toString().equals(buf.toString())) {
                        continue;
                    }
                }
                if (!preStmt.isAfterSemi()) {
                    out.append(";\n");
                }
                else {
                    out.append('\n');
                }
            }
            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }
            final Class<?> stmtClass = stmt.getClass();
            if (stmtClass == SQLSelectStatement.class) {
                final SQLSelectStatement selectStatement = (SQLSelectStatement)stmt;
                visitor.visit(selectStatement);
                visitor.postVisit(selectStatement);
            }
            else {
                stmt.accept(visitor);
            }
        }
        if (visitor.getReplaceCount() == 0 && parser.getLexer().getCommentCount() == 0 && sql.charAt(0) != '/') {
            boolean notUseOriginalSql = false;
            if (visitorFeatures != null) {
                for (final VisitorFeature visitorFeature : visitorFeatures) {
                    if (visitorFeature == VisitorFeature.OutputParameterizedZeroReplaceNotUseOriginalSql) {
                        notUseOriginalSql = true;
                    }
                }
            }
            if (!notUseOriginalSql) {
                int ddlStmtCount = 0;
                for (final SQLStatement stmt2 : statementList) {
                    if (stmt2 instanceof SQLDDLStatement) {
                        ++ddlStmtCount;
                    }
                }
                if (ddlStmtCount == statementList.size()) {
                    notUseOriginalSql = true;
                }
            }
            if (!notUseOriginalSql) {
                return sql;
            }
        }
        return out.toString();
    }
    
    public static long parameterizeHash(final String sql, final DbType dbType, final List<Object> outParameters) {
        return parameterizeHash(sql, dbType, null, outParameters, (VisitorFeature[])null);
    }
    
    public static long parameterizeHash(final String sql, final DbType dbType, final SQLSelectListCache selectListCache, final List<Object> outParameters) {
        return parameterizeHash(sql, dbType, selectListCache, outParameters, (VisitorFeature[])null);
    }
    
    public static long parameterizeHash(final String sql, final DbType dbType, final SQLSelectListCache selectListCache, final List<Object> outParameters, final VisitorFeature... visitorFeatures) {
        final SQLParserFeature[] features = (outParameters == null) ? ParameterizedOutputVisitorUtils.defaultFeatures2 : ParameterizedOutputVisitorUtils.defaultFeatures;
        final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);
        if (selectListCache != null) {
            parser.setSelectListCache(selectListCache);
        }
        final List<SQLStatement> statementList = parser.parseStatementList();
        final int stmtSize = statementList.size();
        if (stmtSize == 0) {
            return 0L;
        }
        final StringBuilder out = new StringBuilder(sql.length());
        final ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        if (outParameters != null) {
            visitor.setOutputParameters(outParameters);
        }
        configVisitorFeatures(visitor, visitorFeatures);
        if (stmtSize == 1) {
            final SQLStatement stmt = statementList.get(0);
            if (stmt.getClass() == SQLSelectStatement.class) {
                final SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
                if (selectListCache != null) {
                    final SQLSelectQueryBlock queryBlock = selectStmt.getSelect().getQueryBlock();
                    if (queryBlock != null) {
                        final String cachedSelectList = queryBlock.getCachedSelectList();
                        final long cachedSelectListHash = queryBlock.getCachedSelectListHash();
                        if (cachedSelectList != null) {
                            visitor.config(VisitorFeature.OutputSkipSelectListCacheString, true);
                        }
                        visitor.visit(selectStmt);
                        return FnvHash.fnv1a_64_lower(cachedSelectListHash, out);
                    }
                }
                visitor.visit(selectStmt);
            }
            else if (stmt.getClass() == MySqlInsertStatement.class) {
                final MySqlInsertStatement insertStmt = (MySqlInsertStatement)stmt;
                final String columnsString = insertStmt.getColumnsString();
                if (columnsString != null) {
                    final long columnsStringHash = insertStmt.getColumnsStringHash();
                    visitor.config(VisitorFeature.OutputSkipInsertColumnsString, true);
                    ((MySqlASTVisitor)visitor).visit(insertStmt);
                    return FnvHash.fnv1a_64_lower(columnsStringHash, out);
                }
            }
            else {
                stmt.accept(visitor);
            }
            return FnvHash.fnv1a_64_lower(out);
        }
        for (int i = 0; i < statementList.size(); ++i) {
            if (i > 0) {
                out.append(";\n");
            }
            final SQLStatement stmt2 = statementList.get(i);
            if (stmt2.hasBeforeComment()) {
                stmt2.getBeforeCommentsDirect().clear();
            }
            final Class<?> stmtClass = stmt2.getClass();
            if (stmtClass == SQLSelectStatement.class) {
                final SQLSelectStatement selectStatement = (SQLSelectStatement)stmt2;
                visitor.visit(selectStatement);
                visitor.postVisit(selectStatement);
            }
            else {
                stmt2.accept(visitor);
            }
        }
        return FnvHash.fnv1a_64_lower(out);
    }
    
    public static String parameterize(final List<SQLStatement> statementList, final DbType dbType) {
        final StringBuilder out = new StringBuilder();
        final ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        for (int i = 0; i < statementList.size(); ++i) {
            if (i > 0) {
                out.append(";\n");
            }
            final SQLStatement stmt = statementList.get(i);
            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }
            stmt.accept(visitor);
        }
        return out.toString();
    }
    
    public static String parameterize(final SQLStatement stmt, final DbType dbType) {
        final StringBuilder out = new StringBuilder();
        final ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        if (stmt.hasBeforeComment()) {
            stmt.getBeforeCommentsDirect().clear();
        }
        stmt.accept(visitor);
        return out.toString();
    }
    
    public static SQLStatement parameterizeOf(final String sql, final DbType dbType) {
        return parameterizeOf(sql, null, dbType);
    }
    
    public static SQLStatement parameterizeOf(final String sql, final List<Object> outParameters, final DbType dbType) {
        if (dbType == DbType.mysql) {
            final SQLStatement stmt = SQLUtils.parseSingleMysqlStatement(sql);
            final MySqlParameterizedVisitor visitor = new MySqlParameterizedVisitor(outParameters);
            stmt.accept(visitor);
            return stmt;
        }
        if (dbType == DbType.oracle) {
            final SQLStatement stmt = SQLUtils.parseSingleStatement(sql, DbType.oracle, new SQLParserFeature[0]);
            final OracleASTParameterizedVisitor visitor2 = new OracleASTParameterizedVisitor(outParameters);
            stmt.accept(visitor2);
            return stmt;
        }
        throw new UnsupportedOperationException();
    }
    
    public static ParameterizedVisitor createParameterizedOutputVisitor(final Appendable out, DbType dbType) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case oracle:
            case oceanbase_oracle: {
                return new OracleParameterizedOutputVisitor(out);
            }
            case mysql:
            case mariadb:
            case elastic_search: {
                return new MySqlOutputVisitor(out, true);
            }
            case h2: {
                return new H2OutputVisitor(out, true);
            }
            case postgresql:
            case edb: {
                return new PGOutputVisitor(out, true);
            }
            case sqlserver:
            case jtds: {
                return new SQLServerOutputVisitor(out, true);
            }
            case db2: {
                return new DB2OutputVisitor(out, true);
            }
            case phoenix: {
                return new PhoenixOutputVisitor(out, true);
            }
            default: {
                return new SQLASTOutputVisitor(out, true);
            }
        }
    }
    
    public static String restore(final String sql, final DbType dbType, final List<Object> parameters) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        final StringBuilder out = new StringBuilder();
        final SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(out, dbType);
        visitor.setInputParameters(parameters);
        for (final SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
        return out.toString();
    }
    
    public static String restore(final String sql, final DbType dbType, final Map<String, Object> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return sql;
        }
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        final RestoreVisitor v = new RestoreVisitor(parameters);
        for (final SQLStatement stmt : stmtList) {
            stmt.accept(v);
        }
        final StringBuilder out = new StringBuilder(sql.length());
        final SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(out, dbType);
        for (final SQLStatement stmt2 : stmtList) {
            stmt2.accept(visitor);
        }
        return out.toString();
    }
    
    static {
        defaultFeatures = new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized };
        defaultFeatures2 = new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.OptimizedForForParameterizedSkipValue };
        defaultFeatures_tddl = new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.TDDLHint };
        defaultFeatures2_tddl = new SQLParserFeature[] { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.OptimizedForForParameterizedSkipValue, SQLParserFeature.TDDLHint };
    }
    
    private static class RestoreVisitor extends SQLASTVisitorAdapter
    {
        Map<String, Object> parameters;
        TimeZone timeZone;
        
        public RestoreVisitor(final Map<String, Object> parameters) {
            this.parameters = parameters;
        }
        
        @Override
        public boolean visit(final SQLVariantRefExpr x) {
            final String name = x.getName();
            if (name.length() > 3) {
                final char c0 = name.charAt(0);
                final char c2 = name.charAt(1);
                final char c1x = name.charAt(name.length() - 1);
                if (c0 == '#' && c2 == '{' && c1x == '}') {
                    final String key = name.substring(2, name.length() - 1);
                    final Object value = this.parameters.get(x);
                    final SQLExpr expr = SQLExprUtils.fromJavaObject(value, this.timeZone);
                    SQLUtils.replaceInParent(x, expr);
                }
            }
            return true;
        }
    }
}
