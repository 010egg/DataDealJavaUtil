// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.parser;

import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerWaitForStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import com.alibaba.druid.sql.ast.statement.SQLDeclareStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLScriptCommitStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class SQLServerStatementParser extends SQLStatementParser
{
    public SQLServerStatementParser(final String sql) {
        super(new SQLServerExprParser(sql));
    }
    
    public SQLServerStatementParser(final String sql, final SQLParserFeature... features) {
        super(new SQLServerExprParser(sql, features));
    }
    
    @Override
    public SQLSelectParser createSQLSelectParser() {
        return new SQLServerSelectParser(this.exprParser, this.selectListCache);
    }
    
    public SQLServerStatementParser(final Lexer lexer) {
        super(new SQLServerExprParser(lexer));
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        if (this.lexer.token() == Token.WITH) {
            final SQLStatement stmt = this.parseSelect();
            statementList.add(stmt);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXEC) || this.lexer.identifierEquals(FnvHash.Constants.EXECUTE)) {
            this.lexer.nextToken();
            final SQLServerExecStatement execStmt = new SQLServerExecStatement();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.parseExecParameter(execStmt.getParameters(), execStmt);
                this.accept(Token.RPAREN);
            }
            else {
                final SQLName sqlNameName = this.exprParser.name();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                    execStmt.setReturnStatus(sqlNameName);
                    execStmt.setModuleName(this.exprParser.name());
                }
                else {
                    execStmt.setModuleName(sqlNameName);
                }
                if (this.lexer.token() != Token.SEMI && this.lexer.token() != Token.EOF) {
                    this.parseExecParameter(execStmt.getParameters(), execStmt);
                }
            }
            statementList.add(execStmt);
            return true;
        }
        if (this.lexer.token() == Token.DECLARE) {
            statementList.add(this.parseDeclare());
            return true;
        }
        if (this.lexer.token() == Token.IF) {
            statementList.add(this.parseIf());
            return true;
        }
        if (this.lexer.token() == Token.BEGIN) {
            statementList.add(this.parseBlock());
            return true;
        }
        if (this.lexer.token() == Token.COMMIT) {
            statementList.add(this.parseCommit());
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WAITFOR)) {
            statementList.add(this.parseWaitFor());
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.GO)) {
            this.lexer.nextToken();
            final SQLStatement stmt = new SQLScriptCommitStatement();
            statementList.add(stmt);
            return true;
        }
        return false;
    }
    
    public void parseExecParameter(final Collection<SQLServerExecStatement.SQLServerParameter> exprCol, final SQLObject parent) {
        if (this.lexer.token() == Token.RPAREN || this.lexer.token() == Token.RBRACKET) {
            return;
        }
        if (this.lexer.token() == Token.EOF) {
            return;
        }
        SQLServerExecStatement.SQLServerParameter param = new SQLServerExecStatement.SQLServerParameter();
        SQLExpr expr = this.exprParser.expr();
        expr.setParent(parent);
        param.setExpr(expr);
        if (this.lexer.token() == Token.OUT) {
            param.setType(true);
            this.accept(Token.OUT);
        }
        exprCol.add(param);
        while (this.lexer.token() == Token.COMMA) {
            this.lexer.nextToken();
            param = new SQLServerExecStatement.SQLServerParameter();
            expr = this.exprParser.expr();
            expr.setParent(parent);
            param.setExpr(expr);
            if (this.lexer.token() == Token.OUT) {
                param.setType(true);
                this.accept(Token.OUT);
            }
            exprCol.add(param);
        }
    }
    
    @Override
    public SQLStatement parseDeclare() {
        this.accept(Token.DECLARE);
        final SQLDeclareStatement declareStatement = new SQLDeclareStatement();
        while (true) {
            final SQLDeclareItem item = new SQLDeclareItem();
            declareStatement.addItem(item);
            item.setName(this.exprParser.name());
            if (this.lexer.token() == Token.AS) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.TABLE) {
                this.lexer.nextToken();
                item.setType(SQLDeclareItem.Type.TABLE);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    do {
                        if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_ALIAS) {
                            final SQLColumnDefinition column = this.exprParser.parseColumn();
                            item.getTableElementList().add(column);
                        }
                        else if (this.lexer.token() == Token.PRIMARY || this.lexer.token() == Token.UNIQUE || this.lexer.token() == Token.CHECK || this.lexer.token() == Token.CONSTRAINT) {
                            final SQLConstraint constraint = this.exprParser.parseConstaint();
                            constraint.setParent(item);
                            item.getTableElementList().add((SQLTableElement)constraint);
                        }
                        else {
                            if (this.lexer.token() == Token.TABLESPACE) {
                                throw new ParserException("TODO " + this.lexer.info());
                            }
                            final SQLColumnDefinition column = this.exprParser.parseColumn();
                            item.getTableElementList().add(column);
                        }
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    } while (this.lexer.token() != Token.RPAREN);
                    this.accept(Token.RPAREN);
                    break;
                }
                break;
            }
            else {
                if (this.lexer.token() == Token.CURSOR) {
                    item.setType(SQLDeclareItem.Type.CURSOR);
                    this.lexer.nextToken();
                }
                else {
                    item.setType(SQLDeclareItem.Type.LOCAL);
                    item.setDataType(this.exprParser.parseDataType());
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                        item.setValue(this.exprParser.expr());
                    }
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return declareStatement;
    }
    
    @Override
    public SQLStatement parseInsert() {
        final SQLServerInsertStatement insertStatement = new SQLServerInsertStatement();
        if (this.lexer.token() == Token.INSERT) {
            this.accept(Token.INSERT);
        }
        this.parseInsert0(insertStatement);
        return insertStatement;
    }
    
    @Override
    protected void parseInsert0(final SQLInsertInto insert, final boolean acceptSubQuery) {
        final SQLServerInsertStatement insertStatement = (SQLServerInsertStatement)insert;
        final SQLServerTop top = this.getExprParser().parseTop();
        if (top != null) {
            insertStatement.setTop(top);
        }
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
        }
        final SQLName tableName = this.exprParser.name();
        insertStatement.setTableName(tableName);
        if (this.lexer.token() == Token.LITERAL_ALIAS) {
            insertStatement.setAlias(this.tableAlias());
        }
        this.parseInsert0_hinits(insertStatement);
        if (this.lexer.token() == Token.IDENTIFIER && !this.lexer.stringVal().equalsIgnoreCase("OUTPUT")) {
            insertStatement.setAlias(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(insertStatement.getColumns(), insertStatement);
            this.accept(Token.RPAREN);
        }
        final SQLServerOutput output = this.getExprParser().parserOutput();
        if (output != null) {
            insertStatement.setOutput(output);
        }
        if (this.lexer.token() == Token.VALUES) {
            this.lexer.nextToken();
            while (true) {
                this.accept(Token.LPAREN);
                final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
                this.exprParser.exprList(values.getValues(), values);
                insertStatement.addValueCause(values);
                this.accept(Token.RPAREN);
                if (!this.parseCompleteValues && insertStatement.getValuesList().size() >= this.parseValuesSize) {
                    this.lexer.skipToEOF();
                    break;
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (acceptSubQuery && (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.LPAREN)) {
            final SQLQueryExpr queryExpr = (SQLQueryExpr)this.exprParser.expr();
            insertStatement.setQuery(queryExpr.getSubQuery());
        }
        else if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
            this.accept(Token.VALUES);
            insertStatement.setDefaultValues(true);
        }
    }
    
    @Override
    protected SQLServerUpdateStatement createUpdateStatement() {
        return new SQLServerUpdateStatement();
    }
    
    @Override
    public SQLUpdateStatement parseUpdateStatement() {
        final SQLServerUpdateStatement udpateStatement = this.createUpdateStatement();
        this.accept(Token.UPDATE);
        final SQLServerTop top = this.getExprParser().parseTop();
        if (top != null) {
            udpateStatement.setTop(top);
        }
        final SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
        udpateStatement.setTableSource(tableSource);
        this.parseUpdateSet(udpateStatement);
        final SQLServerOutput output = this.getExprParser().parserOutput();
        if (output != null) {
            udpateStatement.setOutput(output);
        }
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            final SQLTableSource from = this.exprParser.createSelectParser().parseTableSource();
            udpateStatement.setFrom(from);
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            udpateStatement.setWhere(this.exprParser.expr());
        }
        return udpateStatement;
    }
    
    @Override
    public SQLServerExprParser getExprParser() {
        return (SQLServerExprParser)this.exprParser;
    }
    
    @Override
    public SQLStatement parseSet() {
        this.accept(Token.SET);
        if (this.lexer.identifierEquals(FnvHash.Constants.TRANSACTION)) {
            this.lexer.nextToken();
            this.acceptIdentifier("ISOLATION");
            this.acceptIdentifier("LEVEL");
            final SQLServerSetTransactionIsolationLevelStatement stmt = new SQLServerSetTransactionIsolationLevelStatement();
            if (this.lexer.identifierEquals("READ")) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("UNCOMMITTED")) {
                    stmt.setLevel("READ UNCOMMITTED");
                    this.lexer.nextToken();
                }
                else {
                    if (!this.lexer.identifierEquals("COMMITTED")) {
                        throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal() + ", " + this.lexer.info());
                    }
                    stmt.setLevel("READ COMMITTED");
                    this.lexer.nextToken();
                }
            }
            else if (this.lexer.identifierEquals("SERIALIZABLE")) {
                stmt.setLevel("SERIALIZABLE");
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("SNAPSHOT")) {
                stmt.setLevel("SNAPSHOT");
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
                stmt.setLevel("REPEATABLE READ");
                this.lexer.nextToken();
            }
            return stmt;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STATISTICS)) {
            this.lexer.nextToken();
            final SQLSetStatement stmt2 = new SQLSetStatement();
            if (this.lexer.identifierEquals("IO") || this.lexer.identifierEquals("XML") || this.lexer.identifierEquals("PROFILE") || this.lexer.identifierEquals("TIME")) {
                final SQLExpr target = new SQLIdentifierExpr("STATISTICS " + this.lexer.stringVal().toUpperCase());
                this.lexer.nextToken();
                if (this.lexer.token() == Token.ON) {
                    stmt2.set(target, new SQLIdentifierExpr("ON"));
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.OFF)) {
                    stmt2.set(target, new SQLIdentifierExpr("OFF"));
                    this.lexer.nextToken();
                }
            }
            return stmt2;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IDENTITY_INSERT)) {
            final SQLSetStatement stmt2 = new SQLSetStatement();
            stmt2.setOption(SQLSetStatement.Option.IDENTITY_INSERT);
            this.lexer.nextToken();
            final SQLName table = this.exprParser.name();
            if (this.lexer.token() == Token.ON) {
                final SQLExpr value = new SQLIdentifierExpr("ON");
                stmt2.set(table, value);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.OFF)) {
                final SQLExpr value = new SQLIdentifierExpr("OFF");
                stmt2.set(table, value);
                this.lexer.nextToken();
            }
            return stmt2;
        }
        if (this.lexer.token() == Token.VARIANT) {
            final SQLSetStatement stmt2 = new SQLSetStatement(this.getDbType());
            this.parseAssignItems(stmt2.getItems(), stmt2);
            return stmt2;
        }
        final SQLSetStatement stmt2 = new SQLSetStatement();
        final SQLExpr target = this.exprParser.expr();
        if (this.lexer.token() == Token.ON) {
            stmt2.set(target, new SQLIdentifierExpr("ON"));
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals("OFF")) {
            stmt2.set(target, new SQLIdentifierExpr("OFF"));
            this.lexer.nextToken();
        }
        else {
            stmt2.set(target, this.exprParser.expr());
        }
        return stmt2;
    }
    
    @Override
    public SQLIfStatement parseIf() {
        this.accept(Token.IF);
        final SQLIfStatement stmt = new SQLIfStatement();
        stmt.setCondition(this.exprParser.expr());
        this.parseStatementList(stmt.getStatements(), 1, stmt);
        if (this.lexer.token() == Token.SEMI) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            final SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
            this.parseStatementList(elseItem.getStatements(), 1, elseItem);
            stmt.setElseItem(elseItem);
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseBlock() {
        this.accept(Token.BEGIN);
        if (this.lexer.identifierEquals("TRANSACTION") || this.lexer.identifierEquals("TRAN")) {
            this.lexer.nextToken();
            final SQLStartTransactionStatement startTrans = new SQLStartTransactionStatement(this.dbType);
            if (this.lexer.token() == Token.IDENTIFIER) {
                final SQLName name = this.exprParser.name();
                startTrans.setName(name);
            }
            return startTrans;
        }
        final SQLBlockStatement block = new SQLBlockStatement();
        this.parseStatementList(block.getStatementList());
        this.accept(Token.END);
        return block;
    }
    
    @Override
    public SQLStatement parseCommit() {
        this.acceptIdentifier("COMMIT");
        final SQLCommitStatement stmt = new SQLCommitStatement();
        if (this.lexer.identifierEquals("WORK")) {
            this.lexer.nextToken();
            stmt.setWork(true);
        }
        if (this.lexer.identifierEquals("TRAN") || this.lexer.identifierEquals("TRANSACTION")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.VARIANT) {
                stmt.setTransactionName(this.exprParser.expr());
            }
            if (this.lexer.token() == Token.WITH) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                this.acceptIdentifier("DELAYED_DURABILITY");
                this.accept(Token.EQ);
                stmt.setDelayedDurability(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
        }
        return stmt;
    }
    
    @Override
    public SQLServerRollbackStatement parseRollback() {
        this.acceptIdentifier("ROLLBACK");
        final SQLServerRollbackStatement stmt = new SQLServerRollbackStatement();
        if (this.lexer.identifierEquals("WORK")) {
            this.lexer.nextToken();
            stmt.setWork(true);
        }
        if (this.lexer.identifierEquals("TRAN") || this.lexer.identifierEquals("TRANSACTION")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.VARIANT) {
                stmt.setName(this.exprParser.expr());
            }
        }
        return stmt;
    }
    
    public SQLServerWaitForStatement parseWaitFor() {
        this.acceptIdentifier("WAITFOR");
        final SQLServerWaitForStatement stmt = new SQLServerWaitForStatement();
        if (this.lexer.identifierEquals("DELAY")) {
            this.lexer.nextToken();
            stmt.setDelay(this.exprParser.expr());
        }
        if (this.lexer.identifierEquals("TIME")) {
            this.lexer.nextToken();
            stmt.setTime(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.COMMA) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("TIMEOUT")) {
                this.lexer.nextToken();
                stmt.setTimeout(this.exprParser.expr());
            }
        }
        return stmt;
    }
}
