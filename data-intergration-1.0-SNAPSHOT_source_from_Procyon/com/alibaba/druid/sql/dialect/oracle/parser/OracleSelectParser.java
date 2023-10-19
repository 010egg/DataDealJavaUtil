// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivotBase;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLFlashbackExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collection;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLSelectParser;

public class OracleSelectParser extends SQLSelectParser
{
    public OracleSelectParser(final String sql) {
        super(new OracleExprParser(sql));
    }
    
    public OracleSelectParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    public OracleSelectParser(final SQLExprParser exprParser, final SQLSelectListCache selectListCache) {
        super(exprParser, selectListCache);
    }
    
    @Override
    public SQLSelect select() {
        final SQLSelect select = new SQLSelect();
        if (this.lexer.token() == Token.WITH) {
            final SQLWithSubqueryClause with = this.parseWith();
            select.setWithSubQuery(with);
        }
        final SQLSelectQuery query = this.query();
        select.setQuery(query);
        SQLOrderBy orderBy = this.parseOrderBy();
        OracleSelectQueryBlock queryBlock = null;
        if (query instanceof SQLSelectQueryBlock) {
            queryBlock = (OracleSelectQueryBlock)query;
            if (queryBlock.getOrderBy() == null) {
                queryBlock.setOrderBy(orderBy);
            }
            else {
                select.setOrderBy(orderBy);
            }
            if (orderBy != null) {
                this.parseFetchClause(queryBlock);
                select.setQuery(this.queryRest(queryBlock, true));
            }
        }
        else {
            select.setOrderBy(orderBy);
        }
        if (this.lexer.token() == Token.FOR) {
            if (queryBlock == null) {
                throw new ParserException("TODO. " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.UPDATE);
            queryBlock.setForUpdate(true);
            if (this.lexer.token() == Token.OF) {
                this.lexer.nextToken();
                this.exprParser.exprList(queryBlock.getForUpdateOf(), queryBlock);
            }
            else if (this.lexer.token() == Token.LPAREN && queryBlock.isForUpdate()) {
                this.exprParser.exprList(queryBlock.getForUpdateOf(), queryBlock);
            }
            if (this.lexer.token() == Token.NOWAIT) {
                this.lexer.nextToken();
                queryBlock.setNoWait(true);
            }
            else if (this.lexer.token() == Token.WAIT) {
                this.lexer.nextToken();
                queryBlock.setWaitTime(this.exprParser.primary());
            }
            else if (this.lexer.identifierEquals("SKIP")) {
                this.lexer.nextToken();
                this.acceptIdentifier("LOCKED");
                queryBlock.setSkipLocked(true);
            }
        }
        if (this.lexer.token() == Token.ORDER) {
            orderBy = this.exprParser.parseOrderBy();
            if (queryBlock != null && queryBlock.getOrderBy() == null) {
                queryBlock.setOrderBy(orderBy);
            }
            else {
                if (select.getOrderBy() != null) {
                    throw new ParserException("illegal state.");
                }
                select.setOrderBy(orderBy);
            }
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            OracleSelectRestriction restriction = null;
            if (this.lexer.identifierEquals("READ")) {
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals("ONLY")) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                this.lexer.nextToken();
                restriction = new OracleSelectRestriction.ReadOnly();
            }
            else {
                if (this.lexer.token() != Token.CHECK) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals("OPTION")) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                this.lexer.nextToken();
                restriction = new OracleSelectRestriction.CheckOption();
            }
            if (this.lexer.token() == Token.CONSTRAINT) {
                this.lexer.nextToken();
                final String constraintName = this.lexer.stringVal();
                final SQLName constraint = new SQLIdentifierExpr(constraintName);
                restriction.setConstraint(constraint);
                this.lexer.nextToken();
            }
            select.setRestriction(restriction);
        }
        return select;
    }
    
    @Override
    public SQLWithSubqueryClause parseWith() {
        this.accept(Token.WITH);
        final SQLWithSubqueryClause subqueryFactoringClause = new SQLWithSubqueryClause();
        while (true) {
            final OracleWithSubqueryEntry entry = new OracleWithSubqueryEntry();
            final String alias = this.lexer.stringVal();
            this.lexer.nextToken();
            entry.setAlias(alias);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprParser.names(entry.getColumns());
                this.accept(Token.RPAREN);
            }
            this.accept(Token.AS);
            this.accept(Token.LPAREN);
            entry.setSubQuery(this.select());
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals("SEARCH")) {
                this.lexer.nextToken();
                final SearchClause searchClause = new SearchClause();
                if (this.lexer.token() != Token.IDENTIFIER) {
                    throw new ParserException("syntax erorr : " + this.lexer.token());
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.DEPTH)) {
                    this.lexer.nextToken();
                    searchClause.setType(SearchClause.Type.DEPTH);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.BREADTH)) {
                    this.lexer.nextToken();
                    searchClause.setType(SearchClause.Type.BREADTH);
                }
                else {
                    searchClause.setType(SearchClause.Type.valueOf(this.lexer.stringVal().toUpperCase()));
                    this.lexer.nextToken();
                }
                this.acceptIdentifier("FIRST");
                this.accept(Token.BY);
                searchClause.addItem(this.exprParser.parseSelectOrderByItem());
                while (this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    searchClause.addItem(this.exprParser.parseSelectOrderByItem());
                }
                this.accept(Token.SET);
                searchClause.setOrderingColumn((SQLIdentifierExpr)this.exprParser.name());
                entry.setSearchClause(searchClause);
            }
            if (this.lexer.identifierEquals("CYCLE")) {
                this.lexer.nextToken();
                final CycleClause cycleClause = new CycleClause();
                this.exprParser.exprList(cycleClause.getAliases(), cycleClause);
                this.accept(Token.SET);
                cycleClause.setMark(this.exprParser.expr());
                this.accept(Token.TO);
                cycleClause.setValue(this.exprParser.expr());
                this.accept(Token.DEFAULT);
                cycleClause.setDefaultValue(this.exprParser.expr());
                entry.setCycleClause(cycleClause);
            }
            subqueryFactoringClause.addEntry(entry);
            if (this.lexer.token() != Token.COMMA) {
                return subqueryFactoringClause;
            }
            this.lexer.nextToken();
        }
    }
    
    @Override
    public SQLSelectQuery query(final SQLObject parent, final boolean acceptUnion) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLSelectQuery select = this.query();
            this.accept(Token.RPAREN);
            return this.queryRest(select, acceptUnion);
        }
        final OracleSelectQueryBlock queryBlock = new OracleSelectQueryBlock();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            queryBlock.addBeforeComment(this.lexer.readAndResetComments());
        }
        if (this.lexer.token() == Token.SELECT) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }
            this.parseHints(queryBlock);
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
            this.exprParser.parseHints(queryBlock.getHints());
            this.parseSelectList(queryBlock);
        }
        this.parseInto(queryBlock);
        this.parseFrom(queryBlock);
        this.parseWhere(queryBlock);
        this.parseHierachical(queryBlock);
        this.parseGroupBy(queryBlock);
        this.parseModelClause(queryBlock);
        this.parseFetchClause(queryBlock);
        return this.queryRest(queryBlock, acceptUnion);
    }
    
    @Override
    public SQLSelectQuery queryRest(SQLSelectQuery selectQuery, final boolean acceptUnion) {
        if (!acceptUnion) {
            return selectQuery;
        }
        if (this.lexer.token() == Token.UNION) {
            do {
                final SQLUnionQuery union = new SQLUnionQuery();
                union.setLeft(selectQuery);
                this.lexer.nextToken();
                if (this.lexer.token() == Token.ALL) {
                    union.setOperator(SQLUnionOperator.UNION_ALL);
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.DISTINCT) {
                    union.setOperator(SQLUnionOperator.DISTINCT);
                    this.lexer.nextToken();
                }
                final SQLSelectQuery right = this.query(null, false);
                union.setRight(right);
                selectQuery = union;
            } while (this.lexer.token() == Token.UNION);
            selectQuery = this.queryRest(selectQuery, true);
            return selectQuery;
        }
        if (this.lexer.token() == Token.INTERSECT) {
            this.lexer.nextToken();
            final SQLUnionQuery union = new SQLUnionQuery();
            union.setLeft(selectQuery);
            union.setOperator(SQLUnionOperator.INTERSECT);
            final SQLSelectQuery right = this.query(null, false);
            union.setRight(right);
            return this.queryRest(union, true);
        }
        if (this.lexer.token() == Token.MINUS) {
            this.lexer.nextToken();
            final SQLUnionQuery union = new SQLUnionQuery();
            union.setLeft(selectQuery);
            union.setOperator(SQLUnionOperator.MINUS);
            final SQLSelectQuery right = this.query(null, false);
            union.setRight(right);
            return this.queryRest(union, true);
        }
        return selectQuery;
    }
    
    private void parseModelClause(final OracleSelectQueryBlock queryBlock) {
        final Lexer.SavePoint savePoint = this.lexer.mark();
        if (!this.lexer.identifierEquals(FnvHash.Constants.MODEL)) {
            return;
        }
        this.lexer.nextToken();
        final ModelClause model = new ModelClause();
        this.parseCellReferenceOptions(model.getCellReferenceOptions());
        if (this.lexer.identifierEquals(FnvHash.Constants.RETURN)) {
            this.lexer.nextToken();
            final ModelClause.ReturnRowsClause returnRowsClause = new ModelClause.ReturnRowsClause();
            if (this.lexer.token() == Token.ALL) {
                this.lexer.nextToken();
                returnRowsClause.setAll(true);
            }
            else {
                this.acceptIdentifier("UPDATED");
            }
            this.acceptIdentifier("ROWS");
            model.setReturnRowsClause(returnRowsClause);
        }
        while (this.lexer.identifierEquals(FnvHash.Constants.REFERENCE)) {
            final ModelClause.ReferenceModelClause referenceModelClause = new ModelClause.ReferenceModelClause();
            this.lexer.nextToken();
            final SQLExpr name = this.expr();
            referenceModelClause.setName(name);
            this.accept(Token.ON);
            this.accept(Token.LPAREN);
            final SQLSelect subQuery = this.select();
            this.accept(Token.RPAREN);
            referenceModelClause.setSubQuery(subQuery);
            this.parseModelColumnClause(referenceModelClause);
            this.parseCellReferenceOptions(referenceModelClause.getCellReferenceOptions());
            model.getReferenceModelClauses().add(referenceModelClause);
        }
        this.parseMainModelClause(model);
        queryBlock.setModelClause(model);
    }
    
    private void parseMainModelClause(final ModelClause modelClause) {
        final ModelClause.MainModelClause mainModel = new ModelClause.MainModelClause();
        if (this.lexer.identifierEquals("MAIN")) {
            this.lexer.nextToken();
            mainModel.setMainModelName(this.expr());
        }
        final ModelClause.ModelColumnClause modelColumnClause = new ModelClause.ModelColumnClause();
        this.parseQueryPartitionClause(modelColumnClause);
        mainModel.setModelColumnClause(modelColumnClause);
        this.acceptIdentifier("DIMENSION");
        this.accept(Token.BY);
        this.accept(Token.LPAREN);
        while (this.lexer.token() != Token.RPAREN) {
            final ModelClause.ModelColumn column = new ModelClause.ModelColumn();
            column.setExpr(this.expr());
            column.setAlias(this.as());
            modelColumnClause.getDimensionByColumns().add(column);
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
            }
        }
        this.lexer.nextToken();
        this.acceptIdentifier("MEASURES");
        this.accept(Token.LPAREN);
        while (this.lexer.token() != Token.RPAREN) {
            final ModelClause.ModelColumn column = new ModelClause.ModelColumn();
            column.setExpr(this.expr());
            column.setAlias(this.as());
            modelColumnClause.getMeasuresColumns().add(column);
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
            }
        }
        this.lexer.nextToken();
        mainModel.setModelColumnClause(modelColumnClause);
        this.parseCellReferenceOptions(mainModel.getCellReferenceOptions());
        this.parseModelRulesClause(mainModel);
        modelClause.setMainModel(mainModel);
    }
    
    private void parseModelRulesClause(final ModelClause.MainModelClause mainModel) {
        final ModelClause.ModelRulesClause modelRulesClause = new ModelClause.ModelRulesClause();
        if (this.lexer.identifierEquals("RULES")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.UPDATE) {
                modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.UPDATE);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals("UPSERT")) {
                modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.UPSERT);
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals("AUTOMATIC")) {
                this.lexer.nextToken();
                this.accept(Token.ORDER);
                modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.AUTOMATIC_ORDER);
            }
            else if (this.lexer.identifierEquals("SEQUENTIAL")) {
                this.lexer.nextToken();
                this.accept(Token.ORDER);
                modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.SEQUENTIAL_ORDER);
            }
        }
        if (this.lexer.identifierEquals("ITERATE")) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            modelRulesClause.setIterate(this.expr());
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals("UNTIL")) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                modelRulesClause.setUntil(this.expr());
                this.accept(Token.RPAREN);
            }
        }
        this.accept(Token.LPAREN);
        while (this.lexer.token() != Token.RPAREN) {
            final ModelClause.CellAssignmentItem item = new ModelClause.CellAssignmentItem();
            if (this.lexer.token() == Token.UPDATE) {
                item.setOption(ModelClause.ModelRuleOption.UPDATE);
            }
            else if (this.lexer.identifierEquals("UPSERT")) {
                item.setOption(ModelClause.ModelRuleOption.UPSERT);
            }
            item.setCellAssignment(this.parseCellAssignment());
            item.setOrderBy(this.parseOrderBy());
            this.accept(Token.EQ);
            final SQLExpr expr = this.expr();
            item.setExpr(expr);
            modelRulesClause.getCellAssignmentItems().add(item);
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
            }
        }
        this.lexer.nextToken();
        mainModel.setModelRulesClause(modelRulesClause);
    }
    
    private ModelClause.CellAssignment parseCellAssignment() {
        final ModelClause.CellAssignment cellAssignment = new ModelClause.CellAssignment();
        cellAssignment.setMeasureColumn(this.exprParser.name());
        this.accept(Token.LBRACKET);
        this.exprParser.exprList(cellAssignment.getConditions(), cellAssignment);
        this.accept(Token.RBRACKET);
        return cellAssignment;
    }
    
    private void parseQueryPartitionClause(final ModelClause.ModelColumnClause modelColumnClause) {
        if (this.lexer.token() == Token.PARTITION) {
            final ModelClause.QueryPartitionClause queryPartitionClause = new ModelClause.QueryPartitionClause();
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprParser.exprList(queryPartitionClause.getExprList(), queryPartitionClause);
                this.accept(Token.RPAREN);
            }
            else {
                this.exprParser.exprList(queryPartitionClause.getExprList(), queryPartitionClause);
            }
            modelColumnClause.setQueryPartitionClause(queryPartitionClause);
        }
    }
    
    private void parseModelColumnClause(final ModelClause.ReferenceModelClause referenceModelClause) {
        throw new ParserException();
    }
    
    private void parseCellReferenceOptions(final List<ModelClause.CellReferenceOption> options) {
        if (this.lexer.identifierEquals(FnvHash.Constants.IGNORE)) {
            this.lexer.nextToken();
            this.acceptIdentifier("NAV");
            options.add(ModelClause.CellReferenceOption.IgnoreNav);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.KEEP)) {
            this.lexer.nextToken();
            this.acceptIdentifier("NAV");
            options.add(ModelClause.CellReferenceOption.KeepNav);
        }
        if (this.lexer.token() == Token.UNIQUE) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("DIMENSION")) {
                this.lexer.nextToken();
                options.add(ModelClause.CellReferenceOption.UniqueDimension);
            }
            else {
                this.acceptIdentifier("SINGLE");
                this.acceptIdentifier("REFERENCE");
                options.add(ModelClause.CellReferenceOption.UniqueDimension);
            }
        }
    }
    
    @Override
    public SQLTableSource parseTableSource() {
        final SQLTableSource tableSource = this.parseTableSourcePrimary();
        if (tableSource instanceof OracleSelectTableSource) {
            return this.parseTableSourceRest((OracleSelectTableSource)tableSource);
        }
        return this.parseTableSourceRest(tableSource);
    }
    
    public SQLTableSource parseTableSourcePrimary() {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            OracleSelectTableSource tableSource;
            if (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.WITH) {
                tableSource = new OracleSelectSubqueryTableSource(this.select());
            }
            else if (this.lexer.token() == Token.LPAREN) {
                tableSource = (OracleSelectTableSource)this.parseTableSource();
            }
            else {
                if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_ALIAS) {
                    final SQLTableSource identTable = this.parseTableSource();
                    this.accept(Token.RPAREN);
                    this.parsePivot((OracleSelectTableSource)identTable);
                    return identTable;
                }
                throw new ParserException("TODO :" + this.lexer.info());
            }
            this.accept(Token.RPAREN);
            if ((this.lexer.token() == Token.UNION || this.lexer.token() == Token.MINUS || this.lexer.token() == Token.EXCEPT) && tableSource instanceof OracleSelectSubqueryTableSource) {
                final OracleSelectSubqueryTableSource selectSubqueryTableSource = (OracleSelectSubqueryTableSource)tableSource;
                final SQLSelect select = selectSubqueryTableSource.getSelect();
                final SQLSelectQuery selectQuery = this.queryRest(select.getQuery(), true);
                select.setQuery(selectQuery);
            }
            this.parsePivot(tableSource);
            return tableSource;
        }
        if (this.lexer.token() == Token.SELECT) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
        final OracleSelectTableReference tableReference = new OracleSelectTableReference();
        if (this.lexer.identifierEquals("ONLY")) {
            this.lexer.nextToken();
            tableReference.setOnly(true);
            this.accept(Token.LPAREN);
            this.parseTableSourceQueryTableExpr(tableReference);
            this.accept(Token.RPAREN);
        }
        else {
            this.parseTableSourceQueryTableExpr(tableReference);
            this.parsePivot(tableReference);
        }
        return tableReference;
    }
    
    private void parseTableSourceQueryTableExpr(final OracleSelectTableReference tableReference) {
        tableReference.setExpr(this.exprParser.expr());
        if (this.lexer.identifierEquals("SAMPLE")) {
            this.lexer.nextToken();
            final SampleClause sample = new SampleClause();
            if (this.lexer.identifierEquals("BLOCK")) {
                sample.setBlock(true);
                this.lexer.nextToken();
            }
            this.accept(Token.LPAREN);
            this.exprParser.exprList(sample.getPercent(), sample);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals("SEED")) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                sample.setSeedValue(this.expr());
                this.accept(Token.RPAREN);
            }
            tableReference.setSampleClause(sample);
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            final PartitionExtensionClause partition = new PartitionExtensionClause();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                partition.setPartition(this.exprParser.name());
                this.accept(Token.RPAREN);
            }
            else if (this.lexer.token() == Token.BY) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                partition.setPartition(this.exprParser.name());
                this.accept(Token.RPAREN);
            }
            else {
                this.accept(Token.FOR);
                this.accept(Token.LPAREN);
                this.exprParser.names(partition.getFor());
                this.accept(Token.RPAREN);
            }
            tableReference.setPartition(partition);
        }
        if (this.lexer.identifierEquals("SUBPARTITION")) {
            this.lexer.nextToken();
            final PartitionExtensionClause partition = new PartitionExtensionClause();
            partition.setSubPartition(true);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                partition.setPartition(this.exprParser.name());
                this.accept(Token.RPAREN);
            }
            else {
                this.accept(Token.FOR);
                this.accept(Token.LPAREN);
                this.exprParser.names(partition.getFor());
                this.accept(Token.RPAREN);
            }
            tableReference.setPartition(partition);
        }
        if (this.lexer.identifierEquals("VERSIONS")) {
            final SQLBetweenExpr betweenExpr = new SQLBetweenExpr();
            betweenExpr.setTestExpr(new SQLIdentifierExpr("VERSIONS"));
            this.lexer.nextToken();
            this.accept(Token.BETWEEN);
            final SQLFlashbackExpr start = new SQLFlashbackExpr();
            if (this.lexer.identifierEquals("SCN")) {
                this.lexer.nextToken();
                start.setType(SQLFlashbackExpr.Type.SCN);
            }
            else {
                this.acceptIdentifier("TIMESTAMP");
                start.setType(SQLFlashbackExpr.Type.TIMESTAMP);
            }
            final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)this.exprParser.expr();
            if (binaryExpr.getOperator() != SQLBinaryOperator.BooleanAnd) {
                throw new ParserException("syntax error : " + binaryExpr.getOperator() + ", " + this.lexer.info());
            }
            start.setExpr(binaryExpr.getLeft());
            betweenExpr.setBeginExpr(start);
            betweenExpr.setEndExpr(binaryExpr.getRight());
            tableReference.setFlashback(betweenExpr);
        }
    }
    
    private SQLExpr flashback() {
        this.accept(Token.OF);
        if (this.lexer.identifierEquals("SCN")) {
            this.lexer.nextToken();
            return new SQLFlashbackExpr(SQLFlashbackExpr.Type.SCN, this.expr());
        }
        if (this.lexer.identifierEquals("SNAPSHOT")) {
            return this.expr();
        }
        this.lexer.nextToken();
        return new SQLFlashbackExpr(SQLFlashbackExpr.Type.TIMESTAMP, this.expr());
    }
    
    @Override
    protected SQLTableSource primaryTableSourceRest(final SQLTableSource tableSource) {
        if (tableSource instanceof OracleSelectTableSource && this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OF) {
                ((OracleSelectTableSource)tableSource).setFlashback(this.flashback());
            }
            tableSource.setAlias(this.tableAlias());
        }
        return tableSource;
    }
    
    protected SQLTableSource parseTableSourceRest(final OracleSelectTableSource tableSource) {
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OF) {
                tableSource.setFlashback(this.flashback());
                return this.parseTableSourceRest(tableSource);
            }
            tableSource.setAlias(this.tableAlias(true));
        }
        else if ((tableSource.getAlias() == null || tableSource.getAlias().length() == 0) && this.lexer.token() != Token.LEFT && this.lexer.token() != Token.RIGHT && this.lexer.token() != Token.FULL) {
            final String tableAlias = this.tableAlias();
            tableSource.setAlias(tableAlias);
        }
        if (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(tableSource.getHints());
        }
        SQLJoinTableSource.JoinType joinType = null;
        if (this.lexer.token() == Token.LEFT) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OUTER) {
                this.lexer.nextToken();
            }
            this.accept(Token.JOIN);
            joinType = SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN;
        }
        if (this.lexer.token() == Token.RIGHT) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OUTER) {
                this.lexer.nextToken();
            }
            this.accept(Token.JOIN);
            joinType = SQLJoinTableSource.JoinType.RIGHT_OUTER_JOIN;
        }
        if (this.lexer.token() == Token.FULL) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.OUTER) {
                this.lexer.nextToken();
            }
            this.accept(Token.JOIN);
            joinType = SQLJoinTableSource.JoinType.FULL_OUTER_JOIN;
        }
        final boolean natural = this.lexer.identifierEquals(FnvHash.Constants.NATURAL);
        if (natural) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.INNER) {
            this.lexer.nextToken();
            this.accept(Token.JOIN);
            if (natural) {
                joinType = SQLJoinTableSource.JoinType.NATURAL_INNER_JOIN;
            }
            else {
                joinType = SQLJoinTableSource.JoinType.INNER_JOIN;
            }
        }
        if (this.lexer.token() == Token.CROSS) {
            this.lexer.nextToken();
            this.accept(Token.JOIN);
            joinType = SQLJoinTableSource.JoinType.CROSS_JOIN;
        }
        if (this.lexer.token() == Token.JOIN) {
            this.lexer.nextToken();
            if (natural) {
                joinType = SQLJoinTableSource.JoinType.NATURAL_JOIN;
            }
            else {
                joinType = SQLJoinTableSource.JoinType.JOIN;
            }
        }
        if (this.lexer.token() == Token.COMMA) {
            this.lexer.nextToken();
            joinType = SQLJoinTableSource.JoinType.COMMA;
        }
        if (joinType != null) {
            final OracleSelectJoin join = new OracleSelectJoin();
            join.setLeft(tableSource);
            join.setJoinType(joinType);
            final SQLTableSource right = this.parseTableSourcePrimary();
            final String tableAlias2 = this.tableAlias();
            right.setAlias(tableAlias2);
            join.setRight(right);
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                join.setCondition(this.exprParser.expr());
                if (this.lexer.token() == Token.ON && tableSource instanceof SQLJoinTableSource && ((SQLJoinTableSource)tableSource).getCondition() == null) {
                    this.lexer.nextToken();
                    final SQLExpr leftCondidition = this.exprParser.expr();
                    ((SQLJoinTableSource)tableSource).setCondition(leftCondidition);
                }
            }
            else if (this.lexer.token() == Token.USING) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                this.exprParser.exprList(join.getUsing(), join);
                this.accept(Token.RPAREN);
            }
            this.parsePivot(join);
            return this.parseTableSourceRest(join);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PIVOT)) {
            this.parsePivot(tableSource);
        }
        return tableSource;
    }
    
    private void parsePivot(final OracleSelectTableSource tableSource) {
        if (this.lexer.identifierEquals(FnvHash.Constants.PIVOT)) {
            this.lexer.nextToken();
            final OracleSelectPivot pivot = new OracleSelectPivot();
            if (this.lexer.identifierEquals("XML")) {
                this.lexer.nextToken();
                pivot.setXml(true);
            }
            this.accept(Token.LPAREN);
            while (true) {
                final OracleSelectPivot.Item item = new OracleSelectPivot.Item();
                item.setExpr(this.exprParser.expr());
                item.setAlias(this.as());
                pivot.addItem(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.FOR);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                while (true) {
                    pivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            else {
                pivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
            this.accept(Token.IN);
            this.accept(Token.LPAREN);
            if (this.lexer.token() == Token.SELECT) {
                final SQLExpr expr = this.exprParser.expr();
                final OracleSelectPivot.Item item = new OracleSelectPivot.Item();
                item.setExpr(expr);
                pivot.getPivotIn().add(item);
            }
            else {
                while (true) {
                    final OracleSelectPivot.Item item = new OracleSelectPivot.Item();
                    item.setExpr(this.exprParser.expr());
                    item.setAlias(this.as());
                    pivot.getPivotIn().add(item);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            this.accept(Token.RPAREN);
            this.accept(Token.RPAREN);
            tableSource.setPivot(pivot);
        }
        else if (this.lexer.identifierEquals("UNPIVOT")) {
            this.lexer.nextToken();
            final OracleSelectUnPivot unPivot = new OracleSelectUnPivot();
            if (this.lexer.identifierEquals("INCLUDE")) {
                this.lexer.nextToken();
                this.acceptIdentifier("NULLS");
                unPivot.setNullsIncludeType(OracleSelectUnPivot.NullsIncludeType.INCLUDE_NULLS);
            }
            else if (this.lexer.identifierEquals("EXCLUDE")) {
                this.lexer.nextToken();
                this.acceptIdentifier("NULLS");
                unPivot.setNullsIncludeType(OracleSelectUnPivot.NullsIncludeType.EXCLUDE_NULLS);
            }
            this.accept(Token.LPAREN);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprParser.exprList(unPivot.getItems(), unPivot);
                this.accept(Token.RPAREN);
            }
            else {
                unPivot.addItem(this.exprParser.expr());
            }
            this.accept(Token.FOR);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                while (true) {
                    unPivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            else {
                unPivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
            this.accept(Token.IN);
            this.accept(Token.LPAREN);
            if (this.lexer.token() == Token.LPAREN) {
                throw new ParserException("TODO. " + this.lexer.info());
            }
            if (this.lexer.token() == Token.SELECT) {
                throw new ParserException("TODO. " + this.lexer.info());
            }
            while (true) {
                final OracleSelectPivot.Item item = new OracleSelectPivot.Item();
                item.setExpr(this.exprParser.expr());
                item.setAlias(this.as());
                unPivot.getPivotIn().add(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
            this.accept(Token.RPAREN);
            tableSource.setPivot(unPivot);
        }
    }
    
    protected void parseInto(final OracleSelectQueryBlock x) {
        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.FROM) {
                return;
            }
            final SQLExpr expr = this.expr();
            if (this.lexer.token() != Token.COMMA) {
                x.setInto(expr);
                return;
            }
            final SQLListExpr list = new SQLListExpr();
            list.addItem(expr);
            while (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
                list.addItem(this.expr());
            }
            x.setInto(list);
        }
    }
    
    private void parseHints(final OracleSelectQueryBlock queryBlock) {
        this.exprParser.parseHints(queryBlock.getHints());
    }
}
