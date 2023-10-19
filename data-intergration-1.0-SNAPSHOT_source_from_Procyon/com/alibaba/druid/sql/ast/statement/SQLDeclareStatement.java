// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDeclareStatement extends SQLStatementImpl
{
    protected List<SQLDeclareItem> items;
    
    public SQLDeclareStatement() {
        this.items = new ArrayList<SQLDeclareItem>();
    }
    
    public SQLDeclareStatement(final SQLName name, final SQLDataType dataType) {
        this.items = new ArrayList<SQLDeclareItem>();
        this.addItem(new SQLDeclareItem(name, dataType));
    }
    
    public SQLDeclareStatement(final SQLName name, final SQLDataType dataType, final SQLExpr value) {
        this.items = new ArrayList<SQLDeclareItem>();
        this.addItem(new SQLDeclareItem(name, dataType, value));
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLDeclareItem> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLDeclareItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
}
