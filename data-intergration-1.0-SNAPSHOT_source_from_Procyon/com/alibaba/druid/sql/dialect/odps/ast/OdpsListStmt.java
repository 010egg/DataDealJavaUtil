// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsListStmt extends SQLStatementImpl
{
    private SQLExpr object;
    
    public OdpsListStmt() {
        super(DbType.odps);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.object);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getObject() {
        return this.object;
    }
    
    public void setObject(final SQLExpr object) {
        if (object != null) {
            object.setParent(this);
        }
        this.object = object;
    }
}
