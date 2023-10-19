// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Concat implements Function
{
    public static final Concat instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final StringBuilder buf = new StringBuilder();
        for (final SQLExpr item : x.getArguments()) {
            item.accept(visitor);
            final Object itemValue = item.getAttribute("eval.value");
            if (itemValue == null) {
                return null;
            }
            buf.append(itemValue.toString());
        }
        return buf.toString();
    }
    
    public Object eval(final SQLMethodInvokeExpr x) {
        final StringBuilder buf = new StringBuilder();
        for (final SQLExpr param : x.getArguments()) {
            if (param instanceof SQLValuableExpr) {
                final Object val = ((SQLValuableExpr)param).getValue();
                if (val instanceof String) {
                    buf.append(val);
                    continue;
                }
                if (val instanceof Integer) {
                    buf.append(val);
                    continue;
                }
            }
            return SQLEvalVisitor.EVAL_ERROR;
        }
        return buf.toString();
    }
    
    static {
        instance = new Concat();
    }
}
