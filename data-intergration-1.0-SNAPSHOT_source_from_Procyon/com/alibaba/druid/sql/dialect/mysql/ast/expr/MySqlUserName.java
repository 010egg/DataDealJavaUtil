// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.util.FnvHash;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlUserName extends MySqlExprImpl implements SQLName, Cloneable
{
    private String userName;
    private String host;
    private String identifiedBy;
    private long userNameHashCod64;
    private long hashCode64;
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
        this.hashCode64 = 0L;
        this.userNameHashCod64 = 0L;
    }
    
    public String getNormalizeUserName() {
        return SQLUtils.normalize(this.userName);
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
        this.hashCode64 = 0L;
        this.userNameHashCod64 = 0L;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public String getSimpleName() {
        final StringBuilder buf = new StringBuilder();
        if (this.userName.length() == 0 || this.userName.charAt(0) != '\'') {
            buf.append('\'');
            buf.append(this.userName);
            buf.append('\'');
        }
        else {
            buf.append(this.userName);
        }
        buf.append('@');
        if (this.host.length() == 0 || this.host.charAt(0) != '\'') {
            buf.append('\'');
            buf.append(this.host);
            buf.append('\'');
        }
        else {
            buf.append(this.host);
        }
        if (this.identifiedBy != null) {
            buf.append(" identifiedBy by ");
            buf.append(this.identifiedBy);
        }
        return buf.toString();
    }
    
    public String getIdentifiedBy() {
        return this.identifiedBy;
    }
    
    public void setIdentifiedBy(final String identifiedBy) {
        this.identifiedBy = identifiedBy;
    }
    
    @Override
    public String toString() {
        return this.getSimpleName();
    }
    
    @Override
    public MySqlUserName clone() {
        final MySqlUserName x = new MySqlUserName();
        x.userName = this.userName;
        x.host = this.host;
        x.identifiedBy = this.identifiedBy;
        x.hashCode64 = this.hashCode64;
        x.userNameHashCod64 = this.userNameHashCod64;
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public long nameHashCode64() {
        if (this.userNameHashCod64 == 0L && this.userName != null) {
            this.userNameHashCod64 = FnvHash.hashCode64(this.userName);
        }
        return this.userNameHashCod64;
    }
    
    @Override
    public long hashCode64() {
        if (this.hashCode64 == 0L) {
            if (this.host != null) {
                long hash = FnvHash.hashCode64(this.host);
                hash ^= 0x40L;
                hash *= 1099511628211L;
                hash = FnvHash.hashCode64(hash, this.userName);
                this.hashCode64 = hash;
            }
            else {
                this.hashCode64 = this.nameHashCode64();
            }
        }
        return this.hashCode64;
    }
    
    @Override
    public SQLColumnDefinition getResolvedColumn() {
        return null;
    }
}
