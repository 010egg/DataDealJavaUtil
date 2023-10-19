// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLExportDatabaseStatement extends SQLStatementImpl
{
    private SQLName db;
    private boolean realtime;
    
    public SQLExportDatabaseStatement() {
        this.realtime = false;
    }
    
    public SQLName getDb() {
        return this.db;
    }
    
    public void setDb(final SQLName db) {
        this.db = db;
    }
    
    public boolean isRealtime() {
        return this.realtime;
    }
    
    public void setRealtime(final boolean realtime) {
        this.realtime = realtime;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.db);
        }
        v.endVisit(this);
    }
}
