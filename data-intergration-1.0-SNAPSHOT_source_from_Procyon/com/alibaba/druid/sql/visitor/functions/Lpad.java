// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Lpad implements Function
{
    public static final Lpad instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> params = x.getArguments();
        final int paramSize = params.size();
        if (paramSize != 3) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = params.get(0);
        final SQLExpr param2 = params.get(1);
        final SQLExpr param3 = params.get(2);
        param0.accept(visitor);
        param2.accept(visitor);
        param3.accept(visitor);
        final Object param0Value = param0.getAttributes().get("eval.value");
        final Object param1Value = param2.getAttributes().get("eval.value");
        final Object param2Value = param3.getAttributes().get("eval.value");
        if (param0Value == null || param1Value == null || param2Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String strValue0 = param0Value.toString();
        final int len = ((Number)param1Value).intValue();
        final String strValue2 = param2Value.toString();
        String result = strValue0;
        if (result.length() > len) {
            return result.substring(0, len);
        }
        while (result.length() < len) {
            result = strValue2 + result;
        }
        return result;
    }
    
    static {
        instance = new Lpad();
    }
}
