// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.ast.statement.SQLShowACLStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRolesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowHistoryStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloneTableStatement;
import com.alibaba.druid.sql.ast.SQLTableDataType;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableChangeOwner;
import com.alibaba.druid.sql.ast.SQLPartitionSpec;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableMergePartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLListResourceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLAlterResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLCreateResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddClusteringKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropClusteringKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropSubpartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSubpartitionLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCompression;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableBlockSize;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionCount;
import com.alibaba.druid.sql.ast.SQLPartitionByValue;
import com.alibaba.druid.sql.ast.statement.SQLRenameUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLCancelJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLBuildTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLRestoreStatement;
import com.alibaba.druid.sql.ast.statement.SQLBackupStatement;
import com.alibaba.druid.sql.ast.statement.SQLArchiveTableStatement;
import com.alibaba.druid.sql.ast.SQLDataTypeRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesQuery;
import com.alibaba.druid.sql.ast.statement.SQLSyncMetaStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableLike;
import com.alibaba.druid.sql.ast.statement.SQLSubmitJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowUsersStatement;
import com.alibaba.druid.sql.ast.statement.SQLCopyFromStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnnestTableSource;
import com.alibaba.druid.sql.ast.statement.SQLForStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhoamiStatement;
import com.alibaba.druid.sql.ast.statement.SQLPartitionRef;
import com.alibaba.druid.sql.ast.statement.SQLExplainAnalyzeStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLAdhocTableSource;
import com.alibaba.druid.sql.ast.statement.SQLShowQueryTaskStatement;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLDbLinkExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowSessionStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowFunctionsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCatalogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddSupplemental;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticListStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.ast.statement.SQLPurgeRecyclebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeLogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTemporaryOutputStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowOutlinesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableUnarchivePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableArchivePartition;
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSampling;
import com.alibaba.druid.sql.ast.statement.SQLImportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowIndexesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRecoverPartitions;
import com.alibaba.druid.sql.ast.SQLUnionDataType;
import com.alibaba.druid.sql.ast.SQLAnnIndex;
import com.alibaba.druid.sql.ast.expr.SQLDecimalExpr;
import com.alibaba.druid.sql.ast.expr.SQLJSONExpr;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.expr.SQLExtractExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLDumpStatement;
import com.alibaba.druid.sql.ast.expr.SQLContainsExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuesExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.ast.statement.SQLDropCatalogStatement;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLMatchAgainstExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReplaceColumn;
import com.alibaba.druid.sql.ast.statement.SQLDropRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameIndex;
import com.alibaba.druid.sql.ast.statement.SQLShowViewsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetOption;
import com.alibaba.druid.sql.ast.statement.SQLShowProcessListStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTableGroupsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSubpartitionAvailablePartitionNum;
import com.alibaba.druid.sql.ast.statement.SQLDropTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLRefreshMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropMaterializedViewStatement;
import com.alibaba.druid.sql.ast.SQLRowDataType;
import com.alibaba.druid.sql.ast.SQLStructDataType;
import com.alibaba.druid.sql.ast.SQLMapDataType;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLDropTypeStatement;
import com.alibaba.druid.sql.ast.SQLRecordDataType;
import com.alibaba.druid.sql.ast.statement.SQLDropSynonymStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropServerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropLogFileGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropEventStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterCharacter;
import com.alibaba.druid.sql.ast.statement.SQLShowRecylebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPackagesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowGrantsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowErrorsStatement;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLScriptCommitStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.expr.SQLFlashbackExpr;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.SQLArgument;
import com.alibaba.druid.sql.ast.statement.SQLReturnStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeclareStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhileStatement;
import com.alibaba.druid.sql.ast.statement.SQLDescribeStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.alibaba.druid.sql.ast.SQLSubPartitionByList;
import com.alibaba.druid.sql.ast.SQLSubPartitionByRange;
import com.alibaba.druid.sql.ast.SQLSubPartitionByHash;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.alibaba.druid.sql.ast.statement.SQLCloseStatement;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.statement.SQLOpenStatement;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTouch;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionSetProperties;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLocation;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetComment;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewRenameStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.ast.statement.SQLRevokeStatement;
import com.alibaba.druid.sql.ast.expr.SQLFloatExpr;
import com.alibaba.druid.sql.ast.expr.SQLDoubleExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLIndexOptions;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLColumnReference;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLExprHint;
import com.alibaba.druid.sql.ast.statement.SQLColumnCheck;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLDefault;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.alibaba.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.alibaba.druid.sql.ast.SQLKeep;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLReleaseSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemGetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemSetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableModifyClusteredBy;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDeleteByCondition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import com.alibaba.druid.sql.ast.expr.SQLDefaultExpr;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllExpr;
import com.alibaba.druid.sql.ast.expr.SQLAnyExpr;
import com.alibaba.druid.sql.ast.expr.SQLSomeExpr;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLNotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLZOrderBy;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLRealExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLNotExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLTinyIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLBigIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLSmallIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseStatement;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;

public interface SQLASTVisitor
{
    default void endVisit(final SQLAllColumnExpr x) {
    }
    
    default void endVisit(final SQLBetweenExpr x) {
    }
    
    default void endVisit(final SQLBinaryOpExpr x) {
    }
    
    default void endVisit(final SQLCaseExpr x) {
    }
    
    default void endVisit(final SQLCaseExpr.Item x) {
    }
    
    default void endVisit(final SQLCaseStatement x) {
    }
    
    default void endVisit(final SQLCaseStatement.Item x) {
    }
    
    default void endVisit(final SQLCharExpr x) {
    }
    
    default void endVisit(final SQLIdentifierExpr x) {
    }
    
    default void endVisit(final SQLInListExpr x) {
    }
    
    default void endVisit(final SQLIntegerExpr x) {
    }
    
    default void endVisit(final SQLSmallIntExpr x) {
    }
    
