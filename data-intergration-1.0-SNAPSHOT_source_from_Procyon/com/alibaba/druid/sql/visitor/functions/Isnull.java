// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Isnull implements Function
{
    public static final Isnull instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr condition = arguments.get(0);
        condition.accept(visitor);
        final Object itemValue = condition.getAttributes().get("eval.value");
        if (itemValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return Boolean.TRUE;
        }
        if (itemValue == null) {
            return null;
        }
        return Boolean.FALSE;
    }
    
    static {
        instance = new Isnull();
    }
}
