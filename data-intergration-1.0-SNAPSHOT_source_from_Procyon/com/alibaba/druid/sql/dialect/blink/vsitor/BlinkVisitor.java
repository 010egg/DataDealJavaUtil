// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.vsitor;

import com.alibaba.druid.sql.dialect.blink.ast.BlinkCreateTableStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface BlinkVisitor extends SQLASTVisitor
{
    boolean visit(final BlinkCreateTableStatement p0);
    
    void endVisit(final BlinkCreateTableStatement p0);
}