    default void endVisit(final SQLBigIntExpr x) {
    }
    
    default void endVisit(final SQLTinyIntExpr x) {
    }
    
    default void endVisit(final SQLExistsExpr x) {
    }
    
    default void endVisit(final SQLNCharExpr x) {
    }
    
    default void endVisit(final SQLNotExpr x) {
    }
    
    default void endVisit(final SQLNullExpr x) {
    }
    
    default void endVisit(final SQLNumberExpr x) {
    }
    
    default void endVisit(final SQLRealExpr x) {
    }
    
    default void endVisit(final SQLPropertyExpr x) {
    }
    
    default void endVisit(final SQLSelectGroupByClause x) {
    }
    
    default void endVisit(final SQLSelectItem x) {
    }
    
    default void endVisit(final SQLSelectStatement x) {
    }
    
    default void postVisit(final SQLObject x) {
    }
    
    default void preVisit(final SQLObject x) {
    }
    
    default boolean visit(final SQLAllColumnExpr x) {
        return true;
    }
    
    default boolean visit(final SQLBetweenExpr x) {
        return true;
    }
    
    default boolean visit(final SQLBinaryOpExpr x) {
        return true;
    }
    
    default boolean visit(final SQLCaseExpr x) {
        return true;
    }
    
    default boolean visit(final SQLCaseExpr.Item x) {
        return true;
    }
    
    default boolean visit(final SQLCaseStatement x) {
        return true;
    }
    
    default boolean visit(final SQLCaseStatement.Item x) {
        return true;
    }
    
    default boolean visit(final SQLCastExpr x) {
        return true;
    }
    
    default boolean visit(final SQLCharExpr x) {
        return true;
    }
    
    default boolean visit(final SQLExistsExpr x) {
        return true;
    }
    
    default boolean visit(final SQLIdentifierExpr x) {
        return true;
    }
    
    default boolean visit(final SQLInListExpr x) {
        return true;
    }
    
    default boolean visit(final SQLIntegerExpr x) {
        return true;
    }
    
    default boolean visit(final SQLSmallIntExpr x) {
        return true;
    }
    
    default boolean visit(final SQLBigIntExpr x) {
        return true;
    }
    
    default boolean visit(final SQLTinyIntExpr x) {
        return true;
    }
    
    default boolean visit(final SQLNCharExpr x) {
        return true;
    }
    
    default boolean visit(final SQLNotExpr x) {
        return true;
    }
    
    default boolean visit(final SQLNullExpr x) {
        return true;
    }
    
    default boolean visit(final SQLNumberExpr x) {
        return true;
    }
    
    default boolean visit(final SQLRealExpr x) {
        return true;
    }
    
    default boolean visit(final SQLPropertyExpr x) {
        return true;
    }
    
    default boolean visit(final SQLSelectGroupByClause x) {
        return true;
    }
    
    default boolean visit(final SQLSelectItem x) {
        return true;
    }
    
    default void endVisit(final SQLCastExpr x) {
    }
    
    default boolean visit(final SQLSelectStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAggregateExpr x) {
    }
    
    default boolean visit(final SQLAggregateExpr x) {
        return true;
    }
    
    default boolean visit(final SQLVariantRefExpr x) {
        return true;
    }
    
    default void endVisit(final SQLVariantRefExpr x) {
    }
    
    default boolean visit(final SQLQueryExpr x) {
        return true;
    }
    
    default void endVisit(final SQLQueryExpr x) {
    }
    
    default boolean visit(final SQLUnaryExpr x) {
        return true;
    }
    
    default void endVisit(final SQLUnaryExpr x) {
    }
    
    default boolean visit(final SQLHexExpr x) {
        return true;
    }
    
    default void endVisit(final SQLHexExpr x) {
    }
    
    default boolean visit(final SQLSelect x) {
        return true;
    }
    
    default void endVisit(final SQLSelect select) {
    }
    
    default boolean visit(final SQLSelectQueryBlock x) {
        return true;
    }
    
    default void endVisit(final SQLSelectQueryBlock x) {
    }
    
    default boolean visit(final SQLExprTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLExprTableSource x) {
    }
    
    default boolean visit(final SQLOrderBy x) {
        return true;
    }
    
    default void endVisit(final SQLOrderBy x) {
    }
    
    default boolean visit(final SQLZOrderBy x) {
        return true;
    }
    
    default void endVisit(final SQLZOrderBy x) {
    }
    
    default boolean visit(final SQLSelectOrderByItem x) {
        return true;
    }
    
    default void endVisit(final SQLSelectOrderByItem x) {
    }
    
    default boolean visit(final SQLDropTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropTableStatement x) {
    }
    
    default boolean visit(final SQLCreateTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateTableStatement x) {
    }
    
    default boolean visit(final SQLColumnDefinition x) {
        return true;
    }
    
    default void endVisit(final SQLColumnDefinition x) {
    }
    
    default boolean visit(final SQLColumnDefinition.Identity x) {
        return true;
    }
    
    default void endVisit(final SQLColumnDefinition.Identity x) {
    }
    
    default boolean visit(final SQLDataType x) {
        return true;
    }
    
    default void endVisit(final SQLDataType x) {
    }
    
    default boolean visit(final SQLCharacterDataType x) {
        return true;
    }
    
    default void endVisit(final SQLCharacterDataType x) {
    }
    
    default boolean visit(final SQLDeleteStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDeleteStatement x) {
    }
    
    default boolean visit(final SQLCurrentOfCursorExpr x) {
        return true;
    }
    
    default void endVisit(final SQLCurrentOfCursorExpr x) {
    }
    
    default boolean visit(final SQLInsertStatement x) {
        return true;
    }
    
    default void endVisit(final SQLInsertStatement x) {
    }
    
    default boolean visit(final SQLInsertStatement.ValuesClause x) {
        return true;
    }
    
    default void endVisit(final SQLInsertStatement.ValuesClause x) {
    }
    
