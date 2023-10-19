// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.antspark.parser.AntsparkLexer;
import com.alibaba.druid.sql.dialect.presto.parser.PrestoLexer;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixLexer;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsLexer;
import com.alibaba.druid.sql.dialect.db2.parser.DB2Lexer;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGLexer;
import com.alibaba.druid.sql.dialect.h2.parser.H2Lexer;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleLexer;
import com.alibaba.druid.sql.dialect.hive.parser.HiveExprParser;
import com.alibaba.druid.sql.dialect.presto.parser.PrestoExprParser;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixExprParser;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsExprParser;
import com.alibaba.druid.sql.dialect.db2.parser.DB2ExprParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerExprParser;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGExprParser;
import com.alibaba.druid.sql.dialect.h2.parser.H2ExprParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlExprParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleExprParser;
import com.alibaba.druid.sql.dialect.clickhouse.parser.ClickhouseStatementParser;
import com.alibaba.druid.sql.dialect.antspark.parser.AntsparkStatementParser;
import com.alibaba.druid.sql.dialect.ads.parser.AdsStatementParser;
import com.alibaba.druid.sql.dialect.presto.parser.PrestoStatementParser;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixStatementParser;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsStatementParser;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.dialect.blink.parser.BlinkStatementParser;
import com.alibaba.druid.sql.dialect.h2.parser.H2StatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.DbType;

public class SQLParserUtils
{
    public static SQLStatementParser createSQLStatementParser(final String sql, final DbType dbType) {
        SQLParserFeature[] features;
        if (DbType.odps == dbType || DbType.mysql == dbType) {
            features = new SQLParserFeature[] { SQLParserFeature.KeepComments };
        }
        else {
            features = new SQLParserFeature[0];
        }
        return createSQLStatementParser(sql, dbType, features);
    }
    
    public static SQLStatementParser createSQLStatementParser(final String sql, final DbType dbType, final boolean keepComments) {
        SQLParserFeature[] features;
        if (keepComments) {
            features = new SQLParserFeature[] { SQLParserFeature.KeepComments };
        }
        else {
            features = new SQLParserFeature[0];
        }
        return createSQLStatementParser(sql, dbType, features);
    }
    
    public static SQLStatementParser createSQLStatementParser(final String sql, final String dbType, final SQLParserFeature... features) {
        return createSQLStatementParser(sql, (dbType == null) ? null : DbType.valueOf(dbType), features);
    }
    
