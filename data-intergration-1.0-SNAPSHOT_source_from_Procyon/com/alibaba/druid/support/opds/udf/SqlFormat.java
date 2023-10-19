// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlFormat extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName) {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName, final boolean throwError) {
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        try {
            final SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
            final List<SQLStatement> statementList = parser.parseStatementList();
            return SQLUtils.toSQLString(statementList, dbType);
        }
        catch (Exception ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return sql;
        }
    }
}
