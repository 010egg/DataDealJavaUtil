// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLTableSampling extends SQLObjectImpl implements SQLReplaceable
{
    private SQLExpr bucket;
    private SQLExpr outOf;
    private SQLExpr on;
    private SQLExpr percent;
    private SQLExpr rows;
    private SQLExpr byteLength;
    private boolean bernoulli;
    private boolean system;
    
    @Override
    public SQLTableSampling clone() {
        final SQLTableSampling x = new SQLTableSampling();
        if (this.bucket != null) {
            x.setBucket(this.bucket.clone());
        }
        if (this.outOf != null) {
            x.setOutOf(this.outOf.clone());
        }
        if (this.on != null) {
            x.setOn(this.on.clone());
        }
        if (this.percent != null) {
            x.setPercent(this.percent.clone());
        }
        if (this.rows != null) {
            x.setRows(this.rows.clone());
        }
        if (this.byteLength != null) {
            x.setByteLength(this.byteLength.clone());
        }
        x.bernoulli = this.bernoulli;
        x.system = this.system;
        return x;
    }
    
    public SQLExpr getBucket() {
        return this.bucket;
    }
    
    public void setBucket(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.bucket = x;
    }
    
    public SQLExpr getOutOf() {
        return this.outOf;
    }
    
    public void setOutOf(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.outOf = x;
    }
    
    public SQLExpr getOn() {
        return this.on;
    }
    
    public void setOn(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.on = x;
    }
    
    public SQLExpr getPercent() {
        return this.percent;
    }
    
    public void setPercent(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.percent = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.bucket);
            this.acceptChild(v, this.outOf);
            this.acceptChild(v, this.on);
            this.acceptChild(v, this.percent);
            this.acceptChild(v, this.byteLength);
        }
        v.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.bucket == expr) {
            this.setBucket(target);
            return true;
        }
        if (this.outOf == expr) {
            this.setOutOf(target);
            return true;
        }
        if (this.on == expr) {
            this.setOn(target);
            return true;
        }
        if (this.percent == expr) {
            this.setPercent(target);
            return true;
        }
        if (this.byteLength == expr) {
            this.setByteLength(target);
            return true;
        }
        return false;
    }
    
    public SQLExpr getByteLength() {
        return this.byteLength;
    }
    
    public void setByteLength(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.byteLength = x;
    }
    
    public SQLExpr getRows() {
        return this.rows;
    }
    
    public void setRows(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.rows = x;
    }
    
    public boolean isBernoulli() {
        return this.bernoulli;
    }
    
    public void setBernoulli(final boolean bernoulli) {
        this.bernoulli = bernoulli;
    }
    
    public boolean isSystem() {
        return this.system;
    }
    
    public void setSystem(final boolean system) {
        this.system = system;
    }
}
