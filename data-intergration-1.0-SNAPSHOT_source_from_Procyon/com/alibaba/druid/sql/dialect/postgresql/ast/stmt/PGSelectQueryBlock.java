// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.dialect.postgresql.ast.PGSQLObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.postgresql.ast.PGSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class PGSelectQueryBlock extends SQLSelectQueryBlock implements PGSQLObject
{
    private List<SQLExpr> distinctOn;
    private FetchClause fetch;
    private ForClause forClause;
    private IntoOption intoOption;
    
    public PGSelectQueryBlock() {
        this.distinctOn = new ArrayList<SQLExpr>(2);
        this.dbType = DbType.postgresql;
    }
    
    public IntoOption getIntoOption() {
        return this.intoOption;
    }
    
    public void setIntoOption(final IntoOption intoOption) {
        this.intoOption = intoOption;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.distinctOn);
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.groupBy);
            this.acceptChild(visitor, this.windows);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.limit);
            this.acceptChild(visitor, this.fetch);
            this.acceptChild(visitor, this.forClause);
        }
        visitor.endVisit(this);
    }
    
    public FetchClause getFetch() {
        return this.fetch;
    }
    
    public void setFetch(final FetchClause fetch) {
        this.fetch = fetch;
    }
    
    public ForClause getForClause() {
        return this.forClause;
    }
    
    public void setForClause(final ForClause forClause) {
        this.forClause = forClause;
    }
    
    public List<SQLExpr> getDistinctOn() {
        return this.distinctOn;
    }
    
    public void setDistinctOn(final List<SQLExpr> distinctOn) {
        this.distinctOn = distinctOn;
    }
    
    public enum IntoOption
    {
        TEMPORARY, 
        TEMP, 
        UNLOGGED;
    }
    
    public static class FetchClause extends PGSQLObjectImpl
    {
        private Option option;
        private SQLExpr count;
        
        public Option getOption() {
            return this.option;
        }
        
        public void setOption(final Option option) {
            this.option = option;
        }
        
        public SQLExpr getCount() {
            return this.count;
        }
        
        public void setCount(final SQLExpr count) {
            this.count = count;
        }
        
        @Override
        public void accept0(final PGASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.count);
            }
            visitor.endVisit(this);
        }
        
        public enum Option
        {
            FIRST, 
            NEXT;
        }
    }
    
    public static class ForClause extends PGSQLObjectImpl
    {
        private List<SQLExpr> of;
        private boolean noWait;
        private boolean skipLocked;
        private Option option;
        
        public ForClause() {
            this.of = new ArrayList<SQLExpr>(2);
        }
        
        public Option getOption() {
            return this.option;
        }
        
        public void setOption(final Option option) {
            this.option = option;
        }
        
        public List<SQLExpr> getOf() {
            return this.of;
        }
        
        public void setOf(final List<SQLExpr> of) {
            this.of = of;
        }
        
        public boolean isNoWait() {
            return this.noWait;
        }
        
        public void setNoWait(final boolean noWait) {
            this.noWait = noWait;
        }
        
        public boolean isSkipLocked() {
            return this.skipLocked;
        }
        
        public void setSkipLocked(final boolean skipLocked) {
            this.skipLocked = skipLocked;
        }
        
        @Override
        public void accept0(final PGASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.of);
            }
            visitor.endVisit(this);
        }
        
        public enum Option
        {
            UPDATE, 
            SHARE;
        }
    }
}