    default boolean visit(final SQLUpdateSetItem x) {
        return true;
    }
    
    default void endVisit(final SQLUpdateSetItem x) {
    }
    
    default boolean visit(final SQLUpdateStatement x) {
        return true;
    }
    
    default void endVisit(final SQLUpdateStatement x) {
    }
    
    default boolean visit(final SQLCreateViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateViewStatement x) {
    }
    
    default boolean visit(final SQLCreateViewStatement.Column x) {
        return true;
    }
    
    default void endVisit(final SQLCreateViewStatement.Column x) {
    }
    
    default boolean visit(final SQLNotNullConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLNotNullConstraint x) {
    }
    
    default void endVisit(final SQLMethodInvokeExpr x) {
    }
    
    default boolean visit(final SQLMethodInvokeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLUnionQuery x) {
    }
    
    default boolean visit(final SQLUnionQuery x) {
        return true;
    }
    
    default void endVisit(final SQLSetStatement x) {
    }
    
    default boolean visit(final SQLSetStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAssignItem x) {
    }
    
    default boolean visit(final SQLAssignItem x) {
        return true;
    }
    
    default void endVisit(final SQLCallStatement x) {
    }
    
    default boolean visit(final SQLCallStatement x) {
        return true;
    }
    
    default void endVisit(final SQLJoinTableSource x) {
    }
    
    default boolean visit(final SQLJoinTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLJoinTableSource.UDJ x) {
    }
    
    default boolean visit(final SQLJoinTableSource.UDJ x) {
        return true;
    }
    
    default void endVisit(final SQLSomeExpr x) {
    }
    
    default boolean visit(final SQLSomeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLAnyExpr x) {
    }
    
    default boolean visit(final SQLAnyExpr x) {
        return true;
    }
    
    default void endVisit(final SQLAllExpr x) {
    }
    
    default boolean visit(final SQLAllExpr x) {
        return true;
    }
    
    default void endVisit(final SQLInSubQueryExpr x) {
    }
    
    default boolean visit(final SQLInSubQueryExpr x) {
        return true;
    }
    
    default void endVisit(final SQLListExpr x) {
    }
    
    default boolean visit(final SQLListExpr x) {
        return true;
    }
    
    default void endVisit(final SQLSubqueryTableSource x) {
    }
    
    default boolean visit(final SQLSubqueryTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLTruncateStatement x) {
    }
    
    default boolean visit(final SQLTruncateStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDefaultExpr x) {
    }
    
    default boolean visit(final SQLDefaultExpr x) {
        return true;
    }
    
    default void endVisit(final SQLCommentStatement x) {
    }
    
    default boolean visit(final SQLCommentStatement x) {
        return true;
    }
    
    default void endVisit(final SQLUseStatement x) {
    }
    
    default boolean visit(final SQLUseStatement x) {
        return true;
    }
    
    default boolean visit(final SQLAlterTableAddColumn x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddColumn x) {
    }
    
    default boolean visit(final SQLAlterTableDeleteByCondition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDeleteByCondition x) {
    }
    
    default boolean visit(final SQLAlterTableModifyClusteredBy x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableModifyClusteredBy x) {
    }
    
    default boolean visit(final SQLAlterTableDropColumnItem x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropColumnItem x) {
    }
    
    default boolean visit(final SQLAlterTableDropIndex x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropIndex x) {
    }
    
    default boolean visit(final SQLAlterTableGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableGroupStatement x) {
    }
    
    default boolean visit(final SQLAlterSystemSetConfigStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterSystemSetConfigStatement x) {
    }
    
    default boolean visit(final SQLAlterSystemGetConfigStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterSystemGetConfigStatement x) {
    }
    
    default boolean visit(final SQLDropIndexStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropIndexStatement x) {
    }
    
    default boolean visit(final SQLDropViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropViewStatement x) {
    }
    
    default boolean visit(final SQLSavePointStatement x) {
        return true;
    }
    
    default void endVisit(final SQLSavePointStatement x) {
    }
    
    default boolean visit(final SQLRollbackStatement x) {
        return true;
    }
    
    default void endVisit(final SQLRollbackStatement x) {
    }
    
    default boolean visit(final SQLReleaseSavePointStatement x) {
        return true;
    }
    
    default void endVisit(final SQLReleaseSavePointStatement x) {
    }
    
    default void endVisit(final SQLCommentHint x) {
    }
    
    default boolean visit(final SQLCommentHint x) {
        return true;
    }
    
    default void endVisit(final SQLCreateDatabaseStatement x) {
    }
    
    default boolean visit(final SQLCreateDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final SQLOver x) {
    }
    
    default boolean visit(final SQLOver x) {
        return true;
    }
    
    default void endVisit(final SQLKeep x) {
    }
    
    default boolean visit(final SQLKeep x) {
        return true;
    }
    
    default void endVisit(final SQLColumnPrimaryKey x) {
    }
    
    default boolean visit(final SQLColumnPrimaryKey x) {
        return true;
    }
    
    default boolean visit(final SQLColumnUniqueKey x) {
        return true;
    }
    
    default void endVisit(final SQLColumnUniqueKey x) {
    }
    
    default void endVisit(final SQLWithSubqueryClause x) {
    }
    
    default boolean visit(final SQLWithSubqueryClause x) {
        return true;
    }
    
    default void endVisit(final SQLWithSubqueryClause.Entry x) {
    }
    
    default boolean visit(final SQLWithSubqueryClause.Entry x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAlterColumn x) {
    }
    
    default boolean visit(final SQLAlterTableAlterColumn x) {
        return true;
    }
    
    default boolean visit(final SQLCheck x) {
        return true;
    }
    
    default void endVisit(final SQLCheck x) {
    }
    
    default boolean visit(final SQLDefault x) {
        return true;
    }
    
    default void endVisit(final SQLDefault x) {
    }
    
    default boolean visit(final SQLAlterTableDropForeignKey x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropForeignKey x) {
    }
    
