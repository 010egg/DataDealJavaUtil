// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

public interface PrintableVisitor extends SQLASTVisitor
{
    boolean isUppCase();
    
    void print(final char p0);
    
    void print(final String p0);
}
