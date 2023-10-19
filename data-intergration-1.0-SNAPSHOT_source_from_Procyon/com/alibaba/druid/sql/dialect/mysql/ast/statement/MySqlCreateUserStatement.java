// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class MySqlCreateUserStatement extends MySqlStatementImpl implements SQLCreateStatement
{
    private List<UserSpecification> users;
    private boolean ifNotExists;
    
    public MySqlCreateUserStatement() {
        this.users = new ArrayList<UserSpecification>(2);
        this.ifNotExists = false;
    }
    
    public List<UserSpecification> getUsers() {
        return this.users;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public void addUser(final UserSpecification user) {
        if (user != null) {
            user.setParent(this);
        }
        this.users.add(user);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.users);
        }
        visitor.endVisit(this);
    }
    
    public static class UserSpecification extends MySqlObjectImpl
    {
        private SQLExpr user;
        private boolean passwordHash;
        private SQLExpr password;
        private SQLExpr authPlugin;
        private boolean pluginAs;
        
        public UserSpecification() {
            this.passwordHash = false;
        }
        
        public SQLExpr getUser() {
            return this.user;
        }
        
        public void setUser(final SQLExpr user) {
            this.user = user;
        }
        
        public boolean isPasswordHash() {
            return this.passwordHash;
        }
        
        public void setPasswordHash(final boolean passwordHash) {
            this.passwordHash = passwordHash;
        }
        
        public SQLExpr getPassword() {
            return this.password;
        }
        
        public void setPassword(final SQLExpr password) {
            this.password = password;
        }
        
        public SQLExpr getAuthPlugin() {
            return this.authPlugin;
        }
        
        public void setAuthPlugin(final SQLExpr authPlugin) {
            this.authPlugin = authPlugin;
        }
        
        public boolean isPluginAs() {
            return this.pluginAs;
        }
        
        public void setPluginAs(final boolean pluginAs) {
            this.pluginAs = pluginAs;
        }
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.user);
                this.acceptChild(visitor, this.password);
                this.acceptChild(visitor, this.authPlugin);
            }
            visitor.endVisit(this);
        }
    }
}
