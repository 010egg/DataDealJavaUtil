// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;

public class OracleSelectSubqueryTableSource extends SQLSubqueryTableSource implements OracleSelectTableSource
{
    protected OracleSelectPivotBase pivot;
    
    public OracleSelectSubqueryTableSource() {
    }
    
    public OracleSelectSubqueryTableSource(final String alias) {
        super(alias);
    }
    
    public OracleSelectSubqueryTableSource(final SQLSelect select, final String alias) {
        super(select, alias);
    }
    
    public OracleSelectSubqueryTableSource(final SQLSelect select) {
        super(select);
    }
    
    @Override
    public OracleSelectPivotBase getPivot() {
        return this.pivot;
    }
    
    @Override
    public void setPivot(final OracleSelectPivotBase pivot) {
        this.pivot = pivot;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    protected void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getHints());
            this.acceptChild(visitor, this.select);
            this.acceptChild(visitor, this.pivot);
            this.acceptChild(visitor, this.flashback);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
    
    @Override
    public OracleSelectSubqueryTableSource clone() {
        final OracleSelectSubqueryTableSource x = new OracleSelectSubqueryTableSource();
        this.cloneTo(x);
        if (this.pivot != null) {
            this.setParent(this.pivot.clone());
        }
        return x;
    }
}
