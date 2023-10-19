// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.parser;

import com.alibaba.druid.sql.ast.statement.SQLExprHint;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class SQLServerSelectParser extends SQLSelectParser
{
    public SQLServerSelectParser(final String sql) {
        super(new SQLServerExprParser(sql));
    }
    
    public SQLServerSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public SQLServerSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    @Override
    public SQLSelect select() {
        final SQLSelect select = new SQLSelect();
        if (this.lexer.token() == Token.WITH) {
            final SQLWithSubqueryClause with = this.parseWith();
            select.setWithSubQuery(with);
        }
        select.setQuery(this.query());
        select.setOrderBy(this.parseOrderBy());
        if (select.getOrderBy() == null) {
            select.setOrderBy(this.parseOrderBy());
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("BROWSE")) {
                this.lexer.nextToken();
                select.setForBrowse(true);
            }
            else {
                if (!this.lexer.identifierEquals("XML")) {
                    throw new ParserException("syntax error, not support option : " + this.lexer.token() + ", " + this.lexer.info());
                }
                this.lexer.nextToken();
                while (true) {
                    if (this.lexer.identifierEquals("AUTO") || this.lexer.identifierEquals("TYPE") || this.lexer.identifierEquals("XMLSCHEMA")) {
                        select.getForXmlOptions().add(this.lexer.stringVal());
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.identifierEquals("ELEMENTS")) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("XSINIL")) {
                            this.lexer.nextToken();
                            select.getForXmlOptions().add("ELEMENTS XSINIL");
                        }
                        else {
                            select.getForXmlOptions().add("ELEMENTS");
                        }
                    }
                    else {
                        if (!this.lexer.identifierEquals("PATH")) {
                            break;
                        }
                        final SQLExpr xmlPath = this.exprParser.expr();
                        select.setXmlPath(xmlPath);
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
        }
        if (this.lexer.identifierEquals("OFFSET")) {
            this.lexer.nextToken();
            final SQLExpr offset = this.expr();
            this.acceptIdentifier("ROWS");
            select.setOffset(offset);
            if (this.lexer.token() == Token.FETCH) {
                this.lexer.nextToken();
                this.acceptIdentifier("NEXT");
                final SQLExpr rowCount = this.expr();
                this.acceptIdentifier("ROWS");
                this.acceptIdentifier("ONLY");
                select.setRowCount(rowCount);
            }
        }
        return select;
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        final SQLServerSelectQueryBlock queryBlock = new SQLServerSelectQueryBlock();
        if (this.lexer.token() == Token.SELECT) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.DISTINCT) {
                queryBlock.setDistionOption(2);
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.ALL) {
                queryBlock.setDistionOption(1);
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.TOP) {
                final SQLServerTop top = this.createExprParser().parseTop();
                queryBlock.setTop(top);
            }
            this.parseSelectList(queryBlock);
        }
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
            final SQLTableSource into = this.parseTableSource();
            queryBlock.setInto((SQLExprTableSource)into);
        }
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseGroupBy(queryBlock);
        queryBlock.setOrderBy(this.exprParser.parseOrderBy());
        this.parseFetchClause(queryBlock);
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    protected SQLServerExprParser createExprParser() {
        return new SQLServerExprParser(this.lexer);
    }
    
    @Override
    public SQLTableSource parseTableSourceRest(final SQLTableSource tableSource) {
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final SQLExpr expr = this.expr();
                final SQLExprHint hint = new SQLExprHint(expr);
                hint.setParent(tableSource);
                tableSource.getHints().add(hint);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return super.parseTableSourceRest(tableSource);
    }
}
