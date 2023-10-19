// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;

public class SQLTransformUtils
{
    public static SQLExpr transformDecode(final SQLMethodInvokeExpr x) {
        if (x == null) {
            return null;
        }
        if (FnvHash.Constants.DECODE != x.methodNameHashCode64()) {
            throw new IllegalArgumentException(x.getMethodName());
        }
        final List<SQLExpr> arguments = x.getArguments();
        final SQLCaseExpr caseExpr = new SQLCaseExpr();
        caseExpr.setParent(x.getParent());
        caseExpr.setValueExpr(arguments.get(0));
        if (arguments.size() == 4) {
            final SQLExpr param1 = arguments.get(1);
            x.setMethodName("if");
            SQLBinaryOpExpr condition;
            if (param1 instanceof SQLNullExpr) {
                condition = new SQLBinaryOpExpr(arguments.get(0), SQLBinaryOperator.Is, param1);
            }
            else {
                condition = new SQLBinaryOpExpr(arguments.get(0), SQLBinaryOperator.Equality, param1);
            }
            condition.setParent(x);
            arguments.set(0, condition);
            arguments.set(1, arguments.get(2));
            arguments.set(2, arguments.get(3));
            arguments.remove(3);
            return x;
        }
        for (int i = 1; i + 1 < arguments.size(); i += 2) {
            final SQLCaseExpr.Item item = new SQLCaseExpr.Item();
            final SQLExpr conditionExpr = arguments.get(i);
            item.setConditionExpr(conditionExpr);
            SQLExpr valueExpr = arguments.get(i + 1);
            if (valueExpr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)valueExpr;
                if (FnvHash.Constants.DECODE == methodInvokeExpr.methodNameHashCode64()) {
                    valueExpr = transformDecode(methodInvokeExpr);
                }
            }
            item.setValueExpr(valueExpr);
            caseExpr.addItem(item);
        }
        if (arguments.size() % 2 == 0) {
            SQLExpr defaultExpr = arguments.get(arguments.size() - 1);
            if (defaultExpr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr2 = (SQLMethodInvokeExpr)defaultExpr;
                if (FnvHash.Constants.DECODE == methodInvokeExpr2.methodNameHashCode64()) {
                    defaultExpr = transformDecode(methodInvokeExpr2);
                }
            }
            caseExpr.setElseExpr(defaultExpr);
        }
        caseExpr.setParent(x.getParent());
        return caseExpr;
    }
    
    public static SQLDataType transformOracleToMySql(final SQLDataType x) {
        final String name = x.getName();
        final long nameHash = x.nameHashCode64();
        if (name == null) {
            return x;
        }
        final List<SQLExpr> argumentns = x.getArguments();
        SQLDataType dataType;
        if (nameHash == FnvHash.Constants.UROWID) {
            int len = 4000;
            if (argumentns.size() == 1) {
                final SQLExpr arg0 = argumentns.get(0);
                if (arg0 instanceof SQLIntegerExpr) {
                    len = ((SQLIntegerExpr)arg0).getNumber().intValue();
                }
            }
            dataType = new SQLDataTypeImpl("varchar", len);
        }
        else if (nameHash == FnvHash.Constants.ROWID) {
            dataType = new SQLDataTypeImpl("char", 10);
        }
        else if (nameHash == FnvHash.Constants.BOOLEAN) {
            dataType = new SQLDataTypeImpl("tinyint");
        }
        else if (nameHash == FnvHash.Constants.INTEGER) {
            dataType = new SQLDataTypeImpl("int");
        }
        else if (nameHash == FnvHash.Constants.FLOAT || nameHash == FnvHash.Constants.BINARY_FLOAT) {
            dataType = new SQLDataTypeImpl("float");
        }
        else if (nameHash == FnvHash.Constants.REAL || nameHash == FnvHash.Constants.BINARY_DOUBLE || nameHash == FnvHash.Constants.DOUBLE_PRECISION) {
            dataType = new SQLDataTypeImpl("double");
        }
        else if (nameHash == FnvHash.Constants.NUMBER) {
            if (argumentns.size() == 0) {
                dataType = new SQLDataTypeImpl("decimal", 38);
            }
            else {
                final SQLExpr arg2 = argumentns.get(0);
                int scale = 0;
                int precision;
                if (arg2 instanceof SQLAllColumnExpr) {
                    precision = 9;
                }
                else {
                    precision = ((SQLIntegerExpr)arg2).getNumber().intValue();
                }
                if (argumentns.size() > 1) {
                    scale = argumentns.get(1).getNumber().intValue();
                }
                if (scale > precision) {
                    if (arg2 instanceof SQLAllColumnExpr) {
                        precision = 19;
                        if (scale > precision) {
                            precision = scale;
                        }
                    }
                    else {
                        precision = scale;
                    }
                }
                if (scale == 0) {
                    if (precision < 3) {
                        dataType = new SQLDataTypeImpl("tinyint");
                    }
                    else if (precision < 5) {
                        dataType = new SQLDataTypeImpl("smallint");
                    }
                    else if (precision < 9) {
                        dataType = new SQLDataTypeImpl("int");
                    }
                    else if (precision <= 20) {
                        dataType = new SQLDataTypeImpl("bigint");
                    }
                    else {
                        dataType = new SQLDataTypeImpl("decimal", precision);
                    }
                }
                else {
                    dataType = new SQLDataTypeImpl("decimal", precision, scale);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.DEC || nameHash == FnvHash.Constants.DECIMAL) {
            dataType = x.clone();
            dataType.setName("decimal");
            int precision2 = 0;
            if (argumentns.size() > 0) {
                precision2 = argumentns.get(0).getNumber().intValue();
            }
            int scale2 = 0;
            if (argumentns.size() > 1) {
                scale2 = argumentns.get(1).getNumber().intValue();
                if (precision2 < scale2) {
                    dataType.getArguments().get(1).setNumber(precision2);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.RAW) {
            int len;
            if (argumentns.size() == 0) {
                len = -1;
            }
            else {
                if (argumentns.size() != 1) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
            }
            if (len == -1) {
                dataType = new SQLDataTypeImpl("binary");
            }
            else if (len <= 255) {
                dataType = new SQLDataTypeImpl("binary", len);
            }
            else {
                dataType = new SQLDataTypeImpl("varbinary", len);
            }
        }
        else if (nameHash == FnvHash.Constants.CHAR || nameHash == FnvHash.Constants.CHARACTER) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                if (len2 <= 255) {
                    dataType = new SQLCharacterDataType("char", len2);
                }
                else {
                    dataType = new SQLCharacterDataType("varchar", len2);
                }
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("char");
            }
        }
        else if (nameHash == FnvHash.Constants.NCHAR) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                if (len2 <= 255) {
                    dataType = new SQLCharacterDataType("nchar", len2);
                }
                else {
                    dataType = new SQLCharacterDataType("nvarchar", len2);
                }
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("nchar");
            }
        }
        else if (nameHash == FnvHash.Constants.VARCHAR2) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                dataType = new SQLCharacterDataType("varchar", len);
            }
            else {
                dataType = new SQLCharacterDataType("varchar");
            }
        }
        else if (nameHash == FnvHash.Constants.NVARCHAR2) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                dataType = new SQLCharacterDataType("nvarchar", len);
            }
            else {
                dataType = new SQLCharacterDataType("nvarchar");
            }
        }
        else if (nameHash == FnvHash.Constants.BFILE) {
            dataType = new SQLCharacterDataType("varchar", 255);
        }
        else if (nameHash == FnvHash.Constants.DATE || nameHash == FnvHash.Constants.TIMESTAMP) {
            int len = -1;
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
            }
            if (len >= 0) {
                if (len > 6) {
                    len = 6;
                }
                dataType = new SQLDataTypeImpl("datetime", len);
            }
            else {
                dataType = new SQLDataTypeImpl("datetime");
            }
        }
        else if (nameHash == FnvHash.Constants.BLOB || nameHash == FnvHash.Constants.LONG_RAW) {
            argumentns.clear();
            dataType = new SQLDataTypeImpl("LONGBLOB");
        }
        else if (nameHash == FnvHash.Constants.CLOB || nameHash == FnvHash.Constants.NCLOB || nameHash == FnvHash.Constants.LONG || nameHash == FnvHash.Constants.XMLTYPE) {
            argumentns.clear();
            dataType = new SQLCharacterDataType("LONGTEXT");
        }
        else {
            dataType = x;
        }
        if (dataType != x) {
            dataType.setParent(x.getParent());
        }
        return dataType;
    }
    
    public static SQLDataType transformOracleToAliyunAds(final SQLDataType x) {
        final String dataTypeName = x.getName().toLowerCase();
        SQLDataType dataType;
        if (dataTypeName.equals("varchar2") || dataTypeName.equals("varchar") || dataTypeName.equals("char") || dataTypeName.equals("nchar") || dataTypeName.equals("nvarchar") || dataTypeName.equals("nvarchar2") || dataTypeName.equals("clob") || dataTypeName.equals("nclob") || dataTypeName.equals("blob") || dataTypeName.equals("long") || dataTypeName.equals("long raw") || dataTypeName.equals("raw")) {
            dataType = new SQLCharacterDataType("varchar");
        }
        else if (dataTypeName.equals("number") || dataTypeName.equals("decimal") || dataTypeName.equals("dec") || dataTypeName.equals("numeric")) {
            int scale = 0;
            if (x.getArguments().size() > 1) {
                scale = x.getArguments().get(1).getNumber().intValue();
            }
            if (scale == 0) {
                dataType = new SQLDataTypeImpl("bigint");
            }
            else {
                dataType = new SQLDataTypeImpl("double");
            }
        }
        else if (dataTypeName.equals("date") || dataTypeName.equals("datetime") || dataTypeName.equals("timestamp")) {
            dataType = new SQLDataTypeImpl("timestamp");
        }
        else if (dataTypeName.equals("float") || dataTypeName.equals("binary_float")) {
            dataType = new SQLDataTypeImpl("float");
        }
        else if (dataTypeName.equals("double") || dataTypeName.equals("binary_double")) {
            dataType = new SQLDataTypeImpl("double");
        }
        else {
            dataType = x;
        }
        if (dataType != x) {
            dataType.setParent(x.getParent());
        }
        return dataType;
    }
    
    public static SQLDataType transformOracleToPostgresql(final SQLDataType x) {
        final String name = x.getName();
        final long nameHash = x.nameHashCode64();
        if (name == null) {
            return x;
        }
        final List<SQLExpr> argumentns = x.getArguments();
        SQLDataType dataType;
        if (nameHash == FnvHash.Constants.UROWID) {
            int len = 4000;
            if (argumentns.size() == 1) {
                final SQLExpr arg0 = argumentns.get(0);
                if (arg0 instanceof SQLIntegerExpr) {
                    len = ((SQLIntegerExpr)arg0).getNumber().intValue();
                }
            }
            dataType = new SQLDataTypeImpl("VARCHAR", len);
        }
        else if (nameHash == FnvHash.Constants.ROWID) {
            dataType = new SQLDataTypeImpl("CHAR", 10);
        }
        else if (nameHash == FnvHash.Constants.BOOLEAN || nameHash == FnvHash.Constants.SMALLINT) {
            dataType = new SQLDataTypeImpl("SMALLINT");
        }
        else if (nameHash == FnvHash.Constants.INTEGER || nameHash == FnvHash.Constants.INT) {
            dataType = new SQLDataTypeImpl("DECIMAL", 38);
        }
        else if (nameHash == FnvHash.Constants.BINARY_FLOAT) {
            dataType = new SQLDataTypeImpl("REAL");
        }
        else if (nameHash == FnvHash.Constants.BINARY_DOUBLE || nameHash == FnvHash.Constants.FLOAT || nameHash == FnvHash.Constants.DOUBLE || nameHash == FnvHash.Constants.REAL || nameHash == FnvHash.Constants.DOUBLE_PRECISION) {
            dataType = new SQLDataTypeImpl("DOUBLE PRECISION");
        }
        else if (nameHash == FnvHash.Constants.NUMBER) {
            if (argumentns.size() == 0) {
                dataType = new SQLDataTypeImpl("DECIMAL", 38);
            }
            else {
                final SQLExpr arg2 = argumentns.get(0);
                int scale = 0;
                int precision;
                if (arg2 instanceof SQLAllColumnExpr) {
                    precision = 19;
                    scale = -1;
                }
                else {
                    precision = ((SQLIntegerExpr)arg2).getNumber().intValue();
                }
                if (argumentns.size() > 1) {
                    scale = argumentns.get(1).getNumber().intValue();
                }
                if (scale > precision) {
                    if (arg2 instanceof SQLAllColumnExpr) {
                        precision = 19;
                        if (scale > precision) {
                            precision = scale;
                        }
                    }
                    else {
                        precision = scale;
                    }
                }
                if (scale == 0) {
                    if (precision < 5) {
                        dataType = new SQLDataTypeImpl("SMALLINT");
                    }
                    else if (precision < 9) {
                        dataType = new SQLDataTypeImpl("INT");
                    }
                    else if (precision <= 20) {
                        dataType = new SQLDataTypeImpl("BIGINT");
                    }
                    else {
                        dataType = new SQLDataTypeImpl("DECIMAL", precision);
                    }
                }
                else if (scale == -1) {
                    dataType = new SQLDataTypeImpl("DOUBLE PRECISION");
                }
                else {
                    dataType = new SQLDataTypeImpl("DECIMAL", precision, scale);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.DEC || nameHash == FnvHash.Constants.DECIMAL) {
            dataType = x.clone();
            dataType.setName("DECIMAL");
            int precision2 = 0;
            if (argumentns.size() > 0) {
                precision2 = argumentns.get(0).getNumber().intValue();
            }
            int scale2 = 0;
            if (argumentns.size() > 1) {
                scale2 = argumentns.get(1).getNumber().intValue();
                if (precision2 < scale2) {
                    dataType.getArguments().get(1).setNumber(precision2);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.CHARACTER) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                dataType = new SQLCharacterDataType("CHAR", len2);
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("CHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.CHAR) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                if (len2 <= 2000) {
                    dataType = x;
                    dataType.setName("CHAR");
                }
                else {
                    dataType = new SQLCharacterDataType("TEXT");
                }
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("CHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.NCHAR) {
            dataType = x;
            dataType.setName("NCHAR");
        }
        else if (nameHash == FnvHash.Constants.VARCHAR || nameHash == FnvHash.Constants.VARCHAR2) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                int len;
                if (arg0 instanceof SQLNumericLiteralExpr) {
                    len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                }
                else {
                    if (!(arg0 instanceof SQLVariantRefExpr)) {
                        throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                    }
                    len = 2000;
                }
                if (len <= 4000) {
                    dataType = new SQLCharacterDataType("VARCHAR", len);
                }
                else {
                    dataType = new SQLCharacterDataType("TEXT");
                }
            }
            else {
                dataType = new SQLCharacterDataType("VARCHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.NVARCHAR || nameHash == FnvHash.Constants.NVARCHAR2 || nameHash == FnvHash.Constants.NCHAR_VARYING) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                dataType = new SQLCharacterDataType("VARCHAR", len);
            }
            else {
                dataType = new SQLCharacterDataType("VARCHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.BFILE) {
            dataType = new SQLCharacterDataType("VARCHAR", 255);
        }
        else if (nameHash == FnvHash.Constants.DATE) {
            dataType = new SQLDataTypeImpl("TIMESTAMP", 0);
        }
        else if (nameHash == FnvHash.Constants.TIMESTAMP) {
            x.setName("TIMESTAMP");
            if (x.isWithLocalTimeZone()) {
                x.setWithLocalTimeZone(false);
                x.setWithTimeZone(null);
            }
            dataType = x;
        }
        else if (nameHash == FnvHash.Constants.DATETIME) {
            int len = -1;
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
            }
            if (len > 0) {
                dataType = new SQLDataTypeImpl("TIMESTAMP", len);
            }
            else {
                dataType = new SQLDataTypeImpl("TIMESTAMP");
            }
        }
        else if (nameHash == FnvHash.Constants.BLOB || nameHash == FnvHash.Constants.LONG_RAW || nameHash == FnvHash.Constants.RAW) {
            argumentns.clear();
            dataType = new SQLDataTypeImpl("BYTEA");
        }
        else if (nameHash == FnvHash.Constants.CLOB || nameHash == FnvHash.Constants.NCLOB || nameHash == FnvHash.Constants.LONG) {
            argumentns.clear();
            dataType = new SQLCharacterDataType("TEXT");
        }
        else if (nameHash == FnvHash.Constants.XMLTYPE) {
            dataType = new SQLDataTypeImpl("XML");
        }
        else {
            dataType = x;
        }
        if (dataType != x) {
            dataType.setParent(x.getParent());
        }
        return dataType;
    }
    
    public static SQLExpr transformOracleToPostgresql(final SQLMethodInvokeExpr x) {
        final long nameHashCode64 = x.methodNameHashCode64();
        final List<SQLExpr> parameters = x.getArguments();
        if (nameHashCode64 == FnvHash.Constants.SYS_GUID) {
            final SQLMethodInvokeExpr uuid_generate_v4 = new SQLMethodInvokeExpr("uuid_generate_v4");
            uuid_generate_v4.setParent(x.getParent());
            return uuid_generate_v4;
        }
        if (nameHashCode64 == FnvHash.Constants.TRUNC && parameters.size() == 1) {
            final SQLExpr param0 = parameters.get(0);
            if (param0 instanceof OracleSysdateExpr || (param0 instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)param0).nameHashCode64() == FnvHash.Constants.CURRENT_TIMESTAMP)) {
                final SQLMethodInvokeExpr current_timestamp = new SQLMethodInvokeExpr("CURRENT_TIMESTAMP");
                current_timestamp.addArgument(new SQLIntegerExpr(0));
                current_timestamp.setParent(x.getParent());
                return current_timestamp;
            }
        }
        if (nameHashCode64 == FnvHash.Constants.CURRENT_TIMESTAMP && parameters.size() == 0 && x.getParent() instanceof SQLColumnDefinition) {
            final SQLDataType dataType = ((SQLColumnDefinition)x.getParent()).getDataType();
            if (dataType.nameHashCode64() == FnvHash.Constants.TIMESTAMP && dataType.getArguments().size() == 1) {
                x.addArgument(dataType.getArguments().get(0).clone());
            }
            else {
                x.addArgument(new SQLIntegerExpr(0));
            }
            return x;
        }
        if (nameHashCode64 == FnvHash.Constants.SYSTIMESTAMP) {
            final SQLMethodInvokeExpr xx = x.clone();
            xx.setMethodName("SYSTIMESTAMP");
            xx.setParent(x.getParent());
            return xx;
        }
        if (nameHashCode64 == FnvHash.Constants.LOCALTIMESTAMP) {
            final SQLMethodInvokeExpr xx = x.clone();
            xx.setMethodName("LOCALTIMESTAMP");
            xx.setParent(x.getParent());
            return xx;
        }
        if (nameHashCode64 == FnvHash.Constants.USERENV && x.getArguments().size() == 1) {
            final SQLExpr param0 = x.getArguments().get(0);
            if (param0 instanceof SQLCharExpr) {
                final String text = ((SQLCharExpr)param0).getText();
                if ("SESSIONID".equalsIgnoreCase(text)) {
                    final SQLMethodInvokeExpr xx2 = new SQLMethodInvokeExpr();
                    xx2.setMethodName("get_session_id");
                    xx2.setParent(x.getParent());
                    return xx2;
                }
            }
        }
        if (nameHashCode64 == FnvHash.Constants.USERENV && x.getArguments().size() == 1) {
            final SQLExpr param0 = x.getArguments().get(0);
            if (param0 instanceof SQLCharExpr) {
                final String text = ((SQLCharExpr)param0).getText();
                if ("SESSIONID".equalsIgnoreCase(text)) {
                    final SQLMethodInvokeExpr xx2 = new SQLMethodInvokeExpr();
                    xx2.setMethodName("get_session_id");
                    xx2.setParent(x.getParent());
                    return xx2;
                }
            }
        }
        if (nameHashCode64 == FnvHash.Constants.NUMTODSINTERVAL && x.getArguments().size() == 2) {
            final SQLExpr param0 = x.getArguments().get(0);
            final SQLExpr param2 = x.getArguments().get(1);
            if (param0 instanceof SQLIntegerExpr && param2 instanceof SQLCharExpr) {
                final String text2 = ((SQLCharExpr)param2).getText();
                if ("DAY".equalsIgnoreCase(text2)) {
                    final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
                    intervalExpr.setValue(new SQLCharExpr(param0.toString() + " DAYS"));
                    intervalExpr.setParent(x.getParent());
                    return intervalExpr;
                }
            }
        }
        return x;
    }
    
    public static SQLTableSource transformOracleToPostgresql(final SQLTableSource x) {
        if (x instanceof OracleSelectTableReference) {
            final OracleSelectTableReference xx = (OracleSelectTableReference)x;
            final SQLExprTableSource y = new SQLExprTableSource();
            xx.cloneTo(y);
            y.setParent(x.getParent());
            return y;
        }
        if (x instanceof OracleSelectJoin) {
            final OracleSelectJoin xx2 = (OracleSelectJoin)x;
            final SQLJoinTableSource y2 = new SQLJoinTableSource();
            xx2.cloneTo(y2);
            y2.setLeft(transformOracleToPostgresql(y2.getLeft()));
            y2.setRight(transformOracleToPostgresql(y2.getRight()));
            y2.setParent(x.getParent());
            return y2;
        }
        if (x instanceof OracleSelectSubqueryTableSource) {
            final OracleSelectSubqueryTableSource xx3 = (OracleSelectSubqueryTableSource)x;
            final SQLSubqueryTableSource y3 = new SQLSubqueryTableSource();
            xx3.cloneTo(y3);
            y3.setParent(x.getParent());
            return y3;
        }
        if (x instanceof OracleWithSubqueryEntry) {
            final SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
            ((OracleWithSubqueryEntry)x).cloneTo(entry);
            entry.setParent(x.getParent());
            return entry;
        }
        return x;
    }
    
    public static SQLSelectQueryBlock transformOracleToPostgresql(final SQLSelectQueryBlock x) {
        if (x instanceof OracleSelectQueryBlock) {
            final OracleSelectQueryBlock xx = (OracleSelectQueryBlock)x;
            final SQLSelectQueryBlock y = new SQLSelectQueryBlock();
            xx.cloneTo(y);
            y.setFrom(transformOracleToPostgresql(y.getFrom()));
            y.setParent(x.getParent());
            return y;
        }
        return x;
    }
    
    public static SQLDataType transformOracleToPPAS(final SQLDataType x) {
        final String name = x.getName();
        final long nameHash = x.nameHashCode64();
        if (name == null) {
            return x;
        }
        final List<SQLExpr> argumentns = x.getArguments();
        SQLDataType dataType;
        if (nameHash == FnvHash.Constants.UROWID) {
            int len = 4000;
            if (argumentns.size() == 1) {
                final SQLExpr arg0 = argumentns.get(0);
                if (arg0 instanceof SQLIntegerExpr) {
                    len = ((SQLIntegerExpr)arg0).getNumber().intValue();
                }
            }
            dataType = new SQLDataTypeImpl("VARCHAR", len);
        }
        else if (nameHash == FnvHash.Constants.ROWID) {
            dataType = new SQLDataTypeImpl("CHAR", 10);
        }
        else if (nameHash == FnvHash.Constants.BOOLEAN || nameHash == FnvHash.Constants.SMALLINT) {
            dataType = new SQLDataTypeImpl("SMALLINT");
        }
        else if (nameHash == FnvHash.Constants.INTEGER || nameHash == FnvHash.Constants.INT) {
            dataType = new SQLDataTypeImpl("DECIMAL", 38);
        }
        else if (nameHash == FnvHash.Constants.BINARY_FLOAT) {
            dataType = new SQLDataTypeImpl("REAL");
        }
        else if (nameHash == FnvHash.Constants.BINARY_DOUBLE || nameHash == FnvHash.Constants.FLOAT || nameHash == FnvHash.Constants.DOUBLE || nameHash == FnvHash.Constants.REAL || nameHash == FnvHash.Constants.DOUBLE_PRECISION) {
            dataType = new SQLDataTypeImpl("DOUBLE PRECISION");
        }
        else if (nameHash == FnvHash.Constants.NUMBER) {
            dataType = x.clone();
            if (argumentns.size() > 0) {
                final SQLExpr arg2 = argumentns.get(0);
                if (arg2 instanceof SQLAllColumnExpr) {
                    final SQLIntegerExpr precisionExpr = new SQLIntegerExpr(38);
                    dataType.getArguments().set(0, precisionExpr);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.DEC || nameHash == FnvHash.Constants.DECIMAL) {
            dataType = x.clone();
            dataType.setName("DECIMAL");
            int precision = 0;
            if (argumentns.size() > 0) {
                precision = argumentns.get(0).getNumber().intValue();
            }
            int scale = 0;
            if (argumentns.size() > 1) {
                scale = argumentns.get(1).getNumber().intValue();
                if (precision < scale) {
                    dataType.getArguments().get(1).setNumber(precision);
                }
            }
        }
        else if (nameHash == FnvHash.Constants.CHARACTER) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                dataType = new SQLCharacterDataType("CHAR", len2);
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("CHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.CHAR) {
            if (argumentns.size() == 1) {
                final SQLExpr arg2 = argumentns.get(0);
                if (!(arg2 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len2 = ((SQLNumericLiteralExpr)arg2).getNumber().intValue();
                if (len2 <= 2000) {
                    dataType = x;
                    dataType.setName("CHAR");
                }
                else {
                    dataType = new SQLCharacterDataType("TEXT");
                }
            }
            else {
                if (argumentns.size() != 0) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                dataType = new SQLCharacterDataType("CHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.NCHAR) {
            dataType = x;
            dataType.setName("NCHAR");
        }
        else if (nameHash == FnvHash.Constants.VARCHAR || nameHash == FnvHash.Constants.VARCHAR2) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                int len;
                if (arg0 instanceof SQLNumericLiteralExpr) {
                    len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                }
                else {
                    if (!(arg0 instanceof SQLVariantRefExpr)) {
                        throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                    }
                    len = 2000;
                }
                if (len <= 4000) {
                    dataType = new SQLCharacterDataType("VARCHAR", len);
                }
                else {
                    dataType = new SQLCharacterDataType("TEXT");
                }
            }
            else {
                dataType = new SQLCharacterDataType("VARCHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.NVARCHAR || nameHash == FnvHash.Constants.NVARCHAR2 || nameHash == FnvHash.Constants.NCHAR_VARYING) {
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                final int len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
                dataType = new SQLCharacterDataType("VARCHAR", len);
            }
            else {
                dataType = new SQLCharacterDataType("VARCHAR");
            }
        }
        else if (nameHash == FnvHash.Constants.BFILE) {
            dataType = new SQLCharacterDataType("VARCHAR", 255);
        }
        else if (nameHash == FnvHash.Constants.DATE) {
            dataType = new SQLDataTypeImpl("TIMESTAMP", 0);
        }
        else if (nameHash == FnvHash.Constants.TIMESTAMP) {
            x.setName("TIMESTAMP");
            if (x.isWithLocalTimeZone()) {
                x.setWithLocalTimeZone(false);
                x.setWithTimeZone(null);
            }
            dataType = x;
        }
        else if (nameHash == FnvHash.Constants.DATETIME) {
            int len = -1;
            if (argumentns.size() > 0) {
                final SQLExpr arg0 = argumentns.get(0);
                if (!(arg0 instanceof SQLNumericLiteralExpr)) {
                    throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
                }
                len = ((SQLNumericLiteralExpr)arg0).getNumber().intValue();
            }
            if (len > 0) {
                dataType = new SQLDataTypeImpl("TIMESTAMP", len);
            }
            else {
                dataType = new SQLDataTypeImpl("TIMESTAMP");
            }
        }
        else if (nameHash == FnvHash.Constants.BLOB || nameHash == FnvHash.Constants.LONG_RAW || nameHash == FnvHash.Constants.RAW) {
            argumentns.clear();
            dataType = new SQLDataTypeImpl("BYTEA");
        }
        else if (nameHash == FnvHash.Constants.CLOB || nameHash == FnvHash.Constants.NCLOB || nameHash == FnvHash.Constants.LONG) {
            argumentns.clear();
            dataType = new SQLCharacterDataType("TEXT");
        }
        else if (nameHash == FnvHash.Constants.XMLTYPE) {
            dataType = new SQLDataTypeImpl("XML");
        }
        else {
            dataType = x;
        }
        if (dataType != x) {
            dataType.setParent(x.getParent());
        }
        return dataType;
    }
}
