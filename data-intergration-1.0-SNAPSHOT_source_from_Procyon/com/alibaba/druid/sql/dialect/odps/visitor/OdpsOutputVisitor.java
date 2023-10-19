// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.visitor;

import com.alibaba.druid.sql.dialect.odps.ast.OdpsInstallPackageStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsNewExpr;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsExstoreStatement;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsTransformExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsQueryAliasStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCountStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetFileFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetChangeLogs;
import com.alibaba.druid.sql.ast.statement.SQLWhoamiStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddFileStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsGrantStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsListStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsShowGrantsStmt;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsReadStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsStatisticClause;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveStatisticStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddStatisticStatement;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLZOrderBy;
import com.alibaba.druid.sql.ast.SQLWindow;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSetLabelStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUDTFSQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.ClusteringType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import java.util.HashSet;
import com.alibaba.druid.DbType;
import java.util.Set;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveOutputVisitor;

public class OdpsOutputVisitor extends HiveOutputVisitor implements OdpsASTVisitor
{
    private Set<String> builtInFunctions;
    
    public OdpsOutputVisitor(final Appendable appender) {
        super(appender, DbType.odps);
        (this.builtInFunctions = new HashSet<String>()).add("IF");
        this.builtInFunctions.add("COALESCE");
        this.builtInFunctions.add("TO_DATE");
        this.builtInFunctions.add("SUBSTR");
        this.builtInFunctions.add("INSTR");
        this.builtInFunctions.add("LENGTH");
        this.builtInFunctions.add("SPLIT");
        this.builtInFunctions.add("TOLOWER");
        this.builtInFunctions.add("TOUPPER");
        this.builtInFunctions.add("EXPLODE");
        this.builtInFunctions.add("LEAST");
        this.builtInFunctions.add("GREATEST");
        this.groupItemSingleLine = true;
    }
    