    public static SQLStatementParser createSQLStatementParser(final String sql, DbType dbType, final SQLParserFeature... features) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case oracle:
            case oceanbase_oracle: {
                return new OracleStatementParser(sql, features);
            }
            case mysql:
            case mariadb:
            case drds: {
                return new MySqlStatementParser(sql, features);
            }
            case elastic_search: {
                final MySqlStatementParser parser = new MySqlStatementParser(sql, features);
                parser.dbType = dbType;
                parser.exprParser.dbType = dbType;
                return parser;
            }
            case postgresql:
            case edb: {
                return new PGSQLStatementParser(sql, features);
            }
            case sqlserver:
            case jtds: {
                return new SQLServerStatementParser(sql);
            }
            case h2: {
                return new H2StatementParser(sql, features);
            }
            case blink: {
                return new BlinkStatementParser(sql, features);
            }
            case db2: {
                return new DB2StatementParser(sql, features);
            }
            case odps: {
                return new OdpsStatementParser(sql, features);
            }
            case phoenix: {
                return new PhoenixStatementParser(sql);
            }
            case hive: {
                return new HiveStatementParser(sql, features);
            }
            case presto:
            case trino: {
                return new PrestoStatementParser(sql);
            }
            case ads: {
                return new AdsStatementParser(sql);
            }
            case antspark: {
                return new AntsparkStatementParser(sql);
            }
            case clickhouse: {
                return new ClickhouseStatementParser(sql);
            }
            default: {
                return new SQLStatementParser(sql, dbType);
            }
        }
    }
    
    public static SQLExprParser createExprParser(final String sql, DbType dbType, final SQLParserFeature... features) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case oracle: {
                return new OracleExprParser(sql, features);
            }
            case mysql:
            case mariadb: {
                return new MySqlExprParser(sql, features);
            }
            case elastic_search: {
                final MySqlExprParser parser = new MySqlExprParser(sql, features);
                parser.dbType = dbType;
                return parser;
            }
            case h2: {
                return new H2ExprParser(sql, features);
            }
            case postgresql:
            case edb: {
                return new PGExprParser(sql, features);
            }
            case sqlserver:
            case jtds: {
                return new SQLServerExprParser(sql, features);
            }
            case db2: {
                return new DB2ExprParser(sql, features);
            }
            case odps: {
                return new OdpsExprParser(sql, features);
            }
            case phoenix: {
                return new PhoenixExprParser(sql, features);
            }
            case presto:
            case trino: {
                return new PrestoExprParser(sql, features);
            }
            case hive: {
                return new HiveExprParser(sql, features);
            }
            default: {
                return new SQLExprParser(sql, dbType, features);
            }
        }
    }
    
    public static Lexer createLexer(final String sql, final DbType dbType) {
        return createLexer(sql, dbType, new SQLParserFeature[0]);
    }
    
    public static Lexer createLexer(final String sql, DbType dbType, final SQLParserFeature... features) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case oracle: {
                return new OracleLexer(sql);
            }
            case mysql:
            case mariadb: {
                return new MySqlLexer(sql);
            }
            case elastic_search: {
                final MySqlLexer lexer = new MySqlLexer(sql);
                lexer.dbType = dbType;
                return lexer;
            }
            case h2: {
                return new H2Lexer(sql);
            }
            case postgresql:
            case edb: {
                return new PGLexer(sql, new SQLParserFeature[0]);
            }
            case db2: {
                return new DB2Lexer(sql);
            }
            case odps: {
                return new OdpsLexer(sql, new SQLParserFeature[0]);
            }
            case phoenix: {
                return new PhoenixLexer(sql, new SQLParserFeature[0]);
            }
            case presto:
            case trino: {
                return new PrestoLexer(sql, new SQLParserFeature[0]);
            }
            case antspark: {
                return new AntsparkLexer(sql);
            }
            default: {
                return new Lexer(sql, null, dbType);
            }
        }
    }
    
    public static SQLSelectQueryBlock createSelectQueryBlock(DbType dbType) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case mysql: {
                return new MySqlSelectQueryBlock();
            }
            case oracle: {
                return new OracleSelectQueryBlock();
            }
            case db2: {
                return new DB2SelectQueryBlock();
            }
            case postgresql: {
                return new PGSelectQueryBlock();
            }
            case odps: {
                return new OdpsSelectQueryBlock();
            }
            case sqlserver: {
                return new SQLServerSelectQueryBlock();
            }
            default: {
                return new SQLSelectQueryBlock(dbType);
            }
        }
    }
    
    public static SQLType getSQLType(final String sql, final DbType dbType) {
        final Lexer lexer = createLexer(sql, dbType);
        return lexer.scanSQLType();
    }
    
    public static SQLType getSQLTypeV2(final String sql, final DbType dbType) {
        final Lexer lexer = createLexer(sql, dbType);
        return lexer.scanSQLTypeV2();
    }
    
    public static boolean startsWithHint(final String sql, final DbType dbType) {
        final Lexer lexer = createLexer(sql, dbType);
        lexer.nextToken();
        return lexer.token() == Token.HINT;
    }
    
    public static boolean containsAny(final String sql, final DbType dbType, final Token token) {
        final Lexer lexer = createLexer(sql, dbType);
        while (true) {
            lexer.nextToken();
            final Token tok = lexer.token;
            switch (tok) {
                case EOF:
                case ERROR: {
                    return false;
                }
                default: {
                    if (tok == token) {
                        return true;
                    }
                    continue;
                }
            }
        }
    }
    
    public static boolean containsAny(final String sql, final DbType dbType, final Token token1, final Token token2) {
        final Lexer lexer = createLexer(sql, dbType);
        while (true) {
            lexer.nextToken();
            final Token tok = lexer.token;
            switch (tok) {
                case EOF:
                case ERROR: {
                    return false;
                }
                default: {
                    if (tok == token1 || tok == token2) {
                        return true;
                    }
                    continue;
                }
            }
        }
    }
    
    public static boolean containsAny(final String sql, final DbType dbType, final Token token1, final Token token2, final Token token3) {
        final Lexer lexer = createLexer(sql, dbType);
        while (true) {
            lexer.nextToken();
            final Token tok = lexer.token;
            switch (tok) {
                case EOF:
                case ERROR: {
                    return false;
                }
                default: {
                    if (tok == token1 || tok == token2 || tok == token3) {
                        return true;
                    }
                    continue;
                }
            }
        }
    }
    
    public static boolean containsAny(final String sql, final DbType dbType, final Token... tokens) {
        if (tokens == null) {
            return false;
        }
        final Lexer lexer = createLexer(sql, dbType);
        while (true) {
            lexer.nextToken();
            final Token tok = lexer.token;
            switch (tok) {
                case EOF:
                case ERROR: {
                    return false;
                }
                default: {
                    for (int i = 0; i < tokens.length; ++i) {
                        if (tokens[i] == tok) {
                            return true;
                        }
                    }
                    continue;
                }
            }
        }
    }
    
    public static Object getSimpleSelectValue(final String sql, final DbType dbType) {
        return getSimpleSelectValue(sql, dbType, null);
    }
    
    public static Object getSimpleSelectValue(final String sql, final DbType dbType, final SimpleValueEvalHandler handler) {
        final Lexer lexer = createLexer(sql, dbType);
        lexer.nextToken();
        if (lexer.token != Token.SELECT && lexer.token != Token.VALUES) {
            return null;
        }
        lexer.nextTokenValue();
        SQLExpr expr = null;
        Object value = null;
        switch (lexer.token) {
            case LITERAL_INT: {
                value = lexer.integerValue();
                break;
            }
            case LITERAL_CHARS:
            case LITERAL_NCHARS: {
                value = lexer.stringVal();
                break;
            }
            case LITERAL_FLOAT: {
                value = lexer.decimalValue();
                break;
            }
            default: {
                if (handler == null) {
                    return null;
                }
                expr = new SQLExprParser(lexer).expr();
                try {
                    value = handler.eval(expr);
                }
                catch (Exception error) {
                    value = null;
                }
                break;
            }
        }
        lexer.nextToken();
        if (lexer.token == Token.FROM) {
            lexer.nextToken();
            if (lexer.token != Token.DUAL) {
                return null;
            }
            lexer.nextToken();
        }
        if (lexer.token != Token.EOF) {
            return null;
        }
        return value;
    }
    
    public static String replaceBackQuote(final String sql, final DbType dbType) {
        final int i = sql.indexOf(96);
        if (i == -1) {
            return sql;
        }
        final char[] chars = sql.toCharArray();
        final Lexer lexer = createLexer(sql, dbType);
        int len = chars.length;
        int off = 0;
    Label_0225:
        while (true) {
            lexer.nextToken();
            switch (lexer.token) {
                case IDENTIFIER: {
                    final int p0 = lexer.startPos + off;
                    final int p2 = lexer.pos - 1 + off;
                    final char c0 = chars[p0];
                    final char c2 = chars[p2];
                    if (c0 != '`' || c2 != '`') {
                        continue;
                    }
                    if (p2 - p0 > 2 && chars[p0 + 1] == '\'' && chars[p2 - 1] == '\'') {
                        System.arraycopy(chars, p0 + 1, chars, p0, p2 - p0 - 1);
                        System.arraycopy(chars, p2 + 1, chars, p2 - 1, chars.length - p2 - 1);
                        len -= 2;
                        off -= 2;
                        continue;
                    }
                    chars[p2] = (chars[p0] = '\"');
                    continue;
                }
                case EOF:
                case ERROR: {
                    break Label_0225;
                }
                default: {
                    continue;
                }
            }
        }
        return new String(chars, 0, len);
    }
    
    public static String addBackQuote(final String sql, final DbType dbType) {
        if (StringUtils.isEmpty(sql)) {
            return sql;
        }
        final SQLStatementParser parser = createSQLStatementParser(sql, dbType);
        final StringBuffer buf = new StringBuffer(sql.length() + 20);
        final SQLASTOutputVisitor out = SQLUtils.createOutputVisitor(buf, DbType.mysql);
        out.config(VisitorFeature.OutputNameQuote, true);
        final SQLType sqlType = getSQLType(sql, dbType);
        if (sqlType == SQLType.INSERT) {
            parser.config(SQLParserFeature.InsertReader, true);
            final SQLInsertStatement stmt = (SQLInsertStatement)parser.parseStatement();
            final int startPos = parser.getLexer().startPos;
            stmt.accept(out);
            if (stmt.getQuery() == null) {
                buf.append(' ');
                buf.append(sql, startPos, sql.length());
            }
        }
        else {
            final SQLStatement stmt2 = parser.parseStatement();
            stmt2.accept(out);
        }
        return buf.toString();
    }
    
    public interface SimpleValueEvalHandler
    {
        Object eval(final SQLExpr p0);
    }
}
