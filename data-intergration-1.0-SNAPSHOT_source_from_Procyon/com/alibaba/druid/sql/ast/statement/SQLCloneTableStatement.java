// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCloneTableStatement extends SQLStatementImpl
{
    protected SQLExprTableSource from;
    protected List<SQLAssignItem> partitions;
    protected SQLExprTableSource to;
    protected boolean ifExistsOverwrite;
    protected boolean ifExistsIgnore;
    
    public SQLCloneTableStatement() {
        this.partitions = new ArrayList<SQLAssignItem>();
        this.ifExistsOverwrite = false;
        this.ifExistsIgnore = false;
    }
    
    public SQLCloneTableStatement(final SQLExpr to) {
        this.partitions = new ArrayList<SQLAssignItem>();
        this.ifExistsOverwrite = false;
        this.ifExistsIgnore = false;
        this.setTo(to);
    }
    
    public SQLExprTableSource getTo() {
        return this.to;
    }
    
    public void setTo(final SQLExprTableSource to) {
        if (to != null) {
            to.setParent(this);
        }
        this.to = to;
    }
    
    public void setTo(final SQLExpr to) {
        this.setTo(new SQLExprTableSource(to));
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public SQLName getToName() {
        if (this.to == null) {
            return null;
        }
        final SQLExpr expr = this.to.expr;
        if (expr instanceof SQLName) {
            return (SQLName)expr;
        }
        return null;
    }
    
    public SQLExprTableSource getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.from = x;
    }
    
    public void setFrom(final SQLName x) {
        this.setFrom(new SQLExprTableSource(x));
    }
    
    public boolean isIfExistsOverwrite() {
        return this.ifExistsOverwrite;
    }
    
    public void setIfExistsOverwrite(final boolean ifExistsOverwrite) {
        this.ifExistsOverwrite = ifExistsOverwrite;
    }
    
    public boolean isIfExistsIgnore() {
        return this.ifExistsIgnore;
    }
    
    public void setIfExistsIgnore(final boolean ifExistsIgnore) {
        this.ifExistsIgnore = ifExistsIgnore;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.to);
        }
        visitor.endVisit(this);
    }
}
