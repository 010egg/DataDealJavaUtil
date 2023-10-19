// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;

public class OdpsInstallPackageStatement extends OdpsStatementImpl
{
    private SQLName packageName;
    
    @Override
    protected void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.packageName);
        }
        v.endVisit(this);
    }
    
    public SQLName getPackageName() {
        return this.packageName;
    }
    
    public void setPackageName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.packageName = x;
    }
}
