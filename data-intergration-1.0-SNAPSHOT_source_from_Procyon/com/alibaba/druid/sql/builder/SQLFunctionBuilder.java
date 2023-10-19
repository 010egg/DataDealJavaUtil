// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;

public class SQLFunctionBuilder
{
    private final DbType dbType;
    public static final SQLFunctionBuilder Oracle;
    
    public SQLFunctionBuilder(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public SQLMethodInvokeExpr length(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("length", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr lower(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("lower", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr upper(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("upper", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr substr(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("substr", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr ltrim(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("ltrim", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr rtrim(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("rtrim", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr trim(final SQLExpr expr) {
        return new SQLMethodInvokeExpr("trim", null, new SQLExpr[] { expr });
    }
    
    public SQLMethodInvokeExpr ifnull(final SQLExpr expr1, final SQLExpr expr2) {
        switch (this.dbType) {
            case ads:
            case presto:
            case trino:
            case odps: {
                return new SQLMethodInvokeExpr("coalesce", null, new SQLExpr[] { expr1, expr2 });
            }
            case oracle: {
                return new SQLMethodInvokeExpr("nvl", null, new SQLExpr[] { expr1, expr2 });
            }
            case sqlserver: {
                return new SQLMethodInvokeExpr("isnull", null, new SQLExpr[] { expr1, expr2 });
            }
            default: {
                return new SQLMethodInvokeExpr("ifnull", null, new SQLExpr[] { expr1, expr2 });
            }
        }
    }
    
    static {
        Oracle = new SQLFunctionBuilder(DbType.oracle);
    }
}
