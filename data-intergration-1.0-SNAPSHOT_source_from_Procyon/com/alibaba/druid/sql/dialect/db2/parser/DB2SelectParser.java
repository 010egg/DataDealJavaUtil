// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.parser;

import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class DB2SelectParser extends SQLSelectParser
{
    public DB2SelectParser(final SQLExprParser exprParser) {
        super(exprParser);
        this.dbType = DbType.db2;
    }
    
    public DB2SelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
        this.dbType = DbType.db2;
    }
    
    public DB2SelectParser(final String sql) {
        this(new DB2ExprParser(sql));
    }
    
    protected SQLExprParser createExprParser() {
        return new DB2ExprParser(this.lexer);
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        this.accept(Token.SELECT);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
        }
        final DB2SelectQueryBlock queryBlock = new DB2SelectQueryBlock();
        if (this.lexer.token() == Token.DISTINCT) {
            queryBlock.setDistionOption(2);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.UNIQUE) {
            queryBlock.setDistionOption(3);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.ALL) {
            queryBlock.setDistionOption(1);
            this.lexer.nextToken();
        }
        this.parseSelectList(queryBlock);
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            if (this.lexer.token() != Token.COMMA) {
                queryBlock.setInto(expr);
            }
        }
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseHierachical(queryBlock);
        this.parseGroupBy(queryBlock);
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.parseOrderBy();
            queryBlock.setOrderBy(orderBy);
        }
        while (true) {
            if (this.lexer.token() == Token.FETCH) {
                this.lexer.nextToken();
                this.accept(Token.FIRST);
                final SQLExpr first = this.exprParser.primary();
                queryBlock.setFirst(first);
                if (this.lexer.identifierEquals("ROW") || this.lexer.identifierEquals("ROWS")) {
                    this.lexer.nextToken();
                }
                this.accept(Token.ONLY);
            }
            else {
                if (this.lexer.token() != Token.WITH) {
                    if (this.lexer.token() == Token.FOR) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.UPDATE) {
                            queryBlock.setForUpdate(true);
                            this.lexer.nextToken();
                        }
                        else {
                            this.acceptIdentifier("READ");
                            this.accept(Token.ONLY);
                            queryBlock.setForReadOnly(true);
                        }
                    }
                    if (this.lexer.token() == Token.OPTIMIZE) {
                        this.lexer.nextToken();
                        this.accept(Token.FOR);
                        queryBlock.setOptimizeFor(this.expr());
                        if (this.lexer.identifierEquals("ROW")) {
                            this.lexer.nextToken();
                        }
                        else {
                            this.acceptIdentifier("ROWS");
                        }
                    }
                    return this.queryRest(queryBlock, acceptUnion);
                }
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("RR")) {
                    queryBlock.setIsolation(DB2SelectQueryBlock.Isolation.RR);
                }
                else if (this.lexer.identifierEquals("RS")) {
                    queryBlock.setIsolation(DB2SelectQueryBlock.Isolation.RS);
                }
                else if (this.lexer.identifierEquals("CS")) {
                    queryBlock.setIsolation(DB2SelectQueryBlock.Isolation.CS);
                }
                else {
                    if (!this.lexer.identifierEquals("UR")) {
                        throw new ParserException("TODO. " + this.lexer.info());
                    }
                    queryBlock.setIsolation(DB2SelectQueryBlock.Isolation.UR);
                }
                this.lexer.nextToken();
            }
        }
    }
}
