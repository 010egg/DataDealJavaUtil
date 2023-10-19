// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.util.HexBin;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Hex implements Function
{
    public static final Hex instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        if (x.getArguments().size() != 1) {
            throw new ParserException("argument's != 1, " + x.getArguments().size());
        }
        final SQLExpr param0 = x.getArguments().get(0);
        param0.accept(visitor);
        final Object param0Value = param0.getAttributes().get("eval.value");
        if (param0Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        if (param0Value instanceof String) {
            final byte[] bytes = ((String)param0Value).getBytes();
            final String result = HexBin.encode(bytes);
            return result;
        }
        if (param0Value instanceof Number) {
            final long value = ((Number)param0Value).longValue();
            final String result2 = Long.toHexString(value).toUpperCase();
            return result2;
        }
        return SQLEvalVisitor.EVAL_ERROR;
    }
    
    static {
        instance = new Hex();
    }
}
