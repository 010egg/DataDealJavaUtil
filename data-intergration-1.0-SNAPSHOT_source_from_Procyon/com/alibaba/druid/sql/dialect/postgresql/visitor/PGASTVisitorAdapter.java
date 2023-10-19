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
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class PGASTVisitorAdapter extends SQLASTVisitorAdapter implements PGASTVisitor
{
    @Override
    public void endVisit(final PGSelectQueryBlock.FetchClause x) {
    }
    
    @Override
    public boolean visit(final PGSelectQueryBlock.FetchClause x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGSelectQueryBlock.ForClause x) {
    }
    
    @Override
    public boolean visit(final PGSelectQueryBlock.ForClause x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGDeleteStatement x) {
    }
    
    @Override
    public boolean visit(final PGDeleteStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGInsertStatement x) {
    }
    
    @Override
    public boolean visit(final PGInsertStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGUpdateStatement x) {
    }
    
    @Override
    public boolean visit(final PGUpdateStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGFunctionTableSource x) {
    }
    
    @Override
    public boolean visit(final PGFunctionTableSource x) {
        return true;
    }
    
    @Override
    public boolean visit(final PGTypeCastExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGTypeCastExpr x) {
    }
    
    @Override
    public void endVisit(final PGExtractExpr x) {
    }
    
    @Override
    public boolean visit(final PGExtractExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGBoxExpr x) {
    }
    
    @Override
    public boolean visit(final PGBoxExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGPointExpr x) {
    }
    
    @Override
    public boolean visit(final PGPointExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGMacAddrExpr x) {
    }
    
    @Override
    public boolean visit(final PGMacAddrExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGInetExpr x) {
    }
    
    @Override
    public boolean visit(final PGInetExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGCidrExpr x) {
    }
    
    @Override
    public boolean visit(final PGCidrExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGPolygonExpr x) {
    }
    
    @Override
    public boolean visit(final PGPolygonExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGCircleExpr x) {
    }
    
    @Override
    public boolean visit(final PGCircleExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGLineSegmentsExpr x) {
    }
    
    @Override
    public boolean visit(final PGLineSegmentsExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGShowStatement x) {
    }
    
    @Override
    public boolean visit(final PGShowStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGStartTransactionStatement x) {
    }
    
    @Override
    public boolean visit(final PGStartTransactionStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGConnectToStatement x) {
    }
    
    @Override
    public boolean visit(final PGConnectToStatement x) {
        return true;
    }
    
    @Override
    public void endVisit(final PGCreateSchemaStatement x) {
    }
    
    @Override
    public boolean visit(final PGCreateSchemaStatement x) {
        return false;
    }
    
    @Override
    public void endVisit(final PGDropSchemaStatement x) {
    }
    
    @Override
    public boolean visit(final PGDropSchemaStatement x) {
        return false;
    }
    
    @Override
    public void endVisit(final PGAlterSchemaStatement x) {
    }
    
    @Override
    public boolean visit(final PGAlterSchemaStatement x) {
        return false;
    }
}
