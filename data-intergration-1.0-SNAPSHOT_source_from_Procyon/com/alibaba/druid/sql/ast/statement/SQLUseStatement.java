// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLUseStatement extends SQLStatementImpl implements SQLReplaceable
{
    private SQLName database;
    
    public SQLUseStatement() {
    }
    
    public SQLUseStatement(final DbType dbType) {
        super(dbType);
    }
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.database = x;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.database == expr) {
            this.setDatabase((SQLName)target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.database);
    }
}
