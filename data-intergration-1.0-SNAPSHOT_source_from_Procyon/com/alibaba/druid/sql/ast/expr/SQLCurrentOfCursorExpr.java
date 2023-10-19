// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLCurrentOfCursorExpr extends SQLExprImpl implements SQLReplaceable
{
    private SQLName cursorName;
    
    public SQLCurrentOfCursorExpr() {
    }
    
    public SQLCurrentOfCursorExpr(final SQLName cursorName) {
        this.cursorName = cursorName;
    }
    
    @Override
    public SQLCurrentOfCursorExpr clone() {
        final SQLCurrentOfCursorExpr x = new SQLCurrentOfCursorExpr();
        if (this.cursorName != null) {
            x.setCursorName(this.cursorName.clone());
        }
        return x;
    }
    
    public SQLName getCursorName() {
        return this.cursorName;
    }
    
    public void setCursorName(final SQLName cursorName) {
        if (cursorName != null) {
            cursorName.setParent(this);
        }
        this.cursorName = cursorName;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("CURRENT OF ");
            this.cursorName.output(buf);
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.cursorName != null) {
            this.cursorName.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.cursorName);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.cursorName == null) ? 0 : this.cursorName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLCurrentOfCursorExpr other = (SQLCurrentOfCursorExpr)obj;
        if (this.cursorName == null) {
            if (other.cursorName != null) {
                return false;
            }
        }
        else if (!this.cursorName.equals(other.cursorName)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.cursorName == expr) {
            this.setCursorName((SQLName)target);
            return true;
        }
        return false;
    }
}
