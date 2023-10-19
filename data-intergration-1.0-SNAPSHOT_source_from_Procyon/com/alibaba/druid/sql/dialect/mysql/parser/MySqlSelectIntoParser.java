// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIndexHintImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class MySqlSelectIntoParser extends SQLSelectParser
{
    private List<SQLExpr> argsList;
    
    public MySqlSelectIntoParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public MySqlSelectIntoParser(final String sql) {
        this(new MySqlExprParser(sql));
    }
    
    public MySqlSelectIntoStatement parseSelectInto() {
        final SQLSelect select = this.select();
        final MySqlSelectIntoStatement stmt = new MySqlSelectIntoStatement();
        stmt.setSelect(select);
        stmt.setVarList(this.argsList);
        return stmt;
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        final MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();
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
            else if (this.lexer.identifierEquals("DISTINCTROW")) {
                queryBlock.setDistionOption(4);
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.ALL) {
                queryBlock.setDistionOption(1);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("HIGH_PRIORITY")) {
                queryBlock.setHignPriority(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("STRAIGHT_JOIN")) {
                queryBlock.setStraightJoin(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_SMALL_RESULT")) {
                queryBlock.setSmallResult(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_BIG_RESULT")) {
                queryBlock.setBigResult(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_BUFFER_RESULT")) {
                queryBlock.setBufferResult(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_CACHE")) {
                queryBlock.setCache(true);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_NO_CACHE")) {
                queryBlock.setCache(false);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("SQL_CALC_FOUND_ROWS")) {
                queryBlock.setCalcFoundRows(true);
                this.lexer.nextToken();
            }
            this.parseSelectList(queryBlock);
            this.argsList = this.parseIntoArgs();
        }
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseGroupBy(queryBlock);
        queryBlock.setOrderBy(this.exprParser.parseOrderBy());
        if (this.lexer.token() == Token.LIMIT) {
            queryBlock.setLimit(this.exprParser.parseLimit());
        }
        if (this.lexer.token() == Token.PROCEDURE) {
            this.lexer.nextToken();
            throw new ParserException("TODO. " + this.lexer.info());
        }
        this.parseInto(queryBlock);
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            this.accept(Token.UPDATE);
            queryBlock.setForUpdate(true);
        }
        if (this.lexer.token() == Token.LOCK) {
            this.lexer.nextToken();
            this.accept(Token.IN);
            this.acceptIdentifier("SHARE");
            this.acceptIdentifier("MODE");
            queryBlock.setLockInShareMode(true);
        }
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    protected List<SQLExpr> parseIntoArgs() {
        final List<SQLExpr> args = new ArrayList<SQLExpr>();
        if (this.lexer.token() == Token.INTO) {
            this.accept(Token.INTO);
            while (true) {
                SQLExpr var = this.exprParser.primary();
                if (var instanceof SQLIdentifierExpr) {
                    var = new SQLVariantRefExpr(((SQLIdentifierExpr)var).getName());
                }
                args.add(var);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.accept(Token.COMMA);
            }
        }
        return args;
    }
    
    protected void parseInto(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("OUTFILE")) {
                this.lexer.nextToken();
                final MySqlOutFileExpr outFile = new MySqlOutFileExpr();
                outFile.setFile(this.expr());
                queryBlock.setInto(outFile);
                if (this.lexer.identifierEquals("FIELDS") || this.lexer.identifierEquals("COLUMNS")) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("TERMINATED")) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                    }
                    outFile.setColumnsTerminatedBy(this.expr());
                    if (this.lexer.identifierEquals("OPTIONALLY")) {
                        this.lexer.nextToken();
                        outFile.setColumnsEnclosedOptionally(true);
                    }
                    if (this.lexer.identifierEquals("ENCLOSED")) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        outFile.setColumnsEnclosedBy((SQLLiteralExpr)this.expr());
                    }
                    if (this.lexer.identifierEquals("ESCAPED")) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        outFile.setColumnsEscaped((SQLLiteralExpr)this.expr());
                    }
                }
                if (this.lexer.identifierEquals("LINES")) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("STARTING")) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        outFile.setLinesStartingBy((SQLLiteralExpr)this.expr());
                    }
                    else {
                        this.lexer.identifierEquals("TERMINATED");
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        outFile.setLinesTerminatedBy((SQLLiteralExpr)this.expr());
                    }
                }
            }
            else {
                queryBlock.setInto(this.exprParser.name());
            }
        }
    }
    
    @Override
    public SQLTableSource parseTableSourceRest(final SQLTableSource tableSource) {
        if (this.lexer.identifierEquals("USING")) {
            return tableSource;
        }
        this.parseIndexHintList(tableSource);
        return super.parseTableSourceRest(tableSource);
    }
    
    private void parseIndexHintList(final SQLTableSource tableSource) {
        if (this.lexer.token() == Token.USE) {
            this.lexer.nextToken();
            final MySqlUseIndexHint hint = new MySqlUseIndexHint();
            this.parseIndexHint(hint);
            tableSource.getHints().add(hint);
            this.parseIndexHintList(tableSource);
        }
        if (this.lexer.identifierEquals("IGNORE")) {
            this.lexer.nextToken();
            final MySqlIgnoreIndexHint hint2 = new MySqlIgnoreIndexHint();
            this.parseIndexHint(hint2);
            tableSource.getHints().add(hint2);
            this.parseIndexHintList(tableSource);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            final MySqlForceIndexHint hint3 = new MySqlForceIndexHint();
            this.parseIndexHint(hint3);
            tableSource.getHints().add(hint3);
            this.parseIndexHintList(tableSource);
        }
    }
    
    private void parseIndexHint(final MySqlIndexHintImpl hint) {
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.KEY);
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.JOIN) {
                this.lexer.nextToken();
                hint.setOption(MySqlIndexHint.Option.JOIN);
            }
            else if (this.lexer.token() == Token.ORDER) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.ORDER_BY);
            }
            else {
                this.accept(Token.GROUP);
                this.accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.GROUP_BY);
            }
        }
        this.accept(Token.LPAREN);
        if (this.lexer.token() == Token.PRIMARY) {
            this.lexer.nextToken();
            hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
        }
        else {
            this.exprParser.names(hint.getIndexList());
        }
        this.accept(Token.RPAREN);
    }
    
    @Override
    public SQLUnionQuery unionRest(final SQLUnionQuery union) {
        if (this.lexer.token() == Token.LIMIT) {
            union.setLimit(this.exprParser.parseLimit());
        }
        return super.unionRest(union);
    }
    
    public MySqlExprParser getExprParser() {
        return (MySqlExprParser)this.exprParser;
    }
}
