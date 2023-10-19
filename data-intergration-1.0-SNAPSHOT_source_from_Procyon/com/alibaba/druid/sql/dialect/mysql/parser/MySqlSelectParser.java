// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIndexHint;
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSampling;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIndexHintImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnnestTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.Collection;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.TDDLHint;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLAdhocTableSource;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.dialect.hive.parser.HiveCreateTableParser;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class MySqlSelectParser extends SQLSelectParser
{
    protected boolean returningFlag;
    protected MySqlUpdateStatement updateStmt;
    
    public MySqlSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
        this.returningFlag = false;
    }
    
    public MySqlSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
        this.returningFlag = false;
    }
    
    public MySqlSelectParser(final String sql) {
        this(new MySqlExprParser(sql));
    }
    
    @Override
    public void parseFrom(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.SEMI || this.lexer.token() == Token.ORDER || this.lexer.token() == Token.RPAREN || this.lexer.token() == Token.UNION) {
            return;
        }
        if (this.lexer.token() != Token.FROM) {
            for (final SQLSelectItem item : queryBlock.getSelectList()) {
                final SQLExpr expr = item.getExpr();
                if (expr instanceof SQLAggregateExpr) {
                    throw new ParserException("syntax error, expect " + Token.FROM + ", actual " + this.lexer.token() + ", " + this.lexer.info());
                }
            }
            return;
        }
        this.lexer.nextTokenIdent();
        while (this.lexer.token() == Token.HINT) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.TABLE) {
            final HiveCreateTableParser createTableParser = new HiveCreateTableParser(this.lexer);
            final HiveCreateTableStatement stmt = (HiveCreateTableStatement)createTableParser.parseCreateTable(false);
            final SQLAdhocTableSource tableSource = new SQLAdhocTableSource(stmt);
            queryBlock.setFrom(this.parseTableSourceRest(tableSource));
            return;
        }
        if (this.lexer.token() == Token.UPDATE) {
            this.updateStmt = this.parseUpdateStatment();
            final List<SQLExpr> returnning = this.updateStmt.getReturning();
            for (final SQLSelectItem item2 : queryBlock.getSelectList()) {
                final SQLExpr itemExpr = item2.getExpr();
                itemExpr.setParent(this.updateStmt);
                returnning.add(itemExpr);
            }
            this.returningFlag = true;
            return;
        }
        final SQLTableSource from = this.parseTableSource(queryBlock);
        queryBlock.setFrom(from);
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        List<SQLCommentHint> hints = null;
        if (this.lexer.token() == Token.HINT) {
            hints = this.exprParser.parseHints();
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            select.setParenthesized(true);
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        if (this.lexer.token() == Token.VALUES) {
            return this.valuesQuery(acceptUnion);
        }
        final MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();
        queryBlock.setParent(parent);
        class QueryHintHandler implements Lexer.CommentHandler
        {
            private MySqlSelectQueryBlock queryBlock = queryBlock;
            private Lexer lexer = MySqlSelectParser.this.lexer;
            
            QueryHintHandler(final Lexer lexer) {
            }
            
            @Override
            public boolean handle(final Token lastToken, final String comment) {
                if (this.lexer.isEnabled(SQLParserFeature.TDDLHint) && (comment.startsWith("+ TDDL") || comment.startsWith("+TDDL") || comment.startsWith("!TDDL") || comment.startsWith("TDDL"))) {
                    final SQLCommentHint hint = new TDDLHint(comment);
                    if (this.lexer.getCommentCount() > 0) {
                        hint.addBeforeComment(this.lexer.getComments());
                    }
                    this.queryBlock.getHints().add(hint);
                    this.lexer.nextToken();
                }
                return false;
            }
        }
        this.lexer.setCommentHandler(new QueryHintHandler(MySqlSelectParser.this.lexer));
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            queryBlock.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (this.lexer.token() == Token.SELECT && this.selectListCache != null) {
            this.selectListCache.match(this.lexer, queryBlock);
        }
        if (this.lexer.token() == Token.SELECT) {
            this.lexer.nextTokenValue();
            while (this.lexer.token() == Token.HINT) {
                this.exprParser.parseHints(queryBlock.getHints());
            }
            while (true) {
                final Token token = this.lexer.token();
                if (token == Token.DISTINCT) {
                    queryBlock.setDistionOption(2);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.DISTINCTROW)) {
                    queryBlock.setDistionOption(4);
                    this.lexer.nextToken();
                }
                else if (token == Token.ALL) {
                    queryBlock.setDistionOption(1);
                    this.lexer.nextToken();
                }
                else if (token == Token.UNIQUE) {
                    queryBlock.setDistionOption(3);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.HIGH_PRIORITY)) {
                    queryBlock.setHignPriority(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.STRAIGHT_JOIN)) {
                    queryBlock.setStraightJoin(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_SMALL_RESULT)) {
                    queryBlock.setSmallResult(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_BIG_RESULT)) {
                    queryBlock.setBigResult(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_BUFFER_RESULT)) {
                    queryBlock.setBufferResult(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_CACHE)) {
                    queryBlock.setCache(true);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_NO_CACHE)) {
                    queryBlock.setCache(false);
                    this.lexer.nextToken();
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SQL_CALC_FOUND_ROWS)) {
                    queryBlock.setCalcFoundRows(true);
                    this.lexer.nextToken();
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.TOP)) {
                        break;
                    }
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.LITERAL_INT) {
                        final SQLLimit limit = new SQLLimit(this.lexer.integerValue().intValue());
                        queryBlock.setLimit(limit);
                        this.lexer.nextToken();
                    }
                    else {
                        if (this.lexer.token() == Token.DOT) {
                            this.lexer.reset(mark);
                            break;
                        }
                        continue;
                    }
                }
            }
            this.parseSelectList(queryBlock);
            if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
                this.lexer.nextToken();
                this.accept(Token.PARTITION);
                final SQLName partition = this.exprParser.name();
                queryBlock.setForcePartition(partition);
            }
            this.parseInto(queryBlock);
        }
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseHierachical(queryBlock);
        if (this.lexer.token() == Token.GROUP || this.lexer.token() == Token.HAVING) {
            this.parseGroupBy(queryBlock);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WINDOW)) {
            this.parseWindow(queryBlock);
        }
        if (this.lexer.token() == Token.ORDER) {
            queryBlock.setOrderBy(this.exprParser.parseOrderBy());
        }
        if (this.lexer.token() == Token.LIMIT) {
            queryBlock.setLimit(this.exprParser.parseLimit());
        }
        if (this.lexer.token() == Token.FETCH) {
            final Lexer.SavePoint mark2 = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.NEXT)) {
                this.lexer.nextToken();
                final SQLExpr rows = this.exprParser.primary();
                queryBlock.setLimit(new SQLLimit(rows));
                this.acceptIdentifier("ROWS");
                this.acceptIdentifier("ONLY");
            }
            else {
                this.lexer.reset(mark2);
            }
        }
        if (this.lexer.token() == Token.PROCEDURE) {
            this.lexer.nextToken();
            throw new ParserException("TODO. " + this.lexer.info());
        }
        if (this.lexer.token() == Token.INTO) {
            this.parseInto(queryBlock);
        }
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.UPDATE) {
                this.lexer.nextToken();
                queryBlock.setForUpdate(true);
                if (this.lexer.identifierEquals(FnvHash.Constants.NO_WAIT) || this.lexer.identifierEquals(FnvHash.Constants.NOWAIT)) {
                    this.lexer.nextToken();
                    queryBlock.setNoWait(true);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.WAIT)) {
                    this.lexer.nextToken();
                    final SQLExpr waitTime = this.exprParser.primary();
                    queryBlock.setWaitTime(waitTime);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.SKIP)) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("LOCKED");
                    queryBlock.setSkipLocked(true);
                }
            }
            else {
                this.acceptIdentifier("SHARE");
                queryBlock.setForShare(true);
            }
        }
        if (this.lexer.token() == Token.LOCK) {
            this.lexer.nextToken();
            this.accept(Token.IN);
            this.acceptIdentifier("SHARE");
            this.acceptIdentifier("MODE");
            queryBlock.setLockInShareMode(true);
        }
        if (hints != null) {
            queryBlock.setHints(hints);
        }
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    @Override
    public SQLTableSource parseTableSource() {
        return this.parseTableSource(null);
    }
    
    public SQLTableSource parseTableSource(final SQLObject parent) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            List hints = null;
            if (this.lexer.token() == Token.HINT) {
                hints = new ArrayList();
                this.exprParser.parseHints(hints);
            }
            SQLTableSource tableSource;
            if (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.WITH) {
                final SQLSelect select = this.select();
                this.accept(Token.RPAREN);
                final SQLSelectQueryBlock innerQuery = select.getQueryBlock();
                final boolean noOrderByAndLimit = innerQuery instanceof SQLSelectQueryBlock && innerQuery.getOrderBy() == null && ((SQLSelectQueryBlock)select.getQuery()).getLimit() == null;
                if (this.lexer.token() == Token.LIMIT) {
                    final SQLLimit limit = this.exprParser.parseLimit();
                    if (parent != null && parent instanceof SQLSelectQueryBlock) {
                        ((SQLSelectQueryBlock)parent).setLimit(limit);
                    }
                    if (parent == null && noOrderByAndLimit) {
                        innerQuery.setLimit(limit);
                    }
                }
                else if (this.lexer.token() == Token.ORDER) {
                    final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
                    if (parent != null && parent instanceof SQLSelectQueryBlock) {
                        ((SQLSelectQueryBlock)parent).setOrderBy(orderBy);
                    }
                    if (parent == null && noOrderByAndLimit) {
                        innerQuery.setOrderBy(orderBy);
                    }
                }
                final SQLSelectQuery query = this.queryRest(select.getQuery(), false);
                if (query instanceof SQLUnionQuery && select.getWithSubQuery() == null) {
                    select.getQuery().setParenthesized(true);
                    tableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query);
                }
                else {
                    tableSource = new SQLSubqueryTableSource(select);
                }
                if (hints != null) {
                    tableSource.getHints().addAll(hints);
                }
            }
            else if (this.lexer.token() == Token.LPAREN) {
                tableSource = this.parseTableSource();
                if (this.lexer.token() != Token.RPAREN && tableSource instanceof SQLSubqueryTableSource) {
                    final SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource)tableSource;
                    final SQLSelect select2 = sqlSubqueryTableSource.getSelect();
                    final SQLSelectQuery query2 = this.queryRest(select2.getQuery(), true);
                    if (query2 instanceof SQLUnionQuery && select2.getWithSubQuery() == null) {
                        select2.getQuery().setParenthesized(true);
                        tableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query2);
                    }
                    else {
                        tableSource = new SQLSubqueryTableSource(select2);
                    }
                    if (hints != null) {
                        tableSource.getHints().addAll(hints);
                    }
                }
                else if (this.lexer.token() != Token.RPAREN && tableSource instanceof SQLUnionQueryTableSource) {
                    final SQLUnionQueryTableSource unionQueryTableSource = (SQLUnionQueryTableSource)tableSource;
                    final SQLUnionQuery unionQuery = unionQueryTableSource.getUnion();
                    final SQLSelectQuery query2 = this.queryRest(unionQuery, true);
                    if (query2 instanceof SQLUnionQuery) {
                        unionQuery.setParenthesized(true);
                        tableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query2);
                    }
                    else {
                        tableSource = new SQLSubqueryTableSource(unionQuery);
                    }
                    if (hints != null) {
                        tableSource.getHints().addAll(hints);
                    }
                }
                this.accept(Token.RPAREN);
            }
            else {
                tableSource = this.parseTableSource();
                this.accept(Token.RPAREN);
                if (this.lexer.token() == Token.AS && tableSource instanceof SQLValuesTableSource) {
                    this.lexer.nextToken();
                    final String alias = this.lexer.stringVal();
                    this.lexer.nextToken();
                    tableSource.setAlias(alias);
                    this.accept(Token.LPAREN);
                    final SQLValuesTableSource values = (SQLValuesTableSource)tableSource;
                    this.exprParser.names(values.getColumns(), tableSource);
                    this.accept(Token.RPAREN);
                }
            }
            return this.parseTableSourceRest(tableSource);
        }
        if (this.lexer.token() == Token.LBRACE) {
            this.accept(Token.LBRACE);
            this.acceptIdentifier("OJ");
            SQLTableSource tableSrc = this.parseTableSource();
            this.accept(Token.RBRACE);
            tableSrc = this.parseTableSourceRest(tableSrc);
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                tableSrc.addAfterComment(this.lexer.readAndResetComments());
            }
            return tableSrc;
        }
        if (this.lexer.token() == Token.VALUES) {
            return this.parseValues();
        }
        if (this.lexer.token() == Token.UPDATE) {
            final SQLTableSource tableSource2 = new MySqlUpdateTableSource(this.parseUpdateStatment());
            return this.parseTableSourceRest(tableSource2);
        }
        if (this.lexer.token() == Token.SELECT) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.UNNEST)) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLUnnestTableSource unnest = new SQLUnnestTableSource();
                this.exprParser.exprList(unnest.getItems(), unnest);
                this.accept(Token.RPAREN);
                if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("ORDINALITY");
                    unnest.setOrdinality(true);
                }
                final String alias = this.tableAlias();
                unnest.setAlias(alias);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprParser.names(unnest.getColumns(), unnest);
                    this.accept(Token.RPAREN);
                }
                final SQLTableSource tableSrc2 = this.parseTableSourceRest(unnest);
                return tableSrc2;
            }
            this.lexer.reset(mark);
        }
        final SQLExprTableSource tableReference = new SQLExprTableSource();
        this.parseTableSourceQueryTableExpr(tableReference);
        final SQLTableSource tableSrc3 = this.parseTableSourceRest(tableReference);
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            tableSrc3.addAfterComment(this.lexer.readAndResetComments());
        }
        return tableSrc3;
    }
    
    protected MySqlUpdateStatement parseUpdateStatment() {
        final MySqlUpdateStatement update = new MySqlUpdateStatement();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.LOW_PRIORITY)) {
            this.lexer.nextToken();
            update.setLowPriority(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            this.lexer.nextToken();
            update.setIgnore(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COMMIT_ON_SUCCESS)) {
            this.lexer.nextToken();
            update.setCommitOnSuccess(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ROLLBACK_ON_FAIL)) {
            this.lexer.nextToken();
            update.setRollBackOnFail(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.QUEUE_ON_PK)) {
            this.lexer.nextToken();
            update.setQueryOnPk(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TARGET_AFFECT_ROW)) {
            this.lexer.nextToken();
            final SQLExpr targetAffectRow = this.exprParser.expr();
            update.setTargetAffectRow(targetAffectRow);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.ALL) {
                this.lexer.nextToken();
                this.acceptIdentifier("PARTITIONS");
                update.setForceAllPartitions(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
                this.lexer.nextToken();
                update.setForceAllPartitions(true);
            }
            else {
                if (this.lexer.token() != Token.PARTITION) {
                    throw new ParserException("TODO. " + this.lexer.info());
                }
                this.lexer.nextToken();
                final SQLName partition = this.exprParser.name();
                update.setForcePartition(partition);
            }
        }
        while (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(update.getHints());
        }
        final SQLSelectParser selectParser = this.exprParser.createSelectParser();
        final SQLTableSource updateTableSource = selectParser.parseTableSource();
        update.setTableSource(updateTableSource);
        this.accept(Token.SET);
        while (true) {
            final SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
            update.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            update.setWhere(this.exprParser.expr());
        }
        update.setOrderBy(this.exprParser.parseOrderBy());
        update.setLimit(this.exprParser.parseLimit());
        return update;
    }
    
    protected void parseInto(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token() != Token.INTO) {
            return;
        }
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.OUTFILE)) {
            this.lexer.nextToken();
            final MySqlOutFileExpr outFile = new MySqlOutFileExpr();
            outFile.setFile(this.expr());
            queryBlock.setInto(outFile);
            if (this.lexer.identifierEquals(FnvHash.Constants.FIELDS) || this.lexer.identifierEquals(FnvHash.Constants.COLUMNS)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.TERMINATED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                }
                outFile.setColumnsTerminatedBy(this.expr());
                if (this.lexer.identifierEquals(FnvHash.Constants.OPTIONALLY)) {
                    this.lexer.nextToken();
                    outFile.setColumnsEnclosedOptionally(true);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ENCLOSED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    outFile.setColumnsEnclosedBy((SQLLiteralExpr)this.expr());
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ESCAPED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    outFile.setColumnsEscaped((SQLLiteralExpr)this.expr());
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LINES)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.STARTING)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    outFile.setLinesStartingBy((SQLLiteralExpr)this.expr());
                }
                else {
                    if (this.lexer.identifierEquals(FnvHash.Constants.TERMINATED)) {
                        this.lexer.nextToken();
                    }
                    this.accept(Token.BY);
                    outFile.setLinesTerminatedBy((SQLLiteralExpr)this.expr());
                }
            }
        }
        else {
            SQLExpr intoExpr = this.exprParser.name();
            if (this.lexer.token() == Token.COMMA) {
                final SQLListExpr list = new SQLListExpr();
                list.addItem(intoExpr);
                while (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    final SQLName name = this.exprParser.name();
                    list.addItem(name);
                }
                intoExpr = list;
            }
            queryBlock.setInto(intoExpr);
        }
    }
    
    @Override
    protected SQLTableSource primaryTableSourceRest(final SQLTableSource tableSource) {
        if (this.lexer.token() == Token.USE) {
            this.lexer.nextToken();
            final MySqlUseIndexHint hint = new MySqlUseIndexHint();
            this.parseIndexHint(hint);
            tableSource.getHints().add(hint);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            this.lexer.nextToken();
            final MySqlIgnoreIndexHint hint2 = new MySqlIgnoreIndexHint();
            this.parseIndexHint(hint2);
            tableSource.getHints().add(hint2);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FORCE)) {
            this.lexer.nextToken();
            final MySqlForceIndexHint hint3 = new MySqlForceIndexHint();
            this.parseIndexHint(hint3);
            tableSource.getHints().add(hint3);
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.ON) {
                tableSource.setAlias("partition");
            }
            else {
                this.accept(Token.LPAREN);
                this.exprParser.names(((SQLExprTableSource)tableSource).getPartitions(), tableSource);
                this.accept(Token.RPAREN);
            }
        }
        return tableSource;
    }
    
    @Override
    public SQLTableSource parseTableSourceRest(final SQLTableSource tableSource) {
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLESAMPLE) && tableSource instanceof SQLExprTableSource) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            final SQLTableSampling sampling = new SQLTableSampling();
            if (this.lexer.identifierEquals(FnvHash.Constants.BERNOULLI)) {
                this.lexer.nextToken();
                sampling.setBernoulli(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SYSTEM)) {
                this.lexer.nextToken();
                sampling.setSystem(true);
            }
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.BUCKET)) {
                    this.lexer.nextToken();
                    final SQLExpr bucket = this.exprParser.primary();
                    sampling.setBucket(bucket);
                    if (this.lexer.token() == Token.OUT) {
                        this.lexer.nextToken();
                        this.accept(Token.OF);
                        final SQLExpr outOf = this.exprParser.primary();
                        sampling.setOutOf(outOf);
                    }
                    if (this.lexer.token() == Token.ON) {
                        this.lexer.nextToken();
                        final SQLExpr on = this.exprParser.expr();
                        sampling.setOn(on);
                    }
                }
                if (this.lexer.token() == Token.LITERAL_INT || this.lexer.token() == Token.LITERAL_FLOAT) {
                    final SQLExpr val = this.exprParser.primary();
                    if (this.lexer.identifierEquals(FnvHash.Constants.ROWS)) {
                        this.lexer.nextToken();
                        sampling.setRows(val);
                    }
                    else if (this.lexer.token() == Token.RPAREN) {
                        sampling.setRows(val);
                    }
                    else {
                        this.acceptIdentifier("PERCENT");
                        sampling.setPercent(val);
                    }
                }
                if (this.lexer.token() == Token.IDENTIFIER) {
                    final String strVal = this.lexer.stringVal();
                    final char first = strVal.charAt(0);
                    char last = strVal.charAt(strVal.length() - 1);
                    if (last >= 'a' && last <= 'z') {
                        last -= ' ';
                    }
                    boolean match = false;
                    if (first == '.' || (first >= '0' && first <= '9')) {
                        switch (last) {
                            case 'B':
                            case 'G':
                            case 'K':
                            case 'M':
                            case 'P':
                            case 'T': {
                                match = true;
                                break;
                            }
                        }
                    }
                    final SQLSizeExpr size = new SQLSizeExpr(strVal.substring(0, strVal.length() - 2), last);
                    sampling.setByteLength(size);
                    this.lexer.nextToken();
                }
                final SQLExprTableSource table = (SQLExprTableSource)tableSource;
                table.setSampling(sampling);
                this.accept(Token.RPAREN);
            }
            else {
                this.lexer.reset(mark);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
            return tableSource;
        }
        this.parseIndexHintList(tableSource);
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprParser.names(((SQLExprTableSource)tableSource).getPartitions(), tableSource);
            this.accept(Token.RPAREN);
        }
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
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
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
        while (this.lexer.token() != Token.RPAREN && this.lexer.token() != Token.EOF) {
            if (this.lexer.token() == Token.PRIMARY) {
                this.lexer.nextToken();
                hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
            }
            else {
                final SQLName name = this.exprParser.name();
                name.setParent(hint);
                hint.getIndexList().add(name);
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
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
