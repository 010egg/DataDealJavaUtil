// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class AdsCreateTableParser extends SQLCreateTableParser
{
    public AdsCreateTableParser(final String sql) {
        super(sql);
    }
    
    public AdsCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final SQLCreateTableStatement stmt = this.newCreateStatement();
        if (acceptCreate) {
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                stmt.addBeforeComment(this.lexer.readAndResetComments());
            }
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DIMENSION)) {
            this.lexer.nextToken();
            stmt.setDimension(true);
        }
        this.accept(Token.TABLE);
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExiists(true);
        }
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            do {
                final Token token = this.lexer.token();
                if (token == Token.IDENTIFIER || token == Token.LITERAL_ALIAS) {
                    final SQLColumnDefinition column = this.exprParser.parseColumn();
                    stmt.getTableElementList().add(column);
                }
                else if (token == Token.PRIMARY || token == Token.UNIQUE || token == Token.CHECK || token == Token.CONSTRAINT || token == Token.FOREIGN) {
                    final SQLConstraint constraint = this.exprParser.parseConstaint();
                    constraint.setParent(stmt);
                    stmt.getTableElementList().add((SQLTableElement)constraint);
                }
                else {
                    if (token == Token.TABLESPACE) {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                    if (this.lexer.token() == Token.INDEX) {
                        this.lexer.nextToken();
                        this.accept(Token.IDENTIFIER);
                        this.accept(Token.IDENTIFIER);
                        this.accept(Token.LPAREN);
                        this.accept(Token.IDENTIFIER);
                        while (this.lexer.token() == Token.COMMA) {
                            this.accept(Token.IDENTIFIER);
                        }
                        this.accept(Token.RPAREN);
                    }
                    else {
                        final SQLColumnDefinition column = this.exprParser.parseColumn();
                        stmt.getTableElementList().add(column);
                    }
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            } while (this.lexer.token() != Token.RPAREN);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            final SQLSelect select = this.createSQLSelectParser().select();
            stmt.setSelect(select);
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLExpr comment = this.exprParser.expr();
            stmt.setComment(comment);
        }
        Label_0735: {
            if (this.lexer.identifierEquals("PARTITION")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                this.acceptIdentifier("HASH");
                this.accept(Token.KEY);
                this.accept(Token.LPAREN);
                while (this.lexer.token() == Token.IDENTIFIER) {
                    final SQLColumnDefinition column2 = this.exprParser.parseColumn();
                    stmt.addPartitionColumn(column2);
                    if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                        column2.addAfterComment(this.lexer.readAndResetComments());
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        this.accept(Token.RPAREN);
                        this.acceptIdentifier("PARTITION");
                        this.acceptIdentifier("NUM");
                        this.accept(Token.LITERAL_INT);
                        break Label_0735;
                    }
                    this.lexer.nextToken();
                    if (!this.lexer.isKeepComments() || !this.lexer.hasComment()) {
                        continue;
                    }
                    column2.addAfterComment(this.lexer.readAndResetComments());
                }
                throw new ParserException("expect identifier. " + this.lexer.info());
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            this.accept(Token.LPAREN);
            while (true) {
                final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
                stmt.addClusteredByItem(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUP)) {
            this.lexer.nextToken();
            this.accept(Token.IDENTIFIER);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.OPTIONS)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final String name = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.primary();
                stmt.addOption(name, value);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            this.accept(Token.LITERAL_CHARS);
        }
        return stmt;
    }
}
