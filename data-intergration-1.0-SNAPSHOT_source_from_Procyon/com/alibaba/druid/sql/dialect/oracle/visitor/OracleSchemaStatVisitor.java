// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleXmlColumnProperties;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsOfTypeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePipeRowStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTypeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
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
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.repository.SchemaRepository;
import java.util.List;
import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class OracleSchemaStatVisitor extends SchemaStatVisitor implements OracleASTVisitor
{
    public OracleSchemaStatVisitor() {
        this(new ArrayList<Object>());
    }
    
    public OracleSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    public OracleSchemaStatVisitor(final List<Object> parameters) {
        super(DbType.oracle, parameters);
    }
    
    @Override
    protected TableStat.Column getColumn(SQLExpr expr) {
        if (expr instanceof OracleOuterExpr) {
            expr = ((OracleOuterExpr)expr).getExpr();
        }
        return super.getColumn(expr);
    }
    
    @Override
    public boolean visit(final OracleSelectTableReference x) {
        final SQLExpr expr = x.getExpr();
        final TableStat stat = this.getTableStat(x);
        if (!(expr instanceof SQLName)) {
            return false;
        }
        if (((SQLName)expr).nameHashCode64() == FnvHash.Constants.DUAL) {
            return false;
        }
        if (expr instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
            if (this.isSubQueryOrParamOrVariant(propertyExpr)) {
                return false;
            }
        }
        else if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            if (this.isSubQueryOrParamOrVariant(identifierExpr)) {
                return false;
            }
        }
        final TableStat.Mode mode = this.getMode();
        switch (mode) {
            case Delete: {
                stat.incrementDeleteCount();
                break;
            }
            case Insert: {
                stat.incrementInsertCount();
                break;
            }
            case Update: {
                stat.incrementUpdateCount();
                break;
            }
            case Select: {
                stat.incrementSelectCount();
                break;
            }
            case Merge: {
                stat.incrementMergeCount();
                break;
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleUpdateStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        this.setMode(x, TableStat.Mode.Update);
        final SQLTableSource tableSource = x.getTableSource();
        SQLExpr tableExpr = null;
        if (tableSource instanceof SQLExprTableSource) {
            tableExpr = ((SQLExprTableSource)tableSource).getExpr();
        }
        if (tableExpr instanceof SQLName) {
            final TableStat stat = this.getTableStat((SQLName)tableExpr);
            stat.incrementUpdateCount();
        }
        else {
            tableSource.accept(this);
        }
        this.accept(x.getItems());
        this.accept(x.getWhere());
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectQueryBlock x) {
        final SQLExprTableSource into = x.getInto();
        return this.visit(x);
    }
    
    @Override
    public void endVisit(final OracleSelectQueryBlock x) {
        this.endVisit(x);
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        return !"ROWNUM".equalsIgnoreCase(x.getName()) && super.visit(x);
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        final String name = x.getName();
        if ("+".equalsIgnoreCase(name)) {
            return false;
        }
        final long hashCode64 = x.hashCode64();
        return hashCode64 != FnvHash.Constants.ROWNUM && hashCode64 != FnvHash.Constants.SYSDATE && hashCode64 != FnvHash.Constants.LEVEL && hashCode64 != FnvHash.Constants.SQLCODE && (hashCode64 != FnvHash.Constants.ISOPEN || !(x.getParent() instanceof SQLBinaryOpExpr) || ((SQLBinaryOpExpr)x.getParent()).getOperator() != SQLBinaryOperator.Modulus) && super.visit(x);
    }
    
    @Override
    public boolean visit(final OracleSelectJoin x) {
        super.visit(x);
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectSubqueryTableSource x) {
        this.accept(x.getSelect());
        this.accept(x.getPivot());
        this.accept(x.getFlashback());
        return false;
    }
    
    @Override
    public boolean visit(final OracleWithSubqueryEntry x) {
        x.getSubQuery().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
        if (x.getTableName() instanceof SQLName) {
            final TableStat stat = this.getTableStat(x.getTableName());
            stat.incrementInsertCount();
        }
        this.accept(x.getColumns());
        this.accept(x.getQuery());
        this.accept(x.getReturning());
        this.accept(x.getErrorLogging());
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        x.putAttribute("_original_use_mode", this.getMode());
        this.setMode(x, TableStat.Mode.Insert);
        this.accept(x.getSubQuery());
        this.accept(x.getEntries());
        return false;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
        SQLObject parent = x.getParent();
        if (parent instanceof OracleMultiInsertStatement.ConditionalInsertClause) {
            parent = parent.getParent();
        }
        if (parent instanceof OracleMultiInsertStatement) {
            ((OracleMultiInsertStatement)parent).getSubQuery();
        }
        x.getWhen().accept(this);
        x.getThen().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSessionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleLockTableStatement x) {
        this.getTableStat(x.getTable());
        return false;
    }
    
    @Override
    public boolean visit(final OracleSysdateExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleExceptionStatement.Item x) {
        final SQLExpr when = x.getWhen();
        if (when instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr ident = (SQLIdentifierExpr)when;
            if (!ident.getName().equalsIgnoreCase("OTHERS")) {
                this.visit(ident);
            }
        }
        else if (when != null) {
            when.accept(this);
        }
        for (final SQLStatement stmt : x.getStatements()) {
            stmt.accept(this);
        }
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
        final SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        final SQLName tableName = stmt.getName();
        for (final SQLColumnDefinition column : x.getColumns()) {
            final SQLName columnName = column.getName();
            this.addColumn(tableName, columnName.toString());
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleForStatement x) {
        x.getRange().accept(this);
        this.accept(x.getStatements());
        return false;
    }
    
    @Override
    public boolean visit(final OraclePrimaryKey x) {
        this.accept(x.getColumns());
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement x) {
        this.visit(x);
        if (x.getSelect() != null) {
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
        return false;
    }
    
    @Override
    public boolean visit(final OracleLabelStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTriggerStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterSynonymStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterViewStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleAlterTableMoveTablespace x) {
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
    public boolean visit(final OracleCreateTableStatement.Organization x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTableStatement.OIDIndex x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreatePackageStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        for (final SQLStatement stmt : x.getStatements()) {
            stmt.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleExecuteImmediateStatement x) {
        SQLExpr dynamicSql = x.getDynamicSql();
        String sql = null;
        if (dynamicSql instanceof SQLIdentifierExpr) {
            final String varName = ((SQLIdentifierExpr)dynamicSql).getName();
            SQLExpr valueExpr = null;
            if (x.getParent() instanceof SQLBlockStatement) {
                final List<SQLStatement> statementList = ((SQLBlockStatement)x.getParent()).getStatementList();
                for (int i = 0, size = statementList.size(); i < size; ++i) {
                    final SQLStatement stmt = statementList.get(i);
                    if (stmt == x) {
                        break;
                    }
                    if (stmt instanceof SQLSetStatement) {
                        final List<SQLAssignItem> items = ((SQLSetStatement)stmt).getItems();
                        for (final SQLAssignItem item : items) {
                            if (item.getTarget().equals(dynamicSql)) {
                                valueExpr = item.getValue();
                                break;
                            }
                        }
                    }
                }
            }
            if (valueExpr != null) {
                dynamicSql = valueExpr;
            }
        }
        if (dynamicSql instanceof SQLCharExpr) {
            sql = ((SQLCharExpr)dynamicSql).getText();
        }
        if (sql != null) {
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, this.dbType);
            for (final SQLStatement stmt2 : stmtList) {
                stmt2.accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateSynonymStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleCreateTypeStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OraclePipeRowStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleIsOfTypeExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties x) {
        return false;
    }
    
    @Override
    public boolean visit(final OracleXmlColumnProperties.OracleXMLTypeStorage x) {
        return false;
    }
}
