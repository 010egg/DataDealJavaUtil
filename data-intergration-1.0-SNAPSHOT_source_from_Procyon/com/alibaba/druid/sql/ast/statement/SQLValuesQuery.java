// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class SQLValuesQuery extends SQLSelectQueryBase
{
    private List<SQLExpr> values;
    
    public SQLValuesQuery() {
        this.values = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getValues() {
        return this.values;
    }
    
    public void addValue(final SQLListExpr value) {
        value.setParent(this);
        this.values.add(value);
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.values);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLValuesQuery clone() {
        final SQLValuesQuery x = new SQLValuesQuery();
        x.parenthesized = this.parenthesized;
        for (int i = 0; i < this.values.size(); ++i) {
            final SQLExpr value = this.values.get(i).clone();
            value.setParent(x);
            x.values.add(value);
        }
        return x;
    }
}