    @Override
    public boolean visit(final OdpsCreateTableStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isExternal()) {
            this.print0(this.ucase ? "EXTERNAL " : "external ");
        }
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "TABLE IF NOT EXISTS " : "table if not exists ");
        }
        else {
            this.print0(this.ucase ? "TABLE " : "table ");
        }
        x.getName().accept(this);
        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }
        final List<SQLTableElement> tableElementList = x.getTableElementList();
        final int size = tableElementList.size();
        if (size > 0) {
            this.print0(" (");
            if (this.isPrettyFormat() && x.hasBodyBeforeComment()) {
                this.print(' ');
                this.printlnComment(x.getBodyBeforeCommentsDirect());
            }
            ++this.indentCount;
            this.println();
            for (int i = 0; i < size; ++i) {
                final SQLTableElement element = tableElementList.get(i);
                element.accept(this);
                if (i != size - 1) {
                    this.print(',');
                }
                if (this.isPrettyFormat() && element.hasAfterComment()) {
                    this.print(' ');
                    this.printlnComment(element.getAfterCommentsDirect());
                }
                if (i != size - 1) {
                    this.println();
                }
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        if (x.getComment() != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }
        final int partitionSize = x.getPartitionColumns().size();
        if (partitionSize > 0) {
            this.println();
            this.print0(this.ucase ? "PARTITIONED BY (" : "partitioned by (");
            ++this.indentCount;
            this.println();
            for (int j = 0; j < partitionSize; ++j) {
                final SQLColumnDefinition column = x.getPartitionColumns().get(j);
                column.accept(this);
                if (j != partitionSize - 1) {
                    this.print(',');
                }
                if (this.isPrettyFormat() && column.hasAfterComment()) {
                    this.print(' ');
                    this.printlnComment(column.getAfterCommentsDirect());
                }
                if (j != partitionSize - 1) {
                    this.println();
                }
            }
            --this.indentCount;
            this.println();
            this.print(')');
        }
        final List<SQLSelectOrderByItem> clusteredBy = x.getClusteredBy();
        if (clusteredBy.size() > 0) {
            this.println();
            if (x.getClusteringType() == ClusteringType.Range) {
                this.print0(this.ucase ? "RANGE " : "range ");
            }
            this.print0(this.ucase ? "CLUSTERED BY (" : "clustered by (");
            this.printAndAccept(clusteredBy, ",");
            this.print(')');
        }
        final List<SQLSelectOrderByItem> sortedBy = x.getSortedBy();
        if (sortedBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "SORTED BY (" : "sorted by (");
            this.printAndAccept(sortedBy, ", ");
            this.print(')');
        }
        final int buckets = x.getBuckets();
        if (buckets > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(buckets);
            this.print0(this.ucase ? " BUCKETS" : " buckets");
        }
        final int shards = x.getShards();
        if (shards > 0) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            this.print(shards);
            this.print0(this.ucase ? " SHARDS" : " shards");
        }
        if (x.getLifecycle() != null) {
            this.println();
            this.print0(this.ucase ? "LIFECYCLE " : "lifecycle ");
            x.getLifecycle().accept(this);
        }
        final SQLSelect select = x.getSelect();
        if (select != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            select.accept(this);
        }
        final SQLExpr storedBy = x.getStoredBy();
        if (storedBy != null) {
            this.println();
            this.print0(this.ucase ? "STORED BY " : "stored by ");
            storedBy.accept(this);
        }
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.println();
            this.print0(this.ucase ? "STORED AS " : "stored as ");
            storedAs.accept(this);
        }
        final List<SQLExpr> withSerdeproperties = x.getWithSerdeproperties();
        if (withSerdeproperties.size() > 0) {
            this.println();
            this.print0(this.ucase ? "WITH SERDEPROPERTIES (" : "with serdeproperties (");
            this.printAndAccept(withSerdeproperties, ", ");
            this.print(')');
        }
        this.printTblProperties(x);
        final SQLExpr location = x.getLocation();
        if (location != null) {
            this.println();
            this.print0(this.ucase ? "LOCATION " : "location ");
            location.accept(this);
        }
        final SQLExpr using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        this.print('(');
        ++this.indentCount;
        this.println();
        x.getSelect().accept(this);
        --this.indentCount;
        this.println();
        this.print(')');
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource x) {
        final SQLTableSource left = x.getLeft();
        left.accept(this);
        if (left.hasAfterComment() && this.isPrettyFormat()) {
            this.println();
            this.printlnComment(left.getAfterCommentsDirect());
        }
        if (x.getJoinType() == SQLJoinTableSource.JoinType.COMMA) {
            this.print(',');
        }
        else {
            this.println();
            this.printJoinType(x.getJoinType());
        }
        this.print(' ');
        x.getRight().accept(this);
        if (x.getCondition() != null) {
            this.println();
            this.print0(this.ucase ? "ON " : "on ");
            ++this.indentCount;
            x.getCondition().accept(this);
            --this.indentCount;
        }
        if (x.getUsing().size() > 0) {
            this.print0(this.ucase ? " USING (" : " using (");
            this.printAndAccept(x.getUsing(), ", ");
            this.print(')');
        }
        if (x.getAlias() != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }
        final SQLJoinTableSource.UDJ udj = x.getUdj();
        if (udj != null) {
            this.println();
            udj.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsUDTFSQLSelectItem x) {
        x.getExpr().accept(this);
        this.println();
        this.print0(this.ucase ? "AS (" : "as (");
        final int aliasSize = x.getAliasList().size();
        if (aliasSize > 5) {
            ++this.indentCount;
            this.println();
        }
        for (int i = 0; i < aliasSize; ++i) {
            if (i != 0) {
                if (aliasSize > 5) {
                    this.println(",");
                }
                else {
                    this.print0(", ");
                }
            }
            this.print0(x.getAliasList().get(i));
        }
        if (aliasSize > 5) {
            --this.indentCount;
            this.println();
        }
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowStatisticStmt x) {
        this.print0(this.ucase ? "SHOW STATISTIC" : "show statistic");
        final SQLExprTableSource tableSource = x.getTableSource();
        if (tableSource != null) {
            this.print(' ');
            tableSource.accept(this);
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (!partitions.isEmpty()) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(partitions, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsSetLabelStatement x) {
        this.print0(this.ucase ? "SET LABEL " : "set label ");
        this.print0(x.getLabel());
        this.print0(this.ucase ? " TO " : " to ");
        if (x.getUser() != null) {
            this.print0(this.ucase ? "USER " : "user ");
            x.getUser().accept(this);
        }
        else if (x.getTable() != null) {
            this.print0(this.ucase ? "TABLE " : "table ");
            x.getTable().accept(this);
            if (x.getColumns().size() > 0) {
                this.print('(');
                this.printAndAccept(x.getColumns(), ", ");
                this.print(')');
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsSelectQueryBlock x) {
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "SELECT " : "select ");
        final List<SQLCommentHint> hints = x.getHintsDirect();
        if (hints != null) {
            this.printAndAccept(hints, " ");
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
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            from.accept(this);
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            if (where.hasBeforeComment() && this.isPrettyFormat()) {
                this.printlnComments(x.getWhere().getBeforeCommentsDirect());
            }
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
        final SQLZOrderBy zorderBy = x.getZOrderBy();
        if (zorderBy != null) {
            this.println();
            zorderBy.accept(this);
        }
        final List<SQLSelectOrderByItem> distributeBy = x.getDistributeByDirect();
        if (distributeBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "DISTRIBUTE BY " : "distribute by ");
            this.printAndAccept(distributeBy, ", ");
        }
        final List<SQLSelectOrderByItem> sortBy = x.getSortByDirect();
        if (!sortBy.isEmpty()) {
            this.println();
            this.print0(this.ucase ? "SORT BY " : "sort by ");
            this.printAndAccept(sortBy, ", ");
        }
        final List<SQLSelectOrderByItem> clusterBy = x.getClusterByDirect();
        if (clusterBy.size() > 0) {
            this.println();
            this.print0(this.ucase ? "CLUSTER BY " : "cluster by ");
            this.printAndAccept(clusterBy, ", ");
        }
        if (x.getLimit() != null) {
            this.println();
            x.getLimit().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLOrderBy x) {
        final int itemSize = x.getItems().size();
        if (itemSize > 0) {
            this.print0(this.ucase ? "ORDER BY " : "order by ");
            ++this.indentCount;
            for (int i = 0; i < itemSize; ++i) {
                if (i != 0) {
                    this.println(", ");
                }
                x.getItems().get(i).accept(this);
            }
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLZOrderBy x) {
        final int itemSize = x.getItems().size();
        if (itemSize > 0) {
            this.print0(this.ucase ? "ZORDER BY " : "zorder by ");
            ++this.indentCount;
            for (int i = 0; i < itemSize; ++i) {
                if (i != 0) {
                    this.println(", ");
                }
                x.getItems().get(i).accept(this);
            }
            --this.indentCount;
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddStatisticStatement x) {
        this.print0(this.ucase ? "ADD STATISTIC " : "add statistic ");
        x.getTable().accept(this);
        this.print(' ');
        x.getStatisticClause().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsRemoveStatisticStatement x) {
        this.print0(this.ucase ? "REMOVE STATISTIC " : "remove statistic ");
        x.getTable().accept(this);
        this.print(' ');
        x.getStatisticClause().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.TableCount x) {
        this.print0(this.ucase ? "TABLE_COUNT" : "table_count");
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.ExpressionCondition x) {
        this.print0(this.ucase ? "EXPRESSION_CONDITION " : "expression_condition ");
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.NullValue x) {
        this.print0(this.ucase ? "NULL_VALUE " : "null_value ");
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.DistinctValue x) {
        this.print0(this.ucase ? "DISTINCT_VALUE " : "distinct_value ");
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.ColumnSum x) {
        this.print0(this.ucase ? "COLUMN_SUM " : "column_sum ");
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.ColumnMax x) {
        this.print0(this.ucase ? "COLUMN_MAX " : "column_max ");
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsStatisticClause.ColumnMin x) {
        this.print0(this.ucase ? "COLUMN_MIN " : "column_min ");
        x.getColumn().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsReadStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "READ " : "read ");
        x.getTable().accept(this);
        if (x.getColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        if (x.getRowCount() != null) {
            this.print(' ');
            x.getRowCount().accept(this);
        }
        return false;
    }
    
    @Override
    protected void printMethodOwner(final SQLExpr owner) {
        owner.accept(this);
        if (owner instanceof SQLMethodInvokeExpr) {
            this.print('.');
        }
        else {
            this.print(':');
        }
    }
    
    @Override
    protected void printJoinType(final SQLJoinTableSource.JoinType joinType) {
        if (joinType.equals(SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN)) {
            this.print0(this.ucase ? "LEFT OUTER JOIN" : "left outer join");
        }
        else if (joinType.equals(SQLJoinTableSource.JoinType.RIGHT_OUTER_JOIN)) {
            this.print0(this.ucase ? "RIGHT OUTER JOIN" : "right outer join");
        }
        else if (joinType.equals(SQLJoinTableSource.JoinType.FULL_OUTER_JOIN)) {
            this.print0(this.ucase ? "FULL OUTER JOIN" : "full outer join");
        }
        else {
            this.print0(this.ucase ? joinType.name : joinType.name_lcase);
        }
    }
    
    @Override
    public boolean visit(final SQLDataType x) {
        final String dataTypeName = x.getName();
        if (dataTypeName.indexOf(60) != -1 || dataTypeName.equals("Object")) {
            this.print0(dataTypeName);
        }
        else {
            this.print0(this.ucase ? dataTypeName.toUpperCase() : dataTypeName.toLowerCase());
        }
        if (x.getArguments().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getArguments(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    protected void printFunctionName(final String name) {
        if (name == null) {
            return;
        }
        final String upperName = name.toUpperCase();
        if (this.builtInFunctions.contains(upperName)) {
            this.print0(this.ucase ? upperName : name);
        }
        else {
            this.print0(name);
        }
    }
    
    @Override
    public boolean visit(final OdpsShowGrantsStmt x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        if (x.isLabel()) {
            this.print0(this.ucase ? "SHOW LABEL GRANTS" : "show label grants");
            if (x.getObjectType() != null) {
                this.print0(this.ucase ? " ON TABLE " : " on table ");
                x.getObjectType().accept(this);
            }
            if (x.getUser() != null) {
                this.print0(this.ucase ? " FOR USER " : " for user ");
                x.getUser().accept(this);
            }
        }
        else {
            this.print0(this.ucase ? "SHOW GRANTS" : "show grants");
            if (x.getUser() != null) {
                this.print0(this.ucase ? " FOR " : " for ");
                x.getUser().accept(this);
            }
            if (x.getObjectType() != null) {
                this.print0(this.ucase ? " ON TYPE " : " on type ");
                x.getObjectType().accept(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsListStmt x) {
        this.print0(this.ucase ? "LIST " : "list ");
        if (x.getObject() != null) {
            x.getObject().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsGrantStmt x) {
        this.print0(this.ucase ? "GRANT " : "grant ");
        if (x.isSuper()) {
            this.print0(this.ucase ? "SUPER " : "super ");
        }
        if (x.isLabel()) {
            this.print0(this.ucase ? "LABEL " : "label ");
            x.getLabel().accept(this);
        }
        else {
            this.printAndAccept(x.getPrivileges(), ", ");
        }
        if (x.getResource() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            if (x.getResourceType() != null) {
                this.print0(this.ucase ? x.getResourceType().name() : x.getResourceType().name().toLowerCase());
                this.print(' ');
            }
            x.getResource().accept(this);
            if (x.getColumns().size() > 0) {
                this.print('(');
                this.printAndAccept(x.getColumns(), ", ");
                this.print(')');
            }
        }
        if (x.getUsers() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            if (x.getSubjectType() != null) {
                this.print0(x.getSubjectType().name());
                this.print(' ');
            }
            this.printAndAccept(x.getUsers(), ",");
        }
        if (x.getExpire() != null) {
            this.print0(this.ucase ? " WITH EXP " : " with exp ");
            x.getExpire().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x, final boolean parameterized) {
        final String text = x.getText();
        if (text == null) {
            this.print0(this.ucase ? "NULL" : "null");
        }
        else {
            final StringBuilder buf = new StringBuilder(text.length() + 2);
            buf.append('\'');
            for (int i = 0; i < text.length(); ++i) {
                final char ch = text.charAt(i);
                switch (ch) {
                    case '\\': {
                        buf.append("\\\\");
                        break;
                    }
                    case '\'': {
                        buf.append("\\'");
                        break;
                    }
                    case '\0': {
                        buf.append("\\0");
                        break;
                    }
                    case '\n': {
                        buf.append("\\n");
                        break;
                    }
                    default: {
                        buf.append(ch);
                        break;
                    }
                }
            }
            buf.append('\'');
            this.print0(buf.toString());
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableRenameColumn x) {
        this.print0(this.ucase ? "CHANGE COLUMN " : "change column ");
        x.getColumn().accept(this);
        this.print0(this.ucase ? " RENAME TO " : " rename to ");
        x.getTo().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddTableStatement x) {
        this.print0(this.ucase ? "ADD TABLE " : "add table ");
        x.getTable().accept(this);
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions.size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(partitions, ", ");
            this.print(')');
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment);
        }
        if (x.isForce()) {
            this.print0(" -f");
        }
        final SQLName toPackage = x.getToPackage();
        if (toPackage != null) {
            this.print0(this.ucase ? " TO PACKAGE " : " to package ");
            this.printExpr(toPackage);
            final List<SQLPrivilegeItem> privileges = x.getPrivileges();
            if (!privileges.isEmpty()) {
                this.print0(this.ucase ? " WITH PRIVILEGES " : " with privileges ");
                this.printAndAccept(privileges, ", ");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddFileStatement x) {
        this.print0(this.ucase ? "ADD " : "add ");
        final OdpsAddFileStatement.FileType type = x.getType();
        switch (type) {
            case JAR: {
                this.print0(this.ucase ? "JAR " : "jar ");
                break;
            }
            case ARCHIVE: {
                this.print0(this.ucase ? "ARCHIVE " : "archive ");
                break;
            }
            case PY: {
                this.print0(this.ucase ? "PY " : "py ");
                break;
            }
            default: {
                this.print0(this.ucase ? "FILE " : "file ");
                break;
            }
        }
        this.print0(x.getFile());
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            this.printExpr(comment);
        }
        if (x.isForce()) {
            this.print0(" -f");
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddUserStatement x) {
        this.print0(this.ucase ? "ADD USER " : "add user ");
        this.printExpr(x.getUser());
        return false;
    }
    
    @Override
    public boolean visit(final OdpsRemoveUserStatement x) {
        this.print0(this.ucase ? "REMOVE USER " : "remove user ");
        this.printExpr(x.getUser());
        return false;
    }
    
    @Override
    public boolean visit(final SQLWhoamiStatement x) {
        this.print0(this.ucase ? "WHOAMI" : "whoami");
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAlterTableSetChangeLogs x) {
        this.print0(this.ucase ? "SET CHANGELOGS " : "set changelogs ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAlterTableSetFileFormat x) {
        this.print0(this.ucase ? "SET FILEFORMAT " : "set fileformat ");
        x.getValue().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsCountStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                this.visit(hint);
                this.println();
            }
        }
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        this.print0(this.ucase ? "COUNT " : "count ");
        x.getTable().accept(this);
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions.size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(partitions, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsQueryAliasStatement x) {
        this.print0(x.getVariant());
        this.print0(" := ");
        x.getStatement().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsTransformExpr x) {
        this.print0(this.ucase ? "TRANSFORM(" : "transform(");
        this.printAndAccept(x.getInputColumns(), ", ");
        this.print(')');
        final SQLExpr using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            using.accept(this);
        }
        final List<SQLExpr> resources = x.getResources();
        if (!resources.isEmpty()) {
            this.println();
            this.print0(this.ucase ? "RESOURCES " : "resources ");
            this.printAndAccept(resources, ", ");
        }
        final List<SQLColumnDefinition> outputColumns = x.getOutputColumns();
        if (!outputColumns.isEmpty()) {
            this.println();
            this.print0(this.ucase ? "AS (" : "as (");
            this.printAndAccept(outputColumns, ", ");
            this.print(')');
        }
        final SQLExternalRecordFormat inputRowFormat = x.getInputRowFormat();
        if (inputRowFormat != null) {
            this.println();
            this.print0(this.ucase ? "ROW FORMAT DELIMITED" : "row format delimited");
            inputRowFormat.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsExstoreStatement x) {
        this.print0(this.ucase ? "EXSTORE " : "exstore ");
        x.getTable().accept(this);
        this.print0(this.ucase ? " PARTITION (" : " partition (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final HiveLoadDataStatement x) {
        this.print0(this.ucase ? "LOAD " : "load ");
        if (x.isOverwrite()) {
            this.print0(this.ucase ? "OVERWRITE " : "overwrite ");
        }
        this.print0(this.ucase ? "INTO TABLE " : "into table ");
        x.getInto().accept(this);
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        this.println();
        this.print0(this.ucase ? "LOCATION " : "location ");
        x.getInpath().accept(this);
        final SQLExpr storedBy = x.getStoredBy();
        if (storedBy != null) {
            this.println();
            this.print0(this.ucase ? "STORED BY " : "stored by ");
            storedBy.accept(this);
        }
        final SQLExpr rowFormat = x.getRowFormat();
        if (rowFormat != null) {
            this.println();
            this.print0(this.ucase ? "ROW FORMAT SERDE " : "row format serde ");
            rowFormat.accept(this);
        }
        this.printSerdeProperties(x.getSerdeProperties());
        final SQLExpr storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.println();
            this.print0(this.ucase ? "STORED AS " : "stored as ");
            this.printExpr(storedAs);
        }
        final SQLExpr using = x.getUsing();
        if (using != null) {
            this.println();
            this.print0(this.ucase ? "USING " : "using ");
            this.printExpr(using);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsNewExpr x) {
        this.print0("new ");
        super.visit((SQLMethodInvokeExpr)x);
        return false;
    }
    
    @Override
    public boolean visit(final OdpsInstallPackageStatement x) {
        this.print0(this.ucase ? "INSTALL PACKAGE " : "install package ");
        this.printExpr(x.getPackageName());
        return false;
    }
}
