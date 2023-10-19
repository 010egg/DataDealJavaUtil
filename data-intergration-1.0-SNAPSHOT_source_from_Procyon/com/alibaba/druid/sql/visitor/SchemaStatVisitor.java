// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLSyncMetaStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloneTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCopyFromStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableArchivePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLDumpStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPartitionRef;
import com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowIndexesStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLDropCatalogStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.statement.SQLShowViewsStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetOption;
import com.alibaba.druid.sql.ast.statement.SQLShowTableGroupsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLDropTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSynonymStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLDescribeStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
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
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLSubPartitionByHash;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloseStatement;
import com.alibaba.druid.sql.ast.statement.SQLRefreshMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.statement.SQLOpenStatement;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDefault;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLRevokeStatement;
import com.alibaba.druid.sql.ast.statement.SQLObjectType;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableMergePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import java.util.Collection;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLDbLinkExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.expr.SQLExprUtils;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.repository.SchemaObject;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.SQLOver;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlExpr;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import java.util.Set;
import java.util.Map;
import com.alibaba.druid.stat.TableStat;
import java.util.HashMap;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.repository.SchemaRepository;

public class SchemaStatVisitor extends SQLASTVisitorAdapter
{
    protected SchemaRepository repository;
    protected final List<SQLName> originalTables;
    protected final HashMap<TableStat.Name, TableStat> tableStats;
    protected final Map<Long, TableStat.Column> columns;
    protected final List<TableStat.Condition> conditions;
    protected final Set<TableStat.Relationship> relationships;
    protected final List<TableStat.Column> orderByColumns;
    protected final Set<TableStat.Column> groupByColumns;
    protected final List<SQLAggregateExpr> aggregateFunctions;
    protected final List<SQLMethodInvokeExpr> functions;
    private List<Object> parameters;
    private TableStat.Mode mode;
    protected DbType dbType;
    
    public SchemaStatVisitor() {
        this((DbType)null);
    }
    
    public SchemaStatVisitor(final SchemaRepository repository) {
        this.originalTables = new ArrayList<SQLName>();
        this.tableStats = new LinkedHashMap<TableStat.Name, TableStat>();
        this.columns = new LinkedHashMap<Long, TableStat.Column>();
        this.conditions = new ArrayList<TableStat.Condition>();
        this.relationships = new LinkedHashSet<TableStat.Relationship>();
        this.orderByColumns = new ArrayList<TableStat.Column>();
        this.groupByColumns = new LinkedHashSet<TableStat.Column>();
        this.aggregateFunctions = new ArrayList<SQLAggregateExpr>();
        this.functions = new ArrayList<SQLMethodInvokeExpr>(2);
        if (repository != null) {
            this.dbType = repository.getDbType();
        }
        this.repository = repository;
    }
    
    public SchemaStatVisitor(final DbType dbType) {
        this(new SchemaRepository(dbType), new ArrayList<Object>());
        this.dbType = dbType;
    }
    
    public SchemaStatVisitor(final List<Object> parameters) {
        this((DbType)null, parameters);
    }
    
    public SchemaStatVisitor(final DbType dbType, final List<Object> parameters) {
        this(new SchemaRepository(dbType), parameters);
        this.parameters = parameters;
    }
    
    public SchemaStatVisitor(final SchemaRepository repository, final List<Object> parameters) {
        this.originalTables = new ArrayList<SQLName>();
        this.tableStats = new LinkedHashMap<TableStat.Name, TableStat>();
        this.columns = new LinkedHashMap<Long, TableStat.Column>();
        this.conditions = new ArrayList<TableStat.Condition>();
        this.relationships = new LinkedHashSet<TableStat.Relationship>();
        this.orderByColumns = new ArrayList<TableStat.Column>();
        this.groupByColumns = new LinkedHashSet<TableStat.Column>();
        this.aggregateFunctions = new ArrayList<SQLAggregateExpr>();
        this.functions = new ArrayList<SQLMethodInvokeExpr>(2);
        this.repository = repository;
        this.parameters = parameters;
        if (repository != null) {
            final DbType dbType = repository.getDbType();
            if (dbType != null && this.dbType == null) {
                this.dbType = dbType;
            }
        }
    }
    
    public SchemaRepository getRepository() {
        return this.repository;
    }
    
    public void setRepository(final SchemaRepository repository) {
        this.repository = repository;
    }
    
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(final List<Object> parameters) {
        this.parameters = parameters;
    }
    
    public TableStat getTableStat(String tableName) {
        tableName = this.handleName(tableName);
        final TableStat.Name tableNameObj = new TableStat.Name(tableName);
        TableStat stat = this.tableStats.get(tableNameObj);
        if (stat == null) {
            stat = new TableStat();
            this.tableStats.put(new TableStat.Name(tableName), stat);
        }
        return stat;
    }
    
    public TableStat getTableStat(final SQLName tableName) {
        String strName;
        if (tableName instanceof SQLIdentifierExpr) {
            strName = ((SQLIdentifierExpr)tableName).normalizedName();
        }
        else if (tableName instanceof SQLPropertyExpr) {
            strName = ((SQLPropertyExpr)tableName).normalizedName();
        }
        else {
            strName = tableName.toString();
        }
        final long hashCode64 = tableName.hashCode64();
        if (hashCode64 == FnvHash.Constants.DUAL) {
            return null;
        }
        this.originalTables.add(tableName);
        final TableStat.Name tableNameObj = new TableStat.Name(strName, hashCode64);
        TableStat stat = this.tableStats.get(tableNameObj);
        if (stat == null) {
            stat = new TableStat();
            this.tableStats.put(new TableStat.Name(strName, hashCode64), stat);
        }
        return stat;
    }
    
    protected TableStat.Column addColumn(final String tableName, final String columnName) {
        final TableStat.Column c = new TableStat.Column(tableName, columnName, this.dbType);
        TableStat.Column column = this.columns.get(c.hashCode64());
        if (column == null && columnName != null) {
            column = c;
            this.columns.put(c.hashCode64(), c);
        }
        return column;
    }
    
    protected TableStat.Column addColumn(final SQLName table, final String columnName) {
        String tableName;
        if (table instanceof SQLIdentifierExpr) {
            tableName = ((SQLIdentifierExpr)table).normalizedName();
        }
        else if (table instanceof SQLPropertyExpr) {
            tableName = ((SQLPropertyExpr)table).normalizedName();
        }
        else {
            tableName = table.toString();
        }
        long basic;
        final long tableHashCode64 = basic = table.hashCode64();
        basic ^= 0x2EL;
        basic *= 1099511628211L;
        final long columnHashCode64 = FnvHash.hashCode64(basic, columnName);
        TableStat.Column column = this.columns.get(columnHashCode64);
        if (column == null && columnName != null) {
            column = new TableStat.Column(tableName, columnName, columnHashCode64);
            this.columns.put(columnHashCode64, column);
        }
        return column;
    }
    
    private String handleName(String ident) {
        final int len = ident.length();
        if (ident.charAt(0) == '[' && ident.charAt(len - 1) == ']') {
            ident = ident.substring(1, len - 1);
        }
        else {
            boolean flag0 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            for (int i = 0; i < len; ++i) {
                final char ch = ident.charAt(i);
                if (ch == '\"') {
                    flag0 = true;
                }
                else if (ch == '`') {
                    flag2 = true;
                }
                else if (ch == ' ') {
                    flag3 = true;
                }
                else if (ch == '\'') {
                    flag4 = true;
                }
            }
            if (flag0) {
                ident = ident.replaceAll("\"", "");
            }
            if (flag2) {
                ident = ident.replaceAll("`", "");
            }
            if (flag3) {
                ident = ident.replaceAll(" ", "");
            }
            if (flag4) {
                ident = ident.replaceAll("'", "");
            }
        }
        return ident;
    }
    
    protected TableStat.Mode getMode() {
        return this.mode;
    }
    
    protected void setModeOrigin(final SQLObject x) {
        final TableStat.Mode originalMode = (TableStat.Mode)x.getAttribute("_original_use_mode");
        this.mode = originalMode;
    }
    
    protected TableStat.Mode setMode(final SQLObject x, final TableStat.Mode mode) {
        final TableStat.Mode oldMode = this.mode;
        x.putAttribute("_original_use_mode", oldMode);
        this.mode = mode;
        return oldMode;
    }
    
