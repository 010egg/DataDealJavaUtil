// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class DB2CreateTableParser extends SQLCreateTableParser
{
    public DB2CreateTableParser(final String sql) {
        super(new DB2ExprParser(sql));
    }
    
    public DB2CreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final DB2CreateTableStatement createTable = this.newCreateStatement();
        if (acceptCreate) {
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                createTable.addBeforeComment(this.lexer.readAndResetComments());
            }
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals("GLOBAL")) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals("TEMPORARY")) {
                throw new ParserException("syntax error " + this.lexer.info());
            }
            this.lexer.nextToken();
            createTable.setType(SQLCreateTableStatement.Type.GLOBAL_TEMPORARY);
        }
        else if (this.lexer.token() == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase("LOCAL")) {
            this.lexer.nextToken();
            if (this.lexer.token() != Token.IDENTIFIER || !this.lexer.stringVal().equalsIgnoreCase("TEMPORAY")) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            this.lexer.nextToken();
            createTable.setType(SQLCreateTableStatement.Type.LOCAL_TEMPORARY);
        }
        this.accept(Token.TABLE);
        createTable.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            do {
                final Token token = this.lexer.token();
                if (token == Token.IDENTIFIER || token == Token.LITERAL_ALIAS) {
                    final SQLColumnDefinition column = this.exprParser.parseColumn();
                    createTable.getTableElementList().add(column);
                }
                else if (token == Token.PRIMARY || token == Token.UNIQUE || token == Token.CHECK || token == Token.CONSTRAINT || token == Token.FOREIGN) {
                    final SQLConstraint constraint = this.exprParser.parseConstaint();
                    constraint.setParent(createTable);
                    createTable.getTableElementList().add((SQLTableElement)constraint);
                }
                else {
                    if (token == Token.TABLESPACE) {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                    final SQLColumnDefinition column = this.exprParser.parseColumn();
                    createTable.getTableElementList().add(column);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            } while (this.lexer.token() != Token.RPAREN);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals("INHERITS")) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLName inherits = this.exprParser.name();
                createTable.setInherits(new SQLExprTableSource(inherits));
                this.accept(Token.RPAREN);
            }
        }
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            final SQLSelect select = this.createSQLSelectParser().select();
            createTable.setSelect(select);
        }
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                this.lexer.nextToken();
                this.acceptIdentifier("CAPTURE");
                if (!this.lexer.identifierEquals(FnvHash.Constants.NONE)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                createTable.setDataCaptureNone(true);
            }
            else if (this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.DATABASE) {
                    this.lexer.nextToken();
                    final SQLName database = this.exprParser.name();
                    createTable.setDatabase(database);
                }
                else {
                    if (this.lexer.identifierEquals("tablespace")) {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                    final SQLName tablespace = this.exprParser.name();
                    createTable.setTablespace(tablespace);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONING)) {
                final SQLPartitionByHash partitionBy = new SQLPartitionByHash();
                this.lexer.nextToken();
                this.accept(Token.KEY);
                this.accept(Token.LPAREN);
                this.exprParser.exprList(partitionBy.getColumns(), partitionBy);
                this.accept(Token.RPAREN);
                this.accept(Token.USING);
                this.acceptIdentifier("HASHING");
                createTable.setPartitioning(partitionBy);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.VALIDPROC)) {
                this.lexer.nextToken();
                final SQLName validproc = this.exprParser.name();
                createTable.setValidproc(validproc);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.COMPRESS)) {
                this.lexer.nextToken();
                createTable.setCompress(true);
                if (!this.lexer.identifierEquals(FnvHash.Constants.YES)) {
                    continue;
                }
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token() != Token.INDEX) {
                    return createTable;
                }
                this.lexer.nextToken();
                this.accept(Token.IN);
                final SQLName indexIn = this.exprParser.name();
                createTable.setIndexIn(indexIn);
            }
        }
    }
    
    @Override
    protected DB2CreateTableStatement newCreateStatement() {
        return new DB2CreateTableStatement();
    }
}
