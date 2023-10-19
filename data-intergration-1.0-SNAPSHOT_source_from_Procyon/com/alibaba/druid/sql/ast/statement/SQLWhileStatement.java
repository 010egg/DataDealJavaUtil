// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLWhileStatement extends SQLStatementImpl implements SQLReplaceable
{
    private SQLExpr condition;
    private List<SQLStatement> statements;
    private String labelName;
    
    public SQLWhileStatement() {
        this.statements = new ArrayList<SQLStatement>();
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.condition);
            this.acceptChild(visitor, this.statements);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.condition);
        children.addAll(this.statements);
        return children;
    }
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
    
    public void setStatements(final List<SQLStatement> statements) {
        this.statements = statements;
    }
    
    public SQLExpr getCondition() {
        return this.condition;
    }
    
    public void setCondition(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.condition = x;
    }
    
    @Override
    public SQLWhileStatement clone() {
        final SQLWhileStatement x = new SQLWhileStatement();
        if (this.condition != null) {
            x.setCondition(this.condition.clone());
        }
        for (final SQLStatement stmt : this.statements) {
            final SQLStatement stmt2 = stmt.clone();
            stmt2.setParent(x);
            x.statements.add(stmt2);
        }
        x.labelName = this.labelName;
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.condition == expr) {
            this.setCondition(target);
            return true;
        }
        return false;
    }
}
