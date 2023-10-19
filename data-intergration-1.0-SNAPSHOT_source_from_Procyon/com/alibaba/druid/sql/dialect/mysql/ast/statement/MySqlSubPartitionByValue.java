// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLSubPartitionBy;

public class MySqlSubPartitionByValue extends SQLSubPartitionBy implements MySqlObject
{
    private List<SQLExpr> columns;
    
    public MySqlSubPartitionByValue() {
        this.columns = new ArrayList<SQLExpr>();
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
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLExpr column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public void cloneTo(final MySqlSubPartitionByValue x) {
        super.cloneTo(x);
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
    }
    
    @Override
    public MySqlSubPartitionByValue clone() {
        final MySqlSubPartitionByValue x = new MySqlSubPartitionByValue();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        for (final SQLExpr column : this.columns) {
            if (column instanceof SQLName) {
                if (((SQLName)column).nameHashCode64() == columnNameHashCode64) {
                    return true;
                }
                if (!(column instanceof SQLMethodInvokeExpr)) {
                    continue;
                }
                final List<SQLExpr> arguments = ((SQLMethodInvokeExpr)column).getArguments();
                for (final SQLExpr argument : arguments) {
                    if (((SQLName)argument).nameHashCode64() == columnNameHashCode64) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
