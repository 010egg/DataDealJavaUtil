// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.presto.parser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class PrestoExprParser extends SQLExprParser
{
    public PrestoExprParser(final String sql, final SQLParserFeature... features) {
        this(new PrestoLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public PrestoExprParser(final Lexer lexer) {
        super(lexer);
        this.dbType = DbType.presto;
    }
}
