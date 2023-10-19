// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;

public class DrdsShowGlobalIndex extends MySqlStatementImpl implements SQLShowStatement
{
    private SQLName tableName;
    
    public DrdsShowGlobalIndex() {
        this.tableName = null;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public SQLName getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final SQLName tableName) {
        tableName.setParent(this);
        this.tableName = tableName;
    }
}
