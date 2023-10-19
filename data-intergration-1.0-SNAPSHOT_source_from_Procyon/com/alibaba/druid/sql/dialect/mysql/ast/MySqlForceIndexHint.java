// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlForceIndexHint extends MySqlIndexHintImpl
{
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getIndexList());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlForceIndexHint clone() {
        final MySqlForceIndexHint x = new MySqlForceIndexHint();
        this.cloneTo(x);
        return x;
    }
}
