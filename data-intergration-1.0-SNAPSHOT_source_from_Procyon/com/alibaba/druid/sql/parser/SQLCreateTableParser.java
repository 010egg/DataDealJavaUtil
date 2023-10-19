// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleSelectParser;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.util.FnvHash;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class SQLCreateTableParser extends SQLDDLParser
{
    public SQLCreateTableParser(final String sql) {
        super(sql);
    }
    
    public SQLCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
        this.dbType = exprParser.dbType;
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        List<String> comments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            comments = this.lexer.readAndResetComments();
        }
        final SQLCreateTableStatement stmt = this.parseCreateTable(true);
        if (comments != null) {
            stmt.addBeforeComment(comments);
        }
        return stmt;
    }
    
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final SQLCreateTableStatement createTable = this.newCreateStatement();
        createTable.setDbType(this.getDbType());
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
        else if (this.lexer.token == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase("LOCAL")) {
            this.lexer.nextToken();
            if (this.lexer.token != Token.IDENTIFIER || !this.lexer.stringVal().equalsIgnoreCase("TEMPORAY")) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            this.lexer.nextToken();
            createTable.setType(SQLCreateTableStatement.Type.LOCAL_TEMPORARY);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DIMENSION)) {
            this.lexer.nextToken();
            createTable.setDimension(true);
        }
        this.accept(Token.TABLE);
        if (this.lexer.token() == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            createTable.setIfNotExiists(true);
        }
        createTable.setName(this.exprParser.name());
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            do {
                final Token token = this.lexer.token;
                if (this.lexer.identifierEquals(FnvHash.Constants.SUPPLEMENTAL) && DbType.oracle == this.dbType) {
                    final SQLTableElement element = this.parseCreateTableSupplementalLogingProps();
                    element.setParent(createTable);
                    createTable.getTableElementList().add(element);
                }
                else if (token == Token.IDENTIFIER || token == Token.LITERAL_ALIAS) {
                    final SQLColumnDefinition column = this.exprParser.parseColumn(createTable);
                    column.setParent(createTable);
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
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            } while (this.lexer.token != Token.RPAREN);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals(FnvHash.Constants.INHERITS)) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLName inherits = this.exprParser.name();
                createTable.setInherits(new SQLExprTableSource(inherits));
                this.accept(Token.RPAREN);
            }
        }
        if (this.lexer.token == Token.AS) {
            this.lexer.nextToken();
            SQLSelect select = null;
            if (DbType.oracle == this.dbType) {
                select = new OracleSelectParser(this.exprParser).select();
            }
            else {
                select = this.createSQLSelectParser().select();
            }
            createTable.setSelect(select);
        }
        if (this.lexer.token == Token.WITH && DbType.postgresql == this.dbType) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.parseAssignItems(createTable.getTableOptions(), createTable, false);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.TABLESPACE) {
            this.lexer.nextToken();
            createTable.setTablespace(this.exprParser.name());
        }
        if (this.lexer.token() == Token.PARTITION) {
            final SQLPartitionBy partitionClause = this.parsePartitionBy();
            createTable.setPartitioning(partitionClause);
        }
        this.parseCreateTableRest(createTable);
        return createTable;
    }
    
    protected void parseCreateTableRest(final SQLCreateTableStatement stmt) {
    }
    
    public SQLPartitionBy parsePartitionBy() {
        return null;
    }
    
    protected SQLTableElement parseCreateTableSupplementalLogingProps() {
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    @Override
    protected SQLCreateTableStatement newCreateStatement() {
        return new SQLCreateTableStatement(this.getDbType());
    }
}
