// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowCreateDatabaseStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr database;
    private boolean ifNotExists;
    
    public MySqlShowCreateDatabaseStatement() {
        this.ifNotExists = false;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLExpr database) {
        this.database = database;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
