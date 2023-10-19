// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.List;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class If implements Function
{
    public static final If instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr condition = arguments.get(0);
        condition.accept(visitor);
        final Object itemValue = condition.getAttributes().get("eval.value");
        if (itemValue == null) {
            return null;
        }
        if (Boolean.TRUE == itemValue || !SQLEvalVisitorUtils.eq(itemValue, 0)) {
            final SQLExpr trueExpr = arguments.get(1);
            trueExpr.accept(visitor);
            return trueExpr.getAttributes().get("eval.value");
        }
        final SQLExpr falseExpr = arguments.get(2);
        falseExpr.accept(visitor);
        return falseExpr.getAttributes().get("eval.value");
    }
    
    static {
        instance = new If();
    }
}
