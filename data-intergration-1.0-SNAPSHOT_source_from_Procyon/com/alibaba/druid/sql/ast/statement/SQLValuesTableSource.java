// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLValuesTableSource extends SQLTableSourceImpl implements SQLSelectQuery, SQLReplaceable
{
    private boolean parenthesized;
    private List<SQLListExpr> values;
    private List<SQLName> columns;
    
    public SQLValuesTableSource() {
        this.values = new ArrayList<SQLListExpr>();
        this.columns = new ArrayList<SQLName>();
    }
    
    public List<SQLListExpr> getValues() {
        return this.values;
    }
    
    public void addValue(final SQLListExpr row) {
        if (row == null) {
            return;
        }
        row.setParent(this);
        this.values.add(row);
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        column.setParent(this);
        this.columns.add(column);
    }
    
    public void addColumn(final String column) {
        this.addColumn(new SQLIdentifierExpr(column));
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.values);
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean isParenthesized() {
        return this.parenthesized;
    }
    
    @Override
    public void setParenthesized(final boolean paren) {
        this.parenthesized = paren;
    }
    
    @Override
    public SQLValuesTableSource clone() {
        final SQLValuesTableSource x = new SQLValuesTableSource();
        x.setAlias(this.alias);
        for (final SQLListExpr e : this.values) {
            final SQLListExpr e2 = e.clone();
            e2.setParent(x);
            x.getValues().add(e2);
        }
        for (final SQLName e3 : this.columns) {
            final SQLName e4 = e3.clone();
            e4.setParent(x);
            x.getColumns().add(e4);
        }
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        if (this.hints != null) {
            for (final SQLHint e5 : this.hints) {
                final SQLHint e6 = e5.clone();
                e6.setParent(x);
                x.getHints().add(e6);
            }
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) == expr) {
                target.setParent(this);
                this.values.set(i, (SQLListExpr)expr);
                return true;
            }
        }
        for (int i = 0; i < this.columns.size(); ++i) {
            if (this.columns.get(i) == expr) {
                target.setParent(this);
                this.columns.set(i, (SQLName)expr);
                return true;
            }
        }
        return false;
    }
}