    default boolean visit(final SQLAlterTableDropPrimaryKey x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropPrimaryKey x) {
    }
    
    default boolean visit(final SQLAlterTableDisableKeys x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDisableKeys x) {
    }
    
    default boolean visit(final SQLAlterTableEnableKeys x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableEnableKeys x) {
    }
    
    default boolean visit(final SQLAlterTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableStatement x) {
    }
    
    default boolean visit(final SQLAlterTableDisableConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDisableConstraint x) {
    }
    
    default boolean visit(final SQLAlterTableEnableConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableEnableConstraint x) {
    }
    
    default boolean visit(final SQLColumnCheck x) {
        return true;
    }
    
    default void endVisit(final SQLColumnCheck x) {
    }
    
    default boolean visit(final SQLExprHint x) {
        return true;
    }
    
    default void endVisit(final SQLExprHint x) {
    }
    
    default boolean visit(final SQLAlterTableDropConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropConstraint x) {
    }
    
    default boolean visit(final SQLUnique x) {
        return true;
    }
    
    default void endVisit(final SQLUnique x) {
    }
    
    default boolean visit(final SQLPrimaryKeyImpl x) {
        return true;
    }
    
    default void endVisit(final SQLPrimaryKeyImpl x) {
    }
    
    default boolean visit(final SQLCreateIndexStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateIndexStatement x) {
    }
    
    default boolean visit(final SQLAlterTableRenameColumn x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRenameColumn x) {
    }
    
    default boolean visit(final SQLColumnReference x) {
        return true;
    }
    
    default void endVisit(final SQLColumnReference x) {
    }
    
    default boolean visit(final SQLForeignKeyImpl x) {
        return true;
    }
    
    default void endVisit(final SQLForeignKeyImpl x) {
    }
    
    default boolean visit(final SQLDropSequenceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropSequenceStatement x) {
    }
    
    default boolean visit(final SQLDropTriggerStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropTriggerStatement x) {
    }
    
    default void endVisit(final SQLDropUserStatement x) {
    }
    
    default boolean visit(final SQLDropUserStatement x) {
        return true;
    }
    
    default void endVisit(final SQLExplainStatement x) {
    }
    
    default boolean visit(final SQLExplainStatement x) {
        return true;
    }
    
    default void endVisit(final SQLGrantStatement x) {
    }
    
    default boolean visit(final SQLGrantStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropDatabaseStatement x) {
    }
    
    default boolean visit(final SQLDropDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final SQLIndexOptions x) {
    }
    
    default boolean visit(final SQLIndexOptions x) {
        return true;
    }
    
    default void endVisit(final SQLIndexDefinition x) {
    }
    
    default boolean visit(final SQLIndexDefinition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddIndex x) {
    }
    
    default boolean visit(final SQLAlterTableAddIndex x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAlterIndex x) {
    }
    
    default boolean visit(final SQLAlterTableAlterIndex x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddConstraint x) {
    }
    
    default boolean visit(final SQLAlterTableAddConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLCreateTriggerStatement x) {
    }
    
    default boolean visit(final SQLCreateTriggerStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropFunctionStatement x) {
    }
    
    default boolean visit(final SQLDropFunctionStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropTableSpaceStatement x) {
    }
    
    default boolean visit(final SQLDropTableSpaceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropProcedureStatement x) {
    }
    
    default boolean visit(final SQLDropProcedureStatement x) {
        return true;
    }
    
    default void endVisit(final SQLBooleanExpr x) {
    }
    
    default boolean visit(final SQLBooleanExpr x) {
        return true;
    }
    
    default void endVisit(final SQLUnionQueryTableSource x) {
    }
    
    default boolean visit(final SQLUnionQueryTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLTimestampExpr x) {
    }
    
    default boolean visit(final SQLTimestampExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDateTimeExpr x) {
    }
    
    default boolean visit(final SQLDateTimeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDoubleExpr x) {
    }
    
    default boolean visit(final SQLDoubleExpr x) {
        return true;
    }
    
    default void endVisit(final SQLFloatExpr x) {
    }
    
    default boolean visit(final SQLFloatExpr x) {
        return true;
    }
    
    default void endVisit(final SQLRevokeStatement x) {
    }
    
    default boolean visit(final SQLRevokeStatement x) {
        return true;
    }
    
    default void endVisit(final SQLBinaryExpr x) {
    }
    
    default boolean visit(final SQLBinaryExpr x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRename x) {
    }
    
    default boolean visit(final SQLAlterTableRename x) {
        return true;
    }
    
    default void endVisit(final SQLAlterViewRenameStatement x) {
    }
    
    default boolean visit(final SQLAlterViewRenameStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowTablesStatement x) {
    }
    
    default boolean visit(final SQLShowTablesStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddPartition x) {
    }
    
    default boolean visit(final SQLAlterTableAddPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddExtPartition x) {
    }
    
    default boolean visit(final SQLAlterTableAddExtPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropExtPartition x) {
    }
    
    default boolean visit(final SQLAlterTableDropExtPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropPartition x) {
    }
    
    default boolean visit(final SQLAlterTableDropPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRenamePartition x) {
    }
    
    default boolean visit(final SQLAlterTableRenamePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSetComment x) {
    }
    
    default boolean visit(final SQLAlterTableSetComment x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSetLifecycle x) {
    }
    
    default boolean visit(final SQLPrivilegeItem x) {
        return true;
    }
    
    default void endVisit(final SQLPrivilegeItem x) {
    }
    
    default boolean visit(final SQLAlterTableSetLifecycle x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableEnableLifecycle x) {
    }
    
    default boolean visit(final SQLAlterTableSetLocation x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSetLocation x) {
    }
    
    default boolean visit(final SQLAlterTableEnableLifecycle x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTablePartition x) {
    }
    
    default boolean visit(final SQLAlterTablePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTablePartitionSetProperties x) {
    }
    
