// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class SQLPrivilegeStatement extends SQLStatementImpl
{
    protected final List<SQLPrivilegeItem> privileges;
    protected List<SQLExpr> users;
    protected SQLObject resource;
    protected SQLObjectType resourceType;
    
    public SQLPrivilegeStatement() {
        this.privileges = new ArrayList<SQLPrivilegeItem>();
        this.users = new ArrayList<SQLExpr>();
    }
    
    public SQLPrivilegeStatement(final DbType dbType) {
        super(dbType);
        this.privileges = new ArrayList<SQLPrivilegeItem>();
        this.users = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getUsers() {
        return this.users;
    }
    
    public void addUser(final SQLExpr user) {
        if (user == null) {
            return;
        }
        user.setParent(this);
        this.users.add(user);
    }
    
    public void setUsers(final List<SQLExpr> users) {
        this.users = users;
    }
    
    public SQLObject getResource() {
        return this.resource;
    }
    
    public void setResource(final SQLObject x) {
        if (x != null) {
            x.setParent(this);
        }
        this.resource = x;
    }
    
    public void setResource(final SQLExpr resource) {
        if (resource != null) {
            resource.setParent(this);
        }
        this.resource = resource;
    }
    
    public List<SQLPrivilegeItem> getPrivileges() {
        return this.privileges;
    }
    
    public SQLObjectType getResourceType() {
        return this.resourceType;
    }
    
    public void setResourceType(final SQLObjectType x) {
        this.resourceType = x;
    }
}
