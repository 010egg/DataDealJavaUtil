// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.parser;

import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGFunctionTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class PGSelectParser extends SQLSelectParser
{
    public PGSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public PGSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    public PGSelectParser(final String sql) {
        this(new PGExprParser(sql));
    }
    
    protected SQLExprParser createExprParser() {
        return new PGExprParser(this.lexer);
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.VALUES) {
            return this.valuesQuery(acceptUnion);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            if (select instanceof SQLSelectQueryBlock) {
                ((SQLSelectQueryBlock)select).setParenthesized(true);
            }
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        final PGSelectQueryBlock queryBlock = new PGSelectQueryBlock();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            queryBlock.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (this.lexer.token() == Token.SELECT) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.DISTINCT) {
                queryBlock.setDistionOption(2);
                this.lexer.nextToken();
                if (this.lexer.token() == Token.ON) {
                    this.lexer.nextToken();
                    while (true) {
                        final SQLExpr expr = this.createExprParser().expr();
                        queryBlock.getDistinctOn().add(expr);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                }
            }
            else if (this.lexer.token() == Token.ALL) {
                queryBlock.setDistionOption(1);
                this.lexer.nextToken();
            }
            this.parseSelectList(queryBlock);
            if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.TEMPORARY) {
                    this.lexer.nextToken();
                    queryBlock.setIntoOption(PGSelectQueryBlock.IntoOption.TEMPORARY);
                }
                else if (this.lexer.token() == Token.TEMP) {
                    this.lexer.nextToken();
                    queryBlock.setIntoOption(PGSelectQueryBlock.IntoOption.TEMP);
                }
                else if (this.lexer.token() == Token.UNLOGGED) {
                    this.lexer.nextToken();
                    queryBlock.setIntoOption(PGSelectQueryBlock.IntoOption.UNLOGGED);
                }
                if (this.lexer.token() == Token.TABLE) {
                    this.lexer.nextToken();
                }
                final SQLExpr name = this.createExprParser().name();
                queryBlock.setInto(new SQLExprTableSource(name));
            }
        }
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseGroupBy(queryBlock);
        if (this.lexer.token() == Token.WINDOW) {
            this.parseWindow(queryBlock);
        }
        queryBlock.setOrderBy(this.createExprParser().parseOrderBy());
        while (true) {
            if (this.lexer.token() == Token.LIMIT) {
                final SQLLimit limit = new SQLLimit();
                this.lexer.nextToken();
                if (this.lexer.token() == Token.ALL) {
                    limit.setRowCount(new SQLIdentifierExpr("ALL"));
                    this.lexer.nextToken();
                }
                else {
                    limit.setRowCount(this.expr());
                }
                queryBlock.setLimit(limit);
            }
            else {
                if (this.lexer.token() != Token.OFFSET) {
                    break;
                }
                SQLLimit limit = queryBlock.getLimit();
                if (limit == null) {
                    limit = new SQLLimit();
                    queryBlock.setLimit(limit);
                }
                this.lexer.nextToken();
                final SQLExpr offset = this.expr();
                limit.setOffset(offset);
                if (this.lexer.token() != Token.ROW && this.lexer.token() != Token.ROWS) {
                    continue;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token() == Token.FETCH) {
            this.lexer.nextToken();
            final PGSelectQueryBlock.FetchClause fetch = new PGSelectQueryBlock.FetchClause();
            if (this.lexer.token() == Token.FIRST) {
                fetch.setOption(PGSelectQueryBlock.FetchClause.Option.FIRST);
            }
            else {
                if (this.lexer.token() != Token.NEXT) {
                    throw new ParserException("expect 'FIRST' or 'NEXT'. " + this.lexer.info());
                }
                fetch.setOption(PGSelectQueryBlock.FetchClause.Option.NEXT);
            }
            final SQLExpr count = this.expr();
            fetch.setCount(count);
            if (this.lexer.token() != Token.ROW && this.lexer.token() != Token.ROWS) {
                throw new ParserException("expect 'ROW' or 'ROWS'. " + this.lexer.info());
            }
            this.lexer.nextToken();
            if (this.lexer.token() != Token.ONLY) {
                throw new ParserException("expect 'ONLY'. " + this.lexer.info());
            }
            this.lexer.nextToken();
            queryBlock.setFetch(fetch);
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            final PGSelectQueryBlock.ForClause forClause = new PGSelectQueryBlock.ForClause();
            if (this.lexer.token() == Token.UPDATE) {
                forClause.setOption(PGSelectQueryBlock.ForClause.Option.UPDATE);
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token() != Token.SHARE) {
                    throw new ParserException("expect 'FIRST' or 'NEXT'. " + this.lexer.info());
                }
                forClause.setOption(PGSelectQueryBlock.ForClause.Option.SHARE);
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.OF) {
                while (true) {
                    final SQLExpr expr2 = this.createExprParser().expr();
                    forClause.getOf().add(expr2);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token() == Token.NOWAIT) {
                this.lexer.nextToken();
                forClause.setNoWait(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SKIP)) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOCKED");
                forClause.setSkipLocked(true);
            }
            queryBlock.setForClause(forClause);
        }
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    @Override
    public SQLTableSource parseTableSourceRest(final SQLTableSource tableSource) {
        if (this.lexer.token() == Token.AS && tableSource instanceof SQLExprTableSource) {
            this.lexer.nextToken();
            String alias = null;
            if (this.lexer.token() == Token.IDENTIFIER) {
                alias = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.LPAREN) {
                final SQLExprTableSource exprTableSource = (SQLExprTableSource)tableSource;
                final PGFunctionTableSource functionTableSource = new PGFunctionTableSource(exprTableSource.getExpr());
                if (alias != null) {
                    functionTableSource.setAlias(alias);
                }
                this.lexer.nextToken();
                this.parserParameters(functionTableSource.getParameters());
                this.accept(Token.RPAREN);
                return super.parseTableSourceRest(functionTableSource);
            }
            if (alias != null) {
                tableSource.setAlias(alias);
                return super.parseTableSourceRest(tableSource);
            }
        }
        return super.parseTableSourceRest(tableSource);
    }
    
    private void parserParameters(final List<SQLParameter> parameters) {
        do {
            final SQLParameter parameter = new SQLParameter();
            parameter.setName(this.exprParser.name());
            parameter.setDataType(this.exprParser.parseDataType());
            parameters.add(parameter);
            if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            }
        } while (this.lexer.token() != Token.BEGIN && this.lexer.token() != Token.RPAREN);
    }
}
