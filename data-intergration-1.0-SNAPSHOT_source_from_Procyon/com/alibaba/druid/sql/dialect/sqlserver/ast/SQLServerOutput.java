// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class SQLServerOutput extends SQLServerObjectImpl
{
    protected SQLExprTableSource into;
    protected final List<SQLExpr> columns;
    protected final List<SQLSelectItem> selectList;
    
    public SQLServerOutput() {
        this.columns = new ArrayList<SQLExpr>();
        this.selectList = new ArrayList<SQLSelectItem>();
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public SQLExprTableSource getInto() {
        return this.into;
    }
    
    public void setInto(final SQLExprTableSource into) {
        this.into = into;
    }
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    public List<SQLSelectItem> getSelectList() {
        return this.selectList;
    }
    
    @Override
    public SQLServerOutput clone() {
        final SQLServerOutput x = new SQLServerOutput();
        if (this.into != null) {
            x.setInto(this.into.clone());
        }
        for (final SQLExpr c : this.columns) {
            final SQLExpr c2 = c.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        for (final SQLSelectItem item : this.selectList) {
            final SQLSelectItem item2 = item.clone();
            item2.setParent(x);
            x.selectList.add(item2);
        }
        return x;
    }
}
