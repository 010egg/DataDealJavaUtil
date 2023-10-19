// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;

public class OracleExplainStatement extends SQLExplainStatement implements OracleStatement
{
    private SQLExpr statementId;
    private SQLExpr into;
    
    public OracleExplainStatement() {
        super(DbType.oracle);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statementId);
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.statement);
        }
        visitor.endVisit(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
    
    public SQLExpr getStatementId() {
        return this.statementId;
    }
    
    public void setStatementId(final SQLExpr statementId) {
        this.statementId = statementId;
    }
    
    public SQLExpr getInto() {
        return this.into;
    }
    
    public void setInto(final SQLExpr into) {
        this.into = into;
    }
}
