// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class MySqlResetStatement extends MySqlStatementImpl
{
    private List<String> options;
    
    public MySqlResetStatement() {
        this.options = new ArrayList<String>();
    }
    
    public List<String> getOptions() {
        return this.options;
    }
    
    public void setOptions(final List<String> options) {
        this.options = options;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
