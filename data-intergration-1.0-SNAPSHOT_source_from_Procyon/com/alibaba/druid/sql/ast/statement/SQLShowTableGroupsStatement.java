// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowTableGroupsStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    protected SQLName database;
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName database) {
        if (database != null) {
            database.setParent(this);
        }
        this.database = database;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.database == expr) {
            this.setDatabase((SQLName)target);
            return true;
        }
        return false;
    }
}
