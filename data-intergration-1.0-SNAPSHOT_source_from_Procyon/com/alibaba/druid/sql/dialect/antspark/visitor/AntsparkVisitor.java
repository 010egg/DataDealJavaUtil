// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.visitor;

import com.alibaba.druid.sql.dialect.antspark.ast.AntsparkCreateTableStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface AntsparkVisitor extends SQLASTVisitor
{
    boolean visit(final AntsparkCreateTableStatement p0);
    
    void endVisit(final AntsparkCreateTableStatement p0);
}
