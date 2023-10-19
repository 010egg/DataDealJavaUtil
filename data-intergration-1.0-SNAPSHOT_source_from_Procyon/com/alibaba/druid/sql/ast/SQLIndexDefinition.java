// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

public class SQLIndexDefinition extends SQLObjectImpl implements SQLIndex
{
    private boolean hasConstraint;
    private SQLName symbol;
    private boolean global;
    private boolean local;
    private String type;
    private boolean hashMapType;
    private boolean hashType;
    private boolean index;
    private boolean key;
    private SQLName name;
    private SQLTableSource table;
    private List<SQLSelectOrderByItem> columns;
    private SQLIndexOptions options;
    private SQLExpr dbPartitionBy;
    private SQLExpr tbPartitionBy;
    private SQLExpr tbPartitions;
    private List<SQLName> covering;
    private SQLName analyzerName;
    private SQLName indexAnalyzerName;
    private SQLName queryAnalyzerName;
    private SQLName withDicName;
    private List<SQLAssignItem> compatibleOptions;
    
    public SQLIndexDefinition() {
        this.columns = new ArrayList<SQLSelectOrderByItem>();
        this.covering = new ArrayList<SQLName>();
        this.compatibleOptions = new ArrayList<SQLAssignItem>();
    }
    
    public boolean hasConstraint() {
        return this.hasConstraint;
    }
    
    public void setHasConstraint(final boolean hasConstraint) {
        this.hasConstraint = hasConstraint;
    }
    
    public SQLName getSymbol() {
        return this.symbol;
    }
    
    public void setSymbol(final SQLName symbol) {
        if (symbol != null) {
            if (this.getParent() != null) {
                symbol.setParent(this.getParent());
            }
            else {
                symbol.setParent(this);
            }
        }
        this.symbol = symbol;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public boolean isHashMapType() {
        return this.hashMapType;
    }
    
    public void setHashMapType(final boolean hashMapType) {
        this.hashMapType = hashMapType;
    }
    
    public boolean isHashType() {
        return this.hashType;
    }
    
    public void setHashType(final boolean hashType) {
        this.hashType = hashType;
    }
    
    public boolean isIndex() {
        return this.index;
    }
    
    public void setIndex(final boolean index) {
        this.index = index;
    }
    
    public boolean isKey() {
        return this.key;
    }
    
    public void setKey(final boolean key) {
        this.key = key;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            if (this.getParent() != null) {
                name.setParent(this.getParent());
            }
            else {
                name.setParent(this);
            }
        }
        this.name = name;
    }
    
