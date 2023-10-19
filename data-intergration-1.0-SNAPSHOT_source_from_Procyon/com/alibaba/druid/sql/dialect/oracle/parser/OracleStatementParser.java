// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.sql.ast.SQLRecordDataType;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLDeclareStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddSupplemental;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.alibaba.druid.sql.ast.statement.SQLAlterFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTypeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceItem;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterProcedureStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLLoopStatement;
import com.alibaba.druid.sql.ast.expr.SQLCaseStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRaiseStatement;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhileStatement;
import com.alibaba.druid.sql.ast.statement.SQLReturnStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.SQLArgument;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePipeRowStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSynonymStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropMaterializedViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTypeStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropUserStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRunStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLShowErrorsStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleContinueStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLScriptCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class OracleStatementParser extends SQLStatementParser
{
    public OracleStatementParser(final String sql) {
        super(new OracleExprParser(sql));
    }
    
    public OracleStatementParser(final String sql, final SQLParserFeature... features) {
        super(new OracleExprParser(sql, features));
    }
    
    public OracleStatementParser(final Lexer lexer) {
        super(new OracleExprParser(lexer));
    }
    
    @Override
    public OracleExprParser getExprParser() {
        return (OracleExprParser)this.exprParser;
    }
    
    @Override
    public OracleCreateTableParser getSQLCreateTableParser() {
        return new OracleCreateTableParser(this.lexer);
    }
    
    @Override
    protected void parseInsert0_hinits(final SQLInsertInto insertStatement) {
        if (insertStatement instanceof OracleInsertStatement) {
            final OracleInsertStatement stmt = (OracleInsertStatement)insertStatement;
            this.getExprParser().parseHints(stmt.getHints());
        }
        else {
            final List<SQLHint> hints = new ArrayList<SQLHint>(1);
            this.getExprParser().parseHints(hints);
        }
    }
    
    @Override
    public void parseStatementList(final List<SQLStatement> statementList, final int max, final SQLObject parent) {
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
                if (statementList.size() <= 0) {
                    continue;
                }
                final SQLStatement lastStmt = statementList.get(statementList.size() - 1);
                lastStmt.setAfterSemi(true);
            }
            else if (this.lexer.token() == Token.SELECT) {
                final SQLStatement stmt = new SQLSelectStatement(new OracleSelectParser(this.exprParser).select(), DbType.oracle);
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.UPDATE) {
                final SQLStatement stmt = this.parseUpdateStatement();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.CREATE) {
                final SQLStatement stmt = this.parseCreate();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.INSERT) {
                final SQLStatement stmt = this.parseInsert();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.DELETE) {
                final SQLStatement stmt = this.parseDeleteStatement();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.SLASH) {
                this.lexer.nextToken();
                final SQLStatement stmt = new SQLScriptCommitStatement();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.ALTER) {
                final SQLStatement stmt = this.parserAlter();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.WITH) {
                final SQLSelectStatement stmt2 = new SQLSelectStatement(this.createSQLSelectParser().select(), this.dbType);
                stmt2.setParent(parent);
                statementList.add(stmt2);
            }
            else if (this.lexer.token() == Token.LBRACE || this.lexer.identifierEquals("CALL")) {
                final SQLStatement stmt = this.parseCall();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.MERGE) {
                final SQLStatement stmt = this.parseMerge();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.BEGIN || this.lexer.token() == Token.DECLARE) {
                final SQLStatement stmt = this.parseBlock();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.LOCK) {
                final SQLStatement stmt = this.parseLock();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.TRUNCATE) {
                final SQLStatement stmt = this.parseTruncate();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.VARIANT) {
                final SQLExpr variant = this.exprParser.primary();
                if (variant instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)variant;
                    if (binaryOpExpr.getOperator() == SQLBinaryOperator.Assignment) {
                        final SQLSetStatement stmt3 = new SQLSetStatement(binaryOpExpr.getLeft(), binaryOpExpr.getRight(), this.getDbType());
                        stmt3.setParent(parent);
                        statementList.add(stmt3);
                        continue;
                    }
                }
                this.accept(Token.COLONEQ);
                final SQLExpr value = this.exprParser.expr();
                final SQLSetStatement stmt3 = new SQLSetStatement(variant, value, this.getDbType());
                stmt3.setParent(parent);
                statementList.add(stmt3);
            }
            else if (this.lexer.token() == Token.EXCEPTION) {
                final OracleExceptionStatement stmt4 = this.parseException();
                stmt4.setParent(parent);
                if (parent instanceof SQLBlockStatement) {
                    ((SQLBlockStatement)parent).setException(stmt4);
                }
                else {
                    statementList.add(stmt4);
                }
            }
            else if (this.lexer.identifierEquals("EXIT")) {
                this.lexer.nextToken();
                final OracleExitStatement stmt5 = this.parseExit();
                stmt5.setParent(parent);
                statementList.add(stmt5);
            }
            else if (this.lexer.token() == Token.CONTINUE) {
                this.lexer.nextToken();
                final OracleContinueStatement stmt6 = new OracleContinueStatement();
                if (this.lexer.token() == Token.IDENTIFIER) {
                    final String label = this.lexer.stringVal();
                    this.lexer.nextToken();
                    stmt6.setLabel(label);
                }
                if (this.lexer.token() == Token.WHEN) {
                    this.lexer.nextToken();
                    stmt6.setWhen(this.exprParser.expr());
                }
                stmt6.setParent(parent);
                statementList.add(stmt6);
            }
            else if (this.lexer.token() == Token.FETCH || this.lexer.identifierEquals("FETCH")) {
                final SQLStatement stmt = this.parseFetch();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.identifierEquals("ROLLBACK")) {
                final SQLStatement stmt = this.parseRollback();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.EXPLAIN) {
                final OracleExplainStatement stmt7 = this.parseExplain();
                stmt7.setParent(parent);
                statementList.add(stmt7);
            }
            else if (this.lexer.token() == Token.IDENTIFIER) {
                final String strVal = this.lexer.stringVal();
                if (strVal.equalsIgnoreCase("RAISE")) {
                    final SQLStatement stmt8 = this.parseRaise();
                    stmt8.setParent(parent);
                    statementList.add(stmt8);
                }
                else if (strVal.equalsIgnoreCase("FORALL")) {
                    final SQLStatement stmt8 = this.parseFor();
                    stmt8.setParent(parent);
                    statementList.add(stmt8);
                }
                else if (strVal.equalsIgnoreCase("RENAME")) {
                    final SQLStatement stmt8 = this.parseRename();
                    stmt8.setParent(parent);
                    statementList.add(stmt8);
                }
                else if (strVal.equalsIgnoreCase("EXECUTE")) {
                    final SQLStatement stmt8 = this.parseExecute();
                    stmt8.setParent(parent);
                    statementList.add(stmt8);
                }
                else if (strVal.equalsIgnoreCase("PIPE")) {
                    final Lexer.SavePoint savePoint = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.ROW) {
                        this.lexer.reset(savePoint);
                        final SQLStatement stmt9 = this.parsePipeRow();
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                    else {
                        this.lexer.reset(savePoint);
                    }
                }
                else if (strVal.equalsIgnoreCase("SHOW")) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("ERR")) {
                        this.lexer.nextToken();
                    }
                    else {
                        this.accept(Token.ERRORS);
                    }
                    final SQLShowErrorsStatement stmt10 = new SQLShowErrorsStatement();
                    stmt10.setDbType(this.dbType);
                    stmt10.setParent(parent);
                    statementList.add(stmt10);
                }
                else {
                    final SQLExpr expr = this.exprParser.expr();
                    if (expr instanceof SQLBinaryOpExpr) {
                        final SQLBinaryOpExpr binaryOpExpr2 = (SQLBinaryOpExpr)expr;
                        if (binaryOpExpr2.getOperator() == SQLBinaryOperator.Assignment) {
                            final SQLSetStatement stmt11 = new SQLSetStatement();
                            stmt11.setDbType(DbType.oracle);
                            stmt11.setParent(parent);
                            final SQLAssignItem assignItem = new SQLAssignItem(binaryOpExpr2.getLeft(), binaryOpExpr2.getRight());
                            assignItem.setParent(stmt11);
                            stmt11.getItems().add(assignItem);
                            statementList.add(stmt11);
                            continue;
                        }
                    }
                    final SQLExprStatement stmt12 = new SQLExprStatement(expr);
                    stmt12.setDbType(this.dbType);
                    stmt12.setParent(parent);
                    statementList.add(stmt12);
                }
            }
            else if (this.lexer.token() == Token.LPAREN) {
                Lexer.SavePoint savePoint2 = this.lexer.mark();
                this.lexer.nextToken();
                int parenCount = 0;
                while (this.lexer.token() == Token.LPAREN) {
                    savePoint2 = this.lexer.mark();
                    this.lexer.nextToken();
                    ++parenCount;
                }
                if (this.lexer.token() != Token.SELECT) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.reset(savePoint2);
                final SQLStatement stmt9 = this.parseSelect();
                stmt9.setParent(parent);
                statementList.add(stmt9);
                for (int i = 0; i < parenCount; ++i) {
                    this.accept(Token.RPAREN);
                }
            }
            else if (this.lexer.token() == Token.SET) {
                final SQLStatement stmt = this.parseSet();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.GRANT) {
                statementList.add(this.parseGrant());
            }
            else if (this.lexer.token() == Token.REVOKE) {
                statementList.add(this.parseRevoke());
            }
            else if (this.lexer.token() == Token.COMMENT) {
                statementList.add(this.parseComment());
            }
            else if (this.lexer.token() == Token.FOR) {
                final OracleForStatement forStatement = this.parseFor();
                forStatement.setParent(parent);
                if (this.lexer.token() == Token.IDENTIFIER) {
                    final String strVal2 = this.lexer.stringVal();
                    final int stmtListSize = statementList.size();
                    if (stmtListSize > 0) {
                        final SQLStatement lastStmt2 = statementList.get(stmtListSize - 1);
                        if (lastStmt2 instanceof OracleLabelStatement && ((OracleLabelStatement)lastStmt2).getLabel().getSimpleName().equalsIgnoreCase(strVal2)) {
                            final SQLName endLabbel = this.exprParser.name();
                            forStatement.setEndLabel(endLabbel);
                        }
                    }
                }
                statementList.add(forStatement);
            }
            else if (this.lexer.token() == Token.LOOP) {
                final SQLStatement stmt = this.parseLoop();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.IF) {
                final SQLStatement stmt = this.parseIf();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.GOTO) {
                this.lexer.nextToken();
                final SQLName label2 = this.exprParser.name();
                final OracleGotoStatement stmt13 = new OracleGotoStatement(label2);
                stmt13.setParent(parent);
                statementList.add(stmt13);
            }
            else if (this.lexer.token() == Token.COMMIT) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("WORK")) {
                    this.lexer.nextToken();
                }
                final SQLCommitStatement stmt14 = new SQLCommitStatement();
                stmt14.setParent(parent);
                if (this.lexer.identifierEquals("WRITE")) {
                    stmt14.setWrite(true);
                    this.lexer.nextToken();
                    while (true) {
                        if (this.lexer.token() == Token.WAIT) {
                            this.lexer.nextToken();
                            stmt14.setWait(Boolean.TRUE);
                        }
                        else if (this.lexer.token() == Token.NOWAIT) {
                            this.lexer.nextToken();
                            stmt14.setWait(Boolean.FALSE);
                        }
                        else if (this.lexer.token() == Token.IMMEDIATE) {
                            this.lexer.nextToken();
                            stmt14.setImmediate(Boolean.TRUE);
                        }
                        else {
                            if (!this.lexer.identifierEquals("BATCH")) {
                                break;
                            }
                            this.lexer.nextToken();
                            stmt14.setImmediate(Boolean.FALSE);
                        }
                    }
                }
                statementList.add(stmt14);
            }
            else if (this.lexer.token() == Token.SAVEPOINT) {
                this.lexer.nextToken();
                final SQLSavePointStatement stmt15 = new SQLSavePointStatement();
                stmt15.setDbType(this.dbType);
                stmt15.setParent(parent);
                if (this.lexer.token() == Token.TO) {
                    this.lexer.nextToken();
                    stmt15.setName(this.exprParser.name());
                }
                else if (this.lexer.token() != Token.SEMI) {
                    stmt15.setName(this.exprParser.name());
                }
                this.accept(Token.SEMI);
                stmt15.setAfterSemi(true);
                statementList.add(stmt15);
            }
            else if (this.lexer.token() == Token.LTLT) {
                this.lexer.nextToken();
                final SQLName label2 = this.exprParser.name();
                final OracleLabelStatement stmt16 = new OracleLabelStatement(label2);
                this.accept(Token.GTGT);
                stmt16.setParent(parent);
                statementList.add(stmt16);
            }
            else if (this.lexer.token() == Token.DROP) {
                final Lexer.SavePoint savePoint2 = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TABLE) {
                    final SQLDropTableStatement stmt17 = this.parseDropTable(false);
                    stmt17.setParent(parent);
                    statementList.add(stmt17);
                }
                else {
                    boolean isPublic = false;
                    if (this.lexer.identifierEquals("PUBLIC")) {
                        this.lexer.nextToken();
                        isPublic = true;
                    }
                    if (this.lexer.token() == Token.DATABASE) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("LINK")) {
                            this.lexer.nextToken();
                            final OracleDropDbLinkStatement stmt18 = new OracleDropDbLinkStatement();
                            if (isPublic) {
                                stmt18.setPublic(isPublic);
                            }
                            stmt18.setName(this.exprParser.name());
                            statementList.add(stmt18);
                            continue;
                        }
                    }
                    if (this.lexer.token() == Token.INDEX) {
                        final SQLStatement stmt9 = this.parseDropIndex();
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                    else if (this.lexer.token() == Token.VIEW) {
                        final SQLStatement stmt9 = this.parseDropView(false);
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                    else if (this.lexer.token() == Token.SEQUENCE) {
                        final SQLDropSequenceStatement stmt19 = this.parseDropSequence(false);
                        stmt19.setParent(parent);
                        statementList.add(stmt19);
                    }
                    else if (this.lexer.token() == Token.TRIGGER) {
                        final SQLDropTriggerStatement stmt20 = this.parseDropTrigger(false);
                        stmt20.setParent(parent);
                        statementList.add(stmt20);
                    }
                    else if (this.lexer.token() == Token.USER) {
                        final SQLDropUserStatement stmt21 = this.parseDropUser();
                        stmt21.setParent(parent);
                        statementList.add(stmt21);
                    }
                    else if (this.lexer.token() == Token.PROCEDURE) {
                        final SQLDropProcedureStatement stmt22 = this.parseDropProcedure(false);
                        stmt22.setParent(parent);
                        statementList.add(stmt22);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.SYNONYM)) {
                        this.lexer.reset(savePoint2);
                        final SQLStatement stmt9 = this.parseDropSynonym();
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                        this.lexer.reset(savePoint2);
                        final SQLStatement stmt9 = this.parseDropType();
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.MATERIALIZED)) {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                        this.lexer.reset(savePoint2);
                        final SQLStatement stmt9 = this.parseDropMaterializedView();
                        stmt9.setParent(parent);
                        statementList.add(stmt9);
                    }
                }
            }
            else if (this.lexer.token() == Token.NULL) {
                this.lexer.nextToken();
                final SQLExprStatement stmt23 = new SQLExprStatement(new SQLNullExpr());
                stmt23.setParent(parent);
                statementList.add(stmt23);
            }
            else if (this.lexer.token() == Token.OPEN) {
                final SQLStatement stmt = this.parseOpen();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.CLOSE) {
                final SQLStatement stmt = this.parseClose();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else if (this.lexer.token() == Token.CASE) {
                final SQLStatement stmt = this.parseCase();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
            else {
                if (this.lexer.token() != Token.PROCEDURE) {
                    if (this.lexer.token() != Token.ELSIF || !(parent instanceof SQLIfStatement)) {
                        if (this.lexer.token() != Token.WHEN || !(parent instanceof OracleExceptionStatement.Item)) {
                            if (this.lexer.token() == Token.FUNCTION) {
                                final SQLStatement stmt = this.parseFunction();
                                stmt.setParent(parent);
                                statementList.add(stmt);
                                continue;
                            }
                            if (this.lexer.token() == Token.WHILE) {
                                final SQLStatement stmt = this.parseWhile();
                                stmt.setParent(parent);
                                statementList.add(stmt);
                                continue;
                            }
                            if (this.lexer.token() == Token.RETURN) {
                                final SQLStatement stmt = this.parseReturn();
                                stmt.setParent(parent);
                                statementList.add(stmt);
                                continue;
                            }
                            if (this.lexer.token() == Token.TRIGGER) {
                                final SQLStatement stmt = this.parseCreateTrigger();
                                stmt.setParent(parent);
                                statementList.add(stmt);
                                continue;
                            }
                            if (this.lexer.token() == Token.MONKEYS_AT_AT) {
                                this.lexer.nextToken();
                                final SQLExpr expr2 = this.exprParser.primary();
                                final OracleRunStatement stmt24 = new OracleRunStatement(expr2);
                                stmt24.setParent(parent);
                                statementList.add(stmt24);
                                continue;
                            }
                            if (this.lexer.token() == Token.QUES) {
                                final SQLExpr expr2 = this.exprParser.expr();
                                final SQLExprStatement stmt25 = new SQLExprStatement(expr2);
                                stmt25.setParent(parent);
                                statementList.add(stmt25);
                                continue;
                            }
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                    }
                    return;
                }
                final SQLStatement stmt = this.parseCreateProcedure();
                stmt.setParent(parent);
                statementList.add(stmt);
            }
        }
    }
    
    public SQLStatement parseDropType() {
        if (this.lexer.token() == Token.DROP) {
            this.lexer.nextToken();
        }
        final SQLDropTypeStatement stmt = new SQLDropTypeStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("TYPE");
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    @Override
    public SQLStatement parseDropMaterializedView() {
        if (this.lexer.token() == Token.DROP) {
            this.lexer.nextToken();
        }
        final SQLDropMaterializedViewStatement stmt = new SQLDropMaterializedViewStatement();
        stmt.setDbType(this.dbType);
        this.acceptIdentifier("MATERIALIZED");
        this.accept(Token.VIEW);
        stmt.setName(this.exprParser.name());
        return stmt;
    }
    
    public SQLStatement parseDropSynonym() {
        if (this.lexer.token() == Token.DROP) {
            this.lexer.nextToken();
        }
        final SQLDropSynonymStatement stmt = new SQLDropSynonymStatement();
        stmt.setDbType(this.dbType);
        if (this.lexer.identifierEquals(FnvHash.Constants.PUBLIC)) {
            this.lexer.nextToken();
            stmt.setPublic(true);
        }
        this.acceptIdentifier("SYNONYM");
        stmt.setName(this.exprParser.name());
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            stmt.setForce(true);
        }
        return stmt;
    }
    
    public SQLStatement parsePipeRow() {
        final OraclePipeRowStatement stmt = new OraclePipeRowStatement();
        this.acceptIdentifier("PIPE");
        this.accept(Token.ROW);
        this.accept(Token.LPAREN);
        this.exprParser.exprList(stmt.getParameters(), stmt);
        this.accept(Token.RPAREN);
        return stmt;
    }
    
    public SQLStatement parseExecute() {
        this.acceptIdentifier("EXECUTE");
        if (this.lexer.token() == Token.IMMEDIATE) {
            this.lexer.nextToken();
            final OracleExecuteImmediateStatement stmt = new OracleExecuteImmediateStatement();
            final SQLExpr dyanmiacSql = this.exprParser.primary();
            stmt.setDynamicSql(dyanmiacSql);
            if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                this.exprParser.exprList(stmt.getInto(), stmt);
            }
            if (this.lexer.token() == Token.USING) {
                this.lexer.nextToken();
                while (true) {
                    final SQLArgument arg = new SQLArgument();
                    if (this.lexer.token() == Token.IN) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.OUT) {
                            this.lexer.nextToken();
                            arg.setType(SQLParameter.ParameterType.INOUT);
                        }
                        else {
                            arg.setType(SQLParameter.ParameterType.IN);
                        }
                    }
                    else if (this.lexer.token() == Token.OUT) {
                        this.lexer.nextToken();
                        arg.setType(SQLParameter.ParameterType.OUT);
                    }
                    arg.setExpr(this.exprParser.primary());
                    arg.setParent(stmt);
                    stmt.getArguments().add(arg);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token() == Token.RETURNING) {
                this.lexer.nextToken();
                this.accept(Token.INTO);
                this.exprParser.exprList(stmt.getReturnInto(), stmt);
            }
            return stmt;
        }
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    @Override
    public SQLStatement parseRename() {
        this.lexer.nextToken();
        final SQLName from = this.exprParser.name();
        this.accept(Token.TO);
        final SQLName to = this.exprParser.name();
        final SQLAlterTableStatement stmt = new SQLAlterTableStatement(this.dbType);
        stmt.setTableSource(from);
        final SQLAlterTableRename toItem = new SQLAlterTableRename(to);
        stmt.addItem(toItem);
        return stmt;
    }
    
    private OracleExitStatement parseExit() {
        final OracleExitStatement stmt = new OracleExitStatement();
        if (this.lexer.token() == Token.IDENTIFIER) {
            final String label = this.lexer.stringVal();
            stmt.setLabel(label);
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.WHEN) {
            this.lexer.nextToken();
            stmt.setWhen(this.exprParser.expr());
        }
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public SQLStatement parseReturn() {
        this.accept(Token.RETURN);
        final SQLReturnStatement stmt = new SQLReturnStatement();
        if (this.lexer.token() != Token.SEMI) {
            final SQLExpr expr = this.exprParser.expr();
            stmt.setExpr(expr);
        }
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public SQLStatement parseWhile() {
        this.accept(Token.WHILE);
        final SQLWhileStatement stmt = new SQLWhileStatement();
        stmt.setDbType(this.dbType);
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.LOOP);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.END);
        this.accept(Token.LOOP);
        this.accept(Token.SEMI);
        return stmt;
    }
    
    @Override
    public SQLCreateFunctionStatement parseCreateFunction() {
        final SQLCreateFunctionStatement stmt = (SQLCreateFunctionStatement)this.parseFunction();
        stmt.setCreate(true);
        return stmt;
    }
    
    public SQLStatement parseFunction() {
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
        else {
            if (this.lexer.token() == Token.DECLARE) {
                this.lexer.nextToken();
            }
            stmt.setCreate(false);
        }
        this.accept(Token.FUNCTION);
        final SQLName functionName = this.exprParser.name();
        stmt.setName(functionName);
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WRAPPED)) {
            this.lexer.nextToken();
            final int pos = this.lexer.text.indexOf(59, this.lexer.pos());
            if (pos != -1) {
                final String wrappedString = this.lexer.subString(this.lexer.pos(), pos - this.lexer.pos());
                stmt.setWrappedSource(wrappedString);
                this.lexer.reset(pos, ';', Token.LITERAL_CHARS);
                this.lexer.nextToken();
                stmt.setAfterSemi(true);
                return stmt;
            }
            final String wrappedString = this.lexer.text.substring(this.lexer.pos());
            stmt.setWrappedSource(wrappedString);
            this.lexer.reset(this.lexer.text.length(), '\u001a', Token.EOF);
            return stmt;
        }
        else {
            this.accept(Token.RETURN);
            final SQLDataType returnDataType = this.exprParser.parseDataType(false);
            stmt.setReturnDataType(returnDataType);
            if (this.identifierEquals("PIPELINED")) {
                this.lexer.nextToken();
                stmt.setPipelined(true);
            }
            if (this.identifierEquals("DETERMINISTIC")) {
                this.lexer.nextToken();
                stmt.setDeterministic(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.AUTHID)) {
                this.lexer.nextToken();
                final String strVal = this.lexer.stringVal();
                if (this.lexer.identifierEquals(FnvHash.Constants.CURRENT_USER)) {
                    this.lexer.nextToken();
                }
                else {
                    this.acceptIdentifier("DEFINER");
                }
                final SQLName authid = new SQLIdentifierExpr(strVal);
                stmt.setAuthid(authid);
            }
            if (this.identifierEquals("RESULT_CACHE")) {
                this.lexer.nextToken();
                stmt.setResultCache(true);
            }
            if (this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
                return stmt;
            }
            if (this.lexer.token() == Token.IS || this.lexer.token() == Token.AS) {
                this.lexer.nextToken();
            }
            if (!this.lexer.identifierEquals("LANGUAGE")) {
                if (this.lexer.identifierEquals("PARALLEL_ENABLE")) {
                    this.lexer.nextToken();
                    stmt.setParallelEnable(true);
                }
                if (this.lexer.identifierEquals("AGGREGATE")) {
                    this.lexer.nextToken();
                    stmt.setAggregate(true);
                }
                if (this.lexer.token() == Token.USING) {
                    this.lexer.nextToken();
                    final SQLName using = this.exprParser.name();
                    stmt.setUsing(using);
                }
                SQLStatement block;
                if (this.lexer.token() == Token.SEMI) {
                    stmt.setAfterSemi(true);
                    this.lexer.nextToken();
                    block = null;
                }
                else {
                    block = this.parseBlock();
                }
                stmt.setBlock(block);
                if (this.lexer.identifierEquals(functionName.getSimpleName())) {
                    this.lexer.nextToken();
                }
                if (this.lexer.identifierEquals(functionName.getSimpleName())) {
                    this.lexer.nextToken();
                }
                return stmt;
            }
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("JAVA")) {
                this.lexer.nextToken();
                this.acceptIdentifier("NAME");
                final String javaCallSpec = this.lexer.stringVal();
                this.accept(Token.LITERAL_CHARS);
                stmt.setJavaCallSpec(javaCallSpec);
                return stmt;
            }
            throw new ParserException("TODO : " + this.lexer.info());
        }
    }
    
    public SQLStatement parseRaise() {
        this.lexer.nextToken();
        final OracleRaiseStatement stmt = new OracleRaiseStatement();
        if (this.lexer.token() != Token.SEMI) {
            stmt.setException(this.exprParser.expr());
        }
        this.accept(Token.SEMI);
        return stmt;
    }
    
    @Override
    public SQLStatement parseCase() {
        final SQLCaseStatement caseStmt = new SQLCaseStatement();
        caseStmt.setDbType(this.dbType);
        this.lexer.nextToken();
        if (this.lexer.token() != Token.WHEN) {
            caseStmt.setValueExpr(this.exprParser.expr());
        }
        this.accept(Token.WHEN);
        SQLExpr testExpr = this.exprParser.expr();
        this.accept(Token.THEN);
        SQLStatement stmt = this.parseStatement();
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
        }
        SQLCaseStatement.Item caseItem = new SQLCaseStatement.Item(testExpr, stmt);
        caseStmt.addItem(caseItem);
        while (this.lexer.token() == Token.WHEN) {
            this.lexer.nextToken();
            testExpr = this.exprParser.expr();
            this.accept(Token.THEN);
            stmt = this.parseStatement();
            if (this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            }
            caseItem = new SQLCaseStatement.Item(testExpr, stmt);
            caseStmt.addItem(caseItem);
        }
        if (this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            this.parseStatementList(caseStmt.getElseStatements(), -1, caseStmt);
        }
        this.accept(Token.END);
        this.accept(Token.CASE);
        this.accept(Token.SEMI);
        return caseStmt;
    }
    
    @Override
    public SQLStatement parseIf() {
        this.accept(Token.IF);
        final SQLIfStatement stmt = new SQLIfStatement();
        stmt.setDbType(this.dbType);
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.THEN);
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        while (this.lexer.token() == Token.ELSIF) {
            this.lexer.nextToken();
            final SQLIfStatement.ElseIf elseIf = new SQLIfStatement.ElseIf();
            elseIf.setCondition(this.exprParser.expr());
            elseIf.setParent(stmt);
            this.accept(Token.THEN);
            this.parseStatementList(elseIf.getStatements(), -1, stmt);
            stmt.getElseIfList().add(elseIf);
        }
        if (this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            final SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
            this.parseStatementList(elseItem.getStatements(), -1, elseItem);
            stmt.setElseItem(elseItem);
        }
        this.accept(Token.END);
        this.accept(Token.IF);
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public OracleForStatement parseFor() {
        final OracleForStatement stmt = new OracleForStatement();
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
        }
        else {
            this.acceptIdentifier("FORALL");
            stmt.setAll(true);
        }
        stmt.setIndex(this.exprParser.name());
        this.accept(Token.IN);
        stmt.setRange(this.exprParser.expr());
        if (stmt.isAll()) {
            final SQLStatement itemStmt = this.parseStatement();
            itemStmt.setParent(stmt);
            stmt.getStatements().add(itemStmt);
        }
        else {
            this.accept(Token.LOOP);
            this.parseStatementList(stmt.getStatements(), -1, stmt);
            this.accept(Token.END);
            this.accept(Token.LOOP);
            if (this.lexer.token() != Token.SEMI) {
                final SQLName endLabel = this.exprParser.name();
                stmt.setEndLabel(endLabel);
            }
            this.accept(Token.SEMI);
            stmt.setAfterSemi(true);
        }
        return stmt;
    }
    
    public SQLLoopStatement parseLoop() {
        this.accept(Token.LOOP);
        final SQLLoopStatement stmt = new SQLLoopStatement();
        this.parseStatementList(stmt.getStatements(), -1, stmt);
        this.accept(Token.END);
        this.accept(Token.LOOP);
        if (this.lexer.token() == Token.IDENTIFIER) {
            final String label = this.lexer.stringVal();
            stmt.setLabelName(label);
            this.lexer.nextToken();
        }
        this.accept(Token.SEMI);
        stmt.setAfterSemi(true);
        return stmt;
    }
    
    @Override
    public SQLStatement parseSet() {
        this.accept(Token.SET);
        if (this.lexer.identifierEquals("TRANSACTION")) {
            this.lexer.nextToken();
            final OracleSetTransactionStatement stmt = new OracleSetTransactionStatement();
            if (this.lexer.identifierEquals("READ")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("ONLY")) {
                    this.lexer.nextToken();
                    stmt.setReadOnly(true);
                }
                else {
                    this.acceptIdentifier("WRITE");
                    stmt.setWrite(true);
                }
            }
            if (this.lexer.identifierEquals("NAME")) {
                this.lexer.nextToken();
                stmt.setName(this.exprParser.expr());
            }
            return stmt;
        }
        final SQLSetStatement stmt2 = new SQLSetStatement(this.dbType);
        this.parseAssignItems(stmt2.getItems(), stmt2);
        stmt2.putAttribute("parser.set", Boolean.TRUE);
        return stmt2;
    }
    
    public SQLStatement parserAlter() {
        final Lexer.SavePoint savePoint = this.lexer.mark();
        this.accept(Token.ALTER);
        if (this.lexer.token() == Token.SESSION) {
            this.lexer.nextToken();
            final OracleAlterSessionStatement stmt = new OracleAlterSessionStatement();
            if (this.lexer.token() == Token.SET) {
                this.lexer.nextToken();
                this.parseAssignItems(stmt.getItems(), stmt);
                return stmt;
            }
            throw new ParserException("TODO : " + this.lexer.info());
        }
        else {
            if (this.lexer.token() == Token.PROCEDURE) {
                this.lexer.nextToken();
                final SQLAlterProcedureStatement stmt2 = new SQLAlterProcedureStatement();
                stmt2.setName(this.exprParser.name());
                if (this.lexer.identifierEquals("COMPILE")) {
                    this.lexer.nextToken();
                    stmt2.setCompile(true);
                }
                if (this.lexer.identifierEquals("REUSE")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("SETTINGS");
                    stmt2.setReuseSettings(true);
                }
                return stmt2;
            }
            if (this.lexer.token() == Token.TABLE) {
                return this.parseAlterTable();
            }
            if (this.lexer.token() == Token.INDEX) {
                this.lexer.reset(savePoint);
                return this.parseAlterIndex();
            }
            if (this.lexer.token() == Token.TRIGGER) {
                this.lexer.nextToken();
                final OracleAlterTriggerStatement stmt3 = new OracleAlterTriggerStatement();
                stmt3.setName(this.exprParser.name());
                while (true) {
                    if (this.lexer.token() == Token.ENABLE) {
                        this.lexer.nextToken();
                        stmt3.setEnable(Boolean.TRUE);
                    }
                    else if (this.lexer.token() == Token.DISABLE) {
                        this.lexer.nextToken();
                        stmt3.setEnable(Boolean.FALSE);
                    }
                    else {
                        if (!this.lexer.identifierEquals("COMPILE")) {
                            break;
                        }
                        this.lexer.nextToken();
                        stmt3.setCompile(true);
                    }
                }
                return stmt3;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SYNONYM)) {
                this.lexer.nextToken();
                final OracleAlterSynonymStatement stmt4 = new OracleAlterSynonymStatement();
                stmt4.setName(this.exprParser.name());
                while (true) {
                    if (this.lexer.token() == Token.ENABLE) {
                        this.lexer.nextToken();
                        stmt4.setEnable(Boolean.TRUE);
                    }
                    else if (this.lexer.token() == Token.DISABLE) {
                        this.lexer.nextToken();
                        stmt4.setEnable(Boolean.FALSE);
                    }
                    else {
                        if (!this.lexer.identifierEquals("COMPILE")) {
                            break;
                        }
                        this.lexer.nextToken();
                        stmt4.setCompile(true);
                    }
                }
                return stmt4;
            }
            if (this.lexer.token() == Token.VIEW) {
                this.lexer.nextToken();
                final OracleAlterViewStatement stmt5 = new OracleAlterViewStatement();
                stmt5.setName(this.exprParser.name());
                while (true) {
                    if (this.lexer.token() == Token.ENABLE) {
                        this.lexer.nextToken();
                        stmt5.setEnable(Boolean.TRUE);
                    }
                    else if (this.lexer.token() == Token.DISABLE) {
                        this.lexer.nextToken();
                        stmt5.setEnable(Boolean.FALSE);
                    }
                    else {
                        if (!this.lexer.identifierEquals("COMPILE")) {
                            break;
                        }
                        this.lexer.nextToken();
                        stmt5.setCompile(true);
                    }
                }
                return stmt5;
            }
            if (this.lexer.token() == Token.TABLESPACE) {
                this.lexer.nextToken();
                final OracleAlterTablespaceStatement stmt6 = new OracleAlterTablespaceStatement();
                stmt6.setName(this.exprParser.name());
                if (!this.lexer.identifierEquals("ADD")) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals("DATAFILE")) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.nextToken();
                final OracleAlterTablespaceAddDataFile item = new OracleAlterTablespaceAddDataFile();
                while (true) {
                    final OracleFileSpecification file = new OracleFileSpecification();
                    while (true) {
                        final SQLExpr fileName = this.exprParser.expr();
                        file.getFileNames().add(fileName);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    if (this.lexer.identifierEquals("SIZE")) {
                        this.lexer.nextToken();
                        file.setSize(this.exprParser.expr());
                    }
                    if (this.lexer.identifierEquals("AUTOEXTEND")) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("OFF")) {
                            this.lexer.nextToken();
                            file.setAutoExtendOff(true);
                        }
                        else {
                            if (!this.lexer.identifierEquals("ON")) {
                                throw new ParserException("TODO : " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            file.setAutoExtendOn(this.exprParser.expr());
                        }
                    }
                    item.getFiles().add(file);
                    if (this.lexer.token() != Token.COMMA) {
                        stmt6.setItem(item);
                        return stmt6;
                    }
                    this.lexer.nextToken();
                }
            }
            else {
                if (this.lexer.token() == Token.FUNCTION) {
                    this.lexer.reset(savePoint);
                    return this.parseAlterFunction();
                }
                if (this.lexer.token() == Token.SEQUENCE) {
                    this.lexer.reset(savePoint);
                    return this.parseAlterSequence();
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.reset(savePoint);
                    return this.parseAlterType();
                }
                throw new ParserException("TODO : " + this.lexer.info());
            }
        }
    }
    
    protected SQLStatement parseAlterType() {
        this.accept(Token.ALTER);
        this.acceptIdentifier("TYPE");
        final SQLAlterTypeStatement stmt = new SQLAlterTypeStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.identifierEquals("COMPILE")) {
            stmt.setCompile(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("DEBUG")) {
            stmt.setDebug(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("BODY")) {
            stmt.setBody(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("REUSE")) {
            stmt.setReuseSettings(true);
            this.lexer.nextToken();
            this.acceptIdentifier("SETTINGS");
        }
        return stmt;
    }
    
    @Override
    protected SQLStatement parseAlterFunction() {
        this.accept(Token.ALTER);
        this.accept(Token.FUNCTION);
        final SQLAlterFunctionStatement stmt = new SQLAlterFunctionStatement();
        stmt.setDbType(this.dbType);
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        this.acceptIdentifier("COMPILE");
        if (this.lexer.identifierEquals("DEBUG")) {
            stmt.setDebug(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("REUSE")) {
            stmt.setReuseSettings(true);
            this.lexer.nextToken();
            this.acceptIdentifier("SETTINGS");
        }
        return stmt;
    }
    
    private SQLStatement parseAlterTable() {
        this.lexer.nextToken();
        final SQLAlterTableStatement stmt = new SQLAlterTableStatement(this.getDbType());
        stmt.setName(this.exprParser.name());
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.ADD)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLAlterTableAddColumn item = this.parseAlterTableAddColumn();
                    stmt.addItem(item);
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.token() == Token.CONSTRAINT || this.lexer.token() == Token.FOREIGN || this.lexer.token() == Token.PRIMARY || this.lexer.token() == Token.UNIQUE || this.lexer.token() == Token.CHECK) {
                    final OracleConstraint constraint = ((OracleExprParser)this.exprParser).parseConstaint();
                    final SQLAlterTableAddConstraint item2 = new SQLAlterTableAddConstraint();
                    constraint.setParent(item2);
                    item2.setParent(stmt);
                    item2.setConstraint(constraint);
                    stmt.addItem(item2);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SUPPLEMENTAL)) {
                    final SQLTableElement element = this.getSQLCreateTableParser().parseCreateTableSupplementalLogingProps();
                    final SQLAlterTableAddSupplemental item3 = new SQLAlterTableAddSupplemental();
                    item3.setElement(element);
                    stmt.addItem(item3);
                }
                else if (this.lexer.token() == Token.IDENTIFIER) {
                    final SQLAlterTableAddColumn item = this.parseAlterTableAddColumn();
                    stmt.addItem(item);
                }
                else {
                    if (this.lexer.token() != Token.LITERAL_ALIAS) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    final SQLAlterTableAddColumn item = this.parseAlterTableAddColumn();
                    stmt.addItem(item);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MOVE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TABLESPACE) {
                    this.lexer.nextToken();
                    final OracleAlterTableMoveTablespace item4 = new OracleAlterTableMoveTablespace();
                    item4.setName(this.exprParser.name());
                    stmt.addItem(item4);
                    break;
                }
                throw new ParserException("TODO : " + this.lexer.info());
            }
            else {
                if (this.lexer.identifierEquals("RENAME")) {
                    stmt.addItem(this.parseAlterTableRename());
                    break;
                }
                if (this.lexer.identifierEquals("MODIFY")) {
                    this.lexer.nextToken();
                    final OracleAlterTableModify item5 = new OracleAlterTableModify();
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        while (true) {
                            final SQLColumnDefinition columnDef = this.exprParser.parseColumn();
                            item5.addColumn(columnDef);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                    }
                    else {
                        final SQLColumnDefinition columnDef = this.exprParser.parseColumn();
                        item5.addColumn(columnDef);
                    }
                    stmt.addItem(item5);
                }
                else if (this.lexer.identifierEquals("SPLIT")) {
                    this.parseAlterTableSplit(stmt);
                }
                else if (this.lexer.token() == Token.TRUNCATE) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.PARTITION) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    final OracleAlterTableTruncatePartition item6 = new OracleAlterTableTruncatePartition();
                    item6.setName(this.exprParser.name());
                    stmt.addItem(item6);
                }
                else if (this.lexer.token() == Token.DROP) {
                    this.parseAlterDrop(stmt);
                }
                else if (this.lexer.token() == Token.DISABLE) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableDisableConstraint item7 = new SQLAlterTableDisableConstraint();
                        item7.setConstraintName(this.exprParser.name());
                        stmt.addItem(item7);
                        break;
                    }
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                else {
                    if (this.lexer.token() != Token.ENABLE) {
                        break;
                    }
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        final SQLAlterTableEnableConstraint item8 = new SQLAlterTableEnableConstraint();
                        item8.setConstraintName(this.exprParser.name());
                        stmt.addItem(item8);
                        break;
                    }
                    throw new ParserException("TODO : " + this.lexer.info());
                }
            }
        }
        if (this.lexer.token() == Token.UPDATE) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals("GLOBAL")) {
                throw new ParserException("TODO : " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.acceptIdentifier("INDEXES");
            stmt.setUpdateGlobalIndexes(true);
        }
        return stmt;
    }
    
    @Override
    public void parseAlterDrop(final SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        if (this.lexer.token() == Token.CONSTRAINT) {
            this.lexer.nextToken();
            final SQLAlterTableDropConstraint item = new SQLAlterTableDropConstraint();
            item.setConstraintName(this.exprParser.name());
            stmt.addItem(item);
        }
        else if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLAlterTableDropColumnItem item2 = new SQLAlterTableDropColumnItem();
            this.exprParser.names(item2.getColumns());
            stmt.addItem(item2);
            this.accept(Token.RPAREN);
        }
        else if (this.lexer.token() == Token.COLUMN) {
            this.lexer.nextToken();
            final SQLAlterTableDropColumnItem item2 = new SQLAlterTableDropColumnItem();
            this.exprParser.names(item2.getColumns());
            stmt.addItem(item2);
        }
        else if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            final OracleAlterTableDropPartition item3 = new OracleAlterTableDropPartition();
            item3.setName(this.exprParser.name());
            stmt.addItem(item3);
        }
        else if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
            final SQLName indexName = this.exprParser.name();
            final SQLAlterTableDropIndex item4 = new SQLAlterTableDropIndex();
            item4.setIndexName(indexName);
            stmt.addItem(item4);
        }
        else {
            if (this.lexer.token() != Token.PRIMARY) {
                throw new ParserException("TODO : " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.KEY);
            final SQLAlterTableDropPrimaryKey item5 = new SQLAlterTableDropPrimaryKey();
            stmt.addItem(item5);
        }
    }
    
    private void parseAlterTableSplit(final SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        if (this.lexer.token() != Token.PARTITION) {
            throw new ParserException("TODO : " + this.lexer.info());
        }
        this.lexer.nextToken();
        final OracleAlterTableSplitPartition item = new OracleAlterTableSplitPartition();
        item.setName(this.exprParser.name());
        if (this.lexer.identifierEquals("AT")) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprParser.exprList(item.getAt(), item);
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    final OracleAlterTableSplitPartition.NestedTablePartitionSpec spec = new OracleAlterTableSplitPartition.NestedTablePartitionSpec();
                    this.accept(Token.PARTITION);
                    spec.setPartition(this.exprParser.name());
                    while (this.lexer.token() == Token.TABLESPACE) {
                        this.lexer.nextToken();
                        final SQLName tablespace = this.exprParser.name();
                        spec.getSegmentAttributeItems().add(new OracleAlterTableSplitPartition.TableSpaceItem(tablespace));
                    }
                    if (this.lexer.identifierEquals("PCTREE")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("PCTUSED")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("INITRANS")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("STORAGE")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("LOGGING")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("NOLOGGING")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    if (this.lexer.identifierEquals("FILESYSTEM_LIKE_LOGGING")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    item.getInto().add(spec);
                    if (this.lexer.token() != Token.COMMA) {
                        this.accept(Token.RPAREN);
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token() == Token.UPDATE) {
                this.lexer.nextToken();
                this.acceptIdentifier("INDEXES");
                final OracleAlterTableSplitPartition.UpdateIndexesClause updateIndexes = new OracleAlterTableSplitPartition.UpdateIndexesClause();
                item.setUpdateIndexes(updateIndexes);
            }
            stmt.addItem(item);
            return;
        }
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    public OracleLockTableStatement parseLock() {
        this.accept(Token.LOCK);
        this.accept(Token.TABLE);
        final OracleLockTableStatement stmt = new OracleLockTableStatement();
        stmt.setTable(this.exprParser.name());
        if (Token.PARTITION == this.lexer.token()) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            stmt.setPartition(this.exprParser.expr());
            this.accept(Token.RPAREN);
        }
        this.accept(Token.IN);
        Token token = this.lexer.token();
        if (token == Token.SHARE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.ROW) {
                this.lexer.nextToken();
                this.accept(Token.EXCLUSIVE);
                stmt.setLockMode(OracleLockTableStatement.LockMode.SHARE_ROW_EXCLUSIVE);
            }
            else if (this.lexer.token() == Token.UPDATE) {
                this.lexer.nextToken();
                stmt.setLockMode(OracleLockTableStatement.LockMode.SHARE_UPDATE);
            }
            else {
                stmt.setLockMode(OracleLockTableStatement.LockMode.SHARE);
            }
        }
        else if (token == Token.EXCLUSIVE) {
            stmt.setLockMode(OracleLockTableStatement.LockMode.EXCLUSIVE);
            this.lexer.nextToken();
        }
        else {
            if (token != Token.ROW) {
                throw new ParserException(this.lexer.info());
            }
            this.lexer.nextToken();
            token = this.lexer.token();
            if (token == Token.SHARE) {
                stmt.setLockMode(OracleLockTableStatement.LockMode.ROW_SHARE);
                this.lexer.nextToken();
            }
            else {
                if (token != Token.EXCLUSIVE) {
                    throw new ParserException(this.lexer.info());
                }
                stmt.setLockMode(OracleLockTableStatement.LockMode.ROW_EXCLUSIVE);
                this.lexer.nextToken();
            }
        }
        this.accept(Token.MODE);
        if (this.lexer.token() == Token.NOWAIT) {
            this.lexer.nextToken();
            stmt.setNoWait(true);
        }
        else if (this.lexer.token() == Token.WAIT) {
            this.lexer.nextToken();
            stmt.setWait(this.exprParser.expr());
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseBlock() {
        final SQLBlockStatement block = new SQLBlockStatement();
        block.setDbType(DbType.oracle);
        final Lexer.SavePoint savePoint = this.lexer.mark();
        if (this.lexer.token() == Token.DECLARE) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.CURSOR) {
            this.parserParameters(block.getParameters(), block);
            for (final SQLParameter param : block.getParameters()) {
                param.setParent(block);
            }
        }
        if (this.lexer.token() == Token.PROCEDURE) {
            final SQLCreateProcedureStatement stmt = this.parseCreateProcedure();
            for (final SQLParameter param2 : block.getParameters()) {
                param2.setParent(stmt);
                stmt.getParameters().add(param2);
            }
            return stmt;
        }
        if (this.lexer.token() == Token.FUNCTION) {
            if (savePoint.token == Token.DECLARE) {
                this.lexer.reset(savePoint);
            }
            return this.parseCreateFunction();
        }
        this.accept(Token.BEGIN);
        this.parseStatementList(block.getStatementList(), -1, block);
        this.accept(Token.END);
        final Token token = this.lexer.token();
        if (token == Token.EOF) {
            return block;
        }
        if (token != Token.SEMI) {
            final String endLabel = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            block.setEndLabel(endLabel);
        }
        this.accept(Token.SEMI);
        return block;
    }
    
    private void parserParameters(final List<SQLParameter> parameters, final SQLObject parent) {
        Token token;
        do {
            final SQLParameter parameter = new SQLParameter();
            parameter.setParent(parent);
            if (parent instanceof OracleCreateTypeStatement) {
                if (this.lexer.identifierEquals(FnvHash.Constants.MAP)) {
                    this.lexer.nextToken();
                    parameter.setMap(true);
                }
                else if (this.lexer.token() == Token.ORDER) {
                    this.lexer.nextToken();
                    parameter.setOrder(true);
                }
            }
            SQLDataType dataType = null;
            SQLName name;
            if (this.lexer.token() == Token.CURSOR) {
                this.lexer.nextToken();
                dataType = new SQLDataTypeImpl();
                dataType.setName("CURSOR");
                name = this.exprParser.name();
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.parserParameters(parameter.getCursorParameters(), parameter);
                    this.accept(Token.RPAREN);
                }
                this.accept(Token.IS);
                final SQLSelect select = this.createSQLSelectParser().select();
                parameter.setDefaultValue(new SQLQueryExpr(select));
            }
            else {
                if (this.lexer.token() == Token.PROCEDURE || this.lexer.token() == Token.END) {
                    break;
                }
                if (this.lexer.token() == Token.TABLE) {
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.nextToken();
                    name = this.exprParser.name();
                    this.accept(Token.IS);
                    if (this.lexer.identifierEquals("REF")) {
                        this.lexer.nextToken();
                        this.accept(Token.CURSOR);
                        dataType = new SQLDataTypeImpl("REF CURSOR");
                        dataType.setDbType(this.dbType);
                    }
                    else if (this.lexer.token() == Token.TABLE) {
                        this.lexer.nextToken();
                        this.accept(Token.OF);
                        name = this.exprParser.name();
                        if (this.lexer.token() == Token.PERCENT) {
                            this.lexer.nextToken();
                            String typeName;
                            if (this.lexer.identifierEquals(FnvHash.Constants.ROWTYPE)) {
                                this.lexer.nextToken();
                                typeName = "TABLE OF " + name.toString() + "%ROWTYPE";
                            }
                            else {
                                this.acceptIdentifier("TYPE");
                                typeName = "TABLE OF " + name.toString() + "%TYPE";
                            }
                            dataType = new SQLDataTypeImpl(typeName);
                        }
                        else if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            final String typeName = name.toString();
                            final SQLIntegerExpr lenExpr = (SQLIntegerExpr)this.exprParser.expr();
                            final int len = lenExpr.getNumber().intValue();
                            dataType = new SQLDataTypeImpl(typeName, len);
                            this.accept(Token.RPAREN);
                            if (this.lexer.token() == Token.INDEX) {
                                this.lexer.nextToken();
                                this.accept(Token.BY);
                                final SQLExpr indexBy = this.exprParser.primary();
                                ((SQLDataTypeImpl)dataType).setIndexBy(indexBy);
                            }
                        }
                        dataType.setDbType(this.dbType);
                    }
                    else {
                        if (!this.lexer.identifierEquals("VARRAY")) {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        final int len2 = this.exprParser.acceptInteger();
                        this.accept(Token.RPAREN);
                        this.accept(Token.OF);
                        if (this.lexer.identifierEquals("NUMBER")) {
                            this.lexer.nextToken();
                            String typeName2 = "VARRAY(" + len2 + ") OF NUMBER";
                            if (this.lexer.token() == Token.LPAREN) {
                                this.accept(Token.LPAREN);
                                final int numLen = this.exprParser.acceptInteger();
                                this.accept(Token.RPAREN);
                                typeName2 = typeName2 + "(" + numLen + ")";
                            }
                            dataType = new SQLDataTypeImpl(typeName2);
                            dataType.setDbType(this.dbType);
                        }
                        else {
                            if (!this.lexer.identifierEquals("VARCHAR2")) {
                                throw new ParserException("TODO : " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            final String typeName2 = "VARRAY(" + len2 + ") OF VARCHAR2";
                            dataType = new SQLDataTypeImpl(typeName2);
                            dataType.setDbType(this.dbType);
                            if (this.lexer.token() == Token.LPAREN) {
                                this.lexer.nextToken();
                                this.exprParser.exprList(dataType.getArguments(), dataType);
                                this.accept(Token.RPAREN);
                            }
                        }
                    }
                }
                else {
                    if (this.lexer.token() == Token.KEY) {
                        name = new SQLIdentifierExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                    }
                    else {
                        name = this.exprParser.name();
                    }
                    if (this.lexer.token() == Token.IN) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.OUT) {
                            this.lexer.nextToken();
                            parameter.setParamType(SQLParameter.ParameterType.INOUT);
                        }
                        else {
                            parameter.setParamType(SQLParameter.ParameterType.IN);
                        }
                    }
                    else if (this.lexer.token() == Token.OUT) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.IN) {
                            this.lexer.nextToken();
                            parameter.setParamType(SQLParameter.ParameterType.INOUT);
                        }
                        else {
                            parameter.setParamType(SQLParameter.ParameterType.OUT);
                        }
                    }
                    else if (this.lexer.token() == Token.INOUT) {
                        this.lexer.nextToken();
                        parameter.setParamType(SQLParameter.ParameterType.INOUT);
                    }
                    if (this.lexer.identifierEquals("NOCOPY")) {
                        this.lexer.nextToken();
                        parameter.setNoCopy(true);
                    }
                    if (this.lexer.identifierEquals("CONSTANT")) {
                        this.lexer.nextToken();
                        parameter.setConstant(true);
                    }
                    if ((name.nameHashCode64() == FnvHash.Constants.MEMBER || name.nameHashCode64() == FnvHash.Constants.STATIC) && this.lexer.token() == Token.FUNCTION) {
                        if (name.nameHashCode64() == FnvHash.Constants.MEMBER) {
                            parameter.setMember(true);
                        }
                        final OracleFunctionDataType functionDataType = new OracleFunctionDataType();
                        functionDataType.setStatic(name.nameHashCode64() == FnvHash.Constants.STATIC);
                        this.lexer.nextToken();
                        functionDataType.setName(this.lexer.stringVal());
                        this.accept(Token.IDENTIFIER);
                        if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.parserParameters(functionDataType.getParameters(), functionDataType);
                            this.accept(Token.RPAREN);
                        }
                        this.accept(Token.RETURN);
                        functionDataType.setReturnDataType(this.exprParser.parseDataType(false));
                        dataType = functionDataType;
                        name = null;
                        if (this.lexer.token() == Token.IS) {
                            this.lexer.nextToken();
                            final SQLStatement block = this.parseBlock();
                            functionDataType.setBlock(block);
                        }
                    }
                    else if ((name.nameHashCode64() == FnvHash.Constants.MEMBER || name.nameHashCode64() == FnvHash.Constants.STATIC) && this.lexer.token() == Token.PROCEDURE) {
                        if (name.nameHashCode64() == FnvHash.Constants.MEMBER) {
                            parameter.setMember(true);
                        }
                        final OracleProcedureDataType procedureDataType = new OracleProcedureDataType();
                        procedureDataType.setStatic(name.nameHashCode64() == FnvHash.Constants.STATIC);
                        this.lexer.nextToken();
                        procedureDataType.setName(this.lexer.stringVal());
                        this.accept(Token.IDENTIFIER);
                        if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.parserParameters(procedureDataType.getParameters(), procedureDataType);
                            this.accept(Token.RPAREN);
                        }
                        dataType = procedureDataType;
                        name = null;
                        if (this.lexer.token() == Token.IS) {
                            this.lexer.nextToken();
                            final SQLStatement block = this.parseBlock();
                            procedureDataType.setBlock(block);
                        }
                    }
                    else {
                        dataType = this.exprParser.parseDataType(false);
                    }
                    if (this.lexer.token() == Token.COLONEQ || this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                        parameter.setDefaultValue(this.exprParser.expr());
                    }
                }
            }
            parameter.setName(name);
            parameter.setDataType(dataType);
            parameters.add(parameter);
            token = this.lexer.token();
            if (token == Token.COMMA || token == Token.SEMI || token == Token.IS) {
                this.lexer.nextToken();
            }
            token = this.lexer.token();
        } while (token != Token.BEGIN && token != Token.RPAREN && token != Token.EOF && token != Token.FUNCTION && !this.lexer.identifierEquals("DETERMINISTIC"));
    }
    
    @Override
    public OracleSelectParser createSQLSelectParser() {
        return new OracleSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public OracleStatement parseInsert() {
        if (this.lexer.token() == Token.LPAREN) {
            final OracleInsertStatement stmt = new OracleInsertStatement();
            this.parseInsert0(stmt, false);
            stmt.setReturning(this.parseReturningClause());
            stmt.setErrorLogging(this.parseErrorLoggingClause());
            return stmt;
        }
        this.accept(Token.INSERT);
        final List<SQLHint> hints = new ArrayList<SQLHint>();
        this.parseHints(hints);
        if (this.lexer.token() == Token.INTO) {
            final OracleInsertStatement stmt2 = new OracleInsertStatement();
            stmt2.setHints(hints);
            this.parseInsert0(stmt2);
            stmt2.setReturning(this.parseReturningClause());
            stmt2.setErrorLogging(this.parseErrorLoggingClause());
            return stmt2;
        }
        final OracleMultiInsertStatement stmt3 = this.parseMultiInsert();
        stmt3.setHints(hints);
        return stmt3;
    }
    
    public OracleMultiInsertStatement parseMultiInsert() {
        final OracleMultiInsertStatement stmt = new OracleMultiInsertStatement();
        if (this.lexer.token() == Token.ALL) {
            this.lexer.nextToken();
            stmt.setOption(OracleMultiInsertStatement.Option.ALL);
        }
        else if (this.lexer.token() == Token.FIRST || this.lexer.identifierEquals("FIRST")) {
            this.lexer.nextToken();
            stmt.setOption(OracleMultiInsertStatement.Option.FIRST);
        }
        while (this.lexer.token() == Token.INTO) {
            final OracleMultiInsertStatement.InsertIntoClause clause = new OracleMultiInsertStatement.InsertIntoClause();
            final boolean acceptSubQuery = stmt.getEntries().size() == 0;
            this.parseInsert0(clause, acceptSubQuery);
            clause.setReturning(this.parseReturningClause());
            clause.setErrorLogging(this.parseErrorLoggingClause());
            stmt.addEntry(clause);
        }
        if (this.lexer.token() == Token.WHEN) {
            final OracleMultiInsertStatement.ConditionalInsertClause clause2 = new OracleMultiInsertStatement.ConditionalInsertClause();
            while (this.lexer.token() == Token.WHEN) {
                this.lexer.nextToken();
                final OracleMultiInsertStatement.ConditionalInsertClauseItem item = new OracleMultiInsertStatement.ConditionalInsertClauseItem();
                item.setWhen(this.exprParser.expr());
                this.accept(Token.THEN);
                final OracleMultiInsertStatement.InsertIntoClause insertInto = new OracleMultiInsertStatement.InsertIntoClause();
                this.parseInsert0(insertInto);
                item.setThen(insertInto);
                clause2.addItem(item);
            }
            if (this.lexer.token() == Token.ELSE) {
                this.lexer.nextToken();
                final OracleMultiInsertStatement.InsertIntoClause insertInto2 = new OracleMultiInsertStatement.InsertIntoClause();
                this.parseInsert0(insertInto2, false);
                clause2.setElseItem(insertInto2);
            }
            stmt.addEntry(clause2);
        }
        final SQLSelect subQuery = this.createSQLSelectParser().select();
        stmt.setSubQuery(subQuery);
        return stmt;
    }
    
    private OracleExceptionStatement parseException() {
        this.accept(Token.EXCEPTION);
        final OracleExceptionStatement stmt = new OracleExceptionStatement();
        do {
            this.accept(Token.WHEN);
            final OracleExceptionStatement.Item item = new OracleExceptionStatement.Item();
            item.setWhen(this.exprParser.expr());
            this.accept(Token.THEN);
            this.parseStatementList(item.getStatements(), -1, item);
            stmt.addItem(item);
            if (this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            }
        } while (this.lexer.token() == Token.WHEN);
        return stmt;
    }
    
    public OracleReturningClause parseReturningClause() {
        OracleReturningClause clause = null;
        if (this.lexer.token() == Token.RETURNING) {
            this.lexer.nextToken();
            clause = new OracleReturningClause();
            while (true) {
                final SQLExpr item = this.exprParser.expr();
                clause.addItem(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.INTO);
            while (true) {
                final SQLExpr item = this.exprParser.expr();
                clause.addValue(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return clause;
    }
    
    @Override
    public OracleExplainStatement parseExplain() {
        this.accept(Token.EXPLAIN);
        this.acceptIdentifier("PLAN");
        final OracleExplainStatement stmt = new OracleExplainStatement();
        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            this.acceptIdentifier("STATEMENT_ID");
            this.accept(Token.EQ);
            stmt.setStatementId(this.exprParser.primary());
        }
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
            stmt.setInto(this.exprParser.name());
        }
        this.accept(Token.FOR);
        stmt.setStatement(this.parseStatement());
        return stmt;
    }
    
    @Override
    public OracleDeleteStatement parseDeleteStatement() {
        final OracleDeleteStatement deleteStatement = new OracleDeleteStatement();
        if (this.lexer.token() == Token.DELETE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }
            this.parseHints(deleteStatement.getHints());
            if (this.lexer.token() == Token.FROM) {
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("ONLY")) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLName tableName = this.exprParser.name();
                deleteStatement.setTableName(tableName);
                this.accept(Token.RPAREN);
            }
            else if (this.lexer.token() == Token.LPAREN) {
                final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSource();
                deleteStatement.setTableSource(tableSource);
            }
            else {
                final SQLName tableName = this.exprParser.name();
                deleteStatement.setTableName(tableName);
            }
            deleteStatement.setAlias(this.tableAlias());
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            deleteStatement.setWhere(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.RETURNING) {
            final OracleReturningClause clause = this.parseReturningClause();
            deleteStatement.setReturning(clause);
        }
        if (this.lexer.identifierEquals("RETURN") || this.lexer.identifierEquals("RETURNING")) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        if (this.lexer.identifierEquals("LOG")) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        return deleteStatement;
    }
    
    @Override
    public SQLStatement parseCreateDbLink() {
        this.accept(Token.CREATE);
        final OracleCreateDatabaseDbLinkStatement dbLink = new OracleCreateDatabaseDbLinkStatement();
        if (this.lexer.identifierEquals("SHARED")) {
            dbLink.setShared(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("PUBLIC")) {
            dbLink.setPublic(true);
            this.lexer.nextToken();
        }
        this.accept(Token.DATABASE);
        this.acceptIdentifier("LINK");
        dbLink.setName(this.exprParser.name());
        if (this.lexer.token() == Token.CONNECT) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            dbLink.setUser(this.exprParser.name());
            if (this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED)) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                dbLink.setPassword(this.lexer.stringVal());
                if (this.lexer.token() == Token.IDENTIFIER) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.LITERAL_ALIAS);
                }
            }
        }
        if (this.lexer.identifierEquals("AUTHENTICATED")) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            dbLink.setAuthenticatedUser(this.exprParser.name());
            this.acceptIdentifier("IDENTIFIED");
            this.accept(Token.BY);
            dbLink.setPassword(this.lexer.stringVal());
            this.accept(Token.IDENTIFIER);
        }
        if (this.lexer.token() == Token.USING) {
            this.lexer.nextToken();
            dbLink.setUsing(this.exprParser.expr());
        }
        return dbLink;
    }
    
    @Override
    public OracleCreateIndexStatement parseCreateIndex(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        final OracleCreateIndexStatement stmt = new OracleCreateIndexStatement();
        if (this.lexer.token() == Token.UNIQUE) {
            stmt.setType("UNIQUE");
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals("BITMAP")) {
            stmt.setType("BITMAP");
            this.lexer.nextToken();
        }
        this.accept(Token.INDEX);
        stmt.setName(this.exprParser.name());
        this.accept(Token.ON);
        if (this.lexer.identifierEquals("CLUSTER")) {
            this.lexer.nextToken();
            stmt.setCluster(true);
        }
        stmt.setTable(this.exprParser.name());
        if (this.lexer.token() == Token.IDENTIFIER) {
            final String alias = this.lexer.stringVal();
            stmt.getTable().setAlias(alias);
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
                stmt.addItem(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
    Label_0238:
        while (true) {
            this.getExprParser().parseSegmentAttributes(stmt);
            if (this.lexer.token() == Token.COMPUTE) {
                this.lexer.nextToken();
                this.acceptIdentifier("STATISTICS");
                stmt.setComputeStatistics(true);
            }
            else if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                stmt.setEnable(true);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                stmt.setEnable(false);
            }
            else if (this.lexer.identifierEquals("ONLINE")) {
                this.lexer.nextToken();
                stmt.setOnline(true);
            }
            else if (this.lexer.identifierEquals("NOPARALLEL")) {
                this.lexer.nextToken();
                stmt.setNoParallel(true);
            }
            else if (this.lexer.identifierEquals("PARALLEL")) {
                this.lexer.nextToken();
                stmt.setParallel(this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.INDEX) {
                this.lexer.nextToken();
                this.acceptIdentifier("ONLY");
                this.acceptIdentifier("TOPLEVEL");
                stmt.setIndexOnlyTopLevel(true);
            }
            else if (this.lexer.identifierEquals("SORT")) {
                this.lexer.nextToken();
                stmt.setSort(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOSORT")) {
                this.lexer.nextToken();
                stmt.setSort(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("REVERSE")) {
                this.lexer.nextToken();
                stmt.setReverse(true);
            }
            else {
                if (this.lexer.identifierEquals("LOCAL")) {
                    this.lexer.nextToken();
                    stmt.setLocal(true);
                    while (true) {
                        if (this.lexer.token() == Token.STORE) {
                            this.lexer.nextToken();
                            this.accept(Token.IN);
                            this.accept(Token.LPAREN);
                            this.exprParser.names(stmt.getLocalStoreIn(), stmt);
                            this.accept(Token.RPAREN);
                        }
                        else {
                            if (this.lexer.token() != Token.LPAREN) {
                                continue Label_0238;
                            }
                            this.lexer.nextToken();
                            while (true) {
                                final SQLPartition partition = this.getExprParser().parsePartition();
                                partition.setParent(stmt);
                                stmt.getLocalPartitions().add(partition);
                                if (this.lexer.token() != Token.COMMA) {
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            if (this.lexer.token() != Token.RPAREN) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                    }
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                if (!this.lexer.identifierEquals("GLOBAL")) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setGlobal(true);
                if (this.lexer.token() != Token.PARTITION) {
                    break;
                }
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.identifierEquals("RANGE")) {
                    final SQLPartitionByRange partitionByRange = this.getExprParser().partitionByRange();
                    this.getExprParser().partitionClauseRest(partitionByRange);
                    partitionByRange.setParent(stmt);
                    stmt.getGlobalPartitions().add(partitionByRange);
                }
                else {
                    if (!this.lexer.identifierEquals("HASH")) {
                        break;
                    }
                    final SQLPartitionByHash partitionByHash = this.getExprParser().partitionByHash();
                    this.getExprParser().partitionClauseRest(partitionByHash);
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        while (true) {
                            final SQLPartition partition2 = this.getExprParser().parsePartition();
                            partitionByHash.addPartition(partition2);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        if (this.lexer.token() != Token.RPAREN) {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                    }
                    partitionByHash.setParent(stmt);
                    stmt.getGlobalPartitions().add(partitionByHash);
                }
            }
        }
        return stmt;
    }
    
    @Override
    public SQLCreateSequenceStatement parseCreateSequence(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        this.accept(Token.SEQUENCE);
        final SQLCreateSequenceStatement stmt = new SQLCreateSequenceStatement();
        stmt.setDbType(DbType.oracle);
        stmt.setName(this.exprParser.name());
        while (true) {
            if (this.lexer.token() == Token.START) {
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
        else {
            stmt.setCreate(false);
        }
        this.accept(Token.PROCEDURE);
        final SQLName procedureName = this.exprParser.name();
        stmt.setName(procedureName);
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals("AUTHID")) {
            this.lexer.nextToken();
            final String strVal = this.lexer.stringVal();
            if (this.lexer.identifierEquals("CURRENT_USER")) {
                this.lexer.nextToken();
            }
            else {
                this.acceptIdentifier("DEFINER");
            }
            final SQLName authid = new SQLIdentifierExpr(strVal);
            stmt.setAuthid(authid);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WRAPPED)) {
            this.lexer.nextToken();
            final int pos = this.lexer.text.indexOf(59, this.lexer.pos());
            if (pos != -1) {
                final String wrappedString = this.lexer.subString(this.lexer.pos(), pos - this.lexer.pos());
                stmt.setWrappedSource(wrappedString);
                this.lexer.reset(pos, ';', Token.LITERAL_CHARS);
                this.lexer.nextToken();
                stmt.setAfterSemi(true);
            }
            else {
                final String wrappedString = this.lexer.text.substring(this.lexer.pos());
                stmt.setWrappedSource(wrappedString);
                this.lexer.reset(this.lexer.text.length(), '\u001a', Token.EOF);
            }
            return stmt;
        }
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
            return stmt;
        }
        if (this.lexer.token() == Token.IS) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.AS);
        }
        if (!this.lexer.identifierEquals("LANGUAGE")) {
            final SQLStatement block = this.parseBlock();
            stmt.setBlock(block);
            if (this.lexer.identifierEquals(procedureName.getSimpleName())) {
                this.lexer.nextToken();
            }
            return stmt;
        }
        this.lexer.nextToken();
        if (this.lexer.identifierEquals("JAVA")) {
            this.lexer.nextToken();
            this.acceptIdentifier("NAME");
            final String javaCallSpec = this.lexer.stringVal();
            this.accept(Token.LITERAL_CHARS);
            stmt.setJavaCallSpec(javaCallSpec);
            return stmt;
        }
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    @Override
    public SQLUpdateStatement parseUpdateStatement() {
        return new OracleUpdateParser(this.lexer).parseUpdateStatement();
    }
    
    @Override
    public SQLStatement parseCreatePackage() {
        this.accept(Token.CREATE);
        boolean repalce = false;
        if (this.lexer.token() == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            repalce = true;
        }
        this.acceptIdentifier("PACKAGE");
        final OracleCreatePackageStatement stmt = new OracleCreatePackageStatement();
        stmt.setOrReplace(repalce);
        if (this.lexer.identifierEquals("BODY")) {
            this.lexer.nextToken();
            stmt.setBody(true);
        }
        final SQLName pkgName = this.exprParser.name();
        stmt.setName(pkgName);
        if (this.lexer.token() == Token.IS) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.AS);
        }
        while (true) {
            if (this.lexer.token() == Token.IDENTIFIER) {
                final SQLDeclareStatement varDecl = new SQLDeclareStatement();
                varDecl.setDbType(this.dbType);
                varDecl.setParent(stmt);
                final SQLDeclareItem varItem = new SQLDeclareItem();
                boolean type = false;
                if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.nextToken();
                    type = true;
                }
                final SQLName name = this.exprParser.name();
                varItem.setName(name);
                if (type) {
                    this.accept(Token.IS);
                    if (this.lexer.identifierEquals(FnvHash.Constants.RECORD)) {
                        this.lexer.nextToken();
                        final SQLRecordDataType recordDataType = new SQLRecordDataType();
                        this.accept(Token.LPAREN);
                        while (true) {
                            final SQLColumnDefinition column = this.exprParser.parseColumn();
                            recordDataType.addColumn(column);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        varItem.setDataType(recordDataType);
                    }
                    else {
                        this.acceptIdentifier("REF");
                        this.accept(Token.CURSOR);
                        varItem.setDataType(new SQLDataTypeImpl("REF CURSOR"));
                    }
                }
                else {
                    varItem.setDataType(this.exprParser.parseDataType(false));
                }
                varItem.setParent(varDecl);
                if (this.lexer.token() == Token.COLONEQ) {
                    this.lexer.nextToken();
                    final SQLExpr defaultVal = this.exprParser.expr();
                    varItem.setValue(defaultVal);
                }
                varDecl.getItems().add(varItem);
                this.accept(Token.SEMI);
                varDecl.setAfterSemi(true);
                stmt.getStatements().add(varDecl);
            }
            else if (this.lexer.token() == Token.FUNCTION) {
                final SQLStatement function = this.parseFunction();
                function.setParent(stmt);
                stmt.getStatements().add(function);
            }
            else {
                if (this.lexer.token() != Token.PROCEDURE) {
                    break;
                }
                final SQLStatement proc = this.parseCreateProcedure();
                proc.setParent(stmt);
                stmt.getStatements().add(proc);
            }
        }
        if (this.lexer.token() != Token.END) {
            if (this.lexer.token() != Token.BEGIN) {
                throw new ParserException("TODO : " + this.lexer.info());
            }
            this.lexer.nextToken();
            final SQLBlockStatement block = new SQLBlockStatement();
            this.parseStatementList(block.getStatementList(), -1, block);
            this.accept(Token.END);
            block.setParent(stmt);
            stmt.getStatements().add(block);
            if (this.lexer.identifierEquals(pkgName.getSimpleName())) {
                this.lexer.nextToken();
                this.accept(Token.SEMI);
                return stmt;
            }
        }
        this.accept(Token.END);
        if (this.lexer.identifierEquals(pkgName.getSimpleName())) {
            this.lexer.nextToken();
        }
        this.accept(Token.SEMI);
        return stmt;
    }
    
    @Override
    public SQLStatement parseCreateSynonym() {
        final OracleCreateSynonymStatement stmt = new OracleCreateSynonymStatement();
        this.accept(Token.CREATE);
        if (this.lexer.token() == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            stmt.setOrReplace(true);
        }
        if (this.lexer.identifierEquals("PUBLIC")) {
            this.lexer.nextToken();
            stmt.setPublic(true);
        }
        this.acceptIdentifier("SYNONYM");
        stmt.setName(this.exprParser.name());
        this.accept(Token.FOR);
        stmt.setObject(this.exprParser.name());
        return stmt;
    }
    
    @Override
    public SQLStatement parseCreateType() {
        final OracleCreateTypeStatement stmt = new OracleCreateTypeStatement();
        this.accept(Token.CREATE);
        if (this.lexer.token() == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            stmt.setOrReplace(true);
        }
        this.acceptIdentifier("TYPE");
        if (this.lexer.identifierEquals("BODY")) {
            this.lexer.nextToken();
            stmt.setBody(true);
        }
        final SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.UNDER)) {
            this.lexer.nextToken();
            final SQLName under = this.exprParser.name();
            stmt.setUnder(under);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.AUTHID)) {
            this.lexer.nextToken();
            final SQLName authId = this.exprParser.name();
            stmt.setAuthId(authId);
        }
        if (this.lexer.token() == Token.AS || this.lexer.token() == Token.IS) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("OBJECT")) {
            this.lexer.nextToken();
            stmt.setObject(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STATIC)) {
            this.parserParameters(stmt.getParameters(), stmt);
        }
        else if (this.lexer.token() == Token.TABLE) {
            this.lexer.nextToken();
            this.accept(Token.OF);
            final SQLDataType dataType = this.exprParser.parseDataType();
            stmt.setTableOf(dataType);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.VARRAY)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            final SQLExpr sizeLimit = this.exprParser.primary();
            stmt.setVarraySizeLimit(sizeLimit);
            this.accept(Token.RPAREN);
            this.accept(Token.OF);
            final SQLDataType dataType2 = this.exprParser.parseDataType();
            stmt.setVarrayDataType(dataType2);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.WRAPPED)) {
            final int pos = this.lexer.text.indexOf(59, this.lexer.pos());
            if (pos != -1) {
                final String wrappedString = this.lexer.subString(this.lexer.pos(), pos - this.lexer.pos());
                stmt.setWrappedSource(wrappedString);
                this.lexer.reset(pos, ';', Token.LITERAL_CHARS);
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters(), stmt);
            stmt.setParen(true);
            this.accept(Token.RPAREN);
        }
        else {
            this.parserParameters(stmt.getParameters(), stmt);
            if (this.lexer.token() == Token.END) {
                this.lexer.nextToken();
            }
        }
        while (true) {
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.FINAL)) {
                    this.lexer.nextToken();
                    stmt.setFinal(false);
                }
                else {
                    this.acceptIdentifier("INSTANTIABLE");
                    stmt.setInstantiable(false);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.FINAL)) {
                this.lexer.nextToken();
                stmt.setFinal(true);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.INSTANTIABLE)) {
                    break;
                }
                this.lexer.nextToken();
                stmt.setInstantiable(true);
            }
        }
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
            stmt.setAfterSemi(true);
        }
        return stmt;
    }
}
