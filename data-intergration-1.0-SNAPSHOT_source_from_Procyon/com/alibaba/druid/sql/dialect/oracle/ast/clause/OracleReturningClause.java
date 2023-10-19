// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleReturningClause extends OracleSQLObjectImpl implements SQLReplaceable
{
    private List<SQLExpr> items;
    private List<SQLExpr> values;
    
    public OracleReturningClause() {
        this.items = new ArrayList<SQLExpr>();
        this.values = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public List<SQLExpr> getValues() {
        return this.values;
    }
    
    public void addValue(final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.values.add(value);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.values);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleReturningClause clone() {
        final OracleReturningClause x = new OracleReturningClause();
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        for (final SQLExpr v : this.values) {
            final SQLExpr v2 = v.clone();
            v2.setParent(x);
            x.values.add(v2);
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = this.items.size() - 1; i >= 0; --i) {
            if (this.items.get(i) == expr) {
                target.setParent(this);
                this.items.set(i, target);
                return true;
            }
        }
        for (int i = this.values.size() - 1; i >= 0; --i) {
            if (this.values.get(i) == expr) {
                target.setParent(this);
                this.values.set(i, target);
                return true;
            }
        }
        return false;
    }
}
