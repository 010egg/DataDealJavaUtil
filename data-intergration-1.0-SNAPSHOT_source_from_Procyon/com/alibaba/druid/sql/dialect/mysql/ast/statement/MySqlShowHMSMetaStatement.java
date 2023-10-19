// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlShowHMSMetaStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLName name;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public String getSchema() {
        if (this.name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)this.name).getOwnernName();
        }
        return null;
    }
    
    public String getTableName() {
        return this.name.getSimpleName();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
}
