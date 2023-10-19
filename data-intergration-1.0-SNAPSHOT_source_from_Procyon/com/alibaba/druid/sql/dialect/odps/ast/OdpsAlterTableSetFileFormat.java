// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;

public class OdpsAlterTableSetFileFormat extends OdpsObjectImpl implements SQLAlterTableItem
{
    private SQLExpr value;
    
    @Override
    public void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.value);
        }
        v.endVisit(this);
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.value = x;
    }
}
