// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLSubPartitionByRange extends SQLSubPartitionBy
{
    private List<SQLExpr> columns;
    
    public SQLSubPartitionByRange() {
        this.columns = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.columns);
            this.acceptChild(v, this.subPartitionsCount);
        }
        v.endVisit(this);
    }
    
    @Override
    public SQLSubPartitionByRange clone() {
        final SQLSubPartitionByRange x = new SQLSubPartitionByRange();
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    @Override
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        for (final SQLExpr column : this.columns) {
            if (column instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)column).nameHashCode64() == columnNameHashCode64) {
                return true;
            }
        }
        return false;
    }
}
