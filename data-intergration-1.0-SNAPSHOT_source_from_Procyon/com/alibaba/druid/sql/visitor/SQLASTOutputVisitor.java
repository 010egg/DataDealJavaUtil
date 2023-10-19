// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.ast.statement.SQLCloneTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTemporaryOutputStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsNewExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLAlterResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLCreateResourceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLBackupStatement;
import com.alibaba.druid.sql.ast.statement.SQLArchiveTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLRestoreStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubmitJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLRenameUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLBuildTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLValuesQuery;
import com.alibaba.druid.sql.ast.statement.SQLTableLike;
import com.alibaba.druid.sql.ast.statement.SQLSyncMetaStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowUsersStatement;
import com.alibaba.druid.sql.ast.statement.SQLCopyFromStatement;
import com.alibaba.druid.sql.ast.statement.SQLForStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhoamiStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLAdhocTableSource;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowGrantsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPackagesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticListStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.ast.expr.SQLDbLinkExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddSupplemental;
import com.alibaba.druid.sql.ast.statement.SQLAlterOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeLogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeRecyclebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowOutlinesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowQueryTaskStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableUnarchivePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableArchivePartition;
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.ast.statement.SQLImportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPartitionRef;
import com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowIndexesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRecoverPartitions;
import com.alibaba.druid.sql.ast.SQLAnnIndex;
import com.alibaba.druid.sql.ast.expr.SQLJSONExpr;
import com.alibaba.druid.sql.ast.expr.SQLExtractExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLDumpStatement;
import com.alibaba.druid.sql.ast.expr.SQLValuesExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.ast.expr.SQLMatchAgainstExpr;
import com.alibaba.druid.sql.ast.statement.SQLDropRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateRoleStatement;
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
import com.alibaba.druid.sql.ast.SQLPartitionSpec;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableMergePartition;
import com.alibaba.druid.sql.ast.SQLUnionDataType;
import com.alibaba.druid.sql.ast.SQLRowDataType;
import com.alibaba.druid.sql.ast.SQLStructDataType;
import com.alibaba.druid.sql.ast.SQLMapDataType;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowSessionStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowFunctionsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCatalogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRecylebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowErrorsStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLRefreshMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropMaterializedViewStatement;
import com.alibaba.druid.sql.ast.expr.SQLFlashbackExpr;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.SQLArgument;
import com.alibaba.druid.sql.ast.statement.SQLReturnStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeclareStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhileStatement;
import com.alibaba.druid.sql.ast.statement.SQLDescribeStatement;
import com.alibaba.druid.sql.ast.expr.SQLBigIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLTinyIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLSmallIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLFloatExpr;
import com.alibaba.druid.sql.ast.expr.SQLDoubleExpr;
import com.alibaba.druid.sql.ast.expr.SQLDecimalExpr;
import com.alibaba.druid.sql.ast.expr.SQLRealExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateTimeExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.alibaba.druid.sql.ast.statement.SQLAlterCharacter;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseKillJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseSetOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.alibaba.druid.sql.ast.SQLSubPartitionByList;
import com.alibaba.druid.sql.ast.SQLSubPartitionByRange;
import com.alibaba.druid.sql.ast.SQLSubPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionByValue;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLRecordDataType;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleProcedureDataType;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleFunctionDataType;
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloseStatement;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTouch;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionSetProperties;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLocation;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetComment;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropExtPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddExtPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewRenameStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLIndexOptions;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropCatalogStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLRevokeStatement;
import com.alibaba.druid.sql.ast.statement.SQLObjectType;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainAnalyzeStatement;
import com.alibaba.druid.sql.ast.statement.SQLCancelJobStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLColumnReference;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprHint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableBlockSize;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionCount;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLDefault;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableModifyClusteredBy;
import com.alibaba.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.alibaba.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemSetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSystemGetConfigStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLReleaseSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSynonymStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropServerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropLogFileGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDeleteByCondition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReplaceColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import com.alibaba.druid.sql.ast.expr.SQLDefaultExpr;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnnestTableSource;
import com.alibaba.druid.sql.ast.expr.SQLAllExpr;
import com.alibaba.druid.sql.ast.expr.SQLAnyExpr;
import com.alibaba.druid.sql.ast.expr.SQLSomeExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLNotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.AutoIncrementType;
import com.alibaba.druid.sql.ast.statement.SQLColumnCheck;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLListResourceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropEventStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Blob;
import java.io.Reader;
import java.io.InputStream;
import java.time.temporal.Temporal;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSampling;
import java.util.LinkedHashSet;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLOpenStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.SQLKeep;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.util.FnvHash;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.expr.SQLContainsExpr;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCaseStatement;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.math.BigInteger;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLExprUtils;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLNotExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Map;
import com.alibaba.druid.DbType;
import java.util.Set;
import java.util.List;

public class SQLASTOutputVisitor extends SQLASTVisitorAdapter implements ParameterizedVisitor, PrintableVisitor
{
    public static Boolean defaultPrintStatementAfterSemi;
    protected final Appendable appender;
    protected int indentCount;
    protected boolean ucase;
    protected int selectListNumberOfLine;
    protected boolean groupItemSingleLine;
    protected List<Object> parameters;
    protected List<Object> inputParameters;
    protected Set<String> tables;
    protected String table;
    protected boolean exportTables;
    protected DbType dbType;
    protected Map<String, String> tableMapping;
    protected int replaceCount;
    protected boolean parameterizedMergeInList;
    protected boolean parameterizedQuesUnMergeInList;
    protected boolean parameterizedQuesUnMergeValuesList;
    protected boolean printNameQuote;
    protected char quote;
    protected boolean parameterized;
    protected boolean shardingSupport;
    protected transient int lines;
    private TimeZone timeZone;
    protected Boolean printStatementAfterSemi;
    private static final Integer ONE;
    static String[] variantValuesCache_1;
    static String[] variantValuesCache;
    
    public SQLASTOutputVisitor(final Appendable appender) {
        this.indentCount = 0;
        this.ucase = true;
        this.selectListNumberOfLine = 5;
        this.groupItemSingleLine = false;
        this.exportTables = false;
        this.parameterizedMergeInList = false;
        this.parameterizedQuesUnMergeInList = false;
        this.parameterizedQuesUnMergeValuesList = false;
        this.printNameQuote = false;
        this.quote = '\"';
        this.parameterized = false;
        this.shardingSupport = false;
        this.lines = 0;
        this.printStatementAfterSemi = SQLASTOutputVisitor.defaultPrintStatementAfterSemi;
        this.features |= VisitorFeature.OutputPrettyFormat.mask;
        this.appender = appender;
    }
    
    public SQLASTOutputVisitor(final Appendable appender, final DbType dbType) {
        this.indentCount = 0;
        this.ucase = true;
        this.selectListNumberOfLine = 5;
        this.groupItemSingleLine = false;
        this.exportTables = false;
        this.parameterizedMergeInList = false;
        this.parameterizedQuesUnMergeInList = false;
        this.parameterizedQuesUnMergeValuesList = false;
        this.printNameQuote = false;
        this.quote = '\"';
        this.parameterized = false;
        this.shardingSupport = false;
        this.lines = 0;
        this.printStatementAfterSemi = SQLASTOutputVisitor.defaultPrintStatementAfterSemi;
        this.features |= VisitorFeature.OutputPrettyFormat.mask;
        this.appender = appender;
        this.dbType = dbType;
    }
    
    public SQLASTOutputVisitor(final Appendable appender, final boolean parameterized) {
        this.indentCount = 0;
        this.ucase = true;
        this.selectListNumberOfLine = 5;
        this.groupItemSingleLine = false;
        this.exportTables = false;
        this.parameterizedMergeInList = false;
        this.parameterizedQuesUnMergeInList = false;
        this.parameterizedQuesUnMergeValuesList = false;
        this.printNameQuote = false;
        this.quote = '\"';
        this.parameterized = false;
        this.shardingSupport = false;
        this.lines = 0;
        this.printStatementAfterSemi = SQLASTOutputVisitor.defaultPrintStatementAfterSemi;
        this.features |= VisitorFeature.OutputPrettyFormat.mask;
        this.appender = appender;
        this.config(VisitorFeature.OutputParameterized, parameterized);
    }
    
    @Override
    public int getReplaceCount() {
        return this.replaceCount;
    }
    
