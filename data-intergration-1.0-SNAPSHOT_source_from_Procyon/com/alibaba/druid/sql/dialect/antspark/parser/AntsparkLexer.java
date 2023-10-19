// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.parser;

import java.util.Map;
import com.alibaba.druid.sql.parser.Token;
import java.util.HashMap;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class AntsparkLexer extends Lexer
{
    public static final Keywords DEFAULT_ANTSPARK_KEYWORDS;
    
    public AntsparkLexer(final String input) {
        super(input);
        this.dbType = DbType.antspark;
        super.keywords = AntsparkLexer.DEFAULT_ANTSPARK_KEYWORDS;
    }
    
    static {
        final Map<String, Token> map = new HashMap<String, Token>();
        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
        map.put("OF", Token.OF);
        map.put("CONCAT", Token.CONCAT);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("MERGE", Token.MERGE);
        map.put("MATCHED", Token.MATCHED);
        map.put("USING", Token.USING);
        map.put("ROW", Token.ROW);
        map.put("LIMIT", Token.LIMIT);
        map.put("PARTITIONED", Token.PARTITIONED);
        map.put("PARTITION", Token.PARTITION);
        map.put("OVERWRITE", Token.OVERWRITE);
        map.put("IF", Token.IF);
        map.put("TRUE", Token.TRUE);
        map.put("FALSE", Token.FALSE);
        map.put("RLIKE", Token.RLIKE);
        map.put("CONSTRAINT", Token.CONSTRAINT);
        DEFAULT_ANTSPARK_KEYWORDS = new Keywords(map);
    }
}
