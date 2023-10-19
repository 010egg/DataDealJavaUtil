// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Ascii implements Function
{
    public static final Ascii instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param = x.getArguments().get(0);
        param.accept(visitor);
        final Object paramValue = param.getAttributes().get("eval.value");
        if (paramValue == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (paramValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        final String strValue = paramValue.toString();
        if (strValue.length() == 0) {
            return 0;
        }
        final int ascii = strValue.charAt(0);
        return ascii;
    }
    
    static {
        instance = new Ascii();
    }
}
