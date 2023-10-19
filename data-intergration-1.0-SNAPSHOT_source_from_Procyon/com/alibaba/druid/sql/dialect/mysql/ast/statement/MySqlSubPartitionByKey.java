// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLSubPartitionBy;

public class MySqlSubPartitionByKey extends SQLSubPartitionBy implements MySqlObject
{
    private int algorithm;
    private List<SQLName> columns;
    
    public MySqlSubPartitionByKey() {
        this.algorithm = 2;
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
            return;
        }
        throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.subPartitionsCount);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public void cloneTo(final MySqlSubPartitionByKey x) {
        super.cloneTo(x);
        for (final SQLName column : this.columns) {
            final SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        x.algorithm = this.algorithm;
    }
    
    @Override
    public MySqlSubPartitionByKey clone() {
        final MySqlSubPartitionByKey x = new MySqlSubPartitionByKey();
        this.cloneTo(x);
        return x;
    }
    
    public int getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final int algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        for (final SQLName column : this.columns) {
            if (column.nameHashCode64() == columnNameHashCode64) {
                return true;
            }
        }
        return false;
    }
}
