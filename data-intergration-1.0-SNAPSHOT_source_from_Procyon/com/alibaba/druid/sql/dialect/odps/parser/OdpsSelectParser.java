// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.parser;

import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class OdpsSelectParser extends SQLSelectParser
{
    public OdpsSelectParser(final SQLExprParser exprParser) {
        super(exprParser.getLexer());
        this.exprParser = exprParser;
    }
    
    public OdpsSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser.getLexer());
        this.exprParser = exprParser;
        this.selectListCache = selectListCache;
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        final OdpsSelectQueryBlock queryBlock = new OdpsSelectQueryBlock();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            queryBlock.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (this.lexer.token() == Token.FROM) {
            this.parseFrom(queryBlock);
            this.parseWhere(queryBlock);
            this.parseGroupBy(queryBlock);
            if (this.lexer.token() == Token.SELECT) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.HINT) {
                    this.exprParser.parseHints(queryBlock.getHints());
                }
                if (this.lexer.token() == Token.COMMENT) {
                    this.lexer.nextToken();
                }
                if (this.lexer.token() == Token.DISTINCT) {
                    queryBlock.setDistionOption(2);
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.UNIQUE) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.DOT) {
                        this.lexer.reset(mark);
                    }
                    else {
                        queryBlock.setDistionOption(3);
                    }
                }
                else if (this.lexer.token() == Token.ALL) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.DOT) {}
                    queryBlock.setDistionOption(1);
                }
                this.parseSelectList(queryBlock);
            }
            if (queryBlock.getWhere() == null && this.lexer.token() == Token.WHERE) {
                this.parseWhere(queryBlock);
            }
        }
        else {
            this.accept(Token.SELECT);
            if (this.lexer.token() == Token.HINT) {
                this.exprParser.parseHints(queryBlock.getHints());
            }
            if (this.lexer.token() == Token.COMMENT) {
                final Lexer.SavePoint mark = this.lexer.mark();
                final String tokenStr = this.lexer.stringVal();
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA) {
                    final SQLIdentifierExpr expr = new SQLIdentifierExpr(tokenStr);
                    queryBlock.addSelectItem(expr);
                    this.lexer.nextToken();
                }
                else {
                    this.lexer.reset(mark);
                }
            }
            if (queryBlock.getSelectList().isEmpty()) {
                if (this.lexer.token() == Token.DISTINCT) {
                    queryBlock.setDistionOption(2);
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.UNIQUE) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.DOT || this.lexer.token() == Token.COMMA) {
                        this.lexer.reset(mark);
                    }
                    else {
                        queryBlock.setDistionOption(3);
                    }
                }
                else if (this.lexer.token() == Token.ALL) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    switch (this.lexer.token()) {
                        case DOT:
                        case COMMA:
                        case SUB:
                        case PLUS:
                        case SLASH:
                        case GT:
                        case GTEQ:
                        case EQ:
                        case LT:
                        case LTEQ: {
                            this.lexer.reset(mark);
                            break;
                        }
                        default: {
                            queryBlock.setDistionOption(1);
                            break;
                        }
                    }
                }
            }
            this.parseSelectList(queryBlock);
            this.parseFrom(queryBlock);
            if (queryBlock.getFrom() == null && this.lexer.token() == Token.LATERAL) {
                this.lexer.nextToken();
                final SQLTableSource tableSource = this.parseLateralView(null);
                queryBlock.setFrom(tableSource);
            }
            this.parseWhere(queryBlock);
            this.parseGroupBy(queryBlock);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WINDOW)) {
            this.parseWindow(queryBlock);
        }
        this.parseGroupBy(queryBlock);
        queryBlock.setOrderBy(this.exprParser.parseOrderBy());
        queryBlock.setZOrderBy(this.exprParser.parseZOrderBy());
        if (this.lexer.token() == Token.DISTRIBUTE) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            while (true) {
                final SQLSelectOrderByItem distributeByItem = this.exprParser.parseSelectOrderByItem();
                queryBlock.addDistributeBy(distributeByItem);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ZORDER)) {
            queryBlock.setZOrderBy(this.exprParser.parseZOrderBy());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SORT)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            while (true) {
                final SQLSelectOrderByItem sortByItem = this.exprParser.parseSelectOrderByItem();
                queryBlock.addSortBy(sortByItem);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTER)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            while (true) {
                final SQLSelectOrderByItem clusterByItem = this.exprParser.parseSelectOrderByItem();
                queryBlock.addClusterBy(clusterByItem);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token() == Token.LIMIT) {
            final SQLLimit limit = this.exprParser.parseLimit();
            queryBlock.setLimit(limit);
        }
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    @Override
    public SQLTableSource parseTableSource() {
        if (this.lexer.token() == Token.NULL) {
            final String str = this.lexer.stringVal();
            this.lexer.nextToken();
            return new SQLExprTableSource(new SQLIdentifierExpr(str));
        }
        SQLTableSource tableSource = super.parseTableSource();
        if (this.lexer.token() == Token.TABLE && tableSource.getAlias() == null) {
            tableSource.setAlias(this.lexer.stringVal());
            this.lexer.nextToken();
            if (tableSource instanceof SQLLateralViewTableSource && this.lexer.token() == Token.AS) {
                this.parseLateralViewAs((SQLLateralViewTableSource)tableSource);
            }
            tableSource = this.parseTableSourceRest(tableSource);
        }
        return tableSource;
    }
    
    @Override
    protected SQLTableSource primaryTableSourceRest(SQLTableSource tableSource) {
        if (this.lexer.identifierEquals(FnvHash.Constants.LATERAL) || this.lexer.token() == Token.LATERAL) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.VIEW) {
                tableSource = this.parseLateralView(tableSource);
            }
            else {
                this.lexer.reset(mark);
            }
        }
        return tableSource;
    }
    
    @Override
    public void parseTableSourceSample(final SQLTableSource tableSource) {
        this.parseTableSourceSampleHive(tableSource);
    }
}