    default boolean visit(final SQLAlterTablePartitionSetProperties x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDisableLifecycle x) {
    }
    
    default boolean visit(final SQLAlterTableDisableLifecycle x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableTouch x) {
    }
    
    default boolean visit(final SQLAlterTableTouch x) {
        return true;
    }
    
    default void endVisit(final SQLArrayExpr x) {
    }
    
    default boolean visit(final SQLArrayExpr x) {
        return true;
    }
    
    default void endVisit(final SQLOpenStatement x) {
    }
    
    default boolean visit(final SQLOpenStatement x) {
        return true;
    }
    
    default void endVisit(final SQLFetchStatement x) {
    }
    
    default boolean visit(final SQLFetchStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCloseStatement x) {
    }
    
    default boolean visit(final SQLCloseStatement x) {
        return true;
    }
    
    default boolean visit(final SQLGroupingSetExpr x) {
        return true;
    }
    
    default void endVisit(final SQLGroupingSetExpr x) {
    }
    
    default boolean visit(final SQLIfStatement x) {
        return true;
    }
    
    default void endVisit(final SQLIfStatement x) {
    }
    
    default boolean visit(final SQLIfStatement.ElseIf x) {
        return true;
    }
    
    default void endVisit(final SQLIfStatement.ElseIf x) {
    }
    
    default boolean visit(final SQLIfStatement.Else x) {
        return true;
    }
    
    default void endVisit(final SQLIfStatement.Else x) {
    }
    
    default boolean visit(final SQLLoopStatement x) {
        return true;
    }
    
    default void endVisit(final SQLLoopStatement x) {
    }
    
    default boolean visit(final SQLParameter x) {
        return true;
    }
    
    default void endVisit(final SQLParameter x) {
    }
    
    default boolean visit(final SQLCreateProcedureStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateProcedureStatement x) {
    }
    
    default boolean visit(final SQLCreateFunctionStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateFunctionStatement x) {
    }
    
    default boolean visit(final SQLBlockStatement x) {
        return true;
    }
    
    default void endVisit(final SQLBlockStatement x) {
    }
    
    default boolean visit(final SQLAlterTableDropKey x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropKey x) {
    }
    
    default boolean visit(final SQLDeclareItem x) {
        return true;
    }
    
    default void endVisit(final SQLDeclareItem x) {
    }
    
    default boolean visit(final SQLPartitionValue x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionValue x) {
    }
    
    default boolean visit(final SQLPartition x) {
        return true;
    }
    
    default void endVisit(final SQLPartition x) {
    }
    
    default boolean visit(final SQLPartitionByRange x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionByRange x) {
    }
    
    default boolean visit(final SQLPartitionByHash x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionByHash x) {
    }
    
    default boolean visit(final SQLPartitionByList x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionByList x) {
    }
    
    default boolean visit(final SQLSubPartition x) {
        return true;
    }
    
    default void endVisit(final SQLSubPartition x) {
    }
    
    default boolean visit(final SQLSubPartitionByHash x) {
        return true;
    }
    
    default void endVisit(final SQLSubPartitionByHash x) {
    }
    
    default boolean visit(final SQLSubPartitionByRange x) {
        return true;
    }
    
    default void endVisit(final SQLSubPartitionByRange x) {
    }
    
    default boolean visit(final SQLSubPartitionByList x) {
        return true;
    }
    
    default void endVisit(final SQLSubPartitionByList x) {
    }
    
    default boolean visit(final SQLAlterDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterDatabaseStatement x) {
    }
    
    default boolean visit(final SQLAlterTableConvertCharSet x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableConvertCharSet x) {
    }
    
    default boolean visit(final SQLAlterTableReOrganizePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableReOrganizePartition x) {
    }
    
    default boolean visit(final SQLAlterTableCoalescePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableCoalescePartition x) {
    }
    
    default boolean visit(final SQLAlterTableTruncatePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableTruncatePartition x) {
    }
    
    default boolean visit(final SQLAlterTableDiscardPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDiscardPartition x) {
    }
    
    default boolean visit(final SQLAlterTableImportPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableImportPartition x) {
    }
    
    default boolean visit(final SQLAlterTableAnalyzePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAnalyzePartition x) {
    }
    
    default boolean visit(final SQLAlterTableCheckPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableCheckPartition x) {
    }
    
    default boolean visit(final SQLAlterTableOptimizePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableOptimizePartition x) {
    }
    
    default boolean visit(final SQLAlterTableRebuildPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRebuildPartition x) {
    }
    
    default boolean visit(final SQLAlterTableRepairPartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRepairPartition x) {
    }
    
    default boolean visit(final SQLSequenceExpr x) {
        return true;
    }
    
    default void endVisit(final SQLSequenceExpr x) {
    }
    
    default boolean visit(final SQLMergeStatement x) {
        return true;
    }
    
    default void endVisit(final SQLMergeStatement x) {
    }
    
    default boolean visit(final SQLMergeStatement.MergeUpdateClause x) {
        return true;
    }
    
    default void endVisit(final SQLMergeStatement.MergeUpdateClause x) {
    }
    
    default boolean visit(final SQLMergeStatement.MergeInsertClause x) {
        return true;
    }
    
    default void endVisit(final SQLMergeStatement.MergeInsertClause x) {
    }
    
    default boolean visit(final SQLErrorLoggingClause x) {
        return true;
    }
    
    default void endVisit(final SQLErrorLoggingClause x) {
    }
    
    default boolean visit(final SQLNullConstraint x) {
        return true;
    }
    
    default void endVisit(final SQLNullConstraint x) {
    }
    
    default boolean visit(final SQLCreateSequenceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateSequenceStatement x) {
    }
    
    default boolean visit(final SQLDateExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDateExpr x) {
    }
    
    default boolean visit(final SQLLimit x) {
        return true;
    }
    
    default void endVisit(final SQLLimit x) {
    }
    
