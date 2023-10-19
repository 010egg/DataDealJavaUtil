// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLMergeStatement extends SQLStatementImpl
{
    private final List<SQLHint> hints;
    private SQLTableSource into;
    private String alias;
    private SQLTableSource using;
    private SQLExpr on;
    private MergeUpdateClause updateClause;
    private MergeInsertClause insertClause;
    private SQLErrorLoggingClause errorLoggingClause;
    
    public SQLMergeStatement() {
        this.hints = new ArrayList<SQLHint>();
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.using);
            this.acceptChild(visitor, this.on);
            this.acceptChild(visitor, this.updateClause);
            this.acceptChild(visitor, this.insertClause);
            this.acceptChild(visitor, this.errorLoggingClause);
        }
        visitor.endVisit(this);
    }
    
    public String getAlias() {
        return this.into.getAlias();
    }
    
    public SQLTableSource getInto() {
        return this.into;
    }
    
    public void setInto(final SQLName into) {
        this.setInto(new SQLExprTableSource(into));
    }
    
    public void setInto(final SQLTableSource into) {
        if (into != null) {
            into.setParent(this);
        }
        this.into = into;
    }
    
    public SQLTableSource getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLTableSource using) {
        this.using = using;
    }
    
    public SQLExpr getOn() {
        return this.on;
    }
    
    public void setOn(final SQLExpr on) {
        this.on = on;
    }
    
    public MergeUpdateClause getUpdateClause() {
        return this.updateClause;
    }
    
    public void setUpdateClause(final MergeUpdateClause updateClause) {
        this.updateClause = updateClause;
    }
    
    public MergeInsertClause getInsertClause() {
        return this.insertClause;
    }
    
    public void setInsertClause(final MergeInsertClause insertClause) {
        this.insertClause = insertClause;
    }
    
    public SQLErrorLoggingClause getErrorLoggingClause() {
        return this.errorLoggingClause;
    }
    
    public void setErrorLoggingClause(final SQLErrorLoggingClause errorLoggingClause) {
        this.errorLoggingClause = errorLoggingClause;
    }
    
    public List<SQLHint> getHints() {
        return this.hints;
    }
    
    public static class MergeUpdateClause extends SQLObjectImpl
    {
        private List<SQLUpdateSetItem> items;
        private SQLExpr where;
        private SQLExpr deleteWhere;
        private boolean delete;
        
        public MergeUpdateClause() {
            this.items = new ArrayList<SQLUpdateSetItem>();
        }
        
        public List<SQLUpdateSetItem> getItems() {
            return this.items;
        }
        
        public void addItem(final SQLUpdateSetItem item) {
            if (item != null) {
                item.setParent(this);
            }
            this.items.add(item);
        }
        
        public boolean isDelete() {
            return this.delete;
        }
        
        public void setDelete(final boolean delete) {
            this.delete = delete;
        }
        
        public SQLExpr getWhere() {
            return this.where;
        }
        
        public void setWhere(final SQLExpr where) {
            this.where = where;
        }
        
        public SQLExpr getDeleteWhere() {
            return this.deleteWhere;
        }
        
        public void setDeleteWhere(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.deleteWhere = x;
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.items);
                this.acceptChild(visitor, this.where);
                this.acceptChild(visitor, this.deleteWhere);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class MergeInsertClause extends SQLObjectImpl
    {
        private List<SQLExpr> columns;
        private List<SQLExpr> values;
        private SQLExpr where;
        
        public MergeInsertClause() {
            this.columns = new ArrayList<SQLExpr>();
            this.values = new ArrayList<SQLExpr>();
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.columns);
                this.acceptChild(visitor, this.values);
                this.acceptChild(visitor, this.where);
            }
            visitor.endVisit(this);
        }
        
        public List<SQLExpr> getColumns() {
            return this.columns;
        }
        
        public void setColumns(final List<SQLExpr> columns) {
            this.columns = columns;
        }
        
        public List<SQLExpr> getValues() {
            return this.values;
        }
        
        public void setValues(final List<SQLExpr> values) {
            this.values = values;
        }
        
        public SQLExpr getWhere() {
            return this.where;
        }
        
        public void setWhere(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.where = x;
        }
    }
}
