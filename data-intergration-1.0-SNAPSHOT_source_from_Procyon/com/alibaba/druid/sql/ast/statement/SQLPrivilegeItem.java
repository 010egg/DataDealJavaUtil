// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLPrivilegeItem extends SQLObjectImpl
{
    private SQLExpr action;
    private List<SQLName> columns;
    
    public SQLPrivilegeItem() {
        this.columns = new ArrayList<SQLName>();
    }
    
    public SQLExpr getAction() {
        return this.action;
    }
    
    public void setAction(final SQLExpr action) {
        this.action = action;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.action);
            this.acceptChild(v, this.columns);
        }
        v.endVisit(this);
    }
}
