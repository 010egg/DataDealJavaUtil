// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLOpenStatement extends SQLStatementImpl
{
    private SQLName cursorName;
    private final List<SQLName> columns;
    private SQLExpr forExpr;
    private final List<SQLExpr> using;
    
    public SQLOpenStatement() {
        this.columns = new ArrayList<SQLName>();
        this.using = new ArrayList<SQLExpr>();
    }
    
    public SQLName getCursorName() {
        return this.cursorName;
    }
    
    public void setCursorName(final String cursorName) {
        this.setCursorName(new SQLIdentifierExpr(cursorName));
    }
    
    public void setCursorName(final SQLName cursorName) {
        if (cursorName != null) {
            cursorName.setParent(this);
        }
        this.cursorName = cursorName;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.cursorName);
            this.acceptChild(visitor, this.forExpr);
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getFor() {
        return this.forExpr;
    }
    
    public void setFor(final SQLExpr forExpr) {
        if (forExpr != null) {
            forExpr.setParent(this);
        }
        this.forExpr = forExpr;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public List<SQLExpr> getUsing() {
        return this.using;
    }
}
