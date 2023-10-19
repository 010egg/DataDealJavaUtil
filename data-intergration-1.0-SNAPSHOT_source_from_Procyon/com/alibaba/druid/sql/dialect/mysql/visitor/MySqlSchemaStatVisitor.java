// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

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
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlIterateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlLeaveStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableValidation;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOrderBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableLock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableForce;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterCharacter;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTopologyStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDdlStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBroadcastsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTraceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowStcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowHtcStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlShowDbLockStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlowStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSequencesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRuleStatement;
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
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
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
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowHMSMetaStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseKillJob;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterDatabaseSetOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDisabledPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlClearPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPlanCacheStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdatePlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateExternalCatalogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabaseStatusStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class MySqlSchemaStatVisitor extends SchemaStatVisitor implements MySqlASTVisitor
{
    public MySqlSchemaStatVisitor() {
        super(DbType.mysql);
    }
    
    public MySqlSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public boolean visit(final SQLSelectStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        return true;
    }
    
    @Override
    public DbType getDbType() {
        return DbType.mysql;
    }
    
    @Override
    public boolean visit(final MySqlDeleteStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            from.accept(this);
        }
        final SQLTableSource using = x.getUsing();
        if (using != null) {
            using.accept(this);
        }
        final SQLTableSource tableSource = x.getTableSource();
        tableSource.accept(this);
        if (tableSource instanceof SQLExprTableSource) {
            final TableStat stat = this.getTableStat((SQLExprTableSource)tableSource);
            stat.incrementDeleteCount();
        }
        this.accept(x.getWhere());
        this.accept(x.getOrderBy());
        this.accept(x.getLimit());
        return false;
    }
    
    @Override
    public void endVisit(final MySqlInsertStatement x) {
        this.setModeOrigin(x);
    }
    
    @Override
    public boolean visit(final MySqlInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Insert);
        final TableStat stat = this.getTableStat(x.getTableSource());
        if (stat != null) {
            stat.incrementInsertCount();
        }
        this.accept(x.getColumns());
        this.accept(x.getValuesList());
        this.accept(x.getQuery());
        this.accept(x.getDuplicateKeyUpdate());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlTableIndex x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlKey x) {
        for (final SQLObject item : x.getColumns()) {
            item.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlPrimaryKey x) {
        for (final SQLSelectOrderByItem item : x.getColumns()) {
            final SQLExpr expr = item.getExpr();
            expr.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowColumnsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDatabaseStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateExternalCatalogStatement x) {
        return true;
    }
    
    @Override
    public boolean visit(final MySqlCreateUserStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlPartitionByKey x) {
        this.accept(x.getColumns());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdatePlanCacheStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPlanCacheStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlClearPlanCacheStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDisabledPlanCacheStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterDatabaseSetOption x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterDatabaseKillJob x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExplainPlanCacheStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlOutFileExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExplainStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLName tableName = x.getTableName();
        if (tableName != null) {
            this.getTableStat(tableName);
            final SQLName columnName = x.getColumnName();
            if (columnName != null) {
                this.addColumn(tableName, columnName.getSimpleName());
            }
        }
        if (x.getStatement() != null) {
            this.accept(x.getStatement());
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdateStatement x) {
        this.visit(x);
        for (final SQLExpr item : x.getReturning()) {
            item.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSetTransactionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowHMSMetaStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowAuthorsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBinaryLogsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMasterLogsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCollationStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBinLogEventsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCharacterSetStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowContributorsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateDatabaseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateEventStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateFunctionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateProcedureStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateTableStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowCreateTriggerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowEngineStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowEnginesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowErrorsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowEventsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowFunctionCodeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowFunctionStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowGrantsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUserName x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMasterStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowOpenTablesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPluginsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPartitionsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPrivilegesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProcedureCodeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProcedureStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProcessListStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProfileStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowProfilesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowRelayLogEventsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowRuleStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowRuleStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlaveHostsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSequencesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlowStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowSlaveStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTableStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowDbLockStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowHtcStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowStcStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTriggersStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTraceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowBroadcastsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDdlStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowDsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowTopologyStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowVariantsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameTableStatement.Item x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameTableStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUseIndexHint x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlIgnoreIndexHint x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLockTableStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLockTableStatement.Item x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUnlockTablesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlForceIndexHint x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableChangeColumn x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final SQLName table = stmt.getName();
        final SQLName column = x.getColumnName();
        final String columnName = column.toString();
        this.addColumn(table, columnName);
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableModifyColumn x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final SQLName table = stmt.getName();
        final SQLName column = x.getNewColumnDefinition().getName();
        final String columnName = column.toString();
        this.addColumn(table, columnName);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterCharacter x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableOption x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateTableStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final boolean val = super.visit(x);
        final SQLExpr union = x.getOption("union");
        if (union instanceof SQLListExpr) {
            for (final SQLExpr item : ((SQLListExpr)union).getItems()) {
                if (item instanceof SQLName) {
                    this.getTableStatWithUnwrap(item);
                }
            }
        }
        return val;
    }
    
    @Override
    public boolean visit(final MySqlHelpStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCharExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUnique x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableDiscardTablespace x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableImportTablespace x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateTableStatement.TableSpaceOption x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableAlterColumn x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableForce x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableLock x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableOrderBy x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableValidation x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCaseStatement x) {
        this.accept(x.getWhenList());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSelectIntoStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCaseStatement.MySqlWhenStatement x) {
        this.accept(x.getStatements());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlLeaveStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlIterateStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRepeatStatement x) {
        this.accept(x.getStatements());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCursorDeclareStatement x) {
        this.accept(x.getSelect());
        return false;
    }
    
    @Override
    public boolean visit(final MySqlUpdateTableSource x) {
        return x.getUpdate() != null && this.visit(x.getUpdate());
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByKey x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByList x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeclareHandlerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlDeclareConditionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlFlushStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlEventSchedule x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateEventStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateAddLogFileGroupStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateServerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCreateTableSpaceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterEventStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterLogFileGroupStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterServerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTablespaceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlChecksumTableStatement x) {
        return true;
    }
    
    @Override
    public boolean visit(final MySqlShowDatasourcesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowNodeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowHelpStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlFlashbackStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowConfigStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPlanCacheStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowPhysicalProcesslistStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRenameSequenceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlCheckTableStatement x) {
        for (final SQLExprTableSource tableSource : x.getTables()) {
            tableSource.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextCharFilterStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowFullTextStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlShowCreateFullTextStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlAlterFullTextStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlDropFullTextStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextTokenizerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextTokenFilterStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextAnalyzerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MysqlCreateFullTextDictionaryStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlAlterTableAlterFullTextIndex x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExecuteForAdsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlManageInstanceGroupStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRaftMemberChangeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlRaftLeaderTransferStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlMigrateStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowClusterNameStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowJobStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlShowMigrateTaskStatusStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlSubPartitionByValue x) {
        return false;
    }
    
    @Override
    public boolean visit(final MySqlExtPartition x) {
        return true;
    }
    
    @Override
    public boolean visit(final MySqlExtPartition.Item x) {
        return false;
    }
}
