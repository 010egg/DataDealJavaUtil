// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.List;
import com.alibaba.druid.sql.visitor.functions.Function;

public interface SQLEvalVisitor extends SQLASTVisitor
{
    public static final String EVAL_VALUE = "eval.value";
    public static final String EVAL_EXPR = "eval.expr";
    public static final Object EVAL_ERROR = new Object();
    public static final Object EVAL_VALUE_COUNT = new Object();
    public static final Object EVAL_VALUE_NULL = new Object();
    
    Function getFunction(final String p0);
    
    void registerFunction(final String p0, final Function p1);
    
    void unregisterFunction(final String p0);
    
    List<Object> getParameters();
    
    void setParameters(final List<Object> p0);
    
    int incrementAndGetVariantIndex();
    
    boolean isMarkVariantIndex();
    
    void setMarkVariantIndex(final boolean p0);
}
