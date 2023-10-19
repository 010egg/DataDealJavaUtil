// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;

public class MySqlDeleteStatement extends SQLDeleteStatement
{
    private boolean lowPriority;
    private boolean quick;
    private boolean ignore;
    private SQLOrderBy orderBy;
    private SQLLimit limit;
    private boolean forceAllPartitions;
    private SQLName forcePartition;
    private List<SQLCommentHint> hints;
    private boolean fulltextDictionary;
    
    public MySqlDeleteStatement() {
        super(DbType.mysql);
        this.lowPriority = false;
        this.quick = false;
        this.ignore = false;
        this.forceAllPartitions = false;
        this.fulltextDictionary = false;
    }
    
    @Override
    public MySqlDeleteStatement clone() {
        final MySqlDeleteStatement x = new MySqlDeleteStatement();
        this.cloneTo(x);
        x.lowPriority = this.lowPriority;
        x.quick = this.quick;
        x.ignore = this.ignore;
        x.fulltextDictionary = this.fulltextDictionary;
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        if (this.limit != null) {
            x.setLimit(this.limit.clone());
        }
        return x;
    }
    
    public List<SQLCommentHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLCommentHint>();
        }
        return this.hints;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public boolean isLowPriority() {
        return this.lowPriority;
    }
    
    public void setLowPriority(final boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
    
    public boolean isQuick() {
        return this.quick;
    }
    
    public void setQuick(final boolean quick) {
        this.quick = quick;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }
    
    public boolean isFulltextDictionary() {
        return this.fulltextDictionary;
    }
    
    public void setFulltextDictionary(final boolean fulltextDictionary) {
        this.fulltextDictionary = fulltextDictionary;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.with != null) {
                this.with.accept(visitor);
            }
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            if (this.where != null) {
                this.where.accept(visitor);
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.using != null) {
                this.using.accept(visitor);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
            if (this.limit != null) {
                this.limit.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isForceAllPartitions() {
        return this.forceAllPartitions;
    }
    
    public void setForceAllPartitions(final boolean forceAllPartitions) {
        this.forceAllPartitions = forceAllPartitions;
    }
    
    public SQLName getForcePartition() {
        return this.forcePartition;
    }
    
    public void setForcePartition(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.forcePartition = x;
    }
}
