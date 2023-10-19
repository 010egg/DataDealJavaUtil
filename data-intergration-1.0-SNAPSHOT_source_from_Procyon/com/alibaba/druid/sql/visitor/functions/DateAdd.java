// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class DateAdd implements Function
{
    public static final DateAdd instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() != 3) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        for (final SQLExpr arg : arguments) {
            arg.accept(visitor);
        }
        final Object v0 = arguments.get(0).getAttributes().get("eval.value");
        final Object v2 = arguments.get(1).getAttributes().get("eval.value");
        final Object v3 = arguments.get(2).getAttributes().get("eval.value");
        if (v0 instanceof Date && v2 instanceof Integer && v3 instanceof String) {
            final Date date = (Date)v0;
            final int delta = (int)v2;
            if ("day".equalsIgnoreCase((String)v3)) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(5, delta);
                return calendar.getTime();
            }
        }
        final SQLExpr arg2 = arguments.get(0);
        arg2.accept(visitor);
        final Object itemValue = arg2.getAttributes().get("eval.value");
        if (itemValue == null) {
            return null;
        }
        return null;
    }
    
    static {
        instance = new DateAdd();
    }
}
