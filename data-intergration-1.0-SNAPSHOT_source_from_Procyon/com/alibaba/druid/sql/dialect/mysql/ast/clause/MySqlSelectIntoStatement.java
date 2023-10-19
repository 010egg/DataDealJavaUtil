// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlSelectIntoStatement extends MySqlStatementImpl
{
    private SQLSelect select;
    private List<SQLExpr> varList;
    
    public MySqlSelectIntoStatement() {
        this.varList = new ArrayList<SQLExpr>();
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect select) {
        this.select = select;
    }
    
    public List<SQLExpr> getVarList() {
        return this.varList;
    }
    
    public void setVarList(final List<SQLExpr> varList) {
        this.varList = varList;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.select);
            this.acceptChild(visitor, this.varList);
        }
        visitor.endVisit(this);
    }
}
