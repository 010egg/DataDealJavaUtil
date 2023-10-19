// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLShowGrantsStatement;

public class MySqlShowGrantsStatement extends SQLShowGrantsStatement implements MySqlShowStatement
{
    public MySqlShowGrantsStatement() {
        this.dbType = DbType.mysql;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
        }
        visitor.endVisit(this);
    }
}
