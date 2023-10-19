// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Least implements Function
{
    public static final Least instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        Object result = null;
        for (final SQLExpr item : x.getArguments()) {
            item.accept(visitor);
            final Object itemValue = item.getAttributes().get("eval.value");
            if (result == null) {
                result = itemValue;
            }
            else {
                if (!SQLEvalVisitorUtils.lt(itemValue, result)) {
                    continue;
                }
                result = itemValue;
            }
        }
        return result;
    }
    
    public Object eval(final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() > 0) {
            final SQLExpr p0 = arguments.get(0);
            if (p0 instanceof SQLIntegerExpr && ((SQLIntegerExpr)p0).getNumber() instanceof Integer) {
                int val = ((SQLIntegerExpr)p0).getNumber().intValue();
                for (int i = 1; i < arguments.size(); ++i) {
                    final SQLExpr param = arguments.get(i);
                    if (!(param instanceof SQLIntegerExpr) || !(((SQLIntegerExpr)param).getNumber() instanceof Integer)) {
                        return SQLEvalVisitor.EVAL_ERROR;
                    }
                    final int paramVal = ((SQLIntegerExpr)param).getNumber().intValue();
                    if (paramVal < val) {
                        val = paramVal;
                    }
                }
                return val;
            }
        }
        return SQLEvalVisitor.EVAL_ERROR;
    }
    
    static {
        instance = new Least();
    }
}
