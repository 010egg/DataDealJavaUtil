// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlAlterUserStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private boolean ifExists;
    private final List<AlterUser> alterUsers;
    private PasswordOption passwordOption;
    
    public MySqlAlterUserStatement() {
        this.ifExists = false;
        this.alterUsers = new ArrayList<AlterUser>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (final AlterUser alterUser : this.alterUsers) {
                this.acceptChild(visitor, alterUser.user);
            }
            if (this.passwordOption != null && this.passwordOption.getIntervalDays() != null) {
                this.acceptChild(visitor, this.passwordOption.getIntervalDays());
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public List<SQLExpr> getUsers() {
        final List<SQLExpr> users = new ArrayList<SQLExpr>();
        for (final AlterUser alterUser : this.alterUsers) {
            users.add(alterUser.user);
        }
        return users;
    }
    
    public List<AlterUser> getAlterUsers() {
        return this.alterUsers;
    }
    
    public PasswordOption getPasswordOption() {
        return this.passwordOption;
    }
    
    public void setPasswordOption(final PasswordOption passwordOption) {
        this.passwordOption = passwordOption;
    }
    
    public static class AuthOption
    {
        private SQLCharExpr authString;
        
        public SQLCharExpr getAuthString() {
            return this.authString;
        }
        
        public void setAuthString(final SQLCharExpr authString) {
            this.authString = authString;
        }
    }
    
    public static class AlterUser
    {
        private SQLExpr user;
        private AuthOption authOption;
        
        public SQLExpr getUser() {
            return this.user;
        }
        
        public void setUser(final SQLExpr user) {
            this.user = user;
        }
        
        public AuthOption getAuthOption() {
            return this.authOption;
        }
        
        public void setAuthOption(final AuthOption authOption) {
            this.authOption = authOption;
        }
    }
    
    public static class PasswordOption
    {
        private PasswordExpire expire;
        private SQLIntegerExpr intervalDays;
        
        public PasswordExpire getExpire() {
            return this.expire;
        }
        
        public void setExpire(final PasswordExpire expire) {
            this.expire = expire;
        }
        
        public SQLIntegerExpr getIntervalDays() {
            return this.intervalDays;
        }
        
        public void setIntervalDays(final SQLIntegerExpr intervalDays) {
            this.intervalDays = intervalDays;
        }
    }
    
    public enum PasswordExpire
    {
        PASSWORD_EXPIRE, 
        PASSWORD_EXPIRE_DEFAULT, 
        PASSWORD_EXPIRE_NEVER, 
        PASSWORD_EXPIRE_INTERVAL;
    }
}
