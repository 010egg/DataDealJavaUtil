// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterMaterializedViewStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLAlterResourceGroupStatement;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLCreateResourceGroupStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveMsckRepairStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTableGroupsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import java.sql.SQLException;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.TimeZone;
import java.util.UUID;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLDecimalExpr;
import com.alibaba.druid.util.MySqlUtils;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLDescribeStatement;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloseStatement;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLFetchStatement;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLOpenStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddClusteringKey;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.SQLTableDataType;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlExprParser;
import com.alibaba.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateExternalCatalogStatement;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleExprParser;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.ast.statement.SQLCreateMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPartitionRef;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLListResourceGroupStatement;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropSubpartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableReplaceColumn;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewRenameStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRecoverPartitions;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableMergePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSubpartitionAvailablePartitionNum;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableUnarchivePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableArchivePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableChangeOwner;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTouch;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartitionSetProperties;
import com.alibaba.druid.sql.ast.statement.SQLAlterTablePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetFileFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetChangeLogs;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLocation;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetComment;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDeleteByCondition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLReleaseSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLRevokeStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLObjectType;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLReturnStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeLogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTemporaryOutputStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeRecyclebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropResourceGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropEventStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropLogFileGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropServerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableGroupStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropCatalogStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLRefreshMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropOutlineStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhoamiStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.FullTextType;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlManageInstanceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MysqlDropFullTextStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLDumpStatement;
import com.alibaba.druid.sql.ast.statement.SQLForStatement;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainPlanCacheStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdatePlanCacheStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.DbType;
import java.sql.Date;
import java.sql.Timestamp;
import com.alibaba.druid.sql.repository.SchemaRepository;

public class SQLStatementParser extends SQLParser
{
    protected SchemaRepository repository;
    protected SQLExprParser exprParser;
    protected boolean parseCompleteValues;
    protected int parseValuesSize;
    protected SQLSelectListCache selectListCache;
    protected InsertColumnsCache insertColumnsCache;
    protected Timestamp now;
    protected Date currentDate;
    
    public SQLStatementParser(final String sql) {
        this(sql, null);
    }
    
    public SQLStatementParser(final String sql, final DbType dbType) {
        this(new SQLExprParser(sql, dbType, new SQLParserFeature[0]));
    }
    
    public SQLStatementParser(final SQLExprParser exprParser) {
        super(exprParser.getLexer(), exprParser.getDbType());
        this.parseCompleteValues = true;
        this.parseValuesSize = 3;
        this.selectListCache = null;
        this.insertColumnsCache = null;
        this.exprParser = exprParser;
        this.dbType = exprParser.dbType;
    }
    
    protected SQLStatementParser(final Lexer lexer, final DbType dbType) {
        super(lexer, dbType);
        this.parseCompleteValues = true;
        this.parseValuesSize = 3;
        this.selectListCache = null;
        this.insertColumnsCache = null;
    }
    
    public boolean isKeepComments() {
        return this.lexer.isKeepComments();
    }
    
    public void setKeepComments(final boolean keepComments) {
        this.lexer.setKeepComments(keepComments);
    }
    
    public SQLExprParser getExprParser() {
        return this.exprParser;
    }
    
    public SchemaRepository getRepository() {
        return this.repository;
    }
    
    public void setRepository(final SchemaRepository repository) {
        this.repository = repository;
    }
    
    public List<SQLStatement> parseStatementList() {
        final List<SQLStatement> statementList = new ArrayList<SQLStatement>();
        this.parseStatementList(statementList, -1, null);
        return statementList;
    }
    
    public List<SQLStatement> parseStatementList(final SQLObject parent) {
        final List<SQLStatement> statementList = new ArrayList<SQLStatement>();
        this.parseStatementList(statementList, -1, parent);
        return statementList;
    }
    
    public void parseStatementList(final List<SQLStatement> statementList) {
        this.parseStatementList(statementList, -1, null);
    }
    
    public void parseStatementList(final List<SQLStatement> statementList, final int max) {
        this.parseStatementList(statementList, max, null);
    }
    
