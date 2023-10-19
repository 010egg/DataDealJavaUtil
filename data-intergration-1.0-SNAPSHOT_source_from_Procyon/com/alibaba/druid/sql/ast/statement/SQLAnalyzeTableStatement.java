// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAnalyzeTableStatement extends SQLStatementImpl
{
    protected final List<SQLExprTableSource> tableSources;
    private SQLPartitionRef partition;
    private boolean forColums;
    private List<SQLName> columns;
    private boolean cacheMetadata;
    private boolean noscan;
    private boolean computeStatistics;
    private SQLIdentifierExpr adbSchema;
    private List<SQLIdentifierExpr> adbColumns;
    private List<SQLIdentifierExpr> adbColumnsGroup;
    private SQLExpr adbWhere;
    
    public SQLAnalyzeTableStatement() {
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.forColums = false;
        this.columns = new ArrayList<SQLName>();
        this.adbColumns = new ArrayList<SQLIdentifierExpr>();
        this.adbColumnsGroup = new ArrayList<SQLIdentifierExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSources);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.adbSchema);
            this.acceptChild(visitor, this.adbColumns);
            this.acceptChild(visitor, this.adbColumnsGroup);
            this.acceptChild(visitor, this.adbWhere);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLExprTableSource> getTables() {
        return this.tableSources;
    }
    
    public SQLExprTableSource getTable() {
        if (this.tableSources.size() == 0) {
            return null;
        }
        if (this.tableSources.size() == 1) {
            return this.tableSources.get(0);
        }
        throw new UnsupportedOperationException();
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void setTable(final SQLExprTableSource table) {
        if (table != null) {
            table.setParent(this);
        }
        if (this.tableSources.size() == 0) {
            if (table == null) {
                return;
            }
            this.tableSources.add(table);
        }
        else {
            if (this.tableSources.size() == 1) {
                if (table == null) {
                    this.tableSources.remove(0);
                }
                else {
                    this.tableSources.set(0, table);
                }
                return;
            }
            throw new UnsupportedOperationException();
        }
    }
    
    public SQLIdentifierExpr getAdbSchema() {
        return this.adbSchema;
    }
    
    public void setAdbSchema(final SQLIdentifierExpr adbSchema) {
        this.adbSchema = adbSchema;
    }
    
    public List<SQLIdentifierExpr> getAdbColumns() {
        return this.adbColumns;
    }
    
    public void setAdbColumns(final List<SQLIdentifierExpr> adbColumns) {
        this.adbColumns = adbColumns;
    }
    
    public List<SQLIdentifierExpr> getAdbColumnsGroup() {
        return this.adbColumnsGroup;
    }
    
    public void setAdbColumnsGroup(final List<SQLIdentifierExpr> adbColumnsGroup) {
        this.adbColumnsGroup = adbColumnsGroup;
    }
    
    public SQLExpr getAdbWhere() {
        return this.adbWhere;
    }
    
    public void setAdbWhere(final SQLExpr adbWhere) {
        this.adbWhere = adbWhere;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public SQLPartitionRef getPartition() {
        return this.partition;
    }
    
    public void setPartition(final SQLPartitionRef x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partition = x;
    }
    
    public boolean isForColums() {
        return this.forColums;
    }
    
    public void setForColums(final boolean forColums) {
        this.forColums = forColums;
    }
    
    public boolean isCacheMetadata() {
        return this.cacheMetadata;
    }
    
    public void setCacheMetadata(final boolean cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
    }
    
    public boolean isNoscan() {
        return this.noscan;
    }
    
    public void setNoscan(final boolean noscan) {
        this.noscan = noscan;
    }
    
    public boolean isComputeStatistics() {
        return this.computeStatistics;
    }
    
    public void setComputeStatistics(final boolean computeStatistics) {
        this.computeStatistics = computeStatistics;
    }
}
