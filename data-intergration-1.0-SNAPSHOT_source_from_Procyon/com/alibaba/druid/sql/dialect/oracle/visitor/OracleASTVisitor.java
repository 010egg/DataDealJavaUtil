// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleXmlColumnProperties;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRunStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsOfTypeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePipeRowStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTypeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleTreatExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalLogGrp;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalIdKey;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRaiseStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleContinueStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface OracleASTVisitor extends SQLASTVisitor
{
    default void endVisit(final OracleAnalytic x) {
    }
    
    default void endVisit(final OracleAnalyticWindowing x) {
    }
    
    default void endVisit(final OracleDeleteStatement x) {
        this.endVisit(x);
    }
    
    default void endVisit(final OracleIntervalExpr x) {
    }
    
    default void endVisit(final OracleOuterExpr x) {
    }
    
    default void endVisit(final OracleSelectJoin x) {
    }
    
    default void endVisit(final OracleSelectPivot x) {
    }
    
    default void endVisit(final OracleSelectPivot.Item x) {
    }
    
    default void endVisit(final OracleSelectRestriction.CheckOption x) {
    }
    
    default void endVisit(final OracleSelectRestriction.ReadOnly x) {
    }
    
    default void endVisit(final OracleSelectSubqueryTableSource x) {
    }
    
    default void endVisit(final OracleSelectUnPivot x) {
    }
    
    default void endVisit(final OracleUpdateStatement x) {
    }
    
    default boolean visit(final OracleAnalytic x) {
        return true;
    }
    
    default boolean visit(final OracleAnalyticWindowing x) {
        return true;
    }
    
    default boolean visit(final OracleDeleteStatement x) {
        return this.visit(x);
    }
    
    default boolean visit(final OracleIntervalExpr x) {
        return true;
    }
    
    default boolean visit(final OracleOuterExpr x) {
        return true;
    }
    
    default boolean visit(final OracleSelectJoin x) {
        return true;
    }
    
    default boolean visit(final OracleSelectPivot x) {
        return true;
    }
    
    default boolean visit(final OracleSelectPivot.Item x) {
        return true;
    }
    
    default boolean visit(final OracleSelectRestriction.CheckOption x) {
        return true;
    }
    
    default boolean visit(final OracleSelectRestriction.ReadOnly x) {
        return true;
    }
    
    default boolean visit(final OracleSelectSubqueryTableSource x) {
        return true;
    }
    
    default boolean visit(final OracleSelectUnPivot x) {
        return true;
    }
    
    default boolean visit(final OracleUpdateStatement x) {
        return this.visit(x);
    }
    
    default boolean visit(final SampleClause x) {
        return true;
    }
    
    default void endVisit(final SampleClause x) {
    }
    
    default boolean visit(final OracleSelectTableReference x) {
        return true;
    }
    
    default void endVisit(final OracleSelectTableReference x) {
    }
    
    default boolean visit(final PartitionExtensionClause x) {
        return true;
    }
    
    default void endVisit(final PartitionExtensionClause x) {
    }
    
    default boolean visit(final OracleWithSubqueryEntry x) {
        return true;
    }
    
    default void endVisit(final OracleWithSubqueryEntry x) {
    }
    
    default boolean visit(final SearchClause x) {
        return true;
    }
    
    default void endVisit(final SearchClause x) {
    }
    
    default boolean visit(final CycleClause x) {
        return true;
    }
    
    default void endVisit(final CycleClause x) {
    }
    
    default boolean visit(final OracleBinaryFloatExpr x) {
        return true;
    }
    
    default void endVisit(final OracleBinaryFloatExpr x) {
    }
    
    default boolean visit(final OracleBinaryDoubleExpr x) {
        return true;
    }
    
    default void endVisit(final OracleBinaryDoubleExpr x) {
    }
    
    default boolean visit(final OracleCursorExpr x) {
        return true;
    }
    
    default void endVisit(final OracleCursorExpr x) {
    }
    
    default boolean visit(final OracleIsSetExpr x) {
        return true;
    }
    
    default void endVisit(final OracleIsSetExpr x) {
    }
    
    default boolean visit(final ModelClause.ReturnRowsClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause.ReturnRowsClause x) {
    }
    
    default boolean visit(final ModelClause.MainModelClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause.MainModelClause x) {
    }
    
    default boolean visit(final ModelClause.ModelColumnClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause.ModelColumnClause x) {
    }
    
    default boolean visit(final ModelClause.QueryPartitionClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause.QueryPartitionClause x) {
    }
    
    default boolean visit(final ModelClause.ModelColumn x) {
        return true;
    }
    
    default void endVisit(final ModelClause.ModelColumn x) {
    }
    
    default boolean visit(final ModelClause.ModelRulesClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause.ModelRulesClause x) {
    }
    
    default boolean visit(final ModelClause.CellAssignmentItem x) {
        return true;
    }
    
    default void endVisit(final ModelClause.CellAssignmentItem x) {
    }
    
    default boolean visit(final ModelClause.CellAssignment x) {
        return true;
    }
    
    default void endVisit(final ModelClause.CellAssignment x) {
    }
    
    default boolean visit(final ModelClause x) {
        return true;
    }
    
    default void endVisit(final ModelClause x) {
    }
    
    default boolean visit(final OracleReturningClause x) {
        return true;
    }
    
    default void endVisit(final OracleReturningClause x) {
    }
    
    default boolean visit(final OracleInsertStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleInsertStatement x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
        return true;
    }
    
    default void endVisit(final OracleMultiInsertStatement.InsertIntoClause x) {
    }
    
    default boolean visit(final OracleMultiInsertStatement x) {
        return true;
    }
    
    default void endVisit(final OracleMultiInsertStatement x) {
    }
    
    default boolean visit(final OracleMultiInsertStatement.ConditionalInsertClause x) {
        return true;
    }
    
    default void endVisit(final OracleMultiInsertStatement.ConditionalInsertClause x) {
    }
    
    default boolean visit(final OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
        return true;
    }
    
    default void endVisit(final OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
    }
    
    default boolean visit(final OracleSelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleLockTableStatement x) {
        return true;
    }
    
    default void endVisit(final OracleLockTableStatement x) {
    }
    
    default boolean visit(final OracleAlterSessionStatement x) {
        return true;
    }
    
    default void endVisit(final OracleAlterSessionStatement x) {
    }
    
    default boolean visit(final OracleDatetimeExpr x) {
        return true;
    }
    
    default void endVisit(final OracleDatetimeExpr x) {
    }
    
    default boolean visit(final OracleSysdateExpr x) {
        return true;
    }
    
    default void endVisit(final OracleSysdateExpr x) {
    }
    
    default boolean visit(final OracleExceptionStatement x) {
        return true;
    }
    
    default void endVisit(final OracleExceptionStatement x) {
    }
    
    default boolean visit(final OracleExceptionStatement.Item x) {
        return true;
    }
    
    default void endVisit(final OracleExceptionStatement.Item x) {
    }
    
    default boolean visit(final OracleArgumentExpr x) {
        return true;
    }
    
    default void endVisit(final OracleArgumentExpr x) {
    }
    
    default boolean visit(final OracleSetTransactionStatement x) {
        return true;
    }
    
    default void endVisit(final OracleSetTransactionStatement x) {
    }
    
    default boolean visit(final OracleExplainStatement x) {
        return true;
    }
    
    default void endVisit(final OracleExplainStatement x) {
    }
    
    default boolean visit(final OracleAlterTableDropPartition x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableDropPartition x) {
    }
    
    default boolean visit(final OracleAlterTableTruncatePartition x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableTruncatePartition x) {
    }
    
    default boolean visit(final OracleAlterTableSplitPartition.TableSpaceItem x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableSplitPartition.TableSpaceItem x) {
    }
    
    default boolean visit(final OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableSplitPartition.UpdateIndexesClause x) {
    }
    
    default boolean visit(final OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
    }
    
    default boolean visit(final OracleAlterTableSplitPartition x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableSplitPartition x) {
    }
    
    default boolean visit(final OracleAlterTableModify x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableModify x) {
    }
    
    default boolean visit(final OracleCreateIndexStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleCreateIndexStatement x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleForStatement x) {
        return true;
    }
    
    default void endVisit(final OracleForStatement x) {
    }
    
    default boolean visit(final OracleRangeExpr x) {
        return true;
    }
    
    default void endVisit(final OracleRangeExpr x) {
    }
    
    default boolean visit(final OraclePrimaryKey x) {
        return true;
    }
    
    default void endVisit(final OraclePrimaryKey x) {
    }
    
    default boolean visit(final OracleCreateTableStatement x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleCreateTableStatement x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleStorageClause x) {
        return true;
    }
    
    default void endVisit(final OracleStorageClause x) {
    }
    
    default boolean visit(final OracleGotoStatement x) {
        return true;
    }
    
    default void endVisit(final OracleGotoStatement x) {
    }
    
    default boolean visit(final OracleLabelStatement x) {
        return true;
    }
    
    default void endVisit(final OracleLabelStatement x) {
    }
    
    default boolean visit(final OracleAlterTriggerStatement x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTriggerStatement x) {
    }
    
    default boolean visit(final OracleAlterSynonymStatement x) {
        return true;
    }
    
    default void endVisit(final OracleAlterSynonymStatement x) {
    }
    
    default boolean visit(final OracleAlterViewStatement x) {
        return true;
    }
    
    default void endVisit(final OracleAlterViewStatement x) {
    }
    
    default boolean visit(final OracleAlterTableMoveTablespace x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTableMoveTablespace x) {
    }
    
    default boolean visit(final OracleFileSpecification x) {
        return true;
    }
    
    default void endVisit(final OracleFileSpecification x) {
    }
    
    default boolean visit(final OracleAlterTablespaceAddDataFile x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTablespaceAddDataFile x) {
    }
    
    default boolean visit(final OracleAlterTablespaceStatement x) {
        return true;
    }
    
    default void endVisit(final OracleAlterTablespaceStatement x) {
    }
    
    default boolean visit(final OracleExitStatement x) {
        return true;
    }
    
    default void endVisit(final OracleExitStatement x) {
    }
    
    default boolean visit(final OracleContinueStatement x) {
        return true;
    }
    
    default void endVisit(final OracleContinueStatement x) {
    }
    
    default boolean visit(final OracleRaiseStatement x) {
        return true;
    }
    
    default void endVisit(final OracleRaiseStatement x) {
    }
    
    default boolean visit(final OracleCreateDatabaseDbLinkStatement x) {
        return true;
    }
    
    default void endVisit(final OracleCreateDatabaseDbLinkStatement x) {
    }
    
    default boolean visit(final OracleDropDbLinkStatement x) {
        return true;
    }
    
    default void endVisit(final OracleDropDbLinkStatement x) {
    }
    
    default boolean visit(final OracleDataTypeIntervalYear x) {
        return true;
    }
    
    default void endVisit(final OracleDataTypeIntervalYear x) {
    }
    
    default boolean visit(final OracleDataTypeIntervalDay x) {
        return true;
    }
    
    default void endVisit(final OracleDataTypeIntervalDay x) {
    }
    
    default boolean visit(final OracleUsingIndexClause x) {
        return true;
    }
    
    default void endVisit(final OracleUsingIndexClause x) {
    }
    
    default boolean visit(final OracleLobStorageClause x) {
        return true;
    }
    
    default void endVisit(final OracleLobStorageClause x) {
    }
    
    default boolean visit(final OracleUnique x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleUnique x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleForeignKey x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleForeignKey x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleCheck x) {
        return this.visit(x);
    }
    
    default void endVisit(final OracleCheck x) {
        this.endVisit(x);
    }
    
    default boolean visit(final OracleSupplementalIdKey x) {
        return true;
    }
    
    default void endVisit(final OracleSupplementalIdKey x) {
    }
    
    default boolean visit(final OracleSupplementalLogGrp x) {
        return true;
    }
    
    default void endVisit(final OracleSupplementalLogGrp x) {
    }
    
    default boolean visit(final OracleCreateTableStatement.Organization x) {
        return true;
    }
    
    default void endVisit(final OracleCreateTableStatement.Organization x) {
    }
    
    default boolean visit(final OracleCreateTableStatement.OIDIndex x) {
        return true;
    }
    
    default void endVisit(final OracleCreateTableStatement.OIDIndex x) {
    }
    
    default boolean visit(final OracleCreatePackageStatement x) {
        return true;
    }
    
    default void endVisit(final OracleCreatePackageStatement x) {
    }
    
    default boolean visit(final OracleExecuteImmediateStatement x) {
        return true;
    }
    
    default void endVisit(final OracleExecuteImmediateStatement x) {
    }
    
    default boolean visit(final OracleTreatExpr x) {
        return true;
    }
    
    default void endVisit(final OracleTreatExpr x) {
    }
    
    default boolean visit(final OracleCreateSynonymStatement x) {
        return true;
    }
    
    default void endVisit(final OracleCreateSynonymStatement x) {
    }
    
    default boolean visit(final OracleCreateTypeStatement x) {
        return true;
    }
    
    default void endVisit(final OracleCreateTypeStatement x) {
    }
    
    default boolean visit(final OraclePipeRowStatement x) {
        return true;
    }
    
    default void endVisit(final OraclePipeRowStatement x) {
    }
    
    default boolean visit(final OracleIsOfTypeExpr x) {
        return true;
    }
    
    default void endVisit(final OracleIsOfTypeExpr x) {
    }
    
    default boolean visit(final OracleRunStatement x) {
        return true;
    }
    
    default void endVisit(final OracleRunStatement x) {
    }
    
    default boolean visit(final OracleXmlColumnProperties x) {
        return true;
    }
    
    default void endVisit(final OracleXmlColumnProperties x) {
    }
    
    default boolean visit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
        return true;
    }
    
    default void endVisit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
    }
}
