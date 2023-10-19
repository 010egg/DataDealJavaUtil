// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlSetTransactionStatement extends MySqlStatementImpl
{
    private Boolean global;
    private Boolean session;
    private boolean local;
    private String isolationLevel;
    private String accessModel;
    private SQLExpr policy;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public Boolean getGlobal() {
        return this.global;
    }
    
    public void setGlobal(final Boolean global) {
        this.global = global;
    }
    
    public String getIsolationLevel() {
        return this.isolationLevel;
    }
    
    public void setIsolationLevel(final String isolationLevel) {
        this.isolationLevel = isolationLevel;
    }
    
    public String getAccessModel() {
        return this.accessModel;
    }
    
    public void setAccessModel(final String accessModel) {
        this.accessModel = accessModel;
    }
    
    public Boolean getSession() {
        return this.session;
    }
    
    public void setSession(final Boolean session) {
        this.session = session;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public SQLExpr getPolicy() {
        return this.policy;
    }
    
    public void setPolicy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.policy = x;
    }
}
