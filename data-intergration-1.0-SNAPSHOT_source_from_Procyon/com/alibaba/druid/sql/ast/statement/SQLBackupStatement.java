// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLBackupStatement extends SQLStatementImpl
{
    private SQLName type;
    private SQLName action;
    private List<SQLCharExpr> properties;
    
    public SQLBackupStatement() {
        this.properties = new ArrayList<SQLCharExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.type);
            this.acceptChild(visitor, this.action);
            this.acceptChild(visitor, this.properties);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getType() {
        return this.type;
    }
    
    public void setType(final SQLName type) {
        this.type = type;
    }
    
    public SQLName getAction() {
        return this.action;
    }
    
    public void setAction(final SQLName action) {
        this.action = action;
    }
    
    public List<SQLCharExpr> getProperties() {
        return this.properties;
    }
}
