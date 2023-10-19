// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatementImpl;

public class SQLServerSetTransactionIsolationLevelStatement extends SQLServerStatementImpl implements SQLServerStatement
{
    private String level;
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public String getLevel() {
        return this.level;
    }
    
    public void setLevel(final String level) {
        this.level = level;
    }
}
