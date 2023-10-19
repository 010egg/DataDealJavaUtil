// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;

public class OracleWithSubqueryEntry extends SQLWithSubqueryClause.Entry implements OracleSQLObject
{
    private SearchClause searchClause;
    private CycleClause cycleClause;
    
    public CycleClause getCycleClause() {
        return this.cycleClause;
    }
    
    public void setCycleClause(final CycleClause cycleClause) {
        if (cycleClause != null) {
            cycleClause.setParent(this);
        }
        this.cycleClause = cycleClause;
    }
    
    public SearchClause getSearchClause() {
        return this.searchClause;
    }
    
    public void setSearchClause(final SearchClause searchClause) {
        if (searchClause != null) {
            searchClause.setParent(this);
        }
        this.searchClause = searchClause;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.subQuery);
            this.acceptChild(visitor, this.searchClause);
            this.acceptChild(visitor, this.cycleClause);
        }
        visitor.endVisit(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    public void cloneTo(final OracleWithSubqueryEntry x) {
        super.cloneTo(x);
        if (this.searchClause != null) {
            this.setSearchClause(this.searchClause.clone());
        }
        if (this.cycleClause != null) {
            this.setCycleClause(this.cycleClause.clone());
        }
    }
    
    @Override
    public OracleWithSubqueryEntry clone() {
        final OracleWithSubqueryEntry x = new OracleWithSubqueryEntry();
        this.cloneTo(x);
        return x;
    }
}
