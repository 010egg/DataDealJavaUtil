// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlSyntaxCheck extends UDF
{
    public Boolean evaluate(final String sql) {
        return this.evaluate(sql, null, false);
    }
    
    public Boolean evaluate(final String sql, final String dbTypeName) {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    public Boolean evaluate(final String sql, final String dbTypeName, final boolean throwError) {
        if (sql == null || sql.length() == 0) {
            return null;
        }
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        try {
            final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
            final List<SQLStatement> statementList = parser.parseStatementList();
            return true;
        }
        catch (ParserException ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return false;
        }
    }
}
