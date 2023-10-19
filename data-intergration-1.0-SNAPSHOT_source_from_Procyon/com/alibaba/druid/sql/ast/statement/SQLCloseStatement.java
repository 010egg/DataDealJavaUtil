// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCloseStatement extends SQLStatementImpl
{
    private SQLName cursorName;
    
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
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
