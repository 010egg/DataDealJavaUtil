// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;

public class SQLGrantStatement extends SQLPrivilegeStatement
{
    private SQLExpr maxQueriesPerHour;
    private SQLExpr maxUpdatesPerHour;
    private SQLExpr maxConnectionsPerHour;
    private SQLExpr maxUserConnections;
    private boolean adminOption;
    private SQLExpr identifiedBy;
    private String identifiedByPassword;
    private boolean withGrantOption;
    
    public SQLGrantStatement() {
    }
    
    public SQLGrantStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.privileges);
            this.acceptChild(visitor, this.resource);
            this.acceptChild(visitor, this.users);
            this.acceptChild(visitor, this.identifiedBy);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.privileges);
        if (this.resource != null) {
            children.add(this.resource);
        }
        if (this.users != null) {
            children.addAll(this.users);
        }
        if (this.identifiedBy != null) {
            children.add(this.identifiedBy);
        }
        return children;
    }
    
    @Override
    public SQLObjectType getResourceType() {
        return this.resourceType;
    }
    
    @Override
    public void setResourceType(final SQLObjectType resourceType) {
        this.resourceType = resourceType;
    }
    
    public SQLExpr getMaxQueriesPerHour() {
        return this.maxQueriesPerHour;
    }
    
    public void setMaxQueriesPerHour(final SQLExpr maxQueriesPerHour) {
        this.maxQueriesPerHour = maxQueriesPerHour;
    }
    
    public SQLExpr getMaxUpdatesPerHour() {
        return this.maxUpdatesPerHour;
    }
    
    public void setMaxUpdatesPerHour(final SQLExpr maxUpdatesPerHour) {
        this.maxUpdatesPerHour = maxUpdatesPerHour;
    }
    
    public SQLExpr getMaxConnectionsPerHour() {
        return this.maxConnectionsPerHour;
    }
    
    public void setMaxConnectionsPerHour(final SQLExpr maxConnectionsPerHour) {
        this.maxConnectionsPerHour = maxConnectionsPerHour;
    }
    
    public SQLExpr getMaxUserConnections() {
        return this.maxUserConnections;
    }
    
    public void setMaxUserConnections(final SQLExpr maxUserConnections) {
        this.maxUserConnections = maxUserConnections;
    }
    
    public boolean isAdminOption() {
        return this.adminOption;
    }
    
    public void setAdminOption(final boolean adminOption) {
        this.adminOption = adminOption;
    }
    
    public SQLExpr getIdentifiedBy() {
        return this.identifiedBy;
    }
    
    public void setIdentifiedBy(final SQLExpr identifiedBy) {
        this.identifiedBy = identifiedBy;
    }
    
    public String getIdentifiedByPassword() {
        return this.identifiedByPassword;
    }
    
    public void setIdentifiedByPassword(final String identifiedByPassword) {
        this.identifiedByPassword = identifiedByPassword;
    }
    
    public boolean getWithGrantOption() {
        return this.withGrantOption;
    }
    
    public void setWithGrantOption(final boolean withGrantOption) {
        this.withGrantOption = withGrantOption;
    }
}
