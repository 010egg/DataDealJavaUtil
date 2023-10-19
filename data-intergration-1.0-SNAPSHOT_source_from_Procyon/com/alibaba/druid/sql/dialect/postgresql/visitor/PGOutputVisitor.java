// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.visitor;

import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.SQLIndexOptions;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleXmlColumnProperties;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRunStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsOfTypeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLUniqueConstraint;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePipeRowStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleProcedureDataType;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleFunctionDataType;
import com.alibaba.druid.sql.ast.SQLParameter;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTypeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleTreatExpr;
import com.alibaba.druid.sql.ast.SQLArgument;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalLogGrp;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalIdKey;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleRaiseStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleContinueStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.statement.SQLGrantStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateUserStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGAlterSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDropSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGCreateSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGConnectToStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGLineSegmentsExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCircleExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPolygonExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCidrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGInetExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGMacAddrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPointExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGBoxExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGExtractExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGFunctionTableSource;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class PGOutputVisitor extends SQLASTOutputVisitor implements PGASTVisitor, OracleASTVisitor
{
    public PGOutputVisitor(final Appendable appender) {
        super(appender);
        this.dbType = DbType.postgresql;
    }
    
    public PGOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        this.dbType = DbType.postgresql;
    }
    
    @Override
    public boolean visit(final PGSelectQueryBlock.FetchClause x) {
        this.print0(this.ucase ? "FETCH " : "fetch ");
        if (PGSelectQueryBlock.FetchClause.Option.FIRST.equals(x.getOption())) {
            this.print0(this.ucase ? "FIRST " : "first ");
        }
        else if (PGSelectQueryBlock.FetchClause.Option.NEXT.equals(x.getOption())) {
            this.print0(this.ucase ? "NEXT " : "next ");
        }
        x.getCount().accept(this);
        this.print0(this.ucase ? " ROWS ONLY" : " rows only");
        return false;
    }
    
    @Override
    public boolean visit(final PGSelectQueryBlock.ForClause x) {
        this.print0(this.ucase ? "FOR " : "for ");
        if (PGSelectQueryBlock.ForClause.Option.UPDATE.equals(x.getOption())) {
            this.print0(this.ucase ? "UPDATE" : "update");
        }
        else if (PGSelectQueryBlock.ForClause.Option.SHARE.equals(x.getOption())) {
            this.print0(this.ucase ? "SHARE" : "share");
        }
        if (x.getOf().size() > 0) {
            this.print(' ');
            for (int i = 0; i < x.getOf().size(); ++i) {
                if (i != 0) {
                    this.println(", ");
                }
                x.getOf().get(i).accept(this);
            }
        }
        if (x.isNoWait()) {
            this.print0(this.ucase ? " NOWAIT" : " nowait");
        }
        else if (x.isSkipLocked()) {
            this.print0(this.ucase ? " SKIP LOCKED" : " skip locked");
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGSelectQueryBlock x) {
        if (!this.isParameterized() && this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        final boolean bracket = x.isParenthesized();
        if (bracket) {
            this.print('(');
        }
        this.print0(this.ucase ? "SELECT " : "select ");
        if (1 == x.getDistionOption()) {
            this.print0(this.ucase ? "ALL " : "all ");
        }
        else if (2 == x.getDistionOption()) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
            final List<SQLExpr> distinctOn = x.getDistinctOn();
            if (distinctOn != null && distinctOn.size() > 0) {
                this.print0(this.ucase ? "ON " : "on ");
                if (distinctOn.size() == 1 && distinctOn.get(0) instanceof SQLListExpr) {
                    this.printExpr(distinctOn.get(0));
                    this.print(' ');
                }
                else {
                    this.print0("(");
                    this.printAndAccept(distinctOn, ", ");
                    this.print0(") ");
                }
            }
        }
        this.printSelectList(x.getSelectList());
        if (x.getInto() != null) {
            this.println();
            if (x.getIntoOption() != null) {
                this.print0(x.getIntoOption().name());
                this.print(' ');
            }
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
        }
        if (x.getFrom() != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            x.getFrom().accept(this);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            where.accept(this);
            if (where.hasAfterComment() && this.isPrettyFormat()) {
                this.print(' ');
                this.printlnComment(x.getWhere().getAfterCommentsDirect());
            }
        }
        if (x.getGroupBy() != null) {
            this.println();
            x.getGroupBy().accept(this);
        }
        final List<SQLWindow> windows = x.getWindows();
        if (windows != null && windows.size() > 0) {
            this.println();
            this.print0(this.ucase ? "WINDOW " : "window ");
            this.printAndAccept(windows, ", ");
        }
        if (x.getOrderBy() != null) {
            this.println();
            x.getOrderBy().accept(this);
        }
        if (x.getLimit() != null) {
            this.println();
            x.getLimit().accept(this);
        }
        if (x.getFetch() != null) {
            this.println();
            x.getFetch().accept(this);
        }
        if (x.getForClause() != null) {
            this.println();
            x.getForClause().accept(this);
        }
        if (bracket) {
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLTruncateStatement x) {
        this.print0(this.ucase ? "TRUNCATE TABLE " : "truncate table ");
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY " : "only ");
        }
        this.printlnAndAccept(x.getTableSources(), ", ");
        if (x.getRestartIdentity() != null) {
            if (x.getRestartIdentity()) {
                this.print0(this.ucase ? " RESTART IDENTITY" : " restart identity");
            }
            else {
                this.print0(this.ucase ? " CONTINUE IDENTITY" : " continue identity");
            }
        }
        if (x.getCascade() != null) {
            if (x.getCascade()) {
                this.print0(this.ucase ? " CASCADE" : " cascade");
            }
            else {
                this.print0(this.ucase ? " RESTRICT" : " restrict");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGDeleteStatement x) {
        if (x.getWith() != null) {
            x.getWith().accept(this);
            this.println();
        }
        this.print0(this.ucase ? "DELETE FROM " : "delete from ");
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY " : "only ");
        }
        this.printTableSourceExpr(x.getTableName());
        if (x.getAlias() != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }
        final SQLTableSource using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            ++this.indentCount;
            x.getWhere().accept(this);
            --this.indentCount;
        }
        if (x.isReturning()) {
            this.println();
            this.print0(this.ucase ? "RETURNING *" : "returning *");
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGInsertStatement x) {
        if (x.getWith() != null) {
            x.getWith().accept(this);
            this.println();
        }
        this.print0(this.ucase ? "INSERT INTO " : "insert into ");
        x.getTableSource().accept(this);
        this.printInsertColumns(x.getColumns());
        if (x.getValues() != null) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            this.printlnAndAccept(x.getValuesList(), ", ");
        }
        else if (x.getQuery() != null) {
            this.println();
            x.getQuery().accept(this);
        }
        final List<SQLExpr> onConflictTarget = x.getOnConflictTarget();
        final List<SQLUpdateSetItem> onConflictUpdateSetItems = x.getOnConflictUpdateSetItems();
        final boolean onConflictDoNothing = x.isOnConflictDoNothing();
        if (onConflictDoNothing || (onConflictTarget != null && onConflictTarget.size() > 0) || (onConflictUpdateSetItems != null && onConflictUpdateSetItems.size() > 0)) {
            this.println();
            this.print0(this.ucase ? "ON CONFLICT" : "on conflict");
            if (onConflictTarget != null && onConflictTarget.size() > 0) {
                this.print0(" (");
                this.printAndAccept(onConflictTarget, ", ");
                this.print(')');
            }
            final SQLName onConflictConstraint = x.getOnConflictConstraint();
            if (onConflictConstraint != null) {
                this.print0(this.ucase ? " ON CONSTRAINT " : " on constraint ");
                this.printExpr(onConflictConstraint);
            }
            final SQLExpr onConflictWhere = x.getOnConflictWhere();
            if (onConflictWhere != null) {
                this.print0(this.ucase ? " WHERE " : " where ");
                this.printExpr(onConflictWhere);
            }
            if (onConflictDoNothing) {
                this.print0(this.ucase ? " DO NOTHING" : " do nothing");
            }
            else if (onConflictUpdateSetItems != null && onConflictUpdateSetItems.size() > 0) {
                this.print0(this.ucase ? " DO UPDATE SET " : " do update set ");
                this.printAndAccept(onConflictUpdateSetItems, ", ");
                final SQLExpr onConflictUpdateWhere = x.getOnConflictUpdateWhere();
                if (onConflictUpdateWhere != null) {
                    this.print0(this.ucase ? " WHERE " : " where ");
                    this.printExpr(onConflictUpdateWhere);
                }
            }
        }
        if (x.getReturning() != null) {
            this.println();
            this.print0(this.ucase ? "RETURNING " : "returning ");
            x.getReturning().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGSelectStatement x) {
        return this.visit(x);
    }
    
    @Override
    public boolean visit(final PGUpdateStatement x) {
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            this.visit(with);
            this.println();
        }
        this.print0(this.ucase ? "UPDATE " : "update ");
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY " : "only ");
        }
        this.printTableSource(x.getTableSource());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLUpdateSetItem item = x.getItems().get(i);
            this.visit(item);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            this.printTableSource(from);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            ++this.indentCount;
            this.print0(this.ucase ? "WHERE " : "where ");
            this.printExpr(where);
            --this.indentCount;
        }
        final List<SQLExpr> returning = x.getReturning();
        if (returning.size() > 0) {
            this.println();
            this.print0(this.ucase ? "RETURNING " : "returning ");
            this.printAndAccept(returning, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGFunctionTableSource x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }
        if (x.getParameters().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getParameters(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGTypeCastExpr x) {
        final SQLExpr expr = x.getExpr();
        final SQLDataType dataType = x.getDataType();
        if (dataType.nameHashCode64() == FnvHash.Constants.VARBIT) {
            dataType.accept(this);
            this.print(' ');
            this.printExpr(expr);
            return false;
        }
        if (expr != null) {
            if (expr instanceof SQLBinaryOpExpr) {
                this.print('(');
                expr.accept(this);
                this.print(')');
            }
            else {
                if (expr instanceof PGTypeCastExpr && dataType.getArguments().size() == 0) {
                    dataType.accept(this);
                    this.print('(');
                    this.visit((PGTypeCastExpr)expr);
                    this.print(')');
                    return false;
                }
                expr.accept(this);
            }
        }
        this.print0("::");
        dataType.accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGExtractExpr x) {
        this.print0(this.ucase ? "EXTRACT (" : "extract (");
        this.print0(x.getField().name());
        this.print0(this.ucase ? " FROM " : " from ");
        x.getSource().accept(this);
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final PGBoxExpr x) {
        this.print0(this.ucase ? "BOX " : "box ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGPointExpr x) {
        this.print0(this.ucase ? "POINT " : "point ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGMacAddrExpr x) {
        this.print0("macaddr ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGInetExpr x) {
        this.print0("inet ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGCidrExpr x) {
        this.print0("cidr ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGPolygonExpr x) {
        this.print0("polygon ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGCircleExpr x) {
        this.print0("circle ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGLineSegmentsExpr x) {
        this.print0("lseg ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryExpr x) {
        this.print0(this.ucase ? "B'" : "b'");
        this.print0(x.getText());
        this.print('\'');
        return false;
    }
    
    @Override
    public boolean visit(final PGShowStatement x) {
        this.print0(this.ucase ? "SHOW " : "show ");
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final SQLLimit x) {
        this.print0(this.ucase ? "LIMIT " : "limit ");
        x.getRowCount().accept(this);
        if (x.getOffset() != null) {
            this.print0(this.ucase ? " OFFSET " : " offset ");
            x.getOffset().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final PGStartTransactionStatement x) {
        this.print0(this.ucase ? "START TRANSACTION" : "start transaction");
        return false;
    }
    
    @Override
    public boolean visit(final PGConnectToStatement x) {
        this.print0(this.ucase ? "CONNECT TO " : "connect to ");
        x.getTarget().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGCreateSchemaStatement x) {
        this.printUcase("CREATE SCHEMA ");
        if (x.isIfNotExists()) {
            this.printUcase("IF NOT EXISTS ");
        }
        if (x.getSchemaName() != null) {
            x.getSchemaName().accept(this);
        }
        if (x.isAuthorization()) {
            this.printUcase("AUTHORIZATION ");
            x.getUserName().accept(this);
        }
        return false;
    }
    
    @Override
    public void endVisit(final PGDropSchemaStatement x) {
        this.printUcase("DROP SCHEMA ");
        if (x.isIfExists()) {
            this.printUcase("IF EXISTS ");
        }
        x.getSchemaName().accept(this);
    }
    
    @Override
    public boolean visit(final PGDropSchemaStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGAlterSchemaStatement x) {
        this.printUcase("ALTER SCHEMA ");
        x.getSchemaName().accept(this);
        if (x.getNewName() != null) {
            this.print0(" RENAME TO ");
            x.getNewName().accept(this);
        }
        else if (x.getNewOwner() != null) {
            this.print0(" OWNER TO ");
            x.getNewOwner().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSetStatement x) {
        this.print0(this.ucase ? "SET " : "set ");
        final SQLSetStatement.Option option = x.getOption();
        if (option != null) {
            this.print(option.name());
            this.print(' ');
        }
        final List<SQLAssignItem> items = x.getItems();
        for (int i = 0; i < items.size(); ++i) {
            if (i != 0) {
                this.print0(", ");
            }
            final SQLAssignItem item = x.getItems().get(i);
            final SQLExpr target = item.getTarget();
            target.accept(this);
            final SQLExpr value = item.getValue();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).getName().equalsIgnoreCase("TIME ZONE")) {
                this.print(' ');
            }
            else if (value instanceof SQLPropertyExpr && ((SQLPropertyExpr)value).getOwner() instanceof SQLVariantRefExpr) {
                this.print0(" := ");
            }
            else {
                this.print0(" TO ");
            }
            if (value instanceof SQLListExpr) {
                final SQLListExpr listExpr = (SQLListExpr)value;
                this.printAndAccept(listExpr.getItems(), ", ");
            }
            else {
                value.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateUserStatement x) {
        this.print0(this.ucase ? "CREATE USER " : "create user ");
        x.getUser().accept(this);
        this.print0(this.ucase ? " PASSWORD " : " password ");
        final SQLExpr passoword = x.getPassword();
        if (passoword instanceof SQLIdentifierExpr) {
            this.print('\'');
            passoword.accept(this);
            this.print('\'');
        }
        else {
            passoword.accept(this);
        }
        return false;
    }
    
    @Override
    protected void printGrantPrivileges(final SQLGrantStatement x) {
        final List<SQLPrivilegeItem> privileges = x.getPrivileges();
        int i = 0;
        for (final SQLPrivilegeItem privilege : privileges) {
            if (i != 0) {
                this.print(", ");
            }
            final SQLExpr action = privilege.getAction();
            if (action instanceof SQLIdentifierExpr) {
                final String name = ((SQLIdentifierExpr)action).getName();
                if ("RESOURCE".equalsIgnoreCase(name)) {
                    continue;
                }
            }
            privilege.accept(this);
            ++i;
        }
    }
    
    @Override
    public boolean visit(final SQLGrantStatement x) {
        if (x.getResource() == null) {
            this.print("ALTER ROLE ");
            this.printAndAccept(x.getUsers(), ",");
            this.print(' ');
            final Set<SQLIdentifierExpr> pgPrivilegs = new LinkedHashSet<SQLIdentifierExpr>();
            for (final SQLPrivilegeItem privilege : x.getPrivileges()) {
                final SQLExpr action = privilege.getAction();
                if (action instanceof SQLIdentifierExpr) {
                    final String name = ((SQLIdentifierExpr)action).getName();
                    if (name.equalsIgnoreCase("CONNECT")) {
                        pgPrivilegs.add(new SQLIdentifierExpr("LOGIN"));
                    }
                    if (!name.toLowerCase().startsWith("create ")) {
                        continue;
                    }
                    pgPrivilegs.add(new SQLIdentifierExpr("CREATEDB"));
                }
            }
            int i = 0;
            for (final SQLExpr privilege2 : pgPrivilegs) {
                if (i != 0) {
                    this.print(' ');
                }
                privilege2.accept(this);
                ++i;
            }
            return false;
        }
        return super.visit(x);
    }
    
    @Override
    public boolean visit(final OracleSysdateExpr x) {
        this.print0(this.ucase ? "CURRENT_TIMESTAMP" : "CURRENT_TIMESTAMP");
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement.Item x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleArgumentExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleSetTransactionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExplainStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableDropPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableTruncatePartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.TableSpaceItem x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.UpdateIndexesClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableSplitPartition x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableModify x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateIndexStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleForStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleFileSpecification x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceAddDataFile x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTablespaceStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExitStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleContinueStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleRaiseStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateDatabaseDbLinkStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDropDbLinkStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalYear x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleDataTypeIntervalDay x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleUsingIndexClause x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleLobStorageClause x) {
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
        this.printAlias(x.getAlias());
        return false;
    }
    
    @Override
    public boolean visit(final PartitionExtensionClause x) {
        return false;
    }
    
    private void printHints(final List<SQLHint> hints) {
        if (hints.size() > 0) {
            this.print0("/*+ ");
            this.printAndAccept(hints, ", ");
            this.print0(" */");
        }
    }
    
    @Override
    public boolean visit(final OracleIntervalExpr x) {
        if (x.getValue() instanceof SQLLiteralExpr) {
            this.print0(this.ucase ? "INTERVAL " : "interval ");
            x.getValue().accept(this);
            this.print(' ');
        }
        else {
            this.print('(');
            x.getValue().accept(this);
            this.print0(") ");
        }
        this.print0(x.getType().name());
        if (x.getPrecision() != null) {
            this.print('(');
            this.printExpr(x.getPrecision());
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
                this.printExpr(x.getToFactionalSecondsPrecision());
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
    public boolean visit(final OracleLockTableStatement x) {
        this.print0(this.ucase ? "LOCK TABLE " : "lock table ");
        x.getTable().accept(this);
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
    public boolean visit(final OracleRangeExpr x) {
        x.getLowBound().accept(this);
        this.print0("..");
        x.getUpBound().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleCheck x) {
        this.visit(x);
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
            this.print(x.getPctfree());
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
    public boolean visit(final OraclePrimaryKey x) {
        this.visit((SQLUniqueConstraint)x);
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement x) {
        this.printCreateTable(x, false);
        if (x.getOf() != null) {
            this.println();
            this.print0(this.ucase ? "OF " : "of ");
            x.getOf().accept(this);
        }
        if (x.getOidIndex() != null) {
            this.println();
            x.getOidIndex().accept(this);
        }
        if (x.getOrganization() != null) {
            this.println();
            ++this.indentCount;
            x.getOrganization().accept(this);
            --this.indentCount;
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
        if (x.getSelect() != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            x.getSelect().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleStorageClause x) {
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
    public boolean visit(final OracleForeignKey x) {
        this.visit(x);
        return false;
    }
    
    @Override
    public boolean visit(final OracleUnique x) {
        this.visit(x);
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
        if (x.getPivot() != null) {
            this.println();
            x.getPivot().accept(this);
        }
        this.printFlashback(x.getFlashback());
        if (x.getAlias() != null && x.getAlias().length() != 0) {
            this.print(' ');
            this.print0(x.getAlias());
        }
        return false;
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
                right.accept(this);
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
                x.getCondition().accept(this);
                this.print(' ');
            }
            if (x.getUsing().size() > 0) {
                this.print0(this.ucase ? " USING (" : " using (");
                this.printAndAccept(x.getUsing(), ", ");
                this.print(')');
            }
            this.printFlashback(x.getFlashback());
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
    public boolean visit(final OracleDeleteStatement x) {
        return this.visit(x);
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
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final OracleAnalyticWindowing x) {
        this.print0(x.getType().name().toUpperCase());
        this.print(' ');
        x.getExpr().accept(this);
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
    public boolean visit(final SQLCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.getType() != null) {
            this.print0(x.getType());
            this.print(' ');
        }
        this.print0(this.ucase ? "INDEX" : "index");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? " IF NOT EXISTS" : " if not exists");
        }
        if (x.isConcurrently()) {
            this.print0(this.ucase ? " CONCURRENTLY" : " concurrently");
        }
        final SQLName name = x.getName();
        if (name != null) {
            this.print(' ');
            name.accept(this);
        }
        this.print0(this.ucase ? " ON " : " on ");
        x.getTable().accept(this);
        if (x.getUsing() != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(x.getUsing());
        }
        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            comment.accept(this);
        }
        boolean hasOptions = false;
        if (x.getIndexDefinition().hasOptions()) {
            final SQLIndexOptions indexOptions = x.getIndexDefinition().getOptions();
            if (indexOptions.getKeyBlockSize() != null || indexOptions.getParserName() != null || indexOptions.getAlgorithm() != null || indexOptions.getLock() != null || indexOptions.getOtherOptions().size() > 0) {
                hasOptions = true;
            }
        }
        if (hasOptions) {
            this.print0(this.ucase ? " WITH (" : " with (");
            final SQLIndexOptions indexOptions = x.getIndexDefinition().getOptions();
            final SQLExpr keyBlockSize = indexOptions.getKeyBlockSize();
            if (keyBlockSize != null) {
                this.print0(this.ucase ? " KEY_BLOCK_SIZE = " : " key_block_size = ");
                this.printExpr(keyBlockSize, this.parameterized);
            }
            final String parserName = indexOptions.getParserName();
            if (parserName != null) {
                this.print0(this.ucase ? " WITH PARSER " : " with parser ");
                this.print0(parserName);
            }
            final String algorithm = indexOptions.getAlgorithm();
            if (algorithm != null) {
                this.print0(this.ucase ? " ALGORITHM = " : " algorithm = ");
                this.print0(algorithm);
            }
            final String lock = indexOptions.getLock();
            if (lock != null) {
                this.print0(this.ucase ? " LOCK " : " lock ");
                this.print0(lock);
            }
            for (final SQLAssignItem option : indexOptions.getOtherOptions()) {
                option.accept(this);
            }
            this.print(')');
        }
        final SQLName tablespace = x.getTablespace();
        if (tablespace != null) {
            this.print0(this.ucase ? " TABLESPACE " : " tablespace ");
            tablespace.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        final boolean odps = this.isOdps();
        this.print0(this.ucase ? "ADD COLUMN " : "add column ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLArrayDataType x) {
        x.getComponentType().accept(this);
        this.print('[');
        this.printAndAccept(x.getArguments(), ", ");
        this.print(']');
        return false;
    }
}
