// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Insert implements Function
{
    public static final Insert instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() != 4) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = x.getArguments().get(0);
        final SQLExpr param2 = x.getArguments().get(1);
        final SQLExpr param3 = x.getArguments().get(2);
        final SQLExpr param4 = x.getArguments().get(3);
        param0.accept(visitor);
        param2.accept(visitor);
        param3.accept(visitor);
        param4.accept(visitor);
        final Object param0Value = param0.getAttributes().get("eval.value");
        final Object param1Value = param2.getAttributes().get("eval.value");
        final Object param2Value = param3.getAttributes().get("eval.value");
        final Object param3Value = param4.getAttributes().get("eval.value");
        if (!(param0Value instanceof String)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (!(param1Value instanceof Number)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (!(param2Value instanceof Number)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (!(param3Value instanceof String)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String str = (String)param0Value;
        final int pos = ((Number)param1Value).intValue();
        final int len = ((Number)param2Value).intValue();
        final String newstr = (String)param3Value;
        if (pos <= 0) {
            return str;
        }
        if (pos == 1) {
            if (len > str.length()) {
                return newstr;
            }
            return newstr + str.substring(len);
        }
        else {
            final String first = str.substring(0, pos - 1);
            if (pos + len - 1 > str.length()) {
                return first + newstr;
            }
            return first + newstr + str.substring(pos + len - 1);
        }
    }
    
    static {
        instance = new Insert();
    }
}
