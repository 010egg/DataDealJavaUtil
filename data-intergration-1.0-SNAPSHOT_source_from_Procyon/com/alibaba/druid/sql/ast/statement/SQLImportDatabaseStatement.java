// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLImportDatabaseStatement extends SQLStatementImpl
{
    private SQLName db;
    private SQLName status;
    
    public SQLName getDb() {
        return this.db;
    }
    
    public void setDb(final SQLName db) {
        this.db = db;
    }
    
    public SQLName getStatus() {
        return this.status;
    }
    
    public void setStatus(final SQLName status) {
        this.status = status;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.db);
            this.acceptChild(v, this.status);
        }
        v.endVisit(this);
    }
}
