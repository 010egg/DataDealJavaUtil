// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public abstract class SQLTextLiteralExpr extends SQLExprImpl implements SQLLiteralExpr
{
    protected String text;
    
    public SQLTextLiteralExpr() {
    }
    
    public SQLTextLiteralExpr(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.text == null) ? 0 : this.text.hashCode());
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
        final SQLTextLiteralExpr other = (SQLTextLiteralExpr)obj;
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
        }
        else if (!this.text.equals(other.text)) {
            return false;
        }
        return true;
    }
    
    @Override
    public abstract SQLTextLiteralExpr clone();
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
