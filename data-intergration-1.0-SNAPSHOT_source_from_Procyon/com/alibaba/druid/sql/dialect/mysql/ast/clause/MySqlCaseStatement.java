// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlCaseStatement extends MySqlStatementImpl
{
    private SQLExpr condition;
    private List<MySqlWhenStatement> whenList;
    private SQLIfStatement.Else elseItem;
    
    public MySqlCaseStatement() {
        this.whenList = new ArrayList<MySqlWhenStatement>();
    }
    
    public SQLExpr getCondition() {
        return this.condition;
    }
    
    public void setCondition(final SQLExpr condition) {
        this.condition = condition;
    }
    
    public List<MySqlWhenStatement> getWhenList() {
        return this.whenList;
    }
    
    public void setWhenList(final List<MySqlWhenStatement> whenList) {
        this.whenList = whenList;
    }
    
    public void addWhenStatement(final MySqlWhenStatement stmt) {
        this.whenList.add(stmt);
    }
    
    public SQLIfStatement.Else getElseItem() {
        return this.elseItem;
    }
    
    public void setElseItem(final SQLIfStatement.Else elseItem) {
        this.elseItem = elseItem;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.condition);
            this.acceptChild(visitor, this.whenList);
            this.acceptChild(visitor, this.elseItem);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(children);
        children.addAll(this.whenList);
        children.addAll(this.whenList);
        if (this.elseItem != null) {
            children.add(this.elseItem);
        }
        return children;
    }
    
    public static class MySqlWhenStatement extends MySqlObjectImpl
    {
        private SQLExpr condition;
        private List<SQLStatement> statements;
        
        public MySqlWhenStatement() {
            this.statements = new ArrayList<SQLStatement>();
        }
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.condition);
                this.acceptChild(visitor, this.statements);
            }
            visitor.endVisit(this);
        }
        
        public SQLExpr getCondition() {
            return this.condition;
        }
        
        public void setCondition(final SQLExpr condition) {
            this.condition = condition;
        }
        
        public List<SQLStatement> getStatements() {
            return this.statements;
        }
        
        public void setStatements(final List<SQLStatement> statements) {
            this.statements = statements;
        }
    }
}
