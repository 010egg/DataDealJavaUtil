// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class SampleClause extends OracleSQLObjectImpl implements SQLReplaceable
{
    private boolean block;
    private final List<SQLExpr> percent;
    private SQLExpr seedValue;
    
    public SampleClause() {
        this.block = false;
        this.percent = new ArrayList<SQLExpr>();
    }
    
    public boolean isBlock() {
        return this.block;
    }
    
    public void setBlock(final boolean block) {
        this.block = block;
    }
    
    public List<SQLExpr> getPercent() {
        return this.percent;
    }
    
    public void addPercent(final SQLExpr x) {
        if (x == null) {
            return;
        }
        x.setParent(this);
        this.percent.add(x);
    }
    
    public SQLExpr getSeedValue() {
        return this.seedValue;
    }
    
    public void setSeedValue(final SQLExpr seedValue) {
        if (seedValue != null) {
            seedValue.setParent(this);
        }
        this.seedValue = seedValue;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.seedValue);
            this.acceptChild(visitor, this.percent);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SampleClause clone() {
        final SampleClause x = new SampleClause();
        x.block = this.block;
        for (final SQLExpr item : this.percent) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.percent.add(item2);
        }
        if (this.seedValue != null) {
            x.setSeedValue(this.seedValue.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = this.percent.size() - 1; i >= 0; --i) {
            if (this.percent.get(i) == expr) {
                this.percent.set(i, target);
                return true;
            }
        }
        if (expr == this.seedValue) {
            this.setSeedValue(target);
            return true;
        }
        return false;
    }
}
