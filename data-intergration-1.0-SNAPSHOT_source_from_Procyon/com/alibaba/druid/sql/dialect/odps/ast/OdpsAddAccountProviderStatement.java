// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsAddAccountProviderStatement extends SQLStatementImpl
{
    private SQLName provider;
    
    public OdpsAddAccountProviderStatement() {
        super(DbType.odps);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.provider);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getProvider() {
        return this.provider;
    }
    
    public void setProvider(final SQLName provider) {
        if (provider != null) {
            provider.setParent(this);
        }
        this.provider = provider;
    }
}
