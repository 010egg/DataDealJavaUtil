// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.parser;

import java.util.Arrays;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class BlinkExprParser extends SQLExprParser
{
    private static final String[] AGGREGATE_FUNCTIONS;
    private static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public BlinkExprParser(final String sql) {
        this(new BlinkLexer(sql));
        this.lexer.nextToken();
    }
    
    public BlinkExprParser(final String sql, final SQLParserFeature... features) {
        this(new BlinkLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public BlinkExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = BlinkExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = BlinkExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[BlinkExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(BlinkExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            BlinkExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
