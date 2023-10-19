// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;

public class OracleExceptionStatement extends OracleStatementImpl implements OracleStatement
{
    private List<Item> items;
    
    public OracleExceptionStatement() {
        this.items = new ArrayList<Item>();
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(final Item item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleExceptionStatement clone() {
        final OracleExceptionStatement x = new OracleExceptionStatement();
        for (final Item item : this.items) {
            final Item item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        return x;
    }
    
    public static class Item extends OracleSQLObjectImpl
    {
        private SQLExpr when;
        private List<SQLStatement> statements;
        
        public Item() {
            this.statements = new ArrayList<SQLStatement>();
        }
        
        public SQLExpr getWhen() {
            return this.when;
        }
        
        public void setWhen(final SQLExpr when) {
            this.when = when;
        }
        
        public List<SQLStatement> getStatements() {
            return this.statements;
        }
        
        public void setStatement(final SQLStatement statement) {
            if (statement != null) {
                statement.setParent(this);
                this.statements.add(statement);
            }
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.when);
                this.acceptChild(visitor, this.statements);
            }
            visitor.endVisit(this);
        }
        
        @Override
        public Item clone() {
            final Item x = new Item();
            if (this.when != null) {
                x.setWhen(this.when.clone());
            }
            for (final SQLStatement stmt : this.statements) {
                final SQLStatement stmt2 = stmt.clone();
                stmt2.setParent(x);
                x.statements.add(stmt2);
            }
            return x;
        }
    }
}
