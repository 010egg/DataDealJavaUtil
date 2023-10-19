// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;

public class DrdsShowMetadataLock extends MySqlStatementImpl implements SQLShowStatement
{
    private SQLName schemaName;
    
    public DrdsShowMetadataLock() {
        this.schemaName = null;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public SQLName getSchemaName() {
        return this.schemaName;
    }
    
    public void setSchemaName(final SQLName schemaName) {
        schemaName.setParent(this);
        this.schemaName = schemaName;
    }
}
