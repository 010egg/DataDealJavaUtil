// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.parser;

import java.util.Map;
import com.alibaba.druid.sql.parser.Token;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class PhoenixLexer extends Lexer
{
    public static final Keywords DEFAULT_PHOENIX_KEYWORDS;
    
    public PhoenixLexer(final String input, final SQLParserFeature... features) {
        super(input);
        super.keywords = PhoenixLexer.DEFAULT_PHOENIX_KEYWORDS;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("FETCH", Token.FETCH);
        map.put("FIRST", Token.FIRST);
        map.put("ONLY", Token.ONLY);
        map.put("OPTIMIZE", Token.OPTIMIZE);
        map.put("OF", Token.OF);
        map.put("CONCAT", Token.CONCAT);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("IDENTITY", Token.IDENTITY);
        map.put("MERGE", Token.MERGE);
        map.put("USING", Token.USING);
        map.put("MATCHED", Token.MATCHED);
        map.put("UPSERT", Token.UPSERT);
        DEFAULT_PHOENIX_KEYWORDS = new Keywords(map);
    }
}
