// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddIndex extends SQLObjectImpl implements SQLAlterTableItem, SQLIndex
{
    private SQLIndexDefinition indexDefinition;
    
    public SQLAlterTableAddIndex() {
        (this.indexDefinition = new SQLIndexDefinition()).setParent(this);
    }
    
    public SQLIndexDefinition getIndexDefinition() {
        return this.indexDefinition;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.indexDefinition.getName() != null) {
                this.indexDefinition.getName().accept(visitor);
            }
            for (int i = 0; i < this.getColumns().size(); ++i) {
                final SQLSelectOrderByItem item = this.getColumns().get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
            for (int i = 0; i < this.getCovering().size(); ++i) {
                final SQLName item2 = this.getCovering().get(i);
                if (item2 != null) {
                    item2.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isUnique() {
        return this.indexDefinition.getType() != null && this.indexDefinition.getType().equalsIgnoreCase("UNIQUE");
    }
    
    public void setUnique(final boolean unique) {
        this.indexDefinition.setType("UNIQUE");
    }
    
    public List<SQLSelectOrderByItem> getItems() {
        return this.indexDefinition.getColumns();
    }
    
    public void addItem(final SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.indexDefinition.getColumns().add(item);
    }
    
    public SQLName getName() {
        return this.indexDefinition.getName();
    }
    
    public void setName(final SQLName name) {
        this.indexDefinition.setName(name);
    }
    
    public String getType() {
        return this.indexDefinition.getType();
    }
    
    public void setType(final String type) {
        this.indexDefinition.setType(type);
    }
    
    public String getUsing() {
        if (this.indexDefinition.hasOptions()) {
            return this.indexDefinition.getOptions().getIndexType();
        }
        return null;
    }
    
    public void setUsing(final String using) {
        this.indexDefinition.getOptions().setIndexType(using);
    }
    
    public boolean isKey() {
        return this.indexDefinition.isKey();
    }
    
    public void setKey(final boolean key) {
        this.indexDefinition.setKey(key);
    }
    
    public void cloneTo(final MySqlTableIndex x) {
        this.indexDefinition.cloneTo(x.getIndexDefinition());
    }
    
    public void cloneTo(final MySqlKey x) {
        this.indexDefinition.cloneTo(x.getIndexDefinition());
    }
    
    public SQLExpr getComment() {
        if (this.indexDefinition.hasOptions()) {
            return this.indexDefinition.getOptions().getComment();
        }
        return null;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.indexDefinition.getOptions().setComment(comment);
    }
    
    public SQLExpr getKeyBlockSize() {
        if (this.indexDefinition.hasOptions()) {
            return this.indexDefinition.getOptions().getKeyBlockSize();
        }
        return null;
    }
    
    public void setKeyBlockSize(final SQLExpr keyBlockSize) {
        this.indexDefinition.getOptions().setKeyBlockSize(keyBlockSize);
    }
    
    public String getParserName() {
        if (this.indexDefinition.hasOptions()) {
            return this.indexDefinition.getOptions().getParserName();
        }
        return null;
    }
    
    public void setParserName(final String parserName) {
        this.indexDefinition.getOptions().setParserName(parserName);
    }
    
    public boolean isHashMapType() {
        return this.indexDefinition.isHashMapType();
    }
    
    public void setHashMapType(final boolean hashMapType) {
        this.indexDefinition.setHashMapType(hashMapType);
    }
    
    protected SQLExpr getOption(final long hash64) {
        return this.indexDefinition.getOption(hash64);
    }
    
    public String getDistanceMeasure() {
        return this.indexDefinition.getDistanceMeasure();
    }
    
    public String getAlgorithm() {
        return this.indexDefinition.getAlgorithm();
    }
    
    public void addOption(final String name, final SQLExpr value) {
        this.indexDefinition.addOption(name, value);
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.indexDefinition.getCompatibleOptions();
    }
    
    public boolean isGlobal() {
        return this.indexDefinition.isGlobal();
    }
    
    public void setGlobal(final boolean global) {
        this.indexDefinition.setGlobal(global);
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.indexDefinition.getDbPartitionBy();
    }
    
    public void setDbPartitionBy(final SQLExpr x) {
        this.indexDefinition.setDbPartitionBy(x);
    }
    
    public SQLExpr getTablePartitionBy() {
        return this.indexDefinition.getTbPartitionBy();
    }
    
    public void setTablePartitionBy(final SQLExpr x) {
        this.indexDefinition.setTbPartitionBy(x);
    }
    
    public SQLExpr getTablePartitions() {
        return this.indexDefinition.getTbPartitions();
    }
    
    public void setTablePartitions(final SQLExpr x) {
        this.indexDefinition.setTbPartitions(x);
    }
    
    @Override
    public List<SQLName> getCovering() {
        return this.indexDefinition.getCovering();
    }
    
    @Override
    public List<SQLSelectOrderByItem> getColumns() {
        return this.indexDefinition.getColumns();
    }
}
