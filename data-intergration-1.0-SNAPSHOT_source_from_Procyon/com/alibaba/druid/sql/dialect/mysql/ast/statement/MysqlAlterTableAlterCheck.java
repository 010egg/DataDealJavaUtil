// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MysqlAlterTableAlterCheck extends MySqlObjectImpl implements SQLAlterTableItem
{
    private SQLName name;
    private Boolean enforced;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this) && this.getName() != null) {
            this.getName().accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public Boolean getEnforced() {
        return this.enforced;
    }
    
    public void setEnforced(final Boolean enforced) {
        this.enforced = enforced;
    }
}
