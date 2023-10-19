// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;

public class SQLRevokeStatement extends SQLPrivilegeStatement
{
    private boolean grantOption;
    
    public SQLRevokeStatement() {
    }
    
    public SQLRevokeStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.resource);
            this.acceptChild(visitor, this.users);
        }
        visitor.endVisit(this);
    }
    
    public boolean isGrantOption() {
        return this.grantOption;
    }
    
    public void setGrantOption(final boolean grantOption) {
        this.grantOption = grantOption;
    }
}
