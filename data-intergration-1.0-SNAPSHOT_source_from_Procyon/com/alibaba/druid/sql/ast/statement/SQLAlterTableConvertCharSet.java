// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableConvertCharSet extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr charset;
    private SQLExpr collate;
    
    public SQLExpr getCharset() {
        return this.charset;
    }
    
    public void setCharset(final SQLExpr charset) {
        if (charset != null) {
            charset.setParent(this);
        }
        this.charset = charset;
    }
    
    public SQLExpr getCollate() {
        return this.collate;
    }
    
    public void setCollate(final SQLExpr collate) {
        if (collate != null) {
            collate.setParent(this);
        }
        this.collate = collate;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.charset);
            this.acceptChild(visitor, this.collate);
        }
        visitor.endVisit(this);
    }
}