    @Override
    public void incrementReplaceCunt() {
        ++this.replaceCount;
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    public void addTableMapping(String srcTable, final String destTable) {
        if (this.tableMapping == null) {
            this.tableMapping = new HashMap<String, String>();
        }
        if (srcTable.indexOf(46) >= 0) {
            final SQLExpr expr = SQLUtils.toSQLExpr(srcTable, this.dbType);
            if (expr instanceof SQLPropertyExpr) {
                srcTable = ((SQLPropertyExpr)expr).simplify().toString();
            }
        }
        else {
            srcTable = SQLUtils.normalize(srcTable);
        }
        this.tableMapping.put(srcTable, destTable);
    }
    
    public void setTableMapping(final Map<String, String> tableMapping) {
        this.tableMapping = tableMapping;
    }
    
    public List<Object> getParameters() {
        if (this.parameters == null) {
            this.parameters = new ArrayList<Object>();
        }
        return this.parameters;
    }
    
    public boolean isDesensitize() {
        return this.isEnabled(VisitorFeature.OutputDesensitize);
    }
    
    public void setDesensitize(final boolean desensitize) {
        this.config(VisitorFeature.OutputDesensitize, desensitize);
    }
    
    public Set<String> getTables() {
        if (this.table != null && this.tables == null) {
            return Collections.singleton(this.table);
        }
        return this.tables;
    }
    
    @Deprecated
    public void setParameters(final List<Object> parameters) {
        if (parameters != null && parameters.size() > 0) {
            this.inputParameters = parameters;
        }
        else {
            this.parameters = parameters;
        }
    }
    
    public void setInputParameters(final List<Object> parameters) {
        this.inputParameters = parameters;
    }
    
    @Override
    public void setOutputParameters(final List<Object> parameters) {
        this.parameters = parameters;
    }
    
    public int getIndentCount() {
        return this.indentCount;
    }
    
    public Appendable getAppender() {
        return this.appender;
    }
    
    public boolean isPrettyFormat() {
        return this.isEnabled(VisitorFeature.OutputPrettyFormat);
    }
    
    public void setPrettyFormat(final boolean prettyFormat) {
        this.config(VisitorFeature.OutputPrettyFormat, prettyFormat);
    }
    
    public void decrementIndent() {
        --this.indentCount;
    }
    
    public void incrementIndent() {
        ++this.indentCount;
    }
    
    public boolean isParameterized() {
        return this.isEnabled(VisitorFeature.OutputParameterized);
    }
    
    public void setParameterized(final boolean parameterized) {
        this.config(VisitorFeature.OutputParameterized, parameterized);
    }
    
    public boolean isParameterizedMergeInList() {
        return this.parameterizedMergeInList;
    }
    
    public void setParameterizedMergeInList(final boolean parameterizedMergeInList) {
        this.parameterizedMergeInList = parameterizedMergeInList;
    }
    
    public boolean isParameterizedQuesUnMergeInList() {
        return this.parameterizedQuesUnMergeInList;
    }
    
    public void setParameterizedQuesUnMergeInList(final boolean parameterizedQuesUnMergeInList) {
        this.config(VisitorFeature.OutputParameterizedQuesUnMergeInList, parameterizedQuesUnMergeInList);
    }
    
    public boolean isExportTables() {
        return this.exportTables;
    }
    
    public void setExportTables(final boolean exportTables) {
        this.exportTables = exportTables;
    }
    
    @Override
    public void print(final char value) {
        if (this.appender == null) {
            return;
        }
        try {
            this.appender.append(value);
        }
        catch (IOException e) {
            throw new RuntimeException("print error", e);
        }
    }
    
    public void print(final int value) {
        if (this.appender == null) {
            return;
        }
        if (this.appender instanceof StringBuffer) {
            ((StringBuffer)this.appender).append(value);
        }
        else if (this.appender instanceof StringBuilder) {
            ((StringBuilder)this.appender).append(value);
        }
        else {
            this.print0(Integer.toString(value));
        }
    }
    
    public void print(final long value) {
        if (this.appender == null) {
            return;
        }
        if (this.appender instanceof StringBuilder) {
            ((StringBuilder)this.appender).append(value);
        }
        else if (this.appender instanceof StringBuffer) {
            ((StringBuffer)this.appender).append(value);
        }
        else {
            this.print0(Long.toString(value));
        }
    }
    
    public void print(final float value) {
        if (this.appender == null) {
            return;
        }
        if (this.appender instanceof StringBuilder) {
            ((StringBuilder)this.appender).append(value);
        }
        else if (this.appender instanceof StringBuffer) {
            ((StringBuffer)this.appender).append(value);
        }
        else {
            this.print0(Float.toString(value));
        }
    }
    
    public void print(final double value) {
        if (this.appender == null) {
            return;
        }
        if (this.appender instanceof StringBuilder) {
            ((StringBuilder)this.appender).append(value);
        }
        else if (this.appender instanceof StringBuffer) {
            ((StringBuffer)this.appender).append(value);
        }
        else {
            this.print0(Double.toString(value));
        }
    }
    
    public void print(final Date date) {
        if (this.appender == null) {
            return;
        }
        SimpleDateFormat dateFormat;
        if (date instanceof java.sql.Date) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.print0("DATE ");
        }
        else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            this.print0("TIMESTAMP ");
        }
        if (this.timeZone != null) {
            dateFormat.setTimeZone(this.timeZone);
        }
        this.print0("'" + dateFormat.format(date) + "'");
    }
    
    @Override
    public void print(final String text) {
        if (this.appender == null) {
            return;
        }
        this.print0(text);
    }
    
    protected void print0(final String text) {
        if (this.appender == null) {
            return;
        }
        try {
            this.appender.append(text);
        }
        catch (IOException e) {
            throw new RuntimeException("println error", e);
        }
    }
    
    protected void printUcase(final String text) {
        this.print0(this.ucase ? text.toUpperCase() : text.toLowerCase());
    }
    
    protected void printName0(final String text) {
        if (this.appender == null || text.length() == 0) {
            return;
        }
        try {
            if (this.printNameQuote) {
                final char c0 = text.charAt(0);
                if (c0 == this.quote) {
                    this.appender.append(text);
                }
                else if (c0 == '\"' && text.charAt(text.length() - 1) == '\"') {
                    this.appender.append(this.quote);
                    this.appender.append(text.substring(1, text.length() - 1));
                    this.appender.append(this.quote);
                }
                else if (c0 == '`' && text.charAt(text.length() - 1) == '`') {
                    this.appender.append(this.quote);
                    this.appender.append(text.substring(1, text.length() - 1));
                    this.appender.append(this.quote);
                }
                else {
                    this.appender.append(this.quote);
                    this.appender.append(text);
                    this.appender.append(this.quote);
                }
            }
            else {
                this.appender.append(text);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("println error", e);
        }
    }
    
    protected void printAlias(final String alias) {
        if (alias == null || alias.length() == 0) {
            return;
        }
        this.print(' ');
        try {
            this.appender.append(alias);
        }
        catch (IOException e) {
            throw new RuntimeException("println error", e);
        }
    }
    
    protected void printAndAccept(final List<? extends SQLObject> nodes, final String seperator) {
        for (int i = 0, size = nodes.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(seperator);
            }
            ((SQLObject)nodes.get(i)).accept(this);
        }
    }
    
    protected void printAndAccept(final List<? extends SQLExpr> nodes, final String seperator, final boolean parameterized) {
        for (int i = 0, size = nodes.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(seperator);
            }
            this.printExpr((SQLExpr)nodes.get(i), parameterized);
        }
    }
    
    private static int paramCount(final SQLExpr x) {
        if (x instanceof SQLName) {
            return 1;
        }
        if (x instanceof SQLAggregateExpr) {
            final SQLAggregateExpr aggregateExpr = (SQLAggregateExpr)x;
            final List<SQLExpr> args = aggregateExpr.getArguments();
            int paramCount = 1;
            for (final SQLExpr arg : args) {
                paramCount += paramCount(arg);
            }
            if (aggregateExpr.getOver() != null) {
                ++paramCount;
            }
            return paramCount;
        }
        if (x instanceof SQLMethodInvokeExpr) {
            final List<SQLExpr> params = ((SQLMethodInvokeExpr)x).getArguments();
            int paramCount2 = 1;
            for (final SQLExpr param : params) {
                paramCount2 += paramCount(param);
            }
            return paramCount2;
        }
        if (x instanceof SQLBinaryOpExpr) {
            return paramCount(((SQLBinaryOpExpr)x).getLeft()) + paramCount(((SQLBinaryOpExpr)x).getRight());
        }
        if (x instanceof SQLCaseExpr) {
            return 10;
        }
        return 1;
    }
    
    protected void printSelectList(final List<SQLSelectItem> selectList) {
        ++this.indentCount;
        for (int i = 0, lineItemCount = 0, size = selectList.size(); i < size; ++i, ++lineItemCount) {
            final SQLSelectItem selectItem = selectList.get(i);
            final SQLExpr selectItemExpr = selectItem.getExpr();
            final int paramCount = paramCount(selectItemExpr);
            final boolean methodOrBinary = !(selectItemExpr instanceof SQLName) && (selectItemExpr instanceof SQLMethodInvokeExpr || selectItemExpr instanceof SQLAggregateExpr || selectItemExpr instanceof SQLBinaryOpExpr);
            if (methodOrBinary) {
                lineItemCount += paramCount - 1;
            }
            if (i != 0) {
                final SQLSelectItem preSelectItem = selectList.get(i - 1);
                if (preSelectItem.getAfterCommentsDirect() != null) {
                    lineItemCount = 0;
                    this.println();
                }
                else if (methodOrBinary) {
                    if (lineItemCount >= this.selectListNumberOfLine) {
                        lineItemCount = paramCount;
                        this.println();
                    }
                }
                else if (lineItemCount >= this.selectListNumberOfLine || selectItemExpr instanceof SQLQueryExpr || selectItemExpr instanceof SQLCaseExpr || (selectItemExpr instanceof SQLCharExpr && ((SQLCharExpr)selectItemExpr).getText().length() > 20)) {
                    lineItemCount = 0;
                    this.println();
                }
                this.print0(", ");
            }
            if (selectItem.getClass() == SQLSelectItem.class) {
                this.visit(selectItem);
            }
            else {
                selectItem.accept(this);
            }
            if (selectItem.hasAfterComment()) {
                this.print(' ');
                this.printlnComment(selectItem.getAfterCommentsDirect());
            }
        }
        --this.indentCount;
    }
    
    protected void printlnAndAccept(final List<? extends SQLObject> nodes, final String seperator) {
        for (int i = 0, size = nodes.size(); i < size; ++i) {
            if (i != 0) {
                this.println(seperator);
            }
            ((SQLObject)nodes.get(i)).accept(this);
        }
    }
    
    protected void printIndent() {
        if (this.appender == null) {
            return;
        }
        try {
            for (int i = 0; i < this.indentCount; ++i) {
                this.appender.append('\t');
            }
        }
        catch (IOException e) {
            throw new RuntimeException("print error", e);
        }
    }
    
    public void println() {
        if (!this.isPrettyFormat()) {
            this.print(' ');
            return;
        }
        this.print('\n');
        ++this.lines;
        this.printIndent();
    }
    
    public void println(final String text) {
        this.print(text);
        this.println();
    }
    
    @Override
    public boolean visit(final SQLBetweenExpr x) {
        final SQLExpr testExpr = x.getTestExpr();
        final SQLExpr beginExpr = x.getBeginExpr();
        final SQLExpr endExpr = x.getEndExpr();
        boolean quote = false;
        if (testExpr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOperator operator = ((SQLBinaryOpExpr)testExpr).getOperator();
            switch (operator) {
                case BooleanAnd:
                case BooleanOr:
                case BooleanXor:
                case Assignment: {
                    quote = true;
                    break;
                }
                default: {
                    quote = ((SQLBinaryOpExpr)testExpr).isParenthesized();
                    break;
                }
            }
        }
        else if (testExpr instanceof SQLInListExpr || testExpr instanceof SQLBetweenExpr || testExpr instanceof SQLNotExpr || testExpr instanceof SQLUnaryExpr || testExpr instanceof SQLCaseExpr || testExpr instanceof SQLBinaryOpExprGroup) {
            quote = true;
        }
        if (testExpr != null) {
            if (quote) {
                this.print('(');
                this.printExpr(testExpr, this.parameterized);
                this.print(')');
            }
            else {
                this.printExpr(testExpr, this.parameterized);
            }
        }
        if (x.isNot()) {
            this.print0(this.ucase ? " NOT BETWEEN " : " not between ");
        }
        else {
            this.print0(this.ucase ? " BETWEEN " : " between ");
        }
        final int lines = this.lines;
        if (beginExpr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpBegin = (SQLBinaryOpExpr)beginExpr;
            this.incrementIndent();
            if (binaryOpBegin.isParenthesized() || binaryOpBegin.getOperator().isLogical() || binaryOpBegin.getOperator().isRelational()) {
                this.print('(');
                this.printExpr(beginExpr, this.parameterized);
                this.print(')');
            }
            else {
                this.printExpr(beginExpr, this.parameterized);
            }
            this.decrementIndent();
        }
        else if (beginExpr instanceof SQLInListExpr || beginExpr instanceof SQLBetweenExpr || beginExpr instanceof SQLNotExpr || beginExpr instanceof SQLUnaryExpr || beginExpr instanceof SQLCaseExpr || beginExpr instanceof SQLBinaryOpExprGroup) {
            this.print('(');
            this.printExpr(beginExpr, this.parameterized);
            this.print(')');
        }
        else {
            this.printExpr(beginExpr, this.parameterized);
        }
        if (lines != this.lines) {
            this.println();
            this.print0(this.ucase ? "AND " : "and ");
        }
        else {
            this.print0(this.ucase ? " AND " : " and ");
        }
        if (endExpr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpEnd = (SQLBinaryOpExpr)endExpr;
            this.incrementIndent();
            if (binaryOpEnd.isParenthesized() || binaryOpEnd.getOperator().isLogical() || binaryOpEnd.getOperator().isRelational()) {
                this.print('(');
                this.printExpr(endExpr, this.parameterized);
                this.print(')');
            }
            else {
                this.printExpr(endExpr, this.parameterized);
            }
            this.decrementIndent();
        }
        else if (endExpr instanceof SQLInListExpr || endExpr instanceof SQLBetweenExpr || endExpr instanceof SQLNotExpr || endExpr instanceof SQLUnaryExpr || endExpr instanceof SQLCaseExpr || endExpr instanceof SQLBinaryOpExprGroup) {
            this.print('(');
            this.printExpr(endExpr, this.parameterized);
            this.print(')');
        }
        else {
            this.printExpr(endExpr, this.parameterized);
        }
        if (x.getHint() != null) {
            x.getHint().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExprGroup x) {
        final SQLObject parent = x.getParent();
        final SQLBinaryOperator operator = x.getOperator();
        final boolean isRoot = parent instanceof SQLSelectQueryBlock || parent instanceof SQLBinaryOpExprGroup;
        final List<SQLExpr> items = x.getItems();
        if (items.size() == 0) {
            this.print("true");
            return false;
        }
        if (isRoot) {
            ++this.indentCount;
        }
        if (this.parameterized) {
            SQLExpr firstLeft = null;
            SQLBinaryOperator firstOp = null;
            final List<Object> parameters = new ArrayList<Object>(items.size());
            List<SQLBinaryOpExpr> literalItems = null;
            if ((operator != SQLBinaryOperator.BooleanOr || !this.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeOr)) && (operator != SQLBinaryOperator.BooleanAnd || !this.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeAnd))) {
                for (int i = 0; i < items.size(); ++i) {
                    final SQLExpr item = items.get(i);
                    if (!(item instanceof SQLBinaryOpExpr)) {
                        firstLeft = null;
                        break;
                    }
                    final SQLBinaryOpExpr binaryItem = (SQLBinaryOpExpr)item;
                    final SQLExpr left = binaryItem.getLeft();
                    final SQLExpr right = binaryItem.getRight();
                    if (right instanceof SQLLiteralExpr && !(right instanceof SQLNullExpr)) {
                        if (left instanceof SQLLiteralExpr) {
                            if (literalItems == null) {
                                literalItems = new ArrayList<SQLBinaryOpExpr>();
                            }
                            literalItems.add(binaryItem);
                            continue;
                        }
                        if (this.parameters != null) {
                            ExportParameterVisitorUtils.exportParameter(parameters, right);
                        }
                    }
                    else if (!(right instanceof SQLVariantRefExpr)) {
                        firstLeft = null;
                        break;
                    }
                    if (firstLeft == null) {
                        firstLeft = binaryItem.getLeft();
                        firstOp = binaryItem.getOperator();
                    }
                    else if (firstOp != binaryItem.getOperator() || !SQLExprUtils.equals(firstLeft, left)) {
                        firstLeft = null;
                        break;
                    }
                }
            }
            if (firstLeft != null) {
                if (literalItems != null) {
                    for (final SQLBinaryOpExpr literalItem : literalItems) {
                        this.visit(literalItem);
                        this.println();
                        this.printOperator(operator);
                        this.print(' ');
                    }
                }
                this.printExpr(firstLeft, this.parameterized);
                this.print(' ');
                this.printOperator(firstOp);
                this.print0(" ?");
                if (this.parameters != null && parameters.size() > 0) {
                    this.parameters.addAll(parameters);
                }
                this.incrementReplaceCunt();
                if (isRoot) {
                    --this.indentCount;
                }
                return false;
            }
        }
        for (int j = 0; j < items.size(); ++j) {
            final SQLExpr item2 = items.get(j);
            if (j != 0) {
                this.println();
                this.printOperator(operator);
                this.print(' ');
            }
            if (item2.hasBeforeComment()) {
                this.printlnComments(item2.getBeforeCommentsDirect());
            }
            if (item2 instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)item2;
                final SQLExpr binaryOpExprRight = binaryOpExpr.getRight();
                final SQLBinaryOperator itemOp = binaryOpExpr.getOperator();
                final boolean isLogic = itemOp.isLogical();
                if (isLogic) {
                    ++this.indentCount;
                }
                final boolean bracket = itemOp.priority > operator.priority || (binaryOpExpr.isParenthesized() & !this.parameterized);
                if (bracket) {
                    this.print('(');
                    this.visit(binaryOpExpr);
                    this.print(')');
                }
                else {
                    this.visit(binaryOpExpr);
                }
                if (item2.hasAfterComment() && !this.parameterized) {
                    this.print(' ');
                    this.printlnComment(item2.getAfterCommentsDirect());
                }
                if (isLogic) {
                    --this.indentCount;
                }
            }
            else if (item2 instanceof SQLBinaryOpExprGroup) {
                this.print('(');
                this.visit((SQLBinaryOpExprGroup)item2);
                this.print(')');
            }
            else {
                this.printExpr(item2, this.parameterized);
            }
        }
        if (isRoot) {
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(SQLBinaryOpExpr x) {
        SQLBinaryOperator operator = x.getOperator();
        if (this.parameterized && operator == SQLBinaryOperator.BooleanOr && !this.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeOr)) {
            x = SQLBinaryOpExpr.merge(this, x);
            operator = x.getOperator();
        }
        if (this.inputParameters != null && this.inputParameters.size() > 0 && operator == SQLBinaryOperator.Equality && x.getRight() instanceof SQLVariantRefExpr) {
            final SQLVariantRefExpr right = (SQLVariantRefExpr)x.getRight();
            final int index = right.getIndex();
            if (index >= 0 && index < this.inputParameters.size()) {
                final Object param = this.inputParameters.get(index);
                if (param instanceof Collection) {
                    x.getLeft().accept(this);
                    this.print0(" IN (");
                    right.accept(this);
                    this.print(')');
                    return false;
                }
            }
        }
        final SQLObject parent = x.getParent();
        final boolean isRoot = parent instanceof SQLSelectQueryBlock;
        final boolean relational = operator == SQLBinaryOperator.BooleanAnd || operator == SQLBinaryOperator.BooleanOr;
        if (isRoot && relational) {
            ++this.indentCount;
        }
        final List<SQLExpr> groupList = new ArrayList<SQLExpr>();
        SQLExpr left = x.getLeft();
        final SQLExpr right2 = x.getRight();
        if (this.inputParameters != null && operator != SQLBinaryOperator.Equality) {
            int varIndex = -1;
            if (right2 instanceof SQLVariantRefExpr) {
                varIndex = ((SQLVariantRefExpr)right2).getIndex();
            }
            Object param2 = null;
            if (varIndex >= 0 && varIndex < this.inputParameters.size()) {
                param2 = this.inputParameters.get(varIndex);
            }
            if (param2 instanceof Collection) {
                final Collection values = (Collection)param2;
                if (values.size() > 0) {
                    this.print('(');
                    int valIndex = 0;
                    for (final Object value : values) {
                        if (valIndex++ != 0) {
                            this.print0(this.ucase ? " OR " : " or ");
                        }
                        this.printExpr(left, this.parameterized);
                        this.print(' ');
                        if (operator == SQLBinaryOperator.Is) {
                            this.print('=');
                        }
                        else {
                            this.printOperator(operator);
                        }
                        this.print(' ');
                        this.printParameter(value);
                    }
                    this.print(')');
                    return false;
                }
            }
        }
        if (operator.isRelational() && left instanceof SQLIntegerExpr && right2 instanceof SQLIntegerExpr) {
            this.print(((SQLIntegerExpr)left).getNumber().longValue());
            this.print(' ');
            this.printOperator(operator);
            this.print(' ');
            final Number number = ((SQLIntegerExpr)right2).getNumber();
            if (number instanceof BigInteger) {
                this.print0(((BigInteger)number).toString());
            }
            else {
                this.print(number.longValue());
            }
            return false;
        }
        while (left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)left).getOperator() == operator && operator != SQLBinaryOperator.IsNot && operator != SQLBinaryOperator.Is) {
            final SQLBinaryOpExpr binaryLeft = (SQLBinaryOpExpr)left;
            groupList.add(binaryLeft.getRight());
            left = binaryLeft.getLeft();
        }
        groupList.add(left);
        for (int i = groupList.size() - 1; i >= 0; --i) {
            final SQLExpr item = groupList.get(i);
            if (relational && this.isPrettyFormat() && item.hasBeforeComment() && !this.parameterized) {
                this.printlnComments(item.getBeforeCommentsDirect());
            }
            if (this.isPrettyFormat() && item.hasBeforeComment() && !this.parameterized) {
                this.printlnComments(item.getBeforeCommentsDirect());
            }
            this.visitBinaryLeft(item, operator);
            if (this.isPrettyFormat() && item.hasAfterComment()) {
                this.print(' ');
                this.printlnComment(item.getAfterCommentsDirect());
            }
            if (i != groupList.size() - 1 && this.isPrettyFormat() && item.getParent() != null && item.getParent().hasAfterComment() && !this.parameterized) {
                this.print(' ');
                this.printlnComment(item.getParent().getAfterCommentsDirect());
            }
            boolean printOpSpace = true;
            if (relational) {
                this.println();
            }
            else {
                if (operator == SQLBinaryOperator.Modulus && DbType.oracle == this.dbType && left instanceof SQLIdentifierExpr && right2 instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)right2).getName().equalsIgnoreCase("NOTFOUND")) {
                    printOpSpace = false;
                }
                if (printOpSpace) {
                    this.print(' ');
                }
            }
            this.printOperator(operator);
            if (printOpSpace) {
                this.print(' ');
            }
        }
        this.visitorBinaryRight(x);
        if (isRoot && relational) {
            --this.indentCount;
        }
        return false;
    }
    
    protected void printOperator(final SQLBinaryOperator operator) {
        this.print0(this.ucase ? operator.name : operator.name_lcase);
    }
    
    private void visitorBinaryRight(final SQLBinaryOpExpr x) {
        final SQLExpr right = x.getRight();
        final SQLBinaryOperator op = x.getOperator();
        if (this.isPrettyFormat() && right.hasBeforeComment()) {
            this.printlnComments(right.getBeforeCommentsDirect());
        }
        if (right instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryRight = (SQLBinaryOpExpr)right;
            final SQLBinaryOperator rightOp = binaryRight.getOperator();
            final boolean rightRational = rightOp == SQLBinaryOperator.BooleanAnd || rightOp == SQLBinaryOperator.BooleanOr;
            if (rightOp.priority >= op.priority || (binaryRight.isParenthesized() && rightOp != op && rightOp.isLogical() && op.isLogical())) {
                if (rightRational) {
                    ++this.indentCount;
                }
                this.print('(');
                this.printExpr(binaryRight, this.parameterized);
                this.print(')');
                if (rightRational) {
                    --this.indentCount;
                }
            }
            else {
                this.printExpr(binaryRight, this.parameterized);
            }
        }
        else if (right instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)right;
            if (group.getOperator() == x.getOperator()) {
                this.visit(group);
            }
            else {
                ++this.indentCount;
                this.print('(');
                this.visit(group);
                this.print(')');
                --this.indentCount;
            }
        }
        else if (SQLBinaryOperator.Equality.priority >= op.priority && (right instanceof SQLInListExpr || right instanceof SQLBetweenExpr || right instanceof SQLNotExpr)) {
            ++this.indentCount;
            this.print('(');
            this.printExpr(right, this.parameterized);
            this.print(')');
            --this.indentCount;
        }
        else {
            this.printExpr(right, this.parameterized);
        }
        if (right.hasAfterComment() && this.isPrettyFormat()) {
            this.print(' ');
            this.printlnComment(right.getAfterCommentsDirect());
        }
        if (x.getHint() != null) {
            x.getHint().accept(this);
        }
    }
    
    private void visitBinaryLeft(final SQLExpr left, final SQLBinaryOperator op) {
        if (left instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryLeft = (SQLBinaryOpExpr)left;
            final SQLBinaryOperator leftOp = binaryLeft.getOperator();
            final SQLObject parent = left.getParent();
            final boolean leftRational = leftOp == SQLBinaryOperator.BooleanAnd || leftOp == SQLBinaryOperator.BooleanOr;
            final boolean bracket = leftOp.priority > op.priority || (leftOp.priority == op.priority && parent instanceof SQLBinaryOpExpr && parent.getParent() instanceof SQLBinaryOpExpr && leftOp.priority == ((SQLBinaryOpExpr)parent.getParent()).getOperator().priority && left == ((SQLBinaryOpExpr)parent).getRight()) || ((leftOp == SQLBinaryOperator.Is || leftOp == SQLBinaryOperator.IsNot) && !op.isLogical()) || (binaryLeft.isParenthesized() && (leftOp != op && ((leftOp.isLogical() && op.isLogical()) || op == SQLBinaryOperator.Is)));
            if (bracket) {
                if (leftRational) {
                    ++this.indentCount;
                }
                this.print('(');
                this.printExpr(left, this.parameterized);
                this.print(')');
                if (leftRational) {
                    --this.indentCount;
                }
            }
            else {
                this.printExpr(left, this.parameterized);
            }
        }
        else if (left instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)left;
            if (group.getOperator() == op) {
                this.visit(group);
            }
            else {
                ++this.indentCount;
                this.print('(');
                this.visit(group);
                this.print(')');
                --this.indentCount;
            }
        }
        else if (left instanceof SQLInListExpr) {
            final SQLInListExpr inListExpr = (SQLInListExpr)left;
            boolean quote;
            if (inListExpr.isNot()) {
                quote = (op.priority <= SQLBinaryOperator.Equality.priority);
            }
            else {
                quote = (op.priority < SQLBinaryOperator.Equality.priority);
            }
            if (quote) {
                this.print('(');
            }
            this.visit(inListExpr);
            if (quote) {
                this.print(')');
            }
        }
        else if (left instanceof SQLBetweenExpr) {
            final SQLBetweenExpr betweenExpr = (SQLBetweenExpr)left;
            boolean quote;
            if (betweenExpr.isNot()) {
                quote = (op.priority <= SQLBinaryOperator.Equality.priority);
            }
            else {
                quote = (op.priority < SQLBinaryOperator.Equality.priority);
            }
            if (quote) {
                this.print('(');
            }
            this.visit(betweenExpr);
            if (quote) {
                this.print(')');
            }
        }
        else if (left instanceof SQLNotExpr) {
            this.print('(');
            this.printExpr(left);
            this.print(')');
        }
        else if (left instanceof SQLUnaryExpr) {
            final SQLUnaryExpr unary = (SQLUnaryExpr)left;
            boolean quote = true;
            switch (unary.getOperator()) {
                case BINARY: {
                    quote = false;
                    break;
                }
                case Plus:
                case Negative: {
                    quote = (op.priority < SQLBinaryOperator.Add.priority);
                    break;
                }
            }
            if (quote) {
                this.print('(');
                this.printExpr(left);
                this.print(')');
            }
            else {
                this.printExpr(left);
            }
        }
        else {
            this.printExpr(left, this.parameterized);
        }
    }
    
    protected void printTableSource(final SQLTableSource x) {
        final Class<?> clazz = x.getClass();
        if (clazz == SQLJoinTableSource.class) {
            this.visit((SQLJoinTableSource)x);
        }
        else if (clazz == SQLExprTableSource.class) {
            this.visit((SQLExprTableSource)x);
        }
        else if (clazz == SQLSubqueryTableSource.class) {
            this.visit((SQLSubqueryTableSource)x);
        }
        else {
            x.accept(this);
        }
    }
    
    protected void printQuery(final SQLSelectQuery x) {
        final Class<?> clazz = x.getClass();
        if (clazz == SQLSelectQueryBlock.class) {
            this.visit((SQLSelectQueryBlock)x);
        }
        else if (clazz == SQLUnionQuery.class) {
            this.visit((SQLUnionQuery)x);
        }
        else {
            x.accept(this);
        }
    }
    
    protected final void printExpr(final SQLExpr x) {
        this.printExpr(x, this.parameterized);
    }
    
    protected final void printExpr(final SQLExpr x, final boolean parameterized) {
        final Class<?> clazz = x.getClass();
        if (clazz == SQLIdentifierExpr.class) {
            this.visit((SQLIdentifierExpr)x);
        }
        else if (clazz == SQLPropertyExpr.class) {
            this.visit((SQLPropertyExpr)x);
        }
        else if (clazz == SQLAllColumnExpr.class) {
            this.print('*');
        }
        else if (clazz == SQLAggregateExpr.class) {
            this.visit((SQLAggregateExpr)x);
        }
        else if (clazz == SQLBinaryOpExpr.class) {
            this.visit((SQLBinaryOpExpr)x);
        }
        else if (clazz == SQLCharExpr.class) {
            this.visit((SQLCharExpr)x, parameterized);
        }
        else if (clazz == SQLNullExpr.class) {
            this.visit((SQLNullExpr)x);
        }
        else if (clazz == SQLIntegerExpr.class) {
            this.printInteger((SQLIntegerExpr)x, parameterized);
        }
        else if (clazz == SQLNumberExpr.class) {
            this.visit((SQLNumberExpr)x);
        }
        else if (clazz == SQLMethodInvokeExpr.class) {
            this.visit((SQLMethodInvokeExpr)x);
        }
        else if (clazz == SQLVariantRefExpr.class) {
            this.visit((SQLVariantRefExpr)x);
        }
        else if (clazz == SQLBinaryOpExprGroup.class) {
            this.visit((SQLBinaryOpExprGroup)x);
        }
        else if (clazz == SQLCaseExpr.class) {
            this.visit((SQLCaseExpr)x);
        }
        else if (clazz == SQLInListExpr.class) {
            this.visit((SQLInListExpr)x);
        }
        else if (clazz == SQLNotExpr.class) {
            this.visit((SQLNotExpr)x);
        }
        else {
            x.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLCaseExpr x) {
        ++this.indentCount;
        this.print0(this.ucase ? "CASE " : "case ");
        final SQLExpr valueExpr = x.getValueExpr();
        if (valueExpr != null) {
            this.printExpr(valueExpr, this.parameterized);
        }
        final List<SQLCaseExpr.Item> items = x.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            this.println();
            this.visit(items.get(i));
        }
        final SQLExpr elExpr = x.getElseExpr();
        if (elExpr != null) {
            this.println();
            this.print0(this.ucase ? "ELSE " : "else ");
            if (elExpr instanceof SQLCaseExpr) {
                ++this.indentCount;
                this.println();
                this.visit((SQLCaseExpr)elExpr);
                --this.indentCount;
            }
            else {
                this.printExpr(elExpr, this.parameterized);
            }
        }
        --this.indentCount;
        this.println();
        this.print0(this.ucase ? "END" : "end");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCaseExpr.Item x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        final SQLExpr conditionExpr = x.getConditionExpr();
        final int lines = this.lines;
        ++this.indentCount;
        this.printExpr(conditionExpr, this.parameterized);
        --this.indentCount;
        if (lines != this.lines) {
            this.println();
        }
        else {
            this.print(' ');
        }
        this.print0(this.ucase ? "THEN " : "then ");
        final SQLExpr valueExpr = x.getValueExpr();
        if (valueExpr instanceof SQLCaseExpr) {
            ++this.indentCount;
            this.println();
            this.visit((SQLCaseExpr)valueExpr);
            --this.indentCount;
        }
        else {
            ++this.indentCount;
            this.printExpr(valueExpr, this.parameterized);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCaseStatement x) {
        this.print0(this.ucase ? "CASE" : "case");
        final SQLExpr valueExpr = x.getValueExpr();
        if (valueExpr != null) {
            this.print(' ');
            this.printExpr(valueExpr, this.parameterized);
        }
        ++this.indentCount;
        this.println();
        this.printlnAndAccept(x.getItems(), " ");
        if (x.getElseStatements().size() > 0) {
            this.println();
            this.print0(this.ucase ? "ELSE " : "else ");
            this.printlnAndAccept(x.getElseStatements(), "");
        }
        --this.indentCount;
        this.println();
        this.print0(this.ucase ? "END CASE" : "end case");
        if (DbType.oracle == this.dbType) {
            this.print(';');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCaseStatement.Item x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        this.printExpr(x.getConditionExpr(), this.parameterized);
        this.print0(this.ucase ? " THEN " : " then ");
        final SQLStatement stmt = x.getStatement();
        if (stmt != null) {
            stmt.accept(this);
            this.print(';');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCastExpr x) {
        if (x.isTry()) {
            this.print0(this.ucase ? "TRY_CAST(" : "try_cast(");
        }
        else {
            this.print0(this.ucase ? "CAST(" : "cast(");
        }
        x.getExpr().accept(this);
        this.print0(this.ucase ? " AS " : " as ");
        x.getDataType().accept(this);
        this.print0(")");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x) {
        return this.visit(x, this.parameterized);
    }
    
    public boolean visit(final SQLCharExpr x, final boolean parameterized) {
        if (parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
            return false;
        }
        this.printChars(x.getText());
        return false;
    }
    
    protected void printChars(String text) {
        if (text == null) {
            this.print0(this.ucase ? "NULL" : "null");
        }
        else {
            this.print('\'');
            final int index = text.indexOf(39);
            if (index >= 0) {
                text = text.replaceAll("'", "''");
            }
            this.print0(text);
            this.print('\'');
        }
    }
    
    @Override
    public boolean visit(final SQLDataType x) {
        this.printDataType(x);
        return false;
    }
    
    protected void printDataType(final SQLDataType x) {
        final boolean parameterized = this.parameterized;
        this.parameterized = false;
        this.print0(x.getName());
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() > 0) {
            this.print('(');
            for (int i = 0, size = arguments.size(); i < size; ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.printExpr(arguments.get(i), false);
            }
            this.print(')');
        }
        final Boolean withTimeZone = x.getWithTimeZone();
        if (withTimeZone != null) {
            if (withTimeZone) {
                if (x.isWithLocalTimeZone()) {
                    this.print0(this.ucase ? " WITH LOCAL TIME ZONE" : " with local time zone");
                }
                else {
                    this.print0(this.ucase ? " WITH TIME ZONE" : " with time zone");
                }
            }
            else {
                this.print0(this.ucase ? " WITHOUT TIME ZONE" : " without time zone");
            }
        }
        if (x instanceof SQLDataTypeImpl) {
            final SQLExpr indexBy = ((SQLDataTypeImpl)x).getIndexBy();
            if (indexBy != null) {
                this.print0(this.ucase ? " INDEX BY " : " index by ");
                indexBy.accept(this);
            }
        }
        this.parameterized = parameterized;
    }
    
    @Override
    public boolean visit(final SQLCharacterDataType x) {
        this.visit((SQLDataType)x);
        final List<SQLCommentHint> hints = x.hints;
        if (hints != null) {
            this.print(' ');
            for (final SQLCommentHint hint : hints) {
                hint.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExistsExpr x) {
        if (x.isNot()) {
            this.print0(this.ucase ? "NOT EXISTS (" : "not exists (");
        }
        else {
            this.print0(this.ucase ? "EXISTS (" : "exists (");
        }
        ++this.indentCount;
        this.println();
        this.visit(x.getSubQuery());
        --this.indentCount;
        this.println();
        this.print(')');
        final SQLCommentHint hint = x.getHint();
        if (hint != null) {
            this.print(' ');
            hint.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        this.printName0(x.getName());
        return false;
    }
    
    private boolean printName(final SQLName x, final String name) {
        final boolean shardingSupport = this.shardingSupport && this.parameterized;
        return this.printName(x, name, shardingSupport);
    }
    
    public String unwrapShardingTable(final String name) {
        final char c0 = name.charAt(0);
        final char c_last = name.charAt(name.length() - 1);
        final boolean quote = (c0 == '`' && c_last == '`') || (c0 == '\"' && c_last == '\"');
        int end = name.length();
        if (quote) {
            --end;
        }
        int num_cnt = 0;
        int postfixed_cnt = 0;
        for (int i = end - 1; i > 0; --i, ++postfixed_cnt) {
            final char ch = name.charAt(i);
            if (ch >= '0' && ch <= '9') {
                ++num_cnt;
            }
            if (ch != '_') {
                if (ch < '0') {
                    break;
                }
                if (ch > '9') {
                    break;
                }
            }
        }
        if (num_cnt < 1 || postfixed_cnt < 2) {
            return name;
        }
        final int start = end - postfixed_cnt;
        if (start < 1) {
            return name;
        }
        final String realName = name.substring(quote ? 1 : 0, start);
        return realName;
    }
    
    private boolean printName(final SQLName x, final String name, boolean shardingSupport) {
        if (shardingSupport) {
            final SQLObject parent = x.getParent();
            shardingSupport = (parent instanceof SQLExprTableSource || parent instanceof SQLPropertyExpr);
            if (parent instanceof SQLPropertyExpr && parent.getParent() instanceof SQLExprTableSource) {
                shardingSupport = false;
            }
        }
        if (shardingSupport) {
            final boolean quote = name.charAt(0) == '`' && name.charAt(name.length() - 1) == '`';
            final String unwrappedName = this.unwrapShardingTable(name);
            if (unwrappedName != name) {
                boolean isAlias = false;
                SQLObject parent2 = x.getParent();
                while (parent2 != null) {
                    if (parent2 instanceof SQLSelectQueryBlock) {
                        final SQLTableSource from = ((SQLSelectQueryBlock)parent2).getFrom();
                        if (quote) {
                            final String name2 = name.substring(1, name.length() - 1);
                            if (this.isTableSourceAlias(from, name, name2)) {
                                isAlias = true;
                            }
                            break;
                        }
                        if (this.isTableSourceAlias(from, name)) {
                            isAlias = true;
                            break;
                        }
                        break;
                    }
                    else {
                        parent2 = parent2.getParent();
                    }
                }
                if (!isAlias) {
                    this.print0(unwrappedName);
                    this.incrementReplaceCunt();
                    return false;
                }
                this.printName0(name);
                return false;
            }
        }
        this.printName0(name);
        return false;
    }
    
    @Override
    public boolean visit(final SQLInListExpr x) {
        final SQLExpr expr = x.getExpr();
        boolean quote = false;
        if (expr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOperator operator = ((SQLBinaryOpExpr)expr).getOperator();
            switch (operator) {
                case BooleanAnd:
                case BooleanOr:
                case BooleanXor:
                case Assignment: {
                    quote = true;
                    break;
                }
                default: {
                    quote = ((SQLBinaryOpExpr)expr).isParenthesized();
                    break;
                }
            }
        }
        else if (expr instanceof SQLNotExpr || expr instanceof SQLBetweenExpr || expr instanceof SQLInListExpr || expr instanceof SQLUnaryExpr || expr instanceof SQLBinaryOpExprGroup) {
            quote = true;
        }
        if (this.parameterized) {
            final List<SQLExpr> targetList = x.getTargetList();
            boolean allLiteral = true;
            for (final SQLExpr item : targetList) {
                if (!(item instanceof SQLLiteralExpr) && !(item instanceof SQLVariantRefExpr)) {
                    if (!(item instanceof SQLListExpr)) {
                        allLiteral = false;
                        break;
                    }
                    final SQLListExpr list = (SQLListExpr)item;
                    for (final SQLExpr listItem : list.getItems()) {
                        if (!(listItem instanceof SQLLiteralExpr) && !(listItem instanceof SQLVariantRefExpr)) {
                            allLiteral = false;
                            break;
                        }
                    }
                    if (allLiteral) {
                        break;
                    }
                    continue;
                }
            }
            if (allLiteral) {
                boolean changed = true;
                if (targetList.size() == 1 && targetList.get(0) instanceof SQLVariantRefExpr) {
                    changed = false;
                }
                if (quote) {
                    this.print('(');
                }
                this.printExpr(expr, this.parameterized);
                if (quote) {
                    this.print(')');
                }
                if (x.isNot()) {
                    this.print(this.ucase ? " NOT IN" : " not in");
                }
                else {
                    this.print(this.ucase ? " IN" : " in");
                }
                if (!this.parameterizedQuesUnMergeInList || (targetList.size() == 1 && !(targetList.get(0) instanceof SQLListExpr))) {
                    this.print(" (?)");
                }
                else {
                    this.print(" (");
                    for (int i = 0; i < targetList.size(); ++i) {
                        if (i != 0) {
                            this.print(", ");
                        }
                        final SQLExpr item2 = targetList.get(i);
                        if (item2 instanceof SQLListExpr) {
                            this.visit((SQLListExpr)item2);
                            changed = false;
                        }
                        else {
                            this.print("?");
                        }
                    }
                    this.print(")");
                }
                if (changed) {
                    this.incrementReplaceCunt();
                    if (this.parameters != null) {
                        if (this.parameterizedMergeInList) {
                            final List<Object> subList = new ArrayList<Object>(x.getTargetList().size());
                            for (final SQLExpr target : x.getTargetList()) {
                                ExportParameterVisitorUtils.exportParameter(subList, target);
                            }
                            if (subList != null) {
                                this.parameters.add(subList);
                            }
                        }
                        else {
                            for (final SQLExpr target2 : x.getTargetList()) {
                                ExportParameterVisitorUtils.exportParameter(this.parameters, target2);
                            }
                        }
                    }
                }
                if (x.getHint() != null) {
                    x.getHint().accept(this);
                }
                return false;
            }
        }
        if (quote) {
            this.print('(');
        }
        this.printExpr(expr, this.parameterized);
        if (quote) {
            this.print(')');
        }
        if (x.isNot()) {
            this.print0(this.ucase ? " NOT IN (" : " not in (");
        }
        else {
            this.print0(this.ucase ? " IN (" : " in (");
        }
        final List<SQLExpr> list2 = x.getTargetList();
        boolean printLn = false;
        if (list2.size() > 5) {
            printLn = true;
            for (int j = 0, size = list2.size(); j < size; ++j) {
                if (!(list2.get(j) instanceof SQLCharExpr)) {
                    printLn = false;
                    break;
                }
            }
        }
        if (printLn) {
            ++this.indentCount;
            this.println();
            for (int j = 0, size = list2.size(); j < size; ++j) {
                if (j != 0) {
                    this.print0(", ");
                    this.println();
                }
                final SQLExpr item2 = list2.get(j);
                this.printExpr(item2, this.parameterized);
            }
            --this.indentCount;
            this.println();
        }
        else {
            final List<SQLExpr> targetList2 = x.getTargetList();
            for (int i = 0; i < targetList2.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.printExpr(targetList2.get(i), this.parameterized);
            }
        }
        this.print(')');
        final List<String> afterComments = x.getAfterCommentsDirect();
        if (afterComments != null && !afterComments.isEmpty() && afterComments.get(0).startsWith("--")) {
            this.print(' ');
        }
        this.printlnComment(afterComments);
        if (x.getHint() != null) {
            x.getHint().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLContainsExpr x) {
        final SQLExpr expr = x.getExpr();
        if (expr != null) {
            this.printExpr(expr, this.parameterized);
            this.print(' ');
        }
        if (x.isNot()) {
            this.print0(this.ucase ? "NOT CONTAINS (" : " not contains (");
        }
        else {
            this.print0(this.ucase ? "CONTAINS (" : " contains (");
        }
        final List<SQLExpr> list = x.getTargetList();
        boolean printLn = false;
        if (list.size() > 5) {
            printLn = true;
            for (int i = 0, size = list.size(); i < size; ++i) {
                if (!(list.get(i) instanceof SQLCharExpr)) {
                    printLn = false;
                    break;
                }
            }
        }
        if (printLn) {
            ++this.indentCount;
            this.println();
            for (int i = 0, size = list.size(); i < size; ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }
                final SQLExpr item = list.get(i);
                this.printExpr(item, this.parameterized);
            }
            --this.indentCount;
            this.println();
        }
        else {
            final List<SQLExpr> targetList = x.getTargetList();
            for (int j = 0; j < targetList.size(); ++j) {
                if (j != 0) {
                    this.print0(", ");
                }
                this.printExpr(targetList.get(j), this.parameterized);
            }
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLIntegerExpr x) {
        final boolean parameterized = this.parameterized;
        this.printInteger(x, parameterized);
        return false;
    }
    
    protected void printInteger(final SQLIntegerExpr x, final boolean parameterized) {
        final Number number = x.getNumber();
        if (number.equals(SQLASTOutputVisitor.ONE) && DbType.oracle.equals(this.dbType)) {
            final SQLObject parent = x.getParent();
            if (parent instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)parent;
                final SQLExpr left = binaryOpExpr.getLeft();
                final SQLBinaryOperator op = binaryOpExpr.getOperator();
                if (left instanceof SQLIdentifierExpr && op == SQLBinaryOperator.Equality) {
                    final String name = ((SQLIdentifierExpr)left).getName();
                    if ("rownum".equals(name)) {
                        this.print(1);
                        return;
                    }
                }
            }
        }
        if (parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
            return;
        }
        if (number instanceof BigDecimal || number instanceof BigInteger) {
            this.print(number.toString());
        }
        else {
            this.print(number.longValue());
        }
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        final SQLExpr owner = x.getOwner();
        if (owner != null) {
            this.printMethodOwner(owner);
        }
        if (this.parameterized) {
            final List<SQLExpr> arguments = x.getArguments();
            if (x.methodNameHashCode64() == FnvHash.Constants.TRIM && arguments.size() == 1 && arguments.get(0) instanceof SQLCharExpr && x.getTrimOption() == null && x.getFrom() == null) {
                this.print('?');
                if (this.parameters != null) {
                    final SQLCharExpr charExpr = arguments.get(0);
                    this.parameters.add(charExpr.getText().trim());
                }
                ++this.replaceCount;
                return false;
            }
        }
        final String function = x.getMethodName();
        if (function != null) {
            this.printFunctionName(function);
        }
        this.printMethodParameters(x);
        return false;
    }
    
    protected void printMethodParameters(final SQLMethodInvokeExpr x) {
        final String function = x.getMethodName();
        final List<SQLExpr> parameters = x.getArguments();
        this.print('(');
        final String trimOption = x.getTrimOption();
        if (trimOption != null) {
            this.print0(trimOption);
            if (parameters.size() > 0) {
                this.print(' ');
            }
        }
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLExpr param = parameters.get(i);
            if (this.parameterized && size == 2 && i == 1 && param instanceof SQLCharExpr) {
                if (DbType.oracle == this.dbType) {
                    if ("TO_CHAR".equalsIgnoreCase(function) || "TO_DATE".equalsIgnoreCase(function)) {
                        this.printChars(((SQLCharExpr)param).getText());
                        continue;
                    }
                }
                else if (DbType.mysql == this.dbType && "DATE_FORMAT".equalsIgnoreCase(function)) {
                    this.printChars(((SQLCharExpr)param).getText());
                    continue;
                }
            }
            if (param instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)param;
                final SQLBinaryOperator op = binaryOpExpr.getOperator();
                if (op == SQLBinaryOperator.BooleanAnd || op == SQLBinaryOperator.BooleanOr) {
                    ++this.indentCount;
                    this.printExpr(param, this.parameterized);
                    --this.indentCount;
                    continue;
                }
            }
            this.printExpr(param, this.parameterized);
        }
        final SQLExpr from = x.getFrom();
        if (from != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            this.printExpr(from, this.parameterized);
            final SQLExpr _for = x.getFor();
            if (_for != null) {
                this.print0(this.ucase ? " FOR " : " for ");
                this.printExpr(_for, this.parameterized);
            }
        }
        final SQLExpr using = x.getUsing();
        boolean odpsTransformUsing = false;
        if (using != null) {
            odpsTransformUsing = (x.methodNameHashCode64() == FnvHash.Constants.TRANSFORM);
            if (!odpsTransformUsing) {
                this.print0(this.ucase ? " USING " : " using ");
                this.printExpr(using, this.parameterized);
            }
        }
        if (x.methodNameHashCode64() == FnvHash.Constants.WEIGHT_STRING) {
            final SQLDataType as = (SQLDataType)x.getAttribute("as");
            if (as != null) {
                this.print0(this.ucase ? " AS " : " as ");
                as.accept(this);
            }
            final List<SQLSelectOrderByItem> levels = (List<SQLSelectOrderByItem>)x.getAttribute("levels");
            if (levels != null && levels.size() > 0) {
                this.print0(this.ucase ? " LEVEL " : " level ");
                this.printAndAccept(levels, ", ");
            }
            final Boolean reverse = (Boolean)x.getAttribute("reverse");
            if (reverse != null) {
                this.print0(this.ucase ? " REVERSE" : " reverse");
            }
        }
        this.print(')');
        if (odpsTransformUsing) {
            this.print0(this.ucase ? " USING " : " using ");
            this.printExpr(using, this.parameterized);
        }
    }
    
    protected void printMethodOwner(final SQLExpr owner) {
        this.printExpr(owner, this.parameterized);
        this.print('.');
    }
    
    protected void printFunctionName(final String name) {
        this.print0(name);
    }
    
    @Override
    public boolean visit(final SQLAggregateExpr x) {
        final boolean parameterized = this.parameterized;
        if (x.methodNameHashCode64() == FnvHash.Constants.GROUP_CONCAT) {
            this.parameterized = false;
        }
        if (x.methodNameHashCode64() == FnvHash.Constants.COUNT) {
            final List<SQLExpr> arguments = x.getArguments();
            if (arguments.size() == 1) {
                final SQLExpr arg0 = arguments.get(0);
                if (arg0 instanceof SQLLiteralExpr) {
                    this.parameterized = false;
                }
            }
        }
        if (x.getOwner() != null) {
            this.printExpr(x.getOwner());
            this.print(".");
        }
        final String methodName = x.getMethodName();
        this.print0(this.ucase ? methodName : methodName.toLowerCase());
        this.print('(');
        final SQLAggregateOption option = x.getOption();
        if (option != null) {
            this.print0(option.toString());
            this.print(' ');
        }
        final List<SQLExpr> arguments2 = x.getArguments();
        for (int i = 0, size = arguments2.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            this.printExpr(arguments2.get(i), false);
        }
        final boolean withGroup = x.isWithinGroup();
        if (withGroup) {
            this.print0(this.ucase ? ") WITHIN GROUP (" : " within group (");
        }
        this.visitAggreateRest(x);
        this.print(')');
        if (x.isIgnoreNulls()) {
            this.print0(this.ucase ? " IGNORE NULLS" : " ignore nulls");
        }
        final SQLKeep keep = x.getKeep();
        if (keep != null) {
            this.print(' ');
            this.visit(keep);
        }
        final SQLOver over = x.getOver();
        if (over != null) {
            this.print0(this.ucase ? " OVER " : " over ");
            over.accept(this);
        }
        final SQLName overRef = x.getOverRef();
        if (overRef != null) {
            this.print0(this.ucase ? " OVER " : " over ");
            overRef.accept(this);
        }
        final SQLExpr filter = x.getFilter();
        if (filter != null) {
            this.print0(this.ucase ? " FILTER (WHERE " : " filter (where ");
            this.printExpr(filter);
            this.print(')');
        }
        this.parameterized = parameterized;
        return false;
    }
    
    protected void visitAggreateRest(final SQLAggregateExpr x) {
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            if (!x.isWithinGroup()) {
                this.print(' ');
            }
            orderBy.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLAllColumnExpr x) {
        this.print('*');
        return true;
    }
    
    @Override
    public boolean visit(final SQLNCharExpr x) {
        if (this.parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
            return false;
        }
        if (x.getText() == null || x.getText().length() == 0) {
            this.print0(this.ucase ? "NULL" : "null");
        }
        else {
            this.print0(this.ucase ? "N'" : "n'");
            this.print0(x.getText().replace("'", "''"));
            this.print('\'');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLNotExpr x) {
        this.print0(this.ucase ? "NOT " : "not ");
        final SQLExpr expr = x.getExpr();
        boolean needQuote = false;
        if (expr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)expr;
            needQuote = binaryOpExpr.getOperator().isLogical();
        }
        else if (expr instanceof SQLInListExpr || expr instanceof SQLNotExpr || expr instanceof SQLBinaryOpExprGroup) {
            needQuote = true;
        }
        if (needQuote) {
            this.print('(');
        }
        this.printExpr(expr, this.parameterized);
        if (needQuote) {
            this.print(')');
        }
        final SQLCommentHint hint = x.getHint();
        if (hint != null) {
            hint.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLNullExpr x) {
        final SQLObject parent = x.getParent();
        if (this.parameterized && (parent instanceof SQLInsertStatement.ValuesClause || parent instanceof SQLInListExpr || (parent instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)parent).getOperator() == SQLBinaryOperator.Equality))) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                if (parent instanceof SQLBinaryOpExpr) {
                    ExportParameterVisitorUtils.exportParameter(this.getParameters(), x);
                }
                else {
                    this.getParameters().add(null);
                }
            }
            return false;
        }
        this.print0(this.ucase ? "NULL" : "null");
        return false;
    }
    
    @Override
    public boolean visit(final SQLNumberExpr x) {
        if (this.parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.getParameters(), x);
            }
            return false;
        }
        if (this.appender instanceof StringBuilder) {
            x.output((StringBuilder)this.appender);
        }
        else if (this.appender instanceof StringBuilder) {
            x.output((StringBuilder)this.appender);
        }
        else {
            this.print0(x.getNumber().toString());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        final SQLExpr owner = x.getOwner();
        String mapTableName = null;
        String ownerName = null;
        if (owner instanceof SQLIdentifierExpr) {
            ownerName = ((SQLIdentifierExpr)owner).getName();
            if (this.tableMapping != null) {
                mapTableName = this.tableMapping.get(ownerName);
                if (mapTableName == null && ownerName.length() > 2 && ownerName.charAt(0) == '`' && ownerName.charAt(ownerName.length() - 1) == '`') {
                    ownerName = ownerName.substring(1, ownerName.length() - 1);
                    mapTableName = this.tableMapping.get(ownerName);
                }
            }
        }
        if (mapTableName != null) {
            SQLObject parent = x.getParent();
            while (parent != null) {
                if (parent instanceof SQLSelectQueryBlock) {
                    final SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                    if (this.isTableSourceAlias(from, mapTableName, ownerName)) {
                        mapTableName = null;
                        break;
                    }
                    break;
                }
                else {
                    parent = parent.getParent();
                }
            }
        }
        if (mapTableName != null) {
            this.printName0(mapTableName);
        }
        else if (owner instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr ownerIdent = (SQLIdentifierExpr)owner;
            this.printName(ownerIdent, ownerIdent.getName(), this.shardingSupport && this.parameterized);
        }
        else {
            this.printExpr(owner, this.parameterized);
        }
        this.print('.');
        final String name = x.getName();
        if ("*".equals(name)) {
            this.print0(name);
        }
        else {
            this.printName0(name);
        }
        return false;
    }
    
    protected boolean isTableSourceAlias(final SQLTableSource from, final String... tableNames) {
        String alias = from.getAlias();
        if (alias != null) {
            for (final String tableName : tableNames) {
                if (alias.equalsIgnoreCase(tableName)) {
                    return true;
                }
            }
            if (alias.length() > 2 && alias.charAt(0) == '`' && alias.charAt(alias.length() - 1) == '`') {
                alias = alias.substring(1, alias.length() - 1);
                for (final String tableName : tableNames) {
                    if (alias.equalsIgnoreCase(tableName)) {
                        return true;
                    }
                }
            }
        }
        if (from instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)from;
            return this.isTableSourceAlias(join.getLeft(), tableNames) || this.isTableSourceAlias(join.getRight(), tableNames);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLQueryExpr x) {
        SQLObject parent = x.getParent();
        if (parent instanceof SQLSelect) {
            parent = parent.getParent();
        }
        final SQLSelect subQuery = x.getSubQuery();
        if (parent instanceof SQLInsertStatement.ValuesClause) {
            this.println();
            this.print('(');
            this.visit(subQuery);
            this.print(')');
            this.println();
        }
        else if ((parent instanceof SQLStatement && !(parent instanceof OracleForStatement)) || parent instanceof OracleSelectPivot.Item) {
            ++this.indentCount;
            this.println();
            this.visit(subQuery);
            --this.indentCount;
        }
        else if (parent instanceof SQLOpenStatement) {
            this.visit(subQuery);
        }
        else if (parent instanceof SQLMethodInvokeExpr && ((SQLMethodInvokeExpr)parent).getArguments().size() == 1 && (((SQLMethodInvokeExpr)parent).methodNameHashCode64() == FnvHash.Constants.LATERAL || ((SQLMethodInvokeExpr)parent).methodNameHashCode64() == FnvHash.Constants.ARRAY)) {
            ++this.indentCount;
            this.println();
            this.visit(subQuery);
            --this.indentCount;
            this.println();
        }
        else {
            this.print('(');
            ++this.indentCount;
            this.println();
            this.visit(subQuery);
            --this.indentCount;
            this.println();
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectGroupByClause x) {
        final boolean paren = DbType.oracle == this.dbType || x.isParen();
        final boolean rollup = x.isWithRollUp();
        final boolean cube = x.isWithCube();
        final List<SQLExpr> items = x.getItems();
        final int itemSize = items.size();
        if (itemSize > 0) {
            this.print0(this.ucase ? "GROUP BY " : "group by ");
            if (x.isDistinct()) {
                this.print0(this.ucase ? "DISTINCT " : "distinct ");
            }
            if (paren && rollup) {
                this.print0(this.ucase ? "ROLLUP (" : "rollup (");
            }
            else if (paren && cube) {
                this.print0(this.ucase ? "CUBE (" : "cube (");
            }
            ++this.indentCount;
            for (int i = 0; i < itemSize; ++i) {
                final SQLExpr item = items.get(i);
                if (i != 0) {
                    if (this.groupItemSingleLine) {
                        this.println(", ");
                    }
                    else if (item instanceof SQLGroupingSetExpr) {
                        this.println();
                    }
                    else {
                        this.print(", ");
                    }
                }
                if (item instanceof SQLIntegerExpr) {
                    this.printInteger((SQLIntegerExpr)item, false);
                }
                else if (item instanceof MySqlOrderingExpr && ((MySqlOrderingExpr)item).getExpr() instanceof SQLIntegerExpr) {
                    final MySqlOrderingExpr orderingExpr = (MySqlOrderingExpr)item;
                    this.printInteger((SQLIntegerExpr)orderingExpr.getExpr(), false);
                    this.print(' ' + orderingExpr.getType().name);
                }
                else {
                    item.accept(this);
                }
                SQLCommentHint hint = null;
                if (item instanceof SQLExprImpl) {
                    hint = ((SQLExprImpl)item).getHint();
                }
                if (hint != null) {
                    hint.accept(this);
                }
            }
            if (paren && (rollup || cube)) {
                this.print(')');
            }
            --this.indentCount;
        }
        if (x.getHaving() != null) {
            this.println();
            this.print0(this.ucase ? "HAVING " : "having ");
            x.getHaving().accept(this);
        }
        if (x.isWithRollUp() && !paren) {
            this.print0(this.ucase ? " WITH ROLLUP" : " with rollup");
        }
        if (x.isWithCube() && !paren) {
            this.print0(this.ucase ? " WITH CUBE" : " with cube");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        final SQLHint headHint = x.getHeadHint();
        if (headHint != null) {
            headHint.accept(this);
            this.println();
        }
        final SQLWithSubqueryClause withSubQuery = x.getWithSubQuery();
        if (withSubQuery != null) {
            withSubQuery.accept(this);
            this.println();
        }
        this.printQuery(x.getQuery());
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            orderBy.accept(this);
        }
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.println();
            limit.accept(this);
        }
        if (x.getHintsSize() > 0) {
            this.printAndAccept(x.getHints(), "");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectQueryBlock x) {
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "SELECT " : "select ");
        if (x.getHintsSize() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        final boolean informix = DbType.informix == this.dbType;
        if (informix) {
            this.printFetchFirst(x);
        }
        final int distinctOption = x.getDistionOption();
        if (1 == distinctOption) {
            this.print0(this.ucase ? "ALL " : "all ");
        }
        else if (2 == distinctOption) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
        }
        else if (3 == distinctOption) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }
        this.printSelectList(x.getSelectList());
        final SQLExprTableSource into = x.getInto();
        if (into != null) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            into.accept(this);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            final boolean printFrom = from instanceof SQLLateralViewTableSource && ((SQLLateralViewTableSource)from).getTableSource() == null;
            if (!printFrom) {
                this.print0(this.ucase ? "FROM " : "from ");
            }
            this.printTableSource(from);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where, this.parameterized);
            if (where.hasAfterComment() && this.isPrettyFormat()) {
                this.print(' ');
                this.printlnComment(x.getWhere().getAfterCommentsDirect());
            }
        }
        this.printHierarchical(x);
        final SQLSelectGroupByClause groupBy = x.getGroupBy();
        if (groupBy != null) {
            this.println();
            this.visit(groupBy);
        }
        final List<SQLWindow> windows = x.getWindows();
        if (windows != null && windows.size() > 0) {
            this.println();
            this.print0(this.ucase ? "WINDOW " : "window ");
            this.printAndAccept(windows, ", ");
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            orderBy.accept(this);
        }
        final List<SQLSelectOrderByItem> distributeBy = x.getDistributeByDirect();
        if (distributeBy != null && distributeBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "DISTRIBUTE BY " : "distribute by ");
            this.printAndAccept(distributeBy, ", ");
        }
        final List<SQLSelectOrderByItem> sortBy = x.getSortByDirect();
        if (sortBy != null && sortBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SORT BY " : "sort by ");
            this.printAndAccept(sortBy, ", ");
        }
        final List<SQLSelectOrderByItem> clusterBy = x.getClusterByDirect();
        if (clusterBy != null && clusterBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "CLUSTER BY " : "cluster by ");
            this.printAndAccept(clusterBy, ", ");
        }
        if (!informix) {
            this.printFetchFirst(x);
        }
        if (x.isForUpdate()) {
            this.println();
            this.print0(this.ucase ? "FOR UPDATE" : "for update");
        }
        return false;
    }
    
    protected void printFetchFirst(final SQLSelectQueryBlock x) {
        final SQLLimit limit = x.getLimit();
        if (limit == null) {
            return;
        }
        final SQLExpr offset = limit.getOffset();
        final SQLExpr first = limit.getRowCount();
        if (DbType.informix == this.dbType) {
            if (offset != null) {
                this.print0(this.ucase ? "SKIP " : "skip ");
                offset.accept(this);
            }
            this.print0(this.ucase ? " FIRST " : " first ");
            first.accept(this);
            this.print(' ');
        }
        else if (DbType.db2 == this.dbType || DbType.oracle == this.dbType || DbType.sqlserver == this.dbType) {
            final SQLObject parent = x.getParent();
            if (parent instanceof SQLSelect) {
                final SQLOrderBy orderBy = ((SQLSelect)parent).getOrderBy();
                if (orderBy != null && orderBy.getItems().size() > 0) {
                    this.println();
                    this.print0(this.ucase ? "ORDER BY " : "order by ");
                    this.printAndAccept(orderBy.getItems(), ", ");
                }
            }
            this.println();
            if (offset != null) {
                this.print0(this.ucase ? "OFFSET " : "offset ");
                offset.accept(this);
                this.print0(this.ucase ? " ROWS" : " rows");
            }
            if (first != null) {
                if (offset != null) {
                    this.print(' ');
                }
                if (DbType.sqlserver == this.dbType && offset != null) {
                    this.print0(this.ucase ? "FETCH NEXT " : "fetch next ");
                }
                else {
                    this.print0(this.ucase ? "FETCH FIRST " : "fetch first ");
                }
                first.accept(this);
                this.print0(this.ucase ? " ROWS ONLY" : " rows only");
            }
        }
        else {
            this.println();
            limit.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLSelectItem x) {
        if (x.isConnectByRoot()) {
            this.print0(this.ucase ? "CONNECT_BY_ROOT " : "connect_by_root ");
        }
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIdentifierExpr) {
            this.printName0(((SQLIdentifierExpr)expr).getName());
        }
        else if (expr instanceof SQLPropertyExpr) {
            this.visit((SQLPropertyExpr)expr);
        }
        else if (expr != null) {
            this.printExpr(expr, this.parameterized);
        }
        final String alias = x.getAlias();
        if (alias != null && alias.length() > 0) {
            this.print0(this.ucase ? " AS " : " as ");
            final char c0 = alias.charAt(0);
            boolean special = false;
            if (c0 != '\"' && c0 != '\'' && c0 != '`' && c0 != '[') {
                for (int i = 1; i < alias.length(); ++i) {
                    final char ch = alias.charAt(i);
                    if (ch < '\u0100') {
                        if (ch < '0' || ch > '9') {
                            if (ch < 'a' || ch > 'z') {
                                if (ch < 'A' || ch > 'Z') {
                                    if (ch != '_') {
                                        if (ch != '$') {
                                            special = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!this.printNameQuote && !special) {
                this.print0(alias);
            }
            else {
                this.print(this.quote);
                String unquoteAlias = null;
                if (c0 == '`' && alias.charAt(alias.length() - 1) == '`') {
                    unquoteAlias = alias.substring(1, alias.length() - 1);
                }
                else if (c0 == '\'' && alias.charAt(alias.length() - 1) == '\'') {
                    unquoteAlias = alias.substring(1, alias.length() - 1);
                }
                else if (c0 == '\"' && alias.charAt(alias.length() - 1) == '\"') {
                    unquoteAlias = alias.substring(1, alias.length() - 1);
                }
                else {
                    this.print0(alias);
                }
                if (unquoteAlias != null) {
                    this.print0(unquoteAlias);
                }
                this.print(this.quote);
            }
            return false;
        }
        final List<String> aliasList = x.getAliasList();
        if (aliasList == null) {
            return false;
        }
        this.println();
        this.print0(this.ucase ? "AS (" : "as (");
        final int aliasSize = aliasList.size();
        if (aliasSize > 5) {
            ++this.indentCount;
            this.println();
        }
        for (int i = 0; i < aliasSize; ++i) {
            if (i != 0) {
                if (aliasSize > 5) {
                    this.println(",");
                }
                else {
                    this.print0(", ");
                }
            }
            this.print0(aliasList.get(i));
        }
        if (aliasSize > 5) {
            --this.indentCount;
            this.println();
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLOrderBy x) {
        final List<SQLSelectOrderByItem> items = x.getItems();
        if (items.size() > 0) {
            if (x.isSibings()) {
                this.print0(this.ucase ? "ORDER SIBLINGS BY " : "order siblings by ");
            }
            else {
                this.print0(this.ucase ? "ORDER BY " : "order by ");
            }
            for (int i = 0, size = items.size(); i < size; ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                final SQLSelectOrderByItem item = items.get(i);
                this.visit(item);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectOrderByItem x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIntegerExpr) {
            this.print(((SQLIntegerExpr)expr).getNumber().longValue());
        }
        else {
            this.printExpr(expr, this.parameterized);
        }
        final SQLOrderingSpecification type = x.getType();
        if (type != null) {
            this.print(' ');
            this.print0(this.ucase ? type.name : type.name_lcase);
        }
        final String collate = x.getCollate();
        if (collate != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(collate);
        }
        final SQLSelectOrderByItem.NullsOrderType nullsOrderType = x.getNullsOrderType();
        if (nullsOrderType != null) {
            this.print(' ');
            this.print0(nullsOrderType.toFormalString());
        }
        final SQLCommentHint hint = x.getHint();
        if (hint != null) {
            this.visit(hint);
        }
        return false;
    }
    
    protected void addTable(final String table) {
        if (this.tables == null) {
            if (this.table == null) {
                this.table = table;
                return;
            }
            (this.tables = new LinkedHashSet<String>()).add(this.table);
        }
        this.tables.add(table);
    }
    
    protected void printTableSourceExpr(final SQLExpr expr) {
        if (this.exportTables) {
            this.addTable(expr.toString());
        }
        if (this.isEnabled(VisitorFeature.OutputDesensitize)) {
            String ident = null;
            if (expr instanceof SQLIdentifierExpr) {
                ident = ((SQLIdentifierExpr)expr).getName();
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                propertyExpr.getOwner().accept(this);
                this.print('.');
                ident = propertyExpr.getName();
            }
            if (ident != null) {
                final String desensitizeTable = SQLUtils.desensitizeTable(ident);
                this.print0(desensitizeTable);
                return;
            }
        }
        if (this.tableMapping != null && expr instanceof SQLName) {
            String tableName;
            if (expr instanceof SQLIdentifierExpr) {
                tableName = ((SQLIdentifierExpr)expr).normalizedName();
            }
            else if (expr instanceof SQLPropertyExpr) {
                tableName = ((SQLPropertyExpr)expr).normalizedName();
            }
            else {
                tableName = expr.toString();
            }
            String destTableName = this.tableMapping.get(tableName);
            if (destTableName == null) {
                if (expr instanceof SQLPropertyExpr) {
                    final SQLPropertyExpr propertyExpr2 = (SQLPropertyExpr)expr;
                    final String propName = propertyExpr2.getName();
                    destTableName = this.tableMapping.get(propName);
                    if (destTableName == null && propName.length() > 2 && propName.charAt(0) == '`' && propName.charAt(propName.length() - 1) == '`') {
                        destTableName = this.tableMapping.get(propName.substring(1, propName.length() - 1));
                    }
                    if (destTableName != null) {
                        propertyExpr2.getOwner().accept(this);
                        this.print('.');
                        this.print(destTableName);
                        return;
                    }
                }
                else if (expr instanceof SQLIdentifierExpr) {
                    final boolean quote = tableName.length() > 2 && tableName.charAt(0) == '`' && tableName.charAt(tableName.length() - 1) == '`';
                    if (quote) {
                        destTableName = this.tableMapping.get(tableName.substring(1, tableName.length() - 1));
                    }
                }
            }
            if (destTableName != null) {
                tableName = destTableName;
                this.printName0(tableName);
                return;
            }
        }
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            final String name = identifierExpr.getName();
            if (!this.parameterized) {
                this.printName0(name);
                return;
            }
            final boolean shardingSupport = this.shardingSupport && this.parameterized;
            if (shardingSupport) {
                final String nameUnwrappe = this.unwrapShardingTable(name);
                if (!name.equals(nameUnwrappe)) {
                    this.incrementReplaceCunt();
                }
                this.printName0(nameUnwrappe);
            }
            else {
                this.printName0(name);
            }
        }
        else if (expr instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr3 = (SQLPropertyExpr)expr;
            final SQLExpr owner = propertyExpr3.getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identOwner = (SQLIdentifierExpr)owner;
                String ownerName = identOwner.getName();
                if (!this.parameterized) {
                    this.printName0(identOwner.getName());
                }
                else {
                    if (this.shardingSupport) {
                        ownerName = this.unwrapShardingTable(ownerName);
                    }
                    this.printName0(ownerName);
                }
            }
            else {
                this.printExpr(owner);
            }
            this.print('.');
            final String name2 = propertyExpr3.getName();
            if (!this.parameterized) {
                this.printName0(propertyExpr3.getName());
                return;
            }
            final boolean shardingSupport2 = this.shardingSupport && this.parameterized;
            if (shardingSupport2) {
                final String nameUnwrappe2 = this.unwrapShardingTable(name2);
                if (!name2.equals(nameUnwrappe2)) {
                    this.incrementReplaceCunt();
                }
                this.printName0(nameUnwrappe2);
            }
            else {
                this.printName0(name2);
            }
        }
        else if (expr instanceof SQLMethodInvokeExpr) {
            this.visit((SQLMethodInvokeExpr)expr);
        }
        else {
            expr.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        this.printTableSourceExpr(x.getExpr());
        final SQLTableSampling sampling = x.getSampling();
        if (sampling != null) {
            this.print(' ');
            sampling.accept(this);
        }
        final String alias = x.getAlias();
        final List<SQLName> columns = x.getColumnsDirect();
        if (alias != null) {
            this.print(' ');
            if (columns != null && columns.size() > 0) {
                this.print0(this.ucase ? " AS " : " as ");
            }
            this.print0(alias);
        }
        if (columns != null && columns.size() > 0) {
            this.print(" (");
            this.printAndAccept(columns, ", ");
            this.print(')');
        }
        if (this.isPrettyFormat() && x.hasAfterComment()) {
            this.print(' ');
            this.printlnComment(x.getAfterCommentsDirect());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectStatement stmt) {
        final List<SQLCommentHint> headHints = stmt.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        final SQLSelect select = stmt.getSelect();
        if (select != null) {
            this.visit(select);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        final int index = x.getIndex();
        if (index < 0 || this.inputParameters == null || index >= this.inputParameters.size()) {
            this.print0(x.getName());
            return false;
        }
        final Object param = this.inputParameters.get(index);
        final SQLObject parent = x.getParent();
        boolean in;
        if (parent instanceof SQLInListExpr) {
            in = true;
        }
        else if (parent instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)parent;
            in = (binaryOpExpr.getOperator() == SQLBinaryOperator.Equality);
        }
        else {
            in = false;
        }
        if (in && param instanceof Collection) {
            boolean first = true;
            for (final Object item : (Collection)param) {
                if (!first) {
                    this.print0(", ");
                }
                this.printParameter(item);
                first = false;
            }
        }
        else {
            this.printParameter(param);
        }
        return false;
    }
    
    public void printParameter(final Object param) {
        if (param == null) {
            this.print0(this.ucase ? "NULL" : "null");
            return;
        }
        if (param instanceof Number || param instanceof Boolean || param instanceof Temporal) {
            this.print0(param.toString());
            return;
        }
        if (param instanceof String) {
            final SQLCharExpr charExpr = new SQLCharExpr((String)param);
            this.visit(charExpr);
            return;
        }
        if (param instanceof Date) {
            this.print((Date)param);
            return;
        }
        if (param instanceof InputStream) {
            this.print0("'<InputStream>'");
            return;
        }
        if (param instanceof Reader) {
            this.print0("'<Reader>'");
            return;
        }
        if (param instanceof Blob) {
            this.print0("'<Blob>'");
            return;
        }
        if (param instanceof NClob) {
            this.print0("'<NClob>'");
            return;
        }
        if (param instanceof Clob) {
            this.print0("'<Clob>'");
            return;
        }
        if (param instanceof byte[]) {
            final byte[] bytes = (byte[])param;
            final int bytesLen = bytes.length;
            final char[] chars = new char[bytesLen * 2 + 3];
            chars[0] = 'x';
            chars[1] = '\'';
            for (int i = 0; i < bytes.length; ++i) {
                final int a = bytes[i] & 0xFF;
                final int b0 = a >> 4;
                final int b2 = a & 0xF;
                chars[i * 2 + 2] = (char)(b0 + ((b0 < 10) ? 48 : 55));
                chars[i * 2 + 3] = (char)(b2 + ((b2 < 10) ? 48 : 55));
            }
            chars[chars.length - 1] = '\'';
            this.print0(new String(chars));
            return;
        }
        if (param instanceof Character) {
            this.print('\'');
            this.print((char)param);
            this.print('\'');
            return;
        }
        this.print0("'" + param.getClass().getName() + "'");
    }
    
    @Override
    public boolean visit(final SQLDropTableStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "DROP " : "drop ");
        final List<SQLCommentHint> hints = x.getHints();
        if (hints != null) {
            this.printAndAccept(hints, " ");
            this.print(' ');
        }
        if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL " : "external ");
        }
        if (x.isDropPartition()) {
            this.print0(this.ucase ? "PARTITIONED " : "partitioned ");
        }
        if (x.isTemporary()) {
            this.print0(this.ucase ? "TEMPORARY TABLE " : "temporary table ");
        }
        else {
            this.print0(this.ucase ? "TABLE " : "table ");
        }
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isCascade()) {
            this.printCascade();
        }
        if (x.isRestrict()) {
            this.print0(this.ucase ? " RESTRICT" : " restrict");
        }
        if (x.isPurge()) {
            this.print0(this.ucase ? " PURGE" : " purge");
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    protected void printCascade() {
        this.print0(this.ucase ? " CASCADE" : " cascade");
    }
    
    @Override
    public boolean visit(final SQLDropViewStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "DROP VIEW " : "drop view ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isCascade()) {
            this.printCascade();
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropEventStatement x) {
        this.print0(this.ucase ? "DROP EVENT " : "drop event ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printExpr(x.getName(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropResourceStatement x) {
        this.print0(this.ucase ? "DROP RESOURCE " : "drop resource ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printExpr(x.getName(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropResourceGroupStatement x) {
        this.print0(this.ucase ? "DROP RESOURCE GROUP " : "drop resource group ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printExpr(x.getName(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLListResourceGroupStatement x) {
        this.print0(this.ucase ? "LIST RESOURCE GROUP" : "list resource group");
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnDefinition x) {
        final boolean parameterized = this.parameterized;
        this.parameterized = false;
        x.getName().accept(this);
        final SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            this.print(' ');
            dataType.accept(this);
        }
        if (x.getDefaultExpr() != null) {
            this.visitColumnDefault(x);
        }
        if (x.isAutoIncrement()) {
            this.print0(this.ucase ? " AUTO_INCREMENT" : " auto_increment");
        }
        final AutoIncrementType sequenceType = x.getSequenceType();
        if (sequenceType != null) {
            this.print0(" ");
            this.print0(this.ucase ? sequenceType.getKeyword() : sequenceType.getKeyword().toLowerCase());
        }
        for (final SQLColumnConstraint item : x.getConstraints()) {
            final boolean newLine = item instanceof SQLForeignKeyConstraint || item instanceof SQLPrimaryKey || item instanceof SQLColumnCheck || item instanceof SQLColumnCheck || item.getName() != null;
            if (newLine) {
                ++this.indentCount;
                this.println();
            }
            else {
                this.print(' ');
            }
            item.accept(this);
            if (newLine) {
                --this.indentCount;
            }
        }
        final SQLExpr generatedAlawsAs = x.getGeneratedAlawsAs();
        if (generatedAlawsAs != null) {
            this.print0(this.ucase ? " GENERATED ALWAYS AS " : " generated always as ");
            this.printExpr(generatedAlawsAs, parameterized);
        }
        final SQLColumnDefinition.Identity identity = x.getIdentity();
        if (identity != null) {
            if (this.dbType == DbType.h2) {
                this.print0(this.ucase ? " AS " : " as ");
            }
            else {
                this.print(' ');
            }
            identity.accept(this);
        }
        if (x.isVirtual()) {
            this.print0(this.ucase ? " VIRTUAL" : " virtual");
        }
        if (x.isVisible()) {
            this.print0(this.ucase ? " VISIBLE" : " visible");
        }
        final Boolean enable = x.getEnable();
        if (enable != null && enable) {
            this.print0(this.ucase ? " ENABLE" : " enable");
        }
        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }
        final List<SQLAssignItem> mappedBy = x.getMappedByDirect();
        if (mappedBy != null && mappedBy.size() > 0) {
            this.print0(this.ucase ? " MAPPED BY (" : " mapped by (");
            this.printAndAccept(mappedBy, ", ");
            this.print0(this.ucase ? ")" : ")");
        }
        final List<SQLAssignItem> colProperties = x.getColPropertiesDirect();
        if (colProperties != null && colProperties.size() > 0) {
            this.print0(this.ucase ? " COLPROPERTIES (" : " colproperties (");
            this.printAndAccept(colProperties, ", ");
            this.print0(this.ucase ? ")" : ")");
        }
        if (x.getEncode() != null) {
            this.print0(this.ucase ? " ENCODE=" : " encode=");
            x.getEncode().accept(this);
        }
        if (x.getCompression() != null) {
            this.print0(this.ucase ? " COMPRESSION=" : " compression=");
            x.getCompression().accept(this);
        }
        this.parameterized = parameterized;
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnDefinition.Identity x) {
        this.print0(this.ucase ? "IDENTITY" : "identity");
        final Integer seed = x.getSeed();
        if (seed != null) {
            this.print0(" (");
            this.print(seed);
            this.print0(", ");
            this.print(x.getIncrement());
            this.print(')');
        }
        return false;
    }
    
    protected void visitColumnDefault(final SQLColumnDefinition x) {
        this.print0(this.ucase ? " DEFAULT " : " default ");
        this.printExpr(x.getDefaultExpr(), false);
    }
    
    @Override
    public boolean visit(final SQLDeleteStatement x) {
        final SQLTableSource from = x.getFrom();
        final String alias = x.getAlias();
        if (from == null) {
            this.print0(this.ucase ? "DELETE FROM " : "delete from ");
            this.printTableSourceExpr(x.getTableName());
            if (alias != null) {
                this.print(' ');
                this.print0(alias);
            }
        }
        else {
            this.print0(this.ucase ? "DELETE " : "delete ");
            this.printTableSourceExpr(x.getTableName());
            this.print0(this.ucase ? " FROM " : " from ");
            from.accept(this);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            ++this.indentCount;
            where.accept(this);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCurrentOfCursorExpr x) {
        this.print0(this.ucase ? "CURRENT OF " : "current of ");
        this.printExpr(x.getCursorName(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLInsertStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (x.getInsertBeforeCommentsDirect() != null) {
            this.printlnComments(x.getInsertBeforeCommentsDirect());
        }
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            this.visit(with);
            this.println();
        }
        if (x.isUpsert()) {
            this.print0(this.ucase ? "UPSERT INTO " : "upsert into ");
        }
        else if (x.isOverwrite() && this.dbType == DbType.odps) {
            this.print0(this.ucase ? "INSERT OVERWRITE " : "insert overwrite ");
        }
        else {
            this.print0(this.ucase ? "INSERT INTO " : "insert into ");
        }
        x.getTableSource().accept(this);
        final String columnsString = x.getColumnsString();
        if (columnsString != null) {
            this.print0(columnsString);
        }
        else {
            this.printInsertColumns(x.getColumns());
        }
        if (!x.getValuesList().isEmpty()) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            this.printAndAccept(x.getValuesList(), ", ");
        }
        else if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        return false;
    }
    
    public void printInsertColumns(final List<SQLExpr> columns) {
        final int size = columns.size();
        if (size > 0) {
            if (size > 5) {
                ++this.indentCount;
                this.println();
            }
            else {
                this.print(' ');
            }
            this.print('(');
            for (int i = 0; i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        this.println();
                    }
                    this.print0(", ");
                }
                final SQLExpr column = columns.get(i);
                if (column instanceof SQLIdentifierExpr) {
                    this.visit((SQLIdentifierExpr)column);
                }
                else {
                    this.printExpr(column, this.parameterized);
                }
                final String dataType = (String)column.getAttribute("dataType");
                if (dataType != null) {
                    this.print(' ');
                    this.print(dataType);
                }
            }
            this.print(')');
            if (size > 5) {
                --this.indentCount;
            }
        }
    }
    
    @Override
    public boolean visit(final SQLUpdateSetItem x) {
        this.printExpr(x.getColumn(), this.parameterized);
        this.print0(" = ");
        this.printExpr(x.getValue(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLUpdateStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (x.getWith() != null) {
            x.getWith().accept(this);
            this.println();
        }
        this.print0(this.ucase ? "UPDATE " : "update ");
        this.printTableSource(x.getTableSource());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLUpdateSetItem item = x.getItems().get(i);
            this.visit(item);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where, this.parameterized);
            --this.indentCount;
        }
        return false;
    }
    
    protected void printTableElements(final List<SQLTableElement> tableElementList) {
        final int size = tableElementList.size();
        if (size == 0) {
            return;
        }
        this.print0(" (");
        ++this.indentCount;
        this.println();
        for (int i = 0; i < size; ++i) {
            final SQLTableElement element = tableElementList.get(i);
            element.accept(this);
            if (i != size - 1) {
                this.print(',');
            }
            if (this.isPrettyFormat() && element.hasAfterComment()) {
                this.print(' ');
                this.printlnComment(element.getAfterCommentsDirect());
            }
            if (i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        this.println();
        this.print(')');
    }
    
    @Override
    public boolean visit(final SQLCreateTableStatement x) {
        this.printCreateTable(x, true);
        final SQLPartitionBy partitionBy = x.getPartitioning();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        final List<SQLAssignItem> options = x.getTableOptions();
        if (options.size() > 0) {
            this.println();
            this.print0(this.ucase ? "WITH (" : "with (");
            this.printAndAccept(options, ", ");
            this.print(')');
        }
        final SQLName tablespace = x.getTablespace();
        if (tablespace != null) {
            this.println();
            this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
            tablespace.accept(this);
        }
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE = " : " engine = ");
            engine.accept(this);
        }
        return false;
    }
    
    protected void printCreateTable(final SQLCreateTableStatement x, final boolean printSelect) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL " : "external ");
        }
        final SQLCreateTableStatement.Type tableType = x.getType();
        if (SQLCreateTableStatement.Type.GLOBAL_TEMPORARY.equals(tableType)) {
            this.print0(this.ucase ? "GLOBAL TEMPORARY " : "global temporary ");
        }
        else if (SQLCreateTableStatement.Type.LOCAL_TEMPORARY.equals(tableType)) {
            this.print0(this.ucase ? "LOCAL TEMPORARY " : "local temporary ");
        }
        else if (SQLCreateTableStatement.Type.SHADOW.equals(tableType)) {
            this.print0(this.ucase ? "SHADOW " : "shadow ");
        }
        if (x.isDimension()) {
            this.print0(this.ucase ? "DIMENSION " : "dimension ");
        }
        this.print0(this.ucase ? "TABLE " : "table ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        this.printTableSourceExpr(x.getName());
        this.printTableElements(x.getTableElementList());
        final SQLExprTableSource inherits = x.getInherits();
        if (inherits != null) {
            this.print0(this.ucase ? " INHERITS (" : " inherits (");
            inherits.accept(this);
            this.print(')');
        }
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.print0(this.ucase ? " STORE AS " : " store as ");
            this.printExpr(storedAs, this.parameterized);
        }
        final SQLSelect select = x.getSelect();
        if (printSelect && select != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            this.visit(select);
        }
    }
    
    public boolean visit(final SQLUniqueConstraint x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "UNIQUE (" : "unique (");
        final List<SQLSelectOrderByItem> columns = x.getColumns();
        for (int i = 0, size = columns.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            this.visit(columns.get(i));
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLNotNullConstraint x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "NOT NULL" : "not null");
        final List<SQLCommentHint> hints = x.hints;
        if (hints != null) {
            this.print(' ');
            for (final SQLCommentHint hint : hints) {
                hint.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLNullConstraint x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "NULL" : "null");
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQuery x) {
        final SQLUnionOperator operator = x.getOperator();
        final List<SQLSelectQuery> relations = x.getRelations();
        if (relations.size() > 2) {
            for (int i = 0; i < relations.size(); ++i) {
                if (i != 0) {
                    this.println();
                    this.print0(this.ucase ? operator.name : operator.name_lcase);
                    this.println();
                }
                final SQLSelectQuery item = relations.get(i);
                item.accept(this);
            }
            final SQLOrderBy orderBy = x.getOrderBy();
            if (orderBy != null) {
                this.println();
                orderBy.accept(this);
            }
            final SQLLimit limit = x.getLimit();
            if (limit != null) {
                this.println();
                limit.accept(this);
            }
            return false;
        }
        final SQLSelectQuery left = x.getLeft();
        final SQLSelectQuery right = x.getRight();
        final boolean bracket = x.isParenthesized() && !(x.getParent() instanceof SQLUnionQueryTableSource);
        final SQLOrderBy orderBy2 = x.getOrderBy();
        if (!bracket && left instanceof SQLUnionQuery && ((SQLUnionQuery)left).getOperator() == operator && !right.isParenthesized() && x.getLimit() == null && orderBy2 == null) {
            SQLUnionQuery leftUnion = (SQLUnionQuery)left;
            if (right instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock rightQueryBlock = (SQLSelectQueryBlock)right;
                if (rightQueryBlock.getOrderBy() != null || rightQueryBlock.getLimit() != null) {
                    rightQueryBlock.setParenthesized(true);
                }
            }
            final List<SQLSelectQuery> rights = new ArrayList<SQLSelectQuery>();
            rights.add(right);
            if (leftUnion.getRelations().size() > 2) {
                rights.addAll(leftUnion.getRelations());
            }
            else {
                SQLSelectQuery leftLeft;
                SQLSelectQuery leftRight;
                while (true) {
                    leftLeft = leftUnion.getLeft();
                    leftRight = leftUnion.getRight();
                    if (leftRight instanceof SQLSelectQueryBlock) {
                        final SQLSelectQueryBlock leftRightQueryBlock = (SQLSelectQueryBlock)leftRight;
                        if (leftRightQueryBlock.getOrderBy() != null || leftRightQueryBlock.getLimit() != null) {
                            leftRightQueryBlock.setParenthesized(true);
                        }
                    }
                    if (leftUnion.isParenthesized() || leftUnion.getOrderBy() != null || leftLeft.isParenthesized() || leftRight.isParenthesized() || !(leftLeft instanceof SQLUnionQuery) || ((SQLUnionQuery)leftLeft).getOperator() != operator) {
                        break;
                    }
                    rights.add(leftRight);
                    leftUnion = (SQLUnionQuery)leftLeft;
                }
                rights.add(leftRight);
                rights.add(leftLeft);
            }
            for (int j = rights.size() - 1; j >= 0; --j) {
                final SQLSelectQuery item2 = rights.get(j);
                item2.accept(this);
                if (j > 0) {
                    this.println();
                    this.print0(this.ucase ? operator.name : operator.name_lcase);
                    this.println();
                }
            }
            return false;
        }
        if (bracket) {
            this.print('(');
        }
        if (left != null) {
            if (left.getClass() == SQLUnionQuery.class) {
                final SQLUnionQuery leftUnion = (SQLUnionQuery)left;
                if (leftUnion.getRelations().size() > 2) {
                    this.visit(leftUnion);
                }
                else {
                    final SQLSelectQuery leftLeft2 = leftUnion.getLeft();
                    final SQLSelectQuery leftRigt = leftUnion.getRight();
                    if (!leftUnion.isParenthesized() && leftUnion.getRight() instanceof SQLSelectQueryBlock && leftUnion.getLeft() != null && leftUnion.getLimit() == null && leftUnion.getOrderBy() == null) {
                        if (leftLeft2.getClass() == SQLUnionQuery.class) {
                            this.visit((SQLUnionQuery)leftLeft2);
                        }
                        else {
                            this.printQuery(leftLeft2);
                        }
                        this.println();
                        this.print0(this.ucase ? leftUnion.getOperator().name : leftUnion.getOperator().name_lcase);
                        this.println();
                        leftRigt.accept(this);
                    }
                    else {
                        this.visit(leftUnion);
                    }
                }
            }
            else {
                left.accept(this);
            }
        }
        if (right == null) {
            return false;
        }
        this.println();
        this.print0(this.ucase ? operator.name : operator.name_lcase);
        this.println();
        boolean needParen = false;
        if (!right.isParenthesized() && right instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock rightQuery = (SQLSelectQueryBlock)right;
            if (rightQuery.getOrderBy() != null || rightQuery.getLimit() != null) {
                needParen = true;
            }
        }
        if (needParen) {
            this.print('(');
            right.accept(this);
            this.print(')');
        }
        else {
            right.accept(this);
        }
        if (orderBy2 != null) {
            this.println();
            orderBy2.accept(this);
        }
        final SQLLimit limit2 = x.getLimit();
        if (limit2 != null) {
            this.println();
            limit2.accept(this);
        }
        if (bracket) {
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnaryExpr x) {
        this.print0(x.getOperator().name);
        final SQLExpr expr = x.getExpr();
        switch (x.getOperator()) {
            case BINARY:
            case Prior:
            case ConnectByRoot: {
                this.print(' ');
                expr.accept(this);
                return false;
            }
            default: {
                if (expr instanceof SQLBinaryOpExpr || expr instanceof SQLBinaryOpExprGroup || expr instanceof SQLUnaryExpr || expr instanceof SQLNotExpr || expr instanceof SQLBetweenExpr || expr instanceof SQLInListExpr || expr instanceof SQLInSubQueryExpr) {
                    this.incrementIndent();
                    this.print('(');
                    expr.accept(this);
                    this.print(')');
                    this.decrementIndent();
                }
                else {
                    expr.accept(this);
                }
                return false;
            }
        }
    }
    
    @Override
    public boolean visit(final SQLHexExpr x) {
        if (this.parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
            return false;
        }
        this.print0("0x");
        this.print0(x.getHex());
        final String charset = (String)x.getAttribute("USING");
        if (charset != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(charset);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSetStatement x) {
        if (x.getHeadHintsDirect() != null) {
            for (final SQLCommentHint hint : x.getHeadHintsDirect()) {
                hint.accept(this);
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        final boolean printSet = x.getAttribute("parser.set") == Boolean.TRUE || !JdbcUtils.isOracleDbType(this.dbType);
        if (printSet) {
            this.print0(this.ucase ? "SET " : "set ");
        }
        final SQLSetStatement.Option option = x.getOption();
        if (option != null) {
            this.print(option.name());
            this.print(' ');
        }
        this.printAndAccept(x.getItems(), ", ");
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAssignItem x) {
        x.getTarget().accept(this);
        final SQLExpr value = x.getValue();
        if (value != null) {
            this.print0(" = ");
            value.accept(this);
        }
        else if (this.dbType == DbType.odps) {
            this.print0(" = ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCallStatement x) {
        if (x.isBrace()) {
            this.print('{');
        }
        if (x.getOutParameter() != null) {
            x.getOutParameter().accept(this);
            this.print0(" = ");
        }
        this.print0(this.ucase ? "CALL " : "call ");
        x.getProcedureName().accept(this);
        this.print('(');
        this.printAndAccept(x.getParameters(), ", ");
        this.print(')');
        if (x.isBrace()) {
            this.print('}');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource x) {
        final SQLCommentHint hint = x.getHint();
        if (hint != null) {
            hint.accept(this);
        }
        final SQLTableSource left = x.getLeft();
        final String alias = x.getAlias();
        if (alias != null) {
            this.print('(');
        }
        final SQLJoinTableSource.JoinType joinType = x.getJoinType();
        if (left instanceof SQLJoinTableSource && ((SQLJoinTableSource)left).getJoinType() == SQLJoinTableSource.JoinType.COMMA && joinType != SQLJoinTableSource.JoinType.COMMA && DbType.postgresql != this.dbType && ((SQLJoinTableSource)left).getAttribute("ads.comma_join") == null) {
            this.print('(');
            this.printTableSource(left);
            this.print(')');
        }
        else {
            this.printTableSource(left);
        }
        ++this.indentCount;
        if (joinType == SQLJoinTableSource.JoinType.COMMA) {
            this.print(',');
        }
        else {
            this.println();
            final boolean asof = x.isAsof();
            if (asof) {
                this.print0(this.ucase ? " ASOF " : " asof");
            }
            if (x.isNatural()) {
                this.print0(this.ucase ? "NATURAL " : "natural ");
            }
            if (x.isGlobal()) {
                this.print0(this.ucase ? "GLOBAL " : "global ");
            }
            this.printJoinType(joinType);
        }
        this.print(' ');
        final SQLTableSource right = x.getRight();
        if (right instanceof SQLJoinTableSource) {
            this.print('(');
            this.printTableSource(right);
            this.print(')');
        }
        else {
            this.printTableSource(right);
        }
        final SQLExpr condition = x.getCondition();
        if (condition != null) {
            boolean newLine = false;
            if (right instanceof SQLSubqueryTableSource) {
                newLine = true;
            }
            else if (condition instanceof SQLBinaryOpExpr) {
                final SQLBinaryOperator op = ((SQLBinaryOpExpr)condition).getOperator();
                if (op == SQLBinaryOperator.BooleanAnd || op == SQLBinaryOperator.BooleanOr) {
                    newLine = true;
                }
            }
            else if (condition instanceof SQLBinaryOpExprGroup) {
                newLine = true;
            }
            if (newLine) {
                this.println();
            }
            else {
                this.print(' ');
            }
            ++this.indentCount;
            this.print0(this.ucase ? "ON " : "on ");
            this.printExpr(condition, this.parameterized);
            --this.indentCount;
        }
        if (x.getUsing().size() > 0) {
            this.print0(this.ucase ? " USING (" : " using (");
            this.printAndAccept(x.getUsing(), ", ");
            this.print(')');
        }
        if (alias != null) {
            this.print0(this.ucase ? ") AS " : ") as ");
            this.print0(alias);
        }
        final SQLJoinTableSource.UDJ udj = x.getUdj();
        if (udj != null) {
            this.println();
            udj.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource.UDJ x) {
        this.print0(this.ucase ? "USING " : "using ");
        x.getFunction().accept(this);
        this.print('(');
        this.printAndAccept(x.getArguments(), ", ");
        this.print0(") ");
        this.print0(x.getAlias());
        this.print0(this.ucase ? " AS (" : " as (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print0(")");
        final List<SQLAssignItem> properties = x.getProperties();
        if (!properties.isEmpty()) {
            this.print0(this.ucase ? " WITH UDFPROPERTIES (" : " with udfproperties (");
            this.printAndAccept(properties, ", ");
            this.print(')');
        }
        return false;
    }
    
    protected void printJoinType(final SQLJoinTableSource.JoinType joinType) {
        this.print0(this.ucase ? joinType.name : joinType.name_lcase);
    }
    
    @Override
    public boolean visit(final SQLInsertStatement.ValuesClause x) {
        return this.visit(x, this.parameters);
    }
    
    public boolean visit(final SQLInsertStatement.ValuesClause x, final List<Object> parameters) {
        if (!this.parameterized && this.isEnabled(VisitorFeature.OutputUseInsertValueClauseOriginalString) && x.getOriginalString() != null) {
            this.print0(x.getOriginalString());
            return false;
        }
        final int xReplaceCount = x.getReplaceCount();
        final List<SQLExpr> values = x.getValues();
        this.replaceCount += xReplaceCount;
        if (xReplaceCount == values.size() && xReplaceCount < SQLASTOutputVisitor.variantValuesCache.length) {
            String variantValues;
            if (this.indentCount == 0) {
                variantValues = SQLASTOutputVisitor.variantValuesCache_1[xReplaceCount];
            }
            else {
                variantValues = SQLASTOutputVisitor.variantValuesCache[xReplaceCount];
            }
            this.print0(variantValues);
            return false;
        }
        if (this.inputParameters != null && this.inputParameters.size() > 0 && this.inputParameters.get(0) instanceof Collection && this.inputParameters.get(0).size() == x.getValues().size()) {
            this.incrementIndent();
            for (int i = 0; i < this.inputParameters.size(); ++i) {
                final Collection params = this.inputParameters.get(i);
                if (i != 0) {
                    this.print(',');
                    this.println();
                }
                this.print('(');
                int j = 0;
                for (final Object item : params) {
                    if (j != 0) {
                        if (j % 5 == 0) {
                            this.println();
                        }
                        this.print0(", ");
                    }
                    this.printParameter(item);
                    ++j;
                }
                this.print(')');
            }
            this.decrementIndent();
            return false;
        }
        this.print('(');
        ++this.indentCount;
        for (int i = 0, size = values.size(); i < size; ++i) {
            if (i != 0) {
                if (i % 5 == 0) {
                    this.println();
                }
                this.print0(", ");
            }
            final Object value = values.get(i);
            if (value instanceof SQLExpr) {
                final SQLExpr expr = values.get(i);
                if (this.parameterized && (expr instanceof SQLIntegerExpr || expr instanceof SQLBooleanExpr || expr instanceof SQLNumberExpr || expr instanceof SQLCharExpr || expr instanceof SQLNCharExpr || expr instanceof SQLTimestampExpr || expr instanceof SQLDateExpr || expr instanceof SQLTimeExpr)) {
                    this.print('?');
                    this.incrementReplaceCunt();
                    if (parameters != null) {
                        ExportParameterVisitorUtils.exportParameter(parameters, expr);
                    }
                }
                else if (this.parameterized && expr instanceof SQLNullExpr) {
                    this.print('?');
                    if (parameters != null) {
                        parameters.add(null);
                    }
                    this.incrementReplaceCunt();
                }
                else if (expr instanceof SQLVariantRefExpr) {
                    this.visit((SQLVariantRefExpr)expr);
                }
                else {
                    expr.accept(this);
                }
            }
            else if (value instanceof Integer) {
                this.print((int)value);
            }
            else if (value instanceof Long) {
                this.print((long)value);
            }
            else if (value instanceof String) {
                this.printChars((String)value);
            }
            else if (value instanceof Float) {
                this.print((float)value);
            }
            else if (value instanceof Double) {
                this.print((double)value);
            }
            else if (value instanceof Date) {
                this.print((Date)value);
            }
            else if (value instanceof BigDecimal) {
                this.print(value.toString());
            }
            else {
                if (value != null) {
                    throw new UnsupportedOperationException();
                }
                this.print0(this.ucase ? "NULL" : "null");
            }
        }
        --this.indentCount;
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLSomeExpr x) {
        this.print0(this.ucase ? "SOME (" : "some (");
        ++this.indentCount;
        this.println();
        x.getSubQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAnyExpr x) {
        this.print0(this.ucase ? "ANY (" : "any (");
        ++this.indentCount;
        this.println();
        x.getSubQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAllExpr x) {
        this.print0(this.ucase ? "ALL (" : "all (");
        ++this.indentCount;
        this.println();
        x.getSubQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLInSubQueryExpr x) {
        x.getExpr().accept(this);
        if (x.isNot()) {
            this.print0(this.ucase ? " NOT IN (" : " not in (");
        }
        else if (x.isGlobal()) {
            this.print0(this.ucase ? " GLOBAL IN (" : " global in (");
        }
        else {
            this.print0(this.ucase ? " IN (" : " in (");
        }
        ++this.indentCount;
        this.println();
        x.getSubQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        if (x.getHint() != null) {
            x.getHint().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLListExpr x) {
        this.print('(');
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        this.print('(');
        ++this.indentCount;
        this.println();
        if (x.getHintsSize() > 0) {
            this.printAndAccept(x.getHints(), " ");
            this.println();
        }
        this.visit(x.getSelect());
        --this.indentCount;
        this.println();
        this.print(')');
        final List<SQLName> columns = x.getColumns();
        final String alias = x.getAlias();
        if (alias != null) {
            if (columns.size() > 0) {
                this.print0(" AS ");
            }
            else {
                this.print(' ');
            }
            this.print0(alias);
        }
        if (columns.size() > 0) {
            this.print0(" (");
            for (int i = 0; i < columns.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.printExpr(columns.get(i));
            }
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnnestTableSource x) {
        this.print0(this.ucase ? "UNNEST(" : "unnest(");
        final List<SQLExpr> items = x.getItems();
        this.printAndAccept(items, ", ");
        this.print(')');
        if (x.isOrdinality()) {
            this.print0(this.ucase ? " WITH ORDINALITY" : " with ordinality");
        }
        final List<SQLName> columns = x.getColumns();
        final String alias = x.getAlias();
        if (alias != null) {
            if (columns.size() > 0) {
                this.print0(this.ucase ? " AS " : " as ");
            }
            else {
                this.print(' ');
            }
            this.print0(alias);
        }
        if (columns.size() > 0) {
            this.print0(" (");
            for (int i = 0; i < columns.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.printExpr(columns.get(i));
            }
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTruncateStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        this.print0(this.ucase ? "TRUNCATE TABLE " : "truncate table ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.getPartitions().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }
        if (x.isPartitionAll()) {
            this.print0(this.ucase ? " PARTITION ALL" : " partition all");
        }
        else if (x.getPartitionsForADB().size() > 0) {
            this.print0(this.ucase ? " PARTITION " : " partition ");
            this.printAndAccept(x.getPartitionsForADB(), ", ");
        }
        if (x.isDropStorage()) {
            this.print0(this.ucase ? " DROP STORAGE" : " drop storage");
        }
        if (x.isReuseStorage()) {
            this.print0(this.ucase ? " REUSE STORAGE" : " reuse storage");
        }
        if (x.isIgnoreDeleteTriggers()) {
            this.print0(this.ucase ? " IGNORE DELETE TRIGGERS" : " ignore delete triggers");
        }
        if (x.isRestrictWhenDeleteTriggers()) {
            this.print0(this.ucase ? " RESTRICT WHEN DELETE TRIGGERS" : " restrict when delete triggers");
        }
        if (x.isContinueIdentity()) {
            this.print0(this.ucase ? " CONTINUE IDENTITY" : " continue identity");
        }
        if (x.isImmediate()) {
            this.print0(this.ucase ? " IMMEDIATE" : " immediate");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDefaultExpr x) {
        this.print0(this.ucase ? "DEFAULT" : "default");
        return false;
    }
    
    @Override
    public void endVisit(final SQLCommentStatement x) {
    }
    
    @Override
    public boolean visit(final SQLCommentStatement x) {
        this.print0(this.ucase ? "COMMENT ON " : "comment on ");
        if (x.getType() != null) {
            this.print0(x.getType().name());
            this.print(' ');
        }
        x.getOn().accept(this);
        this.print0(this.ucase ? " IS " : " is ");
        x.getComment().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLUseStatement x) {
        this.print0(this.ucase ? "USE " : "use ");
        x.getDatabase().accept(this);
        return false;
    }
    
    protected boolean isOdps() {
        return DbType.odps == this.dbType;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        if (DbType.odps == this.dbType || DbType.hive == this.dbType) {
            this.print0(this.ucase ? "ADD COLUMNS (" : "add columns (");
        }
        else {
            this.print0(this.ucase ? "ADD (" : "add (");
        }
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        final Boolean restrict = x.getRestrict();
        if (restrict != null && restrict) {
            this.print0(this.ucase ? " RESTRICT" : " restrict");
        }
        if (x.isCascade()) {
            this.print0(this.ucase ? " CASCADE" : " cascade");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableReplaceColumn x) {
        this.print0(this.ucase ? "REPLACE COLUMNS (" : "replace columns (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropColumnItem x) {
        this.print0(this.ucase ? "DROP COLUMN " : "drop column ");
        this.printAndAccept(x.getColumns(), ", ");
        if (x.isCascade()) {
            this.print0(this.ucase ? " CASCADE" : " cascade");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDeleteByCondition x) {
        this.printUcase("DELETE WHERE ");
        x.getWhere().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(final SQLAlterTableAddColumn x) {
    }
    
    @Override
    public boolean visit(final SQLDropIndexStatement x) {
        this.print0(this.ucase ? "DROP INDEX " : "drop index ");
        x.getIndexName().accept(this);
        final SQLExprTableSource table = x.getTableName();
        if (table != null) {
            this.print0(this.ucase ? " ON " : " on ");
            table.accept(this);
        }
        final SQLExpr algorithm = x.getAlgorithm();
        if (algorithm != null) {
            this.print0(this.ucase ? " ALGORITHM " : " algorithm ");
            algorithm.accept(this);
        }
        final SQLExpr lockOption = x.getLockOption();
        if (lockOption != null) {
            this.print0(this.ucase ? " LOCK " : " lock ");
            lockOption.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropLogFileGroupStatement x) {
        this.print0(this.ucase ? "DROP LOGFILE GROUP " : "drop logfile group ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropServerStatement x) {
        this.print0(this.ucase ? "DROP SERVER " : "drop server ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTypeStatement x) {
        this.print0(this.ucase ? "DROP TYPE " : "drop type ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropSynonymStatement x) {
        if (x.isPublic()) {
            this.print0(this.ucase ? "DROP PUBLIC SYNONYM " : "drop public synonym ");
        }
        else {
            this.print0(this.ucase ? "DROP SYNONYM " : "drop synonym ");
        }
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        if (x.isForce()) {
            this.print0(this.ucase ? " FORCE" : " force");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSavePointStatement x) {
        this.print0(this.ucase ? "SAVEPOINT" : "savepoint");
        final SQLExpr name = x.getName();
        if (name != null) {
            this.print(' ');
            name.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLReleaseSavePointStatement x) {
        this.print0(this.ucase ? "RELEASE SAVEPOINT " : "release savepoint ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLRollbackStatement x) {
        this.print0(this.ucase ? "ROLLBACK" : "rollback");
        if (x.getTo() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getTo().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCommentHint x) {
        if (x.hasBeforeComment() && !this.parameterized) {
            this.printlnComment(x.getBeforeCommentsDirect());
            this.println();
        }
        this.print0("/*");
        this.print0(x.getText());
        this.print0("*/");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateDatabaseStatement x) {
        if (x.getHeadHintsDirect() != null) {
            for (final SQLCommentHint hint : x.getHeadHintsDirect()) {
                hint.accept(this);
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isPhysical()) {
            this.print0(this.ucase ? "PHYSICAL " : "physical ");
        }
        this.print0(this.ucase ? "DATABASE " : "database ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);
        if (x.getCharacterSet() != null) {
            this.print0(this.ucase ? " CHARACTER SET " : " character set ");
            this.print0(x.getCharacterSet());
        }
        final String collate = x.getCollate();
        if (collate != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(collate);
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment, false);
        }
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.print0(this.ucase ? " LOCATION " : " location ");
            this.printExpr(location, false);
        }
        if (x.getDbProperties().size() > 0) {
            if (this.dbType == DbType.mysql || this.dbType == DbType.presto || this.dbType == DbType.trino || this.dbType == DbType.ads || this.dbType == DbType.mariadb) {
                this.println();
                this.print0(this.ucase ? "WITH (" : "with (");
            }
            else if (this.dbType == DbType.hive) {
                this.println();
                this.print0(this.ucase ? "WITH DBPROPERTIES (" : "with dbproperties (");
            }
            else {
                this.println();
                this.print0(this.ucase ? "WITH PROPERTIES (" : "with properties (");
            }
            this.incrementIndent();
            this.println();
            this.printlnAndAccept(x.getDbProperties(), ",");
            this.decrementIndent();
            this.println();
            this.print(')');
        }
        if (x.getUser() != null) {
            this.print0(this.ucase ? " FOR " : " for ");
            this.print0("'" + x.getUser() + "'");
        }
        if (x.getOptions().size() > 0) {
            this.print0(this.ucase ? " OPTIONS (" : " options (");
            for (final Map.Entry<String, SQLExpr> option : x.getOptions().entrySet()) {
                this.print0(option.getKey());
                this.print0("=");
                this.printExpr(option.getValue(), false);
                this.print0(" ");
            }
            this.print(')');
        }
        final List<List<SQLAssignItem>> storedBy = x.getStoredBy();
        if (storedBy.size() != 0) {
            this.println();
            this.print0(this.ucase ? "STORED BY " : "stored by ");
            this.incrementIndent();
            for (int i = 0; i < storedBy.size(); ++i) {
                final List<SQLAssignItem> items = storedBy.get(i);
                if (i != 0) {
                    this.print(',');
                    this.println();
                }
                this.print('(');
                this.printAndAccept(items, ", ");
                this.print(')');
            }
            this.decrementIndent();
        }
        final SQLExpr storedIn = x.getStoredIn();
        if (storedIn != null) {
            this.println();
            this.print0(this.ucase ? "STORED IN " : "stored in ");
            this.printExpr(storedIn);
            final List<SQLAssignItem> storedOn = x.getStoredOn();
            if (storedOn.size() > 0) {
                this.print0(this.ucase ? " ON (" : " ON (");
                this.printAndAccept(storedOn, ", ");
                this.print(')');
            }
        }
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.println();
            this.print0(this.ucase ? "STORED AS " : "stored as ");
            this.printExpr(storedAs);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateTableGroupStatement x) {
        this.print0(this.ucase ? "CREATE TABLEGROUP " : "create tablegroup ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);
        final SQLExpr partitionNum = x.getPartitionNum();
        if (partitionNum != null) {
            this.print0(this.ucase ? " PARTITION NUM " : " partition num ");
            this.printExpr(partitionNum);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTableGroupStatement x) {
        this.print0(this.ucase ? "DROP TABLEGROUP " : "drop tablegroup ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableGroupStatement x) {
        this.print0(this.ucase ? "ALTER TABLEGROUP " : "alter tablegroup ");
        x.getName().accept(this);
        this.print0(" ");
        this.printAndAccept(x.getOptions(), " ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterSystemGetConfigStatement x) {
        this.print0(this.ucase ? "ALTER SYSTEM GET CONFIG " : "alter system get config ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterSystemSetConfigStatement x) {
        this.print0(this.ucase ? "ALTER SYSTEM SET COFNIG " : "alter system set config ");
        this.printAndAccept(x.getOptions(), " ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterViewStatement x) {
        this.print0(this.ucase ? "ALTER " : "alter ");
        ++this.indentCount;
        final String algorithm = x.getAlgorithm();
        if (algorithm != null && algorithm.length() > 0) {
            this.print0(this.ucase ? "ALGORITHM = " : "algorithm = ");
            this.print0(algorithm);
            this.println();
        }
        final SQLName definer = x.getDefiner();
        if (definer != null) {
            this.print0(this.ucase ? "DEFINER = " : "definer = ");
            definer.accept(this);
            this.println();
        }
        final String sqlSecurity = x.getSqlSecurity();
        if (sqlSecurity != null && sqlSecurity.length() > 0) {
            this.print0(this.ucase ? "SQL SECURITY " : "sql security ");
            this.print0(sqlSecurity);
            this.println();
        }
        --this.indentCount;
        this.print0(this.ucase ? "VIEW " : "view ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getTableSource().accept(this);
        final List<SQLTableElement> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print0(" (");
            ++this.indentCount;
            this.println();
            for (int i = 0; i < columns.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }
                columns.get(i).accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        if (x.getComment() != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }
        this.println();
        this.print0(this.ucase ? "AS" : "as");
        this.println();
        x.getSubQuery().accept(this);
        if (x.isWithCheckOption()) {
            this.println();
            this.print0(this.ucase ? "WITH CHECK OPTION" : "with check option");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateViewStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "OR REPLACE " : "or replace ");
        }
        ++this.indentCount;
        final String algorithm = x.getAlgorithm();
        if (algorithm != null && algorithm.length() > 0) {
            this.print0(this.ucase ? "ALGORITHM = " : "algorithm = ");
            this.print0(algorithm);
            this.println();
        }
        final SQLName definer = x.getDefiner();
        if (definer != null) {
            this.print0(this.ucase ? "DEFINER = " : "definer = ");
            definer.accept(this);
            this.println();
        }
        final String sqlSecurity = x.getSqlSecurity();
        if (sqlSecurity != null && sqlSecurity.length() > 0) {
            this.print0(this.ucase ? "SQL SECURITY = " : "sql security = ");
            this.print0(sqlSecurity);
            this.println();
        }
        --this.indentCount;
        this.print0(this.ucase ? "VIEW " : "view ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getTableSource().accept(this);
        if (x.isOnCluster()) {
            this.print0(this.ucase ? " ON CLUSTER" : " on cluster");
        }
        final SQLName to = x.getTo();
        if (to != null) {
            this.print0(this.ucase ? " TO " : " to ");
            to.accept(this);
        }
        final List<SQLTableElement> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print0(" (");
            ++this.indentCount;
            this.println();
            for (int i = 0; i < columns.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }
                columns.get(i).accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final SQLLiteralExpr comment = x.getComment();
        if (comment != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            comment.accept(this);
        }
        this.println();
        this.print0(this.ucase ? "AS" : "as");
        this.println();
        final SQLSelect subQuery = x.getSubQuery();
        if (subQuery != null) {
            subQuery.accept(this);
        }
        final SQLBlockStatement script = x.getScript();
        if (script != null) {
            script.accept(this);
        }
        if (x.isWithCheckOption()) {
            this.println();
            this.print0(this.ucase ? "WITH CHECK OPTION" : "with check option");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateViewStatement.Column x) {
        x.getExpr().accept(this);
        final SQLCharExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            comment.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropIndex x) {
        this.print0(this.ucase ? "DROP INDEX " : "drop index ");
        x.getIndexName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLOver x) {
        this.print0(this.ucase ? "(" : "(");
        if (x.getPartitionBy().size() > 0) {
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            this.printAndAccept(x.getPartitionBy(), ", ");
            this.print(' ');
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            orderBy.accept(this);
        }
        final SQLOrderBy distributeBy = x.getDistributeBy();
        if (distributeBy != null) {
            final List<SQLSelectOrderByItem> items = distributeBy.getItems();
            if (items.size() > 0) {
                this.print0(this.ucase ? "DISTRIBUTE BY " : "distribute by ");
                for (int i = 0, size = items.size(); i < size; ++i) {
                    if (i != 0) {
                        this.print0(", ");
                    }
                    final SQLSelectOrderByItem item = items.get(i);
                    this.visit(item);
                }
            }
        }
        final SQLOrderBy sortBy = x.getDistributeBy();
        if (sortBy != null) {
            final List<SQLSelectOrderByItem> items2 = sortBy.getItems();
            if (items2.size() > 0) {
                this.print0(this.ucase ? "SORT BY " : "sort by ");
                for (int j = 0, size2 = items2.size(); j < size2; ++j) {
                    if (j != 0) {
                        this.print0(", ");
                    }
                    final SQLSelectOrderByItem item2 = items2.get(j);
                    this.visit(item2);
                }
            }
        }
        if (x.getOf() != null) {
            this.print0(this.ucase ? " OF " : " of ");
            x.getOf().accept(this);
        }
        final SQLExpr windowingBetweenBegin = x.getWindowingBetweenBegin();
        final SQLExpr windowingBetweenEnd = x.getWindowingBetweenEnd();
        final SQLOver.WindowingBound beginBound = x.getWindowingBetweenBeginBound();
        final SQLOver.WindowingBound endBound = x.getWindowingBetweenEndBound();
        final SQLOver.WindowingType windowingType = x.getWindowingType();
        if (windowingBetweenEnd != null || endBound != null) {
            if (windowingType != null) {
                this.print(' ');
                this.print0(this.ucase ? windowingType.name : windowingType.name_lower);
            }
            this.print0(this.ucase ? " BETWEEN" : " between");
            this.printWindowingExpr(windowingBetweenBegin);
            if (beginBound != null) {
                this.print(' ');
                this.print0(this.ucase ? beginBound.name : beginBound.name_lower);
            }
            this.print0(this.ucase ? " AND" : " and");
            this.printWindowingExpr(windowingBetweenEnd);
            if (endBound != null) {
                this.print(' ');
                this.print0(this.ucase ? endBound.name : endBound.name_lower);
            }
        }
        else {
            if (windowingType != null) {
                this.print(' ');
                this.print0(this.ucase ? windowingType.name : windowingType.name_lower);
            }
            this.printWindowingExpr(windowingBetweenBegin);
            if (beginBound != null) {
                this.print(' ');
                this.print0(this.ucase ? beginBound.name : beginBound.name_lower);
            }
        }
        if (x.isExcludeCurrentRow()) {
            this.print0(this.ucase ? " EXCLUDE CURRENT ROW" : " exclude current row");
        }
        this.print(')');
        return false;
    }
    
    void printWindowingExpr(final SQLExpr expr) {
        if (expr == null) {
            return;
        }
        this.print(' ');
        if (expr instanceof SQLIdentifierExpr) {
            final String ident = ((SQLIdentifierExpr)expr).getName();
            this.print0(this.ucase ? ident : ident.toLowerCase());
        }
        else {
            expr.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLKeep x) {
        if (x.getDenseRank() == SQLKeep.DenseRank.FIRST) {
            this.print0(this.ucase ? "KEEP (DENSE_RANK FIRST " : "keep (dense_rank first ");
        }
        else {
            this.print0(this.ucase ? "KEEP (DENSE_RANK LAST " : "keep (dense_rank last ");
        }
        x.getOrderBy().accept(this);
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnPrimaryKey x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "PRIMARY KEY" : "primary key");
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnUniqueKey x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "UNIQUE" : "unique");
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnCheck x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "CHECK (" : "check (");
        x.getExpr().accept(this);
        this.print(')');
        final Boolean enable = x.getEnable();
        if (enable != null) {
            if (enable) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
            }
        }
        final Boolean enforced = x.getEnforced();
        if (enforced != null) {
            if (enforced) {
                this.print0(this.ucase ? " ENFORCED" : " enforced");
            }
            else {
                this.print0(this.ucase ? " NOT ENFORCED" : " not enforced");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause x) {
        if (!this.isParameterized() && this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "WITH " : "with ");
        if (x.getRecursive() == Boolean.TRUE) {
            this.print0(this.ucase ? "RECURSIVE " : "recursive ");
        }
        ++this.indentCount;
        this.printlnAndAccept(x.getEntries(), ", ");
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause.Entry x) {
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(x.getAlias());
        if (x.getColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        this.print(' ');
        this.print0(this.ucase ? "AS " : "as ");
        this.print('(');
        ++this.indentCount;
        this.println();
        final SQLSelect query = x.getSubQuery();
        if (query != null) {
            query.accept(this);
        }
        else {
            x.getReturningStatement().accept(this);
        }
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableModifyClusteredBy x) {
        this.print0(this.ucase ? "CLUSTERED BY " : "clustered by ");
        if (!x.getClusterColumns().isEmpty()) {
            this.printAndAccept(x.getClusterColumns(), ", ");
        }
        else {
            this.print0("()");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAlterColumn x) {
        if (DbType.odps == this.dbType) {
            this.print0(this.ucase ? "CHANGE COLUMN " : "change column ");
        }
        else if (DbType.hive == this.dbType) {
            this.print0(this.ucase ? "CHANGE " : "change ");
        }
        else {
            this.print0(this.ucase ? "ALTER COLUMN " : "alter column ");
        }
        final SQLName originColumn = x.getOriginColumn();
        if (originColumn != null) {
            originColumn.accept(this);
            this.print(' ');
        }
        x.getColumn().accept(this);
        if (x.isSetNotNull()) {
            this.print0(this.ucase ? " SET NOT NULL" : " set not null");
        }
        if (x.isDropNotNull()) {
            this.print0(this.ucase ? " DROP NOT NULL" : " drop not null");
        }
        if (x.getSetDefault() != null) {
            this.print0(this.ucase ? " SET DEFAULT " : " set default ");
            x.getSetDefault().accept(this);
        }
        if (x.isDropDefault()) {
            this.print0(this.ucase ? " DROP DEFAULT" : " drop default");
        }
        final SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            this.print0(this.ucase ? " SET DATA TYPE " : " set data type ");
            dataType.accept(this);
        }
        final SQLName after = x.getAfter();
        if (after != null) {
            this.print0(this.ucase ? " AFTER " : " after ");
            after.accept(this);
        }
        if (x.isFirst()) {
            this.print0(this.ucase ? " FIRST" : " first");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCheck x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "CHECK (" : "check (");
        ++this.indentCount;
        x.getExpr().accept(this);
        --this.indentCount;
        this.print(')');
        final Boolean enforced = x.getEnforced();
        if (enforced != null) {
            if (enforced) {
                this.print0(this.ucase ? " ENFORCED" : " enforced");
            }
            else {
                this.print0(this.ucase ? " NOT ENFORCED" : " not enforced");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDefault x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "DEFAULT " : "default ");
        ++this.indentCount;
        x.getExpr().accept(this);
        --this.indentCount;
        this.print0(this.ucase ? " FOR " : " for ");
        ++this.indentCount;
        x.getColumn().accept(this);
        --this.indentCount;
        if (x.isWithValues()) {
            this.print0(this.ucase ? " WITH VALUES" : " with values");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropForeignKey x) {
        this.print0(this.ucase ? "DROP FOREIGN KEY " : "drop foreign key ");
        x.getIndexName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropPrimaryKey x) {
        this.print0(this.ucase ? "DROP PRIMARY KEY" : "drop primary key");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropKey x) {
        this.print0(this.ucase ? "DROP KEY " : "drop key ");
        x.getKeyName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTablePartitionCount x) {
        this.print0(this.ucase ? "PARTITIONS " : "partitons ");
        x.getCount().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableBlockSize x) {
        this.print0(this.ucase ? "BLOCK_SIZE " : "block_size ");
        x.getSize().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableEnableKeys x) {
        this.print0(this.ucase ? "ENABLE KEYS" : "enable keys");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDisableKeys x) {
        this.print0(this.ucase ? "DISABLE KEYS" : "disable keys");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDisableConstraint x) {
        this.print0(this.ucase ? "DISABLE CONSTRAINT " : "disable constraint ");
        x.getConstraintName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableEnableConstraint x) {
        this.print0(this.ucase ? "ENABLE CONSTRAINT " : "enable constraint ");
        x.getConstraintName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropConstraint x) {
        this.print0(this.ucase ? "DROP CONSTRAINT " : "drop constraint ");
        x.getConstraintName().accept(this);
        if (x.isCascade()) {
            this.print0(this.ucase ? " CASCADE" : " cascade");
        }
        else if (x.isRestrict()) {
            this.print0(this.ucase ? " RESTRICT" : " restrict");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableStatement x) {
        this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printTableSourceExpr(x.getName());
        ++this.indentCount;
        for (int i = 0; i < x.getItems().size(); ++i) {
            final SQLAlterTableItem item = x.getItems().get(i);
            if (i != 0) {
                final SQLAlterTableItem former = x.getItems().get(i - 1);
                if (this.dbType != DbType.hive || !(former instanceof SQLAlterTableAddPartition) || !(item instanceof SQLAlterTableAddPartition)) {
                    this.print(',');
                }
            }
            this.println();
            item.accept(this);
        }
        --this.indentCount;
        if (x.isMergeSmallFiles()) {
            this.print0(this.ucase ? " MERGE SMALLFILES" : " merge smallfiles");
        }
        final List<SQLSelectOrderByItem> clusteredBy = x.getClusteredBy();
        if (clusteredBy.size() > 0) {
            this.println();
            if (x.isRange()) {
                this.print0(this.ucase ? "RANGE " : "range ");
            }
            this.print0(this.ucase ? "CLUSTERED BY (" : "clustered by (");
            this.printAndAccept(clusteredBy, ",");
            this.print0(")");
        }
        final List<SQLSelectOrderByItem> sortedBy = x.getSortedBy();
        if (sortedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SORTED BY (" : "sorted by (");
            this.printAndAccept(sortedBy, ", ");
            this.print(')');
        }
        final int buckets = x.getBuckets();
        if (buckets > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(buckets);
            this.print0(this.ucase ? " BUCKETS" : " buckets");
        }
        final int shards = x.getShards();
        if (shards > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(shards);
            this.print0(this.ucase ? " SHARDS" : " shards");
        }
        if (x.isNotClustered()) {
            this.print0(this.ucase ? " NOT CLUSTERED" : " not clustered");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprHint x) {
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        final String type = x.getType();
        if (type != null) {
            this.print0(this.ucase ? type.toUpperCase() : type.toLowerCase());
            this.print(' ');
        }
        if (x.isGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "INDEX " : "index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON " : " on ");
        x.getTable().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        final List<SQLName> covering = x.getCovering();
        if (covering.size() > 0) {
            this.print0(this.ucase ? " COVERING (" : " covering (");
            this.printAndAccept(covering, ", ");
            this.print(')');
        }
        final SQLExpr dbPartitionBy = x.getDbPartitionBy();
        if (dbPartitionBy != null) {
            this.print0(this.ucase ? " DBPARTITION BY " : " dbpartition by ");
            dbPartitionBy.accept(this);
        }
        final SQLExpr tablePartitionBy = x.getTablePartitionBy();
        if (tablePartitionBy != null) {
            this.print0(this.ucase ? " TBPARTITION BY " : " tbpartition by ");
            tablePartitionBy.accept(this);
        }
        final SQLExpr tablePartitions = x.getTablePartitions();
        if (tablePartitions != null) {
            this.print0(this.ucase ? " TBPARTITIONS " : " tbpartitions ");
            tablePartitions.accept(this);
        }
        if (x.getIndexDefinition().hasOptions()) {
            final String using = x.getIndexDefinition().getOptions().getIndexType();
            if (using != null) {
                this.print0(this.ucase ? " USING " : " using ");
                this.print0(this.ucase ? using.toUpperCase() : using.toLowerCase());
            }
            x.getIndexDefinition().getOptions().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnique x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "UNIQUE (" : "unique (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLPrimaryKeyImpl x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "PRIMARY KEY " : "primary key ");
        if (x.isClustered()) {
            this.print0(this.ucase ? "CLUSTERED " : "clustered ");
        }
        this.print('(');
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        if (x.isDisableNovalidate()) {
            this.print0(this.ucase ? " DISABLE NOVALIDATE" : " disable novalidate");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRenameColumn x) {
        this.print0(this.ucase ? "RENAME COLUMN " : "rename column ");
        x.getColumn().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnReference x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "REFERENCES " : "references ");
        x.getTable().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        final SQLForeignKeyImpl.Match match = x.getReferenceMatch();
        if (match != null) {
            this.print0(this.ucase ? " MATCH " : " match ");
            this.print0(this.ucase ? match.name : match.name_lcase);
        }
        if (x.getOnDelete() != null) {
            this.print0(this.ucase ? " ON DELETE " : " on delete ");
            this.print0(this.ucase ? x.getOnDelete().name : x.getOnDelete().name_lcase);
        }
        if (x.getOnUpdate() != null) {
            this.print0(this.ucase ? " ON UPDATE " : " on update ");
            this.print0(this.ucase ? x.getOnUpdate().name : x.getOnUpdate().name_lcase);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLForeignKeyImpl x) {
        final SQLName name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            name.accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "FOREIGN KEY (" : "foreign key (");
        this.printAndAccept(x.getReferencingColumns(), ", ");
        this.print(')');
        ++this.indentCount;
        this.println();
        this.print0(this.ucase ? "REFERENCES " : "references ");
        x.getReferencedTableName().accept(this);
        if (x.getReferencedColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getReferencedColumns(), ", ");
            this.print(')');
        }
        if (x.isOnDeleteCascade()) {
            this.println();
            this.print0(this.ucase ? "ON DELETE CASCADE" : "on delete cascade");
        }
        else if (x.isOnDeleteSetNull()) {
            this.print0(this.ucase ? "ON DELETE SET NULL" : "on delete set null");
        }
        if (x.isDisableNovalidate()) {
            this.print0(this.ucase ? " DISABLE NOVALIDATE" : " disable novalidate");
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropSequenceStatement x) {
        this.print0(this.ucase ? "DROP SEQUENCE " : "drop sequence ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(final SQLDropSequenceStatement x) {
    }
    
    @Override
    public boolean visit(final SQLDropTriggerStatement x) {
        this.print0(this.ucase ? "DROP TRIGGER " : "drop trigger ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(final SQLDropUserStatement x) {
    }
    
    @Override
    public boolean visit(final SQLDropUserStatement x) {
        this.print0(this.ucase ? "DROP USER " : "drop user ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.printAndAccept(x.getUsers(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCancelJobStatement x) {
        this.print0(this.ucase ? "CANCEL JOB " : "cancel job ");
        x.getJobName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLExplainAnalyzeStatement x) {
        this.print0(this.ucase ? "EXPLAIN ANALYZE " : "explain analyze ");
        this.println();
        x.getSelect().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLExplainStatement x) {
        this.print0(this.ucase ? "EXPLAIN" : "explain");
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        if (x.isExtended()) {
            this.print0(this.ucase ? " EXTENDED" : " extended");
        }
        if (x.isDependency()) {
            this.print0(this.ucase ? " DEPENDENCY" : " dependency");
        }
        if (x.isAuthorization()) {
            this.print0(this.ucase ? " AUTHORIZATION" : " authorization");
        }
        final String type = x.getType();
        if (type != null) {
            this.print(' ');
            if (type.indexOf(32) >= 0) {
                this.print('(');
                this.print0(type);
                this.print(')');
            }
            else {
                this.print0(type);
            }
        }
        this.println();
        x.getStatement().accept(this);
        return false;
    }
    
    protected void printGrantPrivileges(final SQLGrantStatement x) {
    }
    
    @Override
    public boolean visit(final SQLGrantStatement x) {
        this.print0(this.ucase ? "GRANT " : "grant ");
        this.printAndAccept(x.getPrivileges(), ", ");
        this.printGrantOn(x);
        if (x.getUsers() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            this.printAndAccept(x.getUsers(), ",");
        }
        if (x.getWithGrantOption()) {
            this.print0(this.ucase ? " WITH GRANT OPTION" : " with grant option");
        }
        boolean with = false;
        final SQLExpr maxQueriesPerHour = x.getMaxQueriesPerHour();
        if (maxQueriesPerHour != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }
            this.print0(this.ucase ? " MAX_QUERIES_PER_HOUR " : " max_queries_per_hour ");
            maxQueriesPerHour.accept(this);
        }
        final SQLExpr maxUpdatesPerHour = x.getMaxUpdatesPerHour();
        if (maxUpdatesPerHour != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }
            this.print0(this.ucase ? " MAX_UPDATES_PER_HOUR " : " max_updates_per_hour ");
            maxUpdatesPerHour.accept(this);
        }
        final SQLExpr maxConnectionsPerHour = x.getMaxConnectionsPerHour();
        if (maxConnectionsPerHour != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }
            this.print0(this.ucase ? " MAX_CONNECTIONS_PER_HOUR " : " max_connections_per_hour ");
            maxConnectionsPerHour.accept(this);
        }
        final SQLExpr maxUserConnections = x.getMaxUserConnections();
        if (maxUserConnections != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }
            this.print0(this.ucase ? " MAX_USER_CONNECTIONS " : " max_user_connections ");
            maxUserConnections.accept(this);
        }
        if (x.isAdminOption()) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }
            this.print0(this.ucase ? " ADMIN OPTION" : " admin option");
        }
        if (x.getIdentifiedBy() != null) {
            this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
            x.getIdentifiedBy().accept(this);
        }
        return false;
    }
    
    protected void printGrantOn(final SQLGrantStatement x) {
        if (x.getResource() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            final SQLObjectType resourceType = x.getResourceType();
            if (resourceType != null) {
                this.print0(this.ucase ? resourceType.name : resourceType.name_lcase);
                this.print(' ');
            }
            x.getResource().accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLRevokeStatement x) {
        this.print0(this.ucase ? "REVOKE " : "revoke ");
        if (x.isGrantOption()) {
            this.print0(this.ucase ? "GRANT OPTION" : "grant option");
            if (x.getPrivileges().size() > 0) {
                this.print0(this.ucase ? " FOR " : " for ");
            }
        }
        this.printAndAccept(x.getPrivileges(), ", ");
        if (x.getResource() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            if (x.getResourceType() != null) {
                this.print0(x.getResourceType().name());
                this.print(' ');
            }
            x.getResource().accept(this);
        }
        if (x.getUsers() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            if (this.dbType == DbType.odps) {
                this.print0(this.ucase ? "USER " : "user ");
            }
            this.printAndAccept(x.getUsers(), ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropDatabaseStatement x) {
        this.print0(this.ucase ? "DROP " : "drop ");
        if (x.isPhysical()) {
            this.print0(this.ucase ? "PHYSICAL " : "physical ");
        }
        this.print0(this.ucase ? "DATABASE " : "database ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getDatabase().accept(this);
        final Boolean restrict = x.getRestrict();
        if (restrict != null && restrict) {
            this.print0(this.ucase ? " RESTRICT" : " restrict");
        }
        if (x.isCascade()) {
            this.print0(this.ucase ? " CASCADE" : " cascade");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropCatalogStatement x) {
        if (x.isExternal()) {
            this.print0(this.ucase ? "DROP EXTERNAL CATALOG " : "drop external catalog ");
        }
        else {
            this.print0(this.ucase ? "DROP CATALOG " : "drop catalog ");
        }
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropFunctionStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        if (x.isTemporary()) {
            this.print0(this.ucase ? "DROP TEMPORARY FUNCTION " : "drop temporary function ");
        }
        else {
            this.print0(this.ucase ? "DROP FUNCTION " : "drop function ");
        }
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTableSpaceStatement x) {
        this.print0(this.ucase ? "DROP TABLESPACE " : "drop tablespace ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE " : " engine ");
            engine.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropProcedureStatement x) {
        this.print0(this.ucase ? "DROP PROCEDURE " : "drop procedure ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLIndexOptions x) {
        if (x.isInvisible()) {
            this.print0(this.ucase ? " INVISIBLE" : " invisible");
        }
        else if (x.isVisible()) {
            this.print0(this.ucase ? " VISIBLE" : " visible");
        }
        final SQLExpr keyBlockSize = x.getKeyBlockSize();
        if (keyBlockSize != null) {
            this.print0(this.ucase ? " KEY_BLOCK_SIZE = " : " key_block_size = ");
            this.printExpr(keyBlockSize, this.parameterized);
        }
        final String parserName = x.getParserName();
        if (parserName != null) {
            this.print0(this.ucase ? " WITH PARSER " : " with parser ");
            this.print0(parserName);
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment, this.parameterized);
        }
        final String algorithm = x.getAlgorithm();
        if (algorithm != null) {
            this.print0(this.ucase ? " ALGORITHM = " : " algorithm = ");
            this.print0(algorithm);
        }
        final String lock = x.getLock();
        if (lock != null) {
            this.print0(this.ucase ? " LOCK = " : " lock = ");
            this.print0(lock);
        }
        final List<SQLAssignItem> options = x.getOtherOptions();
        if (options.size() > 0) {
            for (final SQLAssignItem option : options) {
                this.print(' ');
                option.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIndexDefinition x) {
        final boolean mysql = DbType.mysql == this.dbType;
        final String type = x.getType();
        if (type != null) {
            this.print0(this.ucase ? type.toUpperCase() : type.toLowerCase());
            this.print(' ');
        }
        if (x.isGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        else if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        if (x.isIndex()) {
            this.print0(this.ucase ? "INDEX " : "index ");
        }
        if (x.isKey()) {
            this.print0(this.ucase ? "KEY " : "key ");
        }
        if (x.getName() != null && (type == null || !type.equalsIgnoreCase("primary"))) {
            x.getName().accept(this);
            this.print(' ');
        }
        final String using = x.getOptions().getIndexType();
        if (using != null) {
            this.print0(this.ucase ? "USING " : "using ");
            this.print0(using);
            this.print(' ');
        }
        if (x.isHashMapType()) {
            this.print0(this.ucase ? "HASHMAP " : "hashmap ");
        }
        if (x.getColumns().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        if (x.getAnalyzerName() != null) {
            this.print0(this.ucase ? " WITH ANALYZER " : " with analyzer ");
            x.getAnalyzerName().accept(this);
        }
        else {
            if (x.getIndexAnalyzerName() != null) {
                this.print0(this.ucase ? " WITH INDEX ANALYZER " : " with index analyzer ");
                x.getIndexAnalyzerName().accept(this);
            }
            if (x.getQueryAnalyzerName() != null) {
                this.print0(this.ucase ? " WITH QUERY ANALYZER " : " with query analyzer ");
                x.getQueryAnalyzerName().accept(this);
            }
            if (x.getWithDicName() != null) {
                this.printUcase(" WITH DICT ");
                x.getWithDicName().accept(this);
            }
        }
        final List<SQLName> covering = x.getCovering();
        if (covering.size() > 0) {
            this.print0(this.ucase ? " COVERING (" : " covering (");
            this.printAndAccept(covering, ", ");
            this.print(')');
        }
        final SQLExpr dbPartitionBy = x.getDbPartitionBy();
        if (dbPartitionBy != null) {
            this.print0(this.ucase ? " DBPARTITION BY " : " dbpartition by ");
            dbPartitionBy.accept(this);
        }
        final SQLExpr tablePartitionBy = x.getTbPartitionBy();
        if (tablePartitionBy != null) {
            this.print0(this.ucase ? " TBPARTITION BY " : " tbpartition by ");
            tablePartitionBy.accept(this);
        }
        final SQLExpr tablePartitions = x.getTbPartitions();
        if (tablePartitions != null) {
            this.print0(this.ucase ? " TBPARTITIONS " : " tbpartitions ");
            tablePartitions.accept(this);
        }
        if (x.hasOptions()) {
            x.getOptions().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddIndex x) {
        this.print0(this.ucase ? "ADD " : "add ");
        this.visit(x.getIndexDefinition());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAlterIndex x) {
        this.print0(this.ucase ? "ALTER " : "alter ");
        this.visit(x.getIndexDefinition());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddConstraint x) {
        if (x.isWithNoCheck()) {
            this.print0(this.ucase ? "WITH NOCHECK " : "with nocheck ");
        }
        this.print0(this.ucase ? "ADD " : "add ");
        x.getConstraint().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateTriggerStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "OR REPLACE " : "or replace ");
        }
        this.print0(this.ucase ? "TRIGGER " : "trigger ");
        x.getName().accept(this);
        ++this.indentCount;
        this.println();
        if (SQLCreateTriggerStatement.TriggerType.INSTEAD_OF.equals(x.getTriggerType())) {
            this.print0(this.ucase ? "INSTEAD OF" : "instead of");
        }
        else {
            final String triggerTypeName = x.getTriggerType().name();
            this.print0(this.ucase ? triggerTypeName : triggerTypeName.toLowerCase());
        }
        if (x.isInsert()) {
            this.print0(this.ucase ? " INSERT" : " insert");
        }
        if (x.isDelete()) {
            if (x.isInsert()) {
                this.print0(this.ucase ? " OR" : " or");
            }
            this.print0(this.ucase ? " DELETE" : " delete");
        }
        if (x.isUpdate()) {
            if (x.isInsert() || x.isDelete()) {
                this.print0(this.ucase ? " OR" : " or");
            }
            this.print0(this.ucase ? " UPDATE" : " update");
            final List<SQLName> colums = x.getUpdateOfColumns();
            for (final SQLName colum : colums) {
                this.print(' ');
                colum.accept(this);
            }
        }
        this.println();
        this.print0(this.ucase ? "ON " : "on ");
        x.getOn().accept(this);
        if (x.isForEachRow()) {
            this.println();
            this.print0(this.ucase ? "FOR EACH ROW" : "for each row");
        }
        final SQLExpr when = x.getWhen();
        if (when != null) {
            this.println();
            this.print0(this.ucase ? "WHEN " : "when ");
            when.accept(this);
        }
        --this.indentCount;
        this.println();
        x.getBody().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLBooleanExpr x) {
        this.print0(x.getBooleanValue() ? "true" : "false");
        return false;
    }
    
    @Override
    public void endVisit(final SQLBooleanExpr x) {
    }
    
    @Override
    public boolean visit(final SQLUnionQueryTableSource x) {
        this.print('(');
        ++this.indentCount;
        this.println();
        x.getUnion().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        final List<SQLName> columns = x.getColumns();
        final String alias = x.getAlias();
        if (alias != null) {
            if (columns.size() > 0) {
                this.print0(" AS ");
            }
            else {
                this.print(' ');
            }
            this.print0(alias);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTimestampExpr x) {
        if (this.parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
        }
        else {
            this.print0(this.ucase ? "TIMESTAMP " : "timestamp ");
            if (x.isWithTimeZone()) {
                this.print0(this.ucase ? " WITH TIME ZONE " : " with time zone ");
            }
            this.print('\'');
            this.print0(x.getLiteral());
            this.print('\'');
            if (x.getTimeZone() != null) {
                this.print0(this.ucase ? " AT TIME ZONE '" : " at time zone '");
                this.print0(x.getTimeZone());
                this.print('\'');
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryExpr x) {
        this.print0("b'");
        this.print0(x.getText());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRename x) {
        this.print0(this.ucase ? "RENAME TO " : "rename to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowTablesStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (x.isFull()) {
            this.print0(this.ucase ? "SHOW FULL TABLES" : "show full tables");
        }
        else {
            this.print0(this.ucase ? "SHOW TABLES" : "show tables");
        }
        if (x.isExtended()) {
            this.print0(this.ucase ? " EXTENDED" : " extended");
        }
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
        }
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        else if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    protected void printlnComment(final List<String> comments) {
        if (comments != null) {
            for (int i = 0; i < comments.size(); ++i) {
                final String comment = comments.get(i);
                if (i != 0 && comment.startsWith("--")) {
                    this.println();
                }
                this.printComment(comment);
            }
        }
    }
    
    public void printComment(final String comment) {
        if (comment == null) {
            return;
        }
        if (comment.startsWith("--") && comment.length() > 2 && comment.charAt(2) != ' ' && comment.charAt(2) != '-') {
            this.print0("-- ");
            this.print0(comment.substring(2));
        }
        else {
            this.print0(comment);
        }
    }
    
    protected void printlnComments(final List<String> comments) {
        if (comments != null) {
            for (int i = 0; i < comments.size(); ++i) {
                final String comment = comments.get(i);
                this.printComment(comment);
                this.println();
            }
        }
    }
    
    @Override
    public boolean visit(final SQLAlterViewRenameStatement x) {
        this.print0(this.ucase ? "ALTER VIEW " : "alter view ");
        x.getName().accept(this);
        final SQLName to = x.getTo();
        if (to != null) {
            this.print0(this.ucase ? " RENAME TO " : " rename to ");
            this.printExpr(to);
        }
        final SQLName changeOwnerTo = x.getChangeOwnerTo();
        if (changeOwnerTo != null) {
            this.print0(this.ucase ? " CHANGEOWNER TO " : " changeowner to ");
            this.printExpr(changeOwnerTo);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddPartition x) {
        boolean printAdd = true;
        if (x.getParent() instanceof SQLAlterTableStatement) {
            final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
            final int p = stmt.getChildren().indexOf(x);
            if (p > 0 && stmt.getChildren().get(p - 1) instanceof SQLAlterTableAddPartition) {
                printAdd = false;
            }
        }
        if (printAdd) {
            this.print0(this.ucase ? "ADD " : "add ");
            if (x.isIfNotExists()) {
                this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
            }
        }
        else {
            this.print('\t');
            ++this.indentCount;
        }
        if (x.getPartitionCount() != null) {
            this.print0(this.ucase ? "PARTITION PARTITIONS " : "partition partitions ");
            x.getPartitionCount().accept(this);
        }
        if (x.getPartitions().size() > 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.print0(this.ucase ? " LOCATION " : " locationn ");
            location.accept(this);
        }
        if (!printAdd) {
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddExtPartition x) {
        this.print0(this.ucase ? "ADD " : "add ");
        final MySqlExtPartition extPartition = x.getExtPartition();
        if (extPartition != null) {
            this.println();
            extPartition.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropExtPartition x) {
        this.print0(this.ucase ? "DROP " : "drop ");
        final MySqlExtPartition extPartition = x.getExtPartition();
        if (extPartition != null) {
            this.println();
            extPartition.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableReOrganizePartition x) {
        this.print0(this.ucase ? "REORGANIZE " : "reorganize ");
        this.printAndAccept(x.getNames(), ", ");
        this.print0(this.ucase ? " INTO (" : " into (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropPartition x) {
        boolean printDrop = true;
        if (x.getParent() instanceof SQLAlterTableStatement) {
            final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
            final int p = stmt.getChildren().indexOf(x);
            if (p > 0 && stmt.getChildren().get(p - 1) instanceof SQLAlterTableDropPartition) {
                printDrop = false;
            }
        }
        if (printDrop) {
            this.print0(this.ucase ? "DROP " : "drop ");
            if (x.isIfExists()) {
                this.print0(this.ucase ? "IF EXISTS " : "if exists ");
            }
        }
        else {
            this.print('\t');
            ++this.indentCount;
        }
        if (x.getAttribute("SIMPLE") != null) {
            this.print0(this.ucase ? "PARTITION " : "partition ");
            this.printAndAccept(x.getPartitions(), ",");
        }
        else {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }
        if (x.isPurge()) {
            this.print0(this.ucase ? " PURGE" : " purge");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRenamePartition x) {
        this.print0(this.ucase ? "PARTITION (" : "partition (");
        this.printAndAccept(x.getPartition(), ", ");
        this.print0(this.ucase ? ") RENAME TO PARTITION(" : ") rename to partition(");
        this.printAndAccept(x.getTo(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableSetComment x) {
        this.print0(this.ucase ? "SET COMMENT " : "set comment ");
        x.getComment().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLPrivilegeItem x) {
        this.printExpr(x.getAction());
        if (!x.getColumns().isEmpty()) {
            this.print0("(");
            this.printAndAccept(x.getColumns(), ", ");
            this.print0(")");
        }
        return false;
    }
    
    @Override
    public void endVisit(final SQLPrivilegeItem x) {
    }
    
    @Override
    public boolean visit(final SQLAlterTableSetLifecycle x) {
        this.print0(this.ucase ? "SET LIFECYCLE " : "set lifecycle ");
        x.getLifecycle().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableSetLocation x) {
        this.print0(this.ucase ? "SET LOCATION " : "set location ");
        x.getLocation().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableEnableLifecycle x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }
        this.print0(this.ucase ? "ENABLE LIFECYCLE" : "enable lifecycle");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDisableLifecycle x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }
        this.print0(this.ucase ? "DISABLE LIFECYCLE" : "disable lifecycle");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTablePartition x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTablePartitionSetProperties x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }
        if (x.getPartitionProperties().size() != 0) {
            this.print0(this.ucase ? "SET PARTITIONPROPERTIES (" : "set partitionproperties (");
            this.printAndAccept(x.getPartitionProperties(), ", ");
            this.print0(") ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableTouch x) {
        this.print0(this.ucase ? "TOUCH" : "touch");
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLArrayExpr x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).nameHashCode64() == FnvHash.Constants.ARRAY && this.printNameQuote) {
            this.print0(((SQLIdentifierExpr)expr).getName());
        }
        else if (expr != null) {
            expr.accept(this);
        }
        this.print('[');
        this.printAndAccept(x.getValues(), ", ");
        this.print(']');
        return false;
    }
    
    @Override
    public boolean visit(final SQLOpenStatement x) {
        this.print0(this.ucase ? "OPEN " : "open ");
        this.printExpr(x.getCursorName(), this.parameterized);
        final List<SQLName> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print('(');
            this.printAndAccept(columns, ", ");
            this.print(')');
        }
        final SQLExpr forExpr = x.getFor();
        if (forExpr != null) {
            this.print0(this.ucase ? " FOR " : " for ");
            forExpr.accept(this);
        }
        final List<SQLExpr> using = x.getUsing();
        if (using.size() > 0) {
            this.print0(this.ucase ? " USING " : " using ");
            this.printAndAccept(using, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLFetchStatement x) {
        this.print0(this.ucase ? "FETCH " : "fetch ");
        x.getCursorName().accept(this);
        if (x.isBulkCollect()) {
            this.print0(this.ucase ? " BULK COLLECT INTO " : " bulk collect into ");
        }
        else {
            this.print0(this.ucase ? " INTO " : " into ");
        }
        this.printAndAccept(x.getInto(), ", ");
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.print(' ');
            limit.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCloseStatement x) {
        this.print0(this.ucase ? "CLOSE " : "close ");
        this.printExpr(x.getCursorName(), this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLGroupingSetExpr x) {
        this.print0(this.ucase ? "GROUPING SETS" : "grouping sets");
        this.print0(" (");
        this.printAndAccept(x.getParameters(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement x) {
        this.print0(this.ucase ? "IF " : "if ");
        x.getCondition().accept(this);
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        for (final SQLIfStatement.ElseIf elseIf : x.getElseIfList()) {
            this.println();
            elseIf.accept(this);
        }
        if (x.getElseItem() != null) {
            this.println();
            x.getElseItem().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.Else x) {
        this.print0(this.ucase ? "ELSE" : "else");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.ElseIf x) {
        this.print0(this.ucase ? "ELSE IF" : "else if");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLLoopStatement x) {
        this.print0(this.ucase ? "LOOP" : "loop");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        this.println();
        this.print0(this.ucase ? "END LOOP" : "end loop");
        if (x.getLabelName() != null) {
            this.print(' ');
            this.print0(x.getLabelName());
        }
        return false;
    }
    
    public boolean visit(final OracleFunctionDataType x) {
        if (x.isStatic()) {
            this.print0(this.ucase ? "STATIC " : "static ");
        }
        this.print0(this.ucase ? "FUNCTION " : "function ");
        this.print0(x.getName());
        this.print(" (");
        this.printAndAccept(x.getParameters(), ", ");
        this.print(")");
        this.print0(this.ucase ? " RETURN " : " return ");
        x.getReturnDataType().accept(this);
        final SQLStatement block = x.getBlock();
        if (block != null) {
            this.println();
            this.print0(this.ucase ? "IS" : "is");
            this.println();
            block.accept(this);
        }
        return false;
    }
    
    public boolean visit(final OracleProcedureDataType x) {
        if (x.isStatic()) {
            this.print0(this.ucase ? "STATIC " : "static ");
        }
        this.print0(this.ucase ? "PROCEDURE " : "procedure ");
        this.print0(x.getName());
        if (x.getParameters().size() > 0) {
            this.print(" (");
            this.printAndAccept(x.getParameters(), ", ");
            this.print(")");
        }
        final SQLStatement block = x.getBlock();
        if (block != null) {
            this.println();
            this.print0(this.ucase ? "IS" : "is");
            this.println();
            block.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLParameter x) {
        final SQLName name = x.getName();
        if (x.getDataType().getName().equalsIgnoreCase("CURSOR")) {
            this.print0(this.ucase ? "CURSOR " : "cursor ");
            x.getName().accept(this);
            this.print0(this.ucase ? " IS" : " is");
            ++this.indentCount;
            this.println();
            final SQLSelect select = ((SQLQueryExpr)x.getDefaultValue()).getSubQuery();
            select.accept(this);
            --this.indentCount;
        }
        else {
            if (x.isMap()) {
                this.print0(this.ucase ? "MAP MEMBER " : "map member ");
            }
            else if (x.isOrder()) {
                this.print0(this.ucase ? "ORDER MEMBER " : "order member ");
            }
            else if (x.isMember()) {
                this.print0(this.ucase ? "MEMBER " : "member ");
            }
            final SQLDataType dataType = x.getDataType();
            if (DbType.oracle == this.dbType || dataType instanceof OracleFunctionDataType || dataType instanceof OracleProcedureDataType) {
                if (dataType instanceof OracleFunctionDataType) {
                    final OracleFunctionDataType functionDataType = (OracleFunctionDataType)dataType;
                    this.visit(functionDataType);
                    return false;
                }
                if (dataType instanceof OracleProcedureDataType) {
                    final OracleProcedureDataType procedureDataType = (OracleProcedureDataType)dataType;
                    this.visit(procedureDataType);
                    return false;
                }
                final String dataTypeName = dataType.getName();
                final boolean printType = (dataTypeName.startsWith("TABLE OF") && x.getDefaultValue() == null) || dataTypeName.equalsIgnoreCase("REF CURSOR") || dataTypeName.startsWith("VARRAY(");
                if (printType) {
                    this.print0(this.ucase ? "TYPE " : "type ");
                }
                name.accept(this);
                if (x.getParamType() == SQLParameter.ParameterType.IN) {
                    this.print0(this.ucase ? " IN " : " in ");
                }
                else if (x.getParamType() == SQLParameter.ParameterType.OUT) {
                    this.print0(this.ucase ? " OUT " : " out ");
                }
                else if (x.getParamType() == SQLParameter.ParameterType.INOUT) {
                    this.print0(this.ucase ? " IN OUT " : " in out ");
                }
                else {
                    this.print(' ');
                }
                if (x.isNoCopy()) {
                    this.print0(this.ucase ? "NOCOPY " : "nocopy ");
                }
                if (x.isConstant()) {
                    this.print0(this.ucase ? "CONSTANT " : "constant ");
                }
                if (printType) {
                    this.print0(this.ucase ? "IS " : "is ");
                }
            }
            else {
                if (x.getParamType() == SQLParameter.ParameterType.IN) {
                    final boolean skip = DbType.mysql == this.dbType && x.getParent() instanceof SQLCreateFunctionStatement;
                    if (!skip) {
                        this.print0(this.ucase ? "IN " : "in ");
                    }
                }
                else if (x.getParamType() == SQLParameter.ParameterType.OUT) {
                    this.print0(this.ucase ? "OUT " : "out ");
                }
                else if (x.getParamType() == SQLParameter.ParameterType.INOUT) {
                    this.print0(this.ucase ? "INOUT " : "inout ");
                }
                x.getName().accept(this);
                this.print(' ');
            }
            dataType.accept(this);
            this.printParamDefaultValue(x);
        }
        return false;
    }
    
    protected void printParamDefaultValue(final SQLParameter x) {
        if (x.getDefaultValue() != null) {
            this.print0(" := ");
            x.getDefaultValue().accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLDeclareItem x) {
        final SQLDataType dataType = x.getDataType();
        if (dataType instanceof SQLRecordDataType) {
            this.print0(this.ucase ? "TYPE " : "type ");
        }
        x.getName().accept(this);
        if (x.getType() == SQLDeclareItem.Type.TABLE) {
            this.print0(this.ucase ? " TABLE" : " table");
            final int size = x.getTableElementList().size();
            if (size > 0) {
                this.print0(" (");
                ++this.indentCount;
                this.println();
                for (int i = 0; i < size; ++i) {
                    if (i != 0) {
                        this.print(',');
                        this.println();
                    }
                    x.getTableElementList().get(i).accept(this);
                }
                --this.indentCount;
                this.println();
                this.print(')');
            }
        }
        else if (x.getType() == SQLDeclareItem.Type.CURSOR) {
            this.print0(this.ucase ? " CURSOR" : " cursor");
        }
        else {
            if (dataType != null) {
                if (dataType instanceof SQLRecordDataType) {
                    this.print0(this.ucase ? " IS " : " is ");
                }
                else {
                    this.print(' ');
                }
                dataType.accept(this);
            }
            if (x.getValue() != null) {
                if (DbType.mysql == this.getDbType()) {
                    this.print0(this.ucase ? " DEFAULT " : " default ");
                }
                else {
                    this.print0(" = ");
                }
                x.getValue().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionValue x) {
        if (x.getOperator() == SQLPartitionValue.Operator.LessThan && DbType.oracle != this.getDbType() && x.getItems().size() == 1 && x.getItems().get(0) instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr ident = x.getItems().get(0);
            if ("MAXVALUE".equalsIgnoreCase(ident.getName())) {
                this.print0(this.ucase ? "VALUES LESS THAN MAXVALUE" : "values less than maxvalue");
                return false;
            }
        }
        if (x.getOperator() == SQLPartitionValue.Operator.LessThan) {
            this.print0(this.ucase ? "VALUES LESS THAN (" : "values less than (");
        }
        else if (x.getOperator() == SQLPartitionValue.Operator.In) {
            this.print0(this.ucase ? "VALUES IN (" : "values in (");
        }
        else {
            this.print(this.ucase ? "VALUES (" : "values (");
        }
        this.printAndAccept(x.getItems(), ", ", false);
        this.print(')');
        return false;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    @Override
    public boolean isUppCase() {
        return this.ucase;
    }
    
    public void setUppCase(final boolean val) {
        this.config(VisitorFeature.OutputUCase, true);
    }
    
    @Override
    public boolean visit(final SQLPartition x) {
        boolean isDbPartiton = false;
        boolean isTbPartition = false;
        final SQLObject parent = x.getParent();
        if (parent != null) {
            final SQLObject parent2 = parent.getParent();
            if (parent2 instanceof MySqlCreateTableStatement) {
                final MySqlCreateTableStatement stmt = (MySqlCreateTableStatement)parent2;
                isDbPartiton = (parent == stmt.getDbPartitionBy());
                isTbPartition = (parent == stmt.getTablePartitionBy());
            }
        }
        if (isDbPartiton) {
            this.print0(this.ucase ? "DBPARTITION " : "dbpartition ");
        }
        else if (isTbPartition) {
            this.print0(this.ucase ? "TBPARTITION " : "tbpartition ");
        }
        else {
            this.print0(this.ucase ? "PARTITION " : "partition ");
        }
        x.getName().accept(this);
        if (x.getValues() != null) {
            this.print(' ');
            x.getValues().accept(this);
        }
        if (x.getDataDirectory() != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "DATA DIRECTORY " : "data directory ");
            x.getDataDirectory().accept(this);
            --this.indentCount;
        }
        if (x.getIndexDirectory() != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "INDEX DIRECTORY " : "index directory ");
            x.getIndexDirectory().accept(this);
            --this.indentCount;
        }
        ++this.indentCount;
        this.printOracleSegmentAttributes(x);
        if (x.getEngine() != null) {
            this.println();
            this.print0(this.ucase ? "STORAGE ENGINE " : "storage engine ");
            x.getEngine().accept(this);
        }
        --this.indentCount;
        if (x.getMaxRows() != null) {
            this.print0(this.ucase ? " MAX_ROWS " : " max_rows ");
            x.getMaxRows().accept(this);
        }
        if (x.getMinRows() != null) {
            this.print0(this.ucase ? " MIN_ROWS " : " min_rows ");
            x.getMinRows().accept(this);
        }
        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }
        if (x.getSubPartitionsCount() != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "SUBPARTITIONS " : "subpartitions ");
            x.getSubPartitionsCount().accept(this);
            --this.indentCount;
        }
        if (x.getSubPartitions().size() > 0) {
            this.print(" (");
            ++this.indentCount;
            for (int i = 0; i < x.getSubPartitions().size(); ++i) {
                if (i != 0) {
                    this.print(',');
                }
                this.println();
                x.getSubPartitions().get(i).accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByRange x) {
        final SQLExpr interval = x.getInterval();
        if (x.getColumns().size() == 0 && (interval instanceof SQLBetweenExpr || interval instanceof SQLMethodInvokeExpr)) {
            interval.accept(this);
        }
        else {
            this.print0(this.ucase ? "RANGE" : "range");
            boolean columns = true;
            for (final SQLExpr column : x.getColumns()) {
                if (!(column instanceof SQLName)) {
                    columns = false;
                    break;
                }
            }
            if (x.getColumns().size() == 1) {
                if (DbType.mysql == this.getDbType() && columns) {
                    this.print0(this.ucase ? " COLUMNS (" : " columns (");
                }
                else {
                    this.print0(" (");
                }
                x.getColumns().get(0).accept(this);
                this.print(')');
            }
            else {
                if (DbType.mysql == this.getDbType() && columns) {
                    this.print0(this.ucase ? " COLUMNS (" : " columns (");
                }
                else {
                    this.print0(" (");
                }
                this.printAndAccept(x.getColumns(), ", ");
                this.print(')');
            }
            if (interval != null) {
                this.print0(this.ucase ? " INTERVAL (" : " interval (");
                interval.accept(this);
                this.print(')');
            }
        }
        this.printPartitionsCountAndSubPartitions(x);
        if (x.getPartitions().size() > 0) {
            this.print(" (");
            ++this.indentCount;
            for (int i = 0, size = x.getPartitions().size(); i < size; ++i) {
                if (i != 0) {
                    this.print(',');
                }
                this.println();
                x.getPartitions().get(i).accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByList x) {
        this.print0(this.ucase ? "LIST " : "list ");
        if (x.getColumns().size() == 1) {
            this.print('(');
            x.getColumns().get(0).accept(this);
            this.print0(")");
        }
        else {
            this.print0(this.ucase ? "COLUMNS (" : "columns (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print0(")");
        }
        this.printPartitionsCountAndSubPartitions(x);
        this.printSQLPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByHash x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "LINEAR HASH " : "linear hash ");
        }
        else if (x.isUnique()) {
            this.print0(this.ucase ? "UNI_HASH " : "uni_hash ");
        }
        else {
            this.print0(this.ucase ? "HASH " : "hash ");
        }
        if (x.isKey()) {
            this.print0(this.ucase ? "KEY" : "key");
        }
        this.print('(');
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        this.printPartitionsCountAndSubPartitions(x);
        this.printSQLPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByValue x) {
        this.print0(this.ucase ? "VALUE " : "value ");
        this.print('(');
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        this.printPartitionsCountAndSubPartitions(x);
        this.printSQLPartitions(x.getPartitions());
        return false;
    }
    
    private void printSQLPartitions(final List<SQLPartition> partitions) {
        final int partitionsSize = partitions.size();
        if (partitionsSize > 0) {
            this.print0(" (");
            ++this.indentCount;
            for (int i = 0; i < partitionsSize; ++i) {
                this.println();
                partitions.get(i).accept(this);
                if (i != partitionsSize - 1) {
                    this.print0(", ");
                }
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
    }
    
    protected void printPartitionsCountAndSubPartitions(final SQLPartitionBy x) {
        final SQLExpr partitionsCount = x.getPartitionsCount();
        if (partitionsCount != null) {
            boolean isDbPartiton = false;
            boolean isTbPartition = false;
            if (x.getParent() instanceof MySqlCreateTableStatement) {
                final MySqlCreateTableStatement stmt = (MySqlCreateTableStatement)x.getParent();
                isDbPartiton = (x == stmt.getDbPartitionBy());
                isTbPartition = (x == stmt.getTablePartitionBy());
            }
            if (Boolean.TRUE.equals(x.getAttribute("ads.partition"))) {
                this.print0(this.ucase ? " PARTITION NUM " : " partition num ");
            }
            else if (isDbPartiton) {
                this.print0(this.ucase ? " DBPARTITIONS " : " dbpartitions ");
            }
            else if (isTbPartition) {
                this.print0(this.ucase ? " TBPARTITIONS " : " tbpartitions ");
            }
            else {
                this.print0(this.ucase ? " PARTITIONS " : " partitions ");
            }
            partitionsCount.accept(this);
        }
        if (x.getLifecycle() != null) {
            this.print0(this.ucase ? " LIFECYCLE " : " lifecycle ");
            x.getLifecycle().accept(this);
        }
        if (x.getSubPartitionBy() != null) {
            this.println();
            x.getSubPartitionBy().accept(this);
        }
        if (x.getStoreIn().size() > 0) {
            this.println();
            this.print0(this.ucase ? "STORE IN (" : "store in (");
            this.printAndAccept(x.getStoreIn(), ", ");
            this.print(')');
        }
    }
    
    @Override
    public boolean visit(final SQLSubPartitionByHash x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "SUBPARTITION BY LINEAR HASH " : "subpartition by linear hash ");
        }
        else {
            this.print0(this.ucase ? "SUBPARTITION BY HASH " : "subpartition by hash ");
        }
        if (x.isKey()) {
            this.print0(this.ucase ? "KEY" : "key");
        }
        this.print('(');
        x.getExpr().accept(this);
        this.print(')');
        if (x.getSubPartitionsCount() != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            x.getSubPartitionsCount().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartitionByRange x) {
        this.print0(this.ucase ? "SUBPARTITION BY RANGE " : "subpartition by range ");
        final SQLExpr subPartitionsCount = x.getSubPartitionsCount();
        if (subPartitionsCount != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            subPartitionsCount.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartitionByList x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "SUBPARTITION BY LINEAR HASH " : "subpartition by linear hash ");
        }
        else {
            this.print0(this.ucase ? "SUBPARTITION BY HASH " : "subpartition by hash ");
        }
        this.print('(');
        x.getColumn().accept(this);
        this.print(')');
        if (x.getSubPartitionsCount() != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            x.getSubPartitionsCount().accept(this);
        }
        if (x.getSubPartitionTemplate().size() > 0) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "SUBPARTITION TEMPLATE (" : "subpartition template (");
            ++this.indentCount;
            this.println();
            this.printlnAndAccept(x.getSubPartitionTemplate(), ",");
            --this.indentCount;
            this.println();
            this.print(')');
            --this.indentCount;
        }
        if (x.getLifecycle() != null) {
            this.print0(this.ucase ? " LIFECYCLE " : " lifecycle ");
            x.getLifecycle().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartition x) {
        this.print0(this.ucase ? "SUBPARTITION " : "subpartition ");
        x.getName().accept(this);
        if (x.getValues() != null) {
            this.print(' ');
            x.getValues().accept(this);
        }
        final SQLName tableSpace = x.getTableSpace();
        if (tableSpace != null) {
            this.print0(this.ucase ? " TABLESPACE " : " tablespace ");
            tableSpace.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterDatabaseStatement x) {
        this.print0(this.ucase ? "ALTER DATABASE " : "alter database ");
        x.getName().accept(this);
        if (x.isUpgradeDataDirectoryName()) {
            this.print0(this.ucase ? " UPGRADE DATA DIRECTORY NAME" : " upgrade data directory name");
        }
        if (x.getProperties().size() > 0) {
            this.print0(this.ucase ? " SET DBPROPERTIES (" : "set dbproperties (");
            this.printAndAccept(x.getProperties(), ", ");
            this.print(')');
        }
        final SQLAlterDatabaseItem item = x.getItem();
        if (item instanceof MySqlAlterDatabaseSetOption) {
            final MySqlAlterDatabaseSetOption setOption = (MySqlAlterDatabaseSetOption)item;
            this.print0(this.ucase ? " SET " : " set ");
            this.printAndAccept(setOption.getOptions(), ", ");
            final SQLName on = setOption.getOn();
            if (on != null) {
                this.print0(this.ucase ? " ON " : " on ");
                on.accept(this);
            }
        }
        if (item instanceof MySqlAlterDatabaseKillJob) {
            this.print0(this.ucase ? " KILL " : " kill ");
            final MySqlAlterDatabaseKillJob kill = (MySqlAlterDatabaseKillJob)item;
            kill.getJobType().accept(this);
            this.print0(" ");
            kill.getJobId().accept(this);
        }
        final SQLAlterCharacter character = x.getCharacter();
        if (character != null) {
            this.print(' ');
            character.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableConvertCharSet x) {
        this.print0(this.ucase ? "CONVERT TO CHARACTER SET " : "convertToSqlNode to character set ");
        x.getCharset().accept(this);
        if (x.getCollate() != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            x.getCollate().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableCoalescePartition x) {
        this.print0(this.ucase ? "COALESCE PARTITION " : "coalesce partition ");
        x.getCount().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableTruncatePartition x) {
        this.print0(this.ucase ? "TRUNCATE PARTITION " : "truncate partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDiscardPartition x) {
        this.print0(this.ucase ? "DISCARD PARTITION " : "discard partition ");
        this.printPartitions(x.getPartitions());
        if (x.isTablespace()) {
            this.print0(this.ucase ? " TABLESPACE" : " tablespace");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableExchangePartition x) {
        this.print0(this.ucase ? "EXCHANGE PARTITION " : "exchange partition ");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print0(this.ucase ? " WITH TABLE " : " with table ");
        x.getTable().accept(this);
        final Boolean validation = x.getValidation();
        if (validation != null) {
            if (validation) {
                this.print0(this.ucase ? " WITH VALIDATION" : " with validation");
            }
            else {
                this.print0(this.ucase ? " WITHOUT VALIDATION" : " without validation");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableImportPartition x) {
        this.print0(this.ucase ? "IMPORT PARTITION " : "import partition ");
        this.printPartitions(x.getPartitions());
        if (x.isTablespace()) {
            this.print0(this.ucase ? " TABLESPACE" : " tablespace");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAnalyzePartition x) {
        this.print0(this.ucase ? "ANALYZE PARTITION " : "analyze partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    protected void printPartitions(final List<SQLName> partitions) {
        if (partitions.size() == 1 && "ALL".equalsIgnoreCase(partitions.get(0).getSimpleName())) {
            this.print0(this.ucase ? "ALL" : "all");
        }
        else {
            this.printAndAccept(partitions, ", ");
        }
    }
    
    @Override
    public boolean visit(final SQLAlterTableCheckPartition x) {
        this.print0(this.ucase ? "CHECK PARTITION " : "check partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableOptimizePartition x) {
        this.print0(this.ucase ? "OPTIMIZE PARTITION " : "optimize partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRebuildPartition x) {
        this.print0(this.ucase ? "REBUILD PARTITION " : "rebuild partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRepairPartition x) {
        this.print0(this.ucase ? "REPAIR PARTITION " : "repair partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final SQLSequenceExpr x) {
        x.getSequence().accept(this);
        this.print('.');
        this.print0(this.ucase ? x.getFunction().name : x.getFunction().name_lcase);
        return false;
    }
    
    @Override
    public boolean visit(final SQLMergeStatement x) {
        this.print0(this.ucase ? "MERGE " : "merge ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        this.print0(this.ucase ? "INTO " : "into ");
        x.getInto().accept(this);
        this.println();
        this.print0(this.ucase ? "USING " : "using ");
        x.getUsing().accept(this);
        this.print0(this.ucase ? " ON (" : " on (");
        x.getOn().accept(this);
        this.print0(") ");
        if (x.getUpdateClause() != null) {
            this.println();
            x.getUpdateClause().accept(this);
        }
        if (x.getInsertClause() != null) {
            this.println();
            x.getInsertClause().accept(this);
        }
        if (x.getErrorLoggingClause() != null) {
            this.println();
            x.getErrorLoggingClause().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLMergeStatement.MergeUpdateClause x) {
        if (x.isDelete()) {
            this.print0(this.ucase ? "WHEN MATCHED THEN DELETE" : "when matched then delete");
            return false;
        }
        this.print0(this.ucase ? "WHEN MATCHED THEN UPDATE SET " : "when matched then update set ");
        this.printAndAccept(x.getItems(), ", ");
        final SQLExpr where = x.getWhere();
        if (where != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where, this.parameterized);
            --this.indentCount;
        }
        final SQLExpr deleteWhere = x.getDeleteWhere();
        if (deleteWhere != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "DELETE WHERE " : "delete where ");
            this.printExpr(deleteWhere, this.parameterized);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLMergeStatement.MergeInsertClause x) {
        this.print0(this.ucase ? "WHEN NOT MATCHED THEN INSERT" : "when not matched then insert");
        if (x.getColumns().size() > 0) {
            this.print(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        this.print0(this.ucase ? " VALUES (" : " values (");
        this.printAndAccept(x.getValues(), ", ");
        this.print(')');
        if (x.getWhere() != null) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().accept(this);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLErrorLoggingClause x) {
        this.print0(this.ucase ? "LOG ERRORS " : "log errors ");
        if (x.getInto() != null) {
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
            this.print(' ');
        }
        if (x.getSimpleExpression() != null) {
            this.print('(');
            x.getSimpleExpression().accept(this);
            this.print(')');
        }
        if (x.getLimit() != null) {
            this.print0(this.ucase ? " REJECT LIMIT " : " reject limit ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateSequenceStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isGroup()) {
            this.print0(this.ucase ? "GROUP " : "group ");
        }
        else if (x.isSimple()) {
            this.print0(this.ucase ? "SIMPLE " : "simple ");
            final Boolean cache = x.getWithCache();
            if (cache != null && cache) {
                this.print0(this.ucase ? "WITH CACHE " : "with cache ");
            }
        }
        else if (x.isTime()) {
            this.print0(this.ucase ? "TIME " : "time ");
        }
        this.print0(this.ucase ? "SEQUENCE " : "sequence ");
        x.getName().accept(this);
        if (x.getStartWith() != null) {
            this.print0(this.ucase ? " START WITH " : " start with ");
            x.getStartWith().accept(this);
        }
        if (x.getIncrementBy() != null) {
            this.print0(this.ucase ? " INCREMENT BY " : " increment by ");
            x.getIncrementBy().accept(this);
        }
        if (x.getMaxValue() != null) {
            this.print0(this.ucase ? " MAXVALUE " : " maxvalue ");
            x.getMaxValue().accept(this);
        }
        if (x.isNoMaxValue()) {
            if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO MAXVALUE" : " no maxvalue");
            }
            else {
                this.print0(this.ucase ? " NOMAXVALUE" : " nomaxvalue");
            }
        }
        if (x.getMinValue() != null) {
            this.print0(this.ucase ? " MINVALUE " : " minvalue ");
            x.getMinValue().accept(this);
        }
        if (x.isNoMinValue()) {
            if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO MINVALUE" : " no minvalue");
            }
            else {
                this.print0(this.ucase ? " NOMINVALUE" : " nominvalue");
            }
        }
        if (x.getCycle() != null) {
            if (x.getCycle()) {
                this.print0(this.ucase ? " CYCLE" : " cycle");
            }
            else if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO CYCLE" : " no cycle");
            }
            else {
                this.print0(this.ucase ? " NOCYCLE" : " nocycle");
            }
        }
        final Boolean cache = x.getCache();
        if (cache != null) {
            if (cache) {
                this.print0(this.ucase ? " CACHE" : " cache");
                final SQLExpr cacheValue = x.getCacheValue();
                if (cacheValue != null) {
                    this.print(' ');
                    cacheValue.accept(this);
                }
            }
            else {
                this.print0(this.ucase ? " NOCACHE" : " nocache");
            }
        }
        final Boolean order = x.getOrder();
        if (order != null) {
            if (order) {
                this.print0(this.ucase ? " ORDER" : " order");
            }
            else {
                this.print0(this.ucase ? " NOORDER" : " noorder");
            }
        }
        final SQLExpr unitCount = x.getUnitCount();
        if (unitCount != null) {
            this.print0(this.ucase ? " UNIT COUNT " : " unit count ");
            this.printExpr(unitCount);
        }
        final SQLExpr unitIndex = x.getUnitIndex();
        if (unitIndex != null) {
            this.print0(this.ucase ? " INDEX " : " index ");
            this.printExpr(unitIndex);
        }
        if (x.getStep() != null) {
            this.print0(this.ucase ? " STEP " : " STEP ");
            this.printExpr(x.getStep());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterSequenceStatement x) {
        this.print0(this.ucase ? "ALTER SEQUENCE " : "alter sequence ");
        x.getName().accept(this);
        if (x.isChangeToSimple()) {
            this.print0(this.ucase ? " CHANGE TO SIMPLE" : " change to simple");
            final Boolean cache = x.getWithCache();
            if (cache != null && cache) {
                this.print0(this.ucase ? " WITH CACHE" : "  WITH CACHE");
            }
        }
        else if (x.isChangeToGroup()) {
            this.print0(this.ucase ? " CHANGE TO GROUP" : " change to group");
        }
        else if (x.isChangeToTime()) {
            this.print0(this.ucase ? " CHANGE TO TIME" : " change to time");
        }
        if (x.getStartWith() != null) {
            this.print0(this.ucase ? " START WITH " : " start with ");
            x.getStartWith().accept(this);
        }
        if (x.getIncrementBy() != null) {
            this.print0(this.ucase ? " INCREMENT BY " : " increment by ");
            x.getIncrementBy().accept(this);
        }
        if (x.getMaxValue() != null) {
            this.print0(this.ucase ? " MAXVALUE " : " maxvalue ");
            x.getMaxValue().accept(this);
        }
        if (x.isNoMaxValue()) {
            if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO MAXVALUE" : " no maxvalue");
            }
            else {
                this.print0(this.ucase ? " NOMAXVALUE" : " nomaxvalue");
            }
        }
        if (x.getMinValue() != null) {
            this.print0(this.ucase ? " MINVALUE " : " minvalue ");
            x.getMinValue().accept(this);
        }
        if (x.isNoMinValue()) {
            if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO MINVALUE" : " no minvalue");
            }
            else {
                this.print0(this.ucase ? " NOMINVALUE" : " nominvalue");
            }
        }
        if (x.getCycle() != null) {
            if (x.getCycle()) {
                this.print0(this.ucase ? " CYCLE" : " cycle");
            }
            else if (DbType.postgresql == this.dbType) {
                this.print0(this.ucase ? " NO CYCLE" : " no cycle");
            }
            else {
                this.print0(this.ucase ? " NOCYCLE" : " nocycle");
            }
        }
        final Boolean cache = x.getCache();
        if (cache != null) {
            if (cache) {
                this.print0(this.ucase ? " CACHE" : " cache");
                final SQLExpr cacheValue = x.getCacheValue();
                if (cacheValue != null) {
                    this.print(' ');
                    cacheValue.accept(this);
                }
            }
            else {
                this.print0(this.ucase ? " NOCACHE" : " nocache");
            }
        }
        final Boolean order = x.getOrder();
        if (order != null) {
            if (order) {
                this.print0(this.ucase ? " ORDER" : " order");
            }
            else {
                this.print0(this.ucase ? " NOORDER" : " noorder");
            }
        }
        if (x.isRestart()) {
            this.print0(this.ucase ? " RESTART" : " restart");
            final SQLExpr restartWith = x.getRestartWith();
            if (restartWith != null) {
                this.print0(this.ucase ? " WITH " : " with ");
                restartWith.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDateExpr x) {
        final String literal = x.getLiteral();
        this.print0(this.ucase ? "DATE '" : "date '");
        this.print0(literal);
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLTimeExpr x) {
        final SQLExpr literal = x.getLiteral();
        this.print0(this.ucase ? "TIME " : "time ");
        this.printExpr(literal, this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDateTimeExpr x) {
        final SQLExpr literal = x.getLiteral();
        this.print0(this.ucase ? "DATETIME " : "datetime ");
        this.printExpr(literal, this.parameterized);
        return false;
    }
    
    @Override
    public boolean visit(final SQLRealExpr x) {
        final Float value = x.getValue();
        this.print0(this.ucase ? "REAL '" : "real '");
        this.print(value);
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLDecimalExpr x) {
        final BigDecimal value = x.getValue();
        this.print0(this.ucase ? "DECIMAL '" : "decimal '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLDoubleExpr x) {
        final Double value = x.getValue();
        this.print0(this.ucase ? "DOUBLE '" : "double '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLFloatExpr x) {
        final Float value = x.getValue();
        this.print0(this.ucase ? "FLOAT '" : "float '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLSmallIntExpr x) {
        final Short value = x.getValue();
        this.print0(this.ucase ? "SMALLINT '" : "smallint '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLTinyIntExpr x) {
        final Byte value = x.getValue();
        this.print0(this.ucase ? "TINYINT '" : "tinyint '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLBigIntExpr x) {
        final Long value = x.getValue();
        this.print0(this.ucase ? "BIGINT '" : "bigint '");
        this.print(value.toString());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final SQLLimit x) {
        this.print0(this.ucase ? "LIMIT " : "limit ");
        final SQLExpr offset = x.getOffset();
        if (offset != null) {
            this.printExpr(offset, this.parameterized);
            this.print0(", ");
        }
        final SQLExpr rowCount = x.getRowCount();
        if (rowCount != null) {
            this.printExpr(rowCount, this.parameterized);
        }
        final List<SQLExpr> by = x.getBy();
        if (by != null) {
            this.print0(this.ucase ? " BY " : " by ");
            for (int i = 0; i < by.size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                by.get(i).accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDescribeStatement x) {
        this.print0(this.ucase ? "DESC " : "desc ");
        if (x.getObjectType() != null) {
            this.print0(x.getObjectType().name());
            this.print(' ');
        }
        if (x.isExtended()) {
            this.print0(this.ucase ? "EXTENDED " : "extended ");
        }
        if (x.isFormatted()) {
            this.print0(this.ucase ? "FORMATTED " : "formatted ");
        }
        if (x.getObject() != null) {
            x.getObject().accept(this);
        }
        final SQLName column = x.getColumn();
        if (column != null) {
            this.print(' ');
            column.accept(this);
        }
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        return false;
    }
    
    protected void printHierarchical(final SQLSelectQueryBlock x) {
        final SQLExpr startWith = x.getStartWith();
        final SQLExpr connectBy = x.getConnectBy();
        if (startWith != null || connectBy != null) {
            this.println();
            if (x.getStartWith() != null) {
                this.print0(this.ucase ? "START WITH " : "start with ");
                x.getStartWith().accept(this);
                this.println();
            }
            this.print0(this.ucase ? "CONNECT BY " : "connect by ");
            if (x.isNoCycle()) {
                this.print0(this.ucase ? "NOCYCLE " : "nocycle ");
            }
            if (x.isPrior()) {
                this.print0(this.ucase ? "PRIOR " : "prior ");
            }
            x.getConnectBy().accept(this);
        }
    }
    
    public void printOracleSegmentAttributes(final OracleSegmentAttributes x) {
        if (x.getPctfree() != null) {
            this.println();
            this.print0(this.ucase ? "PCTFREE " : "pctfree ");
            this.print(x.getPctfree());
        }
        if (x.getPctused() != null) {
            this.println();
            this.print0(this.ucase ? "PCTUSED " : "pctused ");
            this.print(x.getPctused());
        }
        if (x.getInitrans() != null) {
            this.println();
            this.print0(this.ucase ? "INITRANS " : "initrans ");
            this.print(x.getInitrans());
        }
        if (x.getMaxtrans() != null) {
            this.println();
            this.print0(this.ucase ? "MAXTRANS " : "maxtrans ");
            this.print(x.getMaxtrans());
        }
        if (x.getCompress() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOCOMPRESS" : "nocompress");
        }
        else if (x.getCompress() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "COMPRESS" : "compress");
            if (x.getCompressLevel() != null) {
                this.print(' ');
                this.print(x.getCompressLevel());
            }
        }
        if (x.getLogging() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "LOGGING" : "logging");
        }
        else if (x.getLogging() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOLOGGING" : "nologging");
        }
        if (x.getTablespace() != null) {
            this.println();
            this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
            x.getTablespace().accept(this);
        }
        if (x.getStorage() != null) {
            this.println();
            x.getStorage().accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLWhileStatement x) {
        final String label = x.getLabelName();
        if (label != null && label.length() != 0) {
            this.print0(x.getLabelName());
            this.print0(": ");
        }
        this.print0(this.ucase ? "WHILE " : "while ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " DO" : " do");
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }
        this.println();
        this.print0(this.ucase ? "END WHILE" : "end while");
        if (label != null && label.length() != 0) {
            this.print(' ');
            this.print0(label);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDeclareStatement x) {
        final boolean printDeclare = !(x.getParent() instanceof OracleCreatePackageStatement);
        if (printDeclare) {
            this.print0(this.ucase ? "DECLARE " : "declare ");
        }
        this.printAndAccept(x.getItems(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLReturnStatement x) {
        this.print0(this.ucase ? "RETURN" : "return");
        if (x.getExpr() != null) {
            this.print(' ');
            x.getExpr().accept(this);
        }
        return false;
    }
    
    @Override
    public void postVisit(final SQLObject x) {
        if (x instanceof SQLStatement) {
            final SQLStatement stmt = (SQLStatement)x;
            final boolean printSemi = (this.printStatementAfterSemi == null) ? stmt.isAfterSemi() : this.printStatementAfterSemi;
            if (printSemi) {
                this.print(';');
            }
        }
    }
    
    @Override
    public boolean visit(final SQLArgument x) {
        final SQLParameter.ParameterType type = x.getType();
        if (type != null) {
            this.print0(type.name());
            this.print(' ');
        }
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCommitStatement x) {
        this.print0(this.ucase ? "COMMIT" : "commit");
        if (x.isWrite()) {
            this.print0(this.ucase ? " WRITE" : " write");
            if (x.getWait() != null) {
                if (x.getWait()) {
                    this.print0(this.ucase ? " WAIT" : " wait");
                }
                else {
                    this.print0(this.ucase ? " NOWAIT" : " nowait");
                }
            }
            if (x.getImmediate() != null) {
                if (x.getImmediate()) {
                    this.print0(this.ucase ? " IMMEDIATE" : " immediate");
                }
                else {
                    this.print0(this.ucase ? " BATCH" : " batch");
                }
            }
        }
        if (x.isWork()) {
            this.print0(this.ucase ? " WORK" : " work");
        }
        if (x.getChain() != null) {
            if (x.getChain()) {
                this.print0(this.ucase ? " AND CHAIN" : " and chain");
            }
            else {
                this.print0(this.ucase ? " AND NO CHAIN" : " and no chain");
            }
        }
        if (x.getRelease() != null) {
            if (x.getRelease()) {
                this.print0(this.ucase ? " AND RELEASE" : " and release");
            }
            else {
                this.print0(this.ucase ? " AND NO RELEASE" : " and no release");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLFlashbackExpr x) {
        this.print0(x.getType().name());
        this.print(' ');
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLBinaryOpExpr) {
            this.print('(');
            expr.accept(this);
            this.print(')');
        }
        else {
            expr.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropMaterializedViewStatement x) {
        this.print0(this.ucase ? "DROP MATERIALIZED VIEW " : "drop materialized view ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowMaterializedViewStatement x) {
        this.print0(this.ucase ? "SHOW MATERIALIZED VIEWS" : "show materialized views");
        if (x.getLike() != null) {
            this.printUcase(" LIKE ");
            x.getLike().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateMaterializedViewStatement x) {
        this.print0(this.ucase ? "SHOW CREATE MATERIALIZED VIEW " : "show create materialized view ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLRefreshMaterializedViewStatement x) {
        this.print0(this.ucase ? "REFRESH MATERIALIZED VIEW " : "refresh materialized view ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterMaterializedViewStatement x) {
        this.print0(this.ucase ? "ALTER MATERIALIZED VIEW " : "alter materialized view ");
        x.getName().accept(this);
        if (x.isRefresh()) {
            this.println();
            this.print(this.ucase ? "REFRESH" : "refresh");
            if (x.isRefreshFast()) {
                this.print(this.ucase ? " FAST" : " fast");
            }
            else if (x.isRefreshComplete()) {
                this.print(this.ucase ? " COMPLETE" : " complete");
            }
            else if (x.isRefreshForce()) {
                this.print(this.ucase ? " FORCE" : " force");
            }
            if (x.isRefreshOnCommit()) {
                this.print(this.ucase ? " ON COMMIT" : " on commit");
            }
            else if (x.isRefreshOnDemand()) {
                this.print(this.ucase ? " ON DEMAND" : " on demand");
            }
            else if (x.isRefreshOnOverWrite()) {
                this.print(this.ucase ? " ON OVERWRITE" : " on overwrite");
            }
            if (x.getStartWith() != null) {
                this.println();
                this.print(this.ucase ? "START WITH " : "start with ");
                x.getStartWith().accept(this);
            }
            if (x.getNext() != null) {
                this.print(this.ucase ? " NEXT " : " next ");
                x.getNext().accept(this);
            }
        }
        final Boolean enableQueryRewrite = x.getEnableQueryRewrite();
        if (enableQueryRewrite != null) {
            this.println();
            if (enableQueryRewrite) {
                this.print(this.ucase ? "ENABLE QUERY REWRITE" : "enable query rewrite");
            }
            else {
                this.print(this.ucase ? "DISABLE QUERY REWRITE" : "disable query rewrite");
            }
        }
        if (x.isRebuild()) {
            this.print0(this.ucase ? " REBUILD" : " rebuild");
        }
        final List<SQLExpr> partitions = x.getPartitions();
        if (!partitions.isEmpty()) {
            this.print0(this.ucase ? "PARTITIONS (" : "partitions (");
            this.printAndAccept(partitions, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public void endVisit(final SQLAlterMaterializedViewStatement x) {
    }
    
    @Override
    public boolean visit(final SQLCreateMaterializedViewStatement x) {
        this.print0(this.ucase ? "CREATE MATERIALIZED VIEW " : "create materialized view ");
        x.getName().accept(this);
        if (this.dbType == DbType.mysql) {
            this.printTableElements(x.getTableElementList());
            if (x.getDistributedByType() != null) {
                this.println();
                if (this.isEnabled(VisitorFeature.OutputDistributedLiteralInCreateTableStmt)) {
                    this.print0(this.ucase ? "DISTRIBUTED BY " : "distributed by ");
                }
                else {
                    this.print0(this.ucase ? "DISTRIBUTE BY " : "distribute by ");
                }
                final SQLName distributeByType = x.getDistributedByType();
                if ("HASH".equalsIgnoreCase(distributeByType.getSimpleName())) {
                    this.print0(this.ucase ? "HASH(" : "hash(");
                    this.printAndAccept(x.getDistributedBy(), ",");
                    this.print0(")");
                }
                else if ("BROADCAST".equalsIgnoreCase(distributeByType.getSimpleName())) {
                    this.print0(this.ucase ? "BROADCAST " : "broadcast ");
                }
            }
            for (final SQLAssignItem option : x.getTableOptions()) {
                final String key = ((SQLIdentifierExpr)option.getTarget()).getName();
                this.print(' ');
                this.print0(this.ucase ? key : key.toLowerCase());
                if ("TABLESPACE".equals(key)) {
                    this.print(' ');
                    option.getValue().accept(this);
                }
                else {
                    this.print0(" = ");
                    option.getValue().accept(this);
                }
            }
            if (x.getComment() != null) {
                this.println();
                this.print0(this.ucase ? "COMMENT " : "comment ");
                x.getComment().accept(this);
            }
        }
        final SQLPartitionBy partitionBy = x.getPartitionBy();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        this.printOracleSegmentAttributes(x);
        this.println();
        final Boolean cache = x.getCache();
        if (cache != null) {
            this.print(((boolean)cache) ? "CACHE" : "NOCACHE");
            this.println();
        }
        final Boolean parallel = x.getParallel();
        if (parallel != null) {
            if (parallel) {
                this.print(this.ucase ? "PARALLEL" : "parallel");
                final Integer parallelValue = x.getParallelValue();
                if (parallelValue != null) {
                    this.print(' ');
                    this.print(parallelValue);
                }
            }
            else {
                this.print(this.ucase ? "NOPARALLEL" : "noparallel");
            }
            this.println();
        }
        if (x.isBuildImmediate()) {
            this.println(this.ucase ? "BUILD IMMEDIATE" : "build immediate");
        }
        if (x.isRefresh()) {
            this.print(this.ucase ? "REFRESH" : "refresh");
            if (x.isRefreshFast()) {
                this.print(this.ucase ? " FAST" : " fast");
            }
            else if (x.isRefreshComplete()) {
                this.print(this.ucase ? " COMPLETE" : " complete");
            }
            else if (x.isRefreshForce()) {
                this.print(this.ucase ? " FORCE" : " force");
            }
            if (x.isRefreshOnCommit()) {
                this.print(this.ucase ? " ON COMMIT" : " on commit");
            }
            else if (x.isRefreshOnDemand()) {
                this.print(this.ucase ? " ON DEMAND" : " on demand");
            }
            else if (x.isRefreshOnOverWrite()) {
                this.print(this.ucase ? " ON OVERWRITE" : " on overwrite");
            }
            if (x.getStartWith() != null) {
                this.println();
                this.print(this.ucase ? "START WITH " : "start with ");
                x.getStartWith().accept(this);
            }
            if (x.getNext() != null) {
                this.print(this.ucase ? " NEXT " : " next ");
                x.getNext().accept(this);
            }
            this.println();
        }
        final Boolean enableQueryRewrite = x.getEnableQueryRewrite();
        if (enableQueryRewrite != null) {
            if (enableQueryRewrite) {
                this.print(this.ucase ? "ENABLE QUERY REWRITE" : "enable query rewrite");
            }
            else {
                this.print(this.ucase ? "DISABLE QUERY REWRITE" : "disable query rewrite");
            }
            this.println();
        }
        this.println(this.ucase ? "AS" : "as");
        x.getQuery().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateUserStatement x) {
        this.print0(this.ucase ? "CREATE USER " : "create user ");
        x.getUser().accept(this);
        this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
        x.getPassword().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterFunctionStatement x) {
        this.print0(this.ucase ? "ALTER FUNCTION " : "alter function ");
        x.getName().accept(this);
        if (x.isDebug()) {
            this.print0(this.ucase ? " DEBUG" : " debug");
        }
        if (x.isReuseSettings()) {
            this.print0(this.ucase ? " REUSE SETTINGS" : " reuse settings");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTypeStatement x) {
        this.print0(this.ucase ? "ALTER TYPE " : "alter type ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.isBody()) {
            this.print0(this.ucase ? " BODY" : " body");
        }
        if (x.isDebug()) {
            this.print0(this.ucase ? " DEBUG" : " debug");
        }
        if (x.isReuseSettings()) {
            this.print0(this.ucase ? " REUSE SETTINGS" : " reuse settings");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIntervalExpr x) {
        this.print0(this.ucase ? "INTERVAL " : "interval ");
        final SQLExpr value = x.getValue();
        value.accept(this);
        final SQLIntervalUnit unit = x.getUnit();
        if (unit != null) {
            this.print(' ');
            this.print0(this.ucase ? unit.name : unit.name_lcase);
        }
        return false;
    }
    
    public Boolean getPrintStatementAfterSemi() {
        return this.printStatementAfterSemi;
    }
    
    public void setPrintStatementAfterSemi(final Boolean printStatementAfterSemi) {
        this.printStatementAfterSemi = printStatementAfterSemi;
    }
    
    @Override
    public void config(final VisitorFeature feature, final boolean state) {
        super.config(feature, state);
        if (feature == VisitorFeature.OutputUCase) {
            this.ucase = state;
        }
        else if (feature == VisitorFeature.OutputParameterized) {
            this.parameterized = state;
        }
        else if (feature == VisitorFeature.OutputParameterizedQuesUnMergeInList) {
            this.parameterizedQuesUnMergeInList = state;
        }
        else if (feature == VisitorFeature.OutputParameterizedQuesUnMergeValuesList) {
            this.parameterizedQuesUnMergeValuesList = state;
        }
        else if (feature == VisitorFeature.OutputParameterizedUnMergeShardingTable) {
            this.shardingSupport = !state;
            this.parameterizedQuesUnMergeValuesList = state;
        }
        else if (feature == VisitorFeature.OutputNameQuote) {
            this.printNameQuote = state;
        }
    }
    
    @Override
    public void setFeatures(final int features) {
        super.setFeatures(features);
        this.ucase = this.isEnabled(VisitorFeature.OutputUCase);
        this.parameterized = this.isEnabled(VisitorFeature.OutputParameterized);
        this.parameterizedQuesUnMergeInList = this.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeInList);
        this.parameterizedQuesUnMergeValuesList = this.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeValuesList);
        this.shardingSupport = !this.isEnabled(VisitorFeature.OutputParameterizedUnMergeShardingTable);
        this.printNameQuote = this.isEnabled(VisitorFeature.OutputNameQuote);
    }
    
    public boolean visit(final OracleCursorExpr x) {
        this.print0(this.ucase ? "CURSOR(" : "cursor(");
        ++this.indentCount;
        this.println();
        x.getQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    public boolean visit(final OracleDatetimeExpr x) {
        x.getExpr().accept(this);
        final SQLExpr timeZone = x.getTimeZone();
        if (timeZone instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)timeZone).getName().equalsIgnoreCase("LOCAL")) {
            this.print0(this.ucase ? " AT LOCAL" : "alter session set ");
            return false;
        }
        this.print0(this.ucase ? " AT TIME ZONE " : " at time zone ");
        timeZone.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLLateralViewTableSource x) {
        final SQLTableSource tableSource = x.getTableSource();
        if (tableSource != null) {
            tableSource.accept(this);
        }
        ++this.indentCount;
        this.println();
        this.print0(this.ucase ? "LATERAL VIEW " : "lateral view ");
        if (x.isOuter()) {
            this.print0(this.ucase ? "OUTER " : "outer ");
        }
        x.getMethod().accept(this);
        this.print(' ');
        this.print0(x.getAlias());
        if (x.getColumns() != null && x.getColumns().size() > 0) {
            this.print0(this.ucase ? " AS " : " as ");
            this.printAndAccept(x.getColumns(), ", ");
        }
        final SQLExpr on = x.getOn();
        if (on != null) {
            this.println();
            this.print0(this.ucase ? "ON " : "on ");
            this.printExpr(on);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowErrorsStatement x) {
        this.print0(this.ucase ? "SHOW ERRORS" : "show errors");
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowRecylebinStatement x) {
        this.print0(this.ucase ? "SHOW RECYCLEBIN" : "show recyclebin");
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCatalogsStatement x) {
        this.print0(this.ucase ? "SHOW CATALOGS" : "show catalogs");
        final SQLExpr like = x.getLike();
        if (like != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            this.printExpr(like);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowFunctionsStatement x) {
        this.print0(this.ucase ? "SHOW FUNCTIONS" : "show functions");
        final SQLExpr like = x.getLike();
        if (like != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            this.printExpr(like);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowSessionStatement x) {
        this.print0(this.ucase ? "SHOW SESSION" : "show session");
        final SQLExpr like = x.getLike();
        if (like != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            this.printExpr(like);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterCharacter x) {
        if (x.getCharacterSet() != null) {
            this.print0(this.ucase ? "CHARACTER SET = " : "character set = ");
            x.getCharacterSet().accept(this);
        }
        if (x.getCollate() != null) {
            if (x.getCharacterSet() != null) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "COLLATE = " : "collate = ");
            x.getCollate().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLRecordDataType x) {
        this.print0(this.ucase ? "RECORD (" : "record (");
        ++this.indentCount;
        this.println();
        final List<SQLColumnDefinition> columns = x.getColumns();
        for (int i = 0; i < columns.size(); ++i) {
            if (i != 0) {
                this.println();
            }
            columns.get(i).accept(this);
            if (i != columns.size() - 1) {
                this.print0(", ");
            }
        }
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprStatement x) {
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLBlockStatement x) {
        if (x.getParameters().size() != 0) {
            ++this.indentCount;
            if (x.getParent() instanceof SQLCreateProcedureStatement) {
                final SQLCreateProcedureStatement procedureStatement = (SQLCreateProcedureStatement)x.getParent();
                if (procedureStatement.isCreate()) {
                    this.printIndent();
                }
            }
            if (!(x.getParent() instanceof SQLCreateProcedureStatement) && !(x.getParent() instanceof SQLCreateFunctionStatement) && !(x.getParent() instanceof OracleFunctionDataType) && !(x.getParent() instanceof OracleProcedureDataType)) {
                this.print0(this.ucase ? "DECLARE" : "declare");
                this.println();
            }
            for (int i = 0, size = x.getParameters().size(); i < size; ++i) {
                if (i != 0) {
                    this.println();
                }
                final SQLParameter param = x.getParameters().get(i);
                param.accept(this);
                this.print(';');
            }
            --this.indentCount;
            this.println();
        }
        this.print0(this.ucase ? "BEGIN" : "begin");
        ++this.indentCount;
        for (int i = 0, size = x.getStatementList().size(); i < size; ++i) {
            this.println();
            final SQLStatement stmt = x.getStatementList().get(i);
            stmt.accept(this);
        }
        --this.indentCount;
        final SQLStatement exception = x.getException();
        if (exception != null) {
            this.println();
            exception.accept(this);
        }
        this.println();
        this.print0(this.ucase ? "END;" : "end;");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateProcedureStatement x) {
        final boolean create = x.isCreate();
        if (!create) {
            this.print0(this.ucase ? "PROCEDURE " : "procedure ");
        }
        else if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE PROCEDURE " : "create or replace procedure ");
        }
        else {
            this.print0(this.ucase ? "CREATE PROCEDURE " : "create procedure ");
        }
        x.getName().accept(this);
        final int paramSize = x.getParameters().size();
        if (paramSize > 0) {
            this.print0(" (");
            ++this.indentCount;
            this.println();
            for (int i = 0; i < paramSize; ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }
                final SQLParameter param = x.getParameters().get(i);
                param.accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final SQLName authid = x.getAuthid();
        if (authid != null) {
            this.print(this.ucase ? " AUTHID " : " authid ");
            authid.accept(this);
        }
        final SQLStatement block = x.getBlock();
        final String wrappedSource = x.getWrappedSource();
        if (wrappedSource != null) {
            this.print0(this.ucase ? " WRAPPED " : " wrapped ");
            this.print0(wrappedSource);
        }
        else {
            if (block != null && !create) {
                this.println();
                this.print("IS");
                this.println();
            }
            else {
                this.println();
                if (block instanceof SQLBlockStatement) {
                    final SQLBlockStatement blockStatement = (SQLBlockStatement)block;
                    if (blockStatement.getParameters().size() > 0 || authid != null) {
                        this.println(this.ucase ? "AS" : "as");
                    }
                    else {
                        this.println(this.ucase ? "IS" : "is");
                    }
                }
            }
            final String javaCallSpec = x.getJavaCallSpec();
            if (javaCallSpec != null) {
                this.print0(this.ucase ? "LANGUAGE JAVA NAME '" : "language java name '");
                this.print0(javaCallSpec);
                this.print('\'');
                return false;
            }
        }
        boolean afterSemi = false;
        if (block != null) {
            block.accept(this);
            if (block instanceof SQLBlockStatement && ((SQLBlockStatement)block).getStatementList().size() > 0) {
                afterSemi = ((SQLBlockStatement)block).getStatementList().get(0).isAfterSemi();
            }
        }
        if (!afterSemi && x.getParent() instanceof OracleCreatePackageStatement) {
            this.print(';');
        }
        return false;
    }
    
    protected boolean hiveVisit(final SQLExternalRecordFormat x) {
        ++this.indentCount;
        if (x.getDelimitedBy() != null) {
            this.println();
            this.print0(this.ucase ? "LINES TERMINATED BY " : "lines terminated by ");
            x.getDelimitedBy().accept(this);
        }
        final SQLExpr terminatedBy = x.getTerminatedBy();
        if (terminatedBy != null) {
            this.println();
            this.print0(this.ucase ? "FIELDS TERMINATED BY " : "fields terminated by ");
            terminatedBy.accept(this);
        }
        final SQLExpr escapedBy = x.getEscapedBy();
        if (escapedBy != null) {
            this.println();
            this.print0(this.ucase ? "ESCAPED BY " : "escaped by ");
            escapedBy.accept(this);
        }
        final SQLExpr collectionItemsTerminatedBy = x.getCollectionItemsTerminatedBy();
        if (collectionItemsTerminatedBy != null) {
            this.println();
            this.print0(this.ucase ? "COLLECTION ITEMS TERMINATED BY " : "collection items terminated by ");
            collectionItemsTerminatedBy.accept(this);
        }
        final SQLExpr mapKeysTerminatedBy = x.getMapKeysTerminatedBy();
        if (mapKeysTerminatedBy != null) {
            this.println();
            this.print0(this.ucase ? "MAP KEYS TERMINATED BY " : "map keys terminated by ");
            mapKeysTerminatedBy.accept(this);
        }
        final SQLExpr linesTerminatedBy = x.getLinesTerminatedBy();
        if (linesTerminatedBy != null) {
            this.println();
            this.print0(this.ucase ? "LINES TERMINATED BY " : "lines terminated by ");
            linesTerminatedBy.accept(this);
        }
        final SQLExpr nullDefinedAs = x.getNullDefinedAs();
        if (nullDefinedAs != null) {
            this.println();
            this.print0(this.ucase ? "NULL DEFINED AS " : "null defined as ");
            nullDefinedAs.accept(this);
        }
        final SQLExpr serde = x.getSerde();
        if (serde != null) {
            this.println();
            this.print0(this.ucase ? "SERDE " : "serde ");
            serde.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLExternalRecordFormat x) {
        final SQLExpr delimitedBy = x.getDelimitedBy();
        if (delimitedBy != null) {
            this.println();
            this.print0(this.ucase ? "RECORDS DELIMITED BY " : "records delimited by ");
            delimitedBy.accept(this);
        }
        final Boolean logfile = x.getLogfile();
        if (logfile != null) {
            if (logfile) {
                this.print0(this.ucase ? " LOGFILE" : " logfile");
            }
            else {
                this.print0(this.ucase ? " NOLOGFILE" : " nologfile");
            }
        }
        final Boolean badfile = x.getBadfile();
        if (badfile != null) {
            if (badfile) {
                this.print0(this.ucase ? " BADFILE" : " badfile");
            }
            else {
                this.print0(this.ucase ? " NOBADFILE" : " nobadfile");
            }
        }
        final SQLExpr terminatedBy = x.getTerminatedBy();
        if (terminatedBy != null) {
            this.println();
            this.print0(this.ucase ? "FIELDS TERMINATED BY " : "fields terminated by ");
            terminatedBy.accept(this);
        }
        if (x.isLtrim()) {
            this.print0(this.ucase ? " LTRIM" : " ltrim");
        }
        if (x.isMissingFieldValuesAreNull()) {
            this.print0(this.ucase ? " MISSING FIELD VALUES ARE NULL" : " missing field values are null");
        }
        if (x.isRejectRowsWithAllNullFields()) {
            this.print0(this.ucase ? " REJECT ROWS WITH ALL NULL FIELDS" : " reject rows with all null fields");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLArrayDataType x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (Boolean.TRUE.equals(x.getAttribute("ads.arrayDataType"))) {
            x.getComponentType().accept(this);
            this.print('[');
            this.printAndAccept(arguments, ", ");
            this.print(']');
        }
        else {
            final SQLDataType componentType = x.getComponentType();
            if (componentType != null) {
                this.print0(this.ucase ? "ARRAY<" : "array<");
                componentType.accept(this);
                this.print('>');
            }
            else {
                this.print0(this.ucase ? "ARRAY" : "array");
            }
            if (arguments.size() > 0) {
                this.print('(');
                this.printAndAccept(arguments, ", ");
                this.print(')');
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLMapDataType x) {
        this.print0(this.ucase ? "MAP<" : "map<");
        final SQLDataType keyType = x.getKeyType();
        final SQLDataType valueType = x.getValueType();
        keyType.accept(this);
        this.print0(", ");
        valueType.accept(this);
        this.print('>');
        return false;
    }
    
    @Override
    public boolean visit(final SQLStructDataType x) {
        this.print0(this.ucase ? "STRUCT<" : "struct<");
        this.printAndAccept(x.getFields(), ", ");
        this.print('>');
        return false;
    }
    
    @Override
    public boolean visit(final SQLRowDataType x) {
        this.print0(this.ucase ? "ROW(" : "row(");
        this.printAndAccept(x.getFields(), ",");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionDataType x) {
        this.print0(this.ucase ? "UNIONTYPE<" : "uniontype<");
        this.printAndAccept(x.getItems(), ", ");
        this.print('>');
        return false;
    }
    
    @Override
    public boolean visit(final SQLStructDataType.Field x) {
        final SQLName name = x.getName();
        if (name != null) {
            name.accept(this);
        }
        final SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            if (x.getParent() instanceof SQLRowDataType) {
                if (name != null) {
                    this.print(' ');
                }
            }
            else {
                this.print(':');
            }
            dataType.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableMergePartition x) {
        this.print0(this.ucase ? "MERGE " : "merge ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }
        this.println();
        this.printlnAndAccept(x.getPartitions(), ", ");
        this.println();
        this.print0(this.ucase ? "OVERWRITE" : "overwrite");
        this.println();
        x.getOverwritePartition().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionSpec x) {
        this.print0(this.ucase ? "PARTITION (" : "partition (");
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionSpec.Item x) {
        x.getColumn().accept(this);
        this.print0(" = ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableSubpartitionAvailablePartitionNum x) {
        this.print0(this.ucase ? "SUBPARTITION_AVAILABLE_PARTITION_NUM = " : "subpartition_available_partition_num = ");
        x.getNumber().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowDatabasesStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL " : "full ");
        }
        if (x.isPhysical()) {
            this.print0(this.ucase ? "PHYSICAL " : "physical ");
        }
        this.print0(this.ucase ? "DATABASES" : "databases");
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.isExtra()) {
            this.print0(this.ucase ? " EXTRA" : " extra");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowTableGroupsStatement x) {
        this.print0(this.ucase ? "SHOW TABLEGROUPS" : "show tablegroups");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " IN " : " in ");
            x.getDatabase().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowColumnsStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (x.isFull()) {
            this.print0(this.ucase ? "SHOW FULL COLUMNS" : "show full columns");
        }
        else {
            this.print0(this.ucase ? "SHOW COLUMNS" : "show columns");
        }
        if (x.getTable() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            if (x.getDatabase() != null) {
                x.getDatabase().accept(this);
                this.print('.');
            }
            x.getTable().accept(this);
        }
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateTableStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (x.isAll()) {
            this.print0(this.ucase ? "SHOW ALL CREATE TABLE " : "show all create table ");
        }
        else {
            this.print0(this.ucase ? "SHOW CREATE TABLE " : "show create table ");
        }
        x.getName().accept(this);
        if (x.getLikeMapping() != null) {
            this.print0(this.ucase ? " LIKE MAPPING (" : " like mapping ");
            x.getLikeMapping().accept(this);
            this.print0(")");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowProcessListStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL " : "full ");
        }
        this.print0(this.ucase ? "PROCESSLIST" : "processlist");
        if (x.isMpp()) {
            this.print0(this.ucase ? " MPP" : " mpp");
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            where.accept(this);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.print(' ');
            orderBy.accept(this);
        }
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.print(' ');
            limit.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableSetOption x) {
        this.print0(this.ucase ? "SET TBLPROPERTIES (" : "set tblproperties (");
        this.printAndAccept(x.getOptions(), ", ");
        this.print(')');
        final SQLExpr on = x.getOn();
        if (on != null) {
            this.print0(this.ucase ? " ON " : " on ");
            on.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateViewStatement x) {
        this.print0(this.ucase ? "SHOW CREATE VIEW " : "show create view ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowViewsStatement x) {
        this.print0(this.ucase ? "SHOW VIEWS" : "show views");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
        }
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRenameIndex x) {
        this.print0(this.ucase ? "RENAME INDEX " : "rename index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateRoleStatement x) {
        this.print0(this.ucase ? "CREATE ROLE " : "create role ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropRoleStatement x) {
        this.print0(this.ucase ? "DROP ROLE " : "drop role ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(final SQLMatchAgainstExpr x) {
    }
    
    @Override
    public boolean visit(final SQLMatchAgainstExpr x) {
        this.print0(this.ucase ? "MATCH (" : "match (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        this.print0(this.ucase ? " AGAINST (" : " against (");
        x.getAgainst().accept(this);
        if (x.getSearchModifier() != null) {
            this.print(' ');
            this.print0(this.ucase ? x.getSearchModifier().name : x.getSearchModifier().name_lcase);
        }
        this.print(')');
        return false;
    }
    
    public boolean visit(final MySqlPrimaryKey x) {
        this.visit(x.getIndexDefinition());
        return false;
    }
    
    public boolean visit(final MySqlCreateTableStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "CREATE " : "create ");
        for (final SQLCommentHint hint : x.getHints()) {
            hint.accept(this);
            this.print(' ');
        }
        if (x.isDimension()) {
            this.print0(this.ucase ? "DIMENSION " : "dimension ");
        }
        if (SQLCreateTableStatement.Type.GLOBAL_TEMPORARY.equals(x.getType())) {
            this.print0(this.ucase ? "TEMPORARY TABLE " : "temporary table ");
        }
        else if (SQLCreateTableStatement.Type.SHADOW.equals(x.getType())) {
            this.print0(this.ucase ? "SHADOW TABLE " : "shadow table ");
        }
        else if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL TABLE " : "external table ");
        }
        else {
            this.print0(this.ucase ? "TABLE " : "table ");
        }
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        this.printTableSourceExpr(x.getName());
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        this.printTableElements(x.getTableElementList());
        if (x.isBroadCast()) {
            this.print0(this.ucase ? " BROADCAST" : " broadcast");
        }
        final List<SQLAssignItem> tableOptions = x.getTableOptions();
        if (Boolean.TRUE.equals(x.getAttribute("ads.options"))) {
            if (tableOptions.size() > 0) {
                this.println();
                this.print0(this.ucase ? "OPTIONS (" : "options (");
                this.printAndAccept(tableOptions, ", ");
                this.print(')');
            }
        }
        else {
            for (final SQLAssignItem option : tableOptions) {
                final String key = ((SQLIdentifierExpr)option.getTarget()).getName();
                this.print(' ');
                this.print0(this.ucase ? key : key.toLowerCase());
                if ("TABLESPACE".equals(key)) {
                    this.print(' ');
                    option.getValue().accept(this);
                }
                else {
                    this.print0(" = ");
                    option.getValue().accept(this);
                }
            }
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            comment.accept(this);
        }
        if (x.getDistributeByType() != null) {
            this.println();
            if (this.isEnabled(VisitorFeature.OutputDistributedLiteralInCreateTableStmt)) {
                this.print0(this.ucase ? "DISTRIBUTED BY " : "distributed by ");
            }
            else {
                this.print0(this.ucase ? "DISTRIBUTE BY " : "distribute by ");
            }
            final SQLName distributeByType = x.getDistributeByType();
            if ("HASH".equalsIgnoreCase(distributeByType.getSimpleName())) {
                this.print0(this.ucase ? "HASH(" : "hash(");
                this.printAndAccept(x.getDistributeBy(), ",");
                this.print0(")");
            }
            else if ("BROADCAST".equalsIgnoreCase(distributeByType.getSimpleName())) {
                this.print0(this.ucase ? "BROADCAST " : "broadcast ");
            }
        }
        final SQLPartitionBy partitionBy = x.getPartitioning();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        final List<SQLSelectOrderByItem> clusteredBy = x.getClusteredBy();
        if (clusteredBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "CLUSTERED BY (" : "clustered by (");
            this.printAndAccept(clusteredBy, ",");
            this.print0(")");
        }
        final SQLExpr dbPartitionBy = x.getDbPartitionBy();
        if (dbPartitionBy != null) {
            this.println();
            this.print0(this.ucase ? "DBPARTITION BY " : "dbpartition by ");
            dbPartitionBy.accept(this);
        }
        final SQLExpr dbpartitions = x.getDbpartitions();
        if (dbpartitions != null) {
            this.print0(this.ucase ? " DBPARTITIONS " : " dbpartitions ");
            dbpartitions.accept(this);
        }
        final SQLExpr tbPartitionsBy = x.getTablePartitionBy();
        if (tbPartitionsBy != null) {
            this.println();
            this.print0(this.ucase ? "TBPARTITION BY " : "tbpartition by ");
            tbPartitionsBy.accept(this);
        }
        final SQLExpr tablePartitions = x.getTablePartitions();
        if (tablePartitions != null) {
            this.print0(this.ucase ? " TBPARTITIONS " : " tbpartitions ");
            tablePartitions.accept(this);
        }
        final MySqlExtPartition extPartition = x.getExtPartition();
        if (extPartition != null) {
            this.println();
            extPartition.accept(this);
        }
        if (x.getArchiveBy() != null) {
            this.println();
            this.print0(this.ucase ? "ARCHIVE BY = " : "archive by = ");
            x.getArchiveBy().accept(this);
        }
        if (x.getTableGroup() != null) {
            this.println();
            this.print0(this.ucase ? "TABLEGROUP " : "tablegroup ");
            x.getTableGroup().accept(this);
        }
        if (x.isReplace()) {
            this.println();
            this.print0(this.ucase ? "REPLACE " : "replace ");
        }
        else if (x.isIgnore()) {
            this.println();
            this.print0(this.ucase ? "IGNORE " : "ignore ");
        }
        if (x.getSelect() != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            x.getSelect().accept(this);
        }
        if (x.getStoredBy() != null) {
            this.println();
            this.print0(this.ucase ? " STORED BY " : " stored by ");
            x.getStoredBy().accept(this);
        }
        if (x.getWith().size() > 0) {
            this.println();
            this.print0(this.ucase ? " WITH (" : " with (");
            int i = 0;
            for (final Map.Entry<String, SQLName> option2 : x.getWith().entrySet()) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.print0(option2.getKey());
                this.print0(" = ");
                option2.getValue().accept(this);
                ++i;
            }
            this.print(')');
        }
        if (x.getWithData() != null) {
            this.println();
            if (x.getWithData()) {
                this.print0(this.ucase ? "WITH DATA" : "with data");
            }
            else {
                this.print0(this.ucase ? "WITH NO DATA" : "with no data");
            }
        }
        for (final SQLCommentHint hint2 : x.getOptionHints()) {
            this.print(' ');
            hint2.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowPartitionsStmt x) {
        this.print0(this.ucase ? "SHOW PARTITIONS " : "show partitions ");
        x.getTableSource().accept(this);
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(")");
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            where.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLValuesExpr x) {
        this.print0(this.ucase ? "VALUES (" : "values (");
        this.printAndAccept(x.getValues(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLDumpStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "DUMP DATA " : "dump data ");
        if (x.isOverwrite()) {
            this.print0(this.ucase ? "OVERWRITE " : "overwrite ");
        }
        final SQLExprTableSource into = x.getInto();
        if (into != null) {
            this.print0(this.ucase ? "INTO " : "into ");
            into.accept(this);
            this.print0(" ");
        }
        x.getSelect().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLValuesTableSource x) {
        final List<SQLName> columns = x.getColumns();
        final boolean quote = columns.size() > 0;
        if (quote) {
            this.print('(');
        }
        this.print0(this.ucase ? "VALUES " : "values ");
        this.printAndAccept(x.getValues(), ", ");
        if (quote) {
            this.print(')');
        }
        final String alias = x.getAlias();
        if (alias != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(alias);
        }
        if (columns.size() > 0) {
            if (alias == null) {
                this.print0(this.ucase ? " AS" : " as");
            }
            this.print0(" (");
            this.printAndAccept(columns, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExtractExpr x) {
        this.print0(this.ucase ? "EXTRACT(" : "extract(");
        this.print0(x.getUnit().name());
        this.print0(this.ucase ? " FROM " : " from ");
        x.getValue().accept(this);
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLWindow x) {
        x.getName().accept(this);
        this.print0(this.ucase ? " AS " : " as ");
        x.getOver().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLJSONExpr x) {
        this.print0(this.ucase ? "JSON " : "json ");
        this.printChars(x.getLiteral());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAnnIndex x) {
        this.print0(this.ucase ? "ANNINDX (type = '" : "annindx (type = '");
        final int indexType = x.getIndexType();
        if ((indexType & SQLAnnIndex.IndexType.Flat.mask) != 0x0) {
            this.print0(this.ucase ? "FLAT" : "flat");
        }
        if ((indexType & SQLAnnIndex.IndexType.FastIndex.mask) != 0x0) {
            if (indexType != SQLAnnIndex.IndexType.FastIndex.mask) {
                this.print(',');
            }
            this.print0(this.ucase ? "FLAT_INDEX" : "fast_index");
        }
        this.print0(this.ucase ? "', DISTANCE = '" : "', distance = '");
        this.print0(x.getDistance().name());
        this.print0("'");
        final int rtIndexType = x.getRtIndexType();
        if (rtIndexType != 0) {
            this.print0(this.ucase ? ", RTTYPE = '" : ", rttype = '");
            if ((rtIndexType & SQLAnnIndex.IndexType.Flat.mask) != 0x0) {
                this.print0(this.ucase ? "FLAT" : "flat");
            }
            if ((rtIndexType & SQLAnnIndex.IndexType.FastIndex.mask) != 0x0) {
                if (rtIndexType != SQLAnnIndex.IndexType.FastIndex.mask) {
                    this.print(',');
                }
                this.print0(this.ucase ? "FLAT_INDEX " : "fast_index ");
            }
            this.print('\'');
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRecoverPartitions x) {
        this.print0(this.ucase ? "RECOVER PARTITIONS" : "recover partitions");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterIndexStatement x) {
        this.print0(this.ucase ? "ALTER INDEX " : "alter index ");
        x.getName().accept(this);
        if (x.getRenameTo() != null) {
            this.print0(this.ucase ? " RENAME TO " : " rename to ");
            x.getRenameTo().accept(this);
        }
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            this.print0(this.ucase ? " ON " : " on ");
            table.accept(this);
        }
        if (x.getPartitions().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.getEnable() == Boolean.TRUE) {
            this.print0(this.ucase ? " ENABLE" : " enable");
        }
        if (x.getEnable() == Boolean.FALSE) {
            this.print0(this.ucase ? " DISABLE" : " disable");
        }
        if (x.isUnusable()) {
            this.print0(this.ucase ? " UNUSABLE" : " unusable");
        }
        if (x.getMonitoringUsage() != null) {
            this.print0(this.ucase ? " MONITORING USAGE" : " monitoring usage");
        }
        if (x.getRebuild() != null) {
            this.print(' ');
            x.getRebuild().accept(this);
        }
        if (x.getParallel() != null) {
            this.print0(this.ucase ? " PARALLEL" : " parallel");
            x.getParallel().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterIndexStatement.Rebuild x) {
        this.print0(this.ucase ? "REBUILD" : "rebuild");
        if (x.getOption() != null) {
            this.print(' ');
            x.getOption().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowIndexesStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "SHOW " : "show ");
        this.print0(this.ucase ? x.getType().toUpperCase() : x.getType().toLowerCase());
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getTable().accept(this);
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAnalyzeTableStatement x) {
        this.print0(this.ucase ? "ANALYZE TABLE " : "analyze table ");
        this.printAndAccept(x.getTables(), ", ");
        final SQLPartitionRef partition = x.getPartition();
        if (partition != null) {
            this.print(' ');
            partition.accept(this);
        }
        if (x.isComputeStatistics()) {
            this.print0(this.ucase ? " COMPUTE STATISTICS" : " compute statistics");
        }
        if (x.isForColums()) {
            this.print0(this.ucase ? " FOR COLUMNS" : " for columns");
        }
        if (x.isCacheMetadata()) {
            this.print0(this.ucase ? " CACHE METADATA" : " cache metadata");
        }
        if (x.isNoscan()) {
            this.print0(this.ucase ? " NOSCAN" : " noscan");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionRef x) {
        this.print0(this.ucase ? "PARTITION (" : "partition (");
        for (int i = 0; i < x.getItems().size(); ++i) {
            if (i != 0) {
                this.print(", ");
            }
            final SQLPartitionRef.Item item = x.getItems().get(i);
            this.visit(item);
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionRef.Item x) {
        this.visit(x.getColumnName());
        final SQLExpr value = x.getValue();
        if (value != null) {
            final SQLBinaryOperator operator = x.getOperator();
            if (operator == null) {
                this.print(" = ");
            }
            else {
                this.printOperator(operator);
            }
            this.printExpr(value);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExportTableStatement x) {
        this.print0(this.ucase ? "EXPORT TABLE " : "export table ");
        x.getTable().accept(this);
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        final SQLExpr to = x.getTo();
        if (to != null) {
            this.print0(this.ucase ? " TO " : " to ");
            to.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLImportTableStatement x) {
        if (x.isExtenal()) {
            this.print0(this.ucase ? "IMPORT EXTERNAL" : "import external");
        }
        else {
            this.print0(this.ucase ? "IMPORT" : "import");
        }
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            this.print0(this.ucase ? " TABLE " : " table ");
            table.accept(this);
        }
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        final SQLExpr from = x.getFrom();
        if (from != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            from.accept(this);
        }
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.print0(this.ucase ? " LOCATION " : " location ");
            location.accept(this);
        }
        if (x.getVersion() != null) {
            this.print0(this.ucase ? " VERSIOIN = " : " version = ");
            x.getVersion().accept(this);
        }
        if (x.isUsingBuild()) {
            this.print0(this.ucase ? " BUILD = 'Y'" : " build = 'y'");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTableSampling x) {
        this.print0(this.ucase ? "TABLESAMPLE " : "tablesample ");
        if (x.isBernoulli()) {
            this.print0(this.ucase ? "BERNOULLI " : "bernoulli ");
        }
        else if (x.isSystem()) {
            this.print0(this.ucase ? "SYSTEM " : "system ");
        }
        this.print('(');
        final SQLExpr bucket = x.getBucket();
        if (bucket != null) {
            this.print0(this.ucase ? "BUCKET " : "bucket ");
            bucket.accept(this);
        }
        final SQLExpr outOf = x.getOutOf();
        if (outOf != null) {
            this.print0(this.ucase ? " OUT OF " : " out of ");
            outOf.accept(this);
        }
        final SQLExpr on = x.getOn();
        if (on != null) {
            this.print0(this.ucase ? " ON " : " on ");
            on.accept(this);
        }
        final SQLExpr percent = x.getPercent();
        if (percent != null) {
            percent.accept(this);
            this.print0(this.ucase ? " PERCENT" : " percent");
        }
        final SQLExpr rows = x.getRows();
        if (rows != null) {
            rows.accept(this);
            if (this.dbType != DbType.mysql) {
                this.print0(this.ucase ? " ROWS" : " rows");
            }
        }
        final SQLExpr size = x.getByteLength();
        if (size != null) {
            size.accept(this);
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLSizeExpr x) {
        x.getValue().accept(this);
        this.print0(x.getUnit().name());
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableArchivePartition x) {
        this.print0(this.ucase ? "ARCHIVE PARTITION (" : "archive partition (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableUnarchivePartition x) {
        this.print0(this.ucase ? "UNARCHIVE PARTITION (" : "unarchive partition (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateOutlineStatement x) {
        this.print0(this.ucase ? "CREATE OUTLINE " : "create outline ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON " : " on ");
        x.getOn().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropOutlineStatement x) {
        this.print0(this.ucase ? "DROP OUTLINE " : "drop outline ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowQueryTaskStatement x) {
        if (x.isFull()) {
            this.print0(this.ucase ? "SHOW FULL QUERY_TASK" : "show full query_task");
        }
        else {
            this.print0(this.ucase ? "SHOW QUERY_TASK" : "show query_task");
        }
        if (x.getUser() != null) {
            this.println();
            this.print0(this.ucase ? "FOR " : "for ");
            this.printExpr(x.getUser());
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            this.visit(orderBy);
        }
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.println();
            this.visit(limit);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowOutlinesStatement x) {
        this.print0(this.ucase ? "SHOW OUTLINES" : "show outlines");
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            this.visit(orderBy);
        }
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.println();
            this.visit(limit);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPurgeTableStatement x) {
        this.print0(this.ucase ? "PURGE TABLE " : "purge table ");
        x.getTable().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLPurgeRecyclebinStatement x) {
        this.print0(this.ucase ? "PURGE RECYCLEBIN" : "purge recyclebin");
        return false;
    }
    
    @Override
    public boolean visit(final SQLPurgeLogsStatement x) {
        this.print0(this.ucase ? "PURGE" : "purge");
        if (x.isBinary()) {
            this.print0(this.ucase ? " BINARY" : " binary");
        }
        if (x.isMaster()) {
            this.print0(this.ucase ? " MASTER" : " MASTER");
        }
        if (x.isAll()) {
            this.print0(this.ucase ? " ALL" : " all");
            return false;
        }
        this.print0(this.ucase ? " LOGS" : " logs");
        final SQLExpr to = x.getTo();
        if (to != null) {
            this.print0(this.ucase ? " TO " : " to ");
            to.accept(this);
        }
        if (x.getBefore() != null) {
            this.print0(this.ucase ? " BEFORE " : " before ");
            x.getBefore().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterOutlineStatement x) {
        this.print0(this.ucase ? "ALTER OUTLINE " : "alter outline ");
        x.getName().accept(this);
        if (x.isResync()) {
            this.print0(this.ucase ? " RESYNC" : " resync");
        }
        if (x.isDisable()) {
            this.print0(this.ucase ? " DISABLE" : " disable");
        }
        if (x.isEnable()) {
            this.print0(this.ucase ? " ENABLE" : " enable");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddSupplemental x) {
        this.print0(this.ucase ? "ADD " : "add ");
        x.getElement().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDbLinkExpr x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)x.getExpr();
            final SQLExpr owner = methodInvokeExpr.getOwner();
            if (owner != null) {
                this.printMethodOwner(owner);
            }
            final String function = methodInvokeExpr.getMethodName();
            this.printFunctionName(function);
            this.print('@');
            this.print0(x.getDbLink());
            this.printMethodParameters(methodInvokeExpr);
        }
        else {
            if (expr != null) {
                expr.accept(this);
                this.print('@');
            }
            this.print0(x.getDbLink());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowStatisticStmt x) {
        if (x.isFull()) {
            this.print0(this.ucase ? "SHOW FULL STATS" : "show full stats");
        }
        else {
            this.print0(this.ucase ? "SHOW STATS" : "show stats");
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (!partitions.isEmpty()) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(partitions, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowStatisticListStmt x) {
        this.print0(this.ucase ? "SHOW STATISTIC_LIST" : "show statistic_list");
        final SQLExprTableSource table = x.getTableSource();
        if (table != null) {
            this.print(' ');
            table.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowPackagesStatement x) {
        this.print0(this.ucase ? "SHOW PACKAGES" : "show packages");
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowGrantsStatement x) {
        this.print0(this.ucase ? "SHOW GRANTS" : "show grants");
        final SQLExpr user = x.getUser();
        if (user != null) {
            this.print0(this.ucase ? " FOR " : " for ");
            user.accept(this);
        }
        final SQLExpr on = x.getOn();
        if (on != null) {
            this.print0(this.ucase ? " ON " : " on ");
            on.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCurrentTimeExpr x) {
        final SQLCurrentTimeExpr.Type type = x.getType();
        this.print(this.ucase ? type.name : type.name_lower);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCurrentUserExpr x) {
        this.print(this.ucase ? "CURRENT_USER" : "current_user");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAdhocTableSource x) {
        final SQLCreateTableStatement definition = x.getDefinition();
        definition.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final HiveCreateTableStatement x) {
        this.printCreateTable(x, true);
        return false;
    }
    
    protected void printCreateTable(final HiveCreateTableStatement x, final boolean printSelect) {
        final SQLObject parent = x.getParent();
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        if (!(parent instanceof SQLAdhocTableSource)) {
            this.print0(this.ucase ? "CREATE " : "create ");
        }
        if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL " : "external ");
        }
        final SQLCreateTableStatement.Type tableType = x.getType();
        if (SQLCreateTableStatement.Type.TEMPORARY.equals(tableType)) {
            this.print0(this.ucase ? "TEMPORARY " : "temporary ");
        }
        this.print0(this.ucase ? "TABLE " : "table ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        this.printTableSourceExpr(x.getName());
        this.printTableElements(x.getTableElementList());
        final SQLExprTableSource inherits = x.getInherits();
        if (inherits != null) {
            this.print0(this.ucase ? " INHERITS (" : " inherits (");
            inherits.accept(this);
            this.print(')');
        }
        final SQLExpr using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        final List<SQLAssignItem> tableOptions = x.getTableOptions();
        if (tableOptions.size() > 0) {
            this.println();
            this.print0(this.ucase ? "OPTIONS (" : "options (");
            this.incrementIndent();
            this.println();
            int i = 0;
            for (final SQLAssignItem option : tableOptions) {
                if (i != 0) {
                    this.print(",");
                    this.println();
                }
                final String key = option.getTarget().toString();
                boolean unquote = false;
                final char c0;
                if (key.length() > 0 && (c0 = key.charAt(0)) != '\"' && c0 != '`' && c0 != '\'') {
                    unquote = true;
                }
                if (unquote) {
                    this.print('\'');
                }
                this.print0(key);
                if (unquote) {
                    this.print('\'');
                }
                this.print0(" = ");
                option.getValue().accept(this);
                ++i;
            }
            this.decrementIndent();
            this.println();
            this.print(')');
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            comment.accept(this);
        }
        final List<SQLAssignItem> mappedBy = x.getMappedBy();
        if (mappedBy != null && mappedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "MAPPED BY (" : "mapped by (");
            this.printAndAccept(mappedBy, ", ");
            this.print0(this.ucase ? ")" : ")");
        }
        final int partitionSize = x.getPartitionColumns().size();
        if (partitionSize > 0) {
            this.println();
            this.print0(this.ucase ? "PARTITIONED BY (" : "partitioned by (");
            ++this.indentCount;
            this.println();
            for (int j = 0; j < partitionSize; ++j) {
                final SQLColumnDefinition column = x.getPartitionColumns().get(j);
                column.accept(this);
                if (j != partitionSize - 1) {
                    this.print(',');
                }
                if (this.isPrettyFormat() && column.hasAfterComment()) {
                    this.print(' ');
                    this.printlnComment(column.getAfterCommentsDirect());
                }
                if (j != partitionSize - 1) {
                    this.println();
                }
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final List<SQLSelectOrderByItem> clusteredBy = x.getClusteredBy();
        if (clusteredBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "CLUSTERED BY (" : "clustered by (");
            this.printAndAccept(clusteredBy, ",");
            this.print(')');
        }
        final List<SQLExpr> skewedBy = x.getSkewedBy();
        if (skewedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SKEWED BY (" : "skewed by (");
            this.printAndAccept(skewedBy, ",");
            this.print(')');
            final List<SQLExpr> skewedByOn = x.getSkewedByOn();
            if (skewedByOn.size() > 0) {
                this.print0(this.ucase ? " ON (" : " on (");
                this.printAndAccept(skewedByOn, ",");
                this.print(')');
            }
        }
        final SQLExternalRecordFormat format = x.getRowFormat();
        if (format != null) {
            this.println();
            this.print0(this.ucase ? "ROW FORMAT" : "row rowFormat");
            if (format.getSerde() == null) {
                this.print0(this.ucase ? " DELIMITED" : " delimited ");
            }
            this.visit(format);
        }
        final Map<String, SQLObject> serdeProperties = x.getSerdeProperties();
        this.printSerdeProperties(serdeProperties);
        final List<SQLSelectOrderByItem> sortedBy = x.getSortedBy();
        if (sortedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SORTED BY (" : "sorted by (");
            this.printAndAccept(sortedBy, ", ");
            this.print(')');
        }
        final int buckets = x.getBuckets();
        if (buckets > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(buckets);
            this.print0(this.ucase ? " BUCKETS" : " buckets");
        }
        final SQLExprTableSource like = x.getLike();
        if (like != null) {
            this.println();
            this.print0(this.ucase ? "LIKE " : "like ");
            like.accept(this);
        }
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.println();
            this.print0(this.ucase ? "STORED AS" : "stored as");
            if (storedAs instanceof SQLIdentifierExpr) {
                this.print(' ');
                this.printExpr(storedAs, this.parameterized);
            }
            else {
                this.incrementIndent();
                this.println();
                this.printExpr(storedAs, this.parameterized);
                this.decrementIndent();
            }
        }
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.println();
            this.print0(this.ucase ? "LOCATION " : "location ");
            this.printExpr(location, this.parameterized);
        }
        this.printTblProperties(x);
        final SQLExpr metaLifeCycle = x.getMetaLifeCycle();
        if (metaLifeCycle != null) {
            this.println();
            this.print0(this.ucase ? "META LIFECYCLE " : "meta lifecycle ");
            this.printExpr(metaLifeCycle);
        }
        final SQLSelect select = x.getSelect();
        if (printSelect && select != null) {
            this.println();
            if (x.isLikeQuery()) {
                this.print0(this.ucase ? "LIKE" : "like");
            }
            else {
                this.print0(this.ucase ? "AS" : "as");
            }
            this.println();
            this.visit(select);
        }
    }
    
    protected void printSerdeProperties(final Map<String, SQLObject> serdeProperties) {
        if (serdeProperties.isEmpty()) {
            return;
        }
        this.println();
        this.print0(this.ucase ? "WITH SERDEPROPERTIES (" : "with serdeproperties (");
        this.incrementIndent();
        this.println();
        int i = 0;
        for (final Map.Entry<String, SQLObject> option : serdeProperties.entrySet()) {
            if (i != 0) {
                this.print(",");
                this.println();
            }
            final String key = option.getKey();
            boolean unquote = false;
            final char c0;
            if (key.length() > 0 && (c0 = key.charAt(0)) != '\"' && c0 != '`' && c0 != '\'') {
                unquote = true;
            }
            if (unquote) {
                this.print('\'');
            }
            this.print0(key);
            if (unquote) {
                this.print('\'');
            }
            this.print0(" = ");
            option.getValue().accept(this);
            ++i;
        }
        this.decrementIndent();
        this.println();
        this.print(')');
    }
    
    protected void printTblProperties(final HiveCreateTableStatement x) {
        final List<SQLAssignItem> tblProperties = x.getTblProperties();
        if (tblProperties.size() > 0) {
            this.println();
            this.print0(this.ucase ? "TBLPROPERTIES (" : "tblproperties (");
            this.incrementIndent();
            this.println();
            int i = 0;
            for (final SQLAssignItem property : tblProperties) {
                if (i != 0) {
                    this.print(",");
                    this.println();
                }
                final String key = property.getTarget().toString();
                boolean unquote = false;
                final char c0;
                if (key.length() > 0 && (c0 = key.charAt(0)) != '\"' && c0 != '`' && c0 != '\'') {
                    unquote = true;
                }
                if (unquote) {
                    this.print('\'');
                }
                this.print0(key);
                if (unquote) {
                    this.print('\'');
                }
                this.print0(" = ");
                property.getValue().accept(this);
                ++i;
            }
            this.decrementIndent();
            this.println();
            this.print(')');
        }
    }
    
    @Override
    public boolean visit(final HiveInputOutputFormat x) {
        this.print0(this.ucase ? "INPUTFORMAT " : "inputformat ");
        x.getInput().accept(this);
        this.println();
        this.print0(this.ucase ? "OUTPUTFORMAT " : "outputformat ");
        x.getOutput().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLWhoamiStatement x) {
        this.print0(this.ucase ? "WHO AM I" : "who am i");
        return false;
    }
    
    @Override
    public boolean visit(final SQLForStatement x) {
        this.print0(this.ucase ? "FOR " : "for ");
        x.getIndex().accept(this);
        this.print0(this.ucase ? " IN " : " in ");
        final SQLExpr range = x.getRange();
        range.accept(this);
        this.println();
        this.print0(this.ucase ? "LOOP" : "loop");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            final SQLStatement stmt = x.getStatements().get(i);
            stmt.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        this.println();
        this.print0(this.ucase ? "END LOOP" : "end loop");
        return false;
    }
    
    @Override
    public boolean visit(final SQLCopyFromStatement x) {
        this.print0(this.ucase ? "COPY " : "copy ");
        final SQLExprTableSource table = x.getTable();
        table.accept(this);
        final List<SQLName> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print('(');
            this.printAndAccept(columns, ", ");
            this.print(")");
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions.size() > 0) {
            this.print0(this.ucase ? " PARTITIONS (" : " partitions (");
            this.printAndAccept(partitions, ", ");
            this.print(")");
        }
        this.print0(this.ucase ? " FROM " : " from ");
        x.getFrom().accept(this);
        final SQLExpr accessKeyId = x.getAccessKeyId();
        final SQLExpr accessKeySecret = x.getAccessKeySecret();
        if (accessKeyId != null || accessKeySecret != null) {
            this.print0(this.ucase ? " CREDENTIALS" : " credentials");
            if (accessKeyId != null) {
                this.print0(this.ucase ? " access_key_id " : " access_key_id ");
                accessKeyId.accept(this);
            }
            if (accessKeySecret != null) {
                this.print0(this.ucase ? " ACCESS_KEY_SECRET " : " access_key_secret ");
                accessKeySecret.accept(this);
            }
        }
        final List<SQLAssignItem> options = x.getOptions();
        if (options.size() > 0) {
            this.print0(this.ucase ? " WITH (" : " with (");
            this.printAndAccept(options, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowUsersStatement x) {
        this.print0(this.ucase ? "SHOW USERS" : "show users");
        return false;
    }
    
    @Override
    public boolean visit(final SQLSyncMetaStatement x) {
        this.print0(this.ucase ? "SYNC META TABLE" : "SYNC META TABLE");
        final Boolean restrict = x.getRestrict();
        if (restrict != null && restrict) {
            this.print0(this.ucase ? " RESTRICT " : " restrict ");
        }
        final Boolean ignore = x.getIgnore();
        if (ignore != null && ignore) {
            this.print0(this.ucase ? " IGNORE " : " ignore ");
        }
        final SQLName from = x.getFrom();
        if (from != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            from.accept(this);
        }
        final SQLExpr like = x.getLike();
        if (like != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            like.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTableLike x) {
        this.print0(this.ucase ? "LIKE " : "like ");
        x.getTable().accept(this);
        if (x.isIncludeProperties()) {
            this.print0(this.ucase ? " INCLUDING PROPERTIES" : " including properties");
        }
        else if (x.isExcludeProperties()) {
            this.print0(this.ucase ? " EXCLUDING PROPERTIES" : " excluding properties");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLValuesQuery x) {
        this.print0(this.ucase ? "VALUES " : "values ");
        this.printAndAccept(x.getValues(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLBuildTableStatement x) {
        this.print0(this.ucase ? "BUILD TABLE " : "build table ");
        x.getTable().accept(this);
        if (x.getVersion() != null) {
            this.print0(this.ucase ? " VERSION = " : " version = ");
            x.getVersion().accept(this);
        }
        if (x.isWithSplit()) {
            this.print0(this.ucase ? " WITH SPLIT" : " with split");
        }
        if (x.isForce()) {
            this.print0(this.ucase ? " FORCE = true" : " force = true");
        }
        else {
            this.print0(this.ucase ? " FORCE = false" : " force = false");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExportDatabaseStatement x) {
        this.print0(this.ucase ? "EXPORT DATABASE " : "export database ");
        x.getDb().accept(this);
        this.print0(this.ucase ? " REALTIME = " : " realtime = ");
        if (x.isRealtime()) {
            this.print0(this.ucase ? "'Y'" : "'y'");
        }
        else {
            this.print0(this.ucase ? "'N'" : "'n'");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLImportDatabaseStatement x) {
        this.print0(this.ucase ? "IMPORT DATABASE " : "import database ");
        x.getDb().accept(this);
        if (x.getStatus() != null) {
            this.print0(this.ucase ? " STATUS = " : " status = ");
            x.getStatus().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLRenameUserStatement x) {
        this.print0(this.ucase ? "RENAME USER " : "rename user ");
        x.getName().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubmitJobStatement x) {
        this.print0(this.ucase ? "SUBMIT JOB " : "SUBMIT JOB ");
        if (x.isAwait()) {
            this.print0(this.ucase ? "AWAIT " : "await ");
        }
        x.getStatment().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLRestoreStatement x) {
        this.print0(this.ucase ? "RESTORE " : "RESTORE ");
        x.getType().accept(this);
        this.print0(this.ucase ? " FROM " : " from ");
        this.printAndAccept(x.getProperties(), ",");
        return false;
    }
    
    @Override
    public boolean visit(final SQLArchiveTableStatement x) {
        this.print0(this.ucase ? "ARCHIVE TABLE " : "archive table ");
        x.getTable().accept(this);
        for (int i = 0; i < x.getSpIdList().size(); ++i) {
            if (i != 0) {
                this.print0(",");
            }
            this.print0(" ");
            x.getSpIdList().get(i).accept(this);
            this.print0(":");
            x.getpIdList().get(i).accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBackupStatement x) {
        this.print0(this.ucase ? "BACKUP " : "backup ");
        final String type = x.getType().getSimpleName().toUpperCase();
        final String action = x.getAction().getSimpleName().toUpperCase();
        if ("BACKUP_DATA".equalsIgnoreCase(type)) {
            if ("BACKUP".equalsIgnoreCase(action)) {
                this.print0(this.ucase ? "DATA INTO " : "data into ");
            }
            else if ("BACKUP_CANCEL".equalsIgnoreCase(action)) {
                this.print0(this.ucase ? "CANCEL " : "cancel ");
            }
        }
        else if ("BACKUP_LOG".equalsIgnoreCase(type)) {
            this.print0(this.ucase ? "LOG " : "log ");
            if ("BACKUP".equalsIgnoreCase(action)) {
                this.print0(this.ucase ? "INTO " : "into ");
            }
            else if ("LIST_LOG".equalsIgnoreCase(action)) {
                this.print0(this.ucase ? "LIST_LOGS" : "list_logs");
            }
            else if ("STATUS".equalsIgnoreCase(action)) {
                this.print0(this.ucase ? "STATUS" : "status");
            }
        }
        this.printAndAccept(x.getProperties(), ",");
        return false;
    }
    
    public void visitStatementList(final List<SQLStatement> statementList) {
        final boolean printStmtSeperator = DbType.sqlserver != this.dbType && DbType.oracle != this.dbType;
        for (int i = 0, size = statementList.size(); i < size; ++i) {
            final SQLStatement stmt = statementList.get(i);
            if (i > 0) {
                final SQLStatement preStmt = statementList.get(i - 1);
                if (printStmtSeperator && !preStmt.isAfterSemi()) {
                    this.print(";");
                }
                final List<String> comments = preStmt.getAfterCommentsDirect();
                if (comments != null) {
                    for (int j = 0; j < comments.size(); ++j) {
                        final String comment = comments.get(j);
                        if (j != 0) {
                            this.println();
                        }
                        this.printComment(comment);
                    }
                }
                if (printStmtSeperator) {
                    this.println();
                }
                if (!(stmt instanceof SQLSetStatement)) {
                    this.println();
                }
            }
            List<String> comments2 = stmt.getBeforeCommentsDirect();
            if (comments2 != null) {
                for (final String comment2 : comments2) {
                    this.printComment(comment2);
                    this.println();
                }
            }
            stmt.accept(this);
            if (i == size - 1) {
                comments2 = stmt.getAfterCommentsDirect();
                if (comments2 != null) {
                    for (int k = 0; k < comments2.size(); ++k) {
                        final String comment2 = comments2.get(k);
                        if (k != 0) {
                            this.println();
                        }
                        this.printComment(comment2);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean visit(final SQLCreateResourceGroupStatement x) {
        this.print0(this.ucase ? "CREATE RESOURCE GROUP " : "create resource group ");
        x.getName().accept(this);
        for (final Map.Entry<String, SQLExpr> entry : x.getProperties().entrySet()) {
            this.print(' ');
            this.print(entry.getKey());
            this.print(" = ");
            final SQLExpr value = entry.getValue();
            if (value instanceof SQLListExpr) {
                this.printAndAccept(((SQLListExpr)value).getItems(), ",");
            }
            else {
                value.accept(this);
            }
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterResourceGroupStatement x) {
        this.print0(this.ucase ? "ALTER RESOURCE GROUP " : "create resource group ");
        x.getName().accept(this);
        for (final Map.Entry<String, SQLExpr> entry : x.getProperties().entrySet()) {
            this.print(' ');
            this.print(entry.getKey());
            this.print(" = ");
            final SQLExpr value = entry.getValue();
            if (value instanceof SQLListExpr) {
                this.printAndAccept(((SQLListExpr)value).getItems(), ",");
            }
            else {
                value.accept(this);
            }
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlKillStatement x) {
        if (MySqlKillStatement.Type.CONNECTION.equals(x.getType())) {
            this.print0(this.ucase ? "KILL CONNECTION " : "kill connection ");
        }
        else if (MySqlKillStatement.Type.QUERY.equals(x.getType())) {
            this.print0(this.ucase ? "KILL QUERY " : "kill query ");
        }
        else {
            this.print0(this.ucase ? "KILL " : "kill ");
        }
        this.printAndAccept(x.getThreadIds(), ", ");
        return false;
    }
    
    public boolean visit(final OdpsNewExpr x) {
        this.print0(this.ucase ? "NEW " : "new ");
        return super.visit(x);
    }
    
    @Override
    public boolean visit(final SQLPurgeTemporaryOutputStatement x) {
        this.print0(this.ucase ? "PURGE TEMPORARY OUTPUT " : "purge temporary output ");
        this.printExpr(x.getName());
        return false;
    }
    
    @Override
    public boolean visit(final SQLCloneTableStatement x) {
        this.print0(this.ucase ? "CLONE TABLE " : "clone table ");
        x.getFrom().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    public char getNameQuote() {
        return this.quote;
    }
    
    public void setNameQuote(final char quote) {
        this.quote = quote;
    }
    
    static {
        try {
            SQLASTOutputVisitor.defaultPrintStatementAfterSemi = Utils.getBoolean(System.getProperties(), "fastsql.sql.output.printStatementAfterSemi");
        }
        catch (SecurityException ex) {}
        ONE = 1;
        SQLASTOutputVisitor.variantValuesCache_1 = new String[64];
        SQLASTOutputVisitor.variantValuesCache = new String[64];
        for (int len = 0; len < SQLASTOutputVisitor.variantValuesCache_1.length; ++len) {
            final StringBuffer buf = new StringBuffer();
            buf.append('(');
            for (int i = 0; i < len; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        buf.append("\n\t");
                    }
                    buf.append(", ");
                }
                buf.append('?');
            }
            buf.append(')');
            SQLASTOutputVisitor.variantValuesCache_1[len] = buf.toString();
        }
        for (int len = 0; len < SQLASTOutputVisitor.variantValuesCache.length; ++len) {
            final StringBuffer buf = new StringBuffer();
            buf.append('(');
            for (int i = 0; i < len; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        buf.append("\n\t\t");
                    }
                    buf.append(", ");
                }
                buf.append('?');
            }
            buf.append(')');
            SQLASTOutputVisitor.variantValuesCache[len] = buf.toString();
        }
    }
}
