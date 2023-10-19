// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLSubmitJobStatement extends SQLStatementImpl
{
    private boolean await;
    private SQLStatement statment;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statment);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final ArrayList<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.statment);
        return children;
    }
    
    public boolean isAwait() {
        return this.await;
    }
    
    public void setAwait(final boolean await) {
        this.await = await;
    }
    
    public SQLStatement getStatment() {
        return this.statment;
    }
    
    public void setStatment(final SQLStatement statment) {
        this.statment = statment;
    }
    
    @Override
    public SQLSubmitJobStatement clone() {
        final SQLSubmitJobStatement x = new SQLSubmitJobStatement();
        if (this.statment != null) {
            x.setStatment(this.statment.clone());
        }
        return x;
    }
}
