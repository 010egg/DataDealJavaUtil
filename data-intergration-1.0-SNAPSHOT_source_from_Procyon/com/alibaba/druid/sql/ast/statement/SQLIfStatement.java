// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLIfStatement extends SQLStatementImpl implements SQLReplaceable
{
    private SQLExpr condition;
    private List<SQLStatement> statements;
    private List<ElseIf> elseIfList;
    private Else elseItem;
    
    public SQLIfStatement() {
        this.statements = new ArrayList<SQLStatement>();
        this.elseIfList = new ArrayList<ElseIf>();
    }
    
    @Override
    public SQLIfStatement clone() {
        final SQLIfStatement x = new SQLIfStatement();
        for (final SQLStatement stmt : this.statements) {
            final SQLStatement stmt2 = stmt.clone();
            stmt2.setParent(x);
            x.statements.add(stmt2);
        }
        for (final ElseIf o : this.elseIfList) {
            final ElseIf o2 = o.clone();
            o2.setParent(x);
            x.elseIfList.add(o2);
        }
        if (this.elseItem != null) {
            x.setElseItem(this.elseItem.clone());
        }
        return x;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.condition != null) {
                this.condition.accept(visitor);
            }
            for (int i = 0; i < this.statements.size(); ++i) {
                this.statements.get(i).accept(visitor);
            }
            for (int i = 0; i < this.elseIfList.size(); ++i) {
                this.elseIfList.get(i).accept(visitor);
            }
            if (this.elseItem != null) {
                this.elseItem.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getCondition() {
        return this.condition;
    }
    
    public void setCondition(final SQLExpr condition) {
        if (condition != null) {
            condition.setParent(this);
        }
        this.condition = condition;
    }
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
    
    public void addStatement(final SQLStatement statement) {
        if (statement != null) {
            statement.setParent(this);
        }
        this.statements.add(statement);
    }
    
    public List<ElseIf> getElseIfList() {
        return this.elseIfList;
    }
    
    public Else getElseItem() {
        return this.elseItem;
    }
    
    public void setElseItem(final Else elseItem) {
        if (elseItem != null) {
            elseItem.setParent(this);
        }
        this.elseItem = elseItem;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.condition == expr) {
            this.setCondition(target);
            return true;
        }
        return false;
    }
    
    public static class ElseIf extends SQLObjectImpl implements SQLReplaceable
    {
        private SQLExpr condition;
        private List<SQLStatement> statements;
        
        public ElseIf() {
            this.statements = new ArrayList<SQLStatement>();
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.condition);
                this.acceptChild(visitor, this.statements);
            }
            visitor.endVisit(this);
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
        
        public void setCondition(final SQLExpr condition) {
            if (condition != null) {
                condition.setParent(this);
            }
            this.condition = condition;
        }
        
        @Override
        public boolean replace(final SQLExpr expr, final SQLExpr target) {
            if (this.condition == expr) {
                this.setCondition(target);
                return true;
            }
            return false;
        }
        
        @Override
        public ElseIf clone() {
            final ElseIf x = new ElseIf();
            if (this.condition != null) {
                x.setCondition(this.condition.clone());
            }
            for (final SQLStatement stmt : this.statements) {
                final SQLStatement stmt2 = stmt.clone();
                stmt2.setParent(x);
                x.statements.add(stmt2);
            }
            return x;
        }
    }
    
    public static class Else extends SQLObjectImpl
    {
        private List<SQLStatement> statements;
        
        public Else() {
            this.statements = new ArrayList<SQLStatement>();
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.statements);
            }
            visitor.endVisit(this);
        }
        
        public List<SQLStatement> getStatements() {
            return this.statements;
        }
        
        public void setStatements(final List<SQLStatement> statements) {
            this.statements = statements;
        }
        
        @Override
        public Else clone() {
            final Else x = new Else();
            for (final SQLStatement stmt : this.statements) {
                final SQLStatement stmt2 = stmt.clone();
                stmt2.setParent(x);
                x.statements.add(stmt2);
            }
            return x;
        }
    }
}
