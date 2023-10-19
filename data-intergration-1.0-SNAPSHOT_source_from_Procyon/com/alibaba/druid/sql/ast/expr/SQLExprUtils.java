// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.DbType;
import java.util.Iterator;
import com.alibaba.druid.sql.SQLUtils;
import java.util.List;
import com.alibaba.druid.sql.parser.ParserException;
import java.util.Date;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.TimeZone;
import com.alibaba.druid.sql.ast.SQLExpr;

public class SQLExprUtils
{
    public static boolean equals(final SQLExpr a, final SQLExpr b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        final Class<?> clazz_a = a.getClass();
        final Class<?> clazz_b = b.getClass();
        if (clazz_a == SQLPropertyExpr.class && clazz_b == SQLIdentifierExpr.class) {
            return ((SQLPropertyExpr)a).equals((SQLIdentifierExpr)b);
        }
        if (clazz_a != clazz_b) {
            return false;
        }
        if (clazz_a == SQLIdentifierExpr.class) {
            final SQLIdentifierExpr x_a = (SQLIdentifierExpr)a;
            final SQLIdentifierExpr x_b = (SQLIdentifierExpr)b;
            return x_a.hashCode() == x_b.hashCode();
        }
        if (clazz_a == SQLBinaryOpExpr.class) {
            final SQLBinaryOpExpr x_a2 = (SQLBinaryOpExpr)a;
            final SQLBinaryOpExpr x_b2 = (SQLBinaryOpExpr)b;
            return x_a2.equals(x_b2);
        }
        return a.equals(b);
    }
    
    public static boolean isLiteralExpr(final SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        }
        if (expr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binary = (SQLBinaryOpExpr)expr;
            return isLiteralExpr(binary.left) && isLiteralExpr(binary.right);
        }
        return false;
    }
    
    public static SQLExpr fromJavaObject(final Object o) {
        return fromJavaObject(o, null);
    }
    
    public static SQLExpr fromJavaObject(final Object o, final TimeZone timeZone) {
        if (o == null) {
            return new SQLNullExpr();
        }
        if (o instanceof String) {
            return new SQLCharExpr((String)o);
        }
        if (o instanceof BigDecimal) {
            return new SQLDecimalExpr((BigDecimal)o);
        }
        if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long || o instanceof BigInteger) {
            return new SQLIntegerExpr((Number)o);
        }
        if (o instanceof Number) {
            return new SQLNumberExpr((Number)o);
        }
        if (o instanceof Date) {
            return new SQLTimestampExpr((Date)o, timeZone);
        }
        throw new ParserException("not support class : " + o.getClass());
    }
    
    public static SQLInListExpr conditionIn(final String column, final List<Object> values, final TimeZone timeZone) {
        final SQLInListExpr in = new SQLInListExpr();
        in.setExpr(SQLUtils.toSQLExpr(column));
        for (final Object value : values) {
            in.addTarget(fromJavaObject(value, timeZone));
        }
        return in;
    }
    
    public static String quote(final String str, final DbType dbType, final char quote) {
        SQLExpr expr;
        if (quote == '`') {
            expr = new SQLIdentifierExpr(str);
        }
        else if (quote == '\"') {
            if (dbType == DbType.oracle || dbType == DbType.presto || dbType == DbType.trino) {
                expr = new SQLIdentifierExpr(str);
            }
            else {
                expr = new SQLCharExpr(str);
            }
        }
        else {
            if (quote != '\'') {
                throw new FastsqlException("quote not support");
            }
            expr = new SQLCharExpr(str);
        }
        return SQLUtils.toSQLString(expr, dbType);
    }
    
    public static SQLDataType createDataTypeFromJdbc(final DbType dbType, final int jdbType, final Integer precision, final Integer scale) {
        final SQLDataType dataType = null;
        Label_0125: {
            switch (jdbType) {
                case 4: {
                    if (dbType == null) {
                        return new SQLDataTypeImpl("integer");
                    }
                    switch (dbType) {
                        case mysql: {
                            return new SQLDataTypeImpl("int");
                        }
                        default: {
                            break Label_0125;
                        }
                    }
                    break;
                }
                case 12: {
                    switch (dbType) {
                        case mysql: {
                            return new SQLDataTypeImpl("varchar");
                        }
                        default: {
                            break Label_0125;
                        }
                    }
                    break;
                }
            }
        }
        if (dataType != null) {
            if (dbType != null) {
                dataType.setDbType(dbType);
            }
            return dataType;
        }
        throw new FastsqlException("type " + jdbType + " not support");
    }
}
