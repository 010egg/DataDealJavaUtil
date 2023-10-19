// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.io.UnsupportedEncodingException;
import com.alibaba.druid.util.HexBin;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Unhex implements Function
{
    public static final Unhex instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() != 1) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = x.getArguments().get(0);
        if (param0 instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr paramMethodExpr = (SQLMethodInvokeExpr)param0;
            if (paramMethodExpr.getMethodName().equalsIgnoreCase("hex")) {
                final SQLExpr subParamExpr = paramMethodExpr.getArguments().get(0);
                subParamExpr.accept(visitor);
                final Object param0Value = subParamExpr.getAttributes().get("eval.value");
                if (param0Value == null) {
                    x.putAttribute("eval.expr", subParamExpr);
                    return SQLEvalVisitor.EVAL_ERROR;
                }
                return param0Value;
            }
        }
        param0.accept(visitor);
        final Object param0Value2 = param0.getAttributes().get("eval.value");
        if (param0Value2 == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (!(param0Value2 instanceof String)) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final byte[] bytes = HexBin.decode((String)param0Value2);
        if (bytes == null) {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
        String result;
        try {
            result = new String(bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return result;
    }
    
    static {
        instance = new Unhex();
    }
}
