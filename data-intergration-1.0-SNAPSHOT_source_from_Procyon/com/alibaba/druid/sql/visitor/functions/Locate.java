// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Locate implements Function
{
    public static final Locate instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        final List<SQLExpr> params = x.getArguments();
        final int paramSize = params.size();
        if (paramSize != 2 && paramSize != 3) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final SQLExpr param0 = params.get(0);
        final SQLExpr param2 = params.get(1);
        SQLExpr param3 = null;
        param0.accept(visitor);
        param2.accept(visitor);
        if (paramSize == 3) {
            param3 = params.get(2);
            param3.accept(visitor);
        }
        final Object param0Value = param0.getAttributes().get("eval.value");
        final Object param1Value = param2.getAttributes().get("eval.value");
        if (param0Value == null || param1Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        final String strValue0 = param0Value.toString();
        final String strValue2 = param1Value.toString();
        if (paramSize == 2) {
            final int result = strValue2.indexOf(strValue0) + 1;
            return result;
        }
        final Object param2Value = param3.getAttributes().get("eval.value");
        final int start = ((Number)param2Value).intValue();
        final int result2 = strValue2.indexOf(strValue0, start + 1) + 1;
        return result2;
    }
    
    static {
        instance = new Locate();
    }
}
