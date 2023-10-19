// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.Iterator;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLTextLiteralExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MysqlCreateFullTextTokenFilterStatement extends MySqlStatementImpl
{
    private SQLName name;
    private SQLTextLiteralExpr typeName;
    protected final List<SQLAssignItem> options;
    
    public MysqlCreateFullTextTokenFilterStatement() {
        this.options = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.typeName);
            this.acceptChild(visitor, this.options);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLTextLiteralExpr getTypeName() {
        return this.typeName;
    }
    
    public void setTypeName(final SQLTextLiteralExpr typeName) {
        if (this.name != null) {
            this.name.setParent(this);
        }
        this.typeName = typeName;
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
    }
    
    public void addOption(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        assignItem.setParent(this);
        this.options.add(assignItem);
    }
    
    public SQLExpr getOption(final String name) {
        if (name == null) {
            return null;
        }
        final long hash64 = FnvHash.hashCode64(name);
        for (final SQLAssignItem item : this.options) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                return item.getValue();
            }
        }
        return null;
    }
    
    public Object getOptionValue(final String name) {
        final SQLExpr option = this.getOption(name);
        if (option instanceof SQLValuableExpr) {
            return ((SQLValuableExpr)option).getValue();
        }
        return null;
    }
}
