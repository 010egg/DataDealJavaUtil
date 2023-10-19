// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterTableAlterCheck;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlJSONTableExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByValue;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMigrateTaskStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowJobStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowClusterNameStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlMigrateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftLeaderTransferStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftMemberChangeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlManageInstanceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteForAdsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterFullTextIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextDictionaryStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextAnalyzerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenFilterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenizerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDropFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowCreateFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextCharFilterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCheckTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameSequenceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPhysicalProcesslistStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowConfigStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlashbackStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowNodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatasourcesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlChecksumTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterLogFileGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableSpaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateAddLogFileGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlEventSchedule;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByList;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableValidation;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOrderBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableLock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableForce;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlIterateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlLeaveStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateExternalCatalogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabaseStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowDbLockStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTopologyStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDdlStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBroadcastsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTraceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlowStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSequencesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPartitionsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseKillJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseSetOption;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHMSMetaStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDisabledPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlClearPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdatePlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowMetadataLock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowGlobalIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsBaselineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsChangeDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsClearDDLJobCache;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsInspectDDLJobCache;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRemoveDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRollbackDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRecoverDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsCancelDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowDDLJobs;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowStcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowHtcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface MySqlASTVisitor extends SQLASTVisitor
{
    default boolean visit(final MySqlTableIndex x) {
        return true;
    }
    
    default void endVisit(final MySqlTableIndex x) {
    }
    
    default boolean visit(final MySqlKey x) {
        return true;
    }
    
    default void endVisit(final MySqlKey x) {
    }
    
    default boolean visit(final MySqlPrimaryKey x) {
        return true;
    }
    
    default void endVisit(final MySqlPrimaryKey x) {
    }
    
    default boolean visit(final MySqlUnique x) {
        return true;
    }
    
    default void endVisit(final MySqlUnique x) {
    }
    
    default boolean visit(final MysqlForeignKey x) {
        return this.visit(x);
    }
    
    default void endVisit(final MysqlForeignKey x) {
        this.endVisit(x);
    }
    
    default void endVisit(final MySqlPrepareStatement x) {
    }
    
    default boolean visit(final MySqlPrepareStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlExecuteStatement x) {
    }
    
    default boolean visit(final MysqlDeallocatePrepareStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlDeallocatePrepareStatement x) {
    }
    
    default boolean visit(final MySqlExecuteStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlDeleteStatement x) {
    }
    
    default boolean visit(final MySqlDeleteStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlInsertStatement x) {
    }
    
    default boolean visit(final MySqlInsertStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlLoadDataInFileStatement x) {
    }
    
    default boolean visit(final MySqlLoadDataInFileStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlLoadXmlStatement x) {
    }
    
    default boolean visit(final MySqlLoadXmlStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowWarningsStatement x) {
    }
    
    default boolean visit(final MySqlShowWarningsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowAuthorsStatement x) {
    }
    
    default boolean visit(final MySqlShowAuthorsStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlShowHtcStatement x) {
    }
    
    default boolean visit(final MysqlShowHtcStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlShowStcStatement x) {
    }
    
    default boolean visit(final MysqlShowStcStatement x) {
        return true;
    }
    
    default void endVisit(final CobarShowStatus x) {
    }
    
    default boolean visit(final CobarShowStatus x) {
        return true;
    }
    
    default void endVisit(final DrdsShowDDLJobs x) {
    }
    
    default boolean visit(final DrdsShowDDLJobs x) {
        return true;
    }
    
    default void endVisit(final DrdsCancelDDLJob x) {
    }
    
    default boolean visit(final DrdsCancelDDLJob x) {
        return true;
    }
    
    default void endVisit(final DrdsRecoverDDLJob x) {
    }
    
    default boolean visit(final DrdsRecoverDDLJob x) {
        return true;
    }
    
    default void endVisit(final DrdsRollbackDDLJob x) {
    }
    
    default boolean visit(final DrdsRollbackDDLJob x) {
        return true;
    }
    
    default void endVisit(final DrdsRemoveDDLJob x) {
    }
    
    default boolean visit(final DrdsRemoveDDLJob x) {
        return true;
    }
    
    default void endVisit(final DrdsInspectDDLJobCache x) {
    }
    
    default boolean visit(final DrdsInspectDDLJobCache x) {
        return true;
    }
    
    default void endVisit(final DrdsClearDDLJobCache x) {
    }
    
    default boolean visit(final DrdsClearDDLJobCache x) {
        return true;
    }
    
    default void endVisit(final DrdsChangeDDLJob x) {
    }
    
    default boolean visit(final DrdsChangeDDLJob x) {
        return true;
    }
    
    default void endVisit(final DrdsBaselineStatement x) {
    }
    
    default boolean visit(final DrdsBaselineStatement x) {
        return true;
    }
    
    default void endVisit(final DrdsShowGlobalIndex x) {
    }
    
    default boolean visit(final DrdsShowGlobalIndex x) {
        return true;
    }
    
    default void endVisit(final DrdsShowMetadataLock x) {
    }
    
    default boolean visit(final DrdsShowMetadataLock x) {
        return true;
    }
    
    default void endVisit(final MySqlBinlogStatement x) {
    }
    
    default boolean visit(final MySqlBinlogStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlResetStatement x) {
    }
    
    default boolean visit(final MySqlResetStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateUserStatement x) {
    }
    
    default boolean visit(final MySqlCreateUserStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateUserStatement.UserSpecification x) {
    }
    
    default boolean visit(final MySqlCreateUserStatement.UserSpecification x) {
        return true;
    }
    
    default void endVisit(final MySqlPartitionByKey x) {
    }
    
    default boolean visit(final MySqlPartitionByKey x) {
        return true;
    }
    
    default void endVisit(final MySqlUpdatePlanCacheStatement x) {
    }
    
    default boolean visit(final MySqlUpdatePlanCacheStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPlanCacheStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowPlanCacheStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlClearPlanCacheStatement x) {
    }
    
    default boolean visit(final MySqlClearPlanCacheStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlDisabledPlanCacheStatement x) {
    }
    
    default boolean visit(final MySqlDisabledPlanCacheStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlExplainPlanCacheStatement x) {
    }
    
    default boolean visit(final MySqlExplainPlanCacheStatement x) {
        return true;
    }
    
    default boolean visit(final MySqlSelectQueryBlock x) {
        return this.visit(x);
    }
    
    default void endVisit(final MySqlSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    default boolean visit(final MySqlOutFileExpr x) {
        return true;
    }
    
    default void endVisit(final MySqlOutFileExpr x) {
    }
    
    default boolean visit(final MySqlExplainStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlExplainStatement x) {
    }
    
    default boolean visit(final MySqlUpdateStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlUpdateStatement x) {
    }
    
    default boolean visit(final MySqlSetTransactionStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlSetTransactionStatement x) {
    }
    
    default boolean visit(final MySqlShowHMSMetaStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowHMSMetaStatement x) {
    }
    
    default boolean visit(final MySqlShowBinaryLogsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowBinaryLogsStatement x) {
    }
    
    default boolean visit(final MySqlShowMasterLogsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowMasterLogsStatement x) {
    }
    
    default boolean visit(final MySqlShowCharacterSetStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCharacterSetStatement x) {
    }
    
    default boolean visit(final MySqlShowCollationStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCollationStatement x) {
    }
    
    default boolean visit(final MySqlShowBinLogEventsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowBinLogEventsStatement x) {
    }
    
    default boolean visit(final MySqlShowContributorsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowContributorsStatement x) {
    }
    
    default boolean visit(final MySqlShowCreateDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCreateDatabaseStatement x) {
    }
    
    default boolean visit(final MySqlShowCreateEventStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCreateEventStatement x) {
    }
    
    default boolean visit(final MySqlShowCreateFunctionStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCreateFunctionStatement x) {
    }
    
    default boolean visit(final MySqlShowCreateProcedureStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCreateProcedureStatement x) {
    }
    
    default boolean visit(final MySqlShowCreateTriggerStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowCreateTriggerStatement x) {
    }
    
    default boolean visit(final MySqlShowEngineStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowEngineStatement x) {
    }
    
    default boolean visit(final MySqlShowEnginesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowEnginesStatement x) {
    }
    
    default boolean visit(final MySqlShowErrorsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowErrorsStatement x) {
    }
    
    default boolean visit(final MySqlShowEventsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowEventsStatement x) {
    }
    
    default boolean visit(final MySqlShowFunctionCodeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowFunctionCodeStatement x) {
    }
    
    default boolean visit(final MySqlShowFunctionStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowFunctionStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowGrantsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowGrantsStatement x) {
    }
    
    default boolean visit(final MySqlUserName x) {
        return true;
    }
    
    default void endVisit(final MySqlUserName x) {
    }
    
    default boolean visit(final MySqlAlterDatabaseSetOption x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterDatabaseSetOption x) {
    }
    
    default boolean visit(final MySqlAlterDatabaseKillJob x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterDatabaseKillJob x) {
    }
    
    default boolean visit(final MySqlShowMasterStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowMasterStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowOpenTablesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowOpenTablesStatement x) {
    }
    
    default boolean visit(final MySqlShowPluginsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPluginsStatement x) {
    }
    
    default boolean visit(final MySqlShowPartitionsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPartitionsStatement x) {
    }
    
    default boolean visit(final MySqlShowPrivilegesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPrivilegesStatement x) {
    }
    
    default boolean visit(final MySqlShowProcedureCodeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowProcedureCodeStatement x) {
    }
    
    default boolean visit(final MySqlShowProcedureStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowProcedureStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowProcessListStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowProcessListStatement x) {
    }
    
    default boolean visit(final MySqlShowProfileStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowProfileStatement x) {
    }
    
    default boolean visit(final MySqlShowProfilesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowProfilesStatement x) {
    }
    
    default boolean visit(final MySqlShowRelayLogEventsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowRelayLogEventsStatement x) {
    }
    
    default boolean visit(final MySqlShowSlaveHostsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowSlaveHostsStatement x) {
    }
    
    default boolean visit(final MySqlShowSequencesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowSequencesStatement x) {
    }
    
    default boolean visit(final MySqlShowSlaveStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowSlaveStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowSlowStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowSlowStatement x) {
    }
    
    default boolean visit(final MySqlShowTableStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowTableStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowTriggersStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowTriggersStatement x) {
    }
    
    default boolean visit(final MySqlShowTraceStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowTraceStatement x) {
    }
    
    default boolean visit(final MySqlShowBroadcastsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowBroadcastsStatement x) {
    }
    
    default boolean visit(final MySqlShowRuleStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowRuleStatement x) {
    }
    
    default boolean visit(final MySqlShowRuleStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowRuleStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowDsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowDsStatement x) {
    }
    
    default boolean visit(final MySqlShowDdlStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowDdlStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowTopologyStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowTopologyStatement x) {
    }
    
    default boolean visit(final MySqlRenameTableStatement.Item x) {
        return true;
    }
    
    default void endVisit(final MySqlRenameTableStatement.Item x) {
    }
    
    default boolean visit(final MySqlRenameTableStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlRenameTableStatement x) {
    }
    
    default boolean visit(final MysqlShowDbLockStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlShowDbLockStatement x) {
    }
    
    default boolean visit(final MySqlShowDatabaseStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowDatabaseStatusStatement x) {
    }
    
    default boolean visit(final MySqlUseIndexHint x) {
        return true;
    }
    
    default void endVisit(final MySqlUseIndexHint x) {
    }
    
    default boolean visit(final MySqlIgnoreIndexHint x) {
        return true;
    }
    
    default void endVisit(final MySqlIgnoreIndexHint x) {
    }
    
    default boolean visit(final MySqlLockTableStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlLockTableStatement x) {
    }
    
    default boolean visit(final MySqlLockTableStatement.Item x) {
        return true;
    }
    
    default void endVisit(final MySqlLockTableStatement.Item x) {
    }
    
    default boolean visit(final MySqlUnlockTablesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlUnlockTablesStatement x) {
    }
    
    default boolean visit(final MySqlForceIndexHint x) {
        return true;
    }
    
    default void endVisit(final MySqlForceIndexHint x) {
    }
    
    default boolean visit(final MySqlAlterTableChangeColumn x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableChangeColumn x) {
    }
    
    default boolean visit(final MySqlAlterTableOption x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableOption x) {
    }
    
    default boolean visit(final MySqlCreateTableStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateTableStatement x) {
    }
    
    default boolean visit(final MySqlHelpStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlHelpStatement x) {
    }
    
    default boolean visit(final MySqlCharExpr x) {
        return true;
    }
    
    default void endVisit(final MySqlCharExpr x) {
    }
    
    default boolean visit(final MySqlAlterTableModifyColumn x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableModifyColumn x) {
    }
    
    default boolean visit(final MySqlAlterTableDiscardTablespace x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableDiscardTablespace x) {
    }
    
    default boolean visit(final MySqlAlterTableImportTablespace x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableImportTablespace x) {
    }
    
    default boolean visit(final MySqlCreateTableStatement.TableSpaceOption x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateTableStatement.TableSpaceOption x) {
    }
    
    default boolean visit(final MySqlAnalyzeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAnalyzeStatement x) {
    }
    
    default boolean visit(final MySqlCreateExternalCatalogStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateExternalCatalogStatement x) {
    }
    
    default boolean visit(final MySqlAlterUserStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterUserStatement x) {
    }
    
    default boolean visit(final MySqlOptimizeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlOptimizeStatement x) {
    }
    
    default boolean visit(final MySqlHintStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlHintStatement x) {
    }
    
    default boolean visit(final MySqlOrderingExpr x) {
        return true;
    }
    
    default void endVisit(final MySqlOrderingExpr x) {
    }
    
    default boolean visit(final MySqlCaseStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCaseStatement x) {
    }
    
    default boolean visit(final MySqlDeclareStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlDeclareStatement x) {
    }
    
    default boolean visit(final MySqlSelectIntoStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlSelectIntoStatement x) {
    }
    
    default boolean visit(final MySqlCaseStatement.MySqlWhenStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCaseStatement.MySqlWhenStatement x) {
    }
    
    default boolean visit(final MySqlLeaveStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlLeaveStatement x) {
    }
    
    default boolean visit(final MySqlIterateStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlIterateStatement x) {
    }
    
    default boolean visit(final MySqlRepeatStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlRepeatStatement x) {
    }
    
    default boolean visit(final MySqlCursorDeclareStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCursorDeclareStatement x) {
    }
    
    default boolean visit(final MySqlUpdateTableSource x) {
        return true;
    }
    
    default void endVisit(final MySqlUpdateTableSource x) {
    }
    
    default boolean visit(final MySqlAlterTableAlterColumn x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableAlterColumn x) {
    }
    
    default boolean visit(final MySqlAlterTableForce x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableForce x) {
    }
    
    default boolean visit(final MySqlAlterTableLock x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableLock x) {
    }
    
    default boolean visit(final MySqlAlterTableOrderBy x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableOrderBy x) {
    }
    
    default boolean visit(final MySqlAlterTableValidation x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableValidation x) {
    }
    
    default boolean visit(final MySqlSubPartitionByKey x) {
        return true;
    }
    
    default void endVisit(final MySqlSubPartitionByKey x) {
    }
    
    default boolean visit(final MySqlSubPartitionByList x) {
        return true;
    }
    
    default void endVisit(final MySqlSubPartitionByList x) {
    }
    
    default boolean visit(final MySqlDeclareHandlerStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlDeclareHandlerStatement x) {
    }
    
    default boolean visit(final MySqlDeclareConditionStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlDeclareConditionStatement x) {
    }
    
    default boolean visit(final MySqlFlushStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlFlushStatement x) {
    }
    
    default boolean visit(final MySqlEventSchedule x) {
        return true;
    }
    
    default void endVisit(final MySqlEventSchedule x) {
    }
    
    default boolean visit(final MySqlCreateEventStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateEventStatement x) {
    }
    
    default boolean visit(final MySqlCreateAddLogFileGroupStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateAddLogFileGroupStatement x) {
    }
    
    default boolean visit(final MySqlCreateServerStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateServerStatement x) {
    }
    
    default boolean visit(final MySqlCreateTableSpaceStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCreateTableSpaceStatement x) {
    }
    
    default boolean visit(final MySqlAlterEventStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterEventStatement x) {
    }
    
    default boolean visit(final MySqlAlterLogFileGroupStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterLogFileGroupStatement x) {
    }
    
    default boolean visit(final MySqlAlterServerStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterServerStatement x) {
    }
    
    default boolean visit(final MySqlAlterTablespaceStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTablespaceStatement x) {
    }
    
    default boolean visit(final MySqlChecksumTableStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlChecksumTableStatement x) {
    }
    
    default boolean visit(final MySqlShowDatasourcesStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowDatasourcesStatement x) {
    }
    
    default boolean visit(final MySqlShowNodeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowNodeStatement x) {
    }
    
    default boolean visit(final MySqlShowHelpStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowHelpStatement x) {
    }
    
    default boolean visit(final MySqlFlashbackStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlFlashbackStatement x) {
    }
    
    default boolean visit(final MySqlShowConfigStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowConfigStatement x) {
    }
    
    default boolean visit(final MySqlShowPlanCacheStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPlanCacheStatement x) {
    }
    
    default boolean visit(final MySqlShowPhysicalProcesslistStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowPhysicalProcesslistStatement x) {
    }
    
    default boolean visit(final MySqlRenameSequenceStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlRenameSequenceStatement x) {
    }
    
    default boolean visit(final MySqlCheckTableStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlCheckTableStatement x) {
    }
    
    default boolean visit(final MysqlCreateFullTextCharFilterStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlCreateFullTextCharFilterStatement x) {
    }
    
    default boolean visit(final MysqlShowFullTextStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlShowFullTextStatement x) {
    }
    
    default boolean visit(final MysqlShowCreateFullTextStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlShowCreateFullTextStatement x) {
    }
    
    default boolean visit(final MysqlAlterFullTextStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlAlterFullTextStatement x) {
    }
    
    default boolean visit(final MysqlDropFullTextStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlDropFullTextStatement x) {
    }
    
    default boolean visit(final MysqlCreateFullTextTokenizerStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlCreateFullTextTokenizerStatement x) {
    }
    
    default boolean visit(final MysqlCreateFullTextTokenFilterStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlCreateFullTextTokenFilterStatement x) {
    }
    
    default boolean visit(final MysqlCreateFullTextAnalyzerStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlCreateFullTextAnalyzerStatement x) {
    }
    
    default boolean visit(final MysqlCreateFullTextDictionaryStatement x) {
        return true;
    }
    
    default void endVisit(final MysqlCreateFullTextDictionaryStatement x) {
    }
    
    default boolean visit(final MySqlAlterTableAlterFullTextIndex x) {
        return true;
    }
    
    default void endVisit(final MySqlAlterTableAlterFullTextIndex x) {
    }
    
    default boolean visit(final MySqlExecuteForAdsStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlExecuteForAdsStatement x) {
    }
    
    default boolean visit(final MySqlManageInstanceGroupStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlManageInstanceGroupStatement x) {
    }
    
    default boolean visit(final MySqlRaftMemberChangeStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlRaftMemberChangeStatement x) {
    }
    
    default boolean visit(final MySqlRaftLeaderTransferStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlRaftLeaderTransferStatement x) {
    }
    
    default boolean visit(final MySqlMigrateStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlMigrateStatement x) {
    }
    
    default boolean visit(final MySqlShowClusterNameStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowClusterNameStatement x) {
    }
    
    default boolean visit(final MySqlShowJobStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowJobStatusStatement x) {
    }
    
    default boolean visit(final MySqlShowMigrateTaskStatusStatement x) {
        return true;
    }
    
    default void endVisit(final MySqlShowMigrateTaskStatusStatement x) {
    }
    
    default boolean visit(final MySqlSubPartitionByValue x) {
        return true;
    }
    
    default void endVisit(final MySqlSubPartitionByValue x) {
    }
    
    default boolean visit(final MySqlExtPartition x) {
        return true;
    }
    
    default void endVisit(final MySqlExtPartition x) {
    }
    
    default boolean visit(final MySqlExtPartition.Item x) {
        return true;
    }
    
    default void endVisit(final MySqlExtPartition.Item x) {
    }
    
    default boolean visit(final MySqlJSONTableExpr x) {
        return true;
    }
    
    default void endVisit(final MySqlJSONTableExpr x) {
    }
    
    default boolean visit(final MySqlJSONTableExpr.Column x) {
        return true;
    }
    
    default void endVisit(final MySqlJSONTableExpr.Column x) {
    }
    
    default boolean visit(final MysqlAlterTableAlterCheck x) {
        return true;
    }
    
    default void endVisit(final MysqlAlterTableAlterCheck x) {
    }
}
