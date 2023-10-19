// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.visitor;

import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerWaitForStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface SQLServerASTVisitor extends SQLASTVisitor
{
    default boolean visit(final SQLServerSelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final SQLServerSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final SQLServerTop x) {
        return true;
    }
    
    default void endVisit(final SQLServerTop x) {
    }
    
    default boolean visit(final SQLServerObjectReferenceExpr x) {
        return true;
    }
    
    default void endVisit(final SQLServerObjectReferenceExpr x) {
    }
    
    default boolean visit(final SQLServerInsertStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final SQLServerInsertStatement x) {
        this.endVisit(x);
    }
    
    default boolean visit(final SQLServerUpdateStatement x) {
        return true;
    }
    
    default void endVisit(final SQLServerUpdateStatement x) {
    }
    
    default boolean visit(final SQLServerExecStatement x) {
        return true;
    }
    
    default void endVisit(final SQLServerExecStatement x) {
    }
    
    default boolean visit(final SQLServerSetTransactionIsolationLevelStatement x) {
        return true;
    }
    
    default void endVisit(final SQLServerSetTransactionIsolationLevelStatement x) {
    }
    
    default boolean visit(final SQLServerOutput x) {
        return true;
    }
    
    default void endVisit(final SQLServerOutput x) {
    }
    
    default boolean visit(final SQLServerRollbackStatement x) {
        return true;
    }
    
    default void endVisit(final SQLServerRollbackStatement x) {
    }
    
    default boolean visit(final SQLServerWaitForStatement x) {
        return true;
    }
    
    default void endVisit(final SQLServerWaitForStatement x) {
    }
    
    default boolean visit(final SQLServerExecStatement.SQLServerParameter x) {
        return true;
    }
    
    default void endVisit(final SQLServerExecStatement.SQLServerParameter x) {
    }
}
