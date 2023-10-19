// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.Date;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class ToChar implements Function
{
    public static final ToChar instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() != 2) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        for (final SQLExpr arg : arguments) {
            arg.accept(visitor);
        }
        final Object v0 = arguments.get(0).getAttributes().get("eval.value");
        final Object v2 = arguments.get(1).getAttributes().get("eval.value");
        if (v0 instanceof Date && v2 instanceof String) {
            final Date date = (Date)v0;
            final String format = arguments.get(1).getText();
            if (format.equals("yyyymmdd")) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                return dateFormat.format(date);
            }
        }
        return null;
    }
    
    static {
        instance = new ToChar();
    }
}
