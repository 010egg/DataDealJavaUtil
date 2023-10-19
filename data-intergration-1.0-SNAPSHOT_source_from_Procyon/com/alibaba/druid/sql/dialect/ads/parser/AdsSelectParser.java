// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.parser;

import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class AdsSelectParser extends SQLSelectParser
{
    public AdsSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public AdsSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    public AdsSelectParser(final String sql) {
        this(new AdsExprParser(sql));
    }
    
    protected SQLExprParser createExprParser() {
        return new AdsExprParser(this.lexer);
    }
}
