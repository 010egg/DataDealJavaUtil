// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.visitor;

import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGAlterSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDropSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGCreateSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGConnectToStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGLineSegmentsExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCircleExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPolygonExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCidrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGInetExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGMacAddrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPointExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGBoxExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGExtractExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGFunctionTableSource;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface PGASTVisitor extends SQLASTVisitor
{
    default void endVisit(final PGSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final PGSelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final PGSelectQueryBlock.FetchClause x) {
    }
    
    default boolean visit(final PGSelectQueryBlock.FetchClause x) {
        return true;
    }
    
    default void endVisit(final PGSelectQueryBlock.ForClause x) {
    }
    
    default boolean visit(final PGSelectQueryBlock.ForClause x) {
        return true;
    }
    
    default void endVisit(final PGDeleteStatement x) {
    }
    
    default boolean visit(final PGDeleteStatement x) {
        return true;
    }
    
    default void endVisit(final PGInsertStatement x) {
    }
    
    default boolean visit(final PGInsertStatement x) {
        return true;
    }
    
    default void endVisit(final PGSelectStatement x) {
        this.endVisit(x);
    }
    
    default boolean visit(final PGSelectStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final PGUpdateStatement x) {
    }
    
    default boolean visit(final PGUpdateStatement x) {
        return true;
    }
    
    default void endVisit(final PGFunctionTableSource x) {
    }
    
    default boolean visit(final PGFunctionTableSource x) {
        return true;
    }
    
    default void endVisit(final PGTypeCastExpr x) {
    }
    
    default boolean visit(final PGTypeCastExpr x) {
        return true;
    }
    
    default void endVisit(final PGExtractExpr x) {
    }
    
    default boolean visit(final PGExtractExpr x) {
        return true;
    }
    
    default void endVisit(final PGBoxExpr x) {
    }
    
    default boolean visit(final PGBoxExpr x) {
        return true;
    }
    
    default void endVisit(final PGPointExpr x) {
    }
    
    default boolean visit(final PGPointExpr x) {
        return true;
    }
    
    default void endVisit(final PGMacAddrExpr x) {
    }
    
    default boolean visit(final PGMacAddrExpr x) {
        return true;
    }
    
    default void endVisit(final PGInetExpr x) {
    }
    
    default boolean visit(final PGInetExpr x) {
        return true;
    }
    
    default void endVisit(final PGCidrExpr x) {
    }
    
    default boolean visit(final PGCidrExpr x) {
        return true;
    }
    
    default void endVisit(final PGPolygonExpr x) {
    }
    
    default boolean visit(final PGPolygonExpr x) {
        return true;
    }
    
    default void endVisit(final PGCircleExpr x) {
    }
    
    default boolean visit(final PGCircleExpr x) {
        return true;
    }
    
    default void endVisit(final PGLineSegmentsExpr x) {
    }
    
    default boolean visit(final PGLineSegmentsExpr x) {
        return true;
    }
    
    default void endVisit(final PGShowStatement x) {
    }
    
    default boolean visit(final PGShowStatement x) {
        return true;
    }
    
    default void endVisit(final PGStartTransactionStatement x) {
    }
    
    default boolean visit(final PGStartTransactionStatement x) {
        return true;
    }
    
    default void endVisit(final PGConnectToStatement x) {
    }
    
    default boolean visit(final PGConnectToStatement x) {
        return true;
    }
    
    default void endVisit(final PGCreateSchemaStatement x) {
    }
    
    default boolean visit(final PGCreateSchemaStatement x) {
        return true;
    }
    
    default void endVisit(final PGDropSchemaStatement x) {
    }
    
    default boolean visit(final PGDropSchemaStatement x) {
        return true;
    }
    
    default void endVisit(final PGAlterSchemaStatement x) {
    }
    
    default boolean visit(final PGAlterSchemaStatement x) {
        return true;
    }
}
