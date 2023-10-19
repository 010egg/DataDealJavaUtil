// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;

public class OracleInsertStatement extends SQLInsertStatement implements OracleStatement
{
    private OracleReturningClause returning;
    private SQLErrorLoggingClause errorLogging;
    private List<SQLHint> hints;
    
    public OracleInsertStatement() {
        this.hints = new ArrayList<SQLHint>();
        this.dbType = DbType.oracle;
    }
    
    public void cloneTo(final OracleInsertStatement x) {
        super.cloneTo(x);
        if (this.returning != null) {
            x.setReturning(this.returning.clone());
        }
        if (this.errorLogging != null) {
            x.setErrorLogging(this.errorLogging.clone());
        }
        for (final SQLHint hint : this.hints) {
            final SQLHint h2 = hint.clone();
            h2.setParent(x);
            x.hints.add(h2);
        }
    }
    
    public List<SQLHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLHint> hints) {
        this.hints = hints;
    }
    
    public OracleReturningClause getReturning() {
        return this.returning;
    }
    
    public void setReturning(final OracleReturningClause returning) {
        this.returning = returning;
    }
    
    public SQLErrorLoggingClause getErrorLogging() {
        return this.errorLogging;
    }
    
    public void setErrorLogging(final SQLErrorLoggingClause errorLogging) {
        this.errorLogging = errorLogging;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getTableSource());
            this.acceptChild(visitor, this.getColumns());
            this.acceptChild(visitor, this.getValues());
            this.acceptChild(visitor, this.getQuery());
            this.acceptChild(visitor, this.returning);
            this.acceptChild(visitor, this.errorLogging);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleInsertStatement clone() {
        final OracleInsertStatement x = new OracleInsertStatement();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public List<SQLCommentHint> getHeadHintsDirect() {
        return null;
    }
}
