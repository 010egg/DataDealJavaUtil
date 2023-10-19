// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLHint;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;

public class SQLLateralViewTableSource extends SQLTableSourceImpl
{
    private SQLTableSource tableSource;
    private boolean outer;
    private SQLMethodInvokeExpr method;
    private List<SQLName> columns;
    private SQLExpr on;
    
    public SQLLateralViewTableSource() {
        this.columns = new ArrayList<SQLName>(2);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.method);
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public SQLTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    public SQLMethodInvokeExpr getMethod() {
        return this.method;
    }
    
    public void setMethod(final SQLMethodInvokeExpr method) {
        if (method != null) {
            method.setParent(this);
        }
        this.method = method;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final List<SQLName> columns) {
        this.columns = columns;
    }
    
    @Override
    public SQLTableSource findTableSource(final long alias_hash) {
        final long hash = this.aliasHashCode64();
        if (hash != 0L && hash == alias_hash) {
            return this;
        }
        for (final SQLName column : this.columns) {
            if (column.nameHashCode64() == alias_hash) {
                return this;
            }
        }
        if (this.tableSource != null) {
            return this.tableSource.findTableSource(alias_hash);
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String columnName, final int option) {
        for (final SQLName column : this.columns) {
            if (column.nameHashCode64() == columnNameHash) {
                return this;
            }
        }
        if (this.tableSource != null) {
            return this.tableSource.findTableSourceWithColumn(columnNameHash, columnName, option);
        }
        return null;
    }
    
    @Override
    public SQLLateralViewTableSource clone() {
        final SQLLateralViewTableSource x = new SQLLateralViewTableSource();
        x.setAlias(this.alias);
        x.outer = this.outer;
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
        }
        if (this.method != null) {
            x.setMethod(this.method.clone());
        }
        for (final SQLName column : this.columns) {
            final SQLName e2 = column.clone();
            e2.setParent(x);
            x.getColumns().add(e2);
        }
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        if (this.hints != null) {
            for (final SQLHint e3 : this.hints) {
                final SQLHint e4 = e3.clone();
                e4.setParent(x);
                x.getHints().add(e4);
            }
        }
        return x;
    }
    
    public boolean isOuter() {
        return this.outer;
    }
    
    public void setOuter(final boolean outer) {
        this.outer = outer;
    }
    
    public SQLExpr getOn() {
        return this.on;
    }
    
    public void setOn(final SQLExpr on) {
        this.on = on;
    }
}
