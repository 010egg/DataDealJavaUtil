// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

public enum AutoIncrementType
{
    GROUP("GROUP"), 
    SIMPLE("SIMPLE"), 
    SIMPLE_CACHE("SIMPLE WITH CACHE"), 
    TIME("TIME");
    
    private final String keyword;
    
    public String getKeyword() {
        return this.keyword;
    }
    
    private AutoIncrementType(final String keyword) {
        this.keyword = keyword;
    }
}