    public void parseStatementList(final List<SQLStatement> statementList, final int max, final SQLObject parent) {
        if ("select @@session.tx_read_only".equals(this.lexer.text) && this.lexer.token == Token.SELECT) {
            final SQLSelect select = new SQLSelect();
            final MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();
            queryBlock.addSelectItem(new SQLPropertyExpr(new SQLVariantRefExpr("@@session"), "tx_read_only"));
            select.setQuery(queryBlock);
            final SQLSelectStatement stmt = new SQLSelectStatement(select);
            statementList.add(stmt);
            this.lexer.reset(29, '\u001a', Token.EOF);
            return;
        }
        boolean semi = false;
        int i = 0;
        while (max == -1 || statementList.size() < max) {
            while (this.lexer.token == Token.MULTI_LINE_COMMENT || this.lexer.token == Token.LINE_COMMENT) {
                this.lexer.nextToken();
            }
            switch (this.lexer.token) {
                case EOF:
                case END:
                case UNTIL:
                case ELSE:
                case WHEN: {
                    if (this.lexer.isKeepComments() && this.lexer.hasComment() && statementList.size() > 0) {
                        final SQLStatement stmt2 = statementList.get(statementList.size() - 1);
                        stmt2.addAfterComment(this.lexer.readAndResetComments());
                    }
                    return;
                }
                case SEMI: {
                    final int line0 = this.lexer.getLine();
                    final char ch = this.lexer.ch;
                    this.lexer.nextToken();
                    final int line2 = this.lexer.getLine();
                    if (statementList.size() > 0) {
                        final SQLStatement lastStmt = statementList.get(statementList.size() - 1);
                        lastStmt.setAfterSemi(true);
                        if (this.lexer.isKeepComments()) {
                            if (ch == '\n' && this.lexer.getComments() != null && !this.lexer.getComments().isEmpty() && !(lastStmt instanceof SQLSetStatement)) {
                                this.lexer.getComments().add(0, new String("\n"));
                            }
                            if (line2 - line0 <= 1) {
                                lastStmt.addAfterComment(this.lexer.readAndResetComments());
                            }
                        }
                    }
                    semi = true;
                    break;
                }
                case WITH: {
                    final SQLStatement stmt2 = this.parseWith();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case SELECT: {
                    MySqlHintStatement hintStatement = null;
                    if (i == 1 && statementList.size() > 0 && statementList.get(statementList.size() - i) instanceof MySqlHintStatement) {
                        hintStatement = statementList.get(statementList.size() - i);
                    }
                    else if (i > 0 && this.dbType != DbType.odps && !semi) {
                        throw new ParserException("syntax error. " + this.lexer.info());
                    }
                    final SQLStatement stmt3 = this.parseSelect();
                    stmt3.setParent(parent);
                    if (hintStatement != null && stmt3 instanceof SQLStatementImpl) {
                        final SQLStatementImpl stmtImpl = (SQLStatementImpl)stmt3;
                        final List<SQLCommentHint> hints = stmtImpl.getHeadHintsDirect();
                        if (hints == null) {
                            stmtImpl.setHeadHints(hintStatement.getHints());
                        }
                        else {
                            hints.addAll(hintStatement.getHints());
                        }
                        statementList.set(statementList.size() - 1, stmt3);
                    }
                    else {
                        statementList.add(stmt3);
                    }
                    semi = false;
                    break;
                }
                case UPDATE: {
                    final Lexer.SavePoint savePoint = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.dbType == DbType.mysql && this.lexer.identifierEquals("PLANCACHE")) {
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.SELECT) {
                            final MySqlUpdatePlanCacheStatement stmt4 = new MySqlUpdatePlanCacheStatement();
                            final SQLSelect fromSelect = this.createSQLSelectParser().select();
                            this.accept(Token.TO);
                            final SQLSelect toSelect = this.createSQLSelectParser().select();
                            stmt4.setFormSelect(fromSelect);
                            stmt4.setToSelect(toSelect);
                            statementList.add(stmt4);
                            break;
                        }
                    }
                    this.lexer.reset(savePoint);
                    final SQLStatement stmt3 = this.parseUpdateStatement();
                    stmt3.setParent(parent);
                    statementList.add(stmt3);
                    break;
                }
                case CREATE: {
                    final SQLStatement stmt2 = this.parseCreate();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case INSERT: {
                    final SQLStatement stmt2 = this.parseInsert();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case DELETE: {
                    final SQLStatement stmt2 = this.parseDeleteStatement();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case EXPLAIN: {
                    this.lexer.computeRowAndColumn();
                    final int sourceLine = this.lexer.posLine;
                    final int sourceColumn = this.lexer.posColumn;
                    final Lexer.SavePoint savePoint2 = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("PLANCACHE")) {
                        this.lexer.nextToken();
                        final MySqlExplainPlanCacheStatement stmt5 = new MySqlExplainPlanCacheStatement();
                        stmt5.setSourceLine(sourceLine);
                        stmt5.setSourceLine(sourceColumn);
                        statementList.add(stmt5);
                        break;
                    }
                    this.lexer.reset(savePoint2);
                    final SQLExplainStatement stmt6 = this.parseExplain();
                    stmt6.setSourceLine(sourceLine);
                    stmt6.setSourceLine(sourceColumn);
                    stmt6.setParent(parent);
                    statementList.add(stmt6);
                    break;
                }
                case SET: {
                    final SQLStatement stmt2 = this.parseSet();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case ALTER: {
                    final SQLStatement stmt2 = this.parseAlter();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case TRUNCATE: {
                    final SQLStatement stmt2 = this.parseTruncate();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case USE: {
                    final SQLStatement stmt2 = this.parseUse();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case GRANT: {
                    final SQLStatement stmt2 = this.parseGrant();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case REVOKE: {
                    final SQLStatement stmt2 = this.parseRevoke();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case SHOW: {
                    final SQLStatement stmt2 = this.parseShow();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case MERGE: {
                    final SQLStatement stmt2 = this.parseMerge();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case REPEAT: {
                    final SQLStatement stmt2 = this.parseRepeat();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case DECLARE: {
                    final SQLStatement stmt2 = this.parseDeclare();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case WHILE: {
                    final SQLStatement stmt2 = this.parseWhile();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case IF: {
                    final SQLStatement stmt2 = this.parseIf();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case CASE: {
                    final SQLStatement stmt2 = this.parseCase();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case OPEN: {
                    final SQLStatement stmt2 = this.parseOpen();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case FETCH: {
                    final SQLStatement stmt2 = this.parseFetch();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case DROP: {
                    final SQLStatement stmt2 = this.parseDrop();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case COMMENT: {
                    final SQLStatement stmt2 = this.parseComment();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case KILL: {
                    final SQLStatement stmt2 = this.parseKill();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case CLOSE: {
                    final SQLStatement stmt2 = this.parseClose();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case RETURN: {
                    final SQLStatement stmt2 = this.parseReturn();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case UPSERT: {
                    final SQLStatement stmt2 = this.parseUpsert();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                case LEAVE: {
                    final SQLStatement stmt2 = this.parseLeave();
                    stmt2.setParent(parent);
                    statementList.add(stmt2);
                    break;
                }
                default: {
                    if (this.lexer.token == Token.LBRACE || this.lexer.identifierEquals("CALL")) {
                        final SQLCallStatement stmt7 = this.parseCall();
                        statementList.add(stmt7);
                        break;
                    }
                    if (this.lexer.identifierEquals("UPSERT")) {
                        final SQLStatement stmt2 = this.parseUpsert();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("LIST")) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        final SQLStatement stmt3 = this.parseList();
                        if (stmt3 != null) {
                            statementList.add(stmt3);
                            break;
                        }
                        this.lexer.reset(mark);
                    }
                    if (this.lexer.identifierEquals("RENAME")) {
                        final SQLStatement stmt2 = this.parseRename();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("RELEASE")) {
                        final SQLStatement stmt2 = this.parseReleaseSavePoint();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("SAVEPOINT")) {
                        final SQLStatement stmt2 = this.parseSavePoint();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("REFRESH")) {
                        final SQLStatement stmt2 = this.parseRefresh();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("SETPROJECT")) {
                        final SQLStatement stmt2 = this.parseSet();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.COPY)) {
                        final SQLStatement stmt2 = this.parseCopy();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.token == Token.DESC || this.lexer.identifierEquals(FnvHash.Constants.DESCRIBE)) {
                        final SQLStatement stmt2 = this.parseDescribe();
                        statementList.add(stmt2);
                        break;
                    }
                    if (this.lexer.identifierEquals("ROLLBACK")) {
                        final SQLStatement stmt2 = this.parseRollback();
                        statementList.add(stmt2);
                        if (parent instanceof SQLBlockStatement && DbType.mysql == this.dbType) {
                            return;
                        }
                        break;
                    }
                    else {
                        if (this.lexer.identifierEquals("DUMP")) {
                            final SQLStatement stmt2 = this.parseDump();
                            statementList.add(stmt2);
                            break;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.COMMIT)) {
                            final SQLStatement stmt2 = this.parseCommit();
                            statementList.add(stmt2);
                            if (parent instanceof SQLBlockStatement && DbType.mysql == this.dbType) {
                                return;
                            }
                            break;
                        }
                        else {
                            if (this.lexer.identifierEquals(FnvHash.Constants.RETURN)) {
                                final SQLStatement stmt2 = this.parseReturn();
                                statementList.add(stmt2);
                                break;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.PURGE)) {
                                final SQLStatement stmt2 = this.parsePurge();
                                statementList.add(stmt2);
                                break;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.FLASHBACK)) {
                                final SQLStatement stmt2 = this.parseFlashback();
                                statementList.add(stmt2);
                                break;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.WHO)) {
                                final SQLStatement stmt2 = this.parseWhoami();
                                statementList.add(stmt2);
                                break;
                            }
                            if (this.lexer.token == Token.FOR) {
                                final SQLStatement stmt2 = this.parseFor();
                                statementList.add(stmt2);
                                stmt2.setParent(parent);
                                break;
                            }
                            if (this.lexer.token == Token.LPAREN) {
                                final char markChar = this.lexer.current();
                                final int markBp = this.lexer.bp();
                                int parenCount = 0;
                                do {
                                    this.lexer.nextToken();
                                    ++parenCount;
                                } while (this.lexer.token == Token.LPAREN);
                                if (this.lexer.token == Token.RPAREN && parenCount == 1 && this.dbType == DbType.odps) {
                                    this.lexer.nextToken();
                                    break;
                                }
                                if (this.lexer.token == Token.SELECT) {
                                    this.lexer.reset(markBp, markChar, Token.LPAREN);
                                    final SQLStatement stmt8 = this.parseSelect();
                                    statementList.add(stmt8);
                                    break;
                                }
                                throw new ParserException("TODO " + this.lexer.info());
                            }
                            else {
                                if (this.lexer.token == Token.VALUES) {
                                    final SQLValuesTableSource values = this.createSQLSelectParser().parseValues();
                                    final SQLSelectStatement stmt9 = new SQLSelectStatement();
                                    stmt9.setSelect(new SQLSelect(values));
                                    statementList.add(stmt9);
                                    stmt9.setParent(parent);
                                    break;
                                }
                                final int size = statementList.size();
                                if (!this.parseStatementListDialect(statementList)) {
                                    throw new ParserException("not supported." + this.lexer.info());
                                }
                                if (parent != null) {
                                    for (int j = size; j < statementList.size(); ++j) {
                                        final SQLStatement dialectStmt = statementList.get(j);
                                        dialectStmt.setParent(parent);
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            ++i;
        }
    }
    
    public SQLStatement parseCopy() {
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    public SQLStatement parseFor() {
        this.accept(Token.FOR);
        final SQLForStatement stmt = new SQLForStatement();
        stmt.setDbType(this.dbType);
        stmt.setIndex(this.exprParser.name());
        this.accept(Token.IN);
        stmt.setRange(this.exprParser.expr());
        this.accept(Token.LOOP);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.END);
        this.accept(Token.LOOP);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    public SQLStatement parseFlashback() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseDump() {
        final SQLDumpStatement stmt = new SQLDumpStatement();
        this.acceptIdentifier("DUMP");
        this.acceptIdentifier("DATA");
        if (this.lexer.identifierEquals(FnvHash.Constants.OVERWRITE)) {
            this.lexer.nextToken();
            stmt.setOverwrite(true);
        }
        if (this.lexer.token == Token.INTO) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.LITERAL_CHARS) {
                stmt.setInto(new SQLCharExpr(this.lexer.stringVal));
                this.lexer.nextToken();
            }
            else {
                stmt.setInto(this.exprParser.expr());
            }
        }
        final SQLSelect select = this.createSQLSelectParser().select();
        stmt.setSelect(select);
        return stmt;
    }
    
    public SQLStatement parseDrop() {
        List<String> beforeComments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            beforeComments = this.lexer.readAndResetComments();
        }
        final Lexer.SavePoint mark = this.lexer.mark();
        this.lexer.nextToken();
        List<SQLCommentHint> hints = null;
        if (this.lexer.token == Token.HINT) {
            hints = this.exprParser.parseHints();
        }
        boolean temporary = false;
        if (this.lexer.token == Token.TEMPORARY || this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY)) {
            this.lexer.nextToken();
            temporary = true;
        }
        boolean physical = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.PHYSICAL)) {
            this.lexer.nextToken();
            physical = true;
        }
        SQLStatement stmt = null;
        switch (this.lexer.token) {
            case USER: {
                stmt = this.parseDropUser();
                break;
            }
            case INDEX: {
                stmt = this.parseDropIndex();
                break;
            }
            case VIEW: {
                stmt = this.parseDropView(false);
                break;
            }
            case TRIGGER: {
                stmt = this.parseDropTrigger(false);
                break;
            }
            case DATABASE: {
                stmt = this.parseDropDatabaseOrSchema(false);
                if (physical) {
                    ((SQLDropDatabaseStatement)stmt).setPhysical(physical);
                    break;
                }
                break;
            }
            case SCHEMA: {
                if (this.dbType == DbType.postgresql) {
                    stmt = this.parseDropSchema();
                    break;
                }
                stmt = this.parseDropDatabaseOrSchema(false);
                if (physical) {
                    ((SQLDropDatabaseStatement)stmt).setPhysical(physical);
                    break;
                }
                break;
            }
            case FUNCTION: {
                final SQLDropFunctionStatement dropFunc = this.parseDropFunction(false);
                if (temporary) {
                    dropFunc.setTemporary(true);
                }
                stmt = dropFunc;
                break;
            }
            case TABLESPACE: {
                stmt = this.parseDropTablespace(false);
                break;
            }
            case PROCEDURE: {
                stmt = this.parseDropProcedure(false);
                break;
            }
            case SEQUENCE: {
                stmt = this.parseDropSequence(false);
                break;
            }
            case TABLE: {
                final SQLDropTableStatement dropTable = this.parseDropTable(false);
                if (temporary) {
                    dropTable.setTemporary(true);
                }
                if (hints != null) {
                    dropTable.setHints(hints);
                }
                stmt = dropTable;
                break;
            }
            default: {
                if (this.lexer.token == Token.TABLE || this.lexer.identifierEquals("TEMPORARY") || this.lexer.identifierEquals(FnvHash.Constants.PARTITIONED)) {
                    final SQLDropTableStatement dropTable = this.parseDropTable(false);
                    if (hints != null) {
                        dropTable.setHints(hints);
                    }
                    stmt = dropTable;
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
                    stmt = this.parseDropTable(false);
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.EVENT)) {
                    stmt = this.parseDropEvent();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                    stmt = this.parseDropResource();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.LOGFILE)) {
                    stmt = this.parseDropLogFileGroup();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.SERVER)) {
                    stmt = this.parseDropServer();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUP)) {
                    stmt = this.parseDropTableGroup();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ROLE)) {
                    this.lexer.reset(mark);
                    stmt = this.parseDropRole();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.OUTLINE)) {
                    this.lexer.reset(mark);
                    stmt = this.parseDropOutline();
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.TABLE) {
                        this.lexer.reset(mark);
                        stmt = this.parseDropTable(true);
                        break;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.CATALOG)) {
                        this.lexer.reset(mark);
                        stmt = this.parseDropCatalog();
                        break;
                    }
                    throw new ParserException("TODO " + this.lexer.info());
                }
                else {
                    if (this.lexer.token() == Token.FULLTEXT) {
                        this.lexer.nextToken();
                        final FullTextType type = this.parseFullTextType();
                        final SQLName name = this.exprParser.name();
                        final MysqlDropFullTextStatement x = new MysqlDropFullTextStatement();
                        x.setName(name);
                        x.setType(type);
                        stmt = x;
                        break;
                    }
                    if (this.lexer.identifierEquals("INSTANCE_GROUP")) {
                        this.lexer.nextToken();
                        final MySqlManageInstanceGroupStatement x2 = new MySqlManageInstanceGroupStatement();
                        x2.setOperation(new SQLIdentifierExpr("DROP"));
                        while (true) {
                            x2.getGroupNames().add(this.exprParser.expr());
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        stmt = x2;
                        break;
                    }
                    if (this.lexer.identifierEquals("MATERIALIZED")) {
                        stmt = this.parseDropMaterializedView();
                        break;
                    }
                    throw new ParserException("TODO " + this.lexer.info());
                }
                break;
            }
        }
        if (beforeComments != null) {
            stmt.addBeforeComment(beforeComments);
        }
        return stmt;
    }
    
    protected FullTextType parseFullTextType() {
        FullTextType textType;
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARFILTER)) {
            textType = FullTextType.CHARFILTER;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TOKENIZER)) {
            textType = FullTextType.TOKENIZER;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TOKENFILTER)) {
            textType = FullTextType.TOKENFILTER;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.ANALYZER)) {
            textType = FullTextType.ANALYZER;
        }
        else {
            if (!this.lexer.identifierEquals(FnvHash.Constants.DICTIONARY)) {
                throw new ParserException("type of full text must be [CHARFILTER/TOKENIZER/TOKENFILTER/ANALYZER/DICTIONARY] .");
            }
            textType = FullTextType.DICTIONARY;
        }
        this.lexer.nextToken();
        return textType;
    }
    
    protected SQLStatement parseWhoami() {
        this.lexer.nextToken();
        this.acceptIdentifier("AM");
        this.acceptIdentifier("I");
        return new SQLWhoamiStatement();
    }
    
    protected SQLStatement parseDropOutline() {
        this.accept(Token.DROP);
        final SQLDropOutlineStatement stmt = new SQLDropOutlineStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("OUTLINE");
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    public SQLStatement parseRefresh() {
        if (this.lexer.identifierEquals("REFRESH")) {
            this.lexer.nextToken();
        }
        final SQLRefreshMaterializedViewStatement stmt = new SQLRefreshMaterializedViewStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("MATERIALIZED");
        this.accept(Token.VIEW);
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    public SQLStatement parseShowMaterializedView() {
        if (this.lexer.token() == Token.SHOW) {
            this.lexer.nextToken();
        }
        final SQLShowMaterializedViewStatement stmt = new SQLShowMaterializedViewStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("MATERIALIZED");
        this.acceptIdentifier("VIEWS");
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            stmt.setLike(this.exprParser.charExpr());
        }
        return stmt;
    }
    
    public SQLStatement parseDropMaterializedView() {
        if (this.lexer.token() == Token.DROP) {
            this.lexer.nextToken();
        }
        final SQLDropMaterializedViewStatement stmt = new SQLDropMaterializedViewStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("MATERIALIZED");
        this.accept(Token.VIEW);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    protected SQLStatement parseDropCatalog() {
        this.accept(Token.DROP);
        final SQLDropCatalogStatement stmt = new SQLDropCatalogStatement(this.dbType);
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            stmt.setExternal(true);
            this.lexer.nextToken();
        }
        this.acceptIdentifier("CATALOG");
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLStatement parseDropRole() {
        this.accept(Token.DROP);
        this.acceptIdentifier("ROLE");
        final SQLDropRoleStatement stmt = new SQLDropRoleStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLStatement parseDropTableGroup() {
        if (this.lexer.token == Token.DROP) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("TABLEGROUP");
        final SQLDropTableGroupStatement stmt = new SQLDropTableGroupStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLStatement parseDropServer() {
        if (this.lexer.token == Token.DROP) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("SERVER");
        final SQLDropServerStatement stmt = new SQLDropServerStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLStatement parseDropLogFileGroup() {
        if (this.lexer.token == Token.DROP) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("LOGFILE");
        this.accept(Token.GROUP);
        final SQLDropLogFileGroupStatement stmt = new SQLDropLogFileGroupStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr engine = this.exprParser.primary();
            stmt.setEngine(engine);
        }
        return stmt;
    }
    
    protected SQLStatement parseDropEvent() {
        if (this.lexer.token == Token.DROP) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("EVENT");
        final SQLDropEventStatement stmt = new SQLDropEventStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLStatement parseDropResource() {
        if (this.lexer.token == Token.DROP) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("RESOURCE");
        if (this.lexer.token == Token.GROUP) {
            this.lexer.nextToken();
            final SQLDropResourceGroupStatement stmt = new SQLDropResourceGroupStatement();
            stmt.setDbType(this.dbType);
            if (this.lexer.token == Token.IF) {
                this.lexer.nextToken();
                this.accept(Token.EXISTS);
                stmt.setIfExists(true);
            }
            final SQLName name = this.exprParser.name();
            stmt.setName(name);
            return stmt;
        }
        final SQLDropResourceStatement stmt2 = new SQLDropResourceStatement();
        stmt2.setDbType(this.dbType);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt2.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt2.setName(name);
        return stmt2;
    }
    
    protected SQLStatement parseAlterFunction() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLStatement parseKill() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseCase() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseIf() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseWhile() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseDeclare() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseRepeat() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parseLeave() {
        throw new ParserException("not supported. " + this.lexer.info());
    }
    
    public SQLStatement parsePurge() {
        this.acceptIdentifier("PURGE");
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
            final SQLName tableName = this.exprParser.name();
            final SQLPurgeTableStatement stmt = new SQLPurgeTableStatement();
            stmt.setTable(tableName);
            return stmt;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.RECYCLEBIN)) {
            this.lexer.nextToken();
            final SQLPurgeRecyclebinStatement stmt2 = new SQLPurgeRecyclebinStatement();
            return stmt2;
        }
        if (this.lexer.token == Token.ALL) {
            this.lexer.nextToken();
            final SQLPurgeTableStatement stmt3 = new SQLPurgeTableStatement();
            stmt3.setAll(true);
            if (this.lexer.token == Token.LITERAL_INT) {
                stmt3.setCount(this.lexer.integerValue().intValue());
                this.lexer.nextToken();
            }
            return stmt3;
        }
        if (this.lexer.identifierEquals("TEMPORARY")) {
            this.lexer.nextToken();
            this.acceptIdentifier("OUTPUT");
            final SQLPurgeTemporaryOutputStatement stmt4 = new SQLPurgeTemporaryOutputStatement();
            stmt4.setName(this.exprParser.name());
            return stmt4;
        }
        final SQLPurgeLogsStatement stmt5 = new SQLPurgeLogsStatement();
        if (this.lexer.token == Token.BINARY) {
            this.lexer.nextToken();
            stmt5.setBinary(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MASTER)) {
            this.lexer.nextToken();
            stmt5.setMaster(true);
        }
        if (this.lexer.token == Token.ALL) {
            this.lexer.nextToken();
            stmt5.setAll(true);
            return stmt5;
        }
        this.acceptIdentifier("LOGS");
        if (this.lexer.token == Token.TO) {
            this.lexer.nextToken();
            final SQLExpr to = this.exprParser.expr();
            stmt5.setTo(to);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.BEFORE)) {
            this.lexer.nextToken();
            final SQLExpr before = this.exprParser.expr();
            stmt5.setBefore(before);
        }
        return stmt5;
    }
    
    public SQLStatement parseReturn() {
        if (this.lexer.token == Token.RETURN || this.lexer.identifierEquals("RETURN")) {
            this.lexer.nextToken();
        }
        final SQLReturnStatement stmt = new SQLReturnStatement();
        if (this.lexer.token != Token.SEMI) {
            final SQLExpr expr = this.exprParser.expr();
            stmt.setExpr(expr);
        }
        if (this.lexer.token == Token.SEMI) {
            this.accept(Token.SEMI);
            stmt.setAfterSemi(true);
        }
        return stmt;
    }
    
    public SQLStatement parseUpsert() {
        final SQLInsertStatement insertStatement = new SQLInsertStatement();
        if (this.lexer.token == Token.UPSERT || this.lexer.identifierEquals("UPSERT")) {
            this.lexer.nextToken();
            insertStatement.setUpsert(true);
        }
        this.parseInsert0(insertStatement);
        return insertStatement;
    }
    
    public SQLStatement parseRollback() {
        this.lexer.nextToken();
        if (this.lexer.identifierEquals("WORK")) {
            this.lexer.nextToken();
        }
        final SQLRollbackStatement stmt = new SQLRollbackStatement(this.getDbType());
        if (this.lexer.token == Token.TO) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("SAVEPOINT") || this.lexer.token == Token.SAVEPOINT) {
                this.lexer.nextToken();
            }
            stmt.setTo(this.exprParser.name());
        }
        return stmt;
    }
    
    public SQLStatement parseCommit() {
        this.acceptIdentifier("COMMIT");
        return new SQLCommitStatement();
    }
    
    public SQLStatement parseShow() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLUseStatement parseUse() {
        this.accept(Token.USE);
        final SQLUseStatement stmt = new SQLUseStatement(this.getDbType());
        stmt.setDatabase(this.exprParser.name());
        return stmt;
    }
    
    protected SQLExpr parseUser() {
        final SQLExpr user = this.exprParser.expr();
        return user;
    }
    
    public SQLGrantStatement parseGrant() {
        this.accept(Token.GRANT);
        final SQLGrantStatement stmt = new SQLGrantStatement(this.getDbType());
        this.parsePrivileages(stmt.getPrivileges(), stmt);
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            switch (this.lexer.token) {
                case PROCEDURE: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.PROCEDURE);
                    break;
                }
                case FUNCTION: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.FUNCTION);
                    break;
                }
                case TABLE: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.TABLE);
                    break;
                }
                case USER: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.USER);
                    break;
                }
                case DATABASE: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.DATABASE);
                    break;
                }
                case IDENTIFIER: {
                    if (this.lexer.identifierEquals("SYSTEM")) {
                        this.lexer.nextToken();
                        stmt.setResourceType(SQLObjectType.SYSTEM);
                        break;
                    }
                    break;
                }
            }
            if (stmt.getResourceType() != null && this.lexer.token == Token.COLONCOLON) {
                this.lexer.nextToken();
            }
            SQLExpr expr;
            if (this.lexer.token == Token.DOT) {
                expr = new SQLAllColumnExpr();
                this.lexer.nextToken();
            }
            else {
                expr = this.exprParser.expr();
            }
            if (stmt.getResourceType() == SQLObjectType.TABLE || stmt.getResourceType() == null) {
                stmt.setResource(new SQLExprTableSource(expr));
            }
            else {
                stmt.setResource(expr);
            }
        }
        if (this.lexer.token == Token.TO) {
            this.lexer.nextToken();
            while (true) {
                final SQLExpr user = this.parseUser();
                stmt.getUsers().add(user);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token == Token.WITH) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.GRANT) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTION");
                stmt.setWithGrantOption(true);
            }
            while (true) {
                if (this.lexer.identifierEquals("MAX_QUERIES_PER_HOUR")) {
                    this.lexer.nextToken();
                    stmt.setMaxQueriesPerHour(this.exprParser.primary());
                }
                else if (this.lexer.identifierEquals("MAX_UPDATES_PER_HOUR")) {
                    this.lexer.nextToken();
                    stmt.setMaxUpdatesPerHour(this.exprParser.primary());
                }
                else if (this.lexer.identifierEquals("MAX_CONNECTIONS_PER_HOUR")) {
                    this.lexer.nextToken();
                    stmt.setMaxConnectionsPerHour(this.exprParser.primary());
                }
                else {
                    if (!this.lexer.identifierEquals("MAX_USER_CONNECTIONS")) {
                        break;
                    }
                    this.lexer.nextToken();
                    stmt.setMaxUserConnections(this.exprParser.primary());
                }
            }
        }
        if (this.lexer.identifierEquals("ADMIN")) {
            this.lexer.nextToken();
            this.acceptIdentifier("OPTION");
            stmt.setAdminOption(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.identifierEquals("PASSWORD")) {
                this.lexer.nextToken();
                final String password = this.lexer.stringVal();
                this.accept(Token.LITERAL_CHARS);
                stmt.setIdentifiedByPassword(password);
            }
            else {
                stmt.setIdentifiedBy(this.exprParser.expr());
            }
        }
        if (this.lexer.token == Token.WITH) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.GRANT) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTION");
                stmt.setWithGrantOption(true);
            }
        }
        return stmt;
    }
    
    protected void parsePrivileages(final List<SQLPrivilegeItem> privileges, final SQLObject parent) {
        while (true) {
            String privilege = null;
            if (this.lexer.token == Token.ALL) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("PRIVILEGES")) {
                    privilege = "ALL PRIVILEGES";
                    this.lexer.nextToken();
                }
                else {
                    privilege = "ALL";
                }
            }
            else if (this.lexer.token == Token.SELECT) {
                privilege = "SELECT";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.UPDATE) {
                privilege = "UPDATE";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.DELETE) {
                privilege = "DELETE";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.INSERT) {
                privilege = "INSERT";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.INDEX) {
                this.lexer.nextToken();
                privilege = "INDEX";
            }
            else if (this.lexer.token == Token.TRIGGER) {
                this.lexer.nextToken();
                privilege = "TRIGGER";
            }
            else if (this.lexer.token == Token.REFERENCES) {
                privilege = "REFERENCES";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.DESC) {
                privilege = "DESCRIBE";
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.CREATE) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.TABLE) {
                    privilege = "CREATE TABLE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.SESSION) {
                    privilege = "CREATE SESSION";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.TABLESPACE) {
                    privilege = "CREATE TABLESPACE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.USER) {
                    privilege = "CREATE USER";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.VIEW) {
                    privilege = "CREATE VIEW";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.PROCEDURE) {
                    privilege = "CREATE PROCEDURE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.SEQUENCE) {
                    privilege = "CREATE SEQUENCE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.ANY) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.TABLE) {
                        this.lexer.nextToken();
                        privilege = "CREATE ANY TABLE";
                    }
                    else {
                        if (!this.lexer.identifierEquals("MATERIALIZED")) {
                            throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.VIEW);
                        privilege = "CREATE ANY MATERIALIZED VIEW";
                    }
                }
                else if (this.lexer.identifierEquals("SYNONYM")) {
                    privilege = "CREATE SYNONYM";
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals("ROUTINE")) {
                    privilege = "CREATE ROUTINE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals("TEMPORARY")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("TABLES");
                    privilege = "CREATE TEMPORARY TABLES";
                }
                else if (this.lexer.token == Token.ON) {
                    privilege = "CREATE";
                }
                else {
                    if (this.lexer.token != Token.COMMA) {
                        throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                    }
                    privilege = "CREATE";
                }
            }
            else if (this.lexer.token == Token.ALTER) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.TABLE) {
                    privilege = "ALTER TABLE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.SESSION) {
                    privilege = "ALTER SESSION";
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ROUTINE)) {
                    privilege = "ALTER ROUTINE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.ANY) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.TABLE) {
                        this.lexer.nextToken();
                        privilege = "ALTER ANY TABLE";
                    }
                    else {
                        if (!this.lexer.identifierEquals("MATERIALIZED")) {
                            throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.VIEW);
                        privilege = "ALTER ANY MATERIALIZED VIEW";
                    }
                }
                else {
                    if (this.lexer.token != Token.ON && this.lexer.token != Token.COMMA) {
                        throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                    }
                    privilege = "ALTER";
                }
            }
            else if (this.lexer.token == Token.DROP) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.DROP) {
                    privilege = "DROP TABLE";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.SESSION) {
                    privilege = "DROP SESSION";
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.ANY) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.TABLE) {
                        this.lexer.nextToken();
                        privilege = "DROP ANY TABLE";
                    }
                    else {
                        if (!this.lexer.identifierEquals("MATERIALIZED")) {
                            throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.VIEW);
                        privilege = "DROP ANY MATERIALIZED VIEW";
                    }
                }
                else {
                    privilege = "DROP";
                }
            }
            else if (this.lexer.identifierEquals("USAGE")) {
                privilege = "USAGE";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("EXECUTE")) {
                privilege = "EXECUTE";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("PROXY")) {
                privilege = "PROXY";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("QUERY")) {
                this.lexer.nextToken();
                this.acceptIdentifier("REWRITE");
                privilege = "QUERY REWRITE";
            }
            else if (this.lexer.identifierEquals("GLOBAL")) {
                this.lexer.nextToken();
                this.acceptIdentifier("QUERY");
                this.acceptIdentifier("REWRITE");
                privilege = "GLOBAL QUERY REWRITE";
            }
            else if (this.lexer.identifierEquals("INHERIT")) {
                this.lexer.nextToken();
                this.acceptIdentifier("PRIVILEGES");
                privilege = "INHERIT PRIVILEGES";
            }
            else if (this.lexer.identifierEquals("EVENT")) {
                this.lexer.nextToken();
                privilege = "EVENT";
            }
            else if (this.lexer.identifierEquals("FILE")) {
                this.lexer.nextToken();
                privilege = "FILE";
            }
            else if (this.lexer.identifierEquals("DESCRIBE")) {
                this.lexer.nextToken();
                privilege = "DESCRIBE";
            }
            else if (this.lexer.token == Token.GRANT) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTION");
                if (this.lexer.token == Token.FOR) {
                    privilege = "GRANT OPTION FOR";
                    this.lexer.nextToken();
                }
                else {
                    privilege = "GRANT OPTION";
                }
            }
            else if (this.lexer.token == Token.LOCK) {
                this.lexer.nextToken();
                this.acceptIdentifier("TABLES");
                privilege = "LOCK TABLES";
            }
            else if (this.lexer.identifierEquals("PROCESS")) {
                this.lexer.nextToken();
                privilege = "PROCESS";
            }
            else if (this.lexer.identifierEquals("RELOAD")) {
                this.lexer.nextToken();
                privilege = "RELOAD";
            }
            else if (this.lexer.identifierEquals("CONNECT")) {
                privilege = "CONNECT";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("RESOURCE")) {
                this.lexer.nextToken();
                privilege = "RESOURCE";
            }
            else if (this.lexer.token == Token.CONNECT) {
                this.lexer.nextToken();
                privilege = "CONNECT";
            }
            else if (this.lexer.identifierEquals("REPLICATION")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("SLAVE")) {
                    this.lexer.nextToken();
                    privilege = "REPLICATION SLAVE";
                }
                else {
                    this.acceptIdentifier("CLIENT");
                    privilege = "REPLICATION CLIENT";
                }
            }
            else if (this.lexer.token == Token.SHOW) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.VIEW) {
                    this.lexer.nextToken();
                    privilege = "SHOW VIEW";
                }
                else if (this.lexer.identifierEquals("DATABASES")) {
                    this.acceptIdentifier("DATABASES");
                    privilege = "SHOW DATABASES";
                }
                else {
                    privilege = "SHOW";
                }
            }
            else if (this.lexer.identifierEquals("SHUTDOWN")) {
                this.lexer.nextToken();
                privilege = "SHUTDOWN";
            }
            else if (this.lexer.identifierEquals("SUPER")) {
                this.lexer.nextToken();
                privilege = "SUPER";
            }
            else if (this.lexer.identifierEquals("CONTROL")) {
                this.lexer.nextToken();
                privilege = "CONTROL";
            }
            else if (this.lexer.identifierEquals("IMPERSONATE")) {
                this.lexer.nextToken();
                privilege = "IMPERSONATE";
            }
            else if (this.lexer.identifierEquals("LOAD")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("DATA")) {
                    this.lexer.nextToken();
                    privilege = "LOAD DATA";
                }
            }
            else if (this.lexer.identifierEquals("DUMP")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("DATA")) {
                    this.lexer.nextToken();
                    privilege = "DUMP DATA";
                }
            }
            if (privilege != null) {
                final SQLExpr expr = new SQLIdentifierExpr(privilege);
                final SQLPrivilegeItem privilegeItem = new SQLPrivilegeItem();
                privilegeItem.setAction(expr);
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    while (true) {
                        privilegeItem.getColumns().add(this.exprParser.name());
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                expr.setParent(parent);
                privileges.add(privilegeItem);
            }
            if (this.lexer.token != Token.COMMA) {
                return;
            }
            this.lexer.nextToken();
        }
    }
    
    public SQLRevokeStatement parseRevoke() {
        this.accept(Token.REVOKE);
        final SQLRevokeStatement stmt = new SQLRevokeStatement(this.dbType);
        if (this.lexer.token == Token.GRANT) {
            this.lexer.nextToken();
            this.acceptIdentifier("OPTION");
            stmt.setGrantOption(true);
            if (this.lexer.token == Token.FOR) {
                this.lexer.nextToken();
            }
        }
        this.parsePrivileages(stmt.getPrivileges(), stmt);
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            switch (this.lexer.token) {
                case PROCEDURE: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.PROCEDURE);
                    break;
                }
                case FUNCTION: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.FUNCTION);
                    break;
                }
                case TABLE: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.TABLE);
                    break;
                }
                case USER: {
                    this.lexer.nextToken();
                    stmt.setResourceType(SQLObjectType.USER);
                    break;
                }
                case IDENTIFIER: {
                    if (this.lexer.identifierEquals("SYSTEM")) {
                        this.lexer.nextToken();
                        stmt.setResourceType(SQLObjectType.SYSTEM);
                        break;
                    }
                    if (this.lexer.identifierEquals("PROJECT")) {
                        this.lexer.nextToken();
                        stmt.setResourceType(SQLObjectType.PROJECT);
                        break;
                    }
                    break;
                }
            }
            final SQLExpr expr = this.exprParser.expr();
            if (stmt.getResourceType() == SQLObjectType.TABLE || stmt.getResourceType() == null) {
                stmt.setResource(new SQLExprTableSource(expr));
            }
            else {
                stmt.setResource(expr);
            }
        }
        if (this.lexer.token == Token.FROM) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token() == Token.USER && this.dbType == DbType.odps) {
                    this.lexer.nextToken();
                }
                final SQLExpr user = this.parseUser();
                stmt.getUsers().add(user);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return stmt;
    }
    
    public SQLStatement parseSavePoint() {
        this.acceptIdentifier("SAVEPOINT");
        final SQLSavePointStatement stmt = new SQLSavePointStatement(this.getDbType());
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    public SQLStatement parseReleaseSavePoint() {
        this.acceptIdentifier("RELEASE");
        this.acceptIdentifier("SAVEPOINT");
        final SQLReleaseSavePointStatement stmt = new SQLReleaseSavePointStatement(this.getDbType());
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    public SQLStatement parseAlter() {
        final Lexer.SavePoint mark = this.lexer.mark();
        this.accept(Token.ALTER);
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
            final SQLAlterTableStatement stmt = new SQLAlterTableStatement(this.getDbType());
            if (this.lexer.token == Token.IF) {
                this.lexer.nextToken();
                this.accept(Token.EXISTS);
                stmt.setIfExists(true);
            }
            stmt.setName(this.exprParser.name());
            while (true) {
                if (this.lexer.token == Token.DROP) {
                    this.parseAlterDrop(stmt);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
                    this.alterTableAdd(stmt);
                }
                else if (this.lexer.token == Token.DISABLE) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableDisableConstraint item = new SQLAlterTableDisableConstraint();
                        item.setConstraintName(this.exprParser.name());
                        stmt.addItem(item);
                    }
                    else if (this.lexer.identifierEquals("LIFECYCLE")) {
                        this.lexer.nextToken();
                        final SQLAlterTableDisableLifecycle item2 = new SQLAlterTableDisableLifecycle();
                        stmt.addItem(item2);
                    }
                    else {
                        this.acceptIdentifier("KEYS");
                        final SQLAlterTableDisableKeys item3 = new SQLAlterTableDisableKeys();
                        stmt.addItem(item3);
                    }
                }
                else if (this.lexer.token == Token.ENABLE) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableEnableConstraint item4 = new SQLAlterTableEnableConstraint();
                        item4.setConstraintName(this.exprParser.name());
                        stmt.addItem(item4);
                    }
                    else if (this.lexer.identifierEquals("LIFECYCLE")) {
                        this.lexer.nextToken();
                        final SQLAlterTableEnableLifecycle item5 = new SQLAlterTableEnableLifecycle();
                        stmt.addItem(item5);
                    }
                    else {
                        this.acceptIdentifier("KEYS");
                        final SQLAlterTableEnableKeys item6 = new SQLAlterTableEnableKeys();
                        stmt.addItem(item6);
                    }
                }
                else if (this.lexer.token == Token.ALTER) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.COLUMN) {
                        final SQLAlterTableAlterColumn alterColumn = this.parseAlterColumn();
                        stmt.addItem(alterColumn);
                        if (this.dbType != DbType.postgresql || this.lexer.token != Token.COMMA) {
                            continue;
                        }
                        this.lexer.nextToken();
                    }
                    else {
                        if (this.lexer.token != Token.LITERAL_ALIAS) {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                        final SQLAlterTableAlterColumn alterColumn = this.parseAlterColumn();
                        stmt.addItem(alterColumn);
                    }
                }
                else if (this.lexer.token == Token.DELETE) {
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.WHERE) {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    final SQLAlterTableDeleteByCondition alterColumn2 = new SQLAlterTableDeleteByCondition();
                    alterColumn2.setWhere(this.exprParser.expr());
                    stmt.addItem(alterColumn2);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.CHANGE)) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.COLUMN) {
                        this.lexer.nextToken();
                    }
                    final SQLName columnName = this.exprParser.name();
                    if (this.lexer.identifierEquals("RENAME")) {
                        this.lexer.nextToken();
                        this.accept(Token.TO);
                        final SQLName toName = this.exprParser.name();
                        final SQLAlterTableRenameColumn renameColumn = new SQLAlterTableRenameColumn();
                        renameColumn.setColumn(columnName);
                        renameColumn.setTo(toName);
                        stmt.addItem(renameColumn);
                    }
                    else if (this.lexer.token == Token.COMMENT) {
                        this.lexer.nextToken();
                        SQLExpr comment;
                        if (this.lexer.token == Token.LITERAL_ALIAS) {
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
                        final SQLColumnDefinition column = new SQLColumnDefinition();
                        column.setDbType(this.dbType);
                        column.setName(columnName);
                        column.setComment(comment);
                        final SQLAlterTableAlterColumn changeColumn = new SQLAlterTableAlterColumn();
                        changeColumn.setColumn(column);
                        stmt.addItem(changeColumn);
                    }
                    else if (this.lexer.token == Token.NULL) {
                        this.lexer.nextToken();
                        stmt.addItem(new SQLAlterTableAddConstraint(new SQLNullConstraint()));
                    }
                    else {
                        final SQLColumnDefinition column2 = this.exprParser.parseColumn();
                        final SQLAlterTableAlterColumn alterColumn3 = new SQLAlterTableAlterColumn();
                        alterColumn3.setColumn(column2);
                        alterColumn3.setOriginColumn(columnName);
                        if (this.lexer.identifierEquals(FnvHash.Constants.AFTER)) {
                            this.lexer.nextToken();
                            alterColumn3.setAfter(this.exprParser.name());
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.FIRST)) {
                            this.lexer.nextToken();
                            alterColumn3.setFirst(true);
                        }
                        stmt.addItem(alterColumn3);
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.EXCHANGE)) {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableExchangePartition item7 = new SQLAlterTableExchangePartition();
                    this.accept(Token.LPAREN);
                    while (true) {
                        SQLExpr partition = this.exprParser.name();
                        if (this.lexer.token == Token.EQ) {
                            this.lexer.nextToken();
                            final SQLExpr value = this.exprParser.primary();
                            partition = new SQLAssignItem(partition, value);
                        }
                        item7.addPartition(partition);
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                    this.accept(Token.WITH);
                    this.accept(Token.TABLE);
                    final SQLName table = this.exprParser.name();
                    item7.setTable(table);
                    if (this.lexer.token == Token.WITH) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("VALIDATION");
                        item7.setValidation(true);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.WITHOUT)) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("VALIDATION");
                        item7.setValidation(false);
                    }
                    stmt.addItem(item7);
                }
                else if (this.lexer.token == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("NOCHECK");
                    this.acceptIdentifier("ADD");
                    final SQLConstraint check = this.exprParser.parseConstaint();
                    final SQLAlterTableAddConstraint addCheck = new SQLAlterTableAddConstraint();
                    addCheck.setWithNoCheck(true);
                    addCheck.setConstraint(check);
                    stmt.addItem(addCheck);
                }
                else if (this.lexer.identifierEquals("RENAME")) {
                    stmt.addItem(this.parseAlterTableRename());
                }
                else if (this.lexer.token == Token.SET) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.COMMENT) {
                        this.lexer.nextToken();
                        final SQLAlterTableSetComment setComment = new SQLAlterTableSetComment();
                        setComment.setComment(this.exprParser.primary());
                        stmt.addItem(setComment);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                        this.lexer.nextToken();
                        final SQLAlterTableSetLifecycle setLifecycle = new SQLAlterTableSetLifecycle();
                        setLifecycle.setLifecycle(this.exprParser.primary());
                        stmt.addItem(setLifecycle);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
                        this.lexer.nextToken();
                        final SQLAlterTableSetLocation setLocation = new SQLAlterTableSetLocation();
                        setLocation.setLocation(this.exprParser.primary());
                        stmt.addItem(setLocation);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
                        this.lexer.nextToken();
                        final SQLAlterTableSetOption setOption = new SQLAlterTableSetOption();
                        this.accept(Token.LPAREN);
                        while (true) {
                            final SQLAssignItem item8 = this.exprParser.parseAssignItem();
                            setOption.addOption(item8);
                            if (this.lexer.token != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        stmt.addItem(setOption);
                    }
                    else if (this.lexer.identifierEquals("CHANGELOGS") && this.dbType == DbType.odps) {
                        this.lexer.nextToken();
                        final OdpsAlterTableSetChangeLogs item9 = new OdpsAlterTableSetChangeLogs();
                        item9.setValue(this.exprParser.primary());
                        stmt.addItem(item9);
                    }
                    else {
                        if (!this.lexer.identifierEquals("FILEFORMAT") || this.dbType != DbType.odps) {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        final OdpsAlterTableSetFileFormat item10 = new OdpsAlterTableSetFileFormat();
                        item10.setValue(this.exprParser.primary());
                        stmt.addItem(item10);
                    }
                }
                else if (this.lexer.token == Token.PARTITION) {
                    this.lexer.nextToken();
                    final SQLAlterTableRenamePartition renamePartition = new SQLAlterTableRenamePartition();
                    this.accept(Token.LPAREN);
                    this.parseAssignItems(renamePartition.getPartition(), renamePartition);
                    this.accept(Token.RPAREN);
                    if (this.lexer.token == Token.ENABLE) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("LIFECYCLE")) {
                            this.lexer.nextToken();
                        }
                        final SQLAlterTableEnableLifecycle enableLifeCycle = new SQLAlterTableEnableLifecycle();
                        for (final SQLAssignItem condition : renamePartition.getPartition()) {
                            enableLifeCycle.getPartition().add(condition);
                            condition.setParent(enableLifeCycle);
                        }
                        stmt.addItem(enableLifeCycle);
                    }
                    else if (this.lexer.token == Token.DISABLE) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("LIFECYCLE")) {
                            this.lexer.nextToken();
                        }
                        final SQLAlterTableDisableLifecycle disableLifeCycle = new SQLAlterTableDisableLifecycle();
                        for (final SQLAssignItem condition : renamePartition.getPartition()) {
                            disableLifeCycle.getPartition().add(condition);
                            condition.setParent(disableLifeCycle);
                        }
                        stmt.addItem(disableLifeCycle);
                    }
                    else {
                        if (DbType.odps == this.dbType) {
                            if (this.lexer.identifierEquals("MERGE")) {
                                final SQLAlterTablePartition alterTablePartition = new SQLAlterTablePartition();
                                for (final SQLAssignItem condition : renamePartition.getPartition()) {
                                    alterTablePartition.getPartition().add(condition);
                                    condition.setParent(alterTablePartition);
                                }
                                stmt.addItem(alterTablePartition);
                                continue;
                            }
                            if (this.lexer.token == Token.SET) {
                                final SQLAlterTablePartitionSetProperties alterTablePartition2 = new SQLAlterTablePartitionSetProperties();
                                for (final SQLAssignItem condition : renamePartition.getPartition()) {
                                    alterTablePartition2.getPartition().add(condition);
                                    condition.setParent(alterTablePartition2);
                                }
                                this.lexer.nextToken();
                                this.acceptIdentifier("PARTITIONPROPERTIES");
                                this.accept(Token.LPAREN);
                                this.parseAssignItems(alterTablePartition2.getPartitionProperties(), alterTablePartition2);
                                this.accept(Token.RPAREN);
                                stmt.addItem(alterTablePartition2);
                                continue;
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
                                this.alterTableAdd(stmt);
                                continue;
                            }
                        }
                        this.acceptIdentifier("RENAME");
                        this.accept(Token.TO);
                        this.accept(Token.PARTITION);
                        this.accept(Token.LPAREN);
                        this.parseAssignItems(renamePartition.getTo(), renamePartition);
                        this.accept(Token.RPAREN);
                        stmt.addItem(renamePartition);
                    }
                }
                else if (this.lexer.identifierEquals("TOUCH")) {
                    this.lexer.nextToken();
                    final SQLAlterTableTouch item11 = new SQLAlterTableTouch();
                    if (this.lexer.token == Token.PARTITION) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        this.parseAssignItems(item11.getPartition(), item11);
                        this.accept(Token.RPAREN);
                    }
                    stmt.addItem(item11);
                }
                else if (this.lexer.identifierEquals("CHANGEOWNER")) {
                    this.lexer.nextToken();
                    this.accept(Token.TO);
                    final SQLName name = this.exprParser.name();
                    final SQLAlterTableChangeOwner changeOwner = new SQLAlterTableChangeOwner();
                    changeOwner.setOwner(name);
                    stmt.addItem(changeOwner);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ARCHIVE)) {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableArchivePartition item12 = new SQLAlterTableArchivePartition();
                    this.accept(Token.LPAREN);
                    this.parseAssignItems(item12.getPartitions(), item12, false);
                    this.accept(Token.RPAREN);
                    stmt.addItem(item12);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.UNARCHIVE)) {
                    this.lexer.nextToken();
                    this.accept(Token.PARTITION);
                    final SQLAlterTableUnarchivePartition item13 = new SQLAlterTableUnarchivePartition();
                    this.accept(Token.LPAREN);
                    this.parseAssignItems(item13.getPartitions(), item13, false);
                    this.accept(Token.RPAREN);
                    stmt.addItem(item13);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION_AVAILABLE_PARTITION_NUM)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    final SQLIntegerExpr num = this.exprParser.integerExpr();
                    final SQLAlterTableSubpartitionAvailablePartitionNum item14 = new SQLAlterTableSubpartitionAvailablePartitionNum();
                    item14.setNumber(num);
                    stmt.addItem(item14);
                }
                else if (DbType.odps == this.dbType && this.lexer.identifierEquals("MERGE")) {
                    this.lexer.nextToken();
                    boolean ifExists = false;
                    if (this.lexer.token == Token.IF) {
                        this.lexer.nextToken();
                        this.accept(Token.EXISTS);
                        ifExists = true;
                    }
                    if (this.lexer.token == Token.PARTITION) {
                        final SQLAlterTableMergePartition item15 = new SQLAlterTableMergePartition();
                        while (true) {
                            item15.addPartition(this.getExprParser().parsePartitionSpec());
                            if (this.lexer.token != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.OVERWRITE);
                        item15.setOverwritePartition(this.getExprParser().parsePartitionSpec());
                        if (ifExists) {
                            item15.setIfExists(true);
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.PURGE)) {
                            this.lexer.nextToken();
                            item15.setPurge(true);
                        }
                        stmt.addItem(item15);
                    }
                    else {
                        this.acceptIdentifier("SMALLFILES");
                        stmt.setMergeSmallFiles(true);
                    }
                }
                else if (DbType.odps == this.dbType && (this.lexer.identifierEquals(FnvHash.Constants.RANGE) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED))) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("CLUSTERED");
                        stmt.setRange(true);
                    }
                    else {
                        this.lexer.nextToken();
                    }
                    this.accept(Token.BY);
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLSelectOrderByItem item16 = this.exprParser.parseSelectOrderByItem();
                        stmt.addClusteredByItem(item16);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                else if (DbType.odps == this.dbType && this.lexer.identifierEquals(FnvHash.Constants.SORTED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLSelectOrderByItem item16 = this.exprParser.parseSelectOrderByItem();
                        stmt.addSortedByItem(item16);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                else if ((stmt.getClusteredBy().size() > 0 || stmt.getSortedBy().size() > 0) && this.lexer.token == Token.INTO) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT) {
                        throw new ParserException("into buckets must be integer. " + this.lexer.info());
                    }
                    final int num2 = this.lexer.integerValue().intValue();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.BUCKETS)) {
                        stmt.setBuckets(num2);
                        this.lexer.nextToken();
                    }
                    else {
                        this.acceptIdentifier("SHARDS");
                        stmt.setShards(num2);
                    }
                }
                else if (this.lexer.token == Token.REPLACE) {
                    final SQLAlterTableReplaceColumn item17 = this.parseAlterTableReplaceColumn();
                    stmt.addItem(item17);
                }
                else if (DbType.hive == this.dbType && this.lexer.identifierEquals(FnvHash.Constants.RECOVER)) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("PARTITIONS");
                    stmt.addItem(new SQLAlterTableRecoverPartitions());
                }
                else {
                    if (this.dbType != DbType.odps || this.lexer.token != Token.NOT) {
                        return stmt;
                    }
                    this.lexer.nextToken();
                    this.acceptIdentifier("CLUSTERED");
                    stmt.setNotClustered(true);
                }
            }
        }
        else {
            if (this.lexer.token == Token.VIEW) {
                this.lexer.nextToken();
                final SQLName viewName = this.exprParser.name();
                final SQLAlterViewRenameStatement stmt2 = new SQLAlterViewRenameStatement();
                stmt2.setName(viewName);
                if (this.lexer.identifierEquals("RENAME")) {
                    this.lexer.nextToken();
                    this.accept(Token.TO);
                    stmt2.setTo(this.exprParser.name());
                }
                if (this.lexer.identifierEquals("CHANGEOWNER")) {
                    this.lexer.nextToken();
                    this.accept(Token.TO);
                    stmt2.setChangeOwnerTo(this.exprParser.name());
                }
                return stmt2;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                return this.parseAlterMaterialized();
            }
            if (this.lexer.token == Token.INDEX) {
                this.lexer.reset(mark);
                return this.parseAlterIndex();
            }
            if (this.lexer.token == Token.DATABASE) {
                this.lexer.reset(mark);
                return this.parseAlterDatabase();
            }
            if (this.lexer.token == Token.SCHEMA) {
                this.lexer.reset(mark);
                return this.parseAlterSchema();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                this.lexer.reset(mark);
                return this.parseAlterResourceGroup();
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
    }
    
    private void alterTableAdd(final SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        boolean ifNotExists = false;
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            ifNotExists = true;
        }
        if (this.lexer.token == Token.PRIMARY) {
            final SQLPrimaryKey primaryKey = this.exprParser.parsePrimaryKey();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(primaryKey);
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.UNIQUE) {
            final SQLUnique unique = this.exprParser.parseUnique();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(unique);
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.IDENTIFIER) {
            final SQLAlterTableAddColumn item2 = this.parseAlterTableAddColumn();
            stmt.addItem(item2);
        }
        else if (this.lexer.token == Token.LPAREN) {
            if (this.dbType == DbType.h2) {
                this.lexer.nextToken();
                final SQLAlterTableAddColumn item2 = this.parseAlterTableAddColumn();
                stmt.addItem(item2);
                this.accept(Token.RPAREN);
            }
        }
        else if (this.lexer.token == Token.COLUMN) {
            this.lexer.nextToken();
            final SQLAlterTableAddColumn item2 = this.parseAlterTableAddColumn();
            stmt.addItem(item2);
        }
        else if (this.lexer.token == Token.CHECK) {
            final SQLCheck check = this.exprParser.parseCheck();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(check);
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.CONSTRAINT) {
            final SQLConstraint constraint = this.exprParser.parseConstaint();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(constraint);
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.FOREIGN) {
            final SQLConstraint constraint = this.exprParser.parseForeignKey();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(constraint);
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.PARTITION) {
            while (true) {
                this.lexer.nextToken();
                final SQLAlterTableAddPartition addPartition = new SQLAlterTableAddPartition();
                addPartition.setIfNotExists(ifNotExists);
                this.accept(Token.LPAREN);
                this.parseAssignItems(addPartition.getPartitions(), addPartition, false);
                this.accept(Token.RPAREN);
                if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
                    this.lexer.nextToken();
                    final SQLExpr location = this.exprParser.primary();
                    addPartition.setLocation(location);
                }
                stmt.addItem(addPartition);
                if (this.lexer.token == Token.PARTITION) {
                    continue;
                }
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("ADD")) {
                    continue;
                }
                if (this.lexer.token == Token.PARTITION) {
                    continue;
                }
                break;
            }
        }
        else {
            if (this.lexer.token != Token.DEFAULT) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            final SQLConstraint constraint = this.exprParser.parseConstaint();
            final SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(constraint);
            stmt.addItem(item);
        }
    }
    
    protected SQLStatement parseAlterDatabase() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLStatement parseAlterSchema() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLAlterTableItem parseAlterTableRename() {
        this.acceptIdentifier("RENAME");
        if (this.lexer.token == Token.COLUMN) {
            this.lexer.nextToken();
            final SQLAlterTableRenameColumn renameColumn = new SQLAlterTableRenameColumn();
            renameColumn.setColumn(this.exprParser.name());
            this.accept(Token.TO);
            renameColumn.setTo(this.exprParser.name());
            return renameColumn;
        }
        if (this.lexer.token == Token.TO) {
            this.lexer.nextToken();
            final SQLAlterTableRename item = new SQLAlterTableRename();
            item.setTo(this.exprParser.name());
            return item;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLAlterTableAlterColumn parseAlterColumn() {
        this.lexer.nextToken();
        final SQLColumnDefinition column = this.exprParser.parseColumn();
        final SQLAlterTableAlterColumn alterColumn = new SQLAlterTableAlterColumn();
        alterColumn.setColumn(column);
        return alterColumn;
    }
    
    public void parseAlterDrop(final SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        boolean ifExists = false;
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            ifExists = true;
        }
        if (this.lexer.token == Token.CONSTRAINT) {
            this.lexer.nextToken();
            final SQLAlterTableDropConstraint item = new SQLAlterTableDropConstraint();
            item.setConstraintName(this.exprParser.name());
            if (this.lexer.token == Token.RESTRICT) {
                this.lexer.nextToken();
                item.setRestrict(true);
            }
            else if (this.lexer.token == Token.CASCADE) {
                this.lexer.nextToken();
                item.setCascade(true);
            }
            stmt.addItem(item);
        }
        else if (this.lexer.token == Token.COLUMN || this.lexer.identifierEquals(FnvHash.Constants.COLUMNS)) {
            this.lexer.nextToken();
            final SQLAlterTableDropColumnItem item2 = new SQLAlterTableDropColumnItem();
            if (this.dbType == DbType.postgresql) {
                item2.getColumns().add(this.exprParser.name());
            }
            else {
                boolean paren = false;
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    paren = true;
                }
                this.exprParser.names(item2.getColumns());
                if (paren) {
                    this.accept(Token.RPAREN);
                }
            }
            if (this.lexer.token == Token.CASCADE) {
                item2.setCascade(true);
                this.lexer.nextToken();
            }
            stmt.addItem(item2);
            if (this.dbType == DbType.postgresql && this.lexer.token == Token.COMMA) {
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token == Token.LITERAL_ALIAS) {
            final SQLAlterTableDropColumnItem item2 = new SQLAlterTableDropColumnItem();
            this.exprParser.names(item2.getColumns());
            if (this.lexer.token == Token.CASCADE) {
                item2.setCascade(true);
                this.lexer.nextToken();
            }
            stmt.addItem(item2);
        }
        else if (this.lexer.token == Token.PARTITION) {
            final SQLAlterTableDropPartition dropPartition = this.parseAlterTableDropPartition(ifExists);
            stmt.addItem(dropPartition);
            while (this.lexer.token == Token.COMMA) {
                this.lexer.nextToken();
                final Lexer.SavePoint mark = this.lexer.mark();
                if (this.lexer.token == Token.PARTITION) {
                    final SQLAlterTableDropPartition dropPartition2 = this.parseAlterTableDropPartition(ifExists);
                    stmt.addItem(dropPartition2);
                }
                else {
                    this.lexer.reset(mark);
                }
            }
        }
        else if (this.lexer.token == Token.INDEX) {
            this.lexer.nextToken();
            final SQLName indexName = this.exprParser.name();
            final SQLAlterTableDropIndex item3 = new SQLAlterTableDropIndex();
            item3.setIndexName(indexName);
            stmt.addItem(item3);
        }
        else {
            if (this.lexer.token != Token.PRIMARY) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.KEY);
            final SQLAlterTableDropPrimaryKey item4 = new SQLAlterTableDropPrimaryKey();
            stmt.addItem(item4);
        }
    }
    
    protected SQLAlterTableDropPartition parseAlterTableDropPartition(final boolean ifExists) {
        this.lexer.nextToken();
        final SQLAlterTableDropPartition dropPartition = new SQLAlterTableDropPartition();
        dropPartition.setIfExists(ifExists);
        if (this.lexer.token == Token.LPAREN) {
            this.accept(Token.LPAREN);
            this.exprParser.exprList(dropPartition.getPartitions(), dropPartition);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals("PURGE")) {
                this.lexer.nextToken();
                dropPartition.setPurge(true);
            }
        }
        else {
            while (true) {
                final SQLExpr partition = this.exprParser.expr();
                dropPartition.addPartition(partition);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            dropPartition.getAttributes().put("SIMPLE", true);
        }
        return dropPartition;
    }
    
    protected SQLAlterTableDropSubpartition parseAlterTableDropSubpartition() {
        this.lexer.nextToken();
        final SQLAlterTableDropSubpartition item = new SQLAlterTableDropSubpartition();
        if (this.lexer.token() == Token.LITERAL_INT) {
            while (true) {
                item.getPartitionIds().add(this.exprParser.integerExpr());
                final String pidStr = this.lexer.stringVal();
                this.accept(Token.VARIANT);
                final String s = pidStr.replaceAll(":", "");
                if (StringUtils.isEmpty(s)) {
                    item.getSubpartitionIds().add(this.exprParser.integerExpr());
                }
                else {
                    item.getSubpartitionIds().add(new SQLIntegerExpr(Integer.valueOf(s)));
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return item;
    }
    
    public SQLStatement parseRename() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLStatement parseList() {
        if (this.lexer.identifierEquals(FnvHash.Constants.LIST)) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.GROUP) || this.lexer.token == Token.GROUP) {
                    this.lexer.nextToken();
                    return new SQLListResourceGroupStatement();
                }
            }
        }
        return null;
    }
    
    protected SQLDropTableStatement parseDropTable(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        final SQLDropTableStatement stmt = new SQLDropTableStatement(this.getDbType());
        if (this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY)) {
            this.lexer.nextToken();
            stmt.setTemporary(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONED)) {
            this.lexer.nextToken();
            stmt.setDropPartition(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExternal(true);
        }
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
        }
        else {
            if (!this.lexer.identifierEquals(FnvHash.Constants.TABLES) || this.dbType != DbType.mysql) {
                throw new ParserException("expected token: TABLE.");
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        while (true) {
            final SQLName name = this.exprParser.name();
            stmt.addPartition(new SQLExprTableSource(name));
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        while (true) {
            if (this.lexer.identifierEquals("RESTRICT")) {
                this.lexer.nextToken();
                stmt.setRestrict(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.CASCADE)) {
                this.lexer.nextToken();
                stmt.setCascade(true);
                if (!this.lexer.identifierEquals("CONSTRAINTS")) {
                    continue;
                }
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token != Token.PURGE && !this.lexer.identifierEquals("PURGE")) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setPurge(true);
            }
        }
        if (stmt.isDropPartition() && this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        return stmt;
    }
    
    protected SQLDropSequenceStatement parseDropSequence(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        this.lexer.nextToken();
        final SQLName name = this.exprParser.name();
        final SQLDropSequenceStatement stmt = new SQLDropSequenceStatement(this.getDbType());
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLDropTriggerStatement parseDropTrigger(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        this.lexer.nextToken();
        final SQLDropTriggerStatement stmt = new SQLDropTriggerStatement(this.getDbType());
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLDropViewStatement parseDropView(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        final SQLDropViewStatement stmt = new SQLDropViewStatement(this.getDbType());
        this.accept(Token.VIEW);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        while (true) {
            final SQLName name = this.exprParser.name();
            stmt.addPartition(new SQLExprTableSource(name));
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("RESTRICT")) {
            this.lexer.nextToken();
            stmt.setRestrict(true);
        }
        else if (this.lexer.identifierEquals("CASCADE")) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("CONSTRAINTS")) {
                this.lexer.nextToken();
            }
            stmt.setCascade(true);
        }
        return stmt;
    }
    
    protected SQLDropStatement parseDropSchema() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected SQLDropStatement parseDropDatabaseOrSchema(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        final SQLDropDatabaseStatement stmt = new SQLDropDatabaseStatement(this.getDbType());
        if (this.lexer.token == Token.SCHEMA) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setDatabase(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.RESTRICT)) {
            this.lexer.nextToken();
            stmt.setRestrict(true);
        }
        else if (this.lexer.token == Token.CASCADE || this.lexer.identifierEquals(FnvHash.Constants.CASCADE)) {
            this.lexer.nextToken();
            stmt.setCascade(true);
        }
        else {
            stmt.setCascade(false);
        }
        return stmt;
    }
    
    protected SQLDropFunctionStatement parseDropFunction(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        final SQLDropFunctionStatement stmt = new SQLDropFunctionStatement(this.getDbType());
        this.accept(Token.FUNCTION);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    protected SQLDropTableSpaceStatement parseDropTablespace(final boolean acceptDrop) {
        final SQLDropTableSpaceStatement stmt = new SQLDropTableSpaceStatement(this.getDbType());
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            stmt.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        this.accept(Token.TABLESPACE);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr engine = this.exprParser.primary();
            stmt.setEngine(engine);
        }
        return stmt;
    }
    
    protected SQLDropProcedureStatement parseDropProcedure(final boolean acceptDrop) {
        if (acceptDrop) {
            this.accept(Token.DROP);
        }
        final SQLDropProcedureStatement stmt = new SQLDropProcedureStatement(this.getDbType());
        this.accept(Token.PROCEDURE);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        return stmt;
    }
    
    public SQLStatement parseTruncate() {
        this.accept(Token.TRUNCATE);
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
        }
        final SQLTruncateStatement stmt = new SQLTruncateStatement(this.getDbType());
        if (this.lexer.token == Token.ONLY) {
            this.lexer.nextToken();
            stmt.setOnly(true);
        }
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        while (true) {
            final SQLName name = this.exprParser.name();
            stmt.addTableSource(name);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.PARTITION) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.LPAREN) {
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLAssignItem item = this.exprParser.parseAssignItem();
                    item.setParent(stmt);
                    stmt.getPartitions().add(item);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            else if (this.lexer.token == Token.ALL) {
                this.lexer.nextToken();
                stmt.setPartitionAll(true);
            }
            else {
                while (true) {
                    stmt.getPartitionsForADB().add(this.exprParser.integerExpr());
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
        }
        while (true) {
            if (this.lexer.token == Token.PURGE) {
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals("SNAPSHOT")) {
                    throw new ParserException("TODO : " + this.lexer.token + " " + this.lexer.stringVal());
                }
                this.lexer.nextToken();
                this.acceptIdentifier("LOG");
                stmt.setPurgeSnapshotLog(true);
            }
            else if (this.lexer.token == Token.RESTART) {
                this.lexer.nextToken();
                this.accept(Token.IDENTITY);
                stmt.setRestartIdentity(Boolean.TRUE);
            }
            else if (this.lexer.token == Token.SHARE) {
                this.lexer.nextToken();
                this.accept(Token.IDENTITY);
                stmt.setRestartIdentity(Boolean.FALSE);
            }
            else if (this.lexer.token == Token.CASCADE) {
                this.lexer.nextToken();
                stmt.setCascade(Boolean.TRUE);
            }
            else if (this.lexer.token == Token.RESTRICT) {
                this.lexer.nextToken();
                stmt.setCascade(Boolean.FALSE);
            }
            else if (this.lexer.token == Token.DROP) {
                this.lexer.nextToken();
                this.acceptIdentifier("STORAGE");
                stmt.setDropStorage(true);
            }
            else if (this.lexer.identifierEquals("REUSE")) {
                this.lexer.nextToken();
                this.acceptIdentifier("STORAGE");
                stmt.setReuseStorage(true);
            }
            else if (this.lexer.identifierEquals("IGNORE")) {
                this.lexer.nextToken();
                this.accept(Token.DELETE);
                this.acceptIdentifier("TRIGGERS");
                stmt.setIgnoreDeleteTriggers(true);
            }
            else if (this.lexer.identifierEquals("RESTRICT")) {
                this.lexer.nextToken();
                this.accept(Token.WHEN);
                this.accept(Token.DELETE);
                this.acceptIdentifier("TRIGGERS");
                stmt.setRestrictWhenDeleteTriggers(true);
            }
            else if (this.lexer.token == Token.CONTINUE) {
                this.lexer.nextToken();
                this.accept(Token.IDENTITY);
            }
            else {
                if (!this.lexer.identifierEquals("IMMEDIATE")) {
                    return stmt;
                }
                this.lexer.nextToken();
                stmt.setImmediate(true);
            }
        }
    }
    
    public SQLStatement parseInsert() {
        final SQLInsertStatement stmt = new SQLInsertStatement();
        if (this.lexer.token == Token.INSERT) {
            this.accept(Token.INSERT);
        }
        this.parseInsert0(stmt);
        return stmt;
    }
    
    protected void parseInsert0(final SQLInsertInto insertStatement) {
        this.parseInsert0(insertStatement, true);
    }
    
    protected void parseInsert0_hinits(final SQLInsertInto insertStatement) {
    }
    
    protected void parseInsert0(final SQLInsertInto insertStatement, final boolean acceptSubQuery) {
        if (this.lexer.token == Token.INTO) {
            this.lexer.nextToken();
            final SQLName tableName = this.exprParser.name();
            insertStatement.setTableName(tableName);
            if (this.lexer.token == Token.LITERAL_ALIAS) {
                insertStatement.setAlias(this.tableAlias());
            }
            this.parseInsert0_hinits(insertStatement);
            if (this.lexer.token == Token.IDENTIFIER) {
                insertStatement.setAlias(this.lexer.stringVal());
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.parseInsertColumns(insertStatement);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.VALUES) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
                    this.exprParser.exprList(values.getValues(), values);
                    insertStatement.addValueCause(values);
                    this.accept(Token.RPAREN);
                }
                else {
                    final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
                    final SQLExpr value = this.exprParser.expr();
                    values.addValue(value);
                    insertStatement.addValueCause(values);
                }
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (acceptSubQuery && (this.lexer.token == Token.SELECT || this.lexer.token == Token.LPAREN)) {
            final SQLSelect select = this.createSQLSelectParser().select();
            insertStatement.setQuery(select);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
            throw new ParserException("'values' expected, but 'value'. " + this.lexer.info());
        }
    }
    
    protected void parseInsertColumns(final SQLInsertInto insert) {
        this.exprParser.exprList(insert.getColumns(), insert);
    }
    
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        return false;
    }
    
    public SQLDropUserStatement parseDropUser() {
        this.accept(Token.USER);
        final SQLDropUserStatement stmt = new SQLDropUserStatement(this.getDbType());
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        while (true) {
            if (this.lexer.token == Token.IF) {
                this.lexer.nextToken();
                this.accept(Token.EXISTS);
            }
            final SQLExpr expr = this.exprParser.expr();
            stmt.addUser(expr);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return stmt;
    }
    
    public SQLStatement parseDropIndex() {
        this.accept(Token.INDEX);
        final SQLDropIndexStatement stmt = new SQLDropIndexStatement(this.getDbType());
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        stmt.setIndexName(this.exprParser.name());
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            stmt.setTableName(this.exprParser.name());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr algorithm = this.exprParser.primary();
            stmt.setAlgorithm(algorithm);
        }
        if (this.lexer.token == Token.LOCK) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr option = this.exprParser.primary();
            stmt.setLockOption(option);
        }
        return stmt;
    }
    
    public SQLCallStatement parseCall() {
        boolean brace = false;
        if (this.lexer.token == Token.LBRACE) {
            this.lexer.nextToken();
            brace = true;
        }
        final SQLCallStatement stmt = new SQLCallStatement(this.getDbType());
        if (this.lexer.token == Token.QUES) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            stmt.setOutParameter(new SQLVariantRefExpr("?"));
        }
        this.acceptIdentifier("CALL");
        stmt.setProcedureName(this.exprParser.name());
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getParameters(), stmt);
            this.accept(Token.RPAREN);
        }
        if (brace) {
            this.accept(Token.RBRACE);
            stmt.setBrace(true);
        }
        return stmt;
    }
    
    public SQLStatement parseSet() {
        this.accept(Token.SET);
        final SQLSetStatement stmt = new SQLSetStatement(this.getDbType());
        this.parseAssignItems(stmt.getItems(), stmt);
        return stmt;
    }
    
    public void parseAssignItems(final List<? super SQLAssignItem> items, final SQLObject parent) {
        this.parseAssignItems(items, parent, true);
    }
    
    public void parseAssignItems(final List<? super SQLAssignItem> items, final SQLObject parent, final boolean variant) {
        while (true) {
            final SQLAssignItem item = this.exprParser.parseAssignItem(variant, parent);
            item.setParent(parent);
            items.add(item);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    public SQLPartitionRef parsePartitionRef() {
        this.accept(Token.PARTITION);
        final SQLPartitionRef partitionRef = new SQLPartitionRef();
        this.accept(Token.LPAREN);
        while (true) {
            final SQLIdentifierExpr name = (SQLIdentifierExpr)this.exprParser.name();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
                final SQLExpr value = this.exprParser.expr();
                partitionRef.addItem(name, value);
            }
            else {
                partitionRef.addItem(new SQLPartitionRef.Item(name));
            }
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        return partitionRef;
    }
    
    public SQLStatement parseCreatePackage() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLStatement parseCreate() {
        final Lexer.SavePoint mark = this.lexer.mark();
        List<String> comments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            comments = this.lexer.readAndResetComments();
        }
        this.accept(Token.CREATE);
        boolean global = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.GLOBAL)) {
            this.lexer.nextToken();
            global = true;
        }
        boolean temporary = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY) || this.lexer.token == Token.TEMPORARY) {
            this.lexer.nextToken();
            temporary = true;
        }
        boolean nonclustered = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.NONCLUSTERED)) {
            this.lexer.nextToken();
            nonclustered = true;
        }
        final Token token = this.lexer.token;
        switch (this.lexer.token) {
            case TABLE: {
                this.lexer.reset(mark);
                final SQLCreateTableParser createTableParser = this.getSQLCreateTableParser();
                final SQLCreateTableStatement stmt = createTableParser.parseCreateTable();
                if (temporary) {
                    if (global) {
                        stmt.setType(SQLCreateTableStatement.Type.GLOBAL_TEMPORARY);
                    }
                    else {
                        stmt.setType(SQLCreateTableStatement.Type.TEMPORARY);
                    }
                }
                if (comments != null) {
                    stmt.addBeforeComment(comments);
                }
                return stmt;
            }
            case INDEX:
            case UNIQUE: {
                final SQLCreateIndexStatement createIndex = this.parseCreateIndex(false);
                if (nonclustered) {
                    createIndex.setType("NONCLUSTERED");
                }
                return createIndex;
            }
            case SEQUENCE: {
                return this.parseCreateSequence(false);
            }
            case DATABASE: {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("LINK")) {
                    this.lexer.reset(mark);
                    return this.parseCreateDbLink();
                }
                this.lexer.reset(mark);
                final SQLStatement stmt2 = this.parseCreateDatabase();
                if (comments != null) {
                    stmt2.addBeforeComment(comments);
                    comments = null;
                }
                return stmt2;
            }
            case SCHEMA: {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("LINK")) {
                    this.lexer.reset(mark);
                    return this.parseCreateDbLink();
                }
                this.lexer.reset(mark);
                final SQLStatement stmt2 = this.parseCreateSchema();
                if (comments != null) {
                    stmt2.addBeforeComment(comments);
                    comments = null;
                }
                return stmt2;
            }
            case USER: {
                this.lexer.reset(mark);
                return this.parseCreateUser();
            }
            case FUNCTION: {
                this.lexer.reset(mark);
                final SQLStatement stmt3;
                final SQLStatement createFunct = stmt3 = this.parseCreateFunction();
                return stmt3;
            }
            default: {
                if (token == Token.OR) {
                    this.lexer.nextToken();
                    this.accept(Token.REPLACE);
                    if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                        this.lexer.nextToken();
                    }
                    if (this.lexer.token == Token.PROCEDURE) {
                        this.lexer.reset(mark);
                        return this.parseCreateProcedure();
                    }
                    if (this.lexer.token == Token.VIEW) {
                        this.lexer.reset(mark);
                        return this.parseCreateView();
                    }
                    if (this.lexer.token == Token.TRIGGER) {
                        this.lexer.reset(mark);
                        return this.parseCreateTrigger();
                    }
                    if (this.lexer.token == Token.FUNCTION || this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
                        this.lexer.reset(mark);
                        return this.parseCreateFunction();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.PACKAGE)) {
                        this.lexer.reset(mark);
                        return this.parseCreatePackage();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateType();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.PUBLIC)) {
                        this.lexer.reset(mark);
                        return this.parseCreateSynonym();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.SYNONYM)) {
                        this.lexer.reset(mark);
                        return this.parseCreateSynonym();
                    }
                    throw new ParserException("TODO " + this.lexer.info());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.PUBLIC)) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("SYNONYM")) {
                        this.lexer.reset(mark);
                        return this.parseCreateSynonym();
                    }
                    this.lexer.reset(mark);
                    return this.parseCreateDbLink();
                }
                else {
                    if (this.lexer.identifierEquals("SHARE")) {
                        this.lexer.reset(mark);
                        return this.parseCreateDbLink();
                    }
                    if (this.lexer.identifierEquals("SYNONYM")) {
                        this.lexer.reset(mark);
                        return this.parseCreateSynonym();
                    }
                    if (token == Token.VIEW) {
                        return this.parseCreateView();
                    }
                    if (token == Token.TRIGGER) {
                        this.lexer.reset(mark);
                        return this.parseCreateTrigger();
                    }
                    if (token == Token.PROCEDURE) {
                        final SQLCreateProcedureStatement stmt4 = this.parseCreateProcedure();
                        stmt4.setCreate(true);
                        return stmt4;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.BITMAP)) {
                        this.lexer.reset(mark);
                        return this.parseCreateIndex(true);
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                        this.lexer.reset(mark);
                        return this.parseCreateMaterializedView();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateType();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
                        this.lexer.reset(mark);
                        final SQLCreateTableStatement createTable = this.parseCreateTable();
                        if (comments != null) {
                            createTable.addBeforeComment(comments);
                            comments = null;
                        }
                        return createTable;
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUP)) {
                        this.lexer.reset(mark);
                        return this.parseCreateTableGroup();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.SQL) && this.dbType == DbType.odps) {
                        this.lexer.reset(mark);
                        return this.parseCreateFunction();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.DIMENSION)) {
                        this.lexer.reset(mark);
                        return this.parseCreateTable();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.ROLE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateRole();
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCE)) {
                        this.lexer.reset(mark);
                        return this.parseCreateResourceGroup();
                    }
                    if (this.lexer.token() == Token.FOREIGN) {
                        this.lexer.reset(mark);
                        return this.parseCreateTable();
                    }
                    throw new ParserException("TODO " + this.lexer.info());
                }
                break;
            }
        }
    }
    
    public SQLStatement parseCreateRole() {
        this.accept(Token.CREATE);
        this.acceptIdentifier("ROLE");
        final SQLName name = this.exprParser.name();
        final SQLCreateRoleStatement stmt = new SQLCreateRoleStatement(this.dbType);
        stmt.setName(name);
        return stmt;
    }
    
    public SQLStatement parseCreateType() {
        throw new ParserException("TODO " + this.lexer.token);
    }
    
    public SQLStatement parseCreateTableGroup() {
        this.accept(Token.CREATE);
        this.acceptIdentifier("TABLEGROUP");
        final SQLCreateTableGroupStatement stmt = new SQLCreateTableGroupStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.IF)) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.token == Token.PARTITION || this.lexer.identifierEquals("PARTITION")) {
            this.lexer.nextToken();
            this.acceptIdentifier("NUM");
            final SQLExpr num = this.exprParser.expr();
            stmt.setPartitionNum(num);
        }
        return stmt;
    }
    
    public SQLStatement parseCreateUser() {
        this.accept(Token.CREATE);
        this.accept(Token.USER);
        final SQLCreateUserStatement stmt = new SQLCreateUserStatement();
        stmt.setUser(this.exprParser.name());
        this.acceptIdentifier("IDENTIFIED");
        this.accept(Token.BY);
        stmt.setPassword(this.exprParser.primary());
        return stmt;
    }
    
    public SQLCreateFunctionStatement parseCreateFunction() {
        throw new ParserException("TODO " + this.lexer.token);
    }
    
    public SQLStatement parseCreateMaterializedView() {
        this.accept(Token.CREATE);
        this.acceptIdentifier("MATERIALIZED");
        this.accept(Token.VIEW);
        final SQLCreateMaterializedViewStatement stmt = new SQLCreateMaterializedViewStatement();
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        stmt.setName(this.exprParser.name());
        if (this.dbType == DbType.mysql) {
            stmt.setDbType(DbType.mysql);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                while (true) {
                    final Token token = this.lexer.token;
                    if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.KEY) {
                            final MySqlKey clsKey = new MySqlKey();
                            this.exprParser.parseIndex(clsKey.getIndexDefinition());
                            clsKey.setIndexType("CLUSTERED");
                            clsKey.setParent(stmt);
                            stmt.getTableElementList().add(clsKey);
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else if (this.lexer.token() == Token.INDEX) {
                            final MySqlTableIndex idx = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx.getIndexDefinition());
                            idx.setIndexType("CLUSTERED");
                            idx.setParent(stmt);
                            stmt.getTableElementList().add(idx);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                    }
                    if (token == Token.IDENTIFIER) {
                        final SQLColumnDefinition column = this.exprParser.parseColumn(stmt);
                        stmt.getTableElementList().add(column);
                    }
                    else if (token == Token.PRIMARY || token == Token.UNIQUE || token == Token.CHECK || token == Token.CONSTRAINT || token == Token.FOREIGN) {
                        final SQLConstraint constraint = this.exprParser.parseConstaint();
                        constraint.setParent(stmt);
                        stmt.getTableElementList().add((SQLTableElement)constraint);
                    }
                    else if (this.lexer.token() == Token.INDEX) {
                        final MySqlTableIndex idx = new MySqlTableIndex();
                        this.exprParser.parseIndex(idx.getIndexDefinition());
                        idx.setParent(stmt);
                        stmt.getTableElementList().add(idx);
                    }
                    else if (this.lexer.token() == Token.KEY) {
                        final Lexer.SavePoint savePoint = this.lexer.mark();
                        this.lexer.nextToken();
                        boolean isColumn = false;
                        if (this.lexer.identifierEquals(FnvHash.Constants.VARCHAR)) {
                            isColumn = true;
                        }
                        this.lexer.reset(savePoint);
                        if (isColumn) {
                            stmt.getTableElementList().add(this.exprParser.parseColumn());
                            continue;
                        }
                        SQLName name = null;
                        if (this.lexer.token() == Token.IDENTIFIER) {
                            name = this.exprParser.name();
                        }
                        final MySqlKey key = new MySqlKey();
                        this.exprParser.parseIndex(key.getIndexDefinition());
                        if (name != null) {
                            key.setName(name);
                        }
                        key.setParent(stmt);
                        stmt.getTableElementList().add(key);
                        continue;
                    }
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            while (true) {
                if (this.lexer.identifierEquals(FnvHash.Constants.DISTRIBUTED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    if (this.lexer.identifierEquals(FnvHash.Constants.HASH)) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        while (true) {
                            final SQLName name2 = this.exprParser.name();
                            stmt.getDistributedBy().add(name2);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        stmt.setDistributedByType(new SQLIdentifierExpr("HASH"));
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.BROADCAST)) {
                            continue;
                        }
                        this.lexer.nextToken();
                        stmt.setDistributedByType(new SQLIdentifierExpr("BROADCAST"));
                    }
                }
                else if (this.lexer.identifierEquals("INDEX_ALL")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    if (this.lexer.token() != Token.LITERAL_CHARS) {
                        continue;
                    }
                    if ("Y".equalsIgnoreCase(this.lexer.stringVal())) {
                        this.lexer.nextToken();
                        stmt.addOption("INDEX_ALL", new SQLCharExpr("Y"));
                    }
                    else {
                        if (!"N".equalsIgnoreCase(this.lexer.stringVal())) {
                            throw new ParserException("INDEX_ALL accept parameter ['Y' or 'N'] only.");
                        }
                        this.lexer.nextToken();
                        stmt.addOption("INDEX_ALL", new SQLCharExpr("N"));
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    final SQLExpr expr = this.exprParser.expr();
                    stmt.addOption("ENGINE", expr);
                }
                else if (this.lexer.token == Token.PARTITION) {
                    final SQLPartitionBy partitionBy = this.exprParser.parsePartitionBy();
                    stmt.setPartitionBy(partitionBy);
                }
                else {
                    if (this.lexer.token() != Token.COMMENT) {
                        break;
                    }
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.setComment(this.exprParser.expr());
                }
            }
        }
        else if (this.dbType == DbType.odps) {
            if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                this.lexer.nextToken();
                stmt.setLifyCycle(this.exprParser.primary());
            }
            if (this.lexer.token == Token.PARTITIONED) {
                this.lexer.nextToken();
                this.accept(Token.ON);
                this.accept(Token.LPAREN);
                this.exprParser.names(stmt.getPartitionedOn(), stmt);
                this.accept(Token.RPAREN);
            }
        }
        else if (this.lexer.token == Token.PARTITION) {
            final SQLPartitionBy partitionBy = this.exprParser.parsePartitionBy();
            stmt.setPartitionBy(partitionBy);
        }
        while (true) {
            if (this.exprParser instanceof OracleExprParser) {
                ((OracleExprParser)this.exprParser).parseSegmentAttributes(stmt);
            }
            if (this.lexer.identifierEquals("REFRESH")) {
                this.lexer.nextToken();
                boolean refresh = false;
                while (true) {
                    if (this.lexer.identifierEquals("FAST")) {
                        this.lexer.nextToken();
                        stmt.setRefreshFast(true);
                        refresh = true;
                    }
                    else if (this.lexer.identifierEquals("COMPLETE")) {
                        this.lexer.nextToken();
                        stmt.setRefreshComplete(true);
                        refresh = true;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                        this.lexer.nextToken();
                        stmt.setRefreshForce(true);
                        refresh = true;
                    }
                    else if (this.lexer.token == Token.ON) {
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.COMMIT || this.lexer.identifierEquals(FnvHash.Constants.COMMIT)) {
                            this.lexer.nextToken();
                            stmt.setRefreshOnCommit(true);
                            refresh = true;
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.OVERWRITE)) {
                            this.lexer.nextToken();
                            stmt.setRefreshOnOverWrite(true);
                            refresh = true;
                        }
                        else {
                            this.acceptIdentifier("DEMAND");
                            stmt.setRefreshOnDemand(true);
                            refresh = true;
                        }
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.START)) {
                        this.lexer.nextToken();
                        this.accept(Token.WITH);
                        final SQLExpr startWith = this.exprParser.expr();
                        stmt.setStartWith(startWith);
                        stmt.setRefreshStartWith(true);
                        refresh = true;
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.NEXT)) {
                            break;
                        }
                        this.lexer.nextToken();
                        final SQLExpr next = this.exprParser.expr();
                        stmt.setNext(next);
                        stmt.setRefreshNext(true);
                        refresh = true;
                    }
                }
                if (!refresh) {
                    throw new ParserException("refresh clause is empty. " + this.lexer.info());
                }
                continue;
            }
            else if (this.lexer.identifierEquals("BUILD")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("IMMEDIATE") || this.lexer.token == Token.IMMEDIATE) {
                    this.lexer.nextToken();
                    stmt.setBuildImmediate(true);
                }
                else {
                    this.accept(Token.DEFERRED);
                    stmt.setBuildDeferred(true);
                }
            }
            else if (this.lexer.identifierEquals("PARALLEL")) {
                this.lexer.nextToken();
                stmt.setParallel(true);
                if (this.lexer.token != Token.LITERAL_INT) {
                    continue;
                }
                stmt.setParallelValue(this.lexer.integerValue().intValue());
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NOCACHE) || this.lexer.token == Token.NOCACHE) {
                this.lexer.nextToken();
                stmt.setCache(false);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NOPARALLEL)) {
                this.lexer.nextToken();
                stmt.setParallel(false);
            }
            else {
                if (this.lexer.token != Token.WITH) {
                    Boolean enableQueryRewrite = null;
                    if (this.lexer.token == Token.ENABLE) {
                        this.lexer.nextToken();
                        enableQueryRewrite = true;
                    }
                    if (this.lexer.token == Token.DISABLE) {
                        this.lexer.nextToken();
                        enableQueryRewrite = false;
                    }
                    if (enableQueryRewrite != null) {
                        this.acceptIdentifier("QUERY");
                        this.acceptIdentifier("REWRITE");
                        stmt.setEnableQueryRewrite(enableQueryRewrite);
                    }
                    this.accept(Token.AS);
                    final SQLSelect select = this.createSQLSelectParser().select();
                    stmt.setQuery(select);
                    return stmt;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("ROWID");
                stmt.setWithRowId(true);
            }
        }
    }
    
    public SQLStatement parseCreateDbLink() {
        throw new ParserException("TODO " + this.lexer.token);
    }
    
    public SQLStatement parseCreateSynonym() {
        throw new ParserException("TODO " + this.lexer.token);
    }
    
    public SQLStatement parseCreateExternalCatalog() {
        final MySqlCreateExternalCatalogStatement stmt = new MySqlCreateExternalCatalogStatement();
        if (this.lexer.token == Token.CREATE) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("EXTERNAL");
        this.acceptIdentifier("CATALOG");
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.acceptIdentifier("PROPERTIES");
        this.accept(Token.LPAREN);
        do {
            final SQLName key = this.exprParser.name();
            this.accept(Token.EQ);
            final SQLName value = this.exprParser.name();
            stmt.getProperties().put(key, value);
        } while (this.lexer.token != Token.RPAREN);
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLName comment = this.exprParser.name();
            stmt.setComment(comment);
        }
        return stmt;
    }
    
    public SQLStatement parseCreateTrigger() {
        final SQLCreateTriggerStatement stmt = new SQLCreateTriggerStatement(this.getDbType());
        if (this.lexer.token == Token.CREATE) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.OR) {
                this.lexer.nextToken();
                this.accept(Token.REPLACE);
                stmt.setOrReplace(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = ((MySqlExprParser)this.exprParser).userName();
            stmt.setDefiner(definer);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                this.accept(Token.RPAREN);
            }
        }
        this.accept(Token.TRIGGER);
        stmt.setName(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.BEFORE)) {
            stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.BEFORE);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.AFTER)) {
            stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.AFTER);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.INSTEAD)) {
            this.lexer.nextToken();
            this.accept(Token.OF);
            stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.INSTEAD_OF);
        }
        while (true) {
            if (this.lexer.token == Token.INSERT) {
                this.lexer.nextToken();
                stmt.setInsert(true);
            }
            else if (this.lexer.token == Token.UPDATE) {
                this.lexer.nextToken();
                stmt.setUpdate(true);
                if (this.lexer.token == Token.OF) {
                    this.lexer.nextToken();
                    this.exprParser.names(stmt.getUpdateOfColumns(), stmt);
                }
            }
            else if (this.lexer.token == Token.DELETE) {
                this.lexer.nextToken();
                stmt.setDelete(true);
            }
            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.OR) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.ON);
        stmt.setOn(this.exprParser.name());
        if (this.lexer.token == Token.FOR) {
            this.lexer.nextToken();
            this.acceptIdentifier("EACH");
            this.accept(Token.ROW);
            stmt.setForEachRow(true);
        }
        if (this.lexer.token == Token.WHEN) {
            this.lexer.nextToken();
            final SQLExpr condition = this.exprParser.expr();
            stmt.setWhen(condition);
        }
        final List<SQLStatement> body = this.parseStatementList();
        if (body == null || body.isEmpty()) {
            throw new ParserException("syntax error");
        }
        stmt.setBody(body.get(0));
        return stmt;
    }
    
    public SQLStatement parseBlock() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLStatement parseCreateSchema() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    public SQLStatement parseCreateDatabase() {
        final SQLCreateDatabaseStatement stmt = new SQLCreateDatabaseStatement(this.dbType);
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            stmt.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (this.lexer.token == Token.CREATE) {
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.SCHEMA && this.dbType == DbType.hive) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        if (this.lexer.token == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        stmt.setName(this.exprParser.name());
        if (this.lexer.token == Token.COMMENT) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr comment = this.exprParser.expr();
            stmt.setComment(comment);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
            this.lexer.nextToken();
            final SQLExpr location = this.exprParser.expr();
            stmt.setLocation(location);
        }
        if (this.lexer.token == Token.WITH) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.DBPROPERTIES)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLAssignItem assignItem = this.exprParser.parseAssignItem();
                assignItem.setParent(stmt);
                stmt.getDbProperties().add(assignItem);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.COMMENT) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
            }
            final SQLExpr comment = this.exprParser.expr();
            stmt.setComment(comment);
        }
        return stmt;
    }
    
    public SQLCreateProcedureStatement parseCreateProcedure() {
        throw new ParserException("TODO " + this.lexer.token);
    }
    
    public SQLStatement parseCreateSequence(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        final SQLCreateSequenceStatement stmt = new SQLCreateSequenceStatement();
        if (this.lexer.token == Token.GROUP) {
            this.lexer.nextToken();
            stmt.setGroup(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SIMPLE)) {
            this.lexer.nextToken();
            stmt.setSimple(true);
            if (this.lexer.token == Token.WITH) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
                stmt.setWithCache(true);
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
            this.lexer.nextToken();
            stmt.setTime(true);
        }
        this.accept(Token.SEQUENCE);
        stmt.setDbType(this.dbType);
        stmt.setName(this.exprParser.name());
        while (true) {
            if (this.lexer.token() == Token.START || this.lexer.identifierEquals(FnvHash.Constants.START)) {
                this.lexer.nextToken();
                this.accept(Token.WITH);
                stmt.setStartWith(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.INCREMENT)) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setIncrementBy(this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.CACHE || this.lexer.identifierEquals(FnvHash.Constants.CACHE)) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.TRUE);
                if (this.lexer.token() != Token.LITERAL_INT) {
                    continue;
                }
                stmt.setCacheValue(this.exprParser.primary());
            }
            else if (this.lexer.token == Token.WITH) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
                stmt.setCache(true);
            }
            else if (this.lexer.token() == Token.NOCACHE || this.lexer.identifierEquals(FnvHash.Constants.NOCACHE)) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.ORDER) {
                this.lexer.nextToken();
                stmt.setOrder(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOORDER")) {
                this.lexer.nextToken();
                stmt.setOrder(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("CYCLE")) {
                this.lexer.nextToken();
                stmt.setCycle(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NOCYCLE)) {
                this.lexer.nextToken();
                stmt.setCycle(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("MINVALUE")) {
                this.lexer.nextToken();
                stmt.setMinValue(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("MAXVALUE")) {
                this.lexer.nextToken();
                stmt.setMaxValue(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("NOMAXVALUE")) {
                this.lexer.nextToken();
                stmt.setNoMaxValue(true);
            }
            else {
                if (!this.lexer.identifierEquals("NOMINVALUE")) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setNoMinValue(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.UNIT)) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.COUNT)) {
                this.lexer.nextToken();
                final SQLExpr unitCount = this.exprParser.primary();
                stmt.setUnitCount(unitCount);
            }
            if (this.lexer.token == Token.INDEX) {
                this.lexer.nextToken();
                final SQLExpr unitIndex = this.exprParser.primary();
                stmt.setUnitIndex(unitIndex);
            }
            if (this.lexer.hash_lower() == FnvHash.Constants.STEP) {
                this.lexer.nextToken();
                final SQLExpr step = this.exprParser.primary();
                stmt.setStep(step);
            }
        }
        return stmt;
    }
    
    public SQLCreateIndexStatement parseCreateIndex(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        final SQLCreateIndexStatement stmt = new SQLCreateIndexStatement(this.getDbType());
        if (this.lexer.token == Token.UNIQUE) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("CLUSTERED")) {
                this.lexer.nextToken();
                stmt.setType("UNIQUE CLUSTERED");
            }
            else if (this.lexer.identifierEquals("NONCLUSTERED")) {
                stmt.setType("UNIQUE NONCLUSTERED");
                this.lexer.nextToken();
            }
            else {
                stmt.setType("UNIQUE");
            }
        }
        else if (this.lexer.token() == Token.FULLTEXT) {
            stmt.setType("FULLTEXT");
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals("NONCLUSTERED")) {
            stmt.setType("NONCLUSTERED");
            this.lexer.nextToken();
        }
        this.accept(Token.INDEX);
        stmt.setName(this.exprParser.name());
        this.accept(Token.ON);
        stmt.setTable(this.exprParser.name());
        this.accept(Token.LPAREN);
        while (true) {
            final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
            item.setParent(stmt);
            stmt.addItem(item);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        return stmt;
    }
    
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new SQLCreateTableParser(this.exprParser);
    }
    
    public SQLStatement parseSelect() {
        final SQLSelectParser selectParser = this.createSQLSelectParser();
        final SQLSelect select = selectParser.select();
        return new SQLSelectStatement(select, this.getDbType());
    }
    
    public SQLSelectParser createSQLSelectParser() {
        return new SQLSelectParser(this.exprParser, this.selectListCache);
    }
    
    public SQLSelectParser createSQLSelectParser(final SQLExprParser exprParser) {
        return new SQLSelectParser(exprParser);
    }
    
    public SQLUpdateStatement parseUpdateStatement() {
        final SQLUpdateStatement udpateStatement = this.createUpdateStatement();
        if (this.lexer.token == Token.UPDATE) {
            this.lexer.nextToken();
            final SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
            udpateStatement.setTableSource(tableSource);
        }
        if (this.dbType == DbType.odps && this.lexer.token == Token.PARTITION) {
            this.lexer.nextToken();
            udpateStatement.setPartitions(new ArrayList<SQLAssignItem>());
            this.exprParser.parseAssignItem(udpateStatement.getPartitions(), udpateStatement);
        }
        this.parseUpdateSet(udpateStatement);
        if (this.lexer.token == Token.WHERE) {
            this.lexer.nextToken();
            udpateStatement.setWhere(this.exprParser.expr());
        }
        return udpateStatement;
    }
    
    protected void parseUpdateSet(final SQLUpdateStatement update) {
        this.accept(Token.SET);
        if (this.lexer.token == Token.COMMA && this.dbType == DbType.odps) {
            this.lexer.nextToken();
        }
        while (true) {
            final SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
            update.addItem(item);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    protected SQLUpdateStatement createUpdateStatement() {
        return new SQLUpdateStatement(this.getDbType());
    }
    
    public SQLDeleteStatement parseDeleteStatement() {
        final SQLDeleteStatement deleteStatement = new SQLDeleteStatement(this.getDbType());
        if (this.lexer.token == Token.DELETE) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.FROM) {
                this.lexer.nextToken();
            }
            if (this.lexer.token == Token.COMMENT) {
                this.lexer.nextToken();
            }
            final SQLName tableName = this.exprParser.name();
            deleteStatement.setTableName(tableName);
            if (this.lexer.token == Token.FROM) {
                this.lexer.nextToken();
                final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSource();
                deleteStatement.setFrom(tableSource);
            }
        }
        if (this.lexer.token == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            deleteStatement.setWhere(where);
        }
        return deleteStatement;
    }
    
    public SQLCreateTableStatement parseCreateTable() {
        final SQLCreateTableParser parser = new SQLCreateTableParser(this.exprParser);
        return parser.parseCreateTable();
    }
    
    public SQLCreateViewStatement parseCreateView() {
        final SQLCreateViewStatement createView = new SQLCreateViewStatement(this.getDbType());
        if (this.lexer.token == Token.CREATE) {
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            createView.setOrReplace(true);
        }
        if (this.lexer.identifierEquals("ALGORITHM")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final String algorithm = this.lexer.stringVal();
            createView.setAlgorithm(algorithm);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEFINER)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLName definer = ((MySqlExprParser)this.exprParser).userName();
            createView.setDefiner(definer);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
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
        if (this.lexer.token == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            createView.setIfNotExists(true);
        }
        createView.setName(this.exprParser.name());
        if (this.dbType == DbType.clickhouse) {
            if (this.lexer.token == Token.ON) {
                this.lexer.nextToken();
                this.acceptIdentifier("CLUSTER");
                createView.setOnCluster(true);
            }
            if (this.lexer.token == Token.LITERAL_CHARS) {
                final SQLName to = this.exprParser.name();
                createView.setTo(to);
            }
            else if (this.lexer.token == Token.TO) {
                this.lexer.nextToken();
                final SQLName to = this.exprParser.name();
                createView.setTo(to);
            }
        }
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token == Token.CONSTRAINT) {
                    final SQLTableConstraint constraint = (SQLTableConstraint)this.exprParser.parseConstaint();
                    createView.addColumn(constraint);
                }
                else {
                    if (this.lexer.token == Token.RPAREN) {
                        break;
                    }
                    final SQLColumnDefinition column = new SQLColumnDefinition();
                    column.setDbType(this.dbType);
                    final SQLName expr = this.exprParser.name();
                    column.setName(expr);
                    if (this.dbType == DbType.odps && expr.getSimpleName().startsWith("@")) {
                        column.setDataType(this.exprParser.parseDataType());
                    }
                    this.exprParser.parseColumnRest(column);
                    if (this.lexer.token == Token.COMMENT) {
                        this.lexer.nextToken();
                        SQLExpr comment;
                        if (this.lexer.token == Token.LITERAL_ALIAS) {
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
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals("RETURNS")) {
            this.lexer.nextToken();
            final SQLVariantRefExpr varRef = (SQLVariantRefExpr)this.exprParser.expr();
            createView.setReturns(varRef);
            createView.setReturnsDataType((SQLTableDataType)this.exprParser.parseDataType());
        }
        if (this.lexer.token == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLCharExpr comment2 = (SQLCharExpr)this.exprParser.primary();
            createView.setComment(comment2);
        }
        this.accept(Token.AS);
        if (this.lexer.identifierEquals(FnvHash.Constants.BEGIN)) {
            final SQLBlockStatement block = (SQLBlockStatement)this.parseBlock();
            createView.setScript(block);
            return createView;
        }
        final SQLSelectParser selectParser = this.createSQLSelectParser();
        createView.setSubQuery(selectParser.select());
        if (this.lexer.token == Token.WITH) {
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
            if (this.lexer.token == Token.CHECK) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTION");
                createView.setWithCheckOption(true);
            }
        }
        return createView;
    }
    
    public SQLCommentStatement parseComment() {
        this.accept(Token.COMMENT);
        final SQLCommentStatement stmt = new SQLCommentStatement();
        this.accept(Token.ON);
        if (this.lexer.token == Token.TABLE) {
            stmt.setType(SQLCommentStatement.Type.TABLE);
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.COLUMN) {
            stmt.setType(SQLCommentStatement.Type.COLUMN);
            this.lexer.nextToken();
        }
        stmt.setOn(this.exprParser.name());
        this.accept(Token.IS);
        stmt.setComment(this.exprParser.expr());
        return stmt;
    }
    
    protected SQLAlterTableAddColumn parseAlterTableAddColumn() {
        final boolean odps = DbType.odps == this.dbType || DbType.hive == this.dbType;
        boolean columns = false;
        if (odps) {
            if (this.lexer.identifierEquals("COLUMNS")) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    columns = true;
                }
            }
            else if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                columns = true;
            }
        }
        final SQLAlterTableAddColumn item = new SQLAlterTableAddColumn();
        do {
            final SQLColumnDefinition columnDef = this.exprParser.parseColumn();
            item.addColumn(columnDef);
            if (this.lexer.token == Token.WITH) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token == Token.DEFAULT) {
                    this.lexer.nextToken();
                    final SQLExpr defaultExpr = this.exprParser.expr();
                    columnDef.setDefaultExpr(defaultExpr);
                }
                else {
                    this.lexer.reset(mark);
                }
            }
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        } while (!this.lexer.identifierEquals("ADD"));
        if (odps && columns) {
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
        return item;
    }
    
    protected SQLAlterTableReplaceColumn parseAlterTableReplaceColumn() {
        this.accept(Token.REPLACE);
        this.acceptIdentifier("COLUMNS");
        final SQLAlterTableReplaceColumn item = new SQLAlterTableReplaceColumn();
        this.accept(Token.LPAREN);
        do {
            final SQLColumnDefinition columnDef = this.exprParser.parseColumn();
            item.addColumn(columnDef);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        } while (!this.lexer.identifierEquals(FnvHash.Constants.ADD));
        this.accept(Token.RPAREN);
        return item;
    }
    
    public SQLStatement parseStatement() {
        if (this.lexer.token == Token.SELECT) {
            return this.parseSelect();
        }
        if (this.lexer.token == Token.INSERT) {
            return this.parseInsert();
        }
        if (this.lexer.token == Token.UPDATE) {
            return this.parseUpdateStatement();
        }
        if (this.lexer.token == Token.DELETE) {
            return this.parseDeleteStatement();
        }
        final List<SQLStatement> list = new ArrayList<SQLStatement>(1);
        this.parseStatementList(list, 1, null);
        return list.get(0);
    }
    
    public SQLStatement parseStatement(final boolean tryBest) {
        final List<SQLStatement> list = new ArrayList<SQLStatement>();
        this.parseStatementList(list, 1, null);
        if (tryBest && this.lexer.token != Token.EOF) {
            throw new ParserException("sql syntax error, no terminated. " + this.lexer.info());
        }
        return list.get(0);
    }
    
    public SQLExplainStatement parseExplain() {
        this.accept(Token.EXPLAIN);
        if (this.lexer.identifierEquals("PLAN")) {
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.FOR) {
            this.lexer.nextToken();
        }
        final SQLExplainStatement explain = new SQLExplainStatement(this.dbType);
        if (this.lexer.token == Token.ANALYZE || this.lexer.identifierEquals(FnvHash.Constants.ANALYZE)) {
            this.lexer.nextToken();
            explain.setType("ANALYZE");
        }
        if (this.lexer.token == Token.HINT) {
            explain.setHints(this.exprParser.parseHints());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTENDED)) {
            this.lexer.nextToken();
            explain.setExtended(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DEPENDENCY)) {
            this.lexer.nextToken();
            explain.setDependency(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.AUTHORIZATION)) {
            this.lexer.nextToken();
            explain.setAuthorization(true);
        }
        if (DbType.mysql == this.dbType && (this.lexer.identifierEquals("FORMAT") || this.lexer.identifierEquals("PARTITIONS"))) {
            explain.setType(this.lexer.stringVal);
            this.lexer.nextToken();
        }
        if ((DbType.mysql == this.dbType || DbType.ads == this.dbType || DbType.presto == this.dbType || DbType.trino == this.dbType) && this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("FORMAT")) {
                this.lexer.nextToken();
                final String type = "FORMAT " + this.lexer.stringVal;
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("TYPE")) {
                this.lexer.nextToken();
                final String type = "TYPE " + this.lexer.stringVal;
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        explain.setStatement(this.parseStatement());
        return explain;
    }
    
    protected SQLAlterTableAddClusteringKey parseAlterTableAddClusteringKey() {
        this.lexer.nextToken();
        final SQLAlterTableAddClusteringKey item = new SQLAlterTableAddClusteringKey();
        this.accept(Token.KEY);
        item.setName(this.exprParser.name());
        this.accept(Token.LPAREN);
        while (true) {
            item.getColumns().add(this.exprParser.name());
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        return item;
    }
    
    public SQLOpenStatement parseOpen() {
        final SQLOpenStatement stmt = new SQLOpenStatement();
        this.accept(Token.OPEN);
        SQLName cursorName;
        if (this.lexer.token == Token.QUES) {
            this.lexer.nextToken();
            cursorName = new SQLIdentifierExpr("?");
        }
        else {
            cursorName = this.exprParser.name();
        }
        stmt.setCursorName(cursorName);
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.names(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.FOR) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.SELECT) {
                final SQLSelectParser selectParser = this.createSQLSelectParser();
                final SQLSelect select = selectParser.select();
                final SQLQueryExpr queryExpr = new SQLQueryExpr(select);
                stmt.setFor(queryExpr);
            }
            else if (this.lexer.token == Token.LITERAL_CHARS) {
                final String chars = this.lexer.stringVal;
                final SQLExprParser exprParser = SQLParserUtils.createExprParser(chars, this.dbType, new SQLParserFeature[0]);
                final SQLSelectParser selectParser2 = this.createSQLSelectParser(exprParser);
                final SQLSelect select2 = selectParser2.select();
                final SQLQueryExpr queryExpr2 = new SQLQueryExpr(select2);
                stmt.setFor(queryExpr2);
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token != Token.QUES) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                stmt.setFor(new SQLVariantRefExpr("?"));
            }
        }
        if (this.lexer.token == Token.USING) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getUsing(), stmt);
        }
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    public SQLFetchStatement parseFetch() {
        this.accept(Token.FETCH);
        final SQLFetchStatement stmt = new SQLFetchStatement();
        stmt.setCursorName(this.exprParser.name());
        if (this.lexer.identifierEquals("BULK")) {
            this.lexer.nextToken();
            this.acceptIdentifier("COLLECT");
            stmt.setBulkCollect(true);
        }
        this.accept(Token.INTO);
        while (true) {
            stmt.getInto().add(this.exprParser.name());
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.LIMIT) {
            final SQLLimit limit = this.exprParser.parseLimit();
            stmt.setLimit(limit);
        }
        return stmt;
    }
    
    public SQLStatement parseClose() {
        final SQLCloseStatement stmt = new SQLCloseStatement();
        this.accept(Token.CLOSE);
        stmt.setCursorName(this.exprParser.name());
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    public boolean isParseCompleteValues() {
        return this.parseCompleteValues;
    }
    
    public void setParseCompleteValues(final boolean parseCompleteValues) {
        this.parseCompleteValues = parseCompleteValues;
    }
    
    public int getParseValuesSize() {
        return this.parseValuesSize;
    }
    
    public void setParseValuesSize(final int parseValuesSize) {
        this.parseValuesSize = parseValuesSize;
    }
    
    public SQLStatement parseMerge() {
        if (this.lexer.identifierEquals(FnvHash.Constants.MERGE)) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.MERGE);
        }
        final SQLMergeStatement stmt = new SQLMergeStatement();
        stmt.setDbType(this.dbType);
        this.parseHints(stmt.getHints());
        this.accept(Token.INTO);
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelect select = this.createSQLSelectParser().select();
            final SQLSubqueryTableSource tableSource = new SQLSubqueryTableSource(select);
            stmt.setInto(tableSource);
            this.accept(Token.RPAREN);
        }
        else {
            stmt.setInto(this.exprParser.name());
        }
        stmt.getInto().setAlias(this.tableAlias());
        if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.USING);
        }
        final SQLTableSource using = this.createSQLSelectParser().parseTableSource();
        stmt.setUsing(using);
        this.accept(Token.ON);
        stmt.setOn(this.exprParser.expr());
        do {
            boolean insertFlag = false;
            if (this.lexer.token == Token.WHEN) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.MATCHED || this.lexer.identifierEquals(FnvHash.Constants.MATCHED)) {
                    final SQLMergeStatement.MergeUpdateClause updateClause = new SQLMergeStatement.MergeUpdateClause();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.AND) {
                        this.lexer.nextToken();
                        final SQLExpr where = this.exprParser.expr();
                        updateClause.setWhere(where);
                    }
                    this.accept(Token.THEN);
                    if (this.lexer.token == Token.DELETE) {
                        this.lexer.nextToken();
                        updateClause.setDelete(true);
                        stmt.setUpdateClause(updateClause);
                        break;
                    }
                    this.accept(Token.UPDATE);
                    this.accept(Token.SET);
                    while (true) {
                        final SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
                        updateClause.addItem(item);
                        item.setParent(updateClause);
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    if (this.lexer.token == Token.WHERE) {
                        this.lexer.nextToken();
                        updateClause.setWhere(this.exprParser.expr());
                    }
                    SQLExpr deleteWhere = null;
                    if (this.lexer.token == Token.WHEN) {
                        final Lexer.SavePoint savePoint = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.MATCHED) {
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.AND) {
                                this.lexer.nextToken();
                                deleteWhere = this.exprParser.expr();
                            }
                            if (this.lexer.token == Token.THEN) {
                                this.lexer.nextToken();
                                if (this.lexer.token == Token.DELETE) {
                                    this.lexer.nextToken();
                                    updateClause.setDeleteWhere(deleteWhere);
                                }
                                else {
                                    deleteWhere = null;
                                }
                            }
                            else {
                                deleteWhere = null;
                            }
                            if (deleteWhere == null) {
                                this.lexer.reset(savePoint);
                            }
                        }
                    }
                    if (this.lexer.token == Token.DELETE) {
                        this.lexer.nextToken();
                        this.accept(Token.WHERE);
                        updateClause.setDeleteWhere(this.exprParser.expr());
                    }
                    stmt.setUpdateClause(updateClause);
                }
                else if (this.lexer.token == Token.NOT) {
                    this.lexer.nextToken();
                    insertFlag = true;
                }
            }
            if (!insertFlag) {
                if (this.lexer.token == Token.WHEN) {
                    this.lexer.nextToken();
                }
                if (this.lexer.token == Token.NOT) {
                    this.lexer.nextToken();
                    insertFlag = true;
                }
            }
            if (insertFlag) {
                final SQLMergeStatement.MergeInsertClause insertClause = new SQLMergeStatement.MergeInsertClause();
                if (this.lexer.identifierEquals(FnvHash.Constants.MATCHED)) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.MATCHED);
                }
                if (this.lexer.token == Token.AND) {
                    this.lexer.nextToken();
                    insertClause.setWhere(this.exprParser.expr());
                }
                this.accept(Token.THEN);
                this.accept(Token.INSERT);
                if (this.lexer.token == Token.LPAREN) {
                    this.accept(Token.LPAREN);
                    this.exprParser.exprList(insertClause.getColumns(), insertClause);
                    this.accept(Token.RPAREN);
                }
                this.accept(Token.VALUES);
                this.accept(Token.LPAREN);
                this.exprParser.exprList(insertClause.getValues(), insertClause);
                this.accept(Token.RPAREN);
                if (this.lexer.token == Token.WHERE) {
                    this.lexer.nextToken();
                    insertClause.setWhere(this.exprParser.expr());
                }
                stmt.setInsertClause(insertClause);
            }
        } while (this.lexer.token == Token.WHEN);
        final SQLErrorLoggingClause errorClause = this.parseErrorLoggingClause();
        stmt.setErrorLoggingClause(errorClause);
        return stmt;
    }
    
    protected SQLErrorLoggingClause parseErrorLoggingClause() {
        if (this.lexer.identifierEquals("LOG")) {
            final SQLErrorLoggingClause errorClause = new SQLErrorLoggingClause();
            this.lexer.nextToken();
            this.accept(Token.ERRORS);
            if (this.lexer.token == Token.INTO) {
                this.lexer.nextToken();
                errorClause.setInto(this.exprParser.name());
            }
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                errorClause.setSimpleExpression(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token == Token.REJECT) {
                this.lexer.nextToken();
                this.accept(Token.LIMIT);
                errorClause.setLimit(this.exprParser.expr());
            }
            return errorClause;
        }
        return null;
    }
    
    public void parseHints(final List<SQLHint> hints) {
        this.getExprParser().parseHints(hints);
    }
    
    public SQLStatement parseDescribe() {
        if (this.lexer.token == Token.DESC || this.lexer.identifierEquals("DESCRIBE")) {
            this.lexer.nextToken();
            final SQLDescribeStatement stmt = new SQLDescribeStatement();
            stmt.setDbType(this.dbType);
            if (this.lexer.token == Token.DATABASE) {
                this.lexer.nextToken();
                stmt.setObjectType(SQLObjectType.DATABASE);
            }
            else if (this.lexer.token == Token.SCHEMA) {
                this.lexer.nextToken();
                stmt.setObjectType(SQLObjectType.SCHEMA);
            }
            else if (this.lexer.identifierEquals("ROLE")) {
                this.lexer.nextToken();
                stmt.setObjectType(SQLObjectType.ROLE);
            }
            else if (this.lexer.identifierEquals("PACKAGE")) {
                this.lexer.nextToken();
                stmt.setObjectType(SQLObjectType.PACKAGE);
            }
            else if (this.lexer.identifierEquals("INSTANCE")) {
                this.lexer.nextToken();
                stmt.setObjectType(SQLObjectType.INSTANCE);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.EXTENDED)) {
                this.lexer.nextToken();
                stmt.setExtended(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.FORMATTED)) {
                this.lexer.nextToken();
                stmt.setFormatted(true);
            }
            stmt.setObject(this.exprParser.name());
            if (this.lexer.token == Token.IDENTIFIER) {
                final SQLName column = this.exprParser.name();
                stmt.setColumn(column);
            }
            if (this.lexer.token == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    stmt.getPartition().add(this.exprParser.expr());
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.nextToken();
                    }
                    else {
                        if (this.lexer.token == Token.RPAREN) {
                            break;
                        }
                        continue;
                    }
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token == Token.IDENTIFIER && stmt.getColumn() == null) {
                final SQLName column = this.exprParser.name();
                stmt.setColumn(column);
            }
            return stmt;
        }
        throw new ParserException("expect DESC, actual " + this.lexer.token);
    }
    
    public SQLWithSubqueryClause parseWithQuery() {
        final SQLWithSubqueryClause withQueryClause = new SQLWithSubqueryClause();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            withQueryClause.addBeforeComment(this.lexer.readAndResetComments());
        }
        this.accept(Token.WITH);
        if (this.lexer.token == Token.RECURSIVE || this.lexer.identifierEquals("RECURSIVE")) {
            this.lexer.nextToken();
            withQueryClause.setRecursive(true);
        }
        while (true) {
            final SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
            entry.setParent(withQueryClause);
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                entry.addBeforeComment(this.lexer.readAndResetComments());
            }
            final String alias = this.lexer.stringVal();
            this.lexer.nextToken();
            entry.setAlias(alias);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                while (this.lexer.token != Token.RPAREN) {
                    final SQLName name = this.exprParser.name();
                    if (this.dbType == DbType.odps && (this.lexer.identifierEquals(FnvHash.Constants.STRING) || this.lexer.identifierEquals(FnvHash.Constants.INT) || this.lexer.identifierEquals(FnvHash.Constants.BIGINT))) {
                        this.lexer.nextToken();
                    }
                    name.setParent(entry);
                    entry.getColumns().add(name);
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.nextToken();
                    }
                }
                this.accept(Token.RPAREN);
            }
            this.accept(Token.AS);
            this.accept(Token.LPAREN);
            switch (this.lexer.token) {
                case WITH:
                case SELECT:
                case VALUES:
                case LPAREN:
                case FROM: {
                    entry.setSubQuery(this.createSQLSelectParser().select());
                    break;
                }
                case INSERT: {
                    entry.setReturningStatement(this.parseInsert());
                    break;
                }
                case UPDATE: {
                    entry.setReturningStatement(this.parseUpdateStatement());
                    break;
                }
                case DELETE: {
                    entry.setReturningStatement(this.parseDeleteStatement());
                    break;
                }
            }
            this.accept(Token.RPAREN);
            withQueryClause.addEntry(entry);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return withQueryClause;
    }
    
    public SQLStatement parseWith() {
        final SQLWithSubqueryClause with = this.parseWithQuery();
        if (this.lexer.token == Token.SELECT || this.lexer.token == Token.LPAREN) {
            final SQLSelectParser selectParser = this.createSQLSelectParser();
            final SQLSelect select = selectParser.select();
            select.setWithSubQuery(with);
            return new SQLSelectStatement(select, this.dbType);
        }
        if (this.lexer.token == Token.INSERT) {
            final SQLInsertStatement insert = (SQLInsertStatement)this.parseInsert();
            insert.setWith(with);
            return insert;
        }
        if (this.lexer.token == Token.FROM) {
            final HiveMultiInsertStatement insert2 = (HiveMultiInsertStatement)this.parseInsert();
            insert2.setWith(with);
            return insert2;
        }
        if (this.lexer.token == Token.UPDATE) {
            final SQLUpdateStatement update = this.parseUpdateStatement();
            update.setWith(with);
            return update;
        }
        throw new ParserException("TODO. " + this.lexer.info());
    }
    
    protected void parseValueClause(final List<SQLInsertStatement.ValuesClause> valueClauseList, final int columnSize, final SQLObject parent) {
        this.parseValueClause(valueClauseList, null, 0, parent);
    }
    
    protected void parseValueClauseNative(final List<SQLInsertStatement.ValuesClause> valueClauseList, final List<SQLColumnDefinition> columnDefinitionList, int columnSize, final SQLObject parent) {
        final TimeZone timeZone = this.lexer.getTimeZone();
        int i = 0;
        while (true) {
            final int startPos = this.lexer.pos - 1;
            if (this.lexer.token != Token.LPAREN) {
                throw new ParserException("syntax error, expect ')', " + this.lexer.info());
            }
            if (this.lexer.ch == '\'') {
                this.lexer.bufPos = 0;
                if (this.dbType == DbType.mysql) {
                    this.lexer.scanString2();
                }
                else {
                    this.lexer.scanString();
                }
            }
            else if (this.lexer.ch == '0') {
                this.lexer.bufPos = 0;
                if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                    this.lexer.scanChar();
                    this.lexer.scanChar();
                    this.lexer.scanHexaDecimal();
                }
                else {
                    this.lexer.scanNumber();
                }
            }
            else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                this.lexer.bufPos = 0;
                this.lexer.scanNumber();
            }
            else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                this.lexer.scanNumber();
            }
            else {
                this.lexer.nextTokenValue();
            }
            SQLInsertStatement.ValuesClause values;
            if (this.lexer.token() != Token.RPAREN) {
                List valueExprList;
                if (columnSize > 0) {
                    valueExprList = new ArrayList(columnSize);
                }
                else {
                    valueExprList = new ArrayList();
                }
                values = new SQLInsertStatement.ValuesClause(valueExprList, parent);
                int funcExecCount = 0;
                int j = 0;
                while (true) {
                    SQLExpr expr = null;
                    Object value = null;
                    SQLColumnDefinition columnDefinition = null;
                    if (columnDefinitionList != null && j < columnDefinitionList.size()) {
                        columnDefinition = columnDefinitionList.get(j);
                    }
                    SQLDataType dataType = null;
                    if (columnDefinition != null) {
                        dataType = columnDefinition.getDataType();
                    }
                    switch (this.lexer.token) {
                        case LITERAL_INT: {
                            final Number integerValue = this.lexer.integerValue();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                expr = new SQLIntegerExpr(integerValue, values);
                                expr = this.exprParser.exprRest(expr);
                                expr.setParent(values);
                                break;
                            }
                            value = integerValue;
                            break;
                        }
                        case LITERAL_CHARS: {
                            final String strVal = this.lexer.stringVal();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                expr = new SQLCharExpr(strVal, values);
                                expr = this.exprParser.exprRest(expr);
                                expr.setParent(values);
                                break;
                            }
                            value = strVal;
                            break;
                        }
                        case LITERAL_NCHARS: {
                            final String strVal = this.lexer.stringVal();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                expr = new SQLNCharExpr(strVal, values);
                                expr = this.exprParser.exprRest(expr);
                                expr.setParent(values);
                                break;
                            }
                            value = strVal;
                            break;
                        }
                        case LITERAL_FLOAT: {
                            BigDecimal number = this.lexer.decimalValue();
                            if (dataType != null && dataType.nameHashCode64() == FnvHash.Constants.DECIMAL) {
                                int precision = 0;
                                int scale = 0;
                                final List<SQLExpr> arguments = dataType.getArguments();
                                if (arguments.size() > 0) {
                                    final SQLExpr arg0 = arguments.get(0);
                                    if (arg0 instanceof SQLIntegerExpr) {
                                        precision = ((SQLIntegerExpr)arg0).getNumber().intValue();
                                    }
                                }
                                if (arguments.size() > 1) {
                                    final SQLExpr arg0 = arguments.get(1);
                                    if (arg0 instanceof SQLIntegerExpr) {
                                        scale = ((SQLIntegerExpr)arg0).getNumber().intValue();
                                    }
                                }
                                if (number instanceof BigDecimal) {
                                    number = MySqlUtils.decimal(number, precision, scale);
                                }
                            }
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                expr = new SQLDecimalExpr(number);
                                expr = this.exprParser.exprRest(expr);
                                expr.setParent(values);
                                break;
                            }
                            value = number;
                            break;
                        }
                        case NULL: {
                            this.lexer.nextTokenCommaValue();
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                expr = new SQLNullExpr(parent);
                                expr = this.exprParser.exprRest(expr);
                                expr.setParent(values);
                                break;
                            }
                            value = null;
                            break;
                        }
                        case IDENTIFIER: {
                            final long hash = this.lexer.hash_lower();
                            if (hash == FnvHash.Constants.DATE) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                value = Date.valueOf(strVal2);
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.TIMESTAMP && timeZone != null) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                value = new Timestamp(MySqlUtils.parseDate(strVal2, timeZone).getTime());
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.CURDATE || hash == FnvHash.Constants.CUR_DATE || hash == FnvHash.Constants.CURRENT_DATE) {
                                this.lexer.nextTokenValue();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                }
                                if (this.now == null) {
                                    this.now = new Timestamp(System.currentTimeMillis());
                                }
                                if (this.currentDate == null) {
                                    this.currentDate = new Date(this.now.getTime());
                                }
                                value = this.currentDate;
                                ++funcExecCount;
                                break;
                            }
                            if ((hash == FnvHash.Constants.SYSDATE || hash == FnvHash.Constants.NOW || hash == FnvHash.Constants.CURRENT_TIMESTAMP) && timeZone != null) {
                                this.lexer.nextTokenValue();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                }
                                if (this.now == null) {
                                    this.now = new Timestamp(System.currentTimeMillis());
                                }
                                value = this.now;
                                ++funcExecCount;
                                break;
                            }
                            if (hash == FnvHash.Constants.UUID) {
                                this.lexer.nextTokenLParen();
                                this.accept(Token.LPAREN);
                                this.accept(Token.RPAREN);
                                value = UUID.randomUUID().toString();
                                ++funcExecCount;
                                break;
                            }
                            value = null;
                            final Lexer.SavePoint mark = this.lexer.mark();
                            expr = this.exprParser.expr();
                            if (expr instanceof SQLName) {
                                this.lexer.reset(mark);
                                this.lexer.info();
                                throw new ParserException("insert value error, token " + this.lexer.stringVal() + ", line " + this.lexer.posLine + ", column " + this.lexer.posColumn, this.lexer.posLine, this.lexer.posColumn);
                            }
                            expr.setParent(values);
                            break;
                        }
                        default: {
                            value = null;
                            expr = this.exprParser.expr();
                            expr.setParent(values);
                            break;
                        }
                    }
                    if (expr != null) {
                        expr.setParent(values);
                        value = expr;
                    }
                    if (this.lexer.token == Token.COMMA) {
                        valueExprList.add(value);
                        if (this.lexer.ch == '\'') {
                            this.lexer.bufPos = 0;
                            if (this.dbType == DbType.mysql) {
                                this.lexer.scanString2();
                            }
                            else {
                                this.lexer.scanString();
                            }
                        }
                        else if (this.lexer.ch == '0') {
                            this.lexer.bufPos = 0;
                            if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                                this.lexer.scanChar();
                                this.lexer.scanChar();
                                this.lexer.scanHexaDecimal();
                            }
                            else {
                                this.lexer.scanNumber();
                            }
                        }
                        else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else {
                            this.lexer.nextTokenValue();
                        }
                    }
                    else {
                        if (this.lexer.token == Token.RPAREN) {
                            valueExprList.add(value);
                            break;
                        }
                        expr = this.exprParser.primaryRest(expr);
                        if (this.lexer.token != Token.COMMA && this.lexer.token() != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                        }
                        expr.setParent(values);
                        valueExprList.add(expr);
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextTokenValue();
                    }
                    ++j;
                }
                if (funcExecCount == 0 && this.lexer.isEnabled(SQLParserFeature.KeepInsertValueClauseOriginalString)) {
                    final int endPos = this.lexer.pos();
                    final String orginalString = this.lexer.subString(startPos, endPos - startPos);
                    values.setOriginalString(orginalString);
                }
            }
            else {
                values = new SQLInsertStatement.ValuesClause(new ArrayList<SQLExpr>(0));
            }
            valueClauseList.add(values);
            if (this.lexer.token != Token.RPAREN) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            if (!this.parseCompleteValues && valueClauseList.size() >= this.parseValuesSize) {
                this.lexer.skipToEOF();
                break;
            }
            this.lexer.nextTokenComma();
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextTokenLParen();
            if (values != null) {
                columnSize = values.getValues().size();
            }
            ++i;
        }
    }
    
    public void parseValueClause(final SQLInsertValueHandler valueHandler) throws SQLException {
        while (this.lexer.token == Token.LPAREN) {
            if (this.lexer.ch == '\'') {
                this.lexer.bufPos = 0;
                if (this.dbType == DbType.mysql) {
                    this.lexer.scanString2();
                }
                else {
                    this.lexer.scanString();
                }
            }
            else if (this.lexer.ch == '0') {
                this.lexer.bufPos = 0;
                if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                    this.lexer.scanChar();
                    this.lexer.scanChar();
                    this.lexer.scanHexaDecimal();
                }
                else {
                    this.lexer.scanNumber();
                }
            }
            else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                this.lexer.bufPos = 0;
                this.lexer.scanNumber();
            }
            else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                this.lexer.scanNumber();
            }
            else {
                this.lexer.nextTokenValue();
            }
            if (this.lexer.token() != Token.RPAREN) {
                final Object row = valueHandler.newRow();
                int j = 0;
                while (true) {
                    switch (this.lexer.token) {
                        case LITERAL_INT: {
                            final Number number = this.lexer.integerValue();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            valueHandler.processInteger(row, j, number);
                            break;
                        }
                        case LITERAL_CHARS:
                        case LITERAL_NCHARS: {
                            final String strVal = this.lexer.stringVal();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            valueHandler.processString(row, j, strVal);
                            break;
                        }
                        case LITERAL_FLOAT: {
                            final BigDecimal number2 = this.lexer.decimalValue();
                            if (this.lexer.ch == ',') {
                                this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                                this.lexer.token = Token.COMMA;
                            }
                            else {
                                this.lexer.nextTokenCommaValue();
                            }
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            valueHandler.processDecimal(row, j, number2);
                            break;
                        }
                        case NULL: {
                            this.lexer.nextTokenCommaValue();
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            valueHandler.processNull(row, j);
                            break;
                        }
                        case TRUE: {
                            valueHandler.processBoolean(row, j, true);
                            this.lexer.nextTokenComma();
                            break;
                        }
                        case FALSE: {
                            valueHandler.processBoolean(row, j, false);
                            this.lexer.nextTokenComma();
                            break;
                        }
                        case IDENTIFIER: {
                            final long hash = this.lexer.hash_lower();
                            if (hash == FnvHash.Constants.DATE) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                valueHandler.processDate(row, j, strVal2);
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.TIMESTAMP) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                valueHandler.processTimestamp(row, j, strVal2);
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.TIME) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                valueHandler.processTime(row, j, strVal2);
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.DECIMAL) {
                                this.lexer.nextTokenValue();
                                final String strVal2 = this.lexer.stringVal();
                                final BigDecimal decimal = new BigDecimal(strVal2);
                                valueHandler.processDecimal(row, j, decimal);
                                this.lexer.nextTokenComma();
                                break;
                            }
                            if (hash == FnvHash.Constants.CURDATE || hash == FnvHash.Constants.CUR_DATE || hash == FnvHash.Constants.CURRENT_DATE || hash == FnvHash.Constants.SYSDATE) {
                                this.lexer.nextTokenLParen();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                }
                                if (this.currentDate == null) {
                                    this.currentDate = new Date(this.now.getTime());
                                }
                                valueHandler.processDate(row, j, this.currentDate);
                                break;
                            }
                            if (hash == FnvHash.Constants.NOW || hash == FnvHash.Constants.CURRENT_TIMESTAMP) {
                                this.lexer.nextTokenLParen();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                }
                                if (this.now == null) {
                                    this.now = new Timestamp(System.currentTimeMillis());
                                }
                                valueHandler.processTimestamp(row, j, this.now);
                                break;
                            }
                            if (hash == FnvHash.Constants.UUID) {
                                final String funcName = this.lexer.stringVal();
                                this.lexer.nextTokenLParen();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                    if (this.now == null) {
                                        this.now = new Timestamp(System.currentTimeMillis());
                                    }
                                    valueHandler.processFunction(row, j, funcName, hash, new Object[0]);
                                    break;
                                }
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            else if (hash == FnvHash.Constants.STR_TO_DATE || hash == FnvHash.Constants.DATE_PARSE) {
                                final String funcName = this.lexer.stringVal();
                                this.lexer.nextTokenLParen();
                                if (this.lexer.token == Token.LPAREN) {
                                    this.lexer.nextTokenValue();
                                    final String strVal3 = this.lexer.stringVal();
                                    this.lexer.nextTokenComma();
                                    this.accept(Token.COMMA);
                                    final String format = this.lexer.stringVal();
                                    this.lexer.nextTokenValue();
                                    this.accept(Token.RPAREN);
                                    valueHandler.processFunction(row, j, funcName, hash, strVal3, format);
                                    break;
                                }
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            else {
                                if (FnvHash.Constants.CLOTHES_FEATURE_EXTRACT_V1 != hash && FnvHash.Constants.CLOTHES_ATTRIBUTE_EXTRACT_V1 != hash && FnvHash.Constants.GENERIC_FEATURE_EXTRACT_V1 != hash && FnvHash.Constants.TEXT_FEATURE_EXTRACT_V1 != hash && FnvHash.Constants.FACE_FEATURE_EXTRACT_V1 != hash) {
                                    throw new ParserException("insert value error, " + this.lexer.info());
                                }
                                final String funcName = this.lexer.stringVal();
                                this.lexer.nextTokenLParen();
                                if (Token.LPAREN == this.lexer.token) {
                                    this.lexer.nextTokenValue();
                                    final String urlVal = this.lexer.stringVal();
                                    this.lexer.nextToken();
                                    this.accept(Token.RPAREN);
                                    valueHandler.processFunction(row, j, funcName, hash, urlVal);
                                    break;
                                }
                                throw new ParserException("insert value error, " + this.lexer.info());
                            }
                            break;
                        }
                        default: {
                            throw new ParserException("insert value error, " + this.lexer.info());
                        }
                    }
                    if (this.lexer.token == Token.COMMA) {
                        if (this.lexer.ch == '\'') {
                            this.lexer.bufPos = 0;
                            if (this.dbType == DbType.mysql) {
                                this.lexer.scanString2();
                            }
                            else {
                                this.lexer.scanString();
                            }
                        }
                        else if (this.lexer.ch == '0') {
                            this.lexer.bufPos = 0;
                            if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                                this.lexer.scanChar();
                                this.lexer.scanChar();
                                this.lexer.scanHexaDecimal();
                            }
                            else {
                                this.lexer.scanNumber();
                            }
                        }
                        else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else {
                            this.lexer.nextTokenValue();
                        }
                        ++j;
                    }
                    else {
                        if (this.lexer.token == Token.RPAREN) {
                            valueHandler.processRow(row);
                            break;
                        }
                        throw new ParserException("insert value error, " + this.lexer.info());
                    }
                }
            }
            if (this.lexer.token != Token.RPAREN) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            this.lexer.nextTokenComma();
            if (this.lexer.token != Token.COMMA) {
                valueHandler.processComplete();
                return;
            }
            this.lexer.nextTokenLParen();
        }
        throw new ParserException("syntax error, expect ')', " + this.lexer.info());
    }
    
    protected void parseValueClause(final List<SQLInsertStatement.ValuesClause> valueClauseList, final List<SQLColumnDefinition> columnDefinitionList, int columnSize, final SQLObject parent) {
        final boolean optimizedForParameterized = this.lexer.isEnabled(SQLParserFeature.OptimizedForForParameterizedSkipValue);
        int i = 0;
        while (true) {
            final int startPos = this.lexer.pos - 1;
            if (this.lexer.token == Token.ROW) {
                this.lexer.nextToken();
            }
            if (this.lexer.token != Token.LPAREN) {
                throw new ParserException("syntax error, expect ')', " + this.lexer.info());
            }
            if (this.lexer.ch == '\'') {
                this.lexer.bufPos = 0;
                if (this.dbType == DbType.mysql) {
                    this.lexer.scanString2();
                }
                else {
                    this.lexer.scanString();
                }
            }
            else if (this.lexer.ch == '0') {
                this.lexer.bufPos = 0;
                if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                    this.lexer.scanChar();
                    this.lexer.scanChar();
                    this.lexer.scanHexaDecimal();
                }
                else {
                    this.lexer.scanNumber();
                }
            }
            else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                this.lexer.bufPos = 0;
                this.lexer.scanNumber();
            }
            else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                this.lexer.bufPos = 0;
                this.lexer.scanNumber();
            }
            else {
                this.lexer.nextTokenValue();
            }
            SQLInsertStatement.ValuesClause values;
            if (this.lexer.token() != Token.RPAREN) {
                List valueExprList;
                if (columnSize > 0) {
                    valueExprList = new ArrayList(columnSize);
                }
                else {
                    valueExprList = new ArrayList();
                }
                values = new SQLInsertStatement.ValuesClause(valueExprList, parent);
                int j = 0;
                while (true) {
                    SQLColumnDefinition columnDefinition = null;
                    if (columnDefinitionList != null && j < columnDefinitionList.size()) {
                        columnDefinition = columnDefinitionList.get(j);
                    }
                    SQLDataType dataType = null;
                    if (columnDefinition != null) {
                        dataType = columnDefinition.getDataType();
                    }
                    SQLExpr expr;
                    if (this.lexer.token == Token.LITERAL_INT) {
                        if (optimizedForParameterized) {
                            expr = new SQLVariantRefExpr("?", values);
                            values.incrementReplaceCount();
                        }
                        else {
                            expr = new SQLIntegerExpr(this.lexer.integerValue(), values);
                        }
                        if (this.lexer.ch == ',') {
                            this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                            this.lexer.token = Token.COMMA;
                        }
                        else {
                            this.lexer.nextTokenCommaValue();
                        }
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                            expr.setParent(values);
                        }
                    }
                    else if (this.lexer.token == Token.LITERAL_CHARS) {
                        if (optimizedForParameterized) {
                            expr = new SQLVariantRefExpr("?", values);
                            values.incrementReplaceCount();
                        }
                        else {
                            expr = new SQLCharExpr(this.lexer.stringVal(), values);
                        }
                        if (this.lexer.ch == ',') {
                            this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                            this.lexer.token = Token.COMMA;
                        }
                        else {
                            this.lexer.nextTokenCommaValue();
                        }
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                            expr.setParent(values);
                        }
                    }
                    else if (this.lexer.token == Token.LITERAL_NCHARS) {
                        if (optimizedForParameterized) {
                            expr = new SQLVariantRefExpr("?", values);
                            values.incrementReplaceCount();
                        }
                        else {
                            expr = new SQLNCharExpr(this.lexer.stringVal(), values);
                        }
                        if (this.lexer.ch == ',') {
                            this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                            this.lexer.token = Token.COMMA;
                        }
                        else {
                            this.lexer.nextTokenCommaValue();
                        }
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                            expr.setParent(values);
                        }
                    }
                    else if (this.lexer.token == Token.LITERAL_FLOAT) {
                        if (optimizedForParameterized) {
                            expr = new SQLVariantRefExpr("?", values);
                            values.incrementReplaceCount();
                        }
                        else {
                            final SQLNumberExpr numberExpr = this.lexer.numberExpr(parent);
                            if (dataType != null && dataType.nameHashCode64() == FnvHash.Constants.DECIMAL) {
                                Number number = numberExpr.getNumber();
                                int precision = 0;
                                int scale = 0;
                                final List<SQLExpr> arguments = dataType.getArguments();
                                if (arguments.size() > 0) {
                                    final SQLExpr arg0 = arguments.get(0);
                                    if (arg0 instanceof SQLIntegerExpr) {
                                        precision = ((SQLIntegerExpr)arg0).getNumber().intValue();
                                    }
                                }
                                if (arguments.size() > 1) {
                                    final SQLExpr arg0 = arguments.get(1);
                                    if (arg0 instanceof SQLIntegerExpr) {
                                        scale = ((SQLIntegerExpr)arg0).getNumber().intValue();
                                    }
                                }
                                if (number instanceof BigDecimal) {
                                    number = MySqlUtils.decimal((BigDecimal)number, precision, scale);
                                    numberExpr.setNumber(number);
                                }
                            }
                            expr = numberExpr;
                        }
                        if (this.lexer.ch == ',') {
                            this.lexer.ch = this.lexer.charAt(++this.lexer.pos);
                            this.lexer.token = Token.COMMA;
                        }
                        else {
                            this.lexer.nextTokenCommaValue();
                        }
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                            expr.setParent(values);
                        }
                    }
                    else if (this.lexer.token == Token.NULL) {
                        if (optimizedForParameterized) {
                            expr = new SQLVariantRefExpr("?", parent);
                            values.incrementReplaceCount();
                        }
                        else {
                            expr = new SQLNullExpr(parent);
                        }
                        this.lexer.nextTokenCommaValue();
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                            expr.setParent(values);
                        }
                    }
                    else {
                        expr = this.exprParser.expr();
                        expr.setParent(values);
                    }
                    if (this.lexer.token == Token.COMMA) {
                        valueExprList.add(expr);
                        if (this.lexer.ch == '\'') {
                            this.lexer.bufPos = 0;
                            if (this.dbType == DbType.mysql) {
                                this.lexer.scanString2();
                            }
                            else {
                                this.lexer.scanString();
                            }
                        }
                        else if (this.lexer.ch == '0') {
                            this.lexer.bufPos = 0;
                            if (this.lexer.charAt(this.lexer.pos + 1) == 'x') {
                                this.lexer.scanChar();
                                this.lexer.scanChar();
                                this.lexer.scanHexaDecimal();
                            }
                            else {
                                this.lexer.scanNumber();
                            }
                        }
                        else if (this.lexer.ch > '0' && this.lexer.ch <= '9') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else if (this.lexer.ch == '-' && this.lexer.charAt(this.lexer.pos + 1) != '-') {
                            this.lexer.bufPos = 0;
                            this.lexer.scanNumber();
                        }
                        else {
                            this.lexer.nextTokenValue();
                        }
                    }
                    else {
                        if (this.lexer.token == Token.RPAREN) {
                            valueExprList.add(expr);
                            break;
                        }
                        expr = this.exprParser.primaryRest(expr);
                        if (this.lexer.token != Token.COMMA && this.lexer.token() != Token.RPAREN) {
                            expr = this.exprParser.exprRest(expr);
                        }
                        expr.setParent(values);
                        valueExprList.add(expr);
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextTokenValue();
                    }
                    ++j;
                }
                if (this.lexer.isEnabled(SQLParserFeature.KeepInsertValueClauseOriginalString)) {
                    final int endPos = this.lexer.pos();
                    final String orginalString = this.lexer.subString(startPos, endPos - startPos);
                    values.setOriginalString(orginalString);
                }
            }
            else {
                values = new SQLInsertStatement.ValuesClause(new ArrayList<SQLExpr>(0));
            }
            valueClauseList.add(values);
            if (this.lexer.token != Token.RPAREN) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            if (!this.parseCompleteValues && valueClauseList.size() >= this.parseValuesSize) {
                this.lexer.skipToEOF();
                break;
            }
            this.lexer.nextTokenComma();
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextTokenLParen();
            if (values != null) {
                columnSize = values.getValues().size();
            }
            ++i;
        }
    }
    
    public SQLSelectListCache getSelectListCache() {
        return this.selectListCache;
    }
    
    public void setSelectListCache(final SQLSelectListCache selectListCache) {
        this.selectListCache = selectListCache;
    }
    
    protected HiveInsertStatement parseHiveInsertStmt() {
        final HiveInsertStatement insert = new HiveInsertStatement();
        insert.setDbType(this.dbType);
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            insert.addInsertBeforeComment(this.lexer.readAndResetComments());
        }
        final SQLSelectParser selectParser = this.createSQLSelectParser();
        this.accept(Token.INSERT);
        if (this.lexer.token == Token.INTO) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.OVERWRITE);
            insert.setOverwrite(true);
        }
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
        }
        insert.setTableSource(this.exprParser.name());
        boolean columnsParsed = false;
        if (this.lexer.token == Token.LPAREN) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token == Token.SELECT) {
                this.lexer.reset(mark);
            }
            else {
                this.parseInsertColumns(insert);
                columnsParsed = true;
                this.accept(Token.RPAREN);
            }
        }
        if (this.lexer.token == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLAssignItem ptExpr = new SQLAssignItem();
                ptExpr.setTarget(this.exprParser.name());
                if (this.lexer.token == Token.EQ || this.lexer.token == Token.EQEQ) {
                    this.lexer.nextTokenValue();
                    final SQLExpr ptValue = this.exprParser.expr();
                    ptExpr.setValue(ptValue);
                }
                insert.addPartition(ptExpr);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (!columnsParsed && this.lexer.token == Token.LPAREN) {
            final Lexer.SavePoint m1 = this.lexer.mark();
            this.lexer.nextToken();
            boolean select;
            if (this.lexer.token == Token.LPAREN) {
                final Lexer.SavePoint m2 = this.lexer.mark();
                this.lexer.nextToken();
                select = (this.lexer.token == Token.SELECT);
                this.lexer.reset(m2);
            }
            else {
                select = (this.lexer.token == Token.SELECT);
            }
            if (!select) {
                this.parseInsertColumns(insert);
                this.accept(Token.RPAREN);
            }
            else {
                this.lexer.reset(m1);
            }
        }
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            insert.setIfNotExists(true);
        }
        if (this.lexer.token == Token.VALUES) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
                    this.exprParser.exprList(values.getValues(), values);
                    insert.addValueCause(values);
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else {
            final SQLSelect query = selectParser.select();
            insert.setQuery(query);
        }
        return insert;
    }
    
    protected HiveInsert parseHiveInsert() {
        final HiveInsert insert = new HiveInsert();
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            insert.addBeforeComment(this.lexer.readAndResetComments());
        }
        final SQLSelectParser selectParser = this.createSQLSelectParser();
        this.accept(Token.INSERT);
        if (this.lexer.token == Token.INTO) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.OVERWRITE);
            insert.setOverwrite(true);
        }
        if (this.lexer.token == Token.TABLE) {
            this.lexer.nextToken();
        }
        insert.setTableSource(this.exprParser.name());
        if (this.lexer.token == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLAssignItem ptExpr = new SQLAssignItem();
                ptExpr.setTarget(this.exprParser.name());
                if (this.lexer.token == Token.EQ) {
                    this.lexer.nextToken();
                    final SQLExpr ptValue = this.exprParser.expr();
                    ptExpr.setValue(ptValue);
                }
                insert.addPartition(ptExpr);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(insert.getColumns(), insert);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.VALUES) {
            this.lexer.nextToken();
            while (true) {
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
                    this.exprParser.exprList(values.getValues(), values);
                    insert.addValueCause(values);
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else {
            final SQLSelect query = selectParser.select();
            insert.setQuery(query);
        }
        return insert;
    }
    
    protected SQLShowDatabasesStatement parseShowDatabases(final boolean isPhysical) {
        final SQLShowDatabasesStatement stmt = new SQLShowDatabasesStatement();
        stmt.setPhysical(isPhysical);
        if (this.lexer.token == Token.LIKE) {
            this.lexer.nextToken();
            final SQLExpr like = this.exprParser.expr();
            stmt.setLike(like);
        }
        if (this.lexer.token == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTRA)) {
            this.lexer.nextToken();
            stmt.setExtra(true);
        }
        return stmt;
    }
    
    protected SQLShowTableGroupsStatement parseShowTableGroups() {
        final SQLShowTableGroupsStatement stmt = new SQLShowTableGroupsStatement();
        if (this.lexer.token == Token.IN) {
            this.lexer.nextToken();
            final SQLName db = this.exprParser.name();
            stmt.setDatabase(db);
        }
        return stmt;
    }
    
    protected SQLShowTablesStatement parseShowTables() {
        final SQLShowTablesStatement stmt = new SQLShowTablesStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.SHOW)) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTENDED)) {
            this.lexer.nextToken();
            stmt.setExtended(true);
        }
        if (this.lexer.token == Token.FROM || this.lexer.token == Token.IN) {
            this.lexer.nextToken();
            SQLName database = this.exprParser.name();
            if (this.lexer.token == Token.SUB && database instanceof SQLIdentifierExpr) {
                this.lexer.mark();
                this.lexer.nextToken();
                final String strVal = this.lexer.stringVal();
                this.lexer.nextToken();
                if (database instanceof SQLIdentifierExpr) {
                    final SQLIdentifierExpr ident = (SQLIdentifierExpr)database;
                    database = new SQLIdentifierExpr(ident.getName() + "-" + strVal);
                }
            }
            stmt.setDatabase(database);
        }
        if (this.lexer.token == Token.LIKE) {
            this.lexer.nextToken();
            final SQLExpr like = this.exprParser.expr();
            stmt.setLike(like);
        }
        if (this.lexer.token == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        return stmt;
    }
    
    protected SQLShowColumnsStatement parseShowColumns() {
        final SQLShowColumnsStatement stmt = new SQLShowColumnsStatement();
        if (this.lexer.token == Token.FROM) {
            this.lexer.nextToken();
            final SQLName table = this.exprParser.name();
            stmt.setTable(table);
            if (this.lexer.token == Token.FROM || this.lexer.token == Token.IN) {
                this.lexer.nextToken();
                final SQLName database = this.exprParser.name();
                stmt.setDatabase(database);
            }
        }
        else if (this.lexer.token == Token.IN) {
            this.lexer.nextToken();
            final SQLName table = this.exprParser.name();
            stmt.setTable(table);
        }
        if (this.lexer.token == Token.LIKE) {
            this.lexer.nextToken();
            final SQLExpr like = this.exprParser.expr();
            stmt.setLike(like);
        }
        if (this.lexer.token == Token.WHERE) {
            this.lexer.nextToken();
            final SQLExpr where = this.exprParser.expr();
            stmt.setWhere(where);
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterIndex() {
        this.accept(Token.ALTER);
        this.lexer.nextToken();
        final SQLAlterIndexStatement stmt = new SQLAlterIndexStatement();
        stmt.setName(this.exprParser.name());
        if (this.lexer.identifierEquals("RENAME")) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            stmt.setRenameTo(this.exprParser.name());
        }
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.TABLE) {
                this.lexer.nextToken();
            }
            final SQLName table = this.exprParser.name();
            stmt.setTable(table);
        }
        if (this.lexer.token == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.parseAssignItems(stmt.getPartitions(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION)) {
            final SQLPartitionBy partitionClause = this.getSQLCreateTableParser().parsePartitionBy();
            stmt.setDbPartitionBy(partitionClause);
        }
        if (this.lexer.token == Token.ENABLE) {
            this.lexer.nextToken();
            stmt.setEnable(true);
        }
        if (this.lexer.token == Token.DISABLE) {
            this.lexer.nextToken();
            stmt.setEnable(false);
        }
        if (this.lexer.hash_lower == FnvHash.Constants.UNUSABLE) {
            this.lexer.nextToken();
            stmt.setUnusable(true);
        }
        while (true) {
            if (this.lexer.identifierEquals("rebuild")) {
                this.lexer.nextToken();
                final SQLAlterIndexStatement.Rebuild rebuild = new SQLAlterIndexStatement.Rebuild();
                stmt.setRebuild(rebuild);
            }
            else {
                if (!this.lexer.identifierEquals("MONITORING")) {
                    break;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("USAGE");
                stmt.setMonitoringUsage(Boolean.TRUE);
            }
        }
        if (this.lexer.identifierEquals("PARALLEL")) {
            this.lexer.nextToken();
            stmt.setParallel(this.exprParser.expr());
        }
        return stmt;
    }
    
    protected SQLStatement parseAnalyze() {
        this.lexer.nextToken();
        this.accept(Token.TABLE);
        final SQLAnalyzeTableStatement stmt = new SQLAnalyzeTableStatement();
        final SQLName table = this.exprParser.name();
        stmt.setTable(table);
        if (this.lexer.token() == Token.PARTITION) {
            stmt.setPartition(this.parsePartitionRef());
        }
        this.accept(Token.COMPUTE);
        this.acceptIdentifier("STATISTICS");
        stmt.setComputeStatistics(true);
        if (this.lexer.token == Token.FOR) {
            this.lexer.nextToken();
            this.acceptIdentifier("COLUMNS");
            stmt.setForColums(true);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprParser.names(stmt.getColumns(), stmt);
                this.accept(Token.RPAREN);
            }
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
    
    public SQLAlterSequenceStatement parseAlterSequence() {
        this.accept(Token.ALTER);
        this.accept(Token.SEQUENCE);
        final SQLAlterSequenceStatement stmt = new SQLAlterSequenceStatement();
        stmt.setDbType(this.dbType);
        stmt.setName(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.CHANGE)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            if (this.lexer.identifierEquals(FnvHash.Constants.SIMPLE)) {
                stmt.setChangeToSimple(true);
                this.lexer.nextToken();
                if (this.lexer.hash_lower() == FnvHash.Constants.WITH) {
                    this.lexer.nextToken();
                    this.accept(Token.CACHE);
                    stmt.setWithCache(true);
                }
            }
            else if (this.lexer.token == Token.GROUP) {
                stmt.setChangeToGroup(true);
                this.lexer.nextToken();
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                stmt.setChangeToTime(true);
                this.lexer.nextToken();
            }
        }
        while (true) {
            if (this.lexer.token() == Token.START || this.lexer.identifierEquals(FnvHash.Constants.START)) {
                this.lexer.nextToken();
                this.accept(Token.WITH);
                stmt.setStartWith(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("INCREMENT")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setIncrementBy(this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.CACHE) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.TRUE);
                if (this.lexer.token() != Token.LITERAL_INT && this.lexer.token() != Token.QUES) {
                    continue;
                }
                stmt.setCacheValue(this.exprParser.primary());
            }
            else if (this.lexer.token() == Token.NOCACHE) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.ORDER) {
                this.lexer.nextToken();
                stmt.setOrder(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.RESTART)) {
                this.lexer.nextToken();
                stmt.setRestart(true);
                if (this.lexer.token == Token.WITH || this.lexer.token == Token.EQ) {
                    this.lexer.nextToken();
                    stmt.setRestartWith(this.exprParser.primary());
                }
                else {
                    if (this.lexer.token != Token.LITERAL_INT) {
                        continue;
                    }
                    stmt.setRestartWith(this.exprParser.primary());
                }
            }
            else if (this.lexer.identifierEquals("NOORDER")) {
                this.lexer.nextToken();
                stmt.setOrder(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("CYCLE")) {
                this.lexer.nextToken();
                stmt.setCycle(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOCYCLE")) {
                this.lexer.nextToken();
                stmt.setCycle(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("MINVALUE")) {
                this.lexer.nextToken();
                stmt.setMinValue(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("MAXVALUE")) {
                this.lexer.nextToken();
                stmt.setMaxValue(this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("NOMAXVALUE")) {
                this.lexer.nextToken();
                stmt.setNoMaxValue(true);
            }
            else {
                if (!this.lexer.identifierEquals("NOMINVALUE")) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setNoMinValue(true);
            }
        }
        return stmt;
    }
    
    protected SQLStatement parseMsck() {
        this.lexer.nextToken();
        if (this.lexer.identifierEquals("REPAIR")) {
            this.lexer.nextToken();
        }
        final HiveMsckRepairStatement stmt = new HiveMsckRepairStatement();
        if (this.lexer.token() == Token.DATABASE || this.lexer.token() == Token.SCHEMA) {
            this.lexer.nextToken();
            final SQLName name = this.exprParser.name();
            stmt.setDatabase(name);
        }
        if (this.lexer.token() == Token.TABLE) {
            this.lexer.nextToken();
            final SQLExpr tableExpr = this.exprParser.expr();
            stmt.setTable(tableExpr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
            this.lexer.nextToken();
            this.acceptIdentifier("PARTITIONS");
            stmt.setAddPartitions(true);
        }
        return stmt;
    }
    
    protected SQLStatement parseCreateResourceGroup() {
        this.accept(Token.CREATE);
        this.acceptIdentifier("RESOURCE");
        this.accept(Token.GROUP);
        final SQLCreateResourceGroupStatement stmt = new SQLCreateResourceGroupStatement();
        stmt.setName(this.exprParser.name());
        while (this.lexer.token() != Token.SEMI) {
            if (this.lexer.token() == Token.EOF) {
                break;
            }
            if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                stmt.setEnable(true);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                stmt.setEnable(false);
            }
            final Lexer.SavePoint m = this.lexer.mark();
            final String name = this.lexer.stringVal();
            this.lexer.nextToken();
            if (this.lexer.token() != Token.EQ) {
                this.lexer.reset(m);
                break;
            }
            this.lexer.nextToken();
            final SQLExpr value = this.exprParser.expr();
            if (this.lexer.token() == Token.COMMA) {
                final SQLListExpr list = new SQLListExpr();
                list.addItem(value);
                while (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    list.addItem(this.exprParser.expr());
                }
                stmt.addProperty(name, list);
            }
            else {
                stmt.addProperty(name, value);
            }
        }
        return stmt;
    }
    
    protected SQLStatement parseAlterResourceGroup() {
        this.accept(Token.ALTER);
        this.acceptIdentifier("RESOURCE");
        this.accept(Token.GROUP);
        final SQLAlterResourceGroupStatement stmt = new SQLAlterResourceGroupStatement();
        stmt.setName(this.exprParser.name());
        while (this.lexer.token() != Token.SEMI) {
            if (this.lexer.token() == Token.EOF) {
                break;
            }
            if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                stmt.setEnable(true);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                stmt.setEnable(false);
            }
            final Lexer.SavePoint m = this.lexer.mark();
            final String name = this.lexer.stringVal();
            this.lexer.nextToken();
            if (this.lexer.token() != Token.EQ) {
                this.lexer.reset(m);
                break;
            }
            this.lexer.nextToken();
            final SQLExpr value = this.exprParser.expr();
            if (this.lexer.token() == Token.COMMA) {
                final SQLListExpr list = new SQLListExpr();
                list.addItem(value);
                while (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    list.addItem(this.exprParser.expr());
                }
                stmt.addProperty(name, list);
            }
            else {
                stmt.addProperty(name, value);
            }
        }
        return stmt;
    }
    
    public SQLStatement parseAlterMaterialized() {
        final SQLAlterMaterializedViewStatement stmt = new SQLAlterMaterializedViewStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token == Token.ALTER) {
            this.lexer.nextToken();
        }
        this.acceptIdentifier("MATERIALIZED");
        this.accept(Token.VIEW);
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.EOF) {
            throw new ParserException("syntax error. " + this.lexer.info());
        }
        while (true) {
            if (this.lexer.identifierEquals("REFRESH")) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EOF) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                boolean refresh = false;
                while (true) {
                    if (this.lexer.identifierEquals("FAST")) {
                        this.lexer.nextToken();
                        stmt.setRefreshFast(true);
                        refresh = true;
                    }
                    else if (this.lexer.identifierEquals("COMPLETE")) {
                        this.lexer.nextToken();
                        stmt.setRefreshComplete(true);
                        refresh = true;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                        this.lexer.nextToken();
                        stmt.setRefreshForce(true);
                        refresh = true;
                    }
                    else if (this.lexer.token == Token.ON) {
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.COMMIT || this.lexer.identifierEquals(FnvHash.Constants.COMMIT)) {
                            this.lexer.nextToken();
                            stmt.setRefreshOnCommit(true);
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.OVERWRITE)) {
                            this.lexer.nextToken();
                            stmt.setRefreshOnOverWrite(true);
                        }
                        else {
                            this.acceptIdentifier("DEMAND");
                            stmt.setRefreshOnDemand(true);
                        }
                        refresh = true;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.START)) {
                        this.lexer.nextToken();
                        this.accept(Token.WITH);
                        final SQLExpr startWith = this.exprParser.expr();
                        stmt.setStartWith(startWith);
                        stmt.setRefreshStartWith(true);
                        refresh = true;
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.NEXT)) {
                            break;
                        }
                        this.lexer.nextToken();
                        final SQLExpr next = this.exprParser.expr();
                        stmt.setNext(next);
                        stmt.setRefreshNext(true);
                        refresh = true;
                    }
                }
                if (!refresh) {
                    throw new ParserException("refresh clause is empty. " + this.lexer.info());
                }
                continue;
            }
            else {
                if (!this.lexer.identifierEquals("REBUILD")) {
                    Boolean enableQueryRewrite = null;
                    if (this.lexer.token == Token.ENABLE) {
                        this.lexer.nextToken();
                        enableQueryRewrite = true;
                    }
                    if (this.lexer.token == Token.DISABLE) {
                        this.lexer.nextToken();
                        enableQueryRewrite = false;
                    }
                    if (enableQueryRewrite != null) {
                        this.acceptIdentifier("QUERY");
                        this.acceptIdentifier("REWRITE");
                        stmt.setEnableQueryRewrite(enableQueryRewrite);
                    }
                    if (this.lexer.token == Token.PARTITION) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        this.exprParser.exprList(stmt.getPartitions(), stmt);
                        this.accept(Token.RPAREN);
                    }
                    return stmt;
                }
                this.lexer.nextToken();
                stmt.setRebuild(true);
            }
        }
    }
    
    public SQLCreateFunctionStatement parseHiveCreateFunction() {
        final HiveCreateFunctionStatement stmt = new HiveCreateFunctionStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.token == Token.CREATE) {
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            stmt.setOrReplace(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY)) {
            this.lexer.nextToken();
            stmt.setTemporary(true);
        }
        boolean sql = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.SQL)) {
            this.lexer.nextToken();
            sql = true;
        }
        this.accept(Token.FUNCTION);
        if (this.lexer.token == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            while (this.lexer.token != Token.RPAREN) {
                final SQLParameter param = new SQLParameter();
                param.setName(this.exprParser.name());
                param.setDataType(this.exprParser.parseDataType());
                if (this.lexer.token == Token.COMMA) {
                    this.lexer.nextToken();
                }
                stmt.getParameters().add(param);
                param.setParent(stmt);
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.RETURNS)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.VARIANT) {
                this.lexer.nextToken();
            }
            final SQLDataType returnDataType = this.exprParser.parseDataType();
            stmt.setReturnDataType(returnDataType);
        }
        if (this.lexer.token == Token.IDENTIFIER && this.lexer.stringVal().toUpperCase().startsWith("RETURNS@")) {
            this.lexer.nextToken();
            final SQLDataType returnDataType = this.exprParser.parseDataType();
            stmt.setReturnDataType(returnDataType);
        }
        if (this.lexer.token() == Token.AS) {
            this.lexer.setToken(Token.IDENTIFIER);
            this.lexer.nextToken();
            final SQLExpr className = this.exprParser.expr();
            stmt.setClassName(className);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
            this.lexer.nextToken();
            final SQLExpr location = this.exprParser.primary();
            stmt.setLocation(location);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SYMBOL)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLExpr symbol = this.exprParser.primary();
            stmt.setSymbol(symbol);
        }
        if (this.lexer.token() == Token.USING || this.lexer.hash_lower() == FnvHash.Constants.USING) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.JAR)) {
                this.lexer.nextToken();
                stmt.setResourceType(HiveCreateFunctionStatement.ResourceType.JAR);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.ARCHIVE)) {
                this.lexer.nextToken();
                stmt.setResourceType(HiveCreateFunctionStatement.ResourceType.ARCHIVE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.FILE)) {
                this.lexer.nextToken();
                stmt.setResourceType(HiveCreateFunctionStatement.ResourceType.FILE);
            }
            else if (this.lexer.token == Token.CODE) {
                stmt.setCode(this.lexer.stringVal());
                this.lexer.nextToken();
                stmt.setResourceType(HiveCreateFunctionStatement.ResourceType.CODE);
                return stmt;
            }
            final SQLExpr location = this.exprParser.primary();
            stmt.setLocation(location);
        }
        return stmt;
    }
    
    protected SQLShowCreateTableStatement parseShowCreateTable() {
        this.lexer.nextToken();
        this.accept(Token.TABLE);
        final SQLShowCreateTableStatement stmt = new SQLShowCreateTableStatement();
        stmt.setDbType(this.dbType);
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.MAPPING)) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLName name = this.exprParser.name();
                stmt.setLikeMapping(name);
                this.accept(Token.RPAREN);
            }
        }
        return stmt;
    }
    
    public SQLShowVariantsStatement parseShowVariants() {
        final SQLShowVariantsStatement stmt = new SQLShowVariantsStatement();
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
    
    public SQLStatement parseClone() {
        throw new ParserException("TODO " + this.lexer.info());
    }
}
