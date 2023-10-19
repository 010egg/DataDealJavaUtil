// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

public interface UpdateBuilder
{
    UpdateBuilder from(final String p0);
    
    UpdateBuilder from(final String p0, final String p1);
    
    UpdateBuilder limit(final int p0);
    
    UpdateBuilder limit(final int p0, final int p1);
    
    UpdateBuilder where(final String p0);
    
    UpdateBuilder whereAnd(final String p0);
    
    UpdateBuilder whereOr(final String p0);
}
