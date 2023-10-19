// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.antspark.ast.AntsparkCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class AntsparkCreateTableParser extends SQLCreateTableParser
{
    public AntsparkCreateTableParser(final String sql) {
        super(new AntsparkExprParser(sql));
    }
    
    public AntsparkCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final AntsparkCreateTableStatement stmt = new AntsparkCreateTableStatement();
        if (acceptCreate) {
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                stmt.addBeforeComment(this.lexer.readAndResetComments());
            }
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExternal(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TEMPORARY)) {
            this.lexer.nextToken();
            stmt.setType(SQLCreateTableStatement.Type.TEMPORARY);
        }
        this.accept(Token.TABLE);
        if (this.lexer.token() == Token.IF || this.lexer.identifierEquals(FnvHash.Constants.IF)) {
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
                    final SQLColumnDefinition column = this.exprParser.parseColumn();
                    stmt.getTableElementList().add(column);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            } while (this.lexer.token() != Token.RPAREN);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.USING) {
            this.lexer.nextToken();
            final SQLName expr = this.exprParser.name();
            stmt.setDatasource(expr);
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLExpr comment = this.exprParser.expr();
            stmt.setComment(comment);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.MAPPED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            this.exprParser.parseAssignItem(stmt.getMappedBy(), stmt);
        }
        Label_0705: {
            if (this.lexer.token() == Token.PARTITIONED) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                this.accept(Token.LPAREN);
                while (this.lexer.token() == Token.IDENTIFIER) {
                    final SQLColumnDefinition column2 = this.exprParser.parseColumn();
                    stmt.addPartitionColumn(column2);
                    if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                        column2.addAfterComment(this.lexer.readAndResetComments());
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        this.accept(Token.RPAREN);
                        break Label_0705;
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
        if (this.lexer.identifierEquals(FnvHash.Constants.SKEWED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            this.accept(Token.LPAREN);
            this.exprParser.exprList(stmt.getSkewedBy(), stmt);
            this.accept(Token.RPAREN);
            this.accept(Token.ON);
            this.accept(Token.LPAREN);
            while (true) {
                if (this.lexer.token() == Token.LPAREN) {
                    final SQLListExpr list = new SQLListExpr();
                    this.lexer.nextToken();
                    this.exprParser.exprList(list.getItems(), list);
                    this.accept(Token.RPAREN);
                    stmt.addSkewedByOn(list);
                }
                else {
                    final SQLExpr expr2 = this.exprParser.expr();
                    stmt.addSkewedByOn(expr2);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SORTED)) {
            this.parseSortedBy(stmt);
        }
        if (this.lexer.token() == Token.ROW || this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
            this.parseRowFormat(stmt);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SORTED)) {
            this.parseSortedBy(stmt);
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
        if (this.lexer.token() == Token.ROW || this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
            this.parseRowFormat(stmt);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
            this.lexer.nextToken();
            this.accept(Token.AS);
            if (this.lexer.identifierEquals(FnvHash.Constants.INPUTFORMAT)) {
                final HiveInputOutputFormat format = new HiveInputOutputFormat();
                this.lexer.nextToken();
                format.setInput(this.exprParser.primary());
                if (this.lexer.identifierEquals(FnvHash.Constants.OUTPUTFORMAT)) {
                    this.lexer.nextToken();
                    format.setOutput(this.exprParser.primary());
                }
                stmt.setStoredAs(format);
            }
            else {
                final SQLName name = this.exprParser.name();
                stmt.setStoredAs(name);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
            this.lexer.nextToken();
            final SQLExpr location = this.exprParser.expr();
            stmt.setLocation(location);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            do {
                final String name2 = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.EQ);
                final SQLExpr value = this.exprParser.primary();
                stmt.addOption(name2, value);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            } while (this.lexer.token() != Token.RPAREN);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.META)) {
            this.lexer.nextToken();
            this.acceptIdentifier("LIFECYCLE");
            stmt.setMetaLifeCycle(this.exprParser.primary());
        }
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            final SQLSelect select = this.createSQLSelectParser().select();
            stmt.setSelect(select);
        }
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            final SQLName name = this.exprParser.name();
            stmt.setLike(name);
        }
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            final SQLExpr comment = this.exprParser.expr();
            stmt.setComment(comment);
        }
        return stmt;
    }
    
    private void parseRowFormat(final AntsparkCreateTableStatement stmt) {
    }
    
    private void parseSortedBy(final AntsparkCreateTableStatement stmt) {
    }
}
