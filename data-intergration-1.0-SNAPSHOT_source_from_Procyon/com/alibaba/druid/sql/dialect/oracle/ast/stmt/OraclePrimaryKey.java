// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLUnique;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKeyImpl;

public class OraclePrimaryKey extends SQLPrimaryKeyImpl implements OracleConstraint, SQLPrimaryKey, SQLTableElement, SQLTableConstraint
{
    private OracleUsingIndexClause using;
    private SQLName exceptionsInto;
    private Boolean enable;
    private Initially initially;
    private Boolean deferrable;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
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
    public OracleUsingIndexClause getUsing() {
        return this.using;
    }
    
    @Override
    public void setUsing(final OracleUsingIndexClause using) {
        this.using = using;
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
    public Boolean getEnable() {
        return this.enable;
    }
    
    @Override
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    @Override
    public Initially getInitially() {
        return this.initially;
    }
    
    @Override
    public void setInitially(final Initially initially) {
        this.initially = initially;
    }
    
    public void cloneTo(final OraclePrimaryKey x) {
        super.cloneTo(x);
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
        if (this.exceptionsInto != null) {
            x.setExceptionsInto(this.exceptionsInto.clone());
        }
        x.enable = this.enable;
        x.initially = this.initially;
        x.deferrable = this.deferrable;
    }
    
    @Override
    public OraclePrimaryKey clone() {
        final OraclePrimaryKey x = new OraclePrimaryKey();
        this.cloneTo(x);
        return x;
    }
}