    default void endVisit(final SQLStartTransactionStatement x) {
    }
    
    default boolean visit(final SQLStartTransactionStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDescribeStatement x) {
    }
    
    default boolean visit(final SQLDescribeStatement x) {
        return true;
    }
    
    default boolean visit(final SQLWhileStatement x) {
        return true;
    }
    
    default void endVisit(final SQLWhileStatement x) {
    }
    
    default boolean visit(final SQLDeclareStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDeclareStatement x) {
    }
    
    default boolean visit(final SQLReturnStatement x) {
        return true;
    }
    
    default void endVisit(final SQLReturnStatement x) {
    }
    
    default boolean visit(final SQLArgument x) {
        return true;
    }
    
    default void endVisit(final SQLArgument x) {
    }
    
    default boolean visit(final SQLCommitStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCommitStatement x) {
    }
    
    default boolean visit(final SQLFlashbackExpr x) {
        return true;
    }
    
    default void endVisit(final SQLFlashbackExpr x) {
    }
    
    default boolean visit(final SQLCreateMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLShowCreateMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowCreateMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLBinaryOpExprGroup x) {
        return true;
    }
    
    default void endVisit(final SQLBinaryOpExprGroup x) {
    }
    
    default boolean visit(final SQLScriptCommitStatement x) {
        return true;
    }
    
    default void endVisit(final SQLScriptCommitStatement x) {
    }
    
    default boolean visit(final SQLReplaceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLReplaceStatement x) {
    }
    
    default boolean visit(final SQLCreateUserStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateUserStatement x) {
    }
    
    default boolean visit(final SQLAlterFunctionStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterFunctionStatement x) {
    }
    
    default boolean visit(final SQLAlterTypeStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTypeStatement x) {
    }
    
    default boolean visit(final SQLIntervalExpr x) {
        return true;
    }
    
    default void endVisit(final SQLIntervalExpr x) {
    }
    
    default boolean visit(final SQLLateralViewTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLLateralViewTableSource x) {
    }
    
    default boolean visit(final SQLShowErrorsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowErrorsStatement x) {
    }
    
    default boolean visit(final SQLShowGrantsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowGrantsStatement x) {
    }
    
    default boolean visit(final SQLShowPackagesStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowPackagesStatement x) {
    }
    
    default boolean visit(final SQLShowRecylebinStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowRecylebinStatement x) {
    }
    
    default boolean visit(final SQLAlterCharacter x) {
        return true;
    }
    
    default void endVisit(final SQLAlterCharacter x) {
    }
    
    default boolean visit(final SQLExprStatement x) {
        return true;
    }
    
    default void endVisit(final SQLExprStatement x) {
    }
    
    default boolean visit(final SQLAlterProcedureStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterProcedureStatement x) {
    }
    
    default boolean visit(final SQLAlterViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterViewStatement x) {
    }
    
    default boolean visit(final SQLDropEventStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropEventStatement x) {
    }
    
    default boolean visit(final SQLDropLogFileGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropLogFileGroupStatement x) {
    }
    
    default boolean visit(final SQLDropServerStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropServerStatement x) {
    }
    
    default boolean visit(final SQLDropSynonymStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropSynonymStatement x) {
    }
    
    default boolean visit(final SQLRecordDataType x) {
        return true;
    }
    
    default void endVisit(final SQLRecordDataType x) {
    }
    
    default boolean visit(final SQLDropTypeStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropTypeStatement x) {
    }
    
    default boolean visit(final SQLExternalRecordFormat x) {
        return true;
    }
    
    default void endVisit(final SQLExternalRecordFormat x) {
    }
    
    default boolean visit(final SQLArrayDataType x) {
        return true;
    }
    
    default void endVisit(final SQLArrayDataType x) {
    }
    
    default boolean visit(final SQLMapDataType x) {
        return true;
    }
    
    default void endVisit(final SQLMapDataType x) {
    }
    
    default boolean visit(final SQLStructDataType x) {
        return true;
    }
    
    default void endVisit(final SQLStructDataType x) {
    }
    
    default boolean visit(final SQLRowDataType x) {
        return true;
    }
    
    default void endVisit(final SQLRowDataType x) {
    }
    
    default boolean visit(final SQLStructDataType.Field x) {
        return true;
    }
    
    default void endVisit(final SQLStructDataType.Field x) {
    }
    
    default boolean visit(final SQLDropMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLShowMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLRefreshMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLRefreshMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLAlterMaterializedViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterMaterializedViewStatement x) {
    }
    
    default boolean visit(final SQLCreateTableGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateTableGroupStatement x) {
    }
    
    default boolean visit(final SQLDropTableGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropTableGroupStatement x) {
    }
    
    default boolean visit(final SQLAlterTableSubpartitionAvailablePartitionNum x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSubpartitionAvailablePartitionNum x) {
    }
    
    default void endVisit(final SQLShowDatabasesStatement x) {
    }
    
    default boolean visit(final SQLShowDatabasesStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowTableGroupsStatement x) {
    }
    
    default boolean visit(final SQLShowTableGroupsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowColumnsStatement x) {
    }
    
    default boolean visit(final SQLShowColumnsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowCreateTableStatement x) {
    }
    
    default boolean visit(final SQLShowCreateTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowProcessListStatement x) {
    }
    
    default boolean visit(final SQLShowProcessListStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSetOption x) {
    }
    
    default boolean visit(final SQLAlterTableSetOption x) {
        return true;
    }
    
    default boolean visit(final SQLShowCreateViewStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowCreateViewStatement x) {
    }
    
    default boolean visit(final SQLShowViewsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowViewsStatement x) {
    }
    
    default boolean visit(final SQLAlterTableRenameIndex x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRenameIndex x) {
    }
    
    default boolean visit(final SQLAlterSequenceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterSequenceStatement x) {
    }
    
    default boolean visit(final SQLAlterTableExchangePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableExchangePartition x) {
    }
    
