// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.parser;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class HiveSelectParser extends SQLSelectParser
{
    public HiveSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public HiveSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    public HiveSelectParser(final String sql) {
        this(new HiveExprParser(sql));
    }
    
    protected SQLExprParser createExprParser() {
        return new HiveExprParser(this.lexer);
    }
    
    @Override
    public void parseTableSourceSample(final SQLTableSource tableSource) {
        this.parseTableSourceSampleHive(tableSource);
    }
}
