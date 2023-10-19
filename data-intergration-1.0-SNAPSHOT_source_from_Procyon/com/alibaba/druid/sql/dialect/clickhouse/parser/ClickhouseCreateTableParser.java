// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.parser;

import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.clickhouse.ast.ClickhouseCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class ClickhouseCreateTableParser extends SQLCreateTableParser
{
    public ClickhouseCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    protected SQLCreateTableStatement newCreateStatement() {
        return new ClickhouseCreateTableStatement();
    }
    
    @Override
    protected void parseCreateTableRest(final SQLCreateTableStatement stmt) {
        final ClickhouseCreateTableStatement ckStmt = (ClickhouseCreateTableStatement)stmt;
        if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            stmt.setEngine(this.exprParser.expr());
        }
        if (this.lexer.identifierEquals("PARTITION")) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            final SQLExpr expr = this.exprParser.expr();
            ckStmt.setPartitionBy(expr);
        }
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
            ckStmt.setOrderBy(orderBy);
        }
        if (this.lexer.identifierEquals("SAMPLE")) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            final SQLExpr expr = this.exprParser.expr();
            ckStmt.setSampleBy(expr);
        }
        if (this.lexer.identifierEquals("SETTINGS")) {
            this.lexer.nextToken();
            while (true) {
                final SQLAssignItem item = this.exprParser.parseAssignItem();
                item.setParent(ckStmt);
                ckStmt.getSettings().add(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
    }
}