    private boolean visitOrderBy(SQLIdentifierExpr x) {
        final SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource == null && x.getParent() instanceof SQLSelectOrderByItem && x.getParent().getParent() instanceof SQLOrderBy && x.getParent().getParent().getParent() instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x.getParent().getParent().getParent();
            final SQLSelectItem selectItem = queryBlock.findSelectItem(x.nameHashCode64());
            if (selectItem != null) {
                final SQLExpr selectItemExpr = selectItem.getExpr();
                if (!(selectItemExpr instanceof SQLIdentifierExpr)) {
                    return selectItemExpr instanceof SQLPropertyExpr && this.visitOrderBy((SQLPropertyExpr)selectItemExpr);
                }
                x = (SQLIdentifierExpr)selectItemExpr;
            }
        }
        String tableName = null;
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)tableSource).getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr table = (SQLIdentifierExpr)expr;
                tableName = table.getName();
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr table2 = (SQLPropertyExpr)expr;
                tableName = table2.toString();
            }
            else if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                if ("table".equalsIgnoreCase(methodInvokeExpr.getMethodName()) && methodInvokeExpr.getArguments().size() == 1 && methodInvokeExpr.getArguments().get(0) instanceof SQLName) {
                    final SQLName table3 = methodInvokeExpr.getArguments().get(0);
                    if (table3 instanceof SQLPropertyExpr) {
                        final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)table3;
                        final SQLIdentifierExpr owner = (SQLIdentifierExpr)propertyExpr.getOwner();
                        if (propertyExpr.getResolvedTableSource() != null && propertyExpr.getResolvedTableSource() instanceof SQLExprTableSource) {
                            final SQLExpr resolveExpr = ((SQLExprTableSource)propertyExpr.getResolvedTableSource()).getExpr();
                            if (resolveExpr instanceof SQLName) {
                                tableName = resolveExpr.toString() + "." + propertyExpr.getName();
                            }
                        }
                    }
                    if (tableName == null) {
                        tableName = table3.toString();
                    }
                }
            }
        }
        else {
            if (tableSource instanceof SQLWithSubqueryClause.Entry) {
                return false;
            }
            if (tableSource instanceof SQLSubqueryTableSource) {
                final SQLSelectQueryBlock queryBlock2 = ((SQLSubqueryTableSource)tableSource).getSelect().getQueryBlock();
                if (queryBlock2 == null) {
                    return false;
                }
                final SQLSelectItem selectItem2 = queryBlock2.findSelectItem(x.nameHashCode64());
                if (selectItem2 == null) {
                    return false;
                }
                final SQLExpr selectItemExpr2 = selectItem2.getExpr();
                SQLTableSource columnTableSource = null;
                if (selectItemExpr2 instanceof SQLIdentifierExpr) {
                    columnTableSource = ((SQLIdentifierExpr)selectItemExpr2).getResolvedTableSource();
                }
                else if (selectItemExpr2 instanceof SQLPropertyExpr) {
                    columnTableSource = ((SQLPropertyExpr)selectItemExpr2).getResolvedTableSource();
                }
                if (columnTableSource instanceof SQLExprTableSource && ((SQLExprTableSource)columnTableSource).getExpr() instanceof SQLName) {
                    final SQLName tableExpr = (SQLName)((SQLExprTableSource)columnTableSource).getExpr();
                    if (tableExpr instanceof SQLIdentifierExpr) {
                        tableName = ((SQLIdentifierExpr)tableExpr).normalizedName();
                    }
                    else if (tableExpr instanceof SQLPropertyExpr) {
                        tableName = ((SQLPropertyExpr)tableExpr).normalizedName();
                    }
                }
            }
            else {
                boolean skip = false;
                for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                    if (parent instanceof SQLSelectQueryBlock) {
                        final SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                        if (from instanceof SQLValuesTableSource) {
                            skip = true;
                            break;
                        }
                    }
                    else if (parent instanceof SQLSelectQuery) {
                        break;
                    }
                }
            }
        }
        final String identName = x.getName();
        if (tableName != null) {
            this.orderByAddColumn(tableName, identName, x);
        }
        else {
            this.orderByAddColumn("UNKNOWN", identName, x);
        }
        return false;
    }
    
    private boolean visitOrderBy(final SQLPropertyExpr x) {
        if (this.isSubQueryOrParamOrVariant(x)) {
            return false;
        }
        String owner = null;
        final SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr tableSourceExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableSourceExpr instanceof SQLName) {
                owner = tableSourceExpr.toString();
            }
        }
        if (owner == null && x.getOwner() instanceof SQLIdentifierExpr) {
            owner = ((SQLIdentifierExpr)x.getOwner()).getName();
        }
        if (owner == null) {
            return false;
        }
        this.orderByAddColumn(owner, x.getName(), x);
        return false;
    }
    
    private boolean visitOrderBy(final SQLIntegerExpr x) {
        final SQLObject parent = x.getParent();
        if (!(parent instanceof SQLSelectOrderByItem)) {
            return false;
        }
        if (parent.getParent() instanceof SQLSelectQueryBlock) {
            final int selectItemIndex = x.getNumber().intValue() - 1;
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent.getParent();
            final List<SQLSelectItem> selectList = queryBlock.getSelectList();
            if (selectItemIndex < 0 || selectItemIndex >= selectList.size()) {
                return false;
            }
            final SQLExpr selectItemExpr = selectList.get(selectItemIndex).getExpr();
            if (selectItemExpr instanceof SQLIdentifierExpr) {
                this.visitOrderBy((SQLIdentifierExpr)selectItemExpr);
            }
            else if (selectItemExpr instanceof SQLPropertyExpr) {
                this.visitOrderBy((SQLPropertyExpr)selectItemExpr);
            }
        }
        return false;
    }
    
    private void orderByAddColumn(final String table, final String columnName, final SQLObject expr) {
        final TableStat.Column column = new TableStat.Column(table, columnName, this.dbType);
        final SQLObject parent = expr.getParent();
        if (parent instanceof SQLSelectOrderByItem) {
            final SQLOrderingSpecification type = ((SQLSelectOrderByItem)parent).getType();
            column.getAttributes().put("orderBy.type", type);
        }
        this.orderByColumns.add(column);
    }
    
    @Override
    public boolean visit(final SQLOrderBy x) {
        final SQLASTVisitor orderByVisitor = this.createOrderByVisitor(x);
        SQLSelectQueryBlock query = null;
        if (x.getParent() instanceof SQLSelectQueryBlock) {
            query = (SQLSelectQueryBlock)x.getParent();
        }
        if (query != null) {
            for (final SQLSelectOrderByItem item : x.getItems()) {
                final SQLExpr expr = item.getExpr();
                if (expr instanceof SQLIntegerExpr) {
                    final int intValue = ((SQLIntegerExpr)expr).getNumber().intValue() - 1;
                    if (intValue >= query.getSelectList().size()) {
                        continue;
                    }
                    final SQLExpr selectItemExpr = query.getSelectList().get(intValue).getExpr();
                    selectItemExpr.accept(orderByVisitor);
                }
                else {
                    if (expr instanceof MySqlExpr) {
                        continue;
                    }
                    if (expr instanceof OracleExpr) {
                        continue;
                    }
                    continue;
                }
            }
        }
        x.accept(orderByVisitor);
        for (final SQLSelectOrderByItem orderByItem : x.getItems()) {
            this.statExpr(orderByItem.getExpr());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLOver x) {
        final SQLName of = x.getOf();
        final SQLOrderBy orderBy = x.getOrderBy();
        final List<SQLExpr> partitionBy = x.getPartitionBy();
        if (of == null && orderBy != null) {
            orderBy.accept(this);
        }
        if (partitionBy != null) {
            for (final SQLExpr expr : partitionBy) {
                expr.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLWindow x) {
        final SQLOver over = x.getOver();
        if (over != null) {
            this.visit(over);
        }
        return false;
    }
    
    protected SQLASTVisitor createOrderByVisitor(final SQLOrderBy x) {
        if (this.dbType == null) {
            this.dbType = DbType.other;
        }
        switch (this.dbType) {
            case mysql: {
                return new MySqlOrderByStatVisitor(x);
            }
            case postgresql: {
                return new PGOrderByStatVisitor(x);
            }
            case oracle: {
                return new OracleOrderByStatVisitor(x);
            }
            default: {
                return new OrderByStatVisitor(x);
            }
        }
    }
    
    public Set<TableStat.Relationship> getRelationships() {
        return this.relationships;
    }
    
    public List<TableStat.Column> getOrderByColumns() {
        return this.orderByColumns;
    }
    
    public Set<TableStat.Column> getGroupByColumns() {
        return this.groupByColumns;
    }
    
    public List<TableStat.Condition> getConditions() {
        return this.conditions;
    }
    
    public List<SQLAggregateExpr> getAggregateFunctions() {
        return this.aggregateFunctions;
    }
    
    @Override
    public boolean visit(final SQLBetweenExpr x) {
        final SQLObject parent = x.getParent();
        final SQLExpr test = x.getTestExpr();
        final SQLExpr begin = x.getBeginExpr();
        final SQLExpr end = x.getEndExpr();
        this.statExpr(test);
        this.statExpr(begin);
        this.statExpr(end);
        this.handleCondition(test, "BETWEEN", begin, end);
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLIfStatement) {
            return true;
        }
        final SQLBinaryOperator op = x.getOperator();
        final SQLExpr left = x.getLeft();
        final SQLExpr right = x.getRight();
        if ((op == SQLBinaryOperator.BooleanAnd || op == SQLBinaryOperator.BooleanOr) && left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)left).getOperator() == op) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x, op);
            for (int i = 0; i < groupList.size(); ++i) {
                final SQLExpr item = groupList.get(i);
                item.accept(this);
            }
            return false;
        }
        switch (op) {
            case LessThan:
            case LessThanOrEqual:
            case GreaterThan:
            case GreaterThanOrEqual:
            case Equality:
            case NotEqual:
            case LessThanOrGreater:
            case LessThanOrEqualOrGreaterThan:
            case SoudsLike:
            case Like:
            case NotLike:
            case Is:
            case IsNot: {
                this.handleCondition(left, op.name, right);
                String reverseOp = op.name;
                switch (op) {
                    case LessThan: {
                        reverseOp = SQLBinaryOperator.GreaterThan.name;
                        break;
                    }
                    case LessThanOrEqual: {
                        reverseOp = SQLBinaryOperator.GreaterThanOrEqual.name;
                        break;
                    }
                    case GreaterThan: {
                        reverseOp = SQLBinaryOperator.LessThan.name;
                        break;
                    }
                    case GreaterThanOrEqual: {
                        reverseOp = SQLBinaryOperator.LessThanOrEqual.name;
                        break;
                    }
                }
                this.handleCondition(right, reverseOp, left);
                this.handleRelationship(left, op.name, right);
                break;
            }
            case BooleanOr: {
                final List<SQLExpr> list = SQLBinaryOpExpr.split(x, op);
                for (final SQLExpr item2 : list) {
                    if (item2 instanceof SQLBinaryOpExpr) {
                        this.visit((SQLBinaryOpExpr)item2);
                    }
                    else {
                        item2.accept(this);
                    }
                }
                return false;
            }
            case Modulus: {
                if (!(right instanceof SQLIdentifierExpr)) {
                    break;
                }
                final long hashCode64 = ((SQLIdentifierExpr)right).hashCode64();
                if (hashCode64 == FnvHash.Constants.ISOPEN) {
                    left.accept(this);
                    return false;
                }
                break;
            }
        }
        if (left instanceof SQLBinaryOpExpr) {
            this.visit((SQLBinaryOpExpr)left);
        }
        else {
            this.statExpr(left);
        }
        this.statExpr(right);
        return false;
    }
    
    protected void handleRelationship(final SQLExpr left, final String operator, final SQLExpr right) {
        final TableStat.Column leftColumn = this.getColumn(left);
        if (leftColumn == null) {
            return;
        }
        final TableStat.Column rightColumn = this.getColumn(right);
        if (rightColumn == null) {
            return;
        }
        final TableStat.Relationship relationship = new TableStat.Relationship(leftColumn, rightColumn, operator);
        this.relationships.add(relationship);
    }
    
    protected void handleCondition(final SQLExpr expr, final String operator, final List<SQLExpr> values) {
        this.handleCondition(expr, operator, (SQLExpr[])values.toArray(new SQLExpr[values.size()]));
    }
    
    protected void handleCondition(SQLExpr expr, final String operator, final SQLExpr... valueExprs) {
        if (expr instanceof SQLCastExpr) {
            expr = ((SQLCastExpr)expr).getExpr();
        }
        else if (expr instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)expr;
            final List<SQLExpr> arguments = func.getArguments();
            if (func.methodNameHashCode64() == FnvHash.Constants.COALESCE && arguments.size() > 0) {
                boolean allLiteral = true;
                for (int i = 1; i < arguments.size(); ++i) {
                    final SQLExpr arg = arguments.get(i);
                    if (!(arg instanceof SQLLiteralExpr)) {
                        allLiteral = false;
                    }
                }
                if (allLiteral) {
                    expr = arguments.get(0);
                }
            }
        }
        final TableStat.Column column = this.getColumn(expr);
        if (column == null && expr instanceof SQLBinaryOpExpr && valueExprs.length == 1 && valueExprs[0] instanceof SQLLiteralExpr) {
            final SQLBinaryOpExpr left = (SQLBinaryOpExpr)expr;
            final SQLLiteralExpr right = (SQLLiteralExpr)valueExprs[0];
            if (left.getRight() instanceof SQLIntegerExpr && right instanceof SQLIntegerExpr) {
                final long v0 = ((SQLIntegerExpr)left.getRight()).getNumber().longValue();
                final long v2 = ((SQLIntegerExpr)right).getNumber().longValue();
                final SQLBinaryOperator op = left.getOperator();
                long v3 = 0L;
                switch (op) {
                    case Add: {
                        v3 = v2 - v0;
                        break;
                    }
                    case Subtract: {
                        v3 = v2 + v0;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.handleCondition(left.getLeft(), operator, new SQLIntegerExpr(v3));
                return;
            }
        }
        if (column == null) {
            return;
        }
        TableStat.Condition condition = null;
        for (final TableStat.Condition item : this.getConditions()) {
            if (item.getColumn().equals(column) && item.getOperator().equals(operator)) {
                condition = item;
                break;
            }
        }
        if (condition == null) {
            condition = new TableStat.Condition(column, operator);
            this.conditions.add(condition);
        }
        for (SQLExpr item2 : valueExprs) {
            final TableStat.Column valueColumn = this.getColumn(item2);
            if (valueColumn == null) {
                if (item2 instanceof SQLCastExpr) {
                    item2 = ((SQLCastExpr)item2).getExpr();
                }
                Object value;
                if (item2 instanceof SQLMethodInvokeExpr || item2 instanceof SQLCurrentTimeExpr) {
                    value = item2.toString();
                }
                else {
                    value = SQLEvalVisitorUtils.eval(this.dbType, item2, this.parameters, false);
                    if (value == SQLEvalVisitor.EVAL_VALUE_NULL) {
                        value = null;
                    }
                }
                condition.addValue(value);
            }
        }
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    protected TableStat.Column getColumn(SQLExpr expr) {
        final SQLExpr original = expr;
        expr = this.unwrapExpr(expr);
        if (expr instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
            final SQLExpr owner = propertyExpr.getOwner();
            final String column = SQLUtils.normalize(propertyExpr.getName());
            if (owner instanceof SQLName) {
                SQLName table = (SQLName)owner;
                final SQLObject resolvedOwnerObject = propertyExpr.getResolvedOwnerObject();
                if (resolvedOwnerObject instanceof SQLSubqueryTableSource || resolvedOwnerObject instanceof SQLCreateProcedureStatement || resolvedOwnerObject instanceof SQLCreateFunctionStatement) {
                    table = null;
                }
                if (resolvedOwnerObject instanceof SQLExprTableSource) {
                    final SQLExpr tableSourceExpr = ((SQLExprTableSource)resolvedOwnerObject).getExpr();
                    if (tableSourceExpr instanceof SQLName) {
                        table = (SQLName)tableSourceExpr;
                    }
                }
                else if (resolvedOwnerObject instanceof SQLValuesTableSource) {
                    return null;
                }
                if (table != null) {
                    String tableName;
                    if (table instanceof SQLIdentifierExpr) {
                        tableName = ((SQLIdentifierExpr)table).normalizedName();
                    }
                    else if (table instanceof SQLPropertyExpr) {
                        tableName = ((SQLPropertyExpr)table).normalizedName();
                    }
                    else {
                        tableName = table.toString();
                    }
                    long tableHashCode64 = table.hashCode64();
                    if (resolvedOwnerObject instanceof SQLExprTableSource) {
                        final SchemaObject schemaObject = ((SQLExprTableSource)resolvedOwnerObject).getSchemaObject();
                        if (schemaObject != null && schemaObject.getStatement() instanceof SQLCreateTableStatement) {
                            final SQLColumnDefinition columnDef = schemaObject.findColumn(propertyExpr.nameHashCode64());
                            if (columnDef == null) {
                                tableName = "UNKNOWN";
                                tableHashCode64 = FnvHash.Constants.UNKNOWN;
                            }
                        }
                    }
                    long basic = tableHashCode64;
                    basic ^= 0x2EL;
                    basic *= 1099511628211L;
                    final long columnHashCode64 = FnvHash.hashCode64(basic, column);
                    TableStat.Column columnObj = this.columns.get(columnHashCode64);
                    if (columnObj == null) {
                        columnObj = new TableStat.Column(tableName, column, columnHashCode64);
                        if (!(resolvedOwnerObject instanceof SQLSubqueryTableSource) && !(resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry)) {
                            this.columns.put(columnHashCode64, columnObj);
                        }
                    }
                    return columnObj;
                }
            }
            return null;
        }
        if (!(expr instanceof SQLIdentifierExpr)) {
            if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                final List<SQLExpr> arguments = methodInvokeExpr.getArguments();
                final long nameHash = methodInvokeExpr.methodNameHashCode64();
                if (nameHash == FnvHash.Constants.DATE_FORMAT && arguments.size() == 2 && arguments.get(0) instanceof SQLName && arguments.get(1) instanceof SQLCharExpr) {
                    return this.getColumn(arguments.get(0));
                }
            }
            return null;
        }
        final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
        if (identifierExpr.getResolvedParameter() != null) {
            return null;
        }
        if (identifierExpr.getResolvedTableSource() instanceof SQLSubqueryTableSource) {
            return null;
        }
        if (identifierExpr.getResolvedDeclareItem() != null || identifierExpr.getResolvedParameter() != null) {
            return null;
        }
        final String column2 = identifierExpr.getName();
        SQLName table2 = null;
        final SQLTableSource tableSource = identifierExpr.getResolvedTableSource();
        if (tableSource instanceof SQLExprTableSource) {
            SQLExpr tableSourceExpr2 = ((SQLExprTableSource)tableSource).getExpr();
            if (tableSourceExpr2 != null && !(tableSourceExpr2 instanceof SQLName)) {
                tableSourceExpr2 = this.unwrapExpr(tableSourceExpr2);
            }
            if (tableSourceExpr2 instanceof SQLName) {
                table2 = (SQLName)tableSourceExpr2;
            }
        }
        if (table2 == null) {
            return new TableStat.Column("UNKNOWN", column2);
        }
        long basic2;
        final long tableHashCode65 = basic2 = table2.hashCode64();
        basic2 ^= 0x2EL;
        basic2 *= 1099511628211L;
        final long columnHashCode65 = FnvHash.hashCode64(basic2, column2);
        final TableStat.Column old = this.columns.get(columnHashCode65);
        if (old != null) {
            return old;
        }
        return new TableStat.Column(table2.toString(), column2, columnHashCode65);
    }
    
    private SQLExpr unwrapExpr(SQLExpr expr) {
        final SQLExpr original = expr;
        for (int i = 0; i <= 1000; ++i) {
            if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExp = (SQLMethodInvokeExpr)expr;
                if (methodInvokeExp.getArguments().size() == 1) {
                    final SQLExpr firstExpr = expr = methodInvokeExp.getArguments().get(0);
                    continue;
                }
            }
            if (!(expr instanceof SQLCastExpr)) {
                if (expr instanceof SQLPropertyExpr) {
                    SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                    final SQLTableSource resolvedTableSource = propertyExpr.getResolvedTableSource();
                    if (resolvedTableSource instanceof SQLSubqueryTableSource) {
                        final SQLSelect select = ((SQLSubqueryTableSource)resolvedTableSource).getSelect();
                        final SQLSelectQueryBlock queryBlock = select.getFirstQueryBlock();
                        if (queryBlock != null) {
                            if (queryBlock.getGroupBy() != null && original.getParent() instanceof SQLBinaryOpExpr) {
                                final SQLExpr other = ((SQLBinaryOpExpr)original.getParent()).other(original);
                                if (!SQLExprUtils.isLiteralExpr(other)) {
                                    return expr;
                                }
                            }
                            final SQLSelectItem selectItem = queryBlock.findSelectItem(propertyExpr.nameHashCode64());
                            if (selectItem != null) {
                                SQLExpr selectItemExpr = selectItem.getExpr();
                                if (selectItemExpr instanceof SQLMethodInvokeExpr && ((SQLMethodInvokeExpr)selectItemExpr).getArguments().size() == 1) {
                                    selectItemExpr = ((SQLMethodInvokeExpr)selectItemExpr).getArguments().get(0);
                                }
                                if (selectItemExpr != expr) {
                                    expr = selectItemExpr;
                                    continue;
                                }
                            }
                            else if (queryBlock.selectItemHasAllColumn()) {
                                SQLTableSource allColumnTableSource = null;
                                final SQLTableSource from = queryBlock.getFrom();
                                if (from instanceof SQLJoinTableSource) {
                                    final SQLSelectItem allColumnSelectItem = queryBlock.findAllColumnSelectItem();
                                    if (allColumnSelectItem != null && allColumnSelectItem.getExpr() instanceof SQLPropertyExpr) {
                                        final SQLExpr owner = ((SQLPropertyExpr)allColumnSelectItem.getExpr()).getOwner();
                                        if (owner instanceof SQLName) {
                                            allColumnTableSource = from.findTableSource(((SQLName)owner).nameHashCode64());
                                        }
                                    }
                                }
                                else {
                                    allColumnTableSource = from;
                                }
                                if (allColumnTableSource != null) {
                                    propertyExpr = propertyExpr.clone();
                                    propertyExpr.setResolvedTableSource(allColumnTableSource);
                                    if (allColumnTableSource instanceof SQLExprTableSource) {
                                        propertyExpr.setOwner(((SQLExprTableSource)allColumnTableSource).getExpr().clone());
                                    }
                                    expr = propertyExpr;
                                    continue;
                                }
                            }
                        }
                    }
                    else if (resolvedTableSource instanceof SQLExprTableSource) {
                        final SQLExprTableSource exprTableSource = (SQLExprTableSource)resolvedTableSource;
                        if (exprTableSource.getSchemaObject() == null) {
                            SQLTableSource redirectTableSource = null;
                            final SQLExpr tableSourceExpr = exprTableSource.getExpr();
                            if (tableSourceExpr instanceof SQLIdentifierExpr) {
                                redirectTableSource = ((SQLIdentifierExpr)tableSourceExpr).getResolvedTableSource();
                            }
                            else if (tableSourceExpr instanceof SQLPropertyExpr) {
                                redirectTableSource = ((SQLPropertyExpr)tableSourceExpr).getResolvedTableSource();
                            }
                            if (redirectTableSource == resolvedTableSource) {
                                redirectTableSource = null;
                            }
                            if (redirectTableSource != null) {
                                propertyExpr = propertyExpr.clone();
                                if (redirectTableSource instanceof SQLExprTableSource) {
                                    propertyExpr.setOwner(((SQLExprTableSource)redirectTableSource).getExpr().clone());
                                }
                                propertyExpr.setResolvedTableSource(redirectTableSource);
                                expr = propertyExpr;
                                continue;
                            }
                            propertyExpr = propertyExpr.clone();
                            propertyExpr.setOwner(tableSourceExpr);
                            expr = propertyExpr;
                        }
                    }
                }
                return expr;
            }
            expr = ((SQLCastExpr)expr).getExpr();
        }
        return null;
    }
    
    @Override
    public boolean visit(final SQLTruncateStatement x) {
        this.setMode(x, TableStat.Mode.Delete);
        for (final SQLExprTableSource tableSource : x.getTableSources()) {
            final SQLName name = (SQLName)tableSource.getExpr();
            final TableStat stat = this.getTableStat(name);
            stat.incrementDeleteCount();
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropViewStatement x) {
        this.setMode(x, TableStat.Mode.Drop);
        return true;
    }
    
    @Override
    public boolean visit(final SQLDropTableStatement x) {
        this.setMode(x, TableStat.Mode.Insert);
        for (final SQLExprTableSource tableSource : x.getTableSources()) {
            final SQLName name = (SQLName)tableSource.getExpr();
            final TableStat stat = this.getTableStat(name);
            stat.incrementDropCount();
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Insert);
        if (x.getTableName() instanceof SQLName) {
            final String ident = x.getTableName().toString();
            final TableStat stat = this.getTableStat(x.getTableName());
            stat.incrementInsertCount();
        }
        this.accept(x.getColumns());
        this.accept(x.getQuery());
        return false;
    }
    
    protected static void putAliasMap(final Map<String, String> aliasMap, final String name, final String value) {
        if (aliasMap == null || name == null) {
            return;
        }
        aliasMap.put(name.toLowerCase(), value);
    }
    
    protected void accept(final SQLObject x) {
        if (x != null) {
            x.accept(this);
        }
    }
    
    protected void accept(final List<? extends SQLObject> nodes) {
        for (int i = 0, size = nodes.size(); i < size; ++i) {
            this.accept((SQLObject)nodes.get(i));
        }
    }
    
    @Override
    public boolean visit(final SQLSelectQueryBlock x) {
        SQLTableSource from = x.getFrom();
        this.setMode(x, TableStat.Mode.Select);
        boolean isHiveMultiInsert = false;
        if (from == null) {
            isHiveMultiInsert = (x.getParent() != null && x.getParent().getParent() instanceof HiveInsert && x.getParent().getParent().getParent() instanceof HiveMultiInsertStatement);
            if (isHiveMultiInsert) {
                from = ((HiveMultiInsertStatement)x.getParent().getParent().getParent()).getFrom();
            }
        }
        if (from == null) {
            for (final SQLSelectItem selectItem : x.getSelectList()) {
                this.statExpr(selectItem.getExpr());
            }
            return false;
        }
        if (from != null) {
            from.accept(this);
        }
        final SQLExprTableSource into = x.getInto();
        if (into != null && into.getExpr() instanceof SQLName) {
            final SQLName intoExpr = (SQLName)into.getExpr();
            final boolean isParam = intoExpr instanceof SQLIdentifierExpr && isParam((SQLIdentifierExpr)intoExpr);
            if (!isParam) {
                final TableStat stat = this.getTableStat(intoExpr);
                if (stat != null) {
                    stat.incrementInsertCount();
                }
            }
            into.accept(this);
        }
        for (final SQLSelectItem selectItem2 : x.getSelectList()) {
            if (selectItem2.getClass() == SQLSelectItem.class) {
                this.statExpr(selectItem2.getExpr());
            }
            else {
                selectItem2.accept(this);
            }
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.statExpr(where);
        }
        final SQLExpr startWith = x.getStartWith();
        if (startWith != null) {
            this.statExpr(startWith);
        }
        final SQLExpr connectBy = x.getConnectBy();
        if (connectBy != null) {
            this.statExpr(connectBy);
        }
        final SQLSelectGroupByClause groupBy = x.getGroupBy();
        if (groupBy != null) {
            for (final SQLExpr expr : groupBy.getItems()) {
                this.statExpr(expr);
            }
        }
        final List<SQLWindow> windows = x.getWindows();
        if (windows != null && windows.size() > 0) {
            for (final SQLWindow window : windows) {
                window.accept(this);
            }
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.visit(orderBy);
        }
        final SQLExpr first = x.getFirst();
        if (first != null) {
            this.statExpr(first);
        }
        final List<SQLSelectOrderByItem> distributeBy = x.getDistributeBy();
        if (distributeBy != null) {
            for (final SQLSelectOrderByItem item : distributeBy) {
                this.statExpr(item.getExpr());
            }
        }
        final List<SQLSelectOrderByItem> sortBy = x.getSortBy();
        if (sortBy != null) {
            for (final SQLSelectOrderByItem orderByItem : sortBy) {
                this.statExpr(orderByItem.getExpr());
            }
        }
        for (final SQLExpr expr2 : x.getForUpdateOf()) {
            this.statExpr(expr2);
        }
        return false;
    }
    
    private static boolean isParam(final SQLIdentifierExpr x) {
        return x.getResolvedParameter() != null || x.getResolvedDeclareItem() != null;
    }
    
    @Override
    public void endVisit(final SQLSelectQueryBlock x) {
        this.setModeOrigin(x);
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource x) {
        final SQLTableSource left = x.getLeft();
        final SQLTableSource right = x.getRight();
        left.accept(this);
        right.accept(this);
        final SQLExpr condition = x.getCondition();
        if (condition != null) {
            condition.accept(this);
        }
        if (x.getUsing().size() > 0 && left instanceof SQLExprTableSource && right instanceof SQLExprTableSource) {
            final SQLExpr leftExpr = ((SQLExprTableSource)left).getExpr();
            final SQLExpr rightExpr = ((SQLExprTableSource)right).getExpr();
            for (final SQLExpr expr : x.getUsing()) {
                if (expr instanceof SQLIdentifierExpr) {
                    final String name = ((SQLIdentifierExpr)expr).getName();
                    final SQLPropertyExpr leftPropExpr = new SQLPropertyExpr(leftExpr, name);
                    final SQLPropertyExpr rightPropExpr = new SQLPropertyExpr(rightExpr, name);
                    leftPropExpr.setResolvedTableSource(left);
                    rightPropExpr.setResolvedTableSource(right);
                    final SQLBinaryOpExpr usingCondition = new SQLBinaryOpExpr(leftPropExpr, SQLBinaryOperator.Equality, rightPropExpr);
                    usingCondition.accept(this);
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        TableStat.Column column = null;
        final String ident = SQLUtils.normalize(x.getName());
        SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource instanceof SQLSubqueryTableSource) {
            final SQLSelect subSelect = ((SQLSubqueryTableSource)tableSource).getSelect();
            final SQLSelectQueryBlock subQuery = subSelect.getQueryBlock();
            if (subQuery != null) {
                final SQLTableSource subTableSource = subQuery.findTableSourceWithColumn(x.nameHashCode64());
                if (subTableSource != null) {
                    tableSource = subTableSource;
                }
            }
        }
        if (tableSource instanceof SQLExprTableSource) {
            SQLExpr expr = ((SQLExprTableSource)tableSource).getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr table = (SQLIdentifierExpr)expr;
                final SQLTableSource resolvedTableSource = table.getResolvedTableSource();
                if (resolvedTableSource instanceof SQLExprTableSource) {
                    expr = ((SQLExprTableSource)resolvedTableSource).getExpr();
                }
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr table2 = (SQLPropertyExpr)expr;
                final SQLTableSource resolvedTableSource = table2.getResolvedTableSource();
                if (resolvedTableSource instanceof SQLExprTableSource) {
                    expr = ((SQLExprTableSource)resolvedTableSource).getExpr();
                }
            }
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr table = (SQLIdentifierExpr)expr;
                final SQLTableSource resolvedTableSource = table.getResolvedTableSource();
                if (resolvedTableSource instanceof SQLWithSubqueryClause.Entry) {
                    return false;
                }
                final String tableName = table.getName();
                final SchemaObject schemaObject = ((SQLExprTableSource)tableSource).getSchemaObject();
                if (schemaObject != null && schemaObject.getStatement() instanceof SQLCreateTableStatement && !"*".equals(ident)) {
                    final SQLColumnDefinition columnDef = schemaObject.findColumn(x.nameHashCode64());
                    if (columnDef == null) {
                        column = this.addColumn("UNKNOWN", ident);
                    }
                }
                if (column == null) {
                    column = this.addColumn(table, ident);
                }
                if (this.isParentGroupBy(x)) {
                    this.groupByColumns.add(column);
                }
            }
            else if (expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr table2 = (SQLPropertyExpr)expr;
                column = this.addColumn(table2, ident);
                if (column != null && this.isParentGroupBy(x)) {
                    this.groupByColumns.add(column);
                }
            }
            else if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                if ("table".equalsIgnoreCase(methodInvokeExpr.getMethodName()) && methodInvokeExpr.getArguments().size() == 1 && methodInvokeExpr.getArguments().get(0) instanceof SQLName) {
                    final SQLName table3 = methodInvokeExpr.getArguments().get(0);
                    String tableName = null;
                    if (table3 instanceof SQLPropertyExpr) {
                        final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)table3;
                        final SQLIdentifierExpr owner = (SQLIdentifierExpr)propertyExpr.getOwner();
                        if (propertyExpr.getResolvedTableSource() != null && propertyExpr.getResolvedTableSource() instanceof SQLExprTableSource) {
                            final SQLExpr resolveExpr = ((SQLExprTableSource)propertyExpr.getResolvedTableSource()).getExpr();
                            if (resolveExpr instanceof SQLName) {
                                tableName = resolveExpr.toString() + "." + propertyExpr.getName();
                            }
                        }
                    }
                    if (tableName == null) {
                        tableName = table3.toString();
                    }
                    column = this.addColumn(tableName, ident);
                }
            }
        }
        else {
            if (tableSource instanceof SQLWithSubqueryClause.Entry || tableSource instanceof SQLSubqueryTableSource || tableSource instanceof SQLUnionQueryTableSource || tableSource instanceof SQLValuesTableSource || tableSource instanceof SQLLateralViewTableSource) {
                return false;
            }
            if (x.getResolvedProcudure() != null) {
                return false;
            }
            if (x.getResolvedOwnerObject() instanceof SQLParameter) {
                return false;
            }
            boolean skip = false;
            for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelectQueryBlock) {
                    final SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                    if (from instanceof SQLValuesTableSource) {
                        skip = true;
                        break;
                    }
                }
                else if (parent instanceof SQLSelectQuery) {
                    break;
                }
            }
            if (!skip) {
                column = this.handleUnknownColumn(ident);
            }
        }
        if (column != null) {
            SQLObject parent2 = x.getParent();
            if (parent2 instanceof SQLSelectOrderByItem) {
                parent2 = parent2.getParent();
                if (parent2 instanceof SQLIndexDefinition) {
                    parent2 = parent2.getParent();
                }
            }
            if (parent2 instanceof SQLPrimaryKey) {
                column.setPrimaryKey(true);
            }
            else if (parent2 instanceof SQLUnique) {
                column.setUnique(true);
            }
            this.setColumn(x, column);
        }
        return false;
    }
    
    protected boolean isPseudoColumn(final long hash) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        if (isParam(x)) {
            return false;
        }
        final SQLTableSource tableSource = x.getResolvedTableSource();
        if (x.getParent() instanceof SQLSelectOrderByItem) {
            final SQLSelectOrderByItem selectOrderByItem = (SQLSelectOrderByItem)x.getParent();
            if (selectOrderByItem.getResolvedSelectItem() != null) {
                return false;
            }
        }
        if (tableSource == null && (x.getResolvedParameter() != null || x.getResolvedDeclareItem() != null)) {
            return false;
        }
        final long hash = x.nameHashCode64();
        if (this.isPseudoColumn(hash)) {
            return false;
        }
        if ((hash == FnvHash.Constants.LEVEL || hash == FnvHash.Constants.CONNECT_BY_ISCYCLE || hash == FnvHash.Constants.ROWNUM) && x.getResolvedColumn() == null && tableSource == null) {
            return false;
        }
        TableStat.Column column = null;
        final String ident = x.normalizedName();
        if (tableSource instanceof SQLExprTableSource) {
            SQLExpr expr = ((SQLExprTableSource)tableSource).getExpr();
            if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)expr;
                if (func.methodNameHashCode64() == FnvHash.Constants.ANN) {
                    expr = func.getArguments().get(0);
                }
            }
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr table = (SQLIdentifierExpr)expr;
                column = this.addColumn(table, ident);
                if (column != null && this.isParentGroupBy(x)) {
                    this.groupByColumns.add(column);
                }
            }
            else if (expr instanceof SQLPropertyExpr || expr instanceof SQLDbLinkExpr) {
                String tableName;
                if (expr instanceof SQLPropertyExpr) {
                    tableName = ((SQLPropertyExpr)expr).normalizedName();
                }
                else {
                    tableName = expr.toString();
                }
                column = this.addColumn(tableName, ident);
                if (column != null && this.isParentGroupBy(x)) {
                    this.groupByColumns.add(column);
                }
            }
            else if (expr instanceof SQLMethodInvokeExpr) {
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                if ("table".equalsIgnoreCase(methodInvokeExpr.getMethodName()) && methodInvokeExpr.getArguments().size() == 1 && methodInvokeExpr.getArguments().get(0) instanceof SQLName) {
                    final SQLName table2 = methodInvokeExpr.getArguments().get(0);
                    String tableName2 = null;
                    if (table2 instanceof SQLPropertyExpr) {
                        final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)table2;
                        final SQLIdentifierExpr owner = (SQLIdentifierExpr)propertyExpr.getOwner();
                        if (propertyExpr.getResolvedTableSource() != null && propertyExpr.getResolvedTableSource() instanceof SQLExprTableSource) {
                            final SQLExpr resolveExpr = ((SQLExprTableSource)propertyExpr.getResolvedTableSource()).getExpr();
                            if (resolveExpr instanceof SQLName) {
                                tableName2 = resolveExpr.toString() + "." + propertyExpr.getName();
                            }
                        }
                    }
                    if (tableName2 == null) {
                        tableName2 = table2.toString();
                    }
                    column = this.addColumn(tableName2, ident);
                }
            }
        }
        else {
            if (tableSource instanceof SQLWithSubqueryClause.Entry || tableSource instanceof SQLSubqueryTableSource || tableSource instanceof SQLLateralViewTableSource) {
                return false;
            }
            boolean skip = false;
            for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelectQueryBlock) {
                    final SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                    if (from instanceof SQLValuesTableSource) {
                        skip = true;
                        break;
                    }
                }
                else if (parent instanceof SQLSelectQuery) {
                    break;
                }
            }
            if (x.getParent() instanceof SQLMethodInvokeExpr && ((SQLMethodInvokeExpr)x.getParent()).methodNameHashCode64() == FnvHash.Constants.ANN) {
                skip = true;
            }
            if (!skip) {
                column = this.handleUnknownColumn(ident);
            }
        }
        if (column != null) {
            SQLObject parent2 = x.getParent();
            if (parent2 instanceof SQLSelectOrderByItem) {
                parent2 = parent2.getParent();
                if (parent2 instanceof SQLIndexDefinition) {
                    parent2 = parent2.getParent();
                }
            }
            if (parent2 instanceof SQLPrimaryKey) {
                column.setPrimaryKey(true);
            }
            else if (parent2 instanceof SQLUnique) {
                column.setUnique(true);
            }
            this.setColumn(x, column);
        }
        return false;
    }
    
    private boolean isParentSelectItem(SQLObject parent) {
        for (int i = 0; parent != null && i <= 100; parent = parent.getParent(), ++i) {
            if (parent instanceof SQLSelectItem) {
                return true;
            }
            if (parent instanceof SQLSelectQueryBlock) {
                return false;
            }
        }
        return false;
    }
    
    private boolean isParentGroupBy(SQLObject parent) {
        while (parent != null) {
            if (parent instanceof SQLSelectItem) {
                return false;
            }
            if (parent instanceof SQLSelectGroupByClause) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
    
    private void setColumn(final SQLExpr x, final TableStat.Column column) {
        SQLObject current = x;
        int i = 0;
        while (i < 100) {
            final SQLObject parent = current.getParent();
            if (parent == null) {
                break;
            }
            if (parent instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock query = (SQLSelectQueryBlock)parent;
                if (query.getWhere() == current) {
                    column.setWhere(true);
                    break;
                }
                break;
            }
            else if (parent instanceof SQLSelectGroupByClause) {
                final SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause)parent;
                if (current == groupBy.getHaving()) {
                    column.setHaving(true);
                    break;
                }
                if (groupBy.getItems().contains(current)) {
                    column.setGroupBy(true);
                    break;
                }
                break;
            }
            else {
                if (this.isParentSelectItem(parent)) {
                    column.setSelec(true);
                    break;
                }
                if (parent instanceof SQLJoinTableSource) {
                    final SQLJoinTableSource join = (SQLJoinTableSource)parent;
                    if (join.getCondition() == current) {
                        column.setJoin(true);
                        break;
                    }
                    break;
                }
                else {
                    current = parent;
                    ++i;
                }
            }
        }
    }
    
    protected TableStat.Column handleUnknownColumn(final String columnName) {
        return this.addColumn("UNKNOWN", columnName);
    }
    
    @Override
    public boolean visit(final SQLAllColumnExpr x) {
        final SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource == null) {
            return false;
        }
        this.statAllColumn(x, tableSource);
        return false;
    }
    
    private void statAllColumn(final SQLAllColumnExpr x, final SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            this.statAllColumn(x, (SQLExprTableSource)tableSource);
            return;
        }
        if (tableSource instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            this.statAllColumn(x, join.getLeft());
            this.statAllColumn(x, join.getRight());
        }
    }
    
    private void statAllColumn(final SQLAllColumnExpr x, final SQLExprTableSource tableSource) {
        final SQLExprTableSource exprTableSource = tableSource;
        final SQLName expr = exprTableSource.getName();
        SQLCreateTableStatement createStmt = null;
        final SchemaObject tableObject = exprTableSource.getSchemaObject();
        if (tableObject != null) {
            final SQLStatement stmt = tableObject.getStatement();
            if (stmt instanceof SQLCreateTableStatement) {
                createStmt = (SQLCreateTableStatement)stmt;
            }
        }
        if (createStmt != null && createStmt.getTableElementList().size() > 0) {
            final SQLName tableName = createStmt.getName();
            for (final SQLTableElement e : createStmt.getTableElementList()) {
                if (e instanceof SQLColumnDefinition) {
                    final SQLColumnDefinition columnDefinition = (SQLColumnDefinition)e;
                    final SQLName columnName = columnDefinition.getName();
                    final TableStat.Column column = this.addColumn(tableName, columnName.toString());
                    if (!this.isParentSelectItem(x.getParent())) {
                        continue;
                    }
                    column.setSelec(true);
                }
            }
        }
        else if (expr != null) {
            final TableStat.Column column2 = this.addColumn(expr, "*");
            if (this.isParentSelectItem(x.getParent())) {
                column2.setSelec(true);
            }
        }
    }
    
    public Map<TableStat.Name, TableStat> getTables() {
        return this.tableStats;
    }
    
    public boolean containsTable(final String tableName) {
        return this.tableStats.containsKey(new TableStat.Name(tableName));
    }
    
    public boolean containsColumn(final String tableName, final String columnName) {
        final int p = tableName.indexOf(46);
        long hashCode;
        if (p != -1) {
            final SQLExpr owner = SQLUtils.toSQLExpr(tableName, this.dbType);
            hashCode = new SQLPropertyExpr(owner, columnName).hashCode64();
        }
        else {
            hashCode = FnvHash.hashCode64(tableName, columnName);
        }
        return this.columns.containsKey(hashCode);
    }
    
    public Collection<TableStat.Column> getColumns() {
        return this.columns.values();
    }
    
    public TableStat.Column getColumn(final String tableName, final String columnName) {
        final TableStat.Column column = new TableStat.Column(tableName, columnName);
        return this.columns.get(column.hashCode64());
    }
    
    @Override
    public boolean visit(final SQLSelectStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.visit(x.getSelect());
        return false;
    }
    
    @Override
    public void endVisit(final SQLSelectStatement x) {
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause.Entry x) {
        final String alias = x.getAlias();
        final SQLWithSubqueryClause with = (SQLWithSubqueryClause)x.getParent();
        if (Boolean.TRUE == with.getRecursive()) {
            final SQLSelect select = x.getSubQuery();
            if (select != null) {
                select.accept(this);
            }
            else {
                x.getReturningStatement().accept(this);
            }
        }
        else {
            final SQLSelect select = x.getSubQuery();
            if (select != null) {
                select.accept(this);
            }
            else {
                x.getReturningStatement().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        x.getSelect().accept(this);
        return false;
    }
    
    protected boolean isSimpleExprTableSource(final SQLExprTableSource x) {
        return x.getExpr() instanceof SQLName;
    }
    
    public TableStat getTableStat(final SQLExprTableSource tableSource) {
        return this.getTableStatWithUnwrap(tableSource.getExpr());
    }
    
    protected TableStat getTableStatWithUnwrap(SQLExpr expr) {
        SQLExpr identExpr = null;
        expr = this.unwrapExpr(expr);
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            if (identifierExpr.nameHashCode64() == FnvHash.Constants.DUAL) {
                return null;
            }
            if (this.isSubQueryOrParamOrVariant(identifierExpr)) {
                return null;
            }
        }
        SQLTableSource tableSource = null;
        if (expr instanceof SQLIdentifierExpr) {
            tableSource = ((SQLIdentifierExpr)expr).getResolvedTableSource();
        }
        else if (expr instanceof SQLPropertyExpr) {
            tableSource = ((SQLPropertyExpr)expr).getResolvedTableSource();
        }
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr tableSourceExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableSourceExpr instanceof SQLName) {
                identExpr = tableSourceExpr;
            }
        }
        if (identExpr == null) {
            identExpr = expr;
        }
        if (identExpr instanceof SQLName) {
            return this.getTableStat((SQLName)identExpr);
        }
        return this.getTableStat(identExpr.toString());
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        SQLExpr expr = x.getExpr();
        if (expr instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr func = (SQLMethodInvokeExpr)expr;
            if (func.methodNameHashCode64() == FnvHash.Constants.ANN) {
                expr = func.getArguments().get(0);
            }
        }
        if (this.isSimpleExprTableSource(x)) {
            final TableStat stat = this.getTableStatWithUnwrap(expr);
            if (stat == null) {
                return false;
            }
            final TableStat.Mode mode = this.getMode();
            if (mode != null) {
                switch (mode) {
                    case Delete: {
                        stat.incrementDeleteCount();
                        break;
                    }
                    case Insert: {
                        stat.incrementInsertCount();
                        break;
                    }
                    case Update: {
                        stat.incrementUpdateCount();
                        break;
                    }
                    case Select: {
                        stat.incrementSelectCount();
                        break;
                    }
                    case Merge: {
                        stat.incrementMergeCount();
                        break;
                    }
                    case Drop: {
                        stat.incrementDropCount();
                        break;
                    }
                }
            }
        }
        else {
            this.accept(expr);
        }
        return false;
    }
    
    protected boolean isSubQueryOrParamOrVariant(final SQLIdentifierExpr identifierExpr) {
        final SQLObject resolvedColumnObject = identifierExpr.getResolvedColumnObject();
        if (resolvedColumnObject instanceof SQLWithSubqueryClause.Entry || resolvedColumnObject instanceof SQLParameter || resolvedColumnObject instanceof SQLDeclareItem) {
            return true;
        }
        final SQLObject resolvedOwnerObject = identifierExpr.getResolvedOwnerObject();
        return resolvedOwnerObject instanceof SQLSubqueryTableSource || resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry;
    }
    
    protected boolean isSubQueryOrParamOrVariant(final SQLPropertyExpr x) {
        final SQLObject resolvedOwnerObject = x.getResolvedOwnerObject();
        if (resolvedOwnerObject instanceof SQLSubqueryTableSource || resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry) {
            return true;
        }
        final SQLExpr owner = x.getOwner();
        if (owner instanceof SQLIdentifierExpr && this.isSubQueryOrParamOrVariant((SQLIdentifierExpr)owner)) {
            return true;
        }
        final SQLTableSource tableSource = x.getResolvedTableSource();
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExprTableSource exprTableSource = (SQLExprTableSource)tableSource;
            if (exprTableSource.getSchemaObject() != null) {
                return false;
            }
            final SQLExpr expr = exprTableSource.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                return this.isSubQueryOrParamOrVariant((SQLIdentifierExpr)expr);
            }
            if (expr instanceof SQLPropertyExpr) {
                return this.isSubQueryOrParamOrVariant((SQLPropertyExpr)expr);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectItem x) {
        this.statExpr(x.getExpr());
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        final SQLWithSubqueryClause with = x.getWithSubQuery();
        if (with != null) {
            with.accept(this);
        }
        final SQLSelectQuery query = x.getQuery();
        if (query != null) {
            query.accept(this);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.accept(x.getOrderBy());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAggregateExpr x) {
        this.aggregateFunctions.add(x);
        this.accept(x.getArguments());
        this.accept(x.getOrderBy());
        this.accept(x.getOver());
        return false;
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        this.functions.add(x);
        this.accept(x.getArguments());
        return false;
    }
    
    @Override
    public boolean visit(final SQLUpdateStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Update);
        final SQLTableSource tableSource = x.getTableSource();
        if (tableSource instanceof SQLExprTableSource) {
            final SQLName identName = ((SQLExprTableSource)tableSource).getName();
            final TableStat stat = this.getTableStat(identName);
            stat.incrementUpdateCount();
        }
        else {
            tableSource.accept(this);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            from.accept(this);
        }
        final List<SQLUpdateSetItem> items = x.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            final SQLUpdateSetItem item = items.get(i);
            this.visit(item);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            where.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLUpdateSetItem x) {
        final SQLExpr column = x.getColumn();
        if (column != null) {
            this.statExpr(column);
            final TableStat.Column columnStat = this.getColumn(column);
            if (columnStat != null) {
                columnStat.setUpdate(true);
            }
        }
        final SQLExpr value = x.getValue();
        if (value != null) {
            this.statExpr(value);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDeleteStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Delete);
        if (x.getTableSource() instanceof SQLSubqueryTableSource) {
            final SQLSelectQuery selectQuery = ((SQLSubqueryTableSource)x.getTableSource()).getSelect().getQuery();
            if (selectQuery instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock subQueryBlock = (SQLSelectQueryBlock)selectQuery;
                subQueryBlock.getWhere().accept(this);
            }
        }
        final TableStat stat = this.getTableStat(x.getTableName());
        stat.incrementDeleteCount();
        final SQLExpr where = x.getWhere();
        if (where != null) {
            where.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLInListExpr x) {
        if (x.isNot()) {
            this.handleCondition(x.getExpr(), "NOT IN", x.getTargetList());
        }
        else {
            this.handleCondition(x.getExpr(), "IN", x.getTargetList());
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLInSubQueryExpr x) {
        if (x.isNot()) {
            this.handleCondition(x.getExpr(), "NOT IN", new SQLExpr[0]);
        }
        else {
            this.handleCondition(x.getExpr(), "IN", new SQLExpr[0]);
        }
        return true;
    }
    
    @Override
    public void endVisit(final SQLDeleteStatement x) {
    }
    
    @Override
    public void endVisit(final SQLUpdateStatement x) {
    }
    
    @Override
    public boolean visit(final SQLCreateTableStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        for (final SQLTableElement e : x.getTableElementList()) {
            e.setParent(x);
        }
        final TableStat stat = this.getTableStat(x.getName());
        stat.incrementCreateCount();
        this.accept(x.getTableElementList());
        if (x.getInherits() != null) {
            x.getInherits().accept(this);
        }
        if (x.getSelect() != null) {
            x.getSelect().accept(this);
            stat.incrementInsertCount();
        }
        final SQLExprTableSource like = x.getLike();
        if (like != null) {
            like.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLColumnDefinition x) {
        SQLName tableName = null;
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLCreateTableStatement) {
            tableName = ((SQLCreateTableStatement)parent).getName();
        }
        if (tableName == null) {
            return true;
        }
        final String columnName = x.getName().toString();
        final TableStat.Column column = this.addColumn(tableName, columnName);
        if (x.getDataType() != null) {
            column.setDataType(x.getDataType().getName());
        }
        for (final SQLColumnConstraint item : x.getConstraints()) {
            if (item instanceof SQLPrimaryKey) {
                column.setPrimaryKey(true);
            }
            else {
                if (!(item instanceof SQLUnique)) {
                    continue;
                }
                column.setUnique(true);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCallStatement x) {
        return false;
    }
    
    @Override
    public void endVisit(final SQLCommentStatement x) {
    }
    
    @Override
    public boolean visit(final SQLCommentStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCurrentOfCursorExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final String table = stmt.getName().toString();
        for (final SQLColumnDefinition column : x.getColumns()) {
            final String columnName = SQLUtils.normalize(column.getName().toString());
            this.addColumn(stmt.getName(), columnName);
        }
        return false;
    }
    
    @Override
    public void endVisit(final SQLAlterTableAddColumn x) {
    }
    
    @Override
    public boolean visit(final SQLRollbackStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateViewStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLSelect subQuery = x.getSubQuery();
        if (subQuery != null) {
            subQuery.accept(this);
        }
        final SQLBlockStatement script = x.getScript();
        if (script != null) {
            script.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterViewStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        x.getSubQuery().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropForeignKey x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLUseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDisableConstraint x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableEnableConstraint x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final TableStat stat = this.getTableStat(x.getName());
        stat.incrementAlterCount();
        for (final SQLAlterTableItem item : x.getItems()) {
            item.setParent(x);
            if (item instanceof SQLAlterTableAddPartition || item instanceof SQLAlterTableRenamePartition || item instanceof SQLAlterTableMergePartition) {
                stat.incrementAddPartitionCount();
            }
            item.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropConstraint x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropIndexStatement x) {
        this.setMode(x, TableStat.Mode.DropIndex);
        final SQLExprTableSource table = x.getTableName();
        if (table != null) {
            final SQLName name = (SQLName)table.getExpr();
            final TableStat stat = this.getTableStat(name);
            stat.incrementDropIndexCount();
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateIndexStatement x) {
        this.setMode(x, TableStat.Mode.CreateIndex);
        final SQLName table = (SQLName)((SQLExprTableSource)x.getTable()).getExpr();
        final TableStat stat = this.getTableStat(table);
        stat.incrementCreateIndexCount();
        for (final SQLSelectOrderByItem item : x.getItems()) {
            final SQLExpr expr = item.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final String columnName = identExpr.getName();
                this.addColumn(table, columnName);
            }
            else {
                if (!(expr instanceof SQLMethodInvokeExpr)) {
                    continue;
                }
                final SQLMethodInvokeExpr methodInvokeExpr = (SQLMethodInvokeExpr)expr;
                if (methodInvokeExpr.getArguments().size() != 1) {
                    continue;
                }
                final SQLExpr param = methodInvokeExpr.getArguments().get(0);
                if (!(param instanceof SQLIdentifierExpr)) {
                    continue;
                }
                final SQLIdentifierExpr identExpr2 = (SQLIdentifierExpr)param;
                final String columnName2 = identExpr2.getName();
                this.addColumn(table, columnName2);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLForeignKeyImpl x) {
        for (final SQLName column : x.getReferencingColumns()) {
            column.accept(this);
        }
        final SQLName table = x.getReferencedTableName();
        final TableStat stat = this.getTableStat(x.getReferencedTableName());
        stat.incrementReferencedCount();
        for (final SQLName column2 : x.getReferencedColumns()) {
            final String columnName = column2.getSimpleName();
            this.addColumn(table, columnName);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropSequenceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTriggerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropUserStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLGrantStatement x) {
        if (x.getResource() != null && (x.getResourceType() == null || x.getResourceType() == SQLObjectType.TABLE)) {
            x.getResource().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLRevokeStatement x) {
        if (x.getResource() != null) {
            x.getResource().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropDatabaseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddIndex x) {
        for (final SQLSelectOrderByItem item : x.getColumns()) {
            item.accept(this);
        }
        final SQLName table = ((SQLAlterTableStatement)x.getParent()).getName();
        final TableStat tableStat = this.getTableStat(table);
        tableStat.incrementCreateIndexCount();
        return false;
    }
    
    @Override
    public boolean visit(final SQLCheck x) {
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDefault x) {
        x.getExpr().accept(this);
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateTriggerStatement x) {
        final SQLExprTableSource on = x.getOn();
        on.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropFunctionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTableSpaceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropProcedureStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRename x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLArrayExpr x) {
        this.accept(x.getValues());
        final SQLExpr exp = x.getExpr();
        if (exp instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)exp).getName().equals("ARRAY")) {
            return false;
        }
        if (exp != null) {
            exp.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLOpenStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLFetchStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropMaterializedViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowMaterializedViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLRefreshMaterializedViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCloseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateProcedureStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.accept(x.getBlock());
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateFunctionStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.accept(x.getBlock());
        return false;
    }
    
    @Override
    public boolean visit(final SQLBlockStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        for (final SQLParameter param : x.getParameters()) {
            param.setParent(x);
            param.accept(this);
        }
        for (final SQLStatement stmt : x.getStatementList()) {
            stmt.accept(this);
        }
        final SQLStatement exception = x.getException();
        if (exception != null) {
            exception.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowTablesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDeclareItem x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByHash x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByRange x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionByList x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartitionByHash x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionValue x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterDatabaseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableConvertCharSet x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableReOrganizePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableCoalescePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableTruncatePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDiscardPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableImportPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAnalyzePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableCheckPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableOptimizePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRebuildPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRepairPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLSequenceExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLMergeStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x.getUsing(), TableStat.Mode.Select);
        x.getUsing().accept(this);
        this.setMode(x, TableStat.Mode.Merge);
        final SQLTableSource into = x.getInto();
        if (into instanceof SQLExprTableSource) {
            final String ident = ((SQLExprTableSource)into).getExpr().toString();
            final TableStat stat = this.getTableStat(ident);
            stat.incrementMergeCount();
        }
        else {
            into.accept(this);
        }
        x.getOn().accept(this);
        if (x.getUpdateClause() != null) {
            x.getUpdateClause().accept(this);
        }
        if (x.getInsertClause() != null) {
            x.getInsertClause().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSetStatement x) {
        return false;
    }
    
    public List<SQLMethodInvokeExpr> getFunctions() {
        return this.functions;
    }
    
    @Override
    public boolean visit(final SQLCreateSequenceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddConstraint x) {
        final SQLConstraint constraint = x.getConstraint();
        if (constraint instanceof SQLUniqueConstraint) {
            final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
            final TableStat tableStat = this.getTableStat(stmt.getName());
            tableStat.incrementCreateIndexCount();
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropIndex x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final TableStat tableStat = this.getTableStat(stmt.getName());
        tableStat.incrementDropIndexCount();
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropPrimaryKey x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final TableStat tableStat = this.getTableStat(stmt.getName());
        tableStat.incrementDropIndexCount();
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableDropKey x) {
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final TableStat tableStat = this.getTableStat(stmt.getName());
        tableStat.incrementDropIndexCount();
        return false;
    }
    
    @Override
    public boolean visit(final SQLDescribeStatement x) {
        final SQLName tableName = x.getObject();
        final TableStat tableStat = this.getTableStat(x.getObject());
        final SQLName column = x.getColumn();
        if (column != null) {
            final String columnName = column.toString();
            this.addColumn(tableName, columnName);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExplainStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        if (x.getStatement() != null) {
            this.accept(x.getStatement());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateMaterializedViewStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLSelect query = x.getQuery();
        if (query != null) {
            query.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLReplaceStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Replace);
        final SQLName tableName = x.getTableName();
        final TableStat stat = this.getTableStat(tableName);
        if (stat != null) {
            stat.incrementInsertCount();
        }
        this.accept(x.getColumns());
        this.accept(x.getValuesList());
        this.accept(x.getQuery());
        return false;
    }
    
    protected final void statExpr(final SQLExpr x) {
        if (x == null) {
            return;
        }
        final Class<?> clazz = x.getClass();
        if (clazz == SQLIdentifierExpr.class) {
            this.visit((SQLIdentifierExpr)x);
        }
        else if (clazz == SQLPropertyExpr.class) {
            this.visit((SQLPropertyExpr)x);
        }
        else if (clazz == SQLBinaryOpExpr.class) {
            this.visit((SQLBinaryOpExpr)x);
        }
        else if (!(x instanceof SQLLiteralExpr)) {
            x.accept(this);
        }
    }
    
    @Override
    public boolean visit(final SQLAlterFunctionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropSynonymStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTypeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterProcedureStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprStatement x) {
        final SQLExpr expr = x.getExpr();
        return !(expr instanceof SQLName);
    }
    
    @Override
    public boolean visit(final SQLDropTypeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLExternalRecordFormat x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateDatabaseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateTableGroupStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropTableGroupStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowDatabasesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowColumnsStatement x) {
        final SQLName table = x.getTable();
        final TableStat stat = this.getTableStat(table);
        if (stat != null) {}
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateTableStatement x) {
        final SQLName table = x.getName();
        if (table != null) {
            final TableStat stat = this.getTableStat(table);
            if (stat != null) {}
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowTableGroupsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableSetOption x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowCreateViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateRoleStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropRoleStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowViewsStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableExchangePartition x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropCatalogStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQuery x) {
        final SQLUnionOperator operator = x.getOperator();
        final List<SQLSelectQuery> relations = x.getRelations();
        if (relations.size() > 2) {
            for (final SQLSelectQuery relation : x.getRelations()) {
                relation.accept(this);
            }
            return false;
        }
        final SQLSelectQuery left = x.getLeft();
        final SQLSelectQuery right = x.getRight();
        final boolean bracket = x.isParenthesized() && !(x.getParent() instanceof SQLUnionQueryTableSource);
        if (!bracket && left instanceof SQLUnionQuery && ((SQLUnionQuery)left).getOperator() == operator && !right.isParenthesized() && x.getOrderBy() == null) {
            SQLUnionQuery leftUnion = (SQLUnionQuery)left;
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
                    if (leftUnion.isParenthesized() || leftUnion.getOrderBy() != null || leftLeft.isParenthesized() || leftRight.isParenthesized() || !(leftLeft instanceof SQLUnionQuery) || ((SQLUnionQuery)leftLeft).getOperator() != operator) {
                        break;
                    }
                    rights.add(leftRight);
                    leftUnion = (SQLUnionQuery)leftLeft;
                }
                rights.add(leftRight);
                rights.add(leftLeft);
            }
            for (int i = rights.size() - 1; i >= 0; --i) {
                final SQLSelectQuery item = rights.get(i);
                item.accept(this);
            }
            return false;
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLValuesTableSource x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterIndexStatement x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowIndexesStatement x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAnalyzeTableStatement x) {
        for (final SQLExprTableSource table : x.getTables()) {
            if (table != null) {
                final TableStat stat = this.getTableStat(table.getName());
                if (stat == null) {
                    continue;
                }
                stat.incrementAnalyzeCount();
            }
        }
        final SQLExprTableSource table2 = (x.getTables().size() == 1) ? x.getTables().get(0) : null;
        final SQLPartitionRef partition = x.getPartition();
        if (partition != null) {
            for (final SQLPartitionRef.Item item : partition.getItems()) {
                final SQLIdentifierExpr columnName = item.getColumnName();
                columnName.setResolvedTableSource(table2);
                columnName.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExportTableStatement x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        for (final SQLAssignItem item : x.getPartition()) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr) {
                ((SQLIdentifierExpr)target).setResolvedTableSource(table);
                target.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLImportTableStatement x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        for (final SQLAssignItem item : x.getPartition()) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr) {
                ((SQLIdentifierExpr)target).setResolvedTableSource(table);
                target.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateOutlineStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        if (x.getOn() != null) {
            x.getOn().accept(this);
        }
        if (x.getTo() != null) {
            x.getTo().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDumpStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLExprTableSource into = x.getInto();
        if (into != null) {
            into.accept(this);
        }
        final SQLSelect select = x.getSelect();
        if (select != null) {
            select.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLDropOutlineStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterOutlineStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableArchivePartition x) {
        return true;
    }
    
    @Override
    public boolean visit(final HiveCreateTableStatement x) {
        return this.visit((SQLCreateTableStatement)x);
    }
    
    @Override
    public boolean visit(final SQLCopyFromStatement x) {
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            table.accept(this);
        }
        for (final SQLName column : x.getColumns()) {
            this.addColumn(table.getName(), column.getSimpleName());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCloneTableStatement x) {
        final SQLExprTableSource from = x.getFrom();
        if (from != null) {
            final TableStat stat = this.getTableStat(from.getName());
            if (stat != null) {
                stat.incrementSelectCount();
            }
        }
        final SQLExprTableSource to = x.getTo();
        if (to != null) {
            final TableStat stat2 = this.getTableStat(to.getName());
            if (stat2 != null) {
                stat2.incrementInsertCount();
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSyncMetaStatement x) {
        return false;
    }
    
    public List<SQLName> getOriginalTables() {
        return this.originalTables;
    }
    
    @Override
    public boolean visit(final SQLUnique x) {
        for (final SQLSelectOrderByItem column : x.getColumns()) {
            column.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSavePointStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowPartitionsStmt x) {
        this.setMode(x, TableStat.Mode.DESC);
        return true;
    }
    
    protected class OrderByStatVisitor extends SQLASTVisitorAdapter
    {
        private final SQLOrderBy orderBy;
        
        public OrderByStatVisitor(final SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            for (final SQLSelectOrderByItem item : orderBy.getItems()) {
                item.getExpr().setParent(item);
            }
        }
        
        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLPropertyExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLIntegerExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }
    
    protected class MySqlOrderByStatVisitor extends MySqlASTVisitorAdapter
    {
        private final SQLOrderBy orderBy;
        
        public MySqlOrderByStatVisitor(final SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            for (final SQLSelectOrderByItem item : orderBy.getItems()) {
                item.getExpr().setParent(item);
            }
        }
        
        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLPropertyExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLIntegerExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }
    
    protected class PGOrderByStatVisitor extends PGASTVisitorAdapter
    {
        private final SQLOrderBy orderBy;
        
        public PGOrderByStatVisitor(final SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            for (final SQLSelectOrderByItem item : orderBy.getItems()) {
                item.getExpr().setParent(item);
            }
        }
        
        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLPropertyExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLIntegerExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }
    
    protected class OracleOrderByStatVisitor extends OracleASTVisitorAdapter
    {
        private final SQLOrderBy orderBy;
        
        public OracleOrderByStatVisitor(final SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            for (final SQLSelectOrderByItem item : orderBy.getItems()) {
                item.getExpr().setParent(item);
            }
        }
        
        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }
        
        @Override
        public boolean visit(final SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
        
        @Override
        public boolean visit(final SQLPropertyExpr x) {
            final SQLExpr unwrapped = SchemaStatVisitor.this.unwrapExpr(x);
            if (unwrapped instanceof SQLPropertyExpr) {
                SchemaStatVisitor.this.visitOrderBy((SQLPropertyExpr)unwrapped);
            }
            else if (unwrapped instanceof SQLIdentifierExpr) {
                SchemaStatVisitor.this.visitOrderBy((SQLIdentifierExpr)unwrapped);
            }
            return false;
        }
        
        @Override
        public boolean visit(final SQLIntegerExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }
}
