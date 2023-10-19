// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OdpsSetLabelStatement extends OdpsStatementImpl
{
    private String label;
    private SQLExpr project;
    private SQLExpr user;
    private SQLTableSource table;
    private List<SQLName> columns;
    
    public OdpsSetLabelStatement() {
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
        }
        visitor.endVisit(this);
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public SQLExpr getUser() {
        return this.user;
    }
    
    public void setUser(final SQLExpr user) {
        (this.user = user).setParent(this);
    }
    
    public SQLTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLTableSource table) {
        (this.table = table).setParent(this);
    }
    
    public SQLExpr getProject() {
        return this.project;
    }
    
    public void setProject(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.project = x;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
}
