// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.visitor;

import com.alibaba.druid.sql.dialect.clickhouse.ast.ClickhouseCreateTableStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface ClickhouseVisitor extends SQLASTVisitor
{
    default boolean visit(final ClickhouseCreateTableStatement x) {
        return true;
    }
    
    default void endVisit(final ClickhouseCreateTableStatement x) {
    }
}
