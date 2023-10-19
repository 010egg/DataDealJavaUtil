// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.blink.ast.BlinkCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class BlinkCreateTableParser extends SQLCreateTableParser
{
    public BlinkCreateTableParser(final String sql) {
        super(new BlinkExprParser(sql));
    }
    
    public BlinkCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final BlinkCreateTableStatement stmt = new BlinkCreateTableStatement();
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExternal(true);
        }
        this.accept(Token.TABLE);
        if (this.lexer.token() == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExiists(true);
        }
        stmt.setName(this.exprParser.name());
        this.accept(Token.LPAREN);
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            stmt.addBodyBeforeComment(this.lexer.readAndResetComments());
        }
    Label_0413:
        while (true) {
            SQLColumnDefinition column = null;
            switch (this.lexer.token()) {
                case IDENTIFIER:
                case KEY: {
                    column = this.exprParser.parseColumn();
                    column.setParent(stmt);
                    stmt.getTableElementList().add(column);
                    break;
                }
                case PRIMARY: {
                    final SQLTableConstraint constraint = this.parseConstraint();
                    constraint.setParent(stmt);
                    stmt.getTableElementList().add(constraint);
                    break;
                }
                case PERIOD: {
                    this.lexer.nextToken();
                    this.accept(Token.FOR);
                    final SQLExpr periodFor = this.exprParser.primary();
                    stmt.setPeriodFor(periodFor);
                    break Label_0413;
                }
                default: {
                    throw new ParserException("expect identifier. " + this.lexer.info());
                }
            }
            if (this.lexer.isKeepComments() && this.lexer.hasComment() && column != null) {
                column.addAfterComment(this.lexer.readAndResetComments());
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
            if (!this.lexer.isKeepComments() || !this.lexer.hasComment() || column == null) {
                continue;
            }
            column.addAfterComment(this.lexer.readAndResetComments());
        }
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            stmt.setComment(this.exprParser.primary());
        }
        if (stmt.getClusteredBy().size() > 0 || stmt.getSortedBy().size() > 0) {
            this.accept(Token.INTO);
            if (this.lexer.token() != Token.LITERAL_INT) {
                throw new ParserException("into buckets must be integer. " + this.lexer.info());
            }
            stmt.setBuckets(this.lexer.integerValue().intValue());
            this.lexer.nextToken();
            this.acceptIdentifier("BUCKETS");
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.parseAssignItems(stmt.getTableOptions(), stmt, true);
            this.accept(Token.RPAREN);
        }
        return stmt;
    }
}
