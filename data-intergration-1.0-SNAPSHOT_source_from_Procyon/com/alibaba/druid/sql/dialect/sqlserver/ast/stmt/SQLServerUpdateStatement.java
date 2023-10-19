// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import java.util.List;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;

public class SQLServerUpdateStatement extends SQLUpdateStatement implements SQLServerStatement
{
    private SQLServerTop top;
    private SQLServerOutput output;
    
    public SQLServerUpdateStatement() {
        super(DbType.sqlserver);
    }
    
    public SQLServerTop getTop() {
        return this.top;
    }
    
    public void setTop(final SQLServerTop top) {
        if (top != null) {
            top.setParent(this);
        }
        this.top = top;
    }
    
    public SQLServerOutput getOutput() {
        return this.output;
    }
    
    public void setOutput(final SQLServerOutput output) {
        if (output != null) {
            output.setParent(this);
        }
        this.output = output;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((SQLServerASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.top);
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.output);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
