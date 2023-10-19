// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.parser;

import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class BlinkStatementParser extends SQLStatementParser
{
    public BlinkStatementParser(final String sql) {
        super(new BlinkExprParser(sql));
    }
    
    public BlinkStatementParser(final String sql, final SQLParserFeature... features) {
        super(new BlinkExprParser(sql, features));
    }
    
    public BlinkStatementParser(final Lexer lexer) {
        super(new BlinkExprParser(lexer));
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new BlinkCreateTableParser(this.exprParser);
    }
}
