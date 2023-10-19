// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.blink.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class BlinkCreateTableStatement extends SQLCreateTableStatement
{
    private SQLExpr periodFor;
    
    public BlinkCreateTableStatement() {
        this.dbType = DbType.blink;
    }
    
    public SQLExpr getPeriodFor() {
        return this.periodFor;
    }
    
    public void setPeriodFor(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.periodFor = x;
    }
}
