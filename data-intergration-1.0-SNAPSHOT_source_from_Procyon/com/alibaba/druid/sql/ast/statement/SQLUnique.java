// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;

public class SQLUnique extends SQLConstraintImpl implements SQLUniqueConstraint, SQLTableElement
{
    protected final SQLIndexDefinition indexDefinition;
    
    public SQLUnique() {
        (this.indexDefinition = new SQLIndexDefinition()).setParent(this);
    }
    
    @Override
    public SQLName getName() {
        return this.indexDefinition.getName();
    }
    
    @Override
    public void setName(final SQLName name) {
        this.indexDefinition.setName(name);
    }
    
    @Override
    public void setName(final String name) {
        this.setName(new SQLIdentifierExpr(name));
    }
    
    @Override
    public SQLExpr getComment() {
        if (this.indexDefinition.hasOptions()) {
            return this.indexDefinition.getOptions().getComment();
        }
        return null;
    }
    
    @Override
    public void setComment(final SQLExpr x) {
        this.indexDefinition.getOptions().setComment(x);
    }
    
    public SQLIndexDefinition getIndexDefinition() {
        return this.indexDefinition;
    }
    
    @Override
    public List<SQLSelectOrderByItem> getColumns() {
        return this.indexDefinition.getColumns();
    }
    
    public void addColumn(final SQLExpr column) {
        if (column == null) {
            return;
        }
        this.addColumn(new SQLSelectOrderByItem(column));
    }
    
    public void addColumn(final SQLSelectOrderByItem column) {
        if (column != null) {
            column.setParent(this);
        }
        this.indexDefinition.getColumns().add(column);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
            this.acceptChild(visitor, this.getCovering());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean containsColumn(final String column) {
        for (final SQLSelectOrderByItem item : this.getColumns()) {
            final SQLExpr expr = item.getExpr();
            if (expr instanceof SQLIdentifierExpr && SQLUtils.nameEquals(((SQLIdentifierExpr)expr).getName(), column)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsColumn(final long columnNameHash) {
        for (final SQLSelectOrderByItem item : this.getColumns()) {
            final SQLExpr expr = item.getExpr();
            if (expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).nameHashCode64() == columnNameHash) {
                return true;
            }
        }
        return false;
    }
    
    public void cloneTo(final SQLUnique x) {
        super.cloneTo(x);
        this.indexDefinition.cloneTo(x.indexDefinition);
    }
    
    @Override
    public SQLUnique clone() {
        final SQLUnique x = new SQLUnique();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public void simplify() {
        super.simplify();
        for (final SQLSelectOrderByItem item : this.getColumns()) {
            final SQLExpr column = item.getExpr();
            if (column instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)column;
                final String columnName = identExpr.getName();
                final String normalized = SQLUtils.normalize(columnName, this.dbType);
                if (normalized == columnName) {
                    continue;
                }
                item.setExpr(new SQLIdentifierExpr(columnName));
            }
        }
    }
    
    public boolean applyColumnRename(final SQLName columnName, final SQLColumnDefinition to) {
        for (final SQLSelectOrderByItem orderByItem : this.getColumns()) {
            final SQLExpr expr = orderByItem.getExpr();
            if (expr instanceof SQLName && SQLUtils.nameEquals((SQLName)expr, columnName)) {
                orderByItem.setExpr(to.getName().clone());
                return true;
            }
            if (expr instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)expr).getMethodName(), columnName.getSimpleName()) && 1 == ((SQLMethodInvokeExpr)expr).getArguments().size() && ((SQLMethodInvokeExpr)expr).getArguments().get(0) instanceof SQLIntegerExpr) {
                if (to.getDataType().hasKeyLength() && 1 == to.getDataType().getArguments().size() && to.getDataType().getArguments().get(0) instanceof SQLIntegerExpr) {
                    final int newKeyLength = to.getDataType().getArguments().get(0).getNumber().intValue();
                    final int oldKeyLength = ((SQLMethodInvokeExpr)expr).getArguments().get(0).getNumber().intValue();
                    if (newKeyLength > oldKeyLength) {
                        ((SQLMethodInvokeExpr)expr).setMethodName(to.getName().getSimpleName());
                        return true;
                    }
                }
                orderByItem.setExpr(to.getName().clone());
                return true;
            }
        }
        return false;
    }
    
    public boolean applyDropColumn(final SQLName columnName) {
        for (int i = this.getColumns().size() - 1; i >= 0; --i) {
            final SQLExpr expr = this.getColumns().get(i).getExpr();
            if (expr instanceof SQLName && SQLUtils.nameEquals((SQLName)expr, columnName)) {
                this.getColumns().remove(i);
                return true;
            }
            if (expr instanceof SQLMethodInvokeExpr && SQLUtils.nameEquals(((SQLMethodInvokeExpr)expr).getMethodName(), columnName.getSimpleName())) {
                this.getColumns().remove(i);
                return true;
            }
        }
        return false;
    }
    
    public List<SQLName> getCovering() {
        return this.indexDefinition.getCovering();
    }
}
