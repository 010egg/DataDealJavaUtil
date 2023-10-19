// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlPattern extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName) {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName, final boolean throwError) {
        try {
            final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
            return ParameterizedOutputVisitorUtils.parameterize(sql, dbType);
        }
        catch (ParserException ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return null;
        }
    }
}
