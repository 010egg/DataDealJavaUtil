// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlExecuteForAdsStatement extends MySqlStatementImpl
{
    private SQLName action;
    private SQLName role;
    private SQLCharExpr targetId;
    private SQLName status;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.action);
            this.acceptChild(visitor, this.role);
            this.acceptChild(visitor, this.targetId);
            this.acceptChild(visitor, this.status);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getAction() {
        return this.action;
    }
    
    public void setAction(final SQLName action) {
        this.action = action;
    }
    
    public SQLName getRole() {
        return this.role;
    }
    
    public void setRole(final SQLName role) {
        this.role = role;
    }
    
    public SQLCharExpr getTargetId() {
        return this.targetId;
    }
    
    public void setTargetId(final SQLCharExpr targetId) {
        this.targetId = targetId;
    }
    
    public SQLName getStatus() {
        return this.status;
    }
    
    public void setStatus(final SQLName status) {
        this.status = status;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.action != null) {
            children.add(this.action);
        }
        if (this.role != null) {
            children.add(this.role);
        }
        if (this.targetId != null) {
            children.add(this.targetId);
        }
        if (this.status != null) {
            children.add(this.status);
        }
        return children;
    }
}
