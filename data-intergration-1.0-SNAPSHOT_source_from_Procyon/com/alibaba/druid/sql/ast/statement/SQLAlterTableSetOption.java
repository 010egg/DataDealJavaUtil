// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSetOption extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLAssignItem> options;
    private SQLName on;
    
    public SQLAlterTableSetOption() {
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
            this.acceptChild(visitor, this.on);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getOn() {
        return this.on;
    }
    
    public void setOn(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.on = x;
    }
}
