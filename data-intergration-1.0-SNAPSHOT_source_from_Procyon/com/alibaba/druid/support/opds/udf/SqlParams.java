// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.List;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlParams extends UDF
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
            final List<Object> outParameters = new ArrayList<Object>();
            ParameterizedOutputVisitorUtils.parameterize(sql, dbType, outParameters);
            return JSONUtils.toJSONString(outParameters);
        }
        catch (ParserException ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return null;
        }
    }
}
