// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.parser;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class PhoenixExprParser extends SQLExprParser
{
    public PhoenixExprParser(final String sql, final SQLParserFeature... features) {
        this(new PhoenixLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public PhoenixExprParser(final Lexer lexer) {
        super(lexer);
    }
}
