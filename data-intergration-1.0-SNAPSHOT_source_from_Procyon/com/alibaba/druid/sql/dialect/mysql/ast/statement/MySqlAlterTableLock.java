// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableLock extends MySqlObjectImpl implements SQLAlterTableItem
{
    private SQLExpr lockType;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.lockType);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getLockType() {
        return this.lockType;
    }
    
    public void setLockType(final SQLExpr lockType) {
        this.lockType = lockType;
    }
}
