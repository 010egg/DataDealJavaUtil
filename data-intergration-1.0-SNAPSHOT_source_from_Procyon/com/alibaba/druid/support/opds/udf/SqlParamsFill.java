// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.support.json.JSONUtils;
import java.util.List;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class SqlParamsFill extends UDF
{
    public String evaluate(final String sql, final String params) {
        return this.evaluate(sql, params, null, false);
    }
    
    public String evaluate(final String sql, final String params, final String dbTypeName) {
        return this.evaluate(sql, params, dbTypeName, false);
    }
    
    public String evaluate(final String sql, final String params, final String dbTypeName, final boolean throwError) {
        try {
            final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
            final List<Object> inputParams = (List<Object>)JSONUtils.parse(params);
            return ParameterizedOutputVisitorUtils.restore(sql, dbType, inputParams);
        }
        catch (ParserException ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            return null;
        }
    }
}
