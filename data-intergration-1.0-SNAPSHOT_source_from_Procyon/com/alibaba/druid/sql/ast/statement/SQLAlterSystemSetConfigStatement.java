// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterSystemSetConfigStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private List<SQLAssignItem> options;
    
    public SQLAlterSystemSetConfigStatement() {
        this.options = new ArrayList<SQLAssignItem>();
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
    }
    
    public void addOption(final SQLAssignItem item) {
        item.setParent(this);
        this.options.add(item);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.options);
        }
        visitor.endVisit(this);
    }
}
