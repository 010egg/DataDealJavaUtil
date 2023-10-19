// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlPrepareStatement extends MySqlStatementImpl
{
    private SQLName name;
    private SQLExpr from;
    
    public MySqlPrepareStatement() {
    }
    
    public MySqlPrepareStatement(final SQLName name, final SQLExpr from) {
        this.name = name;
        this.from = from;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public SQLExpr getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExpr from) {
        this.from = from;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.from);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.name != null) {
            children.add(this.name);
        }
        if (this.from != null) {
            children.add(this.from);
        }
        return children;
    }
}
