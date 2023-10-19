// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;

public class OdpsUDTFSQLSelectItem extends SQLSelectItem implements OdpsObject
{
    public OdpsUDTFSQLSelectItem() {
        super.aliasList = new ArrayList<String>();
    }
    
    @Override
    public void setAlias(final String alias) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OdpsASTVisitor) {
            this.accept0((OdpsASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
}
