// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class MySqlCreateServerStatement extends MySqlStatementImpl implements SQLCreateStatement
{
    private SQLName name;
    private SQLName foreignDataWrapper;
    private SQLExpr host;
    private SQLExpr database;
    private SQLExpr user;
    private SQLExpr password;
    private SQLExpr socket;
    private SQLExpr owner;
    private SQLExpr port;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public SQLName getForeignDataWrapper() {
        return this.foreignDataWrapper;
    }
    
    public void setForeignDataWrapper(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.foreignDataWrapper = x;
    }
    
    public SQLExpr getHost() {
        return this.host;
    }
    
    public void setHost(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.host = x;
    }
    
    public SQLExpr getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.database = x;
    }
    
    public SQLExpr getUser() {
        return this.user;
    }
    
    public void setUser(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.user = x;
    }
    
    public SQLExpr getPassword() {
        return this.password;
    }
    
    public void setPassword(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.password = x;
    }
    
    public SQLExpr getSocket() {
        return this.socket;
    }
    
    public void setSocket(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.socket = x;
    }
    
    public SQLExpr getOwner() {
        return this.owner;
    }
    
    public void setOwner(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.owner = x;
    }
    
    public SQLExpr getPort() {
        return this.port;
    }
    
    public void setPort(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.port = x;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.foreignDataWrapper);
            this.acceptChild(visitor, this.host);
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.user);
            this.acceptChild(visitor, this.password);
            this.acceptChild(visitor, this.socket);
            this.acceptChild(visitor, this.owner);
            this.acceptChild(visitor, this.port);
        }
        visitor.endVisit(this);
    }
}
