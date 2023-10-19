// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class DB2ExprParser extends SQLExprParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public DB2ExprParser(final String sql) {
        this(new DB2Lexer(sql));
        this.lexer.nextToken();
        this.dbType = DbType.db2;
    }
    
    public DB2ExprParser(final String sql, final SQLParserFeature... features) {
        this(new DB2Lexer(sql, features));
        this.lexer.nextToken();
    }
    
    public DB2ExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = DB2ExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = DB2ExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.dbType = DbType.db2;
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        if (this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.NEXT) {
                    this.lexer.nextToken();
                    this.accept(Token.FOR);
                    final SQLName seqName = this.name();
                    final SQLSequenceExpr seqExpr = new SQLSequenceExpr(seqName, SQLSequenceExpr.Function.NextVal);
                    return seqExpr;
                }
                if (identExpr.hashCode64() == FnvHash.Constants.PREVIOUS) {
                    this.lexer.nextToken();
                    this.accept(Token.FOR);
                    final SQLName seqName = this.name();
                    final SQLSequenceExpr seqExpr = new SQLSequenceExpr(seqName, SQLSequenceExpr.Function.PrevVal);
                    return seqExpr;
                }
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.DATE)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    expr = new SQLIdentifierExpr("CURRENT DATE");
                }
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.DAY) && expr instanceof SQLIntegerExpr) {
            this.lexer.nextToken();
            expr = new SQLIntervalExpr(expr, SQLIntervalUnit.DAY);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TIMESTAMP)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    expr = new SQLIdentifierExpr("CURRENT TIMESTAMP");
                }
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    expr = new SQLIdentifierExpr("CURRENT TIME");
                }
            }
        }
        else if (this.lexer.token() == Token.SCHEMA) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    expr = new SQLIdentifierExpr("CURRENT SCHEMA");
                }
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.PATH)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                if (identExpr.hashCode64() == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    expr = new SQLIdentifierExpr("CURRENT PATH");
                }
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MONTHS)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.MONTH);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.YEARS)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.YEAR);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.DAYS)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.DAY);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.HOUR) || this.lexer.identifierEquals(FnvHash.Constants.HOURS)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.HOUR);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MINUTES)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.MINUTE);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SECONDS)) {
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr(expr, SQLIntervalUnit.SECOND);
            this.lexer.nextToken();
            expr = intervalExpr;
        }
        return super.primaryRest(expr);
    }
    
    @Override
    protected SQLExpr dotRest(final SQLExpr expr) {
        if (this.lexer.identifierEquals(FnvHash.Constants.NEXTVAL)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(identExpr, SQLSequenceExpr.Function.NextVal);
                this.lexer.nextToken();
                return seqExpr;
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.PREVVAL)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(identExpr, SQLSequenceExpr.Function.PrevVal);
                this.lexer.nextToken();
                return seqExpr;
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CURRVAL) && expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            final SQLSequenceExpr seqExpr = new SQLSequenceExpr(identExpr, SQLSequenceExpr.Function.CurrVal);
            this.lexer.nextToken();
            return seqExpr;
        }
        return super.dotRest(expr);
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
        column = super.parseColumnRest(column);
        if (this.lexer.identifierEquals(FnvHash.Constants.GENERATED)) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.ALWAYS)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.AS);
            if (this.lexer.token() == Token.IDENTITY) {
                final SQLColumnDefinition.Identity identity = this.parseIdentity();
                column.setIdentity(identity);
            }
            else {
                final SQLExpr expr = this.expr();
                column.setGeneratedAlawsAs(expr);
            }
            this.parseColumnRest(column);
        }
        return column;
    }
    
    private SQLColumnDefinition.Identity parseIdentity() {
        final SQLColumnDefinition.Identity identity = new SQLColumnDefinition.Identity();
        this.accept(Token.IDENTITY);
        if (this.lexer.token() == Token.LPAREN) {
            this.accept(Token.LPAREN);
            if (this.lexer.identifierEquals(FnvHash.Constants.START)) {
                this.lexer.nextToken();
                this.accept(Token.WITH);
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                identity.setSeed((Integer)this.lexer.integerValue());
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.INCREMENT)) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                identity.setIncrement((Integer)this.lexer.integerValue());
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.CYCLE)) {
                this.lexer.nextToken();
                identity.setCycle(true);
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MINVALUE)) {
                this.lexer.nextTokenValue();
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                identity.setMinValue((Integer)this.lexer.integerValue());
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MAXVALUE)) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                identity.setMaxValue((Integer)this.lexer.integerValue());
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
            }
            this.accept(Token.RPAREN);
        }
        return identity;
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[DB2ExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(DB2ExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            DB2ExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
