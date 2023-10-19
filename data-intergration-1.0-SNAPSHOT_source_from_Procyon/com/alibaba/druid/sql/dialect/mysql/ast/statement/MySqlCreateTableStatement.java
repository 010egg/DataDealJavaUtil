// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlExprImpl;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.alibaba.druid.sql.ast.statement.SQLAlterCharacter;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import java.io.IOException;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlShowColumnOutpuVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.ads.visitor.AdsOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import java.util.HashMap;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class MySqlCreateTableStatement extends SQLCreateTableStatement implements MySqlStatement
{
    private List<SQLCommentHint> hints;
    private List<SQLCommentHint> optionHints;
    private SQLName tableGroup;
    protected SQLExpr dbPartitionBy;
    protected SQLExpr dbPartitions;
    protected SQLExpr tablePartitionBy;
    protected SQLExpr tablePartitions;
    protected MySqlExtPartition exPartition;
    protected SQLName storedBy;
    protected SQLName distributeByType;
    protected List<SQLName> distributeBy;
    protected boolean isBroadCast;
    protected Map<String, SQLName> with;
    protected SQLName archiveBy;
    protected Boolean withData;
    
    public MySqlCreateTableStatement() {
        super(DbType.mysql);
        this.hints = new ArrayList<SQLCommentHint>();
        this.optionHints = new ArrayList<SQLCommentHint>();
        this.distributeBy = new ArrayList<SQLName>();
        this.with = new HashMap<String, SQLName>(3);
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    @Deprecated
    public SQLSelect getQuery() {
        return this.select;
    }
    
    @Deprecated
    public void setQuery(final SQLSelect query) {
        this.select = query;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else if (visitor instanceof AdsOutputVisitor) {
            this.accept0((AdsOutputVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    public void accept0(final AdsOutputVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.hints.size(); ++i) {
                final SQLCommentHint hint = this.hints.get(i);
                if (hint != null) {
                    hint.accept(visitor);
                }
            }
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            for (int i = 0; i < this.tableElementList.size(); ++i) {
                final SQLTableElement element = this.tableElementList.get(i);
                if (element != null) {
                    element.accept(visitor);
                }
            }
            if (this.like != null) {
                this.like.accept(visitor);
            }
            if (this.select != null) {
                this.select.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.hints.size(); ++i) {
                final SQLCommentHint hint = this.hints.get(i);
                if (hint != null) {
                    hint.accept(visitor);
                }
            }
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            for (int i = 0; i < this.tableElementList.size(); ++i) {
                final SQLTableElement element = this.tableElementList.get(i);
                if (element != null) {
                    element.accept(visitor);
                }
            }
            if (this.like != null) {
                this.like.accept(visitor);
            }
            if (this.select != null) {
                this.select.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public List<SQLCommentHint> getOptionHints() {
        return this.optionHints;
    }
    
    public void setOptionHints(final List<SQLCommentHint> optionHints) {
        this.optionHints = optionHints;
    }
    
    public SQLName getTableGroup() {
        return this.tableGroup;
    }
    
    public void setTableGroup(final SQLName tableGroup) {
        this.tableGroup = tableGroup;
    }
    
    public void setTableGroup(final String tableGroup) {
        if (StringUtils.isEmpty(tableGroup)) {
            this.tableGroup = null;
        }
        else {
            this.tableGroup = new SQLIdentifierExpr(tableGroup);
        }
    }
    
    @Override
    public void simplify() {
        this.tableOptions.clear();
        this.tblProperties.clear();
        super.simplify();
    }
    
    public void showCoumns(final Appendable out) throws IOException {
        this.accept(new MySqlShowColumnOutpuVisitor(out));
    }
    
    public List<MySqlKey> getMysqlKeys() {
        final List<MySqlKey> mySqlKeys = new ArrayList<MySqlKey>();
        for (final SQLTableElement element : this.getTableElementList()) {
            if (element instanceof MySqlKey) {
                mySqlKeys.add((MySqlKey)element);
            }
        }
        return mySqlKeys;
    }
    
    public List<MySqlTableIndex> getMysqlIndexes() {
        final List<MySqlTableIndex> indexList = new ArrayList<MySqlTableIndex>();
        for (final SQLTableElement element : this.getTableElementList()) {
            if (element instanceof MySqlTableIndex) {
                indexList.add((MySqlTableIndex)element);
            }
        }
        return indexList;
    }
    
    public boolean apply(final MySqlRenameTableStatement x) {
        for (final MySqlRenameTableStatement.Item item : x.getItems()) {
            if (this.apply(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean alterApply(final SQLAlterTableItem item) {
        if (item instanceof MySqlAlterTableAlterColumn) {
            return this.apply((MySqlAlterTableAlterColumn)item);
        }
        if (item instanceof MySqlAlterTableChangeColumn) {
            return this.apply((MySqlAlterTableChangeColumn)item);
        }
        if (item instanceof SQLAlterCharacter) {
            return this.apply((SQLAlterCharacter)item);
        }
        if (item instanceof MySqlAlterTableModifyColumn) {
            return this.apply((MySqlAlterTableModifyColumn)item);
        }
        if (item instanceof MySqlAlterTableOption) {
            return this.apply((MySqlAlterTableOption)item);
        }
        return super.alterApply(item);
    }
    
    public boolean apply(final SQLAlterTableAddIndex item) {
        final SQLName name = item.getIndexDefinition().getName();
        if (name != null) {
            final long nameHashCode = name.nameHashCode64();
            for (int i = 0; i < this.tableElementList.size(); ++i) {
                final SQLTableElement e = this.tableElementList.get(i);
                if (e instanceof MySqlTableIndex) {
                    final SQLName name2 = ((MySqlTableIndex)e).getName();
                    if (name2 != null && name2.nameHashCode64() == nameHashCode) {
                        return false;
                    }
                }
            }
        }
        if (item.isUnique()) {
            final MySqlUnique x = new MySqlUnique();
            item.cloneTo(x);
            x.setParent(this);
            this.tableElementList.add(x);
            return true;
        }
        if (item.isKey()) {
            final MySqlKey x2 = new MySqlKey();
            item.cloneTo(x2);
            x2.setParent(this);
            this.tableElementList.add(x2);
            return true;
        }
        final MySqlTableIndex x3 = new MySqlTableIndex();
        item.cloneTo(x3);
        x3.setParent(this);
        this.tableElementList.add(x3);
        return true;
    }
    
    public boolean apply(final MySqlAlterTableOption item) {
        this.addOption(item.getName(), (SQLExpr)item.getValue());
        return true;
    }
    
    public boolean apply(final SQLAlterCharacter item) {
        final SQLExpr charset = item.getCharacterSet();
        if (charset != null) {
            this.addOption("CHARACTER SET", charset);
        }
        final SQLExpr collate = item.getCollate();
        if (collate != null) {
            this.addOption("COLLATE", collate);
        }
        return true;
    }
    
    public boolean apply(final MySqlRenameTableStatement.Item item) {
        if (!SQLUtils.nameEquals(item.getName(), this.getName())) {
            return false;
        }
        this.setName(item.getTo().clone());
        return true;
    }
    
    public boolean apply(final MySqlAlterTableAlterColumn x) {
        final int columnIndex = this.columnIndexOf(x.getColumn());
        if (columnIndex == -1) {
            return false;
        }
        final SQLExpr defaultExpr = x.getDefaultExpr();
        final SQLColumnDefinition column = this.tableElementList.get(columnIndex);
        if (x.isDropDefault()) {
            column.setDefaultExpr(null);
        }
        else if (defaultExpr != null) {
            column.setDefaultExpr(defaultExpr);
        }
        return true;
    }
    
    public boolean apply(final MySqlAlterTableChangeColumn item) {
        final SQLName columnName = item.getColumnName();
        final int columnIndex = this.columnIndexOf(columnName);
        if (columnIndex == -1) {
            return false;
        }
        final int afterIndex = this.columnIndexOf(item.getAfterColumn());
        final int beforeIndex = this.columnIndexOf(item.getFirstColumn());
        int insertIndex = -1;
        if (beforeIndex != -1) {
            insertIndex = beforeIndex;
        }
        else if (afterIndex != -1) {
            insertIndex = afterIndex + 1;
        }
        else if (item.isFirst()) {
            insertIndex = 0;
        }
        final SQLColumnDefinition column = item.getNewColumnDefinition().clone();
        column.setParent(this);
        if (insertIndex == -1 || insertIndex == columnIndex) {
            this.tableElementList.set(columnIndex, column);
        }
        else if (insertIndex > columnIndex) {
            this.tableElementList.add(insertIndex, column);
            this.tableElementList.remove(columnIndex);
        }
        else {
            this.tableElementList.remove(columnIndex);
            this.tableElementList.add(insertIndex, column);
        }
        for (int i = 0; i < this.tableElementList.size(); ++i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof MySqlTableIndex) {
                ((MySqlTableIndex)e).applyColumnRename(columnName, column);
            }
            else if (e instanceof SQLUnique) {
                final SQLUnique unique = (SQLUnique)e;
                unique.applyColumnRename(columnName, column);
            }
        }
        return true;
    }
    
    public boolean apply(final MySqlAlterTableModifyColumn item) {
        final SQLColumnDefinition column = item.getNewColumnDefinition().clone();
        final SQLName columnName = column.getName();
        final int columnIndex = this.columnIndexOf(columnName);
        if (columnIndex == -1) {
            return false;
        }
        final int afterIndex = this.columnIndexOf(item.getAfterColumn());
        final int beforeIndex = this.columnIndexOf(item.getFirstColumn());
        int insertIndex = -1;
        if (beforeIndex != -1) {
            insertIndex = beforeIndex;
        }
        else if (afterIndex != -1) {
            insertIndex = afterIndex + 1;
        }
        if (item.isFirst()) {
            insertIndex = 0;
        }
        column.setParent(this);
        if (insertIndex == -1 || insertIndex == columnIndex) {
            this.tableElementList.set(columnIndex, column);
            return true;
        }
        if (insertIndex > columnIndex) {
            this.tableElementList.add(insertIndex, column);
            this.tableElementList.remove(columnIndex);
        }
        else {
            this.tableElementList.remove(columnIndex);
            this.tableElementList.add(insertIndex, column);
        }
        for (int i = 0; i < this.tableElementList.size(); ++i) {
            final SQLTableElement e = this.tableElementList.get(i);
            if (e instanceof MySqlTableIndex) {
                ((MySqlTableIndex)e).applyColumnRename(columnName, column);
            }
            else if (e instanceof SQLUnique) {
                final SQLUnique unique = (SQLUnique)e;
                unique.applyColumnRename(columnName, column);
            }
        }
        return true;
    }
    
    public void cloneTo(final MySqlCreateTableStatement x) {
        super.cloneTo(x);
        if (this.partitioning != null) {
            x.setPartitioning(this.partitioning.clone());
        }
        for (final SQLCommentHint hint : this.hints) {
            final SQLCommentHint h2 = hint.clone();
            h2.setParent(x);
            x.hints.add(h2);
        }
        for (final SQLCommentHint hint : this.optionHints) {
            final SQLCommentHint h2 = hint.clone();
            h2.setParent(x);
            x.optionHints.add(h2);
        }
        if (this.like != null) {
            x.setLike(this.like.clone());
        }
        if (this.tableGroup != null) {
            x.setTableGroup(this.tableGroup.clone());
        }
        if (this.dbPartitionBy != null) {
            x.setDbPartitionBy(this.dbPartitionBy.clone());
        }
        if (this.dbPartitions != null) {
            x.setDbPartitionBy(this.dbPartitions.clone());
        }
        if (this.tablePartitionBy != null) {
            x.setTablePartitionBy(this.tablePartitionBy.clone());
        }
        if (this.tablePartitions != null) {
            x.setTablePartitions(this.tablePartitions.clone());
        }
        if (this.exPartition != null) {
            x.setExPartition(this.exPartition.clone());
        }
        if (this.archiveBy != null) {
            x.setArchiveBy(this.archiveBy.clone());
        }
        if (this.distributeByType != null) {
            x.setDistributeByType(this.distributeByType.clone());
        }
        if (this.distributeByType != null) {
            for (final SQLName sqlName : this.distributeBy) {
                x.getDistributeBy().add(sqlName.clone());
            }
        }
    }
    
    @Override
    public MySqlCreateTableStatement clone() {
        final MySqlCreateTableStatement x = new MySqlCreateTableStatement();
        this.cloneTo(x);
        return x;
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.dbPartitionBy;
    }
    
    public void setDbPartitionBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dbPartitionBy = x;
    }
    
    public SQLExpr getTablePartitionBy() {
        return this.tablePartitionBy;
    }
    
    public void setTablePartitionBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.tablePartitionBy = x;
    }
    
    public SQLName getDistributeByType() {
        return this.distributeByType;
    }
    
    public void setDistributeByType(final SQLName distributeByType) {
        this.distributeByType = distributeByType;
    }
    
    public List<SQLName> getDistributeBy() {
        return this.distributeBy;
    }
    
    public SQLExpr getTbpartitions() {
        return this.tablePartitions;
    }
    
    public SQLExpr getTablePartitions() {
        return this.tablePartitions;
    }
    
    public void setTablePartitions(final SQLExpr x) {
        this.tablePartitions = x;
    }
    
    public SQLExpr getDbpartitions() {
        return this.dbPartitions;
    }
    
    public void setDbPartitions(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dbPartitions = x;
    }
    
    public MySqlExtPartition getExtPartition() {
        return this.exPartition;
    }
    
    public void setExPartition(final MySqlExtPartition x) {
        if (x != null) {
            x.setParent(this);
        }
        this.exPartition = x;
    }
    
    public SQLExpr getDbPartitions() {
        return this.dbPartitions;
    }
    
    public SQLName getStoredBy() {
        return this.storedBy;
    }
    
    public void setStoredBy(final SQLName storedBy) {
        this.storedBy = storedBy;
    }
    
    public Map<String, SQLName> getWith() {
        return this.with;
    }
    
    public boolean isBroadCast() {
        return this.isBroadCast;
    }
    
    public void setBroadCast(final boolean broadCast) {
        this.isBroadCast = broadCast;
    }
    
    public SQLName getArchiveBy() {
        return this.archiveBy;
    }
    
    public void setArchiveBy(final SQLName archiveBy) {
        this.archiveBy = archiveBy;
    }
    
    public Boolean getWithData() {
        return this.withData;
    }
    
    public void setWithData(final Boolean withData) {
        this.withData = withData;
    }
    
    @Override
    public SQLExpr getEngine() {
        for (final SQLAssignItem option : this.tableOptions) {
            final SQLExpr target = option.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).getName().equalsIgnoreCase("ENGINE")) {
                return option.getValue();
            }
        }
        return null;
    }
    
    @Override
    public void setEngine(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.addOption("ENGINE", x);
    }
    
    public static class TableSpaceOption extends MySqlExprImpl
    {
        private SQLName name;
        private SQLExpr storage;
        
        public SQLName getName() {
            return this.name;
        }
        
        public void setName(final SQLName name) {
            if (name != null) {
                name.setParent(this);
            }
            this.name = name;
        }
        
        public SQLExpr getStorage() {
            return this.storage;
        }
        
        public void setStorage(final SQLExpr storage) {
            if (storage != null) {
                storage.setParent(this);
            }
            this.storage = storage;
        }
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.getName());
                this.acceptChild(visitor, this.getStorage());
            }
            visitor.endVisit(this);
        }
        
        @Override
        public TableSpaceOption clone() {
            final TableSpaceOption x = new TableSpaceOption();
            if (this.name != null) {
                x.setName(this.name.clone());
            }
            if (this.storage != null) {
                x.setStorage(this.storage.clone());
            }
            return x;
        }
        
        @Override
        public List<SQLObject> getChildren() {
            return null;
        }
    }
}
