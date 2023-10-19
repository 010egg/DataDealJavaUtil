// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.h2.parser;

import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class H2SelectParser extends SQLSelectParser
{
    public H2SelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public H2SelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    public H2SelectParser(final String sql) {
        this(new H2ExprParser(sql));
    }
    
    protected SQLExprParser createExprParser() {
        return new H2ExprParser(this.lexer);
    }
}
