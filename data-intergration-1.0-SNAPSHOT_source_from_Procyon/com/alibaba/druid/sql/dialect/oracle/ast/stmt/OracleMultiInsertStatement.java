// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelect;

public class OracleMultiInsertStatement extends OracleStatementImpl
{
    private SQLSelect subQuery;
    private Option option;
    private List<Entry> entries;
    private List<SQLHint> hints;
    
    public OracleMultiInsertStatement() {
        this.entries = new ArrayList<Entry>();
        this.hints = new ArrayList<SQLHint>(1);
    }
    
    public List<SQLHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLHint> hints) {
        this.hints = hints;
    }
    
    public List<Entry> getEntries() {
        return this.entries;
    }
    
    public void addEntry(final Entry entry) {
        if (entry != null) {
            entry.setParent(this);
        }
        this.entries.add(entry);
    }
    
    public Option getOption() {
        return this.option;
    }
    
    public void setOption(final Option option) {
        this.option = option;
    }
    
    public SQLSelect getSubQuery() {
        return this.subQuery;
    }
    
    public void setSubQuery(final SQLSelect subQuery) {
        this.subQuery = subQuery;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.entries);
            this.acceptChild(visitor, this.subQuery);
        }
        visitor.endVisit(this);
    }
    
    public enum Option
    {
        ALL, 
        FIRST;
    }
    
    public static class ConditionalInsertClause extends OracleSQLObjectImpl implements Entry
    {
        private List<ConditionalInsertClauseItem> items;
        private InsertIntoClause elseItem;
        
        public ConditionalInsertClause() {
            this.items = new ArrayList<ConditionalInsertClauseItem>();
        }
        
        public InsertIntoClause getElseItem() {
            return this.elseItem;
        }
        
        public void setElseItem(final InsertIntoClause elseItem) {
            this.elseItem = elseItem;
        }
        
        public List<ConditionalInsertClauseItem> getItems() {
            return this.items;
        }
        
        public void addItem(final ConditionalInsertClauseItem item) {
            if (item != null) {
                item.setParent(this);
            }
            this.items.add(item);
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.items);
                this.acceptChild(visitor, this.elseItem);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ConditionalInsertClauseItem extends OracleSQLObjectImpl
    {
        private SQLExpr when;
        private InsertIntoClause then;
        
        public SQLExpr getWhen() {
            return this.when;
        }
        
        public void setWhen(final SQLExpr when) {
            this.when = when;
        }
        
        public InsertIntoClause getThen() {
            return this.then;
        }
        
        public void setThen(final InsertIntoClause then) {
            this.then = then;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.when);
                this.acceptChild(visitor, this.then);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class InsertIntoClause extends SQLInsertInto implements OracleSQLObject, Entry
    {
        private OracleReturningClause returning;
        private SQLErrorLoggingClause errorLogging;
        
        public OracleReturningClause getReturning() {
            return this.returning;
        }
        
        public void setReturning(final OracleReturningClause returning) {
            this.returning = returning;
        }
        
        public SQLErrorLoggingClause getErrorLogging() {
            return this.errorLogging;
        }
        
        public void setErrorLogging(final SQLErrorLoggingClause errorLogging) {
            this.errorLogging = errorLogging;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.tableSource);
                this.acceptChild(visitor, this.columns);
                this.acceptChild(visitor, this.valuesList);
                this.acceptChild(visitor, this.query);
                this.acceptChild(visitor, this.returning);
                this.acceptChild(visitor, this.errorLogging);
            }
            visitor.endVisit(this);
        }
        
        public void cloneTo(final InsertIntoClause x) {
            super.cloneTo(x);
            if (this.returning != null) {
                x.setReturning(this.returning.clone());
            }
            if (this.errorLogging != null) {
                x.setErrorLogging(this.errorLogging.clone());
            }
        }
        
        @Override
        public InsertIntoClause clone() {
            final InsertIntoClause x = new InsertIntoClause();
            this.cloneTo(x);
            return x;
        }
    }
    
    public interface Entry extends OracleSQLObject
    {
    }
}
