// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

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
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
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
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
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
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
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
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleToMySqlOutputVisitor extends MySqlOutputVisitor implements OracleASTVisitor
{
    public OracleToMySqlOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    public OracleToMySqlOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
    }
    
    @Override
    public boolean visit(final OracleAnalytic x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAnalyticWindowing x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDeleteStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleIntervalExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleOuterExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectJoin x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectPivot x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectPivot.Item x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectRestriction.CheckOption x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectRestriction.ReadOnly x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectSubqueryTableSource x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectUnPivot x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleUpdateStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SampleClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectTableReference x) {
        return false;
    }
    
    @Override
    public boolean visit(final PartitionExtensionClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleWithSubqueryEntry x) {
        return false;
    }
    
    @Override
    public boolean visit(final SearchClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final CycleClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleBinaryFloatExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleBinaryDoubleExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCursorExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleIsSetExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ReturnRowsClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.MainModelClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelColumnClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.QueryPartitionClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelColumn x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelRulesClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.CellAssignmentItem x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.CellAssignment x) {
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleReturningClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleInsertStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.ConditionalInsertClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectQueryBlock x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleLockTableStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSessionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDatetimeExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSysdateExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement.Item x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleArgumentExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSetTransactionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExplainStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableDropPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableTruncatePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.TableSpaceItem x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableModify x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateIndexStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleForStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleRangeExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OraclePrimaryKey x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleStorageClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleGotoStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleLabelStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTriggerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSynonymStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableMoveTablespace x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLSizeExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleFileSpecification x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceAddDataFile x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExitStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleContinueStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleRaiseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateDatabaseDbLinkStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDropDbLinkStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalYear x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalDay x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleUsingIndexClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleLobStorageClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleUnique x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleForeignKey x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCheck x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSupplementalIdKey x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSupplementalLogGrp x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement.Organization x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement.OIDIndex x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreatePackageStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExecuteImmediateStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleTreatExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateSynonymStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTypeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OraclePipeRowStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleIsOfTypeExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleRunStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
        return false;
    }
}
