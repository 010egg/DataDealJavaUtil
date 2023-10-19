// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterProcedureStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLExpr name;
    private boolean compile;
    private boolean reuseSettings;
    private SQLExpr comment;
    private boolean languageSql;
    private boolean containsSql;
    private SQLExpr sqlSecurity;
    
    public SQLAlterProcedureStatement() {
        this.compile = false;
        this.reuseSettings = false;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr name) {
        this.name = name;
    }
    
    public boolean isCompile() {
        return this.compile;
    }
    
    public void setCompile(final boolean compile) {
        this.compile = compile;
    }
    
    public boolean isReuseSettings() {
        return this.reuseSettings;
    }
    
    public void setReuseSettings(final boolean reuseSettings) {
        this.reuseSettings = reuseSettings;
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
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
}
