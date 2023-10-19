// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlLeaveStatement extends MySqlStatementImpl
{
    private String labelName;
    
    public MySqlLeaveStatement() {
    }
    
    public MySqlLeaveStatement(final String labelName) {
        this.labelName = labelName;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
