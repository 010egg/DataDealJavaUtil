// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlDeclareConditionStatement extends MySqlStatementImpl
{
    private String conditionName;
    private ConditionValue conditionValue;
    
    public String getConditionName() {
        return this.conditionName;
    }
    
    public void setConditionName(final String conditionName) {
        this.conditionName = conditionName;
    }
    
    public ConditionValue getConditionValue() {
        return this.conditionValue;
    }
    
    public void setConditionValue(final ConditionValue conditionValue) {
        this.conditionValue = conditionValue;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
