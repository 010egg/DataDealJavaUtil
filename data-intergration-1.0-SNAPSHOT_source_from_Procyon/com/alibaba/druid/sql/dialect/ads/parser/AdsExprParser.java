// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.parser;

import java.util.Arrays;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class AdsExprParser extends SQLExprParser
{
    private static final String[] AGGREGATE_FUNCTIONS;
    private static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public AdsExprParser(final String sql) {
        this(new AdsLexer(sql));
        this.lexer.nextToken();
    }
    
    public AdsExprParser(final String sql, final SQLParserFeature... features) {
        this(new AdsLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public AdsExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = AdsExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = AdsExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    @Override
    protected SQLExpr parseAliasExpr(final String alias) {
        final String chars = alias.substring(1, alias.length() - 1);
        return new SQLCharExpr(chars);
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
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[AdsExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(AdsExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            AdsExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
