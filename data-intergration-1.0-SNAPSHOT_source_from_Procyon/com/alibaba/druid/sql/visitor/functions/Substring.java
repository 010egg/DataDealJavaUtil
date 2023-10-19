// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Substring implements Function
{
    public static final Substring instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> params = x.getArguments();
        int paramSize = params.size();
        final SQLExpr param0 = params.get(0);
        SQLExpr param2;
        if (paramSize == 1 && x.getFrom() != null) {
            param2 = x.getFrom();
            paramSize = 2;
        }
        else {
            if (paramSize != 2 && paramSize != 3) {
                return SQLEvalVisitor.EVAL_ERROR;
            }
            param2 = params.get(1);
        }
        param0.accept(visitor);
        param2.accept(visitor);
        final Object param0Value = param0.getAttributes().get("eval.value");
        final Object param1Value = param2.getAttributes().get("eval.value");
        if (param0Value == null || param1Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String str = param0Value.toString();
        final int index = ((Number)param1Value).intValue();
        if (paramSize == 2 && x.getFor() == null) {
            if (index <= 0) {
                final int lastIndex = str.length() + index;
                return str.substring(lastIndex);
            }
            return str.substring(index - 1);
        }
        else {
            SQLExpr param3 = x.getFor();
            if (param3 == null && params.size() > 2) {
                param3 = params.get(2);
            }
            param3.accept(visitor);
            final Object param2Value = param3.getAttributes().get("eval.value");
            if (param2Value == null) {
                return SQLEvalVisitor.EVAL_ERROR;
            }
            final int len = ((Number)param2Value).intValue();
            String result;
            if (index <= 0) {
                final int lastIndex2 = str.length() + index;
                result = str.substring(lastIndex2);
            }
            else {
                result = str.substring(index - 1);
            }
            if (len > result.length()) {
                return result;
            }
            return result.substring(0, len);
        }
    }
    
    public Object eval(final SQLMethodInvokeExpr x) {
        final List<SQLExpr> parameters = x.getArguments();
        for (final SQLExpr parameter : parameters) {
            if (!(parameter instanceof SQLValuableExpr)) {
                return null;
            }
        }
        if (parameters.size() == 3) {
            Object p0 = parameters.get(0).getValue();
            final Object p2 = parameters.get(1).getValue();
            final Object p3 = parameters.get(2).getValue();
            if (p0 instanceof Number) {
                p0 = p0.toString();
            }
            if (p0 instanceof String && p2 instanceof Integer && p3 instanceof Integer) {
                final String str = (String)p0;
                final int beginIndex = (int)p2;
                final int len = (int)p3;
                if (len < 0) {
                    return null;
                }
                final int start = beginIndex - 1;
                final int end = beginIndex - 1 + len;
                if (start < 0 || start >= str.length()) {
                    return null;
                }
                if (end < 0 || end >= str.length()) {
                    return null;
                }
                if (beginIndex > 0 && len > 0) {
                    return str.substring(start, end);
                }
            }
        }
        else if (parameters.size() == 2) {
            final Object p0 = parameters.get(0).getValue();
            final Object p2 = parameters.get(1).getValue();
            if (p0 instanceof String && p2 instanceof Integer) {
                final String str2 = (String)p0;
                final int beginIndex2 = (int)p2;
                if (beginIndex2 < 0 || beginIndex2 >= str2.length()) {
                    return null;
                }
                if (beginIndex2 > 0) {
                    return str2.substring(beginIndex2 - 1);
                }
            }
        }
        return SQLEvalVisitor.EVAL_ERROR;
    }
    
    static {
        instance = new Substring();
    }
}
