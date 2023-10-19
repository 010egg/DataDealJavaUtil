// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.List;
import com.alibaba.druid.DbType;

public interface ParameterizedVisitor extends PrintableVisitor
{
    int getReplaceCount();
    
    void incrementReplaceCunt();
    
    DbType getDbType();
    
    void setOutputParameters(final List<Object> p0);
    
    void config(final VisitorFeature p0, final boolean p1);
    
    boolean isEnabled(final VisitorFeature p0);
}
