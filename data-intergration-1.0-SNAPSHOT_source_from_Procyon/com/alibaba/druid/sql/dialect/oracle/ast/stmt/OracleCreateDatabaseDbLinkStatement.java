// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class OracleCreateDatabaseDbLinkStatement extends OracleStatementImpl implements SQLCreateStatement
{
    private boolean shared;
    private boolean _public;
    private SQLName name;
    private SQLName user;
    private String password;
    private SQLExpr using;
    private SQLExpr authenticatedUser;
    private String authenticatedPassword;
    
    public boolean isShared() {
        return this.shared;
    }
    
    public void setShared(final boolean shared) {
        this.shared = shared;
    }
    
    public boolean isPublic() {
        return this._public;
    }
    
    public void setPublic(final boolean value) {
        this._public = value;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public SQLName getUser() {
        return this.user;
    }
    
    public void setUser(final SQLName user) {
        this.user = user;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public SQLExpr getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLExpr using) {
        this.using = using;
    }
    
    public SQLExpr getAuthenticatedUser() {
        return this.authenticatedUser;
    }
    
    public void setAuthenticatedUser(final SQLExpr authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
    
    public String getAuthenticatedPassword() {
        return this.authenticatedPassword;
    }
    
    public void setAuthenticatedPassword(final String authenticatedPassword) {
        this.authenticatedPassword = authenticatedPassword;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.user);
            this.acceptChild(visitor, this.using);
            this.acceptChild(visitor, this.authenticatedUser);
        }
        visitor.endVisit(this);
    }
}
