// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLPurgeTableStatement extends SQLStatementImpl implements SQLDropStatement
{
    private SQLExprTableSource table;
    private boolean all;
    private int count;
    
    public SQLPurgeTableStatement() {
    }
    
    public SQLPurgeTableStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
        }
        visitor.endVisit(this);
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName x) {
        if (x == null) {
            this.table = null;
            return;
        }
        this.setTable(new SQLExprTableSource(x));
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public boolean isAll() {
        return this.all;
    }
    
    public void setAll(final boolean all) {
        this.all = all;
    }
}
