// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;

public class MySqlPrimaryKey extends MySqlKey implements SQLPrimaryKey
{
    public MySqlPrimaryKey() {
        this.dbType = DbType.mysql;
    }
    
    @Override
    protected void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlPrimaryKey clone() {
        final MySqlPrimaryKey x = new MySqlPrimaryKey();
        this.cloneTo(x);
        return x;
    }
}
