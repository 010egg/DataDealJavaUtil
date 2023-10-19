// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;

public class OracleAlterSessionStatement extends OracleStatementImpl implements OracleAlterStatement
{
    private List<SQLAssignItem> items;
    
    public OracleAlterSessionStatement() {
        this.items = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLAssignItem> getItems() {
        return this.items;
    }
    
    public void setItems(final List<SQLAssignItem> items) {
        this.items = items;
    }
}
