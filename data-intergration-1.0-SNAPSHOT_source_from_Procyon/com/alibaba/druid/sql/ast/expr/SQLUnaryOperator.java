// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

public enum SQLUnaryOperator
{
    Plus("+"), 
    Negative("-"), 
    Not("!"), 
    Compl("~"), 
    Prior("PRIOR"), 
    ConnectByRoot("CONNECT BY"), 
    BINARY("BINARY"), 
    RAW("RAW"), 
    NOT("NOT"), 
    Pound("#");
    
    public final String name;
    
    private SQLUnaryOperator(final String name) {
        this.name = name;
    }
}
