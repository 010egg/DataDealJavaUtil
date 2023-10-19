// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.ast.stmt;

import com.alibaba.druid.sql.dialect.db2.visitor.DB2OutputVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.db2.ast.DB2Object;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class DB2SelectQueryBlock extends SQLSelectQueryBlock implements DB2Object
{
    private Isolation isolation;
    private boolean forReadOnly;
    private SQLExpr optimizeFor;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof DB2ASTVisitor) {
            this.accept0((DB2ASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public void accept0(final DB2ASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.groupBy);
            this.acceptChild(visitor, this.getFirst());
        }
        visitor.endVisit(this);
    }
    
    public DB2SelectQueryBlock() {
        this.dbType = DbType.db2;
    }
    
    public Isolation getIsolation() {
        return this.isolation;
    }
    
    public void setIsolation(final Isolation isolation) {
        this.isolation = isolation;
    }
    
    public boolean isForReadOnly() {
        return this.forReadOnly;
    }
    
    public void setForReadOnly(final boolean forReadOnly) {
        this.forReadOnly = forReadOnly;
    }
    
    public SQLExpr getOptimizeFor() {
        return this.optimizeFor;
    }
    
    public void setOptimizeFor(final SQLExpr optimizeFor) {
        this.optimizeFor = optimizeFor;
    }
    
    @Override
    public void limit(final int rowCount, final int offset) {
        if (offset <= 0) {
            this.setFirst(new SQLIntegerExpr(rowCount));
            return;
        }
        throw new UnsupportedOperationException("not support offset");
    }
    
    @Override
    public void output(final Appendable buf) {
        this.accept(new DB2OutputVisitor(buf));
    }
    
    public enum Isolation
    {
        RR, 
        RS, 
        CS, 
        UR;
    }
}
