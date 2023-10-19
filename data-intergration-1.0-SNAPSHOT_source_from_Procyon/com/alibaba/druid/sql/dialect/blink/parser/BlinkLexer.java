// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.parser;

import java.util.Map;
import com.alibaba.druid.sql.parser.Token;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class BlinkLexer extends Lexer
{
    public static final Keywords DEFAULT_BLINK_KEYWORDS;
    
    public BlinkLexer(final String input) {
        super(input);
        super.keywords = BlinkLexer.DEFAULT_BLINK_KEYWORDS;
        this.dbType = DbType.blink;
    }
    
    public BlinkLexer(final String input, final SQLParserFeature... features) {
        super(input);
        super.keywords = BlinkLexer.DEFAULT_BLINK_KEYWORDS;
        this.dbType = DbType.blink;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("OF", Token.OF);
        map.put("CONCAT", Token.CONCAT);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("MERGE", Token.MERGE);
        map.put("USING", Token.USING);
        map.put("ROW", Token.ROW);
        map.put("LIMIT", Token.LIMIT);
        map.put("IF", Token.IF);
        map.put("PERIOD", Token.PERIOD);
        DEFAULT_BLINK_KEYWORDS = new Keywords(map);
    }
}
