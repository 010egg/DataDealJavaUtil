// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Instr implements Function
{
    public static final Instr instance;
    
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
        final String strValue0 = param0Value.toString();
        final String strValue2 = param1Value.toString();
        final int result = strValue0.indexOf(strValue2) + 1;
        return result;
    }
    
    static {
        instance = new Instr();
    }
}
