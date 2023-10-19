// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class ToDate implements Function
{
    public static final ToDate instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (arguments.size() == 2 && arguments.get(0) instanceof SQLCharExpr && arguments.get(1) instanceof SQLCharExpr) {
            final String chars = arguments.get(0).getText();
            final String format = arguments.get(1).getText();
            if (format.equals("yyyymmdd")) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                try {
                    return dateFormat.parse(chars);
                }
                catch (ParseException e) {
                    return false;
                }
            }
        }
        return null;
    }
    
    static {
        instance = new ToDate();
    }
}
