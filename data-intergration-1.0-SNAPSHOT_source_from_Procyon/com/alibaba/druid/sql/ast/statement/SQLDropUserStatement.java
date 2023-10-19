// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropUserStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private List<SQLExpr> users;
    protected boolean ifExists;
    
    public SQLDropUserStatement() {
        this.users = new ArrayList<SQLExpr>(2);
        this.ifExists = false;
    }
    
    public SQLDropUserStatement(final DbType dbType) {
        super(dbType);
        this.users = new ArrayList<SQLExpr>(2);
        this.ifExists = false;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public List<SQLExpr> getUsers() {
        return this.users;
    }
    
    public void addUser(final SQLExpr user) {
        if (user != null) {
            user.setParent(this);
        }
        this.users.add(user);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.users);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return this.users;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.users.size(); ++i) {
            if (this.users.get(i) == expr) {
                target.setParent(this);
                this.users.set(i, target);
                return true;
            }
        }
        return false;
    }
}
