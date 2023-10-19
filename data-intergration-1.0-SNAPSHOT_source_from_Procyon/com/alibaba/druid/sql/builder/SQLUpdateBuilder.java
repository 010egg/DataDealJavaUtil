// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

public interface SQLUpdateBuilder
{
    SQLUpdateBuilder from(final String p0);
    
    SQLUpdateBuilder from(final String p0, final String p1);
    
    SQLUpdateBuilder limit(final int p0);
    
    SQLUpdateBuilder limit(final int p0, final int p1);
    
    SQLUpdateBuilder where(final String p0);
    
    SQLUpdateBuilder whereAnd(final String p0);
    
    SQLUpdateBuilder whereOr(final String p0);
    
    SQLUpdateBuilder set(final String... p0);
}
