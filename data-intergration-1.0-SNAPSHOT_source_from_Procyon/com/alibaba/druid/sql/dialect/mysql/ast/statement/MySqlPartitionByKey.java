// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLPartitionBy;

public class MySqlPartitionByKey extends SQLPartitionBy implements MySqlObject
{
    private int algorithm;
    
    public MySqlPartitionByKey() {
        this.algorithm = 2;
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
            this.acceptChild(visitor, this.partitionsCount);
            this.acceptChild(visitor, this.getPartitions());
            this.acceptChild(visitor, this.subPartitionBy);
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final MySqlPartitionByKey x) {
        super.cloneTo(x);
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        x.algorithm = this.algorithm;
    }
    
    @Override
    public MySqlPartitionByKey clone() {
        final MySqlPartitionByKey x = new MySqlPartitionByKey();
        this.cloneTo(x);
        return x;
    }
    
    public int getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final int algorithm) {
        this.algorithm = algorithm;
    }
}
