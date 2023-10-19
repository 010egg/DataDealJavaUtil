// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsBaselineStatement extends MySqlStatementImpl implements SQLStatement
{
    private String operation;
    private List<Long> baselineIds;
    private SQLSelect select;
    
    public DrdsBaselineStatement() {
        this.baselineIds = new ArrayList<Long>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public String getOperation() {
        return this.operation;
    }
    
    public void setOperation(final String operation) {
        this.operation = operation;
    }
    
    public void addBaselineId(final long id) {
        this.baselineIds.add(id);
    }
    
    public List<Long> getBaselineIds() {
        return this.baselineIds;
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect select) {
        this.select = select;
    }
}
