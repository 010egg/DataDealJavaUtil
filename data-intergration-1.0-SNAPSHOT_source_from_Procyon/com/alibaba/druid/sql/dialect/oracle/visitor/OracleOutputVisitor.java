// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRunStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsOfTypeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePipeRowStatement;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleProcedureDataType;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleFunctionDataType;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTypeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleTreatExpr;
import com.alibaba.druid.sql.ast.SQLArgument;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalLogGrp;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalIdKey;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRaiseStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleContinueStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleXmlColumnProperties;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleConstraint;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterProcedureStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivotBase;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLScriptCommitStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class OracleOutputVisitor extends SQLASTOutputVisitor implements OracleASTVisitor
{
    private final boolean printPostSemi;
    
    public OracleOutputVisitor(final Appendable appender) {
        this(appender, true);
    }
    
    public OracleOutputVisitor(final Appendable appender, final boolean printPostSemi) {
        super(appender);
        this.dbType = DbType.oracle;
        this.printPostSemi = printPostSemi;
    }
    
    public boolean isPrintPostSemi() {
        return this.printPostSemi;
    }
    
    private void printHints(final List<SQLHint> hints) {
        if (hints.size() > 0) {
            this.print0("/*+ ");
            this.printAndAccept(hints, ", ");
            this.print0(" */");
        }
    }
    
    @Override
    public boolean visit(final OracleAnalytic x) {
        this.print0(this.ucase ? "(" : "(");
        boolean space = false;
        if (x.getPartitionBy().size() > 0) {
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            this.printAndAccept(x.getPartitionBy(), ", ");
            space = true;
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            if (space) {
                this.print(' ');
            }
            this.visit(orderBy);
            space = true;
        }
        final OracleAnalyticWindowing windowing = x.getWindowing();
        if (windowing != null) {
            if (space) {
                this.print(' ');
            }
            this.visit(windowing);
        }
        if (x.isWindowingPreceding()) {
            this.print0(this.ucase ? " PRECEDING" : " preceding");
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleAnalyticWindowing x) {
        this.print0(x.getType().name().toUpperCase());
        this.print(' ');
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLBetweenExpr && x.getParent() instanceof SQLOver) {
            final SQLOver over = (SQLOver)x.getParent();
            final SQLBetweenExpr betweenExpr = (SQLBetweenExpr)expr;
            final SQLOver.WindowingBound beginBound = over.getWindowingBetweenBeginBound();
            if (beginBound != null) {
                this.print0(this.ucase ? " BETWEEN " : " between ");
                betweenExpr.getBeginExpr().accept(this);
                this.print(' ');
                this.print0(this.ucase ? beginBound.name : beginBound.name_lower);
                this.print0(this.ucase ? " AND " : " and ");
                betweenExpr.getEndExpr().accept(this);
                return false;
            }
        }
        expr.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleDeleteStatement x) {
        this.print0(this.ucase ? "DELETE " : "delete ");
        final SQLTableSource tableSource = x.getTableSource();
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        this.print0(this.ucase ? "FROM " : "from ");
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            x.getTableName().accept(this);
            this.print(')');
            this.printAlias(x.getAlias());
        }
        else {
            x.getTableSource().accept(this);
        }
        if (x.getWhere() != null) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().accept(this);
            --this.indentCount;
        }
        if (x.getReturning() != null) {
            this.println();
            x.getReturning().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleIntervalExpr x) {
        final SQLExpr value = x.getValue();
        if (value instanceof SQLLiteralExpr || value instanceof SQLVariantRefExpr) {
            this.print0(this.ucase ? "INTERVAL " : "interval ");
            value.accept(this);
            this.print(' ');
        }
        else {
            this.print('(');
            value.accept(this);
            this.print0(") ");
        }
        this.print0(x.getType().name());
        if (x.getPrecision() != null) {
            this.print('(');
            this.printExpr(x.getPrecision(), this.parameterized);
            if (x.getFactionalSecondsPrecision() != null) {
                this.print0(", ");
                this.print(x.getFactionalSecondsPrecision());
            }
            this.print(')');
        }
        if (x.getToType() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            this.print0(x.getToType().name());
            if (x.getToFactionalSecondsPrecision() != null) {
                this.print('(');
                this.printExpr(x.getToFactionalSecondsPrecision(), this.parameterized);
                this.print(')');
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleOuterExpr x) {
        x.getExpr().accept(this);
        this.print0("(+)");
        return false;
    }
    
    @Override
    public boolean visit(final SQLScriptCommitStatement astNode) {
        this.print('/');
        this.println();
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        final SQLWithSubqueryClause with = x.getWithSubQuery();
        if (with != null) {
            with.accept(this);
            this.println();
        }
        final SQLSelectQuery query = x.getQuery();
        query.accept(this);
        if (x.getRestriction() != null) {
            this.println();
            this.print("WITH ");
            x.getRestriction().accept(this);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            boolean hasFirst = false;
            if (query instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                hasFirst = (queryBlock.getFirst() != null);
            }
            if (!hasFirst) {
                this.println();
                orderBy.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectJoin x) {
        x.getLeft().accept(this);
        final SQLTableSource right = x.getRight();
        if (x.getJoinType() == SQLJoinTableSource.JoinType.COMMA) {
            this.print0(", ");
            x.getRight().accept(this);
        }
        else {
            final boolean isRoot = x.getParent() instanceof SQLSelectQueryBlock;
            if (isRoot) {
                ++this.indentCount;
            }
            this.println();
            this.print0(this.ucase ? x.getJoinType().name : x.getJoinType().name_lcase);
            this.print(' ');
            if (right instanceof SQLJoinTableSource) {
                this.print('(');
                this.incrementIndent();
                this.println();
                right.accept(this);
                this.decrementIndent();
                this.println();
                this.print(')');
            }
            else {
                right.accept(this);
            }
            if (isRoot) {
                --this.indentCount;
            }
            if (x.getCondition() != null) {
                this.print0(this.ucase ? " ON " : " on ");
                this.incrementIndent();
                x.getCondition().accept(this);
                this.decrementIndent();
                this.print(' ');
            }
            if (x.getUsing().size() > 0) {
                this.print0(this.ucase ? " USING (" : " using (");
                this.printAndAccept(x.getUsing(), ", ");
                this.print(')');
            }
            this.printFlashback(x.getFlashback());
        }
        final OracleSelectPivotBase pivot = x.getPivot();
        if (pivot != null) {
            this.println();
            pivot.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectOrderByItem x) {
        x.getExpr().accept(this);
        if (x.getType() != null) {
            this.print(' ');
            final String typeName = x.getType().name();
            this.print0(this.ucase ? typeName.toUpperCase() : typeName.toLowerCase());
        }
        if (x.getNullsOrderType() != null) {
            this.print(' ');
            this.print0(x.getNullsOrderType().toFormalString());
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectPivot x) {
        this.print0(this.ucase ? "PIVOT" : "pivot");
        if (x.isXml()) {
            this.print0(this.ucase ? " XML" : " xml");
        }
        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        if (x.getPivotFor().size() > 0) {
            this.print0(this.ucase ? " FOR " : " for ");
            if (x.getPivotFor().size() == 1) {
                x.getPivotFor().get(0).accept(this);
            }
            else {
                this.print('(');
                this.printAndAccept(x.getPivotFor(), ", ");
                this.print(')');
            }
        }
        if (x.getPivotIn().size() > 0) {
            this.print0(this.ucase ? " IN (" : " in (");
            this.printAndAccept(x.getPivotIn(), ", ");
            this.print(')');
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectPivot.Item x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null && x.getAlias().length() > 0) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectQueryBlock select) {
        if (select instanceof OracleSelectQueryBlock) {
            return this.visit((OracleSelectQueryBlock)select);
        }
        return super.visit(select);
    }
    
    @Override
    public boolean visit(final OracleSelectQueryBlock x) {
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "SELECT " : "select ");
        if (x.getHintsSize() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        if (1 == x.getDistionOption()) {
            this.print0(this.ucase ? "ALL " : "all ");
        }
        else if (2 == x.getDistionOption()) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
        }
        else if (3 == x.getDistionOption()) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }
        this.printSelectList(x.getSelectList());
        if (x.getInto() != null) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
        }
        this.println();
        this.print0(this.ucase ? "FROM " : "from ");
        if (x.getFrom() == null) {
            this.print0(this.ucase ? "DUAL" : "dual");
        }
        else {
            x.getFrom().accept(this);
        }
        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().accept(this);
        }
        this.printHierarchical(x);
        if (x.getGroupBy() != null) {
            this.println();
            x.getGroupBy().accept(this);
        }
        if (x.getModelClause() != null) {
            this.println();
            x.getModelClause().accept(this);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            orderBy.accept(this);
        }
        this.printFetchFirst(x);
        if (x.isForUpdate()) {
            this.println();
            this.print0(this.ucase ? "FOR UPDATE" : "for update");
            if (x.getForUpdateOfSize() > 0) {
                this.print('(');
                this.printAndAccept(x.getForUpdateOf(), ", ");
                this.print(')');
            }
            if (x.isNoWait()) {
                this.print0(this.ucase ? " NOWAIT" : " nowait");
            }
            else if (x.isSkipLocked()) {
                this.print0(this.ucase ? " SKIP LOCKED" : " skip locked");
            }
            else if (x.getWaitTime() != null) {
                this.print0(this.ucase ? " WAIT " : " wait ");
                x.getWaitTime().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectRestriction.CheckOption x) {
        this.print0(this.ucase ? "CHECK OPTION" : "check option");
        if (x.getConstraint() != null) {
            this.print0(this.ucase ? " CONSTRAINT" : " constraint");
            this.print(' ');
            x.getConstraint().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectRestriction.ReadOnly x) {
        this.print0(this.ucase ? "READ ONLY" : "read only");
        if (x.getConstraint() != null) {
            this.print0(this.ucase ? " CONSTRAINT" : " constraint");
            this.print(' ');
            x.getConstraint().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectSubqueryTableSource x) {
        this.print('(');
        ++this.indentCount;
        this.println();
        x.getSelect().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        final OracleSelectPivotBase pivot = x.getPivot();
        if (pivot != null) {
            this.println();
            pivot.accept(this);
        }
        this.printFlashback(x.getFlashback());
        if (x.getAlias() != null && x.getAlias().length() != 0) {
            this.print(' ');
            this.print0(x.getAlias());
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectTableReference x) {
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            this.printTableSourceExpr(x.getExpr());
            if (x.getPartition() != null) {
                this.print(' ');
                x.getPartition().accept(this);
            }
            this.print(')');
        }
        else {
            this.printTableSourceExpr(x.getExpr());
            if (x.getPartition() != null) {
                this.print(' ');
                x.getPartition().accept(this);
            }
        }
        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }
        if (x.getSampleClause() != null) {
            this.print(' ');
            x.getSampleClause().accept(this);
        }
        if (x.getPivot() != null) {
            this.println();
            x.getPivot().accept(this);
        }
        this.printFlashback(x.getFlashback());
        this.printAlias(x.getAlias());
        return false;
    }
    
    private void printFlashback(final SQLExpr flashback) {
        if (flashback == null) {
            return;
        }
        this.println();
        if (flashback instanceof SQLBetweenExpr) {
            flashback.accept(this);
        }
        else {
            this.print0(this.ucase ? "AS OF " : "as of ");
            flashback.accept(this);
        }
    }
    
    @Override
    public boolean visit(final OracleSelectUnPivot x) {
        this.print0(this.ucase ? "UNPIVOT" : "unpivot");
        if (x.getNullsIncludeType() != null) {
            this.print(' ');
            this.print0(OracleSelectUnPivot.NullsIncludeType.toString(x.getNullsIncludeType(), this.ucase));
        }
        this.print0(" (");
        if (x.getItems().size() == 1) {
            x.getItems().get(0).accept(this);
        }
        else {
            this.print0(" (");
            this.printAndAccept(x.getItems(), ", ");
            this.print(')');
        }
        if (x.getPivotFor().size() > 0) {
            this.print0(this.ucase ? " FOR " : " for ");
            if (x.getPivotFor().size() == 1) {
                x.getPivotFor().get(0).accept(this);
            }
            else {
                this.print('(');
                this.printAndAccept(x.getPivotFor(), ", ");
                this.print(')');
            }
        }
        if (x.getPivotIn().size() > 0) {
            this.print0(this.ucase ? " IN (" : " in (");
            this.printAndAccept(x.getPivotIn(), ", ");
            this.print(')');
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleUpdateStatement x) {
        this.print0(this.ucase ? "UPDATE " : "update ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            x.getTableSource().accept(this);
            this.print(')');
        }
        else {
            x.getTableSource().accept(this);
        }
        this.printAlias(x.getAlias());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            x.getItems().get(i).accept(this);
        }
        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            ++this.indentCount;
            x.getWhere().accept(this);
            --this.indentCount;
        }
        if (x.getReturning().size() > 0) {
            this.println();
            this.print0(this.ucase ? "RETURNING " : "returning ");
            this.printAndAccept(x.getReturning(), ", ");
            this.print0(this.ucase ? " INTO " : " into ");
            this.printAndAccept(x.getReturningInto(), ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SampleClause x) {
        this.print0(this.ucase ? "SAMPLE " : "sample ");
        if (x.isBlock()) {
            this.print0(this.ucase ? "BLOCK " : "block ");
        }
        this.print('(');
        this.printAndAccept(x.getPercent(), ", ");
        this.print(')');
        if (x.getSeedValue() != null) {
            this.print0(this.ucase ? " SEED (" : " seed (");
            x.getSeedValue().accept(this);
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final PartitionExtensionClause x) {
        if (x.isSubPartition()) {
            this.print0(this.ucase ? "SUBPARTITION " : "subpartition ");
        }
        else {
            this.print0(this.ucase ? "PARTITION " : "partition ");
        }
        if (x.getPartition() != null) {
            this.print('(');
            x.getPartition().accept(this);
            this.print(')');
        }
        else {
            this.print0(this.ucase ? "FOR (" : "for (");
            this.printAndAccept(x.getFor(), ",");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleWithSubqueryEntry x) {
        this.print0(x.getAlias());
        if (x.getColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        this.print0(this.ucase ? " AS " : " as ");
        this.print('(');
        ++this.indentCount;
        this.println();
        x.getSubQuery().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        if (x.getSearchClause() != null) {
            this.println();
            x.getSearchClause().accept(this);
        }
        if (x.getCycleClause() != null) {
            this.println();
            x.getCycleClause().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SearchClause x) {
        this.print0(this.ucase ? "SEARCH " : "search ");
        this.print0(x.getType().name());
        this.print0(this.ucase ? " FIRST BY " : " first by ");
        this.printAndAccept(x.getItems(), ", ");
        this.print0(this.ucase ? " SET " : " set ");
        x.getOrderingColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final CycleClause x) {
        this.print0(this.ucase ? "CYCLE " : "cycle ");
        this.printAndAccept(x.getAliases(), ", ");
        this.print0(this.ucase ? " SET " : " set ");
        x.getMark().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getValue().accept(this);
        this.print0(this.ucase ? " DEFAULT " : " default ");
        x.getDefaultValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleBinaryFloatExpr x) {
        if (x != null && x.getValue() != null) {
            this.print0(x.getValue().toString());
            this.print('F');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleBinaryDoubleExpr x) {
        if (x != null && x.getValue() != null) {
            this.print0(x.getValue().toString());
            this.print('D');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleIsSetExpr x) {
        x.getNestedTable().accept(this);
        this.print0(this.ucase ? " IS A SET" : " is a set");
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ReturnRowsClause x) {
        if (x.isAll()) {
            this.print0(this.ucase ? "RETURN ALL ROWS" : "return all rows");
        }
        else {
            this.print0(this.ucase ? "RETURN UPDATED ROWS" : "return updated rows");
        }
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause x) {
        this.print0(this.ucase ? "MODEL" : "model");
        ++this.indentCount;
        for (final ModelClause.CellReferenceOption opt : x.getCellReferenceOptions()) {
            this.print(' ');
            this.print0(opt.name);
        }
        if (x.getReturnRowsClause() != null) {
            this.print(' ');
            x.getReturnRowsClause().accept(this);
        }
        for (final ModelClause.ReferenceModelClause item : x.getReferenceModelClauses()) {
            this.print(' ');
            item.accept(this);
        }
        x.getMainModel().accept(this);
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.MainModelClause x) {
        if (x.getMainModelName() != null) {
            this.print0(this.ucase ? " MAIN " : " main ");
            x.getMainModelName().accept(this);
        }
        this.println();
        x.getModelColumnClause().accept(this);
        for (final ModelClause.CellReferenceOption opt : x.getCellReferenceOptions()) {
            this.println();
            this.print0(opt.name);
        }
        this.println();
        x.getModelRulesClause().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelColumnClause x) {
        if (x.getQueryPartitionClause() != null) {
            x.getQueryPartitionClause().accept(this);
            this.println();
        }
        this.print0(this.ucase ? "DIMENSION BY (" : "dimension by (");
        this.printAndAccept(x.getDimensionByColumns(), ", ");
        this.print(')');
        this.println();
        this.print0(this.ucase ? "MEASURES (" : "measures (");
        this.printAndAccept(x.getMeasuresColumns(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.QueryPartitionClause x) {
        this.print0(this.ucase ? "PARTITION BY (" : "partition by (");
        this.printAndAccept(x.getExprList(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelColumn x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.ModelRulesClause x) {
        if (x.getOptions().size() > 0) {
            this.print0(this.ucase ? "RULES" : "rules");
            for (final ModelClause.ModelRuleOption opt : x.getOptions()) {
                this.print(' ');
                this.print0(opt.name);
            }
        }
        if (x.getIterate() != null) {
            this.print0(this.ucase ? " ITERATE (" : " iterate (");
            x.getIterate().accept(this);
            this.print(')');
            if (x.getUntil() != null) {
                this.print0(this.ucase ? " UNTIL (" : " until (");
                x.getUntil().accept(this);
                this.print(')');
            }
        }
        this.print0(" (");
        this.printAndAccept(x.getCellAssignmentItems(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.CellAssignmentItem x) {
        if (x.getOption() != null) {
            this.print0(x.getOption().name);
            this.print(' ');
        }
        x.getCellAssignment().accept(this);
        if (x.getOrderBy() != null) {
            this.print(' ');
            x.getOrderBy().accept(this);
        }
        this.print0(" = ");
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final ModelClause.CellAssignment x) {
        x.getMeasureColumn().accept(this);
        this.print0("[");
        this.printAndAccept(x.getConditions(), ", ");
        this.print0("]");
        return false;
    }
    
    @Override
    public boolean visit(final OracleReturningClause x) {
        this.print0(this.ucase ? "RETURNING " : "returning ");
        this.printAndAccept(x.getItems(), ", ");
        this.print0(this.ucase ? " INTO " : " into ");
        this.printAndAccept(x.getValues(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final OracleInsertStatement x) {
        this.print0(this.ucase ? "INSERT " : "insert ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }
        this.print0(this.ucase ? "INTO " : "into ");
        x.getTableSource().accept(this);
        this.printInsertColumns(x.getColumns());
        if (x.getValues() != null) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            x.getValues().accept(this);
        }
        else if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        if (x.getReturning() != null) {
            this.println();
            x.getReturning().accept(this);
        }
        if (x.getErrorLogging() != null) {
            this.println();
            x.getErrorLogging().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
        this.print0(this.ucase ? "INTO " : "into ");
        x.getTableSource().accept(this);
        if (x.getColumns().size() > 0) {
            ++this.indentCount;
            this.println();
            this.print('(');
            for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        this.println();
                    }
                    this.print0(", ");
                }
                x.getColumns().get(i).accept(this);
            }
            this.print(')');
            --this.indentCount;
        }
        if (x.getValues() != null) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            x.getValues().accept(this);
        }
        else if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement x) {
        this.print0(this.ucase ? "INSERT " : "insert ");
        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }
        if (x.getOption() != null) {
            this.print0(x.getOption().name());
            this.print(' ');
        }
        for (int i = 0, size = x.getEntries().size(); i < size; ++i) {
            ++this.indentCount;
            this.println();
            x.getEntries().get(i).accept(this);
            --this.indentCount;
        }
        this.println();
        x.getSubQuery().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.ConditionalInsertClause x) {
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }
            final OracleMultiInsertStatement.ConditionalInsertClauseItem item = x.getItems().get(i);
            item.accept(this);
        }
        if (x.getElseItem() != null) {
            this.println();
            this.print0(this.ucase ? "ELSE" : "else");
            ++this.indentCount;
            this.println();
            x.getElseItem().accept(this);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getWhen().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        ++this.indentCount;
        this.println();
        x.getThen().accept(this);
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final OracleLockTableStatement x) {
        this.print0(this.ucase ? "LOCK TABLE " : "lock table ");
        x.getTable().accept(this);
        if (x.getPartition() != null) {
            this.print0(" PARTITION (");
            x.getPartition().accept(this);
            this.print0(") ");
        }
        this.print0(this.ucase ? " IN " : " in ");
        this.print0(x.getLockMode().toString());
        this.print0(this.ucase ? " MODE " : " mode ");
        if (x.isNoWait()) {
            this.print0(this.ucase ? "NOWAIT" : "nowait");
        }
        else if (x.getWait() != null) {
            this.print0(this.ucase ? "WAIT " : "wait ");
            x.getWait().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSessionStatement x) {
        this.print0(this.ucase ? "ALTER SESSION SET " : "alter session set ");
        this.printAndAccept(x.getItems(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final OracleDatetimeExpr x) {
        x.getExpr().accept(this);
        final SQLExpr timeZone = x.getTimeZone();
        if (timeZone instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)timeZone).getName().equalsIgnoreCase("LOCAL")) {
            this.print0(this.ucase ? " AT LOCAL" : "alter session set ");
            return false;
        }
        this.print0(this.ucase ? " AT TIME ZONE " : " at time zone ");
        timeZone.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleSysdateExpr x) {
        this.print0(this.ucase ? "SYSDATE" : "sysdate");
        if (x.getOption() != null) {
            this.print('@');
            this.print0(x.getOption());
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement.Item x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getWhen().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        ++this.indentCount;
        if (x.getStatements().size() > 1) {
            this.println();
        }
        else if (x.getStatements().size() == 1 && x.getStatements().get(0) instanceof SQLIfStatement) {
            this.println();
        }
        else {
            this.print(' ');
        }
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0 && size > 1) {
                this.println();
            }
            final SQLStatement stmt = x.getStatements().get(i);
            stmt.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement x) {
        this.print0(this.ucase ? "EXCEPTION" : "exception");
        ++this.indentCount;
        final List<OracleExceptionStatement.Item> items = x.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            this.println();
            final OracleExceptionStatement.Item item = items.get(i);
            item.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final OracleArgumentExpr x) {
        this.print0(x.getArgumentName());
        this.print0(" => ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleSetTransactionStatement x) {
        if (x.isReadOnly()) {
            this.print0(this.ucase ? "SET TRANSACTION READ ONLY" : "set transaction read only");
        }
        else {
            this.print0(this.ucase ? "SET TRANSACTION" : "set transaction");
        }
        final SQLExpr name = x.getName();
        if (name != null) {
            this.print0(this.ucase ? " NAME " : " name ");
            name.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleExplainStatement x) {
        this.print0(this.ucase ? "EXPLAIN PLAN" : "explain plan");
        ++this.indentCount;
        this.println();
        if (x.getStatementId() != null) {
            this.print0(this.ucase ? "SET STATEMENT_ID = " : "set statement_id = ");
            x.getStatementId().accept(this);
            this.println();
        }
        if (x.getInto() != null) {
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
            this.println();
        }
        this.print0(this.ucase ? "FOR" : "for");
        this.println();
        x.getStatement().accept(this);
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterProcedureStatement x) {
        this.print0(this.ucase ? "ALTER PROCEDURE " : "alter procedure ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.isReuseSettings()) {
            this.print0(this.ucase ? " REUSE SETTINGS" : " reuse settings");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableDropPartition x) {
        this.print0(this.ucase ? "DROP PARTITION " : "drop partition ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableStatement x) {
        if (x.getItems().size() == 1) {
            final SQLAlterTableItem item = x.getItems().get(0);
            if (item instanceof SQLAlterTableRename) {
                final SQLExpr to = ((SQLAlterTableRename)item).getTo().getExpr();
                this.print0(this.ucase ? "RENAME " : "rename ");
                x.getName().accept(this);
                this.print0(this.ucase ? " TO " : " to ");
                to.accept(this);
                return false;
            }
        }
        this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
        this.printTableSourceExpr(x.getName());
        ++this.indentCount;
        for (final SQLAlterTableItem item2 : x.getItems()) {
            this.println();
            item2.accept(this);
        }
        if (x.isUpdateGlobalIndexes()) {
            this.println();
            this.print0(this.ucase ? "UPDATE GLOABL INDEXES" : "update gloabl indexes");
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableTruncatePartition x) {
        this.print0(this.ucase ? "TRUNCATE PARTITION " : "truncate partition ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.TableSpaceItem x) {
        this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
        x.getTablespace().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        this.print0(this.ucase ? "UPDATE INDEXES" : "update indexes");
        if (x.getItems().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getItems(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition x) {
        this.print0(this.ucase ? "SPLIT PARTITION " : "split partition ");
        x.getName().accept(this);
        if (x.getAt().size() > 0) {
            ++this.indentCount;
            this.println();
            this.print0(this.ucase ? "AT (" : "at (");
            this.printAndAccept(x.getAt(), ", ");
            this.print(')');
            --this.indentCount;
        }
        if (x.getInto().size() > 0) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "INTO (" : "into (");
            this.printAndAccept(x.getInto(), ", ");
            this.print(')');
            --this.indentCount;
        }
        if (x.getUpdateIndexes() != null) {
            this.println();
            ++this.indentCount;
            x.getUpdateIndexes().accept(this);
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        this.print0(this.ucase ? "PARTITION " : "partition ");
        x.getPartition().accept(this);
        for (final SQLObject item : x.getSegmentAttributeItems()) {
            this.print(' ');
            item.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableModify x) {
        this.print0(this.ucase ? "MODIFY (" : "modify (");
        ++this.indentCount;
        for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
            this.println();
            final SQLColumnDefinition column = x.getColumns().get(i);
            column.accept(this);
            if (i != size - 1) {
                this.print0(", ");
            }
        }
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.getType() != null) {
            this.print0(x.getType());
            this.print(' ');
        }
        this.print0(this.ucase ? "INDEX " : "index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON " : " on ");
        if (x.isCluster()) {
            this.print0(this.ucase ? "CLUSTER " : "cluster ");
        }
        x.getTable().accept(this);
        final List<SQLSelectOrderByItem> items = x.getItems();
        if (items.size() > 0) {
            this.print('(');
            this.printAndAccept(items, ", ");
            this.print(')');
        }
        if (x.isIndexOnlyTopLevel()) {
            this.println();
            this.print0(this.ucase ? "INDEX ONLY TOPLEVEL" : "index only toplevel");
        }
        if (x.isComputeStatistics()) {
            this.println();
            this.print0(this.ucase ? "COMPUTE STATISTICS" : "compute statistics");
        }
        if (x.isReverse()) {
            this.println();
            this.print0(this.ucase ? "REVERSE" : "reverse");
        }
        this.printOracleSegmentAttributes(x);
        if (x.isOnline()) {
            this.print0(this.ucase ? " ONLINE" : " online");
        }
        if (x.isNoParallel()) {
            this.print0(this.ucase ? " NOPARALLEL" : " noparallel");
        }
        else if (x.getParallel() != null) {
            this.print0(this.ucase ? " PARALLEL " : " parallel ");
            x.getParallel().accept(this);
        }
        final Boolean sort = x.getSort();
        if (sort != null) {
            if (sort) {
                this.print0(this.ucase ? " SORT" : " sort");
            }
            else {
                this.print0(this.ucase ? " NOSORT" : " nosort");
            }
        }
        if (x.getLocalPartitions().size() > 0) {
            this.println();
            this.print0(this.ucase ? "LOCAL (" : "local (");
            ++this.indentCount;
            this.println();
            this.printlnAndAccept(x.getLocalPartitions(), ",");
            --this.indentCount;
            this.println();
            this.print(')');
        }
        else if (x.isLocal()) {
            this.print0(this.ucase ? " LOCAL" : " local");
        }
        final List<SQLName> localStoreIn = x.getLocalStoreIn();
        if (localStoreIn.size() > 0) {
            this.print0(this.ucase ? " STORE IN (" : " store in (");
            this.printAndAccept(localStoreIn, ", ");
            this.print(')');
        }
        final List<SQLPartitionBy> globalPartitions = x.getGlobalPartitions();
        if (globalPartitions.size() > 0) {
            for (final SQLPartitionBy globalPartition : globalPartitions) {
                this.println();
                this.print0(this.ucase ? "GLOBAL " : "global ");
                this.print0(this.ucase ? "PARTITION BY " : "partition by ");
                globalPartition.accept(this);
            }
        }
        else if (x.isGlobal()) {
            this.print0(this.ucase ? " GLOBAL" : " global");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleForStatement x) {
        final boolean all = x.isAll();
        if (all) {
            this.print0(this.ucase ? "FORALL " : "forall ");
        }
        else {
            this.print0(this.ucase ? "FOR " : "for ");
        }
        x.getIndex().accept(this);
        this.print0(this.ucase ? " IN " : " in ");
        final SQLExpr range = x.getRange();
        range.accept(this);
        if (!all) {
            this.println();
            this.print0(this.ucase ? "LOOP" : "loop");
        }
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            final SQLStatement stmt = x.getStatements().get(i);
            stmt.accept(this);
            if (!all && i != size - 1) {
                this.println();
            }
        }
        --this.indentCount;
        if (!all) {
            this.println();
            this.print0(this.ucase ? "END LOOP" : "end loop");
            final SQLName endLabel = x.getEndLabel();
            if (endLabel != null) {
                this.print(' ');
                endLabel.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.Else x) {
        this.print0(this.ucase ? "ELSE" : "else");
        ++this.indentCount;
        this.println();
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement.ElseIf x) {
        this.print0(this.ucase ? "ELSE IF " : "else if ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        ++this.indentCount;
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            this.println();
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final SQLIfStatement x) {
        this.print0(this.ucase ? "IF " : "if ");
        final int lines = this.lines;
        ++this.indentCount;
        x.getCondition().accept(this);
        --this.indentCount;
        if (lines != this.lines) {
            this.println();
        }
        else {
            this.print(' ');
        }
        this.print0(this.ucase ? "THEN" : "then");
        ++this.indentCount;
        for (int i = 0, size = x.getStatements().size(); i < size; ++i) {
            this.println();
            final SQLStatement item = x.getStatements().get(i);
            item.accept(this);
        }
        --this.indentCount;
        for (final SQLIfStatement.ElseIf elseIf : x.getElseIfList()) {
            this.println();
            elseIf.accept(this);
        }
        if (x.getElseItem() != null) {
            this.println();
            x.getElseItem().accept(this);
        }
        this.println();
        this.print0(this.ucase ? "END IF" : "end if");
        return false;
    }
    
    @Override
    public boolean visit(final OracleRangeExpr x) {
        x.getLowBound().accept(this);
        this.print0("..");
        x.getUpBound().accept(this);
        return false;
    }
    
    @Override
    protected void visitColumnDefault(final SQLColumnDefinition x) {
        if (x.getParent() instanceof SQLBlockStatement) {
            this.print0(" := ");
        }
        else {
            this.print0(this.ucase ? " DEFAULT " : " default ");
        }
        this.printExpr(x.getDefaultExpr(), false);
    }
    
    @Override
    public boolean visit(final OraclePrimaryKey x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }
        this.print0(this.ucase ? "PRIMARY KEY (" : "primary key (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        final Boolean rely = x.getRely();
        if (rely != null && rely) {
            this.print0(this.ucase ? " RELY" : " rely");
        }
        this.printConstraintState(x);
        final Boolean validate = x.getValidate();
        if (validate != null) {
            if (validate) {
                this.print0(this.ucase ? " VALIDATE" : " validate");
            }
            else {
                this.print0(this.ucase ? " NOVALIDATE" : " novalidate");
            }
        }
        return false;
    }
    
    protected void printConstraintState(final OracleConstraint x) {
        ++this.indentCount;
        if (x.getUsing() != null) {
            this.println();
            x.getUsing().accept(this);
        }
        if (x.getExceptionsInto() != null) {
            this.println();
            this.print0(this.ucase ? "EXCEPTIONS INTO " : "exceptions into ");
            x.getExceptionsInto().accept(this);
        }
        final Boolean enable = x.getEnable();
        if (enable != null) {
            if (enable) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            }
            else {
                this.print0(this.ucase ? " DISABLE" : " disable");
            }
        }
        if (x.getInitially() != null) {
            this.print0(this.ucase ? " INITIALLY " : " initially ");
            this.print0(x.getInitially().name());
        }
        if (x.getDeferrable() != null) {
            if (x.getDeferrable()) {
                this.print0(this.ucase ? " DEFERRABLE" : " deferrable");
            }
            else {
                this.print0(this.ucase ? " NOT DEFERRABLE" : " not deferrable");
            }
        }
        --this.indentCount;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement x) {
        this.printCreateTable(x, false);
        if (x.getOf() != null) {
            this.println();
            this.print0(this.ucase ? "OF " : "of ");
            x.getOf().accept(this);
        }
        final OracleCreateTableStatement.OIDIndex oidIndex = x.getOidIndex();
        if (oidIndex != null) {
            this.println();
            oidIndex.accept(this);
        }
        final OracleCreateTableStatement.Organization organization = x.getOrganization();
        if (organization != null) {
            this.println();
            ++this.indentCount;
            organization.accept(this);
            --this.indentCount;
        }
        if (x.getIncluding().size() > 0) {
            this.print0(this.ucase ? " INCLUDING " : " including ");
            this.printAndAccept(x.getIncluding(), ", ");
            this.print0(this.ucase ? " OVERFLOW " : " overflow ");
        }
        this.printOracleSegmentAttributes(x);
        if (x.isInMemoryMetadata()) {
            this.println();
            this.print0(this.ucase ? "IN_MEMORY_METADATA" : "in_memory_metadata");
        }
        if (x.isCursorSpecificSegment()) {
            this.println();
            this.print0(this.ucase ? "CURSOR_SPECIFIC_SEGMENT" : "cursor_specific_segment");
        }
        if (x.getParallel() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "PARALLEL" : "parallel");
            final SQLExpr parallelValue = x.getParallelValue();
            if (parallelValue != null) {
                this.print(' ');
                this.printExpr(parallelValue);
            }
        }
        else if (x.getParallel() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOPARALLEL" : "noparallel");
        }
        if (x.getCache() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "CACHE" : "cache");
        }
        else if (x.getCache() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOCACHE" : "nocache");
        }
        if (x.getLobStorage() != null) {
            this.println();
            x.getLobStorage().accept(this);
        }
        if (x.isOnCommitPreserveRows()) {
            this.println();
            this.print0(this.ucase ? "ON COMMIT PRESERVE ROWS" : "on commit preserve rows");
        }
        else if (x.isOnCommitDeleteRows()) {
            this.println();
            this.print0(this.ucase ? "ON COMMIT DELETE ROWS" : "on commit delete rows");
        }
        if (x.isMonitoring()) {
            this.println();
            this.print0(this.ucase ? "MONITORING" : "monitoring");
        }
        if (x.getPartitioning() != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            x.getPartitioning().accept(this);
        }
        if (x.getCluster() != null) {
            this.println();
            this.print0(this.ucase ? "CLUSTER " : "cluster ");
            x.getCluster().accept(this);
            this.print0(" (");
            this.printAndAccept(x.getClusterColumns(), ",");
            this.print0(")");
        }
        final OracleXmlColumnProperties xmlTypeColumnProperties = x.getXmlTypeColumnProperties();
        if (xmlTypeColumnProperties != null) {
            this.println();
            xmlTypeColumnProperties.accept(this);
        }
        final SQLSelect select = x.getSelect();
        if (select != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            select.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleStorageClause x) {
        this.print0(this.ucase ? "STORAGE (" : "storage (");
        ++this.indentCount;
        final SQLExpr initial = x.getInitial();
        if (initial != null) {
            this.println();
            this.print0(this.ucase ? "INITIAL " : "initial ");
            this.printExpr(initial, false);
        }
        final SQLExpr next = x.getNext();
        if (next != null) {
            this.println();
            this.print0(this.ucase ? "NEXT " : "next ");
            this.printExpr(next, false);
        }
        final SQLExpr minExtents = x.getMinExtents();
        if (minExtents != null) {
            this.println();
            this.print0(this.ucase ? "MINEXTENTS " : "minextents ");
            this.printExpr(minExtents, false);
        }
        final SQLExpr maxExtents = x.getMaxExtents();
        if (maxExtents != null) {
            this.println();
            this.print0(this.ucase ? "MAXEXTENTS " : "maxextents ");
            this.printExpr(maxExtents, false);
        }
        final SQLExpr pctIncrease = x.getPctIncrease();
        if (pctIncrease != null) {
            this.println();
            this.print0(this.ucase ? "PCTINCREASE " : "pctincrease ");
            this.printExpr(pctIncrease, false);
        }
        final SQLExpr maxSize = x.getMaxSize();
        if (maxSize != null) {
            this.println();
            this.print0(this.ucase ? "MAXSIZE " : "maxsize ");
            this.printExpr(maxSize, false);
        }
        final SQLExpr freeLists = x.getFreeLists();
        if (freeLists != null) {
            this.println();
            this.print0(this.ucase ? "FREELISTS " : "freelists ");
            this.printExpr(freeLists, false);
        }
        final SQLExpr freeListGroups = x.getFreeListGroups();
        if (freeListGroups != null) {
            this.println();
            this.print0(this.ucase ? "FREELIST GROUPS " : "freelist groups ");
            this.printExpr(freeListGroups, false);
        }
        final SQLExpr bufferPool = x.getBufferPool();
        if (bufferPool != null) {
            this.println();
            this.print0(this.ucase ? "BUFFER_POOL " : "buffer_pool ");
            this.printExpr(bufferPool, false);
        }
        final SQLExpr objno = x.getObjno();
        if (objno != null) {
            this.println();
            this.print0(this.ucase ? "OBJNO " : "objno ");
            this.printExpr(objno, false);
        }
        if (x.getFlashCache() != null) {
            this.println();
            this.print0(this.ucase ? "FLASH_CACHE " : "flash_cache ");
            this.print0(this.ucase ? x.getFlashCache().name() : x.getFlashCache().name().toLowerCase());
        }
        if (x.getCellFlashCache() != null) {
            this.println();
            this.print0(this.ucase ? "CELL_FLASH_CACHE " : "cell_flash_cache ");
            this.print0(this.ucase ? x.getCellFlashCache().name() : x.getCellFlashCache().name().toLowerCase());
        }
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleGotoStatement x) {
        this.print0(this.ucase ? "GOTO " : "GOTO ");
        x.getLabel().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleLabelStatement x) {
        this.print0("<<");
        x.getLabel().accept(this);
        this.print0(">>");
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTriggerStatement x) {
        this.print0(this.ucase ? "ALTER TRIGGER " : "alter trigger ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            }
            else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSynonymStatement x) {
        this.print0(this.ucase ? "ALTER SYNONYM " : "alter synonym ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            }
            else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterViewStatement x) {
        this.print0(this.ucase ? "ALTER VIEW " : "alter view ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            }
            else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableMoveTablespace x) {
        this.print0(this.ucase ? " MOVE TABLESPACE " : " move tablespace ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleFileSpecification x) {
        this.printAndAccept(x.getFileNames(), ", ");
        if (x.getSize() != null) {
            this.print0(this.ucase ? " SIZE " : " size ");
            x.getSize().accept(this);
        }
        if (x.isAutoExtendOff()) {
            this.print0(this.ucase ? " AUTOEXTEND OFF" : " autoextend off");
        }
        else if (x.getAutoExtendOn() != null) {
            this.print0(this.ucase ? " AUTOEXTEND ON " : " autoextend on ");
            x.getAutoExtendOn().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceAddDataFile x) {
        this.print0(this.ucase ? "ADD DATAFILE" : "add datafile");
        ++this.indentCount;
        for (final OracleFileSpecification file : x.getFiles()) {
            this.println();
            file.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceStatement x) {
        this.print0(this.ucase ? "ALTER TABLESPACE " : "alter tablespace ");
        x.getName().accept(this);
        this.println();
        x.getItem().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLTruncateStatement x) {
        this.print0(this.ucase ? "TRUNCATE TABLE " : "truncate table ");
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isPurgeSnapshotLog()) {
            this.print0(this.ucase ? " PURGE SNAPSHOT LOG" : " purge snapshot log");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleExitStatement x) {
        this.print0(this.ucase ? "EXIT" : "exit");
        if (x.getLabel() != null) {
            this.print(' ');
            this.print0(x.getLabel());
        }
        if (x.getWhen() != null) {
            this.print0(this.ucase ? " WHEN " : " when ");
            x.getWhen().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleContinueStatement x) {
        this.print0(this.ucase ? "CONTINUE" : "continue");
        final String label = x.getLabel();
        if (label != null) {
            this.print(' ');
            this.print0(label);
        }
        if (x.getWhen() != null) {
            this.print0(this.ucase ? " WHEN " : " when ");
            x.getWhen().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleRaiseStatement x) {
        this.print0(this.ucase ? "RAISE" : "raise");
        if (x.getException() != null) {
            this.print(' ');
            x.getException().accept(this);
        }
        this.print(';');
        return false;
    }
    
    @Override
    public boolean visit(final SQLSavePointStatement x) {
        this.print0(this.ucase ? "SAVEPOINT" : "savepoint");
        if (x.getName() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getName().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateFunctionStatement x) {
        final boolean create = x.isCreate();
        if (!create) {
            this.print0(this.ucase ? "FUNCTION " : "function ");
        }
        else if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE FUNCTION " : "create or replace function ");
        }
        else {
            this.print0(this.ucase ? "CREATE FUNCTION " : "create function ");
        }
        x.getName().accept(this);
        final int paramSize = x.getParameters().size();
        if (paramSize > 0) {
            this.print0(" (");
            ++this.indentCount;
            this.println();
            for (int i = 0; i < paramSize; ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }
                final SQLParameter param = x.getParameters().get(i);
                param.accept(this);
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final String wrappedSource = x.getWrappedSource();
        if (wrappedSource != null) {
            this.print0(this.ucase ? " WRAPPED " : " wrapped ");
            this.print0(wrappedSource);
            if (x.isAfterSemi()) {
                this.print(';');
            }
            return false;
        }
        this.println();
        this.print(this.ucase ? "RETURN " : "return ");
        x.getReturnDataType().accept(this);
        if (x.isPipelined()) {
            this.print(this.ucase ? "PIPELINED " : "pipelined ");
        }
        if (x.isDeterministic()) {
            this.print(this.ucase ? "DETERMINISTIC " : "deterministic ");
        }
        final SQLName authid = x.getAuthid();
        if (authid != null) {
            this.print(this.ucase ? " AUTHID " : " authid ");
            authid.accept(this);
        }
        final SQLStatement block = x.getBlock();
        if (block != null && !create) {
            this.println();
            this.println("IS");
        }
        else {
            this.println();
            if (block instanceof SQLBlockStatement) {
                final SQLBlockStatement blockStatement = (SQLBlockStatement)block;
                if (blockStatement.getParameters().size() > 0 || authid != null) {
                    this.println(this.ucase ? "AS" : "as");
                }
            }
        }
        final String javaCallSpec = x.getJavaCallSpec();
        if (javaCallSpec != null) {
            this.print0(this.ucase ? "LANGUAGE JAVA NAME '" : "language java name '");
            this.print0(javaCallSpec);
            this.print('\'');
            return false;
        }
        if (x.isParallelEnable()) {
            this.print0(this.ucase ? "PARALLEL_ENABLE" : "parallel_enable");
            this.println();
        }
        if (x.isAggregate()) {
            this.print0(this.ucase ? "AGGREGATE" : "aggregate");
            this.println();
        }
        final SQLName using = x.getUsing();
        if (using != null) {
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        if (block != null) {
            block.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateDatabaseDbLinkStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isShared()) {
            this.print0(this.ucase ? "SHARE " : "share ");
        }
        if (x.isPublic()) {
            this.print0(this.ucase ? "PUBLIC " : "public ");
        }
        this.print0(this.ucase ? "DATABASE LINK " : "database link ");
        x.getName().accept(this);
        if (x.getUser() != null) {
            this.print0(this.ucase ? " CONNECT TO " : " connect to ");
            x.getUser().accept(this);
            if (x.getPassword() != null) {
                this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
                this.print0(x.getPassword());
            }
        }
        if (x.getAuthenticatedUser() != null) {
            this.print0(this.ucase ? " AUTHENTICATED BY " : " authenticated by ");
            x.getAuthenticatedUser().accept(this);
            if (x.getAuthenticatedPassword() != null) {
                this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
                this.print0(x.getAuthenticatedPassword());
            }
        }
        if (x.getUsing() != null) {
            this.print0(this.ucase ? " USING " : " using ");
            x.getUsing().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleDropDbLinkStatement x) {
        this.print0(this.ucase ? "DROP " : "drop ");
        if (x.isPublic()) {
            this.print0(this.ucase ? "PUBLIC " : "public ");
        }
        this.print0(this.ucase ? "DATABASE LINK " : "database link ");
        x.getName().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharacterDataType x) {
        this.print0(x.getName());
        final List<SQLExpr> arguments = x.getArguments();
        if (arguments.size() > 0) {
            this.print('(');
            final SQLExpr arg0 = arguments.get(0);
            this.printExpr(arg0, false);
            if (x.getCharType() != null) {
                this.print(' ');
                this.print0(x.getCharType());
            }
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalYear x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            x.getArguments().get(0).accept(this);
            this.print(')');
        }
        this.print0(this.ucase ? " TO MONTH" : " to month");
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalDay x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            x.getArguments().get(0).accept(this);
            this.print(')');
        }
        this.print0(this.ucase ? " TO SECOND" : " to second");
        if (x.getFractionalSeconds().size() > 0) {
            this.print('(');
            x.getFractionalSeconds().get(0).accept(this);
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleUsingIndexClause x) {
        this.print0(this.ucase ? "USING INDEX" : "using index");
        final SQLObject index = x.getIndex();
        if (index != null) {
            this.print(' ');
            if (index instanceof SQLCreateIndexStatement) {
                this.print('(');
                index.accept(this);
                this.print(')');
            }
            else {
                index.accept(this);
            }
        }
        this.printOracleSegmentAttributes(x);
        if (x.isComputeStatistics()) {
            this.println();
            this.print0(this.ucase ? "COMPUTE STATISTICS" : "compute statistics");
        }
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.println();
                this.print0(this.ucase ? "ENABLE" : "enable");
            }
            else {
                this.println();
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }
        if (x.isReverse()) {
            this.println();
            this.print0(this.ucase ? "REVERSE" : "reverse");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleLobStorageClause x) {
        this.print0(this.ucase ? "LOB (" : "lob (");
        this.printAndAccept(x.getItems(), ",");
        this.print0(this.ucase ? ") STORE AS" : ") store as");
        if (x.isSecureFile()) {
            this.print0(this.ucase ? " SECUREFILE" : " securefile");
        }
        if (x.isBasicFile()) {
            this.print0(this.ucase ? " BASICFILE" : " basicfile");
        }
        final SQLName segementName = x.getSegementName();
        if (segementName != null) {
            this.print(' ');
            segementName.accept(this);
        }
        this.print0(" (");
        ++this.indentCount;
        this.printOracleSegmentAttributes(x);
        if (x.getEnable() != null) {
            this.println();
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE STORAGE IN ROW" : "enable storage in row");
            }
            else {
                this.print0(this.ucase ? "DISABLE STORAGE IN ROW" : "disable storage in row");
            }
        }
        if (x.getChunk() != null) {
            this.println();
            this.print0(this.ucase ? "CHUNK " : "chunk ");
            x.getChunk().accept(this);
        }
        if (x.getCache() != null) {
            this.println();
            if (x.getCache()) {
                this.print0(this.ucase ? "CACHE" : "cache");
            }
            else {
                this.print0(this.ucase ? "NOCACHE" : "nocache");
            }
        }
        if (x.getKeepDuplicate() != null) {
            this.println();
            if (x.getKeepDuplicate()) {
                this.print0(this.ucase ? "KEEP_DUPLICATES" : "keep_duplicates");
            }
            else {
                this.print0(this.ucase ? "DEDUPLICATE" : "deduplicate");
            }
        }
        if (x.isRetention()) {
            this.println();
            this.print0(this.ucase ? "RETENTION" : "retention");
        }
        --this.indentCount;
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleUnique x) {
        this.visit(x);
        this.printConstraintState(x);
        return false;
    }
    
    @Override
    public boolean visit(final OracleForeignKey x) {
        this.visit(x);
        this.printConstraintState(x);
        return false;
    }
    
    @Override
    public boolean visit(final OracleCheck x) {
        this.visit(x);
        this.printConstraintState(x);
        return false;
    }
    
    @Override
    protected void printCascade() {
        this.print0(this.ucase ? " CASCADE CONSTRAINTS" : " cascade constraints");
    }
    
    @Override
    public boolean visit(final SQLCharExpr x, final boolean parameterized) {
        if (x.getText() != null && x.getText().length() == 0) {
            this.print0(this.ucase ? "NULL" : "null");
        }
        else {
            super.visit(x, parameterized);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSupplementalIdKey x) {
        this.print0(this.ucase ? "SUPPLEMENTAL LOG DATA (" : "supplemental log data (");
        int count = 0;
        if (x.isAll()) {
            this.print0(this.ucase ? "ALL" : "all");
            ++count;
        }
        if (x.isPrimaryKey()) {
            if (count != 0) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "PRIMARY KEY" : "primary key");
            ++count;
        }
        if (x.isUnique()) {
            if (count != 0) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "UNIQUE" : "unique");
            ++count;
        }
        if (x.isUniqueIndex()) {
            if (count != 0) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "UNIQUE INDEX" : "unique index");
            ++count;
        }
        if (x.isForeignKey()) {
            if (count != 0) {
                this.print0(", ");
            }
            this.print0(this.ucase ? "FOREIGN KEY" : "foreign key");
            ++count;
        }
        this.print0(this.ucase ? ") COLUMNS" : ") columns");
        return false;
    }
    
    @Override
    public boolean visit(final OracleSupplementalLogGrp x) {
        this.print0(this.ucase ? "SUPPLEMENTAL LOG GROUP " : "supplemental log group ");
        x.getGroup().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        if (x.isAlways()) {
            this.print0(this.ucase ? " ALWAYS" : " always");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement.Organization x) {
        final String type = x.getType();
        this.print0(this.ucase ? "ORGANIZATION " : "organization ");
        this.print0(this.ucase ? type : type.toLowerCase());
        this.printOracleSegmentAttributes(x);
        if (x.getPctthreshold() != null) {
            this.println();
            this.print0(this.ucase ? "PCTTHRESHOLD " : "pctthreshold ");
            this.print(x.getPctthreshold());
        }
        if ("EXTERNAL".equalsIgnoreCase(type)) {
            this.print0(" (");
            ++this.indentCount;
            if (x.getExternalType() != null) {
                this.println();
                this.print0(this.ucase ? "TYPE " : "type ");
                x.getExternalType().accept(this);
            }
            if (x.getExternalDirectory() != null) {
                this.println();
                this.print0(this.ucase ? "DEFAULT DIRECTORY " : "default directory ");
                x.getExternalDirectory().accept(this);
            }
            if (x.getExternalDirectoryRecordFormat() != null) {
                this.println();
                ++this.indentCount;
                this.print0(this.ucase ? "ACCESS PARAMETERS (" : "access parameters (");
                x.getExternalDirectoryRecordFormat().accept(this);
                --this.indentCount;
                this.println();
                this.print(')');
            }
            if (x.getExternalDirectoryLocation().size() > 0) {
                this.println();
                this.print0(this.ucase ? "LOCATION (" : " location(");
                this.printAndAccept(x.getExternalDirectoryLocation(), ", ");
                this.print(')');
            }
            --this.indentCount;
            this.println();
            this.print(')');
            if (x.getExternalRejectLimit() != null) {
                this.println();
                this.print0(this.ucase ? "REJECT LIMIT " : "reject limit ");
                x.getExternalRejectLimit().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement.OIDIndex x) {
        this.print0(this.ucase ? "OIDINDEX" : "oidindex");
        if (x.getName() != null) {
            this.print(' ');
            x.getName().accept(this);
        }
        this.print(" (");
        ++this.indentCount;
        this.printOracleSegmentAttributes(x);
        --this.indentCount;
        this.println();
        this.print(")");
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreatePackageStatement x) {
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE PACKAGE " : "create or replace procedure ");
        }
        else {
            this.print0(this.ucase ? "CREATE PACKAGE " : "create procedure ");
        }
        if (x.isBody()) {
            this.print0(this.ucase ? "BODY " : "body ");
        }
        x.getName().accept(this);
        if (x.isBody()) {
            this.println();
            this.print0(this.ucase ? "BEGIN" : "begin");
        }
        ++this.indentCount;
        final List<SQLStatement> statements = x.getStatements();
        for (int i = 0, size = statements.size(); i < size; ++i) {
            this.println();
            final SQLStatement stmt = statements.get(i);
            stmt.accept(this);
        }
        --this.indentCount;
        if (x.isBody() || statements.size() > 0) {
            this.println();
            this.print0(this.ucase ? "END " : "end ");
            x.getName().accept(this);
            this.print(';');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAssignItem x) {
        x.getTarget().accept(this);
        this.print0(" := ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleExecuteImmediateStatement x) {
        this.print0(this.ucase ? "EXECUTE IMMEDIATE " : "execute immediate ");
        x.getDynamicSql().accept(this);
        final List<SQLExpr> into = x.getInto();
        if (into.size() > 0) {
            this.print0(this.ucase ? " INTO " : " into ");
            this.printAndAccept(into, ", ");
        }
        final List<SQLArgument> using = x.getArguments();
        if (using.size() > 0) {
            this.print0(this.ucase ? " USING " : " using ");
            this.printAndAccept(using, ", ");
        }
        final List<SQLExpr> returnInto = x.getReturnInto();
        if (returnInto.size() > 0) {
            this.print0(this.ucase ? " RETURNNING INTO " : " returnning into ");
            this.printAndAccept(returnInto, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleTreatExpr x) {
        this.print0(this.ucase ? "TREAT (" : "treat (");
        x.getExpr().accept(this);
        this.print0(this.ucase ? " AS " : " as ");
        if (x.isRef()) {
            this.print0(this.ucase ? "REF " : "ref ");
        }
        x.getType().accept(this);
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateSynonymStatement x) {
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE " : "create or replace ");
        }
        else {
            this.print0(this.ucase ? "CREATE " : "create ");
        }
        if (x.isPublic()) {
            this.print0(this.ucase ? "PUBLIC " : "public ");
        }
        this.print0(this.ucase ? "SYNONYM " : "synonym ");
        x.getName().accept(this);
        this.print0(this.ucase ? " FOR " : " for ");
        x.getObject().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTypeStatement x) {
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE TYPE " : "create or replace type ");
        }
        else {
            this.print0(this.ucase ? "CREATE TYPE " : "create type ");
        }
        if (x.isBody()) {
            this.print0(this.ucase ? "BODY " : "body ");
        }
        x.getName().accept(this);
        final SQLName under = x.getUnder();
        if (under != null) {
            this.print0(this.ucase ? " UNDER " : " under ");
            under.accept(this);
        }
        final SQLName authId = x.getAuthId();
        if (authId != null) {
            this.print0(this.ucase ? " AUTHID " : " authid ");
            authId.accept(this);
        }
        if (x.isForce()) {
            this.print0(this.ucase ? "FORCE " : "force ");
        }
        final List<SQLParameter> parameters = x.getParameters();
        final SQLDataType tableOf = x.getTableOf();
        if (x.isObject()) {
            this.print0(" AS OBJECT");
        }
        if (parameters.size() > 0) {
            if (x.isParen()) {
                this.print(" (");
            }
            else {
                this.print0(this.ucase ? " IS" : " is");
            }
            ++this.indentCount;
            this.println();
            for (int i = 0; i < parameters.size(); ++i) {
                final SQLParameter param = parameters.get(i);
                param.accept(this);
                final SQLDataType dataType = param.getDataType();
                if (i < parameters.size() - 1) {
                    if (dataType instanceof OracleFunctionDataType && ((OracleFunctionDataType)dataType).getBlock() != null) {
                        this.println();
                    }
                    else if (dataType instanceof OracleProcedureDataType && ((OracleProcedureDataType)dataType).getBlock() != null) {
                        this.println();
                    }
                    else {
                        this.println(", ");
                    }
                }
            }
            --this.indentCount;
            this.println();
            if (x.isParen()) {
                this.print0(")");
            }
            else {
                this.print0("END");
            }
        }
        else if (tableOf != null) {
            this.print0(this.ucase ? " AS TABLE OF " : " as table of ");
            tableOf.accept(this);
        }
        else if (x.getVarraySizeLimit() != null) {
            this.print0(this.ucase ? " VARRAY (" : " varray (");
            x.getVarraySizeLimit().accept(this);
            this.print0(this.ucase ? ") OF " : ") of ");
            x.getVarrayDataType().accept(this);
        }
        final Boolean isFinal = x.getFinal();
        if (isFinal != null) {
            if (isFinal) {
                this.print0(this.ucase ? " FINAL" : " final");
            }
            else {
                this.print0(this.ucase ? " NOT FINAL" : " not final");
            }
        }
        final Boolean instantiable = x.getInstantiable();
        if (instantiable != null) {
            if (instantiable) {
                this.print0(this.ucase ? " INSTANTIABLE" : " instantiable");
            }
            else {
                this.print0(this.ucase ? " NOT INSTANTIABLE" : " not instantiable");
            }
        }
        final String wrappedSource = x.getWrappedSource();
        if (wrappedSource != null) {
            this.print0(this.ucase ? " WRAPPED" : " wrapped");
            this.print0(wrappedSource);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OraclePipeRowStatement x) {
        this.print0(this.ucase ? "PIPE ROW(" : "pipe row(");
        this.printAndAccept(x.getParameters(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleIsOfTypeExpr x) {
        this.printExpr(x.getExpr());
        this.print0(this.ucase ? " IS OF TYPE (" : " is of type (");
        final List<SQLExpr> types = x.getTypes();
        for (int i = 0, size = types.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLExpr type = types.get(i);
            if (Boolean.TRUE == type.getAttribute("ONLY")) {
                this.print0(this.ucase ? "ONLY " : "only ");
            }
            type.accept(this);
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleRunStatement x) {
        this.print0("@@");
        this.printExpr(x.getExpr());
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties x) {
        this.print0(this.ucase ? "XMLTYPE " : "xmltype ");
        x.getColumn().accept(this);
        final OracleXmlColumnProperties.OracleXMLTypeStorage storage = x.getStorage();
        if (storage != null) {
            storage.accept(this);
        }
        final Boolean allowNonSchema = x.getAllowNonSchema();
        if (allowNonSchema != null) {
            if (allowNonSchema) {
                this.print0(this.ucase ? " ALLOW NONSCHEMA" : " allow nonschema");
            }
            else {
                this.print0(this.ucase ? " DISALLOW NONSCHEMA" : " disallow nonschema");
            }
        }
        final Boolean allowAnySchema = x.getAllowAnySchema();
        if (allowAnySchema != null) {
            if (allowAnySchema) {
                this.print0(this.ucase ? " ALLOW ANYSCHEMA" : " allow anyschema");
            }
            else {
                this.print0(this.ucase ? " DISALLOW ANYSCHEMA" : " disallow anyschema");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubPartition x) {
        super.visit(x);
        this.incrementIndent();
        this.printOracleSegmentAttributes(x);
        this.decrementIndent();
        return false;
    }
    
    @Override
    public boolean visit(final SQLPartitionValue x) {
        super.visit(x);
        this.incrementIndent();
        this.printOracleSegmentAttributes(x);
        this.decrementIndent();
        return false;
    }
}
