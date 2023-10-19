// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlIgnoreIndexHint extends MySqlIndexHintImpl
{
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getIndexList());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlIgnoreIndexHint clone() {
        final MySqlIgnoreIndexHint x = new MySqlIgnoreIndexHint();
        this.cloneTo(x);
        return x;
    }
}
