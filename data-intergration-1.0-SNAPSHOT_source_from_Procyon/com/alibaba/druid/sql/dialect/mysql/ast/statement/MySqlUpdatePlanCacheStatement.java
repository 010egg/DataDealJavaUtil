// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;

public class MySqlUpdatePlanCacheStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLSelect formSelect;
    private SQLSelect toSelect;
    
    public SQLSelect getFormSelect() {
        return this.formSelect;
    }
    
    public void setFormSelect(final SQLSelect formSelect) {
        this.formSelect = formSelect;
    }
    
    public SQLSelect getToSelect() {
        return this.toSelect;
    }
    
    public void setToSelect(final SQLSelect toSelect) {
        this.toSelect = toSelect;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.formSelect);
            this.acceptChild(visitor, this.toSelect);
        }
        visitor.endVisit(this);
    }
}
