// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class AntsparkExprParser extends SQLExprParser
{
    private static final String[] AGGREGATE_FUNCTIONS;
    private static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public AntsparkExprParser(final String sql) {
        this(new AntsparkLexer(sql));
        this.lexer.nextToken();
    }
    
    public AntsparkExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = AntsparkExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = AntsparkExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    @Override
    public SQLExpr primaryRest(final SQLExpr expr) {
        if (this.lexer.token() == Token.LBRACKET) {
            final SQLArrayExpr array = new SQLArrayExpr();
            array.setExpr(expr);
            this.lexer.nextToken();
            this.exprList(array.getValues(), array);
            this.accept(Token.RBRACKET);
            return this.primaryRest(array);
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
    public SQLExternalRecordFormat parseRowFormat() {
        this.lexer.nextToken();
        this.acceptIdentifier("FORMAT");
        if (this.lexer.identifierEquals(FnvHash.Constants.DELIMITED)) {
            this.lexer.nextToken();
        }
        final SQLExternalRecordFormat format = new SQLExternalRecordFormat();
        if (this.lexer.identifierEquals(FnvHash.Constants.FIELDS)) {
            this.lexer.nextToken();
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setTerminatedBy(this.expr());
        }
        else if (this.lexer.identifierEquals("FIELD")) {
            throw new ParserException("syntax error, expect FIELDS, " + this.lexer.info());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LINES)) {
            this.lexer.nextToken();
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setLinesTerminatedBy(this.expr());
        }
        if (this.lexer.token() == Token.ESCAPE || this.lexer.identifierEquals(FnvHash.Constants.ESCAPED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            format.setEscapedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLLECTION)) {
            this.lexer.nextToken();
            this.acceptIdentifier("ITEMS");
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setCollectionItemsTerminatedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.MAP)) {
            this.lexer.nextToken();
            this.acceptIdentifier("KEYS");
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setMapKeysTerminatedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SERDE)) {
            this.lexer.nextToken();
            format.setSerde(this.expr());
        }
        return format;
    }
    
    @Override
    protected SQLExpr parseAliasExpr(final String alias) {
        final String chars = alias.substring(1, alias.length() - 1);
        return new SQLCharExpr(chars);
    }
    
    protected SQLExpr parseDatasource(final String alias) {
        final String chars = alias.substring(1, alias.length() - 1);
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
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[AntsparkExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(AntsparkExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            AntsparkExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
