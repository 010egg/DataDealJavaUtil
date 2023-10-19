// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlFlashbackStatement extends MySqlStatementImpl
{
    private SQLName name;
    private SQLName renameTo;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.renameTo);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public SQLName getRenameTo() {
        return this.renameTo;
    }
    
    public void setRenameTo(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.renameTo = x;
    }
}
