// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableExchangePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLExpr> partitions;
    private SQLExprTableSource table;
    private Boolean validation;
    
    public SQLAlterTableExchangePartition() {
        this.partitions = new ArrayList<SQLExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.table);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLExpr> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitions.add(x);
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName x) {
        this.setTable(new SQLExprTableSource(x));
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    public void setValidation(final boolean validation) {
        this.validation = validation;
    }
    
    public Boolean getValidation() {
        return this.validation;
    }
}
