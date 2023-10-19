// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLCheck;

public class OracleCheck extends SQLCheck implements OracleConstraint, OracleSQLObject
{
    private OracleUsingIndexClause using;
    private SQLName exceptionsInto;
    private Initially initially;
    private Boolean deferrable;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
            return;
        }
        super.accept(visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getExpr());
            this.acceptChild(visitor, this.using);
            this.acceptChild(visitor, this.exceptionsInto);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public Boolean getDeferrable() {
        return this.deferrable;
    }
    
    @Override
    public void setDeferrable(final Boolean deferrable) {
        this.deferrable = deferrable;
    }
    
    @Override
    public Initially getInitially() {
        return this.initially;
    }
    
    @Override
    public void setInitially(final Initially initially) {
        this.initially = initially;
    }
    
    @Override
    public SQLName getExceptionsInto() {
        return this.exceptionsInto;
    }
    
    @Override
    public void setExceptionsInto(final SQLName exceptionsInto) {
        this.exceptionsInto = exceptionsInto;
    }
    
    @Override
    public OracleUsingIndexClause getUsing() {
        return this.using;
    }
    
    @Override
    public void setUsing(final OracleUsingIndexClause using) {
        if (using != null) {
            using.setParent(this);
        }
        this.using = using;
    }
    
    public void cloneTo(final OracleCheck x) {
        super.cloneTo(x);
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
        if (this.exceptionsInto != null) {
            x.setExceptionsInto(this.exceptionsInto.clone());
        }
        x.initially = this.initially;
        x.deferrable = this.deferrable;
    }
    
    @Override
    public OracleCheck clone() {
        final OracleCheck x = new OracleCheck();
        this.cloneTo(x);
        return x;
    }
}