    public SQLTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLTableSource table) {
        if (table != null) {
            if (this.getParent() != null) {
                table.setParent(this.getParent());
            }
            else {
                table.setParent(this);
            }
        }
        this.table = table;
    }
    
    @Override
    public List<SQLSelectOrderByItem> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final List<SQLSelectOrderByItem> columns) {
        this.columns = columns;
    }
    
    public boolean hasOptions() {
        return this.options != null;
    }
    
    public SQLIndexOptions getOptions() {
        if (null == this.options) {
            (this.options = new SQLIndexOptions()).setParent(this);
        }
        return this.options;
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.dbPartitionBy;
    }
    
    public void setDbPartitionBy(final SQLExpr dbPartitionBy) {
        if (dbPartitionBy != null) {
            if (this.getParent() != null) {
                dbPartitionBy.setParent(this.getParent());
            }
            else {
                dbPartitionBy.setParent(this);
            }
        }
        this.dbPartitionBy = dbPartitionBy;
    }
    
    public SQLExpr getTbPartitionBy() {
        return this.tbPartitionBy;
    }
    
    public void setTbPartitionBy(final SQLExpr tbPartitionBy) {
        if (tbPartitionBy != null) {
            if (this.getParent() != null) {
                tbPartitionBy.setParent(this.getParent());
            }
            else {
                tbPartitionBy.setParent(this);
            }
        }
        this.tbPartitionBy = tbPartitionBy;
    }
    
    public SQLExpr getTbPartitions() {
        return this.tbPartitions;
    }
    
    public void setTbPartitions(final SQLExpr tbPartitions) {
        if (tbPartitions != null) {
            if (this.getParent() != null) {
                tbPartitions.setParent(this.getParent());
            }
            else {
                tbPartitions.setParent(this);
            }
        }
        this.tbPartitions = tbPartitions;
    }
    
    @Override
    public List<SQLName> getCovering() {
        return this.covering;
    }
    
    public void setCovering(final List<SQLName> covering) {
        this.covering = covering;
    }
    
    public SQLName getAnalyzerName() {
        return this.analyzerName;
    }
    
    public void setAnalyzerName(final SQLName analyzerName) {
        if (analyzerName != null) {
            if (this.getParent() != null) {
                analyzerName.setParent(this.getParent());
            }
            else {
                analyzerName.setParent(this);
            }
        }
        this.analyzerName = analyzerName;
    }
    
    public SQLName getIndexAnalyzerName() {
        return this.indexAnalyzerName;
    }
    
    public void setIndexAnalyzerName(final SQLName indexAnalyzerName) {
        if (indexAnalyzerName != null) {
            if (this.getParent() != null) {
                indexAnalyzerName.setParent(this.getParent());
            }
            else {
                indexAnalyzerName.setParent(this);
            }
        }
        this.indexAnalyzerName = indexAnalyzerName;
    }
    
    public SQLName getQueryAnalyzerName() {
        return this.queryAnalyzerName;
    }
    
    public void setQueryAnalyzerName(final SQLName queryAnalyzerName) {
        if (queryAnalyzerName != null) {
            if (this.getParent() != null) {
                queryAnalyzerName.setParent(this.getParent());
            }
            else {
                queryAnalyzerName.setParent(this);
            }
        }
        this.queryAnalyzerName = queryAnalyzerName;
    }
    
    public SQLName getWithDicName() {
        return this.withDicName;
    }
    
    public void setWithDicName(final SQLName withDicName) {
        if (withDicName != null) {
            if (this.getParent() != null) {
                withDicName.setParent(this.getParent());
            }
            else {
                withDicName.setParent(this);
            }
        }
        this.withDicName = withDicName;
    }
    
    public List<SQLAssignItem> getCompatibleOptions() {
        return this.compatibleOptions;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.name != null) {
                this.name.accept(visitor);
            }
            for (final SQLSelectOrderByItem item : this.columns) {
                if (item != null) {
                    item.accept(visitor);
                }
            }
            for (final SQLName item2 : this.covering) {
                if (item2 != null) {
                    item2.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final SQLIndexDefinition definition) {
        SQLObject parent;
        if (definition.getParent() != null) {
            parent = definition.getParent();
        }
        else {
            parent = definition;
        }
        definition.hasConstraint = this.hasConstraint;
        if (this.symbol != null) {
            (definition.symbol = this.symbol.clone()).setParent(parent);
        }
        definition.global = this.global;
        definition.local = this.local;
        definition.type = this.type;
        definition.hashMapType = this.hashMapType;
        definition.index = this.index;
        definition.key = this.key;
        if (this.name != null) {
            (definition.name = this.name.clone()).setParent(parent);
        }
        if (this.table != null) {
            (definition.table = this.table.clone()).setParent(parent);
        }
        for (final SQLSelectOrderByItem item : this.columns) {
            final SQLSelectOrderByItem item2 = item.clone();
            item2.setParent(parent);
            definition.columns.add(item2);
        }
        if (this.options != null) {
            this.options.cloneTo(definition.getOptions());
        }
        if (this.dbPartitionBy != null) {
            (definition.dbPartitionBy = this.dbPartitionBy.clone()).setParent(parent);
        }
        if (this.tbPartitionBy != null) {
            (definition.tbPartitionBy = this.tbPartitionBy.clone()).setParent(parent);
        }
        if (this.tbPartitions != null) {
            (definition.tbPartitions = this.tbPartitions.clone()).setParent(parent);
        }
        for (final SQLName name : this.covering) {
            final SQLName name2 = name.clone();
            name2.setParent(parent);
            definition.covering.add(name2);
        }
        if (this.analyzerName != null) {
            (definition.analyzerName = this.analyzerName.clone()).setParent(parent);
        }
        if (this.indexAnalyzerName != null) {
            (definition.indexAnalyzerName = this.indexAnalyzerName.clone()).setParent(parent);
        }
        if (this.withDicName != null) {
            (definition.withDicName = this.withDicName.clone()).setParent(parent);
        }
        if (this.queryAnalyzerName != null) {
            (definition.queryAnalyzerName = this.queryAnalyzerName.clone()).setParent(parent);
        }
        for (final SQLAssignItem item3 : this.compatibleOptions) {
            final SQLAssignItem item4 = item3.clone();
            item4.setParent(parent);
            definition.compatibleOptions.add(item4);
        }
    }
    
    public void addOption(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        if (this.getParent() != null) {
            assignItem.setParent(this.getParent());
        }
        else {
            assignItem.setParent(this);
        }
        this.getCompatibleOptions().add(assignItem);
    }
    
    public SQLExpr getOption(final String name) {
        if (name == null) {
            return null;
        }
        return this.getOption(FnvHash.hashCode64(name));
    }
    
    public SQLExpr getOption(final long hash64) {
        for (final SQLAssignItem item : this.compatibleOptions) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                return item.getValue();
            }
        }
        if (null == this.options) {
            return null;
        }
        if (hash64 == FnvHash.Constants.KEY_BLOCK_SIZE) {
            return this.options.getKeyBlockSize();
        }
        if (hash64 == FnvHash.Constants.ALGORITHM) {
            if (this.options.getAlgorithm() != null) {
                return new SQLIdentifierExpr(this.options.getAlgorithm());
            }
            return null;
        }
        else {
            if (hash64 != FnvHash.hashCode64("LOCK")) {
                for (final SQLAssignItem item : this.options.getOtherOptions()) {
                    final SQLExpr target = item.getTarget();
                    if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                        return item.getValue();
                    }
                }
                return null;
            }
            if (this.options.getLock() != null) {
                return new SQLIdentifierExpr(this.options.getLock());
            }
            return null;
        }
    }
    
    public String getDistanceMeasure() {
        final SQLExpr expr = this.getOption(FnvHash.Constants.DISTANCEMEASURE);
        if (expr == null) {
            return null;
        }
        return expr.toString();
    }
    
    public String getAlgorithm() {
        if (this.options != null && this.options.getAlgorithm() != null) {
            return this.options.getAlgorithm();
        }
        final SQLExpr expr = this.getOption(FnvHash.Constants.ALGORITHM);
        if (expr == null) {
            return null;
        }
        return expr.toString();
    }
}
