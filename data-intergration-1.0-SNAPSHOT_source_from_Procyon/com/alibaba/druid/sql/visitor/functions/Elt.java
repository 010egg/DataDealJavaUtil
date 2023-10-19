// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Elt implements Function
{
    public static final Elt instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() <= 1) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = x.getArguments().get(0);
        param0.accept(visitor);
        final Object param0Value = param0.getAttribute("eval.value");
        if (!(param0Value instanceof Number)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final int param0IntValue = ((Number)param0Value).intValue();
        if (param0IntValue >= x.getArguments().size()) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        final SQLExpr item = x.getArguments().get(param0IntValue);
        item.accept(visitor);
        final Object itemValue = item.getAttributes().get("eval.value");
        return itemValue;
    }
    
    static {
        instance = new Elt();
    }
}
