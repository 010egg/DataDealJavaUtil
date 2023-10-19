// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableDropExtPartition extends SQLObjectImpl implements SQLAlterTableItem, MySqlObject
{
    private MySqlExtPartition extPartition;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public void setExPartition(final MySqlExtPartition x) {
        if (x != null) {
            x.setParent(this);
        }
        this.extPartition = x;
    }
    
    public MySqlExtPartition getExtPartition() {
        return this.extPartition;
    }
}
