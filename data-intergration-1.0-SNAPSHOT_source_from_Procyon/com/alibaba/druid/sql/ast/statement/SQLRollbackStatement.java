// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLRollbackStatement extends SQLStatementImpl
{
    private SQLName to;
    private Boolean chain;
    private Boolean release;
    private SQLExpr force;
    
    public SQLRollbackStatement() {
    }
    
    public SQLRollbackStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.to);
            this.acceptChild(visitor, this.force);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.to != null) {
            children.add(this.to);
        }
        if (this.force != null) {
            children.add(this.force);
        }
        return children;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName to) {
        this.to = to;
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
    
    public SQLExpr getForce() {
        return this.force;
    }
    
    public void setForce(final SQLExpr force) {
        this.force = force;
    }
}
