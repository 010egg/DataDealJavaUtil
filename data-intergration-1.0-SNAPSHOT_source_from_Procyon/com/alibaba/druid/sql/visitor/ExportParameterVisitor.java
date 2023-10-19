// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.List;

public interface ExportParameterVisitor extends SQLASTVisitor
{
    boolean isParameterizedMergeInList();
    
    void setParameterizedMergeInList(final boolean p0);
    
    List<Object> getParameters();
}
