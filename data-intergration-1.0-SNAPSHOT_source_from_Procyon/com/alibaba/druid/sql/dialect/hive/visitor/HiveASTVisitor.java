// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.visitor;

import com.alibaba.druid.sql.dialect.hive.stmt.HiveMsckRepairStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface HiveASTVisitor extends SQLASTVisitor
{
    default boolean visit(final HiveInsert x) {
        return true;
    }
    
    default void endVisit(final HiveInsert x) {
    }
    
    default boolean visit(final HiveMultiInsertStatement x) {
        return true;
    }
    
    default void endVisit(final HiveMultiInsertStatement x) {
    }
    
    default boolean visit(final HiveInsertStatement x) {
        return true;
    }
    
    default void endVisit(final HiveInsertStatement x) {
    }
    
    default boolean visit(final HiveCreateFunctionStatement x) {
        return true;
    }
    
    default void endVisit(final HiveCreateFunctionStatement x) {
    }
    
    default boolean visit(final HiveLoadDataStatement x) {
        return true;
    }
    
    default void endVisit(final HiveLoadDataStatement x) {
    }
    
    default boolean visit(final HiveMsckRepairStatement x) {
        return true;
    }
    
    default void endVisit(final HiveMsckRepairStatement x) {
    }
}