    default boolean visit(final SQLCreateRoleStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateRoleStatement x) {
    }
    
    default boolean visit(final SQLDropRoleStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropRoleStatement x) {
    }
    
    default boolean visit(final SQLAlterTableReplaceColumn x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableReplaceColumn x) {
    }
    
    default boolean visit(final SQLMatchAgainstExpr x) {
        return true;
    }
    
    default void endVisit(final SQLMatchAgainstExpr x) {
    }
    
    default boolean visit(final SQLTimeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLTimeExpr x) {
    }
    
    default boolean visit(final SQLDropCatalogStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropCatalogStatement x) {
    }
    
    default void endVisit(final SQLShowPartitionsStmt x) {
    }
    
    default boolean visit(final SQLShowPartitionsStmt x) {
        return true;
    }
    
    default void endVisit(final SQLValuesExpr x) {
    }
    
    default boolean visit(final SQLValuesExpr x) {
        return true;
    }
    
    default void endVisit(final SQLContainsExpr x) {
    }
    
    default boolean visit(final SQLContainsExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDumpStatement x) {
    }
    
    default boolean visit(final SQLDumpStatement x) {
        return true;
    }
    
    default void endVisit(final SQLValuesTableSource x) {
    }
    
    default boolean visit(final SQLValuesTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLExtractExpr x) {
    }
    
    default boolean visit(final SQLExtractExpr x) {
        return true;
    }
    
    default void endVisit(final SQLWindow x) {
    }
    
    default boolean visit(final SQLWindow x) {
        return true;
    }
    
    default void endVisit(final SQLJSONExpr x) {
    }
    
    default boolean visit(final SQLJSONExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDecimalExpr x) {
    }
    
    default boolean visit(final SQLDecimalExpr x) {
        return true;
    }
    
    default void endVisit(final SQLAnnIndex x) {
    }
    
    default boolean visit(final SQLAnnIndex x) {
        return true;
    }
    
    default void endVisit(final SQLUnionDataType x) {
    }
    
    default boolean visit(final SQLUnionDataType x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableRecoverPartitions x) {
    }
    
    default boolean visit(final SQLAlterTableRecoverPartitions x) {
        return true;
    }
    
    default void endVisit(final SQLAlterIndexStatement x) {
    }
    
    default boolean visit(final SQLAlterIndexStatement x) {
        return true;
    }
    
    default boolean visit(final SQLAlterIndexStatement.Rebuild x) {
        return true;
    }
    
    default void endVisit(final SQLAlterIndexStatement.Rebuild x) {
    }
    
    default boolean visit(final SQLShowIndexesStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowIndexesStatement x) {
    }
    
    default boolean visit(final SQLAnalyzeTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAnalyzeTableStatement x) {
    }
    
    default boolean visit(final SQLExportTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLExportTableStatement x) {
    }
    
    default boolean visit(final SQLImportTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLImportTableStatement x) {
    }
    
    default boolean visit(final SQLTableSampling x) {
        return true;
    }
    
    default void endVisit(final SQLTableSampling x) {
    }
    
    default boolean visit(final SQLSizeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLSizeExpr x) {
    }
    
    default boolean visit(final SQLAlterTableArchivePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableArchivePartition x) {
    }
    
    default boolean visit(final SQLAlterTableUnarchivePartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableUnarchivePartition x) {
    }
    
    default boolean visit(final SQLCreateOutlineStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateOutlineStatement x) {
    }
    
    default boolean visit(final SQLDropOutlineStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropOutlineStatement x) {
    }
    
    default boolean visit(final SQLAlterOutlineStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterOutlineStatement x) {
    }
    
    default boolean visit(final SQLShowOutlinesStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowOutlinesStatement x) {
    }
    
    default boolean visit(final SQLPurgeTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLPurgeTableStatement x) {
    }
    
    default boolean visit(final SQLPurgeTemporaryOutputStatement x) {
        return true;
    }
    
    default void endVisit(final SQLPurgeTemporaryOutputStatement x) {
    }
    
    default boolean visit(final SQLPurgeLogsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLPurgeLogsStatement x) {
    }
    
    default boolean visit(final SQLPurgeRecyclebinStatement x) {
        return true;
    }
    
    default void endVisit(final SQLPurgeRecyclebinStatement x) {
    }
    
    default boolean visit(final SQLShowStatisticStmt x) {
        return true;
    }
    
    default void endVisit(final SQLShowStatisticStmt x) {
    }
    
    default boolean visit(final SQLShowStatisticListStmt x) {
        return true;
    }
    
    default void endVisit(final SQLShowStatisticListStmt x) {
    }
    
    default boolean visit(final SQLAlterTableAddSupplemental x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddSupplemental x) {
    }
    
    default boolean visit(final SQLShowCatalogsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowCatalogsStatement x) {
    }
    
    default boolean visit(final SQLShowFunctionsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowFunctionsStatement x) {
    }
    
    default boolean visit(final SQLShowSessionStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowSessionStatement x) {
    }
    
    default boolean visit(final SQLDbLinkExpr x) {
        return true;
    }
    
    default void endVisit(final SQLDbLinkExpr x) {
    }
    
    default boolean visit(final SQLCurrentTimeExpr x) {
        return true;
    }
    
    default void endVisit(final SQLCurrentTimeExpr x) {
    }
    
    default boolean visit(final SQLCurrentUserExpr x) {
        return true;
    }
    
    default void endVisit(final SQLCurrentUserExpr x) {
    }
    
    default boolean visit(final SQLShowQueryTaskStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowQueryTaskStatement x) {
    }
    
    default boolean visit(final SQLAdhocTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLAdhocTableSource x) {
    }
    
    default boolean visit(final HiveCreateTableStatement x) {
        return true;
    }
    
    default void endVisit(final HiveCreateTableStatement x) {
    }
    
    default boolean visit(final HiveInputOutputFormat x) {
        return true;
    }
    
