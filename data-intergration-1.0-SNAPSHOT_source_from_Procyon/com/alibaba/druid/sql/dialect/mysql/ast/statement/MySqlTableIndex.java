// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLConstraintImpl;

public class MySqlTableIndex extends SQLConstraintImpl implements SQLTableElement, SQLIndex, MySqlObject
{
    private SQLIndexDefinition indexDefinition;
    
    public MySqlTableIndex() {
        (this.indexDefinition = new SQLIndexDefinition()).setParent(this);
    }
    
    public SQLIndexDefinition getIndexDefinition() {
        return this.indexDefinition;
    }
    
    @Override
    public SQLName getName() {
        return this.indexDefinition.getName();
    }
    
    public String getIndexType() {
        return this.indexDefinition.getType();
    }
    
    public void setIndexType(final String indexType) {
        this.indexDefinition.setType(indexType);
    }
    
    @Override
    public void setName(final SQLName name) {
        this.indexDefinition.setName(name);
    }
    
    @Override
    public List<SQLSelectOrderByItem> getColumns() {
        return this.indexDefinition.getColumns();
    }
    
    public void addColumn(final SQLSelectOrderByItem column) {
        if (column != null) {
            column.setParent(this);
        }
        this.indexDefinition.getColumns().add(column);
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        this.accept0((MySqlASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.indexDefinition.getName() != null) {
                this.indexDefinition.getName().accept(visitor);
            }
            for (int i = 0; i < this.indexDefinition.getColumns().size(); ++i) {
                final SQLSelectOrderByItem item = this.indexDefinition.getColumns().get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
            for (int i = 0; i < this.indexDefinition.getCovering().size(); ++i) {
                final SQLName item2 = this.indexDefinition.getCovering().get(i);
                if (item2 != null) {
                    item2.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlTableIndex clone() {
        final MySqlTableIndex x = new MySqlTableIndex();
        this.indexDefinition.cloneTo(x.indexDefinition);
        return x;
    }
    
    public boolean applyColumnRename(final SQLName columnName, final SQLColumnDefinition to) {
        for (final SQLSelectOrderByItem orderByItem : this.getColumns()) {
            final SQLExpr expr = orderByItem.getExpr();
            if (expr instanceof SQLName && SQLUtils.nameEquals((SQLName)expr, columnName)) {
                orderByItem.setExpr(to.getName().clone());
                return true;
            }
            if (expr instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)expr).getMethodName(), columnName.getSimpleName()) && 1 == ((SQLMethodInvokeExpr)expr).getArguments().size() && ((SQLMethodInvokeExpr)expr).getArguments().get(0) instanceof SQLIntegerExpr) {
                if (to.getDataType().hasKeyLength() && 1 == to.getDataType().getArguments().size() && to.getDataType().getArguments().get(0) instanceof SQLIntegerExpr) {
                    final int newKeyLength = to.getDataType().getArguments().get(0).getNumber().intValue();
                    final int oldKeyLength = ((SQLMethodInvokeExpr)expr).getArguments().get(0).getNumber().intValue();
                    if (newKeyLength > oldKeyLength) {
                        ((SQLMethodInvokeExpr)expr).setMethodName(to.getName().getSimpleName());
                        return true;
                    }
                }
                orderByItem.setExpr(to.getName().clone());
                return true;
            }
        }
        return false;
    }
    
    public boolean applyDropColumn(final SQLName columnName) {
        for (int i = this.indexDefinition.getColumns().size() - 1; i >= 0; --i) {
            final SQLExpr expr = this.indexDefinition.getColumns().get(i).getExpr();
            if (expr instanceof SQLName && SQLUtils.nameEquals((SQLName)expr, columnName)) {
                this.indexDefinition.getColumns().remove(i);
                return true;
            }
            if (expr instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)expr).getMethodName(), columnName.getSimpleName())) {
                this.indexDefinition.getColumns().remove(i);
                return true;
            }
        }
        return false;
    }
    
    public void addOption(final String name, final SQLExpr value) {
        this.indexDefinition.addOption(name, value);
    }
    
    public SQLExpr getOption(final String name) {
        return this.indexDefinition.getOption(name);
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
    
    public List<SQLAssignItem> getOptions() {
        return this.indexDefinition.getCompatibleOptions();
    }
    
    @Override
    public SQLExpr getComment() {
        return this.indexDefinition.getOptions().getComment();
    }
    
    @Override
    public void setComment(final SQLExpr x) {
        this.indexDefinition.getOptions().setComment(x);
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.indexDefinition.getDbPartitionBy();
    }
    
    public void setDbPartitionBy(final SQLExpr x) {
        this.indexDefinition.setDbPartitionBy(x);
    }
    
    public SQLExpr getTablePartitions() {
        return this.indexDefinition.getTbPartitions();
    }
    
    public void setTablePartitions(final SQLExpr x) {
        this.indexDefinition.setTbPartitions(x);
    }
    
    public SQLExpr getTablePartitionBy() {
        return this.indexDefinition.getTbPartitionBy();
    }
    
    public void setTablePartitionBy(final SQLExpr x) {
        this.indexDefinition.setTbPartitionBy(x);
    }
    
    public void setCovering(final List<SQLName> covering) {
        this.indexDefinition.setCovering(covering);
    }
    
    public boolean isGlobal() {
        return this.indexDefinition.isGlobal();
    }
    
    public void setGlobal(final boolean global) {
        this.indexDefinition.setGlobal(global);
    }
    
    public boolean isLocal() {
        return this.indexDefinition.isLocal();
    }
    
    public void setLocal(final boolean local) {
        this.indexDefinition.setLocal(local);
    }
    
    @Override
    public List<SQLName> getCovering() {
        return this.indexDefinition.getCovering();
    }
    
    public SQLName getIndexAnalyzerName() {
        return this.indexDefinition.getIndexAnalyzerName();
    }
    
    public void setIndexAnalyzerName(final SQLName indexAnalyzerName) {
        this.indexDefinition.setIndexAnalyzerName(indexAnalyzerName);
    }
    
    public SQLName getQueryAnalyzerName() {
        return this.indexDefinition.getQueryAnalyzerName();
    }
    
    public void setQueryAnalyzerName(final SQLName queryAnalyzerName) {
        this.indexDefinition.setQueryAnalyzerName(queryAnalyzerName);
    }
    
    public SQLName getWithDicName() {
        return this.indexDefinition.getWithDicName();
    }
    
    public void setWithDicName(final SQLName withDicName) {
        this.indexDefinition.setWithDicName(withDicName);
    }
    
    public SQLName getAnalyzerName() {
        return this.indexDefinition.getAnalyzerName();
    }
    
    public void setAnalyzerName(final SQLName analyzerName) {
        this.indexDefinition.setAnalyzerName(analyzerName);
    }
}
