// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.parser;

import java.util.Map;
import com.alibaba.druid.sql.parser.Token;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class ClickhouseLexer extends Lexer
{
    public static final Keywords DEFAULT_KEYWORDS;
    
    public ClickhouseLexer(final String input) {
        super(input);
        this.dbType = DbType.clickhouse;
        super.keywords = ClickhouseLexer.DEFAULT_KEYWORDS;
    }
    
    public ClickhouseLexer(final String input, final SQLParserFeature... features) {
        super(input);
        super.keywords = ClickhouseLexer.DEFAULT_KEYWORDS;
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
        map.put("SHOW", Token.SHOW);
        map.put("ALL", Token.ALL);
        map.put("GLOBAL", Token.GLOBAL);
        DEFAULT_KEYWORDS = new Keywords(map);
    }
}