    default void endVisit(final HiveInputOutputFormat x) {
    }
    
    default boolean visit(final SQLExplainAnalyzeStatement x) {
        return true;
    }
    
    default void endVisit(final SQLExplainAnalyzeStatement x) {
    }
    
    default boolean visit(final SQLPartitionRef x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionRef x) {
    }
    
    default boolean visit(final SQLPartitionRef.Item x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionRef.Item x) {
    }
    
    default boolean visit(final SQLWhoamiStatement x) {
        return true;
    }
    
    default void endVisit(final SQLWhoamiStatement x) {
    }
    
    default boolean visit(final SQLDropResourceStatement x) {
        return true;
    }
    
    default void endVisit(final SQLDropResourceStatement x) {
    }
    
    default boolean visit(final SQLForStatement x) {
        return true;
    }
    
    default void endVisit(final SQLForStatement x) {
    }
    
    default boolean visit(final SQLUnnestTableSource x) {
        return true;
    }
    
    default void endVisit(final SQLUnnestTableSource x) {
    }
    
    default boolean visit(final SQLCopyFromStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCopyFromStatement x) {
    }
    
    default boolean visit(final SQLShowUsersStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowUsersStatement x) {
    }
    
    default boolean visit(final SQLSubmitJobStatement x) {
        return true;
    }
    
    default void endVisit(final SQLSubmitJobStatement x) {
    }
    
    default boolean visit(final SQLTableLike x) {
        return true;
    }
    
    default void endVisit(final SQLTableLike x) {
    }
    
    default boolean visit(final SQLSyncMetaStatement x) {
        return true;
    }
    
    default void endVisit(final SQLSyncMetaStatement x) {
    }
    
    default void endVisit(final SQLValuesQuery x) {
    }
    
    default boolean visit(final SQLValuesQuery x) {
        return true;
    }
    
    default void endVisit(final SQLDataTypeRefExpr x) {
    }
    
    default boolean visit(final SQLDataTypeRefExpr x) {
        return true;
    }
    
    default void endVisit(final SQLArchiveTableStatement x) {
    }
    
    default boolean visit(final SQLArchiveTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLBackupStatement x) {
    }
    
    default boolean visit(final SQLBackupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLRestoreStatement x) {
    }
    
    default boolean visit(final SQLRestoreStatement x) {
        return true;
    }
    
    default void endVisit(final SQLBuildTableStatement x) {
    }
    
    default boolean visit(final SQLBuildTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCancelJobStatement x) {
    }
    
    default boolean visit(final SQLCancelJobStatement x) {
        return true;
    }
    
    default void endVisit(final SQLExportDatabaseStatement x) {
    }
    
    default boolean visit(final SQLExportDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final SQLImportDatabaseStatement x) {
    }
    
    default boolean visit(final SQLImportDatabaseStatement x) {
        return true;
    }
    
    default void endVisit(final SQLRenameUserStatement x) {
    }
    
    default boolean visit(final SQLRenameUserStatement x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionByValue x) {
    }
    
    default boolean visit(final SQLPartitionByValue x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTablePartitionCount x) {
    }
    
    default boolean visit(final SQLAlterTablePartitionCount x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableBlockSize x) {
    }
    
    default boolean visit(final SQLAlterTableBlockSize x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableCompression x) {
    }
    
    default boolean visit(final SQLAlterTableCompression x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTablePartitionLifecycle x) {
    }
    
    default boolean visit(final SQLAlterTablePartitionLifecycle x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableSubpartitionLifecycle x) {
    }
    
    default boolean visit(final SQLAlterTableSubpartitionLifecycle x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropSubpartition x) {
    }
    
    default boolean visit(final SQLAlterTableDropSubpartition x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableDropClusteringKey x) {
    }
    
    default boolean visit(final SQLAlterTableDropClusteringKey x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableAddClusteringKey x) {
    }
    
    default boolean visit(final SQLAlterTableAddClusteringKey x) {
        return true;
    }
    
    default void endVisit(final MySqlKillStatement x) {
    }
    
    default boolean visit(final MySqlKillStatement x) {
        return true;
    }
    
    default boolean visit(final SQLCreateResourceGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLCreateResourceGroupStatement x) {
    }
    
    default boolean visit(final SQLAlterResourceGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterResourceGroupStatement x) {
    }
    
    default void endVisit(final SQLDropResourceGroupStatement x) {
    }
    
    default boolean visit(final SQLDropResourceGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLListResourceGroupStatement x) {
    }
    
    default boolean visit(final SQLListResourceGroupStatement x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableMergePartition x) {
    }
    
    default boolean visit(final SQLAlterTableMergePartition x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionSpec x) {
    }
    
    default boolean visit(final SQLPartitionSpec x) {
        return true;
    }
    
    default void endVisit(final SQLPartitionSpec.Item x) {
    }
    
    default boolean visit(final SQLPartitionSpec.Item x) {
        return true;
    }
    
    default void endVisit(final SQLAlterTableChangeOwner x) {
    }
    
    default boolean visit(final SQLAlterTableChangeOwner x) {
        return true;
    }
    
    default void endVisit(final SQLTableDataType x) {
    }
    
    default boolean visit(final SQLTableDataType x) {
        return true;
    }
    
    default void endVisit(final SQLCloneTableStatement x) {
    }
    
    default boolean visit(final SQLCloneTableStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowHistoryStatement x) {
    }
    
    default boolean visit(final SQLShowHistoryStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowRoleStatement x) {
    }
    
    default boolean visit(final SQLShowRoleStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowRolesStatement x) {
    }
    
    default boolean visit(final SQLShowRolesStatement x) {
        return true;
    }
    
    default boolean visit(final SQLShowVariantsStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowVariantsStatement x) {
    }
    
    default boolean visit(final SQLShowACLStatement x) {
        return true;
    }
    
    default void endVisit(final SQLShowACLStatement x) {
    }
}
