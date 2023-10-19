// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableDropForeignKey extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName indexName;
    
    public SQLName getIndexName() {
        return this.indexName;
    }
    
    public void setIndexName(final SQLName indexName) {
        this.indexName = indexName;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.indexName);
        }
        visitor.endVisit(this);
    }
}
