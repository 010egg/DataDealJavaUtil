// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerObject;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;

public class SQLServerInsertStatement extends SQLInsertStatement implements SQLServerObject
{
    private boolean defaultValues;
    private SQLServerTop top;
    private SQLServerOutput output;
    
    public SQLServerInsertStatement() {
        this.dbType = DbType.sqlserver;
    }
    
    public void cloneTo(final SQLServerInsertStatement x) {
        super.cloneTo(x);
        x.defaultValues = this.defaultValues;
        if (this.top != null) {
            x.setTop(this.top.clone());
        }
        if (this.output != null) {
            x.setOutput(this.output.clone());
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((SQLServerASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getTop());
            this.acceptChild(visitor, this.getTableSource());
            this.acceptChild(visitor, this.getColumns());
            this.acceptChild(visitor, this.getOutput());
            this.acceptChild(visitor, this.getValuesList());
            this.acceptChild(visitor, this.getQuery());
        }
        visitor.endVisit(this);
    }
    
    public boolean isDefaultValues() {
        return this.defaultValues;
    }
    
    public void setDefaultValues(final boolean defaultValues) {
        this.defaultValues = defaultValues;
    }
    
    public SQLServerOutput getOutput() {
        return this.output;
    }
    
    public void setOutput(final SQLServerOutput output) {
        this.output = output;
    }
    
    public SQLServerTop getTop() {
        return this.top;
    }
    
    public void setTop(final SQLServerTop top) {
        this.top = top;
    }
    
    @Override
    public SQLServerInsertStatement clone() {
        final SQLServerInsertStatement x = new SQLServerInsertStatement();
        this.cloneTo(x);
        return x;
    }
}
