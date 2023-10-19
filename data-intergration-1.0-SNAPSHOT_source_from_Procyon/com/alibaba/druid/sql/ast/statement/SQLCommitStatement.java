// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCommitStatement extends SQLStatementImpl
{
    private boolean write;
    private Boolean wait;
    private Boolean immediate;
    private boolean work;
    private Boolean chain;
    private Boolean release;
    private SQLExpr transactionName;
    private SQLExpr delayedDurability;
    
    public SQLCommitStatement() {
        this.work = false;
    }
    
    @Override
    public SQLCommitStatement clone() {
        final SQLCommitStatement x = new SQLCommitStatement();
        x.write = this.write;
        x.wait = this.wait;
        x.immediate = this.immediate;
        x.work = this.work;
        x.chain = this.chain;
        x.release = this.release;
        if (this.transactionName != null) {
            x.setTransactionName(this.transactionName.clone());
        }
        if (this.delayedDurability != null) {
            x.setDelayedDurability(this.delayedDurability.clone());
        }
        return x;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.transactionName != null) {
                this.transactionName.accept(visitor);
            }
            if (this.delayedDurability != null) {
                this.delayedDurability.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isWrite() {
        return this.write;
    }
    
    public void setWrite(final boolean write) {
        this.write = write;
    }
    
    public Boolean getWait() {
        return this.wait;
    }
    
    public void setWait(final Boolean wait) {
        this.wait = wait;
    }
    
    public Boolean getImmediate() {
        return this.immediate;
    }
    
    public void setImmediate(final Boolean immediate) {
        this.immediate = immediate;
    }
    
    public Boolean getChain() {
        return this.chain;
    }
    
    public void setChain(final Boolean chain) {
        this.chain = chain;
    }
    
    public Boolean getRelease() {
        return this.release;
    }
    
    public void setRelease(final Boolean release) {
        this.release = release;
    }
    
    public boolean isWork() {
        return this.work;
    }
    
    public void setWork(final boolean work) {
        this.work = work;
    }
    
    public SQLExpr getTransactionName() {
        return this.transactionName;
    }
    
    public void setTransactionName(final SQLExpr transactionName) {
        if (transactionName != null) {
            transactionName.setParent(this);
        }
        this.transactionName = transactionName;
    }
    
    public SQLExpr getDelayedDurability() {
        return this.delayedDurability;
    }
    
    public void setDelayedDurability(final SQLExpr delayedDurability) {
        if (delayedDurability != null) {
            delayedDurability.setParent(this);
        }
        this.delayedDurability = delayedDurability;
    }
}
