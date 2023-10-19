// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLTextLiteralExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;

public class MySqlCharExpr extends SQLCharExpr implements MySqlExpr
{
    private String charset;
    private String collate;
    private String type;
    
    public MySqlCharExpr() {
    }
    
    public MySqlCharExpr(final String text) {
        super(text);
    }
    
    public MySqlCharExpr(final String text, final String charset) {
        super(text);
        this.charset = charset;
    }
    
    public MySqlCharExpr(final String text, final String charset, final String collate) {
        super(text);
        this.charset = charset;
        this.collate = collate;
    }
    
    public String getCharset() {
        return this.charset;
    }
    
    public void setCharset(final String charset) {
        this.charset = charset;
    }
    
    public String getCollate() {
        return this.collate;
    }
    
    public void setCollate(final String collate) {
        this.collate = collate;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            if (this.charset != null) {
                buf.append(this.charset);
                buf.append(' ');
            }
            if (super.text != null) {
                super.output(buf);
            }
            if (this.collate != null) {
                buf.append(" COLLATE ");
                buf.append(this.collate);
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            visitor.visit(this);
            visitor.endVisit(this);
        }
    }
    
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        this.output(buf);
        return buf.toString();
    }
    
    @Override
    public MySqlCharExpr clone() {
        final MySqlCharExpr x = new MySqlCharExpr(this.text);
        x.collate = this.collate;
        x.charset = this.charset;
        x.type = this.type;
        return x;
    }
}
