// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterTableAlterCheck;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlJSONTableExpr;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlChecksumTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterLogFileGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableSpaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateServerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateAddLogFileGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateEventStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlEventSchedule;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.ConditionValue;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByList;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableCompression;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableBlockSize;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionCount;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByValue;
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
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import com.alibaba.druid.sql.ast.statement.SQLPartitionRef;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import java.util.Map;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateExternalCatalogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseKillJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseSetOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSampling;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTopologyStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDdlStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowConfigStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlashbackStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowNodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatasourcesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBroadcastsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTraceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterFullTextIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextDictionaryStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextAnalyzerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenFilterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextTokenizerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDropFullTextStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropClusteringKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlAlterFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowCreateFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.FullTextType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowFullTextStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlCreateFullTextCharFilterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCheckTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameSequenceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPhysicalProcesslistStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowStcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowHtcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowDbLockStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSequencesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlowStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowProcessListStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPartitionsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdatePlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDisabledPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlClearPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatusStatement;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowMetadataLock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.DrdsShowGlobalIndex;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
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
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabaseStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHMSMetaStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMigrateTaskStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowJobStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowClusterNameStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlMigrateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftLeaderTransferStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRaftMemberChangeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlManageInstanceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteForAdsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import java.io.IOException;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.ExportParameterVisitorUtils;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLAnnIndex;
import com.alibaba.druid.sql.ast.AutoIncrementType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.statement.SQLColumnReference;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import java.security.AccessControlException;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class MySqlOutputVisitor extends SQLASTOutputVisitor implements MySqlASTVisitor
{
    private static boolean shardingSupportChecked;
    
    public MySqlOutputVisitor(final Appendable appender) {
        super(appender);
        this.dbType = DbType.mysql;
        this.shardingSupport = true;
        this.quote = '`';
    }
    
    public MySqlOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        this.dbType = DbType.mysql;
        this.shardingSupport = true;
        this.quote = '`';
        try {
            this.configFromProperty();
        }
        catch (AccessControlException ex) {}
    }
    
    public void configFromProperty() {
        if (this.parameterized && !MySqlOutputVisitor.shardingSupportChecked) {
            MySqlOutputVisitor.shardingSupportChecked = true;
            final String property = System.getProperties().getProperty("fastsql.parameterized.shardingSupport");
            if ("true".equals(property)) {
                this.setShardingSupport(true);
            }
            else if ("false".equals(property)) {
                this.setShardingSupport(false);
            }
        }
    }
    
    public boolean isShardingSupport() {
        return this.parameterized && this.shardingSupport;
    }
    
    public void setShardingSupport(final boolean shardingSupport) {
        this.shardingSupport = shardingSupport;
    }
    
    @Override
    public boolean visit(final SQLSelectQueryBlock select) {
        if (select instanceof MySqlSelectQueryBlock) {
            return this.visit((MySqlSelectQueryBlock)select);
        }
        return super.visit(select);
    }
    
    @Override
    public boolean visit(final MySqlSelectQueryBlock x) {
        final boolean bracket = x.isParenthesized();
        if (bracket) {
            this.print('(');
        }
        if (!this.isParameterized() && this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        final String cachedSelectList = x.getCachedSelectList();
        if (cachedSelectList != null) {
            if (!this.isEnabled(VisitorFeature.OutputSkipSelectListCacheString)) {
                this.print0(cachedSelectList);
            }
        }
        else {
            this.print0(this.ucase ? "SELECT " : "select ");
            for (int i = 0, size = x.getHintsSize(); i < size; ++i) {
                final SQLCommentHint hint = x.getHints().get(i);
                hint.accept(this);
                this.print(' ');
            }
            switch (x.getDistionOption()) {
                case 1: {
                    this.print0(this.ucase ? "ALL " : "all ");
                    break;
                }
                case 2: {
                    this.print0(this.ucase ? "DISTINCT " : "distinct ");
                    break;
                }
                case 4: {
                    this.print0(this.ucase ? "DISTINCTROW " : "distinctrow ");
                    break;
                }
                case 3: {
                    this.print0(this.ucase ? "UNIQUE " : "unique ");
                    break;
                }
            }
            if (x.isHignPriority()) {
                this.print0(this.ucase ? "HIGH_PRIORITY " : "high_priority ");
            }
            if (x.isStraightJoin()) {
                this.print0(this.ucase ? "STRAIGHT_JOIN " : "straight_join ");
            }
            if (x.isSmallResult()) {
                this.print0(this.ucase ? "SQL_SMALL_RESULT " : "sql_small_result ");
            }
            if (x.isBigResult()) {
                this.print0(this.ucase ? "SQL_BIG_RESULT " : "sql_big_result ");
            }
            if (x.isBufferResult()) {
                this.print0(this.ucase ? "SQL_BUFFER_RESULT " : "sql_buffer_result ");
            }
            if (x.getCache() != null) {
                if (x.getCache()) {
                    this.print0(this.ucase ? "SQL_CACHE " : "sql_cache ");
                }
                else {
                    this.print0(this.ucase ? "SQL_NO_CACHE " : "sql_no_cache ");
                }
            }
            if (x.isCalcFoundRows()) {
                this.print0(this.ucase ? "SQL_CALC_FOUND_ROWS " : "sql_calc_found_rows ");
            }
            this.printSelectList(x.getSelectList());
            final SQLName forcePartition = x.getForcePartition();
            if (forcePartition != null) {
                this.println();
                this.print0(this.ucase ? "FORCE PARTITION " : "force partition ");
                this.printExpr(forcePartition, this.parameterized);
            }
            final SQLExprTableSource into = x.getInto();
            if (into != null) {
                this.println();
                this.print0(this.ucase ? "INTO " : "into ");
                this.printTableSource(into);
            }
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            this.printTableSource(from);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where, this.parameterized);
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
            this.visit(orderBy);
        }
        final SQLLimit limit = x.getLimit();
        if (limit != null) {
            this.println();
            this.visit(limit);
        }
        final SQLName procedureName = x.getProcedureName();
        if (procedureName != null) {
            this.print0(this.ucase ? " PROCEDURE " : " procedure ");
            procedureName.accept(this);
            if (!x.getProcedureArgumentList().isEmpty()) {
                this.print('(');
                this.printAndAccept(x.getProcedureArgumentList(), ", ");
                this.print(')');
            }
        }
        if (x.isForUpdate()) {
            this.println();
            this.print0(this.ucase ? "FOR UPDATE" : "for update");
            if (x.isNoWait()) {
                this.print0(this.ucase ? " NOWAIT" : " nowait");
            }
            else if (x.getWaitTime() != null) {
                this.print0(this.ucase ? " WAIT " : " wait ");
                x.getWaitTime().accept(this);
            }
            if (x.isSkipLocked()) {
                this.print0(this.ucase ? " SKIP LOCKED" : " skip locked");
            }
        }
        if (x.isForShare()) {
            this.println();
            this.print0(this.ucase ? "FOR SHARE" : "for share");
        }
        if (x.isLockInShareMode()) {
            this.println();
            this.print0(this.ucase ? "LOCK IN SHARE MODE" : "lock in share mode");
        }
        if (bracket) {
            this.print(')');
        }
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
        final SQLExpr generatedAlawsAs = x.getGeneratedAlawsAs();
        if (generatedAlawsAs != null) {
            this.print0(this.ucase ? " GENERATED ALWAYS AS (" : " generated always as (");
            this.printExpr(generatedAlawsAs);
            this.print(')');
        }
        if (x.isVirtual()) {
            this.print0(this.ucase ? " VIRTUAL" : " virtual");
        }
        if (x.isVisible()) {
            this.print0(this.ucase ? " VISIBLE" : " visible");
        }
        final SQLExpr charsetExpr = x.getCharsetExpr();
        if (charsetExpr != null) {
            this.print0(this.ucase ? " CHARACTER SET " : " character set ");
            charsetExpr.accept(this);
        }
        final SQLExpr collateExpr = x.getCollateExpr();
        if (collateExpr != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            collateExpr.accept(this);
        }
        for (final SQLColumnConstraint item : x.getConstraints()) {
            if (item instanceof SQLColumnReference) {
                continue;
            }
            this.print(' ');
            item.accept(this);
        }
        final SQLExpr defaultExpr = x.getDefaultExpr();
        if (defaultExpr != null) {
            this.print0(this.ucase ? " DEFAULT " : " default ");
            defaultExpr.accept(this);
        }
        final SQLExpr storage = x.getStorage();
        if (storage != null) {
            this.print0(this.ucase ? " STORAGE " : " storage ");
            storage.accept(this);
        }
        final SQLExpr format = x.getFormat();
        if (format != null) {
            this.printUcase(" COLUMN_FORMAT ");
            format.accept(this);
        }
        final SQLExpr onUpdate = x.getOnUpdate();
        if (onUpdate != null) {
            this.print0(this.ucase ? " ON UPDATE " : " on update ");
            onUpdate.accept(this);
        }
        if (x.getJsonIndexAttrsExpr() != null) {
            this.print0(this.ucase ? " JSONINDEXATTRS '" : " jsonindexattrs '");
            x.getJsonIndexAttrsExpr().accept(this);
            this.print0("' ");
        }
        if (x.isAutoIncrement()) {
            this.print0(this.ucase ? " AUTO_INCREMENT" : " auto_increment");
        }
        if (x.getDelimiterTokenizer() != null) {
            this.print0(this.ucase ? " DELIMITER_TOKENIZER " : " delimiter_tokenizer ");
            x.getDelimiterTokenizer().accept(this);
        }
        if (x.getNlpTokenizer() != null) {
            this.print0(this.ucase ? " NLP_TOKENIZER " : " nlp_tokenizer ");
            x.getNlpTokenizer().accept(this);
        }
        if (x.getValueType() != null) {
            this.print0(this.ucase ? " VALUE_TYPE " : " value_type ");
            x.getValueType().accept(this);
        }
        final AutoIncrementType sequenceType = x.getSequenceType();
        if (sequenceType != null) {
            this.print0(this.ucase ? " BY " : " by ");
            this.print0(this.ucase ? sequenceType.getKeyword() : sequenceType.getKeyword().toLowerCase());
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
        final SQLExpr delimiter = x.getDelimiter();
        if (delimiter != null) {
            this.print0(this.ucase ? " DELIMITER " : " delimiter ");
            delimiter.accept(this);
        }
        if (x.isDisableIndex()) {
            this.print0(this.ucase ? " DISABLEINDEX TRUE" : " disableindex true");
        }
        final SQLAnnIndex annIndex = x.getAnnIndex();
        if (annIndex != null) {
            this.print(' ');
            annIndex.accept(this);
        }
        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }
        if (x.getAsExpr() != null) {
            this.print0(this.ucase ? " AS (" : " as (");
            x.getAsExpr().accept(this);
            this.print(')');
        }
        if (x.isStored()) {
            this.print0(this.ucase ? " STORED" : " stored");
        }
        if (x.getEncode() != null) {
            this.print0(this.ucase ? " ENCODE=" : " encode=");
            x.getEncode().accept(this);
        }
        if (x.getCompression() != null) {
            this.print0(this.ucase ? " COMPRESSION=" : " compression=");
            x.getCompression().accept(this);
        }
        final List<SQLAssignItem> colProperties = x.getColPropertiesDirect();
        if (colProperties != null && colProperties.size() > 0) {
            this.print0(this.ucase ? " COLPROPERTIES (" : " colproperties (");
            this.printAndAccept(colProperties, ", ");
            this.print0(this.ucase ? ")" : ")");
        }
        for (final SQLColumnConstraint item2 : x.getConstraints()) {
            if (item2 instanceof SQLColumnReference) {
                this.print(' ');
                item2.accept(this);
            }
        }
        this.parameterized = parameterized;
        return false;
    }
    
    @Override
    public boolean visit(final SQLDataType x) {
        this.printDataType(x);
        if (x instanceof SQLDataTypeImpl) {
            final SQLDataTypeImpl dataTypeImpl = (SQLDataTypeImpl)x;
            if (dataTypeImpl.isUnsigned()) {
                this.print0(this.ucase ? " UNSIGNED" : " unsigned");
            }
            if (dataTypeImpl.isZerofill()) {
                this.print0(this.ucase ? " ZEROFILL" : " zerofill");
            }
            final SQLExpr indexBy = ((SQLDataTypeImpl)x).getIndexBy();
            if (indexBy != null) {
                this.print0(this.ucase ? " INDEX BY " : " index by ");
                indexBy.accept(this);
            }
        }
        if (x instanceof SQLCharacterDataType) {
            final SQLCharacterDataType charType = (SQLCharacterDataType)x;
            if (charType.getCharSetName() != null) {
                this.print0(this.ucase ? " CHARACTER SET " : " character set ");
                this.print0(charType.getCharSetName());
                if (charType.getCollate() != null) {
                    this.print0(this.ucase ? " COLLATE " : " collate ");
                    this.print0(charType.getCollate());
                }
            }
            final List<SQLCommentHint> hints = ((SQLCharacterDataType)x).hints;
            if (hints != null) {
                this.print(' ');
                for (final SQLCommentHint hint : hints) {
                    hint.accept(this);
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharacterDataType x) {
        this.printDataType(x);
        if (x.isHasBinary()) {
            this.print0(this.ucase ? " BINARY " : " binary ");
        }
        if (x.getCharSetName() != null) {
            this.print0(this.ucase ? " CHARACTER SET " : " character set ");
            this.print0(x.getCharSetName());
            if (x.getCollate() != null) {
                this.print0(this.ucase ? " COLLATE " : " collate ");
                this.print0(x.getCollate());
            }
        }
        else if (x.getCollate() != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(x.getCollate());
        }
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
    public boolean visit(final MySqlTableIndex x) {
        final String indexType = x.getIndexType();
        boolean indexTypePrinted = false;
        if ("FULLTEXT".equalsIgnoreCase(indexType)) {
            this.print0(this.ucase ? "FULLTEXT " : "fulltext ");
            indexTypePrinted = true;
        }
        else if ("SPATIAL".equalsIgnoreCase(indexType)) {
            this.print0(this.ucase ? "SPATIAL " : "spatial ");
            indexTypePrinted = true;
        }
        else if ("CLUSTERED".equalsIgnoreCase(indexType)) {
            this.print0(this.ucase ? "CLUSTERED " : "clustered ");
            indexTypePrinted = true;
        }
        else if ("CLUSTERING".equalsIgnoreCase(indexType)) {
            this.print0(this.ucase ? "CLUSTERING " : "clustering ");
            indexTypePrinted = true;
        }
        if (x.getIndexDefinition().isGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        else if (x.getIndexDefinition().isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "INDEX" : "index");
        if (x.getName() != null) {
            this.print(' ');
            x.getName().accept(this);
        }
        if (indexType != null && !indexTypePrinted && "ANN".equals(indexType)) {
            this.print0(" ");
            this.print0(indexType);
        }
        if (Boolean.TRUE.equals(x.getAttribute("ads.index"))) {
            if (x.getIndexDefinition().isHashMapType()) {
                this.print0(this.ucase ? " HASHMAP" : " hashmap");
            }
            else if (x.getIndexDefinition().isHashType()) {
                this.print0(this.ucase ? " HASH" : " hash");
            }
        }
        final String using = x.getIndexDefinition().hasOptions() ? x.getIndexDefinition().getOptions().getIndexType() : null;
        if (using != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(using);
        }
        this.print('(');
        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        this.print(')');
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
        if (null != covering && covering.size() > 0) {
            this.print0(this.ucase ? " COVERING " : " covering ");
            this.print('(');
            for (int j = 0, size2 = covering.size(); j < size2; ++j) {
                if (j != 0) {
                    this.print0(", ");
                }
                covering.get(j).accept(this);
            }
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
            x.getIndexDefinition().getOptions().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlKey x) {
        if (x.isHasConstraint()) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            if (x.getName() != null) {
                x.getName().accept(this);
                this.print(' ');
            }
        }
        final String indexType = x.getIndexType();
        final boolean fullText = "FULLTEXT".equalsIgnoreCase(indexType);
        final boolean clustering = "CLUSTERING".equalsIgnoreCase(indexType);
        final boolean clustered = "CLUSTERED".equalsIgnoreCase(indexType);
        if (fullText) {
            this.print0(this.ucase ? "FULLTEXT " : "fulltext ");
        }
        else if (clustering) {
            this.print0(this.ucase ? "CLUSTERING " : "clustering ");
        }
        else if (clustered) {
            this.print0(this.ucase ? "CLUSTERED " : "CLUSTERED ");
        }
        this.print0(this.ucase ? "KEY" : "key");
        final SQLName name = x.getName();
        if (name != null) {
            this.print(' ');
            name.accept(this);
        }
        if (indexType != null && !fullText && !clustering && !clustered) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(indexType);
        }
        this.print0(" (");
        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            x.getColumns().get(i).accept(this);
        }
        this.print(')');
        final SQLIndexDefinition indexDefinition = x.getIndexDefinition();
        if (indexDefinition.hasOptions()) {
            indexDefinition.getOptions().accept(this);
        }
        SQLExpr comment = x.getComment();
        if (indexDefinition.hasOptions() && indexDefinition.getOptions().getComment() == comment) {
            comment = null;
        }
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x, final boolean parameterized) {
        if (this.appender == null) {
            return false;
        }
        try {
            if (parameterized) {
                this.appender.append('?');
                this.incrementReplaceCunt();
                if (this.parameters != null) {
                    ExportParameterVisitorUtils.exportParameter(this.parameters, x);
                }
                return false;
            }
            this.appender.append('\'');
            final String text = x.getText();
            boolean hasSpecial = false;
            for (int i = 0; i < text.length(); ++i) {
                final char ch = text.charAt(i);
                if (ch == '\'' || ch == '\\' || ch == '\0') {
                    hasSpecial = true;
                    break;
                }
            }
            if (hasSpecial) {
                boolean regForPresto = false;
                if (this.isEnabled(VisitorFeature.OutputRegForPresto) && x.getParent() instanceof SQLMethodInvokeExpr) {
                    final SQLMethodInvokeExpr regCall = (SQLMethodInvokeExpr)x.getParent();
                    final long nameHash = regCall.methodNameHashCode64();
                    regForPresto = (x == regCall.getArguments().get(1) && (nameHash == FnvHash.Constants.REGEXP_SUBSTR || nameHash == FnvHash.Constants.REGEXP_COUNT || nameHash == FnvHash.Constants.REGEXP_EXTRACT || nameHash == FnvHash.Constants.REGEXP_EXTRACT_ALL || nameHash == FnvHash.Constants.REGEXP_LIKE || nameHash == FnvHash.Constants.REGEXP_REPLACE || nameHash == FnvHash.Constants.REGEXP_SPLIT));
                }
                for (int j = 0; j < text.length(); ++j) {
                    final char ch2 = text.charAt(j);
                    if (ch2 == '\'') {
                        this.appender.append('\'');
                        this.appender.append('\'');
                    }
                    else if (ch2 == '\\') {
                        this.appender.append('\\');
                        if (!regForPresto) {
                            if (j >= text.length() - 1 || text.charAt(j + 1) != '_') {
                                this.appender.append('\\');
                            }
                        }
                    }
                    else if (ch2 == '\0') {
                        this.appender.append('\\');
                        this.appender.append('0');
                    }
                    else {
                        this.appender.append(ch2);
                    }
                }
            }
            else {
                this.appender.append(text);
            }
            this.appender.append('\'');
            return false;
        }
        catch (IOException e) {
            throw new RuntimeException("println error", e);
        }
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        final int index = x.getIndex();
        if (this.inputParameters != null && index < this.inputParameters.size()) {
            return super.visit(x);
        }
        if (x.isGlobal()) {
            this.print0("@@global.");
        }
        else if (x.isSession()) {
            this.print0("@@session.");
        }
        final String varName = x.getName();
        for (int i = 0; i < varName.length(); ++i) {
            final char ch = varName.charAt(i);
            if (ch == '\'') {
                if (varName.startsWith("@@") && i == 2) {
                    this.print(ch);
                }
                else if (varName.startsWith("@") && i == 1) {
                    this.print(ch);
                }
                else if (i != 0 && i != varName.length() - 1) {
                    this.print0("\\'");
                }
                else {
                    this.print(ch);
                }
            }
            else {
                this.print(ch);
            }
        }
        final String collate = (String)x.getAttribute("COLLATE");
        if (collate != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(collate);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlPrepareStatement x) {
        this.print0(this.ucase ? "PREPARE " : "prepare ");
        x.getName().accept(this);
        this.print0(this.ucase ? " FROM " : " from ");
        x.getFrom().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExecuteStatement x) {
        this.print0(this.ucase ? "EXECUTE " : "execute ");
        x.getStatementName().accept(this);
        if (x.getParameters().size() > 0) {
            this.print0(this.ucase ? " USING " : " using ");
            this.printAndAccept(x.getParameters(), ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExecuteForAdsStatement x) {
        this.print0(this.ucase ? "EXECUTE " : "execute ");
        x.getAction().accept(this);
        this.print(" ");
        x.getRole().accept(this);
        this.print(" ");
        x.getTargetId().accept(this);
        this.print(" ");
        if (x.getStatus() != null) {
            x.getStatus().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlManageInstanceGroupStatement x) {
        x.getOperation().accept(this);
        this.print0(this.ucase ? " INSTANCE_GROUP " : " instance_group ");
        this.printAndAccept(x.getGroupNames(), ",");
        if (x.getReplication() != null) {
            this.print0(this.ucase ? " REPLICATION = " : " replication = ");
            x.getReplication().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRaftMemberChangeStatement x) {
        this.print0(this.ucase ? "SYNC RAFT_MEMBER_CHANGE " : "sync raft_member_change ");
        if (x.isNoLeader()) {
            this.print0(this.ucase ? "NOLEADER " : "noleader ");
        }
        this.print0(this.ucase ? "SHARD=" : "shard=");
        x.getShard().accept(this);
        this.print0(this.ucase ? " HOST=" : " host=");
        x.getHost().accept(this);
        this.print0(this.ucase ? " STATUS=" : " status=");
        x.getStatus().accept(this);
        if (x.isForce()) {
            this.print0(this.ucase ? " FORCE" : " force");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRaftLeaderTransferStatement x) {
        this.print0(this.ucase ? "SYNC RAFT_LEADER_TRANSFER SHARD=" : "sync raft_leader_transfer shard=");
        x.getShard().accept(this);
        this.print0(this.ucase ? " FROM=" : " from=");
        x.getFrom().accept(this);
        this.print0(this.ucase ? " TO=" : " to=");
        x.getTo().accept(this);
        this.print0(this.ucase ? " TIMEOUT=" : " timeout=");
        x.getTimeout().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlMigrateStatement x) {
        return false;
    }
    
    @Override
    public void endVisit(final MySqlMigrateStatement x) {
        this.print0(this.ucase ? "MIGRATE DATABASE " : "migrate database ");
        x.getSchema().accept(this);
        this.print0(this.ucase ? " SHARDS=" : "shards= ");
        x.getShardNames().accept(this);
        this.print0(" ");
        if (x.getMigrateType().getNumber().intValue() == 0) {
            this.print0(this.ucase ? "GROUP " : "group ");
        }
        else if (x.getMigrateType().getNumber().intValue() == 1) {
            this.print0(this.ucase ? "HOST " : "host ");
        }
        this.print0(this.ucase ? "FROM " : "from ");
        x.getFromInsId().accept(this);
        if (x.getFromInsIp() != null) {
            this.print(":");
            x.getFromInsIp().accept(this);
            this.print(":");
            x.getFromInsPort().accept(this);
            this.print(":");
            x.getFromInsStatus().accept(this);
        }
        this.print0(this.ucase ? " TO " : " to ");
        x.getToInsId().accept(this);
        if (x.getToInsIp() != null) {
            this.print(":");
            x.getToInsIp().accept(this);
            this.print(":");
            x.getToInsPort().accept(this);
            this.print(":");
            x.getToInsStatus().accept(this);
        }
    }
    
    @Override
    public boolean visit(final MySqlShowClusterNameStatement x) {
        this.print0(this.ucase ? "SHOW CLUSTER NAME" : "show cluster name");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowJobStatusStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isSync()) {
            this.print0(this.ucase ? "SYNC_JOB " : "sync_job ");
        }
        else {
            this.print0(this.ucase ? "JOB " : "job ");
        }
        this.print0(this.ucase ? "STATUS " : "status ");
        if (x.getWhere() != null) {
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMigrateTaskStatusStatement x) {
        this.print0(this.ucase ? "SHOW MIGRATE TASK STATUS" : "show migrate task status");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MysqlDeallocatePrepareStatement x) {
        this.print0(this.ucase ? "DEALLOCATE PREPARE " : "deallocate prepare ");
        x.getStatementName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeleteStatement x) {
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
        this.print0(this.ucase ? "DELETE " : "delete ");
        for (int i = 0, size = x.getHintsSize(); i < size; ++i) {
            final SQLCommentHint hint2 = x.getHints().get(i);
            hint2.accept(this);
            this.print(' ');
        }
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isQuick()) {
            this.print0(this.ucase ? "QUICK " : "quick ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? "IGNORE " : "ignore ");
        }
        if (x.isForceAllPartitions()) {
            this.print0(this.ucase ? "FORCE ALL PARTITIONS " : "force all partitions ");
        }
        else {
            final SQLName partition = x.getForcePartition();
            if (partition != null) {
                this.print0(this.ucase ? "FORCE PARTITION " : "force partition ");
                this.printExpr(partition, this.parameterized);
                this.print(' ');
            }
        }
        final SQLTableSource from = x.getFrom();
        if (from == null) {
            this.print0(this.ucase ? "FROM " : "from ");
            if (x.isFulltextDictionary()) {
                this.print0(this.ucase ? "FULLTEXT DICTIONARY " : "fulltext dictionary ");
            }
            x.getTableSource().accept(this);
        }
        else {
            x.getTableSource().accept(this);
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            from.accept(this);
        }
        final SQLTableSource using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where, this.parameterized);
            --this.indentCount;
        }
        if (x.getOrderBy() != null) {
            this.println();
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.println();
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlInsertStatement x) {
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
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            this.visit(with);
            this.println();
        }
        this.print0(this.ucase ? "INSERT " : "insert ");
        for (int i = 0, size = x.getHintsSize(); i < size; ++i) {
            final SQLCommentHint hint2 = x.getHints().get(i);
            hint2.accept(this);
            this.print(' ');
        }
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isDelayed()) {
            this.print0(this.ucase ? "DELAYED " : "delayed ");
        }
        if (x.isHighPriority()) {
            this.print0(this.ucase ? "HIGH_PRIORITY " : "high_priority ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? "IGNORE " : "ignore ");
        }
        if (x.isRollbackOnFail()) {
            this.print0(this.ucase ? "ROLLBACK_ON_FAIL " : "rollback_on_fail ");
        }
        final boolean outputIntoKeyword = true;
        if (x.isOverwrite()) {
            this.print0(this.ucase ? "OVERWRITE " : "overwrite ");
        }
        if (outputIntoKeyword) {
            this.print0(this.ucase ? "INTO " : "into ");
        }
        if (x.isFulltextDictionary()) {
            this.print0(this.ucase ? "FULLTEXT DICTIONARY " : "fulltext dictionary ");
        }
        final SQLExprTableSource tableSource = x.getTableSource();
        if (tableSource != null) {
            if (tableSource.getClass() == SQLExprTableSource.class) {
                this.visit(tableSource);
            }
            else {
                tableSource.accept(this);
            }
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions != null) {
            final int partitionsSize = partitions.size();
            if (partitionsSize > 0) {
                this.print0(this.ucase ? " PARTITION (" : " partition (");
                for (int j = 0; j < partitionsSize; ++j) {
                    if (j != 0) {
                        this.print0(", ");
                    }
                    final SQLAssignItem assign = partitions.get(j);
                    assign.getTarget().accept(this);
                    if (assign.getValue() != null) {
                        this.print('=');
                        assign.getValue().accept(this);
                    }
                }
                this.print(')');
            }
            if (x.isIfNotExists()) {
                this.print0(this.ucase ? " IF NOT EXISTS " : "if not exists ");
            }
        }
        final String columnsString = x.getColumnsString();
        if (columnsString != null) {
            if (!this.isEnabled(VisitorFeature.OutputSkipInsertColumnsString)) {
                this.print0(columnsString);
            }
        }
        else {
            final List<SQLExpr> columns = x.getColumns();
            if (columns.size() > 0) {
                ++this.indentCount;
                this.print0(" (");
                for (int k = 0, size2 = columns.size(); k < size2; ++k) {
                    if (k != 0) {
                        if (k % 5 == 0) {
                            this.println();
                        }
                        this.print0(", ");
                    }
                    final SQLExpr column = columns.get(k);
                    if (column instanceof SQLIdentifierExpr) {
                        this.printName0(((SQLIdentifierExpr)column).getName());
                    }
                    else {
                        this.printExpr(column, this.parameterized);
                    }
                }
                this.print(')');
                --this.indentCount;
            }
        }
        final List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
        if (!valuesList.isEmpty()) {
            this.println();
            this.printValuesList(valuesList);
        }
        if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        final List<SQLExpr> duplicateKeyUpdate = x.getDuplicateKeyUpdate();
        if (duplicateKeyUpdate.size() != 0) {
            this.println();
            this.print0(this.ucase ? "ON DUPLICATE KEY UPDATE " : "on duplicate key update ");
            for (int l = 0, size3 = duplicateKeyUpdate.size(); l < size3; ++l) {
                if (l != 0) {
                    if (l % 5 == 0) {
                        this.println();
                    }
                    this.print0(", ");
                }
                duplicateKeyUpdate.get(l).accept(this);
            }
        }
        return false;
    }
    
    protected void printValuesList(final List<SQLInsertStatement.ValuesClause> valuesList) {
        if (this.parameterized && valuesList.size() > 1 && !this.parameterizedQuesUnMergeValuesList) {
            this.print0(this.ucase ? "VALUES " : "values ");
            ++this.indentCount;
            boolean allConst = true;
            if (valuesList.size() > 1) {
                for (int index = 0; index < valuesList.size(); ++index) {
                    final List<SQLExpr> values = valuesList.get(index).getValues();
                    for (int i = 0; i < values.size(); ++i) {
                        final SQLExpr value = values.get(i);
                        if (!(value instanceof SQLLiteralExpr)) {
                            if (!(value instanceof SQLVariantRefExpr)) {
                                if (!(value instanceof SQLMethodInvokeExpr) || ((SQLMethodInvokeExpr)value).getArguments().size() != 0) {
                                    allConst = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!allConst) {
                        break;
                    }
                }
            }
            if (!allConst) {
                for (int index = 0; index < valuesList.size(); ++index) {
                    if (index != 0) {
                        this.print(',');
                        this.println();
                    }
                    this.visit(valuesList.get(index), this.parameters);
                }
            }
            else if (valuesList.size() > 1 && this.parameters != null) {
                final SQLInsertStatement.ValuesClause first = valuesList.get(0);
                List<Object> valuesParameters = new ArrayList<Object>(first.getValues().size());
                this.visit(first, valuesParameters);
                this.parameters.add(valuesParameters);
                for (int index2 = 1; index2 < valuesList.size(); ++index2) {
                    final List<SQLExpr> values2 = valuesList.get(index2).getValues();
                    valuesParameters = new ArrayList<Object>(values2.size());
                    for (int j = 0, size = values2.size(); j < size; ++j) {
                        final SQLExpr expr = values2.get(j);
                        if (expr instanceof SQLIntegerExpr || expr instanceof SQLBooleanExpr || expr instanceof SQLNumberExpr || expr instanceof SQLCharExpr || expr instanceof SQLNCharExpr || expr instanceof SQLTimestampExpr || expr instanceof SQLDateExpr || expr instanceof SQLTimeExpr) {
                            this.incrementReplaceCunt();
                            ExportParameterVisitorUtils.exportParameter(valuesParameters, expr);
                        }
                        else if (expr instanceof SQLNullExpr) {
                            this.incrementReplaceCunt();
                            valuesParameters.add(null);
                        }
                    }
                    this.parameters.add(valuesParameters);
                }
                this.incrementReplaceCunt();
            }
            else {
                if (valuesList.size() > 1) {
                    this.incrementReplaceCunt();
                }
                this.visit(valuesList.get(0), this.parameters);
            }
            --this.indentCount;
            return;
        }
        this.print0(this.ucase ? "VALUES " : "values ");
        if (valuesList.size() > 1) {
            ++this.indentCount;
        }
        for (int k = 0, size2 = valuesList.size(); k < size2; ++k) {
            if (k != 0) {
                this.print(',');
                this.println();
            }
            final SQLInsertStatement.ValuesClause item = valuesList.get(k);
            this.visit(item, this.parameters);
        }
        if (valuesList.size() > 1) {
            --this.indentCount;
        }
    }
    
    @Override
    public boolean visit(final MySqlLoadDataInFileStatement x) {
        this.print0(this.ucase ? "LOAD DATA " : "load data ");
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isConcurrent()) {
            this.print0(this.ucase ? "CONCURRENT " : "concurrent ");
        }
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "INFILE " : "infile ");
        x.getFileName().accept(this);
        if (x.isReplicate()) {
            this.print0(this.ucase ? " REPLACE " : " replace ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? " IGNORE " : " ignore ");
        }
        this.print0(this.ucase ? " INTO TABLE " : " into table ");
        x.getTableName().accept(this);
        if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null || x.getColumnsEscaped() != null) {
            this.print0(this.ucase ? " COLUMNS" : " columns");
            if (x.getColumnsTerminatedBy() != null) {
                this.print0(this.ucase ? " TERMINATED BY " : " terminated by ");
                x.getColumnsTerminatedBy().accept(this);
            }
            if (x.getColumnsEnclosedBy() != null) {
                if (x.isColumnsEnclosedOptionally()) {
                    this.print0(this.ucase ? " OPTIONALLY" : " optionally");
                }
                this.print0(this.ucase ? " ENCLOSED BY " : " enclosed by ");
                x.getColumnsEnclosedBy().accept(this);
            }
            if (x.getColumnsEscaped() != null) {
                this.print0(this.ucase ? " ESCAPED BY " : " escaped by ");
                x.getColumnsEscaped().accept(this);
            }
        }
        if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
            this.print0(this.ucase ? " LINES" : " lines");
            if (x.getLinesStartingBy() != null) {
                this.print0(this.ucase ? " STARTING BY " : " starting by ");
                x.getLinesStartingBy().accept(this);
            }
            if (x.getLinesTerminatedBy() != null) {
                this.print0(this.ucase ? " TERMINATED BY " : " terminated by ");
                x.getLinesTerminatedBy().accept(this);
            }
        }
        if (x.getIgnoreLinesNumber() != null) {
            this.print0(this.ucase ? " IGNORE " : " ignore ");
            x.getIgnoreLinesNumber().accept(this);
            this.print0(this.ucase ? " LINES" : " lines");
        }
        if (x.getColumns().size() != 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        if (x.getSetList().size() != 0) {
            this.print0(this.ucase ? " SET " : " set ");
            this.printAndAccept(x.getSetList(), ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLReplaceStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "REPLACE " : "replace ");
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isDelayed()) {
            this.print0(this.ucase ? "DELAYED " : "delayed ");
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), " ");
            this.print0(" ");
        }
        this.print0(this.ucase ? "INTO " : "into ");
        this.printTableSourceExpr(x.getTableName());
        final List<SQLExpr> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print0(" (");
            for (int i = 0, size = columns.size(); i < size; ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                final SQLExpr columnn = columns.get(i);
                this.printExpr(columnn, this.parameterized);
            }
            this.print(')');
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions != null) {
            final int partitionsSize = partitions.size();
            if (partitionsSize > 0) {
                this.print0(this.ucase ? " PARTITION (" : " partition (");
                for (int j = 0; j < partitionsSize; ++j) {
                    if (j != 0) {
                        this.print0(", ");
                    }
                    final SQLAssignItem assign = partitions.get(j);
                    assign.getTarget().accept(this);
                    if (assign.getValue() != null) {
                        this.print('=');
                        assign.getValue().accept(this);
                    }
                }
                this.print(')');
            }
        }
        final List<SQLInsertStatement.ValuesClause> valuesClauseList = x.getValuesList();
        if (valuesClauseList.size() != 0) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            final int size2 = valuesClauseList.size();
            if (size2 == 0) {
                this.print0("()");
            }
            else {
                for (int k = 0; k < size2; ++k) {
                    if (k != 0) {
                        this.print0(", ");
                    }
                    this.visit(valuesClauseList.get(k));
                }
            }
        }
        final SQLQueryExpr query = x.getQuery();
        if (query != null) {
            this.visit(query);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLStartTransactionStatement x) {
        this.print0(this.ucase ? "START TRANSACTION" : "start transaction");
        if (x.isConsistentSnapshot()) {
            this.print0(this.ucase ? " WITH CONSISTENT SNAPSHOT" : " with consistent snapshot");
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        if (x.isBegin()) {
            this.print0(this.ucase ? " BEGIN" : " begin");
        }
        if (x.isWork()) {
            this.print0(this.ucase ? " WORK" : " work");
        }
        final SQLStartTransactionStatement.IsolationLevel isolationLevel = x.getIsolationLevel();
        if (isolationLevel != null) {
            this.print0(" ISOLATION LEVEL ");
            this.print(isolationLevel.getText());
        }
        if (x.isReadOnly()) {
            this.print0(this.ucase ? " READ ONLY" : " read only");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLRollbackStatement x) {
        this.print0(this.ucase ? "ROLLBACK" : "rollback");
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
        if (x.getTo() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getTo().accept(this);
        }
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
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
        }
        final SQLExpr like = x.getLike();
        if (like != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            this.printExpr(like);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowHMSMetaStatement x) {
        this.print0(this.ucase ? "SHOW HMSMETA " : "show hmsmeta ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDatabaseStatusStatement x) {
        if (x.isFull()) {
            this.print0(this.ucase ? "SHOW FULL DATABASE STATUS" : "show full database status");
        }
        else {
            this.print0(this.ucase ? "SHOW DATABASE STATUS" : "show database status");
        }
        if (x.getName() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getName().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowWarningsStatement x) {
        if (x.isCount()) {
            this.print0(this.ucase ? "SHOW COUNT(*) WARNINGS" : "show count(*) warnings");
        }
        else {
            this.print0(this.ucase ? "SHOW WARNINGS" : "show warnings");
            if (x.getLimit() != null) {
                this.print(' ');
                x.getLimit().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowStatusStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        if (x.isSession()) {
            this.print0(this.ucase ? "SESSION " : "session ");
        }
        this.print0(this.ucase ? "STATUS" : "status");
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
    public boolean visit(final MySqlLoadXmlStatement x) {
        this.print0(this.ucase ? "LOAD XML " : "load xml ");
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isConcurrent()) {
            this.print0(this.ucase ? "CONCURRENT " : "concurrent ");
        }
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "INFILE " : "infile ");
        x.getFileName().accept(this);
        if (x.isReplicate()) {
            this.print0(this.ucase ? " REPLACE " : " replace ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? " IGNORE " : " ignore ");
        }
        this.print0(this.ucase ? " INTO TABLE " : " into table ");
        x.getTableName().accept(this);
        if (x.getCharset() != null) {
            this.print0(this.ucase ? " CHARSET " : " charset ");
            this.print0(x.getCharset());
        }
        if (x.getRowsIdentifiedBy() != null) {
            this.print0(this.ucase ? " ROWS IDENTIFIED BY " : " rows identified by ");
            x.getRowsIdentifiedBy().accept(this);
        }
        if (x.getSetList().size() != 0) {
            this.print0(this.ucase ? " SET " : " set ");
            this.printAndAccept(x.getSetList(), ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final CobarShowStatus x) {
        this.print0(this.ucase ? "SHOW COBAR_STATUS" : "show cobar_status");
        return false;
    }
    
    @Override
    public boolean visit(final DrdsShowDDLJobs x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL " : "full ");
        }
        this.print0(this.ucase ? "DDL" : "ddl");
        boolean first = true;
        for (final Long id : x.getJobIds()) {
            if (first) {
                first = false;
                this.print0(" ");
            }
            else {
                this.print0(", ");
            }
            this.print(id);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsCancelDDLJob x) {
        this.print0(this.ucase ? "CANCEL DDL" : "cancel ddl");
        boolean first = true;
        for (final Long id : x.getJobIds()) {
            if (first) {
                first = false;
                this.print0(" ");
            }
            else {
                this.print0(", ");
            }
            this.print(id);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsRecoverDDLJob x) {
        this.print0(this.ucase ? "RECOVER DDL" : "recover ddl");
        if (x.isAllJobs()) {
            this.print0(this.ucase ? " ALL" : " all");
        }
        else {
            boolean first = true;
            for (final Long id : x.getJobIds()) {
                if (first) {
                    first = false;
                    this.print0(" ");
                }
                else {
                    this.print0(", ");
                }
                this.print(id);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsRollbackDDLJob x) {
        this.print0(this.ucase ? "ROLLBACK DDL" : "rollback ddl");
        boolean first = true;
        for (final Long id : x.getJobIds()) {
            if (first) {
                first = false;
                this.print0(" ");
            }
            else {
                this.print0(", ");
            }
            this.print(id);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsRemoveDDLJob x) {
        this.print0(this.ucase ? "REMOVE DDL" : "remove ddl");
        if (x.isAllCompleted()) {
            this.print0(this.ucase ? " ALL COMPLETED" : " all completed");
        }
        else if (x.isAllPending()) {
            this.print(this.ucase ? " ALL PENDING" : " all pending");
        }
        else {
            boolean first = true;
            for (final Long id : x.getJobIds()) {
                if (first) {
                    first = false;
                    this.print0(" ");
                }
                else {
                    this.print0(", ");
                }
                this.print(id);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsInspectDDLJobCache x) {
        this.print0(this.ucase ? "INSPECT DDL CACHE" : "inspect ddl cache");
        return false;
    }
    
    @Override
    public boolean visit(final DrdsClearDDLJobCache x) {
        this.print0(this.ucase ? "CLEAR DDL CACHE" : "clear ddl cache");
        if (x.isAllJobs()) {
            this.print0(this.ucase ? " ALL" : " all");
        }
        else {
            boolean first = true;
            for (final Long id : x.getJobIds()) {
                if (first) {
                    first = false;
                    this.print0(" ");
                }
                else {
                    this.print0(", ");
                }
                this.print(id);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsChangeDDLJob x) {
        this.print0(this.ucase ? "CHANGE DDL " : "change ddl ");
        this.print(x.getJobId());
        if (x.isSkip()) {
            this.print0(this.ucase ? " SKIP" : " skip");
        }
        else if (x.isAdd()) {
            this.print0(this.ucase ? " ADD" : " add");
        }
        boolean first = true;
        for (final String name : x.getGroupAndTableNameList()) {
            if (first) {
                first = false;
                this.print0(" ");
            }
            else {
                this.print0(", ");
            }
            this.print(name);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsBaselineStatement x) {
        this.print0(this.ucase ? "BASELINE " : "baseline ");
        this.print0(this.ucase ? x.getOperation().toUpperCase() : x.getOperation().toLowerCase());
        boolean isFirst = true;
        for (final Long id : x.getBaselineIds()) {
            if (isFirst) {
                this.print0(" ");
                isFirst = false;
            }
            else {
                this.print0(", ");
            }
            this.print(id);
        }
        final SQLSelect select = x.getSelect();
        if (x.getSelect() != null) {
            this.print(this.ucase ? " SQL" : " sql");
            this.println();
            final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
            if (headHints != null) {
                for (final SQLCommentHint hint : headHints) {
                    this.visit(hint);
                    this.println();
                }
            }
            this.visit(select);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsShowGlobalIndex x) {
        this.print0(this.ucase ? "SHOW GLOBAL INDEX" : "show global index");
        if (x.getTableName() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            this.printExpr(x.getTableName(), this.parameterized);
        }
        return false;
    }
    
    @Override
    public boolean visit(final DrdsShowMetadataLock x) {
        this.print0(this.ucase ? "SHOW METADATA LOCK" : "show metadata lock");
        if (x.getSchemaName() != null) {
            this.print0(" ");
            this.printExpr(x.getSchemaName(), this.parameterized);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlBinlogStatement x) {
        this.print0(this.ucase ? "BINLOG " : "binlog ");
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlResetStatement x) {
        this.print0(this.ucase ? "RESET " : "reset ");
        for (int i = 0; i < x.getOptions().size(); ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            this.print0(x.getOptions().get(i));
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateUserStatement x) {
        this.print0(this.ucase ? "CREATE USER " : "create user ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        this.printAndAccept(x.getUsers(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateUserStatement.UserSpecification x) {
        x.getUser().accept(this);
        if (x.getAuthPlugin() != null) {
            this.print0(this.ucase ? " IDENTIFIED WITH " : " identified with ");
            x.getAuthPlugin().accept(this);
            if (x.getPassword() != null) {
                if (x.isPluginAs()) {
                    this.print0(this.ucase ? " AS " : " as ");
                }
                else {
                    this.print0(this.ucase ? " BY " : " by ");
                }
                x.getPassword().accept(this);
            }
        }
        else if (x.getPassword() != null) {
            this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
            if (x.isPasswordHash()) {
                this.print0(this.ucase ? "PASSWORD " : "password ");
            }
            x.getPassword().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlPartitionByKey x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "LINEAR KEY (" : "linear key (");
        }
        else {
            this.print0(this.ucase ? "KEY (" : "key (");
        }
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        this.printPartitionsCountAndSubPartitions(x);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPlanCacheStatusStatement x) {
        this.print0(this.ucase ? "SHOW PLANCACHE STATUS" : "show plancache status");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlClearPlanCacheStatement x) {
        this.print0(this.ucase ? "CLEAR PLANCACHE" : "clear plancache");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDisabledPlanCacheStatement x) {
        this.print0(this.ucase ? "DISABLED PLANCACHE" : "disabled plancache");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExplainPlanCacheStatement x) {
        this.print0(this.ucase ? "EXPLAIN PLANCACHE" : "explain plancache");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdatePlanCacheStatement x) {
        this.print0(this.ucase ? "UPDATE PLANCACHE " : "update plancache ");
        x.getFormSelect().accept(this);
        this.println();
        this.print0(this.ucase ? " TO " : " to ");
        this.println();
        x.getToSelect().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlOutFileExpr x) {
        this.print0(this.ucase ? "OUTFILE " : "outfile ");
        x.getFile().accept(this);
        if (x.getCharset() != null) {
            this.print0(this.ucase ? " CHARACTER SET " : " character set ");
            this.print0(x.getCharset());
        }
        if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null || x.getColumnsEscaped() != null) {
            this.print0(this.ucase ? " COLUMNS" : " columns");
            if (x.getColumnsTerminatedBy() != null) {
                this.print0(this.ucase ? " TERMINATED BY " : " terminated by ");
                x.getColumnsTerminatedBy().accept(this);
            }
            if (x.getColumnsEnclosedBy() != null) {
                if (x.isColumnsEnclosedOptionally()) {
                    this.print0(this.ucase ? " OPTIONALLY" : " optionally");
                }
                this.print0(this.ucase ? " ENCLOSED BY " : " enclosed by ");
                x.getColumnsEnclosedBy().accept(this);
            }
            if (x.getColumnsEscaped() != null) {
                this.print0(this.ucase ? " ESCAPED BY " : " escaped by ");
                x.getColumnsEscaped().accept(this);
            }
        }
        if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
            this.print0(this.ucase ? " LINES" : " lines");
            if (x.getLinesStartingBy() != null) {
                this.print0(this.ucase ? " STARTING BY " : " starting by ");
                x.getLinesStartingBy().accept(this);
            }
            if (x.getLinesTerminatedBy() != null) {
                this.print0(this.ucase ? " TERMINATED BY " : " terminated by ");
                x.getLinesTerminatedBy().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExplainStatement x) {
        List<SQLCommentHint> hints = x.getHeadHintsDirect();
        if (null != hints) {
            for (int i = 0; i < hints.size(); ++i) {
                if (i != 0) {
                    this.print(' ');
                }
                hints.get(i).accept(this);
            }
            this.println();
        }
        final String name = x.isDescribe() ? "desc" : "explain";
        this.print0(this.ucase ? name.toUpperCase() : name);
        hints = x.getHints();
        if (hints != null) {
            this.print(' ');
            for (int j = 0; j < hints.size(); ++j) {
                if (j != 0) {
                    this.print(' ');
                }
                hints.get(j).accept(this);
            }
        }
        final String type = x.getType();
        if (x.getTableName() != null) {
            this.print(' ');
            x.getTableName().accept(this);
            if (x.getColumnName() != null) {
                this.print(' ');
                x.getColumnName().accept(this);
            }
            else if (x.getWild() != null) {
                this.print(' ');
                x.getWild().accept(this);
            }
        }
        else {
            if (x.isExtended()) {
                this.print0(this.ucase ? " EXTENDED" : " extended");
            }
            if (x.isOptimizer()) {
                this.print0(this.ucase ? " OPTIMIZER" : " optimizer");
            }
            if (x.isDependency()) {
                this.print0(this.ucase ? " DEPENDENCY" : " dependency");
            }
            if (x.isAuthorization()) {
                this.print0(this.ucase ? " AUTHORIZATION" : " authorization");
            }
            final String format = x.getFormat();
            if (type != null || format != null) {
                final boolean parenthesis = x.isParenthesis();
                if (parenthesis) {
                    this.print0(" (");
                }
                else {
                    this.print(' ');
                }
                if (type != null) {
                    if (parenthesis) {
                        this.print0(this.ucase ? "TYPE " : "type ");
                    }
                    this.print0(type);
                }
                if (format != null) {
                    if (type != null) {
                        if (parenthesis) {
                            this.print0(", ");
                        }
                        else {
                            this.print(' ');
                        }
                    }
                    this.print0(this.ucase ? "FORMAT " : "format ");
                    if (!parenthesis) {
                        this.print0("= ");
                    }
                    this.print0(format);
                }
                if (parenthesis) {
                    this.print(')');
                }
            }
            if (x.getConnectionId() != null) {
                this.print0(this.ucase ? " FOR CONNECTION " : " for connection ");
                x.getConnectionId().accept(this);
            }
            else {
                this.print(' ');
                x.getStatement().accept(this);
            }
            if (x.isDistributeInfo()) {
                this.print0(this.ucase ? " DISTRIBUTE INFO" : " distribute info");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdateStatement x) {
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
        final List<SQLExpr> returning = x.getReturning();
        if (returning != null && returning.size() > 0) {
            this.print0(this.ucase ? "SELECT " : "select ");
            this.printAndAccept(returning, ", ");
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
        }
        this.print0(this.ucase ? "UPDATE " : "update ");
        if (x.isLowPriority()) {
            this.print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? "IGNORE " : "ignore ");
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), " ");
            this.print0(" ");
        }
        if (x.isCommitOnSuccess()) {
            this.print0(this.ucase ? "COMMIT_ON_SUCCESS " : "commit_on_success ");
        }
        if (x.isRollBackOnFail()) {
            this.print0(this.ucase ? "ROLLBACK_ON_FAIL " : "rollback_on_fail ");
        }
        if (x.isQueryOnPk()) {
            this.print0(this.ucase ? "QUEUE_ON_PK " : "queue_on_pk ");
        }
        final SQLExpr targetAffectRow = x.getTargetAffectRow();
        if (targetAffectRow != null) {
            this.print0(this.ucase ? "TARGET_AFFECT_ROW " : "target_affect_row ");
            this.printExpr(targetAffectRow, this.parameterized);
            this.print(' ');
        }
        if (x.isForceAllPartitions()) {
            this.print0(this.ucase ? "FORCE ALL PARTITIONS " : "force all partitions ");
        }
        else {
            final SQLName partition = x.getForcePartition();
            if (partition != null) {
                this.print0(this.ucase ? "FORCE PARTITION " : "force partition ");
                this.printExpr(partition, this.parameterized);
                this.print(' ');
            }
        }
        this.printTableSource(x.getTableSource());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        final List<SQLUpdateSetItem> items = x.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLUpdateSetItem item = items.get(i);
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
    public boolean visit(final MySqlSetTransactionStatement x) {
        this.print0(this.ucase ? "SET " : "set ");
        if (x.getGlobal() != null && x.getGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        else if (x.getSession() != null && x.getSession()) {
            this.print0(this.ucase ? "SESSION " : "session ");
        }
        this.print0(this.ucase ? "TRANSACTION " : "transaction ");
        if (x.getIsolationLevel() != null) {
            this.print0(this.ucase ? "ISOLATION LEVEL " : "isolation level ");
            this.print0(x.getIsolationLevel());
        }
        final String accessModel = x.getAccessModel();
        if (accessModel != null) {
            this.print0(this.ucase ? "READ " : "read ");
            this.print0(accessModel);
        }
        final SQLExpr policy = x.getPolicy();
        if (policy != null) {
            this.print0(this.ucase ? "POLICY " : "policy ");
            policy.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowAuthorsStatement x) {
        this.print0(this.ucase ? "SHOW AUTHORS" : "show authors");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBinaryLogsStatement x) {
        this.print0(this.ucase ? "SHOW BINARY LOGS" : "show binary logs");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMasterLogsStatement x) {
        this.print0(this.ucase ? "SHOW MASTER LOGS" : "show master logs");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCollationStatement x) {
        this.print0(this.ucase ? "SHOW COLLATION" : "show collation");
        if (x.getPattern() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getPattern().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBinLogEventsStatement x) {
        this.print0(this.ucase ? "SHOW BINLOG EVENTS" : "show binlog events");
        if (x.getIn() != null) {
            this.print0(this.ucase ? " IN " : " in ");
            x.getIn().accept(this);
        }
        if (x.getFrom() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getFrom().accept(this);
        }
        if (x.getLimit() != null) {
            this.print(' ');
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCharacterSetStatement x) {
        this.print0(this.ucase ? "SHOW CHARACTER SET" : "show character set");
        if (x.getPattern() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getPattern().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowContributorsStatement x) {
        this.print0(this.ucase ? "SHOW CONTRIBUTORS" : "show contributors");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateDatabaseStatement x) {
        this.print0(this.ucase ? "SHOW CREATE DATABASE " : "show create database ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getDatabase().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateEventStatement x) {
        this.print0(this.ucase ? "SHOW CREATE EVENT " : "show create event ");
        x.getEventName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateFunctionStatement x) {
        this.print0(this.ucase ? "SHOW CREATE FUNCTION " : "show create function ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateProcedureStatement x) {
        this.print0(this.ucase ? "SHOW CREATE PROCEDURE " : "show create procedure ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateTriggerStatement x) {
        this.print0(this.ucase ? "SHOW CREATE TRIGGER " : "show create trigger ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowEngineStatement x) {
        this.print0(this.ucase ? "SHOW ENGINE " : "show engine ");
        x.getName().accept(this);
        this.print(' ');
        this.print0(x.getOption().name());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowEventsStatement x) {
        this.print0(this.ucase ? "SHOW EVENTS" : "show events");
        if (x.getSchema() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getSchema().accept(this);
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
    public boolean visit(final MySqlShowFunctionCodeStatement x) {
        this.print0(this.ucase ? "SHOW FUNCTION CODE " : "show function code ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowFunctionStatusStatement x) {
        this.print0(this.ucase ? "SHOW FUNCTION STATUS" : "show function status");
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
    public boolean visit(final MySqlShowEnginesStatement x) {
        if (x.isStorage()) {
            this.print0(this.ucase ? "SHOW STORAGE ENGINES" : "show storage engines");
        }
        else {
            this.print0(this.ucase ? "SHOW ENGINES" : "show engines");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowErrorsStatement x) {
        if (x.isCount()) {
            this.print0(this.ucase ? "SHOW COUNT(*) ERRORS" : "show count(*) errors");
        }
        else {
            this.print0(this.ucase ? "SHOW ERRORS" : "show errors");
            if (x.getLimit() != null) {
                this.print(' ');
                x.getLimit().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowGrantsStatement x) {
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
    public boolean visit(final MySqlUserName x) {
        final String userName = x.getUserName();
        if (userName.length() > 0 && userName.charAt(0) == '\'') {
            this.print0(userName);
        }
        else {
            this.print('\'');
            this.print0(userName);
            this.print('\'');
        }
        final String host = x.getHost();
        if (host != null) {
            this.print('@');
            if (host.length() > 0 && host.charAt(0) == '\'') {
                this.print0(host);
            }
            else {
                this.print('\'');
                this.print0(host);
                this.print('\'');
            }
        }
        final String identifiedBy = x.getIdentifiedBy();
        if (identifiedBy != null) {
            this.print0(this.ucase ? " IDENTIFIED BY '" : " identified by '");
            this.print0(identifiedBy);
            this.print('\'');
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMasterStatusStatement x) {
        this.print0(this.ucase ? "SHOW MASTER STATUS" : "show master status");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowOpenTablesStatement x) {
        this.print0(this.ucase ? "SHOW OPEN TABLES" : "show open tables");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
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
    public boolean visit(final MySqlShowPluginsStatement x) {
        this.print0(this.ucase ? "SHOW PLUGINS" : "show plugins");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPartitionsStatement x) {
        this.print0(this.ucase ? "SHOW DBPARTITIONS " : "show dbpartitions ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowPartitionsStmt x) {
        this.print0(this.ucase ? "SHOW PARTITIONS FROM " : "show partitions from ");
        x.getTableSource().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPrivilegesStatement x) {
        this.print0(this.ucase ? "SHOW PRIVILEGES" : "show privileges");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProcedureCodeStatement x) {
        this.print0(this.ucase ? "SHOW PROCEDURE CODE " : "show procedure code ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProcedureStatusStatement x) {
        this.print0(this.ucase ? "SHOW PROCEDURE STATUS" : "show procedure status");
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
    public boolean visit(final MySqlShowProcessListStatement x) {
        return this.visit(x);
    }
    
    @Override
    public boolean visit(final MySqlShowProfileStatement x) {
        this.print0(this.ucase ? "SHOW PROFILE" : "show profile");
        for (int i = 0; i < x.getTypes().size(); ++i) {
            if (i == 0) {
                this.print(' ');
            }
            else {
                this.print0(", ");
            }
            this.print0(x.getTypes().get(i).name);
        }
        if (x.getForQuery() != null) {
            this.print0(this.ucase ? " FOR QUERY " : " for query ");
            x.getForQuery().accept(this);
        }
        if (x.getLimit() != null) {
            this.print(' ');
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProfilesStatement x) {
        this.print0(this.ucase ? "SHOW PROFILES" : "show profiles");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowRelayLogEventsStatement x) {
        this.print0("SHOW RELAYLOG EVENTS");
        if (x.getLogName() != null) {
            this.print0(this.ucase ? " IN " : " in ");
            x.getLogName().accept(this);
        }
        if (x.getFrom() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getFrom().accept(this);
        }
        if (x.getLimit() != null) {
            this.print(' ');
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlaveHostsStatement x) {
        this.print0(this.ucase ? "SHOW SLAVE HOSTS" : "show slave hosts");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlowStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL " : "full ");
        }
        if (x.isPhysical()) {
            this.print0(this.ucase ? "PHYSICAL_SLOW" : "PHYSICAL_SLOW");
        }
        else {
            this.print0(this.ucase ? "SLOW" : "slow");
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSequencesStatement x) {
        this.print0(this.ucase ? "SHOW SEQUENCES" : "show sequences");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlaveStatusStatement x) {
        this.print0(this.ucase ? "SHOW SLAVE STATUS" : "show slave status");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTableStatusStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "SHOW TABLE STATUS" : "show table status");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
            if (x.getTableGroup() != null) {
                this.print0(".");
                x.getTableGroup().accept(this);
            }
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
    public boolean visit(final MysqlShowDbLockStatement x) {
        this.print0(this.ucase ? "SHOW DBLOCK" : "show dblock");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowHtcStatement x) {
        this.print0(this.ucase ? "SHOW HTC" : "show htc");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowStcStatement x) {
        if (x.isHis()) {
            this.print0(this.ucase ? "SHOW STC HIS" : "show stc his");
        }
        else {
            this.print0(this.ucase ? "SHOW STC" : "show stc");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTriggersStatement x) {
        this.print0(this.ucase ? "SHOW TRIGGERS" : "show triggers");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
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
    public boolean visit(final MySqlShowRuleStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL RULE" : "full rule");
        }
        else {
            this.print0(this.ucase ? "RULE" : "rule");
        }
        if (x.isVersion()) {
            this.print0(this.ucase ? " VERSION" : " version");
        }
        if (x.getName() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getName().accept(this);
        }
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowRuleStatusStatement x) {
        this.print0(this.ucase ? "SHOW RULE" : "show rule");
        if (x.isFull()) {
            this.print0(this.ucase ? " FULL" : " full");
        }
        else if (x.isVersion()) {
            this.print0(this.ucase ? " VERSION" : " version");
        }
        this.print0(this.ucase ? " STATUS" : " status");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPhysicalProcesslistStatement x) {
        this.print0(this.ucase ? "SHOW" : "show");
        if (x.isFull()) {
            this.print0(this.ucase ? " FULL" : " full");
        }
        this.print0(this.ucase ? " PHYSICAL_PROCESSLIST" : " physical_processlist");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameSequenceStatement x) {
        this.print0(this.ucase ? "RENAME SEQUENCE " : "rename sequence ");
        x.getName().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCheckTableStatement x) {
        this.print0(this.ucase ? "CHECK TABLE " : "check table ");
        this.printAndAccept(x.getTables(), "\uff0c");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextCharFilterStatement x) {
        this.print0(this.ucase ? "CREATE FULLTEXT CHARFILTER " : "create fulltext charfilter ");
        x.getName().accept(this);
        this.println("(");
        this.print0("\"type\" = " + x.getTypeName());
        this.println(",");
        this.printAndAccept(x.getOptions(), ",");
        this.println();
        this.print0(")");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowFullTextStatement x) {
        this.print0(this.ucase ? "SHOW FULLTEXT " : "show fulltext ");
        if (x.getType() == FullTextType.DICTIONARY) {
            this.print0(this.ucase ? "DICTIONARIES" : "dictionaries");
        }
        else {
            this.print0(this.ucase ? (x.getType().toString().toUpperCase() + "S") : (x.getType().toString().toLowerCase() + "s"));
        }
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowCreateFullTextStatement x) {
        this.print0(this.ucase ? "SHOW CREATE FULLTEXT " : "show create fulltext ");
        this.print0(this.ucase ? x.getType().toString().toUpperCase() : x.getType().toString().toLowerCase());
        this.print0(" ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MysqlAlterFullTextStatement x) {
        this.print0(this.ucase ? "ALTER FULLTEXT " : "alter fulltext ");
        this.print0(this.ucase ? x.getType().toString().toUpperCase() : x.getType().toString().toLowerCase());
        this.print0(" ");
        x.getName().accept(this);
        this.print0(this.ucase ? " SET " : " set ");
        x.getItem().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropClusteringKey x) {
        this.print0(this.ucase ? "DROP CLUSTERED KEY " : "drop clustered key ");
        x.getKeyName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MysqlDropFullTextStatement x) {
        this.print0(this.ucase ? "DROP FULLTEXT " : "drop fulltext ");
        this.print0(this.ucase ? x.getType().toString().toUpperCase() : x.getType().toString().toLowerCase());
        this.print0(" ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextTokenizerStatement x) {
        this.print0(this.ucase ? "CREATE FULLTEXT TOKENIZER " : "create fulltext tokenizer ");
        x.getName().accept(this);
        this.println("(");
        this.print0("\"type\" = " + x.getTypeName());
        if (x.getUserDefinedDict() != null) {
            this.println(",");
            this.print("\"user_defined_dict\" = " + x.getUserDefinedDict());
        }
        if (!x.getOptions().isEmpty()) {
            this.println(",");
            this.printAndAccept(x.getOptions(), ",");
        }
        this.println();
        this.print0(")");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextTokenFilterStatement x) {
        this.print0(this.ucase ? "CREATE FULLTEXT TOKENFILTER " : "create fulltext tokenfilter ");
        x.getName().accept(this);
        this.println("(");
        this.println("\"type\" = " + x.getTypeName() + ",");
        this.printAndAccept(x.getOptions(), ",");
        this.println();
        this.print0(")");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextAnalyzerStatement x) {
        this.print0(this.ucase ? "CREATE FULLTEXT ANALYZER " : "create fulltext analyzer ");
        x.getName().accept(this);
        this.println("(");
        this.print0(this.ucase ? "\"TOKENIZER\" = " : "\"tokenizer\" = ");
        this.print0(x.getTokenizer());
        this.println(",");
        if (!x.getCharfilters().isEmpty()) {
            this.print0(this.ucase ? "\"CHARFILTER\" = [" : "\"charfilter\" = [");
            for (int i = 0; i < x.getCharfilters().size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.print0("\"" + x.getCharfilters().get(i) + "\"");
            }
            this.println("],");
        }
        if (!x.getTokenizers().isEmpty()) {
            this.print0(this.ucase ? "\"TOKENFILTER\" = [" : "\"tokenfilter\" = [");
            for (int i = 0; i < x.getTokenizers().size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                this.print0("\"" + x.getTokenizers().get(i) + "\"");
            }
            this.print0("]");
        }
        this.println();
        this.print0(")");
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextDictionaryStatement x) {
        this.print0(this.ucase ? "CREATE FULLTEXT DICTIONARY " : "create fulltext dictionary ");
        x.getName().accept(this);
        this.println("(");
        x.getColumn().accept(this);
        this.println();
        this.print0(") ");
        if (x.getComment() != null) {
            this.print0(this.ucase ? "COMMENT " : "comment ");
            this.print0(x.getComment());
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableAlterFullTextIndex x) {
        this.print0(this.ucase ? " ALTER INDEX " : " alter index ");
        x.getIndexName().accept(this);
        this.print0(this.ucase ? " FULLTEXT " : " fulltext ");
        if (x.getAnalyzerType() != null) {
            final String analyzerType = x.getAnalyzerType().toString();
            this.print0(this.ucase ? analyzerType.toUpperCase() : analyzerType.toLowerCase());
        }
        this.print0(this.ucase ? " ANALYZER = " : " analyzer = ");
        x.getAnalyzerName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExtPartition x) {
        this.print0(this.ucase ? "EXTPARTITION (" : "extpartition (");
        this.incrementIndent();
        this.println();
        for (int i = 0; i < x.getItems().size(); ++i) {
            if (i != 0) {
                this.println(", ");
            }
            final MySqlExtPartition.Item item = x.getItems().get(i);
            item.accept(this);
        }
        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExtPartition.Item x) {
        final SQLName dbPartition = x.getDbPartition();
        if (dbPartition != null) {
            this.print0(this.ucase ? "DBPARTITION " : "dbpartition ");
            dbPartition.accept(this);
            final SQLExpr dbPartitionBy = x.getDbPartitionBy();
            if (dbPartitionBy != null) {
                this.print0(this.ucase ? " BY " : " by ");
            }
            dbPartitionBy.accept(this);
        }
        final SQLName tbPartition = x.getTbPartition();
        if (tbPartition != null) {
            if (dbPartition != null) {
                this.print(' ');
            }
            this.print0(this.ucase ? "TBPARTITION " : "tbpartition ");
            tbPartition.accept(this);
            final SQLExpr tbPartitionBy = x.getTbPartitionBy();
            if (tbPartitionBy != null) {
                this.print0(this.ucase ? " BY " : " by ");
            }
            tbPartitionBy.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTraceStatement x) {
        this.print0(this.ucase ? "SHOW TRACE" : "show trace");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBroadcastsStatement x) {
        this.print0(this.ucase ? "SHOW BROADCASTS" : "show broadcasts");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDatasourcesStatement x) {
        this.print0(this.ucase ? "SHOW DATASOURCES" : "show datasources");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowNodeStatement x) {
        this.print0(this.ucase ? "SHOW NODE" : "show node");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowHelpStatement x) {
        this.print0(this.ucase ? "SHOW HELP" : "show help");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlFlashbackStatement x) {
        this.print0(this.ucase ? "FLASHBACK TABLE " : "flashback table ");
        x.getName().accept(this);
        this.print0(this.ucase ? " TO BEFORE DROP" : " to before drop");
        final SQLName renameTo = x.getRenameTo();
        if (renameTo != null) {
            this.print0(this.ucase ? " RENAME TO " : " rename to ");
            renameTo.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowConfigStatement x) {
        this.print0(this.ucase ? "SHOW CONFIG " : "show config ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPlanCacheStatement x) {
        this.print0(this.ucase ? "SHOW PLANCACHE PLAN" : "show plancache plan");
        this.println();
        x.getSelect().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDdlStatusStatement x) {
        this.print0(this.ucase ? "SHOW DDL STATUS" : "show ddl status");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDsStatement x) {
        this.print0(this.ucase ? "SHOW DS" : "show ds");
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTopologyStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isFull()) {
            this.print0(this.ucase ? "FULL " : "full ");
        }
        this.print0(this.ucase ? "TOPOLOGY FROM " : "topology from ");
        this.print0(x.getName().getSimpleName());
        if (x.getWhere() != null) {
            this.print0(this.ucase ? " WHERE " : " where ");
            x.getWhere().accept(this);
        }
        if (x.getOrderBy() != null) {
            this.print0(" ");
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.print0(" ");
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowVariantsStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        if (x.isGlobal()) {
            this.print0(this.ucase ? "GLOBAL " : "global ");
        }
        if (x.isSession()) {
            this.print0(this.ucase ? "SESSION " : "session ");
        }
        this.print0(this.ucase ? "VARIABLES" : "variables");
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
    public boolean visit(final SQLAlterTableStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "ALTER " : "alter ");
        if (x.isOnline()) {
            this.print0(this.ucase ? "ONLINE  " : "online ");
        }
        else if (x.isOffline()) {
            this.print0(this.ucase ? "OFFLINE  " : "offline ");
        }
        if (x.isIgnore()) {
            this.print0(this.ucase ? "IGNORE " : "ignore ");
        }
        this.print0(this.ucase ? "TABLE " : "table ");
        this.printTableSourceExpr(x.getName());
        ++this.indentCount;
        for (int i = 0; i < x.getItems().size(); ++i) {
            final SQLAlterTableItem item = x.getItems().get(i);
            if (i != 0) {
                this.print(',');
            }
            this.println();
            item.accept(this);
        }
        if (x.isRemovePatiting()) {
            this.println();
            this.print0(this.ucase ? "REMOVE PARTITIONING" : "remove partitioning");
        }
        if (x.isUpgradePatiting()) {
            this.println();
            this.print0(this.ucase ? "UPGRADE PARTITIONING" : "upgrade partitioning");
        }
        if (x.getTableOptions().size() > 0) {
            if (x.getItems().size() > 0) {
                this.print(',');
            }
            this.println();
        }
        --this.indentCount;
        int i = 0;
        for (final SQLAssignItem item2 : x.getTableOptions()) {
            final SQLExpr key = item2.getTarget();
            if (i != 0) {
                this.print(' ');
            }
            this.print0(this.ucase ? key.toString().toUpperCase() : key.toString().toLowerCase());
            if ("TABLESPACE".equals(key)) {
                this.print(' ');
                item2.getValue().accept(this);
            }
            else if ("UNION".equals(key)) {
                this.print0(" = (");
                item2.getValue().accept(this);
                this.print(')');
            }
            else {
                this.print0(" = ");
                item2.getValue().accept(this);
                ++i;
            }
        }
        final SQLPartitionBy partitionBy = x.getPartition();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        this.print0(this.ucase ? "ADD COLUMN " : "add column ");
        if (x.getColumns().size() > 1) {
            this.print('(');
        }
        this.printAndAccept(x.getColumns(), ", ");
        if (x.getFirstColumn() != null) {
            this.print0(this.ucase ? " FIRST " : " first ");
            x.getFirstColumn().accept(this);
        }
        else if (x.getAfterColumn() != null) {
            this.print0(this.ucase ? " AFTER " : " after ");
            x.getAfterColumn().accept(this);
        }
        else if (x.isFirst()) {
            this.print0(this.ucase ? " FIRST" : " first");
        }
        if (x.getColumns().size() > 1) {
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameTableStatement.Item x) {
        x.getName().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameTableStatement x) {
        this.print0(this.ucase ? "RENAME TABLE " : "rename table ");
        this.printAndAccept(x.getItems(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUseIndexHint x) {
        this.print0(this.ucase ? "USE INDEX " : "use index ");
        if (x.getOption() != null) {
            this.print0(this.ucase ? "FOR " : "for ");
            this.print0(x.getOption().name);
            this.print(' ');
        }
        this.print('(');
        this.printAndAccept(x.getIndexList(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlIgnoreIndexHint x) {
        this.print0(this.ucase ? "IGNORE INDEX " : "ignore index ");
        if (x.getOption() != null) {
            this.print0(this.ucase ? "FOR " : "for ");
            this.print0(this.ucase ? x.getOption().name : x.getOption().name_lcase);
            this.print(' ');
        }
        this.print('(');
        this.printAndAccept(x.getIndexList(), ", ");
        this.print(')');
        return false;
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
        for (int i = 0; i < x.getHintsSize(); ++i) {
            this.print(' ');
            x.getHints().get(i).accept(this);
        }
        if (x.getPartitionSize() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printlnAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLockTableStatement x) {
        this.print0(this.ucase ? "LOCK TABLES" : "lock tables");
        final List<MySqlLockTableStatement.Item> items = x.getItems();
        if (items.size() > 0) {
            this.print(' ');
            this.printAndAccept(items, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLockTableStatement.Item x) {
        x.getTableSource().accept(this);
        if (x.getLockType() != null) {
            this.print(' ');
            this.print0(x.getLockType().name);
        }
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUnlockTablesStatement x) {
        this.print0(this.ucase ? "UNLOCK TABLES" : "unlock tables");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlForceIndexHint x) {
        this.print0(this.ucase ? "FORCE INDEX " : "force index ");
        if (x.getOption() != null) {
            this.print0(this.ucase ? "FOR " : "for ");
            this.print0(x.getOption().name);
            this.print(' ');
        }
        this.print('(');
        this.printAndAccept(x.getIndexList(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableChangeColumn x) {
        this.print0(this.ucase ? "CHANGE COLUMN " : "change column ");
        x.getColumnName().accept(this);
        this.print(' ');
        x.getNewColumnDefinition().accept(this);
        if (x.getFirstColumn() != null) {
            this.print0(this.ucase ? " FIRST " : " first ");
            x.getFirstColumn().accept(this);
        }
        else if (x.getAfterColumn() != null) {
            this.print0(this.ucase ? " AFTER " : " after ");
            x.getAfterColumn().accept(this);
        }
        else if (x.isFirst()) {
            this.print0(this.ucase ? " FIRST" : " first");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableModifyColumn x) {
        this.print0(this.ucase ? "MODIFY COLUMN " : "modify column ");
        x.getNewColumnDefinition().accept(this);
        if (x.getFirstColumn() != null) {
            this.print0(this.ucase ? " FIRST " : " first ");
            x.getFirstColumn().accept(this);
        }
        else if (x.getAfterColumn() != null) {
            this.print0(this.ucase ? " AFTER " : " after ");
            x.getAfterColumn().accept(this);
        }
        else if (x.isFirst()) {
            this.print0(this.ucase ? " FIRST" : " first");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableOption x) {
        this.print0(x.getName());
        this.print0(" = ");
        this.print0(x.getValue().toString());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterDatabaseSetOption x) {
        this.print0(this.ucase ? "SET " : "set ");
        this.printAndAccept(x.getOptions(), ", ");
        final SQLName on = x.getOn();
        if (on != null) {
            this.print0(this.ucase ? " ON " : " on ");
            on.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterDatabaseKillJob x) {
        this.print0(this.ucase ? "KILL " : "kill ");
        x.getJobType().accept(this);
        this.print0(" ");
        x.getJobId().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlHelpStatement x) {
        this.print0(this.ucase ? "HELP " : "help ");
        x.getContent().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCharExpr x) {
        if (this.parameterized) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }
            return false;
        }
        final String charset = x.getCharset();
        final String collate = x.getCollate();
        final String text = x.getText();
        if (charset != null) {
            this.print(charset);
            final long charsetHashCode = FnvHash.hashCode64(charset);
            if (charsetHashCode == FnvHash.Constants._UCS2 || charsetHashCode == FnvHash.Constants._UTF16) {
                this.print(" x'");
            }
            else {
                this.print(" '");
            }
            this.print(text);
            this.print('\'');
        }
        else {
            this.print('\'');
            this.print(text);
            this.print('\'');
        }
        if (collate != null) {
            this.print(" COLLATE ");
            this.print(collate);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUnique x) {
        this.visit(x.getIndexDefinition());
        return false;
    }
    
    @Override
    public boolean visit(final MysqlForeignKey x) {
        if (x.isHasConstraint()) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            if (x.getName() != null) {
                x.getName().accept(this);
                this.print(' ');
            }
        }
        this.print0(this.ucase ? "FOREIGN KEY" : "foreign key");
        if (x.getIndexName() != null) {
            this.print(' ');
            x.getIndexName().accept(this);
        }
        this.print0(" (");
        this.printAndAccept(x.getReferencingColumns(), ", ");
        this.print(')');
        this.print0(this.ucase ? " REFERENCES " : " references ");
        x.getReferencedTableName().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getReferencedColumns(), ", ");
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
    public boolean visit(final MySqlAlterTableDiscardTablespace x) {
        this.print0(this.ucase ? "DISCARD TABLESPACE" : "discard tablespace");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateExternalCatalogStatement x) {
        this.print0(this.ucase ? "CREATE EXTERNAL CATALOG " : "create external catalog ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        x.getName().accept(this);
        this.print0(this.ucase ? " PROPERTIES (" : " properties (");
        for (final Map.Entry<SQLName, SQLName> entry : x.getProperties().entrySet()) {
            this.println();
            entry.getKey().accept(this);
            this.print0("=");
            entry.getValue().accept(this);
        }
        this.print0(")");
        if (x.getComment() != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableImportTablespace x) {
        this.print0(this.ucase ? "IMPORT TABLESPACE" : "import tablespace");
        return false;
    }
    
    @Override
    public boolean visit(final SQLAssignItem x) {
        final String tagetString = x.getTarget().toString();
        boolean mysqlSpecial = false;
        if (DbType.mysql == this.dbType) {
            mysqlSpecial = ("NAMES".equalsIgnoreCase(tagetString) || "CHARACTER SET".equalsIgnoreCase(tagetString) || "CHARSET".equalsIgnoreCase(tagetString));
        }
        if (!mysqlSpecial) {
            x.getTarget().accept(this);
            this.print0(" = ");
        }
        else {
            this.print0(this.ucase ? tagetString.toUpperCase() : tagetString.toLowerCase());
            this.print(' ');
        }
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateTableStatement.TableSpaceOption x) {
        x.getName().accept(this);
        if (x.getStorage() != null) {
            this.print(this.ucase ? " STORAGE " : " storage ");
            x.getStorage().accept(this);
        }
        return false;
    }
    
    @Override
    protected void visitAggreateRest(final SQLAggregateExpr aggregateExpr) {
        final SQLOrderBy value = aggregateExpr.getOrderBy();
        if (value != null) {
            this.print(' ');
            value.accept(this);
        }
        final Object value2 = aggregateExpr.getAttribute("SEPARATOR");
        if (value2 != null) {
            this.print0(this.ucase ? " SEPARATOR " : " separator ");
            ((SQLObject)value2).accept(this);
        }
    }
    
    @Override
    public boolean visit(final MySqlAnalyzeStatement x) {
        this.print0(this.ucase ? "ANALYZE " : "analyze ");
        if (x.isNoWriteToBinlog()) {
            this.print0(this.ucase ? "NO_WRITE_TO_BINLOG " : "no_write_to_binlog ");
        }
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        if (!x.getAdbColumns().isEmpty()) {
            this.print0(this.ucase ? "COLUMN " : "column ");
        }
        else if (!x.getAdbColumnsGroup().isEmpty()) {
            this.print0(this.ucase ? "COLUMNS GROUP " : "columns group ");
        }
        else if (x.getAdbSchema() != null) {
            this.print0(this.ucase ? "DATABASE " : "database ");
            x.getAdbSchema().accept(this);
        }
        else if (!x.getTableSources().isEmpty()) {
            this.print0(this.ucase ? "TABLE " : "table ");
        }
        this.printAndAccept(x.getTableSources(), ", ");
        if (!x.getAdbColumns().isEmpty()) {
            this.print0("(");
            this.printAndAccept(x.getAdbColumns(), ",");
            this.print0(")");
            if (x.getAdbWhere() != null) {
                this.println();
                this.print0(this.ucase ? " WHERE " : " WHERE ");
                this.printExpr(x.getAdbWhere());
            }
        }
        else if (!x.getAdbColumnsGroup().isEmpty()) {
            this.print0("(");
            this.printAndAccept(x.getAdbColumnsGroup(), ",");
            this.print0(")");
            if (x.getAdbWhere() != null) {
                this.println();
                this.print0(this.ucase ? " WHERE " : " WHERE ");
                this.printExpr(x.getAdbWhere());
            }
        }
        else if (!x.getTableSources().isEmpty() && x.getAdbWhere() != null) {
            this.println();
            this.print0(this.ucase ? " WHERE " : " WHERE ");
            this.printExpr(x.getAdbWhere());
        }
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
    public boolean visit(final MySqlOptimizeStatement x) {
        this.print0(this.ucase ? "OPTIMIZE " : "optimize ");
        if (x.isNoWriteToBinlog()) {
            this.print0(this.ucase ? "NO_WRITE_TO_BINLOG " : "No_write_to_binlog ");
        }
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "TABLE " : "table ");
        this.printAndAccept(x.getTableSources(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterUserStatement x) {
        this.print0(this.ucase ? "ALTER USER" : "alter user");
        if (x.isIfExists()) {
            this.print0(this.ucase ? " IF EXISTS" : " if exists");
        }
        for (int i = 0; i < x.getAlterUsers().size(); ++i) {
            if (i != 0) {
                this.print(',');
            }
            final MySqlAlterUserStatement.AlterUser alterUser = x.getAlterUsers().get(i);
            this.print(' ');
            alterUser.getUser().accept(this);
            if (alterUser.getAuthOption() != null) {
                this.print(" IDENTIFIED BY ");
                final SQLCharExpr authString = alterUser.getAuthOption().getAuthString();
                authString.accept(this);
            }
        }
        final MySqlAlterUserStatement.PasswordOption passwordOption = x.getPasswordOption();
        if (passwordOption != null) {
            switch (passwordOption.getExpire()) {
                case PASSWORD_EXPIRE: {
                    this.print0(this.ucase ? " PASSWORD EXPIRE" : " password expire");
                    break;
                }
                case PASSWORD_EXPIRE_DEFAULT: {
                    this.print0(this.ucase ? " PASSWORD EXPIRE DEFAULT" : " password expire default");
                    break;
                }
                case PASSWORD_EXPIRE_NEVER: {
                    this.print0(this.ucase ? " PASSWORD EXPIRE NEVER" : " password expire never");
                    break;
                }
                case PASSWORD_EXPIRE_INTERVAL: {
                    this.print0(this.ucase ? " PASSWORD EXPIRE INTERVAL " : " password expire interval ");
                    passwordOption.getIntervalDays().accept(this);
                    this.print0(this.ucase ? " DAY" : " day");
                    break;
                }
                default: {
                    throw new RuntimeException("invalid password option:" + passwordOption);
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSetStatement x) {
        final boolean printSet = x.getAttribute("parser.set") == Boolean.TRUE || DbType.oracle != this.dbType;
        if (printSet) {
            this.print0(this.ucase ? "SET " : "set ");
        }
        final SQLSetStatement.Option option = x.getOption();
        if (option != null) {
            this.print(option.name());
            this.print(' ');
        }
        if (option == SQLSetStatement.Option.PASSWORD) {
            this.print0("FOR ");
        }
        this.printAndAccept(x.getItems(), ", ");
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlHintStatement x) {
        final List<SQLCommentHint> hints = x.getHints();
        for (final SQLCommentHint hint : hints) {
            hint.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlOrderingExpr x) {
        x.getExpr().accept(this);
        if (x.getType() != null) {
            this.print(' ');
            this.print0(this.ucase ? x.getType().name : x.getType().name_lcase);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBlockStatement x) {
        final SQLObject parent = x.getParent();
        final String labelName = x.getLabelName();
        if (labelName != null && !labelName.equals("")) {
            this.print0(labelName);
            this.print0(": ");
        }
        final List<SQLParameter> parameters = x.getParameters();
        if (parameters.size() != 0) {
            ++this.indentCount;
            if (parent instanceof SQLCreateProcedureStatement) {
                this.printIndent();
            }
            if (!(parent instanceof SQLCreateProcedureStatement)) {
                this.print0(this.ucase ? "DECLARE" : "declare");
                this.println();
            }
            for (int i = 0, size = parameters.size(); i < size; ++i) {
                if (i != 0) {
                    this.println();
                }
                final SQLParameter param = parameters.get(i);
                this.visit(param);
                this.print(';');
            }
            --this.indentCount;
            this.println();
        }
        this.print0(this.ucase ? "BEGIN" : "begin");
        if (!x.isEndOfCommit()) {
            ++this.indentCount;
        }
        else {
            this.print(';');
        }
        this.println();
        final List<SQLStatement> statementList = x.getStatementList();
        for (int j = 0, size2 = statementList.size(); j < size2; ++j) {
            if (j != 0) {
                this.println();
            }
            final SQLStatement stmt = statementList.get(j);
            stmt.accept(this);
        }
        if (!x.isEndOfCommit()) {
            --this.indentCount;
            this.println();
            this.print0(this.ucase ? "END" : "end");
            if (labelName != null && !labelName.equals("")) {
                this.print(' ');
                this.print0(labelName);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement x) {
        this.print0(this.ucase ? "IF " : "if ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        ++this.indentCount;
        this.println();
        final List<SQLStatement> statements = x.getStatements();
        for (int i = 0, size = statements.size(); i < size; ++i) {
            final SQLStatement item = statements.get(i);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        this.println();
        for (final SQLIfStatement.ElseIf iterable_element : x.getElseIfList()) {
            iterable_element.accept(this);
        }
        if (x.getElseItem() != null) {
            x.getElseItem().accept(this);
        }
        this.print0(this.ucase ? "END IF" : "end if");
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.ElseIf x) {
        this.print0(this.ucase ? "ELSE IF " : "else if ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
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
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.Else x) {
        this.print0(this.ucase ? "ELSE " : "else ");
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
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCaseStatement x) {
        this.print0(this.ucase ? "CASE " : "case ");
        x.getCondition().accept(this);
        this.println();
        for (int i = 0; i < x.getWhenList().size(); ++i) {
            x.getWhenList().get(i).accept(this);
        }
        if (x.getElseItem() != null) {
            x.getElseItem().accept(this);
        }
        this.print0(this.ucase ? "END CASE" : "end case");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeclareStatement x) {
        this.print0(this.ucase ? "DECLARE " : "declare ");
        this.printAndAccept(x.getVarList(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSelectIntoStatement x) {
        x.getSelect().accept(this);
        this.print0(this.ucase ? " INTO " : " into ");
        for (int i = 0; i < x.getVarList().size(); ++i) {
            x.getVarList().get(i).accept(this);
            if (i != x.getVarList().size() - 1) {
                this.print0(", ");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCaseStatement.MySqlWhenStatement x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getCondition().accept(this);
        this.print0(" THEN");
        this.println();
        for (int i = 0; i < x.getStatements().size(); ++i) {
            x.getStatements().get(i).accept(this);
            if (i != x.getStatements().size() - 1) {
                this.println();
            }
        }
        this.println();
        return false;
    }
    
    @Override
    public boolean visit(final SQLLoopStatement x) {
        if (x.getLabelName() != null && !x.getLabelName().equals("")) {
            this.print0(x.getLabelName());
            this.print0(": ");
        }
        this.print0(this.ucase ? "LOOP " : "loop ");
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
        if (x.getLabelName() != null && !x.getLabelName().equals("")) {
            this.print0(" ");
            this.print0(x.getLabelName());
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLeaveStatement x) {
        this.print0(this.ucase ? "LEAVE " : "leave ");
        this.print0(x.getLabelName());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlIterateStatement x) {
        this.print0(this.ucase ? "ITERATE " : "iterate ");
        this.print0(x.getLabelName());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRepeatStatement x) {
        if (x.getLabelName() != null && !x.getLabelName().equals("")) {
            this.print0(x.getLabelName());
            this.print0(": ");
        }
        this.print0(this.ucase ? "REPEAT " : "repeat ");
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
        this.print0(this.ucase ? "UNTIL " : "until ");
        x.getCondition().accept(this);
        this.println();
        this.print0(this.ucase ? "END REPEAT" : "end repeat");
        if (x.getLabelName() != null && !x.getLabelName().equals("")) {
            this.print(' ');
            this.print0(x.getLabelName());
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCursorDeclareStatement x) {
        this.print0(this.ucase ? "DECLARE " : "declare ");
        this.printExpr(x.getCursorName(), this.parameterized);
        this.print0(this.ucase ? " CURSOR FOR" : " cursor for");
        ++this.indentCount;
        this.println();
        x.getSelect().accept(this);
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdateTableSource x) {
        final MySqlUpdateStatement update = x.getUpdate();
        if (update != null) {
            update.accept0(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableAlterColumn x) {
        this.print0(this.ucase ? "ALTER COLUMN " : "alter column ");
        x.getColumn().accept(this);
        if (x.getDefaultExpr() != null) {
            this.print0(this.ucase ? " SET DEFAULT " : " set default ");
            x.getDefaultExpr().accept(this);
        }
        else if (x.isDropDefault()) {
            this.print0(this.ucase ? " DROP DEFAULT" : " drop default");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableForce x) {
        this.print0(this.ucase ? "FORCE" : "force");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableLock x) {
        this.print0(this.ucase ? "LOCK = " : "lock = ");
        this.printExpr(x.getLockType());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableOrderBy x) {
        this.print0(this.ucase ? "ORDER BY " : "order by ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableValidation x) {
        if (x.isWithValidation()) {
            this.print0(this.ucase ? "WITH VALIDATION" : "with validation");
        }
        else {
            this.print0(this.ucase ? "WITHOUT VALIDATION" : "without validation");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByKey x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "SUBPARTITION BY LINEAR KEY (" : "subpartition by linear key (");
        }
        else {
            this.print0(this.ucase ? "SUBPARTITION BY KEY (" : "subpartition by key (");
        }
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        if (x.getSubPartitionsCount() != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            x.getSubPartitionsCount().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByValue x) {
        this.print0(this.ucase ? "SUBPARTITION BY VALUE (" : "subpartition by value (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        if (x.getLifecycle() != null) {
            this.print0(this.ucase ? " LIFECYCLE " : " lifecycle ");
            x.getLifecycle().accept(this);
        }
        if (x.getSubPartitionsCount() != null) {
            if (x.getAttribute("adb.partitons")) {
                this.print0(this.ucase ? " PARTITIONS " : " partitions ");
            }
            else {
                this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            }
            x.getSubPartitionsCount().accept(this);
        }
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
    public boolean visit(final SQLAlterTableCompression x) {
        this.print0(this.ucase ? "COMPRESSION = " : "compression = ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByList x) {
        this.print0(this.ucase ? "SUBPARTITION BY LIST " : "subpartition by list ");
        if (x.getKeys().size() > 0) {
            if (Boolean.TRUE.equals(x.getAttribute("ads.subPartitionList"))) {
                this.print0(this.ucase ? "KEY (" : "key (");
            }
            else {
                this.print('(');
            }
            this.printAndAccept(x.getKeys(), ",");
            this.print0(") ");
        }
        else {
            if (x.getColumns().size() == 1 && Boolean.TRUE.equals(x.getAttribute("ads.subPartitionList"))) {
                this.print('(');
            }
            else {
                this.print0(this.ucase ? "COLUMNS (" : "columns (");
            }
            this.printAndAccept(x.getColumns(), ", ");
            this.print(")");
        }
        if (x.getOptions().size() != 0) {
            this.println();
            this.print0(this.ucase ? "SUBPARTITION OPTIONS (" : "subpartition options (");
            this.printAndAccept(x.getOptions(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeclareHandlerStatement x) {
        final String handleType = x.getHandleType().name();
        this.print0(this.ucase ? "DECLARE " : "declare ");
        this.print0(this.ucase ? handleType : handleType.toLowerCase());
        this.print0(this.ucase ? " HANDLER FOR " : " handler for ");
        for (int i = 0; i < x.getConditionValues().size(); ++i) {
            final ConditionValue cv = x.getConditionValues().get(i);
            if (cv.getType() == ConditionValue.ConditionType.SQLSTATE) {
                this.print0(this.ucase ? " SQLSTATE " : " sqlstate ");
                this.print0(cv.getValue());
            }
            else if (cv.getType() == ConditionValue.ConditionType.MYSQL_ERROR_CODE) {
                this.print0(cv.getValue());
            }
            else if (cv.getType() == ConditionValue.ConditionType.SELF) {
                this.print0(cv.getValue());
            }
            else if (cv.getType() == ConditionValue.ConditionType.SYSTEM) {
                this.print0(this.ucase ? cv.getValue().toUpperCase() : cv.getValue().toLowerCase());
            }
            if (i != x.getConditionValues().size() - 1) {
                this.print0(", ");
            }
        }
        ++this.indentCount;
        this.println();
        x.getSpStatement().accept(this);
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeclareConditionStatement x) {
        this.print0(this.ucase ? "DECLARE " : "declare ");
        this.print0(x.getConditionName());
        this.print0(this.ucase ? " CONDITION FOR " : " condition for ");
        if (x.getConditionValue().getType() == ConditionValue.ConditionType.SQLSTATE) {
            this.print0(this.ucase ? "SQLSTATE " : "sqlstate ");
            this.print0(x.getConditionValue().getValue());
        }
        else {
            this.print0(x.getConditionValue().getValue());
        }
        this.println();
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropColumnItem x) {
        for (int i = 0; i < x.getColumns().size(); ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLName columnn = x.getColumns().get(i);
            this.print0(this.ucase ? "DROP COLUMN " : "drop column ");
            columnn.accept(this);
            if (x.isCascade()) {
                this.print0(this.ucase ? " CASCADE" : " cascade");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateProcedureStatement x) {
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE PROCEDURE " : "create or replace procedure ");
        }
        else {
            this.print0(this.ucase ? "CREATE PROCEDURE " : "create procedure ");
        }
        x.getName().accept(this);
        final int paramSize = x.getParameters().size();
        this.print0(" (");
        if (paramSize > 0) {
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
        }
        this.print(')');
        if (x.getComment() != null) {
            this.println();
            this.print(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }
        if (x.isDeterministic()) {
            this.println();
            this.print(this.ucase ? "DETERMINISTIC" : "deterministic");
        }
        if (x.isContainsSql()) {
            this.println();
            this.print0(this.ucase ? "CONTAINS SQL" : "contains sql");
        }
        if (x.isLanguageSql()) {
            this.println();
            this.print0(this.ucase ? "LANGUAGE SQL" : "language sql");
        }
        if (x.isNoSql()) {
            this.println();
            this.print(this.ucase ? "NO SQL" : "no sql");
        }
        if (x.isModifiesSqlData()) {
            this.println();
            this.print(this.ucase ? "MODIFIES SQL DATA" : "modifies sql data");
        }
        final SQLName authid = x.getAuthid();
        if (authid != null) {
            this.println();
            this.print(this.ucase ? "SQL SECURITY " : "sql security ");
            authid.accept(this);
        }
        this.println();
        x.getBlock().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateFunctionStatement x) {
        this.print0(this.ucase ? "CREATE FUNCTION " : "create function ");
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
        this.println();
        this.print(this.ucase ? "RETURNS " : "returns ");
        x.getReturnDataType().accept(this);
        final String comment = x.getComment();
        if (comment != null) {
            this.print(this.ucase ? " COMMENT " : " comment ");
            this.print(this.ucase ? comment.toUpperCase() : comment.toLowerCase());
        }
        if (x.isDeterministic()) {
            this.print(this.ucase ? " DETERMINISTIC" : " deterministic");
        }
        final String language = x.getLanguage();
        if (language != null) {
            this.print(this.ucase ? " LANGUAGE " : " language ");
            this.print(this.ucase ? language.toUpperCase() : language.toLowerCase());
        }
        final SQLStatement block = x.getBlock();
        this.println();
        block.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCommentStatement x) {
        final SQLCommentStatement.Type type = x.getType();
        final SQLExprTableSource on = x.getOn();
        if (type == SQLCommentStatement.Type.TABLE) {
            this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
            on.accept(this);
            this.print0(this.ucase ? " COMMENT = " : " comment = ");
            x.getComment().accept(this);
        }
        else {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)on.getExpr();
            final SQLExpr table = propertyExpr.getOwner();
            final String column = propertyExpr.getName();
            this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
            this.printTableSourceExpr(table);
            this.print0(this.ucase ? " MODIFY COLUMN " : " modify column ");
            this.print(column);
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlFlushStatement x) {
        this.print0(this.ucase ? "FLUSH" : "flush");
        if (x.isNoWriteToBinlog()) {
            this.print0(this.ucase ? " NO_WRITE_TO_BINLOG" : " no_write_to_binlog");
        }
        else if (x.isLocal()) {
            this.print0(this.ucase ? " LOCAL" : " local");
        }
        if (x.isBinaryLogs()) {
            this.print0(this.ucase ? " BINARY LOGS" : " binary logs");
        }
        if (x.isDesKeyFile()) {
            this.print0(this.ucase ? " DES_KEY_FILE" : " des_key_file");
        }
        if (x.isEngineLogs()) {
            this.print0(this.ucase ? " ENGINE LOGS" : " engine logs");
        }
        if (x.isErrorLogs()) {
            this.print0(this.ucase ? " ERROR LOGS" : " error logs");
        }
        if (x.isGeneralLogs()) {
            this.print0(this.ucase ? " GENERAL LOGS" : " general logs");
        }
        if (x.isHots()) {
            this.print0(this.ucase ? " HOSTS" : " hosts");
        }
        if (x.isLogs()) {
            this.print0(this.ucase ? " LOGS" : " logs");
        }
        if (x.isPrivileges()) {
            this.print0(this.ucase ? " PRIVILEGES" : " privileges");
        }
        if (x.isOptimizerCosts()) {
            this.print0(this.ucase ? " OPTIMIZER_COSTS" : " optimizer_costs");
        }
        if (x.isQueryCache()) {
            this.print0(this.ucase ? " QUERY CACHE" : " query cache");
        }
        if (x.isRelayLogs()) {
            this.print0(this.ucase ? " RELAY LOGS" : " relay logs");
            final SQLExpr channel = x.getRelayLogsForChannel();
            if (channel != null) {
                this.print(' ');
                channel.accept(this);
            }
        }
        if (x.isSlowLogs()) {
            this.print0(this.ucase ? " SLOW LOGS" : " slow logs");
        }
        if (x.isStatus()) {
            this.print0(this.ucase ? " STATUS" : " status");
        }
        if (x.isUserResources()) {
            this.print0(this.ucase ? " USER_RESOURCES" : " user_resources");
        }
        if (x.isTableOption()) {
            this.print0(this.ucase ? " TABLES" : " tables");
            final List<SQLExprTableSource> tables = x.getTables();
            if (tables != null && tables.size() > 0) {
                this.print(' ');
                this.printAndAccept(tables, ", ");
            }
            if (x.isWithReadLock()) {
                this.print0(this.ucase ? " WITH READ LOCK" : " with read lock");
            }
            if (x.isForExport()) {
                this.print0(this.ucase ? " FOR EXPORT" : " for export");
            }
            if (x.getVersion() != null) {
                this.print0(this.ucase ? " VERSION = " : " version = ");
                x.getVersion().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlEventSchedule x) {
        int cnt = 0;
        if (x.getAt() != null) {
            this.print0(this.ucase ? "AT " : "at ");
            this.printExpr(x.getAt(), this.parameterized);
            ++cnt;
        }
        if (x.getEvery() != null) {
            this.print0(this.ucase ? "EVERY " : "every ");
            final SQLIntervalExpr interval = (SQLIntervalExpr)x.getEvery();
            this.printExpr(interval.getValue(), this.parameterized);
            this.print(' ');
            this.print(interval.getUnit().name());
            ++cnt;
        }
        if (x.getStarts() != null) {
            if (cnt > 0) {
                this.print(' ');
            }
            this.print0(this.ucase ? "STARTS " : "starts ");
            this.printExpr(x.getStarts(), this.parameterized);
            ++cnt;
        }
        if (x.getEnds() != null) {
            if (cnt > 0) {
                this.print(' ');
            }
            this.print0(this.ucase ? "ENDS " : "ends ");
            this.printExpr(x.getEnds(), this.parameterized);
            ++cnt;
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateEventStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        final SQLName definer = x.getDefiner();
        if (definer != null) {
            this.print0(this.ucase ? "DEFINER = " : "definer = ");
        }
        this.print0(this.ucase ? "EVENT " : "evnet ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }
        this.printExpr(x.getName(), this.parameterized);
        final MySqlEventSchedule schedule = x.getSchedule();
        this.print0(this.ucase ? " ON SCHEDULE " : " on schedule ");
        schedule.accept(this);
        final Boolean enable = x.getEnable();
        if (enable != null) {
            if (enable) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
                if (x.isDisableOnSlave()) {
                    this.print0(this.ucase ? " ON SLAVE" : " on slave");
                }
            }
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? "COMMENT " : "comment ");
            comment.accept(this);
        }
        this.println();
        final SQLStatement body = x.getEventBody();
        if (!(body instanceof SQLExprStatement)) {
            this.print0(this.ucase ? "DO" : "do");
            this.println();
        }
        body.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateAddLogFileGroupStatement x) {
        this.print0(this.ucase ? "CREATE LOGFILE GROUP " : "create logfile group ");
        x.getName().accept(this);
        this.print(' ');
        this.print0(this.ucase ? "ADD UNDOFILE " : "add undofile ");
        this.printExpr(x.getAddUndoFile(), false);
        final SQLExpr initialSize = x.getInitialSize();
        if (initialSize != null) {
            this.print0(this.ucase ? " INITIAL_SIZE " : " initial_size ");
            this.printExpr(initialSize, false);
        }
        final SQLExpr undoBufferSize = x.getUndoBufferSize();
        if (undoBufferSize != null) {
            this.print0(this.ucase ? " UNDO_BUFFER_SIZE " : " undo_buffer_size ");
            this.printExpr(undoBufferSize, false);
        }
        final SQLExpr redoBufferSize = x.getRedoBufferSize();
        if (redoBufferSize != null) {
            this.print0(this.ucase ? " REDO_BUFFER_SIZE " : " redo_buffer_size ");
            this.printExpr(redoBufferSize, false);
        }
        final SQLExpr nodeGroup = x.getNodeGroup();
        if (nodeGroup != null) {
            this.print0(this.ucase ? " NODEGROUP " : " nodegroup ");
            this.printExpr(nodeGroup, false);
        }
        if (x.isWait()) {
            this.print0(this.ucase ? " WAIT" : " wait");
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment, this.parameterized);
        }
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE " : " engine ");
            this.printExpr(engine, this.parameterized);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateServerStatement x) {
        this.print0(this.ucase ? "CREATE SERVER " : "create server ");
        x.getName().accept(this);
        this.print0(this.ucase ? " FOREIGN DATA WRAPPER " : " foreign data wrapper ");
        this.printExpr(x.getForeignDataWrapper(), this.parameterized);
        this.print(" OPTIONS(");
        int cnt = 0;
        final SQLExpr host = x.getHost();
        if (host != null) {
            this.print0(this.ucase ? "HOST " : "host ");
            this.printExpr(host, this.parameterized);
            ++cnt;
        }
        final SQLExpr database = x.getDatabase();
        if (database != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "DATABASE " : "database ");
            this.printExpr(database, this.parameterized);
        }
        final SQLExpr user = x.getUser();
        if (user != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "USER " : "user ");
            this.printExpr(user);
        }
        final SQLExpr password = x.getPassword();
        if (password != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "PASSWORD " : "password ");
            this.printExpr(password);
        }
        final SQLExpr socket = x.getSocket();
        if (socket != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "SOCKET " : "socket ");
            this.printExpr(socket);
        }
        final SQLExpr owner = x.getOwner();
        if (owner != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "OWNER " : "owner ");
            this.printExpr(owner);
        }
        final SQLExpr port = x.getPort();
        if (port != null) {
            if (cnt++ > 0) {
                this.print(", ");
            }
            this.print0(this.ucase ? "PORT " : "port ");
            this.printExpr(port);
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateTableSpaceStatement x) {
        this.print0(this.ucase ? "CREATE TABLESPACE " : "create tablespace ");
        x.getName().accept(this);
        final SQLExpr addDataFile = x.getAddDataFile();
        if (addDataFile != null) {
            this.print0(this.ucase ? " ADD DATAFILE " : " add datafile ");
            addDataFile.accept(this);
        }
        final SQLExpr fileBlockSize = x.getFileBlockSize();
        if (fileBlockSize != null) {
            this.print0(this.ucase ? " FILE_BLOCK_SIZE = " : " file_block_size = ");
            fileBlockSize.accept(this);
        }
        final SQLExpr logFileGroup = x.getLogFileGroup();
        if (logFileGroup != null) {
            this.print0(this.ucase ? " USE LOGFILE GROUP " : " use logfile group ");
            logFileGroup.accept(this);
        }
        final SQLExpr extentSize = x.getExtentSize();
        if (extentSize != null) {
            this.print0(this.ucase ? " EXTENT_SIZE = " : " extent_size = ");
            extentSize.accept(this);
        }
        final SQLExpr initialSize = x.getInitialSize();
        if (initialSize != null) {
            this.print0(this.ucase ? " INITIAL_SIZE = " : " initial_size = ");
            initialSize.accept(this);
        }
        final SQLExpr autoExtentSize = x.getAutoExtentSize();
        if (autoExtentSize != null) {
            this.print0(this.ucase ? " AUTOEXTEND_SIZE = " : " autoextend_size = ");
            autoExtentSize.accept(this);
        }
        final SQLExpr maxSize = x.getMaxSize();
        if (autoExtentSize != null) {
            this.print0(this.ucase ? " MAX_SIZE = " : " max_size = ");
            maxSize.accept(this);
        }
        final SQLExpr nodeGroup = x.getNodeGroup();
        if (nodeGroup != null) {
            this.print0(this.ucase ? " NODEGROUP = " : " nodegroup = ");
            nodeGroup.accept(this);
        }
        if (x.isWait()) {
            this.print0(this.ucase ? " WAIT" : " wait");
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment);
        }
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE " : " engine ");
            this.printExpr(engine);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterEventStatement x) {
        this.print0(this.ucase ? "ALTER " : "alter ");
        final SQLName definer = x.getDefiner();
        if (definer != null) {
            this.print0(this.ucase ? "DEFINER = " : "definer = ");
        }
        this.print0(this.ucase ? "EVENT " : "evnet ");
        this.printExpr(x.getName());
        final MySqlEventSchedule schedule = x.getSchedule();
        if (schedule != null) {
            this.print0(this.ucase ? " ON SCHEDULE " : " on schedule ");
            schedule.accept(this);
        }
        final Boolean enable = x.getEnable();
        if (enable != null) {
            if (enable) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
                if (x.isDisableOnSlave()) {
                    this.print0(this.ucase ? " ON SLAVE" : " on slave");
                }
            }
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? "COMMENT " : "comment ");
            comment.accept(this);
        }
        final SQLStatement body = x.getEventBody();
        if (body != null) {
            this.println();
            if (!(body instanceof SQLExprStatement)) {
                this.print0(this.ucase ? "DO" : "do");
                this.println();
            }
            body.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterLogFileGroupStatement x) {
        this.print0(this.ucase ? "ALTER LOGFILE GROUP " : "alter logfile group ");
        x.getName().accept(this);
        this.print(' ');
        this.print0(this.ucase ? "ADD UNDOFILE " : "add undofile ");
        this.printExpr(x.getAddUndoFile());
        final SQLExpr initialSize = x.getInitialSize();
        if (initialSize != null) {
            this.print0(this.ucase ? " INITIAL_SIZE " : " initial_size ");
            this.printExpr(initialSize);
        }
        if (x.isWait()) {
            this.print0(this.ucase ? " WAIT" : " wait");
        }
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE " : " engine ");
            this.printExpr(engine);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterServerStatement x) {
        this.print0(this.ucase ? "ALTER SERVER " : "alter server ");
        x.getName().accept(this);
        this.print(" OPTIONS(");
        final SQLExpr user = x.getUser();
        if (user != null) {
            this.print0(this.ucase ? "USER " : "user ");
            this.printExpr(user);
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTablespaceStatement x) {
        this.print0(this.ucase ? "CREATE TABLESPACE " : "create tablespace ");
        x.getName().accept(this);
        final SQLExpr addDataFile = x.getAddDataFile();
        if (addDataFile != null) {
            this.print0(this.ucase ? " ADD DATAFILE " : " add datafile ");
            addDataFile.accept(this);
        }
        final SQLExpr initialSize = x.getInitialSize();
        if (initialSize != null) {
            this.print0(this.ucase ? " INITIAL_SIZE = " : " initial_size = ");
            initialSize.accept(this);
        }
        if (x.isWait()) {
            this.print0(this.ucase ? " WAIT" : " wait");
        }
        final SQLExpr engine = x.getEngine();
        if (engine != null) {
            this.print0(this.ucase ? " ENGINE " : " engine ");
            this.printExpr(engine);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlChecksumTableStatement x) {
        this.print0(this.ucase ? "CHECKSUM TABLE " : "checksum table ");
        final List<SQLExprTableSource> tables = x.getTables();
        for (int i = 0; i < tables.size(); ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            tables.get(i).accept(this);
        }
        return false;
    }
    
    @Override
    protected void printQuery(final SQLSelectQuery x) {
        final Class<?> clazz = x.getClass();
        if (clazz == MySqlSelectQueryBlock.class) {
            this.visit((MySqlSelectQueryBlock)x);
        }
        else if (clazz == SQLSelectQueryBlock.class) {
            this.visit((SQLSelectQueryBlock)x);
        }
        else if (clazz == SQLUnionQuery.class) {
            this.visit((SQLUnionQuery)x);
        }
        else {
            x.accept(this);
        }
    }
    
    @Override
    public void printInsertColumns(final List<SQLExpr> columns) {
        final int size = columns.size();
        if (size > 0) {
            if (size > 5) {
                ++this.indentCount;
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
    public boolean visit(final SQLValuesTableSource x) {
        this.print('(');
        this.incrementIndent();
        this.println();
        this.print0(this.ucase ? "VALUES " : "values ");
        final List<SQLListExpr> values = x.getValues();
        for (int i = 0; i < values.size(); ++i) {
            if (i != 0) {
                this.print(", ");
                this.println();
            }
            final SQLListExpr list = values.get(i);
            this.visit(list);
        }
        this.decrementIndent();
        this.println();
        this.print0(")");
        if (x.getAlias() != null) {
            this.print0(" AS ");
            this.print0(x.getAlias());
            if (x.getColumns().size() > 0) {
                this.print0(" (");
                this.printAndAccept(x.getColumns(), ", ");
                this.print(')');
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExternalRecordFormat x) {
        return this.hiveVisit(x);
    }
    
    @Override
    public boolean visit(final MySqlJSONTableExpr x) {
        this.print0(this.ucase ? "JSON_TABLE(" : "json_table(");
        x.getExpr().accept(this);
        this.print(' ');
        x.getPath().accept(this);
        this.incrementIndent();
        this.println();
        this.print0(this.ucase ? "COLUMNS (" : "columns (");
        this.incrementIndent();
        this.println();
        this.printlnAndAccept(x.getColumns(), ", ");
        this.decrementIndent();
        this.println();
        this.print(')');
        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final MySqlJSONTableExpr.Column x) {
        x.getName().accept(this);
        if (x.isOrdinality()) {
            this.print0(this.ucase ? " FOR ORDINALITY" : " for ordinality");
        }
        final SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            this.print(' ');
            dataType.accept(this);
        }
        if (x.isExists()) {
            this.print0(this.ucase ? " EXISTS" : " exists");
        }
        final SQLExpr path = x.getPath();
        if (path != null) {
            this.print0(this.ucase ? " PATH " : " path ");
            path.accept(this);
        }
        final List<MySqlJSONTableExpr.Column> nestedColumns = x.getNestedColumns();
        if (nestedColumns.size() > 0) {
            this.print0(this.ucase ? " COLUMNS (" : " columns (");
            this.printAndAccept(nestedColumns, ", ");
            this.print(')');
        }
        final SQLExpr onEmpty = x.getOnEmpty();
        if (onEmpty != null) {
            this.print(' ');
            if (!(onEmpty instanceof SQLNullExpr) && !(onEmpty instanceof SQLIdentifierExpr)) {
                this.print0(this.ucase ? "DEFAULT " : "default ");
            }
            onEmpty.accept(this);
            this.print0(this.ucase ? " ON EMPTY" : " on empty");
        }
        final SQLExpr onError = x.getOnError();
        if (onError != null) {
            this.print(' ');
            if (!(onEmpty instanceof SQLNullExpr) && !(onEmpty instanceof SQLIdentifierExpr)) {
                this.print0(this.ucase ? "DEFAULT " : "default ");
            }
            onError.accept(this);
            this.print0(this.ucase ? " ON ERROR" : " on error");
        }
        return false;
    }
    
    @Override
    public boolean visit(final MysqlAlterTableAlterCheck x) {
        this.print0(this.ucase ? "ALTER CONSTRAINT " : "alter constraint ");
        final SQLName name = x.getName();
        if (name != null) {
            name.accept(this);
            this.print(' ');
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
    public void endVisit(final MysqlAlterTableAlterCheck x) {
    }
    
    static {
        MySqlOutputVisitor.shardingSupportChecked = false;
    }
}
