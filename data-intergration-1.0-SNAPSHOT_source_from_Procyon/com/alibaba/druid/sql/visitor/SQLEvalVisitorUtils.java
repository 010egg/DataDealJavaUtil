// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import java.util.Date;
import java.util.regex.Pattern;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import java.math.BigInteger;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.util.HexBin;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.functions.OneParamFunctions;
import com.alibaba.druid.sql.visitor.functions.DateAdd;
import com.alibaba.druid.sql.visitor.functions.ToChar;
import com.alibaba.druid.sql.visitor.functions.ToDate;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.druid.sql.visitor.functions.Isnull;
import com.alibaba.druid.sql.visitor.functions.Least;
import com.alibaba.druid.sql.visitor.functions.Greatest;
import com.alibaba.druid.sql.visitor.functions.Unhex;
import com.alibaba.druid.sql.visitor.functions.Hex;
import com.alibaba.druid.sql.visitor.functions.Lcase;
import com.alibaba.druid.sql.visitor.functions.Ucase;
import com.alibaba.druid.sql.visitor.functions.Trim;
import com.alibaba.druid.sql.visitor.functions.Length;
import com.alibaba.druid.sql.visitor.functions.Reverse;
import com.alibaba.druid.sql.visitor.functions.Right;
import com.alibaba.druid.sql.visitor.functions.Substring;
import com.alibaba.druid.sql.visitor.functions.Ltrim;
import com.alibaba.druid.sql.visitor.functions.Lpad;
import com.alibaba.druid.sql.visitor.functions.Locate;
import com.alibaba.druid.sql.visitor.functions.Left;
import com.alibaba.druid.sql.visitor.functions.Elt;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.druid.sql.visitor.functions.Instr;
import com.alibaba.druid.sql.visitor.functions.Insert;
import com.alibaba.druid.sql.visitor.functions.BitLength;
import com.alibaba.druid.sql.visitor.functions.Bin;
import com.alibaba.druid.sql.visitor.functions.Ascii;
import com.alibaba.druid.sql.visitor.functions.Concat;
import com.alibaba.druid.sql.visitor.functions.Now;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2EvalVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGEvalVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleEvalVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlEvalVisitorImpl;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.functions.Function;
import java.util.Map;

public class SQLEvalVisitorUtils
{
    private static Map<String, Function> functions;
    
