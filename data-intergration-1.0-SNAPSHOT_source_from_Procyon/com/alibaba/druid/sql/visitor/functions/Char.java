// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.Iterator;
import java.math.BigDecimal;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Char implements Function
{
    public static final Char instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final StringBuffer buf = new StringBuffer(x.getArguments().size());
        for (final SQLExpr param : x.getArguments()) {
            param.accept(visitor);
            final Object paramValue = param.getAttributes().get("eval.value");
            if (paramValue instanceof Number) {
                final int charCode = ((Number)paramValue).intValue();
                buf.append((char)charCode);
            }
            else {
                if (!(paramValue instanceof String)) {
                    return SQLEvalVisitor.EVAL_ERROR;
                }
                try {
                    final int charCode = new BigDecimal((String)paramValue).intValue();
                    buf.append((char)charCode);
                }
                catch (NumberFormatException ex) {}
            }
        }
        return buf.toString();
    }
    
    static {
        instance = new Char();
    }
}
