// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Left implements Function
{
    public static final Left instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() != 2) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = x.getArguments().get(0);
        final SQLExpr param2 = x.getArguments().get(1);
        param0.accept(visitor);
        param2.accept(visitor);
        final Object param0Value = param0.getAttributes().get("eval.value");
        final Object param1Value = param2.getAttributes().get("eval.value");
        if (param0Value == null || param1Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String strValue = param0Value.toString();
        final int intValue = SQLEvalVisitorUtils.castToInteger(param1Value);
        if (intValue > strValue.length()) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String result = strValue.substring(0, intValue);
        return result;
    }
    
    static {
        instance = new Left();
    }
}
