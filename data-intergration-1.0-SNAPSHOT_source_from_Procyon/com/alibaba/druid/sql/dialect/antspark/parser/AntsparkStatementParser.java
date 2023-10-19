// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.parser;

import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class AntsparkStatementParser extends SQLStatementParser
{
    public AntsparkStatementParser(final String sql) {
        super(new AntsparkExprParser(sql));
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new AntsparkCreateTableParser(this.exprParser);
    }
}
