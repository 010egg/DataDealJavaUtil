// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprHint;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLUnnestTableSource;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLAdhocTableSource;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.dialect.hive.parser.HiveCreateTableParser;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSampling;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLRealExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesQuery;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;

public class SQLSelectParser extends SQLParser
{
    protected SQLExprParser exprParser;
    protected SQLSelectListCache selectListCache;
    
    public SQLSelectParser(final String sql) {
        super(sql);
    }
    
    public SQLSelectParser(final Lexer lexer) {
        super(lexer);
    }
    
    public SQLSelectParser(final SQLExprParser exprParser) {
        this(exprParser, null);
    }
    
    public SQLSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser.getLexer(), exprParser.getDbType());
        this.exprParser = exprParser;
        this.selectListCache = selectListCache;
    }
    
    public SQLSelect select() {
        final SQLSelect select = new SQLSelect();
        if (this.lexer.token == Token.WITH) {
            final SQLWithSubqueryClause with = this.parseWith();
            select.setWithSubQuery(with);
        }
        final SQLSelectQuery query = this.query(select, true);
        select.setQuery(query);
        final SQLOrderBy orderBy = this.parseOrderBy();
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock.getOrderBy() == null) {
                queryBlock.setOrderBy(orderBy);
                if (this.lexer.token == Token.LIMIT) {
                    final SQLLimit limit = this.exprParser.parseLimit();
                    queryBlock.setLimit(limit);
                }
            }
            else {
                select.setOrderBy(orderBy);
                if (this.lexer.token == Token.LIMIT) {
                    final SQLLimit limit = this.exprParser.parseLimit();
                    select.setLimit(limit);
                }
            }
            if (orderBy != null) {
                this.parseFetchClause(queryBlock);
            }
        }
        else {
            select.setOrderBy(orderBy);
        }
        if (this.lexer.token == Token.LIMIT) {
            final SQLLimit limit2 = this.exprParser.parseLimit();
            select.setLimit(limit2);
        }
        while (this.lexer.token == Token.HINT) {
            this.exprParser.parseHints(select.getHints());
        }
        return select;
    }
    
    protected SQLUnionQuery createSQLUnionQuery() {
        return new SQLUnionQuery(this.dbType);
    }
    
    public SQLUnionQuery unionRest(final SQLUnionQuery union) {
        if (this.lexer.token == Token.ORDER) {
            final SQLOrderBy orderBy = this.exprParser.parseOrderBy();
            union.setOrderBy(orderBy);
            return this.unionRest(union);
        }
        if (this.lexer.token == Token.LIMIT) {
            final SQLLimit limit = this.exprParser.parseLimit();
            union.setLimit(limit);
        }
        return union;
    }
    
    public SQLSelectQuery queryRest(final SQLSelectQuery selectQuery) {
        return this.queryRest(selectQuery, true);
    }
    
    public SQLSelectQuery queryRest(SQLSelectQuery selectQuery, final boolean acceptUnion) {
        if (!acceptUnion) {
            return selectQuery;
        }
        if (this.lexer.token == Token.UNION) {
        Label_0632:
            do {
                final Lexer.SavePoint uninMark = this.lexer.mark();
                this.lexer.nextToken();
                switch (this.lexer.token) {
                    case GROUP:
                    case ORDER:
                    case WHERE:
                    case RPAREN: {
                        this.lexer.reset(uninMark);
                        return selectQuery;
                    }
                    default: {
                        if (this.lexer.token == Token.SEMI && this.dbType == DbType.odps) {
                            break Label_0632;
                        }
                        SQLUnionQuery union = this.createSQLUnionQuery();
                        union.setLeft(selectQuery);
                        if (this.lexer.token == Token.ALL) {
                            union.setOperator(SQLUnionOperator.UNION_ALL);
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token == Token.DISTINCT) {
                            union.setOperator(SQLUnionOperator.DISTINCT);
                            this.lexer.nextToken();
                        }
                        boolean paren = this.lexer.token == Token.LPAREN;
                        SQLSelectQuery right = this.query(paren ? null : union, false);
                        union.setRight(right);
                        while (this.lexer.isEnabled(SQLParserFeature.EnableMultiUnion) && this.lexer.token == Token.UNION) {
                            final Lexer.SavePoint mark = this.lexer.mark();
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.UNION && this.dbType == DbType.odps) {
                                continue;
                            }
                            if (this.lexer.token == Token.ALL) {
                                if (union.getOperator() != SQLUnionOperator.UNION_ALL) {
                                    this.lexer.reset(mark);
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            else if (this.lexer.token == Token.DISTINCT) {
                                if (union.getOperator() != SQLUnionOperator.DISTINCT) {
                                    this.lexer.reset(mark);
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            else if (union.getOperator() != SQLUnionOperator.UNION) {
                                this.lexer.reset(mark);
                                break;
                            }
                            paren = (this.lexer.token == Token.LPAREN);
                            final SQLSelectQuery r = this.query(paren ? null : union, false);
                            union.addRelation(r);
                            right = r;
                        }
                        if (!paren) {
                            if (right instanceof SQLSelectQueryBlock) {
                                final SQLSelectQueryBlock rightQuery = (SQLSelectQueryBlock)right;
                                final SQLOrderBy orderBy = rightQuery.getOrderBy();
                                if (orderBy != null) {
                                    union.setOrderBy(orderBy);
                                    rightQuery.setOrderBy(null);
                                }
                                final SQLLimit limit = rightQuery.getLimit();
                                if (limit != null) {
                                    union.setLimit(limit);
                                    rightQuery.setLimit(null);
                                }
                            }
                            else if (right instanceof SQLUnionQuery) {
                                final SQLUnionQuery rightUnion = (SQLUnionQuery)right;
                                final SQLOrderBy orderBy = rightUnion.getOrderBy();
                                if (orderBy != null) {
                                    union.setOrderBy(orderBy);
                                    rightUnion.setOrderBy(null);
                                }
                                final SQLLimit limit = rightUnion.getLimit();
                                if (limit != null) {
                                    union.setLimit(limit);
                                    rightUnion.setLimit(null);
                                }
                            }
                        }
                        union = (SQLUnionQuery)(selectQuery = this.unionRest(union));
                        continue;
                    }
                }
            } while (this.lexer.token() == Token.UNION);
            selectQuery = this.queryRest(selectQuery, true);
            return selectQuery;
        }
        if (this.lexer.token == Token.EXCEPT) {
            this.lexer.nextToken();
            final SQLUnionQuery union2 = new SQLUnionQuery();
            union2.setLeft(selectQuery);
            if (this.lexer.token == Token.ALL) {
                this.lexer.nextToken();
                union2.setOperator(SQLUnionOperator.EXCEPT_ALL);
            }
            else if (this.lexer.token == Token.DISTINCT) {
                this.lexer.nextToken();
                union2.setOperator(SQLUnionOperator.EXCEPT_DISTINCT);
            }
            else {
                union2.setOperator(SQLUnionOperator.EXCEPT);
            }
            final boolean paren2 = this.lexer.token == Token.LPAREN;
            final SQLSelectQuery right2 = this.query(union2, false);
            union2.setRight(right2);
            if (!paren2) {
                if (right2 instanceof SQLSelectQueryBlock) {
                    final SQLSelectQueryBlock rightQuery2 = (SQLSelectQueryBlock)right2;
                    final SQLOrderBy orderBy2 = rightQuery2.getOrderBy();
                    if (orderBy2 != null) {
                        union2.setOrderBy(orderBy2);
                        rightQuery2.setOrderBy(null);
                    }
                    final SQLLimit limit2 = rightQuery2.getLimit();
                    if (limit2 != null) {
                        union2.setLimit(limit2);
                        rightQuery2.setLimit(null);
                    }
                }
                else if (right2 instanceof SQLUnionQuery) {
                    final SQLUnionQuery rightUnion2 = (SQLUnionQuery)right2;
                    final SQLOrderBy orderBy2 = rightUnion2.getOrderBy();
                    if (orderBy2 != null) {
                        union2.setOrderBy(orderBy2);
                        rightUnion2.setOrderBy(null);
                    }
                    final SQLLimit limit2 = rightUnion2.getLimit();
                    if (limit2 != null) {
                        union2.setLimit(limit2);
                        rightUnion2.setLimit(null);
                    }
                }
            }
            return this.queryRest(union2, true);
        }
        if (this.lexer.token == Token.INTERSECT) {
            this.lexer.nextToken();
            final SQLUnionQuery union2 = new SQLUnionQuery();
            union2.setLeft(selectQuery);
            if (this.lexer.token() == Token.DISTINCT) {
                this.lexer.nextToken();
                union2.setOperator(SQLUnionOperator.INTERSECT_DISTINCT);
            }
            else if (this.lexer.token == Token.ALL) {
                this.lexer.nextToken();
                union2.setOperator(SQLUnionOperator.INTERSECT_ALL);
            }
            else {
                union2.setOperator(SQLUnionOperator.INTERSECT);
            }
            final boolean paren2 = this.lexer.token == Token.LPAREN;
            final SQLSelectQuery right2 = this.query(union2, false);
            union2.setRight(right2);
            if (!paren2) {
                if (right2 instanceof SQLSelectQueryBlock) {
                    final SQLSelectQueryBlock rightQuery2 = (SQLSelectQueryBlock)right2;
                    final SQLOrderBy orderBy2 = rightQuery2.getOrderBy();
                    if (orderBy2 != null) {
                        union2.setOrderBy(orderBy2);
                        rightQuery2.setOrderBy(null);
                    }
                    final SQLLimit limit2 = rightQuery2.getLimit();
                    if (limit2 != null) {
                        union2.setLimit(limit2);
                        rightQuery2.setLimit(null);
                    }
                }
                else if (right2 instanceof SQLUnionQuery) {
                    final SQLUnionQuery rightUnion2 = (SQLUnionQuery)right2;
                    final SQLOrderBy orderBy2 = rightUnion2.getOrderBy();
                    if (orderBy2 != null) {
                        union2.setOrderBy(orderBy2);
                        rightUnion2.setOrderBy(null);
                    }
                    final SQLLimit limit2 = rightUnion2.getLimit();
                    if (limit2 != null) {
                        union2.setLimit(limit2);
                        rightUnion2.setLimit(null);
                    }
                }
            }
            return this.queryRest(union2, true);
        }
        if (acceptUnion && this.lexer.token == Token.MINUS) {
            this.lexer.nextToken();
            final SQLUnionQuery union2 = new SQLUnionQuery();
            union2.setLeft(selectQuery);
            union2.setOperator(SQLUnionOperator.MINUS);
            if (this.lexer.token == Token.DISTINCT) {
                union2.setOperator(SQLUnionOperator.MINUS_DISTINCT);
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.ALL) {
                union2.setOperator(SQLUnionOperator.MINUS_ALL);
                this.lexer.nextToken();
            }
            final SQLSelectQuery right3 = this.query(union2, false);
            union2.setRight(right3);
            return this.queryRest(union2, true);
        }
        return selectQuery;
    }
    
    private void setToLeft(final SQLSelectQuery selectQuery, final SQLUnionQuery parentUnion, final SQLUnionQuery union, final SQLSelectQuery right) {
        final SQLUnionOperator operator = union.getOperator();
        if (union.getLeft() instanceof SQLUnionQuery) {
            SQLUnionQuery left;
            for (left = (SQLUnionQuery)union.getLeft(); left.getLeft() instanceof SQLUnionQuery; left = (SQLUnionQuery)left.getLeft()) {}
            left.setLeft(new SQLUnionQuery(parentUnion.getLeft(), parentUnion.getOperator(), left.getLeft()));
            parentUnion.setLeft(union.getLeft());
            parentUnion.setRight(union.getRight());
        }
        else {
            parentUnion.setRight(right);
            union.setLeft(parentUnion.getLeft());
            parentUnion.setLeft(union);
            union.setRight(selectQuery);
            union.setOperator(parentUnion.getOperator());
            parentUnion.setOperator(operator);
        }
    }
    
    public SQLSelectQuery query() {
        return this.query(null, true);
    }
    
    public SQLSelectQuery query(final SQLObject parent) {
        return this.query(parent, true);
    }
    
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        if (this.lexer.token() == Token.VALUES) {
            return this.valuesQuery(acceptUnion);
        }
        final SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock(this.dbType);
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            queryBlock.addBeforeComment(this.lexer.readAndResetComments());
        }
        this.accept(Token.SELECT);
        if (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(queryBlock.getHints());
        }
        if (this.lexer.token == Token.COMMENT) {
            this.lexer.nextToken();
        }
        if (DbType.informix == this.dbType) {
            if (this.lexer.identifierEquals(FnvHash.Constants.SKIP)) {
                this.lexer.nextToken();
                final SQLExpr offset = this.exprParser.primary();
                queryBlock.setOffset(offset);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.FIRST)) {
                this.lexer.nextToken();
                final SQLExpr first = this.exprParser.primary();
                queryBlock.setFirst(first);
            }
        }
        if (this.lexer.token == Token.DISTINCT) {
            queryBlock.setDistionOption(2);
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.UNIQUE) {
            queryBlock.setDistionOption(3);
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.ALL) {
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
        this.parseGroupBy(queryBlock);
        if (this.lexer.identifierEquals(FnvHash.Constants.WINDOW)) {
            this.parseWindow(queryBlock);
        }
        this.parseSortBy(queryBlock);
        this.parseFetchClause(queryBlock);
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            this.accept(Token.UPDATE);
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
        }
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    protected SQLSelectQuery valuesQuery(final boolean acceptUnion) {
        this.lexer.nextToken();
        final SQLValuesQuery valuesQuery = new SQLValuesQuery();
        while (true) {
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLListExpr listExpr = new SQLListExpr();
                this.exprParser.exprList(listExpr.getItems(), listExpr);
                this.accept(Token.RPAREN);
                valuesQuery.addValue(listExpr);
            }
            else {
                this.exprParser.exprList(valuesQuery.getValues(), valuesQuery);
            }
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return this.queryRest(valuesQuery, acceptUnion);
    }
    
    protected void withSubquery(final SQLSelect select) {
        if (this.lexer.token == Token.WITH) {
            this.lexer.nextToken();
            final SQLWithSubqueryClause withQueryClause = new SQLWithSubqueryClause();
            if (this.lexer.token == Token.RECURSIVE || this.lexer.identifierEquals(FnvHash.Constants.RECURSIVE)) {
                this.lexer.nextToken();
                withQueryClause.setRecursive(true);
            }
            while (true) {
                final SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
                entry.setParent(withQueryClause);
                final String alias = this.lexer.stringVal();
                this.lexer.nextToken();
                entry.setAlias(alias);
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprParser.names(entry.getColumns());
                    this.accept(Token.RPAREN);
                }
                this.accept(Token.AS);
                this.accept(Token.LPAREN);
                entry.setSubQuery(this.select());
                this.accept(Token.RPAREN);
                withQueryClause.addEntry(entry);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            select.setWithSubQuery(withQueryClause);
        }
    }
    
    public SQLWithSubqueryClause parseWith() {
        final SQLWithSubqueryClause withQueryClause = new SQLWithSubqueryClause();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            withQueryClause.addBeforeComment(this.lexer.readAndResetComments());
        }
        this.accept(Token.WITH);
        if (this.lexer.token == Token.RECURSIVE || this.lexer.identifierEquals(FnvHash.Constants.RECURSIVE)) {
            this.lexer.nextToken();
            withQueryClause.setRecursive(true);
        }
        while (true) {
            final SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
            entry.setParent(withQueryClause);
            final String alias = this.lexer.stringVal();
            this.lexer.nextToken();
            entry.setAlias(alias);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprParser.names(entry.getColumns());
                this.accept(Token.RPAREN);
            }
            this.accept(Token.AS);
            this.accept(Token.LPAREN);
            switch (this.lexer.token) {
                case SELECT:
                case LPAREN:
                case WITH:
                case FROM: {
                    entry.setSubQuery(this.select());
                    break;
                }
            }
            this.accept(Token.RPAREN);
            withQueryClause.addEntry(entry);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return withQueryClause;
    }
    
    public void parseWhere(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token != Token.WHERE) {
            return;
        }
        this.lexer.nextTokenIdent();
        List<String> beforeComments = null;
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            beforeComments = this.lexer.readAndResetComments();
        }
        SQLExpr where;
        if (this.lexer.token == Token.IDENTIFIER) {
            final String ident = this.lexer.stringVal();
            final long hash_lower = this.lexer.hash_lower();
            this.lexer.nextTokenEq();
            SQLExpr identExpr;
            if (this.lexer.token == Token.LITERAL_CHARS) {
                final String literal = this.lexer.stringVal;
                if (hash_lower == FnvHash.Constants.TIMESTAMP) {
                    identExpr = new SQLTimestampExpr(literal);
                    this.lexer.nextToken();
                }
                else if (hash_lower == FnvHash.Constants.DATE) {
                    identExpr = new SQLDateExpr(literal);
                    this.lexer.nextToken();
                }
                else if (hash_lower == FnvHash.Constants.REAL) {
                    identExpr = new SQLRealExpr(Float.parseFloat(literal));
                    this.lexer.nextToken();
                }
                else {
                    identExpr = new SQLIdentifierExpr(ident, hash_lower);
                }
            }
            else {
                identExpr = new SQLIdentifierExpr(ident, hash_lower);
            }
            if (this.lexer.token == Token.DOT) {
                identExpr = this.exprParser.primaryRest(identExpr);
            }
            if (this.lexer.token == Token.EQ) {
                this.lexer.nextToken();
                SQLExpr rightExp;
                try {
                    rightExp = this.exprParser.bitOr();
                }
                catch (EOFParserException e) {
                    throw new ParserException("EOF, " + ident + "=", e);
                }
                where = new SQLBinaryOpExpr(identExpr, SQLBinaryOperator.Equality, rightExp, this.dbType);
                switch (this.lexer.token) {
                    case BETWEEN:
                    case IS:
                    case EQ:
                    case IN:
                    case CONTAINS:
                    case BANG_TILDE_STAR:
                    case TILDE_EQ:
                    case LT:
                    case LTEQ:
                    case LTEQGT:
                    case GT:
                    case GTEQ:
                    case LTGT:
                    case BANGEQ:
                    case LIKE:
                    case NOT: {
                        where = this.exprParser.relationalRest(where);
                        break;
                    }
                }
                where = this.exprParser.andRest(where);
                where = this.exprParser.xorRest(where);
                where = this.exprParser.orRest(where);
            }
            else {
                identExpr = this.exprParser.primaryRest(identExpr);
                where = this.exprParser.exprRest(identExpr);
            }
        }
        else {
            while (this.lexer.token == Token.HINT) {
                this.lexer.nextToken();
            }
            where = this.exprParser.expr();
            while (this.lexer.token == Token.HINT) {
                this.lexer.nextToken();
            }
        }
        if (beforeComments != null) {
            where.addBeforeComment(beforeComments);
        }
        if (this.lexer.hasComment() && this.lexer.isKeepComments() && this.lexer.token != Token.INSERT) {
            where.addAfterComment(this.lexer.readAndResetComments());
        }
        queryBlock.setWhere(where);
    }
    
    protected void parseSortBy(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.parseOrderBy();
            queryBlock.setOrderBy(orderBy);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DISTRIBUTE)) {
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
    }
    
    protected void parseWindow(final SQLSelectQueryBlock queryBlock) {
        if (!this.lexer.identifierEquals(FnvHash.Constants.WINDOW) && this.lexer.token != Token.WINDOW) {
            return;
        }
        this.lexer.nextToken();
        while (true) {
            final SQLName name = this.exprParser.name();
            this.accept(Token.AS);
            final SQLOver over = new SQLOver();
            this.exprParser.over(over);
            queryBlock.addWindow(new SQLWindow(name, over));
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    public void parseGroupBy(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token == Token.GROUP) {
            this.lexer.nextTokenBy();
            this.accept(Token.BY);
            final SQLSelectGroupByClause groupBy = new SQLSelectGroupByClause();
            if (this.lexer.token == Token.HINT) {
                groupBy.setHint(this.exprParser.parseHint());
            }
            if (this.lexer.token == Token.ALL) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals(FnvHash.Constants.GROUPING)) {
                    if (this.dbType != DbType.odps) {
                        throw new ParserException("group by all syntax error. " + this.lexer.info());
                    }
                    this.lexer.reset(mark);
                }
            }
            else if (this.lexer.token == Token.DISTINCT) {
                this.lexer.nextToken();
                groupBy.setDistinct(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ROLLUP)) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                groupBy.setWithRollUp(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.CUBE)) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                groupBy.setWithCube(true);
            }
            while (true) {
                final SQLExpr item = this.parseGroupByItem();
                item.setParent(groupBy);
                groupBy.addItem(item);
                if (this.lexer.token == Token.COMMA) {
                    this.lexer.nextToken();
                }
                else {
                    if (this.lexer.identifierEquals(FnvHash.Constants.GROUPING)) {
                        continue;
                    }
                    break;
                }
            }
            if (groupBy.isWithRollUp() || groupBy.isWithCube()) {
                this.accept(Token.RPAREN);
                groupBy.setParen(true);
                if (this.lexer.token == Token.COMMA && this.dbType == DbType.odps) {
                    this.lexer.nextToken();
                    final SQLMethodInvokeExpr func = new SQLMethodInvokeExpr(groupBy.isWithCube() ? "CUBE" : "ROLLUP");
                    func.getArguments().addAll(groupBy.getItems());
                    groupBy.getItems().clear();
                    groupBy.setWithCube(false);
                    groupBy.setWithRollUp(false);
                    for (final SQLExpr arg : func.getArguments()) {
                        arg.setParent(func);
                    }
                    groupBy.addItem(func);
                    this.exprParser.exprList(groupBy.getItems(), groupBy);
                }
            }
            if (this.lexer.token == Token.HAVING) {
                this.lexer.nextToken();
                final SQLExpr having = this.exprParser.expr();
                groupBy.setHaving(having);
            }
            if (this.lexer.token == Token.WITH) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.CUBE)) {
                    this.lexer.nextToken();
                    groupBy.setWithCube(true);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ROLLUP)) {
                    this.lexer.nextToken();
                    groupBy.setWithRollUp(true);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.RS) && DbType.db2 == this.dbType) {
                    this.lexer.nextToken();
                    ((DB2SelectQueryBlock)queryBlock).setIsolation(DB2SelectQueryBlock.Isolation.RS);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.RR) && DbType.db2 == this.dbType) {
                    this.lexer.nextToken();
                    ((DB2SelectQueryBlock)queryBlock).setIsolation(DB2SelectQueryBlock.Isolation.RR);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.CS) && DbType.db2 == this.dbType) {
                    this.lexer.nextToken();
                    ((DB2SelectQueryBlock)queryBlock).setIsolation(DB2SelectQueryBlock.Isolation.CS);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.UR) && DbType.db2 == this.dbType) {
                    this.lexer.nextToken();
                    ((DB2SelectQueryBlock)queryBlock).setIsolation(DB2SelectQueryBlock.Isolation.UR);
                }
                else {
                    this.lexer.reset(mark);
                }
            }
            if (groupBy.getHaving() == null && this.lexer.token == Token.HAVING) {
                this.lexer.nextToken();
                final SQLExpr having = this.exprParser.expr();
                groupBy.setHaving(having);
            }
            queryBlock.setGroupBy(groupBy);
        }
        else if (this.lexer.token == Token.HAVING) {
            this.lexer.nextToken();
            final SQLSelectGroupByClause groupBy = new SQLSelectGroupByClause();
            groupBy.setHaving(this.exprParser.expr());
            if (this.lexer.token == Token.GROUP) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                while (true) {
                    final SQLExpr item = this.parseGroupByItem();
                    item.setParent(groupBy);
                    groupBy.addItem(item);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            if (this.lexer.token == Token.WITH) {
                this.lexer.nextToken();
                this.acceptIdentifier("ROLLUP");
                groupBy.setWithRollUp(true);
            }
            if (DbType.mysql == this.dbType && this.lexer.token == Token.DESC) {
                this.lexer.nextToken();
            }
            queryBlock.setGroupBy(groupBy);
        }
    }
    
    protected SQLExpr parseGroupByItem() {
        if (this.lexer.token == Token.LPAREN) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token == Token.RPAREN) {
                this.lexer.nextToken();
                return new SQLListExpr();
            }
            this.lexer.reset(mark);
        }
        SQLExpr item;
        if (this.lexer.identifierEquals(FnvHash.Constants.ROLLUP)) {
            final SQLMethodInvokeExpr rollup = new SQLMethodInvokeExpr(this.lexer.stringVal());
            this.lexer.nextToken();
            Label_0254: {
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    while (true) {
                        while (this.lexer.token != Token.RPAREN) {
                            SQLExpr expr;
                            if (this.lexer.token == Token.LPAREN) {
                                this.accept(Token.LPAREN);
                                final SQLListExpr list = new SQLListExpr();
                                if (this.lexer.token == Token.COMMA) {
                                    this.lexer.nextToken();
                                }
                                this.exprParser.exprList(list.getItems(), list);
                                this.accept(Token.RPAREN);
                                expr = list;
                            }
                            else {
                                expr = this.exprParser.expr();
                            }
                            rollup.addArgument(expr);
                            if (this.lexer.token != Token.COMMA) {
                                this.accept(Token.RPAREN);
                                break Label_0254;
                            }
                            this.lexer.nextToken();
                        }
                        continue;
                    }
                }
            }
            item = rollup;
        }
        else {
            item = this.exprParser.expr();
        }
        if (DbType.mysql == this.dbType) {
            if (this.lexer.token == Token.DESC) {
                this.lexer.nextToken();
                item = new MySqlOrderingExpr(item, SQLOrderingSpecification.DESC);
            }
            else if (this.lexer.token == Token.ASC) {
                this.lexer.nextToken();
                item = new MySqlOrderingExpr(item, SQLOrderingSpecification.ASC);
            }
        }
        if (this.lexer.token == Token.HINT) {
            final SQLCommentHint hint = this.exprParser.parseHint();
            if (item instanceof SQLObjectImpl) {
                ((SQLExprImpl)item).setHint(hint);
            }
        }
        return item;
    }
    
    protected void parseSelectList(final SQLSelectQueryBlock queryBlock) {
        final List<SQLSelectItem> selectList = queryBlock.getSelectList();
        while (true) {
            final SQLSelectItem selectItem = this.exprParser.parseSelectItem();
            selectList.add(selectItem);
            selectItem.setParent(queryBlock);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
    
    public void parseFrom(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token != Token.FROM) {
            return;
        }
        this.lexer.nextToken();
        queryBlock.setFrom(this.parseTableSource());
    }
    
    public SQLTableSource parseTableSource() {
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            SQLTableSource tableSource;
            if (this.lexer.token == Token.SELECT || this.lexer.token == Token.WITH || this.lexer.token == Token.SEL) {
                final SQLSelect select = this.select();
                this.accept(Token.RPAREN);
                final SQLSelectQuery selectQuery = select.getQuery();
                selectQuery.setParenthesized(true);
                final boolean acceptUnion = !(selectQuery instanceof SQLUnionQuery);
                final SQLSelectQuery query = this.queryRest(selectQuery, acceptUnion);
                if (query instanceof SQLUnionQuery) {
                    tableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query);
                }
                else {
                    tableSource = new SQLSubqueryTableSource(select);
                }
            }
            else if (this.lexer.token == Token.LPAREN) {
                tableSource = this.parseTableSource();
                while ((this.lexer.token == Token.UNION || this.lexer.token == Token.EXCEPT || this.lexer.token == Token.INTERSECT || this.lexer.token == Token.MINUS) && tableSource instanceof SQLUnionQueryTableSource) {
                    final SQLUnionQueryTableSource unionQueryTableSource = (SQLUnionQueryTableSource)tableSource;
                    final SQLUnionQuery union = unionQueryTableSource.getUnion();
                    unionQueryTableSource.setUnion((SQLUnionQuery)this.queryRest(union));
                }
                this.accept(Token.RPAREN);
            }
            else {
                tableSource = this.parseTableSource();
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token == Token.AS) {
                this.lexer.nextToken();
                final String alias = this.tableAlias(true);
                tableSource.setAlias(alias);
                if (tableSource instanceof SQLValuesTableSource && ((SQLValuesTableSource)tableSource).getColumns().size() == 0) {
                    final SQLValuesTableSource values = (SQLValuesTableSource)tableSource;
                    this.accept(Token.LPAREN);
                    this.exprParser.names(values.getColumns(), values);
                    this.accept(Token.RPAREN);
                }
                else if (tableSource instanceof SQLSubqueryTableSource) {
                    final SQLSubqueryTableSource values2 = (SQLSubqueryTableSource)tableSource;
                    if (this.lexer.token == Token.LPAREN) {
                        this.lexer.nextToken();
                        this.exprParser.names(values2.getColumns(), values2);
                        this.accept(Token.RPAREN);
                    }
                }
            }
            return this.parseTableSourceRest(tableSource);
        }
        if (this.lexer.token() == Token.VALUES) {
            this.lexer.nextToken();
            final SQLValuesTableSource tableSource2 = new SQLValuesTableSource();
            while (true) {
                this.accept(Token.LPAREN);
                final SQLListExpr listExpr = new SQLListExpr();
                this.exprParser.exprList(listExpr.getItems(), listExpr);
                this.accept(Token.RPAREN);
                listExpr.setParent(tableSource2);
                tableSource2.getValues().add(listExpr);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token == Token.RPAREN) {
                return tableSource2;
            }
            final String alias = this.tableAlias();
            if (alias != null) {
                tableSource2.setAlias(alias);
            }
            this.accept(Token.LPAREN);
            this.exprParser.names(tableSource2.getColumns(), tableSource2);
            this.accept(Token.RPAREN);
            return this.parseTableSourceRest(tableSource2);
        }
        else {
            if (this.lexer.token == Token.SELECT) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            final SQLExprTableSource tableReference = new SQLExprTableSource();
            this.parseTableSourceQueryTableExpr(tableReference);
            final SQLTableSource tableSrc = this.parseTableSourceRest(tableReference);
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                tableSrc.addAfterComment(this.lexer.readAndResetComments());
            }
            return tableSrc;
        }
    }
    
    protected void parseTableSourceQueryTableExpr(final SQLExprTableSource tableReference) {
        if (this.lexer.token == Token.LITERAL_ALIAS || this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED) || this.lexer.token == Token.LITERAL_CHARS) {
            tableReference.setExpr(this.exprParser.name());
            return;
        }
        if (this.lexer.token == Token.HINT) {
            final SQLCommentHint hint = this.exprParser.parseHint();
            tableReference.setHint(hint);
        }
        SQLExpr expr = null;
        switch (this.lexer.token) {
            case ALL:
            case SET: {
                expr = this.exprParser.name();
                break;
            }
            default: {
                expr = this.expr();
                break;
            }
        }
        if (expr instanceof SQLBinaryOpExpr) {
            throw new ParserException("Invalid from clause : " + expr.toString().replace("\n", " "));
        }
        tableReference.setExpr(expr);
    }
    
    protected SQLTableSource primaryTableSourceRest(final SQLTableSource tableSource) {
        return tableSource;
    }
    
    public void parseTableSourceSample(final SQLTableSource tableSource) {
    }
    
    public void parseTableSourceSampleHive(final SQLTableSource tableSource) {
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLESAMPLE) && tableSource instanceof SQLExprTableSource) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLTableSampling sampling = new SQLTableSampling();
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
    }
    
    public SQLTableSource parseTableSourceRest(final SQLTableSource tableSource) {
        this.parseTableSourceSample(tableSource);
        if (this.lexer.hasComment() && this.lexer.isKeepComments() && !(tableSource instanceof SQLSubqueryTableSource)) {
            tableSource.addAfterComment(this.lexer.readAndResetComments());
        }
        if (tableSource.getAlias() == null || tableSource.getAlias().length() == 0) {
            final Token token = this.lexer.token;
            switch (token) {
                case LEFT:
                case RIGHT:
                case FULL: {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    final String strVal = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.OUTER || this.lexer.token == Token.JOIN || this.lexer.identifierEquals(FnvHash.Constants.ANTI) || this.lexer.identifierEquals(FnvHash.Constants.SEMI)) {
                        this.lexer.reset(mark);
                    }
                    else {
                        tableSource.setAlias(strVal);
                    }
                    break;
                }
                case OUTER: {
                    break;
                }
                default: {
                    final long hash;
                    if (token == Token.IDENTIFIER && ((hash = this.lexer.hash_lower()) == FnvHash.Constants.STRAIGHT_JOIN || hash == FnvHash.Constants.CROSS)) {
                        break;
                    }
                    boolean must = false;
                    if (this.lexer.token == Token.AS) {
                        this.lexer.nextToken();
                        must = true;
                    }
                    String alias = this.tableAlias(must);
                    if (alias == null) {
                        break;
                    }
                    if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && alias.length() > 1) {
                        alias = StringUtils.removeNameQuotes(alias);
                    }
                    tableSource.setAlias(alias);
                    if (tableSource instanceof SQLValuesTableSource && ((SQLValuesTableSource)tableSource).getColumns().size() == 0) {
                        final SQLValuesTableSource values = (SQLValuesTableSource)tableSource;
                        this.accept(Token.LPAREN);
                        this.exprParser.names(values.getColumns(), values);
                        this.accept(Token.RPAREN);
                    }
                    else if (tableSource instanceof SQLSubqueryTableSource) {
                        final SQLSubqueryTableSource subQuery = (SQLSubqueryTableSource)tableSource;
                        if (this.lexer.token == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.exprParser.names(subQuery.getColumns(), subQuery);
                            this.accept(Token.RPAREN);
                        }
                    }
                    else if (tableSource instanceof SQLUnionQueryTableSource) {
                        final SQLUnionQueryTableSource union = (SQLUnionQueryTableSource)tableSource;
                        if (this.lexer.token == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.exprParser.names(union.getColumns(), union);
                            this.accept(Token.RPAREN);
                        }
                    }
                    else if (this.lexer.token == Token.LPAREN && tableSource instanceof SQLExprTableSource && (((SQLExprTableSource)tableSource).getExpr() instanceof SQLVariantRefExpr || ((SQLExprTableSource)tableSource).getExpr() instanceof SQLIdentifierExpr)) {
                        this.lexer.nextToken();
                        final SQLExprTableSource exprTableSource = (SQLExprTableSource)tableSource;
                        this.exprParser.names(exprTableSource.getColumns(), exprTableSource);
                        this.accept(Token.RPAREN);
                    }
                    if (this.lexer.token == Token.WHERE) {
                        return tableSource;
                    }
                    return this.parseTableSourceRest(tableSource);
                }
            }
        }
        SQLJoinTableSource.JoinType joinType = null;
        boolean natural = this.lexer.identifierEquals(FnvHash.Constants.NATURAL);
        if (natural) {
            this.lexer.nextToken();
        }
        boolean asof = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.ASOF) && this.dbType == DbType.clickhouse) {
            this.lexer.nextToken();
            asof = true;
        }
        if (this.lexer.token == Token.OUTER) {
            final Lexer.SavePoint mark = this.lexer.mark();
            final String str = this.lexer.stringVal();
            this.lexer.nextToken();
            if (tableSource.getAlias() == null && !this.lexer.identifierEquals(FnvHash.Constants.APPLY)) {
                tableSource.setAlias(str);
            }
            else {
                this.lexer.reset(mark);
            }
        }
        boolean global = false;
        if (this.dbType == DbType.clickhouse && this.lexer.token == Token.GLOBAL) {
            this.lexer.nextToken();
            global = true;
        }
        switch (this.lexer.token) {
            case LEFT: {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.SEMI)) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.LEFT_SEMI_JOIN;
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ANTI)) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.LEFT_ANTI_JOIN;
                }
                else if (this.lexer.token == Token.OUTER) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN;
                }
                else {
                    joinType = SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN;
                }
                if (this.dbType == DbType.odps && this.lexer.token == Token.IDENTIFIER && this.lexer.stringVal().startsWith("join@")) {
                    this.lexer.stringVal = this.lexer.stringVal().substring(5);
                    break;
                }
                this.accept(Token.JOIN);
                break;
            }
            case RIGHT: {
                this.lexer.nextToken();
                if (this.lexer.token == Token.OUTER) {
                    this.lexer.nextToken();
                }
                this.accept(Token.JOIN);
                joinType = SQLJoinTableSource.JoinType.RIGHT_OUTER_JOIN;
                break;
            }
            case FULL: {
                this.lexer.nextToken();
                if (this.lexer.token == Token.OUTER) {
                    this.lexer.nextToken();
                }
                this.accept(Token.JOIN);
                joinType = SQLJoinTableSource.JoinType.FULL_OUTER_JOIN;
                break;
            }
            case INNER: {
                this.lexer.nextToken();
                this.accept(Token.JOIN);
                joinType = SQLJoinTableSource.JoinType.INNER_JOIN;
                break;
            }
            case JOIN: {
                this.lexer.nextToken();
                joinType = (natural ? SQLJoinTableSource.JoinType.NATURAL_JOIN : SQLJoinTableSource.JoinType.JOIN);
                break;
            }
            case COMMA: {
                this.lexer.nextToken();
                joinType = SQLJoinTableSource.JoinType.COMMA;
                break;
            }
            case OUTER: {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.APPLY)) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.OUTER_APPLY;
                    break;
                }
                break;
            }
            case STRAIGHT_JOIN:
            case IDENTIFIER: {
                final long hash2 = this.lexer.hash_lower;
                if (hash2 == FnvHash.Constants.STRAIGHT_JOIN) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.STRAIGHT_JOIN;
                    break;
                }
                if (hash2 == FnvHash.Constants.STRAIGHT) {
                    this.lexer.nextToken();
                    this.accept(Token.JOIN);
                    joinType = SQLJoinTableSource.JoinType.STRAIGHT_JOIN;
                    break;
                }
                if (hash2 != FnvHash.Constants.CROSS) {
                    break;
                }
                this.lexer.nextToken();
                if (this.lexer.token == Token.JOIN) {
                    this.lexer.nextToken();
                    joinType = (natural ? SQLJoinTableSource.JoinType.NATURAL_CROSS_JOIN : SQLJoinTableSource.JoinType.CROSS_JOIN);
                    break;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.APPLY)) {
                    this.lexer.nextToken();
                    joinType = SQLJoinTableSource.JoinType.CROSS_APPLY;
                    break;
                }
                break;
            }
        }
        if (joinType != null) {
            final SQLJoinTableSource join = new SQLJoinTableSource();
            join.setLeft(tableSource);
            join.setJoinType(joinType);
            join.setGlobal(global);
            if (asof) {
                join.setAsof(true);
            }
            boolean isBrace = false;
            if (SQLJoinTableSource.JoinType.COMMA == joinType && this.lexer.token == Token.LBRACE) {
                this.lexer.nextToken();
                this.acceptIdentifier("OJ");
                isBrace = true;
            }
            SQLTableSource rightTableSource = null;
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.SELECT || (this.lexer.token == Token.FROM && (this.dbType == DbType.odps || this.dbType == DbType.hive))) {
                    final SQLSelect select = this.select();
                    rightTableSource = new SQLSubqueryTableSource(select);
                }
                else {
                    rightTableSource = this.parseTableSource();
                }
                if (this.lexer.token == Token.UNION || this.lexer.token == Token.EXCEPT || this.lexer.token == Token.MINUS || this.lexer.token == Token.INTERSECT) {
                    if (rightTableSource instanceof SQLSubqueryTableSource) {
                        final SQLSelect select = ((SQLSubqueryTableSource)rightTableSource).getSelect();
                        final SQLSelectQuery query = this.queryRest(select.getQuery(), true);
                        select.setQuery(query);
                    }
                    else if (rightTableSource instanceof SQLUnionQueryTableSource) {
                        final SQLUnionQueryTableSource unionTableSrc = (SQLUnionQueryTableSource)rightTableSource;
                        unionTableSrc.setUnion((SQLUnionQuery)this.queryRest(unionTableSrc.getUnion()));
                    }
                }
                this.accept(Token.RPAREN);
                if (rightTableSource instanceof SQLValuesTableSource && (this.lexer.token == Token.AS || this.lexer.token == Token.IDENTIFIER) && rightTableSource.getAlias() == null && ((SQLValuesTableSource)rightTableSource).getColumns().size() == 0) {
                    if (this.lexer.token == Token.AS) {
                        this.lexer.nextToken();
                    }
                    rightTableSource.setAlias(this.tableAlias(true));
                    if (this.lexer.token == Token.LPAREN) {
                        this.lexer.nextToken();
                        this.exprParser.names(((SQLValuesTableSource)rightTableSource).getColumns(), rightTableSource);
                        this.accept(Token.RPAREN);
                    }
                }
            }
            else if (this.lexer.token() == Token.TABLE) {
                final HiveCreateTableParser createTableParser = new HiveCreateTableParser(this.lexer);
                final HiveCreateTableStatement stmt = (HiveCreateTableStatement)createTableParser.parseCreateTable(false);
                rightTableSource = new SQLAdhocTableSource(stmt);
                this.primaryTableSourceRest(rightTableSource);
            }
            else {
                if (this.lexer.identifierEquals(FnvHash.Constants.UNNEST)) {
                    final Lexer.SavePoint mark2 = this.lexer.mark();
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
                        final String alias2 = this.tableAlias();
                        unnest.setAlias(alias2);
                        if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.exprParser.names(unnest.getColumns(), unnest);
                            this.accept(Token.RPAREN);
                        }
                        final SQLTableSource tableSrc = rightTableSource = this.parseTableSourceRest(unnest);
                    }
                    else {
                        this.lexer.reset(mark2);
                    }
                }
                else if (this.lexer.token == Token.VALUES) {
                    rightTableSource = this.parseValues();
                }
                if (rightTableSource == null) {
                    final boolean aliasToken = this.lexer.token == Token.LITERAL_ALIAS;
                    SQLExpr expr = null;
                    switch (this.lexer.token) {
                        case ALL: {
                            expr = this.exprParser.name();
                            break;
                        }
                        default: {
                            expr = this.expr();
                            break;
                        }
                    }
                    if (aliasToken && expr instanceof SQLCharExpr) {
                        expr = new SQLIdentifierExpr(((SQLCharExpr)expr).getText());
                    }
                    final SQLExprTableSource exprTableSource2 = new SQLExprTableSource(expr);
                    if (expr instanceof SQLMethodInvokeExpr && this.lexer.token == Token.AS) {
                        this.lexer.nextToken();
                        final String alias3 = this.tableAlias(true);
                        exprTableSource2.setAlias(alias3);
                        if (this.lexer.token == Token.LPAREN) {
                            this.lexer.nextToken();
                            this.exprParser.names(exprTableSource2.getColumns(), exprTableSource2);
                            this.accept(Token.RPAREN);
                        }
                    }
                    rightTableSource = exprTableSource2;
                }
                rightTableSource = this.primaryTableSourceRest(rightTableSource);
            }
            if (this.lexer.token == Token.USING || this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                final Lexer.SavePoint savePoint = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    join.setRight(rightTableSource);
                    this.exprParser.exprList(join.getUsing(), join);
                    this.accept(Token.RPAREN);
                }
                else {
                    if (this.lexer.token == Token.IDENTIFIER) {
                        this.lexer.reset(savePoint);
                        join.setRight(rightTableSource);
                        return join;
                    }
                    join.setAlias(this.tableAlias());
                }
            }
            else if (this.lexer.token == Token.STRAIGHT_JOIN || this.lexer.identifierEquals(FnvHash.Constants.STRAIGHT_JOIN)) {
                this.primaryTableSourceRest(rightTableSource);
            }
            else if (rightTableSource.getAlias() == null && !(rightTableSource instanceof SQLValuesTableSource)) {
                final int line = this.lexer.line;
                String tableAlias;
                if (this.lexer.token == Token.AS) {
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.ON) {
                        tableAlias = this.tableAlias(true);
                    }
                    else {
                        tableAlias = null;
                    }
                }
                else {
                    tableAlias = this.tableAlias(false);
                }
                if (tableAlias != null) {
                    rightTableSource.setAlias(tableAlias);
                    if (line + 1 == this.lexer.line && this.lexer.hasComment() && this.lexer.getComments().get(0).startsWith("--")) {
                        rightTableSource.addAfterComment(this.lexer.readAndResetComments());
                    }
                    if (this.lexer.token == Token.LPAREN) {
                        if (rightTableSource instanceof SQLSubqueryTableSource) {
                            this.lexer.nextToken();
                            final List<SQLName> columns = ((SQLSubqueryTableSource)rightTableSource).getColumns();
                            this.exprParser.names(columns, rightTableSource);
                            this.accept(Token.RPAREN);
                        }
                        else if (rightTableSource instanceof SQLExprTableSource && ((SQLExprTableSource)rightTableSource).getExpr() instanceof SQLMethodInvokeExpr) {
                            final List<SQLName> columns = ((SQLExprTableSource)rightTableSource).getColumns();
                            if (columns.size() == 0) {
                                this.lexer.nextToken();
                                this.exprParser.names(columns, rightTableSource);
                                this.accept(Token.RPAREN);
                            }
                        }
                    }
                }
                rightTableSource = this.primaryTableSourceRest(rightTableSource);
            }
            if (this.lexer.token == Token.WITH) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLExpr hintExpr = this.expr();
                    final SQLExprHint hint = new SQLExprHint(hintExpr);
                    hint.setParent(tableSource);
                    rightTableSource.getHints().add(hint);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            join.setRight(rightTableSource);
            if (!natural && !StringUtils.isEmpty(tableSource.getAlias()) && tableSource.aliasHashCode64() == FnvHash.Constants.NATURAL && DbType.mysql == this.dbType) {
                tableSource.setAlias(null);
                natural = true;
            }
            join.setNatural(natural);
            if (this.lexer.token == Token.ON) {
                this.lexer.nextToken();
                final SQLExpr joinOn = this.expr();
                join.setCondition(joinOn);
                while (this.lexer.token == Token.ON) {
                    this.lexer.nextToken();
                    final SQLExpr joinOn2 = this.expr();
                    join.addCondition(joinOn2);
                }
                if (this.dbType == DbType.odps && this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                    final SQLJoinTableSource.UDJ udj = new SQLJoinTableSource.UDJ();
                    this.lexer.nextToken();
                    udj.setFunction(this.exprParser.name());
                    this.accept(Token.LPAREN);
                    this.exprParser.exprList(udj.getArguments(), udj);
                    this.accept(Token.RPAREN);
                    if (this.lexer.token != Token.AS) {
                        udj.setAlias(this.alias());
                    }
                    this.accept(Token.AS);
                    this.accept(Token.LPAREN);
                    this.exprParser.names(udj.getColumns(), udj);
                    this.accept(Token.RPAREN);
                    if (this.lexer.identifierEquals(FnvHash.Constants.SORT)) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        this.exprParser.orderBy(udj.getSortBy(), udj);
                    }
                    if (this.lexer.token == Token.WITH) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("UDFPROPERTIES");
                        this.exprParser.parseAssignItem(udj.getProperties(), udj);
                    }
                    join.setUdj(udj);
                }
            }
            else if (this.lexer.token == Token.USING || this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                final Lexer.SavePoint savePoint = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprParser.exprList(join.getUsing(), join);
                    this.accept(Token.RPAREN);
                }
                else {
                    this.lexer.reset(savePoint);
                }
            }
            final SQLTableSource tableSourceReturn = this.parseTableSourceRest(join);
            if (isBrace) {
                this.accept(Token.RBRACE);
            }
            return this.parseTableSourceRest(tableSourceReturn);
        }
        if ((tableSource.aliasHashCode64() == FnvHash.Constants.LATERAL || this.lexer.token == Token.LATERAL) && this.lexer.token() == Token.VIEW) {
            return this.parseLateralView(tableSource);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LATERAL) || this.lexer.token == Token.LATERAL) {
            this.lexer.nextToken();
            return this.parseLateralView(tableSource);
        }
        return tableSource;
    }
    
    public SQLExpr expr() {
        return this.exprParser.expr();
    }
    
    public SQLOrderBy parseOrderBy() {
        return this.exprParser.parseOrderBy();
    }
    
    public void acceptKeyword(final String ident) {
        if (this.lexer.token == Token.IDENTIFIER && ident.equalsIgnoreCase(this.lexer.stringVal())) {
            this.lexer.nextToken();
            return;
        }
        this.setErrorEndPos(this.lexer.pos());
        throw new ParserException("syntax error, expect " + ident + ", actual " + this.lexer.token + ", " + this.lexer.info());
    }
    
    public void parseFetchClause(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token == Token.LIMIT) {
            final SQLLimit limit = this.exprParser.parseLimit();
            queryBlock.setLimit(limit);
            return;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.OFFSET) || this.lexer.token == Token.OFFSET) {
            this.lexer.nextToken();
            final SQLExpr offset = this.exprParser.expr();
            queryBlock.setOffset(offset);
            if (this.lexer.identifierEquals(FnvHash.Constants.ROW) || this.lexer.identifierEquals(FnvHash.Constants.ROWS)) {
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token == Token.FETCH) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.FIRST || this.lexer.token == Token.NEXT || this.lexer.identifierEquals(FnvHash.Constants.NEXT)) {
                this.lexer.nextToken();
            }
            else {
                this.acceptIdentifier("FIRST");
            }
            final SQLExpr first = this.exprParser.primary();
            queryBlock.setFirst(first);
            if (this.lexer.identifierEquals(FnvHash.Constants.ROW) || this.lexer.identifierEquals(FnvHash.Constants.ROWS)) {
                this.lexer.nextToken();
            }
            if (this.lexer.token == Token.ONLY) {
                this.lexer.nextToken();
            }
            else {
                this.acceptIdentifier("ONLY");
            }
        }
    }
    
    protected void parseHierachical(final SQLSelectQueryBlock queryBlock) {
        if (this.lexer.token == Token.CONNECT || this.lexer.identifierEquals(FnvHash.Constants.CONNECT)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.token == Token.PRIOR || this.lexer.identifierEquals(FnvHash.Constants.PRIOR)) {
                this.lexer.nextToken();
                queryBlock.setPrior(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.NOCYCLE)) {
                queryBlock.setNoCycle(true);
                this.lexer.nextToken();
                if (this.lexer.token == Token.PRIOR) {
                    this.lexer.nextToken();
                    queryBlock.setPrior(true);
                }
            }
            queryBlock.setConnectBy(this.exprParser.expr());
        }
        if (this.lexer.token == Token.START || this.lexer.identifierEquals(FnvHash.Constants.START)) {
            this.lexer.nextToken();
            this.accept(Token.WITH);
            queryBlock.setStartWith(this.exprParser.expr());
        }
        if (this.lexer.token == Token.CONNECT || this.lexer.identifierEquals(FnvHash.Constants.CONNECT)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.token == Token.PRIOR || this.lexer.identifierEquals(FnvHash.Constants.PRIOR)) {
                this.lexer.nextToken();
                queryBlock.setPrior(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.NOCYCLE)) {
                queryBlock.setNoCycle(true);
                this.lexer.nextToken();
                if (this.lexer.token == Token.PRIOR || this.lexer.identifierEquals(FnvHash.Constants.PRIOR)) {
                    this.lexer.nextToken();
                    queryBlock.setPrior(true);
                }
            }
            queryBlock.setConnectBy(this.exprParser.expr());
        }
    }
    
    protected SQLTableSource parseLateralView(final SQLTableSource tableSource) {
        this.accept(Token.VIEW);
        if (tableSource != null && "LATERAL".equalsIgnoreCase(tableSource.getAlias())) {
            tableSource.setAlias(null);
        }
        final SQLLateralViewTableSource lateralViewTabSrc = new SQLLateralViewTableSource();
        lateralViewTabSrc.setTableSource(tableSource);
        if (this.lexer.token == Token.OUTER) {
            lateralViewTabSrc.setOuter(true);
            this.lexer.nextToken();
        }
        final SQLMethodInvokeExpr udtf = (SQLMethodInvokeExpr)this.exprParser.primary();
        lateralViewTabSrc.setMethod(udtf);
        String alias;
        if (this.lexer.token == Token.AS) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.AS) {
                this.lexer.nextToken();
            }
            alias = this.alias();
        }
        else {
            alias = this.as();
        }
        if (alias != null) {
            lateralViewTabSrc.setAlias(alias);
        }
        if (this.lexer.token == Token.AS) {
            this.parseLateralViewAs(lateralViewTabSrc);
        }
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            lateralViewTabSrc.setOn(this.exprParser.expr());
        }
        return this.parseTableSourceRest(lateralViewTabSrc);
    }
    
    public void parseLateralViewAs(final SQLLateralViewTableSource lateralViewTabSrc) {
        this.accept(Token.AS);
        Lexer.SavePoint mark = null;
        while (true) {
            SQLName name;
            if (this.lexer.token == Token.NULL) {
                name = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                name = this.exprParser.name();
                if (name instanceof SQLPropertyExpr) {
                    this.lexer.reset(mark);
                    break;
                }
            }
            name.setParent(lateralViewTabSrc);
            lateralViewTabSrc.getColumns().add(name);
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            mark = this.lexer.mark();
            this.lexer.nextToken();
        }
    }
    
    public SQLValuesTableSource parseValues() {
        this.accept(Token.VALUES);
        final SQLValuesTableSource tableSource = new SQLValuesTableSource();
        while (true) {
            boolean isSingleValue = true;
            if (this.lexer.token == Token.ROW) {
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.LPAREN) {
                this.accept(Token.LPAREN);
                isSingleValue = false;
            }
            final SQLListExpr listExpr = new SQLListExpr();
            if (isSingleValue) {
                final SQLExpr expr = this.expr();
                expr.setParent(listExpr);
                listExpr.getItems().add(expr);
            }
            else {
                this.exprParser.exprList(listExpr.getItems(), listExpr);
                this.accept(Token.RPAREN);
            }
            listExpr.setParent(tableSource);
            tableSource.getValues().add(listExpr);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        final String alias = this.tableAlias();
        if (alias != null) {
            tableSource.setAlias(alias);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.names(tableSource.getColumns(), tableSource);
            this.accept(Token.RPAREN);
        }
        return tableSource;
    }
}
