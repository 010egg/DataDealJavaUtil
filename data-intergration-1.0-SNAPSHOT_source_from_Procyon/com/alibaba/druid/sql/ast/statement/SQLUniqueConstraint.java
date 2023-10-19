// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.List;

public interface SQLUniqueConstraint extends SQLConstraint
{
    List<SQLSelectOrderByItem> getColumns();
    
    boolean containsColumn(final String p0);
    
    boolean containsColumn(final long p0);
}
