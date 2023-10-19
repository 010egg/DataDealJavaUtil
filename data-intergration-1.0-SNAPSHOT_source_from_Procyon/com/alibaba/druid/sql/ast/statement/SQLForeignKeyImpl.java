// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;

public class SQLForeignKeyImpl extends SQLConstraintImpl implements SQLForeignKeyConstraint
{
    private SQLExprTableSource referencedTable;
    private List<SQLName> referencingColumns;
    private List<SQLName> referencedColumns;
    private boolean onDeleteCascade;
    private boolean onDeleteSetNull;
    private boolean disableNovalidate;
    
    public SQLForeignKeyImpl() {
        this.referencingColumns = new ArrayList<SQLName>();
        this.referencedColumns = new ArrayList<SQLName>();
        this.onDeleteCascade = false;
        this.onDeleteSetNull = false;
        this.disableNovalidate = false;
    }
    
    @Override
    public List<SQLName> getReferencingColumns() {
        return this.referencingColumns;
    }
    
    @Override
    public SQLExprTableSource getReferencedTable() {
        return this.referencedTable;
    }
    
    @Override
    public SQLName getReferencedTableName() {
        if (this.referencedTable == null) {
            return null;
        }
        return this.referencedTable.getName();
    }
    
    @Override
    public void setReferencedTableName(final SQLName value) {
        if (value == null) {
            this.referencedTable = null;
            return;
        }
        this.setReferencedTable(new SQLExprTableSource(value));
    }
    
    public void setReferencedTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.referencedTable = x;
    }
    
    @Override
    public List<SQLName> getReferencedColumns() {
        return this.referencedColumns;
    }
    
    public boolean isOnDeleteCascade() {
        return this.onDeleteCascade;
    }
    
    public void setOnDeleteCascade(final boolean onDeleteCascade) {
        this.onDeleteCascade = onDeleteCascade;
    }
    
    public boolean isOnDeleteSetNull() {
        return this.onDeleteSetNull;
    }
    
    public void setOnDeleteSetNull(final boolean onDeleteSetNull) {
        this.onDeleteSetNull = onDeleteSetNull;
    }
    
    public boolean isDisableNovalidate() {
        return this.disableNovalidate;
    }
    
    public void setDisableNovalidate(final boolean disableNovalidate) {
        this.disableNovalidate = disableNovalidate;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getReferencedTableName());
            this.acceptChild(visitor, this.getReferencingColumns());
            this.acceptChild(visitor, this.getReferencedColumns());
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final SQLForeignKeyImpl x) {
        super.cloneTo(x);
        if (this.referencedTable != null) {
            x.setReferencedTable(this.referencedTable.clone());
        }
        for (final SQLName column : this.referencingColumns) {
            final SQLName columnClone = column.clone();
            columnClone.setParent(x);
            x.getReferencingColumns().add(columnClone);
        }
        for (final SQLName column : this.referencedColumns) {
            final SQLName columnClone = column.clone();
            columnClone.setParent(x);
            x.getReferencedColumns().add(columnClone);
        }
    }
    
    @Override
    public SQLForeignKeyImpl clone() {
        final SQLForeignKeyImpl x = new SQLForeignKeyImpl();
        this.cloneTo(x);
        return x;
    }
    
    public enum Match
    {
        FULL("FULL"), 
        PARTIAL("PARTIAL"), 
        SIMPLE("SIMPLE");
        
        public final String name;
        public final String name_lcase;
        
        private Match(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
    
    public enum On
    {
        DELETE("DELETE"), 
        UPDATE("UPDATE");
        
        public final String name;
        public final String name_lcase;
        
        private On(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
    
    public enum Option
    {
        RESTRICT("RESTRICT"), 
        CASCADE("CASCADE"), 
        SET_NULL("SET NULL"), 
        NO_ACTION("NO ACTION"), 
        SET_DEFAULT("SET DEFAULT");
        
        public final String name;
        public final String name_lcase;
        
        private Option(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
        
        public String getText() {
            return this.name;
        }
    }
}
