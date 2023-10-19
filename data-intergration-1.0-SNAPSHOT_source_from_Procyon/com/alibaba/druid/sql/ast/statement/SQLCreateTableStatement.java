// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.semantic.SemanticException;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.Map;
import java.util.Arrays;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateSynonymStatement;
import com.alibaba.druid.util.ListDG;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Collections;
import com.alibaba.druid.util.lang.Consumer;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import java.util.HashMap;
import java.util.Collection;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.Iterator;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.ClusteringType;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateTableStatement extends SQLStatementImpl implements SQLDDLStatement, SQLCreateStatement
{
    protected boolean ifNotExists;
    protected Type type;
    protected SQLExprTableSource tableSource;
    protected List<SQLTableElement> tableElementList;
    protected SQLExprTableSource inherits;
    protected SQLSelect select;
    protected SQLExpr comment;
    protected SQLExprTableSource like;
    protected Boolean compress;
    protected Boolean logging;
    protected SQLName tablespace;
    protected SQLPartitionBy partitioning;
    protected SQLExpr storedAs;
    protected SQLExpr location;
    protected boolean onCommitPreserveRows;
    protected boolean onCommitDeleteRows;
    protected boolean external;
    protected SQLExternalRecordFormat rowFormat;
    protected final List<SQLColumnDefinition> partitionColumns;
    protected ClusteringType clusteringType;
    protected final List<SQLSelectOrderByItem> clusteredBy;
    protected final List<SQLSelectOrderByItem> sortedBy;
    protected int buckets;
    protected int shards;
    protected final List<SQLAssignItem> tableOptions;
    protected final List<SQLAssignItem> tblProperties;
    protected boolean replace;
    protected boolean ignore;
    protected boolean dimension;
    protected SQLExpr engine;
    
    public SQLCreateTableStatement() {
        this.ifNotExists = false;
        this.tableElementList = new ArrayList<SQLTableElement>();
        this.partitionColumns = new ArrayList<SQLColumnDefinition>(2);
        this.clusteredBy = new ArrayList<SQLSelectOrderByItem>();
        this.sortedBy = new ArrayList<SQLSelectOrderByItem>();
        this.tableOptions = new ArrayList<SQLAssignItem>();
        this.tblProperties = new ArrayList<SQLAssignItem>();
        this.replace = false;
        this.ignore = false;
    }
    
    public SQLCreateTableStatement(final DbType dbType) {
        super(dbType);
        this.ifNotExists = false;
        this.tableElementList = new ArrayList<SQLTableElement>();
        this.partitionColumns = new ArrayList<SQLColumnDefinition>(2);
        this.clusteredBy = new ArrayList<SQLSelectOrderByItem>();
        this.sortedBy = new ArrayList<SQLSelectOrderByItem>();
        this.tableOptions = new ArrayList<SQLAssignItem>();
        this.tblProperties = new ArrayList<SQLAssignItem>();
        this.replace = false;
        this.ignore = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v);
        }
        v.endVisit(this);
    }
    
    protected void acceptChild(final SQLASTVisitor v) {
        this.acceptChild(v, this.tableSource);
        this.acceptChild(v, this.tableElementList);
        this.acceptChild(v, this.inherits);
        this.acceptChild(v, this.select);
        this.acceptChild(v, this.comment);
        this.acceptChild(v, this.like);
        this.acceptChild(v, this.tablespace);
        this.acceptChild(v, this.partitioning);
        this.acceptChild(v, this.storedAs);
        this.acceptChild(v, this.location);
        this.acceptChild(v, this.partitionColumns);
        this.acceptChild(v, this.clusteredBy);
        this.acceptChild(v, this.sortedBy);
        this.acceptChild(v, this.tableOptions);
        this.acceptChild(v, this.tblProperties);
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public SQLName getName() {
        if (this.tableSource == null) {
            return null;
        }
        return (SQLName)this.tableSource.getExpr();
    }
    
    public String getTableName() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        return name.getSimpleName();
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    public void setSchema(final String name) {
        if (this.tableSource == null) {
            return;
        }
        this.tableSource.setSchema(name);
    }
    
    public void setName(final SQLName name) {
        this.setTableSource(new SQLExprTableSource(name));
    }
    
    public void setName(final String name) {
        this.setName(new SQLIdentifierExpr(name));
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    public void setTableName(final String tableName) {
        final SQLExpr name = SQLUtils.toSQLExpr(tableName, this.dbType);
        this.setTableSource(new SQLExprTableSource(name));
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public List<SQLTableElement> getTableElementList() {
        return this.tableElementList;
    }
    
    public SQLColumnDefinition getColumn(final String columnName) {
        final long hashCode64 = FnvHash.hashCode64(columnName);
        for (final SQLTableElement e : this.tableElementList) {
            if (e instanceof SQLColumnDefinition) {
                final SQLColumnDefinition column = (SQLColumnDefinition)e;
                if (column.nameHashCode64() == hashCode64) {
                    return column;
                }
                continue;
            }
        }
        return null;
    }
    
    public List<SQLColumnDefinition> getColumnDefinitions() {
        final ArrayList<SQLColumnDefinition> column = new ArrayList<SQLColumnDefinition>();
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLColumnDefinition) {
                column.add((SQLColumnDefinition)element);
            }
        }
        return column;
    }
    
    public List<String> getColumnNames(final boolean normalized) {
        final List<String> columnNames = new ArrayList<String>();
        for (final SQLColumnDefinition definition : this.getColumnDefinitions()) {
            String columnName = definition.getColumnName();
            if (normalized) {
                columnName = SQLUtils.normalize(columnName);
            }
            columnNames.add(columnName);
        }
        return columnNames;
    }
    
    public List<String> getColumnComments() {
        final List<String> comments = new ArrayList<String>();
        for (final SQLColumnDefinition definition : this.getColumnDefinitions()) {
            comments.add(((SQLCharExpr)definition.getComment()).getText());
        }
        return comments;
    }
    
    public List<String> getPrimaryKeyNames() {
        final List<String> keys = new ArrayList<String>();
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof MySqlPrimaryKey) {
                final List<SQLSelectOrderByItem> columns = ((MySqlPrimaryKey)element).getColumns();
                for (final SQLSelectOrderByItem column : columns) {
                    keys.add(SQLUtils.normalize(column.getExpr().toString()));
                }
            }
        }
        return keys;
    }
    
    public void addColumn(final String columnName, final String dataType) {
        final SQLColumnDefinition column = new SQLColumnDefinition();
        column.setName(columnName);
        column.setDataType(SQLParserUtils.createExprParser(dataType, this.dbType, new SQLParserFeature[0]).parseDataType());
        this.addColumn(column);
    }
    
    public void addColumn(final SQLColumnDefinition column) {
        if (column == null) {
            throw new IllegalArgumentException();
        }
        column.setParent(this);
        this.tableElementList.add(column);
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExiists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public SQLExprTableSource getInherits() {
        return this.inherits;
    }
    
    public void setInherits(final SQLExprTableSource inherits) {
        if (inherits != null) {
            inherits.setParent(this);
        }
        this.inherits = inherits;
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect select) {
        if (select != null) {
            select.setParent(this);
        }
        this.select = select;
    }
    
    public SQLExprTableSource getLike() {
        return this.like;
    }
    
    public void setLike(final SQLName like) {
        this.setLike(new SQLExprTableSource(like));
    }
    
    public void setLike(final SQLExprTableSource like) {
        if (like != null) {
            like.setParent(this);
        }
        this.like = like;
    }
    
    public Boolean getCompress() {
        return this.compress;
    }
    
    public void setCompress(final Boolean compress) {
        this.compress = compress;
    }
    
    public Boolean getLogging() {
        return this.logging;
    }
    
    public void setLogging(final Boolean logging) {
        this.logging = logging;
    }
    
    public SQLName getTablespace() {
        return this.tablespace;
    }
    
    public void setTablespace(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.tablespace = x;
    }
    
    public SQLPartitionBy getPartitioning() {
        return this.partitioning;
    }
    
    public void setPartitioning(final SQLPartitionBy partitioning) {
        if (partitioning != null) {
            partitioning.setParent(this);
        }
        this.partitioning = partitioning;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.tableSource);
        children.addAll(this.tableElementList);
        if (this.inherits != null) {
            children.add(this.inherits);
        }
        if (this.select != null) {
            children.add(this.select);
        }
        return children;
    }
    
    public void addBodyBeforeComment(final List<String> comments) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }
        final List<String> attrComments = this.attributes.get("rowFormat.body_before_comment");
        if (attrComments == null) {
            this.attributes.put("rowFormat.body_before_comment", comments);
        }
        else {
            attrComments.addAll(comments);
        }
    }
    
    public List<String> getBodyBeforeCommentsDirect() {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get("rowFormat.body_before_comment");
    }
    
    public boolean hasBodyBeforeComment() {
        final List<String> comments = this.getBodyBeforeCommentsDirect();
        return comments != null && !comments.isEmpty();
    }
    
    public String computeName() {
        if (this.tableSource == null) {
            return null;
        }
        final SQLExpr expr = this.tableSource.getExpr();
        if (expr instanceof SQLName) {
            final String name = ((SQLName)expr).getSimpleName();
            return SQLUtils.normalize(name);
        }
        return null;
    }
    
    public SQLColumnDefinition findColumn(final String columName) {
        if (columName == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columName);
        return this.findColumn(hash);
    }
    
    public SQLColumnDefinition findColumn(final long columName_hash) {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLColumnDefinition) {
                final SQLColumnDefinition column = (SQLColumnDefinition)element;
                if (column.nameHashCode64() == columName_hash) {
                    return column;
                }
                continue;
            }
        }
        for (final SQLColumnDefinition column2 : this.partitionColumns) {
            if (column2.nameHashCode64() == columName_hash) {
                return column2;
            }
        }
        return null;
    }
    
    public boolean isPrimaryColumn(final String columnName) {
        final SQLPrimaryKey pk = this.findPrimaryKey();
        if (pk != null && pk.containsColumn(columnName)) {
            return true;
        }
        for (final SQLColumnDefinition element : this.getColumnDefinitions()) {
            for (final SQLColumnConstraint constraint : element.constraints) {
                if (constraint instanceof SQLColumnPrimaryKey && SQLUtils.normalize(element.getColumnName()).equalsIgnoreCase(SQLUtils.normalize(columnName))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isPrimaryColumn(final long columnNameHash) {
        final SQLPrimaryKey pk = this.findPrimaryKey();
        return pk != null && pk.containsColumn(columnNameHash);
    }
    
    public boolean isOnlyPrimaryKey(final long columnNameHash) {
        final SQLPrimaryKey pk = this.findPrimaryKey();
        return pk != null && pk.containsColumn(columnNameHash) && pk.getColumns().size() == 1;
    }
    
    public boolean isMUL(final String columnName) {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof MySqlUnique) {
                final MySqlUnique unique = (MySqlUnique)element;
                final SQLExpr column = unique.getColumns().get(0).getExpr();
                if (column instanceof SQLIdentifierExpr && SQLUtils.nameEquals(columnName, ((SQLIdentifierExpr)column).getName())) {
                    return unique.getColumns().size() > 1;
                }
                if (column instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)column).getMethodName(), columnName)) {
                    return true;
                }
                continue;
            }
            else {
                if (!(element instanceof MySqlKey)) {
                    continue;
                }
                final MySqlKey unique2 = (MySqlKey)element;
                final SQLExpr column = unique2.getColumns().get(0).getExpr();
                if (column instanceof SQLIdentifierExpr && SQLUtils.nameEquals(columnName, ((SQLIdentifierExpr)column).getName())) {
                    return true;
                }
                if (column instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)column).getMethodName(), columnName)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean isUNI(final String columnName) {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof MySqlUnique) {
                final MySqlUnique unique = (MySqlUnique)element;
                if (unique.getColumns().size() == 0) {
                    continue;
                }
                final SQLExpr column = unique.getColumns().get(0).getExpr();
                if (column instanceof SQLIdentifierExpr && SQLUtils.nameEquals(columnName, ((SQLIdentifierExpr)column).getName())) {
                    return unique.getColumns().size() == 1;
                }
                if (column instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)column).getMethodName(), columnName)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public MySqlUnique findUnique(final String columnName) {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof MySqlUnique) {
                final MySqlUnique unique = (MySqlUnique)element;
                if (unique.containsColumn(columnName)) {
                    return unique;
                }
                continue;
            }
        }
        return null;
    }
    
    public SQLTableElement findIndex(final String columnName) {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLUniqueConstraint) {
                final SQLUniqueConstraint unique = (SQLUniqueConstraint)element;
                for (final SQLSelectOrderByItem item : unique.getColumns()) {
                    final SQLExpr columnExpr = item.getExpr();
                    if (columnExpr instanceof SQLIdentifierExpr) {
                        String keyColumName = ((SQLIdentifierExpr)columnExpr).getName();
                        keyColumName = SQLUtils.normalize(keyColumName);
                        if (keyColumName.equalsIgnoreCase(columnName)) {
                            return element;
                        }
                        continue;
                    }
                }
            }
            else {
                if (!(element instanceof MySqlTableIndex)) {
                    continue;
                }
                final List<SQLSelectOrderByItem> indexColumns = ((MySqlTableIndex)element).getColumns();
                for (final SQLSelectOrderByItem orderByItem : indexColumns) {
                    final SQLExpr columnExpr = orderByItem.getExpr();
                    if (columnExpr instanceof SQLIdentifierExpr) {
                        String keyColumName = ((SQLIdentifierExpr)columnExpr).getName();
                        keyColumName = SQLUtils.normalize(keyColumName);
                        if (keyColumName.equalsIgnoreCase(columnName)) {
                            return element;
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public void forEachColumn(final Consumer<SQLColumnDefinition> columnConsumer) {
        if (columnConsumer == null) {
            return;
        }
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLColumnDefinition) {
                columnConsumer.accept((SQLColumnDefinition)element);
            }
        }
    }
    
    public SQLPrimaryKey findPrimaryKey() {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLPrimaryKey) {
                return (SQLPrimaryKey)element;
            }
        }
        return null;
    }
    
    public List<SQLForeignKeyConstraint> findForeignKey() {
        final List<SQLForeignKeyConstraint> fkList = new ArrayList<SQLForeignKeyConstraint>();
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLForeignKeyConstraint) {
                fkList.add((SQLForeignKeyConstraint)element);
            }
        }
        return fkList;
    }
    
    public boolean hashForeignKey() {
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLForeignKeyConstraint) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isReferenced(final SQLName tableName) {
        return tableName != null && this.isReferenced(tableName.getSimpleName());
    }
    
    public boolean isReferenced(String tableName) {
        if (tableName == null) {
            return false;
        }
        tableName = SQLUtils.normalize(tableName);
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLForeignKeyConstraint) {
                final SQLForeignKeyConstraint fk = (SQLForeignKeyConstraint)element;
                final String refTableName = fk.getReferencedTableName().getSimpleName();
                if (SQLUtils.nameEquals(tableName, refTableName)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public SQLAlterTableStatement foreignKeyToAlterTable() {
        final SQLAlterTableStatement stmt = new SQLAlterTableStatement();
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement element = this.tableElementList.get(i);
            if (element instanceof SQLForeignKeyConstraint) {
                final SQLForeignKeyConstraint fk = (SQLForeignKeyConstraint)element;
                this.tableElementList.remove(i);
                stmt.addItem(new SQLAlterTableAddConstraint(fk));
            }
        }
        if (stmt.getItems().size() == 0) {
            return null;
        }
        stmt.setDbType(this.getDbType());
        stmt.setTableSource(this.tableSource.clone());
        Collections.reverse(stmt.getItems());
        return stmt;
    }
    
    public static void sort(final List<SQLStatement> stmtList) {
        final Map<String, SQLCreateTableStatement> tables = new HashMap<String, SQLCreateTableStatement>();
        final Map<String, List<SQLCreateTableStatement>> referencedTables = new HashMap<String, List<SQLCreateTableStatement>>();
        for (final SQLStatement stmt : stmtList) {
            if (stmt instanceof SQLCreateTableStatement) {
                final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)stmt;
                String tableName = createTableStmt.getName().getSimpleName();
                tableName = SQLUtils.normalize(tableName).toLowerCase();
                tables.put(tableName, createTableStmt);
            }
        }
        final List<ListDG.Edge> edges = new ArrayList<ListDG.Edge>();
        for (final SQLCreateTableStatement stmt2 : tables.values()) {
            for (final SQLTableElement element : stmt2.getTableElementList()) {
                if (element instanceof SQLForeignKeyConstraint) {
                    final SQLForeignKeyConstraint fk = (SQLForeignKeyConstraint)element;
                    String refTableName = fk.getReferencedTableName().getSimpleName();
                    refTableName = SQLUtils.normalize(refTableName).toLowerCase();
                    final SQLCreateTableStatement refTable = tables.get(refTableName);
                    if (refTable != null) {
                        edges.add(new ListDG.Edge(stmt2, refTable));
                    }
                    List<SQLCreateTableStatement> referencedList = referencedTables.get(refTableName);
                    if (referencedList == null) {
                        referencedList = new ArrayList<SQLCreateTableStatement>();
                        referencedTables.put(refTableName, referencedList);
                    }
                    referencedList.add(stmt2);
                }
            }
        }
        for (final SQLStatement stmt3 : stmtList) {
            if (stmt3 instanceof OracleCreateSynonymStatement) {
                final OracleCreateSynonymStatement createSynonym = (OracleCreateSynonymStatement)stmt3;
                final SQLName object = createSynonym.getObject();
                final String refTableName2 = object.getSimpleName();
                final SQLCreateTableStatement refTable2 = tables.get(refTableName2);
                if (refTable2 == null) {
                    continue;
                }
                edges.add(new ListDG.Edge(stmt3, refTable2));
            }
        }
        ListDG dg = new ListDG(stmtList, edges);
        SQLStatement[] tops = new SQLStatement[stmtList.size()];
        if (dg.topologicalSort(tops)) {
            for (int i = 0, size = stmtList.size(); i < size; ++i) {
                stmtList.set(i, tops[size - i - 1]);
            }
            return;
        }
        final List<SQLAlterTableStatement> alterList = new ArrayList<SQLAlterTableStatement>();
        for (int j = edges.size() - 1; j >= 0; --j) {
            final ListDG.Edge edge = edges.get(j);
            final SQLCreateTableStatement from = (SQLCreateTableStatement)edge.from;
            String fromTableName = from.getName().getSimpleName();
            fromTableName = SQLUtils.normalize(fromTableName).toLowerCase();
            if (referencedTables.containsKey(fromTableName)) {
                edges.remove(j);
                Arrays.fill(tops, null);
                tops = new SQLStatement[stmtList.size()];
                dg = new ListDG(stmtList, edges);
                if (dg.topologicalSort(tops)) {
                    for (int k = 0, size2 = stmtList.size(); k < size2; ++k) {
                        final SQLStatement stmt4 = tops[size2 - k - 1];
                        stmtList.set(k, stmt4);
                    }
                    final SQLAlterTableStatement alter = from.foreignKeyToAlterTable();
                    alterList.add(alter);
                    stmtList.add(alter);
                    return;
                }
                edges.add(j, edge);
            }
        }
        for (int j = edges.size() - 1; j >= 0; --j) {
            final ListDG.Edge edge = edges.get(j);
            final SQLCreateTableStatement from = (SQLCreateTableStatement)edge.from;
            String fromTableName = from.getName().getSimpleName();
            fromTableName = SQLUtils.normalize(fromTableName).toLowerCase();
            if (referencedTables.containsKey(fromTableName)) {
                final SQLAlterTableStatement alter = from.foreignKeyToAlterTable();
                edges.remove(j);
                if (alter != null) {
                    alterList.add(alter);
                }
                Arrays.fill(tops, null);
                tops = new SQLStatement[stmtList.size()];
                dg = new ListDG(stmtList, edges);
                if (dg.topologicalSort(tops)) {
                    for (int l = 0, size3 = stmtList.size(); l < size3; ++l) {
                        final SQLStatement stmt5 = tops[size3 - l - 1];
                        stmtList.set(l, stmt5);
                    }
                    stmtList.addAll(alterList);
                    return;
                }
            }
        }
    }
    
    public void simplify() {
        SQLName name = this.getName();
        if (name instanceof SQLPropertyExpr) {
            String tableName = ((SQLPropertyExpr)name).getName();
            tableName = SQLUtils.normalize(tableName);
            final String normalized = SQLUtils.normalize(tableName, this.dbType);
            if (tableName != normalized) {
                this.setName(normalized);
                name = this.getName();
            }
        }
        if (name instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)name;
            final String tableName2 = identExpr.getName();
            final String normalized2 = SQLUtils.normalize(tableName2, this.dbType);
            if (normalized2 != tableName2) {
                this.setName(normalized2);
            }
        }
        for (final SQLTableElement element : this.tableElementList) {
            if (element instanceof SQLColumnDefinition) {
                final SQLColumnDefinition column = (SQLColumnDefinition)element;
                column.simplify();
            }
            else {
                if (!(element instanceof SQLConstraint)) {
                    continue;
                }
                ((SQLConstraint)element).simplify();
            }
        }
    }
    
    public boolean apply(final SQLDropIndexStatement x) {
        final long indexNameHashCode64 = x.getIndexName().nameHashCode64();
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLUniqueConstraint) {
                final SQLUniqueConstraint unique = (SQLUniqueConstraint)e;
                if (unique.getName().nameHashCode64() == indexNameHashCode64) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
            else if (e instanceof MySqlTableIndex) {
                final MySqlTableIndex tableIndex = (MySqlTableIndex)e;
                if (SQLUtils.nameEquals(tableIndex.getName(), x.getIndexName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean apply(final SQLCommentStatement x) {
        final SQLName on = x.getOn().getName();
        final SQLExpr comment = x.getComment();
        if (comment == null) {
            return false;
        }
        final SQLCommentStatement.Type type = x.getType();
        if (type == SQLCommentStatement.Type.TABLE) {
            if (!SQLUtils.nameEquals(this.getName(), on)) {
                return false;
            }
            this.setComment(comment.clone());
            return true;
        }
        else {
            if (type != SQLCommentStatement.Type.COLUMN) {
                return false;
            }
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)on;
            if (!SQLUtils.nameEquals(this.getName(), (SQLName)propertyExpr.getOwner())) {
                return false;
            }
            final SQLColumnDefinition column = this.findColumn(propertyExpr.nameHashCode64());
            if (column != null) {
                column.setComment(comment.clone());
            }
            return true;
        }
    }
    
    public boolean apply(final SQLAlterTableStatement alter) {
        if (!SQLUtils.nameEquals(alter.getName(), this.getName())) {
            return false;
        }
        int applyCount = 0;
        for (final SQLAlterTableItem item : alter.getItems()) {
            if (this.alterApply(item)) {
                ++applyCount;
            }
        }
        return applyCount > 0;
    }
    
    protected boolean alterApply(final SQLAlterTableItem item) {
        if (item instanceof SQLAlterTableDropColumnItem) {
            return this.apply((SQLAlterTableDropColumnItem)item);
        }
        if (item instanceof SQLAlterTableAddColumn) {
            return this.apply((SQLAlterTableAddColumn)item);
        }
        if (item instanceof SQLAlterTableAddConstraint) {
            return this.apply((SQLAlterTableAddConstraint)item);
        }
        if (item instanceof SQLAlterTableDropPrimaryKey) {
            return this.apply((SQLAlterTableDropPrimaryKey)item);
        }
        if (item instanceof SQLAlterTableDropIndex) {
            return this.apply((SQLAlterTableDropIndex)item);
        }
        if (item instanceof SQLAlterTableDropConstraint) {
            return this.apply((SQLAlterTableDropConstraint)item);
        }
        if (item instanceof SQLAlterTableDropKey) {
            return this.apply((SQLAlterTableDropKey)item);
        }
        if (item instanceof SQLAlterTableDropForeignKey) {
            return this.apply((SQLAlterTableDropForeignKey)item);
        }
        if (item instanceof SQLAlterTableRename) {
            return this.apply((SQLAlterTableRename)item);
        }
        if (item instanceof SQLAlterTableRenameColumn) {
            return this.apply((SQLAlterTableRenameColumn)item);
        }
        return item instanceof SQLAlterTableAddIndex && this.apply((SQLAlterTableAddIndex)item);
    }
    
    private boolean apply(final SQLAlterTableRenameColumn item) {
        final int columnIndex = this.columnIndexOf(item.getColumn());
        if (columnIndex == -1) {
            return false;
        }
        final SQLColumnDefinition column = this.tableElementList.get(columnIndex);
        column.setName(item.getTo().clone());
        return true;
    }
    
    public boolean renameColumn(final String colummName, final String newColumnName) {
        if (colummName == null || newColumnName == null || newColumnName.length() == 0) {
            return false;
        }
        final int columnIndex = this.columnIndexOf(new SQLIdentifierExpr(colummName));
        if (columnIndex == -1) {
            return false;
        }
        final SQLColumnDefinition column = this.tableElementList.get(columnIndex);
        column.setName(new SQLIdentifierExpr(newColumnName));
        return true;
    }
    
    private boolean apply(final SQLAlterTableRename item) {
        final SQLName name = item.getToName();
        if (name == null) {
            return false;
        }
        this.setName(name.clone());
        return true;
    }
    
    private boolean apply(final SQLAlterTableDropForeignKey item) {
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLForeignKeyConstraint) {
                final SQLForeignKeyConstraint fk = (SQLForeignKeyConstraint)e;
                if (SQLUtils.nameEquals(fk.getName(), item.getIndexName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean apply(final SQLAlterTableDropKey item) {
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLUniqueConstraint) {
                final SQLUniqueConstraint unique = (SQLUniqueConstraint)e;
                if (SQLUtils.nameEquals(unique.getName(), item.getKeyName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean apply(final SQLAlterTableDropConstraint item) {
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLConstraint) {
                final SQLConstraint constraint = (SQLConstraint)e;
                if (SQLUtils.nameEquals(constraint.getName(), item.getConstraintName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean apply(final SQLAlterTableDropIndex item) {
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLUniqueConstraint) {
                final SQLUniqueConstraint unique = (SQLUniqueConstraint)e;
                if (SQLUtils.nameEquals(unique.getName(), item.getIndexName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
            else if (e instanceof MySqlTableIndex) {
                final MySqlTableIndex tableIndex = (MySqlTableIndex)e;
                if (SQLUtils.nameEquals(tableIndex.getName(), item.getIndexName())) {
                    this.tableElementList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean apply(final SQLAlterTableDropPrimaryKey item) {
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLPrimaryKey) {
                this.tableElementList.remove(i);
                return true;
            }
        }
        return false;
    }
    
    private boolean apply(final SQLAlterTableAddConstraint item) {
        final SQLName name = item.getConstraint().getName();
        if (name != null) {
            final long nameHashCode = name.nameHashCode64();
            for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
                final SQLTableElement e = this.tableElementList.get(i);
                if (e instanceof SQLConstraint) {
                    final SQLName name2 = ((SQLConstraint)e).getName();
                    if (name2 != null && name2.nameHashCode64() == nameHashCode) {
                        return false;
                    }
                }
            }
        }
        this.tableElementList.add((SQLTableElement)item.getConstraint());
        return true;
    }
    
    private boolean apply(final SQLAlterTableDropColumnItem item) {
        for (final SQLName column : item.getColumns()) {
            final String columnName = column.getSimpleName();
            for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
                final SQLTableElement e = this.tableElementList.get(i);
                if (e instanceof SQLColumnDefinition && SQLUtils.nameEquals(columnName, ((SQLColumnDefinition)e).getName().getSimpleName())) {
                    this.tableElementList.remove(i);
                }
            }
            for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
                final SQLTableElement e = this.tableElementList.get(i);
                if (e instanceof SQLUnique) {
                    final SQLUnique unique = (SQLUnique)e;
                    unique.applyDropColumn(column);
                    if (unique.getColumns().size() == 0) {
                        this.tableElementList.remove(i);
                    }
                }
                else if (e instanceof MySqlTableIndex) {
                    final MySqlTableIndex index = (MySqlTableIndex)e;
                    index.applyDropColumn(column);
                    if (index.getColumns().size() == 0) {
                        this.tableElementList.remove(i);
                    }
                }
            }
        }
        return true;
    }
    
    protected boolean apply(final SQLAlterTableAddIndex item) {
        return false;
    }
    
    private boolean apply(final SQLAlterTableAddColumn item) {
        int startIndex = this.tableElementList.size();
        if (item.isFirst()) {
            startIndex = 0;
        }
        final int afterIndex = this.columnIndexOf(item.getAfterColumn());
        if (afterIndex != -1) {
            startIndex = afterIndex + 1;
        }
        final int beforeIndex = this.columnIndexOf(item.getFirstColumn());
        if (beforeIndex != -1) {
            startIndex = beforeIndex;
        }
        for (int i = 0; i < item.getColumns().size(); ++i) {
            final SQLColumnDefinition column = item.getColumns().get(i);
            int matchIndex = -1;
            for (int j = 0; j < this.tableElementList.size(); ++j) {
                final SQLTableElement element = this.tableElementList.get(j);
                if (element instanceof SQLColumnDefinition && column.nameHashCode64() == ((SQLColumnDefinition)element).nameHashCode64()) {
                    matchIndex = j;
                    break;
                }
            }
            if (matchIndex != -1) {
                return false;
            }
            this.tableElementList.add(i + startIndex, column);
            column.setParent(this);
        }
        return true;
    }
    
    protected int columnIndexOf(final SQLName column) {
        if (column == null) {
            return -1;
        }
        final String columnName = column.getSimpleName();
        for (int i = this.tableElementList.size() - 1; i >= 0; --i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof SQLColumnDefinition && SQLUtils.nameEquals(columnName, ((SQLColumnDefinition)e).getName().getSimpleName())) {
                return i;
            }
        }
        return -1;
    }
    
    public void cloneTo(final SQLCreateTableStatement x) {
        x.setExternal(this.external);
        x.ifNotExists = this.ifNotExists;
        x.type = this.type;
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
        }
        for (final SQLTableElement e : this.tableElementList) {
            final SQLTableElement e2 = e.clone();
            e2.setParent(x);
            x.tableElementList.add(e2);
        }
        for (final SQLColumnDefinition e3 : this.partitionColumns) {
            final SQLColumnDefinition e4 = e3.clone();
            e4.setParent(x);
            x.partitionColumns.add(e4);
        }
        if (this.inherits != null) {
            x.setInherits(this.inherits.clone());
        }
        if (this.select != null) {
            x.setSelect(this.select.clone());
        }
        if (this.comment != null) {
            x.setComment(this.comment.clone());
        }
        if (this.partitioning != null) {
            x.setPartitioning(this.partitioning.clone());
        }
        if (this.like != null) {
            x.setLike(this.like.clone());
        }
        x.compress = this.compress;
        x.logging = this.logging;
        if (this.tablespace != null) {
            x.setTablespace(this.tablespace.clone());
        }
        if (this.partitioning != null) {
            x.setPartitioning(this.partitioning.clone());
        }
        if (this.storedAs != null) {
            x.setStoredAs(this.storedAs.clone());
        }
        if (this.location != null) {
            x.setLocation(this.location.clone());
        }
        x.onCommitPreserveRows = this.onCommitPreserveRows;
        x.onCommitDeleteRows = this.onCommitDeleteRows;
        for (final SQLAssignItem item : this.tableOptions) {
            final SQLAssignItem item2 = item.clone();
            item2.setParent(item);
            x.tableOptions.add(item2);
        }
        for (final SQLAssignItem item : this.tblProperties) {
            final SQLAssignItem item2 = item.clone();
            item2.setParent(item);
            x.tblProperties.add(item2);
        }
        if (this.rowFormat != null) {
            x.setRowFormat(this.rowFormat.clone());
        }
        if (this.clusteringType != null) {
            x.setClusteringType(this.clusteringType);
        }
        for (final SQLSelectOrderByItem e5 : this.clusteredBy) {
            final SQLSelectOrderByItem e6 = e5.clone();
            e6.setParent(x);
            x.clusteredBy.add(e6);
        }
        for (final SQLSelectOrderByItem e5 : this.sortedBy) {
            final SQLSelectOrderByItem e6 = e5.clone();
            e6.setParent(x);
            x.sortedBy.add(e6);
        }
        x.buckets = this.buckets;
        x.shards = this.shards;
        x.dimension = this.dimension;
    }
    
    public boolean isReplace() {
        return this.replace;
    }
    
    public void setReplace(final boolean replace) {
        this.ignore = false;
        this.replace = replace;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.replace = false;
        this.ignore = ignore;
    }
    
    public SQLExpr getStoredAs() {
        return this.storedAs;
    }
    
    public void setStoredAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedAs = x;
    }
    
    @Override
    public SQLCreateTableStatement clone() {
        final SQLCreateTableStatement x = new SQLCreateTableStatement(this.dbType);
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    public boolean isOnCommitPreserveRows() {
        return this.onCommitPreserveRows;
    }
    
    public void setOnCommitPreserveRows(final boolean onCommitPreserveRows) {
        this.onCommitPreserveRows = onCommitPreserveRows;
    }
    
    public boolean isExternal() {
        return this.external;
    }
    
    public void setExternal(final boolean external) {
        this.external = external;
    }
    
    public ClusteringType getClusteringType() {
        return this.clusteringType;
    }
    
    public void setClusteringType(final ClusteringType clusteringType) {
        this.clusteringType = clusteringType;
    }
    
    public List<SQLSelectOrderByItem> getClusteredBy() {
        return this.clusteredBy;
    }
    
    public void addClusteredByItem(final SQLSelectOrderByItem item) {
        item.setParent(this);
        this.clusteredBy.add(item);
    }
    
    public List<SQLSelectOrderByItem> getSortedBy() {
        return this.sortedBy;
    }
    
    public void addSortedByItem(final SQLSelectOrderByItem item) {
        item.setParent(this);
        this.sortedBy.add(item);
    }
    
    public int getBuckets() {
        return this.buckets;
    }
    
    public void setBuckets(final int buckets) {
        this.buckets = buckets;
    }
    
    public int getShards() {
        return this.shards;
    }
    
    public void setShards(final int shards) {
        this.shards = shards;
    }
    
    public List<SQLColumnDefinition> getPartitionColumns() {
        return this.partitionColumns;
    }
    
    public void addPartitionColumn(final SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.partitionColumns.add(column);
    }
    
    public List<SQLAssignItem> getTableOptions() {
        return this.tableOptions;
    }
    
    public List<SQLAssignItem> getTblProperties() {
        return this.tblProperties;
    }
    
    public void addTblProperty(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        assignItem.setParent(this);
        this.tblProperties.add(assignItem);
    }
    
    public SQLExternalRecordFormat getRowFormat() {
        return this.rowFormat;
    }
    
    public void setRowFormat(final SQLExternalRecordFormat x) {
        if (x != null) {
            x.setParent(this);
        }
        this.rowFormat = x;
    }
    
    public boolean isDimension() {
        return this.dimension;
    }
    
    public void setDimension(final boolean dimension) {
        this.dimension = dimension;
    }
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public void setLocation(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.location = x;
    }
    
    public void addOption(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        assignItem.setParent(this);
        this.tableOptions.add(assignItem);
    }
    
    public SQLExpr getOption(final String name) {
        if (name == null) {
            return null;
        }
        final long hash64 = FnvHash.hashCode64(name);
        for (final SQLAssignItem item : this.tableOptions) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                return item.getValue();
            }
        }
        return null;
    }
    
    public SQLExpr getTblProperty(final String name) {
        if (name == null) {
            return null;
        }
        final long hash64 = FnvHash.hashCode64(name);
        for (final SQLAssignItem item : this.tblProperties) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                return item.getValue();
            }
        }
        return null;
    }
    
    public Object getOptionValue(final String name) {
        final SQLExpr option = this.getOption(name);
        if (option instanceof SQLValuableExpr) {
            return ((SQLValuableExpr)option).getValue();
        }
        return null;
    }
    
    public Object getTblPropertyValue(final String name) {
        final SQLExpr option = this.getTblProperty(name);
        if (option instanceof SQLValuableExpr) {
            return ((SQLValuableExpr)option).getValue();
        }
        return null;
    }
    
    public Object getOptionOrTblPropertyValue(final String name) {
        SQLExpr option = this.getTblProperty(name);
        if (option == null) {
            option = this.getOption(name);
        }
        if (option instanceof SQLValuableExpr) {
            return ((SQLValuableExpr)option).getValue();
        }
        return null;
    }
    
    public String getCatalog() {
        return null;
    }
    
    public boolean containsDuplicateColumnNames() {
        return this.containsDuplicateColumnNames(false);
    }
    
    public boolean containsDuplicateColumnNames(final boolean throwException) {
        final Map<Long, SQLTableElement> columnMap = new HashMap<Long, SQLTableElement>();
        for (final SQLTableElement item : this.tableElementList) {
            if (item instanceof SQLColumnDefinition) {
                final SQLName columnName = ((SQLColumnDefinition)item).getName();
                final long nameHashCode64 = columnName.nameHashCode64();
                final SQLTableElement old = columnMap.put(nameHashCode64, item);
                if (old == null) {
                    continue;
                }
                if (throwException) {
                    throw new SemanticException("Table contains duplicate column names : " + SQLUtils.normalize(columnName.getSimpleName()));
                }
                return true;
            }
        }
        return false;
    }
    
    public SQLExpr getEngine() {
        return this.engine;
    }
    
    public void setEngine(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.engine = x;
    }
    
    public enum Type
    {
        GLOBAL_TEMPORARY, 
        LOCAL_TEMPORARY, 
        TEMPORARY, 
        SHADOW;
    }
}
