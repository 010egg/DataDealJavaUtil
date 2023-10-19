// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableValidation extends MySqlObjectImpl implements SQLAlterTableItem
{
    private boolean withValidation;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isWithValidation() {
        return this.withValidation;
    }
    
    public void setWithValidation(final boolean withValidation) {
        this.withValidation = withValidation;
    }
}
