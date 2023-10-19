// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLCommentHint extends SQLObjectImpl implements SQLHint
{
    private String text;
    
    public SQLCommentHint() {
    }
    
    public SQLCommentHint(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public SQLCommentHint clone() {
        return new SQLCommentHint(this.text);
    }
    
    @Override
    public String toString() {
        return "/*" + this.text + "*/";
    }
}
