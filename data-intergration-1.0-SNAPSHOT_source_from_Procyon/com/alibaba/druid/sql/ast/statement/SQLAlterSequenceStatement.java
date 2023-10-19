// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterSequenceStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private Boolean withCache;
    private SQLExpr startWith;
    private SQLExpr incrementBy;
    private SQLExpr minValue;
    private SQLExpr maxValue;
    private boolean noMaxValue;
    private boolean noMinValue;
    private Boolean cycle;
    private Boolean cache;
    private SQLExpr cacheValue;
    private boolean restart;
    private SQLExpr restartWith;
    private Boolean order;
    private boolean changeToSimple;
    private boolean changeToGroup;
    private boolean changeToTime;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.startWith);
            this.acceptChild(visitor, this.incrementBy);
            this.acceptChild(visitor, this.minValue);
            this.acceptChild(visitor, this.maxValue);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.name != null) {
            children.add(this.name);
        }
        if (this.startWith != null) {
            children.add(this.startWith);
        }
        if (this.incrementBy != null) {
            children.add(this.incrementBy);
        }
        if (this.minValue != null) {
            children.add(this.minValue);
        }
        if (this.maxValue != null) {
            children.add(this.maxValue);
        }
        return children;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public SQLExpr getStartWith() {
        return this.startWith;
    }
    
    public void setStartWith(final SQLExpr startWith) {
        this.startWith = startWith;
    }
    
    public SQLExpr getIncrementBy() {
        return this.incrementBy;
    }
    
    public void setIncrementBy(final SQLExpr incrementBy) {
        this.incrementBy = incrementBy;
    }
    
    public SQLExpr getMaxValue() {
        return this.maxValue;
    }
    
    public void setMaxValue(final SQLExpr maxValue) {
        this.maxValue = maxValue;
    }
    
    public Boolean getCycle() {
        return this.cycle;
    }
    
    public void setCycle(final Boolean cycle) {
        this.cycle = cycle;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
    }
    
    public Boolean getWithCache() {
        return this.withCache;
    }
    
    public void setWithCache(final Boolean withCache) {
        this.withCache = withCache;
    }
    
    public Boolean getOrder() {
        return this.order;
    }
    
    public void setOrder(final Boolean order) {
        this.order = order;
    }
    
    public SQLExpr getMinValue() {
        return this.minValue;
    }
    
    public void setMinValue(final SQLExpr minValue) {
        this.minValue = minValue;
    }
    
    public boolean isNoMaxValue() {
        return this.noMaxValue;
    }
    
    public void setNoMaxValue(final boolean noMaxValue) {
        this.noMaxValue = noMaxValue;
    }
    
    public boolean isNoMinValue() {
        return this.noMinValue;
    }
    
    public void setNoMinValue(final boolean noMinValue) {
        this.noMinValue = noMinValue;
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    public SQLExpr getCacheValue() {
        return this.cacheValue;
    }
    
    public void setCacheValue(final SQLExpr cacheValue) {
        if (cacheValue != null) {
            cacheValue.setParent(this);
        }
        this.cacheValue = cacheValue;
    }
    
    public boolean isChangeToSimple() {
        return this.changeToSimple;
    }
    
    public void setChangeToSimple(final boolean changeToSimple) {
        this.changeToSimple = changeToSimple;
    }
    
    public boolean isChangeToGroup() {
        return this.changeToGroup;
    }
    
    public void setChangeToGroup(final boolean changeToGroup) {
        this.changeToGroup = changeToGroup;
    }
    
    public boolean isChangeToTime() {
        return this.changeToTime;
    }
    
    public void setChangeToTime(final boolean changeToTime) {
        this.changeToTime = changeToTime;
    }
    
    public boolean isRestart() {
        return this.restart;
    }
    
    public void setRestart(final boolean restart) {
        this.restart = restart;
    }
    
    public SQLExpr getRestartWith() {
        return this.restartWith;
    }
    
    public void setRestartWith(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.restartWith = x;
    }
}
