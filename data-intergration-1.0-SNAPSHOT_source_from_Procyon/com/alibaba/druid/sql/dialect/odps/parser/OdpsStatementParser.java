// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.parser;

import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.statement.SQLObjectType;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsGrantStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSetLabelStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsShowChangelogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowHistoryStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRolesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowACLStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRoleStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowFunctionsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowRecylebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowUsersStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsShowGrantsStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPackagesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticListStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsStatisticClause;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCloneTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUnloadStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsQueryAliasStatement;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsDeclareVariableStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUndoTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRestoreStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsInstallPackageStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsExstoreStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCountStatement;
import com.alibaba.druid.sql.ast.statement.SQLWhoamiStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsListStmt;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsReadStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveStatisticStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddFileStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddAccountProviderStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddStatisticStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class OdpsStatementParser extends SQLStatementParser
{
    public OdpsStatementParser(final String sql) {
        super(new OdpsExprParser(sql, new SQLParserFeature[0]));
    }
    
    public OdpsStatementParser(final String sql, final SQLParserFeature... features) {
        super(new OdpsExprParser(sql, features));
    }
    
    public OdpsStatementParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLSelectStatement parseSelect() {
        final SQLSelect select = new OdpsSelectParser(this.exprParser).select();
        return new SQLSelectStatement(select, DbType.odps);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        final SQLCreateTableParser parser = new OdpsCreateTableParser(this.exprParser);
        return parser.parseCreateTable();
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new OdpsCreateTableParser(this.exprParser);
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        if (this.lexer.token() == Token.FROM) {
            final SQLStatement stmt = this.parseInsert();
            statementList.add(stmt);
            return true;
        }
        if (this.lexer.identifierEquals("ANALYZE")) {
            final SQLStatement stmt = this.parseAnalyze();
            statementList.add(stmt);
            return true;
        }
        if (this.lexer.identifierEquals("ADD")) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("STATISTIC")) {
                this.lexer.nextToken();
                final OdpsAddStatisticStatement stmt2 = new OdpsAddStatisticStatement();
                stmt2.setTable(this.exprParser.name());
                stmt2.setStatisticClause(this.parseStaticClause());
                statementList.add(stmt2);
                return true;
            }
            if (this.lexer.token() == Token.USER) {
                this.lexer.nextToken();
                final OdpsAddUserStatement stmt3 = new OdpsAddUserStatement();
                stmt3.setUser(this.exprParser.name());
                statementList.add(stmt3);
                return true;
            }
            if (this.lexer.identifierEquals("ACCOUNTPROVIDER")) {
                this.lexer.nextToken();
                final OdpsAddAccountProviderStatement stmt4 = new OdpsAddAccountProviderStatement();
                stmt4.setProvider(this.exprParser.name());
                statementList.add(stmt4);
                return true;
            }
            if (this.lexer.token() == Token.TABLE) {
                this.lexer.nextToken();
                final OdpsAddTableStatement stmt5 = new OdpsAddTableStatement();
                stmt5.setTable(this.exprParser.name());
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.exprParser.parseAssignItem(stmt5.getPartitions(), stmt5);
                }
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                    final SQLName name = this.exprParser.name();
                    stmt5.getTable().setAlias(name.toString());
                }
                if (this.lexer.token() == Token.COMMENT) {
                    this.lexer.nextToken();
                    stmt5.setComment(this.exprParser.primary());
                }
                if (this.lexer.token() == Token.SUB) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("f");
                    stmt5.setForce(true);
                }
                if (this.lexer.token() == Token.TO) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("PACKAGE");
                    final SQLName packageName = this.exprParser.name();
                    stmt5.setToPackage(packageName);
                    if (this.lexer.token() == Token.WITH) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("PRIVILEGES");
                        this.parsePrivileages(stmt5.getPrivileges(), stmt5);
                    }
                }
                statementList.add(stmt5);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.FILE) || this.lexer.identifierEquals(FnvHash.Constants.JAR) || this.lexer.identifierEquals(FnvHash.Constants.PY) || this.lexer.identifierEquals(FnvHash.Constants.ARCHIVE)) {
                final OdpsAddFileStatement stmt6 = new OdpsAddFileStatement();
                final long hash = this.lexer.hash_lower();
                if (hash == FnvHash.Constants.JAR) {
                    stmt6.setType(OdpsAddFileStatement.FileType.JAR);
                }
                else if (hash == FnvHash.Constants.PY) {
                    stmt6.setType(OdpsAddFileStatement.FileType.PY);
                }
                else if (hash == FnvHash.Constants.ARCHIVE) {
                    stmt6.setType(OdpsAddFileStatement.FileType.ARCHIVE);
                }
                this.lexer.nextPath();
                final String path = this.lexer.stringVal();
                this.lexer.nextToken();
                stmt6.setFile(path);
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                    final SQLName name2 = this.exprParser.name();
                    stmt6.setAlias(name2.toString());
                }
                if (this.lexer.token() == Token.COMMENT) {
                    this.lexer.nextToken();
                    stmt6.setComment(this.exprParser.primary());
                }
                if (this.lexer.token() == Token.SUB) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("f");
                    stmt6.setForce(true);
                }
                statementList.add(stmt6);
                return true;
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
        else if (this.lexer.identifierEquals("REMOVE")) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("STATISTIC")) {
                this.lexer.nextToken();
                final OdpsRemoveStatisticStatement stmt7 = new OdpsRemoveStatisticStatement();
                stmt7.setTable(this.exprParser.name());
                stmt7.setStatisticClause(this.parseStaticClause());
                statementList.add(stmt7);
                return true;
            }
            if (this.lexer.token() == Token.USER) {
                this.lexer.nextToken();
                final OdpsRemoveUserStatement stmt8 = new OdpsRemoveUserStatement();
                stmt8.setUser((SQLIdentifierExpr)this.exprParser.name());
                statementList.add(stmt8);
                return true;
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
        else {
            if (this.lexer.identifierEquals("READ")) {
                final OdpsReadStatement stmt9 = new OdpsReadStatement();
                if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                    stmt9.addBeforeComment(this.lexer.readAndResetComments());
                }
                this.lexer.nextToken();
                stmt9.setTable(this.exprParser.name());
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprParser.names(stmt9.getColumns(), stmt9);
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.accept(Token.LPAREN);
                    this.parseAssignItems(stmt9.getPartition(), stmt9);
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token() == Token.LITERAL_INT) {
                    stmt9.setRowCount(this.exprParser.primary());
                }
                statementList.add(stmt9);
                return true;
            }
            if (this.lexer.identifierEquals("LIST")) {
                final OdpsListStmt stmt10 = new OdpsListStmt();
                this.lexer.nextToken();
                stmt10.setObject(this.exprParser.expr());
                if (this.lexer.identifierEquals("ROLES") && stmt10.getObject() instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)stmt10.getObject()).nameEquals("TENANT")) {
                    this.lexer.nextToken();
                    stmt10.setObject(new SQLIdentifierExpr("TENANT ROLES"));
                }
                else if (this.lexer.identifierEquals("OUTPUT") && stmt10.getObject() instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)stmt10.getObject()).nameEquals("TEMPORARY")) {
                    this.lexer.nextToken();
                    stmt10.setObject(new SQLIdentifierExpr("TEMPORARY OUTPUT"));
                }
                statementList.add(stmt10);
                return true;
            }
            if (this.lexer.token() == Token.DESC || this.lexer.identifierEquals("DESCRIBE")) {
                final SQLStatement stmt = this.parseDescribe();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals("WHOAMI")) {
                this.lexer.nextToken();
                final SQLWhoamiStatement stmt11 = new SQLWhoamiStatement();
                statementList.add(stmt11);
                return true;
            }
            if (this.lexer.identifierEquals("COUNT")) {
                this.lexer.nextToken();
                final OdpsCountStatement stmt12 = new OdpsCountStatement();
                stmt12.setTable(this.exprParser.name());
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.exprParser.parseAssignItem(stmt12.getPartitions(), stmt12);
                }
                statementList.add(stmt12);
                return true;
            }
            if (this.lexer.identifierEquals("MSCK")) {
                final SQLStatement stmt = this.parseMsck();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals("EXSTORE")) {
                this.lexer.nextToken();
                final OdpsExstoreStatement stmt13 = new OdpsExstoreStatement();
                final SQLExpr table = this.exprParser.expr();
                stmt13.setTable(new SQLExprTableSource(table));
                this.accept(Token.PARTITION);
                this.exprParser.parseAssignItem(stmt13.getPartitions(), stmt13);
                statementList.add(stmt13);
                return true;
            }
            if (this.lexer.identifierEquals("INSTALL")) {
                this.lexer.nextToken();
                this.acceptIdentifier("PACKAGE");
                final OdpsInstallPackageStatement stmt14 = new OdpsInstallPackageStatement();
                stmt14.setPackageName(this.exprParser.name());
                statementList.add(stmt14);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.KILL)) {
                final SQLStatement stmt = this.parseKill();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LOAD)) {
                final HiveLoadDataStatement stmt15 = this.parseLoad();
                statementList.add(stmt15);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.MERGE)) {
                final SQLStatement stmt = this.parseMerge();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.CLONE)) {
                final SQLStatement stmt = this.parseClone();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.UNLOAD)) {
                final SQLStatement stmt = this.parseUnload();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.BEGIN)) {
                final SQLStatement stmt = this.parseBlock();
                statementList.add(stmt);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.RESTORE)) {
                this.lexer.nextToken();
                this.accept(Token.TABLE);
                final OdpsRestoreStatement stmt16 = new OdpsRestoreStatement();
                stmt16.setTable(this.exprParser.name());
                if (this.lexer.token() == Token.LPAREN) {
                    this.exprParser.parseAssignItem(stmt16.getProperties(), stmt16);
                }
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.exprParser.parseAssignItem(stmt16.getPartitions(), stmt16);
                }
                if (this.lexer.token() == Token.TO) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("LSN");
                    stmt16.setTo(this.exprParser.expr());
                }
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                    stmt16.setAlias(this.alias());
                }
                statementList.add(stmt16);
                return true;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.UNDO)) {
                this.lexer.nextToken();
                this.accept(Token.TABLE);
                final OdpsUndoTableStatement stmt17 = new OdpsUndoTableStatement();
                stmt17.setTable(new SQLExprTableSource(this.exprParser.name()));
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.exprParser.parseAssignItem(stmt17.getPartitions(), stmt17);
                }
                this.accept(Token.TO);
                stmt17.setTo(this.exprParser.expr());
                statementList.add(stmt17);
                return true;
            }
            if (this.lexer.token() == Token.FUNCTION) {
                final HiveCreateFunctionStatement stmt18 = (HiveCreateFunctionStatement)this.parseHiveCreateFunction();
                stmt18.setDeclare(true);
                statementList.add(stmt18);
                return true;
            }
            if (this.lexer.token() == Token.VARIANT && this.lexer.stringVal().startsWith("@")) {
                final Lexer.SavePoint mark = this.lexer.mark();
                final String variant = this.lexer.stringVal();
                this.lexer.nextToken();
                if (this.lexer.token() != Token.COLONEQ) {
                    final OdpsDeclareVariableStatement stmt19 = new OdpsDeclareVariableStatement();
                    if (this.lexer.token() != Token.EQ && this.lexer.token() != Token.SEMI && this.lexer.token() != Token.EOF) {
                        stmt19.setDataType(this.exprParser.parseDataType());
                    }
                    if (this.lexer.token() == Token.EQ || this.lexer.token() == Token.COLONEQ) {
                        this.lexer.nextToken();
                        stmt19.setInitValue(this.exprParser.expr());
                    }
                    if (this.lexer.token() == Token.SEMI) {
                        this.lexer.nextToken();
                    }
                    statementList.add(stmt19);
                    return true;
                }
                this.lexer.nextToken();
                boolean cache = false;
                if (this.lexer.identifierEquals(FnvHash.Constants.CACHE)) {
                    this.lexer.nextToken();
                    this.accept(Token.ON);
                    cache = true;
                }
                Lexer.SavePoint lpMark = null;
                if (this.lexer.token() == Token.LPAREN) {
                    lpMark = this.lexer.mark();
                    this.lexer.nextToken();
                }
                switch (this.lexer.token()) {
                    case LITERAL_INT:
                    case LITERAL_FLOAT:
                    case LITERAL_CHARS:
                    case LITERAL_ALIAS:
                    case IDENTIFIER:
                    case CASE:
                    case CAST:
                    case IF:
                    case VARIANT:
                    case REPLACE:
                    case NEW:
                    case SUB:
                    case TRUE:
                    case FALSE: {
                        if (lpMark != null) {
                            this.lexer.reset(lpMark);
                        }
                        final SQLExpr expr = this.exprParser.expr();
                        final SQLExprStatement stmt20 = new SQLExprStatement(new SQLAssignItem(new SQLIdentifierExpr(variant), expr));
                        statementList.add(stmt20);
                        return true;
                    }
                    default: {
                        if (lpMark != null) {
                            this.lexer.reset(lpMark);
                        }
                        final boolean paren = this.lexer.token() == Token.LPAREN;
                        final Lexer.SavePoint parenMark = this.lexer.mark();
                        SQLSelect select;
                        try {
                            select = new OdpsSelectParser(this.exprParser).select();
                        }
                        catch (ParserException error) {
                            if (paren) {
                                this.lexer.reset(parenMark);
                                final SQLExpr expr2 = this.exprParser.expr();
                                final SQLExprStatement stmt21 = new SQLExprStatement(new SQLAssignItem(new SQLIdentifierExpr(variant), expr2));
                                statementList.add(stmt21);
                                return true;
                            }
                            throw error;
                        }
                        switch (this.lexer.token()) {
                            case GT:
                            case GTEQ:
                            case EQ:
                            case LT:
                            case LTEQ: {
                                statementList.add(new SQLExprStatement(new SQLAssignItem(new SQLIdentifierExpr(variant), this.exprParser.exprRest(new SQLQueryExpr(select)))));
                                return true;
                            }
                            default: {
                                final SQLSelectStatement stmt22 = new SQLSelectStatement(select, this.dbType);
                                final OdpsQueryAliasStatement aliasQueryStatement = new OdpsQueryAliasStatement(variant, stmt22);
                                aliasQueryStatement.setCache(cache);
                                statementList.add(aliasQueryStatement);
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            else {
                if (this.lexer.token() == Token.IF) {
                    final SQLStatement stmt = this.parseIf();
                    statementList.add(stmt);
                    return true;
                }
                if (this.lexer.token() == Token.CODE) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.SEMI) {
                        return true;
                    }
                    this.lexer.reset(mark);
                }
                return false;
            }
        }
    }
    
    @Override
    public SQLStatement parseIf() {
        this.accept(Token.IF);
        final SQLIfStatement ifStmt = new SQLIfStatement();
        ifStmt.setCondition(this.exprParser.expr());
        if (this.lexer.identifierEquals("BEGIN")) {
            this.lexer.nextToken();
            this.parseStatementList(ifStmt.getStatements(), -1, ifStmt);
            this.accept(Token.END);
        }
        else {
            final SQLStatement stmt = this.parseStatement();
            ifStmt.getStatements().add(stmt);
            stmt.setParent(ifStmt);
        }
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            final SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
            if (this.lexer.identifierEquals("BEGIN")) {
                this.lexer.nextToken();
                this.parseStatementList(elseItem.getStatements(), -1, ifStmt);
                this.accept(Token.END);
            }
            else {
                final SQLStatement stmt2 = this.parseStatement();
                elseItem.getStatements().add(stmt2);
                stmt2.setParent(elseItem);
            }
            ifStmt.setElseItem(elseItem);
        }
        return ifStmt;
    }
    
    @Override
    public SQLStatement parseKill() {
        this.acceptIdentifier("KILL");
        final MySqlKillStatement stmt = new MySqlKillStatement();
        final SQLExpr instanceId = this.exprParser.primary();
        stmt.setThreadId(instanceId);
        return stmt;
    }
    
    public SQLStatement parseUnload() {
        this.acceptIdentifier("UNLOAD");
        final OdpsUnloadStatement stmt = new OdpsUnloadStatement();
        this.accept(Token.FROM);
        if (this.lexer.token() == Token.LPAREN || this.lexer.token() == Token.SELECT) {
            stmt.setFrom(this.createSQLSelectParser().parseTableSource());
        }
        else {
            stmt.setFrom(this.exprParser.name());
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getPartitions(), stmt);
        }
        this.accept(Token.INTO);
        if (this.lexer.identifierEquals("LOCATION")) {
            this.lexer.nextToken();
            stmt.setLocation(this.exprParser.primary());
        }
        if (this.lexer.identifierEquals("ROW")) {
            final SQLExternalRecordFormat format = this.exprParser.parseRowFormat();
            stmt.setRowFormat(format);
        }
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.BY) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.AS);
                }
                stmt.setStoredAs(this.exprParser.name());
            }
            else if (this.lexer.token() == Token.WITH) {
                this.lexer.nextToken();
                this.acceptIdentifier("SERDEPROPERTIES");
                this.exprParser.parseAssignItem(stmt.getSerdeProperties(), stmt);
            }
            else {
                if (!this.identifierEquals("PROPERTIES")) {
                    break;
                }
                this.lexer.nextToken();
                this.exprParser.parseAssignItem(stmt.getProperties(), stmt);
            }
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseClone() {
        this.acceptIdentifier("CLONE");
        this.accept(Token.TABLE);
        final SQLCloneTableStatement stmt = new SQLCloneTableStatement();
        stmt.setFrom(this.exprParser.name());
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getPartitions(), stmt);
        }
        this.accept(Token.TO);
        stmt.setTo(this.exprParser.name());
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            if (this.lexer.token() == Token.OVERWRITE) {
                this.lexer.nextToken();
                stmt.setIfExistsOverwrite(true);
            }
            else {
                this.acceptIdentifier("IGNORE");
                stmt.setIfExistsIgnore(true);
            }
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseBlock() {
        final SQLBlockStatement block = new SQLBlockStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.BEGIN)) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.BEGIN);
        }
        this.parseStatementList(block.getStatementList(), -1, block);
        this.accept(Token.END);
        return block;
    }
    
    protected OdpsStatisticClause parseStaticClause() {
        if (this.lexer.identifierEquals("TABLE_COUNT")) {
            this.lexer.nextToken();
            return new OdpsStatisticClause.TableCount();
        }
        if (this.lexer.identifierEquals("NULL_VALUE")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.NullValue null_value = new OdpsStatisticClause.NullValue();
            null_value.setColumn(this.exprParser.name());
            return null_value;
        }
        if (this.lexer.identifierEquals("DISTINCT_VALUE")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.DistinctValue distinctValue = new OdpsStatisticClause.DistinctValue();
            distinctValue.setColumn(this.exprParser.name());
            return distinctValue;
        }
        if (this.lexer.identifierEquals("COLUMN_SUM")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.ColumnSum column_sum = new OdpsStatisticClause.ColumnSum();
            column_sum.setColumn(this.exprParser.name());
            return column_sum;
        }
        if (this.lexer.identifierEquals("COLUMN_MAX")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.ColumnMax column_max = new OdpsStatisticClause.ColumnMax();
            column_max.setColumn(this.exprParser.name());
            return column_max;
        }
        if (this.lexer.identifierEquals("COLUMN_MIN")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.ColumnMin column_min = new OdpsStatisticClause.ColumnMin();
            column_min.setColumn(this.exprParser.name());
            return column_min;
        }
        if (this.lexer.identifierEquals("EXPRESSION_CONDITION")) {
            this.lexer.nextToken();
            final OdpsStatisticClause.ExpressionCondition expr_condition = new OdpsStatisticClause.ExpressionCondition();
            expr_condition.setExpr(this.exprParser.expr());
            return expr_condition;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    @Override
    public SQLStatement parseInsert() {
        if (this.lexer.token() != Token.FROM) {
            return this.parseHiveInsertStmt();
        }
        this.lexer.nextToken();
        final HiveMultiInsertStatement stmt = new HiveMultiInsertStatement();
        if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.VARIANT) {
            final Lexer.SavePoint mark = this.lexer.mark();
            SQLExpr tableName = this.exprParser.name();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.reset(mark);
                tableName = this.exprParser.primary();
            }
            SQLTableSource from = new SQLExprTableSource(tableName);
            if (this.lexer.token() == Token.IDENTIFIER) {
                final String alias = this.alias();
                from.setAlias(alias);
            }
            final SQLSelectParser selectParser = this.createSQLSelectParser();
            from = selectParser.parseTableSourceRest(from);
            if (this.lexer.token() == Token.WHERE) {
                this.lexer.nextToken();
                final SQLExpr where = this.exprParser.expr();
                final SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock();
                queryBlock.addSelectItem(new SQLAllColumnExpr());
                queryBlock.setFrom(from);
                queryBlock.setWhere(where);
                if (this.lexer.token() == Token.GROUP) {
                    selectParser.parseGroupBy(queryBlock);
                }
                stmt.setFrom(new SQLSubqueryTableSource(queryBlock));
            }
            else {
                stmt.setFrom(from);
            }
        }
        else {
            SQLCommentHint hint = null;
            if (this.lexer.token() == Token.HINT) {
                hint = this.exprParser.parseHint();
            }
            this.accept(Token.LPAREN);
            final boolean paren2 = this.lexer.token() == Token.LPAREN;
            final SQLSelectParser selectParser2 = this.createSQLSelectParser();
            final SQLSelect select = selectParser2.select();
            SQLTableSource from2 = null;
            if (paren2 && this.lexer.token() != Token.RPAREN) {
                String subQueryAs = null;
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                    subQueryAs = this.tableAlias(true);
                }
                else {
                    subQueryAs = this.tableAlias(false);
                }
                final SQLSubqueryTableSource subQuery = new SQLSubqueryTableSource(select, subQueryAs);
                from2 = selectParser2.parseTableSourceRest(subQuery);
            }
            this.accept(Token.RPAREN);
            String alias2;
            if (this.lexer.token() == Token.INSERT) {
                alias2 = null;
            }
            else if (this.lexer.token() == Token.SELECT) {
                alias2 = null;
            }
            else {
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                }
                alias2 = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
            }
            if (from2 == null) {
                from2 = new SQLSubqueryTableSource(select, alias2);
            }
            else if (alias2 != null) {
                from2.setAlias(alias2);
            }
            final SQLTableSource tableSource = selectParser2.parseTableSourceRest(from2);
            if (hint != null && tableSource instanceof SQLJoinTableSource) {
                ((SQLJoinTableSource)tableSource).setHint(hint);
            }
            stmt.setFrom(tableSource);
        }
        if (this.lexer.token() == Token.SELECT) {
            final SQLSelectParser selectParser3 = this.createSQLSelectParser();
            final SQLSelect query = selectParser3.select();
            final HiveInsert insert = new HiveInsert();
            insert.setQuery(query);
            stmt.addItem(insert);
            return stmt;
        }
        do {
            final HiveInsert insert2 = this.parseHiveInsert();
            stmt.addItem(insert2);
        } while (this.lexer.token() == Token.INSERT);
        return stmt;
    }
    
    @Override
    public SQLSelectParser createSQLSelectParser() {
        return new OdpsSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLStatement parseShow() {
        this.accept(Token.SHOW);
        if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
            this.lexer.nextToken();
            final SQLShowPartitionsStmt stmt = new SQLShowPartitionsStmt();
            final SQLExpr expr = this.exprParser.expr();
            stmt.setTableSource(new SQLExprTableSource(expr));
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                this.parseAssignItems(stmt.getPartition(), stmt, false);
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token() == Token.WHERE) {
                this.lexer.nextToken();
                stmt.setWhere(this.exprParser.expr());
            }
            return stmt;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STATISTIC)) {
            this.lexer.nextToken();
            final SQLShowStatisticStmt stmt2 = new SQLShowStatisticStmt();
            final SQLExpr expr = this.exprParser.expr();
            stmt2.setTableSource(new SQLExprTableSource(expr));
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                this.parseAssignItems(stmt2.getPartitions(), stmt2, false);
                this.accept(Token.RPAREN);
            }
            if (this.identifierEquals("COLUMNS")) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.SEMI) {
                    this.accept(Token.LPAREN);
                    this.exprParser.names(stmt2.getColumns(), stmt2);
                    this.accept(Token.RPAREN);
                }
            }
            return stmt2;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STATISTIC_LIST)) {
            this.lexer.nextToken();
            final SQLShowStatisticListStmt stmt3 = new SQLShowStatisticListStmt();
            final SQLExpr expr = this.exprParser.expr();
            stmt3.setTableSource(new SQLExprTableSource(expr));
            return stmt3;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PACKAGES)) {
            this.lexer.nextToken();
            final SQLShowPackagesStatement stmt4 = new SQLShowPackagesStatement();
            return stmt4;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
            this.lexer.nextToken();
            final SQLShowTablesStatement stmt5 = new SQLShowTablesStatement();
            if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                stmt5.setDatabase(this.exprParser.name());
            }
            else if (this.lexer.token() == Token.IDENTIFIER) {
                final SQLName database = this.exprParser.name();
                stmt5.setDatabase(database);
            }
            if (this.lexer.token() == Token.LIKE) {
                this.lexer.nextToken();
                stmt5.setLike(this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS) {
                stmt5.setLike(this.exprParser.expr());
            }
            return stmt5;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LABEL)) {
            this.lexer.nextToken();
            this.acceptIdentifier("GRANTS");
            final OdpsShowGrantsStmt stmt6 = new OdpsShowGrantsStmt();
            stmt6.setLabel(true);
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                this.accept(Token.TABLE);
                stmt6.setObjectType(this.exprParser.expr());
            }
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                this.accept(Token.USER);
                stmt6.setUser(this.exprParser.expr());
            }
            return stmt6;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.GRANTS)) {
            this.lexer.nextToken();
            final OdpsShowGrantsStmt stmt6 = new OdpsShowGrantsStmt();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.USER) {
                    this.lexer.nextToken();
                }
                stmt6.setUser(this.exprParser.expr());
            }
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                this.acceptIdentifier("type");
                stmt6.setObjectType(this.exprParser.expr());
            }
            return stmt6;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.USERS)) {
            this.lexer.nextToken();
            final SQLShowUsersStatement stmt7 = new SQLShowUsersStatement();
            return stmt7;
        }
        if (this.lexer.identifierEquals("RECYCLEBIN")) {
            this.lexer.nextToken();
            final SQLShowRecylebinStatement stmt8 = new SQLShowRecylebinStatement();
            return stmt8;
        }
        if (this.lexer.identifierEquals("VARIABLES")) {
            this.lexer.nextToken();
            return this.parseShowVariants();
        }
        if (this.lexer.token() == Token.CREATE) {
            return this.parseShowCreateTable();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FUNCTIONS)) {
            this.lexer.nextToken();
            final SQLShowFunctionsStatement stmt9 = new SQLShowFunctionsStatement();
            if (this.lexer.token() == Token.LIKE) {
                this.lexer.nextToken();
                stmt9.setLike(this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.IDENTIFIER) {
                stmt9.setLike(this.exprParser.expr());
            }
            return stmt9;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ROLE)) {
            this.lexer.nextToken();
            final SQLShowRoleStatement stmt10 = new SQLShowRoleStatement();
            if (this.lexer.token() == Token.GRANT) {
                this.lexer.nextToken();
                stmt10.setGrant(this.exprParser.name());
            }
            return stmt10;
        }
        if (this.lexer.identifierEquals("ACL")) {
            this.lexer.nextToken();
            final SQLShowACLStatement stmt11 = new SQLShowACLStatement();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                stmt11.setTable(new SQLExprTableSource(this.exprParser.name()));
            }
            return stmt11;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ROLES)) {
            this.lexer.nextToken();
            final SQLShowRolesStatement stmt12 = new SQLShowRolesStatement();
            return stmt12;
        }
        if (this.lexer.identifierEquals("HISTORY")) {
            this.lexer.nextToken();
            final SQLShowHistoryStatement stmt13 = new SQLShowHistoryStatement();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
                    this.lexer.nextToken();
                    stmt13.setTables(true);
                }
                else if (this.lexer.token() == Token.TABLE) {
                    this.lexer.nextToken();
                    stmt13.setTable(new SQLExprTableSource(this.exprParser.name()));
                }
            }
            if (this.lexer.token() == Token.LPAREN) {
                this.exprParser.parseAssignItem(stmt13.getProperties(), stmt13);
            }
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.exprParser.parseAssignItem(stmt13.getPartitions(), stmt13);
            }
            return stmt13;
        }
        if (this.lexer.identifierEquals("CHANGELOGS")) {
            this.lexer.nextToken();
            final OdpsShowChangelogsStatement stmt14 = new OdpsShowChangelogsStatement();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
                    this.lexer.nextToken();
                    stmt14.setTables(true);
                }
                else if (this.lexer.token() == Token.TABLE) {
                    this.lexer.nextToken();
                    stmt14.setTable(new SQLExprTableSource(this.exprParser.name()));
                }
                else if (this.lexer.token() == Token.IDENTIFIER) {
                    stmt14.setTable(new SQLExprTableSource(this.exprParser.name()));
                }
            }
            if (this.lexer.token() == Token.LPAREN) {
                this.exprParser.parseAssignItem(stmt14.getProperties(), stmt14);
            }
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.exprParser.parseAssignItem(stmt14.getPartitions(), stmt14);
            }
            if (this.lexer.token() == Token.LITERAL_INT) {
                stmt14.setId(this.exprParser.primary());
            }
            return stmt14;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    @Override
    public SQLStatement parseSet() {
        List<String> comments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            comments = this.lexer.readAndResetComments();
        }
        boolean setProject = false;
        if (this.identifierEquals("SETPROJECT")) {
            this.lexer.nextToken();
            setProject = true;
        }
        else {
            this.accept(Token.SET);
        }
        if (this.lexer.token() == Token.SET && this.dbType == DbType.odps) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("PROJECT")) {
            this.lexer.nextToken();
            setProject = true;
        }
        if (setProject) {
            final SQLSetStatement stmt = new SQLSetStatement();
            stmt.setOption(SQLSetStatement.Option.PROJECT);
            final SQLName target = this.exprParser.name();
            this.accept(Token.EQ);
            final SQLExpr value = this.exprParser.expr();
            stmt.set(target, value);
            return stmt;
        }
        if (!this.lexer.identifierEquals("LABEL")) {
            final SQLSetStatement stmt = new SQLSetStatement(this.dbType);
            stmt.putAttribute("parser.set", Boolean.TRUE);
            if (comments != null) {
                stmt.addBeforeComment(comments);
            }
            this.parseAssignItems(stmt.getItems(), stmt);
            return stmt;
        }
        final OdpsSetLabelStatement stmt2 = new OdpsSetLabelStatement();
        if (comments != null) {
            stmt2.addBeforeComment(comments);
        }
        this.lexer.nextToken();
        stmt2.setLabel(this.lexer.stringVal());
        this.lexer.nextToken();
        this.accept(Token.TO);
        if (this.lexer.token() == Token.USER) {
            this.lexer.nextToken();
            final SQLName name = this.exprParser.name();
            stmt2.setUser(name);
            return stmt2;
        }
        this.accept(Token.TABLE);
        final SQLExpr expr = this.exprParser.name();
        stmt2.setTable(new SQLExprTableSource(expr));
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.names(stmt2.getColumns(), stmt2);
            this.accept(Token.RPAREN);
        }
        return stmt2;
    }
    
    @Override
    public OdpsGrantStmt parseGrant() {
        this.accept(Token.GRANT);
        final OdpsGrantStmt stmt = new OdpsGrantStmt();
        if (this.lexer.identifierEquals("LABEL")) {
            stmt.setLabel(true);
            this.lexer.nextToken();
            stmt.setLabel(this.exprParser.expr());
        }
        else {
            if (this.lexer.identifierEquals("SUPER")) {
                stmt.setSuper(true);
                this.lexer.nextToken();
            }
            this.parsePrivileages(stmt.getPrivileges(), stmt);
        }
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("PROJECT")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.PROJECT);
            }
            else if (this.lexer.identifierEquals("PACKAGE")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.PACKAGE);
            }
            else if (this.lexer.token() == Token.FUNCTION) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.FUNCTION);
            }
            else if (this.lexer.token() == Token.TABLE) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.TABLE);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprParser.names(stmt.getColumns(), stmt);
                    this.accept(Token.RPAREN);
                }
            }
            else if (this.lexer.identifierEquals("RESOURCE")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.RESOURCE);
            }
            else if (this.lexer.identifierEquals("INSTANCE")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.INSTANCE);
            }
            else if (this.lexer.identifierEquals("JOB")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.JOB);
            }
            else if (this.lexer.identifierEquals("VOLUME")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.VOLUME);
            }
            else if (this.lexer.identifierEquals("OfflineModel")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.OfflineModel);
            }
            else if (this.lexer.identifierEquals("XFLOW")) {
                this.lexer.nextToken();
                stmt.setResourceType(SQLObjectType.XFLOW);
            }
            stmt.setResource(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.TO) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.USER) {
                this.lexer.nextToken();
                stmt.setSubjectType(SQLObjectType.USER);
            }
            else if (this.lexer.identifierEquals("ROLE")) {
                this.lexer.nextToken();
                stmt.setSubjectType(SQLObjectType.ROLE);
            }
            stmt.getUsers().add(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("EXP");
            stmt.setExpire(this.exprParser.expr());
        }
        return stmt;
    }
    
    @Override
    protected void parsePrivileages(final List<SQLPrivilegeItem> privileges, final SQLObject parent) {
        while (true) {
            String privilege = null;
            if (this.lexer.token() == Token.ALL) {
                this.lexer.nextToken();
                privilege = "ALL";
            }
            else if (this.lexer.token() == Token.SELECT) {
                privilege = "SELECT";
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.UPDATE) {
                privilege = "UPDATE";
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.DELETE) {
                privilege = "DELETE";
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.INSERT) {
                privilege = "INSERT";
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.DROP) {
                this.lexer.nextToken();
                privilege = "DROP";
            }
            else if (this.lexer.token() == Token.ALTER) {
                this.lexer.nextToken();
                privilege = "ALTER";
            }
            else if (this.lexer.identifierEquals("DESCRIBE")) {
                privilege = "DESCRIBE";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("READ")) {
                privilege = "READ";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("WRITE")) {
                privilege = "WRITE";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("EXECUTE")) {
                this.lexer.nextToken();
                privilege = "EXECUTE";
            }
            else if (this.lexer.identifierEquals("LIST")) {
                this.lexer.nextToken();
                privilege = "LIST";
            }
            else if (this.lexer.identifierEquals("CreateTable")) {
                this.lexer.nextToken();
                privilege = "CreateTable";
            }
            else if (this.lexer.identifierEquals("CreateInstance")) {
                this.lexer.nextToken();
                privilege = "CreateInstance";
            }
            else if (this.lexer.identifierEquals("CreateFunction")) {
                this.lexer.nextToken();
                privilege = "CreateFunction";
            }
            else if (this.lexer.identifierEquals("CreateResource")) {
                this.lexer.nextToken();
                privilege = "CreateResource";
            }
            else if (this.lexer.identifierEquals("CreateJob")) {
                this.lexer.nextToken();
                privilege = "CreateJob";
            }
            else if (this.lexer.identifierEquals("CreateVolume")) {
                this.lexer.nextToken();
                privilege = "CreateVolume";
            }
            else if (this.lexer.identifierEquals("CreateOfflineModel")) {
                this.lexer.nextToken();
                privilege = "CreateOfflineModel";
            }
            else if (this.lexer.identifierEquals("CreateXflow")) {
                this.lexer.nextToken();
                privilege = "CreateXflow";
            }
            SQLExpr expr = null;
            if (privilege != null) {
                expr = new SQLIdentifierExpr(privilege);
            }
            else {
                expr = this.exprParser.expr();
            }
            final SQLPrivilegeItem privilegeItem = new SQLPrivilegeItem();
            privilegeItem.setAction(expr);
            if (this.lexer.token() == Token.LPAREN) {
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
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    @Override
    public SQLCreateFunctionStatement parseCreateFunction() {
        return this.parseHiveCreateFunction();
    }
    
    protected HiveLoadDataStatement parseLoad() {
        this.acceptIdentifier("LOAD");
        final HiveLoadDataStatement stmt = new HiveLoadDataStatement();
        if (this.lexer.token() == Token.OVERWRITE) {
            stmt.setOverwrite(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
        }
        this.accept(Token.TABLE);
        stmt.setInto(this.exprParser.expr());
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprParser.exprList(stmt.getPartition(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
            this.lexer.nextToken();
            stmt.setLocal(true);
        }
        this.accept(Token.FROM);
        this.acceptIdentifier("LOCATION");
        final SQLExpr inpath = this.exprParser.expr();
        stmt.setInpath(inpath);
        if (this.lexer.identifierEquals("STORED")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.BY) {
                this.lexer.nextToken();
                stmt.setStoredBy(this.exprParser.expr());
            }
            else {
                this.accept(Token.AS);
                stmt.setStoredAs(this.exprParser.expr());
            }
        }
        if (this.lexer.identifierEquals("ROW")) {
            this.lexer.nextToken();
            this.acceptIdentifier("FORMAT");
            this.acceptIdentifier("SERDE");
            stmt.setRowFormat(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("SERDEPROPERTIES");
            this.accept(Token.LPAREN);
            while (true) {
                final String name = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.primary();
                stmt.getSerdeProperties().put(name, value);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals("STORED")) {
            this.lexer.nextToken();
            this.accept(Token.AS);
            stmt.setStoredAs(this.exprParser.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
            this.lexer.nextToken();
            stmt.setUsing(this.exprParser.expr());
        }
        return stmt;
    }
}
