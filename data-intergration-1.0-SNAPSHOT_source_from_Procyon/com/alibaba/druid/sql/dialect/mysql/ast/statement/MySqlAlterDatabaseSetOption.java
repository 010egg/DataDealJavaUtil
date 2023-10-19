// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterDatabaseSetOption extends MySqlObjectImpl implements SQLAlterDatabaseItem
{
    private List<SQLAssignItem> options;
    private SQLName on;
    
    public MySqlAlterDatabaseSetOption() {
        this.options = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.options);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getOption(final String name) {
        for (final SQLAssignItem item : this.options) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).getName().equalsIgnoreCase(name)) {
                return item.getValue();
            }
        }
        return null;
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
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
