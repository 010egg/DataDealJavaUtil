// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class HiveCreateTableStatement extends SQLCreateTableStatement
{
    protected List<SQLExpr> skewedBy;
    protected List<SQLExpr> skewedByOn;
    protected Map<String, SQLObject> serdeProperties;
    protected SQLExpr metaLifeCycle;
    protected boolean likeQuery;
    protected List<SQLAssignItem> mappedBy;
    protected SQLExpr intoBuckets;
    protected SQLExpr using;
    
    public HiveCreateTableStatement() {
        this.skewedBy = new ArrayList<SQLExpr>();
        this.skewedByOn = new ArrayList<SQLExpr>();
        this.serdeProperties = new LinkedHashMap<String, SQLObject>();
        this.likeQuery = false;
        this.mappedBy = new ArrayList<SQLAssignItem>(1);
        this.dbType = DbType.hive;
    }
    
    public HiveCreateTableStatement(final DbType dbType) {
        this.skewedBy = new ArrayList<SQLExpr>();
        this.skewedByOn = new ArrayList<SQLExpr>();
        this.serdeProperties = new LinkedHashMap<String, SQLObject>();
        this.likeQuery = false;
        this.mappedBy = new ArrayList<SQLAssignItem>(1);
        this.dbType = dbType;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v);
        }
        v.endVisit(this);
    }
    
    @Override
    protected void acceptChild(final SQLASTVisitor v) {
        super.acceptChild(v);
        this.acceptChild(v, this.skewedBy);
        this.acceptChild(v, this.skewedByOn);
        for (final SQLObject item : this.serdeProperties.values()) {
            this.acceptChild(v, item);
        }
        this.acceptChild(v, this.metaLifeCycle);
        this.acceptChild(v, this.intoBuckets);
    }
    
    public void cloneTo(final HiveCreateTableStatement x) {
        super.cloneTo(x);
        for (final SQLExpr item : this.skewedBy) {
            x.addSkewedBy(item.clone());
        }
        for (final SQLExpr item : this.skewedByOn) {
            x.addSkewedByOn(item.clone());
        }
        for (final Map.Entry<String, SQLObject> entry : this.serdeProperties.entrySet()) {
            final SQLObject entryValue = entry.getValue().clone();
            entryValue.setParent(x);
            x.serdeProperties.put(entry.getKey(), entryValue);
        }
        if (this.metaLifeCycle != null) {
            x.setMetaLifeCycle(this.metaLifeCycle.clone());
        }
        x.setLikeQuery(this.likeQuery);
        if (this.mappedBy != null) {
            for (final SQLAssignItem item2 : this.mappedBy) {
                final SQLAssignItem item3 = item2.clone();
                item3.setParent(this);
                x.mappedBy.add(item3);
            }
        }
        if (this.intoBuckets != null) {
            x.intoBuckets = this.intoBuckets.clone();
        }
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
    }
    
    @Override
    public HiveCreateTableStatement clone() {
        final HiveCreateTableStatement x = new HiveCreateTableStatement();
        this.cloneTo(x);
        return x;
    }
    
    public List<SQLExpr> getSkewedBy() {
        return this.skewedBy;
    }
    
    public void addSkewedBy(final SQLExpr item) {
        item.setParent(this);
        this.skewedBy.add(item);
    }
    
    public List<SQLExpr> getSkewedByOn() {
        return this.skewedByOn;
    }
    
    public void addSkewedByOn(final SQLExpr item) {
        item.setParent(this);
        this.skewedByOn.add(item);
    }
    
    public Map<String, SQLObject> getSerdeProperties() {
        return this.serdeProperties;
    }
    
    public SQLExpr getMetaLifeCycle() {
        return this.metaLifeCycle;
    }
    
    public void setMetaLifeCycle(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.metaLifeCycle = x;
    }
    
    public boolean isLikeQuery() {
        return this.likeQuery;
    }
    
    public void setLikeQuery(final boolean likeQuery) {
        this.likeQuery = likeQuery;
    }
    
    public List<SQLAssignItem> getMappedBy() {
        return this.mappedBy;
    }
    
    public SQLExpr getIntoBuckets() {
        return this.intoBuckets;
    }
    
    public void setIntoBuckets(final SQLExpr intoBuckets) {
        this.intoBuckets = intoBuckets;
    }
    
    public SQLExpr getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.using = x;
    }
}
