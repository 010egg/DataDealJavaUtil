// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLOver;

public class OracleAnalytic extends SQLOver implements SQLReplaceable, OracleExpr
{
    private OracleAnalyticWindowing windowing;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitionBy);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.windowing);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.partitionBy);
        if (this.orderBy != null) {
            children.add(this.orderBy);
        }
        if (this.windowing != null) {
            children.add(this.windowing);
        }
        return children;
    }
    
    public OracleAnalyticWindowing getWindowing() {
        return this.windowing;
    }
    
    @Override
    public OracleAnalytic clone() {
        final OracleAnalytic x = new OracleAnalytic();
        this.cloneTo(x);
        if (this.windowing != null) {
            x.setWindowing(this.windowing.clone());
        }
        return x;
    }
    
    public void setWindowing(final OracleAnalyticWindowing x) {
        if (x != null) {
            x.setParent(this);
        }
        this.windowing = x;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return null;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.partitionBy.size(); ++i) {
            if (this.partitionBy.get(i) == expr) {
                this.partitionBy.set(i, target);
                return true;
            }
        }
        return false;
    }
}
