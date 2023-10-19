// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class IfNull implements Function
{
    public static final IfNull instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() != 2) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr condition = arguments.get(0);
        condition.accept(visitor);
        final Object itemValue = condition.getAttributes().get("eval.value");
        if (itemValue == null) {
            final SQLExpr valueExpr = arguments.get(1);
            valueExpr.accept(visitor);
            return valueExpr.getAttributes().get("eval.value");
        }
        return itemValue;
    }
    
    public Object eval(final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() != 2) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr condition = arguments.get(0);
        final SQLExpr valueExpr = arguments.get(1);
        if (!(condition instanceof SQLValuableExpr) || !(valueExpr instanceof SQLValuableExpr)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final Object itemValue = ((SQLValuableExpr)condition).getValue();
        if (itemValue == null || itemValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return ((SQLValuableExpr)valueExpr).getValue();
        }
        return itemValue;
    }
    
    static {
        instance = new IfNull();
    }
}
