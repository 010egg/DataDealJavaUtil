// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class MySqlObjectImpl extends SQLObjectImpl implements MySqlObject
{
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)v);
            return;
        }
        throw new IllegalArgumentException("not support visitor type : " + v.getClass().getName());
    }
    
    @Override
    public abstract void accept0(final MySqlASTVisitor p0);
}
