// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterFunctionStatement extends SQLStatementImpl
{
    private SQLName name;
    private boolean debug;
    private boolean reuseSettings;
    private SQLExpr comment;
    private boolean languageSql;
    private boolean containsSql;
    private SQLExpr sqlSecurity;
    
    public boolean isDebug() {
        return this.debug;
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public boolean isReuseSettings() {
        return this.reuseSettings;
    }
    
    public void setReuseSettings(final boolean x) {
        this.reuseSettings = x;
    }
    
    public boolean isLanguageSql() {
        return this.languageSql;
    }
    
    public void setLanguageSql(final boolean languageSql) {
        this.languageSql = languageSql;
    }
    
    public boolean isContainsSql() {
        return this.containsSql;
    }
    
    public void setContainsSql(final boolean containsSql) {
        this.containsSql = containsSql;
    }
    
    public SQLExpr getSqlSecurity() {
        return this.sqlSecurity;
    }
    
    public void setSqlSecurity(final SQLExpr sqlSecurity) {
        if (sqlSecurity != null) {
            sqlSecurity.setParent(this);
        }
        this.sqlSecurity = sqlSecurity;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.comment);
            this.acceptChild(visitor, this.sqlSecurity);
        }
        visitor.endVisit(this);
    }
}
