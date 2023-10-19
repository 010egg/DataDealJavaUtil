// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLCharExpr extends SQLTextLiteralExpr implements SQLValuableExpr, Comparable<SQLCharExpr>
{
    public static final SQLDataType DATA_TYPE;
    
    public SQLCharExpr() {
    }
    
    public SQLCharExpr(final String text) {
        this.text = text;
    }
    
    public SQLCharExpr(final String text, final SQLObject parent) {
        this.text = text;
        this.parent = parent;
    }
    
    @Override
    public void output(final Appendable buf) {
        this.accept(new SQLASTOutputVisitor(buf));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public Object getValue() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this);
    }
    
    @Override
    public SQLCharExpr clone() {
        return new SQLCharExpr(this.text);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLCharExpr.DATA_TYPE;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public int compareTo(final SQLCharExpr o) {
        return this.text.compareTo(o.text);
    }
    
    static {
        DATA_TYPE = new SQLCharacterDataType("char");
    }
}
