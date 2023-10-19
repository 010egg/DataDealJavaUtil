// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

public enum SQLOrderingSpecification
{
    ASC("ASC"), 
    DESC("DESC");
    
    public final String name;
    public final String name_lcase;
    
    private SQLOrderingSpecification(final String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
}
