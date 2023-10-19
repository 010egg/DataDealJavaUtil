// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLCopyFromStatement;
import java.sql.Date;
import java.sql.Timestamp;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlashbackStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.ConditionValue;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlHandlerType;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlIterateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlLeaveStatement;
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhileStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlChecksumTableStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseKillJob;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseSetOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLRenameUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropSubpartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropClusteringKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterCharacter;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOrderBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableLock;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterTableAlterCheck;
import com.alibaba.druid.sql.dialect.mysql.ast.AnalyzerIndexType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterFullTextIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSubpartitionAvailablePartitionNum;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableModifyClusteredBy;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableBlockSize;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSubpartitionLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionCount;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableValidation;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableForce;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlEventSchedule;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateEventStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterProcedureStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterLogFileGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateAddLogFileGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTablespaceStatement;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemGetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemSetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlManageInstanceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterIndexStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.repository.SchemaObject;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.parser.EOFParserException;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.parser.InsertColumnsCache;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRollbackDDLJob;
import com.alibaba.druid.sql.ast.statement.SQLShowTableGroupsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlowStatement;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowMetadataLock;
import com.alibaba.druid.sql.dialect.mysql.ast.FullTextType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPhysicalProcesslistStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowUsersStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowFunctionsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCatalogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRecylebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowOutlinesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowQueryTaskStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTopologyStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTraceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDdlStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowDDLJobs;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatusStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSequencesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowNodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatasourcesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBroadcastsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowStcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHMSMetaStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowHtcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowIndexesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabaseStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateMaterializedViewStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowCreateFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateViewStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMigrateTaskStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowJobStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowClusterNameStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alibaba.druid.sql.ast.statement.SQLShowSessionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowGlobalIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowDbLockStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.alibaba.druid.sql.ast.statement.SQLSyncMetaStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubmitJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportDatabaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlMigrateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftMemberChangeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftLeaderTransferStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsCancelDDLJob;
import com.alibaba.druid.sql.ast.statement.SQLCancelJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLBuildTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLRestoreStatement;
import com.alibaba.druid.sql.ast.statement.SQLBackupStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLArchiveTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteForAdsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCheckTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsBaselineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsChangeDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsInspectDDLJobCache;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRemoveDDLJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsRecoverDDLJob;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDisabledPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsClearDDLJobCache;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlClearPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.ArrayList;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableSpaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateOutlineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenFilterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextCharFilterStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.expr.SQLTextLiteralExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenizerStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextAnalyzerStatement;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextDictionaryStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.util.FnvHash;
import java.util.List;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class MySqlStatementParser extends SQLStatementParser
{
    private static final String AUTO_INCREMENT = "AUTO_INCREMENT";
    private static final String AVG_ROW_LENGTH = "AVG_ROW_LENGTH";
    private static final String CHECKSUM2 = "CHECKSUM";
    private static final String DELAY_KEY_WRITE = "DELAY_KEY_WRITE";
    private static final String ENCRYPTION2 = "ENCRYPTION";
    private static final String INSERT_METHOD = "INSERT_METHOD";
    private static final String KEY_BLOCK_SIZE2 = "KEY_BLOCK_SIZE";
    private static final String MAX_ROWS2 = "MAX_ROWS";
    private static final String MIN_ROWS2 = "MIN_ROWS";
    private static final String PASSWORD2 = "PASSWORD";
    private static final String STATS_AUTO_RECALC = "STATS_AUTO_RECALC";
    private static final String STATS_PERSISTENT = "STATS_PERSISTENT";
    private static final String STATS_SAMPLE_PAGES = "STATS_SAMPLE_PAGES";
    private static final String TABLESPACE2 = "TABLESPACE";
    private static final String CHAIN = "CHAIN";
    private static final String ENGINES = "ENGINES";
    private static final String ENGINE = "ENGINE";
    private static final String BINLOG = "BINLOG";
    private static final String EVENTS = "EVENTS";
    private static final String GLOBAL = "GLOBAL";
    private static final String VARIABLES = "VARIABLES";
    private static final String STATUS = "STATUS";
    private static final String DBLOCK = "DBLOCK";
    private static final String RESET = "RESET";
    private static final String DESCRIBE = "DESCRIBE";
    private static final String WRITE = "WRITE";
    private static final String READ = "READ";
    private static final String LOCAL = "LOCAL";
    private static final String TABLES = "TABLES";
    private static final String CONNECTION = "CONNECTION";
    private int maxIntoClause;
    
    public MySqlStatementParser(final String sql) {
        super(new MySqlExprParser(sql));
        this.maxIntoClause = -1;
    }
    
    public MySqlStatementParser(final String sql, final SQLParserFeature... features) {
        super(new MySqlExprParser(sql, features));
        this.maxIntoClause = -1;
    }
    
    public MySqlStatementParser(final String sql, final boolean keepComments) {
        super(new MySqlExprParser(sql, keepComments));
        this.maxIntoClause = -1;
    }
    
    public MySqlStatementParser(final String sql, final boolean skipComment, final boolean keepComments) {
        super(new MySqlExprParser(sql, skipComment, keepComments));
        this.maxIntoClause = -1;
    }
    
    public MySqlStatementParser(final Lexer lexer) {
        super(new MySqlExprParser(lexer));
        this.maxIntoClause = -1;
    }
    
    public int getMaxIntoClause() {
        return this.maxIntoClause;
    }
    
    public void setMaxIntoClause(final int maxIntoClause) {
        this.maxIntoClause = maxIntoClause;
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        final MySqlCreateTableParser parser = new MySqlCreateTableParser(this.exprParser);
        return parser.parseCreateTable();
    }
    
    @Override
    public SQLStatement parseSelect() {
        final MySqlSelectParser selectParser = this.createSQLSelectParser();
        final SQLSelect select = selectParser.select();
        if (selectParser.returningFlag) {
            return selectParser.updateStmt;
        }
        return new SQLSelectStatement(select, DbType.mysql);
    }
    
    @Override
    public SQLUpdateStatement parseUpdateStatement() {
        return new MySqlSelectParser(this.exprParser, this.selectListCache).parseUpdateStatment();
    }
    
    @Override
    protected MySqlUpdateStatement createUpdateStatement() {
        return new MySqlUpdateStatement();
    }
    
    @Override
    public MySqlDeleteStatement parseDeleteStatement() {
        final MySqlDeleteStatement deleteStatement = new MySqlDeleteStatement();
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            final List<String> comments = this.lexer.readAndResetComments();
            if (comments != null) {
                deleteStatement.addBeforeComment(comments);
            }
        }
        if (this.lexer.token() == Token.DELETE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.HINT) {
                this.getExprParser().parseHints(deleteStatement.getHints());
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
                deleteStatement.setLowPriority(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("QUICK")) {
                deleteStatement.setQuick(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
                deleteStatement.setIgnore(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                final Lexer.SavePoint savePoint = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token() == Token.ALL) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("PARTITIONS");
                    deleteStatement.setForceAllPartitions(true);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
                    this.lexer.nextToken();
                    deleteStatement.setForceAllPartitions(true);
                }
                else if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    final SQLName partition = this.exprParser.name();
                    deleteStatement.setForcePartition(partition);
                }
                else {
                    this.lexer.reset(savePoint);
                }
            }
            if (this.lexer.token() == Token.IDENTIFIER) {
                deleteStatement.setTableSource(this.createSQLSelectParser().parseTableSource());
                if (this.lexer.token() == Token.FROM) {
                    this.lexer.nextToken();
                    final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSource();
                    deleteStatement.setFrom(tableSource);
                }
            }
            else {
                if (this.lexer.token() != Token.FROM) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                this.lexer.nextToken();
                if (this.lexer.token() == Token.FULLTEXT) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.DICTIONARY)) {
                        this.lexer.nextToken();
                        deleteStatement.setFulltextDictionary(true);
                    }
                }
                deleteStatement.setTableSource(this.createSQLSelectParser().parseTableSource());
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                this.lexer.nextToken();
                final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSource();
                deleteStatement.setUsing(tableSource);
            }
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            deleteStatement.setWhere(where);
        }
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
            deleteStatement.setOrderBy(orderBy);
        }
        if (this.lexer.token() == Token.LIMIT) {
            deleteStatement.setLimit(this.exprParser.parseLimit());
        }
        if (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
            throw new ParserException("syntax error. " + this.lexer.info());
        }
        return deleteStatement;
    }
    
    @Override
    public SQLStatement parseCreate() {
        List<String> comments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            comments = this.lexer.readAndResetComments();
        }
        final Lexer.SavePoint mark = this.lexer.mark();
        this.accept(Token.CREATE);
        boolean replace = false;
        if (this.lexer.token() == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            replace = true;
        }
        boolean physical = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.PHYSICAL)) {
            this.lexer.nextToken();
            physical = true;
        }
        if (this.lexer.token() == Token.GROUP) {
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SIMPLE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.WITH) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SEQUENCE) {
                this.lexer.reset(mark);
                return this.parseCreateSequence(true);
            }
        }
        final List<SQLCommentHint> hints = this.exprParser.parseHints();
        boolean isExternal = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            isExternal = true;
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.CATALOG)) {
                this.lexer.reset(mark);
                return this.parseCreateExternalCatalog();
            }
        }
        if (this.lexer.token() == Token.TABLE || this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY) || isExternal || this.lexer.identifierEquals("SHADOW")) {
            this.lexer.reset(mark);
            final MySqlCreateTableParser parser = new MySqlCreateTableParser(this.exprParser);
            final MySqlCreateTableStatement stmt = parser.parseCreateTable(true);
            stmt.setHints(hints);
            if (comments != null) {
                stmt.addBeforeComment(comments);
            }
            return stmt;
        }
        switch (this.lexer.token()) {
            case DATABASE:
            case SCHEMA: {
                if (replace) {
                    this.lexer.reset(mark);
                }
                final SQLCreateDatabaseStatement stmt2 = (SQLCreateDatabaseStatement)this.parseCreateDatabase();
                if (physical) {
                    stmt2.setPhysical(true);
                }
                return stmt2;
            }
            case USER: {
                if (replace) {
                    this.lexer.reset(mark);
                }
                return this.parseCreateUser();
            }
            case TRIGGER: {
                this.lexer.reset(mark);
                return this.parseCreateTrigger();
            }
            case PROCEDURE: {
                if (replace) {
                    this.lexer.reset(mark);
                }
                return this.parseCreateProcedure();
            }
            case FUNCTION: {
                if (replace) {
                    this.lexer.reset(mark);
                }
                return this.parseCreateFunction();
            }
            case SEQUENCE: {
                this.lexer.reset(mark);
                return this.parseCreateSequence(true);
            }
            case FULLTEXT: {
                this.lexer.reset(mark);
                return this.parseCreateFullTextStatement();
            }
            default: {
                if (this.lexer.token() == Token.UNIQUE || this.lexer.token() == Token.INDEX || this.lexer.token() == Token.FULLTEXT || this.lexer.identifierEquals(FnvHash.Constants.SPATIAL) || this.lexer.identifierEquals(FnvHash.Constants.ANN) || this.lexer.identifierEquals(FnvHash.Constants.GLOBAL) || this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
                    if (replace) {
                        this.lexer.reset(mark);
                    }
                    return this.parseCreateIndex(false);
                }
                if (this.lexer.token() == Token.VIEW || this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
                    if (replace) {
                        this.lexer.reset(mark);
                    }
                    return this.parseCreateView();
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.EVENT)) {
                    this.lexer.reset(mark);
                    return this.parseCreateEvent();
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    this.getExprParser().userName();
                    if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("SECURITY");
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        this.lexer.nextToken();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.EVENT)) {
                        this.lexer.reset(mark);
                        return this.parseCreateEvent();
                    }
                    if (this.lexer.token() == Token.TRIGGER) {
                        this.lexer.reset(mark);
                        return this.parseCreateTrigger();
                    }
                    if (this.lexer.token() == Token.VIEW) {
                        this.lexer.reset(mark);
                        return this.parseCreateView();
                    }
                    if (this.lexer.token() == Token.FUNCTION) {
                        this.lexer.reset(mark);
                        return this.parseCreateFunction();
                    }
                    this.lexer.reset(mark);
                    return this.parseCreateProcedure();
                }
                else {
                    if (this.lexer.identifierEquals(FnvHash.Constants.LOGFILE)) {
                        return this.parseCreateLogFileGroup();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.SERVER)) {
                        return this.parseCreateServer();
                    }
                    if (this.lexer.token() == Token.TABLESPACE) {
                        return this.parseCreateTableSpace();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.DIMENSION)) {
                        this.lexer.reset(mark);
                        return this.parseCreateTable();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUP)) {
                        this.lexer.reset(mark);
                        return this.parseCreateTableGroup();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.OUTLINE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateOutline();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                        this.lexer.reset(mark);
                        return this.parseCreateIndex(true);
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateResourceGroup();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                        this.lexer.reset(mark);
                        return this.parseCreateMaterializedView();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.ROLE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateRole();
                    }
                    throw new ParserException("TODO " + this.lexer.info());
                }
                break;
            }
        }
    }
    
    public SQLStatement parseCreateFullTextStatement() {
        final Lexer.SavePoint mark = this.lexer.mark();
        this.accept(Token.CREATE);
        this.accept(Token.FULLTEXT);
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARFILTER)) {
            this.lexer.nextToken();
            return this.parseFullTextCharFilter();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TOKENIZER)) {
            this.lexer.nextToken();
            return this.parseFullTextTokenizer();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TOKENFILTER)) {
            this.lexer.nextToken();
            return this.parseFullTextTokenFilter();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ANALYZER)) {
            this.lexer.nextToken();
            return this.parseFullTextAnalyzer();
        }
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.reset(mark);
            return this.parseCreateIndex(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DICTIONARY)) {
            this.lexer.nextToken();
            final MysqlCreateFullTextDictionaryStatement stmt = new MysqlCreateFullTextDictionaryStatement();
            final SQLName name = this.exprParser.name();
            stmt.setName(name);
            this.accept(Token.LPAREN);
            final SQLColumnDefinition col = new SQLColumnDefinition();
            col.setName(this.exprParser.name());
            this.acceptIdentifier("varchar");
            col.setDataType(new SQLDataTypeImpl("varchar"));
            if (this.lexer.token() == Token.COMMENT) {
                this.accept(Token.COMMENT);
                col.setComment(this.exprParser.name());
            }
            stmt.setColumn(col);
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.COMMENT) {
                this.accept(Token.COMMENT);
                stmt.setComment(this.exprParser.name().getSimpleName());
            }
            return stmt;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    private SQLStatement parseFullTextAnalyzer() {
        final MysqlCreateFullTextAnalyzerStatement stmt = new MysqlCreateFullTextAnalyzerStatement();
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.LPAREN);
        while (true) {
            String key = "";
            if (this.lexer.token() == Token.LITERAL_ALIAS || this.lexer.token() == Token.LITERAL_CHARS) {
                key = StringUtils.removeNameQuotes(this.lexer.stringVal());
                if (key.equalsIgnoreCase("charfilter")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    this.accept(Token.LBRACKET);
                    while (true) {
                        final String c = SQLUtils.normalize(this.exprParser.name().getSimpleName());
                        stmt.getCharfilters().add(c);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RBRACKET);
                }
                else if (key.equalsIgnoreCase("tokenfilter")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    this.accept(Token.LBRACKET);
                    while (true) {
                        final String c = SQLUtils.normalize(this.exprParser.name().getSimpleName());
                        stmt.getTokenizers().add(c);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RBRACKET);
                }
                else if (key.equalsIgnoreCase("tokenizer")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    stmt.setTokenizer(SQLUtils.normalize(this.exprParser.name().getSimpleName()));
                }
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        final String tokenizer = stmt.getTokenizer();
        if (tokenizer == null || StringUtils.isEmpty(tokenizer)) {
            throw new ParserException("tokenizer is require.");
        }
        return stmt;
    }
    
    private SQLStatement parseFullTextTokenizer() {
        final MysqlCreateFullTextTokenizerStatement stmt = new MysqlCreateFullTextTokenizerStatement();
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.LPAREN);
        while (true) {
            final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
            assignItem.setParent(stmt);
            final SQLExpr target = assignItem.getTarget();
            if ("type".equalsIgnoreCase(((SQLTextLiteralExpr)target).getText())) {
                stmt.setTypeName((SQLTextLiteralExpr)assignItem.getValue());
            }
            else if ("user_defined_dict".equalsIgnoreCase(((SQLTextLiteralExpr)target).getText())) {
                stmt.setUserDefinedDict((SQLTextLiteralExpr)assignItem.getValue());
            }
            else {
                stmt.getOptions().add(assignItem);
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        final SQLTextLiteralExpr typeName = stmt.getTypeName();
        if (typeName == null || StringUtils.isEmpty(typeName.getText())) {
            throw new ParserException("type is require.");
        }
        return stmt;
    }
    
    private SQLStatement parseFullTextCharFilter() {
        final MysqlCreateFullTextCharFilterStatement stmt = new MysqlCreateFullTextCharFilterStatement();
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.LPAREN);
        while (true) {
            final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
            assignItem.setParent(stmt);
            if ("type".equalsIgnoreCase(((SQLTextLiteralExpr)assignItem.getTarget()).getText())) {
                stmt.setTypeName((SQLTextLiteralExpr)assignItem.getValue());
            }
            else {
                stmt.getOptions().add(assignItem);
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        final SQLTextLiteralExpr typeName = stmt.getTypeName();
        if (typeName == null || StringUtils.isEmpty(typeName.getText())) {
            throw new ParserException("type is require.");
        }
        return stmt;
    }
    
    private SQLStatement parseFullTextTokenFilter() {
        final MysqlCreateFullTextTokenFilterStatement stmt = new MysqlCreateFullTextTokenFilterStatement();
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.LPAREN);
        while (true) {
            final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
            assignItem.setParent(stmt);
            if ("type".equalsIgnoreCase(((SQLTextLiteralExpr)assignItem.getTarget()).getText())) {
                stmt.setTypeName((SQLTextLiteralExpr)assignItem.getValue());
            }
            else {
                stmt.getOptions().add(assignItem);
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        final SQLTextLiteralExpr typeName = stmt.getTypeName();
        if (typeName == null || StringUtils.isEmpty(typeName.getText())) {
            throw new ParserException("type is require.");
        }
        return stmt;
    }
    
    public SQLStatement parseCreateOutline() {
        this.accept(Token.CREATE);
        this.acceptIdentifier("OUTLINE");
        final SQLCreateOutlineStatement stmt = new SQLCreateOutlineStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            stmt.setWhere(this.exprParser.expr());
        }
        this.accept(Token.ON);
        final SQLStatement on = this.parseStatement();
        stmt.setOn(on);
        this.accept(Token.TO);
        final SQLStatement to = this.parseStatement();
        stmt.setTo(to);
        return stmt;
    }
    
    public SQLStatement parseCreateTableSpace() {
        if (this.lexer.token() == Token.CREATE) {
            this.accept(Token.CREATE);
        }
        final MySqlCreateTableSpaceStatement stmt = new MySqlCreateTableSpaceStatement();
        this.accept(Token.TABLESPACE);
        stmt.setName(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
            this.lexer.nextToken();
            this.acceptIdentifier("DATAFILE");
            final SQLExpr file = this.exprParser.primary();
            stmt.setAddDataFile(file);
        }
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.INITIAL_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr initialSize = this.exprParser.expr();
                stmt.setInitialSize(initialSize);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.FILE_BLOCK_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr fileBlockSize = this.exprParser.expr();
                stmt.setFileBlockSize(fileBlockSize);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.EXTENT_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr extentSize = this.exprParser.expr();
                stmt.setExtentSize(extentSize);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.AUTOEXTEND_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr extentSize = this.exprParser.expr();
                stmt.setAutoExtentSize(extentSize);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MAX_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr size = this.exprParser.expr();
                stmt.setMaxSize(size);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NODEGROUP)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr size = this.exprParser.expr();
                stmt.setNodeGroup(size);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.WAIT)) {
                this.lexer.nextToken();
                stmt.setWait(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr engine = this.exprParser.expr();
                stmt.setEngine(engine);
            }
            else if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                final SQLExpr comment = this.exprParser.expr();
                stmt.setComment(comment);
            }
            else {
                if (this.lexer.token() != Token.USE) {
                    break;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("LOGFILE");
                this.accept(Token.GROUP);
                final SQLExpr logFileGroup = this.exprParser.expr();
                stmt.setFileBlockSize(logFileGroup);
            }
        }
        return stmt;
    }
    
    public SQLStatement parseCreateServer() {
        if (this.lexer.token() == Token.CREATE) {
            this.accept(Token.CREATE);
        }
        final MySqlCreateServerStatement stmt = new MySqlCreateServerStatement();
        this.acceptIdentifier("SERVER");
        stmt.setName(this.exprParser.name());
        this.accept(Token.FOREIGN);
        this.acceptIdentifier("DATA");
        this.acceptIdentifier("WRAPPER");
        stmt.setForeignDataWrapper(this.exprParser.name());
        this.acceptIdentifier("OPTIONS");
        this.accept(Token.LPAREN);
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.HOST)) {
                this.lexer.nextToken();
                final SQLExpr host = this.exprParser.expr();
                stmt.setHost(host);
            }
            else if (this.lexer.token() == Token.USER) {
                this.lexer.nextToken();
                final SQLExpr user = this.exprParser.expr();
                stmt.setUser(user);
            }
            else if (this.lexer.token() == Token.DATABASE) {
                this.lexer.nextToken();
                final SQLExpr db = this.exprParser.expr();
                stmt.setDatabase(db);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PASSWORD)) {
                this.lexer.nextToken();
                final SQLExpr pwd = this.exprParser.expr();
                stmt.setPassword(pwd);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SOCKET)) {
                this.lexer.nextToken();
                final SQLExpr sock = this.exprParser.expr();
                stmt.setSocket(sock);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.OWNER)) {
                this.lexer.nextToken();
                final SQLExpr owner = this.exprParser.expr();
                stmt.setOwner(owner);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PORT)) {
                this.lexer.nextToken();
                final SQLExpr port = this.exprParser.expr();
                stmt.setPort(port);
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        return stmt;
    }
    
    @Override
    public SQLCreateIndexStatement parseCreateIndex(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        final SQLCreateIndexStatement stmt = new SQLCreateIndexStatement();
        this.exprParser.parseIndex(stmt.getIndexDefinition());
        return stmt;
    }
    
    private void parseCreateIndexUsing(final SQLCreateIndexStatement stmt) {
        if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.BTREE)) {
                stmt.setUsing("BTREE");
                this.lexer.nextToken();
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.HASH)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                stmt.setUsing("HASH");
                this.lexer.nextToken();
            }
        }
    }
    
    @Override
    public SQLStatement parseCreateUser() {
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
        }
        this.accept(Token.USER);
        final MySqlCreateUserStatement stmt = new MySqlCreateUserStatement();
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        while (true) {
            final MySqlCreateUserStatement.UserSpecification userSpec = new MySqlCreateUserStatement.UserSpecification();
            if (this.lexer.token() == Token.IF) {
                this.lexer.nextToken();
                this.accept(Token.NOT);
                this.accept(Token.EXISTS);
                stmt.setIfNotExists(true);
            }
            SQLExpr expr = this.exprParser.primary();
            if (expr instanceof SQLCharExpr) {
                expr = new SQLIdentifierExpr(((SQLCharExpr)expr).getText());
            }
            if (expr instanceof SQLIdentifierExpr && this.lexer.token() == Token.VARIANT && this.lexer.stringVal().charAt(0) == '@') {
                final String str = this.lexer.stringVal();
                final MySqlUserName mySqlUserName = new MySqlUserName();
                mySqlUserName.setUserName(((SQLIdentifierExpr)expr).getName());
                mySqlUserName.setHost(str.substring(1));
                expr = mySqlUserName;
                this.lexer.nextToken();
            }
            userSpec.setUser(expr);
            if (this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.BY) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("PASSWORD")) {
                        this.lexer.nextToken();
                        userSpec.setPasswordHash(true);
                    }
                    final SQLExpr password = this.exprParser.expr();
                    if (!(password instanceof SQLIdentifierExpr) && !(password instanceof SQLCharExpr)) {
                        throw new ParserException("syntax error. invalid " + password + " expression.");
                    }
                    userSpec.setPassword(password);
                }
                else if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    userSpec.setAuthPlugin(this.exprParser.expr());
                    if (this.lexer.token() == Token.BY || this.lexer.token() == Token.AS) {
                        userSpec.setPluginAs(this.lexer.token() == Token.AS);
                        this.lexer.nextToken();
                        if (userSpec.isPluginAs()) {
                            final String psw = this.lexer.stringVal();
                            if (psw.length() >= 2 && '\'' == psw.charAt(0) && '\'' == psw.charAt(psw.length() - 1)) {
                                userSpec.setPassword(new SQLCharExpr(psw.substring(1, psw.length() - 1)));
                            }
                            else {
                                userSpec.setPassword(new SQLCharExpr(psw));
                            }
                            this.lexer.nextToken();
                        }
                        else {
                            userSpec.setPassword(this.exprParser.charExpr());
                        }
                    }
                }
            }
            stmt.addUser(userSpec);
            if (this.lexer.token() != Token.COMMA) {
                return stmt;
            }
            this.lexer.nextToken();
        }
    }
    
    @Override
    public SQLStatement parseKill() {
        this.accept(Token.KILL);
        final MySqlKillStatement stmt = new MySqlKillStatement();
        if (this.lexer.identifierEquals("CONNECTION")) {
            stmt.setType(MySqlKillStatement.Type.CONNECTION);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.QUERY) || this.lexer.identifierEquals(FnvHash.Constants.PROCESS)) {
            stmt.setType(MySqlKillStatement.Type.QUERY);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() != Token.LITERAL_INT) {
            if (this.lexer.token() != Token.LITERAL_CHARS) {
                if (this.lexer.token() != Token.ALL) {
                    throw new ParserException("not support kill type " + this.lexer.token() + ". " + this.lexer.info());
                }
                final SQLIdentifierExpr all = new SQLIdentifierExpr(this.lexer.stringVal());
                all.setParent(stmt);
                stmt.getThreadIds().add(all);
                this.lexer.nextToken();
            }
        }
        this.exprParser.exprList(stmt.getThreadIds(), stmt);
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
            stmt.setAfterSemi(true);
        }
        return stmt;
    }
    
    public SQLStatement parseBinlog() {
        this.acceptIdentifier("binlog");
        final MySqlBinlogStatement stmt = new MySqlBinlogStatement();
        final SQLExpr expr = this.exprParser.expr();
        stmt.setExpr(expr);
        return stmt;
    }
    
    public MySqlAnalyzeStatement parseAnalyze() {
        this.accept(Token.ANALYZE);
        final MySqlAnalyzeStatement stmt = new MySqlAnalyzeStatement();
        if (this.lexer.token() == Token.TABLE) {
            this.accept(Token.TABLE);
            final List<SQLName> names = new ArrayList<SQLName>();
            this.exprParser.names(names, stmt);
            for (final SQLName name : names) {
                stmt.addTableSource(new SQLExprTableSource(name));
            }
            if (this.lexer.token() == Token.WHERE) {
                this.accept(Token.WHERE);
                final SQLExpr where = this.exprParser.expr();
                stmt.setAdbWhere(where);
            }
        }
        else if (this.lexer.token() == Token.DATABASE) {
            this.accept(Token.DATABASE);
            final SQLName name2 = this.exprParser.name();
            stmt.setAdbSchema((SQLIdentifierExpr)name2);
        }
        else if (this.lexer.token() == Token.COLUMN) {
            this.accept(Token.COLUMN);
            final SQLName table = this.exprParser.name();
            stmt.setTable(table);
            this.accept(Token.LPAREN);
            while (true) {
                final SQLName name3 = this.exprParser.name();
                stmt.getAdbColumns().add((SQLIdentifierExpr)name3);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.accept(Token.COMMA);
            }
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.WHERE) {
                this.accept(Token.WHERE);
                final SQLExpr where = this.exprParser.expr();
                stmt.setAdbWhere(where);
            }
        }
        else if (this.lexer.identifierEquals("columns")) {
            this.lexer.nextToken();
            this.accept(Token.GROUP);
            final SQLName table = this.exprParser.name();
            stmt.setTable(table);
            this.accept(Token.LPAREN);
            while (true) {
                final SQLName name3 = this.exprParser.name();
                stmt.getAdbColumnsGroup().add((SQLIdentifierExpr)name3);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.accept(Token.COMMA);
            }
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.WHERE) {
                this.accept(Token.WHERE);
                final SQLExpr where = this.exprParser.expr();
                stmt.setAdbWhere(where);
            }
        }
        if (this.lexer.token() == Token.PARTITION) {
            stmt.setPartition(this.parsePartitionRef());
        }
        if (this.lexer.token() == Token.COMPUTE) {
            this.lexer.nextToken();
            this.acceptIdentifier("STATISTICS");
            stmt.setComputeStatistics(true);
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            this.acceptIdentifier("COLUMNS");
            stmt.setForColums(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CACHE)) {
            this.lexer.nextToken();
            this.acceptIdentifier("METADATA");
            stmt.setCacheMetadata(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.NOSCAN)) {
            this.lexer.nextToken();
            stmt.setNoscan(true);
        }
        return stmt;
    }
    
    public MySqlOptimizeStatement parseOptimize() {
        this.accept(Token.OPTIMIZE);
        this.accept(Token.TABLE);
        final MySqlOptimizeStatement stmt = new MySqlOptimizeStatement();
        final List<SQLName> names = new ArrayList<SQLName>();
        this.exprParser.names(names, stmt);
        for (final SQLName name : names) {
            stmt.addTableSource(new SQLExprTableSource(name));
        }
        return stmt;
    }
    
    public SQLStatement parseReset() {
        this.acceptIdentifier("RESET");
        final MySqlResetStatement stmt = new MySqlResetStatement();
        while (this.lexer.token() == Token.IDENTIFIER) {
            if (this.lexer.identifierEquals("QUERY")) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
                stmt.getOptions().add("QUERY CACHE");
            }
            else {
                stmt.getOptions().add(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return stmt;
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        if (this.lexer.identifierEquals("PREPARE")) {
            final MySqlPrepareStatement stmt = this.parsePrepare();
            statementList.add(stmt);
            return true;
        }
        if (this.lexer.identifierEquals("EXECUTE")) {
            this.acceptIdentifier("EXECUTE");
            if (this.lexer.identifierEquals("RESTART") || this.lexer.identifierEquals("UPDATE")) {
                final MySqlExecuteForAdsStatement stmt2 = this.parseExecuteForAds();
                statementList.add(stmt2);
            }
            else {
                final MySqlExecuteStatement stmt3 = this.parseExecute();
                statementList.add(stmt3);
            }
            return true;
        }
        if (this.lexer.identifierEquals("DEALLOCATE")) {
            final MysqlDeallocatePrepareStatement stmt4 = this.parseDeallocatePrepare();
            statementList.add(stmt4);
            return true;
        }
        if (this.lexer.identifierEquals("LOAD")) {
            final SQLStatement stmt5 = this.parseLoad();
            statementList.add(stmt5);
            return true;
        }
        if (this.lexer.token() == Token.REPLACE) {
            final SQLReplaceStatement stmt6 = this.parseReplace();
            statementList.add(stmt6);
            return true;
        }
        if (this.lexer.identifierEquals("START")) {
            final SQLStartTransactionStatement stmt7 = this.parseStart();
            statementList.add(stmt7);
            return true;
        }
        if (this.lexer.token() == Token.SHOW) {
            final SQLStatement stmt5 = this.parseShow();
            statementList.add(stmt5);
            return true;
        }
        if (this.lexer.identifierEquals("CLEAR")) {
            this.lexer.nextToken();
            if (!this.isEnabled(SQLParserFeature.DRDSAsyncDDL) || !this.lexer.identifierEquals("DDL")) {
                this.acceptIdentifier("PLANCACHE");
                statementList.add(new MySqlClearPlanCacheStatement());
                return true;
            }
            this.lexer.nextToken();
            this.accept(Token.CACHE);
            final DrdsClearDDLJobCache stmt8 = new DrdsClearDDLJobCache();
            if (Token.ALL == this.lexer.token()) {
                this.lexer.nextToken();
                stmt8.setAllJobs(true);
                statementList.add(stmt8);
                return true;
            }
            while (true) {
                stmt8.addJobId(this.lexer.integerValue().longValue());
                this.accept(Token.LITERAL_INT);
                if (Token.COMMA != this.lexer.token()) {
                    break;
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
                throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
            }
            statementList.add(stmt8);
            return true;
        }
        else {
            if (this.lexer.identifierEquals("DISABLED")) {
                this.lexer.nextToken();
                this.acceptIdentifier("PLANCACHE");
                statementList.add(new MySqlDisabledPlanCacheStatement());
                return true;
            }
            if (this.lexer.token() == Token.EXPLAIN) {
                final SQLStatement stmt5 = this.parseExplain();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals("BINLOG")) {
                final SQLStatement stmt5 = this.parseBinlog();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals("RESET")) {
                final SQLStatement stmt5 = this.parseReset();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.token() == Token.ANALYZE) {
                final SQLStatement stmt5 = this.parseAnalyze();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ARCHIVE)) {
                final SQLStatement stmt5 = this.parseArchive();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.BACKUP)) {
                final SQLStatement stmt5 = this.parseBackup();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.RESTORE)) {
                final SQLStatement stmt5 = this.parseRestore();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals("BUILD")) {
                final SQLStatement stmt5 = this.parseBuildTable();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals("CANCEL")) {
                final SQLStatement stmt5 = this.parseCancelJob();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.EXPORT)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TABLE) {
                    final SQLStatement stmt5 = this.parseExportTable();
                    statementList.add(stmt5);
                }
                else if (this.lexer.token() == Token.DATABASE) {
                    final SQLStatement stmt5 = this.parseExportDB();
                    statementList.add(stmt5);
                }
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.IMPORT)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TABLE) {
                    final SQLStatement stmt5 = this.parseImportTable();
                    statementList.add(stmt5);
                }
                else if (this.lexer.token() == Token.DATABASE) {
                    final SQLStatement stmt5 = this.parseImportDB();
                    statementList.add(stmt5);
                }
                return true;
            }
            if (this.lexer.identifierEquals("SUBMIT")) {
                this.lexer.nextToken();
                this.acceptIdentifier("JOB");
                final SQLStatement stmt5 = this.parseSubmitJob();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MIGRATE)) {
                final SQLStatement stmt5 = this.parseMigrate();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.token() == Token.OPTIMIZE) {
                final SQLStatement stmt5 = this.parseOptimize();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals("HELP")) {
                this.lexer.nextToken();
                final MySqlHelpStatement stmt9 = new MySqlHelpStatement();
                stmt9.setContent(this.exprParser.primary());
                statementList.add(stmt9);
                return true;
            }
            if (this.lexer.identifierEquals("FLUSH")) {
                final SQLStatement stmt5 = this.parseFlush();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SYNC)) {
                final SQLStatement stmt5 = this.parseSync();
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.INIT)) {
                statementList.add(new SQLExprStatement(this.exprParser.expr()));
                return true;
            }
            if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && this.lexer.identifierEquals(FnvHash.Constants.RECOVER)) {
                this.lexer.nextToken();
                this.acceptIdentifier("DDL");
                final DrdsRecoverDDLJob stmt10 = new DrdsRecoverDDLJob();
                if (Token.ALL == this.lexer.token()) {
                    this.lexer.nextToken();
                    stmt10.setAllJobs(true);
                    statementList.add(stmt10);
                    return true;
                }
                while (true) {
                    stmt10.addJobId(this.lexer.integerValue().longValue());
                    this.accept(Token.LITERAL_INT);
                    if (Token.COMMA != this.lexer.token()) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                if (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
                    throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
                }
                statementList.add(stmt10);
                return true;
            }
            else {
                if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && this.lexer.identifierEquals(FnvHash.Constants.REMOVE)) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("DDL");
                    final DrdsRemoveDDLJob stmt11 = new DrdsRemoveDDLJob();
                    if (Token.ALL == this.lexer.token()) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("COMPLETED")) {
                            this.lexer.nextToken();
                            stmt11.setAllCompleted(true);
                        }
                        else {
                            if (!this.lexer.identifierEquals("PENDING")) {
                                throw new ParserException("syntax error, expect COMPLETED or PENDING, actual " + this.lexer.token() + ", " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            stmt11.setAllPending(true);
                        }
                    }
                    else {
                        while (true) {
                            stmt11.addJobId(this.lexer.integerValue().longValue());
                            this.accept(Token.LITERAL_INT);
                            if (Token.COMMA != this.lexer.token()) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        if (this.lexer.token() != Token.EOF) {
                            if (this.lexer.token() != Token.SEMI) {
                                throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
                            }
                        }
                    }
                    statementList.add(stmt11);
                    return true;
                }
                if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && this.lexer.identifierEquals("INSPECT")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("DDL");
                    this.accept(Token.CACHE);
                    statementList.add(new DrdsInspectDDLJobCache());
                    return true;
                }
                if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && this.lexer.identifierEquals(FnvHash.Constants.CHANGE)) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("DDL")) {
                        this.lexer.nextToken();
                        final DrdsChangeDDLJob stmt12 = new DrdsChangeDDLJob();
                        stmt12.setJobId(this.lexer.integerValue().longValue());
                        this.accept(Token.LITERAL_INT);
                        if (this.lexer.identifierEquals("SKIP")) {
                            this.lexer.nextToken();
                            stmt12.setSkip(true);
                        }
                        else {
                            if (!this.lexer.identifierEquals("ADD")) {
                                throw new ParserException("syntax error, expect SKIP or ADD, actual " + this.lexer.token() + ", " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            stmt12.setAdd(true);
                        }
                        StringBuilder builder = new StringBuilder();
                        while (true) {
                            if (Token.COMMA == this.lexer.token()) {
                                this.lexer.nextToken();
                                stmt12.addGroupAndTableNameList(builder.toString());
                                builder = new StringBuilder();
                            }
                            else {
                                if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.SEMI) {
                                    break;
                                }
                                if (this.lexer.token() == Token.COLON) {
                                    builder.append(':');
                                    this.lexer.nextToken();
                                }
                                else if (this.lexer.token() == Token.DOT) {
                                    builder.append('.');
                                    this.lexer.nextToken();
                                }
                                else {
                                    builder.append(this.lexer.stringVal());
                                    this.lexer.nextToken();
                                }
                            }
                        }
                        stmt12.addGroupAndTableNameList(builder.toString());
                        statementList.add(stmt12);
                        return true;
                    }
                    this.lexer.reset(mark);
                }
                if (this.isEnabled(SQLParserFeature.DRDSBaseline) && this.lexer.identifierEquals("BASELINE")) {
                    this.lexer.nextToken();
                    final DrdsBaselineStatement stmt13 = new DrdsBaselineStatement();
                    if (Token.EOF == this.lexer.token() || Token.SEMI == this.lexer.token() || this.lexer.stringVal().isEmpty() || this.lexer.stringVal().equalsIgnoreCase("BASELINE")) {
                        throw new ParserException("syntax error, expect baseline operation, actual " + this.lexer.token() + ", " + this.lexer.info());
                    }
                    stmt13.setOperation(this.lexer.stringVal());
                    this.lexer.setToken(Token.COMMA);
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.HINT) {
                            stmt13.setHeadHints(this.exprParser.parseHints());
                        }
                        final MySqlSelectParser selectParser = this.createSQLSelectParser();
                        stmt13.setSelect(selectParser.select());
                    }
                    else {
                        while (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
                            stmt13.addBaselineId(this.lexer.integerValue().longValue());
                            this.accept(Token.LITERAL_INT);
                            if (Token.COMMA == this.lexer.token()) {
                                this.lexer.nextToken();
                            }
                        }
                    }
                    statementList.add(stmt13);
                    return true;
                }
                else {
                    if (this.lexer.token() == Token.DESC || this.lexer.identifierEquals("DESCRIBE")) {
                        final SQLStatement stmt5 = this.parseDescribe();
                        statementList.add(stmt5);
                        return true;
                    }
                    if (this.lexer.token() == Token.LOCK) {
                        this.lexer.nextToken();
                        final String val = this.lexer.stringVal();
                        final boolean isLockTables = "TABLES".equalsIgnoreCase(val) && this.lexer.token() == Token.IDENTIFIER;
                        final boolean isLockTable = "TABLE".equalsIgnoreCase(val) && this.lexer.token() == Token.TABLE;
                        if (!isLockTables && !isLockTable) {
                            this.setErrorEndPos(this.lexer.pos());
                            throw new ParserException("syntax error, expect TABLES or TABLE, actual " + this.lexer.token() + ", " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        final MySqlLockTableStatement stmt14 = new MySqlLockTableStatement();
                        while (true) {
                            final MySqlLockTableStatement.Item item = new MySqlLockTableStatement.Item();
                            SQLExprTableSource tableSource = null;
                            final SQLName tableName = this.exprParser.name();
                            if (this.lexer.token() == Token.AS) {
                                this.lexer.nextToken();
                                final String as = this.lexer.stringVal();
                                tableSource = new SQLExprTableSource(tableName, as);
                                this.lexer.nextToken();
                            }
                            else {
                                tableSource = new SQLExprTableSource(tableName);
                            }
                            item.setTableSource(tableSource);
                            stmt14.getItems().add(item);
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                            }
                            else {
                                if (this.lexer.identifierEquals("READ")) {
                                    this.lexer.nextToken();
                                    if (this.lexer.identifierEquals("LOCAL")) {
                                        this.lexer.nextToken();
                                        item.setLockType(MySqlLockTableStatement.LockType.READ_LOCAL);
                                    }
                                    else {
                                        item.setLockType(MySqlLockTableStatement.LockType.READ);
                                    }
                                }
                                else if (this.lexer.identifierEquals("WRITE")) {
                                    this.lexer.nextToken();
                                    item.setLockType(MySqlLockTableStatement.LockType.WRITE);
                                }
                                else {
                                    if (!this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
                                        throw new ParserException("syntax error, expect READ or WRITE OR AS, actual " + this.lexer.token() + ", " + this.lexer.info());
                                    }
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("WRITE");
                                    this.lexer.nextToken();
                                    item.setLockType(MySqlLockTableStatement.LockType.LOW_PRIORITY_WRITE);
                                }
                                if (this.lexer.token() == Token.HINT) {
                                    item.setHints(this.exprParser.parseHints());
                                }
                                if (this.lexer.token() != Token.COMMA) {
                                    statementList.add(stmt14);
                                    return true;
                                }
                                this.lexer.nextToken();
                            }
                        }
                    }
                    else if (this.lexer.identifierEquals("UNLOCK")) {
                        this.lexer.nextToken();
                        final String val = this.lexer.stringVal();
                        final boolean isUnLockTables = "TABLES".equalsIgnoreCase(val) && this.lexer.token() == Token.IDENTIFIER;
                        final boolean isUnLockTable = "TABLE".equalsIgnoreCase(val) && this.lexer.token() == Token.TABLE;
                        statementList.add(new MySqlUnlockTablesStatement());
                        if (isUnLockTables || isUnLockTable) {
                            this.lexer.nextToken();
                            return true;
                        }
                        this.setErrorEndPos(this.lexer.pos());
                        throw new ParserException("syntax error, expect TABLES or TABLE, actual " + this.lexer.token() + ", " + this.lexer.info());
                    }
                    else {
                        if (this.lexer.identifierEquals(FnvHash.Constants.CHECKSUM)) {
                            statementList.add(this.parseChecksum());
                            return true;
                        }
                        if (this.lexer.token() == Token.HINT) {
                            final List<SQLCommentHint> hints = this.exprParser.parseHints();
                            boolean tddlHints = false;
                            boolean accept = false;
                            boolean acceptHint = false;
                            switch (this.lexer.token()) {
                                case SELECT:
                                case WITH:
                                case DELETE:
                                case UPDATE:
                                case INSERT:
                                case SHOW:
                                case REPLACE:
                                case TRUNCATE:
                                case DROP:
                                case ALTER:
                                case CREATE:
                                case CHECK:
                                case SET:
                                case DESC:
                                case OPTIMIZE:
                                case ANALYZE:
                                case KILL:
                                case EXPLAIN:
                                case LPAREN: {
                                    acceptHint = true;
                                    break;
                                }
                                case IDENTIFIER: {
                                    acceptHint = (this.lexer.hash_lower() == FnvHash.Constants.DUMP || this.lexer.hash_lower() == FnvHash.Constants.RENAME || this.lexer.hash_lower() == FnvHash.Constants.DESCRIBE);
                                    break;
                                }
                            }
                            if (hints.size() >= 1 && statementList.size() == 0 && acceptHint) {
                                final SQLCommentHint hint = hints.get(0);
                                final String hintText = hint.getText().toUpperCase();
                                if (hintText.startsWith("+TDDL") || hintText.startsWith("+ TDDL") || hintText.startsWith("TDDL") || hintText.startsWith("!TDDL")) {
                                    tddlHints = true;
                                }
                                else if (hintText.startsWith("+")) {
                                    accept = true;
                                }
                            }
                            if (tddlHints) {
                                final SQLStatementImpl stmt15 = (SQLStatementImpl)this.parseStatement();
                                stmt15.setHeadHints(hints);
                                statementList.add(stmt15);
                                return true;
                            }
                            if (accept) {
                                final SQLStatementImpl stmt15 = (SQLStatementImpl)this.parseStatement();
                                stmt15.setHeadHints(hints);
                                statementList.add(stmt15);
                                return true;
                            }
                            final MySqlHintStatement stmt16 = new MySqlHintStatement();
                            stmt16.setHints(hints);
                            statementList.add(stmt16);
                            return true;
                        }
                        else {
                            if (this.lexer.token() == Token.BEGIN) {
                                statementList.add(this.parseBlock());
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
                                statementList.add(this.parseAddManageInstanceGroup());
                                return true;
                            }
                            if (this.lexer.token() == Token.IDENTIFIER) {
                                final String label = this.lexer.stringVal();
                                final char ch = this.lexer.current();
                                final int bp = this.lexer.bp();
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.VARIANT && this.lexer.stringVal().equals(":")) {
                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.LOOP) {
                                        statementList.add(this.parseLoop(label));
                                    }
                                    else if (this.lexer.token() == Token.WHILE) {
                                        statementList.add(this.parseWhile(label));
                                    }
                                    else if (this.lexer.token() == Token.BEGIN) {
                                        final SQLBlockStatement block = this.parseBlock(label);
                                        statementList.add(block);
                                    }
                                    else if (this.lexer.token() == Token.REPEAT) {
                                        statementList.add(this.parseRepeat(label));
                                    }
                                    return true;
                                }
                                this.lexer.reset(bp, ch, Token.IDENTIFIER);
                            }
                            if (this.lexer.token() == Token.CHECK) {
                                final Lexer.SavePoint mark = this.lexer.mark();
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.TABLE) {
                                    this.lexer.nextToken();
                                    final MySqlCheckTableStatement stmt17 = new MySqlCheckTableStatement();
                                    while (true) {
                                        final SQLName table = this.exprParser.name();
                                        stmt17.addTable(new SQLExprTableSource(table));
                                        if (this.lexer.token() != Token.COMMA) {
                                            break;
                                        }
                                        this.lexer.nextToken();
                                    }
                                    statementList.add(stmt17);
                                }
                                return true;
                            }
                            return false;
                        }
                    }
                }
            }
        }
    }
    
    private SQLStatement parseArchive() {
        this.lexer.nextToken();
        this.accept(Token.TABLE);
        final SQLArchiveTableStatement stmt = new SQLArchiveTableStatement();
        final SQLName tableName = this.exprParser.name();
        stmt.setTable(tableName);
        stmt.setType(new SQLIdentifierExpr("UPLOAD"));
        if (this.lexer.token() == Token.LITERAL_INT) {
            while (true) {
                stmt.getSpIdList().add(this.exprParser.integerExpr());
                final String pidStr = this.lexer.stringVal();
                this.accept(Token.VARIANT);
                final String s = pidStr.replaceAll(":", "");
                if (StringUtils.isEmpty(s)) {
                    stmt.getpIdList().add(this.exprParser.integerExpr());
                }
                else {
                    stmt.getpIdList().add(new SQLIntegerExpr(Integer.valueOf(s)));
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return stmt;
    }
    
    private SQLStatement parseBackup() {
        this.lexer.nextToken();
        final SQLBackupStatement stmt = new SQLBackupStatement();
        String type = "BACKUP_DATA";
        String action = "BACKUP";
        if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
            this.lexer.nextToken();
            this.accept(Token.INTO);
            type = "BACKUP_DATA";
            while (true) {
                stmt.getProperties().add(new SQLCharExpr(this.lexer.stringVal()));
                this.accept(Token.LITERAL_CHARS);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LOG)) {
            type = "BACKUP_LOG";
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("LIST_LOGS")) {
                this.lexer.nextToken();
                action = "LIST_LOG";
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.STATUS)) {
                this.lexer.nextToken();
                action = "STATUS";
            }
            else if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                while (true) {
                    stmt.getProperties().add(new SQLCharExpr(this.lexer.stringVal()));
                    this.accept(Token.LITERAL_CHARS);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
        }
        else if (this.lexer.identifierEquals("CANCEL")) {
            this.lexer.nextToken();
            type = "BACKUP_DATA";
            action = "BACKUP_CANCEL";
            stmt.getProperties().add(new SQLCharExpr(this.lexer.stringVal()));
            this.accept(Token.LITERAL_CHARS);
        }
        stmt.setType(new SQLIdentifierExpr(type));
        stmt.setAction(new SQLIdentifierExpr(action));
        return stmt;
    }
    
    private SQLStatement parseRestore() {
        this.lexer.nextToken();
        String type = "DATA";
        final SQLRestoreStatement stmt = new SQLRestoreStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
            this.lexer.nextToken();
            type = "DATA";
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LOG)) {
            this.lexer.nextToken();
            type = "LOG";
        }
        stmt.setType(new SQLIdentifierExpr(type));
        this.accept(Token.FROM);
        while (true) {
            stmt.getProperties().add(new SQLCharExpr(this.lexer.stringVal()));
            this.accept(Token.LITERAL_CHARS);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return stmt;
    }
    
    private SQLStatement parseBuildTable() {
        this.lexer.nextToken();
        final SQLBuildTableStatement stmt = new SQLBuildTableStatement();
        this.accept(Token.TABLE);
        stmt.setTable(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.VERSION)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            stmt.setVersion(this.exprParser.integerExpr());
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("SPLIT");
            stmt.setWithSplit(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            if (this.lexer.token() == Token.TRUE) {
                this.lexer.nextToken();
                stmt.setForce(true);
            }
            else if (this.lexer.token() == Token.FALSE) {
                this.lexer.nextToken();
                stmt.setForce(false);
            }
        }
        return stmt;
    }
    
    private SQLStatement parseCancelJob() {
        this.lexer.nextToken();
        if (!this.isEnabled(SQLParserFeature.DRDSAsyncDDL) || !this.lexer.identifierEquals("DDL")) {
            final SQLCancelJobStatement stmt = new SQLCancelJobStatement();
            if (this.lexer.identifierEquals("JOB")) {
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("LOAD_JOB")) {
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("SYNC_JOB")) {
                this.lexer.nextToken();
                stmt.setImport(true);
            }
            stmt.setJobName(this.exprParser.name());
            return stmt;
        }
        this.lexer.nextToken();
        final DrdsCancelDDLJob cancelDDLJob = new DrdsCancelDDLJob();
        while (true) {
            cancelDDLJob.addJobId(this.lexer.integerValue().longValue());
            this.accept(Token.LITERAL_INT);
            if (Token.COMMA != this.lexer.token()) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
            throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
        }
        return cancelDDLJob;
    }
    
    protected SQLStatement parseExportTable() {
        this.accept(Token.TABLE);
        final SQLExportTableStatement stmt = new SQLExportTableStatement();
        stmt.setTable(new SQLExprTableSource(this.exprParser.name()));
        return stmt;
    }
    
    protected SQLStatement parseExportDB() {
        this.accept(Token.DATABASE);
        final SQLExportDatabaseStatement stmt = new SQLExportDatabaseStatement();
        stmt.setDb(this.exprParser.name());
        if (this.lexer.identifierEquals("REALTIME")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            if ("y".equalsIgnoreCase(this.lexer.stringVal())) {
                this.lexer.nextToken();
                stmt.setRealtime(true);
            }
            else {
                if (!"n".equalsIgnoreCase(this.lexer.stringVal())) {
                    throw new ParserException("Invalid 'realtime' option, should be 'Y' or 'N'. ");
                }
                this.lexer.nextToken();
                stmt.setRealtime(false);
            }
        }
        return stmt;
    }
    
    protected SQLStatement parseRaftLeaderTransfer() {
        this.acceptIdentifier("RAFT_LEADER_TRANSFER");
        final MySqlRaftLeaderTransferStatement stmt = new MySqlRaftLeaderTransferStatement();
        this.acceptIdentifier("SHARD");
        this.accept(Token.EQ);
        stmt.setShard(this.exprParser.charExpr());
        this.accept(Token.FROM);
        this.accept(Token.EQ);
        stmt.setFrom(this.exprParser.charExpr());
        this.accept(Token.TO);
        this.accept(Token.EQ);
        stmt.setTo(this.exprParser.charExpr());
        this.acceptIdentifier("TIMEOUT");
        this.accept(Token.EQ);
        stmt.setTimeout(this.exprParser.integerExpr());
        return stmt;
    }
    
    protected SQLStatement parseRaftMemeberChange() {
        this.acceptIdentifier("RAFT_MEMBER_CHANGE");
        final MySqlRaftMemberChangeStatement stmt = new MySqlRaftMemberChangeStatement();
        if (this.lexer.identifierEquals("NOLEADER")) {
            this.lexer.nextToken();
            stmt.setNoLeader(true);
        }
        this.acceptIdentifier("SHARD");
        this.accept(Token.EQ);
        stmt.setShard(this.exprParser.charExpr());
        this.acceptIdentifier("HOST");
        this.accept(Token.EQ);
        stmt.setHost(this.exprParser.charExpr());
        this.acceptIdentifier("STATUS");
        this.accept(Token.EQ);
        stmt.setStatus(this.exprParser.charExpr());
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            stmt.setForce(true);
        }
        return stmt;
    }
    
    protected SQLStatement parseMigrate() {
        final MySqlMigrateStatement stmt = new MySqlMigrateStatement();
        this.acceptIdentifier("MIGRATE");
        this.accept(Token.DATABASE);
        stmt.setSchema(this.exprParser.name());
        this.acceptIdentifier("SHARDS");
        this.accept(Token.EQ);
        stmt.setShardNames(this.exprParser.charExpr());
        if (this.lexer.token() == Token.GROUP) {
            this.lexer.nextToken();
            stmt.setMigrateType(new SQLIntegerExpr(0));
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.HOST)) {
            this.lexer.nextToken();
            stmt.setMigrateType(new SQLIntegerExpr(1));
        }
        this.accept(Token.FROM);
        stmt.setFromInsId(this.exprParser.charExpr());
        if (this.lexer.token() == Token.VARIANT) {
            this.lexer.nextToken();
            stmt.setFromInsIp(this.exprParser.charExpr());
            final String variant = this.lexer.stringVal();
            final Integer number = Integer.valueOf(variant.substring(1, variant.length()));
            stmt.setFromInsPort(new SQLIntegerExpr(number));
            this.accept(Token.VARIANT);
            this.accept(Token.VARIANT);
            stmt.setFromInsStatus(this.exprParser.charExpr());
        }
        this.accept(Token.TO);
        stmt.setToInsId(this.exprParser.charExpr());
        if (this.lexer.token() == Token.VARIANT) {
            this.lexer.nextToken();
            stmt.setToInsIp(this.exprParser.charExpr());
            final String variant = this.lexer.stringVal();
            final Integer number = Integer.valueOf(variant.substring(1, variant.length()));
            stmt.setToInsPort(new SQLIntegerExpr(number));
            this.accept(Token.VARIANT);
            this.accept(Token.VARIANT);
            stmt.setToInsStatus(this.exprParser.charExpr());
        }
        return stmt;
    }
    
    protected SQLStatement parseImportDB() {
        this.accept(Token.DATABASE);
        final SQLImportDatabaseStatement stmt = new SQLImportDatabaseStatement();
        stmt.setDb(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.STATUS)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            stmt.setStatus(this.exprParser.name());
        }
        return stmt;
    }
    
    protected SQLStatement parseImportTable() {
        final SQLImportTableStatement stmt = new SQLImportTableStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExtenal(true);
        }
        this.accept(Token.TABLE);
        stmt.setTable(new SQLExprTableSource(this.exprParser.name()));
        this.acceptIdentifier("VERSION");
        this.accept(Token.EQ);
        stmt.setVersion(this.exprParser.integerExpr());
        if (this.lexer.identifierEquals("BUILD")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            if ("Y".equalsIgnoreCase(this.lexer.stringVal())) {
                this.lexer.nextToken();
                stmt.setUsingBuild(true);
            }
            else {
                if (!"N".equalsIgnoreCase(this.lexer.stringVal())) {
                    throw new ParserException("Invalid 'build' option, should be 'Y' or 'N'. ");
                }
                this.lexer.nextToken();
                stmt.setUsingBuild(false);
            }
        }
        return stmt;
    }
    
    protected SQLStatement parseSubmitJob() {
        final SQLSubmitJobStatement stmt = new SQLSubmitJobStatement();
        if (this.lexer.identifierEquals("AWAIT")) {
            this.lexer.nextToken();
            stmt.setAwait(true);
        }
        stmt.setStatment(this.parseStatement());
        return stmt;
    }
    
    public SQLStatement parseSync() {
        this.lexer.nextToken();
        if (this.lexer.identifierEquals("RAFT_LEADER_TRANSFER")) {
            return this.parseRaftLeaderTransfer();
        }
        if (this.lexer.identifierEquals("RAFT_MEMBER_CHANGE")) {
            return this.parseRaftMemeberChange();
        }
        this.acceptIdentifier("META");
        this.acceptIdentifier("TABLES");
        final SQLSyncMetaStatement stmt = new SQLSyncMetaStatement();
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            stmt.setFrom(this.exprParser.name());
        }
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            stmt.setLike(this.exprParser.expr());
        }
        return stmt;
    }
    
    public SQLStatement parseFlush() {
        this.acceptIdentifier("FLUSH");
        final MySqlFlushStatement stmt = new MySqlFlushStatement();
        if (this.lexer.identifierEquals("NO_WRITE_TO_BINLOG")) {
            this.lexer.nextToken();
            stmt.setNoWriteToBinlog(true);
        }
        if (this.lexer.identifierEquals("LOCAL")) {
            this.lexer.nextToken();
            stmt.setLocal(true);
        }
        while (true) {
            if (this.lexer.token() == Token.BINARY || this.lexer.identifierEquals("BINARY")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setBinaryLogs(true);
            }
            else if (this.lexer.identifierEquals("DES_KEY_FILE")) {
                this.lexer.nextToken();
                stmt.setDesKeyFile(true);
            }
            else if (this.lexer.identifierEquals("ENGINE")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setEngineLogs(true);
            }
            else if (this.lexer.identifierEquals("ERROR")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setErrorLogs(true);
            }
            else if (this.lexer.identifierEquals("GENERAL")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setGeneralLogs(true);
            }
            else if (this.lexer.identifierEquals("HOSTS")) {
                this.lexer.nextToken();
                stmt.setHots(true);
            }
            else if (this.lexer.identifierEquals("LOGS")) {
                this.lexer.nextToken();
                stmt.setLogs(true);
            }
            else if (this.lexer.identifierEquals("PRIVILEGES")) {
                this.lexer.nextToken();
                stmt.setPrivileges(true);
            }
            else if (this.lexer.identifierEquals("OPTIMIZER_COSTS")) {
                this.lexer.nextToken();
                stmt.setOptimizerCosts(true);
            }
            else if (this.lexer.identifierEquals("QUERY")) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
                stmt.setQueryCache(true);
            }
            else if (this.lexer.identifierEquals("RELAY")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setRelayLogs(true);
                if (this.lexer.token() != Token.FOR) {
                    continue;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("CHANNEL");
                stmt.setRelayLogsForChannel(this.exprParser.primary());
            }
            else if (this.lexer.identifierEquals("SLOW")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOGS");
                stmt.setSlowLogs(true);
            }
            else if (this.lexer.identifierEquals("STATUS")) {
                this.lexer.nextToken();
                stmt.setStatus(true);
            }
            else if (this.lexer.identifierEquals("USER_RESOURCES")) {
                this.lexer.nextToken();
                stmt.setUserResources(true);
            }
            else {
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.identifierEquals("TABLES") || this.lexer.token() == Token.TABLE) {
            this.lexer.nextToken();
            stmt.setTableOption(true);
            if (this.lexer.token() == Token.WITH) {
                this.lexer.nextToken();
                this.acceptIdentifier("READ");
                this.accept(Token.LOCK);
                stmt.setWithReadLock(true);
            }
            if (this.lexer.token() == Token.IDENTIFIER) {
                while (true) {
                    final SQLName name = this.exprParser.name();
                    stmt.addTable(name);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            if (stmt.getTables().size() != 0) {
                if (this.lexer.token() == Token.FOR) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("EXPORT");
                    stmt.setForExport(true);
                }
                else if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("READ");
                    this.accept(Token.LOCK);
                    stmt.setWithReadLock(true);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.VERSION)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    stmt.setVersion(this.exprParser.integerExpr());
                }
            }
        }
        return stmt;
    }
    
    @Override
    public SQLBlockStatement parseBlock() {
        final SQLBlockStatement block = new SQLBlockStatement();
        block.setDbType(this.dbType);
        this.accept(Token.BEGIN);
        final List<SQLStatement> statementList = block.getStatementList();
        this.parseStatementList(statementList, -1, block);
        if (this.lexer.token() != Token.END && statementList.size() > 0 && (statementList.get(statementList.size() - 1) instanceof SQLCommitStatement || statementList.get(statementList.size() - 1) instanceof SQLRollbackStatement)) {
            block.setEndOfCommit(true);
            return block;
        }
        this.accept(Token.END);
        return block;
    }
    
    @Override
    public MySqlExplainStatement parseDescribe() {
        final MySqlExplainStatement describe = new MySqlExplainStatement();
        if (this.lexer.token() == Token.DESC || this.lexer.identifierEquals("DESCRIBE")) {
            this.lexer.nextToken();
            describe.setDescribe(true);
            return this.parseExplain(describe);
        }
        throw new ParserException("expect one of {DESCRIBE | DESC} , actual " + this.lexer.token() + ", " + this.lexer.info());
    }
    
    @Override
    public MySqlExplainStatement parseExplain() {
        final MySqlExplainStatement explain = new MySqlExplainStatement();
        explain.setSourceLine(this.lexer.getPosLine());
        explain.setSourceLine(this.lexer.getPosColumn());
        if (this.lexer.token() == Token.EXPLAIN) {
            this.lexer.nextToken();
            return this.parseExplain(explain);
        }
        throw new ParserException("expect EXPLAIN , actual " + this.lexer.token() + ", " + this.lexer.info());
    }
    
    private MySqlExplainStatement parseExplain(final MySqlExplainStatement explain) {
        if (this.lexer.identifierEquals(FnvHash.Constants.PLAN)) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
            }
            else {
                this.lexer.reset(mark);
            }
        }
        if (this.lexer.token() == Token.ANALYZE) {
            this.lexer.nextToken();
            explain.setType("ANALYZE");
        }
        if (this.lexer.token() == Token.HINT) {
            final List<SQLCommentHint> hints = this.exprParser.parseHints();
            explain.setHints(hints);
        }
        switch (this.dbType) {
            case mysql:
            case ads:
            case presto:
            case trino: {
                final Lexer.SavePoint mark = this.lexer.mark();
                if (this.lexer.token() != Token.LPAREN) {
                    break;
                }
                this.lexer.nextToken();
                if (this.lexer.token() == Token.SELECT) {
                    this.lexer.reset(mark);
                    break;
                }
                while (true) {
                    if (this.lexer.identifierEquals("FORMAT")) {
                        this.lexer.nextToken();
                        final String format = this.lexer.stringVal();
                        explain.setFormat(format);
                        this.lexer.nextToken();
                    }
                    else {
                        if (!this.lexer.identifierEquals("TYPE")) {
                            break;
                        }
                        this.lexer.nextToken();
                        final String type = this.lexer.stringVal();
                        explain.setType(type);
                        this.lexer.nextToken();
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                explain.setParenthesis(true);
                break;
            }
        }
        boolean table = false;
        if (this.lexer.token() == Token.IDENTIFIER) {
            final long hash = this.lexer.hash_lower();
            final String stringVal = this.lexer.stringVal();
            if (hash == FnvHash.Constants.EXTENDED) {
                explain.setExtended(true);
                this.lexer.nextToken();
            }
            else if (hash == FnvHash.Constants.PARTITIONS) {
                explain.setType(stringVal);
                this.lexer.nextToken();
            }
            else if (hash == FnvHash.Constants.OPTIMIZER) {
                explain.setOptimizer(true);
                this.lexer.nextToken();
            }
            else if (hash == FnvHash.Constants.FORMAT) {
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final String format2 = this.lexer.stringVal();
                explain.setFormat(format2);
                this.accept(Token.IDENTIFIER);
            }
            else {
                explain.setTableName(this.exprParser.name());
                if (this.lexer.token() == Token.IDENTIFIER) {
                    explain.setColumnName(this.exprParser.name());
                }
                else if (this.lexer.token() == Token.LITERAL_CHARS) {
                    explain.setWild(this.exprParser.expr());
                }
                table = true;
            }
        }
        if (this.lexer.token() == Token.DISTRIBUTE) {
            this.lexer.nextToken();
            this.acceptIdentifier("INFO");
            explain.setDistributeInfo(true);
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            this.acceptIdentifier("CONNECTION");
            explain.setConnectionId(this.exprParser.expr());
        }
        else if (!table) {
            explain.setStatement(this.parseStatement());
        }
        return explain;
    }
    
    @Override
    public SQLStatement parseShow() {
        this.accept(Token.SHOW);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
        }
        boolean isPhysical = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.PHYSICAL)) {
            this.lexer.nextToken();
            isPhysical = true;
        }
        boolean full = false;
        if (this.lexer.token() == Token.FULL) {
            this.lexer.nextToken();
            full = true;
        }
        else if (this.lexer.token() == Token.HINT) {
            final String hints = this.lexer.stringVal().toLowerCase();
            if (hints.endsWith(" full") && hints.length() <= 11 && hints.charAt(0) == '!' && hints.charAt(1) == '5') {
                this.lexer.nextToken();
                full = true;
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STATS)) {
            this.lexer.nextToken();
            final SQLShowStatisticStmt showStats = new SQLShowStatisticStmt();
            showStats.setDbType(DbType.mysql);
            if (full) {
                showStats.setFull(true);
            }
            return showStats;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PROCESSLIST)) {
            this.lexer.nextToken();
            final MySqlShowProcessListStatement stmt = new MySqlShowProcessListStatement();
            stmt.setFull(full);
            if (!full && this.lexer.identifierEquals(FnvHash.Constants.MPP)) {
                this.lexer.nextToken();
                stmt.setMpp(true);
            }
            if (this.lexer.token() == Token.WHERE) {
                this.lexer.nextToken();
                stmt.setWhere(this.exprParser.expr());
            }
            if (this.lexer.token() == Token.ORDER) {
                stmt.setOrderBy(this.exprParser.parseOrderBy());
            }
            if (this.lexer.token() == Token.LIMIT) {
                stmt.setLimit(this.exprParser.parseLimit());
            }
            return stmt;
        }
        if (this.lexer.identifierEquals("COLUMNS") || this.lexer.identifierEquals("FIELDS")) {
            this.lexer.nextToken();
            final SQLShowColumnsStatement stmt2 = this.parseShowColumns();
            stmt2.setFull(full);
            return stmt2;
        }
        if (this.lexer.identifierEquals("COLUMNS")) {
            this.lexer.nextToken();
            final SQLShowColumnsStatement stmt2 = this.parseShowColumns();
            return stmt2;
        }
        if (this.lexer.identifierEquals("TABLES")) {
            this.lexer.nextToken();
            final SQLShowTablesStatement stmt3 = this.parseShowTables();
            stmt3.setFull(full);
            return stmt3;
        }
        if (this.lexer.identifierEquals("DATABASES")) {
            this.lexer.nextToken();
            final SQLShowDatabasesStatement stmt4 = this.parseShowDatabases(isPhysical);
            if (full) {
                stmt4.setFull(true);
            }
            return stmt4;
        }
        if (this.lexer.identifierEquals("WARNINGS")) {
            this.lexer.nextToken();
            final MySqlShowWarningsStatement stmt5 = this.parseShowWarnings();
            return stmt5;
        }
        if (this.lexer.identifierEquals("COUNT")) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.accept(Token.STAR);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals(FnvHash.Constants.ERRORS)) {
                this.lexer.nextToken();
                final MySqlShowErrorsStatement stmt6 = new MySqlShowErrorsStatement();
                stmt6.setCount(true);
                return stmt6;
            }
            this.acceptIdentifier("WARNINGS");
            final MySqlShowWarningsStatement stmt5 = new MySqlShowWarningsStatement();
            stmt5.setCount(true);
            return stmt5;
        }
        else {
            if (this.lexer.identifierEquals(FnvHash.Constants.ERRORS)) {
                this.lexer.nextToken();
                final MySqlShowErrorsStatement stmt6 = new MySqlShowErrorsStatement();
                stmt6.setLimit(this.exprParser.parseLimit());
                return stmt6;
            }
            if (this.lexer.identifierEquals("STATUS")) {
                this.lexer.nextToken();
                final MySqlShowStatusStatement stmt7 = this.parseShowStatus();
                return stmt7;
            }
            if (this.lexer.identifierEquals("DBLOCK")) {
                this.lexer.nextToken();
                return new MysqlShowDbLockStatement();
            }
            if (this.lexer.identifierEquals("VARIABLES")) {
                this.lexer.nextToken();
                final SQLShowVariantsStatement stmt8 = this.parseShowVariants();
                return stmt8;
            }
            if (this.lexer.identifierEquals("GLOBAL")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("STATUS")) {
                    this.lexer.nextToken();
                    final MySqlShowStatusStatement stmt7 = this.parseShowStatus();
                    stmt7.setGlobal(true);
                    return stmt7;
                }
                if (this.lexer.identifierEquals("VARIABLES")) {
                    this.lexer.nextToken();
                    final SQLShowVariantsStatement stmt8 = this.parseShowVariants();
                    stmt8.setGlobal(true);
                    return stmt8;
                }
                if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && (Token.INDEX == this.lexer.token() || this.lexer.identifierEquals("INDEXES"))) {
                    this.lexer.nextToken();
                    final DrdsShowGlobalIndex stmt9 = new DrdsShowGlobalIndex();
                    if (Token.FROM == this.lexer.token()) {
                        this.lexer.nextToken();
                        stmt9.setTableName(this.exprParser.name());
                    }
                    return stmt9;
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SESSION)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("STATUS")) {
                    this.lexer.nextToken();
                    final MySqlShowStatusStatement stmt7 = this.parseShowStatus();
                    stmt7.setSession(true);
                    return stmt7;
                }
                if (this.lexer.identifierEquals("VARIABLES")) {
                    this.lexer.nextToken();
                    final SQLShowVariantsStatement stmt8 = this.parseShowVariants();
                    stmt8.setSession(true);
                    return stmt8;
                }
                final SQLShowSessionStatement stmt10 = new SQLShowSessionStatement();
                if (this.lexer.token() == Token.LIKE) {
                    this.lexer.nextToken();
                    final SQLExpr like = this.exprParser.expr();
                    stmt10.setLike(like);
                }
                return stmt10;
            }
            else {
                if (this.lexer.identifierEquals("COBAR_STATUS")) {
                    this.lexer.nextToken();
                    return new CobarShowStatus();
                }
                if (this.lexer.identifierEquals("AUTHORS")) {
                    this.lexer.nextToken();
                    return new MySqlShowAuthorsStatement();
                }
                if (this.lexer.token() == Token.BINARY) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("LOGS");
                    return new MySqlShowBinaryLogsStatement();
                }
                if (this.lexer.identifierEquals("MASTER")) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("LOGS")) {
                        this.lexer.nextToken();
                        return new MySqlShowMasterLogsStatement();
                    }
                    this.acceptIdentifier("STATUS");
                    return new MySqlShowMasterStatusStatement();
                }
                else {
                    if (this.lexer.identifierEquals("CLUSTER")) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("NAME");
                        return new MySqlShowClusterNameStatement();
                    }
                    if (this.lexer.identifierEquals("SYNC_JOB")) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("STATUS");
                        final MySqlShowJobStatusStatement stmt11 = new MySqlShowJobStatusStatement();
                        stmt11.setSync(true);
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt11.setWhere(this.exprParser.expr());
                        }
                        return stmt11;
                    }
                    if (this.lexer.identifierEquals("JOB")) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("STATUS");
                        final MySqlShowJobStatusStatement stmt11 = new MySqlShowJobStatusStatement();
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt11.setWhere(this.exprParser.expr());
                        }
                        return stmt11;
                    }
                    if (this.lexer.identifierEquals("MIGRATE")) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("TASK");
                        this.acceptIdentifier("STATUS");
                        final MySqlShowMigrateTaskStatusStatement stmt12 = new MySqlShowMigrateTaskStatusStatement();
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt12.setWhere(this.exprParser.expr());
                        }
                        return stmt12;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.CHARSET)) {
                        this.lexer.nextToken();
                        final MySqlShowCharacterSetStatement stmt13 = new MySqlShowCharacterSetStatement();
                        if (this.lexer.token() == Token.LIKE) {
                            this.lexer.nextTokenValue();
                            stmt13.setPattern(this.exprParser.expr());
                        }
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt13.setWhere(this.exprParser.expr());
                        }
                        return stmt13;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
                        this.lexer.nextToken();
                        this.accept(Token.SET);
                        final MySqlShowCharacterSetStatement stmt13 = new MySqlShowCharacterSetStatement();
                        if (this.lexer.token() == Token.LIKE) {
                            this.lexer.nextTokenValue();
                            stmt13.setPattern(this.exprParser.expr());
                        }
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt13.setWhere(this.exprParser.expr());
                        }
                        return stmt13;
                    }
                    if (this.lexer.identifierEquals("COLLATION")) {
                        this.lexer.nextToken();
                        final MySqlShowCollationStatement stmt14 = new MySqlShowCollationStatement();
                        if (this.lexer.token() == Token.LIKE) {
                            this.lexer.nextTokenValue();
                            stmt14.setPattern(this.exprParser.expr());
                        }
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            stmt14.setWhere(this.exprParser.expr());
                        }
                        return stmt14;
                    }
                    if (this.lexer.identifierEquals("BINLOG")) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("EVENTS");
                        final MySqlShowBinLogEventsStatement stmt15 = new MySqlShowBinLogEventsStatement();
                        if (this.lexer.token() == Token.IN) {
                            this.lexer.nextToken();
                            stmt15.setIn(this.exprParser.expr());
                        }
                        if (this.lexer.token() == Token.FROM) {
                            this.lexer.nextToken();
                            stmt15.setFrom(this.exprParser.expr());
                        }
                        stmt15.setLimit(this.exprParser.parseLimit());
                        return stmt15;
                    }
                    if (this.lexer.identifierEquals("CONTRIBUTORS")) {
                        this.lexer.nextToken();
                        return new MySqlShowContributorsStatement();
                    }
                    if (this.lexer.token() == Token.ALL) {
                        this.lexer.nextToken();
                        this.accept(Token.CREATE);
                        this.accept(Token.TABLE);
                        final SQLShowCreateTableStatement stmt16 = new SQLShowCreateTableStatement();
                        stmt16.setAll(true);
                        stmt16.setName(this.exprParser.name());
                        return stmt16;
                    }
                    if (this.lexer.token() == Token.CREATE) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.DATABASE || this.lexer.token() == Token.SCHEMA) {
                            this.lexer.nextToken();
                            final MySqlShowCreateDatabaseStatement stmt17 = new MySqlShowCreateDatabaseStatement();
                            if (this.lexer.token() == Token.IF) {
                                this.lexer.nextToken();
                                this.accept(Token.NOT);
                                this.accept(Token.EXISTS);
                                stmt17.setIfNotExists(true);
                            }
                            stmt17.setDatabase(this.exprParser.name());
                            return stmt17;
                        }
                        if (this.lexer.identifierEquals("EVENT")) {
                            this.lexer.nextToken();
                            final MySqlShowCreateEventStatement stmt18 = new MySqlShowCreateEventStatement();
                            stmt18.setEventName(this.exprParser.name());
                            return stmt18;
                        }
                        if (this.lexer.token() == Token.FUNCTION) {
                            this.lexer.nextToken();
                            final MySqlShowCreateFunctionStatement stmt19 = new MySqlShowCreateFunctionStatement();
                            stmt19.setName(this.exprParser.name());
                            return stmt19;
                        }
                        if (this.lexer.token() == Token.PROCEDURE) {
                            this.lexer.nextToken();
                            final MySqlShowCreateProcedureStatement stmt20 = new MySqlShowCreateProcedureStatement();
                            stmt20.setName(this.exprParser.name());
                            return stmt20;
                        }
                        if (this.lexer.token() == Token.TABLE) {
                            this.lexer.nextToken();
                            final SQLShowCreateTableStatement stmt16 = new SQLShowCreateTableStatement();
                            if (this.lexer.token() != Token.LIKE) {
                                stmt16.setName(this.exprParser.name());
                            }
                            if (this.lexer.token() == Token.LIKE) {
                                this.lexer.nextToken();
                                if (this.lexer.identifierEquals(FnvHash.Constants.MAPPING)) {
                                    this.lexer.nextToken();
                                    this.accept(Token.LPAREN);
                                    final SQLName name = this.exprParser.name();
                                    stmt16.setLikeMapping(name);
                                    this.accept(Token.RPAREN);
                                }
                            }
                            return stmt16;
                        }
                        if (this.lexer.token() == Token.VIEW) {
                            this.lexer.nextToken();
                            final SQLShowCreateViewStatement stmt21 = new SQLShowCreateViewStatement();
                            stmt21.setName(this.exprParser.name());
                            return stmt21;
                        }
                        if (this.lexer.token() == Token.TRIGGER) {
                            this.lexer.nextToken();
                            final MySqlShowCreateTriggerStatement stmt22 = new MySqlShowCreateTriggerStatement();
                            stmt22.setName(this.exprParser.name());
                            return stmt22;
                        }
                        if (this.lexer.token() == Token.FULLTEXT) {
                            this.lexer.nextToken();
                            final MysqlShowCreateFullTextStatement stmt23 = new MysqlShowCreateFullTextStatement();
                            stmt23.setType(this.parseFullTextType());
                            stmt23.setName(this.exprParser.name());
                            return stmt23;
                        }
                        if (this.lexer.identifierEquals("MATERIALIZED")) {
                            this.lexer.nextToken();
                            final SQLShowCreateMaterializedViewStatement stmt24 = new SQLShowCreateMaterializedViewStatement();
                            this.accept(Token.VIEW);
                            stmt24.setName(this.exprParser.name());
                            return stmt24;
                        }
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                    else {
                        if (this.lexer.identifierEquals("ENGINE")) {
                            this.lexer.nextToken();
                            final MySqlShowEngineStatement stmt25 = new MySqlShowEngineStatement();
                            stmt25.setName(this.exprParser.name());
                            stmt25.setOption(MySqlShowEngineStatement.Option.valueOf(this.lexer.stringVal().toUpperCase()));
                            this.lexer.nextToken();
                            return stmt25;
                        }
                        if (this.lexer.token() == Token.DATABASE || this.lexer.identifierEquals(FnvHash.Constants.DB)) {
                            this.lexer.nextToken();
                            final MySqlShowDatabaseStatusStatement stmt26 = new MySqlShowDatabaseStatusStatement();
                            if (full) {
                                stmt26.setFull(true);
                            }
                            if (this.lexer.identifierEquals("STATUS")) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.LIKE) {
                                    this.lexer.nextTokenValue();
                                    stmt26.setName(this.exprParser.name());
                                }
                                else {
                                    if (this.lexer.token() == Token.WHERE) {
                                        this.lexer.nextToken();
                                        final SQLExpr where = this.exprParser.expr();
                                        stmt26.setWhere(where);
                                    }
                                    if (this.lexer.token() == Token.ORDER) {
                                        final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                        stmt26.setOrderBy(orderBy);
                                    }
                                    if (this.lexer.token() == Token.LIMIT) {
                                        final SQLLimit limit = this.exprParser.parseLimit();
                                        stmt26.setLimit(limit);
                                    }
                                }
                            }
                            return stmt26;
                        }
                        if (this.lexer.identifierEquals("STORAGE")) {
                            this.lexer.nextToken();
                            this.acceptIdentifier("ENGINES");
                            final MySqlShowEnginesStatement stmt27 = new MySqlShowEnginesStatement();
                            stmt27.setStorage(true);
                            return stmt27;
                        }
                        if (this.lexer.identifierEquals("ENGINES")) {
                            this.lexer.nextToken();
                            final MySqlShowEnginesStatement stmt27 = new MySqlShowEnginesStatement();
                            return stmt27;
                        }
                        if (this.lexer.identifierEquals("EVENTS")) {
                            this.lexer.nextToken();
                            final MySqlShowEventsStatement stmt28 = new MySqlShowEventsStatement();
                            if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                this.lexer.nextToken();
                                stmt28.setSchema(this.exprParser.name());
                            }
                            if (this.lexer.token() == Token.LIKE) {
                                this.lexer.nextTokenValue();
                                stmt28.setLike(this.exprParser.expr());
                            }
                            if (this.lexer.token() == Token.WHERE) {
                                this.lexer.nextToken();
                                stmt28.setWhere(this.exprParser.expr());
                            }
                            return stmt28;
                        }
                        if (this.lexer.token() == Token.FUNCTION) {
                            this.lexer.nextToken();
                            if (this.lexer.identifierEquals("CODE")) {
                                this.lexer.nextToken();
                                final MySqlShowFunctionCodeStatement stmt29 = new MySqlShowFunctionCodeStatement();
                                stmt29.setName(this.exprParser.name());
                                return stmt29;
                            }
                            this.acceptIdentifier("STATUS");
                            final MySqlShowFunctionStatusStatement stmt30 = new MySqlShowFunctionStatusStatement();
                            if (this.lexer.token() == Token.LIKE) {
                                this.lexer.nextTokenValue();
                                stmt30.setLike(this.exprParser.expr());
                            }
                            if (this.lexer.token() == Token.WHERE) {
                                this.lexer.nextToken();
                                stmt30.setWhere(this.exprParser.expr());
                            }
                            return stmt30;
                        }
                        else {
                            if (this.lexer.identifierEquals("ENGINE")) {
                                this.lexer.nextToken();
                                final MySqlShowEngineStatement stmt25 = new MySqlShowEngineStatement();
                                stmt25.setName(this.exprParser.name());
                                stmt25.setOption(MySqlShowEngineStatement.Option.valueOf(this.lexer.stringVal().toUpperCase()));
                                this.lexer.nextToken();
                                return stmt25;
                            }
                            if (this.lexer.identifierEquals("STORAGE")) {
                                this.lexer.nextToken();
                                this.accept(Token.EQ);
                                this.accept(Token.DEFAULT);
                                final MySqlShowEnginesStatement stmt27 = new MySqlShowEnginesStatement();
                                stmt27.setStorage(true);
                                return stmt27;
                            }
                            if (this.lexer.identifierEquals("GRANTS")) {
                                this.lexer.nextToken();
                                final MySqlShowGrantsStatement stmt31 = new MySqlShowGrantsStatement();
                                if (this.lexer.token() == Token.FOR) {
                                    this.lexer.nextToken();
                                    stmt31.setUser(this.exprParser.expr());
                                }
                                if (this.lexer.token() == Token.ON) {
                                    this.lexer.nextToken();
                                    final SQLExpr on = this.exprParser.expr();
                                    stmt31.setOn(on);
                                }
                                return stmt31;
                            }
                            if (this.lexer.token() == Token.INDEX || this.lexer.identifierEquals("INDEXES") || this.lexer.identifierEquals("KEYS")) {
                                final SQLShowIndexesStatement stmt32 = new SQLShowIndexesStatement();
                                stmt32.setType(this.lexer.stringVal());
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                    this.lexer.nextToken();
                                    final SQLName table = this.exprParser.name();
                                    stmt32.setTable(table);
                                    if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                        this.lexer.nextToken();
                                        final SQLName database = this.exprParser.name();
                                        stmt32.setDatabase(database.toString());
                                    }
                                    if (this.lexer.token() == Token.WHERE) {
                                        this.lexer.nextToken();
                                        final SQLExpr where2 = this.exprParser.expr();
                                        stmt32.setWhere(where2);
                                    }
                                }
                                if (this.lexer.token() == Token.HINT) {
                                    stmt32.setHints(this.exprParser.parseHints());
                                }
                                return stmt32;
                            }
                            if (this.lexer.token() == Token.OPEN || this.lexer.identifierEquals("OPEN")) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("TABLES");
                                final MySqlShowOpenTablesStatement stmt33 = new MySqlShowOpenTablesStatement();
                                if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                    this.lexer.nextToken();
                                    stmt33.setDatabase(this.exprParser.name());
                                }
                                if (this.lexer.token() == Token.LIKE) {
                                    this.lexer.nextTokenValue();
                                    stmt33.setLike(this.exprParser.expr());
                                }
                                if (this.lexer.token() == Token.WHERE) {
                                    this.lexer.nextToken();
                                    stmt33.setWhere(this.exprParser.expr());
                                }
                                return stmt33;
                            }
                            if (this.lexer.identifierEquals("PLUGINS")) {
                                this.lexer.nextToken();
                                final MySqlShowPluginsStatement stmt34 = new MySqlShowPluginsStatement();
                                return stmt34;
                            }
                            if (this.lexer.identifierEquals("HTC")) {
                                this.lexer.nextToken();
                                final MysqlShowHtcStatement stmt35 = new MysqlShowHtcStatement();
                                stmt35.setFull(false);
                                return stmt35;
                            }
                            if (this.lexer.identifierEquals("HMSMETA")) {
                                this.lexer.nextToken();
                                final SQLName name2 = this.exprParser.name();
                                final MySqlShowHMSMetaStatement stmt36 = new MySqlShowHMSMetaStatement();
                                stmt36.setName(name2);
                                return stmt36;
                            }
                            if (this.lexer.identifierEquals("STC")) {
                                this.lexer.nextToken();
                                final MysqlShowStcStatement stmt37 = new MysqlShowStcStatement();
                                if (this.lexer.identifierEquals("HIS")) {
                                    this.lexer.nextToken();
                                    stmt37.setHis(true);
                                }
                                else {
                                    stmt37.setHis(false);
                                }
                                return stmt37;
                            }
                            if (this.lexer.identifierEquals("PRIVILEGES")) {
                                this.lexer.nextToken();
                                final MySqlShowPrivilegesStatement stmt38 = new MySqlShowPrivilegesStatement();
                                return stmt38;
                            }
                            if (this.lexer.token() == Token.PROCEDURE) {
                                this.lexer.nextToken();
                                if (this.lexer.identifierEquals("CODE")) {
                                    this.lexer.nextToken();
                                    final MySqlShowProcedureCodeStatement stmt39 = new MySqlShowProcedureCodeStatement();
                                    stmt39.setName(this.exprParser.name());
                                    return stmt39;
                                }
                                this.acceptIdentifier("STATUS");
                                final MySqlShowProcedureStatusStatement stmt40 = new MySqlShowProcedureStatusStatement();
                                if (this.lexer.token() == Token.LIKE) {
                                    this.lexer.nextTokenValue();
                                    stmt40.setLike(this.exprParser.expr());
                                }
                                if (this.lexer.token() == Token.WHERE) {
                                    this.lexer.nextToken();
                                    stmt40.setWhere(this.exprParser.expr());
                                }
                                return stmt40;
                            }
                            else {
                                if (this.lexer.identifierEquals("PROFILES")) {
                                    this.lexer.nextToken();
                                    final MySqlShowProfilesStatement stmt41 = new MySqlShowProfilesStatement();
                                    return stmt41;
                                }
                                if (this.lexer.identifierEquals("PROFILE")) {
                                    this.lexer.nextToken();
                                    final MySqlShowProfileStatement stmt42 = new MySqlShowProfileStatement();
                                    while (true) {
                                        if (this.lexer.token() == Token.ALL) {
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.ALL);
                                            this.lexer.nextToken();
                                        }
                                        else if (this.lexer.identifierEquals("BLOCK")) {
                                            this.lexer.nextToken();
                                            this.acceptIdentifier("IO");
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.BLOCK_IO);
                                        }
                                        else if (this.lexer.identifierEquals("CONTEXT")) {
                                            this.lexer.nextToken();
                                            this.acceptIdentifier("SWITCHES");
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.CONTEXT_SWITCHES);
                                        }
                                        else if (this.lexer.identifierEquals("CPU")) {
                                            this.lexer.nextToken();
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.CPU);
                                        }
                                        else if (this.lexer.identifierEquals("IPC")) {
                                            this.lexer.nextToken();
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.IPC);
                                        }
                                        else if (this.lexer.identifierEquals("MEMORY")) {
                                            this.lexer.nextToken();
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.MEMORY);
                                        }
                                        else if (this.lexer.identifierEquals("PAGE")) {
                                            this.lexer.nextToken();
                                            this.acceptIdentifier("FAULTS");
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.PAGE_FAULTS);
                                        }
                                        else if (this.lexer.identifierEquals("SOURCE")) {
                                            this.lexer.nextToken();
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.SOURCE);
                                        }
                                        else {
                                            if (!this.lexer.identifierEquals("SWAPS")) {
                                                break;
                                            }
                                            this.lexer.nextToken();
                                            stmt42.getTypes().add(MySqlShowProfileStatement.Type.SWAPS);
                                        }
                                        if (this.lexer.token() != Token.COMMA) {
                                            break;
                                        }
                                        this.lexer.nextToken();
                                    }
                                    if (this.lexer.token() == Token.FOR) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("QUERY");
                                        stmt42.setForQuery(this.exprParser.primary());
                                    }
                                    stmt42.setLimit(this.exprParser.parseLimit());
                                    return stmt42;
                                }
                                if (this.lexer.identifierEquals("RELAYLOG")) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("EVENTS");
                                    final MySqlShowRelayLogEventsStatement stmt43 = new MySqlShowRelayLogEventsStatement();
                                    if (this.lexer.token() == Token.IN) {
                                        this.lexer.nextToken();
                                        stmt43.setLogName(this.exprParser.primary());
                                    }
                                    if (this.lexer.token() == Token.FROM) {
                                        this.lexer.nextToken();
                                        stmt43.setFrom(this.exprParser.primary());
                                    }
                                    stmt43.setLimit(this.exprParser.parseLimit());
                                    return stmt43;
                                }
                                if (this.lexer.identifierEquals("RELAYLOG")) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("EVENTS");
                                    final MySqlShowRelayLogEventsStatement stmt43 = new MySqlShowRelayLogEventsStatement();
                                    if (this.lexer.token() == Token.IN) {
                                        this.lexer.nextToken();
                                        stmt43.setLogName(this.exprParser.primary());
                                    }
                                    if (this.lexer.token() == Token.FROM) {
                                        this.lexer.nextToken();
                                        stmt43.setFrom(this.exprParser.primary());
                                    }
                                    stmt43.setLimit(this.exprParser.parseLimit());
                                    return stmt43;
                                }
                                if (this.lexer.identifierEquals("SLAVE")) {
                                    this.lexer.nextToken();
                                    if (this.lexer.identifierEquals("STATUS")) {
                                        this.lexer.nextToken();
                                        return new MySqlShowSlaveStatusStatement();
                                    }
                                    this.acceptIdentifier("HOSTS");
                                    final MySqlShowSlaveHostsStatement stmt44 = new MySqlShowSlaveHostsStatement();
                                    return stmt44;
                                }
                                else {
                                    if (this.lexer.token() == Token.TABLE) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("STATUS");
                                        final MySqlShowTableStatusStatement stmt45 = new MySqlShowTableStatusStatement();
                                        if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                            this.lexer.nextToken();
                                            final SQLName name = this.exprParser.name();
                                            if (name instanceof SQLPropertyExpr) {
                                                stmt45.setDatabase((SQLName)((SQLPropertyExpr)name).getOwner());
                                                stmt45.setTableGroup(new SQLIdentifierExpr(((SQLPropertyExpr)name).getName()));
                                            }
                                            else {
                                                stmt45.setDatabase(name);
                                            }
                                        }
                                        if (this.lexer.token() == Token.LIKE) {
                                            this.lexer.nextTokenValue();
                                            stmt45.setLike(this.exprParser.expr());
                                        }
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            stmt45.setWhere(this.exprParser.expr());
                                        }
                                        return stmt45;
                                    }
                                    if (this.lexer.identifierEquals("TRIGGERS")) {
                                        this.lexer.nextToken();
                                        final MySqlShowTriggersStatement stmt46 = new MySqlShowTriggersStatement();
                                        if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                            this.lexer.nextToken();
                                            final SQLName database2 = this.exprParser.name();
                                            stmt46.setDatabase(database2);
                                        }
                                        if (this.lexer.token() == Token.LIKE) {
                                            this.lexer.nextTokenValue();
                                            final SQLExpr like = this.exprParser.expr();
                                            stmt46.setLike(like);
                                        }
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt46.setWhere(where);
                                        }
                                        return stmt46;
                                    }
                                    if (this.lexer.identifierEquals(FnvHash.Constants.BROADCASTS)) {
                                        this.lexer.nextToken();
                                        final MySqlShowBroadcastsStatement stmt47 = new MySqlShowBroadcastsStatement();
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt47.setWhere(where);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                            stmt47.setOrderBy(orderBy);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit = this.exprParser.parseLimit();
                                            stmt47.setLimit(limit);
                                        }
                                        return stmt47;
                                    }
                                    if (this.lexer.identifierEquals(FnvHash.Constants.DATASOURCES)) {
                                        this.lexer.nextToken();
                                        final MySqlShowDatasourcesStatement stmt48 = new MySqlShowDatasourcesStatement();
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt48.setWhere(where);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                            stmt48.setOrderBy(orderBy);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit = this.exprParser.parseLimit();
                                            stmt48.setLimit(limit);
                                        }
                                        return stmt48;
                                    }
                                    if (this.lexer.identifierEquals(FnvHash.Constants.NODE)) {
                                        this.lexer.nextToken();
                                        final MySqlShowNodeStatement stmt49 = new MySqlShowNodeStatement();
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt49.setWhere(where);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                            stmt49.setOrderBy(orderBy);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit = this.exprParser.parseLimit();
                                            stmt49.setLimit(limit);
                                        }
                                        return stmt49;
                                    }
                                    if (this.lexer.identifierEquals(FnvHash.Constants.HELP)) {
                                        this.lexer.nextToken();
                                        final MySqlShowHelpStatement stmt50 = new MySqlShowHelpStatement();
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt50.setWhere(where);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                            stmt50.setOrderBy(orderBy);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit = this.exprParser.parseLimit();
                                            stmt50.setLimit(limit);
                                        }
                                        return stmt50;
                                    }
                                    if (this.lexer.token() == Token.SEQUENCE || this.lexer.identifierEquals(FnvHash.Constants.SEQUENCES)) {
                                        this.lexer.nextToken();
                                        final MySqlShowSequencesStatement stmt51 = new MySqlShowSequencesStatement();
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where = this.exprParser.expr();
                                            stmt51.setWhere(where);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                            stmt51.setOrderBy(orderBy);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit = this.exprParser.parseLimit();
                                            stmt51.setLimit(limit);
                                        }
                                        return stmt51;
                                    }
                                    if (this.lexer.identifierEquals("PARTITIONS")) {
                                        this.lexer.nextToken();
                                        final SQLShowPartitionsStmt stmt52 = new SQLShowPartitionsStmt();
                                        if (this.lexer.token() == Token.FROM) {
                                            this.lexer.nextToken();
                                        }
                                        final SQLName name = this.exprParser.name();
                                        stmt52.setTableSource(name);
                                        if (this.lexer.token() == Token.PARTITION) {
                                            this.lexer.nextToken();
                                            this.accept(Token.LPAREN);
                                            this.parseAssignItems(stmt52.getPartition(), stmt52, false);
                                            this.accept(Token.RPAREN);
                                        }
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            stmt52.setWhere(this.exprParser.expr());
                                        }
                                        return stmt52;
                                    }
                                    if (this.lexer.identifierEquals("RULE")) {
                                        this.lexer.nextToken();
                                        boolean version = false;
                                        if (this.lexer.identifierEquals(FnvHash.Constants.VERSION)) {
                                            version = true;
                                            this.lexer.nextToken();
                                        }
                                        else if (this.lexer.identifierEquals(FnvHash.Constants.FULL) || this.lexer.token() == Token.FULL) {
                                            full = true;
                                            this.lexer.nextToken();
                                        }
                                        if (this.lexer.identifierEquals(FnvHash.Constants.STATUS)) {
                                            this.lexer.nextToken();
                                            final MySqlShowRuleStatusStatement stmt53 = new MySqlShowRuleStatusStatement();
                                            if (full) {
                                                stmt53.setFull(full);
                                            }
                                            if (version) {
                                                stmt53.setVersion(true);
                                            }
                                            if (this.lexer.token() == Token.FROM) {
                                                this.lexer.nextToken();
                                            }
                                            if (full) {
                                                stmt53.setLite(false);
                                            }
                                            if (this.lexer.token() == Token.WHERE) {
                                                this.lexer.nextToken();
                                                final SQLExpr where2 = this.exprParser.expr();
                                                stmt53.setWhere(where2);
                                            }
                                            if (this.lexer.token() == Token.ORDER) {
                                                final SQLOrderBy orderBy2 = this.exprParser.parseOrderBy();
                                                stmt53.setOrderBy(orderBy2);
                                            }
                                            if (this.lexer.token() == Token.LIMIT) {
                                                final SQLLimit limit2 = this.exprParser.parseLimit();
                                                stmt53.setLimit(limit2);
                                            }
                                            return stmt53;
                                        }
                                        final MySqlShowRuleStatement stmt54 = new MySqlShowRuleStatement();
                                        if (full) {
                                            stmt54.setFull(full);
                                        }
                                        if (version) {
                                            stmt54.setVersion(true);
                                        }
                                        if (this.lexer.identifierEquals(FnvHash.Constants.VERSION)) {
                                            this.lexer.nextToken();
                                            stmt54.setVersion(true);
                                        }
                                        if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.SEMI) {
                                            return stmt54;
                                        }
                                        if (this.lexer.token() == Token.FROM) {
                                            this.lexer.nextToken();
                                            stmt54.setName(this.exprParser.name());
                                        }
                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            final SQLExpr where2 = this.exprParser.expr();
                                            stmt54.setWhere(where2);
                                        }
                                        if (this.lexer.token() == Token.ORDER) {
                                            final SQLOrderBy orderBy2 = this.exprParser.parseOrderBy();
                                            stmt54.setOrderBy(orderBy2);
                                        }
                                        if (this.lexer.token() == Token.LIMIT) {
                                            final SQLLimit limit2 = this.exprParser.parseLimit();
                                            stmt54.setLimit(limit2);
                                        }
                                        return stmt54;
                                    }
                                    else {
                                        if (this.lexer.identifierEquals("DS")) {
                                            this.lexer.nextToken();
                                            final MySqlShowDsStatement stmt55 = new MySqlShowDsStatement();
                                            if (this.lexer.token() == Token.WHERE) {
                                                this.lexer.nextToken();
                                                final SQLExpr where = this.exprParser.expr();
                                                stmt55.setWhere(where);
                                            }
                                            if (this.lexer.token() == Token.ORDER) {
                                                final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                stmt55.setOrderBy(orderBy);
                                            }
                                            if (this.lexer.token() == Token.LIMIT) {
                                                final SQLLimit limit = this.exprParser.parseLimit();
                                                stmt55.setLimit(limit);
                                            }
                                            return stmt55;
                                        }
                                        if (this.lexer.identifierEquals("DDL")) {
                                            this.lexer.nextToken();
                                            if (this.isEnabled(SQLParserFeature.DRDSAsyncDDL) && !this.lexer.identifierEquals("STATUS")) {
                                                final DrdsShowDDLJobs showDDLJobs = new DrdsShowDDLJobs();
                                                showDDLJobs.setFull(full);
                                                while (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
                                                    showDDLJobs.addJobId(this.lexer.integerValue().longValue());
                                                    this.accept(Token.LITERAL_INT);
                                                    if (Token.COMMA == this.lexer.token()) {
                                                        this.lexer.nextToken();
                                                    }
                                                    else {
                                                        if (this.lexer.token() == Token.EOF) {
                                                            break;
                                                        }
                                                        if (this.lexer.token() == Token.SEMI) {
                                                            break;
                                                        }
                                                        throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
                                                    }
                                                }
                                                return showDDLJobs;
                                            }
                                            this.acceptIdentifier("STATUS");
                                            final MySqlShowDdlStatusStatement stmt56 = new MySqlShowDdlStatusStatement();
                                            if (this.lexer.token() == Token.WHERE) {
                                                this.lexer.nextToken();
                                                final SQLExpr where = this.exprParser.expr();
                                                stmt56.setWhere(where);
                                            }
                                            if (this.lexer.token() == Token.ORDER) {
                                                final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                stmt56.setOrderBy(orderBy);
                                            }
                                            if (this.lexer.token() == Token.LIMIT) {
                                                final SQLLimit limit = this.exprParser.parseLimit();
                                                stmt56.setLimit(limit);
                                            }
                                            return stmt56;
                                        }
                                        else {
                                            if (this.lexer.identifierEquals("TRACE")) {
                                                this.lexer.nextToken();
                                                final MySqlShowTraceStatement stmt57 = new MySqlShowTraceStatement();
                                                if (this.lexer.token() == Token.WHERE) {
                                                    this.lexer.nextToken();
                                                    final SQLExpr where = this.exprParser.expr();
                                                    stmt57.setWhere(where);
                                                }
                                                if (this.lexer.token() == Token.ORDER) {
                                                    final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                    stmt57.setOrderBy(orderBy);
                                                }
                                                if (this.lexer.token() == Token.LIMIT) {
                                                    final SQLLimit limit = this.exprParser.parseLimit();
                                                    stmt57.setLimit(limit);
                                                }
                                                return stmt57;
                                            }
                                            if (this.lexer.identifierEquals("TOPOLOGY")) {
                                                this.lexer.nextToken();
                                                final MySqlShowTopologyStatement stmt58 = new MySqlShowTopologyStatement();
                                                if (this.lexer.token() == Token.FROM) {
                                                    this.lexer.nextToken();
                                                }
                                                stmt58.setName(this.exprParser.name());
                                                if (this.lexer.token() == Token.WHERE) {
                                                    this.lexer.nextToken();
                                                    final SQLExpr where = this.exprParser.expr();
                                                    stmt58.setWhere(where);
                                                }
                                                if (this.lexer.token() == Token.ORDER) {
                                                    final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                    stmt58.setOrderBy(orderBy);
                                                }
                                                if (this.lexer.token() == Token.LIMIT) {
                                                    final SQLLimit limit = this.exprParser.parseLimit();
                                                    stmt58.setLimit(limit);
                                                }
                                                stmt58.setFull(full);
                                                return stmt58;
                                            }
                                            if (this.lexer.identifierEquals(FnvHash.Constants.PLANCACHE)) {
                                                this.lexer.nextToken();
                                                if (this.lexer.identifierEquals(FnvHash.Constants.STATUS)) {
                                                    this.lexer.nextToken();
                                                    return new MySqlShowPlanCacheStatusStatement();
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.PLAN)) {
                                                    this.lexer.nextToken();
                                                    final SQLSelect select = this.createSQLSelectParser().select();
                                                    return new MySqlShowPlanCacheStatement(select);
                                                }
                                                throw new ParserException("TODO " + this.lexer.info());
                                            }
                                            else {
                                                if (this.lexer.identifierEquals(FnvHash.Constants.SLOW)) {
                                                    final MySqlShowSlowStatement stmt59 = this.parserShowSlow();
                                                    stmt59.setPhysical(false);
                                                    stmt59.setFull(full);
                                                    return stmt59;
                                                }
                                                if (this.lexer.identifierEquals("PHYSICAL_SLOW")) {
                                                    final MySqlShowSlowStatement stmt59 = this.parserShowSlow();
                                                    stmt59.setPhysical(true);
                                                    stmt59.setFull(full);
                                                    return stmt59;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.QUERY_TASK)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowQueryTaskStatement stmt60 = new SQLShowQueryTaskStatement();
                                                    stmt60.setDbType(this.dbType);
                                                    if (this.lexer.token() == Token.FOR) {
                                                        this.lexer.nextToken();
                                                        stmt60.setUser(this.exprParser.expr());
                                                    }
                                                    if (this.lexer.token() == Token.WHERE) {
                                                        this.lexer.nextToken();
                                                        final SQLExpr where = this.exprParser.expr();
                                                        stmt60.setWhere(where);
                                                    }
                                                    if (this.lexer.token() == Token.ORDER) {
                                                        final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                        stmt60.setOrderBy(orderBy);
                                                    }
                                                    if (this.lexer.token() == Token.LIMIT) {
                                                        final SQLLimit limit = this.exprParser.parseLimit();
                                                        stmt60.setLimit(limit);
                                                    }
                                                    if (full) {
                                                        stmt60.setFull(true);
                                                    }
                                                    return stmt60;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.OUTLINES)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowOutlinesStatement stmt61 = new SQLShowOutlinesStatement();
                                                    stmt61.setDbType(this.dbType);
                                                    if (this.lexer.token() == Token.WHERE) {
                                                        this.lexer.nextToken();
                                                        final SQLExpr where = this.exprParser.expr();
                                                        stmt61.setWhere(where);
                                                    }
                                                    if (this.lexer.token() == Token.ORDER) {
                                                        final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                                                        stmt61.setOrderBy(orderBy);
                                                    }
                                                    if (this.lexer.token() == Token.LIMIT) {
                                                        final SQLLimit limit = this.exprParser.parseLimit();
                                                        stmt61.setLimit(limit);
                                                    }
                                                    return stmt61;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.RECYCLEBIN)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowRecylebinStatement stmt62 = new SQLShowRecylebinStatement();
                                                    stmt62.setDbType(this.dbType);
                                                    return stmt62;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUPS)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowTableGroupsStatement stmt63 = this.parseShowTableGroups();
                                                    return stmt63;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.CATALOGS)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowCatalogsStatement stmt64 = new SQLShowCatalogsStatement();
                                                    if (this.lexer.token() == Token.LIKE) {
                                                        this.lexer.nextToken();
                                                        final SQLExpr like = this.exprParser.expr();
                                                        stmt64.setLike(like);
                                                    }
                                                    return stmt64;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.FUNCTIONS)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowFunctionsStatement stmt65 = new SQLShowFunctionsStatement();
                                                    if (this.lexer.token() == Token.LIKE) {
                                                        this.lexer.nextToken();
                                                        final SQLExpr like = this.exprParser.expr();
                                                        stmt65.setLike(like);
                                                    }
                                                    return stmt65;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.SCHEMAS)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowDatabasesStatement stmt4 = new SQLShowDatabasesStatement();
                                                    stmt4.setPhysical(isPhysical);
                                                    if (this.lexer.token() == Token.IN || this.lexer.token() == Token.FROM) {
                                                        this.lexer.nextToken();
                                                        final SQLName db = this.exprParser.name();
                                                        stmt4.setDatabase(db);
                                                    }
                                                    if (full) {
                                                        stmt4.setFull(true);
                                                    }
                                                    if (this.lexer.token() == Token.LIKE) {
                                                        this.lexer.nextToken();
                                                        final SQLExpr like = this.exprParser.expr();
                                                        stmt4.setLike(like);
                                                    }
                                                    return stmt4;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.CONFIG)) {
                                                    this.lexer.nextToken();
                                                    final SQLName name2 = this.exprParser.name();
                                                    final MySqlShowConfigStatement stmt66 = new MySqlShowConfigStatement();
                                                    stmt66.setName(name2);
                                                    return stmt66;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.USERS)) {
                                                    this.lexer.nextToken();
                                                    final SQLShowUsersStatement stmt67 = new SQLShowUsersStatement();
                                                    stmt67.setDbType(this.dbType);
                                                    return stmt67;
                                                }
                                                if (this.lexer.identifierEquals(FnvHash.Constants.PHYSICAL_PROCESSLIST)) {
                                                    this.lexer.nextToken();
                                                    final MySqlShowPhysicalProcesslistStatement stmt68 = new MySqlShowPhysicalProcesslistStatement();
                                                    if (full) {
                                                        stmt68.setFull(full);
                                                    }
                                                    return stmt68;
                                                }
                                                if (this.lexer.identifierEquals("MATERIALIZED")) {
                                                    return this.parseShowMaterializedView();
                                                }
                                                if (this.lexer.token() == Token.FULLTEXT) {
                                                    this.lexer.nextToken();
                                                    final MysqlShowFullTextStatement stmt69 = new MysqlShowFullTextStatement();
                                                    if (this.lexer.identifierEquals(FnvHash.Constants.CHARFILTERS)) {
                                                        stmt69.setType(FullTextType.CHARFILTER);
                                                    }
                                                    else if (this.lexer.identifierEquals(FnvHash.Constants.TOKENIZERS)) {
                                                        stmt69.setType(FullTextType.TOKENIZER);
                                                    }
                                                    else if (this.lexer.identifierEquals(FnvHash.Constants.TOKENFILTERS)) {
                                                        stmt69.setType(FullTextType.TOKENFILTER);
                                                    }
                                                    else if (this.lexer.identifierEquals(FnvHash.Constants.ANALYZERS)) {
                                                        stmt69.setType(FullTextType.ANALYZER);
                                                    }
                                                    else {
                                                        if (!this.lexer.identifierEquals(FnvHash.Constants.DICTIONARIES)) {
                                                            throw new ParserException("type of full text must be [CHARFILTERS/TOKENIZERS/TOKENFILTERS/ANALYZERS/DICTIONARYS] .");
                                                        }
                                                        stmt69.setType(FullTextType.DICTIONARY);
                                                    }
                                                    this.lexer.nextToken();
                                                    return stmt69;
                                                }
                                                if (!this.isEnabled(SQLParserFeature.DRDSAsyncDDL) || !this.lexer.identifierEquals("METADATA")) {
                                                    throw new ParserException("TODO " + this.lexer.info());
                                                }
                                                this.lexer.nextToken();
                                                if (Token.LOCK != this.lexer.token() && !this.lexer.identifierEquals("LOCKS")) {
                                                    throw new ParserException("syntax error, expect LOCK/LOCKS, actual " + this.lexer.token() + ", " + this.lexer.info());
                                                }
                                                this.lexer.nextToken();
                                                final DrdsShowMetadataLock stmt70 = new DrdsShowMetadataLock();
                                                if (Token.SEMI == this.lexer.token() || Token.EOF == this.lexer.token()) {
                                                    return stmt70;
                                                }
                                                stmt70.setSchemaName(this.exprParser.name());
                                                return stmt70;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private MySqlShowStatusStatement parseShowStatus() {
        final MySqlShowStatusStatement stmt = new MySqlShowStatusStatement();
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            final SQLExpr like = this.exprParser.expr();
            stmt.setLike(like);
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        return stmt;
    }
    
    public MySqlShowSlowStatement parserShowSlow() {
        this.lexer.nextToken();
        final MySqlShowSlowStatement stmt = new MySqlShowSlowStatement();
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
            stmt.setOrderBy(orderBy);
        }
        if (this.lexer.token() == Token.LIMIT) {
            final SQLLimit limit = this.exprParser.parseLimit();
            stmt.setLimit(limit);
        }
        return stmt;
    }
    
    private MySqlShowWarningsStatement parseShowWarnings() {
        final MySqlShowWarningsStatement stmt = new MySqlShowWarningsStatement();
        stmt.setLimit(this.exprParser.parseLimit());
        return stmt;
    }
    
    public SQLStartTransactionStatement parseStart() {
        this.acceptIdentifier("START");
        this.acceptIdentifier("TRANSACTION");
        final SQLStartTransactionStatement stmt = new SQLStartTransactionStatement(this.dbType);
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("CONSISTENT");
            this.acceptIdentifier("SNAPSHOT");
            stmt.setConsistentSnapshot(true);
        }
        if (this.lexer.token() == Token.BEGIN) {
            this.lexer.nextToken();
            stmt.setBegin(true);
            if (this.lexer.identifierEquals("WORK")) {
                this.lexer.nextToken();
                stmt.setWork(true);
            }
        }
        if (this.lexer.token() == Token.HINT) {
            stmt.setHints(this.exprParser.parseHints());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ISOLATION)) {
            this.lexer.nextToken();
            this.acceptIdentifier("LEVEL");
            if (this.lexer.identifierEquals(FnvHash.Constants.READ)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.UNCOMMITTED)) {
                    this.lexer.nextToken();
                    stmt.setIsolationLevel(SQLStartTransactionStatement.IsolationLevel.READ_UNCOMMITTED);
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.COMMITTED)) {
                        throw new ParserException(this.lexer.info());
                    }
                    this.lexer.nextToken();
                    stmt.setIsolationLevel(SQLStartTransactionStatement.IsolationLevel.READ_COMMITTED);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.REPEATABLE)) {
                this.lexer.nextToken();
                this.acceptIdentifier("READ");
                stmt.setIsolationLevel(SQLStartTransactionStatement.IsolationLevel.REPEATABLE_READ);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.SERIALIZABLE)) {
                    throw new ParserException(this.lexer.info());
                }
                this.lexer.nextToken();
                stmt.setIsolationLevel(SQLStartTransactionStatement.IsolationLevel.SERIALIZABLE);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.READ)) {
            this.lexer.nextToken();
            this.acceptIdentifier("ONLY");
            stmt.setReadOnly(true);
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseRollback() {
        this.acceptIdentifier("ROLLBACK");
        if (!this.isEnabled(SQLParserFeature.DRDSAsyncDDL) || !this.lexer.identifierEquals("DDL")) {
            final SQLRollbackStatement stmt = new SQLRollbackStatement();
            if (this.lexer.identifierEquals("WORK")) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.AND) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("CHAIN");
                    stmt.setChain(Boolean.FALSE);
                }
                else {
                    this.acceptIdentifier("CHAIN");
                    stmt.setChain(Boolean.TRUE);
                }
            }
            if (this.lexer.token() == Token.TO) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("SAVEPOINT")) {
                    this.lexer.nextToken();
                }
                stmt.setTo(this.exprParser.name());
            }
            return stmt;
        }
        this.lexer.nextToken();
        final DrdsRollbackDDLJob stmt2 = new DrdsRollbackDDLJob();
        while (true) {
            stmt2.addJobId(this.lexer.integerValue().longValue());
            this.accept(Token.LITERAL_INT);
            if (Token.COMMA != this.lexer.token()) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token() != Token.EOF && this.lexer.token() != Token.SEMI) {
            throw new ParserException("syntax error, expect job id, actual " + this.lexer.token() + ", " + this.lexer.info());
        }
        return stmt2;
    }
    
    @Override
    public SQLStatement parseCommit() {
        this.acceptIdentifier("COMMIT");
        final SQLCommitStatement stmt = new SQLCommitStatement();
        if (this.lexer.identifierEquals("WORK")) {
            this.lexer.nextToken();
            stmt.setWork(true);
        }
        if (this.lexer.token() == Token.AND) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.FALSE);
            }
            else {
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.TRUE);
            }
        }
        return stmt;
    }
    
    public SQLReplaceStatement parseReplace() {
        final SQLReplaceStatement stmt = new SQLReplaceStatement();
        stmt.setDbType(DbType.mysql);
        final List<SQLCommentHint> list = new ArrayList<SQLCommentHint>();
        while (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(list);
        }
        stmt.setHeadHints(list);
        this.accept(Token.REPLACE);
        while (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(stmt.getHints());
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DELAYED)) {
            stmt.setDelayed(true);
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
        }
        final SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLAssignItem ptExpr = new SQLAssignItem();
                ptExpr.setTarget(this.exprParser.name());
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                    final SQLExpr ptValue = this.exprParser.expr();
                    ptExpr.setValue(ptValue);
                }
                stmt.addPartition(ptExpr);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                final SQLQueryExpr queryExpr = (SQLQueryExpr)this.exprParser.expr();
                stmt.setQuery(queryExpr);
            }
            else {
                this.exprParser.exprList(stmt.getColumns(), stmt);
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.VALUES || this.lexer.identifierEquals("VALUE")) {
            this.lexer.nextToken();
            this.parseValueClause(stmt.getValuesList(), null, 0, stmt);
        }
        else if (this.lexer.token() == Token.SELECT) {
            final SQLQueryExpr queryExpr = (SQLQueryExpr)this.exprParser.expr();
            stmt.setQuery(queryExpr);
        }
        else if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
            values.setParent(stmt);
            stmt.getValuesList().add(values);
            while (true) {
                stmt.addColumn(this.exprParser.name());
                if (this.lexer.token() == Token.COLONEQ) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.EQ);
                }
                values.addValue(this.exprParser.expr());
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token() == Token.LPAREN) {
            final SQLSelect select = this.createSQLSelectParser().select();
            final SQLQueryExpr queryExpr2 = new SQLQueryExpr(select);
            stmt.setQuery(queryExpr2);
        }
        return stmt;
    }
    
    protected SQLStatement parseLoad() {
        this.acceptIdentifier("LOAD");
        if (this.lexer.identifierEquals("DATA")) {
            final SQLStatement stmt = this.parseLoadDataInFile();
            return stmt;
        }
        if (this.lexer.identifierEquals("XML")) {
            final SQLStatement stmt = this.parseLoadXml();
            return stmt;
        }
        throw new ParserException("TODO. " + this.lexer.info());
    }
    
    protected MySqlLoadXmlStatement parseLoadXml() {
        this.acceptIdentifier("XML");
        final MySqlLoadXmlStatement stmt = new MySqlLoadXmlStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("CONCURRENT")) {
            stmt.setConcurrent(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("LOCAL")) {
            stmt.setLocal(true);
            this.lexer.nextToken();
        }
        this.acceptIdentifier("INFILE");
        final SQLLiteralExpr fileName = (SQLLiteralExpr)this.exprParser.expr();
        stmt.setFileName(fileName);
        if (this.lexer.token() == Token.REPLACE) {
            stmt.setReplicate(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            stmt.setIgnore(true);
            this.lexer.nextToken();
        }
        this.accept(Token.INTO);
        this.accept(Token.TABLE);
        final SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException("syntax error, illegal charset. " + this.lexer.info());
            }
            final String charset = this.lexer.stringVal();
            this.lexer.nextToken();
            stmt.setCharset(charset);
        }
        if (this.lexer.identifierEquals("ROWS")) {
            this.lexer.nextToken();
            this.acceptIdentifier("IDENTIFIED");
            this.accept(Token.BY);
            final SQLExpr rowsIdentifiedBy = this.exprParser.expr();
            stmt.setRowsIdentifiedBy(rowsIdentifiedBy);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        if (this.lexer.token() == Token.SET) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        return stmt;
    }
    
    protected MySqlLoadDataInFileStatement parseLoadDataInFile() {
        this.acceptIdentifier("DATA");
        final MySqlLoadDataInFileStatement stmt = new MySqlLoadDataInFileStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("CONCURRENT")) {
            stmt.setConcurrent(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("LOCAL")) {
            stmt.setLocal(true);
            this.lexer.nextToken();
        }
        this.acceptIdentifier("INFILE");
        final SQLLiteralExpr fileName = (SQLLiteralExpr)this.exprParser.expr();
        stmt.setFileName(fileName);
        if (this.lexer.token() == Token.REPLACE) {
            stmt.setReplicate(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            stmt.setIgnore(true);
            this.lexer.nextToken();
        }
        this.accept(Token.INTO);
        this.accept(Token.TABLE);
        final SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException("syntax error, illegal charset. " + this.lexer.info());
            }
            final String charset = this.lexer.stringVal();
            this.lexer.nextToken();
            stmt.setCharset(charset);
        }
        if (this.lexer.identifierEquals("FIELDS") || this.lexer.identifierEquals("COLUMNS")) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("TERMINATED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.token() == Token.LITERAL_CHARS) {
                    stmt.setColumnsTerminatedBy(new SQLCharExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                }
                else {
                    final SQLExpr primary = this.exprParser.primary();
                    if (!(primary instanceof SQLHexExpr)) {
                        throw new ParserException("invalid expr for columns terminated : " + primary);
                    }
                    stmt.setColumnsTerminatedBy((SQLLiteralExpr)primary);
                }
            }
            if (this.lexer.identifierEquals("OPTIONALLY")) {
                stmt.setColumnsEnclosedOptionally(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("ENCLOSED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setColumnsEnclosedBy(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("ESCAPED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setColumnsEscaped(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
        }
        if (this.lexer.identifierEquals("LINES")) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("STARTING")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.token() == Token.LITERAL_CHARS) {
                    stmt.setLinesStartingBy(new SQLCharExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                }
                else {
                    final SQLExpr primary = this.exprParser.primary();
                    if (!(primary instanceof SQLHexExpr)) {
                        throw new ParserException("invalid expr for lines starting : " + primary);
                    }
                    stmt.setLinesStartingBy((SQLLiteralExpr)primary);
                }
            }
            if (this.lexer.identifierEquals("TERMINATED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.token() == Token.LITERAL_CHARS) {
                    stmt.setLinesTerminatedBy(new SQLCharExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                }
                else {
                    final SQLExpr primary = this.exprParser.primary();
                    if (!(primary instanceof SQLHexExpr)) {
                        throw new ParserException("invalid expr for lines terminated : " + primary);
                    }
                    stmt.setLinesTerminatedBy((SQLLiteralExpr)primary);
                }
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            this.lexer.nextToken();
            stmt.setIgnoreLinesNumber(this.exprParser.expr());
            this.acceptIdentifier("LINES");
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getSetList(), stmt);
        }
        return stmt;
    }
    
    public MySqlPrepareStatement parsePrepare() {
        this.acceptIdentifier("PREPARE");
        final SQLName name = this.exprParser.name();
        this.accept(Token.FROM);
        final SQLExpr from = this.exprParser.expr();
        return new MySqlPrepareStatement(name, from);
    }
    
    public MySqlExecuteStatement parseExecute() {
        final MySqlExecuteStatement stmt = new MySqlExecuteStatement();
        final SQLName statementName = this.exprParser.name();
        stmt.setStatementName(statementName);
        if (this.lexer.identifierEquals("USING")) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getParameters(), stmt);
        }
        else if (this.lexer.token() == Token.IDENTIFIER) {
            this.exprParser.exprList(stmt.getParameters(), stmt);
        }
        return stmt;
    }
    
    public MySqlExecuteForAdsStatement parseExecuteForAds() {
        final MySqlExecuteForAdsStatement stmt = new MySqlExecuteForAdsStatement();
        stmt.setAction(this.exprParser.name());
        stmt.setRole(this.exprParser.name());
        stmt.setTargetId(this.exprParser.charExpr());
        if (this.lexer.token() == Token.IDENTIFIER) {
            stmt.setStatus(this.exprParser.name());
        }
        return stmt;
    }
    
    public MysqlDeallocatePrepareStatement parseDeallocatePrepare() {
        this.acceptIdentifier("DEALLOCATE");
        this.acceptIdentifier("PREPARE");
        final MysqlDeallocatePrepareStatement stmt = new MysqlDeallocatePrepareStatement();
        final SQLName statementName = this.exprParser.name();
        stmt.setStatementName(statementName);
        return stmt;
    }
    
    @Override
    public SQLInsertStatement parseInsert() {
        final MySqlInsertStatement stmt = new MySqlInsertStatement();
        SQLName tableName = null;
        if (this.lexer.token() == Token.INSERT) {
            this.lexer.nextToken();
            while (this.lexer.token() == Token.IDENTIFIER) {
                final long hash = this.lexer.hash_lower();
                if (hash == FnvHash.Constants.LOW_PRIORITY) {
                    stmt.setLowPriority(true);
                    this.lexer.nextToken();
                }
                else if (hash == FnvHash.Constants.DELAYED) {
                    stmt.setDelayed(true);
                    this.lexer.nextToken();
                }
                else if (hash == FnvHash.Constants.HIGH_PRIORITY) {
                    stmt.setHighPriority(true);
                    this.lexer.nextToken();
                }
                else if (hash == FnvHash.Constants.IGNORE) {
                    stmt.setIgnore(true);
                    this.lexer.nextToken();
                }
                else {
                    if (hash != FnvHash.Constants.ROLLBACK_ON_FAIL) {
                        break;
                    }
                    stmt.setRollbackOnFail(true);
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token() == Token.HINT) {
                final List<SQLCommentHint> hints = this.exprParser.parseHints();
                stmt.setHints(hints);
            }
            if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TABLE) {
                    this.lexer.nextToken();
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.OVERWRITE)) {
                this.lexer.nextToken();
                stmt.setOverwrite(true);
                if (this.lexer.token() == Token.TABLE) {
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.INTO) {
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token() == Token.LINE_COMMENT) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.FULLTEXT) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.DICTIONARY)) {
                    this.lexer.nextToken();
                    stmt.setFulltextDictionary(true);
                }
            }
            tableName = this.exprParser.name();
            stmt.setTableName(tableName);
            if (this.lexer.token() == Token.HINT) {
                final String comment = "/*" + this.lexer.stringVal() + "*/";
                this.lexer.nextToken();
                stmt.getTableSource().addAfterComment(comment);
            }
            if (this.lexer.token() == Token.IDENTIFIER && !this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
                stmt.setAlias(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.WITH) {
                final SQLSelectStatement withStmt = (SQLSelectStatement)this.parseWith();
                stmt.setQuery(withStmt.getSelect());
            }
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLAssignItem ptExpr = new SQLAssignItem();
                    ptExpr.setTarget(this.exprParser.name());
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                        final SQLExpr ptValue = this.exprParser.expr();
                        ptExpr.setValue(ptValue);
                    }
                    stmt.addPartition(ptExpr);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                if (this.lexer.token() == Token.IF) {
                    this.lexer.nextToken();
                    this.accept(Token.NOT);
                    this.accept(Token.EXISTS);
                    stmt.setIfNotExists(true);
                }
            }
        }
        int columnSize = 0;
        List<SQLColumnDefinition> columnDefinitionList = null;
        if (this.lexer.token() == Token.LPAREN) {
            final boolean useInsertColumnsCache = this.lexer.isEnabled(SQLParserFeature.UseInsertColumnsCache);
            InsertColumnsCache insertColumnsCache = null;
            long tableNameHash = 0L;
            InsertColumnsCache.Entry cachedColumns = null;
            if (useInsertColumnsCache) {
                insertColumnsCache = this.insertColumnsCache;
                if (insertColumnsCache == null) {
                    insertColumnsCache = InsertColumnsCache.global;
                }
                if (tableName != null) {
                    tableNameHash = tableName.nameHashCode64();
                    cachedColumns = insertColumnsCache.get(tableNameHash);
                }
            }
            SchemaObject tableObject = null;
            final int pos = this.lexer.pos();
            if (cachedColumns != null && this.lexer.text.startsWith(cachedColumns.columnsString, pos)) {
                if (!this.lexer.isEnabled(SQLParserFeature.OptimizedForParameterized)) {
                    final List<SQLExpr> columns = stmt.getColumns();
                    final List<SQLExpr> cachedColumns2 = cachedColumns.columns;
                    for (int i = 0, size = cachedColumns2.size(); i < size; ++i) {
                        columns.add(cachedColumns2.get(i).clone());
                    }
                }
                stmt.setColumnsString(cachedColumns.columnsFormattedString, cachedColumns.columnsFormattedStringHash);
                final int p2 = pos + cachedColumns.columnsString.length();
                this.lexer.reset(p2);
                this.lexer.nextToken();
            }
            else {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token() == Token.SELECT) {
                    this.lexer.reset(mark);
                    final SQLSelect select = this.exprParser.createSelectParser().select();
                    select.setParent(stmt);
                    stmt.setQuery(select);
                }
                else {
                    if (this.repository != null && this.lexer.isEnabled(SQLParserFeature.InsertValueCheckType)) {
                        tableObject = this.repository.findTable(tableName.nameHashCode64());
                    }
                    if (tableObject != null) {
                        columnDefinitionList = new ArrayList<SQLColumnDefinition>();
                    }
                    final List<SQLExpr> columns2 = stmt.getColumns();
                    if (this.lexer.token() != Token.RPAREN) {
                        while (true) {
                            final Token token = this.lexer.token();
                            String identName;
                            long hash2;
                            if (token == Token.IDENTIFIER) {
                                identName = this.lexer.stringVal();
                                hash2 = this.lexer.hash_lower();
                            }
                            else if (token == Token.LITERAL_CHARS) {
                                if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                                    identName = this.lexer.stringVal();
                                }
                                else {
                                    identName = '\'' + this.lexer.stringVal() + '\'';
                                }
                                hash2 = 0L;
                            }
                            else if (token == Token.LITERAL_ALIAS) {
                                identName = this.lexer.stringVal();
                                if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                                    identName = SQLUtils.normalize(identName, this.dbType);
                                }
                                hash2 = 0L;
                            }
                            else {
                                identName = this.lexer.stringVal();
                                hash2 = 0L;
                            }
                            this.lexer.nextTokenComma();
                            SQLExpr expr = new SQLIdentifierExpr(identName, hash2);
                            while (this.lexer.token() == Token.DOT) {
                                this.lexer.nextToken();
                                final String propertyName = this.lexer.stringVal();
                                this.lexer.nextToken();
                                expr = new SQLPropertyExpr(expr, propertyName);
                            }
                            expr.setParent(stmt);
                            columns2.add(expr);
                            ++columnSize;
                            if (tableObject != null) {
                                final SQLColumnDefinition columnDefinition = tableObject.findColumn(hash2);
                                columnDefinitionList.add(columnDefinition);
                            }
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextTokenIdent();
                        }
                        columnSize = stmt.getColumns().size();
                        if (insertColumnsCache != null && tableName != null) {
                            final String columnsString = this.lexer.subString(pos, this.lexer.pos() - pos);
                            final List<SQLExpr> clonedColumns = new ArrayList<SQLExpr>(columnSize);
                            for (int j = 0; j < columns2.size(); ++j) {
                                clonedColumns.add(columns2.get(j).clone());
                            }
                            final StringBuilder buf = new StringBuilder();
                            final SQLASTOutputVisitor outputVisitor = SQLUtils.createOutputVisitor(buf, this.dbType);
                            outputVisitor.printInsertColumns(columns2);
                            final String formattedColumnsString = buf.toString();
                            final long columnsFormattedStringHash = FnvHash.fnv1a_64_lower(formattedColumnsString);
                            insertColumnsCache.put(tableName.hashCode64(), columnsString, formattedColumnsString, clonedColumns);
                            stmt.setColumnsString(formattedColumnsString, columnsFormattedStringHash);
                        }
                    }
                    this.accept(Token.RPAREN);
                }
            }
        }
        List<SQLCommentHint> commentHints = null;
        if (this.lexer.token() == Token.HINT) {
            commentHints = this.exprParser.parseHints();
        }
        else if (this.lexer.token() == Token.LINE_COMMENT) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.VALUES || this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
            this.lexer.nextTokenLParen();
            if (this.lexer.isEnabled(SQLParserFeature.InsertReader)) {
                return stmt;
            }
            if (this.lexer.isEnabled(SQLParserFeature.InsertValueNative)) {
                this.parseValueClauseNative(stmt.getValuesList(), columnDefinitionList, columnSize, stmt);
            }
            else {
                this.parseValueClause(stmt.getValuesList(), columnDefinitionList, columnSize, stmt);
            }
        }
        else if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
            stmt.addValueCause(values);
            while (true) {
                final SQLName name = this.exprParser.name();
                stmt.addColumn(name);
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.COLONEQ);
                }
                values.addValue(this.exprParser.expr());
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token() == Token.SELECT) {
            final SQLSelect select2 = this.exprParser.createSelectParser().select();
            if (commentHints != null && !commentHints.isEmpty()) {
                select2.setHeadHint(commentHints.get(0));
            }
            select2.setParent(stmt);
            stmt.setQuery(select2);
        }
        else if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelect select2 = this.exprParser.createSelectParser().select();
            select2.setParent(stmt);
            stmt.setQuery(select2);
            this.accept(Token.RPAREN);
        }
        else if (this.lexer.token() == Token.WITH) {
            final SQLSelect query = this.exprParser.createSelectParser().select();
            stmt.setQuery(query);
        }
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            this.acceptIdentifier("DUPLICATE");
            this.accept(Token.KEY);
            this.accept(Token.UPDATE);
            final List<SQLExpr> duplicateKeyUpdate = stmt.getDuplicateKeyUpdate();
            while (true) {
                final SQLName name = this.exprParser.name();
                this.accept(Token.EQ);
                SQLExpr value;
                try {
                    value = this.exprParser.expr();
                }
                catch (EOFParserException e) {
                    throw new ParserException("EOF, " + name + "=", e);
                }
                final SQLBinaryOpExpr assignment = new SQLBinaryOpExpr(name, SQLBinaryOperator.Equality, value);
                assignment.setParent(stmt);
                duplicateKeyUpdate.add(assignment);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextTokenIdent();
            }
        }
        return stmt;
    }
    
    @Override
    public MySqlSelectParser createSQLSelectParser() {
        return new MySqlSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLStatement parseSet() {
        this.accept(Token.SET);
        if (this.lexer.identifierEquals(FnvHash.Constants.PASSWORD)) {
            this.lexer.nextToken();
            final SQLSetStatement stmt = new SQLSetStatement();
            stmt.setDbType(this.dbType);
            stmt.setOption(SQLSetStatement.Option.PASSWORD);
            SQLExpr user = null;
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                user = this.exprParser.name();
            }
            this.accept(Token.EQ);
            final SQLExpr password = this.exprParser.expr();
            stmt.set(user, password);
            return stmt;
        }
        Boolean global = null;
        Boolean session = null;
        boolean local = false;
        if (this.lexer.identifierEquals("GLOBAL")) {
            global = Boolean.TRUE;
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SESSION)) {
            session = Boolean.TRUE;
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
            this.lexer.nextToken();
            local = true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TRANSACTION)) {
            final MySqlSetTransactionStatement stmt2 = new MySqlSetTransactionStatement();
            stmt2.setGlobal(global);
            stmt2.setSession(session);
            if (local) {
                stmt2.setLocal(true);
            }
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("ISOLATION")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LEVEL");
                if (this.lexer.identifierEquals("READ")) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("UNCOMMITTED")) {
                        stmt2.setIsolationLevel("READ UNCOMMITTED");
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.identifierEquals("WRITE")) {
                        stmt2.setIsolationLevel("READ WRITE");
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.identifierEquals("ONLY")) {
                        stmt2.setIsolationLevel("READ ONLY");
                        this.lexer.nextToken();
                    }
                    else {
                        if (!this.lexer.identifierEquals("COMMITTED")) {
                            throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal() + ", " + this.lexer.info());
                        }
                        stmt2.setIsolationLevel("READ COMMITTED");
                        this.lexer.nextToken();
                    }
                }
                else if (this.lexer.identifierEquals("SERIALIZABLE")) {
                    stmt2.setIsolationLevel("SERIALIZABLE");
                    this.lexer.nextToken();
                }
                else {
                    if (!this.lexer.identifierEquals("REPEATABLE")) {
                        throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal() + ", " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    if (!this.lexer.identifierEquals("READ")) {
                        throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal() + ", " + this.lexer.info());
                    }
                    stmt2.setIsolationLevel("REPEATABLE READ");
                    this.lexer.nextToken();
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.POLICY)) {
                this.lexer.nextToken();
                final SQLExpr policy = this.exprParser.primary();
                stmt2.setPolicy(policy);
            }
            else if (this.lexer.identifierEquals("READ")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("ONLY")) {
                    stmt2.setAccessModel("ONLY");
                    this.lexer.nextToken();
                }
                else {
                    if (!this.lexer.identifierEquals("WRITE")) {
                        throw new ParserException("UNKOWN ACCESS MODEL : " + this.lexer.stringVal() + ", " + this.lexer.info());
                    }
                    stmt2.setAccessModel("WRITE");
                    this.lexer.nextToken();
                }
            }
            return stmt2;
        }
        final SQLSetStatement stmt3 = new SQLSetStatement(this.getDbType());
        this.parseAssignItems(stmt3.getItems(), stmt3, true);
        if (global != null) {
            final SQLVariantRefExpr varRef = (SQLVariantRefExpr)stmt3.getItems().get(0).getTarget();
            varRef.setGlobal(true);
        }
        if (session != null) {
            final SQLVariantRefExpr varRef = (SQLVariantRefExpr)stmt3.getItems().get(0).getTarget();
            varRef.setSession(true);
        }
        if (this.lexer.token() == Token.HINT) {
            stmt3.setHints(this.exprParser.parseHints());
        }
        return stmt3;
    }
    
    @Override
    public SQLStatement parseAlter() {
        List<String> comments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            comments = this.lexer.readAndResetComments();
        }
        final Lexer.SavePoint mark = this.lexer.mark();
        this.accept(Token.ALTER);
        if (this.lexer.token() == Token.USER) {
            return this.parseAlterUser();
        }
        boolean online = false;
        boolean offline = false;
        if (this.lexer.identifierEquals("ONLINE")) {
            online = true;
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("OFFLINE")) {
            offline = true;
            this.lexer.nextToken();
        }
        boolean ignore = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            ignore = true;
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.TABLE) {
            final SQLStatement alterTable = this.parseAlterTable(ignore, online, offline);
            if (comments != null) {
                alterTable.addBeforeComment(comments);
            }
            return alterTable;
        }
        if (this.lexer.token() == Token.DATABASE || this.lexer.token() == Token.SCHEMA) {
            return this.parseAlterDatabase();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EVENT)) {
            return this.parseAlterEvent();
        }
        if (this.lexer.token() == Token.FUNCTION) {
            return this.parseAlterFunction();
        }
        if (this.lexer.token() == Token.PROCEDURE) {
            return this.parseAlterProcedure();
        }
        if (this.lexer.token() == Token.TABLESPACE) {
            return this.parseAlterTableSpace();
        }
        if (this.lexer.token() == Token.VIEW) {
            return this.parseAlterView();
        }
        if (this.lexer.token() == Token.SEQUENCE) {
            this.lexer.reset(mark);
            return this.parseAlterSequence();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOGFILE)) {
            return this.parseAlterLogFileGroup();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SERVER)) {
            return this.parseAlterServer();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
            return this.parseAlterView();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.OUTLINE)) {
            this.lexer.reset(mark);
            return this.parseAlterOutline();
        }
        if (this.lexer.token() == Token.FULLTEXT) {
            this.lexer.reset(mark);
            return this.parseAlterFullTextCharFilter();
        }
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.reset(mark);
            this.accept(Token.ALTER);
            this.accept(Token.INDEX);
            final SQLAlterIndexStatement stmt = new SQLAlterIndexStatement();
            stmt.setName(this.exprParser.name());
            this.accept(Token.SET);
            this.accept(Token.FULLTEXT);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            final Lexer.SavePoint savePoint = this.lexer.mark();
            this.lexer.nextToken();
            this.accept(Token.EQ);
            this.getExprParser().userName();
            if (this.lexer.identifierEquals(FnvHash.Constants.EVENT)) {
                this.lexer.reset(savePoint);
                return this.parseAlterEvent();
            }
            this.lexer.reset(savePoint);
            return this.parseAlterView();
        }
        else {
            if (this.lexer.identifierEquals("TABLEGROUP")) {
                this.lexer.reset(mark);
                return this.parseAlterTableGroup();
            }
            if (this.lexer.identifierEquals("SYSTEM")) {
                this.lexer.reset(mark);
                return this.parseAlterSystem();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                this.lexer.reset(mark);
                return this.parseAlterResourceGroup();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                this.lexer.reset(mark);
                return this.parseAlterMaterialized();
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
    }
    
    private SQLStatement parseAddManageInstanceGroup() {
        this.lexer.nextToken();
        final MySqlManageInstanceGroupStatement stmt = new MySqlManageInstanceGroupStatement();
        stmt.setOperation(new SQLIdentifierExpr("ADD"));
        this.acceptIdentifier("INSTANCE_GROUP");
        while (true) {
            stmt.getGroupNames().add(this.exprParser.expr());
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.acceptIdentifier("REPLICATION");
        this.accept(Token.EQ);
        stmt.setReplication(this.exprParser.integerExpr());
        return stmt;
    }
    
    private SQLStatement parseAlterFullTextCharFilter() {
        this.accept(Token.ALTER);
        this.accept(Token.FULLTEXT);
        final MysqlAlterFullTextStatement stmt = new MysqlAlterFullTextStatement();
        stmt.setType(this.parseFullTextType());
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.SET);
        final SQLAssignItem item = this.exprParser.parseAssignItem();
        stmt.setItem(item);
        return stmt;
    }
    
    protected SQLStatement parseAlterTableGroup() {
        this.accept(Token.ALTER);
        this.acceptIdentifier("TABLEGROUP");
        final SQLName name = this.exprParser.name();
        final SQLAlterTableGroupStatement stmt = new SQLAlterTableGroupStatement();
        stmt.setName(name);
        do {
            final SQLName key = this.exprParser.name();
            this.accept(Token.EQ);
            final SQLExpr value = this.exprParser.expr();
            stmt.getOptions().add(new SQLAssignItem(key, value));
        } while (this.lexer.token() != Token.EOF);
        return stmt;
    }
    
    protected SQLStatement parseAlterSystem() {
        this.accept(Token.ALTER);
        this.acceptIdentifier("SYSTEM");
        if (this.lexer.token() == Token.SET) {
            this.accept(Token.SET);
            this.acceptIdentifier("CONFIG");
            final SQLAlterSystemSetConfigStatement stmt = new SQLAlterSystemSetConfigStatement();
            do {
                final SQLName key = this.exprParser.name();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.expr();
                stmt.getOptions().add(new SQLAssignItem(key, value));
            } while (this.lexer.token() != Token.EOF);
            return stmt;
        }
        if (this.lexer.identifierEquals("GET")) {
            this.acceptIdentifier("GET");
            this.acceptIdentifier("CONFIG");
            final SQLName name = this.exprParser.name();
            final SQLAlterSystemGetConfigStatement stmt2 = new SQLAlterSystemGetConfigStatement();
            stmt2.setName(name);
            return stmt2;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLStatement parseAlterOutline() {
        this.accept(Token.ALTER);
        if (this.lexer.identifierEquals(FnvHash.Constants.OUTLINE)) {
            this.lexer.nextToken();
            final SQLAlterOutlineStatement stmt = new SQLAlterOutlineStatement();
            stmt.setDbType(this.dbType);
            stmt.setName(this.exprParser.name());
            if (this.lexer.identifierEquals("RESYNC")) {
                this.lexer.nextToken();
                stmt.setResync(true);
            }
            return stmt;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLStatement parseAlterView() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        final SQLAlterViewStatement createView = new SQLAlterViewStatement(this.getDbType());
        if (this.lexer.identifierEquals("ALGORITHM")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final String algorithm = this.lexer.stringVal();
            createView.setAlgorithm(algorithm);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("DEFINER")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = (SQLName)this.exprParser.expr();
            createView.setDefiner(definer);
        }
        if (this.lexer.identifierEquals("SQL")) {
            this.lexer.nextToken();
            this.acceptIdentifier("SECURITY");
            final String sqlSecurity = this.lexer.stringVal();
            createView.setSqlSecurity(sqlSecurity);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            createView.setForce(true);
        }
        this.accept(Token.VIEW);
        if (this.lexer.token() == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            createView.setIfNotExists(true);
        }
        createView.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token() == Token.CONSTRAINT) {
                    final SQLTableConstraint constraint = (SQLTableConstraint)this.exprParser.parseConstaint();
                    createView.addColumn(constraint);
                }
                else {
                    final SQLColumnDefinition column = new SQLColumnDefinition();
                    column.setDbType(this.dbType);
                    final SQLName expr = this.exprParser.name();
                    column.setName(expr);
                    this.exprParser.parseColumnRest(column);
                    if (this.lexer.token() == Token.COMMENT) {
                        this.lexer.nextToken();
                        SQLExpr comment;
                        if (this.lexer.token() == Token.LITERAL_ALIAS) {
                            String alias = this.lexer.stringVal();
                            if (alias.length() > 2 && alias.charAt(0) == '\"' && alias.charAt(alias.length() - 1) == '\"') {
                                alias = alias.substring(1, alias.length() - 1);
                            }
                            comment = new SQLCharExpr(alias);
                            this.lexer.nextToken();
                        }
                        else {
                            comment = this.exprParser.primary();
                        }
                        column.setComment(comment);
                    }
                    column.setParent(createView);
                    createView.addColumn(column);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLCharExpr comment2 = (SQLCharExpr)this.exprParser.primary();
            createView.setComment(comment2);
        }
        this.accept(Token.AS);
        final SQLSelectParser selectParser = this.createSQLSelectParser();
        createView.setSubQuery(selectParser.select());
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("CASCADED")) {
                createView.setWithCascaded(true);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("LOCAL")) {
                createView.setWithLocal(true);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("READ")) {
                this.lexer.nextToken();
                this.accept(Token.ONLY);
                createView.setWithReadOnly(true);
            }
            if (this.lexer.token() == Token.CHECK) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTION");
                createView.setWithCheckOption(true);
            }
        }
        return createView;
    }
    
    protected SQLStatement parseAlterTableSpace() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.accept(Token.TABLESPACE);
        final SQLName name = this.exprParser.name();
        final MySqlAlterTablespaceStatement stmt = new MySqlAlterTablespaceStatement();
        stmt.setName(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
            this.lexer.nextToken();
            this.acceptIdentifier("DATAFILE");
            final SQLExpr file = this.exprParser.primary();
            stmt.setAddDataFile(file);
        }
        else if (this.lexer.token() == Token.DROP) {
            this.lexer.nextToken();
            this.acceptIdentifier("DATAFILE");
            final SQLExpr file = this.exprParser.primary();
            stmt.setDropDataFile(file);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.INITIAL_SIZE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr initialSize = this.exprParser.expr();
            stmt.setInitialSize(initialSize);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WAIT)) {
            this.lexer.nextToken();
            stmt.setWait(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr engine = this.exprParser.expr();
            stmt.setEngine(engine);
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterServer() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("SERVER");
        final SQLName name = this.exprParser.name();
        final MySqlAlterServerStatement stmt = new MySqlAlterServerStatement();
        stmt.setName(name);
        this.acceptIdentifier("OPTIONS");
        this.accept(Token.LPAREN);
        if (this.lexer.token() == Token.USER) {
            this.lexer.nextToken();
            final SQLExpr user = this.exprParser.name();
            stmt.setUser(user);
        }
        this.accept(Token.RPAREN);
        return stmt;
    }
    
    protected SQLStatement parseCreateLogFileGroup() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("LOGFILE");
        this.accept(Token.GROUP);
        final SQLName name = this.exprParser.name();
        final MySqlCreateAddLogFileGroupStatement stmt = new MySqlCreateAddLogFileGroupStatement();
        stmt.setName(name);
        this.acceptIdentifier("ADD");
        this.acceptIdentifier("UNDOFILE");
        final SQLExpr fileName = this.exprParser.primary();
        stmt.setAddUndoFile(fileName);
        if (this.lexer.identifierEquals(FnvHash.Constants.INITIAL_SIZE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr initialSize = this.exprParser.expr();
            stmt.setInitialSize(initialSize);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WAIT)) {
            this.lexer.nextToken();
            stmt.setWait(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr engine = this.exprParser.expr();
            stmt.setEngine(engine);
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterLogFileGroup() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("LOGFILE");
        this.accept(Token.GROUP);
        final SQLName name = this.exprParser.name();
        final MySqlAlterLogFileGroupStatement stmt = new MySqlAlterLogFileGroupStatement();
        stmt.setName(name);
        this.acceptIdentifier("ADD");
        this.acceptIdentifier("UNDOFILE");
        final SQLExpr fileName = this.exprParser.primary();
        stmt.setAddUndoFile(fileName);
        if (this.lexer.identifierEquals(FnvHash.Constants.INITIAL_SIZE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr initialSize = this.exprParser.expr();
            stmt.setInitialSize(initialSize);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WAIT)) {
            this.lexer.nextToken();
            stmt.setWait(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr engine = this.exprParser.expr();
            stmt.setEngine(engine);
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterProcedure() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.accept(Token.PROCEDURE);
        final SQLAlterProcedureStatement stmt = new SQLAlterProcedureStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        while (true) {
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                final SQLExpr comment = this.exprParser.primary();
                stmt.setComment(comment);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.LANGUAGE)) {
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setLanguageSql(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
                this.lexer.nextToken();
                this.acceptIdentifier("SECURITY");
                final SQLExpr sqlSecurity = this.exprParser.name();
                stmt.setSqlSecurity(sqlSecurity);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.CONTAINS) && this.lexer.token() != Token.CONTAINS) {
                    break;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setContainsSql(true);
            }
        }
        return stmt;
    }
    
    @Override
    protected SQLStatement parseAlterFunction() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.accept(Token.FUNCTION);
        final SQLAlterFunctionStatement stmt = new SQLAlterFunctionStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        while (true) {
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                final SQLExpr comment = this.exprParser.primary();
                stmt.setComment(comment);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.LANGUAGE)) {
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setLanguageSql(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
                this.lexer.nextToken();
                this.acceptIdentifier("SECURITY");
                final SQLExpr sqlSecurity = this.exprParser.name();
                stmt.setSqlSecurity(sqlSecurity);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.CONTAINS) && this.lexer.token() != Token.CONTAINS) {
                    break;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setContainsSql(true);
            }
        }
        return stmt;
    }
    
    protected SQLStatement parseCreateEvent() {
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
        }
        final MySqlCreateEventStatement stmt = new MySqlCreateEventStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = this.getExprParser().userName();
            stmt.setDefiner(definer);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.accept(Token.RPAREN);
            }
        }
        this.acceptIdentifier("EVENT");
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        final SQLName eventName = this.exprParser.name();
        stmt.setName(eventName);
        while (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.SCHEDULE)) {
                this.lexer.nextToken();
                final MySqlEventSchedule schedule = this.parseSchedule();
                stmt.setSchedule(schedule);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.COMPLETION)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                boolean value;
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    value = false;
                }
                else {
                    value = true;
                }
                this.acceptIdentifier("PRESERVE");
                stmt.setOnCompletionPreserve(value);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            final SQLName renameTo = this.exprParser.name();
            stmt.setRenameTo(renameTo);
        }
        if (this.lexer.token() == Token.ENABLE) {
            stmt.setEnable(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.DISABLE) {
            this.lexer.nextToken();
            stmt.setEnable(false);
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                this.acceptIdentifier("SLAVE");
                stmt.setDisableOnSlave(true);
            }
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLExpr comment = this.exprParser.primary();
            stmt.setComment(comment);
        }
        if (this.lexer.token() == Token.DO) {
            this.lexer.nextToken();
            final SQLStatement eventBody = this.parseStatement();
            stmt.setEventBody(eventBody);
        }
        else if (this.lexer.token() == Token.IDENTIFIER) {
            final SQLExpr expr = this.exprParser.expr();
            final SQLExprStatement eventBody2 = new SQLExprStatement(expr);
            eventBody2.setDbType(this.dbType);
            stmt.setEventBody(eventBody2);
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterEvent() {
        if (this.lexer.token() == Token.ALTER) {
            this.lexer.nextToken();
        }
        final MySqlAlterEventStatement stmt = new MySqlAlterEventStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = this.getExprParser().userName();
            stmt.setDefiner(definer);
        }
        this.acceptIdentifier("EVENT");
        final SQLName eventName = this.exprParser.name();
        stmt.setName(eventName);
        while (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.SCHEDULE)) {
                this.lexer.nextToken();
                final MySqlEventSchedule schedule = this.parseSchedule();
                stmt.setSchedule(schedule);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.COMPLETION)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                boolean value;
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    value = false;
                }
                else {
                    value = true;
                }
                this.acceptIdentifier("PRESERVE");
                stmt.setOnCompletionPreserve(value);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            final SQLName renameTo = this.exprParser.name();
            stmt.setRenameTo(renameTo);
        }
        if (this.lexer.token() == Token.ENABLE) {
            stmt.setEnable(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.DISABLE) {
            this.lexer.nextToken();
            stmt.setEnable(false);
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                this.acceptIdentifier("SLAVE");
                stmt.setDisableOnSlave(true);
            }
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLExpr comment = this.exprParser.primary();
            stmt.setComment(comment);
        }
        if (this.lexer.token() == Token.DO) {
            this.lexer.nextToken();
            final SQLStatement eventBody = this.parseStatement();
            stmt.setEventBody(eventBody);
        }
        else if (this.lexer.token() == Token.IDENTIFIER) {
            final SQLExpr expr = this.exprParser.expr();
            final SQLExprStatement eventBody2 = new SQLExprStatement(expr);
            eventBody2.setDbType(this.dbType);
            stmt.setEventBody(eventBody2);
        }
        return stmt;
    }
    
    private MySqlEventSchedule parseSchedule() {
        final MySqlEventSchedule schedule = new MySqlEventSchedule();
        if (this.lexer.identifierEquals(FnvHash.Constants.AT)) {
            this.lexer.nextToken();
            schedule.setAt(this.exprParser.expr());
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.EVERY)) {
            this.lexer.nextToken();
            final SQLExpr value = this.exprParser.expr();
            final String unit = this.lexer.stringVal();
            this.lexer.nextToken();
            final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
            intervalExpr.setValue(value);
            intervalExpr.setUnit(SQLIntervalUnit.valueOf(unit.toUpperCase()));
            schedule.setEvery(intervalExpr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STARTS)) {
            this.lexer.nextToken();
            schedule.setStarts(this.exprParser.expr());
            if (this.lexer.identifierEquals(FnvHash.Constants.ENDS)) {
                this.lexer.nextToken();
                schedule.setEnds(this.exprParser.expr());
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.ENDS)) {
            this.lexer.nextToken();
            schedule.setEnds(this.exprParser.expr());
        }
        return schedule;
    }
    
    private boolean parseAlterSpecification(final SQLAlterTableStatement stmt) {
        Label_4915: {
            switch (this.lexer.token()) {
                case IDENTIFIER: {
                    if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
                        this.lexer.nextToken();
                        boolean hasConstraint = false;
                        SQLName constraintSymbol = null;
                        Label_0696: {
                            switch (this.lexer.token()) {
                                case COLUMN: {
                                    this.lexer.nextToken();
                                }
                                case LPAREN: {
                                    this.parseAlterTableAddColumn(stmt);
                                    return true;
                                }
                                case FULLTEXT:
                                case IDENTIFIER: {
                                    if (this.lexer.token() == Token.FULLTEXT || this.lexer.identifierEquals(FnvHash.Constants.SPATIAL) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERING) || this.lexer.identifierEquals(FnvHash.Constants.ANN) || this.lexer.identifierEquals(FnvHash.Constants.GLOBAL) || this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
                                        final SQLAlterTableAddIndex item = new SQLAlterTableAddIndex();
                                        this.exprParser.parseIndex(item.getIndexDefinition());
                                        stmt.addItem(item);
                                    }
                                    else if (this.lexer.identifierEquals(FnvHash.Constants.EXTPARTITION)) {
                                        this.lexer.nextToken();
                                        this.accept(Token.LPAREN);
                                        final SQLAlterTableAddExtPartition extPartitionItem = new SQLAlterTableAddExtPartition();
                                        final MySqlExtPartition partitionDef = this.parseExtPartition();
                                        extPartitionItem.setExPartition(partitionDef);
                                        stmt.addItem(extPartitionItem);
                                        this.accept(Token.RPAREN);
                                    }
                                    else {
                                        this.parseAlterTableAddColumn(stmt);
                                    }
                                    return true;
                                }
                                case INDEX:
                                case KEY: {
                                    final SQLAlterTableAddIndex item = new SQLAlterTableAddIndex();
                                    this.exprParser.parseIndex(item.getIndexDefinition());
                                    stmt.addItem(item);
                                    return true;
                                }
                                case CONSTRAINT: {
                                    hasConstraint = true;
                                    this.lexer.nextToken();
                                    if (this.lexer.token() != Token.IDENTIFIER) {
                                        break Label_0696;
                                    }
                                    constraintSymbol = this.exprParser.name();
                                    if (this.lexer.token() != Token.PRIMARY && this.lexer.token() != Token.UNIQUE && this.lexer.token() != Token.FOREIGN && this.lexer.token() != Token.CHECK) {
                                        throw new ParserException("syntax error, expect PRIMARY, UNIQUE or FOREIGN, actual " + this.lexer.token() + ", " + this.lexer.info());
                                    }
                                    break Label_0696;
                                }
                                case CHECK:
                                case PRIMARY:
                                case UNIQUE:
                                case FOREIGN: {
                                    if (this.lexer.token() == Token.FOREIGN) {
                                        final MysqlForeignKey fk = this.getExprParser().parseForeignKey();
                                        if (constraintSymbol != null) {
                                            fk.setName(constraintSymbol);
                                        }
                                        fk.setHasConstraint(hasConstraint);
                                        final SQLAlterTableAddConstraint constraint = new SQLAlterTableAddConstraint(fk);
                                        stmt.addItem(constraint);
                                    }
                                    else if (this.lexer.token() == Token.PRIMARY) {
                                        final MySqlPrimaryKey pk = new MySqlPrimaryKey();
                                        if (constraintSymbol != null) {
                                            pk.setName(constraintSymbol);
                                        }
                                        pk.getIndexDefinition().setHasConstraint(hasConstraint);
                                        pk.getIndexDefinition().setSymbol(constraintSymbol);
                                        this.exprParser.parseIndex(pk.getIndexDefinition());
                                        final SQLAlterTableAddConstraint item2 = new SQLAlterTableAddConstraint(pk);
                                        stmt.addItem(item2);
                                    }
                                    else if (this.lexer.token() == Token.UNIQUE) {
                                        final MySqlUnique uk = new MySqlUnique();
                                        uk.getIndexDefinition().setHasConstraint(hasConstraint);
                                        uk.getIndexDefinition().setSymbol(constraintSymbol);
                                        this.exprParser.parseIndex(uk.getIndexDefinition());
                                        final SQLAlterTableAddConstraint item2 = new SQLAlterTableAddConstraint(uk);
                                        stmt.addItem(item2);
                                    }
                                    else if (this.lexer.token() == Token.CHECK) {
                                        this.lexer.nextToken();
                                        this.accept(Token.LPAREN);
                                        final SQLCheck check = new SQLCheck();
                                        if (null != constraintSymbol) {
                                            check.setName(constraintSymbol);
                                        }
                                        check.setExpr(this.exprParser.expr());
                                        this.accept(Token.RPAREN);
                                        boolean enforce = true;
                                        if (this.lexer.token() == Token.NOT) {
                                            enforce = false;
                                            this.lexer.nextToken();
                                        }
                                        if (this.lexer.stringVal().equalsIgnoreCase("ENFORCED")) {
                                            check.setEnforced(enforce);
                                            this.lexer.nextToken();
                                        }
                                        final SQLAlterTableAddConstraint item3 = new SQLAlterTableAddConstraint(check);
                                        stmt.addItem(item3);
                                    }
                                    return true;
                                }
                                case PARTITION: {
                                    this.lexer.nextToken();
                                    final SQLAlterTableAddPartition item4 = new SQLAlterTableAddPartition();
                                    if (this.lexer.identifierEquals("PARTITIONS")) {
                                        this.lexer.nextToken();
                                        item4.setPartitionCount(this.exprParser.integerExpr());
                                    }
                                    if (this.lexer.token() == Token.LPAREN) {
                                        this.lexer.nextToken();
                                        final SQLPartition partition = this.getExprParser().parsePartition();
                                        this.accept(Token.RPAREN);
                                        item4.addPartition(partition);
                                    }
                                    stmt.addItem(item4);
                                    return true;
                                }
                                default: {
                                    this.parseAlterTableAddColumn(stmt);
                                    return true;
                                }
                            }
                        }
                    }
                    else {
                        if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.EQ) {
                                this.lexer.nextToken();
                            }
                            stmt.addItem(new MySqlAlterTableOption("ALGORITHM", this.lexer.stringVal()));
                            this.lexer.nextToken();
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.CHANGE)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.COLUMN) {
                                this.lexer.nextToken();
                            }
                            final MySqlAlterTableChangeColumn item5 = new MySqlAlterTableChangeColumn();
                            item5.setColumnName(this.exprParser.name());
                            item5.setNewColumnDefinition(this.exprParser.parseColumn());
                            if (this.lexer.identifierEquals("AFTER")) {
                                this.lexer.nextToken();
                                item5.setAfterColumn(this.exprParser.name());
                            }
                            else if (this.lexer.identifierEquals("FIRST")) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.IDENTIFIER) {
                                    item5.setFirstColumn(this.exprParser.name());
                                }
                                else {
                                    item5.setFirst(true);
                                }
                            }
                            stmt.addItem(item5);
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.CONVERT)) {
                            this.lexer.nextToken();
                            this.accept(Token.TO);
                            this.acceptIdentifier("CHARACTER");
                            this.accept(Token.SET);
                            final SQLAlterTableConvertCharSet item6 = new SQLAlterTableConvertCharSet();
                            final SQLExpr charset = this.exprParser.name();
                            item6.setCharset(charset);
                            if (this.lexer.identifierEquals("COLLATE")) {
                                this.lexer.nextToken();
                                final SQLExpr collate = this.exprParser.primary();
                                item6.setCollate(collate);
                            }
                            stmt.addItem(item6);
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.DISCARD)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.PARTITION) {
                                this.lexer.nextToken();
                                final SQLAlterTableDiscardPartition item7 = new SQLAlterTableDiscardPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item7.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                }
                                else {
                                    this.exprParser.names(item7.getPartitions(), item7);
                                }
                                if (this.lexer.token() == Token.TABLESPACE) {
                                    this.lexer.nextToken();
                                    item7.setTablespace(true);
                                }
                                stmt.addItem(item7);
                            }
                            else {
                                this.accept(Token.TABLESPACE);
                                final MySqlAlterTableDiscardTablespace item8 = new MySqlAlterTableDiscardTablespace();
                                stmt.addItem(item8);
                            }
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.IMPORT)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.PARTITION) {
                                this.lexer.nextToken();
                                final SQLAlterTableImportPartition item9 = new SQLAlterTableImportPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item9.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                }
                                else {
                                    this.exprParser.names(item9.getPartitions(), item9);
                                }
                                if (this.lexer.token() == Token.TABLESPACE) {
                                    this.lexer.nextToken();
                                    item9.setTablespace(true);
                                }
                                stmt.addItem(item9);
                            }
                            else {
                                this.accept(Token.TABLESPACE);
                                final MySqlAlterTableImportTablespace item10 = new MySqlAlterTableImportTablespace();
                                stmt.addItem(item10);
                            }
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                            this.lexer.nextToken();
                            final MySqlAlterTableForce item11 = new MySqlAlterTableForce();
                            stmt.addItem(item11);
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.MODIFY)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.COLUMN) {
                                this.lexer.nextToken();
                            }
                            boolean paren = false;
                            if (this.lexer.token() == Token.LPAREN) {
                                paren = true;
                                this.lexer.nextToken();
                            }
                            while (true) {
                                final MySqlAlterTableModifyColumn item12 = new MySqlAlterTableModifyColumn();
                                item12.setNewColumnDefinition(this.exprParser.parseColumn());
                                if (this.lexer.identifierEquals("AFTER")) {
                                    this.lexer.nextToken();
                                    item12.setAfterColumn(this.exprParser.name());
                                }
                                else if (this.lexer.identifierEquals("FIRST")) {
                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.IDENTIFIER) {
                                        item12.setFirstColumn(this.exprParser.name());
                                    }
                                    else {
                                        item12.setFirst(true);
                                    }
                                }
                                stmt.addItem(item12);
                                if (!paren || this.lexer.token() != Token.COMMA) {
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            if (paren) {
                                this.accept(Token.RPAREN);
                            }
                            return true;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
                            this.lexer.nextToken();
                            switch (this.lexer.token()) {
                                case INDEX:
                                case KEY: {
                                    this.lexer.nextToken();
                                    final SQLName name = this.exprParser.name();
                                    this.accept(Token.TO);
                                    final SQLName to = this.exprParser.name();
                                    final SQLAlterTableRenameIndex item13 = new SQLAlterTableRenameIndex(name, to);
                                    stmt.addItem(item13);
                                    return true;
                                }
                                case COLUMN: {
                                    this.lexer.nextToken();
                                    final SQLName columnName = this.exprParser.name();
                                    this.accept(Token.TO);
                                    final SQLName toName = this.exprParser.name();
                                    final SQLAlterTableRenameColumn renameColumn = new SQLAlterTableRenameColumn();
                                    renameColumn.setColumn(columnName);
                                    renameColumn.setTo(toName);
                                    stmt.addItem(renameColumn);
                                    return true;
                                }
                                case TO:
                                case AS: {
                                    this.lexer.nextToken();
                                }
                                case IDENTIFIER: {
                                    final SQLAlterTableRename item14 = new SQLAlterTableRename();
                                    final SQLName to = this.exprParser.name();
                                    item14.setTo(to);
                                    stmt.addItem(item14);
                                    return true;
                                }
                                default: {
                                    break Label_4915;
                                }
                            }
                        }
                        else {
                            if (this.lexer.identifierEquals(FnvHash.Constants.WITHOUT)) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("VALIDATION");
                                final MySqlAlterTableValidation item15 = new MySqlAlterTableValidation();
                                item15.setWithValidation(false);
                                stmt.addItem(item15);
                                return true;
                            }
                            if (this.lexer.identifierEquals("COALESCE")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                final SQLAlterTableCoalescePartition item16 = new SQLAlterTableCoalescePartition();
                                final SQLIntegerExpr countExpr = this.exprParser.integerExpr();
                                item16.setCount(countExpr);
                                stmt.addItem(item16);
                                return true;
                            }
                            if (this.lexer.identifierEquals("REORGANIZE")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                final SQLAlterTableReOrganizePartition item17 = new SQLAlterTableReOrganizePartition();
                                this.exprParser.names(item17.getNames(), item17);
                                this.accept(Token.INTO);
                                this.accept(Token.LPAREN);
                                while (true) {
                                    final SQLPartition partition2 = this.getExprParser().parsePartition();
                                    item17.addPartition(partition2);
                                    if (this.lexer.token() != Token.COMMA) {
                                        break;
                                    }
                                    this.lexer.nextToken();
                                }
                                this.accept(Token.RPAREN);
                                stmt.addItem(item17);
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.EXCHANGE)) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                final SQLAlterTableExchangePartition item18 = new SQLAlterTableExchangePartition();
                                final SQLName partition3 = this.exprParser.name();
                                item18.addPartition(partition3);
                                this.accept(Token.WITH);
                                this.accept(Token.TABLE);
                                final SQLName table = this.exprParser.name();
                                item18.setTable(table);
                                if (this.lexer.token() == Token.WITH) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("VALIDATION");
                                    item18.setValidation(true);
                                }
                                else if (this.lexer.identifierEquals(FnvHash.Constants.WITHOUT)) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("VALIDATION");
                                    item18.setValidation(false);
                                }
                                stmt.addItem(item18);
                                return true;
                            }
                            if (this.lexer.identifierEquals("REBUILD")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                final SQLAlterTableRebuildPartition item19 = new SQLAlterTableRebuildPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item19.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                }
                                else {
                                    this.exprParser.names(item19.getPartitions(), item19);
                                }
                                stmt.addItem(item19);
                                return true;
                            }
                            if (this.lexer.identifierEquals("REPAIR")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                final SQLAlterTableRepairPartition item20 = new SQLAlterTableRepairPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item20.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                }
                                else {
                                    this.exprParser.names(item20.getPartitions(), item20);
                                }
                                stmt.addItem(item20);
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.REMOVE)) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("PARTITIONING");
                                stmt.setRemovePatiting(true);
                                break;
                            }
                            if (this.lexer.identifierEquals("UPGRADE")) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("PARTITIONING");
                                stmt.setUpgradePatiting(true);
                                break;
                            }
                            if (this.lexer.identifierEquals("HOT_PARTITION_COUNT")) {
                                this.lexer.nextToken();
                                this.accept(Token.EQ);
                                try {
                                    stmt.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("HOT_PARTITION_COUNT"), this.exprParser.integerExpr()));
                                    break;
                                }
                                catch (Exception e) {
                                    throw new ParserException("only integer number is supported for hot_partition_count");
                                }
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
                                final SQLAlterTablePartitionCount item21 = new SQLAlterTablePartitionCount();
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.EQ) {
                                    this.lexer.nextToken();
                                }
                                item21.setCount(this.exprParser.integerExpr());
                                stmt.addItem(item21);
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
                                this.lexer.nextToken();
                                if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                                    this.lexer.nextToken();
                                    final SQLAlterTableSubpartitionLifecycle item22 = new SQLAlterTableSubpartitionLifecycle();
                                    if (this.lexer.token() == Token.LITERAL_INT) {
                                        while (true) {
                                            item22.getPartitionIds().add(this.exprParser.integerExpr());
                                            final String pidStr = this.lexer.stringVal();
                                            this.accept(Token.VARIANT);
                                            final String s = pidStr.replaceAll(":", "");
                                            if (StringUtils.isEmpty(s)) {
                                                item22.getSubpartitionLifeCycle().add(this.exprParser.integerExpr());
                                            }
                                            else {
                                                item22.getSubpartitionLifeCycle().add(new SQLIntegerExpr(Integer.valueOf(s)));
                                            }
                                            if (this.lexer.token() != Token.COMMA) {
                                                break;
                                            }
                                            this.lexer.nextToken();
                                        }
                                    }
                                    stmt.addItem(item22);
                                }
                                return true;
                            }
                            if (this.lexer.identifierEquals("BLOCK_SIZE")) {
                                final SQLAlterTableBlockSize item23 = new SQLAlterTableBlockSize();
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.EQ) {
                                    this.accept(Token.EQ);
                                }
                                item23.setSize((SQLIntegerExpr)this.exprParser.expr());
                                stmt.addItem(item23);
                                return true;
                            }
                            if (this.lexer.identifierEquals("INSERT_METHOD")) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.EQ) {
                                    this.lexer.nextToken();
                                }
                                stmt.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("INSERT_METHOD"), this.exprParser.primary()));
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                                final SQLAlterTableModifyClusteredBy clusteredBy = new SQLAlterTableModifyClusteredBy();
                                this.acceptIdentifier("CLUSTERED");
                                this.accept(Token.BY);
                                this.accept(Token.LPAREN);
                                if (this.lexer.token() != Token.RPAREN) {
                                    while (true) {
                                        clusteredBy.addClusterColumn(this.exprParser.name());
                                        if (this.lexer.token() != Token.COMMA) {
                                            break;
                                        }
                                        this.accept(Token.COMMA);
                                    }
                                }
                                this.accept(Token.RPAREN);
                                stmt.addItem(clusteredBy);
                                return true;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION_AVAILABLE_PARTITION_NUM)) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.EQ) {
                                    this.lexer.nextToken();
                                }
                                final SQLIntegerExpr num = this.exprParser.integerExpr();
                                final SQLAlterTableSubpartitionAvailablePartitionNum item24 = new SQLAlterTableSubpartitionAvailablePartitionNum();
                                item24.setNumber(num);
                                stmt.addItem(item24);
                                return true;
                            }
                            break;
                        }
                    }
                    break;
                }
                case ALTER: {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.INDEX) {
                        this.lexer.nextToken();
                        final SQLName indexName = this.exprParser.name();
                        if (this.lexer.identifierEquals("VISIBLE")) {
                            final SQLAlterTableAlterIndex alterIndex = new SQLAlterTableAlterIndex();
                            alterIndex.setName(indexName);
                            this.lexer.nextToken();
                            alterIndex.getIndexDefinition().getOptions().setVisible(true);
                            stmt.addItem(alterIndex);
                            break;
                        }
                        final MySqlAlterTableAlterFullTextIndex alterIndex2 = new MySqlAlterTableAlterFullTextIndex();
                        alterIndex2.setIndexName(indexName);
                        this.accept(Token.SET);
                        this.accept(Token.FULLTEXT);
                        if (this.lexer.token() == Token.INDEX) {
                            this.lexer.nextToken();
                            alterIndex2.setAnalyzerType(AnalyzerIndexType.INDEX);
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.QUERY)) {
                            this.lexer.nextToken();
                            alterIndex2.setAnalyzerType(AnalyzerIndexType.QUERY);
                        }
                        this.acceptIdentifier("ANALYZER");
                        this.accept(Token.EQ);
                        alterIndex2.setAnalyzerName(this.exprParser.name());
                        stmt.addItem(alterIndex2);
                    }
                    else if (this.lexer.token() == Token.CHECK || this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final MysqlAlterTableAlterCheck check2 = new MysqlAlterTableAlterCheck();
                        check2.setName(this.exprParser.name());
                        boolean enforce2 = true;
                        if (this.lexer.token() == Token.NOT) {
                            enforce2 = false;
                            this.lexer.nextToken();
                        }
                        if (this.lexer.stringVal().equalsIgnoreCase("ENFORCED")) {
                            check2.setEnforced(enforce2);
                            this.lexer.nextToken();
                        }
                        stmt.addItem(check2);
                    }
                    else {
                        if (this.lexer.token() == Token.COLUMN) {
                            this.lexer.nextToken();
                        }
                        final MySqlAlterTableAlterColumn alterColumn = new MySqlAlterTableAlterColumn();
                        alterColumn.setColumn(this.exprParser.name());
                        if (this.lexer.token() == Token.SET) {
                            this.lexer.nextToken();
                            this.accept(Token.DEFAULT);
                            alterColumn.setDefaultExpr(this.exprParser.expr());
                        }
                        else {
                            this.accept(Token.DROP);
                            this.accept(Token.DEFAULT);
                            alterColumn.setDropDefault(true);
                        }
                        stmt.addItem(alterColumn);
                    }
                    return true;
                }
                case DISABLE: {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableDisableConstraint item25 = new SQLAlterTableDisableConstraint();
                        item25.setConstraintName(this.exprParser.name());
                        stmt.addItem(item25);
                    }
                    else {
                        this.acceptIdentifier("KEYS");
                        final SQLAlterTableDisableKeys item26 = new SQLAlterTableDisableKeys();
                        stmt.addItem(item26);
                    }
                    return true;
                }
                case ENABLE: {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableEnableConstraint item27 = new SQLAlterTableEnableConstraint();
                        item27.setConstraintName(this.exprParser.name());
                        stmt.addItem(item27);
                    }
                    else {
                        this.acceptIdentifier("KEYS");
                        final SQLAlterTableEnableKeys item28 = new SQLAlterTableEnableKeys();
                        stmt.addItem(item28);
                    }
                    return true;
                }
                case LOCK: {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    final MySqlAlterTableLock item29 = new MySqlAlterTableLock();
                    item29.setLockType(new SQLIdentifierExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                    stmt.addItem(item29);
                    return true;
                }
                case ORDER: {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    final MySqlAlterTableOrderBy item30 = new MySqlAlterTableOrderBy();
                    while (this.lexer.token() == Token.IDENTIFIER) {
                        final SQLSelectOrderByItem column = this.exprParser.parseSelectOrderByItem();
                        column.setParent(item30);
                        item30.addColumn(column);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    stmt.addItem(item30);
                    return true;
                }
                case WITH: {
                    this.lexer.nextToken();
                    this.acceptIdentifier("VALIDATION");
                    final MySqlAlterTableValidation item15 = new MySqlAlterTableValidation();
                    item15.setWithValidation(true);
                    stmt.addItem(item15);
                    return true;
                }
                case DROP: {
                    this.parseAlterDrop(stmt);
                    return true;
                }
                case TRUNCATE: {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableTruncatePartition item31 = new SQLAlterTableTruncatePartition();
                    if (this.lexer.token() == Token.ALL) {
                        item31.getPartitions().add(new SQLIdentifierExpr("ALL"));
                        this.lexer.nextToken();
                    }
                    else {
                        this.exprParser.names(item31.getPartitions(), item31);
                    }
                    stmt.addItem(item31);
                    return true;
                }
                case ANALYZE: {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableAnalyzePartition item32 = new SQLAlterTableAnalyzePartition();
                    if (this.lexer.token() == Token.ALL) {
                        this.lexer.nextToken();
                        item32.getPartitions().add(new SQLIdentifierExpr("ALL"));
                    }
                    else {
                        this.exprParser.names(item32.getPartitions(), item32);
                    }
                    stmt.addItem(item32);
                    return true;
                }
                case CHECK: {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableCheckPartition item33 = new SQLAlterTableCheckPartition();
                    if (this.lexer.token() == Token.ALL) {
                        this.lexer.nextToken();
                        item33.getPartitions().add(new SQLIdentifierExpr("ALL"));
                    }
                    else {
                        this.exprParser.names(item33.getPartitions(), item33);
                    }
                    stmt.addItem(item33);
                    return true;
                }
                case OPTIMIZE: {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableOptimizePartition item34 = new SQLAlterTableOptimizePartition();
                    if (this.lexer.token() == Token.ALL) {
                        this.lexer.nextToken();
                        item34.getPartitions().add(new SQLIdentifierExpr("ALL"));
                    }
                    else {
                        this.exprParser.names(item34.getPartitions(), item34);
                    }
                    stmt.addItem(item34);
                    return true;
                }
                case SET: {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.RULE)) {
                        final SQLAlterTableSetOption setOption = new SQLAlterTableSetOption();
                        final SQLAssignItem item35 = this.exprParser.parseAssignItem();
                        setOption.addOption(item35);
                        stmt.addItem(setOption);
                    }
                    else {
                        this.acceptIdentifier("TBLPROPERTIES");
                        final SQLAlterTableSetOption setOption = new SQLAlterTableSetOption();
                        this.accept(Token.LPAREN);
                        while (true) {
                            final SQLAssignItem item35 = this.exprParser.parseAssignItem();
                            setOption.addOption(item35);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        stmt.addItem(setOption);
                        if (this.lexer.token() == Token.ON) {
                            this.lexer.nextToken();
                            final SQLName on = this.exprParser.name();
                            setOption.setOn(on);
                        }
                    }
                    return true;
                }
                case PARTITION: {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                        this.lexer.nextToken();
                        final SQLAlterTablePartitionLifecycle item36 = new SQLAlterTablePartitionLifecycle();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        item36.setLifecycle(this.exprParser.integerExpr());
                        stmt.addItem(item36);
                        return true;
                    }
                    this.lexer.reset(mark);
                    break;
                }
            }
        }
        return false;
    }
    
    protected SQLStatement parseAlterTable(final boolean ignore, final boolean online, final boolean offline) {
        this.lexer.nextToken();
        final SQLAlterTableStatement stmt = new SQLAlterTableStatement(this.getDbType());
        stmt.setIgnore(ignore);
        stmt.setOnline(online);
        stmt.setOffline(offline);
        stmt.setName(this.exprParser.name());
        while (true) {
            boolean parsed = ((MySqlExprParser)this.exprParser).parseTableOptions(stmt.getTableOptions(), stmt);
            if (!parsed) {
                parsed = this.parseAlterSpecification(stmt);
            }
            if (!parsed) {
                break;
            }
            if (this.lexer.token() != Token.COMMA) {
                continue;
            }
            this.lexer.nextToken();
        }
        if (Token.PARTITION == this.lexer.token()) {
            final SQLPartitionBy partitionBy = this.getSQLCreateTableParser().parsePartitionBy();
            stmt.setPartition(partitionBy);
        }
        else if (1 == stmt.getItems().size() && stmt.getItems().get(0) instanceof SQLAlterTableRename) {
            final MySqlRenameTableStatement renameStmt = new MySqlRenameTableStatement();
            final MySqlRenameTableStatement.Item item = new MySqlRenameTableStatement.Item();
            item.setName((SQLName)stmt.getTableSource().getExpr());
            item.setTo(stmt.getItems().get(0).getToName());
            renameStmt.addItem(item);
            return renameStmt;
        }
        return stmt;
    }
    
    private MySqlExtPartition parseExtPartition() {
        final MySqlExtPartition partitionDef = new MySqlExtPartition();
        while (true) {
            final MySqlExtPartition.Item item = new MySqlExtPartition.Item();
            if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION)) {
                this.lexer.nextToken();
                final SQLName name = this.exprParser.name();
                item.setDbPartition(name);
                this.accept(Token.BY);
                final SQLExpr value = this.exprParser.primary();
                item.setDbPartitionBy(value);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITION)) {
                this.lexer.nextToken();
                final SQLName name = this.exprParser.name();
                item.setTbPartition(name);
                this.accept(Token.BY);
                final SQLExpr value = this.exprParser.primary();
                item.setTbPartitionBy(value);
            }
            item.setParent(partitionDef);
            partitionDef.getItems().add(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return partitionDef;
    }
    
    private SQLAlterCharacter alterTableCharacter() {
        this.lexer.nextToken();
        this.accept(Token.SET);
        if (this.lexer.token() == Token.EQ) {
            this.lexer.nextToken();
        }
        final SQLAlterCharacter item = new SQLAlterCharacter();
        item.setCharacterSet(this.exprParser.primary());
        if (this.lexer.token() == Token.COMMA) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                item.setCollate(this.exprParser.primary());
            }
        }
        return item;
    }
    
    protected void parseAlterTableAddColumn(final SQLAlterTableStatement stmt) {
        boolean parenFlag = false;
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            parenFlag = true;
        }
        final SQLAlterTableAddColumn item = new SQLAlterTableAddColumn();
        while (true) {
            final SQLColumnDefinition columnDef = this.exprParser.parseColumn();
            item.addColumn(columnDef);
            if (this.lexer.identifierEquals("AFTER")) {
                this.lexer.nextToken();
                item.setAfterColumn(this.exprParser.name());
            }
            else if (this.lexer.identifierEquals("FIRST")) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.IDENTIFIER) {
                    item.setFirstColumn(this.exprParser.name());
                }
                else {
                    item.setFirst(true);
                }
            }
            if (!parenFlag || this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        if (parenFlag) {
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.RESTRICT)) {
            this.lexer.nextToken();
            item.setRestrict(true);
        }
        else if (this.lexer.token() == Token.CASCADE || this.lexer.identifierEquals(FnvHash.Constants.CASCADE)) {
            this.lexer.nextToken();
            item.setCascade(true);
        }
        else {
            item.setCascade(false);
        }
        stmt.addItem(item);
    }
    
    @Override
    public void parseAlterDrop(final SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
            final SQLName indexName = this.exprParser.name();
            final SQLAlterTableDropIndex item = new SQLAlterTableDropIndex();
            item.setIndexName(indexName);
            stmt.addItem(item);
        }
        else if (this.lexer.token() == Token.FOREIGN) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            final SQLName indexName = this.exprParser.name();
            final SQLAlterTableDropForeignKey item2 = new SQLAlterTableDropForeignKey();
            item2.setIndexName(indexName);
            stmt.addItem(item2);
        }
        else if (this.lexer.token() == Token.KEY) {
            this.lexer.nextToken();
            final SQLName keyName = this.exprParser.name();
            final SQLAlterTableDropKey item3 = new SQLAlterTableDropKey();
            item3.setKeyName(keyName);
            stmt.addItem(item3);
        }
        else if (this.lexer.token() == Token.PRIMARY) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            final SQLAlterTableDropPrimaryKey item4 = new SQLAlterTableDropPrimaryKey();
            stmt.addItem(item4);
        }
        else if (this.lexer.token() == Token.CONSTRAINT) {
            this.lexer.nextToken();
            final SQLAlterTableDropConstraint item5 = new SQLAlterTableDropConstraint();
            item5.setConstraintName(this.exprParser.name());
            stmt.addItem(item5);
        }
        else if (this.lexer.token() == Token.COLUMN) {
            this.lexer.nextToken();
            final SQLAlterTableDropColumnItem item6 = new SQLAlterTableDropColumnItem();
            SQLName name = this.exprParser.name();
            name.setParent(item6);
            item6.addColumn(name);
            if (this.dbType != DbType.mysql) {
                while (this.lexer.token() == Token.COMMA) {
                    final char markChar = this.lexer.current();
                    final int markBp = this.lexer.bp();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.CHANGE) || this.lexer.identifierEquals(FnvHash.Constants.MODIFY)) {
                        this.lexer.reset(markBp, markChar, Token.COMMA);
                        break;
                    }
                    if (this.lexer.token() != Token.IDENTIFIER) {
                        this.lexer.reset(markBp, markChar, Token.COMMA);
                        break;
                    }
                    if ("ADD".equalsIgnoreCase(this.lexer.stringVal())) {
                        this.lexer.reset(markBp, markChar, Token.COMMA);
                        break;
                    }
                    name = this.exprParser.name();
                    name.setParent(item6);
                    item6.addColumn(name);
                }
            }
            stmt.addItem(item6);
        }
        else if (this.lexer.token() == Token.PARTITION) {
            final SQLAlterTableDropPartition dropPartition = this.parseAlterTableDropPartition(false);
            stmt.addItem(dropPartition);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
            final SQLAlterTableDropSubpartition dropPartition2 = this.parseAlterTableDropSubpartition();
            stmt.addItem(dropPartition2);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERING) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
            this.lexer.nextToken();
            final SQLAlterTableDropClusteringKey dropPartition3 = new SQLAlterTableDropClusteringKey();
            this.accept(Token.KEY);
            dropPartition3.setKeyName(this.exprParser.name());
            stmt.addItem(dropPartition3);
        }
        else if (this.lexer.token() == Token.IDENTIFIER) {
            if (this.lexer.identifierEquals(FnvHash.Constants.EXTPARTITION)) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLAlterTableDropExtPartition extPartitionItem = new SQLAlterTableDropExtPartition();
                final MySqlExtPartition partitionDef = this.parseExtPartition();
                extPartitionItem.setExPartition(partitionDef);
                stmt.addItem(extPartitionItem);
                this.accept(Token.RPAREN);
            }
            else {
                final SQLAlterTableDropColumnItem item6 = new SQLAlterTableDropColumnItem();
                final SQLName name = this.exprParser.name();
                item6.addColumn(name);
                stmt.addItem(item6);
                if (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                }
                if (this.lexer.token() == Token.DROP) {
                    this.parseAlterDrop(stmt);
                }
            }
        }
        else {
            super.parseAlterDrop(stmt);
        }
    }
    
    @Override
    public SQLStatement parseRename() {
        this.acceptIdentifier("RENAME");
        if (this.lexer.token() == Token.SEQUENCE) {
            this.lexer.nextToken();
            final MySqlRenameSequenceStatement stmt = new MySqlRenameSequenceStatement();
            final SQLName name = this.exprParser.name();
            stmt.setName(name);
            this.accept(Token.TO);
            final SQLName to = this.exprParser.name();
            stmt.setTo(to);
            return stmt;
        }
        if (this.lexer.token() == Token.USER) {
            this.lexer.nextToken();
            final SQLRenameUserStatement stmt2 = new SQLRenameUserStatement();
            final SQLName name = this.exprParser.name();
            stmt2.setName(name);
            this.accept(Token.TO);
            final SQLName to = this.exprParser.name();
            stmt2.setTo(to);
            return stmt2;
        }
        this.accept(Token.TABLE);
        final MySqlRenameTableStatement stmt3 = new MySqlRenameTableStatement();
        while (true) {
            final MySqlRenameTableStatement.Item item = new MySqlRenameTableStatement.Item();
            item.setName(this.exprParser.name());
            this.accept(Token.TO);
            item.setTo(this.exprParser.name());
            stmt3.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return stmt3;
    }
    
    @Override
    public SQLStatement parseCreateDatabase() {
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.SCHEMA) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        final SQLCreateDatabaseStatement stmt = new SQLCreateDatabaseStatement(this.dbType);
        if (this.lexer.token() == Token.HINT) {
            final List<SQLCommentHint> hints = this.exprParser.parseHints();
            if (hints.size() == 1) {
                final String text = hints.get(0).getText();
                if (text.endsWith(" IF NOT EXISTS") && text.charAt(0) == '!') {
                    stmt.setIfNotExists(true);
                }
            }
        }
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.HINT) {
            stmt.setHints(this.exprParser.parseHints());
        }
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
        }
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
                this.lexer.nextToken();
                this.accept(Token.SET);
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final String charset = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
                stmt.setCharacterSet(charset);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.CHARSET)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final String charset = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
                stmt.setCharacterSet(charset);
            }
            else if (this.lexer.token() == Token.DEFAULT) {
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final String collate = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
                stmt.setCollate(collate);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PASSWORD)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr password = this.exprParser.primary();
                stmt.setPassword(password);
            }
            else {
                if (!this.lexer.identifierEquals("SHARDS") && !this.lexer.identifierEquals("SHARD_ID") && !this.lexer.identifierEquals("REPLICATION") && !this.lexer.identifierEquals("STORAGE_DEPENDENCY") && !this.lexer.identifierEquals("REPLICA_TYPE") && !this.lexer.identifierEquals("DATA_REPLICATION")) {
                    break;
                }
                final String key = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.expr();
                stmt.getOptions().put(key, value);
            }
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            final String user = this.lexer.stringVal();
            this.lexer.nextToken();
            stmt.setUser(user);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.OPTIONS)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (this.lexer.token() != Token.RPAREN) {
                final String key = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.expr();
                stmt.getOptions().put(key, value);
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
                assignItem.setParent(stmt);
                stmt.getDbProperties().add(assignItem);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.BY) {
                this.accept(Token.BY);
                while (true) {
                    final List<SQLAssignItem> storedByItem = new ArrayList<SQLAssignItem>();
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLAssignItem assignItem2 = this.exprParser.parseAssignItem();
                        assignItem2.setParent(stmt);
                        storedByItem.add(assignItem2);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                    stmt.getStoredBy().add(storedByItem);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            else if (this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                stmt.setStoredIn(this.exprParser.name());
                this.accept(Token.ON);
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
                    assignItem.setParent(stmt);
                    stmt.getStoredOn().add(assignItem);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            else {
                if (this.lexer.token() != Token.AS) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                final SQLExpr like = this.exprParser.expr();
                stmt.setStoredAs(like);
            }
        }
        return stmt;
    }
    
    @Override
    protected void parseUpdateSet(final SQLUpdateStatement update) {
        this.accept(Token.SET);
        while (true) {
            final SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
            update.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    public SQLStatement parseAlterDatabase() {
        if (this.lexer.token() == Token.SCHEMA) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        final SQLAlterDatabaseStatement stmt = new SQLAlterDatabaseStatement(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            final MySqlAlterDatabaseSetOption option = new MySqlAlterDatabaseSetOption();
            while (true) {
                final SQLName key = this.exprParser.name();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.expr();
                option.getOptions().add(new SQLAssignItem(key, value));
                if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.ON) {
                    break;
                }
                if (this.lexer.token() != Token.COMMA) {
                    continue;
                }
                this.lexer.nextToken();
            }
            stmt.setItem(option);
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                final SQLName on = this.exprParser.name();
                option.setOn(on);
            }
            return stmt;
        }
        if (this.lexer.token() == Token.KILL) {
            final MySqlAlterDatabaseKillJob item = new MySqlAlterDatabaseKillJob();
            this.lexer.nextToken();
            final SQLName jobType = this.exprParser.name();
            final SQLName jobId = this.exprParser.name();
            item.setJobType(jobType);
            item.setJobId(jobId);
            stmt.setItem(item);
        }
        if (this.lexer.identifierEquals("UPGRADE")) {
            this.lexer.nextToken();
            this.acceptIdentifier("DATA");
            this.acceptIdentifier("DIRECTORY");
            this.acceptIdentifier("NAME");
            stmt.setUpgradeDataDirectoryName(true);
        }
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            final SQLAlterCharacter item2 = this.alterTableCharacter();
            stmt.setCharacter(item2);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
            final SQLAlterCharacter item2 = this.alterTableCharacter();
            stmt.setCharacter(item2);
        }
        return stmt;
    }
    
    public MySqlAlterUserStatement parseAlterUser() {
        this.accept(Token.USER);
        final MySqlAlterUserStatement stmt = new MySqlAlterUserStatement();
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        while (true) {
            final MySqlAlterUserStatement.AlterUser alterUser = new MySqlAlterUserStatement.AlterUser();
            final SQLExpr user = this.exprParser.expr();
            alterUser.setUser(user);
            if (this.lexer.identifierEquals("IDENTIFIED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                final MySqlAlterUserStatement.AuthOption authOption = new MySqlAlterUserStatement.AuthOption();
                final SQLCharExpr authString = this.exprParser.charExpr();
                authOption.setAuthString(authString);
                alterUser.setAuthOption(authOption);
            }
            if (this.lexer.identifierEquals("PASSWORD")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("EXPIRE")) {
                    this.lexer.nextToken();
                    final MySqlAlterUserStatement.PasswordOption passwordOption = new MySqlAlterUserStatement.PasswordOption();
                    if (this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                        passwordOption.setExpire(MySqlAlterUserStatement.PasswordExpire.PASSWORD_EXPIRE_DEFAULT);
                    }
                    else if (this.lexer.identifierEquals("NEVER")) {
                        this.lexer.nextToken();
                        passwordOption.setExpire(MySqlAlterUserStatement.PasswordExpire.PASSWORD_EXPIRE_NEVER);
                    }
                    else if (this.lexer.token() == Token.INTERVAL) {
                        this.lexer.nextToken();
                        passwordOption.setExpire(MySqlAlterUserStatement.PasswordExpire.PASSWORD_EXPIRE_INTERVAL);
                        final SQLIntegerExpr days = this.exprParser.integerExpr();
                        passwordOption.setIntervalDays(days);
                        this.acceptIdentifier("DAY");
                    }
                    else {
                        passwordOption.setExpire(MySqlAlterUserStatement.PasswordExpire.PASSWORD_EXPIRE);
                    }
                    stmt.setPasswordOption(passwordOption);
                }
            }
            stmt.getAlterUsers().add(alterUser);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return stmt;
    }
    
    @Override
    public MySqlExprParser getExprParser() {
        return (MySqlExprParser)this.exprParser;
    }
    
    @Override
    public SQLCreateFunctionStatement parseCreateFunction() {
        final SQLCreateFunctionStatement stmt = new SQLCreateFunctionStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OR) {
                this.lexer.nextToken();
                this.accept(Token.REPLACE);
                stmt.setOrReplace(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = this.getExprParser().userName();
            stmt.setDefiner(definer);
        }
        this.accept(Token.FUNCTION);
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters(), stmt);
            this.accept(Token.RPAREN);
        }
        this.acceptIdentifier("RETURNS");
        final SQLDataType dataType = this.exprParser.parseDataType();
        stmt.setReturnDataType(dataType);
        while (true) {
            if (this.lexer.identifierEquals("DETERMINISTIC")) {
                this.lexer.nextToken();
                stmt.setDeterministic(true);
            }
            else if (this.lexer.identifierEquals("DETERMINISTIC")) {
                this.lexer.nextToken();
                stmt.setDeterministic(true);
            }
            else if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                stmt.setComment(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                if (!this.lexer.identifierEquals("LANGUAGE")) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setLanguage(this.lexer.stringVal());
                this.lexer.nextToken();
            }
        }
        SQLStatement block;
        if (this.lexer.token() == Token.BEGIN) {
            block = this.parseBlock();
        }
        else {
            block = this.parseStatement();
        }
        stmt.setBlock(block);
        return stmt;
    }
    
    @Override
    public SQLCreateProcedureStatement parseCreateProcedure() {
        final SQLCreateProcedureStatement stmt = new SQLCreateProcedureStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OR) {
                this.lexer.nextToken();
                this.accept(Token.REPLACE);
                stmt.setOrReplace(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = this.getExprParser().userName();
            stmt.setDefiner(definer);
        }
        this.accept(Token.PROCEDURE);
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters(), stmt);
            this.accept(Token.RPAREN);
        }
        while (true) {
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                stmt.setComment(this.exprParser.charExpr());
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LANGUAGE)) {
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setLanguageSql(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.DETERMINISTIC)) {
                this.lexer.nextToken();
                stmt.setDeterministic(true);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.CONTAINS) && this.lexer.token() != Token.CONTAINS) {
                    break;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("SQL");
                stmt.setContainsSql(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
            this.lexer.nextToken();
            this.acceptIdentifier("SECURITY");
            final SQLName authid = this.exprParser.name();
            stmt.setAuthid(authid);
        }
        SQLStatement block;
        if (this.lexer.token() == Token.BEGIN) {
            block = this.parseBlock();
        }
        else {
            block = this.parseStatement();
        }
        stmt.setBlock(block);
        return stmt;
    }
    
    private void parserParameters(final List<SQLParameter> parameters, final SQLObject parent) {
        if (this.lexer.token() == Token.RPAREN) {
            return;
        }
        do {
            final SQLParameter parameter = new SQLParameter();
            if (this.lexer.token() == Token.CURSOR) {
                this.lexer.nextToken();
                parameter.setName(this.exprParser.name());
                this.accept(Token.IS);
                final SQLSelect select = this.createSQLSelectParser().select();
                final SQLDataTypeImpl dataType = new SQLDataTypeImpl();
                dataType.setName("CURSOR");
                parameter.setDataType(dataType);
                parameter.setDefaultValue(new SQLQueryExpr(select));
            }
            else if (this.lexer.token() == Token.IN || this.lexer.token() == Token.OUT || this.lexer.token() == Token.INOUT) {
                if (this.lexer.token() == Token.IN) {
                    parameter.setParamType(SQLParameter.ParameterType.IN);
                }
                else if (this.lexer.token() == Token.OUT) {
                    parameter.setParamType(SQLParameter.ParameterType.OUT);
                }
                else if (this.lexer.token() == Token.INOUT) {
                    parameter.setParamType(SQLParameter.ParameterType.INOUT);
                }
                this.lexer.nextToken();
                parameter.setName(this.exprParser.name());
                parameter.setDataType(this.exprParser.parseDataType());
            }
            else {
                parameter.setParamType(SQLParameter.ParameterType.DEFAULT);
                parameter.setName(this.exprParser.name());
                parameter.setDataType(this.exprParser.parseDataType());
                if (this.lexer.token() == Token.COLONEQ) {
                    this.lexer.nextToken();
                    parameter.setDefaultValue(this.exprParser.expr());
                }
            }
            parameters.add(parameter);
            if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            }
        } while (this.lexer.token() != Token.BEGIN && this.lexer.token() != Token.RPAREN);
    }
    
    private void parseProcedureStatementList(final List<SQLStatement> statementList) {
        this.parseProcedureStatementList(statementList, -1);
    }
    
    private void parseProcedureStatementList(final List<SQLStatement> statementList, final int max) {
        while (max == -1 || statementList.size() < max) {
            if (this.lexer.token() == Token.EOF) {
                return;
            }
            if (this.lexer.token() == Token.END) {
                return;
            }
            if (this.lexer.token() == Token.ELSE) {
                return;
            }
            if (this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token() == Token.WHEN) {
                    return;
                }
                if (this.lexer.token() == Token.UNTIL) {
                    return;
                }
                if (this.lexer.token() == Token.SELECT) {
                    statementList.add(this.parseSelectInto());
                }
                else if (this.lexer.token() == Token.UPDATE) {
                    statementList.add(this.parseUpdateStatement());
                }
                else if (this.lexer.token() == Token.CREATE) {
                    statementList.add(this.parseCreate());
                }
                else if (this.lexer.token() == Token.INSERT) {
                    final SQLStatement stmt = this.parseInsert();
                    statementList.add(stmt);
                }
                else if (this.lexer.token() == Token.DELETE) {
                    statementList.add(this.parseDeleteStatement());
                }
                else if (this.lexer.token() == Token.LBRACE || this.lexer.identifierEquals("CALL")) {
                    statementList.add(this.parseCall());
                }
                else if (this.lexer.token() == Token.BEGIN) {
                    statementList.add(this.parseBlock());
                }
                else if (this.lexer.token() == Token.VARIANT) {
                    final SQLExpr variant = this.exprParser.primary();
                    if (variant instanceof SQLBinaryOpExpr) {
                        final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)variant;
                        if (binaryOpExpr.getOperator() == SQLBinaryOperator.Assignment) {
                            final SQLSetStatement stmt2 = new SQLSetStatement(binaryOpExpr.getLeft(), binaryOpExpr.getRight(), this.getDbType());
                            statementList.add(stmt2);
                            continue;
                        }
                    }
                    this.accept(Token.COLONEQ);
                    final SQLExpr value = this.exprParser.expr();
                    final SQLSetStatement stmt2 = new SQLSetStatement(variant, value, this.getDbType());
                    statementList.add(stmt2);
                }
                else if (this.lexer.token() == Token.LPAREN) {
                    final char ch = this.lexer.current();
                    final int bp = this.lexer.bp();
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.SELECT) {
                        throw new ParserException("TODO. " + this.lexer.info());
                    }
                    this.lexer.reset(bp, ch, Token.LPAREN);
                    statementList.add(this.parseSelect());
                }
                else if (this.lexer.token() == Token.SET) {
                    statementList.add(this.parseAssign());
                }
                else if (this.lexer.token() == Token.WHILE) {
                    final SQLStatement stmt = this.parseWhile();
                    statementList.add(stmt);
                }
                else if (this.lexer.token() == Token.LOOP) {
                    statementList.add(this.parseLoop());
                }
                else if (this.lexer.token() == Token.IF) {
                    statementList.add(this.parseIf());
                }
                else if (this.lexer.token() == Token.CASE) {
                    statementList.add(this.parseCase());
                }
                else if (this.lexer.token() == Token.DECLARE) {
                    final SQLStatement stmt = this.parseDeclare();
                    statementList.add(stmt);
                }
                else if (this.lexer.token() == Token.LEAVE) {
                    statementList.add(this.parseLeave());
                }
                else if (this.lexer.token() == Token.ITERATE) {
                    statementList.add(this.parseIterate());
                }
                else if (this.lexer.token() == Token.REPEAT) {
                    statementList.add(this.parseRepeat());
                }
                else if (this.lexer.token() == Token.OPEN) {
                    statementList.add(this.parseOpen());
                }
                else if (this.lexer.token() == Token.CLOSE) {
                    statementList.add(this.parseClose());
                }
                else if (this.lexer.token() == Token.FETCH) {
                    statementList.add(this.parseFetch());
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.CHECKSUM)) {
                        if (this.lexer.token() == Token.IDENTIFIER) {
                            final String label = this.lexer.stringVal();
                            final char ch2 = this.lexer.current();
                            final int bp2 = this.lexer.bp();
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.VARIANT && this.lexer.stringVal().equals(":")) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.LOOP) {
                                    statementList.add(this.parseLoop(label));
                                    continue;
                                }
                                if (this.lexer.token() == Token.WHILE) {
                                    statementList.add(this.parseWhile(label));
                                    continue;
                                }
                                if (this.lexer.token() == Token.BEGIN) {
                                    statementList.add(this.parseBlock(label));
                                    continue;
                                }
                                if (this.lexer.token() == Token.REPEAT) {
                                    statementList.add(this.parseRepeat(label));
                                    continue;
                                }
                                continue;
                            }
                            else {
                                this.lexer.reset(bp2, ch2, Token.IDENTIFIER);
                            }
                        }
                        throw new ParserException("TODO, " + this.lexer.info());
                    }
                    statementList.add(this.parseChecksum());
                }
            }
        }
    }
    
    public MySqlChecksumTableStatement parseChecksum() {
        final MySqlChecksumTableStatement stmt = new MySqlChecksumTableStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.CHECKSUM)) {
            this.lexer.nextToken();
            this.accept(Token.TABLE);
            while (true) {
                final SQLName table = this.exprParser.name();
                stmt.addTable(new SQLExprTableSource(table));
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            return stmt;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    @Override
    public SQLIfStatement parseIf() {
        this.accept(Token.IF);
        final SQLIfStatement stmt = new SQLIfStatement();
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.THEN);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        while (this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            if (this.lexer.token() != Token.IF) {
                final SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
                this.parseStatementList(elseItem.getStatements(), -1, elseItem);
                stmt.setElseItem(elseItem);
                break;
            }
            this.lexer.nextToken();
            final SQLIfStatement.ElseIf elseIf = new SQLIfStatement.ElseIf();
            elseIf.setCondition(this.exprParser.expr());
            elseIf.setParent(stmt);
            this.accept(Token.THEN);
            this.parseStatementList(elseIf.getStatements(), -1, elseIf);
            stmt.getElseIfList().add(elseIf);
        }
        this.accept(Token.END);
        this.accept(Token.IF);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public SQLWhileStatement parseWhile() {
        this.accept(Token.WHILE);
        final SQLWhileStatement stmt = new SQLWhileStatement();
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.DO);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.END);
        this.accept(Token.WHILE);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    public SQLWhileStatement parseWhile(final String label) {
        this.accept(Token.WHILE);
        final SQLWhileStatement stmt = new SQLWhileStatement();
        stmt.setLabelName(label);
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.DO);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.END);
        this.accept(Token.WHILE);
        this.acceptIdentifier(label);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public MySqlCaseStatement parseCase() {
        final MySqlCaseStatement stmt = new MySqlCaseStatement();
        this.accept(Token.CASE);
        if (this.lexer.token() == Token.WHEN) {
            while (this.lexer.token() == Token.WHEN) {
                final MySqlCaseStatement.MySqlWhenStatement when = new MySqlCaseStatement.MySqlWhenStatement();
                when.setCondition(this.exprParser.expr());
                this.accept(Token.THEN);
                this.parseStatementList(when.getStatements(), -1, when);
                stmt.addWhenStatement(when);
            }
            if (this.lexer.token() == Token.ELSE) {
                final SQLIfStatement.Else elseStmt = new SQLIfStatement.Else();
                this.parseStatementList(elseStmt.getStatements(), -1, elseStmt);
                stmt.setElseItem(elseStmt);
            }
        }
        else {
            stmt.setCondition(this.exprParser.expr());
            while (this.lexer.token() == Token.WHEN) {
                this.accept(Token.WHEN);
                final MySqlCaseStatement.MySqlWhenStatement when = new MySqlCaseStatement.MySqlWhenStatement();
                when.setCondition(this.exprParser.expr());
                this.accept(Token.THEN);
                this.parseStatementList(when.getStatements(), -1, when);
                stmt.addWhenStatement(when);
            }
            if (this.lexer.token() == Token.ELSE) {
                this.accept(Token.ELSE);
                final SQLIfStatement.Else elseStmt = new SQLIfStatement.Else();
                this.parseStatementList(elseStmt.getStatements(), -1, elseStmt);
                stmt.setElseItem(elseStmt);
            }
        }
        this.accept(Token.END);
        this.accept(Token.CASE);
        this.accept(Token.SEMI);
        return stmt;
    }
    
    @Override
    public SQLStatement parseDeclare() {
        final char markChar = this.lexer.current();
        final int markBp = this.lexer.bp();
        this.lexer.nextToken();
        if (this.lexer.token() == Token.CONTINUE) {
            this.lexer.reset(markBp, markChar, Token.DECLARE);
            return this.parseDeclareHandler();
        }
        this.lexer.nextToken();
        if (this.lexer.token() == Token.CURSOR) {
            this.lexer.reset(markBp, markChar, Token.DECLARE);
            return this.parseCursorDeclare();
        }
        if (this.lexer.identifierEquals("HANDLER")) {
            this.lexer.reset(markBp, markChar, Token.DECLARE);
            return this.parseDeclareHandler();
        }
        if (this.lexer.token() == Token.CONDITION) {
            this.lexer.reset(markBp, markChar, Token.DECLARE);
            return this.parseDeclareCondition();
        }
        this.lexer.reset(markBp, markChar, Token.DECLARE);
        final MySqlDeclareStatement stmt = new MySqlDeclareStatement();
        this.accept(Token.DECLARE);
        SQLDeclareItem item;
        while (true) {
            item = new SQLDeclareItem();
            item.setName(this.exprParser.name());
            stmt.addVar(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
            stmt.setAfterSemi(true);
        }
        if (this.lexer.token() != Token.EOF) {
            item.setDataType(this.exprParser.parseDataType());
            if (this.lexer.token() == Token.DEFAULT) {
                this.lexer.nextToken();
                final SQLExpr defaultValue = this.exprParser.primary();
                item.setValue(defaultValue);
            }
            return stmt;
        }
        throw new ParserException("TODO. " + this.lexer.info());
    }
    
    public SQLSetStatement parseAssign() {
        this.accept(Token.SET);
        final SQLSetStatement stmt = new SQLSetStatement(this.getDbType());
        this.parseAssignItems(stmt.getItems(), stmt);
        return stmt;
    }
    
    public MySqlSelectIntoStatement parseSelectInto() {
        final MySqlSelectIntoParser parse = new MySqlSelectIntoParser(this.exprParser);
        return parse.parseSelectInto();
    }
    
    public SQLLoopStatement parseLoop() {
        final SQLLoopStatement loopStmt = new SQLLoopStatement();
        this.accept(Token.LOOP);
        this.parseStatementList(loopStmt.getStatements(), -1, loopStmt);
        this.accept(Token.END);
        this.accept(Token.LOOP);
        this.accept(Token.SEMI);
        loopStmt.setAfterSemi(true);
        return loopStmt;
    }
    
    public SQLLoopStatement parseLoop(final String label) {
        final SQLLoopStatement loopStmt = new SQLLoopStatement();
        loopStmt.setLabelName(label);
        this.accept(Token.LOOP);
        this.parseStatementList(loopStmt.getStatements(), -1, loopStmt);
        this.accept(Token.END);
        this.accept(Token.LOOP);
        if (this.lexer.token() != Token.SEMI) {
            this.acceptIdentifier(label);
        }
        this.accept(Token.SEMI);
        loopStmt.setAfterSemi(true);
        return loopStmt;
    }
    
    public SQLBlockStatement parseBlock(final String label) {
        final SQLBlockStatement block = new SQLBlockStatement();
        block.setLabelName(label);
        this.accept(Token.BEGIN);
        this.parseStatementList(block.getStatementList(), -1, block);
        this.accept(Token.END);
        this.acceptIdentifier(label);
        return block;
    }
    
    @Override
    public MySqlLeaveStatement parseLeave() {
        this.accept(Token.LEAVE);
        final MySqlLeaveStatement leaveStmt = new MySqlLeaveStatement();
        leaveStmt.setLabelName(this.exprParser.name().getSimpleName());
        this.accept(Token.SEMI);
        return leaveStmt;
    }
    
    public MySqlIterateStatement parseIterate() {
        this.accept(Token.ITERATE);
        final MySqlIterateStatement iterateStmt = new MySqlIterateStatement();
        iterateStmt.setLabelName(this.exprParser.name().getSimpleName());
        this.accept(Token.SEMI);
        return iterateStmt;
    }
    
    @Override
    public MySqlRepeatStatement parseRepeat() {
        final MySqlRepeatStatement stmt = new MySqlRepeatStatement();
        this.accept(Token.REPEAT);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.UNTIL);
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.END);
        this.accept(Token.REPEAT);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    public MySqlRepeatStatement parseRepeat(final String label) {
        final MySqlRepeatStatement repeatStmt = new MySqlRepeatStatement();
        repeatStmt.setLabelName(label);
        this.accept(Token.REPEAT);
        this.parseStatementList(repeatStmt.getStatements(), -1, repeatStmt);
        this.accept(Token.UNTIL);
        repeatStmt.setCondition(this.exprParser.expr());
        this.accept(Token.END);
        this.accept(Token.REPEAT);
        this.acceptIdentifier(label);
        this.accept(Token.SEMI);
        return repeatStmt;
    }
    
    public MySqlCursorDeclareStatement parseCursorDeclare() {
        final MySqlCursorDeclareStatement stmt = new MySqlCursorDeclareStatement();
        this.accept(Token.DECLARE);
        stmt.setCursorName(this.exprParser.name());
        this.accept(Token.CURSOR);
        this.accept(Token.FOR);
        final SQLSelect select = this.createSQLSelectParser().select();
        stmt.setSelect(select);
        this.accept(Token.SEMI);
        return stmt;
    }
    
    public SQLStatement parseSpStatement() {
        if (this.lexer.token() == Token.UPDATE) {
            return this.parseUpdateStatement();
        }
        if (this.lexer.token() == Token.CREATE) {
            return this.parseCreate();
        }
        if (this.lexer.token() == Token.INSERT) {
            return this.parseInsert();
        }
        if (this.lexer.token() == Token.DELETE) {
            return this.parseDeleteStatement();
        }
        if (this.lexer.token() == Token.BEGIN) {
            return this.parseBlock();
        }
        if (this.lexer.token() == Token.LPAREN) {
            final char ch = this.lexer.current();
            final int bp = this.lexer.bp();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                this.lexer.reset(bp, ch, Token.LPAREN);
                return this.parseSelect();
            }
            throw new ParserException("TODO. " + this.lexer.info());
        }
        else {
            if (this.lexer.token() == Token.SET) {
                return this.parseAssign();
            }
            throw new ParserException("error sp_statement. " + this.lexer.info());
        }
    }
    
    public MySqlDeclareHandlerStatement parseDeclareHandler() {
        final MySqlDeclareHandlerStatement stmt = new MySqlDeclareHandlerStatement();
        this.accept(Token.DECLARE);
        if (this.lexer.token() == Token.CONTINUE) {
            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        }
        else if (this.lexer.token() == Token.EXIT) {
            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        }
        else {
            if (this.lexer.token() != Token.UNDO) {
                throw new ParserException("unkown handle type. " + this.lexer.info());
            }
            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        }
        this.lexer.nextToken();
        this.acceptIdentifier("HANDLER");
        this.accept(Token.FOR);
        while (true) {
            final String tokenName = this.lexer.stringVal();
            final ConditionValue condition = new ConditionValue();
            if (tokenName.equalsIgnoreCase("NOT")) {
                this.lexer.nextToken();
                this.acceptIdentifier("FOUND");
                condition.setType(ConditionValue.ConditionType.SYSTEM);
                condition.setValue("NOT FOUND");
            }
            else if (tokenName.equalsIgnoreCase("SQLSTATE")) {
                condition.setType(ConditionValue.ConditionType.SQLSTATE);
                this.lexer.nextToken();
                condition.setValue(this.exprParser.name().toString());
            }
            else if (this.lexer.identifierEquals("SQLEXCEPTION")) {
                condition.setType(ConditionValue.ConditionType.SYSTEM);
                condition.setValue(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("SQLWARNING")) {
                condition.setType(ConditionValue.ConditionType.SYSTEM);
                condition.setValue(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token() == Token.LITERAL_INT) {
                    condition.setType(ConditionValue.ConditionType.MYSQL_ERROR_CODE);
                    condition.setValue(this.lexer.integerValue().toString());
                }
                else {
                    condition.setType(ConditionValue.ConditionType.SELF);
                    condition.setValue(tokenName);
                }
                this.lexer.nextToken();
            }
            stmt.getConditionValues().add(condition);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.accept(Token.COMMA);
        }
        if (this.lexer.token() != Token.EOF) {
            stmt.setSpStatement(this.parseSpStatement());
            if (!(stmt.getSpStatement() instanceof SQLBlockStatement)) {
                this.accept(Token.SEMI);
            }
            return stmt;
        }
        throw new ParserException("declare handle not eof");
    }
    
    public MySqlDeclareConditionStatement parseDeclareCondition() {
        final MySqlDeclareConditionStatement stmt = new MySqlDeclareConditionStatement();
        this.accept(Token.DECLARE);
        stmt.setConditionName(this.exprParser.name().toString());
        this.accept(Token.CONDITION);
        this.accept(Token.FOR);
        final String tokenName = this.lexer.stringVal();
        final ConditionValue condition = new ConditionValue();
        if (tokenName.equalsIgnoreCase("SQLSTATE")) {
            condition.setType(ConditionValue.ConditionType.SQLSTATE);
            this.lexer.nextToken();
            condition.setValue(this.exprParser.name().toString());
        }
        else {
            if (this.lexer.token() != Token.LITERAL_INT) {
                throw new ParserException("declare condition grammer error. " + this.lexer.info());
            }
            condition.setType(ConditionValue.ConditionType.MYSQL_ERROR_CODE);
            condition.setValue(this.lexer.integerValue().toString());
            this.lexer.nextToken();
        }
        stmt.setConditionValue(condition);
        this.accept(Token.SEMI);
        return stmt;
    }
    
    @Override
    public SQLStatement parseFlashback() {
        final MySqlFlashbackStatement stmt = new MySqlFlashbackStatement();
        this.acceptIdentifier("FLASHBACK");
        this.accept(Token.TABLE);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.accept(Token.TO);
        this.acceptIdentifier("BEFORE");
        this.accept(Token.DROP);
        if (this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            final SQLName to = this.exprParser.name();
            stmt.setRenameTo(to);
        }
        return stmt;
    }
    
    public Timestamp getCurrentTimestamp() {
        return this.now;
    }
    
    public Date getCurrentDate() {
        return this.currentDate;
    }
    
    @Override
    public MySqlCreateTableParser getSQLCreateTableParser() {
        return new MySqlCreateTableParser(this.exprParser);
    }
    
    @Override
    public SQLStatement parseCopy() {
        this.acceptIdentifier("COPY");
        final SQLCopyFromStatement stmt = new SQLCopyFromStatement();
        final SQLExpr table = this.exprParser.name();
        stmt.setTable(new SQLExprTableSource(table));
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.names(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getPartitions(), stmt);
        }
        this.accept(Token.FROM);
        final SQLExpr from = this.exprParser.expr();
        stmt.setFrom(from);
        if (this.lexer.identifierEquals(FnvHash.Constants.CREDENTIALS)) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.identifierEquals(FnvHash.Constants.ACCESS_KEY_ID)) {
                    this.lexer.nextToken();
                    final SQLExpr accessKeyId = this.exprParser.primary();
                    stmt.setAccessKeyId(accessKeyId);
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.ACCESS_KEY_SECRET)) {
                        break;
                    }
                    this.lexer.nextToken();
                    final SQLExpr accessKeySecret = this.exprParser.primary();
                    stmt.setAccessKeySecret(accessKeySecret);
                }
            }
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLName name = this.exprParser.name();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr value = this.exprParser.expr();
                final SQLAssignItem item = new SQLAssignItem(name, value);
                item.setParent(stmt);
                stmt.getOptions().add(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return stmt;
    }
}
