// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.expr.SQLDecimalExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class HiveExprParser extends SQLExprParser
{
    private static final String[] AGGREGATE_FUNCTIONS;
    private static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public HiveExprParser(final String sql) {
        this(new HiveLexer(sql));
        this.lexer.nextToken();
    }
    
    public HiveExprParser(final String sql, final SQLParserFeature... features) {
        this(new HiveLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public HiveExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = HiveExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = HiveExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        switch (this.lexer.token()) {
            case LBRACKET: {
                final SQLArrayExpr array = new SQLArrayExpr();
                array.setExpr(expr);
                this.lexer.nextToken();
                this.exprList(array.getValues(), array);
                this.accept(Token.RBRACKET);
                return this.primaryRest(array);
            }
            case LITERAL_CHARS: {
                if (expr instanceof SQLCharExpr) {
                    String text2 = ((SQLCharExpr)expr).getText();
                    do {
                        final String chars = this.lexer.stringVal();
                        text2 += chars;
                        this.lexer.nextToken();
                    } while (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS);
                    expr = new SQLCharExpr(text2);
                    break;
                }
                break;
            }
            case IDENTIFIER: {
                if (this.lexer.identifierEquals(FnvHash.Constants.BD) && expr instanceof SQLNumericLiteralExpr) {
                    this.lexer.nextToken();
                    final Number num = ((SQLNumericLiteralExpr)expr).getNumber();
                    expr = new SQLDecimalExpr(num.toString());
                    break;
                }
                break;
            }
        }
        return super.primaryRest(expr);
    }
    
    @Override
    public SQLExpr primary() {
        final Token tok = this.lexer.token();
        switch (tok) {
            case IDENTIFIER: {
                final long hash_lower = this.lexer.hash_lower();
                if (hash_lower == FnvHash.Constants.OUTLINE) {
                    this.lexer.nextToken();
                    final SQLExpr file = this.primary();
                    final SQLExpr expr = new MySqlOutFileExpr(file);
                    return this.primaryRest(expr);
                }
                SQLCurrentTimeExpr currentTimeExpr = null;
                if (hash_lower == FnvHash.Constants.CURRENT_TIMESTAMP) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP);
                }
                else if (hash_lower == FnvHash.Constants.CURRENT_DATE) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_DATE);
                }
                else if (hash_lower == FnvHash.Constants.CURRENT_USER && this.isEnabled(SQLParserFeature.EnableCurrentUserExpr)) {
                    this.lexer.nextToken();
                    return this.primaryRest(new SQLCurrentUserExpr());
                }
                if (currentTimeExpr != null) {
                    final String methodName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        if (this.lexer.token() != Token.LPAREN) {
                            return this.primaryRest(this.methodRest(new SQLIdentifierExpr(methodName), false));
                        }
                        this.lexer.nextToken();
                    }
                    return this.primaryRest(currentTimeExpr);
                }
                break;
            }
        }
        return super.primary();
    }
    
    @Override
    protected SQLExpr parseAliasExpr(final String alias) {
        String chars = alias.substring(1, alias.length() - 1);
        StringBuilder buf = null;
        for (int i = 0; i < chars.length(); ++i) {
            final char ch = chars.charAt(i);
            if (ch == '\\' && i < chars.length() - 1) {
                final char next = chars.charAt(i + 1);
                if (next == '\\') {
                    if (buf == null) {
                        buf = new StringBuilder();
                        buf.append(chars.substring(0, i));
                    }
                    buf.append('\\');
                    ++i;
                }
                else if (next == '\"') {
                    if (buf == null) {
                        buf = new StringBuilder();
                        buf.append(chars.substring(0, i));
                    }
                    buf.append('\"');
                    ++i;
                }
                else if (buf != null) {
                    buf.append(ch);
                }
            }
            else if (buf != null) {
                buf.append(ch);
            }
        }
        if (buf != null) {
            chars = buf.toString();
        }
        return new SQLCharExpr(chars);
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(final SQLColumnDefinition column) {
        if (this.lexer.identifierEquals(FnvHash.Constants.MAPPED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            this.parseAssignItem(column.getMappedBy(), column);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLPROPERTIES)) {
            this.lexer.nextToken();
            this.parseAssignItem(column.getColProperties(), column);
        }
        return super.parseColumnRest(column);
    }
    
    @Override
    protected SQLExpr parseInterval() {
        this.accept(Token.INTERVAL);
        final SQLExpr value = this.expr();
        if (this.lexer.token() != Token.IDENTIFIER) {
            throw new ParserException("Syntax error. " + this.lexer.info());
        }
        final String unit = this.lexer.stringVal();
        this.lexer.nextToken();
        final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
        intervalExpr.setValue(value);
        SQLIntervalUnit intervalUnit = SQLIntervalUnit.valueOf(unit.toUpperCase());
        if (intervalUnit == SQLIntervalUnit.YEAR && this.lexer.token() == Token.TO) {
            this.lexer.nextToken();
            this.acceptIdentifier("MONTH");
            intervalUnit = SQLIntervalUnit.YEAR_TO_MONTH;
        }
        intervalExpr.setUnit(intervalUnit);
        return intervalExpr;
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[HiveExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(HiveExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            HiveExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
