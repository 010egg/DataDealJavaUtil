// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;

public class OdpsQueryAliasStatement extends OdpsStatementImpl
{
    private String variant;
    private boolean cache;
    private SQLSelectStatement statement;
    
    public OdpsQueryAliasStatement() {
    }
    
    public OdpsQueryAliasStatement(final String variant, final SQLSelectStatement statement) {
        this.variant = variant;
        this.statement = statement;
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.statement);
        }
        v.endVisit(this);
    }
    
    public String getVariant() {
        return this.variant;
    }
    
    public void setVariant(final String variant) {
        this.variant = variant;
    }
    
    public SQLSelectStatement getStatement() {
        return this.statement;
    }
    
    public void setStatement(final SQLSelectStatement statement) {
        this.statement = statement;
    }
    
    public boolean isCache() {
        return this.cache;
    }
    
    public void setCache(final boolean cache) {
        this.cache = cache;
    }
}
