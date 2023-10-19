// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLDeclareItem;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlDeclareStatement extends MySqlStatementImpl
{
    private List<SQLDeclareItem> varList;
    
    public MySqlDeclareStatement() {
        this.varList = new ArrayList<SQLDeclareItem>();
    }
    
    public List<SQLDeclareItem> getVarList() {
        return this.varList;
    }
    
    public void addVar(final SQLDeclareItem expr) {
        this.varList.add(expr);
    }
    
    public void setVarList(final List<SQLDeclareItem> varList) {
        this.varList = varList;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.varList);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return this.varList;
    }
}
