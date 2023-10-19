// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor.functions;

import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;

public class Nil implements Function
{
    public static final Nil instance;
    
    @Override
    public Object eval(final SQLEvalVisitor visitor, final SQLMethodInvokeExpr x) {
        return null;
    }
    
    static {
        instance = new Nil();
    }
}
