// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlCursorDeclareStatement extends MySqlStatementImpl
{
    private SQLName cursorName;
    private SQLSelect select;
    
    public SQLName getCursorName() {
        return this.cursorName;
    }
    
    public void setCursorName(final SQLName cursorName) {
        if (cursorName != null) {
            cursorName.setParent(this);
        }
        this.cursorName = cursorName;
    }
    
    public void setCursorName(final String cursorName) {
        this.setCursorName(new SQLIdentifierExpr(cursorName));
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect select) {
        if (select != null) {
            select.setParent(this);
        }
        this.select = select;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.select);
        }
        visitor.endVisit(this);
    }
}
