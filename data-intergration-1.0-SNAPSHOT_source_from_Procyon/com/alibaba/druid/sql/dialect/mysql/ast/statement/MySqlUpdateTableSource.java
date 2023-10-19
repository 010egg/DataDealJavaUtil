// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLTableSourceImpl;

public class MySqlUpdateTableSource extends SQLTableSourceImpl
{
    private MySqlUpdateStatement update;
    
    public MySqlUpdateTableSource(final MySqlUpdateStatement update) {
        this.update = update;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
            return;
        }
        throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
    }
    
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.update);
        }
        visitor.endVisit(this);
    }
    
    public MySqlUpdateStatement getUpdate() {
        return this.update;
    }
    
    public void setUpdate(final MySqlUpdateStatement update) {
        this.update = update;
    }
    
    @Override
    public MySqlUpdateTableSource clone() {
        final MySqlUpdateTableSource x = new MySqlUpdateTableSource(this.update);
        x.setAlias(this.alias);
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        if (this.hints != null) {
            for (final SQLHint e : this.hints) {
                final SQLHint e2 = e.clone();
                e2.setParent(x);
                x.getHints().add(e2);
            }
        }
        return x;
    }
}
