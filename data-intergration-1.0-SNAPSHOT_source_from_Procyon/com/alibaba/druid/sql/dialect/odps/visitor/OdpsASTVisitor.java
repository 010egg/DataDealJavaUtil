// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.visitor;

import com.alibaba.druid.sql.dialect.odps.ast.OdpsUndoTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRestoreStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetFileFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUnloadStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddAccountProviderStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsDeclareVariableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsInstallPackageStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsNewExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsExstoreStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsTransformExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsQueryAliasStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCountStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetChangeLogs;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddFileStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsGrantStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsListStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsShowChangelogsStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsShowGrantsStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsReadStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsStatisticClause;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveStatisticStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddStatisticStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSetLabelStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUDTFSQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;

public interface OdpsASTVisitor extends HiveASTVisitor
{
    default void endVisit(final OdpsCreateTableStatement x) {
        this.endVisit((SQLCreateTableStatement)x);
    }
    
    default boolean visit(final OdpsCreateTableStatement x) {
        return this.visit((SQLCreateTableStatement)x);
    }
    
    default void endVisit(final OdpsUDTFSQLSelectItem x) {
    }
    
    default boolean visit(final OdpsUDTFSQLSelectItem x) {
        return true;
    }
    
    default void endVisit(final OdpsSetLabelStatement x) {
    }
    
    default boolean visit(final OdpsSetLabelStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OdpsSelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final OdpsAddStatisticStatement x) {
    }
    
    default boolean visit(final OdpsAddStatisticStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsRemoveStatisticStatement x) {
    }
    
    default boolean visit(final OdpsRemoveStatisticStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.TableCount x) {
    }
    
    default boolean visit(final OdpsStatisticClause.TableCount x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.ExpressionCondition x) {
    }
    
    default boolean visit(final OdpsStatisticClause.ExpressionCondition x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.NullValue x) {
    }
    
    default boolean visit(final OdpsStatisticClause.NullValue x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.DistinctValue x) {
    }
    
    default boolean visit(final OdpsStatisticClause.DistinctValue x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.ColumnSum x) {
    }
    
    default boolean visit(final OdpsStatisticClause.ColumnSum x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.ColumnMax x) {
    }
    
    default boolean visit(final OdpsStatisticClause.ColumnMax x) {
        return true;
    }
    
    default void endVisit(final OdpsStatisticClause.ColumnMin x) {
    }
    
    default boolean visit(final OdpsStatisticClause.ColumnMin x) {
        return true;
    }
    
    default void endVisit(final OdpsReadStatement x) {
    }
    
    default boolean visit(final OdpsReadStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsShowGrantsStmt x) {
    }
    
    default boolean visit(final OdpsShowGrantsStmt x) {
        return true;
    }
    
    default void endVisit(final OdpsShowChangelogsStatement x) {
    }
    
    default boolean visit(final OdpsShowChangelogsStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsListStmt x) {
    }
    
    default boolean visit(final OdpsListStmt x) {
        return true;
    }
    
    default void endVisit(final OdpsGrantStmt x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OdpsGrantStmt x) {
        return this.visit(x);
    }
    
    default boolean visit(final OdpsAddTableStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsAddTableStatement x) {
    }
    
    default boolean visit(final OdpsAddFileStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsAddFileStatement x) {
    }
    
    default boolean visit(final OdpsAddUserStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsAddUserStatement x) {
    }
    
    default boolean visit(final OdpsRemoveUserStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsRemoveUserStatement x) {
    }
    
    default boolean visit(final OdpsAlterTableSetChangeLogs x) {
        return true;
    }
    
    default void endVisit(final OdpsAlterTableSetChangeLogs x) {
    }
    
    default boolean visit(final OdpsCountStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsCountStatement x) {
    }
    
    default boolean visit(final OdpsQueryAliasStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsQueryAliasStatement x) {
    }
    
    default boolean visit(final OdpsTransformExpr x) {
        return true;
    }
    
    default void endVisit(final OdpsTransformExpr x) {
    }
    
    default boolean visit(final OdpsExstoreStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsExstoreStatement x) {
    }
    
    default boolean visit(final OdpsNewExpr x) {
        return true;
    }
    
    default void endVisit(final OdpsNewExpr x) {
    }
    
    default boolean visit(final OdpsInstallPackageStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsInstallPackageStatement x) {
    }
    
    default boolean visit(final OdpsDeclareVariableStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsDeclareVariableStatement x) {
    }
    
    default boolean visit(final OdpsAddAccountProviderStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsAddAccountProviderStatement x) {
    }
    
    default boolean visit(final OdpsUnloadStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsUnloadStatement x) {
    }
    
    default boolean visit(final OdpsAlterTableSetFileFormat x) {
        return true;
    }
    
    default void endVisit(final OdpsAlterTableSetFileFormat x) {
    }
    
    default boolean visit(final OdpsRestoreStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsRestoreStatement x) {
    }
    
    default boolean visit(final OdpsUndoTableStatement x) {
        return true;
    }
    
    default void endVisit(final OdpsUndoTableStatement x) {
    }
}
