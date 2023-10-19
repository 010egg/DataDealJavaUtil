// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;

public interface SQLSelectBuilder
{
    SQLSelectStatement getSQLSelectStatement();
    
    SQLSelectBuilder select(final String... p0);
    
    SQLSelectBuilder selectWithAlias(final String p0, final String p1);
    
    SQLSelectBuilder from(final String p0);
    
    SQLSelectBuilder from(final String p0, final String p1);
    
    SQLSelectBuilder orderBy(final String... p0);
    
    SQLSelectBuilder groupBy(final String p0);
    
    SQLSelectBuilder having(final String p0);
    
    SQLSelectBuilder into(final String p0);
    
    SQLSelectBuilder limit(final int p0);
    
    SQLSelectBuilder limit(final int p0, final int p1);
    
    SQLSelectBuilder where(final String p0);
    
    SQLSelectBuilder whereAnd(final String p0);
    
    SQLSelectBuilder whereOr(final String p0);
    
    String toString();
}
