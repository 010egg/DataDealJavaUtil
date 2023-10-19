// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLNCharExpr extends SQLTextLiteralExpr
{
    private static SQLDataType defaultDataType;
    
    public SQLNCharExpr() {
    }
    
    public SQLNCharExpr(final String text) {
        this.text = text;
    }
    
    public SQLNCharExpr(final String text, final SQLObject parent) {
        this.text = text;
        this.parent = parent;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            if (this.text == null || this.text.length() == 0) {
                buf.append("NULL");
                return;
            }
            buf.append("N'");
            buf.append(this.text.replaceAll("'", "''"));
            buf.append("'");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public SQLNCharExpr clone() {
        return new SQLNCharExpr(this.text);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLNCharExpr.defaultDataType;
    }
    
    static {
        SQLNCharExpr.defaultDataType = new SQLCharacterDataType("nvarchar");
    }
}
