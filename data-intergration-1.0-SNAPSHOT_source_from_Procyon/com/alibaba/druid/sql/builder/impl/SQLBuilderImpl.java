// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder.impl;

import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.builder.SQLBuilder;

public class SQLBuilderImpl implements SQLBuilder
{
    public static SQLExpr toSQLExpr(final Object obj, final DbType dbType) {
        if (obj == null) {
            return new SQLNullExpr();
        }
        if (obj instanceof Integer) {
            return new SQLIntegerExpr((Number)obj);
        }
        if (obj instanceof Number) {
            return new SQLNumberExpr((Number)obj);
        }
        if (obj instanceof String) {
            return new SQLCharExpr((String)obj);
        }
        if (obj instanceof Boolean) {
            return new SQLBooleanExpr((boolean)obj);
        }
        throw new IllegalArgumentException("not support : " + obj.getClass().getName());
    }
}
