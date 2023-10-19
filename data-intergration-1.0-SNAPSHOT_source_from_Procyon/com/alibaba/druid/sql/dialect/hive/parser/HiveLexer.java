// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.parser;

import java.util.Map;
import com.alibaba.druid.sql.parser.Token;
import java.util.HashMap;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Keywords;
import com.alibaba.druid.sql.parser.Lexer;

public class HiveLexer extends Lexer
{
    public static final Keywords DEFAULT_HIVE_KEYWORDS;
    
    public HiveLexer(final String input) {
        super(input);
        this.skipComment = true;
        this.keepComments = true;
        this.dbType = DbType.hive;
        this.features |= SQLParserFeature.SupportUnicodeCodePoint.mask;
        super.keywords = HiveLexer.DEFAULT_HIVE_KEYWORDS;
    }
    
    public HiveLexer(final String input, final SQLParserFeature... features) {
        super(input);
        this.dbType = DbType.hive;
        this.skipComment = true;
        this.keepComments = true;
        super.keywords = HiveLexer.DEFAULT_HIVE_KEYWORDS;
        this.features |= SQLParserFeature.SupportUnicodeCodePoint.mask;
        for (final SQLParserFeature feature : features) {
            this.config(feature, true);
        }
    }
    
    @Override
    protected final void scanString() {
        this.scanString2();
    }
    
    @Override
    public void scanComment() {
        this.scanHiveComment();
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
        map.put("DIV", Token.DIV);
        DEFAULT_HIVE_KEYWORDS = new Keywords(map);
    }
}