    public static Object evalExpr(final DbType dbType, final String expr, final Object... parameters) {
        final SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr, dbType);
        return eval(dbType, sqlExpr, parameters);
    }
    
    public static Object evalExpr(final DbType dbType, final String expr, final List<Object> parameters) {
        final SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr);
        return eval(dbType, sqlExpr, parameters);
    }
    
    public static Object eval(final DbType dbType, final SQLObject sqlObject, final Object... parameters) {
        Object value = eval(dbType, sqlObject, Arrays.asList(parameters));
        if (value == SQLEvalVisitor.EVAL_VALUE_NULL) {
            value = null;
        }
        return value;
    }
    
    public static Object getValue(final SQLObject sqlObject) {
        if (sqlObject instanceof SQLNumericLiteralExpr) {
            return ((SQLNumericLiteralExpr)sqlObject).getNumber();
        }
        return sqlObject.getAttribute("eval.value");
    }
    
    public static Object eval(final DbType dbType, final SQLObject sqlObject, final List<Object> parameters) {
        return eval(dbType, sqlObject, parameters, true);
    }
    
    public static Object eval(final DbType dbType, final SQLObject sqlObject, final List<Object> parameters, final boolean throwError) {
        final SQLEvalVisitor visitor = createEvalVisitor(dbType);
        visitor.setParameters(parameters);
        Object value;
        if (sqlObject instanceof SQLValuableExpr) {
            value = ((SQLValuableExpr)sqlObject).getValue();
        }
        else {
            sqlObject.accept(visitor);
            value = getValue(sqlObject);
            if (value == null && throwError && !sqlObject.containsAttribute("eval.value")) {
                throw new FastsqlException("eval error : " + SQLUtils.toSQLString(sqlObject, dbType));
            }
        }
        return value;
    }
    
    public static SQLEvalVisitor createEvalVisitor(DbType dbType) {
        if (dbType == null) {
            dbType = DbType.other;
        }
        switch (dbType) {
            case mysql:
            case mariadb:
            case h2: {
                return new MySqlEvalVisitorImpl();
            }
            case oracle: {
                return new OracleEvalVisitor();
            }
            case postgresql:
            case edb: {
                return new PGEvalVisitor();
            }
            case sqlserver:
            case jtds: {
                return new PGEvalVisitor();
            }
            case db2: {
                return new DB2EvalVisitor();
            }
            default: {
                return new SQLEvalVisitorImpl();
            }
        }
    }
    
    static void registerBaseFunctions() {
        SQLEvalVisitorUtils.functions.put("now", Now.instance);
        SQLEvalVisitorUtils.functions.put("concat", Concat.instance);
        SQLEvalVisitorUtils.functions.put("concat_ws", Concat.instance);
        SQLEvalVisitorUtils.functions.put("ascii", Ascii.instance);
        SQLEvalVisitorUtils.functions.put("bin", Bin.instance);
        SQLEvalVisitorUtils.functions.put("bit_length", BitLength.instance);
        SQLEvalVisitorUtils.functions.put("insert", Insert.instance);
        SQLEvalVisitorUtils.functions.put("instr", Instr.instance);
        SQLEvalVisitorUtils.functions.put("char", Char.instance);
        SQLEvalVisitorUtils.functions.put("elt", Elt.instance);
        SQLEvalVisitorUtils.functions.put("left", Left.instance);
        SQLEvalVisitorUtils.functions.put("locate", Locate.instance);
        SQLEvalVisitorUtils.functions.put("lpad", Lpad.instance);
        SQLEvalVisitorUtils.functions.put("ltrim", Ltrim.instance);
        SQLEvalVisitorUtils.functions.put("mid", Substring.instance);
        SQLEvalVisitorUtils.functions.put("substr", Substring.instance);
        SQLEvalVisitorUtils.functions.put("substring", Substring.instance);
        SQLEvalVisitorUtils.functions.put("right", Right.instance);
        SQLEvalVisitorUtils.functions.put("reverse", Reverse.instance);
        SQLEvalVisitorUtils.functions.put("len", Length.instance);
        SQLEvalVisitorUtils.functions.put("length", Length.instance);
        SQLEvalVisitorUtils.functions.put("char_length", Length.instance);
        SQLEvalVisitorUtils.functions.put("character_length", Length.instance);
        SQLEvalVisitorUtils.functions.put("trim", Trim.instance);
        SQLEvalVisitorUtils.functions.put("ucase", Ucase.instance);
        SQLEvalVisitorUtils.functions.put("upper", Ucase.instance);
        SQLEvalVisitorUtils.functions.put("lcase", Lcase.instance);
        SQLEvalVisitorUtils.functions.put("lower", Lcase.instance);
        SQLEvalVisitorUtils.functions.put("hex", Hex.instance);
        SQLEvalVisitorUtils.functions.put("unhex", Unhex.instance);
        SQLEvalVisitorUtils.functions.put("greatest", Greatest.instance);
        SQLEvalVisitorUtils.functions.put("least", Least.instance);
        SQLEvalVisitorUtils.functions.put("isnull", Isnull.instance);
        SQLEvalVisitorUtils.functions.put("if", If.instance);
        SQLEvalVisitorUtils.functions.put("to_date", ToDate.instance);
        SQLEvalVisitorUtils.functions.put("to_char", ToChar.instance);
        SQLEvalVisitorUtils.functions.put("dateadd", DateAdd.instance);
        SQLEvalVisitorUtils.functions.put("md5", OneParamFunctions.instance);
        SQLEvalVisitorUtils.functions.put("bit_count", OneParamFunctions.instance);
        SQLEvalVisitorUtils.functions.put("soundex", OneParamFunctions.instance);
        SQLEvalVisitorUtils.functions.put("space", OneParamFunctions.instance);
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final String methodName = x.getMethodName().toLowerCase();
        Function function = visitor.getFunction(methodName);
        if (function == null) {
            function = SQLEvalVisitorUtils.functions.get(methodName);
        }
        if (function != null) {
            final Object result = function.eval(visitor, x);
            if (result != SQLEvalVisitor.EVAL_ERROR && result != null) {
                x.putAttribute("eval.value", result);
            }
            return false;
        }
        if ("mod".equals(methodName)) {
            if (x.getArguments().size() != 2) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            final SQLExpr param2 = x.getArguments().get(1);
            param0.accept(visitor);
            param2.accept(visitor);
            final Object param0Value = param0.getAttributes().get("eval.value");
            final Object param1Value = param2.getAttributes().get("eval.value");
            if (param0Value == null || param1Value == null) {
                return false;
            }
            final long intValue0 = castToLong(param0Value);
            final long intValue2 = castToLong(param1Value);
            final long result2 = intValue0 % intValue2;
            if (result2 >= -2147483648L && result2 <= 2147483647L) {
                final int intResult = (int)result2;
                x.putAttribute("eval.value", intResult);
            }
            else {
                x.putAttribute("eval.value", result2);
            }
        }
        else if ("abs".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            Object result3;
            if (paramValue instanceof Integer) {
                result3 = Math.abs((int)paramValue);
            }
            else if (paramValue instanceof Long) {
                result3 = Math.abs((long)paramValue);
            }
            else {
                result3 = castToDecimal(paramValue).abs();
            }
            x.putAttribute("eval.value", result3);
        }
        else if ("acos".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.acos(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("asin".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.asin(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("atan".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.atan(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("atan2".equals(methodName)) {
            if (x.getArguments().size() != 2) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            final SQLExpr param2 = x.getArguments().get(1);
            param0.accept(visitor);
            param2.accept(visitor);
            final Object param0Value = param0.getAttributes().get("eval.value");
            final Object param1Value = param2.getAttributes().get("eval.value");
            if (param0Value == null || param1Value == null) {
                return false;
            }
            final double doubleValue2 = castToDouble(param0Value);
            final double doubleValue3 = castToDouble(param1Value);
            final double result5 = Math.atan2(doubleValue2, doubleValue3);
            if (Double.isNaN(result5)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result5);
            }
        }
        else if ("ceil".equals(methodName) || "ceiling".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final int result6 = (int)Math.ceil(doubleValue);
            if (Double.isNaN(result6)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result6);
            }
        }
        else if ("cos".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.cos(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("sin".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.sin(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("log".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.log(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("log10".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.log10(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("tan".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.tan(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("sqrt".equals(methodName)) {
            if (x.getArguments().size() != 1) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            param0.accept(visitor);
            final Object paramValue = param0.getAttributes().get("eval.value");
            if (paramValue == null) {
                return false;
            }
            final double doubleValue = castToDouble(paramValue);
            final double result4 = Math.sqrt(doubleValue);
            if (Double.isNaN(result4)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result4);
            }
        }
        else if ("power".equals(methodName) || "pow".equals(methodName)) {
            if (x.getArguments().size() != 2) {
                return false;
            }
            final SQLExpr param0 = x.getArguments().get(0);
            final SQLExpr param2 = x.getArguments().get(1);
            param0.accept(visitor);
            param2.accept(visitor);
            final Object param0Value = param0.getAttributes().get("eval.value");
            final Object param1Value = param2.getAttributes().get("eval.value");
            if (param0Value == null || param1Value == null) {
                return false;
            }
            final double doubleValue2 = castToDouble(param0Value);
            final double doubleValue3 = castToDouble(param1Value);
            final double result5 = Math.pow(doubleValue2, doubleValue3);
            if (Double.isNaN(result5)) {
                x.putAttribute("eval.value", null);
            }
            else {
                x.putAttribute("eval.value", result5);
            }
        }
        else if ("pi".equals(methodName)) {
            x.putAttribute("eval.value", 3.141592653589793);
        }
        else if ("rand".equals(methodName)) {
            x.putAttribute("eval.value", Math.random());
        }
        else if ("chr".equals(methodName) && x.getArguments().size() == 1) {
            final SQLExpr first = x.getArguments().get(0);
            final Object firstResult = getValue(first);
            if (firstResult instanceof Number) {
                final int intValue3 = ((Number)firstResult).intValue();
                final char ch = (char)intValue3;
                x.putAttribute("eval.value", Character.toString(ch));
            }
        }
        else if ("current_user".equals(methodName)) {
            x.putAttribute("eval.value", "CURRENT_USER");
        }
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLCharExpr x) {
        x.putAttribute("eval.value", x.getText());
        return true;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLHexExpr x) {
        final String hex = x.getHex();
        final byte[] bytes = HexBin.decode(hex);
        if (bytes == null) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_ERROR);
        }
        else {
            final String val = new String(bytes);
            x.putAttribute("eval.value", val);
        }
        return true;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLBinaryExpr x) {
        final String text = x.getText();
        final long[] words = new long[text.length() / 64 + 1];
        for (int i = text.length() - 1; i >= 0; --i) {
            final char ch = text.charAt(i);
            if (ch == '1') {
                final int wordIndex = i >> 6;
                final long[] array = words;
                final int n = wordIndex;
                array[n] |= 1L << text.length() - 1 - i;
            }
        }
        Object val;
        if (words.length == 1) {
            val = words[0];
        }
        else {
            final byte[] bytes = new byte[words.length * 8];
            for (int j = 0; j < words.length; ++j) {
                Utils.putLong(bytes, (words.length - 1 - j) * 8, words[j]);
            }
            val = new BigInteger(bytes);
        }
        x.putAttribute("eval.value", val);
        return false;
    }
    
    public static SQLExpr unwrap(final SQLExpr expr) {
        if (expr == null) {
            return null;
        }
        if (expr instanceof SQLQueryExpr) {
            final SQLSelect select = ((SQLQueryExpr)expr).getSubQuery();
            if (select == null) {
                return null;
            }
            if (select.getQuery() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)select.getQuery();
                if (queryBlock.getFrom() == null && queryBlock.getSelectList().size() == 1) {
                    return queryBlock.getSelectList().get(0).getExpr();
                }
            }
        }
        return expr;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLBetweenExpr x) {
        final SQLExpr testExpr = unwrap(x.getTestExpr());
        testExpr.accept(visitor);
        if (!testExpr.getAttributes().containsKey("eval.value")) {
            return false;
        }
        final Object value = testExpr.getAttribute("eval.value");
        final SQLExpr beginExpr = unwrap(x.getBeginExpr());
        beginExpr.accept(visitor);
        if (!beginExpr.getAttributes().containsKey("eval.value")) {
            return false;
        }
        final Object begin = beginExpr.getAttribute("eval.value");
        if (lt(value, begin)) {
            x.getAttributes().put("eval.value", x.isNot());
            return false;
        }
        final SQLExpr endExpr = unwrap(x.getEndExpr());
        endExpr.accept(visitor);
        if (!endExpr.getAttributes().containsKey("eval.value")) {
            return false;
        }
        final Object end = endExpr.getAttribute("eval.value");
        if (gt(value, end)) {
            x.getAttributes().put("eval.value", x.isNot());
            return false;
        }
        x.getAttributes().put("eval.value", !x.isNot());
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLNullExpr x) {
        x.getAttributes().put("eval.value", SQLEvalVisitor.EVAL_VALUE_NULL);
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLCaseExpr x) {
        Object value;
        if (x.getValueExpr() != null) {
            x.getValueExpr().accept(visitor);
            if (!x.getValueExpr().getAttributes().containsKey("eval.value")) {
                return false;
            }
            value = x.getValueExpr().getAttribute("eval.value");
        }
        else {
            value = null;
        }
        for (final SQLCaseExpr.Item item : x.getItems()) {
            item.getConditionExpr().accept(visitor);
            if (!item.getConditionExpr().getAttributes().containsKey("eval.value")) {
                return false;
            }
            final Object conditionValue = item.getConditionExpr().getAttribute("eval.value");
            if ((x.getValueExpr() != null && eq(value, conditionValue)) || (x.getValueExpr() == null && conditionValue instanceof Boolean && conditionValue == Boolean.TRUE)) {
                item.getValueExpr().accept(visitor);
                if (item.getValueExpr().getAttributes().containsKey("eval.value")) {
                    x.getAttributes().put("eval.value", item.getValueExpr().getAttribute("eval.value"));
                }
                return false;
            }
        }
        if (x.getElseExpr() != null) {
            x.getElseExpr().accept(visitor);
            if (x.getElseExpr().getAttributes().containsKey("eval.value")) {
                x.getAttributes().put("eval.value", x.getElseExpr().getAttribute("eval.value"));
            }
        }
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLInListExpr x) {
        final SQLExpr valueExpr = x.getExpr();
        valueExpr.accept(visitor);
        if (!valueExpr.getAttributes().containsKey("eval.value")) {
            return false;
        }
        final Object value = valueExpr.getAttribute("eval.value");
        for (final SQLExpr item : x.getTargetList()) {
            item.accept(visitor);
            if (!item.getAttributes().containsKey("eval.value")) {
                return false;
            }
            final Object itemValue = item.getAttribute("eval.value");
            if (eq(value, itemValue)) {
                x.getAttributes().put("eval.value", !x.isNot());
                return false;
            }
        }
        x.getAttributes().put("eval.value", x.isNot());
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLQueryExpr x) {
        if (WallVisitorUtils.isSimpleCountTableSource(null, x.getSubQuery())) {
            x.putAttribute("eval.value", 1);
            return false;
        }
        if (x.getSubQuery().getQuery() instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x.getSubQuery().getQuery();
            boolean nullFrom = false;
            if (queryBlock.getFrom() == null) {
                nullFrom = true;
            }
            else if (queryBlock.getFrom() instanceof SQLExprTableSource) {
                final SQLExpr expr = ((SQLExprTableSource)queryBlock.getFrom()).getExpr();
                if (expr instanceof SQLIdentifierExpr && "dual".equalsIgnoreCase(((SQLIdentifierExpr)expr).getName())) {
                    nullFrom = true;
                }
            }
            if (nullFrom) {
                final List<Object> row = new ArrayList<Object>(queryBlock.getSelectList().size());
                for (int i = 0; i < queryBlock.getSelectList().size(); ++i) {
                    final SQLSelectItem item = queryBlock.getSelectList().get(i);
                    item.getExpr().accept(visitor);
                    final Object cell = item.getExpr().getAttribute("eval.value");
                    row.add(cell);
                }
                final List<List<Object>> rows = new ArrayList<List<Object>>(1);
                rows.add(row);
                final Object result = rows;
                queryBlock.putAttribute("eval.value", result);
                x.getSubQuery().putAttribute("eval.value", result);
                x.putAttribute("eval.value", result);
                return false;
            }
        }
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLUnaryExpr x) {
        final WallVisitorUtils.WallConditionContext wallConditionContext = WallVisitorUtils.getWallConditionContext();
        if (x.getOperator() == SQLUnaryOperator.Compl && wallConditionContext != null) {
            wallConditionContext.setBitwise(true);
        }
        x.getExpr().accept(visitor);
        final Object val = x.getExpr().getAttribute("eval.value");
        if (val == SQLEvalVisitor.EVAL_ERROR) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_ERROR);
            return false;
        }
        if (val == null) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_VALUE_NULL);
            return false;
        }
        switch (x.getOperator()) {
            case BINARY:
            case RAW: {
                x.putAttribute("eval.value", val);
                break;
            }
            case NOT:
            case Not: {
                final Boolean booleanVal = castToBoolean(val);
                if (booleanVal != null) {
                    x.putAttribute("eval.value", !booleanVal);
                    break;
                }
                break;
            }
            case Plus: {
                x.putAttribute("eval.value", val);
                break;
            }
            case Negative: {
                x.putAttribute("eval.value", multi(val, -1));
                break;
            }
            case Compl: {
                x.putAttribute("eval.value", ~castToInteger(val));
                break;
            }
        }
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLBinaryOpExpr x) {
        final SQLExpr left = unwrap(x.getLeft());
        final SQLExpr right = unwrap(x.getRight());
        left.accept(visitor);
        right.accept(visitor);
        final WallVisitorUtils.WallConditionContext wallConditionContext = WallVisitorUtils.getWallConditionContext();
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            if (wallConditionContext != null && (left.getAttribute("eval.value") == Boolean.TRUE || right.getAttribute("eval.value") == Boolean.TRUE)) {
                wallConditionContext.setPartAlwayTrue(true);
            }
        }
        else if (x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            if (wallConditionContext != null && (left.getAttribute("eval.value") == Boolean.FALSE || right.getAttribute("eval.value") == Boolean.FALSE)) {
                wallConditionContext.setPartAlwayFalse(true);
            }
        }
        else if (x.getOperator() == SQLBinaryOperator.BooleanXor) {
            if (wallConditionContext != null) {
                wallConditionContext.setXor(true);
            }
        }
        else if ((x.getOperator() == SQLBinaryOperator.BitwiseAnd || x.getOperator() == SQLBinaryOperator.BitwiseNot || x.getOperator() == SQLBinaryOperator.BitwiseOr || x.getOperator() == SQLBinaryOperator.BitwiseXor) && wallConditionContext != null) {
            wallConditionContext.setBitwise(true);
        }
        Object leftValue = left.getAttribute("eval.value");
        Object rightValue = right.getAttributes().get("eval.value");
        if (x.getOperator() == SQLBinaryOperator.Like && isAlwayTrueLikePattern(x.getRight())) {
            x.putAttribute("hasTrueLike", Boolean.TRUE);
            x.putAttribute("eval.value", Boolean.TRUE);
            return false;
        }
        if (x.getOperator() == SQLBinaryOperator.NotLike && isAlwayTrueLikePattern(x.getRight())) {
            x.putAttribute("eval.value", Boolean.FALSE);
            return false;
        }
        final boolean leftHasValue = left.getAttributes().containsKey("eval.value");
        final boolean rightHasValue = right.getAttributes().containsKey("eval.value");
        if (!leftHasValue && !rightHasValue) {
            final SQLExpr leftEvalExpr = (SQLExpr)left.getAttribute("eval.expr");
            final SQLExpr rightEvalExpr = (SQLExpr)right.getAttribute("eval.expr");
            if (leftEvalExpr != null && leftEvalExpr.equals(rightEvalExpr)) {
                switch (x.getOperator()) {
                    case Like:
                    case SoudsLike:
                    case Equality:
                    case GreaterThanOrEqual:
                    case LessThanOrEqual:
                    case NotLessThan:
                    case NotGreaterThan: {
                        x.putAttribute("eval.value", Boolean.TRUE);
                        return false;
                    }
                    case NotEqual:
                    case LessThanOrGreater:
                    case NotLike:
                    case GreaterThan:
                    case LessThan: {
                        x.putAttribute("eval.value", Boolean.FALSE);
                        return false;
                    }
                }
            }
        }
        if (!leftHasValue) {
            return false;
        }
        if (!rightHasValue) {
            return false;
        }
        if (wallConditionContext != null) {
            wallConditionContext.setConstArithmetic(true);
        }
        leftValue = processValue(leftValue);
        rightValue = processValue(rightValue);
        if (leftValue == null || rightValue == null) {
            return false;
        }
        Object value = null;
        switch (x.getOperator()) {
            case Add: {
                value = add(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case Subtract: {
                value = sub(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case Multiply: {
                value = multi(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case Divide: {
                value = div(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case RightShift: {
                value = rightShift(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case BitwiseAnd: {
                value = bitAnd(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case BitwiseOr: {
                value = bitOr(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case GreaterThan: {
                value = gt(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case GreaterThanOrEqual: {
                value = gteq(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case LessThan: {
                value = lt(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case LessThanOrEqual: {
                value = lteq(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case Is: {
                if (rightValue == SQLEvalVisitor.EVAL_VALUE_NULL && leftValue != null) {
                    value = (leftValue == SQLEvalVisitor.EVAL_VALUE_NULL);
                    x.putAttribute("eval.value", value);
                    break;
                }
                break;
            }
            case Equality: {
                value = eq(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case NotEqual:
            case LessThanOrGreater: {
                value = !eq(leftValue, rightValue);
                x.putAttribute("eval.value", value);
                break;
            }
            case IsNot: {
                if (leftValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
                    x.putAttribute("eval.value", false);
                    break;
                }
                if (leftValue != null) {
                    x.putAttribute("eval.value", true);
                    break;
                }
                break;
            }
            case RegExp:
            case RLike: {
                final String pattern = castToString(rightValue);
                final String input = castToString(left.getAttributes().get("eval.value"));
                final boolean matchResult = Pattern.matches(pattern, input);
                x.putAttribute("eval.value", matchResult);
                break;
            }
            case NotRegExp:
            case NotRLike: {
                final String pattern = castToString(rightValue);
                final String input = castToString(left.getAttributes().get("eval.value"));
                final boolean matchResult = !Pattern.matches(pattern, input);
                x.putAttribute("eval.value", matchResult);
                break;
            }
            case Like: {
                final String pattern = castToString(rightValue);
                final String input = castToString(left.getAttributes().get("eval.value"));
                final boolean matchResult = like(input, pattern);
                x.putAttribute("eval.value", matchResult);
                break;
            }
            case NotLike: {
                final String pattern = castToString(rightValue);
                final String input = castToString(left.getAttributes().get("eval.value"));
                final boolean matchResult = !like(input, pattern);
                x.putAttribute("eval.value", matchResult);
                break;
            }
            case Concat: {
                final String result = leftValue.toString() + rightValue.toString();
                x.putAttribute("eval.value", result);
                break;
            }
            case BooleanAnd: {
                final boolean first = eq(leftValue, true);
                final boolean second = eq(rightValue, true);
                x.putAttribute("eval.value", first && second);
                break;
            }
            case BooleanOr: {
                final boolean first = eq(leftValue, true);
                final boolean second = eq(rightValue, true);
                x.putAttribute("eval.value", first || second);
                break;
            }
        }
        return false;
    }
    
    private static Object processValue(final Object value) {
        if (value instanceof List) {
            final List list = (List)value;
            if (list.size() == 1) {
                return processValue(list.get(0));
            }
        }
        else if (value instanceof Date) {
            return ((Date)value).getTime();
        }
        return value;
    }
    
    private static boolean isAlwayTrueLikePattern(final SQLExpr x) {
        if (x instanceof SQLCharExpr) {
            final String text = ((SQLCharExpr)x).getText();
            if (text.length() > 0) {
                for (final char ch : text.toCharArray()) {
                    if (ch != '%') {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLNumericLiteralExpr x) {
        x.putAttribute("eval.value", x.getNumber());
        return false;
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLVariantRefExpr x) {
        if (!"?".equals(x.getName())) {
            return false;
        }
        final Map<String, Object> attributes = x.getAttributes();
        final int varIndex = x.getIndex();
        final List<Object> parameters = visitor.getParameters();
        if (varIndex != -1 && parameters != null && parameters.size() > varIndex) {
            final boolean containsValue = attributes.containsKey("eval.value");
            if (!containsValue) {
                Object value = parameters.get(varIndex);
                if (value == null) {
                    value = SQLEvalVisitor.EVAL_VALUE_NULL;
                }
                attributes.put("eval.value", value);
            }
        }
        return false;
    }
    
    public static Boolean castToBoolean(final Object val) {
        if (val == null) {
            return null;
        }
        if (val == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (val instanceof Boolean) {
            return (Boolean)val;
        }
        if (val instanceof Number) {
            return ((Number)val).intValue() > 0;
        }
        if (!(val instanceof String)) {
            throw new IllegalArgumentException(val.getClass() + " not supported.");
        }
        if ("1".equals(val) || "true".equalsIgnoreCase((String)val)) {
            return true;
        }
        return false;
    }
    
    public static String castToString(final Object val) {
        final Object value = val;
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    public static Byte castToByte(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Byte) {
            return (Byte)val;
        }
        if (val instanceof String) {
            return Byte.parseByte((String)val);
        }
        return ((Number)val).byteValue();
    }
    
    public static Short castToShort(final Object val) {
        if (val == null || val == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (val instanceof Short) {
            return (Short)val;
        }
        if (val instanceof String) {
            return Short.parseShort((String)val);
        }
        return ((Number)val).shortValue();
    }
    
    public static Integer castToInteger(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Integer) {
            return (Integer)val;
        }
        if (val instanceof String) {
            return Integer.parseInt((String)val);
        }
        if (val instanceof List) {
            final List list = (List)val;
            if (list.size() == 1) {
                return castToInteger(list.get(0));
            }
        }
        if (val instanceof Boolean) {
            if (val) {
                return 1;
            }
            return 0;
        }
        else {
            if (val instanceof Number) {
                return ((Number)val).intValue();
            }
            throw new FastsqlException("cast error");
        }
    }
    
    public static Long castToLong(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Long) {
            return (Long)val;
        }
        if (val instanceof String) {
            return Long.parseLong((String)val);
        }
        if (val instanceof List) {
            final List list = (List)val;
            if (list.size() == 1) {
                return castToLong(list.get(0));
            }
        }
        if (!(val instanceof Boolean)) {
            return ((Number)val).longValue();
        }
        if (val) {
            return 1L;
        }
        return 0L;
    }
    
    public static Float castToFloat(final Object val) {
        if (val == null || val == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (val instanceof Float) {
            return (Float)val;
        }
        return ((Number)val).floatValue();
    }
    
    public static Double castToDouble(final Object val) {
        if (val == null || val == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (val instanceof Double) {
            return (Double)val;
        }
        return ((Number)val).doubleValue();
    }
    
    public static BigInteger castToBigInteger(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof BigInteger) {
            return (BigInteger)val;
        }
        if (val instanceof String) {
            return new BigInteger((String)val);
        }
        return BigInteger.valueOf(((Number)val).longValue());
    }
    
    public static Number castToNumber(final String val) {
        if (val == null) {
            return null;
        }
        try {
            return Byte.parseByte(val);
        }
        catch (NumberFormatException ex) {
            try {
                return Short.parseShort(val);
            }
            catch (NumberFormatException ex2) {
                try {
                    return Integer.parseInt(val);
                }
                catch (NumberFormatException ex3) {
                    try {
                        return Long.parseLong(val);
                    }
                    catch (NumberFormatException ex4) {
                        try {
                            return Float.parseFloat(val);
                        }
                        catch (NumberFormatException ex5) {
                            try {
                                return Double.parseDouble(val);
                            }
                            catch (NumberFormatException ex6) {
                                try {
                                    return new BigInteger(val);
                                }
                                catch (NumberFormatException ex7) {
                                    try {
                                        return new BigDecimal(val);
                                    }
                                    catch (NumberFormatException e) {
                                        return 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static Date castToDate(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Date) {
            return (Date)val;
        }
        if (val instanceof Number) {
            return new Date(((Number)val).longValue());
        }
        if (val instanceof String) {
            return castToDate((String)val);
        }
        throw new FastsqlException("can cast to date");
    }
    
    public static Date castToDate(final String text) {
        if (text == null || text.length() == 0) {
            return null;
        }
        String format;
        if (text.length() == "yyyy-MM-dd".length()) {
            format = "yyyy-MM-dd";
        }
        else {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            return new SimpleDateFormat(format).parse(text);
        }
        catch (ParseException e) {
            throw new FastsqlException("rowFormat : " + format + ", value : " + text, e);
        }
    }
    
    public static BigDecimal castToDecimal(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal)val;
        }
        if (val instanceof String) {
            return new BigDecimal((String)val);
        }
        if (val instanceof Float) {
            return new BigDecimal((float)val);
        }
        if (val instanceof Double) {
            return new BigDecimal((double)val);
        }
        return BigDecimal.valueOf(((Number)val).longValue());
    }
    
    public static Object rightShift(final Object a, final Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (a instanceof Long || b instanceof Long) {
            return (long)castToLong(a) >> (int)(long)castToLong(b);
        }
        return castToInteger(a) >> castToInteger(b);
    }
    
    public static Object bitAnd(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (a instanceof String) {
            a = castToNumber((String)a);
        }
        if (b instanceof String) {
            b = castToNumber((String)b);
        }
        if (a instanceof Long || b instanceof Long) {
            return (long)castToLong(a) & (long)castToLong(b);
        }
        return castToInteger(a) & castToInteger(b);
    }
    
    public static Object bitOr(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (a instanceof String) {
            a = castToNumber((String)a);
        }
        if (b instanceof String) {
            b = castToNumber((String)b);
        }
        if (a instanceof Long || b instanceof Long) {
            return (long)castToLong(a) | (long)castToLong(b);
        }
        return castToInteger(a) | castToInteger(b);
    }
    
    public static Object div(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        }
        if (a instanceof String) {
            a = castToNumber((String)a);
        }
        if (b instanceof String) {
            b = castToNumber((String)b);
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            final BigDecimal decimalA = castToDecimal(a);
            BigDecimal decimalB = castToDecimal(b);
            if (decimalB.scale() < decimalA.scale()) {
                decimalB = decimalB.setScale(decimalA.scale());
            }
            try {
                return decimalA.divide(decimalB);
            }
            catch (ArithmeticException ex) {
                return decimalA.divide(decimalB, 4);
            }
        }
        if (a instanceof Double || b instanceof Double) {
            final Double doubleA = castToDouble(a);
            final Double doubleB = castToDouble(b);
            if (doubleA == null || doubleB == null) {
                return null;
            }
            return doubleA / doubleB;
        }
        else if (a instanceof Float || b instanceof Float) {
            final Float floatA = castToFloat(a);
            final Float floatB = castToFloat(b);
            if (floatA == null || floatB == null) {
                return null;
            }
            return floatA / floatB;
        }
        else {
            if (a instanceof BigInteger || b instanceof BigInteger) {
                return castToBigInteger(a).divide(castToBigInteger(b));
            }
            if (a instanceof Long || b instanceof Long) {
                final Long longA = castToLong(a);
                final Long longB = castToLong(b);
                if (longB != 0L) {
                    return longA / longB;
                }
                if (longA > 0L) {
                    return Double.POSITIVE_INFINITY;
                }
                if (longA < 0L) {
                    return Double.NEGATIVE_INFINITY;
                }
                return Double.NaN;
            }
            else if (a instanceof Integer || b instanceof Integer) {
                final Integer intA = castToInteger(a);
                final Integer intB = castToInteger(b);
                if (intB != 0) {
                    return intA / intB;
                }
                if (intA > 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (intA < 0) {
                    return Double.NEGATIVE_INFINITY;
                }
                return Double.NaN;
            }
            else {
                if (a instanceof Short || b instanceof Short) {
                    return (short)castToShort(a) / (short)castToShort(b);
                }
                if (a instanceof Byte || b instanceof Byte) {
                    return (byte)castToByte(a) / (byte)castToByte(b);
                }
                throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
            }
        }
    }
    
    public static boolean gt(final Object a, final Object b) {
        if (a == null || a == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return false;
        }
        if (b == null || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return true;
        }
        if (a instanceof String || b instanceof String) {
            return castToString(a).compareTo(castToString(b)) > 0;
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).compareTo(castToDecimal(b)) > 0;
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).compareTo(castToBigInteger(b)) > 0;
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a) > castToLong(b);
        }
        if (a instanceof Integer || b instanceof Integer) {
            return castToInteger(a) > castToInteger(b);
        }
        if (a instanceof Short || b instanceof Short) {
            return castToShort(a) > castToShort(b);
        }
        if (a instanceof Byte || b instanceof Byte) {
            return castToByte(a) > castToByte(b);
        }
        if (a instanceof Date || b instanceof Date) {
            final Date d1 = castToDate(a);
            final Date d2 = castToDate(b);
            return d1 != d2 && d1 != null && (d2 == null || d1.compareTo(d2) > 0);
        }
        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
    }
    
    public static boolean gteq(final Object a, final Object b) {
        return eq(a, b) || gt(a, b);
    }
    
    public static boolean lt(final Object a, final Object b) {
        if (a == null) {
            return true;
        }
        if (b == null) {
            return false;
        }
        if (a instanceof String || b instanceof String) {
            return castToString(a).compareTo(castToString(b)) < 0;
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).compareTo(castToDecimal(b)) < 0;
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).compareTo(castToBigInteger(b)) < 0;
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a) < castToLong(b);
        }
        if (a instanceof Integer || b instanceof Integer) {
            final Integer intA = castToInteger(a);
            final Integer intB = castToInteger(b);
            return intA < intB;
        }
        if (a instanceof Short || b instanceof Short) {
            return castToShort(a) < castToShort(b);
        }
        if (a instanceof Byte || b instanceof Byte) {
            return castToByte(a) < castToByte(b);
        }
        if (a instanceof Date || b instanceof Date) {
            final Date d1 = castToDate(a);
            final Date d2 = castToDate(b);
            return d1 != d2 && (d1 == null || (d2 != null && d1.compareTo(d2) < 0));
        }
        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
    }
    
    public static boolean lteq(final Object a, final Object b) {
        return eq(a, b) || lt(a, b);
    }
    
    public static boolean eq(final Object a, final Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }
        if (a instanceof String || b instanceof String) {
            return castToString(a).equals(castToString(b));
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).compareTo(castToDecimal(b)) == 0;
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).compareTo(castToBigInteger(b)) == 0;
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a).equals(castToLong(b));
        }
        if (a instanceof Integer || b instanceof Integer) {
            final Integer inta = castToInteger(a);
            final Integer intb = castToInteger(b);
            return inta != null && intb != null && inta.equals(intb);
        }
        if (a instanceof Short || b instanceof Short) {
            return castToShort(a).equals(castToShort(b));
        }
        if (a instanceof Boolean || b instanceof Boolean) {
            return castToBoolean(a).equals(castToBoolean(b));
        }
        if (a instanceof Byte || b instanceof Byte) {
            return castToByte(a).equals(castToByte(b));
        }
        if (a instanceof Date || b instanceof Date) {
            final Date d1 = castToDate(a);
            final Date d2 = castToDate(b);
            return d1 == d2 || (d1 != null && d2 != null && d1.equals(d2));
        }
        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
    }
    
    public static Object add(Object a, Object b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        if (a instanceof String && !(b instanceof String)) {
            a = castToNumber((String)a);
        }
        if (b instanceof String && !(a instanceof String)) {
            b = castToNumber((String)b);
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).add(castToDecimal(b));
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).add(castToBigInteger(b));
        }
        if (a instanceof Double || b instanceof Double) {
            return castToDouble(a) + castToDouble(b);
        }
        if (a instanceof Float || b instanceof Float) {
            return castToFloat(a) + castToFloat(b);
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a) + castToLong(b);
        }
        if (a instanceof Integer || b instanceof Integer) {
            return castToInteger(a) + castToInteger(b);
        }
        if (a instanceof Short || b instanceof Short) {
            return (short)castToShort(a) + (short)castToShort(b);
        }
        if (a instanceof Boolean || b instanceof Boolean) {
            int aI = 0;
            int bI = 0;
            if (castToBoolean(a)) {
                aI = 1;
            }
            if (castToBoolean(b)) {
                bI = 1;
            }
            return aI + bI;
        }
        if (a instanceof Byte || b instanceof Byte) {
            return (byte)castToByte(a) + (byte)castToByte(b);
        }
        if (a instanceof String && b instanceof String) {
            return castToString(a) + castToString(b);
        }
        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
    }
    
    public static Object sub(Object a, Object b) {
        if (a == null) {
            return null;
        }
        if (b == null) {
            return a;
        }
        if (a == SQLEvalVisitor.EVAL_VALUE_NULL || b == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        if (a instanceof Date || b instanceof Date) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (a instanceof String) {
            a = castToNumber((String)a);
        }
        if (b instanceof String) {
            b = castToNumber((String)b);
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).subtract(castToDecimal(b));
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).subtract(castToBigInteger(b));
        }
        if (a instanceof Double || b instanceof Double) {
            return castToDouble(a) - castToDouble(b);
        }
        if (a instanceof Float || b instanceof Float) {
            return castToFloat(a) - castToFloat(b);
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a) - castToLong(b);
        }
        if (a instanceof Integer || b instanceof Integer) {
            return castToInteger(a) - castToInteger(b);
        }
        if (a instanceof Short || b instanceof Short) {
            return (short)castToShort(a) - (short)castToShort(b);
        }
        if (a instanceof Boolean || b instanceof Boolean) {
            int aI = 0;
            int bI = 0;
            if (castToBoolean(a)) {
                aI = 1;
            }
            if (castToBoolean(b)) {
                bI = 1;
            }
            return aI - bI;
        }
        if (a instanceof Byte || b instanceof Byte) {
            return (byte)castToByte(a) - (byte)castToByte(b);
        }
        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
    }
    
    public static Object multi(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (a instanceof String) {
            a = castToNumber((String)a);
        }
        if (b instanceof String) {
            b = castToNumber((String)b);
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return castToDecimal(a).multiply(castToDecimal(b));
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return castToBigInteger(a).multiply(castToBigInteger(b));
        }
        if (a instanceof Double || b instanceof Double) {
            return castToDouble(a) * castToDouble(b);
        }
        if (a instanceof Float || b instanceof Float) {
            return castToFloat(a) * castToFloat(b);
        }
        if (a instanceof Long || b instanceof Long) {
            return castToLong(a) * castToLong(b);
        }
        if (a instanceof Integer || b instanceof Integer) {
            return castToInteger(a) * castToInteger(b);
        }
        if (a instanceof Short || b instanceof Short) {
            final Short shortA = castToShort(a);
            final Short shortB = castToShort(b);
            if (shortA == null || shortB == null) {
                return null;
            }
            return (short)shortA * (short)shortB;
        }
        else {
            if (a instanceof Byte || b instanceof Byte) {
                return (byte)castToByte(a) * (byte)castToByte(b);
            }
            throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
        }
    }
    
    public static boolean like(final String input, final String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern is null");
        }
        final StringBuilder regexprBuilder = new StringBuilder(pattern.length() + 4);
        final int STAT_NOTSET = 0;
        final int STAT_RANGE = 1;
        final int STAT_LITERAL = 2;
        int stat = 0;
        int blockStart = -1;
        for (int i = 0; i < pattern.length(); ++i) {
            final char ch = pattern.charAt(i);
            if (stat == 2 && (ch == '%' || ch == '_' || ch == '[')) {
                final String block = pattern.substring(blockStart, i);
                regexprBuilder.append("\\Q");
                regexprBuilder.append(block);
                regexprBuilder.append("\\E");
                blockStart = -1;
                stat = 0;
            }
            if (ch == '%') {
                regexprBuilder.append(".*");
            }
            else if (ch == '_') {
                regexprBuilder.append('.');
            }
            else if (ch == '[') {
                if (stat == 1) {
                    throw new IllegalArgumentException("illegal pattern : " + pattern);
                }
                stat = 1;
                blockStart = i;
            }
            else if (ch == ']') {
                if (stat != 1) {
                    throw new IllegalArgumentException("illegal pattern : " + pattern);
                }
                final String block = pattern.substring(blockStart, i + 1);
                regexprBuilder.append(block);
                blockStart = -1;
            }
            else {
                if (stat == 0) {
                    stat = 2;
                    blockStart = i;
                }
                if (stat == 2 && i == pattern.length() - 1) {
                    final String block = pattern.substring(blockStart, i + 1);
                    regexprBuilder.append("\\Q");
                    regexprBuilder.append(block);
                    regexprBuilder.append("\\E");
                }
            }
        }
        if ("%".equals(pattern) || "%%".equals(pattern)) {
            return true;
        }
        final String regexpr = regexprBuilder.toString();
        return Pattern.matches(regexpr, input);
    }
    
    public static boolean visit(final SQLEvalVisitor visitor, final SQLIdentifierExpr x) {
        x.putAttribute("eval.expr", x);
        return false;
    }
    
    static {
        SQLEvalVisitorUtils.functions = new HashMap<String, Function>();
        registerBaseFunctions();
    }
}
