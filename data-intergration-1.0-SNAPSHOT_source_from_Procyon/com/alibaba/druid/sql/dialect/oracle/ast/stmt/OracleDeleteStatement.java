// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;

public class OracleDeleteStatement extends SQLDeleteStatement
{
    private final List<SQLHint> hints;
    private OracleReturningClause returning;
    
    public OracleDeleteStatement() {
        super(DbType.oracle);
        this.hints = new ArrayList<SQLHint>();
        this.returning = null;
    }
    
    public OracleReturningClause getReturning() {
        return this.returning;
    }
    
    public void setReturning(final OracleReturningClause returning) {
        this.returning = returning;
    }
    
    public List<SQLHint> getHints() {
        return this.hints;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    protected void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.hints);
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.getWhere());
            this.acceptChild(visitor, this.returning);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleDeleteStatement clone() {
        final OracleDeleteStatement x = new OracleDeleteStatement();
        this.cloneTo(x);
        for (final SQLHint hint : this.hints) {
            final SQLHint hint2 = hint.clone();
            hint2.setParent(x);
            x.hints.add(hint2);
        }
        if (this.returning != null) {
            x.setReturning(this.returning.clone());
        }
        return x;
    }
}
