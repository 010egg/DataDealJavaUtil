// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

public interface SQLDeleteBuilder
{
    SQLDeleteBuilder from(final String p0);
    
    SQLDeleteBuilder from(final String p0, final String p1);
    
    SQLDeleteBuilder limit(final int p0);
    
    SQLDeleteBuilder limit(final int p0, final int p1);
    
    SQLDeleteBuilder where(final String p0);
    
    SQLDeleteBuilder whereAnd(final String p0);
    
    SQLDeleteBuilder whereOr(final String p0);
}
