// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;

public interface SQLName extends SQLExpr
{
    String getSimpleName();
    
    SQLName clone();
    
    long nameHashCode64();
    
    long hashCode64();
    
    SQLColumnDefinition getResolvedColumn();
}
